package com.suidifu.hathaway.redis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.TypeReference;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.task.service.StageTaskService;
import com.suidifu.hathaway.task.service.TaskService;
import com.suidifu.hathaway.test.BaseTestContext;

public class GenericRedisServiceTest extends BaseTestContext{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	private StageTaskService stageTaskService;

	@Autowired
	private TaskService taskService;
	
	@Test
	public void testFindOneWithString() {
		
		String redisKey = "redisKey";
		
		taskService.delete(redisKey);
		
		Task task = new Task();
		
		String jobUUid = "jobUUid";
		
		task.setJobUUid(jobUUid);
		
		taskService.save(redisKey, task);
		
		Task taskInRedis = taskService.findOne(redisKey, new TypeReference<Task>(){}.getType());
		
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		
		Task taskInRedis2 = taskService.findOne(redisKey, new TypeReference<Task>(){}.getType());
		
		assertEquals(jobUUid,taskInRedis2.getJobUUid());
	}
	@Test
	public void testFindOneWithNullInput() {
		
		assertNull(taskService.findOne(null, Task.class));
		assertNull(taskService.findOne("", Task.class));
		assertNull(taskService.findOne(" ", Task.class));
		assertNull(taskService.findOne(" xxxx", null));
		
	}
	@Test
	public void testFindOneWithNotExistInput() {
		
		assertNull(taskService.findOne("xxxxxxxxxxxxxxx", Task.class));
		
	}
	
	@Test
	public void testFindOneWithSet() {
		
		String stageUuid = "stageUuid";
		
		stageTaskService.delete(stageUuid);
		
		Set<String> taskUuidSet = new HashSet<String>();
		
		taskUuidSet.add("1");
		taskUuidSet.add("2");
		taskUuidSet.add("3");
		
		stageTaskService.save(stageUuid, taskUuidSet);
		
		Set<String> taskUuidSetInRedis = stageTaskService.findOne(stageUuid, new TypeReference<Set<String>>(){}.getType());
		
		assertEquals(3,taskUuidSetInRedis.size());
		
		assertTrue(taskUuidSetInRedis.containsAll(taskUuidSet));
		
		Set<String> taskUuidSetInRedis2 = stageTaskService.findOne(stageUuid);
		
		assertEquals(3,taskUuidSetInRedis2.size());
		
		assertTrue(taskUuidSetInRedis2.containsAll(taskUuidSet));
		
	}

}
