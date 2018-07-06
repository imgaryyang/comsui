package com.zufangbao.earth.yunxin.handler;

public class CreateException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -199463139145627028L;
	
	private String message;
	
	public CreateException() {
		super();
	}
	
	public CreateException(String message) {
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
