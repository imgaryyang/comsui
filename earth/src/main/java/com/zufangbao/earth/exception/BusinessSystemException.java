package com.zufangbao.earth.exception;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Dec 14, 2016 4:57:14 PM 
* 类说明 
*/



public class BusinessSystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6517119443816277042L;

	private int code;
	
	private String msg;

	public BusinessSystemException() {
		super();
	}

	public BusinessSystemException(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public BusinessSystemException(int code) {
		super();
		this.code = code;
	}

	public BusinessSystemException(String msg) {
		super();
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String getMessage() {
		return getMsg();
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}