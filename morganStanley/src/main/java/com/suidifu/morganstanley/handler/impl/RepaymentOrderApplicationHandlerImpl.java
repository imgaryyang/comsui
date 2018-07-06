package com.suidifu.morganstanley.handler.impl;

import com.suidifu.morganstanley.handler.RepaymentOrderApplicationHandler;
import com.suidifu.morganstanley.utils.DateUtils;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.repayment.OrderRequestModel;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.deduct.DeductGatewayMapSpec;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luffych on 2017/5/21.
 */
@Component("repaymentOrderApplicationHandler")
public class RepaymentOrderApplicationHandlerImpl implements RepaymentOrderApplicationHandler {

    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private RepaymentOrderService repaymentOrderService;
    @Autowired
    private RepaymentOrderHandler repaymentOrderHandler;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private PaymentChannelInformationHandler paymentChannelInformationHandler;

    @Value("${yx.api.order_detail_path}")
    private String YX_API_ORDER_DETAIL_PATH = "";

    private static final Log logger = LogFactory.getLog(RepaymentOrderApplicationHandlerImpl.class);

    @Override
    public void cancelRepaymentOrderInfoCheck(OrderRequestModel orderRequestModel, String merId) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(orderRequestModel.getFinancialContractNo());
        if (financialContract == null) {
            throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
        }
        RepaymentOrder repaymentOrderByUniqueId = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestModel.getOrderUniqueId(), orderRequestModel
                .getFinancialContractNo());
        if (repaymentOrderByUniqueId == null) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_IS_NOT_EXIST);
        }
        if (false == StringUtils.equalsIngoreNull(repaymentOrderByUniqueId.getFinancialContractUuid(), financialContract.getFinancialContractUuid())) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_FINANCIAL_CONTRACT_ERROR);
        }
        if (repaymentOrderByUniqueId.isLock()) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OPT_ERROR);
        }
        if (false == repaymentOrderByUniqueId.isCanBeCanceled()) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OPT_ERROR);

        }

    }

    @Override
    public RepaymentOrder cancelRepaymentOrder(OrderRequestModel orderRequestModel, String ip, String merId) {
        if (orderRequestModel == null) {
            return null;
        }
        RepaymentOrder repaymentOrder = null;

        try {
            repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(orderRequestModel.getOrderUniqueId(), orderRequestModel.getFinancialContractNo());
            if (repaymentOrder == null || false == repaymentOrder.isCanBeCanceled()) {
                return null;
            }

            //单合同类型  快捷支付
            if (repaymentOrder.getFirstRepaymentWayGroup().singleContractForRepaymentWayGroupType()) {

                repaymentOrderService.update_repayment_order_lock(repaymentOrder.getOrderUuid(), RepaymentOrder.LAPSE, RepaymentOrder.EMPTY);

            } else {
                //多合同类型
                boolean ifRegisterNewJob = repaymentOrderHandler.register_repayment_order_cancel_job(repaymentOrder.getOrderUuid(), repaymentOrder.getOrderUniqueId
                        (), orderRequestModel.getOrderRequestNo());
                if (ifRegisterNewJob == false) {
                    return null;
                }

                repaymentOrderService.update_repayment_order_lock(repaymentOrder.getOrderUuid(), RepaymentOrder.LAPSE, RepaymentOrder.EMPTY);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("cancelRepaymentOrder occur error,orderUuid[" + repaymentOrder.getOrderUuid() + "],msg :" + ExceptionUtils.getFullStackTrace(e));
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OPT_ERROR);
        }
        return repaymentOrder;
    }

    @Override
	public RepaymentOrder paymentOrderInfoCheck(PaymentOrderRequestModel paymentOrderRequestModel, String merId) {
		
		//校验请求编号唯一
        Integer sizeByRequestNo = paymentOrderService.getPaymentOrderSizeByRequestNo(paymentOrderRequestModel.getRequestNo());
        if(sizeByRequestNo != null && sizeByRequestNo > 0){
            throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
        }
        
        //商户订单号 与 五维订单号二选一
        RepaymentOrder repaymentOrder = null;
        if (!StringUtils.isEmpty(paymentOrderRequestModel.getOrderUniqueId())) {
        	repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueId(paymentOrderRequestModel.getOrderUniqueId(), paymentOrderRequestModel.getFinancialContractNo());
        	if(repaymentOrder == null){
                throw new ApiException(ApiResponseCode.REPAYMENTORDER_IS_NOT_EXISTED);
            }
		} else {
			 repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(paymentOrderRequestModel.getOrderUuid());
			if(repaymentOrder == null){
				throw new ApiException(ApiResponseCode.REPAYMENTORDER_IS_NOT_EXISTED);
			}
		}
        
        //校验信托合同
        FinancialContract  financialContract = financialContractService.getUniqueFinancialContractBy(paymentOrderRequestModel.getFinancialContractNo());
        if(financialContract == null){
            throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
        }
        
        Account account = financialContract.getCapitalAccount();
		if (account == null){
			throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_FOR_ACCOUNT_NOT_EXIST);
		}
        
        //支持线上代扣 和 线下转账  和商户代扣
        if(paymentOrderRequestModel.getFormatPayWay() == null || (paymentOrderRequestModel.getFormatPayWay() != PayWay.MERCHANT_DEDUCT 
        		&& paymentOrderRequestModel.getFormatPayWay() != PayWay.OFFLINE_TRANSFER && paymentOrderRequestModel.getFormatPayWay() != PayWay.BUSINESS_DEDUCT)){
        	throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_ERROR);
        }
        
		// 订单支付  校验交易时间格式
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getTransactionTime())){
			if(DateUtils.parseDate(paymentOrderRequestModel.getTransactionTime(),com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT) == null ){
				throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_DATE_NOT_CORRECT);
			}
		}
		
		//校验 银行code
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentBankCode())){
			Bank bank =  bankService.getCachedBanks().get(paymentOrderRequestModel.getPaymentBankCode());
			if(bank == null){
			    throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
			 }
		}
		
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getReceivableBankCode())){
			Bank bank =  bankService.getCachedBanks().get(paymentOrderRequestModel.getReceivableBankCode());
			if(bank == null){
			    throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
			 }
		}
	
		//校验省市 code
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentProvinceCode())){
			Province province = provinceService.getProvinceByCode(paymentOrderRequestModel.getPaymentProvinceCode());
			if(province == null){
			    throw new ApiException(ApiResponseCode.NO_MATCH_PROVINCE);
			 }
		}
		
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentCityCode())){
			City city = cityService.getCityByCityCode(paymentOrderRequestModel.getPaymentCityCode());
			if(city == null){
			    throw new ApiException(ApiResponseCode.NO_MATCH_CITY);
			 }
		}
		
		//商户支付编号  唯一性 
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentNo())){
			Integer sizeByPaymentNo = paymentOrderService.getPaymentOrderByPaymentNo(paymentOrderRequestModel.getPaymentNo());
	    	 if(sizeByPaymentNo != null && sizeByPaymentNo > 0){
	             throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_PAYMENTNO_REPEAD);
	         }
		}
    	
		 //线上代扣
        if(paymentOrderRequestModel.getFormatPayWay() == PayWay.MERCHANT_DEDUCT){
        	
        	checkPaymentOrderMerchantDeduct(paymentOrderRequestModel, repaymentOrder);
        	
        }else if(paymentOrderRequestModel.getFormatPayWay() == PayWay.OFFLINE_TRANSFER){
        	//线下转账
        	checkPaymentOrderOnline(paymentOrderRequestModel, repaymentOrder);
        }else {
        	//商户代扣
        	checkPaymentOrderBusinessDeduct(paymentOrderRequestModel, repaymentOrder);
        }
		
		return repaymentOrder;
	}
	
	private void checkPaymentOrderBusinessDeduct(PaymentOrderRequestModel paymentOrderRequestModel,RepaymentOrder repaymentOrder){
    	
    	//校验通道交易号   支付失败的可以重传
    	Integer sizeByTransaction = paymentOrderService.getPaymentOrderByTransactionNoAndPayStatus(paymentOrderRequestModel.getTradeUuid());
    	 if(sizeByTransaction != null && sizeByTransaction > 0){
             throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OFFLINE_TRANSACTION_REPEAD);
         }
    	 
    	 //支付方式与还款订单还款方式匹配
         if(!checkOrderPayWay(paymentOrderRequestModel,repaymentOrder)){
         	throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_AND_REPAYMENTORDER_NOT_EQUAL_ERROR);
         }
    	
    	 //校验成功 或 支付异常
         if(!checkOrderCheckAndPayStatus(repaymentOrder)){
         	throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_STATUS_NOT_CORRECT);
         }
         
       //交易金额=还款订单金额
 		if(new BigDecimal(paymentOrderRequestModel.getAmount()).compareTo(repaymentOrder.getOrderAmount()) != 0){
 			throw new ApiException(ApiResponseCode.TRADE_AMOUNT_AND_REPAYMENTORDER_AMOUNT_NOT_EQUAL_ERROR);
 		}
         
       //支付方式与支付通道之间存在映射关系  商户代扣 只能是  0:广银联  1:宝付   2:民生代扣  3:新浪支付  4:中金支付  5:先锋支付 6:易宝支付 7：易极付
 		PaymentInstitutionName paymentGateWay =  null;
 		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentGateWay())){
 			paymentGateWay = DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP_FOR_BUSINESS_DEDUCT.get(paymentOrderRequestModel.getPaymentGateWay());
 			if(paymentGateWay == null){
 				throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_PAYMENTGATEWAY_NOT_CORRECT);
 			}
 		}
 		
 		//校验系统有没有配置接口传的支付通道
 		if(!isExistPaymentGateBySystem(repaymentOrder.getFinancialContractUuid(), paymentOrderRequestModel.getPaymentGateWay(),DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP_FOR_BUSINESS_DEDUCT)){
 			throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_PAYMENTGATE_NOT_EXIST_BY_SYSTEM);
 		}
	}
	
	private void checkPaymentOrderMerchantDeduct(PaymentOrderRequestModel paymentOrderRequestModel,RepaymentOrder repaymentOrder){
    	
		//校验成功 未支付   或   校验成功 支付异常
        if(!checkOrderCheckAndPayStatus(repaymentOrder)){
        	throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_STATUS_NOT_CORRECT);
        }
		
        //支付方式与还款订单还款方式匹配
        if(!checkOrderPayWay(paymentOrderRequestModel,repaymentOrder)){
        	throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_AND_REPAYMENTORDER_NOT_EQUAL_ERROR);
        }
		
		//线上代扣 ：交易金额=还款订单金额
		if(new BigDecimal(paymentOrderRequestModel.getAmount()).compareTo(repaymentOrder.getOrderAmount()) != 0){
			throw new ApiException(ApiResponseCode.TRADE_AMOUNT_AND_REPAYMENTORDER_AMOUNT_NOT_EQUAL_ERROR);
		}
		
		//支付方式与支付通道之间存在映射关系  线上代扣 只能是  0 广银联 1 宝付 2 中金支付
		PaymentInstitutionName paymentGateWay =  null;
		if(StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentGateWay())){
			paymentGateWay = DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP.get(paymentOrderRequestModel.getPaymentGateWay());
			if(paymentGateWay == null){
				throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_PAYMENTGATEWAY_NOT_CORRECT);
			}
		}
		
		//校验系统有没有配置接口传的支付通道
		if(!isExistPaymentGateBySystem(repaymentOrder.getFinancialContractUuid(), paymentOrderRequestModel.getPaymentGateWay(),DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP)){
			throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_PAYMENTGATE_NOT_EXIST_BY_SYSTEM);
		}
		
	}
	
	private boolean isExistPaymentGateBySystem(String financialContractUuid,String gateway,Map<String,PaymentInstitutionName> paymentGateWayMap){
		
    	List<PaymentChannelSummaryInfo> paymentChannelInformations = paymentChannelInformationHandler.getPaymentChannelServiceUuidsBy(financialContractUuid, BusinessType.SELF, AccountSide.DEBIT, null, BigDecimal.ZERO);
    	
    	if(CollectionUtils.isEmpty(paymentChannelInformations)){
    		return false;
    	}
    	
    	PaymentInstitutionName paymentGateWay =  null;
    	
    	if(StringUtils.isNotEmpty(gateway)){
    		
    		paymentGateWay = paymentGateWayMap.get(gateway);
    		
    		
    		List<PaymentChannelSummaryInfo> resultPaymentChannelSummaryInfo = new ArrayList<>();
    		
    		if(paymentGateWay != null){
        		
        		for (PaymentChannelSummaryInfo paymentChannelSummaryInfo : paymentChannelInformations) {
        			
        			if (paymentChannelSummaryInfo.getPaymentGateway() == paymentGateWay.ordinal()) {
    					
        				resultPaymentChannelSummaryInfo.add(paymentChannelSummaryInfo);
    				}
    			}
        	}
        	if(CollectionUtils.isEmpty(resultPaymentChannelSummaryInfo)){
        		return false;
        	}
    	}
    	
    	return true;
	}
    	
	
	private void checkPaymentOrderOnline(PaymentOrderRequestModel paymentOrderRequestModel,RepaymentOrder repaymentOrder){
		
		//线下转账
    	
    	//校验通道交易号 唯一性 （付款流水号）
    	Integer sizeByTransaction = paymentOrderService.getPaymentOrderByTransactionNoAndPayStatus(paymentOrderRequestModel.getTradeUuid());
    	 if(sizeByTransaction != null && sizeByTransaction > 0){
             throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OFFLINE_TRANSACTION_REPEAD);
         }
    	
    	//校验成功  支付异常  支付中
    	if(!checkOrderCheckAndPayStatusOffline(repaymentOrder)){
         	throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_STATUS_NOT_CORRECT);
        }
    	
    	//支付方式与还款订单还款方式匹配
    	if(!checkOrderPayWayOnline(paymentOrderRequestModel,repaymentOrder)){
    		throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_AND_REPAYMENTORDER_NOT_EQUAL_ERROR);
    	}
    	
		//线下转账 ：交易金额<=还款订单剩余未付金额
		BigDecimal successPayingAmount = paymentOrderService.getPaySuccessAndPayingPaymentOrdersAmount(repaymentOrder.getOrderUuid());
		BigDecimal unPaidAmount = repaymentOrder.getOrderAmount().subtract(successPayingAmount);
		
		if(new BigDecimal(paymentOrderRequestModel.getAmount()).compareTo(unPaidAmount) > 0){
			throw new ApiException(ApiResponseCode.TRADE_AMOUNT_AND_REPAYMENTORDER_AMOUNT_NOT_CONTENTMENT);
		}
		
	}
    private boolean checkOrderPayWay(PaymentOrderRequestModel paymentOrderRequestModel, RepaymentOrder order) {

        return repaymentOrderHandler.repayementOrderGroupWayToPayWayMappingTable(order.getFirstRepaymentWayGroup(), paymentOrderRequestModel.getFormatPayWay());

    }

    private boolean checkOrderPayWayOnline(PaymentOrderRequestModel paymentOrderRequestModel, RepaymentOrder order) {

        return repaymentOrderHandler.repayementOrderGroupWayToPayWayMappingTable(order.getFirstRepaymentWayGroup(), paymentOrderRequestModel.getFormatPayWay());

    }

    private boolean checkOrderCheckAndPayStatusOffline(RepaymentOrder repaymentOrder) {

        return (repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.VERIFICATION_SUCCESS ||
                repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.PAYMENT_ABNORMAL) ||
                (repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.PAYMENT_IN_PROCESS);

    }


    private boolean checkOrderCheckAndPayStatus(RepaymentOrder repaymentOrder) {

        //校验成功 未支付   或   校验成功 支付异常
        return (repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.VERIFICATION_SUCCESS ||
                repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.PAYMENT_ABNORMAL);

    }

}
