package com.suidifu.bridgewater.fast;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastCacheObjectWithTemperature;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by qinweichao on 2017/9/8.
 */
public class FastDeductApplication extends FastCacheObjectWithTemperature {

    private Long id;

    private String deductApplicationUuid;

    private Date apiCalledTime;

    private String requestNo;

    private String deductId;

    private String financialContractUuid;

    private String financialProductCode;

    private String contractUniqueId;

    private String contractNo;

    private String customerName;

    private String repaymentPlanCodeList;
    //计划扣款金额
    private BigDecimal plannedDeductTotalAmount;

    //实际扣款金额
    private BigDecimal  actualDeductTotalAmount;

    private String  notifyUrl;

    //接口交易类型
    private Integer transcationType;

    //还款类型
    private Integer repaymentType;

    private Integer transactionRecipient;

    //扣款执行状态
    private Integer executionStatus;

    private Integer receiveStatus;

    private Integer recordStatus;

    private Integer isAvailable;

    private Integer thirdPartVoucherStatus;

    private String executionRemark;

    //扣款申请创建时间
    private Date createTime;

    //交易发起时间
    private Date transactionTime;

    //创建人
    private String creatorName;
    //ip地址
    private String ip;
    /** 成功后设置其为扣款成功时间 */
    private Date  lastModifiedTime;
    /**扣款结束时间**/
    private Date  completeTime;

    private String  mobile;

    private Integer sourceType;

    private String  gateway="";

    /**
     * 计划通知次数
     */
    private Integer planNotifyNumber;

    /**
     * 实际通知次数
     */
    private Integer actualNotifyNumber;

    private String batchDeductApplicationUuid;
    private String batchDeductId;
    private Integer noneBusinessCheckStatus;
    private Integer businessCheckStatus;

    private Integer notifyStatus;

    private boolean retriable;

    private Integer retryTimes;

    private String paymentOrderUuid;

    private String version  = UUID.randomUUID().toString();

    private Integer totalCount = 0;

    private Integer executedCount = 0;

    private String requestParams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeductApplicationUuid() {
        return deductApplicationUuid;
    }

    public void setDeductApplicationUuid(String deductApplicationUuid) {
        this.deductApplicationUuid = deductApplicationUuid;
    }

    public Date getApiCalledTime() {
        return apiCalledTime;
    }

