package com.suidifu.microservice.handler.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;

public interface SpecialAccountHandler {

	public void update_asset_refund_financial_contract_account(String financialContractUuid,Map<String, BigDecimal> account_amount_map);

}
