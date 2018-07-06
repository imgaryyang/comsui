/**
 * 
 */
package com.suidifu.hathaway.redis.service.impl;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.redis.service.GenericRedisService;

/**
 * @author wukai
 *
 */
public abstract class GenericRedisServiceImpl<T>  implements GenericRedisService<T> {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public T findOne(String redisKey,Type type) {
		if(StringUtils.isEmpty(redisKey) || null == type){
			return null;
		}
		return JSON.parseObject(this.opsForValue().get(redisKey), type);
	}
	
	@Override
	public T findOne(String redisKey) {
		return findOne(redisKey, type());
	}
	@Override
	public void save(String rediskey,T t) {
		if(StringUtils.isEmpty(rediskey) || null == t){
			return;
		}
		this.opsForValue().set(rediskey, JsonUtils.toJsonString(t));
	}
	@Override
	public void save(String redisKey,T t,Date expriedTime){
		if(StringUtils.isEmpty(redisKey) || null == t || null == expriedTime){
			return;
		}
		save(redisKey, t);
		this.expireAt(redisKey, expriedTime);
	}
	@Override
	public <T> T execute(RedisCallback<T> action) {
		return stringRedisTemplate.execute(action);
	}

	@Override
	public <T> T execute(SessionCallback<T> session) {
		return stringRedisTemplate.execute(session);
	}

	@Override
	public List<Object> executePipelined(RedisCallback<?> action) {
		return stringRedisTemplate.executePipelined(action);
	}

	@Override
	public List<Object> executePipelined(RedisCallback<?> action,
			RedisSerializer<?> resultSerializer) {
		return stringRedisTemplate.executePipelined(action,resultSerializer);
	}

