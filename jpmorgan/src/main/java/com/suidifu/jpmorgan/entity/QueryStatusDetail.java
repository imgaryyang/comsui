package com.suidifu.jpmorgan.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qinweichao on 2017/8/27.
 */
public class QueryStatusDetail {

    /** 对端交易唯一编号 **/
    private String tradeUuid;

    /** 通道账户号 **/
    private String channelAccountNo;

    private BigDecimal transactionAmount;

    /** 交易状态：0: 队列中，1:处理中，2:对端处理中，3:成功，4:失败，5:撤销**/
    private BusinessStatus businessStatus;

    /** 交易失败原因 **/
    private String businessResultMsg;

    /** 交易成功时间 **/
    private Date businessSuccessTime;

    /** 交易流水号 **/
    private String channelSequenceNo;

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getChannelAccountNo() {
        return channelAccountNo;
    }

    public void setChannelAccountNo(String channelAccountNo) {
        this.channelAccountNo = channelAccountNo;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BusinessStatus getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(BusinessStatus businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getBusinessResultMsg() {
        return businessResultMsg;
    }

    public void setBusinessResultMsg(String businessResultMsg) {
        this.businessResultMsg = businessResultMsg;
    }

    public Date getBusinessSuccessTime() {
        return businessSuccessTime;
    }

    public void setBusinessSuccessTime(Date businessSuccessTime) {
        this.businessSuccessTime = businessSuccessTime;
    }

    public String getChannelSequenceNo() {
        return channelSequenceNo;
    }

    public void setChannelSequenceNo(String channelSequenceNo) {
        this.channelSequenceNo = channelSequenceNo;
    }

    public QueryStatusDetail() {
        super();
    }

    @JSONField(serialize = false)
    public Integer convertToDeductAppliationExecutionStatus() {
        //0: 队列中，1:处理中，2:对端处理中 --> 1: 处理中
        if(this.businessStatus == BusinessStatus.Inqueue || this.businessStatus == BusinessStatus.Processing || this.businessStatus == BusinessStatus.OppositeProcessing) {
            return 1;
        }
        // 3: 成功 --> 2: 成功
        if(this.businessStatus == BusinessStatus.Success) {
            return 2;
        }
        // 4:失败 --> 3:失败
        if(this.businessStatus == BusinessStatus.Failed) {
            return 3;
        }
        // 5:撤销 --> 5:撤销
        if(this.businessStatus == BusinessStatus.Abandon) {
            return 5;
        }
        return null;
    }

}
