package com.suidifu.berkshire.mq.rpc.proxy.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.ModifyOverdueFeeHandler;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.wellsfargo.yunxin.handler.ModifyOverdueFeeNewHandler;

@Component("modifyOverdueFeeNewSyncProxy")
public class ModifyOverdueFeeNewSyncProxy  implements ModifyOverdueFeeHandler{

	@Autowired
	private ModifyOverdueFeeNewHandler modifyOverdueFeeNewHandler;
	
	@Override
	@MicroService(beanName="modifyOverdueFeeNewHandler",
			methodName="modifyOverdueFeeSaveLog",
			sync=true,
			vhostName="/business",
			exchangeName="exchange-business",
			routingKey = "modify-overdue-fee")
	public void modifyOverdueFeeSaveLog(String contractUuid,
			ModifyOverdueParams modifyOverDueFeeDetail, int priority) throws Exception {
		// TODO Auto-generated method stub
		modifyOverdueFeeNewHandler.modifyOverdueFeeSaveLog(contractUuid, modifyOverDueFeeDetail, priority);
	}
}
