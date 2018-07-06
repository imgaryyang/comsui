package com.suidifu.owlman.microservice.spec;


import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Dec 27, 2016 5:44:02 PM
 * 类说明
 */
public class JournalVoucherMapSpec {

    //journalVoucher中的sourceDocumentIndentity是sourceDocumentUuid
    public static final Map<JournalVoucherType, Boolean> sourceDocumentUuidIsSourceDocumentIndentityMap = new HashMap<JournalVoucherType, Boolean>() {{
        put(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT, Boolean.TRUE);
        put(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_ROLL_BACK, Boolean.TRUE);
        put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, Boolean.TRUE);
        put(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, Boolean.TRUE);
        put(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE, Boolean.TRUE);
        put(JournalVoucherType.TRANSFER_BILL_INITIATIVE, Boolean.TRUE);
        put(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE, Boolean.TRUE);
        put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE, Boolean.TRUE);
        put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE, Boolean.TRUE);
        put(JournalVoucherType.TRANSFER_BILL_BY_GUARANTEE_VOUCHER, Boolean.TRUE);

        put(JournalVoucherType.ONLINE_DEDUCT_BACK_ISSUE, Boolean.FALSE);
        put(JournalVoucherType.OFFLINE_BILL_ISSUE, Boolean.FALSE);
        put(JournalVoucherType.BANK_CASHFLOW_DEPOSIT, Boolean.FALSE);
        put(JournalVoucherType.VIRTUAL_ACCOUNT_WITHDRAW_DEPOSIT, Boolean.FALSE);

    }};
}
