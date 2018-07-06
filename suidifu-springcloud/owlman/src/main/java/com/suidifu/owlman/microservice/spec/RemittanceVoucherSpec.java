package com.suidifu.owlman.microservice.spec;

import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;

import java.util.HashMap;
import java.util.Map;

public class RemittanceVoucherSpec {
    public static final Map<SourceDocumentDetailStatus, SourceDocumentStatus> SourceDocumentStatusConversion = new HashMap<SourceDocumentDetailStatus, SourceDocumentStatus>() {
        {
            put(SourceDocumentDetailStatus.UNSUCCESS, SourceDocumentStatus.CREATE);
            put(SourceDocumentDetailStatus.SUCCESS, SourceDocumentStatus.SIGNED);
            put(SourceDocumentDetailStatus.INVALID, SourceDocumentStatus.INVALID);
        }
    };
    public static final String TradeUuid = "TradeUuid";
    public static final String BankName = "BankName";
    public static final String RemittanceBillType = "RemittanceBillType";
    public static final String RemittancePlanExecLog = "RemittancePlanExecLog";
    public static final String RemittanceRefundBill = "RemittanceRefundBill";
    public static final String SystemBillIdentity = "SystemBillIdentity";
    public static final String PaymentInstitutionName = "PaymentInstitutionName";
    public static final String ContractUuid = "ContractUuid";
    public static final String ContractUniqueId = "ContractUniqueId";
    public static final String ContractNo = "ContractNo";
}