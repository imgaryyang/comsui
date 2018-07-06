package com.zufangbao.testAPIWuBo.testAPI.models;

import java.math.BigDecimal;

/**
 * Created by wubo on 17-7-5.
 */
public class BusinessPaymentVoucherDetail {
    private String uniqueId = null;
    private String repaymentPlanNo = null;
    private BigDecimal amount = null;
    private int payer;
    private BigDecimal principal = null;
    private BigDecimal interest = null;
    private BigDecimal serviceCharge = null;
    private BigDecimal maintenanceCharge = null;
    private BigDecimal otherCharge = null;
    private BigDecimal penaltyFee = null;
    private BigDecimal latePenalty = null;
    private BigDecimal lateFee = null;
    private BigDecimal lateOtherCost =null;
    private String transactionTime = null;
    private String repayScheduleNo =null;
    private String currentPeriod = null;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public int getPayer() {
        return payer;
    }

    public void setPayer(int payer) {
        this.payer = payer;
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

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getRepayScheduleNo() {
        return repayScheduleNo;
    }

    public void setRepayScheduleNo(String repayScheduleNo) {
        this.repayScheduleNo = repayScheduleNo;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }
}
