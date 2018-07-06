package com.suidifu.barclays.jobs;

import com.suidifu.barclays.factory.CashflowHandlerFactory;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.handler.CashFlowHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dafuchen
 *         2017/12/25
 */
@Component
public class CleanCashFlowRedisJob {

    @Autowired
    private ZSetOperations<String, CashFlow> cashFlowZSetOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CashFlowHandler cashFlowHandler;
    @Value("#{config['storeCashFlowCacheDays']}")
    private Integer day;

    /**
     * 每天在三点钟的时候把缓存中前{}天的信息给删除掉
     */
    @Scheduled(cron="0 0 3 * * ?")
    public void deleteOutDateData() {
        cashFlowHandler.cleanUpOutDateData(cashFlowZSetOperations,
                stringRedisTemplate, day, CashflowHandlerFactory.channelIdentityCashflowHandlerBeanNameMapper);
    }
}
