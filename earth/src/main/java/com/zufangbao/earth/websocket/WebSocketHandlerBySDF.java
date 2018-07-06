package com.zufangbao.earth.websocket;

import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 前端推送处理
 * @author zhanghongbing
 *
 */
//@Component
public class WebSocketHandlerBySDF implements WebSocketHandler {

	public static final Map<Long, WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<Long, WebSocketSession>();
    }

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1)
			throws Exception {
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Long, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                break;
            }
        }
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		Long uid = (Long) session.getAttributes().get("id");
        if (!userSocketSessionMap.containsKey(uid)) {
            userSocketSessionMap.put(uid, session);
        }
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
			throws Exception {
//		if (message.getPayloadLength() == 0) {
//			return;
//		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable arg1)
			throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				break;
			}
		}

	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
     * 给某个用户发送消息
     */
	public void sendMessageToUser(Long id, TextMessage message)
			throws IOException {
		WebSocketSession session = userSocketSessionMap.get(id);
		if (session != null && session.isOpen()) {
			session.sendMessage(message);
		}
	}

	/**
     * 给所有在线用户发送消息
     */
    public void broadcast(final TextMessage message) throws IOException {
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();

        while (it.hasNext()) {
            final Entry<Long, WebSocketSession> entry = it.next();
            if (entry.getValue().isOpen()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

        }
    }

}
