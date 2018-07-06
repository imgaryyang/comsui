package com.suidifu.owlman.microservice.handler;

import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import java.util.List;

public interface RepaymentOrderItemHandlerProxy {
    void lapseAndCreateNewItem(String contractUuid, String orderUuid, String item_uuid_to_be_lapsed, List<RepaymentOrderItemModel> repaymentOrderItemModels, int priority, Long principalId, String ip);
}