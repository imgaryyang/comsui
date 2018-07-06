package com.suidifu.jpmorgan.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.service.PaymentOrderWorkerConfigService;

@Service("paymentOrderWorkerConfigService")
public class PaymentOrderWorkerConfigServiceImpl extends
		GenericServiceImpl<PaymentOrderWorkerConfig> implements
		PaymentOrderWorkerConfigService {
	
	private static Log logger = LogFactory
			.getLog(PaymentOrderWorkerConfigServiceImpl.class);

	@Override
	public PaymentOrderWorkerConfig getPaymentOrderWorkerConfigBy(
			String workerUuid) {
		if (StringUtils.isEmpty(workerUuid)) {
			return null;
		}
		List<PaymentOrderWorkerConfig> workerConfigList = genericDaoSupport
				.searchForList(
						"FROM PaymentOrderWorkerConfig WHERE paymentOrderWorkerUuid =:workerUuid",
						"workerUuid", workerUuid);

		if (CollectionUtils.isEmpty(workerConfigList)) {
			return null;
		}
		return workerConfigList.get(0);
	}

	@Override
	public PaymentOrderWorkerConfig getPaymentOrderWorkerConfigByPaymentGatewayUuid(String paymentGatewayUuid) {
		if (StringUtils.isEmpty(paymentGatewayUuid)) {
			return null;
		}
		List<PaymentOrderWorkerConfig> workerConfigList = genericDaoSupport
				.searchForList(
						"FROM PaymentOrderWorkerConfig WHERE paymentGatewayUuid =:paymentGatewayUuid",
						"paymentGatewayUuid", paymentGatewayUuid);

		if (CollectionUtils.isEmpty(workerConfigList)) {
			return null;
		}
		return workerConfigList.get(0);
	}

}
