package com.suidifu.datasync.canal.rowprocesser.redis.domain;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;

/**
 * 通道流水Result
 * 
 * @author lisf
 *
 */
public class CashFlowResult extends Result {

	public CashFlowResult() {
		this.setSystemBillType(RemittanceType.未知.type());
		this.setCashFlowAccountSide(RemittanceType.未知.type());
	}

	public static void _clone(Result _new, Result _old) {
		_old.setTradeId(_new.getTradeId());
		_old.setCashFlowIdentity(_new.getCashFlowIdentity());
		_old.setCashFlowAccountSide(_new.getCashFlowAccountSide());
		_old.setHostAccountNo(_new.getHostAccountNo());
		_old.setCashFlowTransactionTime(_new.getCashFlowTransactionTime());
		_old.setCashFlowTransactionAmount(_new.getCashFlowTransactionAmount());
		_old.setResultCode(_new.getResultCode());
	}
}
