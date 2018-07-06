package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.model.Const;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.service.DailyPlanRepaymentService;
import com.suidifu.dowjones.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class DailyPlanRepaymentJob extends QuartzJobBean {
    @Autowired
    private FinancialContractDAO financialContractDAO;

    @Resource
    private DailyPlanRepaymentService dailyPlanRepaymentService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("start execute daily plan repayment job....\n");
        Long startTime = System.currentTimeMillis();

        Date yesterday = DateUtils.yesterday();

        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();
//        financialContractUuids = Arrays
//            .asList("3b12ac75-4c58-4375-a733-78c7810efebb", "429d5892-51dd-42fc-aa7e-e333778fd32f","e0970224-5e9b-477a-82f3-33ab252545b8");

        financialContractUuids = Const.CLIENT_PRODUCT;

        for (String uuid : financialContractUuids){
            log.info("Start plan repayment job, parameter[{},{}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));
            long start = System.currentTimeMillis();
            dailyPlanRepaymentService.executeDailyPlanRepaymentJob(uuid, yesterday);
            long end = System.currentTimeMillis();
            log.info("End plan repayment job, parameter[{},{}], use time {}", uuid,
                DateUtils.getDateFormatYYMMDD(yesterday), end - start);
        }

        Long endTime = System.currentTimeMillis();
        log.info("daily plan repayment job use time {}ms", endTime - startTime);
        log.info("end execute daily plan repayment job....");
        
    }
}
