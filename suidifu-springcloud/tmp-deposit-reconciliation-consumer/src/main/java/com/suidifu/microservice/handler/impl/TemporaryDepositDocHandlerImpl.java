package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.TemporaryDepositDocHandler;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("temporaryDepositDocHandler")
public class TemporaryDepositDocHandlerImpl implements TemporaryDepositDocHandler {
  @Autowired
  private TemporaryDepositDocService temporaryDepositDocService;
  @Autowired
  private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;

  @Override
  public void refreshBookingAmountAndStatus(String docUuid, String ledgerBookNo) {
    if (StringUtils.isEmpty(docUuid)) {
      return;
    }
    TemporaryDepositDoc doc = temporaryDepositDocService.getTemporaryDepositDocBy(docUuid);
    if (doc == null) {
      return;
    }
    BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfTmpDepositDoc(ledgerBookNo, doc.getUuid());
    doc.update_balance_and_status(balance);
    temporaryDepositDocService.update(doc);
  }

}
