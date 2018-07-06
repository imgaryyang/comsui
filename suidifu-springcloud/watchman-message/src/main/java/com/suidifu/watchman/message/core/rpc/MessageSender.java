package com.suidifu.watchman.message.core.rpc;

import com.suidifu.watchman.message.core.request.Request;
import com.suidifu.watchman.message.core.response.Response;


public interface MessageSender {

    /**
     * 发送同步消息
     *
     * @param request
     */
    public void sendAsyncMessage(Request request) throws Exception;

    /**
     * 发送异步消息
     *
     * @param request
     * @return
     */
    public Response sendSyncMessage(Request request) throws Exception;
}
