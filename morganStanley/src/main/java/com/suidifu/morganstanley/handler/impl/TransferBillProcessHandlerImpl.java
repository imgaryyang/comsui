package com.suidifu.morganstanley.handler.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.configuration.bean.transfer.TransferProperties;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.handler.TransferBillProcessHandler;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.PostEntityType;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.CommandOpsFunctionCodes;
import com.zufangbao.sun.api.model.transfer.TransferApplicationReqModel;
import com.zufangbao.sun.api.model.transfer.TransferBillQueryModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBill;
import com.zufangbao.sun.yunxin.entity.transfer.TransferTransactionType;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TransferBillService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.yunxin.handler.TransferBillHandler;

import net.sf.json.JSONArray;

@Component("transferBillProcessHandler")
public class TransferBillProcessHandlerImpl implements TransferBillProcessHandler {
	
	private static final Log logger = LogFactory.getLog(TransferBillProcessHandlerImpl.class);
	
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private TransferBillService transferBillService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private TransferBillHandler transferBillHandler;
	@Resource
	private MorganStanleyNotifyServer morganStanleyNotifyServer;
	
	@Resource
	private YntrustFileTask yntrustFileTask;
	
	@Resource
	private TransferProperties transferProperties;

	@Resource
	private MorganStanleyNotifyConfig morganStanleyNotifyConfig;
	

	@Override
	public String createTransferBill(TransferApplicationReqModel reqModel) {
		FinancialContract  financialContract = financialContractService.getUniqueFinancialContractBy(reqModel.getProductCode());
		if(financialContract == null){
			throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}
		TransferBill transferBill = new TransferBill(reqModel, financialContract);
		if(StringUtils.isEmpty(transferBill.getNotifyUrl())){
			transferBill.setNotifyUrl(transferProperties.getNotifyUrl());
		}
		transferBill.setPlanNotifyNumber(Integer.parseInt(transferProperties.getNotifyNumber()));
		transferBillService.save(transferBill);
		return transferBill.getOrderUuid();
	}

	@Override
	public String processTransferInfo(TransferApplicationReqModel reqModel) {
		
		String orderUuid = StringUtils.EMPTY;
		if(TransferTransactionType.TRANSFER==reqModel.getTransactionTypeEnum()){
			//生成转账单
			orderUuid = createTransferBill(reqModel);
			String result = transferRequestApi(orderUuid);
			return result;
	    }else{
	    	//生成提现单
	    	return null;
	    }
		
	}

