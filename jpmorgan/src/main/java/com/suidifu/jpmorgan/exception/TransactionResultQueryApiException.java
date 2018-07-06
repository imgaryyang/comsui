package com.suidifu.jpmorgan.exception;

public class TransactionResultQueryApiException extends Exception {

	private static final long serialVersionUID = 5437548731129190509L;
	
	private int code;
	
	private String message;
	
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
	
	public TransactionResultQueryApiException(int code){
		this.code = code;
	}
	
	public TransactionResultQueryApiException(int code,String message){
		this.code = code;
		this.message = message;
	}
	
}
