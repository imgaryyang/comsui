package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanExcelVO;
import com.zufangbao.sun.yunxin.entity.model.loanbatch.LoanBatchQueryModel;
import com.zufangbao.sun.yunxin.entity.model.loanbatch.LoanBatchShowModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;

import java.util.List;
import java.util.Map;

public interface LoanBatchHandler {
	Map<String, List<String>> createExcelList(Long loanBatchId);

	void deleteLoanBatchData(Principal principal, String ip, Long loanBatchId) throws Exception;
	
	List<LoanBatchShowModel> generateLoanBatchShowModelList(LoanBatchQueryModel loanBatchQueryModel, Page page);
	
	void activateLoanBatch(Long loanBatchId) ;

	List<LoanBatch> queryLoanBatchs(LoanBatchQueryModel loanBatchQueryModel);
	
	void generateLoanBacthSystemLog(Principal principal,
                                    String ipAddress, LogFunctionType logFunctionType,
                                    LogOperateType logOperateType, Long loanBatchId) throws Exception;

	List<RepaymentPlanExcelVO> previewRepaymentPlanExcelVO(Long loanBatchId, int limit);

}
