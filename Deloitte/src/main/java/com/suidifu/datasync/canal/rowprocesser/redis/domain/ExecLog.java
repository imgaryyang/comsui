package com.suidifu.datasync.canal.rowprocesser.redis.domain;

import com.suidifu.datasync.canal.StaticsConfig.ExecutionStatus;
import com.suidifu.datasync.canal.StaticsConfig.ResultType;
import com.suidifu.datasync.canal.StaticsConfig.ReverseStatus;

public class ExecLog {
	private String execid;
	private String tradeid;

	private int executionStatus = ExecutionStatus.未知.code();
	private int reverseStatus = ReverseStatus.未知.code();

	public ExecLog() {

	}

	public String getExecid() {
		return execid;
	}

	public void setExecid(String execid) {
		this.execid = execid;
	}

	public String getTradeid() {
		return tradeid;
	}

	public void setTradeid(String tradeid) {
		this.tradeid = tradeid;
	}

	public int getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(int executionStatus) {
		this.executionStatus = executionStatus;
	}

	public int getReverseStatus() {
		return reverseStatus;
	}

	public void setReverseStatus(int reverseStatus) {
		this.reverseStatus = reverseStatus;
	}

	public ResultType createResultType(boolean cashflow_exist_tradeid) {
		ResultType resultType = ResultType.未知;
		if (ExecutionStatus.处理中.eq(executionStatus)) {
			resultType = ResultType.本端多账;
		} else if (ExecutionStatus.成功.eq(executionStatus) && ReverseStatus.未发生.eq(reverseStatus)) {
			resultType = cashflow_exist_tradeid ? ResultType.平账 : ResultType.本端多账;
		} else if (ExecutionStatus.失败.eq(executionStatus) && ReverseStatus.未发生.eq(reverseStatus)) {
			resultType = cashflow_exist_tradeid ? ResultType.对端多账 : ResultType.平账;
		} else if (ExecutionStatus.失败.eq(executionStatus) && (ReverseStatus.未冲账.eq(reverseStatus) || ReverseStatus.已冲账.eq(reverseStatus))) {
			resultType = cashflow_exist_tradeid ? ResultType.平账 : ResultType.本端多账;
		}
		return resultType;
	}

	@Override
	public String toString() {
		return "ExecLog [execid=" + execid + ", tradeid=" + tradeid + ", executionStatus=" + executionStatus + ", reverseStatus=" + reverseStatus + "]";
	}

}
