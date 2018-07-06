package com.suidifu.morganstanley.listeners;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.morganstanley.utils.DateUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBill;
import com.zufangbao.sun.yunxin.service.TransferBillService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("asyncTransferBillNotifyResultDelegateHandler")
public class AsyncTransferBillNotifyResultDelegateHandler implements NotifyResultDelegateHandler {
	
	@Autowired
	private TransferBillService transferBillService;
  

    @Override
    public void onResult(NotifyJob result) {
        String requestNo = result.getBusinessId();
        String now = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        log.debug("AsyncTransferBillNotifyResult#Request callback completed at [" + now + "] RequestNo[" + requestNo + "]");
        TransferBill transferBill = transferBillService.queryTransferBillByUuid(requestNo);
        transferBill.setActualNotifyNumber(transferBill.getActualNotifyNumber()+1);
        transferBill.setLastModifyTime(new Date());
        transferBill.setNotifyTime(new Date());
        transferBillService.update(transferBill);
       
    }
}

