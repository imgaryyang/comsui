package com.suidifu.owlman.microservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSystemException extends Exception {
    private int code;

    private String msg;

    public PaymentSystemException(int code) {
        super();
        this.code = code;
    }

    public PaymentSystemException(String msg) {
        super();
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return getMsg();
    }
}