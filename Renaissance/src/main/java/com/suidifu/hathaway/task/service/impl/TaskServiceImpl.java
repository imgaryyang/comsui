/**
 *
 */
package com.suidifu.hathaway.task.service.impl;

import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.redis.service.impl.GenericRedisServiceImpl;
import com.suidifu.hathaway.task.service.TaskService;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author wukai
 *
 */
@Service("taskService")
public class TaskServiceImpl extends GenericRedisServiceImpl<Task>  implements TaskService   {

	@Override
	public Type type() {
		return Task.class;
	}

	@Override
	public ClusterOperations<String, String> opsForCluster() {
		return null;
	}
}
