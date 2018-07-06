package com.zufangbao.earth.yunxin.handler.reportform.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.handler.reportform.LoansReportFormHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansShowModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component("loansReportFormHandler")
public class LoansReportFormHandlerImpl implements LoansReportFormHandler{

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Override
	public List<LoansShowModel> query(LoansQueryModel queryModel, Page page) {
		if(queryModel == null || CollectionUtils.isEmpty(queryModel.getFinancialContractUuidList())) {
			return Collections.emptyList();
		}
		List<FinancialContract> financialContractList = financialContractService.getFinancialContractsByUuidsWithPage(queryModel.getFinancialContractUuidList(), page);
		List<LoansShowModel> showModels = new ArrayList<LoansShowModel>();
		for (FinancialContract financialContract : financialContractList) {
			BigDecimal beginningLoans = calculateBeginningLoans(financialContract, queryModel.getStartDateString());
			BigDecimal newLoans = calculateNewLoansPrincipal(financialContract, queryModel.getStartDateString(), queryModel.getEndDate());
			BigDecimal reduceLoans = calculateReduceLoansPrincipal(financialContract, queryModel.getStartDateString(), queryModel.getEndDate());
			BigDecimal endingLoans = beginningLoans.add(newLoans).subtract(reduceLoans);
			
			LoansShowModel loansShowModel = new LoansShowModel(financialContract, beginningLoans, newLoans, reduceLoans, endingLoans);
			showModels.add(loansShowModel);
		}
		return showModels;
	}

	/**
	 * 贷款规模管理-计算 本期减少本金总额
	 * @param financialContract
	 * @param startDateString
	 * @param endDate
	 * @return
	 */
	private BigDecimal calculateReduceLoansPrincipal(FinancialContract financialContract, String startDateString, Date endDate) {
		Date startDate = DateUtils.asDay(startDateString);
		return repaymentPlanService.calculateReduceLoansPrincipal(financialContract, startDate, endDate);
	}

	/**
	 * 贷款规模管理-计算 本期新增本金总额 
	 * @param financialContract
	 * @param startDateString
	 * @param endDate
	 * @return
	 */
	private BigDecimal calculateNewLoansPrincipal(FinancialContract financialContract, String startDateString, Date endDate) {
		Date startDate = DateUtils.asDay(startDateString);
		return contractService.calculateNewLoansPrincipal(financialContract, startDate, endDate);
	}

	private void validateStartDate(String startDateString) {
		if(StringUtils.isEmpty(startDateString) || DateUtils.asDay(startDateString) == null) {
			throw new RuntimeException("查询起始日期格式错误！");
		}
	}

	/**
	 * 贷款规模管理-计算 期初 贷款总本金余额
	 * @param financialContract
	 * @param startDateString
	 * @return
	 */
	private BigDecimal calculateBeginningLoans(FinancialContract financialContract, String startDateString) {
		validateStartDate(startDateString);
		Date startDate = DateUtils.asDay(startDateString);
		BigDecimal beginningPrincipal = contractService.calculateBeginningPrincipal(financialContract, startDate);
		BigDecimal beginningPaid = repaymentPlanService.calculateBeginningPaid(financialContract, startDate);
		return beginningPrincipal.subtract(beginningPaid);
	}

}
