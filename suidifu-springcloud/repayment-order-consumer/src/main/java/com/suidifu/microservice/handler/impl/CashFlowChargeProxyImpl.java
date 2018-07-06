package com.suidifu.microservice.handler.impl;

import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.microservice.handler.CashFlowAutoIssueHandler;
import com.suidifu.microservice.handler.CashFlowChargeProxy;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("cashFlowChargeProxy")
public class CashFlowChargeProxyImpl implements CashFlowChargeProxy {

    private static Log logger = LogFactory.getLog(CashFlowChargeProxyImpl.class);

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private CashFlowAutoIssueHandler cashFlowAutoIssueHandler;

    // swissre通过rpc调用此方法
    @Override
    public void charge_cash_into_virtual_account_and_recover(String cashFlowUuid, String fstSndContractUuid) {
        logger.info("charge_cash start:cashFlowUuid," + cashFlowUuid + ",fst_snd_contractUuid[" + fstSndContractUuid + "].");

        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
        cashFlowAutoIssueHandler.charge_cash_into_virtual_account(cashFlow, fstSndContractUuid);
        logger.info("charge_cash end:cashFlowUuid," + cashFlowUuid + ",fst_snd_contractUuid[" + fstSndContractUuid + "].");
    }

    @Override
    @MqAsyncRpcMethod
    public void charge_cash_into_virtual_account_for_rpc(String cashFlowUuid,
                                                         @MqRpcBusinessUuid String fstSndContractUuid,
                                                         @MqRpcPriority int priority) {
        charge_cash_into_virtual_account_and_recover(cashFlowUuid, fstSndContractUuid);
    }
}