package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.utils.AsyncFtp;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/20 <br>
 * @time: 下午4:05 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Component
@Slf4j
public class ScheduledJob extends QuartzJobBean {
    @Resource
    private CashFingerPrinterService cashFingerPrinterService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("start execute schedule job....\n");
        Long startTime = new Date().getTime();

        FingerPrinterParameter[] fingerPrinterParameters = cashFingerPrinterService.loadScheduleJob();
        for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
            new Thread(new AsyncFtp(cashFingerPrinterService, fingerPrinterParameter)).start();
        }

        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n",
                endTime - startTime);
        log.info("end execute schedule job....\n");
    }
}