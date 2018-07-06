package com.zufangbao.earth.yunxin.api.handler.impl;


import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.DateUtils;
import com.zufangbao.earth.yunxin.api.handler.PaymentOrderApiHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.repayment.QueryPaymentOrderRequestModel;
import com.zufangbao.sun.api.model.repayment.QueryPaymentOrderResults;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("paymentOrderApiHandler")
public class PaymentOrderApiHandlerImpl implements PaymentOrderApiHandler {

	@Autowired
    private PaymentOrderService paymentOrderService; 
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private DeductApplicationService deductApplicationService; 
    @Autowired
    private CashFlowService cashFlowService;
    @Autowired
    private DeductPlanService deductPlanService;

	@Value("#{config['yx.paymentOrder.batchQuery.size']}")
	@JSONField(serialize = false)
	private String batchQuerySize = "";
    
	@Override
	public List<Map<String, Object>> queryPaymentOrderResponeData(
			QueryPaymentOrderRequestModel commandModel) {
		
		FinancialContract  financialContract = financialContractService.getUniqueFinancialContractBy(commandModel.getFinancialContractNo());
	     if(financialContract == null){
	         throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
	     }
		
		//构建响应结果
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<PaymentOrder> paymentOrders = new ArrayList<PaymentOrder>();
		
	     List<String> paymentNoArrayList = commandModel.getPaymentNosList(batchQuerySize);
	     List<String> paymentUuidArrayList = commandModel.getPaymentUuidsList(batchQuerySize);
	     List<String> orderUniqueIdArrayList = commandModel.getOrderUniqueIdsList(batchQuerySize);
	     List<String> orderUuidArrayList = commandModel.getOrderUuidsList(batchQuerySize);
	     
		 if(CollectionUtils.isNotEmpty(paymentNoArrayList)){
			 paymentOrders =  paymentOrderService.getPaymentOrderListByPaymentNos(paymentNoArrayList);
		 }
		 if(CollectionUtils.isEmpty(paymentOrders) && CollectionUtils.isNotEmpty(paymentUuidArrayList)){
			 paymentOrders =  paymentOrderService.getPaymentOrderListByPaymentUuids(paymentUuidArrayList);
		 }
		 if(CollectionUtils.isEmpty(paymentOrders) && CollectionUtils.isNotEmpty(orderUniqueIdArrayList)){
			 paymentOrders =  paymentOrderService.getPaymentOrderListByOrderUniqueId(orderUniqueIdArrayList, financialContract.getFinancialContractUuid());
		 }
		 if(CollectionUtils.isEmpty(paymentOrders) && CollectionUtils.isNotEmpty(orderUuidArrayList)){
			 paymentOrders =  paymentOrderService.getPaymentOrdersByorderUuids(orderUuidArrayList);
		 }
		 
		 
		 //校验 信托产品代码是否一致
		 checkFinancialContractCode(paymentOrders, commandModel.getFinancialContractNo());
		 
		 Map<String, List<PaymentOrder>> map = new HashMap<String, List<PaymentOrder>>();
		 for (PaymentOrder paymentOrder : paymentOrders) {
			
			 List<PaymentOrder> paymentOrderList = map.get(paymentOrder.getOrderUniqueId());
			 if(paymentOrderList==null){
				 paymentOrderList = new ArrayList<PaymentOrder>();
			 }
			 paymentOrderList.add(paymentOrder);
			 map.put(paymentOrder.getOrderUniqueId(), paymentOrderList);
		}
		 
		 for (String key : map.keySet()) {
			 
			 List<QueryPaymentOrderResults> resultList = new ArrayList<QueryPaymentOrderResults>();
			 Map<String, Object> resultMap = new HashMap<String, Object>();
			 
			 List<PaymentOrder> paymentOrderList = map.get(key);
			 for (PaymentOrder paymentOrder : paymentOrderList) {
				 String completedTime = getCompletedTime(paymentOrder);
    			 String accountedTime = getAccountedTime(paymentOrder);
				 QueryPaymentOrderResults result = new QueryPaymentOrderResults(paymentOrder, completedTime, accountedTime);
				 resultList.add(result);
			}
			 
			resultMap.put("orderUniqueId", key);
    		resultMap.put("queryPaymentOrderResults", resultList);
    		 
    		list.add(resultMap);
		 }
		 
//		 List<QueryPaymentOrderReturnModel> returnModel = paymentOrderService.getQueryPaymentOrderReturnModel(paymentOrderUuids);
		 
	     return list;
	}
	
	private void checkFinancialContractCode(List<PaymentOrder> paymentOrderList ,String financialContractCode){
			
			for (PaymentOrder paymentOrder : paymentOrderList) {
				
				if(!paymentOrder.getFinancialContractNo().equals(financialContractCode)){
					
					throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EQUAL_FOR_REQUESTMODE_EXIST);
				}
				
			}
	}
	
	//资金入账时间
	private String getAccountedTime(PaymentOrder paymentOrder){
		
		String accountedTime = "";
		if(paymentOrder.getPayWay() == PayWay.OFFLINE_TRANSFER && paymentOrder.getCashFlowTime()!=null){
			
			//线下转账  关联的流水时间
			accountedTime = DateUtils.format(paymentOrder.getCashFlowTime(), com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT);
			
		}else if(paymentOrder.getPayWay() == PayWay.MERCHANT_DEDUCT_EASY_PAY || paymentOrder.getPayWay() == PayWay.MERCHANT_DEDUCT){
			//快捷支付  线上代扣    第三方清算流水的时间（或者为空）
//			DeductPlan deductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid(), DeductApplicationExecutionStatus.SUCCESS);
//			if(deductPlan != null){
//				accountedTime = DateUtils.format(deductPlan.getClearingTime(), com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT);
//			}
		}
		
		return accountedTime;
		
	}
	
	//交易完成时间
	private String getCompletedTime(PaymentOrder paymentOrder){
		
		String completedTime = "";
		if(paymentOrder.getPayWay() == PayWay.OFFLINE_TRANSFER && paymentOrder.getPayStatus() == PayStatus.PAY_SUCCESS && paymentOrder.getLastModifiedTime() != null){
			//线下转账    支付请求处理完成时间
			completedTime = DateUtils.format(paymentOrder.getLastModifiedTime(), com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT);
		}else if(paymentOrder.getPayWay() == PayWay.MERCHANT_DEDUCT_EASY_PAY || paymentOrder.getPayWay() == PayWay.MERCHANT_DEDUCT){
			//快捷支付 线上代扣  （扣款完成时间）
			if(paymentOrder.getTransactionTime() != null){
				completedTime = DateUtils.format(paymentOrder.getTransactionTime(), com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT);
			}
		}
		
		return completedTime;
		
	}
	
}
