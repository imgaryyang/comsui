package com.suidifu.bridgewater.handler.remittance.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.v2.RemittanceStrategyFactory;
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
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationContentValue;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.remittance.CertificateType;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
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

/**
 * Created by zhenghangbo on 23/05/2017.
 */

public abstract class AbstractRemittanceApplicationHandler implements IRemittanceApplicationHandler {

    @Autowired
    private IRemittanceApplicationService iRemittanceApplicationService;

    @Autowired
    private IRemittanceApplicationDetailService iRemittanceApplicationDetailService;

    @Autowired
    private IRemittancePlanService iRemittancePlanService;

    @Autowired
    private IRemittancePlanExecLogService iRemittancePlanExecLogService;

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
    private ModifyRemittanceApplicationLogService modifyRemittanceApplicationLogService;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Autowired
    private RemittanceBlackListService remittanceBlackListService;


    @Autowired
    private FinancialContractService financialContractService;

    private static final Log logger = LogFactory.getLog(IRemittanceApplicationHandler.class);

    @Value("#{config['yx.notify.number']}")
    private int YX_NOTIFY_NUMBER = 0;

    @Value("#{config['yx.api.merId']}")
    private String YX_API_MERID = null;

    @Value("#{config['yx.api.secretkey']}")
    private String YX_API_SECRETKEY = null;

    private static final int PLAN_CREDIT_CASH_FLOW_CHECK_NUMBER = 3;

