package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.proxy.ThirdPartVoucherWithReconciliationNoSession;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.pricewaterhouse.mq.adapter.ConsistentHashMessageProducer;
import com.suidifu.pricewaterhouse.yunxin.handler.ThirdPartyDeductNoSessionV2_0;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Component("thirdPartyDeductNoSessionV2_0")
public class ThirdPartyDeductNoSessionImplV2_0 implements ThirdPartyDeductNoSessionV2_0 {

	@Autowired
	@Qualifier("thirdPartVoucherWithReconciliationNoSession")
	private ThirdPartVoucherWithReconciliationNoSession thirdPartVoucherWithReconciliationNoSession;

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private DeductApplicationService deductApplicationService;

	@Resource(name ="messageProducer")
	private ConsistentHashMessageProducer messageProducer;
	
	private Log logger = LogFactory.getLog(getClass());
	
	@Value("${deduct.application.audit.bean}")
	private String bean;

	@Value("${deduct.application.audit.method}")
	private String method;

	@Override
	public void loopGenerateVoucherWithReconciliation() {
		logger.info("begin to generate  third part journal voucher and reconciliation");
		Date now_add_ten_min = DateUtils.addMinutes(new Date(), -10);
		Map<String, String> map = deductApplicationService.getDeductApplicationWithContractUuidListAndCompleteTimeBefore(now_add_ten_min);
		if (CollectionUtils.isEmpty(map))
			return;
		for (String deductApplicationUuid : map.keySet()) {
			try {
				logger.info("begin to generate  third part journal voucher and reconciliation deductApplicationUuid ["
						+ deductApplicationUuid + "]");
				Request request = newRequest();
				
				DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
				Contract contract = contractService.getContractBy(null,deductApplication.getContractNo());
				request.setUuid(deductApplication.getDeductApplicationUuid());
				request.setBusinessId(contract.getUuid());
				sendMessage(request);
				logger.info("end to generate  third part journal voucher and reconciliation deductApplicationUuid ["
						+ deductApplicationUuid + "]");
			} catch (Exception e) {
				logger.error(
						"occur error when loop gengerate third part voucher and reconciliation deductApplicationUuid ["
								+ deductApplicationUuid + "]");
				e.printStackTrace();
			}
		}
	}
	

	private void sendMessage(Request request) {
		Object[] params = new Object[2];
		params[0] = request.getBusinessId();
		params[1] = request.getUuid();
		
		request.setParams(params);
		messageProducer.rpc().sendAsync(request,6);
		logger.info(request.getUuid());
	}
	
	private Request newRequest() {
		Request request = new Request();
		request.setBean(bean);
		request.setMethod(method);
		return request;
	}

}
