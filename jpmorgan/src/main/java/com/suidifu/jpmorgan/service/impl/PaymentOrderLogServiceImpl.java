package com.suidifu.jpmorgan.service.impl;

import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.PaymentOrderLog;
import com.suidifu.jpmorgan.service.PaymentOrderLogService;

@Service("paymentOrderLogService")
public class PaymentOrderLogServiceImpl extends
		GenericServiceImpl<PaymentOrderLog> implements PaymentOrderLogService {

}
