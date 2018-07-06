package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import java.math.BigDecimal;

public interface LedgerBookVirtualAccountHandler {
    BigDecimal getBalanceOfCustomer(String ledgerBookNo, String customerUuid);

    void deposit_independent_account_assets(LedgerBook book, DepositeAccountInfo bankAccount, DepositeAccountInfo customerAccount, AssetCategory assetCategory, BigDecimal amount, String jvUuid, String bvUuid, String sdUuid);
}