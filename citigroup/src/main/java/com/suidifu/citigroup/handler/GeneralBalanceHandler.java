package com.suidifu.citigroup.handler;

import java.util.Map;

import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;

public interface GeneralBalanceHandler {

	void modifyBankSavingFreezeForFreezing(Map<String,String> receiveParams);
	
	public void modifyBankSavingFreezeForUnFreezing(Map<String, String> receiveParams);
	
	void  handleRemittanceBusiness(Map<String, String> receiveParams);
	
	void updatOrInsertBankSavingLoan(Map<String, String> receiveParams);
	
}
