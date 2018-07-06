package com.suidifu.owlman.microservice.model;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class JournalVoucherSearchModel {
    private String payAccountNo;
    private String payAccountName;

    private int journalVoucherStatus = -1;
    private int cashFlowChannelType = -1;
    private int accountSide = -1;

    private String startTime;
    private String endTime;

    public CashFlowChannelType getCashFlowChannelTypeEnum() {
        return CashFlowChannelType.fromValue(cashFlowChannelType);
    }

    public JournalVoucherStatus getJournalVoucherStatusEnum() {
        return JournalVoucherStatus.fromOrdinal(journalVoucherStatus);
    }

    public AccountSide getAccountSideEnum() {
        return AccountSide.fromValue(accountSide);
    }

    public Date getStartDate() {
        return startTime == null ? null : DateUtils.asDay(startTime);
    }

    public Date getEndDate() {
        return endTime == null ? null : DateUtils.asDay(endTime);
    }
}