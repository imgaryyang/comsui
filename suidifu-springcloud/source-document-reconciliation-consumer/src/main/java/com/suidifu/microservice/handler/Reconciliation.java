package com.suidifu.microservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.spec.ThirdPartVoucherSourceMapSpec;
import com.zufangbao.sun.utils.SpringContextUtil;
import java.util.Map;

public interface Reconciliation {
    static Reconciliation reconciliationFactory(String voucherType) {
        return SpringContextUtil.getBean(ThirdPartVoucherSourceMapSpec.reconciliationVoucherTypeTable.get(voucherType));
    }

    void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException;

    void refreshVirtualAccount(ReconciliationContext context);

    void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException;

    void postAccountReconciliation(ReconciliationContext context);

    void refreshAsset(ReconciliationContext context);

    ReconciliationContext preAccountReconciliation(Map<String, Object> inputParams) throws AlreadyProcessedException, JsonProcessingException;

    boolean accountReconciliation(Map<String, Object> params) throws JsonProcessingException;
}