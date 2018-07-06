package com.suidifu.dowjones.quartz;


import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.model.Const;
import com.suidifu.dowjones.service.DailyRemittanceService;
import com.suidifu.dowjones.utils.DateUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author veda
 */
@Slf4j
@Component
public class DailyRemittanceJob extends QuartzJobBean {

    @Autowired
    private DailyRemittanceService dailyRemittanceService;

    @Autowired
    private FinancialContractDAO financialContractDAO;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.info("start execute daily remittance job....");
        Long startTime = System.currentTimeMillis();

        Date yesterday = DateUtils.yesterday();

        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();

//        financialContractUuids = Arrays
//            .asList("3b12ac75-4c58-4375-a733-78c7810efebb", "429d5892-51dd-42fc-aa7e-e333778fd32f","e0970224-5e9b-477a-82f3-33ab252545b8");

        financialContractUuids = Const.CLIENT_PRODUCT;

        for (String uuid : financialContractUuids) {
            log.info("Start remittance job, parameter[{},{}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));

            dailyRemittanceService.doRemittanceData(uuid, yesterday);

            log.info("Start remittance job, parameter[{},{}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));

        }
        Long endTime = System.currentTimeMillis();
        log.info("remittance job use time :{}ms", endTime - startTime);
        log.info("end execute daily remittance job....");


    }
}
