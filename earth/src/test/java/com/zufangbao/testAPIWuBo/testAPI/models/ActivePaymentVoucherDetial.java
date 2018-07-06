package com.zufangbao.testAPIWuBo.testAPI.models;

import java.math.BigDecimal;

/**
 * Created by juxer on 17-7-6.
 */
public class ActivePaymentVoucherDetial {
    private String repaymentPlanNo = null;
    private BigDecimal amount = null;
    private BigDecimal principal = null;
    private BigDecimal interest = null;
    private BigDecimal serviceCharge = null;
    private BigDecimal mainternanceCharge = null;
    private BigDecimal otherCharge = null;
    private BigDecimal penaltyFee = null;
    private BigDecimal latePenalty = null;
    private BigDecimal lateFee = null;
    private BigDecimal lateOtherCost = null;
    private String uniqueId = null;
    private String contractNo = null;

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

    public BigDecimal getMainternanceCharge() {
        return mainternanceCharge;
    }

    public void setMainternanceCharge(BigDecimal mainternanceCharge) {
        this.mainternanceCharge = mainternanceCharge;
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(BigDecimal latePenalty) {
        this.latePenalty = latePenalty;
    }
}
