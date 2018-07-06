/**
 * 
 */
package com.suidifu.hathaway.job;

/**
 * @author wukai
 *
 */
public enum JobType {
	
	Voucher("enum.job-type.voucher"),
	
	/**
	 * 商户代偿销账
	 */
	Account_Reconciliation_Subrogation("enum.job-type.account_reconciliation_subrogation"),
	
	Asset_Valuation("enum.job-type.asset_valuation"),
	
	Other("enum.job-type.other"),
	
	REPAYMENT_ORDER_PLACING("enum.job-type.repayment_oder_placing"),
	
	REPAYMENT_ORDER_CANCEL("enum.job-type.repayment_oder_cancel"),
	
	/**
	 * 还款订单商户代偿销账
	 */
	REPAYMENT_ORDER_RECONCILIATION_SUBROGATION("enum.job-type.account_reconciliation_subrogation"),
	
	/**
	 * 还款订单主动付款核销
	 */
	ACTIVE_VOUCHER_RECOVER("enum.job-type.repayment_oder_active_voucher"),
	
	Deduct_Plan_Clearing("enum.job-type.deduct_plan_clearing"),
	/**
	 * 滞留单商户销账
	 */
	TMP_DEPOSIT_VOUCHER_RECOVER("enum.job-type.tmp_deposit_voucher_recover"),
	
	/**
	 * 专户记账 
	 */
	SPECIAL_ACCOUNT_RECORD("enum.job-type.special_account_record"),
	
	CLEARING_VOUCHER_ACCOUNT_RECORD("enum.job-type.clearing_voucher_account_record"),

	/**
	 * 还款订单校验后佰仟数据同步
	 */
	REPAYMENT_ORDER_BQ_DATA_SYNC("enum.job-type.repayment_order_bq_data_sync"),

	
	/**
	 * 还款订单变更  落盘
	 */
	REPAYMENT_ORDER_MODIFY("enum.job-type.repayment_oder_modify");

	
	private JobType(String key) {
		this.key = key;
	}

	private String key;

	public String getKey() {
		return key;
	}
}
