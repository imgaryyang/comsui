 package com.suidifu.pricewaterhouse.yunxin.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.hathaway.job.Priority;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.OrderPayStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderPayResult;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;

/**
 * 支付单系统自动重试匹配流水 Task
 */
@Component("paymentOrderMatchCashFlowTask")
public class PaymentOrderMatchCashFlowTask {
	
	@Autowired
	private PaymentOrderHandler paymentOrderHandler;
	
	@Autowired
	private RepaymentOrderService repaymentOrderService;

	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private RepaymentOrderHandler repaymentOrderHandler;
	
	@Value("${payment.order.retry.times}")
    private Integer retryTimes;

    @Value("${payment.order.retry.interval}")
    private String retryInterval;
	
	private static Log logger = LogFactory.getLog(PaymentOrderMatchCashFlowTask.class);
	
	private static Map<Integer, Long> retryIntervalMap = new HashMap<>();
	
	/**
	 * 支付单系统自动重试匹配流水 
	 */
	public void retryMatchCashFlow(){
	
		logger.info("##retryMatchCashFlow## start!");
		long start = System.currentTimeMillis();
		String currentTime = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
		try {
			//获取次数小于6次 ,支付中的，未关联流水的支付单
			List<PaymentOrder> needRetryPaymentOrders = paymentOrderService.getNeedRetryPaymenrOrdersBy(); 
			logger.info(currentTime+"#可能需要自动重试匹配流水的支付单总计（"+needRetryPaymentOrders.size()+"）条");
			matchCashFlowByPaymentOrders(needRetryPaymentOrders);
			
		} catch (Exception e) {
			logger.error("occur error retryMatchCashFlow. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		logger.info("##retryMatchCashFlow## end! Used ["+(end-start)+"]ms");
	}
	
	
	private void matchCashFlowByPaymentOrders(List<PaymentOrder> needRetryPaymentOrders){
		
		if(CollectionUtils.isEmpty(needRetryPaymentOrders)){
			return;
		}
		
		for (PaymentOrder paymentOrder : needRetryPaymentOrders) {
			
			logger.info("##matchCashFlowByPaymentOrders## paymentOrderUuid"+paymentOrder.getUuid());
			repaymentOrderHandler.retryMatchCashFlow(paymentOrder);
		}
	}
	
	/**
	 * 重试拉交易记录
	 */
	public void retryPullTransactionRecord(){
		
		logger.info("##retryPullTransactionRecord## start!");
		try {
			
			//获取paymentOrderList
			List<PaymentOrder> paymentOrders = paymentOrderService.getNeedRetryPaymentOrdersByBusinessDeduct(retryTimes);
			logger.info("#可能需要拉交易流水的支付单总计（"+paymentOrders.size()+"）条");
			if(CollectionUtils.isEmpty(paymentOrders)){
				return;
			}
			
			handlerRetryPaymentOrderList(paymentOrders);
			
		} catch (Exception e) {
			logger.error("occur error retryPullTransactionRecord. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("##retryPullTransactionRecord## end! ");
	}
	
	private void handlerRetryPaymentOrderList(List<PaymentOrder> paymentOrders){
		
		if(CollectionUtils.isEmpty(paymentOrders)){
			return;
		}
		
		for (PaymentOrder paymentOrder : paymentOrders) {
			
			try {
				
				RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
				
				//达到重试次数 
				if(paymentOrder.getRetriedTransactionRecordNums() >= retryTimes){ 
					logger.info("paymentOrder businessDeduct retry transactionRecord Nums is 6,update paymentOrder status fail, paymentOrderUuid:"+paymentOrder.getUuid());
					//支付单支付失败 ,订单支付异常
					paymentOrderService.updatePaymentOrderAndRepaymentStatus(paymentOrder, order, RepaymentOrderPayResult.PAY_FAIL, PayStatus.PAY_FAIL, OrderPayStatus.PAY_ABNORMAL);
					return;
				}
				
				//配置对应某一次的时间间隔
				Long retryInterval = getRetryInterval(paymentOrder.getRetriedTransactionRecordNums());
				
	            Long expectIntervalTime =  paymentOrder.getCreateTime().getTime()+retryInterval;
	            //当前时间大，尝试去拉交易记录
	            if (expectIntervalTime <= System.currentTimeMillis()) {
	            	
	            	paymentOrderHandler.rtFlowQueryByNotifyServer(paymentOrder);
	            	
	            	//已拉交易记录次数  加一 sql
	        		paymentOrderService.updateRetriedTransactionRecordNumsByPaymentOrderUuid(paymentOrder.getUuid(), paymentOrder.getRetriedTransactionRecordNums()+1);
	        		logger.info("##handlerRetryPaymentOrderList## TransactionRecordNums add 1, retryNums["+paymentOrder.getRetriedTransactionRecordNums()+"]." +"paymentOrderUuid :"+paymentOrder.getUuid());
	        		
	            }
				
			} catch (Exception e) {
				logger.error("occur error handlerRetryPaymentOrderList . FullStackTrace:"+ ExceptionUtils.getFullStackTrace(e) );
			}
		}
	}
	
    private Long getRetryInterval(int retryTime) {
        retryIntervalMap = new HashMap<>(retryTimes);
        String[] intervalArr = retryInterval.split(",");
        for (int i = 1; i < intervalArr.length + 1; i++) {
            retryIntervalMap.put(i, Long.parseLong(intervalArr[i - 1]));
        }
        Long interval = retryIntervalMap.get(retryTime+1);
        if (null != interval) {
            return interval;
        }
        return retryIntervalMap.get(retryIntervalMap.size());
    }
	
}