    private RemittancePlan convertToRemittancePlan(TradeSchedule tradeSchedule, String remittanceApplicationUuid,
        String financialContractUuid, Long financialContractId, String contractUniqueId, String contractNo, String creatorName) {
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
        PaymentInstitutionName paymentInstitutionName = EnumUtil
            .fromOrdinal(PaymentInstitutionName.class, relatedPaymentGateway);
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

    /**
     * 放款明细内，银行编号校验
     */
    public void validateRemittanceDetailsBankCode(RemittanceCommandModel commandModel) {
        Map<String, Bank> bankMap = bankService.getCachedBanks();
        List<RemittanceDetail> remittanceDetails = commandModel.getRemittanceDetailListFromJsonString();
        for (RemittanceDetail remittanceDetail : remittanceDetails) {
            String bankCode = remittanceDetail.getBankCode();
            if(!bankMap.containsKey(bankCode)){
                throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
            }
        }
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
                logger.info("#放款结果查询，结果解析失败［"+responseStr+"］");
                return;
            }
            singleAnalysisRemittanceResult(remittanceApplicationUuid, queryStatusResults);
        }
    }

    /**
     * 解析放款结果，更新计划表，日志表
     */
    private void singleAnalysisRemittanceResult(String remittanceApplicationUuid, List<QueryStatusResult> queryStatusResults) {

        Map<String, List<QueryStatusResult>> queryStatusResultsMap = queryStatusResults.stream().collect(Collectors.groupingBy(
            QueryStatusResult::getTransactionUuid));

        List<String> planUuids = getRemittancePlanUuidsInProcessingBy(remittanceApplicationUuid);
        List<String> isFinishedPlanUuis = new ArrayList<String>();
        for (String planUuid : planUuids) {
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
        if(isFinishedPlanUuis.containsAll(planUuids)) {
            logger.info("#放款结果同步开始，申请号［"+remittanceApplicationUuid+"］");
            syncRemittanceStatusAfterNoRemiitancePlanInProcessing(remittanceApplicationUuid);
        }
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
        Map<String, List<RemittancePlan>> groups = remittancePlanList.stream().collect(Collectors.groupingBy(
            RemittancePlan::getRemittanceApplicationDetailUuid));
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
        ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actualTotalAmount", actualTotalAmount);
        params.put("executionStatus", finalStatus.ordinal());
        params.put("planNotifyNumber", YX_NOTIFY_NUMBER);
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

        //获取 综合状态
        ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList);

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

    /**
     * 评估最终结果
     * @param executionStatusList 状态列表
     */
    private ExecutionStatus doEvaluateFinalStatus(List<ExecutionStatus> executionStatusList) {
        ExecutionStatus[] statusListForAllMatch = {ExecutionStatus.SUCCESS, ExecutionStatus.FAIL, ExecutionStatus.ABANDON, ExecutionStatus.ABNORMAL};
        //所有成功，失败，或者撤销
        for (ExecutionStatus executionStatus : statusListForAllMatch) {
            boolean isAllMatch = executionStatusList.stream().allMatch(s-> s == executionStatus);
            if(isAllMatch) {
                return executionStatus;
            }
        }
        //部分成功
        boolean isPartSuccess = executionStatusList.stream().anyMatch(s-> s == ExecutionStatus.SUCCESS);
        if(isPartSuccess) {
            return ExecutionStatus.ABNORMAL;
        }

        return ExecutionStatus.FAIL;
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


    @Override
    public List<String> getRemittanceApplicationUuidsInOppositeProcessing(Date queryStartDate) {
        String queryUuidsSql = "SELECT remittance_application_uuid FROM t_remittance_application WHERE create_time >=:queryStartDate AND execution_status =:executionStatus AND transaction_recipient =:transactionRecipient";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("queryStartDate", queryStartDate);
        params.put("executionStatus", ExecutionStatus.PROCESSING.ordinal());
        params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());

        return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
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
    public List<String> getWaitingNoticeRemittanceApplicationBy(
        String financialContractUuid, int pageSize, Date queryStartDate) {
        if(StringUtils.isBlank(financialContractUuid)) {
            return Collections.emptyList();
        }

        String queryUuidsSql = "SELECT remittance_application_uuid "
            + "FROM t_remittance_application "
            + "WHERE financial_contract_uuid =:financialContractUuid "
            + " AND create_time >=:queryStartDate"
            + " AND execution_status IN(:executionStatusList) "
            + " AND actual_notify_number < plan_notify_number "
            + " AND notify_url IS NOT NULL "
            + " LIMIT :pageSize";

        Map<String, Object> params = new HashMap<String, Object>();
        List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
        finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
        finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
        finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());

        params.put("financialContractUuid", financialContractUuid);
        params.put("queryStartDate", queryStartDate);
        params.put("executionStatusList", finishedExecutionStatusList);
        params.put("pageSize", pageSize);

        return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
    }
    
    @Override
    public List<String> getWaitingNoticeRemittanceApplicationV2(int limit, Date queryStartDate, Date notifyOutlierDelay) {

        String queryUuidsSql = "SELECT remittance_application_uuid "
            + " FROM t_remittance_application "
            + " WHERE create_time >=:queryStartDate "
            + " AND execution_status IN(:executionStatusList) "
            + " AND actual_notify_number < plan_notify_number "
            + " AND notify_url IS NOT NULL AND create_time < :notifyOutlierDelay "
            + " LIMIT :limit";

        Map<String, Object> params = new HashMap<String, Object>();
        List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
        finishedExecutionStatusList.add(ExecutionStatus.SUCCESS.ordinal());
        finishedExecutionStatusList.add(ExecutionStatus.FAIL.ordinal());
        finishedExecutionStatusList.add(ExecutionStatus.ABANDON.ordinal());

        params.put("queryStartDate", queryStartDate);
        params.put("executionStatusList", finishedExecutionStatusList);
        params.put("limit", limit);
        params.put("notifyOutlierDelay", notifyOutlierDelay);
        
        return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
    }

    public void updateRemittanceExecuteIfFailed(int actualNotifyNumber,int planNotifyNumber,String oppositeKeyWord, List<String> remittanceApplicationUuids){

        this.genericDaoSupport.executeSQL(
            "UPDATE t_remittance_application "
                + "SET actual_notify_number = (actual_notify_number + 1) "
                + "WHERE remittance_application_uuid IN (:remittanceApplicationUuids)", "remittanceApplicationUuids", remittanceApplicationUuids);
        logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec
                .RawData("处理除了响应成功的情况,如响应超时,异常,响应失败等等情况"));
        logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec
                .RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
        logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec
                .RawData("自增后actual_notify_number:["+ (actualNotifyNumber + 1) +"]操作"));


    }

    private Map<String, String> buildHeaderParamsForNotifyRemittanceResult(
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

        String remittanceRequestNo = model.getRemittanceTradeNo();
        RemittanceApplication application = iRemittanceApplicationService.getRemittanceApplicationByRequestNo(remittanceRequestNo);
        if (application == null) { // 放款请求编号不存在
            throw new ApiException(ApiResponseCode.NOT_EXIST_REQUEST_NO);
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

        List<RemittancePlan> remittancePlans = iRemittancePlanService.getRemittancePlanListBy(application.getRemittanceApplicationUuid());
        // 计划订单中的任何明细正在和银行系统通信中（放款单处理中）不允许变更；
        if (hasProcessingRemittancePlan(remittancePlans)) {
            throw new ApiException(ApiResponseCode.MODIFY_ACCEPTANCE_FAILED);
        }

        validateDetailAmount(application, remittancePlans, remittanceDetails);

        updateRemittancePlanFromFailToAbandon(application, remittancePlans);

        List<TradeSchedule> tradeSchedules = splitRemittancePlanByStrategy(model, application);

        saveRemittanceInfo(remittanceDetails, application, tradeSchedules, financialContract, operator);

        return tradeSchedules;
    }

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
    }

    private void updateRemittancePlanFromFailToAbandon(RemittanceApplication application, List<RemittancePlan> remittancePlans){
        if(application == null || CollectionUtils.isEmpty(remittancePlans)){
            return;
        }
        List<String> remittancePlanUuids = remittancePlans.stream()
            .filter(a->a.getExecutionStatus().ordinal() == ExecutionStatus.FAIL.ordinal())
            .map(RemittancePlan::getRemittancePlanUuid)
            .collect(Collectors.toList());

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
        remittancePlans.forEach(a->{
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

    /**
     * 放款明细内，银行编号校验
     */
    private void validateRemittanceDetailsBankCode(List<RemittanceDetail> remittanceDetails) {
        Map<String, Bank> bankMap = bankService.getCachedBanks();
        for (RemittanceDetail remittanceDetail : remittanceDetails) {
            String bankCode = remittanceDetail.getBankCode();
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



    // 存在正在处理中的放款单
    private boolean hasProcessingRemittancePlan(List<RemittancePlan> remittancePlans) {
        return CollectionUtils.isEmpty(remittancePlans) || remittancePlans.stream()
            .filter(a -> ExecutionStatus.PROCESSING.ordinal() == a.getExecutionStatus().ordinal()).count() > 0;
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



}
