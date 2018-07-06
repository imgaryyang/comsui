package com.suidifu.bridgewater.handler.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.handler.BatchDeductStatusQureyLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.BatchDeductStatusQueryLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.BatchDeductStatusQueryLog;



@Component("batchDeductStatusQureyLogHandler")
public class BatchDeductStatusQureyLogHandlerImpl implements BatchDeductStatusQureyLogHandler{

	private static final int MAX_LOG_LENGTH = 2000;
	@Autowired
	private BatchDeductStatusQueryLogService batchDeductStatusQueryLogService;
	
 
	@Override
	public void checkAndSaveRequest(BatchDeductStatusQueryModel queryModel, HttpServletRequest request)
			throws ApiException {
		
		if(queryModel == null || queryModel.getRequestNo() == null){
			return;
		} 
		
		String requestNo =  queryModel.getRequestNo();
		
		BatchDeductStatusQueryLog batchDeductStatusQueryLog = batchDeductStatusQueryLogService.getBatchDeductStatusQueryLogByReuqestNo(requestNo);
		
		if(batchDeductStatusQueryLog != null){
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}
		String deudctIdListLog = queryModel.getDeductIdList();
		
		if(deudctIdListLog!=null&&deudctIdListLog.length()>MAX_LOG_LENGTH){
			deudctIdListLog = deudctIdListLog.substring(0, MAX_LOG_LENGTH);
		}
		
		String repaymentPlanCodeListLog = queryModel.getRepaymentPlanCodeList();
		if(repaymentPlanCodeListLog!=null&&repaymentPlanCodeListLog.length()>MAX_LOG_LENGTH){
			repaymentPlanCodeListLog = repaymentPlanCodeListLog.substring(0,MAX_LOG_LENGTH);
		}
		
		batchDeductStatusQueryLog = new BatchDeductStatusQueryLog(queryModel.getRequestNo(),repaymentPlanCodeListLog,repaymentPlanCodeListLog
					,new Date(),IpUtil.getIpAddress(request));
		batchDeductStatusQueryLogService.save(batchDeductStatusQueryLog);
	}

}
