package com.suidifu.datasync.canal.rowprocesser.redis.domain;

import com.suidifu.datasync.canal.StaticsConfig.ResultType;
import com.suidifu.datasync.entity.RemittanceAuditResult;

public class Result extends RemittanceAuditResult {
	protected String idv;

	public Result() {
	}

	public Result(String idv, String tradeId, ResultType resultType, String systemBillIdentity, int systemBillType, String financialContractUuid,
			String paymentChannelUuid, String systemBillOccurDate, String cashFlowIdentity, String hostAccountNo, String cashFlowTransactionTime) {
		this.idv = idv;
		this.tradeId = tradeId;
		this.systemBillIdentity = systemBillIdentity;
		this.systemBillType = systemBillType;
		this.financialContractUuid = financialContractUuid;
		this.paymentChannelUuid = paymentChannelUuid;
		this.systemBillOccurDate = systemBillOccurDate;
		this.cashFlowIdentity = cashFlowIdentity;
		this.hostAccountNo = hostAccountNo;
		this.cashFlowTransactionTime = cashFlowTransactionTime;
		this.setResultType(resultType);
	}

	public String getIdv() {
		return idv;
	}

	public void setIdv(String idv) {
		this.idv = idv;
	}

	public void setResultType(ResultType resultType) {
		if (resultType != null) {
			// super.setResultMsg(resultType.msg());
			super.setResultCode(resultType.code());
		}
	}
}
