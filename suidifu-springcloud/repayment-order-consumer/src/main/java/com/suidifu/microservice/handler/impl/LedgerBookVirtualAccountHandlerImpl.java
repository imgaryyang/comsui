package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.ledgerbook.AccountDispersor;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
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
                ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS,
                customerUuid, "", null, null, "", "", "");
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.negate();
    }

    @Override
    public void deposit_independent_account_assets(LedgerBook book,
                                                   DepositeAccountInfo bankAccount,
                                                   DepositeAccountInfo customerAccount,
                                                   AssetCategory assetCategory,
                                                   BigDecimal amount, String jvUuid,
                                                   String bvUuid, String sdUuid) {
        AccountDispersor accountDispersor = new AccountDispersor();
        accountDispersor.dispers(bankAccount.getIndependentAccountName(), amount, AccountSide.DEBIT, bankAccount.getDeopsite_account_owner_party(), jvUuid, bvUuid, sdUuid);
        accountDispersor.dispers(customerAccount.getDeposite_account_name(), amount, AccountSide.CREDIT, customerAccount.getDeopsite_account_owner_party(), jvUuid, bvUuid, sdUuid);
        Map<String, LedgerItem> bookedLegerMaps = ledgerItemService.book_M_Debit_M_Credit_ledgers_V2(book,
                assetCategory, accountDispersor);

        ledgerItemService.saveItemListInsql(new ArrayList<>(bookedLegerMaps.values()));

        log.info("使用程序生成脚本数据:[" + bookedLegerMaps.size() + "]条！！！");
    }
}