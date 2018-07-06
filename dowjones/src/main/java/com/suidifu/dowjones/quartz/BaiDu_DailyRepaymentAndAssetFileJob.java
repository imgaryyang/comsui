package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.service.BaiDu_RepaymentAndAssetFileService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成 还款文件 和 资产文件
 */
@Component
@Slf4j
public class BaiDu_DailyRepaymentAndAssetFileJob extends QuartzJobBean {

    @Autowired
    private BaiDu_RepaymentAndAssetFileService fileService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start execute baidu daily repayment and asset file job....\n");
        Long startTime = System.currentTimeMillis();

        Date yesterday = DateUtils.yesterday();

        List<String> financialContractUuids = new ArrayList<>();

        financialContractUuids.add("a8c21304-b6f7-4895-b2f2-fd960b619406");

        for (String uuid : financialContractUuids) {
            fileService.doFileData(uuid, yesterday);
        }

        Long endTime = System.currentTimeMillis();
        log.info("\n\nduration is :{}ms\n\n", endTime - startTime);
        log.info("end execute baidu daily repayment and asset file job....");
    }
}
