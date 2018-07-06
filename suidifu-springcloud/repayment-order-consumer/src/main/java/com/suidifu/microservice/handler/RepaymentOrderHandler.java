package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.owlman.microservice.model.RepaymentOrderCheck;
import com.suidifu.owlman.microservice.model.RepaymentOrderParameters;

public interface RepaymentOrderHandler {
    void saveAfterCheck(RepaymentOrderParameters repaymentOrderParameters,
                        RepaymentOrderCheck checkModel,
                        boolean isSaveItemCheckFail) throws GiottoException;

    RepaymentOrderCheck check(RepaymentOrderParameters repaymentOrderParameters);

    void lapseOrderItemRollBackRepaymentPlan(String repaymentOrderUuid) throws GiottoException;
}