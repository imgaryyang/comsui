package com.zufangbao.earth.yunxin.api.model.query;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by whb on 17-7-3.
 */
public class AccountTradeList {
    /**
     * 流水号
     */
    private String serialNo;
    /**
     * 借贷标记
     */
    private Integer accountSide;
    /**
     * 对方账户
     */
    private String counterAccountNo;
    /**
     * 对方账户名
     */
    private String counterAccountName;
    /**
     * 对方开户行
     */
    private String counterBankName;
    /**
     * 交易金额
     */
    private BigDecimal plannedAmount;
    /**
     * 入账时间
     */
    private Date transactionTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 附言
     */
    private String otherRemark;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 商户订单号
     */
    private String merchantOrderNo;

    public AccountTradeList(String serialNo,
                            Integer accountSide,
                            String counterAccountNo,
                            String counterAccountName,
                            String counterBankName,
                            BigDecimal plannedAmount,
                            Date transactionTime,
                            String remark,
                            String otherRemark,
                            String batchNo,
                            String merchantOrderNo) {
        this.serialNo = serialNo;
        this.accountSide = accountSide;
        this.counterAccountNo = counterAccountNo;
        this.counterAccountName = counterAccountName;
        this.counterBankName = counterBankName;
        this.plannedAmount = plannedAmount;
        this.transactionTime = transactionTime;
        this.remark = remark;
        this.otherRemark = otherRemark;
        this.batchNo = batchNo;
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getAccountSide() {
        return accountSide;
    }

    public void setAccountSide(Integer accountSide) {
        this.accountSide = accountSide;
    }

    public String getCounterAccountNo() {
        return counterAccountNo;
    }

    public void setCounterAccountNo(String counterAccountNo) {
        this.counterAccountNo = counterAccountNo;
    }

    public String getCounterAccountName() {
        return counterAccountName;
    }

    public void setCounterAccountName(String counterAccountName) {
        this.counterAccountName = counterAccountName;
    }

    public String getCounterBankName() {
        return counterBankName;
    }

    public void setCounterBankName(String counterBankName) {
        this.counterBankName = counterBankName;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public String getTransactionTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(transactionTime);
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOtherRemark() {
        return otherRemark;
    }

    public void setOtherRemark(String otherRemark) {
        this.otherRemark = otherRemark;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }
}
