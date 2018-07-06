package com.suidifu.mq.rpc.response;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.springframework.util.StringUtils;

public class Response implements Serializable {
	private static final long serialVersionUID = 6472055288408283265L;
	private Object result;
	private String stackTrace;
	private String exceptionType;
	private String exceptionCode;
	private String exceptionMsg;

	public Response() {

	}

	public Response(Throwable error) {
		this.setError(error, null, null);
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setError(Throwable error, String errorCode, String errorMsg) {
		if (error == null)
			return;
		StringWriter sw = new StringWriter();
		if (error.getCause() != null)
			error = error.getCause();
		error.printStackTrace(new PrintWriter(sw));
		this.stackTrace = sw.toString();
		this.exceptionType = error.getClass().getName();
		this.exceptionCode = errorCode;
		this.exceptionMsg = errorMsg;
	}

	public Throwable newThrowable() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (Throwable) Class.forName(exceptionType).newInstance();
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getExceptionType() {
		return exceptionType;
	}
	
	public boolean isExceptionFired(){
		return !StringUtils.isEmpty(this.getExceptionType());
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	@Override
	public String toString() {
		return "Response [result=" + result + ", stackTrace=" + stackTrace + ", exceptionType=" + exceptionType + "]";
	}
}