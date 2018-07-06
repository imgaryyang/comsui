package com.suidifu.morganstanley.handler;

import javax.servlet.http.HttpServletRequest;

import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.api.model.transfer.TransferApplicationReqModel;

public interface TransferBillNoSession {
	
	public String receiveTransferBillCallback(HttpServletRequest request);

	void transferInfoCheck(TransferApplicationReqModel reqModel) throws ApiException;

}
