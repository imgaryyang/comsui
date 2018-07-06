package com.suidifu.berkshire;

import com.suidifu.berkshire.configuration.TransactionConfigTest;
import com.suidifu.berkshire.log.test.LogTest;
import com.suidifu.berkshire.mq.receiver.adapter.MqMessageReceiverTest;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobSourceDocumentReconciliationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TransactionConfigTest.class,
	LogTest.class,
	MqMessageReceiverTest.class,
	DstJobSourceDocumentReconciliationTest.class,
	
})
public class AllTestOfWukai {

}
