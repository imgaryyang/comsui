package com.zufangbao.cucumber.method.model;

import java.math.BigDecimal;

/**
 * Created by dzz on 17-5-11.
 */
public class RepurchaseDetailModel {
    private String uniqueId;
    private String contractNo;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal penaltyInterest;
    private BigDecimal repurchaseOtherFee;
    private BigDecimal amount;

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

    public BigDecimal getPenaltyInterest() {
        return penaltyInterest;
    }

    public void setPenaltyInterest(BigDecimal penaltyInterest) {
        this.penaltyInterest = penaltyInterest;
    }

    public BigDecimal getRepurchaseOtherFee() {
        return repurchaseOtherFee;
    }

    public void setRepurchaseOtherFee(BigDecimal repurchaseOtherFee) {
        this.repurchaseOtherFee = repurchaseOtherFee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