    public void setApiCalledTime(Date apiCalledTime) {
        this.apiCalledTime = apiCalledTime;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getDeductId() {
        return deductId;
    }

    public void setDeductId(String deductId) {
        this.deductId = deductId;
    }

    public String getFinancialContractUuid() {
        return financialContractUuid;
    }

    public void setFinancialContractUuid(String financialContractUuid) {
        this.financialContractUuid = financialContractUuid;
    }

    public String getFinancialProductCode() {
        return financialProductCode;
    }

    public void setFinancialProductCode(String financialProductCode) {
        this.financialProductCode = financialProductCode;
    }

    public String getContractUniqueId() {
        return contractUniqueId;
    }

    public void setContractUniqueId(String contractUniqueId) {
        this.contractUniqueId = contractUniqueId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRepaymentPlanCodeList() {
        return repaymentPlanCodeList;
    }

    public void setRepaymentPlanCodeList(String repaymentPlanCodeList) {
        this.repaymentPlanCodeList = repaymentPlanCodeList;
    }

    public BigDecimal getPlannedDeductTotalAmount() {
        return plannedDeductTotalAmount;
    }

    public void setPlannedDeductTotalAmount(BigDecimal plannedDeductTotalAmount) {
        this.plannedDeductTotalAmount = plannedDeductTotalAmount;
    }

    public BigDecimal getActualDeductTotalAmount() {
        return actualDeductTotalAmount;
    }

    public void setActualDeductTotalAmount(BigDecimal actualDeductTotalAmount) {
        this.actualDeductTotalAmount = actualDeductTotalAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getTranscationType() {
        return transcationType;
    }

    public void setTranscationType(Integer transcationType) {
        this.transcationType = transcationType;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Integer getTransactionRecipient() {
        return transactionRecipient;
    }

    public void setTransactionRecipient(Integer transactionRecipient) {
        this.transactionRecipient = transactionRecipient;
    }

    public Integer getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(Integer executionStatus) {
        this.executionStatus = executionStatus;
    }

    public Integer getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(Integer receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getThirdPartVoucherStatus() {
        return thirdPartVoucherStatus;
    }

    public void setThirdPartVoucherStatus(Integer thirdPartVoucherStatus) {
        this.thirdPartVoucherStatus = thirdPartVoucherStatus;
    }

    public String getExecutionRemark() {
        return executionRemark;
    }

    public void setExecutionRemark(String executionRemark) {
        this.executionRemark = executionRemark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Integer getPlanNotifyNumber() {
        return planNotifyNumber;
    }

    public void setPlanNotifyNumber(Integer planNotifyNumber) {
        this.planNotifyNumber = planNotifyNumber;
    }

    public Integer getActualNotifyNumber() {
        return actualNotifyNumber;
    }

    public void setActualNotifyNumber(Integer actualNotifyNumber) {
        this.actualNotifyNumber = actualNotifyNumber;
    }

    public String getBatchDeductApplicationUuid() {
        return batchDeductApplicationUuid;
    }

    public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
        this.batchDeductApplicationUuid = batchDeductApplicationUuid;
    }

    public String getBatchDeductId() {
        return batchDeductId;
    }

    public void setBatchDeductId(String batchDeductId) {
        this.batchDeductId = batchDeductId;
    }

    public Integer getNoneBusinessCheckStatus() {
        return noneBusinessCheckStatus;
    }

    public void setNoneBusinessCheckStatus(Integer noneBusinessCheckStatus) {
        this.noneBusinessCheckStatus = noneBusinessCheckStatus;
    }

    public Integer getBusinessCheckStatus() {
        return businessCheckStatus;
    }

    public void setBusinessCheckStatus(Integer businessCheckStatus) {
        this.businessCheckStatus = businessCheckStatus;
    }

    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public boolean isRetriable() {
        return retriable;
    }

    public void setRetriable(boolean retriable) {
        this.retriable = retriable;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getPaymentOrderUuid() {
        return paymentOrderUuid;
    }

    public void setPaymentOrderUuid(String paymentOrderUuid) {
        this.paymentOrderUuid = paymentOrderUuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getExecutedCount() {
        return executedCount;
    }

    public void setExecutedCount(Integer executedCount) {
        this.executedCount = executedCount;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public List<String> getRepaymentPlanCodeListJsonString() {
        if(this.repaymentPlanCodeList == null){
            return Collections.EMPTY_LIST;
        }

        return JsonUtils.parseArray(repaymentPlanCodeList, String.class);
    }

    public boolean isExecutionFinsh(){
        if (executionStatus == DeductApplicationExecutionStatus.SUCCESS.ordinal() ||
                executionStatus == DeductApplicationExecutionStatus.FAIL.ordinal() ||
                executionStatus == DeductApplicationExecutionStatus.ABANDON.ordinal()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public DataTemperature temperature() {
        if (isExecutionFinsh() && (StringUtils.isEmpty(notifyUrl) || actualNotifyNumber >= planNotifyNumber)){
            return DataTemperature.FROZEN;
        }else {
            return DataTemperature.HOT;
        }
    }

    @Override
    public SqlAndParamTuple updateSqlAndParamTupleWithVersionLock(String oldVersion) {
        return null;
    }

    @Override
    public SqlAndParamTuple checkAfterUpdateSqlAndParamTuple() {
       return null;
    }

    @Override
    public SqlAndParamTuple updateSqlAndParamTuple() {
        String update_sql = "UPDATE t_deduct_application SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime" +
                ", complete_time =:completeTime, version =:version, total_count =:totalCount, executed_count =:executedCount WHERE id =:id AND version =:version";

        Map<String, Object> params = new HashMap<>();
        params.put("executionStatus", executionStatus);
        params.put("executionRemark", executionRemark);
        params.put("lastModifiedTime", lastModifiedTime);
        params.put("completeTime", completeTime);
        params.put("version", version);
        params.put("totalCount", totalCount);
        params.put("executedCount", executedCount);
        params.put("id", id);

        return new SqlAndParamTuple(update_sql, params);
    }

    @Override
    public String obtainAddCacheKey() {
        if (StringUtils.isEmpty(getDeductApplicationUuid())){
            throw new RuntimeException("all keys value is null.");
        }
        String result = FastDeductApplicationEnum.PREFIX_KEY;
        if (StringUtils.isNotEmpty(getDeductApplicationUuid())){
            result = result.concat(getDeductApplicationUuid()).concat(":");
        }else {
            result = result.concat(":");
        }
        if (StringUtils.isNotEmpty(getRequestNo())){
            result = result.concat(getRequestNo()).concat(":");
        }else {
            result = result.concat(":");
        }
        if (StringUtils.isNotEmpty(getDeductId())){
            result = result.concat(getDeductId());
        }else {
            result = result.concat(":");
        }
        return result;
    }

    @Override
    public SqlAndParamTuple obtainInsertSqlAndParam() {
        throw new RuntimeException("prohibited use:obtainInsertSqlAndParam");
    }

    @Override
    public String obtainQueryCheckMD5Sql(String updateSql) {
        throw new RuntimeException("prohibited use:obtainQueryCheckMD5Sql");
    }

    @Override
    public FastKey getColumnName() {
        return FastDeductApplicationEnum.DEDUCT_APPLICATION_UUID;
    }

    @Override
    public String getColumnValue() {
        return getDeductApplicationUuid();
    }

    @Override
    public List<String> obtainAddCacheKeyList() {
        return new ArrayList<String>() {
            private static final long serialVersionUID = -2637200652379763578L;

            {
                if (StringUtils.isNotBlank(getDeductApplicationUuid())) {
                    add(FastDeductApplicationEnum.PREFIX_KEY.concat(getDeductApplicationUuid()));
                }
                if (StringUtils.isNotBlank(getRequestNo())) {
                    add(FastDeductApplicationEnum.PREFIX_KEY.concat(getRequestNo()));
                }
                if (StringUtils.isNotBlank(getDeductId())){
                    add(FastDeductApplicationEnum.PREFIX_KEY.concat(getDeductId()));
                }
            }};
    }
}
