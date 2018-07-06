package com.suidifu.microservice.model;

import com.suidifu.giotto.model.FastSourceDocumentDetail;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAssetInfoModel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

/**
 * 原始凭证明细
 *
 * @author louguanyang
 */
@Entity
@Table(name = "source_document_detail")
public class SourceDocumentDetail {

    @Id
    @GeneratedValue
    private long id;

    private String uuid;

    private String sourceDocumentUuid;

    private String contractUniqueId;

    private String repaymentPlanNo;

    private BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    private SourceDocumentDetailStatus status;

    /**
     * 凭证来源
     * {@link VoucherSource}.key
     */
    private String firstType;
    private String firstNo;//商户付款凭证：接口请求编号; 第三方扣款凭证：deductApplicationUuid;
    /**
     * 凭证类型
     * {@link VoucherType}.key
     */
    private String secondType;
    private String secondNo;//商户付款凭证：外部打款流水号;第三方扣款凭证:deductApplicationDetailUuid;还款订单：itemUuid

    @Enumerated(EnumType.ORDINAL)
    private VoucherPayer payer;

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
    @Enumerated(EnumType.ORDINAL)
    private SourceDocumentDetailCheckState checkState = SourceDocumentDetailCheckState.UNCHECKED;

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

    public SourceDocumentDetail() {
        super();
    }

    public SourceDocumentDetail(String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
                                BigDecimal amount, SourceDocumentDetailStatus status, String firstType, String firstNo, String secondType,
                                String secondNo, VoucherPayer payer, String receivableAccountNo, String paymentAccountNo,
                                String paymentName, String paymentBank, String financialContractUuid, String voucherUuid) {
        super();
        this.uuid = UUID.randomUUID().toString();
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
        this.checkState = SourceDocumentDetailCheckState.UNCHECKED;
        this.status = SourceDocumentDetailStatus.UNSUCCESS;
        this.financialContractUuid = financialContractUuid;
        this.voucherUuid = voucherUuid;
    }

    public SourceDocumentDetail(String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
                                BigDecimal amount, SourceDocumentDetailStatus status, String firstType, String firstNo, String secondType,
                                String secondNo, VoucherPayer payer, String receivableAccountNo, String paymentAccountNo,
                                String paymentName, String paymentBank, String financialContractUuid) {
        super();
        this.uuid = UUID.randomUUID().toString();
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.contractUniqueId = contractUniqueId;
        this.repaymentPlanNo = repaymentPlanNo;
        this.amount = amount;
        this.firstType = firstType;
        this.firstNo = firstNo;
        this.secondType = secondType;
        this.secondNo = secondNo;
        this.payer = payer;
        this.receivableAccountNo = receivableAccountNo;
        this.paymentAccountNo = paymentAccountNo;
        this.paymentName = paymentName;
        this.paymentBank = paymentBank;
        this.checkState = SourceDocumentDetailCheckState.CHECK_SUCCESS;
        this.status = status;
        this.financialContractUuid = financialContractUuid;
    }

    public SourceDocumentDetail(String sourceDocumentUuid, String contractUniqueId, String repaymentPlanNo,
                                BigDecimal amount, SourceDocumentDetailStatus status, String firstType, String firstNo, String secondType,
                                String secondNo, VoucherPayer payer, String receivableAccountNo, String paymentAccountNo,
                                String paymentName, String paymentBank, String financialContractUuid, VoucherCreateAssetInfoModel assetInfoModel) {
        super();
        this.uuid = UUID.randomUUID().toString();
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.contractUniqueId = contractUniqueId;
        this.repaymentPlanNo = repaymentPlanNo;
        this.amount = amount;
        this.firstType = firstType;
        this.firstNo = firstNo;
        this.secondType = secondType;
        this.secondNo = secondNo;
        this.payer = payer;
        this.receivableAccountNo = receivableAccountNo;
        this.paymentAccountNo = paymentAccountNo;
        this.paymentName = paymentName;
        this.paymentBank = paymentBank;
        this.checkState = SourceDocumentDetailCheckState.UNCHECKED;
        this.status = SourceDocumentDetailStatus.UNSUCCESS;
        this.financialContractUuid = financialContractUuid;

        if (null != assetInfoModel) {
            this.principal = assetInfoModel.getPrincipalValueBD();
            this.interest = assetInfoModel.getInterestValueBD();
            this.serviceCharge = assetInfoModel.getServiceChargeBD();
            this.otherCharge = assetInfoModel.getOtherChargeBD();
            this.maintenanceCharge = assetInfoModel.getMaintenanceChargeBD();
            this.penaltyFee = assetInfoModel.getOverduePenaltyFeeBD();
            this.latePenalty = assetInfoModel.getOverdueObligationFeeBD();
            this.lateFee = assetInfoModel.getOverdueServiceChargeBD();
            this.lateOtherCost = assetInfoModel.getOverdueOtherChargeBD();
        }
    }

