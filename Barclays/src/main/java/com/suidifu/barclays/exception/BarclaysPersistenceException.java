package com.suidifu.barclays.exception;

/**
 * BarclaysPersistenceException
 *
 * @author whb
 * @date 2017/6/25
 */

public class BarclaysPersistenceException extends Exception {

    private int code;

    public BarclaysPersistenceException(int code) {
        super();
        this.code = code;
    }

    public BarclaysPersistenceException(String message) {
        super(message);
    }

    public BarclaysPersistenceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
