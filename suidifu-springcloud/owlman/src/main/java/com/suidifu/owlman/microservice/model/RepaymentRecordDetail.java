package com.suidifu.owlman.microservice.model;

import com.suidifu.owlman.microservice.enumation.SecondJournalVoucherType;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentRecordDetail {

    /**
     * 实际还款时间
     **/
    private Date actualRecycleDate;

    /**
     * 发生时间
     **/
    private Date happenDate;

    /** 延迟入账天数时间 **/
    private long delayDays;

    /**
     * 资金入账时间
     **/
    private Date accountedDate;

    /**
     * 业务编号
     **/
    private String repaymentPlanNo;

    /**
     * 商户还款计划编号
     **/
    private String outerRepaymentPlanNo;

    /**
     * 本次实收金额
     **/
    private BigDecimal totalFee = BigDecimal.ZERO;

    /**
     * 本金
     **/
    private BigDecimal loanAssetPrincipal = BigDecimal.ZERO;

    /**
     * 利息
     **/
    private BigDecimal loanAssetInterest = BigDecimal.ZERO;

    /**
     * 贷款服务费
     **/
    private BigDecimal loanServiceFee = BigDecimal.ZERO;

    /**
     * 技术服务费
     **/
    private BigDecimal loanTechFee = BigDecimal.ZERO;

    /**
     * 其他费用
     **/
    private BigDecimal loanOtherFee = BigDecimal.ZERO;

    /**
     * 逾期罚息
     **/
    private BigDecimal overdueFeePenalty = BigDecimal.ZERO;

    /**
     * 逾期违约金
     **/
    private BigDecimal overdueFeeObligation = BigDecimal.ZERO;

    /**
     * 逾期服务费
     **/
    private BigDecimal overdueFeeService = BigDecimal.ZERO;

    /**
     * 逾期其他费用
     **/
    private BigDecimal overdueFeeOther = BigDecimal.ZERO;

    /**
     * 资金类型
     **/
    private String capitalType;

    /**
     * 相关凭证
     **/
    private String voucherNo;

    /**
     * 回购本金金额
     */
    private BigDecimal repurchasePrincipal;

    /**
     * 回购利息金额
     */
    private BigDecimal repurchaseInterest;

    /**
     * 回购罚息金额
     */
    private BigDecimal repurchasePenalty;

    /**
     * 回购其他费用金额
     */
    private BigDecimal repurchaseOtherCharges;

    private Long id;

    private String assetSetUuid;

    private String uuid;

    /*
     * 计划还款日期
     */
    private Date planDate;

    /*
     * 支付通道
     */
    private String paymentGateway;

    private String contractNo;

    private String contractUuid;

    public RepaymentRecordDetail(RepaymentChargesDetail chargesDetail, Date actualRecycleDate, Date issuedTime, String singleLoanContractNo, String outerRepaymentPlanNo,
                                 String voucherNo, JournalVoucherType journalVoucherType, Date accountedDate, Long assetId, String assetSetUuid, String repurchaseUuid, Date planDate, String paymentGateway, BigDecimal totalAmount
            , SecondJournalVoucherType secondJournalVoucherType, String contractNo, String contractUuid) {
        super();

        this.actualRecycleDate = actualRecycleDate;
        this.voucherNo = voucherNo;
        this.happenDate = issuedTime;
        long days = issuedTime.getTime()-actualRecycleDate.getTime();
        this.delayDays =  days / (1000 * 60 * 60 * 24);
        this.repaymentPlanNo = singleLoanContractNo;
        this.outerRepaymentPlanNo = outerRepaymentPlanNo;
        this.planDate = planDate;
        this.paymentGateway = paymentGateway;
        if (accountedDate != null) {
            this.accountedDate = accountedDate;
        }
        if (journalVoucherType != null && journalVoucherType == JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER && secondJournalVoucherType != null) {
            this.capitalType = secondJournalVoucherType.getChineseName();
        } else {
            this.capitalType = journalVoucherType.getChineseName();
        }
        if (journalVoucherType != null && journalVoucherType == JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE) {
            this.repurchasePrincipal = chargesDetail.getLoanAssetPrincipal();
            this.repurchaseInterest = chargesDetail.getLoanAssetInterest();
            this.repurchasePenalty = chargesDetail.getOverdueFeePenalty();
            this.repurchaseOtherCharges = chargesDetail.getLoanOtherFee();
        } else {

            this.loanAssetPrincipal = chargesDetail.getLoanAssetPrincipal();
            this.loanAssetInterest = chargesDetail.getLoanAssetInterest();
            this.loanServiceFee = chargesDetail.getLoanServiceFee();
            this.loanTechFee = chargesDetail.getLoanTechFee();
            this.loanOtherFee = chargesDetail.getLoanOtherFee();
            this.overdueFeePenalty = chargesDetail.getOverdueFeePenalty();
            this.overdueFeeObligation = chargesDetail.getOverdueFeeObligation();
            this.overdueFeeService = chargesDetail.getOverdueFeeService();
            this.overdueFeeOther = chargesDetail.getOverdueFeeOther();
        }

        this.id = assetId;
        this.assetSetUuid = assetSetUuid;
        this.uuid = repurchaseUuid;
        this.totalFee = totalAmount;
        this.contractNo = contractNo;
        this.contractUuid = contractUuid;
    }
}