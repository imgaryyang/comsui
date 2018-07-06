package com.suidifu.datasync.config.domain;

import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * notify-keyspace-events
 * 
 * @author lisf
 *
 */
public class MyStringRedisTemplate extends StringRedisTemplate {

	static final String CONFIG_NOTIFY_KEYSPACE_EVENTS = "notify-keyspace-events";

	public MyStringRedisTemplate(RedisConnectionFactory connectionFactory) {
		super(connectionFactory);
	}

	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		// configureNotifyKeyspaceEventsAction(connection);
		return new DefaultStringRedisConnection(connection);
	}

	protected void configureNotifyKeyspaceEventsAction(RedisConnection connection) {
		String notifyOptions = getNotifyOptions(connection);
		String customizedNotifyOptions = notifyOptions;
		if (!customizedNotifyOptions.contains("E")) {
			customizedNotifyOptions += "E";
		}
		boolean A = customizedNotifyOptions.contains("A");
		if (!(A || customizedNotifyOptions.contains("g"))) {
			customizedNotifyOptions += "g";
		}
		if (!(A || customizedNotifyOptions.contains("x"))) {
			customizedNotifyOptions += "x";
		}
		if (!notifyOptions.equals(customizedNotifyOptions)) {
			connection.setConfig(CONFIG_NOTIFY_KEYSPACE_EVENTS, customizedNotifyOptions);
		}
	}

	private String getNotifyOptions(RedisConnection connection) {
		try {
			List<String> config = connection.getConfig(CONFIG_NOTIFY_KEYSPACE_EVENTS);
			if (config.size() < 2) {
				return "";
			}
			return config.get(1);
		} catch (InvalidDataAccessApiUsageException e) {
			throw new IllegalStateException(
					"Unable to configure Redis to keyspace notifications. See http://docs.spring.io/spring-session/docs/current/reference/html5/#api-redisoperationssessionrepository-sessiondestroyedevent",
					e);
		}
	}
}
