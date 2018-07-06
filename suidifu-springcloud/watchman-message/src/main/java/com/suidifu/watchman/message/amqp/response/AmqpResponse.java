package com.suidifu.watchman.message.amqp.response;

import com.suidifu.watchman.message.core.request.Request;
import com.suidifu.watchman.message.core.response.Response;

import java.util.UUID;
import lombok.NoArgsConstructor;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-10 18:08
 * description:
 */
@NoArgsConstructor
public class AmqpResponse extends Response {

    public AmqpResponse(Request request, Object result) {
        this.setUuid(UUID.randomUUID().toString());
        this.setReferRequestUuid(request.getUuid());
        this.setResult(result);
    }
}
