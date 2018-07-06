package com.suidifu.microservice.handler.impl;

import java.math.BigDecimal;

import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;

public interface LedgerBookVirtualAccountHandler {

	public BigDecimal get_balance_of_customer(String ledgerBookNo, String customerUuid);

}