	@Override
	public String transferRequestApi(String orderUuid){
		if(StringUtils.isEmpty(orderUuid)){
			return null;
		}
		TransferBill transferBill = transferBillService.queryTransferBillByUuid(orderUuid);
		if(transferBill == null){
			return null;
		}
		logger.info("orderUuid:["+orderUuid+"] process send to Remittance to transfer request");
		
		Map<String, String> content = buildRemittanceCommandRequsetMap(transferBill);
		Map<String, String> headerParams = getIdentityInfoMap(content);
//		Map<String, String> headerParams = buildHeaderParamsForNotifyRemittanceRequest(content);
		Result result = HttpClientUtils.executePostRequest(transferProperties.getRequestUrl(), content, headerParams);
		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		if(result == null || !result.isValid()){
			throw new ApiException(ApiMessage.SYSTEM_BUSY);
		}
		Map<String, Object> resMap = JsonUtils.parse(responseStr);
		if("0".equals(resMap.get("code").toString())) {
			logger.info("orderUuid:["+orderUuid+"] process send to Remittance success ,update transferbill status");
			transferBill.setTransactionRecipient(TransactionRecipient.OPPOSITE);
			transferBill.setExecutionStatus(ExecutionStatus.PROCESSING);
			transferBill.setLastModifyTime(new Date());
			transferBillService.saveOrUpdate(transferBill);
			return JsonUtils.toJsonString(responseStr);
		}
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.RawData("转账请求失败，转账单号 ［"+orderUuid+"］，结果［"+responseStr+"］"));
		return JsonUtils.toJsonString(responseStr);
		
	}


	@Override
	public Map<String, String> buildRemittanceCommandRequsetMap(TransferBill transferBill) {

		Map<String, String> result = new HashMap<String, String>();
		result.put("fn", CommandOpsFunctionCodes.COMMAND_TRANSFER_COMMAND);
		result.put("requestNo", transferBill.getOrderUuid());
		result.put("remittanceId", transferBill.getOrderUuid());
		result.put("productCode", transferBill.getFinancialContractNo());
		result.put("plannedRemittanceAmount", transferBill.getPlannedTotalAmount().toString());
		result.put("auditorName", transferBill.getAuditorName());
		if(transferBill.getAuditTime()!=null){
			result.put("auditTime", DateUtils.format(transferBill.getAuditTime(),"yyyy-MM-dd HH:mm:ss"));
		}
		result.put("remark", transferBill.getRemark());
		result.put("remittanceStrategy", "0");
		result.put("remittanceDetails", JSONArray.fromObject(Arrays.asList(transferBill.convertRemittanceDetail())).toString());
		result.put("notifyUrl", transferProperties.getLocalNotifyUrl());
		result.put("transferTransactionType", transferBill.getTransactionType().ordinal()+"");
		
		return result;
	}
	
	
	public static final String TEST_MERID = "t_test_zfb";
	public static final String TEST_SECRET = "123456";
	
	/** t_merchant的私钥 **/
	public  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
	
	public Map<String, String> getIdentityInfoMap(Map<String, String> requestParams) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
		headers.put("sign", sign);
		return headers;
	}
	
	private Map<String, String> buildHeaderParamsForNotifyRemittanceRequest(
	        Map<String, String> content) {
	        Map<String, String> headerParams = new HashMap<String, String>();
	        headerParams.put("merId", yntrustFileTask.getMerId());
	        headerParams.put("secret", yntrustFileTask.getSecret());

	        String privateKey = dictionaryService.getPlatformPrivateKey();
	        String signContent = ApiSignUtils.getSignCheckContent(content);
	        String signedMsg = ApiSignUtils.rsaSign(signContent, privateKey);

	        headerParams.put("sign", signedMsg);
	        return headerParams;
	    }
	
	@Override
	public String buildContentForNotify(String orderUuid) {
		if(StringUtils.isEmpty(orderUuid)){
			return StringUtils.EMPTY;
		}
		TransferBill transferBill = transferBillService.queryTransferBillByUuid(orderUuid);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("requestId",transferBill.getRequestNo());
		result.put("referenceId", transferBill.getRequestNo());
		result.put("orderNo", transferBill.getOrderNo());
		result.put("amount", transferBill.getActualTotalAmount());
		result.put("status", transferBill.getExecutionStatus().ordinal());
		result.put("comment",transferBill.getRemark());
		result.put("paidNoticInfos", "");
		return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}
	
	

	@Override
	public String processNotifyInfo(Map<String, Object> paramMap) {
		
		try {
			List<String> paidNoticInfosList = JsonUtils.parseArray(paramMap.get("PaidNoticInfos").toString(),String.class);
			String paidNoticInfo = paidNoticInfosList.get(0);
			Map<String, Object> paidNoticInfoMap = JsonUtils.parse(paidNoticInfo);
			String referenceId = paidNoticInfoMap.get("ReferenceId").toString();
			if(StringUtils.isNotEmpty(referenceId)){
				logger.info("processNotifyInfo# orderuuid["+referenceId+"] start ");
				TransferBill transferBill = transferBillService.queryTransferBillByUuid(referenceId);
				if(transferBill== null){
					throw new ApiException(ApiResponseCode.INVALID_PARAMS);
				}
				Map<String, Object> paidNoticInfoMapFirst = JsonUtils.parse(paidNoticInfo);
				List<Map<String,Object>> paidDetailsList = JsonUtils.parseArray(paidNoticInfoMapFirst.get("PaidDetails").toString());
				String status = String.valueOf(paidDetailsList.get(0).get("Status"));
				ExecutionStatus statusEnum = parseStatusFliter(status);
				if(statusEnum==null){
					throw new ApiException(ApiResponseCode.INVALID_PARAMS);
				}
				if(statusEnum == ExecutionStatus.SUCCESS){
					transferBill.setCompleteTime(DateUtils.parseDate(paidDetailsList.get(0).get("ActExcutedTime").toString(), "yyyy-MM-dd HH:mm:ss"));
					transferBill.setActualTotalAmount(transferBill.getPlannedTotalAmount());
				}
				transferBill.setExecutionStatus(statusEnum);
				transferBill.setLastModifyTime(new Date());
				transferBill.setExecutionRemark(paidDetailsList.get(0).get("Result").toString());
				transferBillService.saveOrUpdate(transferBill);
				return referenceId;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void processingTransferBillCallback(String orderUuid){
		if(StringUtils.isEmpty(orderUuid)){
			return ;
		}
		NotifyApplication notifyApplication = null;
		try {
			String content = buildContentForNotify(orderUuid);

			HashMap<String, String> headerParams = buildHeaderParamsForNotifyTransferResult(content);

			notifyApplication = buildNotifyApplication(orderUuid, content, headerParams);
		} catch (Exception e) {
			logger.error("构建notifyserver回调上下文失败");
			e.printStackTrace();
		}
		morganStanleyNotifyServer.pushJob(notifyApplication);
	}
	
	private ExecutionStatus parseStatusFliter(String status){
		switch (status) {
		case "1":
			return ExecutionStatus.SUCCESS;
		case "2":
			return ExecutionStatus.FAIL;
		case "3":
			return ExecutionStatus.ABANDON;
		case "4":
			return ExecutionStatus.ABNORMAL;

		default:
			return null;
		}
	}
	

	private HashMap<String, String> buildHeaderParamsForNotifyTransferResult(
			String content){
		HashMap<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merId", yntrustFileTask.getMerId());
		Dictionary dictionary;
		try {
			dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
		} catch (DictionaryNotExsitException e) {
			logger.error(".#TransferBillHandlerImpl get private key fail");
			e.printStackTrace();
			return headerParams;
		}
		String signData =  ApiSignUtils.rsaSign(content, dictionary.getContent());
		headerParams.put("sign", signData);
		
		return headerParams;
	}
	
	private NotifyApplication buildNotifyApplication(String orderUuid, String content, HashMap<String, String> headParameters) throws Exception {
		TransferBill transferBill = transferBillService.queryTransferBillByUuid(orderUuid);
		if(transferBill == null)
			return null;
		String notifyUrl = transferBill.getNotifyUrl();
		NotifyApplication notifyApplication = new NotifyApplication();
		if (notifyUrl == null || "".equals(notifyUrl)) {
			throw new Exception("send async transfer notify error : can not get request url.");
		}
		
		HashMap<String, String> workParams = new HashMap<String, String>();
		workParams.put("orderUuid", orderUuid);
		HashMap<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("code", "0");
		requestParams.put("data", content);
		requestParams.put("message", "");
		notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
		notifyApplication.setPostEntityType(PostEntityType.STRING);
		notifyApplication.setRequestUrl(notifyUrl);
		notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
		notifyApplication.setHeadParameters(headParameters);
		notifyApplication.setRedundanceMap(workParams);
		notifyApplication.setRequestParameters(requestParams);
		notifyApplication.setBusinessId(orderUuid);
		notifyApplication.setRetryTimes(transferBill.getPlanNotifyNumber());
		notifyApplication.setGroupName(morganStanleyNotifyConfig.getTransferNotifyGroupName());
		
		return notifyApplication;
	}
	
	@Override
	public String queryTranferBill(TransferBillQueryModel queryModel) throws Exception{
		//校验请求编号唯一
		Integer uniqueRequsetNo = transferBillService.queryUniqueRequsetNo(queryModel.getRequestNo());
		if(uniqueRequsetNo != null && uniqueRequsetNo > 0){
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}
		//校验信托合同
		FinancialContract  financialContract = financialContractService.getUniqueFinancialContractBy(queryModel.getProductCode());
		if(financialContract == null){
			throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}
		TransferTransactionType transferType = queryModel.getTransactionTypeEnum();
		if(transferType == null){
			throw new ApiException(ApiResponseCode.ORDER_PAY_WAY_ERROR);
		}
		TransferBill transferBill = null;
		if(StringUtils.isNotEmpty(queryModel.getOrderNo())){
			transferBill = transferBillService.queryTransferBillByOrderNo(financialContract.getFinancialContractUuid(), queryModel.getOrderNo(), transferType);
		}
		if(transferBill == null){
			if(StringUtils.isNotEmpty(queryModel.getOrderUuid())){
				transferBill = transferBillService.queryTransferBillByUuidAndType(financialContract.getFinancialContractUuid(), queryModel.getOrderUuid(), transferType);
			}
		}
		if(transferBill == null){
			throw new ApiException(ApiResponseCode.INVALID_PARAMS);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderUuid",transferBill.getOrderUuid());
		result.put("orderNo", transferBill.getOrderNo());
		result.put("productCode", transferBill.getFinancialContractNo());
		result.put("amount", transferBill.getPlannedTotalAmount());
		result.put("status", transferBill.getExecutionStatus().ordinal());
		result.put("comment",transferBill.getExecutionRemark());
		return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		
	}

}
