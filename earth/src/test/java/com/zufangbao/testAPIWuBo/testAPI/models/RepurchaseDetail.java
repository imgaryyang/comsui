package com.zufangbao.testAPIWuBo.testAPI.models;

import java.math.BigDecimal;

/**
 * Created by Cool on 2017/7/18.
 */
public class RepurchaseDetail {
    private String uniqueId = null;
    private String contractNo = null;
    private BigDecimal principal = null;
    private BigDecimal interest = null;
    private BigDecimal penaltyInterest =null;
    private BigDecimal repurchaseOtherFee = null;
    private BigDecimal amount = null;

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
