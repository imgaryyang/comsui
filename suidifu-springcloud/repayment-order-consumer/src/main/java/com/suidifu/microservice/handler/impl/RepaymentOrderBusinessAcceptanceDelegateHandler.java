package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 扣款和签约的监听器
 * @author
 *
 */
@Component("repaymentOrderBusinessAcceptanceDelegateHandler")
public class RepaymentOrderBusinessAcceptanceDelegateHandler implements NotifyResultDelegateHandler {

	@Autowired
	private DeductApplicationService deductApplicationService;

	private static final Log logger = LogFactory.getLog(RepaymentOrderBusinessAcceptanceDelegateHandler.class);

	@Override
	public void onResult(NotifyJob result) {

		//更改deductApplication 状态
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductId(result.getBusinessId());
		deductApplication.setReceiveStatus(DeductApplicationReceiveStatus.SENDSUCCESS);
		deductApplicationService.saveOrUpdate(deductApplication);

		logger.info("#RepaymentOrderBusinessAcceptanceDelegateHandler#result["+ JsonUtils.toJsonString(result)+"]");


	}
}
