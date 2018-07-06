package com.suidifu.bridgewater.handler.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.handler.DeductApplicationLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationLog;



@Component("deductApplicationLogHandler")
public class DeductApplicationLogHandlerImpl implements DeductApplicationLogHandler{

	@Autowired
	DeductApplicationLogService deductApplicationLogService;
	
	@Override
	public void checkAndSaveRequest(OverdueDeductResultQueryModel queryModel, HttpServletRequest request) throws ApiException {
		
		String requestNo =  queryModel.getRequestNo();
		
		DeductApplicationLog deductApplicationLog = deductApplicationLogService.getDeductApplicationLogByReuqestNo(requestNo);
		
		if(deductApplicationLog != null){
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}
		
		DeductApplicationLog recordLog  = new DeductApplicationLog(queryModel.getDeductId(),queryModel.getContractNo(),queryModel.getRequestNo(),queryModel.getUniqueId(),IpUtil.getIpAddress(request));
		deductApplicationLogService.save(recordLog);
		
	}

}
