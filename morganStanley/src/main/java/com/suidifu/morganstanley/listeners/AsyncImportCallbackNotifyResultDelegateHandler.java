package com.suidifu.morganstanley.listeners;

import com.suidifu.morganstanley.utils.DateUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageJob;
import com.zufangbao.sun.service.ImportAssetPackageJobService;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageJobHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Log4j2
@Component("asyncImportCallbackNotifyResultDelegateHandler")
public class AsyncImportCallbackNotifyResultDelegateHandler implements NotifyResultDelegateHandler {
    @Resource
    private ImportAssetPackageJobService importAssetPackageJobService;
    @Resource
    private ImportAssetPackageJobHandler importAssetPackageJobHandler;

    @Override
    public void onResult(NotifyJob result) {
        String requestNo = result.getBusinessId();
        String now = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        log.debug("Request callback completed at [" + now + "] RequestNo[" + requestNo + "]");
        ImportAssetPackageJob importAssetPackageJob = importAssetPackageJobService.getImportAssetPackageJobByRequestNo(requestNo, null);
        if (null != importAssetPackageJob) {
            importAssetPackageJobHandler.removeJobRecord(importAssetPackageJob);
            log.debug("Request callback have removed job RequestNo=[" + requestNo + "]");
        } else {
            log.debug("Request callback job had been removed RequestNo=[" + requestNo + "]");
        }
    }
}