	@Override
	public List<Object> executePipelined(SessionCallback<?> session) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.executePipelined(session);
	}

	@Override
	public List<Object> executePipelined(SessionCallback<?> session,
			RedisSerializer<?> resultSerializer) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.executePipelined( session, resultSerializer);
	}

	@Override
	public <T> T execute(RedisScript<T> script, List<String> keys,
			Object... args) {
		return stringRedisTemplate.execute(script,keys,args);
	}

	@Override
	public <T> T execute(RedisScript<T> script,
			RedisSerializer<?> argsSerializer,
			RedisSerializer<T> resultSerializer, List<String> keys,
			Object... args) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.execute(script,
				 argsSerializer,resultSerializer,keys,args);
	}

	@Override
	public Boolean hasKey(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.hasKey(key);
	}

	@Override
	public void delete(String key) {
		// TODO Auto-generated method stub
		stringRedisTemplate.delete(key);
	}

	@Override
	public void delete(Collection<String> key) {
		// TODO Auto-generated method stub
		stringRedisTemplate.delete(key);
	}

	@Override
	public DataType type(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.type(key);
	}

	@Override
	public Set<String> keys(String pattern) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.keys(pattern);
	}

	@Override
	public String randomKey() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.randomKey();
	}

	@Override
	public void rename(String oldKey, String newKey) {
		// TODO Auto-generated method stub
		stringRedisTemplate.rename(oldKey, newKey);
	}

	@Override
	public Boolean renameIfAbsent(String oldKey, String newKey) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
	}

	@Override
	public Boolean expire(String key, long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.expire(key, timeout, unit);
	}

	@Override
	public Boolean expireAt(String key, Date date) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.expireAt(key, date);
	}

	@Override
	public Boolean persist(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.persist(key);
	}

	@Override
	public Boolean move(String key, int dbIndex) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.move(key, dbIndex);
	}

	@Override
	public byte[] dump(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.dump(key);
	}

	@Override
	public void restore(String key, byte[] value, long timeToLive, TimeUnit unit) {
		// TODO Auto-generated method stub
		stringRedisTemplate.restore(key, value, timeToLive, unit);
	}

	@Override
	public Long getExpire(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getExpire(key);
	}

	@Override
	public Long getExpire(String key, TimeUnit timeUnit) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getExpire(key,timeUnit);
	}

	@Override
	public void watch(String keys) {
		// TODO Auto-generated method stub
		stringRedisTemplate.watch(keys);
	}

	@Override
	public void watch(Collection<String> keys) {
		// TODO Auto-generated method stub
		stringRedisTemplate.watch(keys);
	}

	@Override
	public void unwatch() {
		// TODO Auto-generated method stub
		stringRedisTemplate.unwatch();
	}

	@Override
	public void multi() {
		// TODO Auto-generated method stub
		stringRedisTemplate.multi();
	}

	@Override
	public void discard() {
		// TODO Auto-generated method stub
		stringRedisTemplate.discard();
	}

	@Override
	public List<Object> exec() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.exec();
	}

	@Override
	public List<RedisClientInfo> getClientList() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getClientList();
	}

	@Override
	public List<Object> exec(RedisSerializer<?> valueSerializer) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.exec(valueSerializer);
	}

	@Override
	public void convertAndSend(String destination, Object message) {
		// TODO Auto-generated method stub
		stringRedisTemplate.convertAndSend(destination, message);
	}

	@Override
	public ValueOperations<String, String> opsForValue() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.opsForValue();
	}

	@Override
	public BoundValueOperations<String, String> boundValueOps(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.boundValueOps(key);
	}

	@Override
	public ListOperations<String, String> opsForList() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.opsForList();
	}

	@Override
	public BoundListOperations<String, String> boundListOps(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.boundListOps(key);
	}

	@Override
	public SetOperations<String, String> opsForSet() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.opsForSet();
	}

	@Override
	public BoundSetOperations<String, String> boundSetOps(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.boundSetOps(key);
	}

	@Override
	public ZSetOperations<String, String> opsForZSet() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.opsForZSet();
	}

	@Override
	public HyperLogLogOperations<String, String> opsForHyperLogLog() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.opsForHyperLogLog();
	}

	@Override
	public BoundZSetOperations<String, String> boundZSetOps(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.boundZSetOps(key);
	}

	@Override
	public <HK, HV> HashOperations<String, HK, HV> opsForHash() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.opsForHash();
	}

	@Override
	public <HK, HV> BoundHashOperations<String, HK, HV> boundHashOps(String key) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.boundHashOps(key);
	}

	@Override
	public List<String> sort(SortQuery<String> query) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.sort(query);
	}

	@Override
	public <T> List<T> sort(SortQuery<String> query,
			RedisSerializer<T> resultSerializer) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.sort( query, resultSerializer);
	}

	@Override
	public <T> List<T> sort(SortQuery<String> query,
			BulkMapper<T, String> bulkMapper) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.sort( query, bulkMapper);
	}

	@Override
	public <T, S> List<T> sort(SortQuery<String> query,
			BulkMapper<T, S> bulkMapper, RedisSerializer<S> resultSerializer) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.sort(query, bulkMapper, resultSerializer);
	}

	@Override
	public Long sort(SortQuery<String> query, String storeKey) {
		// TODO Auto-generated method stub
		return stringRedisTemplate.sort(query,  storeKey);
	}

	@Override
	public RedisSerializer<?> getValueSerializer() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getValueSerializer();
	}

	@Override
	public RedisSerializer<?> getKeySerializer() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getKeySerializer();
	}

	@Override
	public RedisSerializer<?> getHashKeySerializer() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getHashKeySerializer();
	}

	@Override
	public RedisSerializer<?> getHashValueSerializer() {
		// TODO Auto-generated method stub
		return stringRedisTemplate.getHashValueSerializer();
	}

	@Override
	public void killClient(String host, int port) {
		// TODO Auto-generated method stub
		stringRedisTemplate.killClient(host, port);
		
	}

	@Override
	public void slaveOf(String host, int port) {
		// TODO Auto-generated method stub
		stringRedisTemplate.slaveOf(host, port);
	}

	@Override
	public void slaveOfNoOne() {
		// TODO Auto-generated method stub
		stringRedisTemplate.slaveOfNoOne();
	}

	
}
