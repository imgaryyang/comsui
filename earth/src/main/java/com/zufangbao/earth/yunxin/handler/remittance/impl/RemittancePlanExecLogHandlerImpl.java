package com.zufangbao.earth.yunxin.handler.remittance.impl;

import com.demo2do.core.web.resolver.Page;
import com.suidifu.coffer.handler.pab.PABDirectBankHandler;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.handler.remittance.RemittancePlanExecLogHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.yunxin.entity.remittance.*;
import com.zufangbao.sun.yunxin.service.remittance.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component("remittancePlanExecLogHandler")
public class RemittancePlanExecLogHandlerImpl extends BaseApiController implements RemittancePlanExecLogHandler {

	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;

	@Autowired
	private IRemittanceRefundBillService remittanceRefundBillService;

	@Autowired
	private CashFlowService cashFlowService;

	@Autowired
	PABDirectBankHandler pABDirectBankHandler;

	@Autowired
	IRemittancePlanService iRemittancePlanService;

	@Autowired
	IRemittanceApplicationService iRemittanceApplicationService;

	@Autowired
	IRemittanceApplicationDetailService iRemittanceApplicationDetailService;

	@Override
	public List<RemittancePlanExecLogShowModel> queryShowModelList(RemittancePlanExecLogQueryModel queryModel,
			Page page) {
		List<RemittancePlanExecLog> remittancePlanExecLogs = remittancePlanExecLogService
				.queryRemittancePlanExecLog(queryModel, page);
		
		List<String> remittanceApplicationUuids = new ArrayList<>();
		remittanceApplicationUuids = remittancePlanExecLogs.stream().map(a -> a.getRemittanceApplicationUuid()).collect(Collectors.toList());
		
		List<RemittanceApplication> remittanceApplications=iRemittanceApplicationService.getRemittanceApplicationsByUuids(remittanceApplicationUuids);
		Map<String, String> remittanceApplicationUuidAndRequestNo = new HashMap<>();
		remittanceApplicationUuidAndRequestNo =  remittanceApplications.stream().collect(Collectors.toMap(a -> a.getRemittanceApplicationUuid(),a -> a.getRequestNo()));
		
		List<RemittancePlanExecLogShowModel> showModels = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(remittancePlanExecLogs)) {
			for(RemittancePlanExecLog remittancePlanExecLog : remittancePlanExecLogs){
				RemittancePlanExecLogShowModel remittancePlanExecLogShowModel = new RemittancePlanExecLogShowModel(remittancePlanExecLog, queryModel.getExeclogType(), remittanceApplicationUuidAndRequestNo.get(remittancePlanExecLog.getRemittanceApplicationUuid()));
				showModels.add(remittancePlanExecLogShowModel);
			}
//			showModels = remittancePlanExecLogs.stream().map(a -> new RemittancePlanExecLogShowModel(a, queryModel.getExeclogType(), remittanceApplicationUuidAndRequestNo.get(a.getRemittanceApplicationUuid())))
//					.collect(Collectors.toList());
		}
		return showModels;
	}

	@Override
	public List<RemittancePlanExecLogExportModel> extractExecLogExportModel(String financialContractUuid,
			Date beginDate, Date endDate) {
		List<RemittancePlanExecLog> remittancePlanExecLogs = remittancePlanExecLogService
				.getRemittacncePlanExecLogsBy(financialContractUuid, beginDate, endDate);
		List<RemittancePlanExecLogExportModel> exportModels = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(remittancePlanExecLogs)) {
			exportModels = remittancePlanExecLogs.stream().map(a -> new RemittancePlanExecLogExportModel(a))
					.collect(Collectors.toList());
		}
		return exportModels;
	}

	@Override
	public void confirmWhetherExistCreditCashFlow(Long remittancePlanExecLogId) {
		RemittancePlanExecLog planExecLog = remittancePlanExecLogService.getById(remittancePlanExecLogId);
		if (planExecLog == null) {
			return;
		}
		List<CashFlow> cashFlows = cashFlowService.listCashFlowBy(planExecLog.getExecRspNo(), AccountSide.CREDIT,
				CashFlowChannelType.DirectBank);
		if (cashFlows != null && cashFlows.size() == 1) {
			CashFlow cf = cashFlows.get(0);
			if (planExecLog.matchInfoWithCreditCashFlow(cf)) {
				planExecLog.setReverseStatus(ReverseStatus.NOTREVERSE);
				planExecLog.setCreditCashFlowUuid(cf.getCashFlowUuid());
			}
		}
		planExecLog.setActualCreditCashFlowCheckNumber(planExecLog.getActualCreditCashFlowCheckNumber() + 1);
		remittancePlanExecLogService.update(planExecLog);
	}

	@Override
	public void confirmWhetherExistDebitCashFlow(Long remittancePlanExecLogId) {
		RemittancePlanExecLog planExecLog = remittancePlanExecLogService.getById(remittancePlanExecLogId);
		if (planExecLog == null) {
			return;
		}
		List<CashFlow> cashFlows = cashFlowService.listCashFlowBy(planExecLog.getExecRspNo(), AccountSide.DEBIT,
				CashFlowChannelType.DirectBank);
		if (cashFlows != null && cashFlows.size() == 1) {
			CashFlow cf = cashFlows.get(0);
			if (cf != null && AmountUtils.equals(cf.getTransactionAmount(), planExecLog.getPlannedAmount())) {
				RemittanceRefundBill refundBill = new RemittanceRefundBill(cf, planExecLog, planExecLog.getCreditCashFlowUuid());
				refundBill.setReverseType(ReverseType.REVERSE);
				remittanceRefundBillService.save(refundBill);

				planExecLog.setReverseStatus(ReverseStatus.REVERSED);
				planExecLog.setDebitCashFlowUuid(cf.getCashFlowUuid());
				planExecLog.setRemittanceRefundBillUuid(refundBill.getRemittanceRefundBillUuid());
				remittancePlanExecLogService.update(planExecLog);
			}
		}
	}

	@Override
	public Map<String, Object> dataStatistics(RemittancePlanExecLogQueryModel queryModel, int limit) {
		Map<String, Object> result = remittancePlanExecLogService.dataStatistics(queryModel);
		if (result.size() <= limit) {
			return result;
		}
		Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
		int cnt = 0;
		Long sum = new Long(0);
		for (Map.Entry<String, Object> map : result.entrySet()) {
			cnt++;
			if (cnt > limit) {
				sum = sum + (Long) map.getValue();
			} else {
				rtnMap.put(map.getKey(), map.getValue());
			}
		}
		rtnMap.put("其他", sum);
		return rtnMap;
	}


	public String  updateRemittanceInfo(String remittancePlanUuid){
		RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
		if (remittancePlan == null) {
			return "未找到对应的放款单";
		}
		RemittanceApplicationDetail remittanceApplicationDetail = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByUuid(remittancePlan.getRemittanceApplicationDetailUuid());
		if (remittanceApplicationDetail == null) {
			return "未找到对应的计划明细单";
		}
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittancePlan.getRemittanceApplicationUuid());
		if (remittanceApplication == null) {
			return "未找到对应的计划单";
		}

		//更新放款计划单
		List<RemittancePlanExecLog> rpelList = remittancePlanExecLogService
				.getRemittancePlanExecLogListBy(remittancePlan.getRemittancePlanUuid());
		List<RemittancePlanExecLog> creditSuccessPlanExecLogList = rpelList.stream()
				.filter(rp -> rp.getExecutionStatus() == ExecutionStatus.SUCCESS
						&& rp.getReverseStatus() != ReverseStatus.REFUND)
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(creditSuccessPlanExecLogList)) {
			remittancePlan.setExecutionStatus(ExecutionStatus.FAIL);
		} else if (creditSuccessPlanExecLogList.size() == 1) {
			remittancePlan.setExecutionStatus(ExecutionStatus.SUCCESS);
		} else {
			remittancePlan.setExecutionStatus(ExecutionStatus.ABNORMAL);
		}
		iRemittancePlanService.update(remittancePlan);

		//更新放款明细单
		List<RemittancePlan> planList = iRemittancePlanService.getRemittancePlanListByRemittanceApplicationDetailUuid(remittanceApplicationDetail.getRemittanceApplicationDetailUuid());
		List<ExecutionStatus> executionStatusList = planList.stream().map(rp->rp.getExecutionStatus()).collect(Collectors.toList());
		List<RemittancePlan> creditSuccessPlanList = planList.stream().filter(rp->rp.isCreditSuccess()).collect(Collectors.toList());
		BigDecimal actualTotalAmount = BigDecimal.ZERO;
		for (RemittancePlan creditSuccessPlan : creditSuccessPlanList) {
			actualTotalAmount = actualTotalAmount.add(creditSuccessPlan.getActualTotalAmount());
		}
		//获取 综合状态
		ExecutionStatus finalStatus = doEvaluateFinalStatus(executionStatusList);
		remittanceApplicationDetail.setActualTotalAmount(actualTotalAmount);
		remittanceApplicationDetail.setExecutionStatus(finalStatus);
		iRemittanceApplicationDetailService.update(remittanceApplicationDetail);

		//更新放款申请单
		List<RemittanceApplicationDetail> applicationDetailList = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid(remittanceApplication.getRemittanceApplicationUuid());
		executionStatusList = applicationDetailList.stream().map(rp->rp.getExecutionStatus()).collect(Collectors.toList());
		List<RemittanceApplicationDetail> creditSuccessApplicationDetail = applicationDetailList.stream().filter(rp->rp.isCreditSuccess()).collect(Collectors.toList());
		actualTotalAmount = BigDecimal.ZERO;
		for (RemittanceApplicationDetail rApplicationDetail : creditSuccessApplicationDetail) {
			actualTotalAmount.add(rApplicationDetail.getActualTotalAmount());
		}
		finalStatus = doEvaluateFinalStatus(executionStatusList);
		remittanceApplication.setActualTotalAmount(actualTotalAmount);
		remittanceApplication.setExecutionStatus(finalStatus);
		iRemittanceApplicationService.update(remittanceApplication);

		return "放款信息更新成功";
	}

	private ExecutionStatus doEvaluateFinalStatus(List<ExecutionStatus> executionStatusList) {
		ExecutionStatus[] statusListForAllMatch = {ExecutionStatus.SUCCESS, ExecutionStatus.FAIL, ExecutionStatus.ABANDON, ExecutionStatus.ABNORMAL};
		//所有成功，失败，或者撤销
		for (ExecutionStatus executionStatus : statusListForAllMatch) {
			boolean isAllMatch = executionStatusList.stream().allMatch(s-> s == executionStatus);
			if(isAllMatch) {
				return executionStatus;
			}
		}
		//部分成功
		boolean isPartSuccess = executionStatusList.stream().anyMatch(s-> s == ExecutionStatus.SUCCESS);
		if(isPartSuccess) {
			return ExecutionStatus.ABNORMAL;
		}
		return ExecutionStatus.FAIL;
	}

}