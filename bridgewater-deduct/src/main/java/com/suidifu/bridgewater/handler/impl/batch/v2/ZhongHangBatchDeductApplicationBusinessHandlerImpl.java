/**
 * 
 */
package com.suidifu.bridgewater.handler.impl.batch.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.NotifyStatus;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanApplicationCheckLogService;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanCacheService;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.fast.FastDeductApplication;
import com.suidifu.bridgewater.fast.FastDeductApplicationEnum;
import com.suidifu.bridgewater.fast.deductplan.FastDeductPlan;
import com.suidifu.bridgewater.fast.deductplan.FastDeductPlanEnum;
import com.suidifu.bridgewater.handler.batch.v2.ZhongHangBatchDeductApplicationBusinessHandler;
import com.suidifu.giotto.handler.FastDataWithTemperatureHandler;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.service.DictionaryService;


/**
 * @author wukai
 *
 */
@Component("zhongHangBatchDeductApplicationBusinessHandler")
public class ZhongHangBatchDeductApplicationBusinessHandlerImpl implements
		ZhongHangBatchDeductApplicationBusinessHandler {
	
	private static Log logger = LogFactory.getLog(ZhongHangBatchDeductApplicationBusinessHandlerImpl.class);
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Value("#{config['zhonghang.merId']}")
	private String ZhonghangMerId;
	
	@Autowired
	private DeductPlanApplicationCheckLogService deductPlanApplicationCheckLogService;
	
	@Autowired
	private BatchDeductApplicationService batchDeductApplicationService;
	
	@Autowired
	private DeductPlanCacheService deductPlanCacheService;
	
	@Autowired
	private FastDataWithTemperatureHandler fastDataWithTemperatureHandler;
	
	@Autowired
	private DeductPlanService deductPlanService;
	
	private Map<String, String> buildHeaderParamsForNotifyDeductResult(
			String content) throws Exception {
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merId", ZhonghangMerId);
		Dictionary privateKey = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
		String signData = ApiSignUtils.rsaSign(content, privateKey.getContent());
		headerParams.put("sign", signData);
		return headerParams;
	}
	@Override
	public void execBatchNotifyForDeductApplication() {
		
		List<BatchDeductApplication> batchDeductApplications = batchDeductApplicationService.getNeedToNotifyCounterBatchDeductApplications();
		
		for (BatchDeductApplication batchDeductApplication : batchDeductApplications) {
			
			if(!isCanNotifyCounter(batchDeductApplication)){
				continue;
			}
			//开始执行批次回调
			
			handleBatchNotify(batchDeductApplication);
		}
		
	}
	
	//批次回调
	private void handleBatchNotify(BatchDeductApplication batchDeductApplication) {
		
		String notifyUrl = batchDeductApplication.getNotifyUrl();
		
		Map<String,Object> reuslt = buildNotifyResult(batchDeductApplication);
		
		logger.info("need to callback content is :"+JsonUtils.toJsonString(reuslt)+",notifyUrl is ["+notifyUrl+"]");
		
		String content = JsonUtils.toJsonString(reuslt);

		Map<String, String> headerParams;
		
		String errorId = UUID.randomUUID().toString();
		
		Long id = batchDeductApplication.getId();
		
		String logErrorMsg = "#handleBatchNotify# occur exception with errorId["+errorId+"],batchDeductApplication id["+batchDeductApplication.getBatchDeductId()+"]";
		
		try {
			headerParams = buildHeaderParamsForNotifyDeductResult(content);
			
			Result result = HttpClientUtils.executePostRequest(notifyUrl, content, headerParams);
			
			if(null == result){
				
				logger.error("result is null in "+logErrorMsg);
				
				batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Fail, null, null, "result is null,errorId["+errorId+"]");
				
				return;
			}
			
			if(!result.isValid()){
				
				logger.error("result code is't success "+logErrorMsg);
				
				batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Fail, null, null, "result code is not 0,errorId["+errorId+"]");
				
				return;
			}
			
				
			String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
			
			Map<String, Object> respMap = JsonUtils.parse(responseStr);
		
			if (MapUtils.isEmpty(respMap)) {
				
				logger.error("respMap is empty "+logErrorMsg);
				
				batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Fail, null, null, "respMap is empty,errorId["+errorId+"]");
			
				return;
			}
			
			Map<String, Object> statusMap;
			Boolean isSuccess = false;
			String responseCode = "";
			
			
				// todo : 将接口方的参数转换成本地化. Status,IsSuccess, ResponseCode,0000
				
				statusMap = (Map<String, Object>) respMap.get("status");
				
				if (MapUtils.isEmpty(statusMap)) {
					
					logger.error("statusMap is empty "+logErrorMsg);
					
					batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Fail, null, null, "statusMap is empty,errorId["+errorId+"]");
					
					return;
				}
				isSuccess = (Boolean) statusMap.get("isSuccess");
				responseCode = String.valueOf(statusMap.get("responseCode"));
				// 返回为true并且ResponseCode为0000的时候,表示为成功
				
				if(!isSuccess || !StringUtils.equals("0000", responseCode)){
					
					String logInfoPrefix = "status from counter is not sucecess,isSuccess["+isSuccess+"],responseCode["+responseCode+"] ";
					
					logger.error(logInfoPrefix+logErrorMsg);
					
					batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Fail, null, null,logInfoPrefix+",errorId["+errorId+"]");
					
					return;
					
				}
			
				batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Success, null, null,"");
			
		} catch (Exception e) {
			
			String logInfoPrefix = "occur exception";
			
			logger.error(logInfoPrefix+logErrorMsg+",with full stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			batchDeductApplicationService.updateBatchDeductApplicationStatus(id, null, NotifyStatus.Fail, null, null,logInfoPrefix+",errorId["+errorId+"]");
		
		}

	}
	private Map<String,Object> buildNotifyResult(
			BatchDeductApplication batchDeductApplication) {
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("requestId",UUID.randomUUID().toString());
		//原始请求编号
		data.put("originRequestId",batchDeductApplication.getRequestNo());
		data.put("batchDeductId",batchDeductApplication.getBatchDeductId());
		
		List<Map<String,String>> paidNoticInfos = buildPaidNoticInfos(batchDeductApplication);
		
		data.put("paidNoticInfos",paidNoticInfos);
		
		return data;
	}

	private List<Map<String, String>> buildPaidNoticInfos(
			BatchDeductApplication batchDeductApplication) {
		
		String batchDeductApplicationUuid = batchDeductApplication.getBatchDeductApplicationUuid();
		
		//从redis中构建
		
		List<DeductNotifyInfo> resultFromRedis = buildBatchPaidNoticInfoFromDeductApplication(batchDeductApplicationUuid);
		
		//从log日志中构建
		List<DeductNotifyInfo> resultFromErrorLog = buildBatchPaidNoticInfoFromLog(batchDeductApplicationUuid);
		
		List<DeductNotifyInfo> allResult = new ArrayList<>();
		
		allResult.addAll(resultFromErrorLog);
		allResult.addAll(resultFromRedis);

		logger.info("key from redis"+ JSON.toJSONString(resultFromRedis.stream().map(a->a.getDeductApplicationUuid()).collect(Collectors.toList())));

		logger.info("key from log"+ JSON.toJSONString(resultFromErrorLog.stream().map(a->a.getDeductApplicationUuid()).collect(Collectors.toList())));

		List<Map<String,String>> result = new ArrayList<>();
		
		for (DeductNotifyInfo DeductNotifyInfo:allResult) {
			
			Map<String,String> map  = converToMap(DeductNotifyInfo);
			
			result.add(map);
			
		}
		return result;
	}
	
	private void updateDeductApplicationStatus(String batchDeductApplicationUuid,Map<String,DeductApplicationExecutionStatus> failOrPartSuccessDeductApplicationMap) {
		
		List<String> deductApplicationUuids = deductApplicationService.getAllDeductApplicationUuidsBy(batchDeductApplicationUuid);
		
		if(CollectionUtils.isEmpty(deductApplicationUuids)) {
			return;
		}
		
		for (String deductApplicationUuid : deductApplicationUuids) {
			
			FastDeductApplication fastDeductApplication = fastDataWithTemperatureHandler.getByKey(FastDeductApplicationEnum.DEDUCT_APPLICATION_UUID, deductApplicationUuid, FastDeductApplication.class);
			
			if(failOrPartSuccessDeductApplicationMap.containsKey(deductApplicationUuid)) {
				
				fastDeductApplication.setExecutionStatus(failOrPartSuccessDeductApplicationMap.get(deductApplicationUuid).ordinal());
				
			}else {
				fastDeductApplication.setExecutionStatus(DeductApplicationExecutionStatus.SUCCESS.ordinal());
			}
			fastDeductApplication.setLastModifiedTime(new Date());
			fastDeductApplication.setCompleteTime(new Date());
			fastDataWithTemperatureHandler.updateData(fastDeductApplication);
			
		}
	}
	
	private Map<String,String> converToMap(DeductNotifyInfo deductNotifyInfo){
		
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("deductId", deductNotifyInfo.getDeductId());
		map.put("accountName", deductNotifyInfo.getAccountName());
		map.put("accountNo", deductNotifyInfo.getAccountNo());
		map.put("failMessage", deductNotifyInfo.getFailMessage());
		map.put("status", deductNotifyInfo.getStatus().ordinal()+"");
		map.put("executedTotalAmount", null== deductNotifyInfo.getExecutedTotalAmount() ? "0":deductNotifyInfo.getExecutedTotalAmount().toString());
		
		return map;
	}
	
	private List<DeductNotifyInfo> buildBatchPaidNoticInfoFromDeductApplication(
			String batchDeductApplicationUuid) {

		List<String> deductApplicationUuidList =  deductApplicationService.getAllDeductApplicationUuidsBy(batchDeductApplicationUuid);
		
		Map<String,DeductApplicationQueryResultMapping> deductApplicationQueryResultMappingAllResult= new HashMap<>();
		
		for (String deductApplicationUuid : deductApplicationUuidList) {
			
			FastDeductApplication deductApplication = fastDataWithTemperatureHandler.getByKey(FastDeductApplicationEnum.DEDUCT_APPLICATION_UUID, deductApplicationUuid, FastDeductApplication.class);
			
			DeductApplicationQueryResultMapping deductApplicationQueryResultMapping = deductApplicationQueryResultMappingAllResult.get(deductApplicationUuid);
			
			if(deductApplicationQueryResultMapping == null) {
				
				deductApplicationQueryResultMapping = new DeductApplicationQueryResultMapping();
			}
			
			deductApplicationQueryResultMapping.setDeductApplication(deductApplication);

			List<DeductPlan> deductPlanList = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplicationUuid);

			if(CollectionUtils.isNotEmpty(deductPlanList)){

				DeductPlan deductPlan = deductPlanList.get(0
				);

				deductApplicationQueryResultMapping.setAccountName(deductPlan.getCpBankAccountHolder());
				deductApplicationQueryResultMapping.setAccountNo(deductPlan.getCpBankCardNo());
			}

			
			deductApplicationQueryResultMappingAllResult.put(deductApplicationUuid, deductApplicationQueryResultMapping);
		}
		
		return deductApplicationQueryResultMappingAllResult.values().stream().map(a->a.convertToNotifyInfo()).filter(a->{return a != null;}).collect(Collectors.toList());
	}

	class DeductNotifyInfo{
		
		private String deductApplicationUuid;
		
		private String deductId;
		
		private String accountName;
		
		private String accountNo;
		
		private String failMessage;
		
		// 扣款状态 0成功 1 失败 2部分成功
		private DeductApplicationExecutionStatus status;
		
		private BigDecimal executedTotalAmount;

		public String getDeductId() {
			return deductId;
		}

		public void setDeductId(String deductId) {
			this.deductId = deductId;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getFailMessage() {
			return failMessage;
		}

		public void setFailMessage(String failMessage) {
			this.failMessage = failMessage;
		}

		public DeductApplicationExecutionStatus getStatus() {
			return status;
		}

		public void setStatus(DeductApplicationExecutionStatus status) {
			this.status = status;
		}

		public BigDecimal getExecutedTotalAmount() {
			return executedTotalAmount;
		}

		public void setExecutedTotalAmount(BigDecimal executedTotalAmount) {
			this.executedTotalAmount = executedTotalAmount;
		}

		public String getDeductApplicationUuid() {
			return deductApplicationUuid;
		}

		public void setDeductApplicationUuid(String deductApplicationUuid) {
			this.deductApplicationUuid = deductApplicationUuid;
		}
		
	}
	
	class DeductApplicationQueryResultMapping{
		
		private FastDeductApplication deductApplication;

		private String accountName;

		private String accountNo;
		
		public FastDeductApplication getDeductApplication() {
			return deductApplication;
		}

		public void setDeductApplication(FastDeductApplication deductApplication) {
			this.deductApplication = deductApplication;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public DeductNotifyInfo convertToNotifyInfo() {
			
			DeductNotifyInfo deductNotifyInfo = null;
			
			DeductApplicationExecutionStatus status = EnumUtil.fromOrdinal(DeductApplicationExecutionStatus.class,this.deductApplication.getExecutionStatus());
			
			if(status == DeductApplicationExecutionStatus.FAIL || status == DeductApplicationExecutionStatus.PART_OF_SUCCESS){
				
					deductNotifyInfo = new DeductNotifyInfo();
					
					deductNotifyInfo.setDeductId(this.getDeductApplication().getDeductId());
					deductNotifyInfo.setAccountName(this.getAccountName());
					deductNotifyInfo.setAccountNo(this.getAccountNo());
					
					deductNotifyInfo.setFailMessage(this.deductApplication.getExecutionRemark());
					deductNotifyInfo.setStatus(status);
					deductNotifyInfo.setExecutedTotalAmount(this.getDeductApplication().getActualDeductTotalAmount());
					deductNotifyInfo.setDeductApplicationUuid(this.getDeductApplication().getDeductApplicationUuid());

				
			}
			return deductNotifyInfo;
		}

		
	}

	private List<DeductNotifyInfo> buildBatchPaidNoticInfoFromLog(String batchDeductApplicationUuid){
	
		List<DeductPlanApplicationCheckLog> deductPlanApplicationCheckLogList = deductPlanApplicationCheckLogService.findDeductPlanApplicationCheckLogListBy(batchDeductApplicationUuid);
		
		List<DeductNotifyInfo> partResult = new ArrayList<>();
		
		for (DeductPlanApplicationCheckLog deductPlanApplicationCheckLog : deductPlanApplicationCheckLogList) {
			
			partResult.add(buildSinglePaidNoticInfo(deductPlanApplicationCheckLog));
		}
		return partResult;
	}
	
	private DeductNotifyInfo buildSinglePaidNoticInfo(DeductPlanApplicationCheckLog deductPlanApplicationCheckLog){
		
		DeductNotifyInfo deductNotifyInfo = new DeductNotifyInfo();
		
		deductNotifyInfo.setDeductId(deductPlanApplicationCheckLog.getDeductId());
		deductNotifyInfo.setAccountName(deductPlanApplicationCheckLog.getAccountName());
		deductNotifyInfo.setAccountNo(deductPlanApplicationCheckLog.getAcccountNo());
		deductNotifyInfo.setFailMessage( deductPlanApplicationCheckLog.getFailReason());
		deductNotifyInfo.setStatus(DeductApplicationExecutionStatus.FAIL);
		deductNotifyInfo.setDeductApplicationUuid(deductPlanApplicationCheckLog.getDeductApplicationUuid());
		
		return deductNotifyInfo;
	}


	//是否可以回调对手方
	private boolean isCanNotifyCounter(BatchDeductApplication batchDeductApplication){
		
		List<String> deductApplicationUuidList =  deductApplicationService.getAllDeductApplicationUuidsBy(batchDeductApplication.getBatchDeductApplicationUuid());
		
		//生成扣款请求数
		int deductApplicationCount = null == deductApplicationUuidList ? 0: deductApplicationUuidList.size();
		
		//生成扣款请求失败数
		int failCount = batchDeductApplication.getFailCount();
		
		 //总的扣款请求数
		int totalCount = batchDeductApplication.getTotalCount();
		
		if(failCount==totalCount){
			return true;
		}
		
		return deductApplicationService.isAllDeductApplicationInFinalState(batchDeductApplication.getBatchDeductApplicationUuid()) && (failCount+deductApplicationCount==totalCount);
	}

}
