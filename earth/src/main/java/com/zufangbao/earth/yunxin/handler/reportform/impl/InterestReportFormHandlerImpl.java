package com.zufangbao.earth.yunxin.handler.reportform.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.handler.reportform.InterestReportFormHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestShowModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component("interestReportFormHandler")
public class InterestReportFormHandlerImpl implements InterestReportFormHandler{

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Override
	public List<InterestShowModel> query(InterestQueryModel queryModel, Page page) {
		if(queryModel == null || CollectionUtils.isEmpty(queryModel.getFinancialContractUuidList())) {
			return Collections.emptyList();
		}
		List<FinancialContract> financialContractList = financialContractService.getFinancialContractsByUuidsWithPage(queryModel.getFinancialContractUuidList(), page);
		List<InterestShowModel> showModels = new ArrayList<InterestShowModel>();
		for (FinancialContract financialContract : financialContractList) {
			BigDecimal beginningInterest = calculateBeginningInterest(financialContract, queryModel.getStartDateString());
			BigDecimal newInterest = calculateNewInterest(financialContract, queryModel.getStartDateString(), queryModel.getEndDate());
			BigDecimal reduceInterest = calculateReduceInterest(financialContract, queryModel.getStartDateString(), queryModel.getEndDate());
			BigDecimal endingInterest = beginningInterest.add(newInterest).subtract(reduceInterest);
			
			InterestShowModel showModel = new InterestShowModel(financialContract, beginningInterest, newInterest, reduceInterest, endingInterest);
			showModels.add(showModel);
		}		
		return showModels;
	}

	private BigDecimal calculateReduceInterest(FinancialContract financialContract, String startDateString, Date endDate) {
		Date startDate = DateUtils.asDay(startDateString);
		return repaymentPlanService.calculateReduceInterest(financialContract, startDate, endDate);
	}

	private BigDecimal calculateNewInterest(FinancialContract financialContract, String startDateString, Date endDate) {
		Date startDate = DateUtils.asDay(startDateString);
		return repaymentPlanService.calculateNewInterest(financialContract, startDate, endDate);
	}

	/**
	 * 计算期初对应的应收利息数额
	 * @param financialContract
	 * @param startDateString
	 * @return
	 */
	private BigDecimal calculateBeginningInterest(FinancialContract financialContract, String startDateString) {
		validateStartDate(startDateString);
		Date startDate = DateUtils.asDay(startDateString);
		return repaymentPlanService.calculateBeginningInterest(financialContract, startDate);
	}
	
	private void validateStartDate(String startDateString) {
		if(StringUtils.isEmpty(startDateString) || DateUtils.asDay(startDateString) == null) {
			throw new RuntimeException("查询起始日期格式错误！");
		}
	}
}
