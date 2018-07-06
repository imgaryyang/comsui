package com.suidifu.watchman.message.amqp.request;

import com.suidifu.watchman.message.amqp.codec.CodecType;
import com.suidifu.watchman.message.core.request.InvokeType;
import com.suidifu.watchman.message.core.request.Request;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.MessageProperties;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-10 18:17
 * description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AmqpRequest extends Request {
    public static final String PARAM_TYPES = "paramTypes";
    public static final String ROUTING_KEY = "routingKey";
    public static final String ENCODING_TYPE = "encodingType";

    private String vhost;
    private String exchange;
    private String bean;
    private String method;
    private Object[] params;
    private String[] paramTypes;
    private String routingKey;
    private CodecType encodingType;

    public AmqpRequest(String uuid, String businessId, String bean, String method, Object[] params,
                       String vhostName, String exchangeName, String routingKey, boolean retry, int priority, boolean sync,String[] paramTypes) {
        this.setUuid(uuid);
        this.setBean(bean);
        this.setMethod(method);
        this.setParams(params);
        this.vhost = vhostName;
        this.exchange = exchangeName;
        this.routingKey = routingKey;
        this.setPriority(priority);
        this.setInvokeType(sync ? InvokeType.Sync : InvokeType.Async);
        this.paramTypes = paramTypes;

        this.putAppendix("businessId", businessId);
        this.putAppendix("retry", retry);
    }

    @Override
    public void initialize() throws MalformedURLException {

        String amqpUrl = super.getTo();

        if (Pattern.matches("^sdf://\\([A-Za-z+].\\){3}[A-Za-z]{1}", amqpUrl)) {
            throw new MalformedURLException("this request url[" + amqpUrl + "] is  malformed");
        }

        String[] urlParts = (amqpUrl.split("sdf://")[1]).split("\\.");

        this.vhost = urlParts[0];
        this.exchange = urlParts[1];
        this.bean = urlParts[2];
        this.method = urlParts[3];
        this.params = (Object[]) super.getPlayLoad();
        this.paramTypes = (String[]) super.getAppendixValue(PARAM_TYPES);
        this.routingKey = super.getHeaderValue(ROUTING_KEY);
        this.encodingType = CodecType.fromOridinal(Integer.parseInt(super.getHeaderValue(ENCODING_TYPE)));
        if (super.getPriority() < 0) {
            this.setPriority(MessageProperties.DEFAULT_PRIORITY);
        }
    }
}
