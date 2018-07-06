package com.suidifu.owlman.microservice.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("第三方还款明细")
public class ThirdPartVoucherRepayDetail {
    /**
     * 期数(选填)
     */
    private Integer currentPeriod;

    /**
     * 商户还款计划编号(MD5加密)
     */
    private String repayScheduleNo;

    private String repaymentPlanNo;

    private String outerRepaymentPlanNo;    //商户还款计划编号

    @NotNull(message = "[principal]不能为空")
    private BigDecimal principal;

    @NotNull(message = "[interest]不能为空")
    private BigDecimal interest;

    @NotNull(message = "[serviceCharge]不能为空")
    private BigDecimal serviceCharge;

    @NotNull(message = "[maintenanceCharge]不能为空")
    private BigDecimal maintenanceCharge;

    @NotNull(message = "[otherCharge]不能为空")
    private BigDecimal otherCharge;

    @NotNull(message = "[penaltyFee]不能为空")
    private BigDecimal penaltyFee;

    @NotNull(message = "[latePenalty]不能为空")
    private BigDecimal latePenalty;

    @NotNull(message = "[lateFee]不能为空")
    private BigDecimal lateFee;

    @NotNull(message = "[lateOtherCost]不能为空")
    private BigDecimal lateOtherCost;

    @NotNull(message = "[amount]不能为空")
    private BigDecimal amount;
}