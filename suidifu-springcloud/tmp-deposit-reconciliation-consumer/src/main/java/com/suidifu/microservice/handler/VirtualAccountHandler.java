package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.account.VirtualAccount;

/**
 * @author louguanyang
 */
public interface VirtualAccountHandler {

  VirtualAccount refreshVirtualAccountBalance(String ledgerBookNo, String customerUuid, String financialContractUuid,
      String oldVersion);

}
