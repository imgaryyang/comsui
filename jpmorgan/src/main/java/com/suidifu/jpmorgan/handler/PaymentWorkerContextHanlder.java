package com.suidifu.jpmorgan.handler;

import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.exception.PaymentWorkerException;

public interface PaymentWorkerContextHanlder {
	public PaymentWorkerContext buildWorkerContext(String worderUuid)throws PaymentWorkerException;

}