    public SourceDocumentDetail(FastSourceDocumentDetail fastSourceDocumentDetail) {
        this.uuid = fastSourceDocumentDetail.getUuid();
        this.sourceDocumentUuid = fastSourceDocumentDetail.getSourceDocumentUuid();
        this.contractUniqueId = fastSourceDocumentDetail.getContractUniqueId();
        this.repaymentPlanNo = fastSourceDocumentDetail.getRepaymentPlanNo();
        this.amount = fastSourceDocumentDetail.getAmount();
        this.status = SourceDocumentDetailStatus.fromValue(fastSourceDocumentDetail.getStatus());
        this.firstType = fastSourceDocumentDetail.getFirstType();
        this.firstNo = fastSourceDocumentDetail.getFirstNo();
        this.secondType = fastSourceDocumentDetail.getSecondType();
        this.secondNo = fastSourceDocumentDetail.getSecondNo();
        this.payer = VoucherPayer.fromValue(fastSourceDocumentDetail.getPayer());
        this.receivableAccountNo = fastSourceDocumentDetail.getReceivableAccountNo();
        this.paymentAccountNo = fastSourceDocumentDetail.getPaymentAccountNo();
        this.paymentName = fastSourceDocumentDetail.getPaymentName();
        this.paymentBank = fastSourceDocumentDetail.getPaymentBank();
        this.checkState = SourceDocumentDetailCheckState.fromValue(fastSourceDocumentDetail.getCheckState());
        this.comment = fastSourceDocumentDetail.getComment();
        this.financialContractUuid = fastSourceDocumentDetail.getFinancialContractUuid();
        this.principal = fastSourceDocumentDetail.getPrincipal();
        this.interest = fastSourceDocumentDetail.getInterest();
        this.serviceCharge = fastSourceDocumentDetail.getServiceCharge();
        this.maintenanceCharge = fastSourceDocumentDetail.getMaintenanceCharge();
        this.otherCharge = fastSourceDocumentDetail.getOtherCharge();
        this.penaltyFee = fastSourceDocumentDetail.getPenaltyFee();
        this.latePenalty = fastSourceDocumentDetail.getLatePenalty();
        this.lateFee = fastSourceDocumentDetail.getLateFee();
        this.lateOtherCost = fastSourceDocumentDetail.getLateOtherCost();
        this.voucherUuid = fastSourceDocumentDetail.getVoucherUuid();
        this.actualPaymentTime = fastSourceDocumentDetail.getActualPaymentTime();
        this.repayScheduleNo = fastSourceDocumentDetail.getRepayScheduleNo();
        this.currentPeriod = fastSourceDocumentDetail.getCurrentPeriod();
        this.outerRepaymentPlanNo = fastSourceDocumentDetail.getOuterRepaymentPlanNo();
    }

    public BigDecimal repurchaseDetailAmount() {
        try {
            return BigDecimal.ZERO.add(principal).add(interest).add(otherCharge).add(penaltyFee);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getTotalOverduFee() {

        BigDecimal totalAmount;

        BigDecimal penaltyFee = this.penaltyFee;
        BigDecimal latePenalty = this.latePenalty;
        BigDecimal lateFee = this.lateFee;
        BigDecimal lateOtherCost = this.lateOtherCost;


        totalAmount = penaltyFee.add(latePenalty).add(lateFee).add(lateOtherCost);
        return totalAmount;
    }

    public Date getActualPaymentTime() {
        return actualPaymentTime;
    }

    public void setActualPaymentTime(Date actualPaymentTime) {
        this.actualPaymentTime = actualPaymentTime;
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

    public VoucherPayer getPayer() {
        return payer;
    }

    public void setPayer(VoucherPayer payer) {
        this.payer = payer;
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

    public SourceDocumentDetailStatus getStatus() {
        return status;
    }

    public void setStatus(SourceDocumentDetailStatus status) {
        this.status = status;
    }

    public SourceDocumentDetailCheckState getCheckState() {
        return checkState;
    }

    public void setCheckState(SourceDocumentDetailCheckState checkState) {
        this.checkState = checkState;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVoucherUuid() {
        return voucherUuid;
    }

    public void setVoucherUuid(String voucherUuid) {
        this.voucherUuid = voucherUuid;
    }

    public void appendComment(String msg) {
        StringBuffer buffer = new StringBuffer(comment);
        buffer.append(",");
        buffer.append(msg);
        this.comment = buffer.toString();
    }

    public boolean isUncheck() {
        return getCheckState() == SourceDocumentDetailCheckState.UNCHECKED;
    }

    public boolean is_business_payment_voucher() {
        return StringUtils.equals(VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey(), firstType);
    }

    public boolean is_third_party_deduction_voucher() {
        return StringUtils.equals(VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), firstType);
    }

    public boolean is_active_payment_voucher() {
        return StringUtils.equals(VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey(), firstType);
    }

    public boolean isInvalid() {
        return this.getStatus() == SourceDocumentDetailStatus.INVALID;
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

    public String getRepayScheduleNo() {
        return repayScheduleNo;
    }

    public void setRepayScheduleNo(String repayScheduleNo) {
        this.repayScheduleNo = repayScheduleNo;
    }

    public String getOuterRepaymentPlanNo() {
        return outerRepaymentPlanNo;
    }

    public void setOuterRepaymentPlanNo(String outerRepaymentPlanNo) {
        this.outerRepaymentPlanNo = outerRepaymentPlanNo;
    }

    public Integer getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(Integer currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public BigDecimal detailAmount() {
        try {
            return BigDecimal.ZERO.add(principal).add(interest).add(serviceCharge).add(maintenanceCharge).add(otherCharge)
                    .add(penaltyFee).add(latePenalty).add(lateFee).add(lateOtherCost);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }


}