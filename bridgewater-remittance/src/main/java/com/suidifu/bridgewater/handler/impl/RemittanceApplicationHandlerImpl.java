package com.suidifu.bridgewater.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.suidifu.giotto.exception.GiottoException;
import com.zufangbao.gluon.util.CardBinUtil;
import com.zufangbao.sun.yunxin.service.*;
import org.apache.commons.collections.CollectionUtils;
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
import com.suidifu.bridgewater.RemittanceContext;
import com.suidifu.bridgewater.RemittanceStrategyFactory;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.fast.FastRemittanceApplication;
import com.suidifu.bridgewater.fast.FastRemittanceApplicationDetail;
import com.suidifu.bridgewater.fast.FastRemittanceApplicationDetailEnum;
import com.suidifu.bridgewater.fast.FastRemittanceApplicationEnum;
import com.suidifu.bridgewater.fast.FastRemittancePlan;
import com.suidifu.bridgewater.fast.FastRemittancePlanEnum;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastDataWithTemperatureHandler;
import com.suidifu.giotto.service.relation.RemittanceRelation;
import com.suidifu.giotto.service.relation.RemittanceRelationCacheService;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationContentValue;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.entity.remittance.CertificateType;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.NotifyStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationCheckStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationDetail;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategy;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.ModifyRemittanceApplicationLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.yunxin.service.remittance.RemittanceBlackListService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;


