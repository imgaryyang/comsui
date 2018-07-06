package com.zufangbao.earth.websocket;

import com.zufangbao.sun.entity.security.Principal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Socket建立连接（握手）和断开
 * @author zhanghongbing
 *
 */
public class HandShakeBySDF implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
        	Authentication authentication = SecurityContextHolder.getContext() .getAuthentication();
    		if (authentication != null) {
    			if(authentication.getPrincipal() instanceof Principal) {
    				Principal principal = (Principal) authentication.getPrincipal();
    				if(principal != null && principal.getId() != null) {
    					attributes.put("id", principal.getId());
    					return true;
    		        }
    			}
    		}
    		return false;
        }
        return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}

}
