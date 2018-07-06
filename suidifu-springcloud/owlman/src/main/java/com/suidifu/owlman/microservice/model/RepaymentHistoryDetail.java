package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentHistoryDetail {
    /**
     * 实际还款时间
     **/
    private Date actualRecycleDate;

    /**
     * 总金额
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
     * 相关凭证
     **/
    private String voucherNo;

    public RepaymentHistoryDetail(RepaymentChargesDetail chargesDetail, Date actualRecycleDate, String voucherNo) {
        super();
        this.actualRecycleDate = actualRecycleDate;
        this.totalFee = chargesDetail.getTotalFee();
        this.loanAssetPrincipal = chargesDetail.getLoanAssetPrincipal();
        this.loanAssetInterest = chargesDetail.getLoanAssetInterest();
        this.loanServiceFee = chargesDetail.getLoanServiceFee();
        this.loanTechFee = chargesDetail.getLoanTechFee();
        this.loanOtherFee = chargesDetail.getLoanOtherFee();
        this.overdueFeePenalty = chargesDetail.getOverdueFeePenalty();
        this.overdueFeeObligation = chargesDetail.getOverdueFeeObligation();
        this.overdueFeeService = chargesDetail.getOverdueFeeService();
        this.overdueFeeOther = chargesDetail.getOverdueFeeOther();
        this.voucherNo = voucherNo;
    }
}