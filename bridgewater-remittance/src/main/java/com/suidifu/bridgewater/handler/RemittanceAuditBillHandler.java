package com.suidifu.bridgewater.handler;

import java.util.Date;

public interface RemittanceAuditBillHandler {

	void generateAuditBill(String financialContractUuid, Date date);
}
