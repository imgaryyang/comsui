package com.suidifu.datasync.canal.rowprocesser.redis.domain;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;

/**
 * 撤销单Result
 * 
 * @author lisf
 *
 */
public class RefundResult extends Result {

	public RefundResult() {
		this.setSystemBillType(RemittanceType.借.type());
	}

	public static void _clone(RefundResult _new, Result _old) {
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
