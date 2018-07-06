package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import java.util.List;

/**
 * @author hjl
 */
public interface OrderItemEditHandler {
    void lapseAndCreateNewItemHandler(String contractUuid, String orderUuid, String itemUuidToBeLapsed,
                                      List<RepaymentOrderItemModel> repaymentOrderItemModels,
                                      int priority) throws GiottoException;
}