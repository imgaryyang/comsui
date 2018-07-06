package com.suidifu.bridgewater.handler.impl;

import com.suidifu.bridgewater.handler.RemittancePlanExecLogHandler;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.sun.yunxin.entity.remittance.*;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component("remittancePlanExecLogHandler")
public class RemittancePlanExecLogHandlerImpl implements RemittancePlanExecLogHandler {
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;

	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;

	@Autowired
	private IRemittanceApplicationDetailService iRemittanceApplicationDetailService;

	@Autowired
	private IRemittancePlanService iRemittancePlanService;

	@Autowired
	public JpmorganApiHelper jpmorganApiHelper;

	@Override
	public String updateRemittanceInfo(String remittancePlanUuid) {
		RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
		if (remittancePlan == null) {
			return "未找到对应的放款单";
		}
		RemittanceApplicationDetail remittanceApplicationDetail = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByUuid(remittancePlan
				.getRemittanceApplicationDetailUuid());
		if (remittanceApplicationDetail == null) {
			return "未找到对应的计划明细单";
		}
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittancePlan.getRemittanceApplicationUuid
				());
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
		List<RemittancePlan> planList = iRemittancePlanService.getRemittancePlanListByRemittanceApplicationDetailUuid(remittanceApplicationDetail
				.getRemittanceApplicationDetailUuid());
		List<ExecutionStatus> executionStatusList = planList.stream().map(rp -> rp.getExecutionStatus()).collect(Collectors.toList());
		List<RemittancePlan> creditSuccessPlanList = planList.stream().filter(rp -> rp.isCreditSuccess()).collect(Collectors.toList());
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
		List<RemittanceApplicationDetail> applicationDetailList = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid
				(remittanceApplication.getRemittanceApplicationUuid());
		executionStatusList = applicationDetailList.stream().map(rp -> rp.getExecutionStatus()).collect(Collectors.toList());
		List<RemittanceApplicationDetail> creditSuccessApplicationDetail = applicationDetailList.stream().filter(rp -> rp.isCreditSuccess()).collect(Collectors
				.toList());
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
			boolean isAllMatch = executionStatusList.stream().allMatch(s -> s == executionStatus);
			if (isAllMatch) {
				return executionStatus;
			}
		}
		//部分成功
		boolean isPartSuccess = executionStatusList.stream().anyMatch(s -> s == ExecutionStatus.SUCCESS);
		if (isPartSuccess) {
			return ExecutionStatus.ABNORMAL;
		}
		return ExecutionStatus.FAIL;
	}
}
