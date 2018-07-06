package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.model.Const;
import com.suidifu.dowjones.service.DailyRepurchaseService;
import com.suidifu.dowjones.utils.DateUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DailyRepurchaseJob extends QuartzJobBean {

    @Autowired
    private FinancialContractDAO financialContractDAO;
    @Autowired
    private DailyRepurchaseService dailyRepurchaseService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("start execute daily repurchase job....\n");
        Long startTime = System.currentTimeMillis();

        Date yesterday = DateUtils.yesterday();

        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();
//        financialContractUuids = Arrays
//            .asList("3b12ac75-4c58-4375-a733-78c7810efebb", "429d5892-51dd-42fc-aa7e-e333778fd32f","e0970224-5e9b-477a-82f3-33ab252545b8");
        financialContractUuids = Const.CLIENT_PRODUCT;

        for (String uuid : financialContractUuids) {
            log.info("Start repurchase job, parameter[{},{}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));
            long start = System.currentTimeMillis();
            dailyRepurchaseService.executeDailyRepurchaseJob(uuid, yesterday);
            long end = System.currentTimeMillis();
            log.info("End repurchase job, parameter[{},{}], use time:{}ms", uuid,
                DateUtils.getDateFormatYYMMDD(yesterday), end - start);
        }
        Long endTime = System.currentTimeMillis();
        log.info("daily repurchase job use time :{}ms\n\n", endTime - startTime);
        log.info("end execute daily repurchase job....");
    }
}
