package com.suidifu.barclays.handler;


import java.util.Map;

import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;

public interface PaymentAsyncHandler extends NotifyResultListener {
	QueryCreditResult queryDebit(QueryCreditModel queryCreditModel, String financialContractUuid);
}
