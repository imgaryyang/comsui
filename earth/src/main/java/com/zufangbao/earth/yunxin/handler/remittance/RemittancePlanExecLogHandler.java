package com.zufangbao.earth.yunxin.handler.remittance;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogExportModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogShowModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RemittancePlanExecLogHandler {
	
	List<RemittancePlanExecLogShowModel> queryShowModelList(RemittancePlanExecLogQueryModel queryModel, Page page);
	
	List<RemittancePlanExecLogExportModel> extractExecLogExportModel(String financialContractUuid, Date beginDate, Date endDate);
	
	void confirmWhetherExistCreditCashFlow(Long remittancePlanExecLogId);
	
	void confirmWhetherExistDebitCashFlow(Long remittancePlanExecLogId);
	
	Map<String, Object> dataStatistics(RemittancePlanExecLogQueryModel queryModel, int limit);
	
	String  updateRemittanceInfo(String remittancePlanUuid);
	
}
