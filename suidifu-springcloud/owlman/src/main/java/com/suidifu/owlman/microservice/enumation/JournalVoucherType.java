package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.BasicEnum;
import java.util.Arrays;
import java.util.List;

/**
 * 不要更改此枚举的顺序!
 */
public enum JournalVoucherType implements BasicEnum {

    /**
     * 0.线上扣款后台制证
     */
    ONLINE_DEDUCT_BACK_ISSUE("enum.journal-voucher-type.online-deduct-back-issue"),
    /**
     * 1.线下支付对账单
     */
    OFFLINE_BILL_ISSUE("enum.journal-voucher-type.offline-bill-issue"),
    /**
     * 2.银行充值凭证
     */
    BANK_CASHFLOW_DEPOSIT("enum.journal-voucher-type.bank-cashflow-deposit"),
    /**
     * 3.余额冲销还款计划
     */
    VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT("enum.journal-voucher-type.virtual-account-transfer-repayment"),
    /**
     * 4.余额支付退款
     */
    VIRTUAL_ACCOUNT_TRANSFER_ROLL_BACK("enum.journal-voucher-type.virtual-account-transfer-roll-back"),
    /**
     * 5.商户付款凭证冲销(委托代付)
     */
    TRANSFER_BILL_BY_VOUCHER("enum.journal-voucher-type.voucher-write-off-bill"),
    /**
     * 6.余额提现
     */
    VIRTUAL_ACCOUNT_WITHDRAW_DEPOSIT("enum.journal-voucher-type.virtual-account-withdraw-deposit"),
    /**
     * 7.第三方扣款凭证
     */
    THIRD_PARTY_DEDUCT_VOUCHER("enum.journal-voucher-type.third_party_deduct_voucher"),
    /**
     * 8.余额冲销担保
     */
    VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE("enum.journal-voucher-type.virtual-account-transfer-guarantee"),
    /**
     * 9.主动付款凭证冲销
     */
    TRANSFER_BILL_INITIATIVE("enum.journal-voucher-type.transfer_bill_initiative"),
    /**
     * 10.商户付款凭证冲销(回购)
     */
    TRANSFER_BILL_BY_REPURCHASE("enum.journal-voucher-type.voucher-write-off-bill-repurchase"),
    /**
     * 11.商户付款凭证冲销(代偿)
     */
    TRANSFER_BILL_BY_VOUCHER_ADVANCE("enum.journal-voucher-type.voucher-write-off-bill-advance"),
    /**
     * 12.商户付款担保凭证冲销(担保)
     */
    TRANSFER_BILL_BY_GUARANTEE_VOUCHER("enum.journal-voucher-type.voucher-write-off-bill-guarantee-voucher"),
    /**
     * 13.清算流水关联
     */
    TRANSFER_BILL_BY_CLEARING_CASH_FLOW("enum.journal-voucher-type.voucher-clearing-cash-flow"),
    /**
     * 14.商户付款凭证冲销(差额划拨)
     */
    TRANSFER_BILL_BY_VOUCHER_BALANCE("enum.journal-voucher-type.voucher-write-off-bill-balance"),
    /**
     * 15.专户放款凭证
     */
    REMITTANCE_VOUCHER("enum.journal-voucher-type.remittance-voucher"),
    /**
     * 16.清算流水凭证
     */
    TRANSFER_BILL_BY_CLEARING_DEDUCT_PLAN("enum.journal-voucher-type.voucher-clearing-deduct-plan"),
    /**
     * 17.进预收凭证
     */
    RECEIVABLE_IN_ADVANCE("enum.journal-voucher-type.receivable_in_advance"),
    /**
     * 18.转账凭证
     */
    TRANSFER_VOUCHER("enum.journal-voucher-type.transfer-voucher");

    private String key;


    /**
     * @param key
     */
    private JournalVoucherType(String key) {
        this.key = key;
    }

    public static List<JournalVoucherType> getJournalVoucherTypeOfVirtualAccount() {
        return Arrays.asList(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT, JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,
                JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE, JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE,
                JournalVoucherType.TRANSFER_BILL_INITIATIVE, JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE,
                JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE, JournalVoucherType.TRANSFER_BILL_BY_GUARANTEE_VOUCHER);
    }

    public static List<JournalVoucherType> getJournalVoucherTypeForRepaymentHistory() {
        return Arrays.asList(JournalVoucherType.ONLINE_DEDUCT_BACK_ISSUE, JournalVoucherType.OFFLINE_BILL_ISSUE,
                JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT, JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,
                JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, JournalVoucherType.TRANSFER_BILL_INITIATIVE, JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE
                , JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE);
    }

    public static List<JournalVoucherType> getJournalVoucherTypeForRepaymentRecord() {
        return Arrays.asList(JournalVoucherType.ONLINE_DEDUCT_BACK_ISSUE, JournalVoucherType.OFFLINE_BILL_ISSUE,
                JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT, JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,
                JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, JournalVoucherType.TRANSFER_BILL_INITIATIVE, JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE
                , JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE, JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE);
    }

    public static boolean JournalVoucherTypeIsOnline(JournalVoucherType journalVoucherType) {
        return journalVoucherType == JournalVoucherType.ONLINE_DEDUCT_BACK_ISSUE || journalVoucherType == JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER;
    }

    public static List<Integer> JournalVoucherTypeUsedSpecialAccount() {
        return Arrays.asList(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER.ordinal(), JournalVoucherType.TRANSFER_BILL_INITIATIVE.ordinal(),
                JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE.ordinal(), JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE.ordinal());
    }

    /**
     * @return the key
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Get alias of order status
     *
     * @return
     */
    public String getAlias() {
        return this.key.substring(this.key.lastIndexOf(".") + 1);
    }

    public boolean isJournalVoucherTypeOfVirtualAccount() {
        return getJournalVoucherTypeOfVirtualAccount().contains(this);
    }

    public boolean isVirtualAccountRemittanceBySelf() {
        return this == JournalVoucherType.TRANSFER_BILL_INITIATIVE || this == JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT;
    }
}