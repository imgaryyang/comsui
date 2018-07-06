package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.account.VirtualAccount;

public interface VirtualAccountHandler {
    VirtualAccount refreshVirtualAccountBalance(String ledgerBookNo, String customerUuid, String financialContractUuid, String oldVirtualAccountVersion);
}