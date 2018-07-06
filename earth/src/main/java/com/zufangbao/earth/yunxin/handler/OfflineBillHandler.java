package com.zufangbao.earth.yunxin.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.exception.OfflineBillAutidtException;
import com.zufangbao.earth.yunxin.exception.SourceDocumentNotExistException;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.entity.IllegalInputAmountException;
import com.zufangbao.sun.yunxin.entity.OfflineBill;
import com.zufangbao.sun.yunxin.entity.model.OfflineBillCreateModel;
import com.zufangbao.sun.yunxin.entity.model.OrderMatchModel;
import com.zufangbao.sun.yunxin.entity.model.OrderMatchQueryModel;
import com.zufangbao.sun.yunxin.entity.model.OrderMatchShowModel;
import com.zufangbao.wellsfargo.exception.OfflineBillCreateException;
import com.zufangbao.wellsfargo.exception.VoucherException;

public interface OfflineBillHandler {

    void createOfflineBillForGuaranteOrderMatch(OfflineBillCreateModel offlineBillCreateModel) throws OfflineBillCreateException, VoucherException;

    void updateOrderAndAsset(List<Order> orderList);

    String extractOrderNoSet(List<Order> guaranteeOrders);

    BigDecimal getTotalAmount(List<Order> guaranteeOrders);

    void validOrderStatus(List<Order> guaranteeOrders) throws OfflineBillCreateException;

    OfflineBill create_offline_bill_and_create_source_document(OfflineBillCreateModel offlineBillCreateModel);

    OfflineBill create_offline_bill(OfflineBillCreateModel offlineBillCreateModel);

    List<Order> smartMatchOrderListBy(String offlineBillUuid);

    void buildAssociationBetweenOrderAndOfflineBill(String offlineBillUuid, Map<String, Object> map, BigDecimal connectionAmount, long userId, String ip) throws SourceDocumentNotExistException, IllegalInputAmountException, OfflineBillAutidtException;

    List<OrderMatchModel> searchMatchModelOrderList(OrderMatchQueryModel orderMatchQueryModel, Page page);
	
    List<OrderMatchShowModel> getOrderMatchShowModelBy(String offlineBillUuid);

}
