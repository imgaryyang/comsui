package com.suidifu.microservice.handler;

public interface TemporaryDepositDocHandler {

  public void refreshBookingAmountAndStatus(String docUuid, String ledgerBookNo);

}
