package com.suidifu.microservice.handler;

/**
 * Created by zhusy on 2018/2/27.
 */
public interface CashFlowAutoIssueV2_0Handler {

  public void recover_assets_repayment_order_deduct(String sourceDocumentUuid);

  void recover_receivable_in_advance(String orderUuid, String paymentOrderUuid);
}
