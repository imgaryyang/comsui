package com.suidifu.jpmorgan.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;

public interface PaymentOrderWorkerConfigService extends
		GenericService<PaymentOrderWorkerConfig> {

	PaymentOrderWorkerConfig getPaymentOrderWorkerConfigBy(String workerUuid);
	
	PaymentOrderWorkerConfig getPaymentOrderWorkerConfigByPaymentGatewayUuid(String paymentGatewayUuid);
}
