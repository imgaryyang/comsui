package com.suidifu.citigroup.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.citigroup.handler.DeductBusinessHandler;
import com.suidifu.citigroup.servers.FileJobServer;
import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_standard_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.wellsfargo.deduct.DeductDataContext;
import com.zufangbao.wellsfargo.deduct.handler.DeductPlanAndScheduleHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wukai
 *
 */
@Component("deductBusinessHandler")
public class DeductBusinessHandlerImpl implements DeductBusinessHandler {

	@Value("${notifyserver.groupCacheJobQueueMap_callbackDeduct}")
	private String groupNameForDeductBusiness;

	@Autowired
	private NotifyJobServer fileJobServer;

	@Autowired
	private PrePositionHandler prePositionHandler;
	
	@Autowired
	private DeductPlanAndScheduleHandler deductPlanAndScheduleHandler;

	@Autowired
	private ContractAccountService contractAccountService;

	@Autowired
	private ContractService contractService;

	private static Log LOG = LogFactory.getLog(DeductBusinessHandlerImpl.class);

	
	public void handleDeductBusiness(String preProcessUrl,
			DeductRequestModel deductRequestModel) {
		
		//LOG.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECE_DEDUCT_INFO_FROM_BRIDGEWATER_DEDUCT_SYSTEM+"begin with preProcessUrl["+preProcessUrl+"],deductRequestModel["+JsonUtils.toJsonString(deductRequestModel)+"]");
		
		DeductDataContext deductDataContext = new DeductDataContext(deductRequestModel);

		String failMsg = "";
		
		int deductPlanModelListSize = 0;
		
		boolean isSuccess = true;

		try {
			
			HashMap<String,String> preRequestParams = preparePreRequestParams(deductRequestModel);
			
			HashMap<String, String> DelayPostRequestParams = new HashMap<>();
			
			// 业务校验
			int errorCode = prePositionHandler.prePositionDefaultTaskHandler(
					preProcessUrl, preRequestParams, DelayPostRequestParams, LOG);

			if (errorCode != ApiResponseCode.SUCCESS) {

				failMsg  = DelayPostRequestParams.get("errorMsg");
				
				deductPlanModelListSize=1;
				
				isSuccess=false;
				
				LOG.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECE_DEDUCT_INFO_FROM_BRIDGEWATER_DEDUCT_SYSTEM+"check business with errorCode["+errorCode+"] ");

				return;
			}

			// 初始化扣款上下文
			initializeDeductDataContext(deductDataContext,deductRequestModel,DelayPostRequestParams);
			
			deductPlanAndScheduleHandler.prepareDeductPlanModelAndTradeSchedule(deductDataContext);

			deductPlanModelListSize=deductDataContext.getDeductPlanModels().size();

		} catch (Exception e) {
			
			e.printStackTrace();
			
			deductPlanModelListSize=1;

			failMsg="系统异常!";

			LOG.error("#handleDeductBusiness# occur exception with deductRequestModel["
					+ JsonUtils.toJsonString(deductRequestModel)
					+ "],stack full exception["
					+ ExceptionUtils.getFullStackTrace(e) + "]");
			
			isSuccess=false;
			

		} finally {
			
			NotifyApplication notifyApplication = deductPlanAndScheduleHandler.sendDeductPlanModelAndTradeScheduleToDeduct(fileJobServer, deductDataContext, failMsg, deductPlanModelListSize, isSuccess, groupNameForDeductBusiness);
			
			LOG.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECE_DEDUCT_INFO_FROM_BRIDGEWATER_DEDUCT_SYSTEM+"end with notifyApplication["+JsonUtils.toJsonString(notifyApplication)+"],isSuccess["+isSuccess+"]"
					+ "deductPlanModelSize["+deductDataContext.getDeductPlanModels().size()+"],tradeSchedule Size["+deductDataContext.getTradeSchedules().size()+"]"
					+ ",result will callback to deduct system");
		}

	}



	private HashMap<String, String> preparePreRequestParams(
			DeductRequestModel deductRequestModel) {
		
		HashMap<String,String> parameters = new HashMap<String,String>();
		
		parameters.put("financialProductCode", deductRequestModel.getFinancialProductCode());
		parameters.put("accountName", deductRequestModel.getAccountHolderName());
		parameters.put("accountNo", deductRequestModel.getDeductAccountNo());
		parameters.put("standardBankCode", deductRequestModel.getStandardBankCode());
		parameters.put("contractNo", deductRequestModel.getContractNo());
		parameters.put("uniqueId", deductRequestModel.getUniqueId());
		parameters.put("repaymentDetails", deductRequestModel.getRepayDetailInfo());
		parameters.put("requestNo",  deductRequestModel.getRequestNo());
		parameters.put("bankCode", deductRequestModel.getStandardBankCode());
		parameters.put("provinceCode", deductRequestModel.getCpBankProvince());
		parameters.put("cityCode", deductRequestModel.getCpBankCity());
		parameters.put("uniqueId", deductRequestModel.getUniqueId());
		parameters.put("repaymentType", Integer.valueOf(deductRequestModel.getRepaymentType()).toString());
		parameters.put("apiCalledTime", deductRequestModel.getApiCalledTime());
		parameters.put("amount", deductRequestModel.getDeductAmount());
		parameters.put("gateway", deductRequestModel.getGateway());
		parameters.put("deductApplicationUuid", deductRequestModel.getDeductApplicationUuid());
		return parameters;
	}



	private void initializeDeductDataContext(DeductDataContext deductDataContext,DeductRequestModel deductRequestModel,Map<String,String> postRequestParams) {

		String financialContractUuid = postRequestParams.get("financialContractUuid");
		String paymentChannelAndSignUpInfoString = postRequestParams.get("paymentChannelAndSignUpInfoString");
		String repaymentDetails = postRequestParams.get("repaymentDetails");
		String deductMode = postRequestParams.get("deductMode");
		if (StringUtils.isNotEmpty(deductMode)){
			deductDataContext.setDeductMode(DeductDataContext.DeductMode.valueOf(deductMode));
		}
		List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfo = JsonUtils.parseArray(paymentChannelAndSignUpInfoString, PaymentChannelAndSignUpInfo.class);
		
		if(CollectionUtils.isEmpty(paymentChannelAndSignUpInfo)){
			throw new ApiException(ApiResponseCode.PAYMENT_CHANNEL_INFO_IS_EMPTY);
		}
		
		deductRequestModel.setFinancialContractUuid(financialContractUuid);

		deductDataContext
		.setPaymentChannelAndSignUpInfos(paymentChannelAndSignUpInfo);
		deductDataContext.setFinancialContractUuid(financialContractUuid);
		extractPaymentAccountInfo(deductRequestModel, postRequestParams);
		deductRequestModel.setRepayDetailInfo(repaymentDetails);

		deductDataContext.setDeductRequestModel(deductRequestModel);

	}

	private void extractPaymentAccountInfo(DeductRequestModel deductRequestModel,Map<String,String> postRequestParams) {

		Contract contract = contractService.getContractByUnigueIdOrContractNo(deductRequestModel.getUniqueId(), deductRequestModel.getContractNo());
		ContractAccount bindContractAccount = contractAccountService.getTheEfficientContractAccountBy(contract );

		if(StringUtils.isBlank(deductRequestModel.getAccountHolderName())){
			deductRequestModel.setAccountHolderName(bindContractAccount.getPayerName());
		}
		if(StringUtils.isBlank(deductRequestModel.getDeductAccountNo())){
			deductRequestModel.setDeductAccountNo(bindContractAccount.getPayAcNo());
		}
		if (StringUtils.isEmpty(deductRequestModel.getContractNo())){
			deductRequestModel.setContractNo(contract.getContractNo());
		}
		if (StringUtils.isEmpty(deductRequestModel.getUniqueId())){
			deductRequestModel.setUniqueId(contract.getUniqueId());
		}

		if (StringUtils.isEmpty(deductRequestModel.getIdCardNo())){
			deductRequestModel.setIdCardNo(bindContractAccount.getIdCardNum());
		}
		deductRequestModel.setCustomerName(contract.getCustomer().getName());
	}

}
