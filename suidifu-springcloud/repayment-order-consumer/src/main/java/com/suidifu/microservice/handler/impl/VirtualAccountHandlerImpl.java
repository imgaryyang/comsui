package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.exception.VirtualAccountNotExsitException;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("virtualAccountHandler")
public class VirtualAccountHandlerImpl implements VirtualAccountHandler {
    @Resource
    private VirtualAccountService virtualAccountService;

    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;

    @Override
    public VirtualAccount refreshVirtualAccountBalance(String ledgerBookNo, String customerUuid, String financialContractUuid, String oldVirtualAccountVersion) {
        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
        if (virtualAccount == null) {
            throw new VirtualAccountNotExsitException();
        }

        BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBookNo, customerUuid);
        if (balance.compareTo(virtualAccount.getTotalBalance()) == 0) {
            return virtualAccount;
        }
        virtualAccountService.updateBalance(virtualAccount.getVirtualAccountUuid(), balance, oldVirtualAccountVersion, new Date());
        return virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccount.getVirtualAccountUuid());
    }

}