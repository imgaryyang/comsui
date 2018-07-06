package com.zufangbao.earth.report.exception;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;



public class ExportException extends IOException {
private static final long serialVersionUID = 7823104901088570592L;
	
	private int code;
	
	private String msg;

	public ExportException() {
		super();
	}

	public ExportException(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public ExportException(int code) {
		super();
		this.code = code;
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

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void printStackTrace() {
		String fullMsg = StringUtils.isEmpty(this.msg) ? getMessage() : this.msg;
		System.err.println("#报表导出异常 Code: " + this.code + " Msg: "+ fullMsg);
		super.printStackTrace();
	}

}
