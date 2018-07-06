package com.suidifu.bridgewater.handler;

import javax.servlet.http.HttpServletRequest;

import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.zufangbao.gluon.exception.ApiException;

public interface BatchDeductStatusQureyLogHandler {
	
	public  void checkAndSaveRequest(BatchDeductStatusQueryModel queryModel, HttpServletRequest request) throws ApiException;

}
