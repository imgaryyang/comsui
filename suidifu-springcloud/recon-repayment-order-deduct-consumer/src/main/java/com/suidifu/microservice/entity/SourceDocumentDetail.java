package com.suidifu.microservice.entity;

import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_INTEREST_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_OTHER_FEE_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_OVERDUE_FEE_OBLIGATION_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_OVERDUE_FEE_OTHER_FEE_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_OVERDUE_FEE_SERVICE_FEE_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_PENALTY_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_PRINCIPAL_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_SERVICE_FEE_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_TECH_FEE_CODE;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.model.FastSourceDocumentDetail;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.suidifu.owlman.microservice.model.BusinessPaymentVoucherDetail;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.api.query.VoucherQueryApiResponseDetail;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAssetInfoModel;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBill;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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


    public SourceDocumentDetail(String sourceDocumentUuid, String firstNo, String secondType,
                                String secondNo, String receivableAccountNo, String paymentAccountNo,
                                String paymentName, String paymentBank, String financialContractUuid,
                                BusinessPaymentVoucherDetail voucherDetail, String voucherUuid, String repayScheduleNoMd5) {
        super();
        this.uuid = UUID.randomUUID().toString();
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.firstType = VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey();
        this.firstNo = firstNo;
        this.secondType = secondType;
        this.secondNo = secondNo;
        this.receivableAccountNo = receivableAccountNo;
        this.paymentAccountNo = paymentAccountNo;
        this.paymentName = paymentName;
        this.paymentBank = paymentBank;
        this.financialContractUuid = financialContractUuid;
        this.checkState = SourceDocumentDetailCheckState.UNCHECKED;
        this.status = SourceDocumentDetailStatus.UNSUCCESS;
        this.voucherUuid = voucherUuid;

        if (voucherDetail != null) {
            this.contractUniqueId = voucherDetail.getUniqueId();
            this.repaymentPlanNo = voucherDetail.getRepaymentPlanNo();
            this.amount = voucherDetail.getAmount();
            this.payer = voucherDetail.getVoucherPayer();
            this.setPrincipal(voucherDetail.getPrincipal());
            this.setInterest(voucherDetail.getInterest());
            this.setServiceCharge(voucherDetail.getServiceCharge());
            this.setMaintenanceCharge(voucherDetail.getMaintenanceCharge());
            this.setOtherCharge(voucherDetail.getOtherCharge());
            this.setPenaltyFee(voucherDetail.getPenaltyFee());
            this.setLatePenalty(voucherDetail.getLatePenalty());
            this.setLateFee(voucherDetail.getLateFee());
            this.setLateOtherCost(voucherDetail.getLateOtherCost());
            if (voucherDetail.getTransactionDate() != null) {
                this.actualPaymentTime = voucherDetail.getTransactionDate();
            }
            this.setRepayScheduleNo(repayScheduleNoMd5);
            this.setCurrentPeriod(voucherDetail.getCurrentPeriod());
            this.setOuterRepaymentPlanNo(voucherDetail.getRepayScheduleNo());
        }
    }

    public SourceDocumentDetail(BusinessPaymentVoucherDetail voucherDetail) {
        super();
        this.uuid = UUID.randomUUID().toString();
        this.firstType = VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey();
        this.checkState = SourceDocumentDetailCheckState.UNCHECKED;
        this.status = SourceDocumentDetailStatus.UNSUCCESS;

        if (voucherDetail != null) {
            this.contractUniqueId = voucherDetail.getUniqueId();
            this.payer = voucherDetail.getVoucherPayer();
            this.actualPaymentTime = voucherDetail.getTransactionDate();
            this.setOuterRepaymentPlanNo(voucherDetail.getRepayScheduleNo());
        }
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

    public SourceDocumentDetail(com.suidifu.owlman.microservice.model.SourceDocumentDetail detailModel) {
        this.id = detailModel.getId();
        this.uuid = detailModel.getUuid();
        this.sourceDocumentUuid = detailModel.getSourceDocumentUuid();
        this.contractUniqueId = detailModel.getContractUniqueId();
        this.repaymentPlanNo = detailModel.getRepaymentPlanNo();
        this.amount = detailModel.getAmount();
        this.status = detailModel.getStatus();
        this.firstType = detailModel.getFirstType();
        this.firstNo = detailModel.getFirstNo();
        this.secondType = detailModel.getSecondType();
        this.secondNo = detailModel.getSecondNo();
        this.payer = detailModel.getPayer();
        this.receivableAccountNo = detailModel.getReceivableAccountNo();
        this.paymentAccountNo = detailModel.getPaymentAccountNo();
        this.paymentName = detailModel.getPaymentName();
        this.paymentBank = detailModel.getPaymentBank();
        this.checkState = detailModel.getCheckState();
        this.comment = detailModel.getComment();
        this.financialContractUuid = detailModel.getFinancialContractUuid();
        this.principal = detailModel.getPrincipal();
        this.interest = detailModel.getInterest();
        this.serviceCharge = detailModel.getServiceCharge();
        this.maintenanceCharge = detailModel.getMaintenanceCharge();
        this.otherCharge = detailModel.getOtherCharge();
        this.penaltyFee = detailModel.getPenaltyFee();
        this.latePenalty = detailModel.getLatePenalty();
        this.lateFee = detailModel.getLateFee();
        this.lateOtherCost = detailModel.getLateOtherCost();
        this.voucherUuid = detailModel.getVoucherUuid();
        this.actualPaymentTime = detailModel.getActualPaymentTime();
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

    public SourceDocumentDetail(TransferBill transferBill,String sourceDocumentUuid,String contractUniqueId,Account account){
        super();
        this.uuid = UUID.randomUUID().toString();
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.contractUniqueId = contractUniqueId;
        this.amount = transferBill.getActualTotalAmount();
        this.status = SourceDocumentDetailStatus.SUCCESS;
        this.firstType = VoucherSource.TRANSFER_VOUCHE.getKey();
        this.firstNo = transferBill.getOrderUuid();
        this.secondType = VoucherType.TRANSFER.getKey();
//       this.secondNo = fastSourceDocumentDetail.getSecondNo();
        this.receivableAccountNo = transferBill.getCpBankCode();
        this.paymentAccountNo = transferBill.getAccountNo();
        if(account != null){
            this.paymentName = account.getAccountName();
            this.paymentBank = account.getBankName();
        }
        this.checkState = SourceDocumentDetailCheckState.CHECK_SUCCESS;
        this.comment = transferBill.getExecutionRemark();
        this.financialContractUuid = transferBill.getFinancialContractUuid();
        this.actualPaymentTime = transferBill.getCompleteTime();
    }

    public HashMap<String, BigDecimal> getDetailAmountMap() {

        HashMap<String, BigDecimal> detailAmountMap = new HashMap<>();
        if (detailAmount().compareTo(BigDecimal.ZERO) == 0) {
            return detailAmountMap;
        }
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, getPrincipal());
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, getInterest());
        detailAmountMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, getServiceCharge());
        detailAmountMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY, getMaintenanceCharge());
        detailAmountMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, getOtherCharge());
        detailAmountMap.put(ExtraChargeSpec.PENALTY_KEY, getPenaltyFee());
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, getLatePenalty());
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, getLateFee());
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, getLateOtherCost());

        detailAmountMap.put(ExtraChargeSpec.TOTAL_OVERDUE_FEE, getTotalOverduFee());

        return detailAmountMap;
    }

    public HashMap<String, BigDecimal> getRepurchaseDocDetailAmountMap() {

        HashMap<String, BigDecimal> detailAmountMap = new HashMap<>();
        if (repurchaseDetailAmount().compareTo(BigDecimal.ZERO) == 0) {
            return detailAmountMap;
        }
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_PRINCIPAL, getPrincipal());
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_INTEREST, getInterest());
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_PENALTY, getPenaltyFee());
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_OTHER_FEE, getOtherCharge());
        return detailAmountMap;
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

    public void setDetailAmount(Map<String, BigDecimal> detailAmountMap) {
        this.principal = detailAmountMap.getOrDefault(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, BigDecimal.ZERO);
        this.interest = detailAmountMap.getOrDefault(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, BigDecimal.ZERO);
        this.serviceCharge = detailAmountMap.getOrDefault(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, BigDecimal.ZERO);
        this.maintenanceCharge = detailAmountMap.getOrDefault(ExtraChargeSpec.LOAN_TECH_FEE_KEY, BigDecimal.ZERO);
        this.otherCharge = detailAmountMap.getOrDefault(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, BigDecimal.ZERO);
        this.penaltyFee = detailAmountMap.getOrDefault(ExtraChargeSpec.PENALTY_KEY, BigDecimal.ZERO);
        this.latePenalty = detailAmountMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, BigDecimal.ZERO);
        this.lateFee = detailAmountMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, BigDecimal.ZERO);
        this.lateOtherCost = detailAmountMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, BigDecimal.ZERO);
    }

    private HashMap<String, String> getDetailNameMap() {

        HashMap<String, String> detailAmountMap = new HashMap<>();
        if (detailAmount().compareTo(BigDecimal.ZERO) == 0) {
            return detailAmountMap;
        }
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, "还款本金");
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, "还款利息");
        detailAmountMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, "贷款服务费");
        detailAmountMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY, "技术维护费");
        detailAmountMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, "其他费用");
        detailAmountMap.put(ExtraChargeSpec.PENALTY_KEY, "罚息");
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, "逾期违约金");
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, "逾期服务费");
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, "逾期其他费用");

        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_PRINCIPAL, "回购本金");
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_INTEREST, "回购利息");
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_PENALTY, "回购罚息");
        detailAmountMap.put(ExtraChargeSpec.REPURCHASE_OTHER_FEE, "回购其他费用");

        return detailAmountMap;
    }

    private HashMap<String, Integer> getDetailErrorCodeMap() {

        HashMap<String, Integer> detailAmountMap = new HashMap<>();
        if (detailAmount().compareTo(BigDecimal.ZERO) == 0) {
            return detailAmountMap;
        }
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, VOUCHER_ERROR_OF_PRINCIPAL_CODE);
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, VOUCHER_ERROR_OF_INTEREST_CODE);
        detailAmountMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, VOUCHER_ERROR_OF_SERVICE_FEE_CODE);
        detailAmountMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY, VOUCHER_ERROR_OF_TECH_FEE_CODE);
        detailAmountMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, VOUCHER_ERROR_OF_OTHER_FEE_CODE);
        detailAmountMap.put(ExtraChargeSpec.PENALTY_KEY, VOUCHER_ERROR_OF_PENALTY_CODE);
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, VOUCHER_ERROR_OF_OVERDUE_FEE_OBLIGATION_CODE);
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, VOUCHER_ERROR_OF_OVERDUE_FEE_SERVICE_FEE_CODE);
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, VOUCHER_ERROR_OF_OVERDUE_FEE_OTHER_FEE_CODE);

        return detailAmountMap;
    }

    public Integer getDetailErrorCode(String key) {
        try {
            return getDetailErrorCodeMap().getOrDefault(key, VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            return VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE;
        }
    }

    public String getDetailAmountName(String key) {
        try {
            return getDetailNameMap().getOrDefault(key, StringUtils.EMPTY);
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public VoucherQueryApiResponseDetail getVoucherQueryApiResponseDetail() {
        try {
            VoucherQueryApiResponseDetail voucherQueryApiResponseDetail = new VoucherQueryApiResponseDetail(this.contractUniqueId, this.repaymentPlanNo, this.checkState.ordinal(), this.status.ordinal(), this.outerRepaymentPlanNo);
            List<Result> models = JsonUtils.parseArray(this.comment, Result.class);
            if (StringUtils.isEmpty(this.comment) || models == null) {
                voucherQueryApiResponseDetail.setErrors(Collections.emptyList());
            } else {
                List<String> errors = models.stream().map(Result::getCode).collect(Collectors.toList());
                errors.stream().sorted();
                voucherQueryApiResponseDetail.setErrors(errors);
            }
            return voucherQueryApiResponseDetail;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkTransactionTime(Date cashFlowTransactionTime, int days, Date contractBeginDate) {
        if (cashFlowTransactionTime == null) {
            return false;
        }
        int distanceDays = DateUtils.compareTwoDatesOnDay(cashFlowTransactionTime, getActualPaymentTime());
        if (distanceDays < 0) {
            return false;
        }
        if (distanceDays > days) {
            return false;
        }

        if (contractBeginDate != null && getActualPaymentTime().before(contractBeginDate)) {
            return false;
        }
        return true;
    }

}