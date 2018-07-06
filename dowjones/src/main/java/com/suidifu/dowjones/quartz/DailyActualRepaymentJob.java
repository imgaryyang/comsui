package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.model.Const;
import com.suidifu.dowjones.service.DailyActualRepaymentService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
@Slf4j
public class DailyActualRepaymentJob extends QuartzJobBean  {
	@Resource
	private FinancialContractDAO financialContractDAO;
    @Resource
    private DailyActualRepaymentService dailyActualRepaymentService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.info("start execute daily actual repayment job....\n");
        Long startTime = System.currentTimeMillis();

        Date yesterday = DateUtils.yesterday();

        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();
//        financialContractUuids = Arrays
//            .asList("3b12ac75-4c58-4375-a733-78c7810efebb", "429d5892-51dd-42fc-aa7e-e333778fd32f","e0970224-5e9b-477a-82f3-33ab252545b8");

        financialContractUuids = Const.CLIENT_PRODUCT;

        for (String uuid : financialContractUuids){
            log.info("Start actual repayment job, parameter[{},{}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));
            long start = System.currentTimeMillis();

            dailyActualRepaymentService.executeDailyActualRepaymentJob(uuid, yesterday);

            long end = System.currentTimeMillis();

            log.info("End actual repayment job, parameter[{},{}], use time {}", uuid, DateUtils.getDateFormatYYMMDD(yesterday), end - start);
        }

        Long endTime = System.currentTimeMillis();
        log.info("\n\nduration is :{}ms\n\n", endTime - startTime);
        log.info("end execute daily actual repayment job....");

    }
}
