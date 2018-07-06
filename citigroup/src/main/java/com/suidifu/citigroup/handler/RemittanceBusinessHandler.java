package com.suidifu.citigroup.handler;

import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;

public interface RemittanceBusinessHandler {
	
	public void handleRemittanceBusiness(String preProcessUrl,RemittanceCommandModel remittanceRequestModel, String ip, String callBackUrl);

}
