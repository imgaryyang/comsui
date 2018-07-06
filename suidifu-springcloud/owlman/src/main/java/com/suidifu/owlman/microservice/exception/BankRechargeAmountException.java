package com.suidifu.owlman.microservice.exception;

public class BankRechargeAmountException extends RuntimeException {
    private static final long serialVersionUID = -9002649588690812845L;
    private int code;
    private String message;

    public BankRechargeAmountException() {
    }

    public BankRechargeAmountException(String message) {
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