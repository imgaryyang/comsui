package com.suidifu.bridgewater;

import com.suidifu.bridgewater.fast.FastRemittanceApplication;
import com.suidifu.bridgewater.fast.FastRemittanceApplicationDetail;
import com.suidifu.bridgewater.fast.FastRemittancePlan;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class RemittanceContext {
	private QueryStatusResult queryStatusResult;

	private FastRemittanceApplication fastRemittanceApplication;

	private FastRemittancePlan fastRemittancePlan;

	private FastRemittanceApplicationDetail fastRemittanceApplicationDetail;

	private List<String> remittancePlanUuidList;

	private List<String> remittanceApplicationDetailsUuidList;

	private boolean isAllPlanFinished;

	private boolean isAllDetailFinished;

	private String latestExecReqNo;

	public RemittanceContext() {
	}

	public RemittanceContext(FastRemittancePlan remittancePlan,
	                         FastRemittanceApplicationDetail remittanceApplicationDetail, FastRemittanceApplication remittanceApplication,
	                         List<String> remittancePlanUuidList, List<String> remittanceApplicationDetailsUuidList, boolean isAllPlanFinished, boolean
			                         isAllDetailFinished, QueryStatusResult queryStatusResult, String latestExecReqNo) {
		this.fastRemittancePlan = remittancePlan;
		this.fastRemittanceApplicationDetail = remittanceApplicationDetail;
		this.fastRemittanceApplication = remittanceApplication;
		this.remittancePlanUuidList = remittancePlanUuidList;
		this.remittanceApplicationDetailsUuidList = remittanceApplicationDetailsUuidList;
		this.isAllPlanFinished = isAllPlanFinished;
		this.isAllDetailFinished = isAllDetailFinished;
		this.queryStatusResult = queryStatusResult;
		this.latestExecReqNo = latestExecReqNo;
	}

	public RemittanceContext(QueryStatusResult queryStatusResult) {
		this.queryStatusResult = queryStatusResult;
	}

	public void validQueryResutAndRemittanceInfo() {
		if (!this.queryStatusResult.isFinish()) {
			throw new ApiException("queryStatusResult_is_not_finished");
		}
		if (fastRemittancePlan.getExecutionStatus() != ExecutionStatus.PROCESSING.ordinal() || fastRemittanceApplication.getExecutionStatus() != ExecutionStatus
				.PROCESSING.ordinal() || fastRemittanceApplicationDetail.getExecutionStatus() != ExecutionStatus.PROCESSING.ordinal()) {
			throw new ApiException("remittance_status_is_prossing");
		}
		if (StringUtils.isEmpty(latestExecReqNo) || (!latestExecReqNo.equals(queryStatusResult.getSourceMessageUuid()))) {
			throw new ApiException("latestExecReqNo_error");
		}
		if (this.isAllPlanFinished == true) {
			if (CollectionUtils.isEmpty(this.getRemittancePlanUuidList()) || this.getRemittancePlanUuidList().size() != this.fastRemittanceApplicationDetail
					.getTotalCount()) {
				throw new ApiException(ApiResponseCode.REMITTANCE_PLAN_COUNT_ERROR, "remittance_plan_count_error");
			}
		}
		if (this.isAllDetailFinished == true) {
			if (CollectionUtils.isEmpty(this.getRemittanceApplicationDetailsUuidList()) || this.getRemittanceApplicationDetailsUuidList().size() != this
					.fastRemittanceApplication.getTotalCount()) {
				throw new ApiException(ApiResponseCode.REMITTANCE_APPLICATION_DETAIL_COUNT_ERROR, "remittance_application_detail_count_error");
			}
		}
	}

	public QueryStatusResult getQueryStatusResult() {
		return queryStatusResult;
	}

	public void setQueryStatusResult(QueryStatusResult queryStatusResult) {
		this.queryStatusResult = queryStatusResult;
	}

	public FastRemittanceApplication getFastRemittanceApplication() {
		return fastRemittanceApplication;
	}

	public void setFastRemittanceApplication(FastRemittanceApplication fastRemittanceApplication) {
		this.fastRemittanceApplication = fastRemittanceApplication;
	}

	public FastRemittancePlan getFastRemittancePlan() {
		return fastRemittancePlan;
	}

	public void setFastRemittancePlan(FastRemittancePlan fastRemittancePlan) {
		this.fastRemittancePlan = fastRemittancePlan;
	}

	public FastRemittanceApplicationDetail getFastRemittanceApplicationDetail() {
		return fastRemittanceApplicationDetail;
	}

	public void setFastRemittanceApplicationDetail(FastRemittanceApplicationDetail fastRemittanceApplicationDetail) {
		this.fastRemittanceApplicationDetail = fastRemittanceApplicationDetail;
	}

	public List<String> getRemittancePlanUuidList() {
		return remittancePlanUuidList;
	}

	public void setRemittancePlanUuidList(List<String> remittancePlanUuidList) {
		this.remittancePlanUuidList = remittancePlanUuidList;
	}

	public List<String> getRemittanceApplicationDetailsUuidList() {
		return remittanceApplicationDetailsUuidList;
	}

	public void setRemittanceApplicationDetailsUuidList(List<String> remittanceApplicationDetailsUuidList) {
		this.remittanceApplicationDetailsUuidList = remittanceApplicationDetailsUuidList;
	}

	public boolean isAllPlanFinished() {
		return isAllPlanFinished;
	}

	public void setAllPlanFinished(boolean isAllPlanFinished) {
		this.isAllPlanFinished = isAllPlanFinished;
	}

	public boolean isAllDetailFinished() {
		return isAllDetailFinished;
	}

	public void setAllDetailFinished(boolean isAllDetailFinished) {
		this.isAllDetailFinished = isAllDetailFinished;
	}

	public String getLatestExecReqNo() {
		return latestExecReqNo;
	}

	public void setLatestExecReqNo(String latestExecReqNo) {
		this.latestExecReqNo = latestExecReqNo;
	}

}
