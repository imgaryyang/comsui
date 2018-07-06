package com.suidifu.watchman.message.core.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 消息请求体
 */
@Data
@NoArgsConstructor
public abstract class Request implements Serializable {

    private static final long serialVersionUID = -4770571847682450725L;

    private String uuid = UUID.randomUUID().toString();//消息的唯一标示

    private RequestMethod requestMethod;

    private String from;

    private String to;

    private InvokeType invokeType;

    private Map<String, String> headers = new HashMap<>();

    private Object playLoad;

    private int priority;

    private Date createTime;

    private Map<String, Object> appendix = new HashMap<>();

    public abstract void initialize() throws MalformedURLException;

    public Object getAppendixValue(String key) {
        return appendix.get(key);
    }

    public void putAppendix(String key, Object value) {
        this.appendix.put(key, value);
    }

    public String getHeaderValue(String key) {
        return headers.get(key);
    }
}