package com.zufangbao.earth.yunxin.exception;

public class BusinessPaymentVoucherException extends RuntimeException {


	private static final long serialVersionUID = 6111145029104552277L;

	private int errorCode;

	private String errorMsg;

	public BusinessPaymentVoucherException(int errorCode,String errorMsg ){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public BusinessPaymentVoucherException(String errorMsg){
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
    public String getMessage(){
		return this.getErrorMsg();
	}

}
