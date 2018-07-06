package com.suidifu.datasync.canal.rowprocesser.redis.domain;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;

/**
 * 代付单Result
 * 
 * @author lisf
 *
 */
public class ExecLogResult extends Result {

	public ExecLogResult() {
		this.setSystemBillType(RemittanceType.贷.type());
	}

	public static void _clone(ExecLogResult _new, Result _old) {
		_old.setSystemBillIdentity(_new.getSystemBillIdentity());
		_old.setTradeId(_new.getTradeId());
		_old.setFinancialContractUuid(_new.getFinancialContractUuid());
		_old.setPaymentChannelUuid(_new.getPaymentChannelUuid());
		_old.setSystemBillOccurDate(_new.getSystemBillOccurDate());
		_old.setSystemBillPlanAmount(_new.getSystemBillPlanAmount());
		_old.setResultCode(_new.getResultCode());
		
		_old.setSystemBillType(_new.getSystemBillType());
	}
}
