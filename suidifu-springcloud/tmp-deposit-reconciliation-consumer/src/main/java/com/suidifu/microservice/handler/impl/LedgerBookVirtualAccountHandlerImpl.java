package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ledgerBookVirtualAccountHandler")
public class LedgerBookVirtualAccountHandlerImpl implements LedgerBookVirtualAccountHandler {

  @Autowired
  private LedgerItemService ledgerItemService;

  private static Log logger = LogFactory.getLog(LedgerBookVirtualAccountHandlerImpl.class);

  @Override
  public BigDecimal getBalanceOfCustomer(String ledgerBookNo, String customerUuid) {
    BigDecimal amount = ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS,
            customerUuid, "", null, null, "", "", "");
    if (amount == null) {
      return BigDecimal.ZERO;
    }
    return amount.negate();
  }


  @Override
  public BigDecimal getBalanceOfFrozenCapital(String ledgerBookNo, String customerUuid,
      String sourceDocumentUuid, String tmpDepositDocUuid) {
    BigDecimal amount = ledgerItemService
        .getFrozenCapitalBalancedAmount(ledgerBookNo, customerUuid, sourceDocumentUuid,
            tmpDepositDocUuid);
    if (amount == null) {
      return BigDecimal.ZERO;
    }
    return amount.negate();
  }

  @Override
  public BigDecimal getBalanceOfTmpDepositDoc(String ledgerBookNo, String tmpDepositDocUuid) {
    if (StringUtils.isEmpty(tmpDepositDocUuid)) {
      return BigDecimal.ZERO;
    }
    BigDecimal amount = ledgerItemService.getBalancedAmount(ledgerBookNo,
        ChartOfAccount.SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING, "", "", null, null, "", "",
        tmpDepositDocUuid);
    if (amount == null) {
      return BigDecimal.ZERO;
    }
    return amount.negate();
  }

}
