package com.suidifu.bridgewater.handler;

import javax.servlet.http.HttpServletRequest;

import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.zufangbao.gluon.exception.ApiException;

public interface DeductApplicationLogHandler {

	
	public  void checkAndSaveRequest(OverdueDeductResultQueryModel queryModel, HttpServletRequest request) throws ApiException;
}
