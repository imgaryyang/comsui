package com.suidifu.microservice.handler.impl;

import static com.zufangbao.sun.ledgerbook.ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("ledgerBookVirtualAccountHandler")
public class LedgerBookVirtualAccountHandlerImpl implements LedgerBookVirtualAccountHandler {
    @Resource
    private LedgerItemService ledgerItemService;

    @Override
    public BigDecimal getBalanceOfCustomer(String ledgerBookNo, String customerUuid) {
        BigDecimal amount = ledgerItemService.getBalancedAmount(ledgerBookNo,
                FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS,
                customerUuid, "", null, null, "", "", "");
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.negate();
    }

    @Override
    public BigDecimal getBalanceOfFrozenCapital(String ledgerBookNo,
                                                String customerUuid,
                                                String sourceDocumentUuid,
                                                String tmpDepositDocUuid) {
        BigDecimal amount = ledgerItemService.getFrozenCapitalBalancedAmount(ledgerBookNo,
                customerUuid, sourceDocumentUuid, tmpDepositDocUuid);
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.negate();
    }
}