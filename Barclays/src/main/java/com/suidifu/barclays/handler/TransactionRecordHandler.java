package com.suidifu.barclays.handler;

import java.util.List;
import java.util.Map;

import com.suidifu.barclays.exception.PullTransactionRecordException;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;

public interface TransactionRecordHandler {

	List<ThirdPartyTransactionRecord> execPullThirdPartyTransactionRecord(ChannelWorkerConfig channelWorkerConfig) throws PullTransactionRecordException;
}
