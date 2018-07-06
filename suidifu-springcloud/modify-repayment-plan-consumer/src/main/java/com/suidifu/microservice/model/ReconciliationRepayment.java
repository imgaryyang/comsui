package com.suidifu.microservice.model;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.zufangbao.sun.utils.SpringContextUtil;
import java.util.Map;

public interface ReconciliationRepayment {

    public abstract void validateReconciliationContext(ReconciliationRepaymentContext context)
        throws AlreadyProcessedException;

    public abstract void refreshVirtualAccount(ReconciliationRepaymentContext context);

    public abstract void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

    public abstract void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException;

    public abstract void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException;

    public abstract ReconciliationRepaymentContext preAccountReconciliation(Map<String, Object> inputParams)
        throws AlreadyProcessedException;

    /**
     * 对账
     * @param params    参数
     * @return
     */
    public abstract boolean accountReconciliation(Map<String, Object> params);

    /**
     * 核销
     * @return
     */
    public static ReconciliationRepayment recover_reconciliation() {
        String beanName = "reconciliationRepaymentOrderForRecoverHandler";
        return SpringContextUtil.getBean(beanName);
    }
}
