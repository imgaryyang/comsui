package com.suidifu.giotto.exception;

/**
 * GiottoException
 *
 * @author whb
 * @date 2017/5/23
 */

public class GiottoException extends Exception {

    private int code;

    public GiottoException(int code) {
        super();
        this.code = code;
    }

    public GiottoException(String message) {
        super(message);
    }

    public GiottoException(int code, String message) {
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
