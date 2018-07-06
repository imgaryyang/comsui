package com.zufangbao.earth.yunxin.handler;

public class ModifyException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5342778939055999682L;
	
	private String message;
	
	public ModifyException() {
		super();
	}
	
	public ModifyException(String message) {
		super();
		this.message = message;
	}

	public String getMsg() {
		return message;
	}

	public void setMsg(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return getMsg();
	}
	
}
