package com.suidifu.giotto.model;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.keyenum.FastSourceDocumentDetailKeyEnum;
import com.suidifu.giotto.util.SqlAndParamTuple;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qinweichao on 2017/12/13.
 */
public class FastSourceDocumentDetail extends FastCacheObject{

    private long id;

    private String uuid;

    private String sourceDocumentUuid;

    private String contractUniqueId;

    private String repaymentPlanNo;

    private BigDecimal amount;

    private Integer status;

    /**
     * 凭证来源
     */
    private String firstType;
    private String firstNo;//商户付款凭证：接口请求编号; 第三方扣款凭证：deductApplicationUuid;
    /**
     * 凭证类型
     */
    private String secondType;
    private String secondNo;//商户付款凭证：外部打款流水号;第三方扣款凭证:deductApplicationDetailUuid;还款订单：itemUuid

    private Integer payer;

    /**
     * 收款账户号
     */
    private String receivableAccountNo;
    /**
     * 付款账户号
     */
    private String paymentAccountNo;
    /**
     * 付款银行帐户名称
     */
    private String paymentName;
    /**
     * 付款银行名称
     */
    private String paymentBank;

    /**
     * 校验状态
     */
    private Integer checkState = 0;

    /**
     * 备注（校验失败错误信息）
     */
    private String comment;

    private String financialContractUuid;

    /*****				明细相关字段				*****/
    /**
     * 还款本金
     */
    private BigDecimal principal = BigDecimal.ZERO;
    /**
     * 还款利息
     */
    private BigDecimal interest = BigDecimal.ZERO;
    /**
     * 贷款服务费
     */
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    /**
     * 技术维护费
     */
    private BigDecimal maintenanceCharge = BigDecimal.ZERO;
    /**
     * 其他费用
     */
    private BigDecimal otherCharge = BigDecimal.ZERO;
    /**
     * 罚息
     */
    private BigDecimal penaltyFee = BigDecimal.ZERO;
    /**
     * 逾期违约金
     */
    private BigDecimal latePenalty = BigDecimal.ZERO;
    /**
     * 逾期服务费
     */
    private BigDecimal lateFee = BigDecimal.ZERO;
    /**
     * 逾期其他费用
     */
    private BigDecimal lateOtherCost = BigDecimal.ZERO;

    /**
     * 凭证唯一标识uuid
     */
    private String voucherUuid;

    /**
     * 实际还款日期
     *
     * @return
     */
    private Date actualPaymentTime;

    /**
     * 商户还款编号(MD5加密)
     */
    private String repayScheduleNo;
    /**
     * 期数
     */
    private Integer currentPeriod;
    /**
     * 商户还款计划(明文)
     */
    private String outerRepaymentPlanNo;

    public FastSourceDocumentDetail() {
    }

    public FastSourceDocumentDetail(String uuid, String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
                                    BigDecimal amount, Integer status, String firstType, String firstNo, String secondType,
                                    String secondNo, Integer payer, String receivableAccountNo, String paymentAccountNo,
                                    String paymentName, String paymentBank, Integer checkState, String comment, String financialContractUuid,
                                    BigDecimal principal, BigDecimal interest, BigDecimal serviceCharge, BigDecimal maintenanceCharge,
                                    BigDecimal otherCharge, BigDecimal penaltyFee, BigDecimal latePenalty, BigDecimal lateFee,
                                    BigDecimal lateOtherCost, String voucherUuid, Date actualPaymentTime, String repayScheduleNo,
                                    Integer currentPeriod, String outerRepaymentPlanNo) {
        this.uuid = uuid;
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.contractUniqueId = contractUniqueId;
        this.repaymentPlanNo = repaymentPlanNo;
        this.amount = amount;
        this.status = status;
        this.firstType = firstType;
        this.firstNo = firstNo;
        this.secondType = secondType;
        this.secondNo = secondNo;
        this.payer = payer;
        this.receivableAccountNo = receivableAccountNo;
        this.paymentAccountNo = paymentAccountNo;
        this.paymentName = paymentName;
        this.paymentBank = paymentBank;
        this.checkState = checkState;
        this.comment = comment;
        this.financialContractUuid = financialContractUuid;
        this.principal = principal;
        this.interest = interest;
        this.serviceCharge = serviceCharge;
        this.maintenanceCharge = maintenanceCharge;
        this.otherCharge = otherCharge;
        this.penaltyFee = penaltyFee;
        this.latePenalty = latePenalty;
        this.lateFee = lateFee;
        this.lateOtherCost = lateOtherCost;
        this.voucherUuid = voucherUuid;
        this.actualPaymentTime = actualPaymentTime;
        this.repayScheduleNo = repayScheduleNo;
        this.currentPeriod = currentPeriod;
        this.outerRepaymentPlanNo = outerRepaymentPlanNo;
    }

