package com.suidifu.dowjones.model;

import com.suidifu.dowjones.vo.enumeration.AuditOverdueStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AssetSet implements Serializable {
    private int overdueStatus = AuditOverdueStatus.NORMAL.getKey();
    private BigDecimal debitBalance;
    private BigDecimal creditBalance;
    private String thirdAccountName;
    private String secondAccountName;
    private String firstAccountName;
    private String thirdAccountUuid;
    private String secondAccountUuid;

    public boolean overDueStatus() {
        return AuditOverdueStatus.OVERDUE.getKey() == overdueStatus;
    }
}