package com.suidifu.microservice.model;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.zufangbao.sun.utils.SpringContextUtil;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import java.util.HashMap;
import java.util.Map;

/**
 * @link com.zufangbao.wellsfargo.silverpool.cashauditing.repayment.handler.ReconciliationRepayment
 */
public interface ReconciliationRepayment {

    void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

    void refreshVirtualAccount(ReconciliationRepaymentContext context);

    void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

    void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException;

    void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException;

    ReconciliationRepaymentContext preAccountReconciliation(Map<String, Object> inputParams)
        throws AlreadyProcessedException;

    boolean accountReconciliation(Map<String, Object> params);


    HashMap<String, String> reconciliationVoucherTypeTable =
        new HashMap<String, String>() {{
            put(RepaymentWay.MERCHANT_PAY.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
            put(RepaymentWay.MERCHANT_TRANSFER.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
            put(RepaymentWay.MERCHANT_TRANSFER_BALANCE.getKey(),
                "reconciliationRepaymentOrderForSubrogationDocumentHandler");
            put(RepaymentWay.ACTIVE_PAY.getKey(), "reconciliationRepaymentOrderForInitiativePaymentDocumentHandler");
            put(RepaymentWay.OTHER_PAY.getKey(), "reconciliationRepaymentOrderForInitiativePaymentDocumentHandler");

            put(RepaymentWay.REPURCHASE.getKey(), "reconciliationRepaymentOrderForRepurchaseDocumentHandler");
            put(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getKey(),
                "reconciliationRepaymentOrderForDeductApiDocumentHandler");
            put(RepaymentWay.ON_LINE_DEDUCT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
            put(RepaymentWay.MERCHANT_DEDUCT_ONLINE_PAYMENT.getKey(),
                "reconciliationRepaymentOrderForDeductApiDocumentHandler");
            put(RepaymentWay.MERCHANT_DEDUCT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");

        }};

    static ReconciliationRepayment receivableInAdvanceBean() {
        return SpringContextUtil.getBean("reconciliationRepaymentOrderForReceivableInAdvanceHandler");
    }

    static ReconciliationRepayment reconciliationFactory(String voucherType) {

        String BeanName = reconciliationVoucherTypeTable.get(voucherType);
        ReconciliationRepayment accountReconciliation = SpringContextUtil.getBean(BeanName);
        return accountReconciliation;
    }

    static ReconciliationRepayment recover_reconciliation() {
        String BeanName = "reconciliationRepaymentOrderForRecoverHandler";
        ReconciliationRepayment accountReconciliation = SpringContextUtil.getBean(BeanName);
        return accountReconciliation;
    }
}
