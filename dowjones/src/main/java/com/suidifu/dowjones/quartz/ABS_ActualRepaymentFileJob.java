package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.service.ABS_FileService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 实际还款文件
 */
@Component
@Slf4j
public class ABS_ActualRepaymentFileJob extends QuartzJobBean {
    @Autowired
    private ABS_FileService fileService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start execute abs actual repayment file job....\n");
        Long startTime = System.currentTimeMillis();

        Date today = new Date();

        fileService.doActualRepaymentFileData(today);

        Long endTime = System.currentTimeMillis();
        log.info("\n\nduration is :{}ms\n\n", endTime - startTime);
        log.info("end execute abs actual repayment file job....");
    }
}
