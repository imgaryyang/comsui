package com.suidifu.morganstanley.listeners;

import com.suidifu.morganstanley.utils.DateUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Component("asyncImportAssetPackageNotifyResultDelegateHandler")
public class AsyncImportAssetPackageNotifyResultDelegateHandler implements NotifyResultDelegateHandler {
    @Override
    public void onResult(NotifyJob result) {
        String now = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        log.info("Sub request is process completed at [" + now + "] subRequestNo[" + result.getBusinessId() + "]");
    }
}

