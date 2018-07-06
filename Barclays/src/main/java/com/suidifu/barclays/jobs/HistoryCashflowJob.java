package com.suidifu.barclays.jobs;

import com.suidifu.barclays.factory.CashflowHandlerFactory;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by dafuchen on 2017/9/11.
 */
@Component("historyCashflowJob")
public class HistoryCashflowJob implements JobHandler{
    private Logger logger = LoggerFactory.getLogger(HistoryCashflowJob.class);
    /**是否有下一页**/
    private static final String HAS_NEXT_PAGE = "HAS_NEXT_PAGE";
    /**是否终止查询**/
    private static final String TERMINAL_SIGNAL = "TERMINAL_SIGNAL";


    @Autowired
    ChannelWorkerConfigService channelWorkerConfigService;
    @Autowired
    CashFlowService cashFlowService;
    @Override
    public void run(Map<String, Object> workingParms) {
        logger.info("start cashflowJob...");

        try {
            String workerUuid = workingParms.getOrDefault("workerUuid", StringUtils.EMPTY).toString();

            ChannelWorkerConfig channelWorkerConfig = channelWorkerConfigService.getChannelWorkerConfigBy(workerUuid);

            if(null == channelWorkerConfig) {
                logger.warn("initialization failed, cannot get channelWorkerConfig for " + workerUuid);
                return;
            }

            String channelIdentity = channelWorkerConfig.getChannelIdentity();
            CashflowHandler cashflowHandler = CashflowHandlerFactory.newInstance(channelIdentity);
            if(null == cashflowHandler) {
                logger.info("initialization failed, cannot create cashflowHandler for " + workerUuid);
            }
            Map<String, String> configForHandler = channelWorkerConfig.getLocalWorkingConfig();
            String terminalSignal = configForHandler.getOrDefault(TERMINAL_SIGNAL, "false");
            if (Boolean.parseBoolean(terminalSignal)) {
                return;
            } else {
                List<CashFlow> cashflowList = cashflowHandler.execPullCashflow(channelWorkerConfig);
                if (null == cashflowList || CollectionUtils.isEmpty(cashflowList)) {
                    return;
                }
                cashFlowService.saveCashflows(cashflowList);
                String hasNextPage = configForHandler.getOrDefault(HAS_NEXT_PAGE, "true");
                if (! Boolean.parseBoolean(hasNextPage)) {
                    configForHandler.put(TERMINAL_SIGNAL, "true");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("end cashflowJob...");
    }
}
