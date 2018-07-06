package com.suidifu.watchman.message.core.response;

import com.suidifu.watchman.message.core.request.Request;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 消息响应体
 */
@Data
@NoArgsConstructor
public abstract class Response implements Serializable {

    private static final long serialVersionUID = 6472055288408283265L;

    private String uuid; // 消息的唯一标示

    /**
     * @see Request#getUuid()
     */
    private String referRequestUuid;//request请求的uuid

    private String from;

    private String to;

    private Object result; // 消息结果

    private String stackTrace; // 异常信息

    private String exceptionType;// 异常的类型

    private String code; // code

    private String message; // messageØ

    private Date createTime;

    private Map<String, Object> appendix;

    public void setError(Throwable error, String errorCode, String errorMsg) {
        if (error == null)
            return;
        StringWriter sw = new StringWriter();
        if (error.getCause() != null)
            error = error.getCause();
        error.printStackTrace(new PrintWriter(sw));
        this.stackTrace = sw.toString();
        this.exceptionType = error.getClass().getName();
        this.code = errorCode;
        this.message = errorMsg;
    }

    public boolean isExceptionFired(){
        return !StringUtils.isEmpty(this.getExceptionType());
    }

}