@Component("RemittanceApplicationHandler")
public class RemittanceApplicationHandlerImpl implements
		IRemittanceApplicationHandler {
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private IRemittanceApplicationDetailService iRemittanceApplicationDetailService;
	
	@Autowired
	private IRemittancePlanService iRemittancePlanService;
	
	@Autowired
	private IRemittancePlanExecLogService iRemittancePlanExecLogService;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private RemittanceStrategyFactory remittanceStrategyFactory;
	
	@Autowired
	public JpmorganApiHelper jpmorganApiHelper;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport; 
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private RemittanceBlackListService remittanceBlackListService;

	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;

	@Autowired
	private ModifyRemittanceApplicationLogService modifyRemittanceApplicationLogService;

	@Autowired
	private FastDataWithTemperatureHandler fastDataWithTemperatureHandler;
	
	@Autowired
	private CardBinService cardBinService;
//	@Autowired
//	private SupplierService supplierService;
//	@Autowired
//	private BankCardService bankCardService;
//	@Autowired
//	private SupplierIdentityMapService supplierIdentityMapService;
	
	@Autowired
	private RemittanceRelationCacheService remittanceRelationCacheService;
	
	@Value("#{config['yx.notify.number']}")
	private int YX_NOTIFY_NUMBER = 0;
	
	@Value("#{config['yx.notify.url']}")
	private String YX_NOTIFY_URL = null;
	
	@Value("#{config['yx.api.merId']}")
	private String YX_API_MERID = null;
	
	@Value("#{config['yx.api.secretkey']}")
	private String YX_API_SECRETKEY = null;
	
	@Value("#{config['remittance.notify.mq.jpmorgan.callback.url']}")
	private String jpmorganCallbackUrl;
	
	@Value("#{config['business_verify_no_response_time']}")
	private Integer business_verify_no_response_time;
	
	private static final int PLAN_CREDIT_CASH_FLOW_CHECK_NUMBER = 3;
	
	private static final Log logger = LogFactory.getLog(RemittanceApplicationHandlerImpl.class);

	private static final int CHECK_RETRY_NUMBER = 3;
	
    private static final Log st_logger = LogFactory.getLog("stLogger");

	@Override
	public List<RemittancePlan> saveRemittanceInfo(RemittanceCommandModel commandModel,List<TradeSchedule> tradeSchedules, FinancialContract financialContract) {
		String remittanceApplicationUuid = commandModel.getRemittanceApplicationUuid();
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getRemittanceApplicationBy(remittanceApplicationUuid);
		
		List<String> detailUuids = iRemittanceApplicationDetailService.getRemittanceApplicationDetailUuidByApplicationUuid(remittanceApplicationUuid);

		String uniqueId = commandModel.getUniqueId();
		String contractNo = commandModel.getContractNo();
		
		Map<String, List<TradeSchedule>> detailMappingTrades = tradeSchedules.stream().collect(Collectors.groupingBy(tradeSchedule -> tradeSchedule.getRelatedDetailUuid()));

		for (String detailUuid : detailUuids) {

			List<TradeSchedule> singleDetailMappingTrades = detailMappingTrades.get(detailUuid);
			
			iRemittanceApplicationDetailService.fillTotalCountIntoDetail(detailUuid, singleDetailMappingTrades.size());
			
		}
		
		List<RemittancePlan> remittancePlans = new ArrayList<>();
		String creatorName =remittanceApplication.getCreatorName();
		for (TradeSchedule tradeSchedule : tradeSchedules) {
			RemittancePlan remittancePlan = convertToRemittancePlan(tradeSchedule, remittanceApplicationUuid, financialContract.getFinancialContractUuid(), financialContract.getId(), uniqueId, contractNo, creatorName);
			remittancePlan.setTransferTransactionType(commandModel.getTransactionType());
			iRemittancePlanService.save(remittancePlan);
			remittancePlans.add(remittancePlan);
			String execReqNo = tradeSchedule.getSourceMessageUuid();
			RemittancePlanExecLog remittancePlanExecLog = new RemittancePlanExecLog(remittancePlan, execReqNo);
			remittancePlanExecLog.setTransferTransactionType(commandModel.getTransactionType());
			iRemittancePlanExecLogService.save(remittancePlanExecLog);
		}
		return remittancePlans;
		
	}

	/**
	 * 更新交易接收方状态，为对端（交易受理成功）
	 * @param remittanceApplicationUuid
	 */
	@Override
	public void updateRemittanceInfoAfterSendSuccessBy(
			String remittanceApplicationUuid) {
		if(StringUtils.isEmpty(remittanceApplicationUuid)) {
			return;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		
		String updateSql =
				" UPDATE t_remittance_application "
					+ " SET transaction_recipient =:transactionRecipient ,version_lock =:newVersion "
					+ " WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion ";
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, updateSql, params);
	}

	/**
	 * 更新放款状态（交易受理失败）
	 */
	@Override
	public void updateRemittanceInfoAfterSendFailBy(String remittanceApplicationUuid, String executionRemark) {
		Map<String, Object> paramsForFail = new HashMap<String, Object>();
		paramsForFail.put("executionStatus", ExecutionStatus.FAIL.ordinal());
		paramsForFail.put("executionRemark", executionRemark);
		paramsForFail.put("lastModifiedTime", new Date());
		paramsForFail.put("remittanceApplicationUuid", remittanceApplicationUuid);
		
		executeHqlForRemittanceExceptApplicationDetailFail(paramsForFail);
		executeHqlForRemittanceExceptApplicationFail("t_remittance_plan", paramsForFail);
		executeHqlForRemittanceExceptApplicationFail("t_remittance_plan_exec_log", paramsForFail);
		executeHqlForRemittanceApplicationFail(remittanceApplicationUuid, paramsForFail);
	}

    /**
     * 更新放款状态（二次放款--交易受理失败）
     */
    @Override
    public void updateRemittanceInfoAfterSendFailForSecondRemittance(String remittanceApplicationUuid, List<String> planUuids, String executionRemark) {
        if (CollectionUtils.isEmpty(planUuids)){
            return;
        }
        Map<String, Object> paramsForFail = new HashMap<String, Object>();
        paramsForFail.put("executionStatus", ExecutionStatus.FAIL.ordinal());
        paramsForFail.put("executionRemark", executionRemark);
        paramsForFail.put("lastModifiedTime", new Date());
        paramsForFail.put("planUuids", planUuids);
        paramsForFail.put("applicationUuid", remittanceApplicationUuid);

        executeHqlForRemittanceExecLogFail(paramsForFail);
        executeHqlForRemittancePlanFail(paramsForFail);

        Set<String> needUpdateRemittanceApplicationDetailUuids = new HashSet<>();
        for (String planUuid : planUuids){
            RemittancePlan rp = iRemittancePlanService.getUniqueRemittancePlanByUuidUseSql(planUuid);
            if (rp == null){
                continue;
            }
            String detailUuid = rp.getRemittanceApplicationDetailUuid();
            needUpdateRemittanceApplicationDetailUuids.add(detailUuid);
        }
        executeHqlForRemittanceApplicationDetailFail(needUpdateRemittanceApplicationDetailUuids,paramsForFail);
        executeHqlForRemittanceApplicationFailV2(remittanceApplicationUuid, paramsForFail);
    }

    private void executeHqlForRemittanceApplicationFail(String remittanceApplicationUuid, Map<String, Object> paramsForFail) {
		Map<String, Object> params = new HashMap<>(paramsForFail);
		params.put("checkRequestNo", UUID.randomUUID().toString());
		
		String sql = "UPDATE t_remittance_application SET actual_count = total_count,check_request_no =:checkRequestNo,version_lock =:newVersion, execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion ";
		
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, sql, params);
	}

    private void executeHqlForRemittanceApplicationFailV2(String remittanceApplicationUuid, Map<String, Object> paramsForFail){
        RemittanceApplication application = iRemittanceApplicationService.getRemittanceApplicationByUseSql(remittanceApplicationUuid);
        List<RemittanceApplicationDetail> details = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuidUseSql(remittanceApplicationUuid);
        if ( application == null || CollectionUtils.isEmpty(details)){
            throw new RuntimeException("application is null or details is empty");
        }
        BigDecimal planTotalAmount = application.getPlannedTotalAmount();
        BigDecimal actualTotalAmount = BigDecimal.ZERO;
        List<ExecutionStatus> planExecutionStatuses = new ArrayList<>();
        for (RemittanceApplicationDetail detail : details) {
            actualTotalAmount = actualTotalAmount.add(detail.getActualTotalAmount());
            planExecutionStatuses.add(detail.getExecutionStatus());
        }

        ExecutionStatus finalStatus = doEvaluateFinalStatus(planExecutionStatuses, planTotalAmount, actualTotalAmount);
        Map<String, Object> params = new HashMap<>(paramsForFail);
        params.put("executionStatus", finalStatus.ordinal());
        params.put("remittanceApplicationUuid", remittanceApplicationUuid);
        params.put("checkRequestNo", UUID.randomUUID().toString());

        String sql = "UPDATE t_remittance_application SET actual_count = total_count,check_request_no =:checkRequestNo,version_lock =:newVersion, execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion ";
        iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, sql, params);


    }
	private void executeHqlForRemittanceExceptApplicationDetailFail(Map<String, Object> paramsForFail) {
		
		String sentence = "UPDATE t_remittance_application_detail SET actual_count = total_count,execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_uuid =:remittanceApplicationUuid";
		
		genericDaoSupport.executeSQL(sentence , paramsForFail);
	}

	private void executeHqlForRemittanceApplicationDetailFail(Set<String> detailUuids ,Map<String, Object> paramsForFail) {
        
        
        for (String detailUuid : detailUuids) {
            RemittanceApplicationDetail detail = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByUuidUseSql(detailUuid);
            List<RemittancePlan> plans = iRemittancePlanService.getRemittancePlanListByRemittanceApplicationDetailUuidUseSql(detailUuid);
            if (detail == null || CollectionUtils.isEmpty(plans)) {
                throw new RuntimeException("detail is null or plans is empty");
            }
            BigDecimal planTotalAmount = detail.getPlannedTotalAmount();
            BigDecimal actualTotalAmount = BigDecimal.ZERO;
            List<ExecutionStatus> planExecutionStatuses = new ArrayList<>();
            for (RemittancePlan plan : plans) {
                actualTotalAmount = actualTotalAmount.add(plan.getActualTotalAmount());
                planExecutionStatuses.add(plan.getExecutionStatus());
            }
            ExecutionStatus finalStatus = doEvaluateFinalStatus(planExecutionStatuses, planTotalAmount, actualTotalAmount);
            paramsForFail.put("executionStatus", finalStatus.ordinal());
            paramsForFail.put("detailUuid", detailUuid);
            String sentence = "UPDATE t_remittance_application_detail SET actual_count = total_count,execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_detail_uuid =:detailUuid";
            genericDaoSupport.executeSQL(sentence, paramsForFail);
        }
    }

    private void executeHqlForRemittanceExceptApplicationFail(String tableName, Map<String, Object> paramsForFail) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ");
		buffer.append(tableName);
		buffer.append(" SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_uuid =:remittanceApplicationUuid");
		
		genericDaoSupport.executeSQL(buffer.toString(), paramsForFail);
	}

    private void executeHqlForRemittanceExecLogFail(Map<String, Object> paramsForFail) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE t_remittance_plan_exec_log");
        buffer.append(" SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_plan_uuid IN (:planUuids)");

        genericDaoSupport.executeSQL(buffer.toString(), paramsForFail);
    }

    private void executeHqlForRemittancePlanFail(Map<String, Object> paramsForFail) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE t_remittance_plan");
        buffer.append(" SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_plan_uuid IN (:planUuids)");

        genericDaoSupport.executeSQL(buffer.toString(), paramsForFail);
    }
	
	private RemittancePlan convertToRemittancePlan(TradeSchedule tradeSchedule,
			String remittanceApplicationUuid, String financialContractUuid,
			Long financialContractId, String contractUniqueId,
			String contractNo, String creatorName) {
		String remittancePlanUuid = tradeSchedule.getOutlierTransactionUuid();
		String remittanceApplicationDetailUuid = tradeSchedule.getRelatedDetailUuid();
		String businessRecordNo = tradeSchedule.getRelatedBusinessRecordNo();
		String paymentChannelUuid = tradeSchedule.getFstPaymentChannelUuid();
		int transactionType = tradeSchedule.getEnumAccountSide().ordinal();
		String transactionRemark = tradeSchedule.getPostscript();
		String cpBankCode = tradeSchedule.getBankCode();
		String cpBankCardNo = tradeSchedule.getDestinationAccountNo();
		String cpBankAccountHolder = tradeSchedule.getDestinationAccountName();
		String cpIdNumber = tradeSchedule.getIdNumber();
		String cpBankProvince = tradeSchedule.getBankProvince();
		String cpBankCity = tradeSchedule.getBankCity();
		String cpBankName = tradeSchedule.getBankName();
		String pgClearingAccount = tradeSchedule.getReckonAccount();
		Date plannedPaymentDate = tradeSchedule.getFstEffectiveAbsoluteTime();
		BigDecimal plannedTotalAmount = tradeSchedule.getFstTransactionAmount();
		String executionPrecond = tradeSchedule.getExecutionPrecond();
		int priorityLevel = tradeSchedule.getPriorityLevel();
		Integer relatedPaymentGateway = tradeSchedule.getRelatedPaymentGateway();
		PaymentInstitutionName paymentInstitutionName = EnumUtil.fromOrdinal(PaymentInstitutionName.class, relatedPaymentGateway);
		String channelName = tradeSchedule.getRelatedPaymentChannelName();
		return new RemittancePlan(remittancePlanUuid,
				remittanceApplicationUuid, remittanceApplicationDetailUuid,
				financialContractUuid, financialContractId, businessRecordNo,
				contractUniqueId, contractNo, paymentInstitutionName, paymentChannelUuid, channelName, null,
				pgClearingAccount, transactionType, transactionRemark, priorityLevel, cpBankCode,
				cpBankCardNo, cpBankAccountHolder, CertificateType.ID_CARD,
				cpIdNumber, cpBankProvince, cpBankCity, cpBankName,
				plannedPaymentDate, plannedTotalAmount, executionPrecond,
				creatorName);
	}

	
	@Override
	public void singleQueryAndProcessingRemittanceResult(
			String remittanceApplicationUuid) {
		
		logger.info("#放款结果查询，开始查询，放款申请号［"+remittanceApplicationUuid+"］");
		Result result = jpmorganApiHelper.queryTradeSchedulesStatus(null, remittanceApplicationUuid);
		if(!result.isValid()) {
			String resultStr = JsonUtils.toJsonString(result);
			logger.info("#放款结果查询，通讯失败，放款申请号［"+remittanceApplicationUuid+"］,结果［"+resultStr+"］");
			return;
		}
		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		Result responseResult = JsonUtils.parse(responseStr, Result.class);
		if(responseResult != null && responseResult.isValid()) {
			String queryResultStr = String.valueOf(responseResult.get("queryResult"));
			List<QueryStatusResult> queryStatusResults = JSON.parseArray(queryResultStr, QueryStatusResult.class);
			if(queryStatusResults == null) {
				logger.info("#放款结果查询，结果解析失败，放款申请号［"+remittanceApplicationUuid+"［"+responseStr+"］");
				return;
			}
			singleAnalysisRemittanceResult(remittanceApplicationUuid,queryStatusResults);
		}else{
			logger.info("#放款结果查询，responseResult is not valid ，放款申请号［"+remittanceApplicationUuid+"］,结果［"+responseStr+"］");
		}
	}

	/**
	 * 解析放款结果，更新计划表，日志表
	 */
	private void singleAnalysisRemittanceResult(
			String remittanceApplicationUuid,
			List<QueryStatusResult> queryStatusResults) {
		Map<String, List<QueryStatusResult>> queryStatusResultsMap = queryStatusResults.stream().collect(Collectors.groupingBy(QueryStatusResult::getTransactionUuid));
				
		List<String> plans = getRemittancePlanUuidsInProcessingBy(remittanceApplicationUuid);
		List<String> notInProcessingPlanUuids = getRemittancePlanUuidsNotInProcessingBy(remittanceApplicationUuid);
		
		List<String> isFinishedPlanUuis = new ArrayList<String>();
		for (String planUuid : plans) {
			boolean isFinish = false;
			if(queryStatusResultsMap.containsKey(planUuid)) {
				isFinish = updateRemittanceInfoWhenRemittancePlanFound(planUuid, queryStatusResultsMap.get(planUuid));
			}else {
				isFinish = updateRemittanceInfoWhenRemittancePlanNotFound(planUuid);
			}
			if(isFinish) {
				isFinishedPlanUuis.add(planUuid);
			}
		}
		
		logger.info("remittanceTask:updateRemittanceActualCount start:remittanceApplicationUuid:["+remittanceApplicationUuid+"]");
		updateRemittanceActualCount(isFinishedPlanUuis, remittanceApplicationUuid ,notInProcessingPlanUuids);
		logger.info("remittanceTask:updateRemittanceActualCount end:remittanceApplicationUuid:["+remittanceApplicationUuid+"]");
		
		if(isFinishedPlanUuis.containsAll(plans)) {
			logger.info("#放款结果同步开始，申请号［"+remittanceApplicationUuid+"］");
			syncRemittanceStatusAfterNoRemiitancePlanInProcessing(remittanceApplicationUuid);
		}
	}

	private void updateRemittanceActualCount(List<String> isFinishedPlanUuis, String remittanceApplicationUuid, List<String> notInProcessingPlanUuids) {
		
		if(CollectionUtils.isEmpty(isFinishedPlanUuis)){
			return ;
		}
		
		List<String> allFinishedPlanUuids = new ArrayList<>();
		allFinishedPlanUuids.addAll(isFinishedPlanUuis);
		if(CollectionUtils.isNotEmpty(notInProcessingPlanUuids)){
			allFinishedPlanUuids.addAll(notInProcessingPlanUuids);
		}
		
		int application_actual_count = updateDetailActualCount(allFinishedPlanUuids);
		
		updateApplicationActualCount(remittanceApplicationUuid, application_actual_count);
	}


	private int updateDetailActualCount(List<String> isFinishedPlanUuis) {
		List<RemittancePlan> remittancePlanList = new ArrayList<>();
		for (String remittancePlanUuid : isFinishedPlanUuis) {
			remittancePlanList.add(iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid));
		}
		Map<String, List<RemittancePlan>> groups = remittancePlanList.stream().collect(Collectors.groupingBy(RemittancePlan::getRemittanceApplicationDetailUuid));
		int application_actual_count = 0;
		for (String remittanceApplicationDetailUuid: groups.keySet()) {
			String sql = "update t_remittance_application_detail set actual_count =:actualCount where remittance_application_detail_uuid =:remittanceApplicationDetailUuid";
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("remittanceApplicationDetailUuid", remittanceApplicationDetailUuid);
			parameters.put("actualCount", groups.get(remittanceApplicationDetailUuid).size());
			genericDaoSupport.executeSQL(sql, parameters);
			
			RemittanceApplicationDetail remittanceApplicationDetail = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByUuid(remittanceApplicationDetailUuid);
			if(remittanceApplicationDetail.getTotalCount() == groups.get(remittanceApplicationDetailUuid).size()){
				application_actual_count++;
			}
			
		}
		return application_actual_count;
	}


	private void updateApplicationActualCount(String remittanceApplicationUuid, int application_actual_count) {
		String application_sql = "UPDATE t_remittance_application SET actual_count=:actualCount,version_lock =:newVersion WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("remittanceApplicationUuid", remittanceApplicationUuid);
		parameters.put("actualCount", application_actual_count);
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, application_sql, parameters);
	}


	/**
	 * 同步放款申请表，明细表状态
	 */
	private void syncRemittanceStatusAfterNoRemiitancePlanInProcessing(String remittanceApplicationUuid) {
		List<RemittancePlan> remittancePlanList = iRemittancePlanService.getRemittancePlanListBy(remittanceApplicationUuid);
		if(CollectionUtils.isEmpty(remittancePlanList)) {
			return;
		}
		//同步明细表
		Map<String, List<RemittancePlan>> groups = remittancePlanList.stream().collect(Collectors.groupingBy(RemittancePlan::getRemittanceApplicationDetailUuid));
		List<Map<String, Object>> paramsForDetails = new ArrayList<Map<String,Object>>();
		for (Entry<String, List<RemittancePlan>> groupsEntry : groups.entrySet()) {
			Map<String, Object> params = syncRemittanceDetailByAnalysisPlans(groupsEntry);
			paramsForDetails.add(params);
		}
		//同步申请表
		syncRemittanceApplicationByAnalysisDetails(remittanceApplicationUuid, paramsForDetails);
	}
	
	/**
	 * 解析放款明细，同步放款申请
	 * @param paramsForDetails 明细参数
	 */
	private void syncRemittanceApplicationByAnalysisDetails(String remittanceApplicationUuid, List<Map<String, Object>> paramsForDetails) {
		BigDecimal actualTotalAmount = BigDecimal.ZERO;
		List<ExecutionStatus> executionStatusList = new ArrayList<ExecutionStatus>();
		for (Map<String, Object> map : paramsForDetails) {
			//合计金额
			BigDecimal detailActualTotalAmount = (BigDecimal) map.getOrDefault("actualTotalAmount", BigDecimal.ZERO);
			actualTotalAmount = actualTotalAmount.add(detailActualTotalAmount);
			//评估状态
			ExecutionStatus executionStatus = EnumUtil.fromOrdinal(ExecutionStatus.class ,(Integer) map.get("executionStatus"));
			executionStatusList.add(executionStatus);
		}

		BigDecimal planTotalAmount = getPlannedTotalAmount(remittanceApplicationUuid);

		ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList,planTotalAmount,actualTotalAmount);

		Integer actualNotifyNumber = getActualNotifyNumber(remittanceApplicationUuid);

		int planNotifyNumber = YX_NOTIFY_NUMBER + (actualNotifyNumber == null ? 0 : actualNotifyNumber);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actualTotalAmount", actualTotalAmount);
		params.put("executionStatus", finalStatus.ordinal());
		params.put("planNotifyNumber", planNotifyNumber);
		params.put("lastModifiedTime", new Date());
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		
		//设定放款生效日期，取放款申请单下，最早发送到对端且执行成功的线上代付单的对端接收时间
		Date oppositeReceiveDate = getRemittanceEffectiveDate(remittanceApplicationUuid);
		params.put("oppositeReceiveDate", oppositeReceiveDate);
		
		this.genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application "
				+ "SET actual_total_amount =:actualTotalAmount, "
				+ " execution_status =:executionStatus, "
				+ " plan_notify_number =:planNotifyNumber, "
				+ " opposite_receive_date =:oppositeReceiveDate, "
				+ " last_modified_time =:lastModifiedTime "
				+ "WHERE remittance_application_uuid =:remittanceApplicationUuid",
						params);
	}

	private Integer getActualNotifyNumber(String remittanceApplicationUuid){
		if(StringUtils.isBlank(remittanceApplicationUuid))
			return null;
		String sql = "select actual_notify_number from t_remittance_application where remittance_application_uuid =:remittanceApplicationUuid";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		List<Integer> actualNotifyNumbers = this.genericDaoSupport.queryForSingleColumnList(sql,params,Integer.class);
		if(CollectionUtils.isNotEmpty(actualNotifyNumbers) && actualNotifyNumbers.size()==1){
			return actualNotifyNumbers.get(0);
		}
		return null;
	}
	
	/**
	 * 获取放款生效日期，取放款申请单下，最早发送到对端且执行成功的线上代付单的对端接收时间
	 * @param remittanceApplicationUuid 放款申请单uuid
	 */
	private Date getRemittanceEffectiveDate(String remittanceApplicationUuid) {
		if(StringUtils.isBlank(remittanceApplicationUuid)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("executionStatus", ExecutionStatus.SUCCESS.ordinal());
		
		List<Date> minDates = this.genericDaoSupport.queryForSingleColumnList(
				"SELECT MIN(opposite_receive_date) "
				+ " FROM t_remittance_plan_exec_log "
				+ " WHERE remittance_application_uuid =:remittanceApplicationUuid "
				+ " AND execution_status =:executionStatus", params, Date.class);
		if(CollectionUtils.isNotEmpty(minDates)) {
			return minDates.get(0);
		}
		return null;
	}

	/**
	 * 解析放款计划，同步放款明细
	 * @param groupsEntry
	 * @return
	 */
	private Map<String, Object> syncRemittanceDetailByAnalysisPlans(
			Entry<String, List<RemittancePlan>> groupsEntry) {
		String detailUuid = groupsEntry.getKey();
		List<RemittancePlan> planList = groupsEntry.getValue();
		List<ExecutionStatus> executionStatusList = planList.stream().map(rp->rp.getExecutionStatus()).collect(Collectors.toList());
		
		//获取 放款实际成功金额，交易完成时间
		List<RemittancePlan> creditSuccessPlanList = planList.stream().filter(rp->rp.isCreditSuccess()).collect(Collectors.toList());
		BigDecimal actualTotalAmount = BigDecimal.ZERO;
		Date completePaymentDate = null;
		for (RemittancePlan creditSuccessPlan : creditSuccessPlanList) {
			actualTotalAmount = actualTotalAmount.add(creditSuccessPlan.getActualTotalAmount());
			completePaymentDate = creditSuccessPlan.getMaxCompletePaymentDate(completePaymentDate);
		}
		
		List<String> executionRemarks = planList.stream()
				.filter(rp -> rp.isCredit()).map(rp -> rp.getExecutionRemark())
				.collect(Collectors.toList());

		BigDecimal planAmount = getDetailPlanTotalAmount(detailUuid);

		//获取 综合状态
		ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList, planAmount, actualTotalAmount);
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actualTotalAmount", actualTotalAmount);
		params.put("executionStatus", finalStatus.ordinal());
		params.put("executionRemark", String.join(",", executionRemarks));
		params.put("completePaymentDate", completePaymentDate);
		params.put("lastModifiedTime", new Date());
		params.put("detailUuid", detailUuid);

		this.genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application_detail "
				+ "SET actual_total_amount =:actualTotalAmount, "
				+ " execution_status =:executionStatus, "
				+ " execution_remark =:executionRemark, "
				+ " complete_payment_date =:completePaymentDate, "
				+ " last_modified_time =:lastModifiedTime "
				+ "WHERE remittance_application_detail_uuid =:detailUuid",
						params);
		
		return params;
	}

	private BigDecimal getDetailPlanTotalAmount(String detailUuid){
		if(StringUtils.isBlank(detailUuid))
			return null;
		String sql = "select planned_total_amount from t_remittance_application_detail where remittance_application_detail_uuid =:detailUuid";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("detailUuid", detailUuid);

		List<BigDecimal> plannedTotalAmounts = this.genericDaoSupport.queryForSingleColumnList(sql, params, BigDecimal.class);
		if(CollectionUtils.isNotEmpty(plannedTotalAmounts) && plannedTotalAmounts.size()==1){
			return plannedTotalAmounts.get(0);
		}
		return null;
	}
	
	/**
	 * 评估最终结果
	 * @param executionStatusList 状态列表
	 */
	private ExecutionStatus doEvaluateFinalStatus(List<ExecutionStatus> executionStatusList,BigDecimal planAmount,BigDecimal actualAmount) {
		ExecutionStatus[] statusListForAllMatch = {ExecutionStatus.SUCCESS, ExecutionStatus.FAIL, ExecutionStatus.ABANDON, ExecutionStatus.ABNORMAL};
		//所有成功，失败，或者撤销
		for (ExecutionStatus executionStatus : statusListForAllMatch) {
			boolean isAllMatch = executionStatusList.stream().allMatch(s-> s == executionStatus);
			if(isAllMatch) {
				return executionStatus;
			}
		}

		boolean onlySuccessAndAbandon = executionStatusList.stream()
			.filter(a->a != ExecutionStatus.SUCCESS)
			.filter(a->a != ExecutionStatus.ABANDON)
			.count() == 0;

		if(onlySuccessAndAbandon && AmountUtils.equals(planAmount, actualAmount)){
			return ExecutionStatus.SUCCESS;
		}

		//部分成功
		boolean isPartSuccess = executionStatusList.stream().anyMatch(s-> s == ExecutionStatus.SUCCESS);

		if(isPartSuccess) {
			return ExecutionStatus.ABNORMAL;
		}
		
		return ExecutionStatus.FAIL;
	}

	private BigDecimal getPlannedTotalAmount(String remittanceApplicationUuid){
		if(StringUtils.isBlank(remittanceApplicationUuid))
			return null;
		String sql="select planned_total_amount from t_remittance_application where remittance_application_uuid =:remittanceApplicationUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("remittanceApplicationUuid",remittanceApplicationUuid);
		List<BigDecimal> plannedTotalAmounts = genericDaoSupport.queryForSingleColumnList(sql, params, BigDecimal.class);
		if(CollectionUtils.isNotEmpty(plannedTotalAmounts) && plannedTotalAmounts.size()==1){
			return plannedTotalAmounts.get(0);
		}
		return null;
	}
	
	/**
	 * 根据查询结果，更新放款计划状态
	 * @param remittancePlanUuid 放款计划uuid
	 */
	private boolean updateRemittanceInfoWhenRemittancePlanFound(String remittancePlanUuid, List<QueryStatusResult> queryStatusResults) {
		if(StringUtils.isEmpty(remittancePlanUuid) || CollectionUtils.isEmpty(queryStatusResults)) {
			return false;
		}
		boolean isAllFinished = queryStatusResults.stream().allMatch(qsr -> qsr.isFinish());
		if(!isAllFinished) {
			loopUpdateRemittanceExecLogWhenOppositeProcessing(queryStatusResults, remittancePlanUuid);
			return false;
		}
		
		//最新的一条线上代付单
		String latestExecReqNo = iRemittancePlanExecLogService.getLatestRemittancePlanExecLogExecReqNo(remittancePlanUuid);
		if(StringUtils.isEmpty(latestExecReqNo)) {
			return false;
 		} 
		loopUpdateRemittancePlanAndExecLogWhenTransactionFinished(queryStatusResults, remittancePlanUuid, latestExecReqNo);
		
		return true;
	}
	
	private void loopUpdateRemittanceExecLogWhenOppositeProcessing(List<QueryStatusResult> queryStatusResults, String remittancePlanUuid) {
		for (QueryStatusResult queryStatusResult : queryStatusResults) {
			Integer executionStatus = queryStatusResult.convertToRemittanceExecutionStatus();
			Integer transactionRecipient = queryStatusResult.convertToTransactionRecipient();
			if(executionStatus == null || transactionRecipient == null) {
				continue;
			}
			if(!queryStatusResult.isFinish() && transactionRecipient == TransactionRecipient.OPPOSITE.ordinal()) {
				updateRemittanceExecLogWhenOppositeProcessing(queryStatusResult, remittancePlanUuid, transactionRecipient);
			}
		}
	}
	
	private void loopUpdateRemittancePlanAndExecLogWhenTransactionFinished(List<QueryStatusResult> queryStatusResults, String remittancePlanUuid, String latestExecReqNo) {
		for (QueryStatusResult queryStatusResult : queryStatusResults) {
			Integer executionStatus = queryStatusResult.convertToRemittanceExecutionStatus();
			Integer transactionRecipient = queryStatusResult.convertToTransactionRecipient();
			if(executionStatus == null || transactionRecipient == null) {
				continue;
			}
			if(queryStatusResult.isFinish()) {
				updateRemittancePlanAndExecLogWhenTransactionFinished(queryStatusResult, remittancePlanUuid, executionStatus, latestExecReqNo, transactionRecipient);
			}
		}
	}

	/**
	 * 更新放款计划和执行日志，当交易完成时
	 * @param queryStatusResult 查询结果
	 * @param remittancePlanUuid 放款计划uuid
	 * @param executionStatus 交易状态
	 * @param latestExecReqNo 最新的执行日志号
	 */
	private void updateRemittancePlanAndExecLogWhenTransactionFinished(QueryStatusResult queryStatusResult, String remittancePlanUuid, Integer executionStatus, String latestExecReqNo, Integer transactionRecipient) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pgAccountName", queryStatusResult.getChannelAccountName());
		params.put("pgAccountNo", queryStatusResult.getChannelAccountNo());
		params.put("oppositeReceiveDate", queryStatusResult.getCommunicationLastSentTime());
		params.put("completePaymentDate", queryStatusResult.getBusinessSuccessTime());
		if(executionStatus == ExecutionStatus.SUCCESS.ordinal()) {
			params.put("actualTotalAmount", queryStatusResult.getTransactionAmount());
		}else {
			params.put("actualTotalAmount", "0.00");
		}
		params.put("executionStatus", executionStatus);
		params.put("transactionRecipient", transactionRecipient);
		params.put("processingStatus", ExecutionStatus.PROCESSING.ordinal());
		params.put("executionRemark", queryStatusResult.getBusinessResultMsg());
		params.put("channelSequenceNo", queryStatusResult.getChannelSequenceNo());
		params.put("planCreditCashFlowCheckNumber", PLAN_CREDIT_CASH_FLOW_CHECK_NUMBER);
		params.put("lastModifiedTime", new Date());
		
		params.put("remittancePlanUuid", remittancePlanUuid);
		
		String execReqNo = queryStatusResult.getSourceMessageUuid();
		String execRspNo = queryStatusResult.getTradeUuid();
		if(StringUtils.isNotEmpty(execReqNo)) {
			params.put("execReqNo", execReqNo);
			params.put("execRspNo", execRspNo);
			this.genericDaoSupport.executeSQL(
					"UPDATE t_remittance_plan_exec_log "
							+ " SET pg_account_name =:pgAccountName, "
							+ " pg_account_no =:pgAccountNo, "
							+ " opposite_receive_date =:oppositeReceiveDate, "
							+ " complete_payment_date =:completePaymentDate, "
							+ " actual_total_amount =:actualTotalAmount, "
							+ " execution_status =:executionStatus, "
							+ " transaction_recipient =:transactionRecipient, "
							+ " execution_remark =:executionRemark, "
							+ " transaction_serial_no =:channelSequenceNo, "
							+ " exec_rsp_no =:execRspNo, "
							+ " last_modified_time =:lastModifiedTime, "
							+ " plan_credit_cash_flow_check_number =:planCreditCashFlowCheckNumber "
							+ "WHERE remittance_plan_uuid =:remittancePlanUuid "
							+ "AND exec_req_no =:execReqNo "
							+ "AND execution_status =:processingStatus", params);
			if(execReqNo.equals(latestExecReqNo)) {
				this.genericDaoSupport.executeSQL(
						"UPDATE t_remittance_plan "
							+ " SET pg_account_name =:pgAccountName, "
							+ " pg_account_no =:pgAccountNo, "
							+ " complete_payment_date =:completePaymentDate, "
							+ " actual_total_amount =:actualTotalAmount, "
							+ " execution_status =:executionStatus, "
							+ " execution_remark =:executionRemark, "
							+ " transaction_serial_no =:channelSequenceNo, "
							+ " last_modified_time =:lastModifiedTime "
							+ "WHERE remittance_plan_uuid =:remittancePlanUuid "
							+ "AND execution_status =:processingStatus", params);
			}
		}
	}

	/**
	 * 更新放款执行日志，当交易进入到对端处理
	 * @param remittancePlanUuid 放款计划uuid
	 * @param transactionRecipient 交易接收方
	 */
	private void updateRemittanceExecLogWhenOppositeProcessing(
			QueryStatusResult queryStatusResult, String remittancePlanUuid,
			Integer transactionRecipient) {
		String execReqNo = queryStatusResult.getSourceMessageUuid();

		if(StringUtils.isNotEmpty(execReqNo)) {
			String execRspNo = queryStatusResult.getTradeUuid();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("transactionRecipient", transactionRecipient);
			params.put("execRspNo", execRspNo);
			params.put("lastModifiedTime", new Date());
			params.put("remittancePlanUuid", remittancePlanUuid);
			params.put("execReqNo", execReqNo);
			params.put("transactionRecipientLocal", TransactionRecipient.LOCAL.ordinal());
			
			this.genericDaoSupport.executeSQL(
				"UPDATE t_remittance_plan_exec_log "
						+ " SET transaction_recipient =:transactionRecipient, "
						+ " exec_rsp_no =:execRspNo, "
						+ " last_modified_time =:lastModifiedTime "
						+ "WHERE remittance_plan_uuid =:remittancePlanUuid"
						+ " AND exec_req_no =:execReqNo "
						+ " AND transaction_recipient =:transactionRecipientLocal", params);
		}
	}
	
	/**
	 * 放款计划查询不存在，更新状态
	 * @param remittancePlanUuid 放款计划uuid
	 */
	private boolean updateRemittanceInfoWhenRemittancePlanNotFound(String remittancePlanUuid) {
		if(StringUtils.isEmpty(remittancePlanUuid)) {
			return false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("executionStatus", ExecutionStatus.FAIL.ordinal());
		params.put("executionRemark", "无法查询到该交易！");
		params.put("lastModifiedTime", new Date());
		params.put("remittancePlanUuid", remittancePlanUuid);
		
		this.genericDaoSupport.executeSQL("UPDATE t_remittance_plan_exec_log SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_plan_uuid =:remittancePlanUuid", params);
		this.genericDaoSupport.executeSQL("UPDATE t_remittance_plan SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_plan_uuid =:remittancePlanUuid", params);
		return true;
	}
	
	/**
	 * 查询放款结果（回调通知用）
	 * 结构：
	 * paidDetails : [
	 * 		{
	 * 			"DetailNo" : "", //明细编号
	 * 			"Status" : "", //状态 0:未回调，1:成功，2:失败，3：根据业务不执行，4:异常
	 * 			"Result" : "", //放款结果
	 * 			"BankSerialNo" : "", //银行流水号列表
	 * 			"ActExcutedTime" : "", //实际完成时间
	 * 		}
	 * ]
	 */
	@Override
	public String getRemittanceResultForNotification(RemittanceApplication remittanceApplication, String remittanceApplicationUuid) {
		String sql ="SELECT "
				+ "`business_record_no` AS 'DetailNo', "
				+ "CASE execution_status "
				+ "WHEN 2 THEN 1 "
				+ "WHEN 3 THEN 2 "
				+ "WHEN 4 THEN 4 "
				+ "WHEN 5 THEN 3 "
				+ "END AS 'Status',"
				+ "`execution_remark` AS 'Result', "
				+ "("
				+ " SELECT"
				+ "   GROUP_CONCAT(trp.transaction_serial_no) "
				+ " FROM "
				+ "   `t_remittance_plan` trp "
				+ " WHERE "
				+ "   trp.remittance_application_detail_uuid = trad.remittance_application_detail_uuid "
				+ "   AND trp.execution_status =:executionStatus "
				+ "   AND trp.transaction_type =:transactionType "
				+ ") AS 'BankSerialNo', "
				+ "`complete_payment_date` AS 'ActExcutedTime' "
				+ "FROM "
				+ "`t_remittance_application_detail` trad "
				+ "WHERE "
				+ "trad.remittance_application_uuid =:remittanceApplicationUuid";
		
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("executionStatus", ExecutionStatus.SUCCESS.ordinal());
				params.put("transactionType", com.zufangbao.sun.yunxin.entity.remittance.AccountSide.CREDIT.ordinal());
				params.put("remittanceApplicationUuid", remittanceApplicationUuid);
				
		List<Map<String, Object>> paidDetails = this.genericDaoSupport.queryForList(sql, params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("RequestId", UUID.randomUUID().toString());
		result.put("ReferenceId", remittanceApplication.getRequestNo());
		result.put("UniqueId", remittanceApplication.getContractUniqueId());
		result.put("PaidDetails", paidDetails);
		
		return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 查询放款结果（批量回调通知用）
	 * 结构：
	 * paidDetails : [
	 * 		{
	 * 			"DetailNo" : "", //明细编号
	 * 			"Status" : "", //状态 0:未回调，1:成功，2:失败，3：根据业务不执行，4:异常
	 * 			"Result" : "", //放款结果
	 * 			"BankSerialNo" : "", //银行流水号列表
	 * 			"ActExcutedTime" : "", //实际完成时间
	 * 		}
	 * ]
	 */
    @Override
	public String getRemittanceResultsForBatchNotice(RemittanceSqlModel remittanceSqlMode, List<String> remittanceApplicationUuids) {
		if(CollectionUtils.isEmpty(remittanceApplicationUuids)) {
			return StringUtils.EMPTY;
		}
		
		String sql ="SELECT "
				+ "`remittance_application_uuid` AS 'RemmittanceApplicationUuid', "
				+ "`business_record_no` AS 'DetailNo', "
				+ "CASE execution_status "
				+ "WHEN 2 THEN 1 "
				+ "WHEN 3 THEN 2 "
				+ "WHEN 4 THEN 4 "
				+ "WHEN 5 THEN 3 "
				+ "END AS 'Status',"
				+ "`execution_remark` AS 'Result', "
				+ "("
				+ " SELECT"
				+ "   GROUP_CONCAT(trp.transaction_serial_no) "
				+ " FROM "
				+ "   `t_remittance_plan` trp "
				+ " WHERE "
				+ "   trp.remittance_application_detail_uuid = trad.remittance_application_detail_uuid "
				+ "   AND trp.execution_status =:executionStatus "
				+ "   AND trp.transaction_type =:transactionType "
				+ ") AS 'BankSerialNo', "
				+ "`complete_payment_date` AS 'ActExcutedTime', "
				+ "`last_modified_time` AS 'StatusChangeTime' "
				+ "FROM "
				+ "`t_remittance_application_detail` trad "
				+ "WHERE "
				+ "trad.remittance_application_uuid IN (:remittanceApplicationUuids)";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("executionStatus", ExecutionStatus.SUCCESS.ordinal());
		params.put("transactionType", com.zufangbao.sun.yunxin.entity.remittance.AccountSide.CREDIT.ordinal());
		params.put("remittanceApplicationUuids", remittanceApplicationUuids);
		
		List<Map<String, Object>> allPaidDetails = this.genericDaoSupport.queryForList(sql, params);
		
		//以放款申请uuid分组，放款明细
		Map<String, List<Map<String, Object>>> groupedPaidDetails = allPaidDetails.stream().collect(Collectors.groupingBy(map -> (String) map.get("RemmittanceApplicationUuid")));

		List<Map<String, Object>> paidNoticInfos = new ArrayList<Map<String,Object>>();
		
		String remittanceApplicationUuid = remittanceSqlMode.getRemittanceApplicationUuid();
		if(groupedPaidDetails.containsKey(remittanceApplicationUuid)) {
				Map<String, Object> paidNoticInfo = new HashMap<String, Object>();
				paidNoticInfo.put("UniqueId", remittanceSqlMode.getContractUniqueId());
				paidNoticInfo.put("ReferenceId", remittanceSqlMode.getRequestNo());
				//剔除多余字段RemmittanceApplicationUuid
				List<Map<String, Object>> paidDetails = groupedPaidDetails.get(remittanceApplicationUuid);
				for (Map<String, Object> paidDetail : paidDetails) {
					paidDetail.remove("RemmittanceApplicationUuid");
				}
				paidNoticInfo.put("PaidDetails", paidDetails);
				paidNoticInfos.add(paidNoticInfo);
		}
			
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("RequestId", UUID.randomUUID().toString());
		result.put("PaidNoticInfos", paidNoticInfos);
		
		return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}

	@Override
	public List<String> getRemittanceApplicationUuidsInOppositeProcessing(Date queryStartDate, int limit, Date queryStatusDelay) {
		
		StringBuffer buffer = new StringBuffer(" SELECT remittance_application_uuid FROM t_remittance_application WHERE last_modified_time >=:queryStartDate AND execution_status =:executionStatus AND transaction_recipient =:transactionRecipient AND create_time < :queryStatusDelay ");
		buffer.append(" limit ");
		buffer.append(limit);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("queryStartDate", queryStartDate);
		params.put("executionStatus", ExecutionStatus.PROCESSING.ordinal());
		params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());
		params.put("queryStatusDelay", queryStatusDelay);
		
		return this.genericDaoSupport.queryForSingleColumnList(buffer.toString(), params, String.class);
	}
	
	@Override
	public RemittanceApplication getRemittanceApplicationUuidsInOppositeProcessing(String remittanceApplicationUuid) {
		String queryUuidsSql = "SELECT remittance_application_uuid FROM t_remittance_application WHERE remittance_application_uuid=:remittanceApplicationUuid AND execution_status =:executionStatus AND transaction_recipient =:transactionRecipient";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("executionStatus", ExecutionStatus.PROCESSING.ordinal());
		params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());
		params.put("remittanceApplicationUuid",remittanceApplicationUuid);
		List<RemittanceApplication> remittanceApplications = this.genericDaoSupport.queryForList(queryUuidsSql, params, RemittanceApplication.class,0,1);
		if(CollectionUtils.isEmpty(remittanceApplications)){
			return null;
		}
		return remittanceApplications.get(0);
	}

	@Override
	public List<String> getRemittancePlanUuidsInProcessingBy(
			String remittanceApplicationUuid) {
		if(StringUtils.isEmpty(remittanceApplicationUuid)) {
			return Collections.emptyList();
		}
		String queryUuidsSql = "SELECT remittance_plan_uuid FROM t_remittance_plan WHERE remittance_application_uuid =:remittanceApplicationUuid AND execution_status =:executionStatus";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("executionStatus", ExecutionStatus.PROCESSING.ordinal());
		
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}

	@Override
	public List<String> getRemittancePlanUuidsNotInProcessingBy(
			String remittanceApplicationUuid) {
		if(StringUtils.isEmpty(remittanceApplicationUuid)) {
			return Collections.emptyList();
		}
		String queryUuidsSql = "SELECT remittance_plan_uuid FROM t_remittance_plan WHERE remittance_application_uuid =:remittanceApplicationUuid AND execution_status!=:executionStatus";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("executionStatus", ExecutionStatus.PROCESSING.ordinal());
		
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}
	
	@Override
	public List<String> getWaitingNoticeRemittanceApplication() {
		String queryUuidsSql = "SELECT remittance_application_uuid "
				+ "FROM t_remittance_application "
				+ "WHERE execution_status IN(:executionStatusList) "
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());
		
		params.put("executionStatusList", finishedExecutionStatusList);
		
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}
	

	@Override
	public List<String> getWaitingNoticeRemittanceApplicationV2(int limit, Date queryStartDate, Date notifyOutlierDelay) {
		
		String queryUuidsSql = "SELECT remittance_application_uuid "
				+ " FROM t_remittance_application "
				+ " WHERE last_modified_time >=:queryStartDate "
				+ " AND execution_status IN(:executionStatusList) "
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL AND create_time < :notifyOutlierDelay "
				+ " LIMIT :pageSize";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABNORMAL.ordinal());

		params.put("queryStartDate", queryStartDate);
		params.put("executionStatusList", finishedExecutionStatusList);

		params.put("pageSize", limit);
		params.put("notifyOutlierDelay", notifyOutlierDelay);
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}
	
	@Override
	public List<String> getWaitingNoticeRemittanceApplicationV3(int pageSize, Date queryStartDate, Date notifyOutlierDelay) {
		
		String queryUuidsSql = "SELECT remittance_application_uuid"
				+ "FROM t_remittance_application "
				+ "WHERE last_modified_time >=:queryStartDate"
				+ " AND execution_status IN(:executionStatusList) "
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL  AND create_time < :notifyOutlierDelay "
				+ " LIMIT :pageSize";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABNORMAL.ordinal());

		params.put("queryStartDate", queryStartDate);
		params.put("executionStatusList", finishedExecutionStatusList);
		params.put("pageSize", pageSize);
		params.put("notifyOutlierDelay", notifyOutlierDelay);
		
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RemittanceApplication> getWaitingNoticeRemittanceApplicationBy( int pageSize, Date queryStartDate) {
		
		String queryUuidsSql = "SELECT * "
				+ "FROM t_remittance_application "
				+ "WHERE last_modified_time >=:queryStartDate"
				+ " AND execution_status IN(:executionStatusList) "
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL "
				+ " LIMIT :pageSize";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABNORMAL.ordinal());

		params.put("queryStartDate", queryStartDate);
		params.put("executionStatusList", finishedExecutionStatusList);
		params.put("pageSize", pageSize);
		
		return this.genericDaoSupport.searchForList(queryUuidsSql, params);
	}


	@Override
	public RemittanceApplication getWaitingNoticeRemittanceApplicationBy(String remittanceApplicationUuid){
		if(StringUtils.isBlank(remittanceApplicationUuid)) {
			return null;
		}
		
		String queryUuidsSql = "SELECT * "
				+ " FROM t_remittance_application "
				+ " WHERE execution_status IN(:executionStatusList) "
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL "
				+ " AND remittance_application_uuid =:remittance_application_uuid ";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());
		finishedExecutionStatusList.add(ExecutionStatus.ABNORMAL.ordinal());

		params.put("executionStatusList", finishedExecutionStatusList);
		params.put("remittance_application_uuid", remittanceApplicationUuid);
		
		List<RemittanceApplication> remittanceApplications= this.genericDaoSupport.queryForList(queryUuidsSql, params, RemittanceApplication.class,0,1);
		if(CollectionUtils.isEmpty(remittanceApplications)) {
			return null;
		}else {
			return remittanceApplications.get(0);
		}
		
	}
	@Override
	public void execSingleNotifyForRemittanceApplication(
			String remittanceApplicationUuid) {
		String oppositeKeyWord = "[requestNo=" +remittanceApplicationUuid +"]";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord);

		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
		if(remittanceApplication == null || StringUtils.isEmpty(remittanceApplication.getNotifyUrl())) {
			return;
		}
		String notifyUrl = remittanceApplication.getNotifyUrl();
		String content = getRemittanceResultForNotification(remittanceApplication, remittanceApplicationUuid);
		
		Map<String, String> headerParams = buildHeaderParamsForNotifyRemittanceResult(content);
		
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+"begin to send post");
		Result result = HttpClientUtils.executePostRequest(notifyUrl, content, headerParams);
		
		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		if(result.isValid()) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("receive notify result success 内容［"+content+"］，结果［"+responseStr+"］"));
			this.genericDaoSupport.executeSQL(
					"UPDATE t_remittance_application "
					+ "SET actual_notify_number = (actual_notify_number + 1) "
					+ "WHERE remittance_application_uuid =:remittanceApplicationUuid", "remittanceApplicationUuid", remittanceApplicationUuid);
			return;
		}
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord + GloableLogSpec.RawData("放款结果回调，回调失败，放款申请号［"+remittanceApplicationUuid+"］，结果［"+responseStr+"］"));
	}
	

	public void updateRemittanceExecuteIfFailed(int actualNotifyNumber,int planNotifyNumber,String oppositeKeyWord, List<String> remittanceApplicationUuids){

		this.genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application "
						+ "SET actual_notify_number = (actual_notify_number + 1) "
						+ "WHERE remittance_application_uuid IN (:remittanceApplicationUuids)", "remittanceApplicationUuids", remittanceApplicationUuids);
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("处理除了响应成功的情况,如响应超时,异常,响应失败等等情况"));
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec.RawData("自增后actual_notify_number:["+ (actualNotifyNumber + 1) +"]操作"));


	}

	@Override
	public Map<String, String> buildHeaderParamsForNotifyRemittanceResult(
			String content) {
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merid", YX_API_MERID);
		headerParams.put("secretkey", YX_API_SECRETKEY);
		
		String privateKey = dictionaryService.getPlatformPrivateKey();
		String signedMsg = ApiSignUtils.rsaSign(content, privateKey);
		
		headerParams.put("signedmsg", signedMsg);
		return headerParams;
	}
	
	@Override
	public List<RemittanceApplication> getRemittanceApplicationsBy(String contractUniqueId, String contractNo) {
		if(!StringUtils.isEmpty(contractUniqueId)) {
			return iRemittanceApplicationService.getRemittanceApplicationsByUniqueId(contractUniqueId);
		}
		
		if(!StringUtils.isEmpty(contractNo)) {
			return iRemittanceApplicationService.getRemittanceApplicationsByContractNo(contractNo);
		}
		return Collections.emptyList();
	}

	@Override
	public List<TradeSchedule> revokeFailRemittancePlanThenSaveInfo(ModifyRemittanceApplicationModel model, String ip, String operator){

		List<RemittanceDetail> remittanceDetails =model.getRemittanceDetailList();
		//放款明细内，银行编号校验
		validateRemittanceDetailsBankCode(remittanceDetails);

		String requestNo = model.getRequestNo();
		//请求编号重复性校验
		if(modifyRemittanceApplicationLogService.isExist(requestNo)){
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}

		String productCode = model.getFinancialProductCode();
		// 信托产品代码存在性校验
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productCode);
		if (financialContract == null) {
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}

		// 信托合同权限校验不允许变更放款订单
		if (!allowModifyRemittanceApplication(financialContract.getFinancialContractUuid())) {
			throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NO_MODIFYING_REPAYMENT_APPLICATION);
		}

		RemittanceApplication application = null;
		String remittanceId = model.getRemittanceId();
		String remittanceRequestNo = model.getRemittanceTradeNo();
		if(StringUtils.isNotEmpty(remittanceId)){
			application = iRemittanceApplicationService.getRemittanceApplicationByRemittanceId(remittanceId);
		}else{
			application = iRemittanceApplicationService.getRemittanceApplicationByRequestNo(remittanceRequestNo);
		}
		if (application == null) { // 不存在商户订单号或放款请求编号对应的计划订单
			throw new ApiException(ApiResponseCode.NOT_EXIST_REQUEST_NO);
		}
		if(StringUtils.isNotEmpty(remittanceId) && StringUtils.isNotEmpty(remittanceRequestNo)){
			if (!Objects.equals(application.getRequestNo(), remittanceRequestNo)) {
				throw new ApiException(ApiResponseCode.NOT_EXIST_REQUEST_NO);
			}
		}

		model.setRemittanceApplicationUuid(application.getRemittanceApplicationUuid());

		// 放款类型为优先级放款予以受理，一放一扣暂不支持，返回变更受理失败
		RemittanceStrategy remittanceStrategy = application.getRemittanceStrategy();
		if (remittanceStrategy != RemittanceStrategy.MULTIOBJECT) {
			throw new ApiException(ApiResponseCode.MODIFY_ACCEPTANCE_FAILED);
		}

		String contractUniqueId = application.getContractUniqueId();
		// 放款黑名单中存在该贷款合同uniqueId
		if(remittanceBlackListService.existUniqueId(contractUniqueId)) {
			throw new ApiException(ApiResponseCode.CONTRACT_UNIQUED_ID_EXIST_IN_REMITTANCE_BLACK_LIST);
		}

		// 该贷款合同下存在处理中或已成功的放款请求
