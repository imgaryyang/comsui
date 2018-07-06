package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.order.Order;
import java.util.Date;

/**
 * @author zjm
 */
public interface OrderHandler {
    void updateOrderStatusAsset(Order order, Date tradeTime);
}