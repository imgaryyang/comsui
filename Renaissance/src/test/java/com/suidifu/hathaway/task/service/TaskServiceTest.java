package com.suidifu.hathaway.task.service;

import java.util.Date;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.hathaway.job.ExecutingResult;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.test.BaseTestContext;

public class TaskServiceTest extends BaseTestContext{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Autowired
	private TaskService taskService;
	
	
	@Test
	public void testWriteTaskToRedis() throws Exception {
		
		String requestUuid = UUID.randomUUID().toString();
		
		String stageUuid = UUID.randomUUID().toString();
		 
		String jobUUid= UUID.randomUUID().toString(); 
		String request = "request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		ExecutingResult executingResult = ExecutingResult.FAIL;
		Date startTime = new Date();
		Date endTime= DateUtils.addDays(startTime, 1);
		String workingQueue = "1";
		Date expiredTime = DateUtils.addDays(startTime, 1);
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);

		long start_time = System.currentTimeMillis();
		
		taskService.save(UUID.randomUUID().toString(), task);
		
		System.out.println("consume time["+(System.currentTimeMillis()-start_time)+"]");
	}


}
