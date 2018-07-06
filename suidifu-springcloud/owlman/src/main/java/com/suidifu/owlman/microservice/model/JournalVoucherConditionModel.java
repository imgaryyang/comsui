package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JournalVoucherConditionModel {
    private String startTime;

    private String endTime;

    private String tradeNo;

    private String orderNo;

    private int journalVoucherStatus;

    private CashFlowChannelType cashFlowChannelType;

    private AccountSide accountSide;

    public JournalVoucherConditionModel(String startTime, String endTime,
                                        String tradeNo, String orderNo, int journalVoucherStatus) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.tradeNo = tradeNo;
        this.orderNo = orderNo;
        this.journalVoucherStatus = journalVoucherStatus;
    }

    public JournalVoucherConditionModel(String startTime, String endTime,
                                        String tradeNo, String orderNo, int journalVoucherStatus,
                                        CashFlowChannelType cashFlowChannelType, AccountSide accountSide) {
        this(startTime, endTime, tradeNo, orderNo, journalVoucherStatus);
        this.cashFlowChannelType = cashFlowChannelType;
        this.accountSide = accountSide;
    }
}