package com.suidifu.owlman.microservice.exception;

public class BankCancelPaymentException extends RuntimeException {
    private static final long serialVersionUID = -2543496960219768726L;
    private int code;
    private String message;

    public BankCancelPaymentException() {

    }

    public BankCancelPaymentException(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}