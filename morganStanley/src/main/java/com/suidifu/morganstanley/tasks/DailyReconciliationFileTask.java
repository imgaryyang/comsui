package com.suidifu.morganstanley.tasks;

import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.utils.DateUtils;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DailyReconciliationHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Log4j2
@Component("dailyReconciliationFileTask")
public class DailyReconciliationFileTask  {

    @Resource
    private DailyReconciliationHandler dailyReconciliationHandler;

    @Resource
    private YntrustFileTask yntrustFileTask;

//    @Override
    public void createDailyAssetChangeRecord() {
        String path = yntrustFileTask.getRebuildPath();
        Date date = DateUtils.getYesterday(new Date());
        log.debug("DailyReconciliationFileTask create dailyAssetChangeRecord path [" + path + "] date[" + DateUtils.format(date) + "]");
        dailyReconciliationHandler.dailyAssetChangeRecord(date, path, true, true);
    }
}
