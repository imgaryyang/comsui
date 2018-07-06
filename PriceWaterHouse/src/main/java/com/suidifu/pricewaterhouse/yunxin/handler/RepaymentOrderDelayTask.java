package com.suidifu.pricewaterhouse.yunxin.handler;

public interface RepaymentOrderDelayTask {

	void repaymentOrderDataSync();

	void registerDataSyncJob(String repaymentOrderUuid);
}
