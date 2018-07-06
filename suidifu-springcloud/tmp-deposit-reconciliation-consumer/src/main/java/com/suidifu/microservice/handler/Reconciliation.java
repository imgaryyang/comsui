package com.suidifu.microservice.handler;

import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.zufangbao.sun.utils.SpringContextUtil;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import java.util.HashMap;
import java.util.Map;

public interface Reconciliation {

  /**
   * @param context 上下文
   */
  void refreshVirtualAccount(ReconciliationContext context);


  /**
   * @param context 上下文
   * @throws AlreadyProcessedException 已处理
   */
  void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException;

  /**
   * @param context 上下文
   */
  void postAccountReconciliation(ReconciliationContext context);

  /**
   * @param context 上下文
   */
  void refreshAsset(ReconciliationContext context);

  /**
   * @param inputParams 入参
   * @return 上下文
   * @throws AlreadyProcessedException 已处理
   */
  ReconciliationContext preAccountReconciliation(Map<String, Object> inputParams) throws AlreadyProcessedException;

  /**
   * @param params 入参
   * @return true or false
   */
  boolean accountReconciliation(Map<String, Object> params);

  HashMap<String, String> RECONCILIATION_VOUCHER_TYPE_TABLE = new HashMap<String, String>() {
    private static final long serialVersionUID = -514218301125839764L;

    {
      put(VoucherType.REPURCHASE.getKey(), "reconciliationForRepurchaseDocumentHandler");
      put(VoucherType.PAY.getKey(), "reconciliationForSubrogationDocumentHandler");
      put(VoucherType.MERCHANT_REFUND.getKey(), "reconciliationForSubrogationDocumentHandler");
      put(VoucherType.ADVANCE.getKey(), "reconciliationForSubrogationDocumentHandler");
      put(VoucherType.GUARANTEE.getKey(), "reconciliationForGuaranteeVoucherHandler");

      put(VoucherType.ACTIVE_PAY.getKey(), "reconciliationForInitiativePaymentDocumentHandler");
      put(VoucherType.OTHER_PAY.getKey(), "reconciliationForInitiativePaymentDocumentHandler");
      put(ReconciliationType.RECONCILIATION_AUTO_RECOVER_SETTLEMENT_SHEET.getKey(),
          "reconciliationForReconciliationSettlementSheetDocumentHandler");
      put(VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), "reconciliationForDeductApiDocumentHandler");
      put(ReconciliationType.RECONCILIATION_DEDUCT_REFUND.getKey(), "reconciliationFoDeductApiRefundHandler");
      put(ReconciliationType.RECONCILIATION_Clearing_Voucher.getKey(), "reconciliationForClearingVoucherHandler");
    }
  };

  static Reconciliation reconciliationFactory(String voucherType) {
    String beanName = RECONCILIATION_VOUCHER_TYPE_TABLE.get(voucherType);
    return SpringContextUtil.getBean(beanName);
  }

}
