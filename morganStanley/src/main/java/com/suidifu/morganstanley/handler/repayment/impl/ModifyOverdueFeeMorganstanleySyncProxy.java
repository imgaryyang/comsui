package com.suidifu.morganstanley.handler.repayment.impl;

import org.springframework.stereotype.Component;

import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod;
import com.suidifu.owlman.microservice.handler.ModifyOverdueFeeHandler;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;

@Component("modifyOverdueFeeMorganstanleySyncProxy")
public class ModifyOverdueFeeMorganstanleySyncProxy implements ModifyOverdueFeeHandler{

    @Override
    @MqSyncRpcMethod(beanName = "modifyOverdueFeeNewSyncProxy", methodName = "modifyOverdueFeeSaveLog")
    public void modifyOverdueFeeSaveLog(@MqRpcBusinessUuid String contractUuid, ModifyOverdueParams parameter,
                                        @MqRpcPriority int priority) throws Exception {
	}
}
