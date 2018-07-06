package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.order.Order;
import java.util.Date;

/**
 * @author louguanyang
 */
public interface OrderHandler {

  /**
   * 更新结算单状态
   *
   * @param order 结算单
   * @param tradeTime 交易时间
   */
  void updateOrderStatusAsset(Order order, Date tradeTime);
}
