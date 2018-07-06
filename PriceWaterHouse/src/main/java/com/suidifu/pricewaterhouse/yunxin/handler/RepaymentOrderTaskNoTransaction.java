package com.suidifu.pricewaterhouse.yunxin.handler;

public interface RepaymentOrderTaskNoTransaction {
	public void check_and_save_repayment_order_items();
	public void repaymentOrderGenerateThirdPartVoucherWithReconciliationTrap();

	public void modify_order_check_and_save_repayment_order_items();
}
