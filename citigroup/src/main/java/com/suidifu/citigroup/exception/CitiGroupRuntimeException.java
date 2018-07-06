package com.suidifu.citigroup.exception;

/**
 * Created by louguanyang on 2017/5/24.
 */
public class CitiGroupRuntimeException extends RuntimeException {
    private Integer code;
    private String message;

    public CitiGroupRuntimeException(){
    	super();
    }
    
    public CitiGroupRuntimeException(Integer code) {
        this.code = code;
    }

    public CitiGroupRuntimeException(String message) {
        this.message = message;
    }

    public CitiGroupRuntimeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
