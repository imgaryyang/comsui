package com.suidifu.owlman.microservice.model;

import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SourceDocumentDetail {
    private SourceDocumentDetailStatus status;
    private SourceDocumentDetailCheckState checkState;
    private long id;
    private String uuid;
    private String sourceDocumentUuid;
    private String contractUniqueId;
    private String repaymentPlanNo;
    private BigDecimal amount;
    private String firstType;
    private String firstNo;
    private String secondType;
    private String secondNo;
    private VoucherPayer payer;
    private String receivableAccountNo;
    private String paymentAccountNo;
    private String paymentName;
    private String paymentBank;
    private String comment;
    private String financialContractUuid;
    private BigDecimal principal = BigDecimal.ZERO;
    private BigDecimal interest = BigDecimal.ZERO;
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    private BigDecimal maintenanceCharge = BigDecimal.ZERO;
    private BigDecimal otherCharge = BigDecimal.ZERO;
    private BigDecimal penaltyFee = BigDecimal.ZERO;
    private BigDecimal latePenalty = BigDecimal.ZERO;
    private BigDecimal lateFee = BigDecimal.ZERO;
    private BigDecimal lateOtherCost = BigDecimal.ZERO;
    private String voucherUuid;
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
}