package com.suidifu.jpmorgan.exception.cache;

public class PaymentOrderCacheLocationException extends Exception {

    private Integer code;

    private String message;

    public PaymentOrderCacheLocationException(String message) {
        this.message = message;
    }

    public PaymentOrderCacheLocationException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
