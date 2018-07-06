package com.suidifu.owlman.microservice.enumation;

public enum ReconciliationType {
    /**
     * 代偿销账
     */
    RECONCILIATION_SUBROGATION("enum.reconciliation_type.reconciliation_subrogation"),
    /**
     * 回购销账
     */
    RECONCILIATION_REPURCHASE("enum.reconciliation_type.reconciliation_repurchase"),
    /**
     * 主动还款销账
     */
    RECONCILIATION_INITIATIVE("enum.reconciliation_type.reconciliation_initiative"),
    /**
     * 第三方扣款销账
     */
    RECONCILIATION_THIRDPARTY_DEDUCT("enum.reconciliation_type.reconciliation_thirdparty_deduct"),
    /**
     * 余额自动销账  结算单
     */
    RECONCILIATION_AUTO_RECOVER_SETTLEMENT_SHEET("enum.reconciliation_type.reconciliation_auto_recovier_settlement_sheet"),
    /**
     * 余额自动销账   担保单
     */
    RECONCILIATION_AUTO_RECOVER_GUARANTEE("enum.reconciliation_type.reconciliation_auto_recovier_guarantee"),

    /**
     * 主动还款充值
     */
    RECONCILIATION_ACTIVE_CHARGE("enum.reconciliation_type.reconciliation_active_charge"),
    /**
     * 客户账号充值
     */
    RECONCILIATION_CUSTOMER_CHARGE("enum.reconciliation_type.reconciliation_customer_charge"),
    /**
     * 商户充值
     */
    RECONCILIATION_MERCHANT_CHARGE("enum.reconciliation_type.reconciliation_merchant_charge"),
    /**
     * 担保凭证销账
     */
    RECONCILIATION_GUARANTEE_VOUCHER("enum.reconciliation_type.reconciliation_guarantee_voucher"),
    /**
     * 第三方扣款销账的撤销
     */
    RECONCILIATION_DEDUCT_REFUND("enum.reconciliation_type.reconciliation_deduct_refund"),

    /**
     * 清算凭证
     */
    RECONCILIATION_Clearing_Voucher("enum.reconciliation_type.reconciliation_clearing_voucher"),;

    private String key;


    /**
     * @param key
     */
    private ReconciliationType(String key) {
        this.key = key;

    }

    /**
     * @return the key
     */
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

    public boolean isRecoveredByVirtualAccountSelf() {
        return this == ReconciliationType.RECONCILIATION_INITIATIVE
                || this == ReconciliationType.RECONCILIATION_AUTO_RECOVER_SETTLEMENT_SHEET
                || this == ReconciliationType.RECONCILIATION_ACTIVE_CHARGE
                || this == ReconciliationType.RECONCILIATION_CUSTOMER_CHARGE
                ;
    }
}