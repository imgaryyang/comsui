package com.zufangbao.earth.websocket;

import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

//@Configuration
//@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Resource
    private WebSocketHandlerBySDF handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws").addInterceptors(new HandShakeBySDF());

        registry.addHandler(handler, "/ws/sockjs").addInterceptors(new HandShakeBySDF()).withSockJS();
    }

}
