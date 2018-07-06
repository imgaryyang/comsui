package com.suidifu.citigroup.handler;

import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;

public interface StandardGeneralBalanceHandler {
	
	void insertGeneralBalance(BalanceRequestModel balanceRequestModel);
	
	void updatBankSavingLoan(BalanceRequestModel balanceRequestModel);
	
	void doUpdatOrInsertBankSavingLoan(BalanceRequestModel balanceRequestModel);
	
	void pushJobToRemittanceForBusinessValidation(RemittanceResponsePacket remittanceResponsePacket,
			boolean isSuccess, String failMessage);
}
