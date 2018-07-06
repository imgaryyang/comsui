package com.suidifu.microservice.handler;

public interface CashFlowChargeProxy {

	void charge_cash_into_virtual_account_and_recover(String cashFlowUuid, String contractUuid);

	void charge_cash_into_virtual_account_for_rpc(String cashFlowUuid, String contractUuid, int priority);

}
