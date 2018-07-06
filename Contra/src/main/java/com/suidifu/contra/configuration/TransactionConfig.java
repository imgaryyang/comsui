package com.suidifu.contra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.impl.DelayProcessingTaskCacheHandlerImpl;
import com.suidifu.matryoshka.delayPosition.handler.impl.DelayTaskConfigCacheHandlerImpl;
import com.zufangbao.sun.ledgerbookv2.service.impl.CacheableInsideAccountServiceImpl;
import com.zufangbao.wellsfargo.yunxin.handler.DataStatisticsCacheHandler;
import com.zufangbao.wellsfargo.yunxin.handler.impl.DataStatisticsCacheHandlerImpl;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TransactionConfig {

	@Primary
	@Bean(name="DelayProcessingTaskCacheHandler")
	public DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler(){
		return new DelayProcessingTaskCacheHandlerImpl();
	}

	@Primary
	@Bean(name="DelayTaskConfigCacheHandler")
	public DelayTaskConfigCacheHandler delayTaskConfigCacheHandler(){
		return new DelayTaskConfigCacheHandlerImpl();
	}
	
	@Primary
	@Bean(name="DataStatisticsCacheHandler")
	public DataStatisticsCacheHandler dataStatisticsCacheHandler(){
		return new DataStatisticsCacheHandlerImpl();
	}
	@Bean(name="ledgerCacheHandler")
	public com.zufangbao.sun.ledgerbookv2.handler.LedgerCacheHandler LedgerCacheHandler(){
		return new com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerCacheHandlerImpl();
	}
	@Bean(name="ledgerTemplateParser")
	public com.zufangbao.sun.ledgerbookv2.handler.impl.LedgerTemplateParser LedgerTemplateParser(){
		
		return new com.zufangbao.wellsfargo.silverpool.ledgerbookv2.template.CachecableLedgerTemplateParser();
	}
	@Bean(name="insideAccountService")
	public com.zufangbao.sun.ledgerbookv2.service.InsideAccountService InsideAccountService(){
		
		return new CacheableInsideAccountServiceImpl();
	}
	
	
	
}
