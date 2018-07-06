package com.suidifu.morganstanley.handler;


import com.zufangbao.sun.api.model.repayment.OrderRequestModel;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;

/**
 * 还款订单请求实现类
 *
 */
public interface RepaymentOrderApplicationHandler {

    /**
     * 撤销还款订单校验
     * @param orderRequestModel
     * @return
     */
    void cancelRepaymentOrderInfoCheck(OrderRequestModel orderRequestModel, String merId);
    /**
     * 撤销还款订单
     * @param orderRequestModel
     * @param ip
     * @return
     */
    RepaymentOrder cancelRepaymentOrder(OrderRequestModel orderRequestModel, String ip, String merId);


    /**
     * 校验订单支付消息
     * @param paymentOrderRequestModel
     * @return
     */
    RepaymentOrder paymentOrderInfoCheck(PaymentOrderRequestModel paymentOrderRequestModel, String merchantId);

}
