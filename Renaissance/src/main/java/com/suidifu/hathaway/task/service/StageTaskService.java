/**
 * 
 */
package com.suidifu.hathaway.task.service;

import java.util.Set;

import com.suidifu.hathaway.redis.service.GenericRedisService;

/**
 * @author wukai
 *
 */
public interface StageTaskService extends GenericRedisService<Set<String>> {

}
