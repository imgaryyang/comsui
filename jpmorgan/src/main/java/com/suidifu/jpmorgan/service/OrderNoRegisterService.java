package com.suidifu.jpmorgan.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.OrderNoRegister;

public interface OrderNoRegisterService extends GenericService<OrderNoRegister> {

	void logOffRegister(String outlierTransactionUuid);
}
