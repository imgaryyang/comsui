package com.suidifu.barclays.handler;

import java.util.List;

import com.suidifu.barclays.exception.PullCashflowException;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;

public interface CashflowHandler {

	List<CashFlow> execPullCashflow(ChannelWorkerConfig channelWorkerConfig) throws PullCashflowException;
}
