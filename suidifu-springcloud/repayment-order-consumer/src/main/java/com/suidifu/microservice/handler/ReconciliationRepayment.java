package com.suidifu.microservice.handler;

import static com.suidifu.owlman.microservice.spec.ThirdPartVoucherSourceMapSpec.reconciliationRepaymentOrderTable;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.zufangbao.sun.utils.SpringContextUtil;
import java.util.Map;

public interface ReconciliationRepayment {
    static ReconciliationRepayment receivableInAdvanceBean() {
        return SpringContextUtil.getBean("reconciliationRepaymentOrderForReceivableInAdvanceHandler");
    }

    static ReconciliationRepayment reconciliationFactory(String voucherType) {
        return SpringContextUtil.getBean(reconciliationRepaymentOrderTable.get(voucherType));
    }

    void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

    void refreshVirtualAccount(ReconciliationRepaymentContext context);

    void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

    void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException;

    void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException;

    ReconciliationRepaymentContext preAccountReconciliation(Map<String, Object> inputParams) throws AlreadyProcessedException;

    boolean accountReconciliation(Map<String, Object> params);
}
