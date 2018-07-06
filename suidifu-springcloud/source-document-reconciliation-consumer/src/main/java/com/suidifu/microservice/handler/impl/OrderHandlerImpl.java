package com.suidifu.microservice.handler.impl;


import com.suidifu.microservice.handler.OrderHandler;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.GuaranteeStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author zjm
 */
@Component("orderHandler")
public class OrderHandlerImpl implements OrderHandler {
    @Resource
    private OrderService orderService;
    @Resource
    private RepaymentPlanService repaymentPlanService;

    @Override
    public void updateOrderStatusAsset(Order order, Date tradeTime) {
        order.updateClearStatus(tradeTime, true);
        orderService.save(order);
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
        if (order.isGuaranteeOrder()) {
            assetSet.setGuaranteeStatus(GuaranteeStatus.HAS_GUARANTEE);
            repaymentPlanService.save(assetSet);
        }
    }
}