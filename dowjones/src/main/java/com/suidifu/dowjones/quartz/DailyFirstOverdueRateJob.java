package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.model.Const;
import com.suidifu.dowjones.service.DailyFirstOverdueRateService;
import com.suidifu.dowjones.utils.DateUtils;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 首逾期率
 */
@Component
@Slf4j
public class DailyFirstOverdueRateJob extends QuartzJobBean {
    @Autowired
    private DailyFirstOverdueRateService firstOverdueRateService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start execute daily first overdue rate job....\n");
        Long startTime = System.currentTimeMillis();

        Date yesterday = DateUtils.yesterday();
        List<String> financialContractUuids = Const.CLIENT_PRODUCT;

        for (String fc : financialContractUuids){
            firstOverdueRateService.doFile(fc, yesterday);
        }

        Long endTime = System.currentTimeMillis();
        log.info("\n\nduration is :{}ms\n\n", endTime - startTime);
        log.info("end execute daily first overdue rate job....");
    }
}
