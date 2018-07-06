package com.suidifu.microservice.handler.impl;


import com.suidifu.microservice.handler.CashFlowHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.PaymentOrderService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("cashFlowHandler")
public class CashFlowHandlerImpl implements CashFlowHandler {
    @Resource
    private CashFlowService cashFlowService;
    @Resource
    private PaymentOrderService paymentOrderService;

    @Override
    public List<CashFlow> getCashFlowBy(String hostAccountNo,
                                        PaymentOrder paymentOrder,
                                        boolean isNeedMatchName,
                                        boolean isNeedAssignRemark) {
        if (StringUtils.isEmpty(hostAccountNo) || paymentOrder == null
                || StringUtils.isEmpty(paymentOrder.getCounterAccountNo()) || paymentOrder.getAmount() == null) {
            return Collections.emptyList();
        }

        //先匹配 账户和金额
        List<CashFlow> filteredCashFlows = cashFlowService.getFilteredCashFlowsBy(hostAccountNo,
                paymentOrder.getCounterAccountNo(),
                paymentOrder.getAmount(),
                paymentOrder.getOrderUuid());

        if (CollectionUtils.isEmpty(filteredCashFlows)) return Collections.emptyList();

        List<CashFlow> cashFlowList = new ArrayList<>();

        //匹配户名
        for (CashFlow cashFlow : filteredCashFlows) {
            if (StringUtils.equals(cashFlow.getCounterAccountName(), paymentOrder.getCounterAccountName())) {
                cashFlowList.add(cashFlow);
            }
        }

        if (CollectionUtils.isEmpty(cashFlowList) && isNeedMatchName) {
            if (filteredCashFlows.size() == 1 && isNeedAssignRemark) {
                paymentOrder.setRemark("付款户名不一致");
                paymentOrderService.update(paymentOrder);
            }
            return cashFlowList;
        }

        if (CollectionUtils.isEmpty(cashFlowList) && !isNeedMatchName) {
            return filteredCashFlows;
        }

        return cashFlowList;
    }
}