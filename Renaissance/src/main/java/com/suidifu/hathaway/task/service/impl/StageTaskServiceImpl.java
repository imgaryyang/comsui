/**
 *
 */
package com.suidifu.hathaway.task.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.suidifu.hathaway.redis.service.impl.GenericRedisServiceImpl;
import com.suidifu.hathaway.task.service.StageTaskService;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author wukai
 *
 */
@Service("stageTaskService")
public class StageTaskServiceImpl extends GenericRedisServiceImpl<Set<String>> implements
		StageTaskService {

	@Override
	public Type type() {
		return new TypeReference<Set<String>>(){}.getType();
	}

	@Override
	public ClusterOperations<String, String> opsForCluster() {
		return null;
	}
}
