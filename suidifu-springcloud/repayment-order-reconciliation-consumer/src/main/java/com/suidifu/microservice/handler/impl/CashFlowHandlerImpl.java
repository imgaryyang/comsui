package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.CashFlowHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.PaymentOrderService;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("cashFlowHandler")
public class CashFlowHandlerImpl implements CashFlowHandler {
    @Resource
    private CashFlowService cashFlowService;

    @Resource
    private PaymentOrderService paymentOrderService;

    @Override
    public String getUniqueCashIdentity(String orderUuid) {
        String cashFlowUuid = paymentOrderService.get_unique_cash_flow_uuid_offline_pay_way(orderUuid);
        if (StringUtils.isEmpty(cashFlowUuid)) {
            return "";
        }

        CashFlow cashflow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
        if (cashflow == null) {
            return "";
        }
        return cashflow.getCashFlowIdentity();
    }
}