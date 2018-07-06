package com.zufangbao.earth.report.wrapper.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.zufangbao.earth.report.wrapper.handler.ReportJobHandler;
import com.zufangbao.sun.entity.reportJob.ReportJob;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.service.ReportJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("reportJobHandler")
public class ReportJobHandlerImpl implements ReportJobHandler {
	
	@Autowired 
	private FinancialContractService financialContractService;
	
	@Autowired
	private  ReportJobService reportJobService;
	
	@Override
	public String createReportJob(String reportId, List<String> financialContractUuids, JSONObject reportParams, Long userId) {
		List<String> financialContractCodes = financialContractService.getFinancialContractCodes(financialContractUuids);
		ReportJob reportJob = new ReportJob(reportId, financialContractCodes, financialContractUuids, reportParams, userId);
		reportJobService.save(reportJob);
		return reportJob.getUuid();
	}
	
}
