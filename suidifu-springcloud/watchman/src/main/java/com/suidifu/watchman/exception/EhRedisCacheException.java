package com.suidifu.watchman.exception;

import lombok.Getter;

import static com.suidifu.watchman.common.constant.EhRedisCacheExceptionSpec.DEFAULT_ERROR_CODE;

/**
 * @author louguanyang at 2018/1/4 11:47
 * @mail louguanyang@hzsuidifu.com
 */
@Getter
public class EhRedisCacheException extends RuntimeException {

    private static final long serialVersionUID = 1486111133383638021L;
    /**
     * code
     */
    private final int code;

    public EhRedisCacheException(String message) {
        super(message);
        code = DEFAULT_ERROR_CODE;
    }

    public EhRedisCacheException(int code, String message) {
        super(message);
        this.code = code;
    }
}
