package com.suidifu.citigroup.exception;

/**
 * Created by zhagnlongfei on 2017/12/19.
 */
public class ValidatorException extends Exception {

    private static final long serialVersionUID = 5349359611290524971L;
    private int code;
    private String msg;
    public ValidatorException() {
        super();
    }

    public ValidatorException(int code) {
        super();
        this.code = code;
    }
    
    public ValidatorException( String msg) {
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
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getMessage(){
    	return this.getMsg();
    }
}
