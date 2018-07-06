package com.suidifu.owlman.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.owlman.microservice.model.RepaymentOrderParameters;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderCheckException;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import java.util.List;
import java.util.Map;

public interface RepaymentOrderPlacingHandler {
    Map<String, String> criticalMarker(List<RepaymentOrderParameters> repaymentOrderParametersList) throws GiottoException;

    boolean checkAndSave(List<RepaymentOrderParameters> repaymentOrderParametersList);

    boolean rollBack(List<RepaymentOrderParameters> repaymentOrderParametersList);

    void check_and_save_for_single_contract_repayment_order(RepaymentOrder repaymentOrder, List<RepaymentOrderDetail> repaymentOrderDetailList) throws RepaymentOrderCheckException;

    boolean checkAndSaveResult(RepaymentOrderParameters repaymentOrderParameters, boolean isSaveItemCheckFail) throws GiottoException;
}