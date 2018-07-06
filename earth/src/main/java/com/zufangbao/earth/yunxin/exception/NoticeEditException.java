package com.zufangbao.earth.yunxin.exception;

public class NoticeEditException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3082113343145800530L;
	private int code;
	private String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	@Override
    public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public NoticeEditException(){

	}

	public NoticeEditException(String message){
		this.message = message;
	}
}