    @Override
    public String obtainAddCacheKey() throws GiottoException {
        if (StringUtils.isEmpty(getUuid())){
            throw new GiottoException("all keys value is null.");
        }
        String result = FastSourceDocumentDetailKeyEnum.PREFIX_KEY;
        if (StringUtils.isEmpty(getUuid())){
            result = result.concat(":");
        }else {
            result = result.concat(getUuid()).concat(":");
        }
        return result;
    }

    @Override
    public SqlAndParamTuple obtainInsertSqlAndParam() {
        return null;
    }

    @Override
    public String obtainQueryCheckMD5Sql(String updateSql) {
        return null;
    }

    @Override
    public FastKey getColumnName() {
        return FastSourceDocumentDetailKeyEnum.UUID;
    }

    @Override
    public String getColumnValue() {
        return uuid;
    }

    @Override
    public List<String> obtainAddCacheKeyList() {
        return new ArrayList<String>() {{
            if (StringUtils.isNotEmpty(getUuid())){
                add(FastSourceDocumentDetailKeyEnum.PREFIX_KEY.concat(getUuid()));
            }
        }};
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSourceDocumentUuid() {
        return sourceDocumentUuid;
    }

    public void setSourceDocumentUuid(String sourceDocumentUuid) {
        this.sourceDocumentUuid = sourceDocumentUuid;
    }

    public String getContractUniqueId() {
        return contractUniqueId;
    }

    public void setContractUniqueId(String contractUniqueId) {
        this.contractUniqueId = contractUniqueId;
    }

    public String getRepaymentPlanNo() {
        return repaymentPlanNo;
    }

    public void setRepaymentPlanNo(String repaymentPlanNo) {
        this.repaymentPlanNo = repaymentPlanNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getFirstNo() {
        return firstNo;
    }

    public void setFirstNo(String firstNo) {
        this.firstNo = firstNo;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public String getSecondNo() {
        return secondNo;
    }

    public void setSecondNo(String secondNo) {
        this.secondNo = secondNo;
    }

    public Integer getPayer() {
        return payer;
    }

    public void setPayer(Integer payer) {
        this.payer = payer;
    }

    public String getReceivableAccountNo() {
        return receivableAccountNo;
    }

    public void setReceivableAccountNo(String receivableAccountNo) {
        this.receivableAccountNo = receivableAccountNo;
    }

    public String getPaymentAccountNo() {
        return paymentAccountNo;
    }

    public void setPaymentAccountNo(String paymentAccountNo) {
        this.paymentAccountNo = paymentAccountNo;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentBank() {
        return paymentBank;
    }

    public void setPaymentBank(String paymentBank) {
        this.paymentBank = paymentBank;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFinancialContractUuid() {
        return financialContractUuid;
    }

    public void setFinancialContractUuid(String financialContractUuid) {
        this.financialContractUuid = financialContractUuid;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getMaintenanceCharge() {
        return maintenanceCharge;
    }

    public void setMaintenanceCharge(BigDecimal maintenanceCharge) {
        this.maintenanceCharge = maintenanceCharge;
    }

    public BigDecimal getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(BigDecimal otherCharge) {
        this.otherCharge = otherCharge;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public BigDecimal getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(BigDecimal latePenalty) {
        this.latePenalty = latePenalty;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getLateOtherCost() {
        return lateOtherCost;
    }

    public void setLateOtherCost(BigDecimal lateOtherCost) {
        this.lateOtherCost = lateOtherCost;
    }

    public String getVoucherUuid() {
        return voucherUuid;
    }

    public void setVoucherUuid(String voucherUuid) {
        this.voucherUuid = voucherUuid;
    }

    public Date getActualPaymentTime() {
        return actualPaymentTime;
    }

    public void setActualPaymentTime(Date actualPaymentTime) {
        this.actualPaymentTime = actualPaymentTime;
    }

    public String getRepayScheduleNo() {
        return repayScheduleNo;
    }

    public void setRepayScheduleNo(String repayScheduleNo) {
        this.repayScheduleNo = repayScheduleNo;
    }

    public Integer getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(Integer currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getOuterRepaymentPlanNo() {
        return outerRepaymentPlanNo;
    }

    public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
        this.outerRepaymentPlanNo = outerRepaymentPlanNo;
    }
}
