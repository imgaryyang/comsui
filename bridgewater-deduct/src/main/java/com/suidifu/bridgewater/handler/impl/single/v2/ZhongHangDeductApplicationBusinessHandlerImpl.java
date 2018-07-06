package com.suidifu.bridgewater.handler.impl.single.v2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.single.v2.ZhongHangDeductApplicationBusinessHandler;
import com.suidifu.bridgewater.model.v2.NotifyModel;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationContentValue;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;

@Component("zhongHangDeductApplicationBusinessHandler")
public class ZhongHangDeductApplicationBusinessHandlerImpl implements ZhongHangDeductApplicationBusinessHandler {

	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
	
	private static Log logger = LogFactory.getLog(ZhongHangDeductApplicationBusinessHandlerImpl.class);
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Value("#{config['zhonghang.merId']}")
	private String YX_API_MERID;
	
	@Value("#{config['zhonghang.secret']}")
	private String YX_API_SECRET;
	
	@Override
	public void execSingleNotifyFordeductApplication(String deductApplicationUuid, DeductNotifyJobServer deductSingleCallBackNotifyJobServer,String groupName, boolean isYunXin) throws Exception {
		
		String oppositeKeyWord = "[deductApplicationUuid=" + deductApplicationUuid + "]";
		
		boolean receiveDataFromRedis = true;
		
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
		
		if (deductApplication == null || StringUtils.isEmpty(deductApplication.getNotifyUrl())) {
			
			return;
			
		}
		
		if (isYunXin&&!deductApplication.isRepaymentOrderSourceType()&&!deductApplication.isSuccessOrFailedOrAbandon()) {
			
			logger.info(
					GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
							+ oppositeKeyWord + " cancel to send.");
			return;
			
		}
		
		if (!isYunXin ||
				(deductApplication.isRepaymentOrderSourceType()&& deductApplication.isProcessingOrPartSucOrAbandon())) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
							+ oppositeKeyWord + "新接口模式");
			receiveDataFromRedis = deductApplicationBusinessHandler.dealCacheDeductPlansAndPushNotifyJob(deductApplication, deductSingleCallBackNotifyJobServer,groupName);
			
		}
		
		if (!receiveDataFromRedis) {
			
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
					+ oppositeKeyWord + "未从redis获取到缓存  不进行数据库更新和回调操作或者不符合回调条件");
			
			return;
		}
		
		String notifyUrl = deductApplication.getNotifyUrl();
		String content = genContext(deductApplicationBusinessHandler.genNotifyModel(deductApplication));

		int actualNotifyNumber = deductApplication.getActualNotifyNumber();
		
		int planNotifyNumber = deductApplication.getPlanNotifyNumber();

		deductApplication.setActualNotifyNumber(actualNotifyNumber+1);
		
		Map<String, String> headerParams = buildHeaderParamsForNotifyDeductResult(content,deductApplication.getFinancialContractUuid());
		logger.info(".#ZhongHangDeductApplicationBusinessHandlerImpl回调参数header："+headerParams);
		
		logger.info(
				GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
						+ oppositeKeyWord + "begin to send post");
		Result result = HttpClientUtils.executePostRequest(notifyUrl, content, headerParams);

		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		if (result.isValid()) {
			Map<String, Object> respMap = JsonUtils.parse(responseStr);
			
			if (MapUtils.isEmpty(respMap)) {
				updateDeductApplicationExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
						deductApplication);
				return;
			}
			Map<String, Object> statusMap;
			Boolean isSuccess = false;
			String responseCode = "";
			try {
				// todo : 将接口方的参数转换成本地化. Status,IsSuccess, ResponseCode,0000
				statusMap = (Map<String, Object>) respMap.get("status");
				if (MapUtils.isEmpty(statusMap)) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ "处理获取status时为空");
					updateDeductApplicationExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
							deductApplication);
					return;
				}
				isSuccess = (Boolean) statusMap.get("isSuccess");
				responseCode = String.valueOf(statusMap.get("responseCode"));
				// 返回为true并且ResponseCode为0000的时候,表示为成功
				if (isSuccess && StringUtils.equals("0000", responseCode)) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec()
							+ bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord
							+ GloableLogSpec.RawData(
									"receive notify result success 内容［" + content + "］，结果［" + responseStr + "］"));
//					this.genericDaoSupport.executeSQL(
//							"UPDATE t_deduct_application " + "SET actual_notify_number = (actual_notify_number + 1) "
//									+ "WHERE deduct_application_uuid =:deductApplicationUuid",
//							"deductApplicationUuid", deductApplicationUuid);
					
					this.genericDaoSupport.save(deductApplication);
					
					return;
				}
				updateDeductApplicationExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
						deductApplication);
			} catch (Exception e) {
				updateDeductApplicationExecuteIfFailed(actualNotifyNumber, planNotifyNumber, oppositeKeyWord,
						deductApplication);
				e.printStackTrace();
				logger.error(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM
						+ oppositeKeyWord +",msg:"+ExceptionUtils.getFullStackTrace(e));

			}
			return;
		}
	}
	
	private Map<String, String> buildHeaderParamsForNotifyDeductResult(
			String content,String financialContractUuid ) {
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merId", YX_API_MERID);
		headerParams.put(ApiConstant.PARAMS_SECRET,YX_API_SECRET);
		
		String context = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_SIGN.getCode());
		if (StringUtils.equals(context, FinancialContractConfigurationContentValue.SIGN)) {
			Dictionary dictionary;
			try {
				dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
			} catch (DictionaryNotExsitException e) {
				logger.error(".#ZhongHangDeductApplicationBusinessHandlerImpl get private key fail");
				e.printStackTrace();
				return headerParams;
			}
			String signData = ApiSignUtils.rsaSign(content, dictionary.getContent());
			headerParams.put("sign", signData);
		}
		
		return headerParams;
	}

	
	
	
	public void updateDeductExecuteIfFailed(int actualNotifyNumber,int planNotifyNumber,String oppositeKeyWord, String deductApplicationUuid){

		this.genericDaoSupport.executeSQL(
				"UPDATE t_deduct_application "
				+ "SET actual_notify_number = (actual_notify_number + 1) "
				+ "WHERE deduct_application_uuid =:deductApplicationUuid", "deductApplicationUuid", deductApplicationUuid);
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("处理除了响应成功的情况,如响应超时,异常,响应失败等等情况"));
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("自增后actual_notify_number:["+ (actualNotifyNumber + 1) +"]操作"));


	}
	
	private void updateDeductApplicationExecuteIfFailed(int actualNotifyNumber,int planNotifyNumber, String oppositeKeyWord,DeductApplication deductApplication){
		this.genericDaoSupport.save(deductApplication);
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("处理除了响应成功的情况,如响应超时,异常,响应失败等等情况"));
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("自增后actual_notify_number:["+ (actualNotifyNumber + 1) +"]操作"));
	}
	
	
	
	private String genContext(NotifyModel model) throws Exception{
		Map<String, Object> params = new HashMap<>();
		params.put("requestId", UUID.randomUUID().toString());
		params.put("referenceId", model.getRequestNo());
		params.put("orderNo",model.getDeductId());
		params.put("amount",model.getAmount());
		params.put("status",model.getExecutionStatus());
		params.put("comment",model.getExecutionRemark());
		params.put("lastModifiedTime",model.getLastModifiedTime());
		params.put("paidNoticInfos",model.getPaidNoticInfos());
		
		return JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}
}
