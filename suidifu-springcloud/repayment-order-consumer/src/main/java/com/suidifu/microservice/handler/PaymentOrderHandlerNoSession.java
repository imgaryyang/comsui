package com.suidifu.microservice.handler;

import com.demo2do.core.entity.Result;
import com.suidifu.owlman.microservice.exception.PaymentOrderException;
import com.suidifu.owlman.microservice.model.PaymentOrderSubmitModel;

public interface PaymentOrderHandlerNoSession {
    Result submit(PaymentOrderSubmitModel model) throws PaymentOrderException;
}