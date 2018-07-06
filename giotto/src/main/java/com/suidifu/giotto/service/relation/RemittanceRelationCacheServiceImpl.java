package com.suidifu.giotto.service.relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.StringUtils;

@Component("remittanceRelationCacheService")
public class RemittanceRelationCacheServiceImpl implements RemittanceRelationCacheService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	private static final Log logger = LogFactory.getLog(RemittanceRelationCacheServiceImpl.class);

	@Override
	public void add(RemittanceRelation relation, String relationUuid, List<String> uuids) {
		if (null == relation || StringUtils.isEmpty(relationUuid) || CollectionUtils.isEmpty(uuids)) {
			return;
		}
		String redisKey = relationUuid.concat(relation.getSuffix());
		try {
			stringRedisTemplate.opsForSet().add(redisKey, uuids.toArray(new String[uuids.size()]));
		} catch (Exception e) {
			logger.error("add by key[" + redisKey + "] from redis error : " + e.getMessage());
			return;
		}
	}

	@Override
	public List<String> get(RemittanceRelation relation, String relationUuid) {
		String redisKey = relationUuid.concat(relation.getSuffix());
		try {
			Set<String> cacheResultList = stringRedisTemplate.opsForSet().members(redisKey);

			if (CollectionUtils.isNotEmpty(cacheResultList)) {
				return new ArrayList<>(cacheResultList);
			}
		} catch (Exception e) {
			logger.error("get by key[" + redisKey + "] from redis error : " + e.getMessage());
		}
		
		List<String> resultList = genericDaoSupport.queryForSingleColumnList(relation.getSql(),relation.getKey(), relationUuid,String.class);
		
		if (CollectionUtils.isEmpty(resultList)) {
			return Collections.emptyList();
		}
		add(relation, relationUuid, resultList);
		return resultList;

	}

	@Override
	public void delete(RemittanceRelation relation, String relationUuid) {
		if (null == relation || StringUtils.isEmpty(relationUuid)) {
			return;
		}
		String redisKey = relationUuid.concat(relation.getSuffix());
		try {
			stringRedisTemplate.delete(redisKey);
		} catch (Exception e) {
			logger.error("delete by key[" + redisKey + "] from redis error : " + e.getMessage());
			// throw new GiottoException();
		}
	}
}
