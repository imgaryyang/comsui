package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.account.VirtualAccount;

/**
 * @author louguanyang at 2018/3/14 12:49
 * @mail louguanyang@hzsuidifu.com
 */
public interface VirtualAccountHandler {

    VirtualAccount refreshVirtualAccountBalance(String ledgerBookNo, String customerUuid, String financialContractUuid,
        String oldVirtualAccountVersion);
}
