package com.suidifu.owlman.microservice.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by qinweichao on 2018/1/3.
 */
@Data
public class VoucherAmountStatisticsModel {
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
     * 逾期服务费0
     */
    private BigDecimal lateFee = BigDecimal.ZERO;
    /**
     * 逾期其他费用
     */
    private BigDecimal lateOtherCost = BigDecimal.ZERO;
    /**
     * 逾期费用总计
     */
    private BigDecimal lateTotalAmount = BigDecimal.ZERO;
}