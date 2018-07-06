package com.suidifu.microservice.exception;

public class AssetRefundException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7061274023629429881L;
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

	public AssetRefundException() {

	}

	public AssetRefundException(String message) {
		this.message = message;
	}

}