//		if(iRemittanceApplicationService.existsProcessingOrSuccessedRemittanceApplication(contractUniqueId)) {
//			throw new ApiException(ApiResponseCode.HAS_EXIST_PROCESSING_OR_SUCCESSED_REMITTANCE);
//		}

		// 计划订单中的任何明细正在和银行系统通信中（放款单处理中）不允许变更；
		List<RemittancePlan> remittancePlans = iRemittancePlanService.getRemittancePlanListBy(application.getRemittanceApplicationUuid());
		if (hasProcessingRemittancePlan(remittancePlans)) {
			throw new ApiException(ApiResponseCode.MODIFY_ACCEPTANCE_FAILED);
		}

		validateDetailAmount(application, remittancePlans, remittanceDetails);

		updateRemittancePlanFromFailToAbandon(application, remittancePlans);

		List<TradeSchedule> tradeSchedules = splitRemittancePlanByStrategy(model, application);

		for(TradeSchedule tradeSchedule:tradeSchedules){
			tradeSchedule.setNotifyUrl(jpmorganCallbackUrl);
		}
		
		saveRemittanceInfo(remittanceDetails, application, tradeSchedules, financialContract, operator);

		return tradeSchedules;
	}


	/**
	 * 通过策略拆分放款单
	 */
	private List<TradeSchedule> splitRemittancePlanByStrategy(ModifyRemittanceApplicationModel model, RemittanceApplication application) {
		RemittanceStrategy remittanceStrategy = application.getRemittanceStrategy();
		switch (remittanceStrategy) {
			case MULTIOBJECT:
				return remittanceStrategyFactory.buildTradeScheduleForStrategyMultiObject(model, application);
			case DEDUCT_AFTER_REMITTANCE:
				throw new ApiException(ApiResponseCode.MODIFY_ACCEPTANCE_FAILED);
			default:
				throw new ApiException(ApiResponseCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 放款明细内，银行编号校验
	 */
	private void validateRemittanceDetailsBankCode(List<RemittanceDetail> remittanceDetails) {
		Map<String, Bank> bankMap = bankService.getCachedBanks();
		for (RemittanceDetail remittanceDetail : remittanceDetails) {
			String bankCode = remittanceDetail.getBankCode();
			if (StringUtils.isEmpty(bankCode)) {
				String bankCardNo = remittanceDetail.getBankCardNo();
				if (StringUtils.isEmpty(bankCardNo)) {
					throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
				}
				Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
				bankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, bankCardNo);
				if (StringUtils.isEmpty(bankCode)) {
					throw new ApiException(ApiResponseCode.RECOGNIZE_BANK_BIN_FAILED);
				} else {
					remittanceDetail.setBankCode(bankCode);
				}
			}
			if(!bankMap.containsKey(bankCode)){
				throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
			}
		}
	}

	// 信托合同是否有变更权限
	private boolean allowModifyRemittanceApplication(String financialContractUuid){
		String allow = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,
			FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode());
		return StringUtils.equals(allow, FinancialContractConfigurationContentValue.ALLOW_MODIFY_REMITTANCE_APPLICATION);
	}

	private void updateRemittancePlanFromFailToAbandon(RemittanceApplication application, List<RemittancePlan> remittancePlans){
		if(application == null || CollectionUtils.isEmpty(remittancePlans)){
			return;
		}
		List<RemittancePlan> filtedRemittancePlans = remittancePlans.stream()
			      .filter(a->a.getExecutionStatus().ordinal() == ExecutionStatus.FAIL.ordinal())
			      .collect(Collectors.toList());
		
		List<String> remittancePlanUuids = filtedRemittancePlans.stream().map(RemittancePlan::getRemittancePlanUuid).collect(Collectors.toList());
			
		if(CollectionUtils.isEmpty(remittancePlanUuids))
			return;

		Date now =  new Date();

		String sql = "UPDATE t_remittance_plan "
			+ "SET execution_status =:abandonStatus, "
			+ "last_modified_time =:lastModifiedTime "
			+ "WHERE remittance_plan_uuid IN (:remittancePlanUuids) "
			+ "AND execution_status =:failStatus";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("abandonStatus", ExecutionStatus.ABANDON.ordinal());
		params.put("failStatus", ExecutionStatus.FAIL.ordinal());
		params.put("lastModifiedTime",now);
		params.put("remittancePlanUuids",remittancePlanUuids);
		this.genericDaoSupport.executeSQL(sql, params);

		List<String> applicationDetailUuids = new ArrayList<>();
		filtedRemittancePlans.forEach(a->{
			if (!applicationDetailUuids.contains(a.getRemittanceApplicationDetailUuid())) {
				applicationDetailUuids.add(a.getRemittanceApplicationDetailUuid());
			}});

		params.put("detailUuids",applicationDetailUuids);
			this.genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application_detail "
					+ "SET execution_status = :abandonStatus, "
					+ " last_modified_time =:lastModifiedTime "
					+ "WHERE remittance_application_detail_uuid IN (:detailUuids)",
				params);

		application.setExecutionStatus(ExecutionStatus.PROCESSING);
		application.setLastModifiedTime(now);
		iRemittanceApplicationService.update(application);
	}



	// 申请二次放款金额=计划放款金额-放款成功金额-放款中金额，若不等于则系统不受理此申请，或申请二次放款金额=0则系统不受理此申请；
	private void validateDetailAmount(RemittanceApplication application, List<RemittancePlan> remittancePlans, List<RemittanceDetail> remittanceDetails){
		BigDecimal amountInApplication = application.getPlannedTotalAmount();
		BigDecimal amountInDetails = getAmountInDetails(remittanceDetails);
		BigDecimal amountInFavour = getAmountInFavour(remittancePlans);
		BigDecimal amountInSuccess = getAmountInSuccess(remittancePlans);
		if (AmountUtils.equals(amountInDetails, BigDecimal.ZERO)
			|| !AmountUtils.equals(amountInDetails, amountInApplication.subtract(amountInSuccess).subtract(amountInFavour))) {
			throw new ApiException(ApiResponseCode.MODIFY_ACCEPTANCE_FAILED);
		}
	}


	// 明细总金额
	private BigDecimal getAmountInDetails(List<RemittanceDetail> remittanceDetails){
		if(CollectionUtils.isEmpty(remittanceDetails)){
			return BigDecimal.ZERO;
		}
		return remittanceDetails.stream()
			.map(RemittanceDetail::getBDAmount)
			.reduce(BigDecimal.ZERO,BigDecimal::add);
	}

	// 放款中金额
	private BigDecimal getAmountInFavour(List<RemittancePlan> remittancePlans){
		if(CollectionUtils.isEmpty(remittancePlans)){
			return BigDecimal.ZERO;
		}
		return remittancePlans.stream()
			.filter(a-> Arrays.asList(ExecutionStatus.CREATE, ExecutionStatus.PROCESSING, ExecutionStatus.ABNORMAL).contains(a.getExecutionStatus()))
			.map(RemittancePlan::getPlannedTotalAmount)
			.reduce(BigDecimal.ZERO,BigDecimal::add);
	}

	// 存在正在处理中的放款单
	private boolean hasProcessingRemittancePlan(List<RemittancePlan> remittancePlans) {
		return CollectionUtils.isEmpty(remittancePlans) || remittancePlans.stream()
			.filter(a -> ExecutionStatus.PROCESSING.ordinal() == a.getExecutionStatus().ordinal()).count() > 0;
	}

	// 放款成功金额
	private BigDecimal getAmountInSuccess(List<RemittancePlan> remittancePlans) {
		if (CollectionUtils.isEmpty(remittancePlans)) {
			return BigDecimal.ZERO;
		}
		return remittancePlans.stream()
			.filter(a -> Collections.singletonList(ExecutionStatus.SUCCESS).contains(a.getExecutionStatus()))
			.map(RemittancePlan::getPlannedTotalAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	private void saveRemittanceInfo(List<RemittanceDetail> remittanceDetails,RemittanceApplication application,
		List<TradeSchedule> tradeSchedules, FinancialContract financialContract,String creatorName) {

		String remittanceApplicationUuid = application.getRemittanceApplicationUuid();
		String contractUniqueId = application.getContractUniqueId();
		String contractNo = application.getContractNo();
		for (RemittanceDetail remittanceDetail : remittanceDetails) {
			RemittanceApplicationDetail remittanceApplicationDetail = remittanceDetail.convertToRemittanceApplicationDetail(remittanceApplicationUuid, financialContract.getFinancialContractUuid(), financialContract.getId(), creatorName);
			iRemittanceApplicationDetailService.save(remittanceApplicationDetail);
		}

		for (TradeSchedule tradeSchedule : tradeSchedules) {
			RemittancePlan remittancePlan = convertToRemittancePlan(tradeSchedule, remittanceApplicationUuid, financialContract.getFinancialContractUuid(), financialContract.getId(), contractUniqueId, contractNo, creatorName);
			iRemittancePlanService.save(remittancePlan);
			String execReqNo = tradeSchedule.getSourceMessageUuid();
			RemittancePlanExecLog remittancePlanExecLog = new RemittancePlanExecLog(remittancePlan, execReqNo);
			iRemittancePlanExecLogService.save(remittancePlanExecLog);
		}
		
		changeApplicationAndDetailTotalCounts(application);
		
		this.deleteAllObjectAndRelationCacheByApplicationUuid(remittanceApplicationUuid);
		
	}

	/**
	 *  根据remittanceApplication找出数据库中所有的details,plans,
	 *  根据查找出的集合的size设置相应的total_count;根据uuids构建相应的映射关系
	 * @param application
	 */
	//TODO params:RemittanceApplication、RemittanceApplicationDetailList
	//只需要更新params:RemittanceApplication的totalSize，更新RemittanceApplicationDetailList下所有的plansize，其他无关的RemittanceApplicationDetail不动
	private  void changeApplicationAndDetailTotalCounts(RemittanceApplication application){
		
		String applicationUuid = application.getRemittanceApplicationUuid();
		
		List<RemittanceApplicationDetail> details = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid(applicationUuid);
		
		application.setTotalCount(details.size());
		
		iRemittanceApplicationService.update(application);
		
		for (RemittanceApplicationDetail detail : details) {
			String detailUuid = detail.getRemittanceApplicationDetailUuid();
			List<RemittancePlan> plans = iRemittancePlanService.getRemittancePlanListByRemittanceApplicationDetailUuid(detailUuid);
			detail.setTotalCount(plans.size());
			iRemittanceApplicationDetailService.update(detail);
		}
		
	}
	
	
	@Override
	public void deleteAllObjectAndRelationCacheByApplicationUuid(String applicationUuid) {
		try {
			fastDataWithTemperatureHandler.delByKey(FastRemittanceApplicationEnum.REMITTANCE_APPLICATION_UUID, applicationUuid, true);
			remittanceRelationCacheService.delete(RemittanceRelation.RA_RD,applicationUuid);
			remittanceRelationCacheService.delete(RemittanceRelation.RA_RP,applicationUuid);

			List<RemittanceApplicationDetail> details = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid(applicationUuid);
			List<RemittancePlan> plans = iRemittancePlanService.getRemittancePlanListBy(applicationUuid);

			for (RemittanceApplicationDetail detail : details) {
				String detailUuid = detail.getRemittanceApplicationDetailUuid();
				remittanceRelationCacheService.delete(RemittanceRelation.RD_RP,detailUuid);

				fastDataWithTemperatureHandler.delByKey(FastRemittanceApplicationDetailEnum.REMITTANCE_APPLICATION_DETAIL_UUID, detailUuid, true);
			}

			for (RemittancePlan remittancePlan : plans) {
				fastDataWithTemperatureHandler.delByKey(FastRemittancePlanEnum.REMITTANCE_PLAN_UUID, remittancePlan.getRemittancePlanUuid(), true);
			}
		} catch (GiottoException e) {
			logger.error("deleteAllObjectAndRelationCacheByApplicationUuid error : " + ExceptionUtils.getFullStackTrace(e));
		}
		
	}
	
	@Override
	public void deleteAllObjectAndRelationCache(String remittancePlanUuid){
		RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
		deleteAllObjectAndRelationCache(remittancePlan);
	}
	
	/**
	 * 根据plan清除所有和该条plan关联对象的object和relation缓存 (plan,detail,application)
	 * @param remittancePlan
	 */
	@Override
	public void deleteAllObjectAndRelationCache(RemittancePlan remittancePlan){
		String remittanceApplicationUuid = remittancePlan.getRemittanceApplicationUuid();
		String remittanceApplicationDetailUuid = remittancePlan.getRemittanceApplicationDetailUuid();
		String remittancePlanUuid = remittancePlan.getRemittancePlanUuid();
		
		try {
			fastDataWithTemperatureHandler.delByKey(FastRemittanceApplicationEnum.REMITTANCE_APPLICATION_UUID, remittanceApplicationUuid, true);
			fastDataWithTemperatureHandler.delByKey(FastRemittanceApplicationDetailEnum.REMITTANCE_APPLICATION_DETAIL_UUID, remittanceApplicationDetailUuid, true);
			fastDataWithTemperatureHandler.delByKey(FastRemittancePlanEnum.REMITTANCE_PLAN_UUID, remittancePlanUuid, true);
		} catch (GiottoException e) {
			logger.error("cache_roll_back_fail : " + ExceptionUtils.getFullStackTrace(e));
		}

		remittanceRelationCacheService.delete(RemittanceRelation.RA_RD,remittanceApplicationUuid);
		remittanceRelationCacheService.delete(RemittanceRelation.RA_RP,remittanceApplicationUuid);
		remittanceRelationCacheService.delete(RemittanceRelation.RD_RP,remittanceApplicationDetailUuid);
		
	}
	
	
	/**
	 * 使用最新的context中的信息更新放款计划，放款执行日志，放款明细，放款申请．
	 */
	public void updateRemittanceInfoAfterInitContext(RemittanceContext remittanceContext){
		updateRemittancePlanAndExecLogAndCache(remittanceContext);
		updateRemittanceDetailAndCache(remittanceContext);
		updateRemittanceApplicationAndCache(remittanceContext);
	}
	private void updateRemittancePlanAndExecLogAndCache(RemittanceContext remittanceContext) {
		QueryStatusResult queryStatusResult = remittanceContext.getQueryStatusResult();
		
		FastRemittancePlan plan = remittanceContext.getFastRemittancePlan();
		
		Map<String, Object> params = buildParamsExecLogAndUpdatePlan(queryStatusResult, plan);

		if (plan.temperature() != DataTemperature.FROZEN) {
			throw new RuntimeException("plan 更新后 应该为 FROZEN");
		}
		
		updateExecLog(params);

		fastDataWithTemperatureHandler.updateData(plan);
		

	}


	private void updateExecLog(Map<String, Object> params) {
		this.genericDaoSupport.executeSQL("UPDATE t_remittance_plan_exec_log "
				+ " SET pg_account_name =:pgAccountName, " + " pg_account_no =:pgAccountNo, "
				+ " opposite_receive_date =:oppositeReceiveDate, " + " complete_payment_date =:completePaymentDate, "
				+ " actual_total_amount =:actualTotalAmount, " + " execution_status =:executionStatus, "
				+ " transaction_recipient =:transactionRecipient, " + " execution_remark =:executionRemark, "
				+ " transaction_serial_no =:transactionSerialNo, " + " exec_rsp_no =:execRspNo, "
				+ " last_modified_time =:lastModifiedTime, "
				+ " plan_credit_cash_flow_check_number =:planCreditCashFlowCheckNumber "
				+ "WHERE remittance_plan_uuid =:remittancePlanUuid " + "AND exec_req_no =:execReqNo "
				+ "AND execution_status =:processingStatus", params);
	}
	
	private void updateRemittanceDetailAndCache(RemittanceContext context) {
		FastRemittanceApplicationDetail detail = null;
		if(context.isAllPlanFinished()) {
			 detail = updateFastDetailStatus(context);
		}else {
			detail = context.getFastRemittanceApplicationDetail();
			detail.setVersionLock(UUID.randomUUID().toString());
			detail.setLastModifiedTime(new Date());
		}
		
		if (detail.temperature() != DataTemperature.FROZEN) {
			throw new RuntimeException("detail 更新后 应该为 FROZEN");
		}
		
		updateDetail(detail);
		
	}

	private void updateDetail(FastRemittanceApplicationDetail detail) {
		String version_sql = " SELECT version_lock FROM t_remittance_application_detail WHERE remittance_application_detail_uuid =:remittanceApplicationDetailUuid ";

		String oldVersion = getVersionNo(version_sql, "remittanceApplicationDetailUuid",
				detail.getRemittanceApplicationDetailUuid());

		if (!fastDataWithTemperatureHandler.updateDataWithVersionLock(detail, oldVersion)) {
			throw new RuntimeException("detail 更新失败");
		}
	}

	private FastRemittanceApplicationDetail updateFastDetailStatus(RemittanceContext remittanceContext) {
		FastRemittanceApplicationDetail detail = remittanceContext.getFastRemittanceApplicationDetail();
		
		List<FastRemittancePlan> planList = getFastRemittancePlanList(remittanceContext);
		
		List<ExecutionStatus> executionStatusList = planList.stream().map(rp->EnumUtil.fromOrdinal(ExecutionStatus.class, rp.getExecutionStatus())).collect(Collectors.toList());
		
		//获取放款实际成功金额，交易完成时间
		List<FastRemittancePlan> creditSuccessPlanList = planList.stream().filter(rp->rp.isCreditSuccess()).collect(Collectors.toList());
		BigDecimal actualTotalAmount = BigDecimal.ZERO;
		Date completePaymentDate = null;
		for (FastRemittancePlan creditSuccessPlan : creditSuccessPlanList) {
			actualTotalAmount = actualTotalAmount.add(creditSuccessPlan.getActualTotalAmount());
			completePaymentDate = creditSuccessPlan.getMaxCompletePaymentDate(completePaymentDate);
		}
		
		List<String> executionRemarks = planList.stream()
				.filter(rp -> rp.isCredit()).map(rp -> rp.getExecutionRemark())
				.collect(Collectors.toList());

		BigDecimal planAmount = getDetailPlanTotalAmount(detail.getRemittanceApplicationDetailUuid());

		//获取 综合状态
		ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList, planAmount, actualTotalAmount);
		String joinRemark = String.join(",", executionRemarks);
		String executionRemark = StringUtils.isEmpty(joinRemark)?StringUtils.EMPTY:joinRemark;
		detail.setActualTotalAmount(actualTotalAmount);
		detail.setExecutionStatus(finalStatus.ordinal());
		detail.setExecutionRemark(executionRemark);
		detail.setCompletePaymentDate(completePaymentDate);
		detail.setLastModifiedTime(new Date());
		detail.setVersionLock(UUID.randomUUID().toString());
		return detail;
	}
	private List<FastRemittancePlan> getFastRemittancePlanList(RemittanceContext remittanceContext) {
		List<String> remittancePlanUuidList = remittanceContext.getRemittancePlanUuidList();
		List<FastRemittancePlan> fastRemittancePlans = new ArrayList<>();
		for (String uuid : remittancePlanUuidList) {
			FastRemittancePlan fastRemittancePlan = fastDataWithTemperatureHandler.getByKey(FastRemittancePlanEnum.REMITTANCE_PLAN_UUID, uuid, FastRemittancePlan.class);
			if(null == fastRemittancePlan) {
				throw new RuntimeException("找不到fastRemittancePlan");
			}
			fastRemittancePlans.add(fastRemittancePlan);
		}
		
		return fastRemittancePlans;
	}

	private String getVersionNo(String version_sql,String key, String value) {
		List<String> resultList = genericDaoSupport.queryForSingleColumnList(version_sql, key, value, String.class);
		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
	}
	
	private void updateApplication(FastRemittanceApplication application) {
		String version_sql = " SELECT version_lock FROM t_remittance_application WHERE remittance_application_uuid =:remittanceApplicationUuid ";

		String oldVersion = getVersionNo(version_sql, "remittanceApplicationUuid",
				application.getRemittanceApplicationUuid());

		if (!fastDataWithTemperatureHandler.updateDataWithVersionLock(application, oldVersion)) {
			throw new RuntimeException("application 更新失败");
		}
	}

	private void updateRemittanceApplicationAndCache(RemittanceContext context) {
		FastRemittanceApplication application = null;
		if(context.isAllDetailFinished()) {
			application = updateApplicationStatus(context);
		}else {
			application = context.getFastRemittanceApplication();
			application.setVersionLock(UUID.randomUUID().toString());
			application.setLastModifiedTime(new Date());
		}

		updateApplication(application);
		
	}

	private FastRemittanceApplication updateApplicationStatus(RemittanceContext context) {
			FastRemittanceApplication application = context.getFastRemittanceApplication();

			List<FastRemittanceApplicationDetail> paramsForDetails = getRemittanceApplicationDetailList(context);
			String remittanceApplicationUuid = application.getRemittanceApplicationUuid();
			BigDecimal actualTotalAmount = BigDecimal.ZERO;
			List<ExecutionStatus> executionStatusList = new ArrayList<ExecutionStatus>();
			for (FastRemittanceApplicationDetail detail : paramsForDetails) {
				BigDecimal detailActualTotalAmount = detail.getActualTotalAmount();
				actualTotalAmount = actualTotalAmount.add(detailActualTotalAmount);
				ExecutionStatus executionStatus = EnumUtil.fromOrdinal(ExecutionStatus.class, detail.getExecutionStatus());
				executionStatusList.add(executionStatus);
			}

			BigDecimal planTotalAmount = application.getPlannedTotalAmount();

			ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList, planTotalAmount, actualTotalAmount);

			Integer actualNotifyNumber = application.getActualNotifyNumber();

			int planNotifyNumber = YX_NOTIFY_NUMBER + (actualNotifyNumber == null ? 0 : actualNotifyNumber);

			application.setActualTotalAmount(actualTotalAmount);
			application.setExecutionStatus(finalStatus.ordinal());
			application.setPlanNotifyNumber(planNotifyNumber);
			application.setLastModifiedTime(new Date());
			application.setVersionLock(UUID.randomUUID().toString());
			application.setOppositeReceiveDate( getRemittanceEffectiveDate(remittanceApplicationUuid));
			
			return application;
	}


	private List<FastRemittanceApplicationDetail> getRemittanceApplicationDetailList(
			RemittanceContext remittanceContext) {
		List<String> remittanceApplicationDetailUuidList = remittanceContext.getRemittanceApplicationDetailsUuidList();
		
		List<FastRemittanceApplicationDetail> fastRemittanceApplicationDetails = new ArrayList<>();
		for (String uuid : remittanceApplicationDetailUuidList) {
			FastRemittanceApplicationDetail fastRemittanceApplicationDetail = fastDataWithTemperatureHandler
					.getByKey(FastRemittanceApplicationDetailEnum.REMITTANCE_APPLICATION_DETAIL_UUID, uuid, FastRemittanceApplicationDetail.class);
			if (null == fastRemittanceApplicationDetail) {
				throw new RuntimeException("找不到fastRemittanceApplicationDetail");
			}
			fastRemittanceApplicationDetails.add(fastRemittanceApplicationDetail);
		}

		return fastRemittanceApplicationDetails;
	}

	private Map<String, Object> buildParamsExecLogAndUpdatePlan(QueryStatusResult queryStatusResult,
			FastRemittancePlan plan) {
		Integer executionStatus = queryStatusResult.convertToRemittanceExecutionStatus();
		Integer transactionRecipient = queryStatusResult.convertToTransactionRecipient();
		
		String pgAccountName = queryStatusResult.getChannelAccountName();
		String pgAccountNo = queryStatusResult.getChannelAccountNo();
		Date oppositeReceiveDate = queryStatusResult.getCommunicationLastSentTime();
		Date completePaymentDate = queryStatusResult.getBusinessSuccessTime();
		Integer processingStatus = ExecutionStatus.PROCESSING.ordinal();
		BigDecimal actualTotalAmount = BigDecimal.ZERO;
		if (executionStatus == ExecutionStatus.SUCCESS.ordinal()) {
			actualTotalAmount = queryStatusResult.getTransactionAmount();
		}
		String executionRemark = queryStatusResult.getBusinessResultMsg();
		String transactionSerialNo = queryStatusResult.getChannelSequenceNo();
		String execReqNo = queryStatusResult.getSourceMessageUuid();
		String execRspNo = queryStatusResult.getTradeUuid();
		Date lastModifiedTime = new Date();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pgAccountName", pgAccountName);
		params.put("pgAccountNo", pgAccountNo);
		params.put("oppositeReceiveDate", oppositeReceiveDate);
		params.put("completePaymentDate", completePaymentDate);
		params.put("actualTotalAmount", actualTotalAmount);
		params.put("executionStatus", executionStatus);
		params.put("transactionRecipient", transactionRecipient);
		params.put("processingStatus", processingStatus);
		params.put("executionRemark", executionRemark);
		params.put("transactionSerialNo", transactionSerialNo);
		params.put("planCreditCashFlowCheckNumber", PLAN_CREDIT_CASH_FLOW_CHECK_NUMBER);
		params.put("lastModifiedTime", lastModifiedTime);
		params.put("remittancePlanUuid", plan.getRemittancePlanUuid());
		params.put("execReqNo", execReqNo);
		params.put("execRspNo", execRspNo);
		
		
		plan.setPgAccountName(pgAccountName);
		plan.setPgAccountNo(pgAccountNo);
		plan.setCompletePaymentDate(completePaymentDate);
		plan.setActualTotalAmount(actualTotalAmount);
		plan.setExecutionStatus(executionStatus);
		plan.setExecutionRemark(executionRemark);
		plan.setLastModifiedTime(lastModifiedTime);
		plan.setTransactionSerialNo(transactionSerialNo);
		
		return params;
	}
	
	/**
	 * 解析单个放款结果，更新计划表，日志表
	 */
	@Override
	public RemittanceContext singleAnalysisRemittanceResult(RemittanceContext remittanceContext) {
		
		String remittanceApplicationUuid = remittanceContext.getFastRemittanceApplication().getRemittanceApplicationUuid();
		String 	eventKey = "planUuid:"+remittanceContext.getFastRemittancePlan().getUuid() + "&applicationUuid:" + remittanceApplicationUuid;

		SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_JPMORGAN_QUERY_RESULT_BUILD_SUCCESS,eventKey, null, null,SystemTraceLog.INFO, null, SYSTEM_NAME.JPMORGAN, SYSTEM_NAME.REMITTANCE_SYSTEM);

		logger.info(systemTraceLog);
		st_logger.debug(systemTraceLog.getSql(remittanceApplicationUuid,new Date()));
		
		remittanceContext.validQueryResutAndRemittanceInfo();
		try {
			updateRemittanceInfoAfterInitContext(remittanceContext);
		} catch (Exception e) {
			systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ACCEPT_JPMORGAN_QUERY_RESULT_UPDATE_ERROR);
			logger.info(systemTraceLog.error("update_fail!"));
			roll_bock_cache(remittanceContext, systemTraceLog);
			throw e;
		}
		return remittanceContext;
										
	}

	@Override
	public  RemittanceContext buildContext(QueryStatusResult queryStatusResult){
		String remittancePlanUuid = getAndCheckRemittancePlanUuid(queryStatusResult);
		
		String eventKey = "planUuid:"+remittancePlanUuid + "&applicationUuid:null";
		SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_JPMORGAN_QUERY_RESULT_START,eventKey, null, null,SystemTraceLog.INFO, null, SYSTEM_NAME.JPMORGAN, SYSTEM_NAME.REMITTANCE_SYSTEM);
		logger.info(systemTraceLog);
		

		FastRemittancePlan remittancePlan = getAndCheckFastRemittancePlan(remittancePlanUuid);
		FastRemittanceApplicationDetail remittanceApplicationDetail = getAndCheckFastRemittanceApplicationDetail(remittancePlan.getRemittanceApplicationDetailUuid()); 
		FastRemittanceApplication remittanceApplication = getAndCheckFastRemittanceApplication(remittancePlan.getRemittanceApplicationUuid());
		
		
		String latestExecReqNo = iRemittancePlanExecLogService.getLatestRemittancePlanExecLogExecReqNo(remittancePlanUuid);
		
		boolean isAllPlanFinished = getIsAllPlanFinished(remittanceApplicationDetail);
		boolean isAllDetailFinished = getIsAllDetailFinished(isAllPlanFinished,remittanceApplication);
		
		List<String> remittancePlanUuidList = getRemittancePlanList(remittanceApplicationDetail, isAllPlanFinished);
		
		List<String> remittanceApplicationDetailUuidList = getRemittanceApplicationDetailList(remittanceApplication, isAllDetailFinished);
		
		return new RemittanceContext(remittancePlan,remittanceApplicationDetail,remittanceApplication,remittancePlanUuidList,remittanceApplicationDetailUuidList,isAllPlanFinished,isAllDetailFinished, queryStatusResult, latestExecReqNo) ;
	}
	private String getAndCheckRemittancePlanUuid(QueryStatusResult queryStatusResult) {
		String remittancePlanUuid;
		if(queryStatusResult == null || StringUtils.isEmpty(remittancePlanUuid = queryStatusResult.getTransactionUuid())){
			throw new ApiException(ApiResponseCode.WRONG_FORMAT,"json parse error,with resultJsonString");
		}
		return remittancePlanUuid;
	}

	private FastRemittanceApplication getAndCheckFastRemittanceApplication(String remittanceApplicationUuid) {
		FastRemittanceApplication application = fastDataWithTemperatureHandler.getByKey(FastRemittanceApplicationEnum.REMITTANCE_APPLICATION_UUID,remittanceApplicationUuid,FastRemittanceApplication.class);
		if(null == application) {
			throw new RuntimeException("找不到 fastRemittanceApplication");
		}
		if(application.temperature() == DataTemperature.FROZEN) {
			throw new RuntimeException("applicationUuid:["+application.getRemittanceApplicationUuid()+"],该fastRemittanceApplicationDetail应为热数据");
		}
		return application;
	}

	private FastRemittanceApplicationDetail getAndCheckFastRemittanceApplicationDetail(
			String remittanceApplicationDetailUuid) {
		FastRemittanceApplicationDetail detail = fastDataWithTemperatureHandler.getByKey(FastRemittanceApplicationDetailEnum.REMITTANCE_APPLICATION_DETAIL_UUID,remittanceApplicationDetailUuid,FastRemittanceApplicationDetail.class);
		if(null == detail) {
			throw new RuntimeException("找不到 fastRemittanceApplicationDetail");
		}
		if(detail.temperature() == DataTemperature.FROZEN) {
			throw new RuntimeException("detailUuid:["+detail.getRemittanceApplicationDetailUuid()+"],该fastRemittanceApplicationDetail应为热数据");
		}
		return detail;
	}

	private FastRemittancePlan getAndCheckFastRemittancePlan(String remittancePlanUuid) {
		
		
		FastRemittancePlan fastRemittancePlan = fastDataWithTemperatureHandler.getByKey(FastRemittancePlanEnum.REMITTANCE_PLAN_UUID,remittancePlanUuid,FastRemittancePlan.class);
		if(null == fastRemittancePlan) {
			throw new RuntimeException("找不到 fastRemittancePlan");
		}
		if(fastRemittancePlan.temperature() == DataTemperature.FROZEN) {
			throw new RuntimeException("planUuid"+remittancePlanUuid+"],该fastRemittancePlan应为热数据");
		}
		return fastRemittancePlan;
	}

	private void roll_bock_cache(RemittanceContext remittanceContext, SystemTraceLog systemTraceLog) {
		FastRemittancePlan remittancePlan = remittanceContext.getFastRemittancePlan(); 
		FastRemittanceApplicationDetail remittanceApplicationDetail = remittanceContext.getFastRemittanceApplicationDetail();
		
		try {
			fastDataWithTemperatureHandler.delByKey(FastRemittancePlanEnum.REMITTANCE_PLAN_UUID,remittancePlan.getRemittancePlanUuid(),true);
			fastDataWithTemperatureHandler.delByKey(FastRemittanceApplicationDetailEnum.REMITTANCE_APPLICATION_DETAIL_UUID,remittancePlan.getRemittanceApplicationDetailUuid(),true);
			fastDataWithTemperatureHandler.delByKey(FastRemittanceApplicationEnum.REMITTANCE_APPLICATION_UUID,remittanceApplicationDetail.getRemittanceApplicationUuid(),true);
		} catch (GiottoException e) {
			logger.info(systemTraceLog.error("cache_roll_back_fail!"));
		}

		logger.info(systemTraceLog.info("cache_roll_back",EVENT_NAME.REMITTANCE_ACCEPT_JPMORGAN_QUERY_RESULT_CACHE_ROLL_BACK));
	}

	private List<String> getRemittanceApplicationDetailList(FastRemittanceApplication remittanceApplication, boolean isAllDetailFinished) {
		if (!isAllDetailFinished) {
			return Collections.emptyList();
		}
		List<String> remittanceApplicationDetailUuidList = remittanceRelationCacheService.get(RemittanceRelation.RA_RD,
				remittanceApplication.getRemittanceApplicationUuid());
		return remittanceApplicationDetailUuidList;
	}
	
	
	private List<String> getRemittancePlanList(FastRemittanceApplicationDetail remittanceApplicationDetail, boolean isAllPlanFinished) {
		if (!isAllPlanFinished) {
			return Collections.emptyList();
		}
		List<String> remittancePlanUuidList = remittanceRelationCacheService.get(RemittanceRelation.RD_RP,
				remittanceApplicationDetail.getRemittanceApplicationDetailUuid());
		return remittancePlanUuidList;
	}
	
	private boolean getIsAllDetailFinished(boolean isAllPlanFinished, FastRemittanceApplication application) {
		if(!isAllPlanFinished) {
			return false;
		}
		int berfore_actual_count = application.getActualCount();
		int total_count = application.getTotalCount();
		int actual_count = berfore_actual_count+1;
		
		application.setActualCount(actual_count);

		if(total_count > actual_count){
			return false;
		}else if(actual_count == total_count){
			return true;
		}else{
			throw new RuntimeException("实际明细数不能大于总明细数");
		}
	}

	private boolean getIsAllPlanFinished(FastRemittanceApplicationDetail detail) {
		int before_actual_count = detail.getActualCount();
		int total_count = detail.getTotalCount();
		int actual_count = before_actual_count+1;
		
		detail.setActualCount(actual_count);

		if(total_count > actual_count){
			return false;
		}else if(total_count == actual_count){
			return true;
		}else {
			throw new RuntimeException("实际计划数不能大于总计划数");
		}
	}

	@Override
	public String saveRemittanceInfo(RemittanceCommandModel commandModel, String ip,
			String creatorName) {
		
		List<RemittanceDetail> remittanceDetailList = commandModel.getRemittanceDetailListFromJsonString();
		int detail_total_count = remittanceDetailList.size();
		
		RemittanceApplication remittanceApplication = commandModel.convertToRemittanceApplication(ip,
				creatorName, YX_NOTIFY_URL, CHECK_RETRY_NUMBER,detail_total_count, YX_NOTIFY_NUMBER);
		
		String remittanceApplicationUuid = remittanceApplication.getRemittanceApplicationUuid();
      for (RemittanceDetail remittanceDetail : commandModel.getRemittanceDetailListFromJsonString()) {
          RemittanceApplicationDetail remittanceApplicationDetail = remittanceDetail.convertToRemittanceApplicationDetail(remittanceApplicationUuid, creatorName, commandModel.getTransactionType());
          iRemittanceApplicationDetailService.save(remittanceApplicationDetail);
        }
		
      remittanceApplication.setCheckSendTime(new Date());
      remittanceApplication.setCheckStatus(RemittanceApplicationCheckStatus.TO_VERIFY);
      
    	 iRemittanceApplicationService.save(remittanceApplication);
    	 
    	 return null;
    	 
	}
	
	/**
	 * 查询一天内所有状态为未校验且当前时间距离最后一次发送校验时间已经过去了30分钟的所有application
	 */
	@Override
	public List<RemittanceApplication> getRemittanceApplicationInNotVerify(int limit) {
		if(business_verify_no_response_time == null){
			business_verify_no_response_time=30;
		}
		
      Calendar c=Calendar.getInstance();
      c.add(Calendar.MINUTE, -business_verify_no_response_time);
      Date date = c.getTime();
      
		String hql = "FROM RemittanceApplication WHERE executionStatus =:executionStatus AND checkStatus IN (:checkStatus) AND checkSendTime <=:timeout AND transactionRecipient = :transactionRecipient ";
		Map<String,Object> params = new HashMap<String,Object>(){
			{
				put("executionStatus", ExecutionStatus.PROCESSING);
				put("checkStatus", Arrays.asList(RemittanceApplicationCheckStatus.TO_VERIFY,RemittanceApplicationCheckStatus.BUSINESS_VERIFICATION_SUCCESS));
				put("timeout", date);
				put("transactionRecipient", TransactionRecipient.LOCAL);
			}
		};

		@SuppressWarnings("unchecked")
		List<RemittanceApplication> remittanceApplicationList = genericDaoSupport.searchForList(hql,params,0,limit);
		return remittanceApplicationList;
	}

	@Override
	public String checkAndUpdateApplication(String remittanceApplicationUuid,boolean is_success, String checkRequestNo,boolean isBusinessValidation, Boolean isNeedDoBalance) {
		
		RemittanceSqlModel remittanceSqlModel = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);

		Integer checkStatus;
		if(isBusinessValidation) {
			businessValidate(remittanceSqlModel, checkRequestNo, isNeedDoBalance);
			checkStatus = is_success ? (isNeedDoBalance ? RemittanceApplicationCheckStatus.BUSINESS_VERIFICATION_SUCCESS.ordinal() : RemittanceApplicationCheckStatus.QUOTA_VERIFICATION_SUCCESS.ordinal())
					: RemittanceApplicationCheckStatus.BUSINESS_VERIFICATION_FAILURE.ordinal();
		}else { //isQuotaValidation
			quotaValidate(remittanceSqlModel, checkRequestNo);
			checkStatus = is_success ? RemittanceApplicationCheckStatus.QUOTA_VERIFICATION_SUCCESS.ordinal() : RemittanceApplicationCheckStatus.QUOTA_VERIFICATION_FAILURE.ordinal();
		}
		
		String application_sql =" UPDATE t_remittance_application SET check_request_no =:checkRequestNo,check_status =:checkStatus,version_lock =:newVersion WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion ";
		String newCheckRequestNo = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<>();
		params.put("checkStatus", checkStatus);
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("checkRequestNo", newCheckRequestNo);

		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, application_sql, params ,remittanceSqlModel.getVersionLock());
	
		return newCheckRequestNo;
	}
	

	private void businessValidate(RemittanceSqlModel remittanceSqlMode, String checkRequestNo, Boolean isNeedDoBalance) {
		
		if (remittanceSqlMode == null || ExecutionStatus.PROCESSING.ordinal() != remittanceSqlMode.getExecutionStatus()
				|| TransactionRecipient.LOCAL.ordinal() != remittanceSqlMode.getTransactionRecipient()){
			throw new ApiException("remittanceApplication_error");
		}
		
		if(isNeedDoBalance) {
			if(remittanceSqlMode.getCheckStatus() != RemittanceApplicationCheckStatus.TO_VERIFY.ordinal() 
					&& remittanceSqlMode.getCheckStatus() != RemittanceApplicationCheckStatus.BUSINESS_VERIFICATION_SUCCESS.ordinal()) {
				throw new ApiException("remittanceApplication_check_status_error");
			}
		}else {
			if(remittanceSqlMode.getCheckStatus() != RemittanceApplicationCheckStatus.TO_VERIFY.ordinal()) {
				throw new ApiException("remittanceApplication_check_status_error");
			}
		}
		
		if (StringUtils.isEmpty(checkRequestNo)) {
			throw new ApiException("checkRequestNo_is_empty");

		}
		String checkReuqestNoInDB = remittanceSqlMode.getCheckRequestNo();
		if (StringUtils.isEmpty(checkRequestNo) || (!checkRequestNo.equals(checkReuqestNoInDB))) {
			throw new ApiException("checkRequestNo_error,checkRequestNo:[" + checkRequestNo + "],checkReuqestNoInDB:["
					+ checkReuqestNoInDB + "]");

		}
	}

	private void quotaValidate(RemittanceSqlModel remittanceSqlMode, String checkRequestNo) {
		if (remittanceSqlMode == null || ExecutionStatus.PROCESSING.ordinal() != remittanceSqlMode.getExecutionStatus()
				|| TransactionRecipient.LOCAL.ordinal() != remittanceSqlMode.getTransactionRecipient()
				|| remittanceSqlMode.getCheckStatus() != RemittanceApplicationCheckStatus.BUSINESS_VERIFICATION_SUCCESS.ordinal()) {
			throw new ApiException("remittanceApplication_error");
		}
		if (StringUtils.isEmpty(checkRequestNo)) {
			throw new ApiException("checkRequestNo_is_empty");

		}
		String checkReuqestNoInDB = remittanceSqlMode.getCheckRequestNo();
		if (StringUtils.isEmpty(checkRequestNo) || (!checkRequestNo.equals(checkReuqestNoInDB))) {
			throw new ApiException("checkRequestNo_error,checkRequestNo:[" + checkRequestNo + "],checkReuqestNoInDB:["
					+ checkReuqestNoInDB + "]");

		}
	}
	
	@Override
	public void fillFinancialInfoIntoRemittance(String remittanceApplicationUuid , FinancialContract financialContract) {

		RemittanceSqlModel remittanceSqlMode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		
		if (!remittanceSqlMode.getFinancialProductCode().equals(financialContract.getContractNo())) {
			throw new RuntimeException(
					"morganStanley 传来的信托合同和放款申请信托不一致，RA.productCode:" + remittanceSqlMode.getFinancialProductCode()
							+ ",morganStanley.productCode:" + financialContract.getContractNo());
		}
		
		long financialContractId = financialContract.getId();
		String financialContractUuid = financialContract.getFinancialContractUuid();
		
		String application_sql =" UPDATE t_remittance_application SET version_lock =:newVersion,financial_contract_id =:financialContractId,financial_contract_uuid =:financialContractUuid WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion ";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractId", financialContractId);
		params.put("financialContractUuid", financialContractUuid);
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);

		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, application_sql, params);
		
		iRemittanceApplicationDetailService.fillFinancialInfoIntoDetail(params);
		
	}
	
	@Override
	public void updateRemittanceExecuteIfFailed(String remittanceApplicationUuid,boolean notifyStatus){
		
		RemittanceSqlModel mode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		
		int beforeActualNotifyNumber = mode.getActualNotifyNumber();
		int planNotifyNumber = mode.getPlanNotifyNumber();
		
		int afterActualNotifyNumber = beforeActualNotifyNumber + 1;
		Map<String, Object> params = new HashMap<>();
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("actualNotifyNumber", afterActualNotifyNumber);
		String updateSql = " UPDATE t_remittance_application SET actual_notify_number =:actualNotifyNumber ,version_lock =:newVersion  WHERE remittance_application_uuid = :remittanceApplicationUuid and version_lock =:oldVersion ";
		
		if(notifyStatus) {
			updateSql = " UPDATE t_remittance_application SET actual_notify_number =:actualNotifyNumber ,version_lock =:newVersion ,notify_status =:notifyStatus   WHERE remittance_application_uuid = :remittanceApplicationUuid and version_lock =:oldVersion ";
			params.put("notifyStatus", NotifyStatus.RETRY_PUSH_TO_OUTLIER.ordinal());
		}
		
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, updateSql, params);
		
		logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + GloableLogSpec
                .RawData("处理除了响应成功的情况,如响应超时,异常,响应失败等等情况"));
		logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + GloableLogSpec
                .RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
		logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + GloableLogSpec
                .RawData("自增后actual_notify_number:["+ afterActualNotifyNumber +"]操作"));
	} 

	@Override
	public void updateRemittanceExecuteIfSuccess(String remittanceApplicationUuid) {
		RemittanceSqlModel mode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		
		int beforeActualNotifyNumber = mode.getActualNotifyNumber();
		
		int afterActualNotifyNumber = beforeActualNotifyNumber + 1;
		int afterPlanNotifyNumber = afterActualNotifyNumber;
		String updateSql = "UPDATE t_remittance_application SET actual_notify_number =:actualNotifyNumber , plan_notify_number =:planNotifyNumber,version_lock =:newVersion WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion";
		Map<String,Object> params = new HashMap<>();
		params.put("actualNotifyNumber", afterActualNotifyNumber);
		params.put("planNotifyNumber", afterPlanNotifyNumber);
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, updateSql, params);

	}

}
