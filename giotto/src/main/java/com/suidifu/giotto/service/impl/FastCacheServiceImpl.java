package com.suidifu.giotto.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.github.tomakehurst.wiremock.common.Json;
import com.suidifu.giotto.exception.GiottoException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.keyenum.FastAccountTemplateKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastCacheObject;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.giotto.service.FastService;

/**
 * FastCacheServiceImpl
 *
 * @author whb
 * @date 2017/5/26
 */
@Component(value = "fastCacheService")
public class FastCacheServiceImpl implements FastService {

    private static final Log LOGGER = LogFactory.getLog(FastCacheServiceImpl.class);

    @Autowired private StringRedisTemplate redisTemplate;

    @Override
    public <T> Map<String, List<T>> getByKeyList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz) {
        if (CollectionUtils.isEmpty(keyValueList)) {
            return Collections.emptyMap();
        }
        // 获取查询实际使用的key
        Map<String, String> queryKeyValMap = new HashMap<>(keyValueList.size());
        for (String keyValue : keyValueList) {
            queryKeyValMap.put(keyValue, keyEnum.getKey(keyValue));
        }
        try {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            List<Object> keyList = redisTemplate.executePipelined(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    for (Map.Entry<String, String> entry : queryKeyValMap.entrySet()) {
                        byte[] keyByte = serializer.serialize(entry.getValue());
                        connection.sMembers(keyByte);
                    }
                    return null;
                }
            }, serializer);

            List<Object> resultList = redisTemplate.executePipelined(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    for (Object obj : keyList) {
                        if (null == obj) continue;
                        Set<String> keySet = (Set<String>) obj;
                        if (CollectionUtils.isEmpty(keySet)) {
                            continue;
                        }
                        for (String key : keySet) {
                            byte[] keyByte = serializer.serialize(key);
                            connection.get(keyByte);
                        }
                    }
                    return null;
                }
            }, serializer);
            Map<String, List<T>> resultMap = new HashMap<>();
            for (Object obj : resultList) {
                if (null == obj) continue;
                T t = JsonUtils.parse((String) obj, clazz);
                if (t instanceof FastRepaymentOrderItem) {
                    FastRepaymentOrderItem orderItem = (FastRepaymentOrderItem) t;
                    if (resultMap.containsKey(orderItem.obtainQueryListKeyValue())) {
                        List<T> tList = resultMap.get(orderItem.obtainQueryListKeyValue());
                        tList.add(t);
                        resultMap.put(orderItem.obtainQueryListKeyValue(), tList);
                    } else {
                        List<T> tList = new ArrayList<>();
                        tList.add(t);
                        resultMap.put(orderItem.obtainQueryListKeyValue(), tList);
                    }
                } else {
                    break;
                }
            }
            for (String key : keyValueList) {
                if (!resultMap.containsKey(key)) {
                    resultMap.put(key, null);
                }
            }
            return resultMap;
        } catch (Exception e) {
            LOGGER.error("batch add from redis error : " + ExceptionUtils.getFullStackTrace(e));
        }
        return Collections.emptyMap();
    }

    @Override
    public <T> List<T> getByKey(FastKey keyEnum, String keyValue, Class<T> clazz) {
        return getByKeyPattern(keyEnum, keyValue, clazz);
    }

    @Override
    public void delByKey(FastKey keyEnum, String keyValue) {
        delete(keyEnum, keyValue);
    }

    @Override
    public void add(FastCacheObject fastObject) {
        String val = fastObject.toString();
        List<String> queryKeyList = fastObject.obtainAddCacheKeyList();
        if (CollectionUtils.isEmpty(queryKeyList)) {
            LOGGER.error("add could not get query key list.");
            return;
        }
        try {
            String key = fastObject.obtainAddCacheKey();
            for (String queryKey : queryKeyList) {
                BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(queryKey);
                setOps.add(key);
                // 更新过期时间
                Long expire = setOps.getExpire();
                if (null == expire || expire <= 0) {
                    setOps.expire(7, TimeUnit.DAYS);
                }
            }
            redisTemplate.opsForValue().set(key, val, 7, TimeUnit.DAYS);
        } catch (Exception e) {
            LOGGER.error("add by key from redis error : " + ExceptionUtils.getFullStackTrace(e));
//            throw new GiottoException();
        }
    }

    public void addNormal(FastCacheObject fastObject) {
//        LOGGER.info("---去除keys的实现---");
        String val = fastObject.toString();
        try {
            String key = fastObject.obtainAddCacheKey();
            if (StringUtils.isBlank(key) || StringUtils.isBlank(val)) {
                LOGGER.error("add could not get query key or value.");
                return;
            }
            redisTemplate.boundValueOps(key).set(val);
        } catch (Exception e) {
            LOGGER.error("add by key from redis error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public void addList(List<FastCacheObject> fastCacheObjectList) {
        if (CollectionUtils.isEmpty(fastCacheObjectList)) {
            return;
        }
        // 在管道操作redis中无法获取key的过期时间，因此只能在这里判断key是否存在
        // 存在就不再设置过期时间, 不存在设置过期时间
        Map<String, Boolean> hashKeyMap = new HashMap<>();
        for (FastCacheObject fastCacheObject : fastCacheObjectList) {
            List<String> queryKeyList = fastCacheObject.obtainAddCacheKeyList();
            for (String queryKey : queryKeyList) {
                hashKeyMap.put(queryKey, redisTemplate.hasKey(queryKey));
            }
        }

        try {
            redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    for (FastCacheObject fastCacheObject : fastCacheObjectList) {
                        List<String> queryKeyList = fastCacheObject.obtainAddCacheKeyList();
                        if (CollectionUtils.isEmpty(queryKeyList)) {
                            LOGGER.error("add could not get query key list.");
                            continue;
                        }
                        String key = null;
                        try {
                            key = fastCacheObject.obtainAddCacheKey();
                        } catch (GiottoException e) {
                            LOGGER.error("batch add from redis error : " + ExceptionUtils.getFullStackTrace(e));
                            continue;
                        }
                        byte[] keyByte = serializer.serialize(key);
                        for (String queryKey : queryKeyList) {
                            byte[] queryKeyByte = serializer.serialize(queryKey);
                            connection.sAdd(queryKeyByte, keyByte);
//                            Long second = connection.ttl(queryKeyByte);
                            if (!hashKeyMap.get(queryKey)) {
                                connection.expire(queryKeyByte, 60*60*24*7);
                            }
                        }
                        String val = fastCacheObject.toString();
                        byte[] valByte = serializer.serialize(val);
                        connection.setEx(keyByte, 60*60*24*7, valByte);
                    }
                    return true;
                }
            }, false, true);
        } catch (Exception e) {
            LOGGER.error("batch add from redis error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public int updateInDB(String updateSql, Map<String, Object> paramMap,
                          FastCacheObject fastCacheObject) {
        return 0;
    }

    private <T> List<T> getByKeyPattern(FastKey keyEnum, String keyValue, Class<T> clazz) {
        String queryKey = keyEnum.getKey(keyValue);
        List<T> orderItemList = new ArrayList<>();
        try {
            BoundSetOperations<String, String> ops = redisTemplate.boundSetOps(queryKey);
            Set<String> valKeySet = ops.members();
            if (CollectionUtils.isEmpty(valKeySet)) {
                return orderItemList;
            }
            for (String valKey : valKeySet) {
                String val = redisTemplate.opsForValue().get(valKey);
                if (StringUtils.isBlank(val)) {
                    ops.remove(valKey);
                    continue;
                }
                orderItemList.add(JsonUtils.parse(val, clazz));
            }
            return orderItemList;
        } catch (Exception e) {
            LOGGER.error("get by key[" + queryKey +"] from redis error : " + e.getMessage());
            return Collections.EMPTY_LIST;
//            throw new GiottoException();
        }
    }

    private void delete(FastKey keyEnum, String keyValue) {
        String delKey = keyEnum.getKey(keyValue);
        try {
            Set<String> valKeySet = redisTemplate.opsForSet().members(delKey);
            if (CollectionUtils.isEmpty(valKeySet)) {
                return;
            }
            // 这里只删除当前key，及当前key引用的key，别的引用引用的key的key在获取的时候删除
            for (String valKey : valKeySet) {
                redisTemplate.delete(valKey);
            }
            redisTemplate.delete(delKey);
        } catch (Exception e) {
            LOGGER.error("delete by key[" + delKey +"] from redis error : " + e.getMessage());
//            throw new GiottoException();
        }
    }

	@Override
	public <T> List<T> getByKey(Map<FastKey, String> map, Class<T> clazz) {
		return getByKeyPatternV2(map, clazz);
	}
    
	
	private <T> List<T> getByKeyPatternV2(Map<FastKey, String> map, Class<T> clazz){
//        LOGGER.info("---去除keys的实现---");
        String keyPattern = FastAccountTemplateKeyEnum.LEDGER_BOOK_NO.getKeyPattern(map);

        try {
            String result = redisTemplate.boundValueOps(keyPattern).get();
            if (StringUtils.isBlank(result)) {
                return Collections.EMPTY_LIST;
            }
            List<T> orderItemList = new ArrayList<>(1);
            orderItemList.add(JsonUtils.parse(result, clazz));
            return orderItemList;
        } catch (Exception e) {
            LOGGER.error("get FastAccountTemplate by key[" + keyPattern +"] from redis error : " + ExceptionUtils.getFullStackTrace(e));
            return Collections.EMPTY_LIST;
        }
//			try {
//	            Set<String> keySet = redisTemplate.keys(keyPattern);
//	            if (CollectionUtils.isEmpty(keySet)) {
//	                return Collections.EMPTY_LIST;
//	            }
//	            List<T> orderItemList = new ArrayList<>(keySet.size());
//	            for (String keyStr : keySet) {
//	                BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(keyStr);
//	                String fastAssetSetStr = ops.get();
//	                if (StringUtils.isNotBlank(fastAssetSetStr)) {
//	                    orderItemList.add(JsonUtils.parse(fastAssetSetStr, clazz));
//	                }
//	            }
//	            return orderItemList;
//	        } catch (Exception e) {
//	            LOGGER.error("get FastRepaymentOrderItem by key["
//	                    + keyPattern +"] from redis error : " + e.getMessage());
//	            return Collections.EMPTY_LIST;
////	            throw new GiottoException();
//	        }
	}
    
}
