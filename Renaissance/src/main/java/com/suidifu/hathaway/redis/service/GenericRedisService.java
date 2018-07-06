/**
 * 
 */
package com.suidifu.hathaway.redis.service;

import java.lang.reflect.Type;
import java.util.Date;

import org.springframework.data.redis.core.RedisOperations;

/**
 * @author wukai
 *
 */
public interface GenericRedisService<T> extends RedisOperations<String,String> {
	
	public abstract Type type();

	public T findOne(String redisKey,Type type);
	
	public T findOne(String redisKey);
	
	public void save(String redisKey,T t);
	
	public void save(String redisKey,T t,Date expriedTime);
}
