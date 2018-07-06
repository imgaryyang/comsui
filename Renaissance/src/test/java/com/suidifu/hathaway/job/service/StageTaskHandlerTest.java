package com.suidifu.hathaway.job.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.job.ExecutingResult;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.task.handler.StageTaskHandler;
import com.suidifu.hathaway.task.service.TaskService;
import com.suidifu.hathaway.test.BaseTestContext;

public class StageTaskHandlerTest extends BaseTestContext{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Autowired
	private StageTaskHandler stageTaskHandler;
	
	@Autowired
	private TaskService taskService;

	@Test
	public void testSaveTask() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		Task taskInRedis = stageTaskHandler.getOneTaskBy(requestUuid);
		
		assertEquals(requestUuid,taskInRedis.getUuid());
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		assertEquals(request,taskInRedis.getRequest());
		assertEquals(response,taskInRedis.getResponse());
		assertEquals(executingStatus,taskInRedis.getExecutingStatus());
		assertEquals(executingResult,taskInRedis.getExecutingResult());
		assertEquals(workingQueue,taskInRedis.getWorkingQueue());
	}

	@Test
	public void testSaveAllTask() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		
		stageTaskHandler.saveAllTask(taskList);
		
		Task taskInRedis = stageTaskHandler.getOneTaskBy(requestUuid);
		
		assertEquals(requestUuid,taskInRedis.getUuid());
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		assertEquals(request,taskInRedis.getRequest());
		assertEquals(response,taskInRedis.getResponse());
		assertEquals(executingStatus,taskInRedis.getExecutingStatus());
		assertEquals(executingResult,taskInRedis.getExecutingResult());
		assertEquals(workingQueue,taskInRedis.getWorkingQueue());
	}
	@Test
	public void testUpdateTaskSuc() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		Object exceptionObject = null;
		
		BigDecimal responseNew = new BigDecimal("111");
		
		Object resultObject = JsonUtils.toJsonString(responseNew);
		
		stageTaskHandler.updateTask(task, exceptionObject, resultObject);
		
		Task taskInRedis = stageTaskHandler.getOneTaskBy(requestUuid);
		
		assertEquals(ExecutingStatus.DONE,taskInRedis.getExecutingStatus());
		assertEquals(ExecutingResult.SUC,taskInRedis.getExecutingResult());
		
		assertEquals(responseNew,taskInRedis.parseResponse(BigDecimal.class));
		
		//返回为空的结果
		
		stageTaskHandler.updateTask(task, exceptionObject, null);
		
		taskInRedis = stageTaskHandler.getOneTaskBy(requestUuid);
		
		assertEquals(ExecutingStatus.DONE,taskInRedis.getExecutingStatus());
		assertEquals(ExecutingResult.SUC,taskInRedis.getExecutingResult());
		
		assertNull(taskInRedis.parseResponse(BigDecimal.class));
		
	}
	@Test
	public void testUpdateTaskFailure() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		Exception exceptionObject = new RuntimeException("runtime exception");
		
		Object resultObject = JsonUtils.toJsonString(exceptionObject);
		
		stageTaskHandler.updateTask(task, exceptionObject, resultObject);
		
		Task taskInRedis = stageTaskHandler.getOneTaskBy(requestUuid);
		
		assertEquals(ExecutingStatus.DONE,taskInRedis.getExecutingStatus());
		assertEquals(ExecutingResult.FAIL,taskInRedis.getExecutingResult());
		
		assertEquals(exceptionObject.getMessage(),((RuntimeException)taskInRedis.parseResponse(RuntimeException.class)).getMessage());
		
	}
	@Test
	public void testUpdateTaskWithValidInput() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		stageTaskHandler.updateTask(null, null, null);
		
		Task task = new Task();
		
		task.setUuid("xxxxx");
		
		stageTaskHandler.updateTask(task, null, null);
	}

	@Test
	public void testGetOneTaskBy() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		Task taskInRedis = stageTaskHandler.getOneTaskBy(requestUuid);
		
		assertEquals(requestUuid,taskInRedis.getUuid());
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		assertEquals(request,taskInRedis.getRequest());
		assertEquals(response,taskInRedis.getResponse());
		assertEquals(executingStatus,taskInRedis.getExecutingStatus());
		assertEquals(executingResult,taskInRedis.getExecutingResult());
		assertEquals(workingQueue,taskInRedis.getWorkingQueue());
	
	}

	@Test
	public void testSetTaskSetToStage() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		
		stageTaskHandler.saveAllTask(taskList);
		
		stageTaskHandler.setTaskSetToStage(stageUuid, taskList);
		
		List<Task> taskListInRedis = stageTaskHandler.getAllTaskListBy(stageUuid);
		
		assertEquals(1,taskListInRedis.size());
		
		Task taskInRedis = taskListInRedis.get(0);
		
		assertEquals(requestUuid,taskInRedis.getUuid());
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		assertEquals(request,taskInRedis.getRequest());
		assertEquals(response,taskInRedis.getResponse());
		assertEquals(executingStatus,taskInRedis.getExecutingStatus());
		assertEquals(executingResult,taskInRedis.getExecutingResult());
		assertEquals(workingQueue,taskInRedis.getWorkingQueue());
	
		
	}

	@Test
	public void testSetTaskSetToStageStringListOfTask() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		
		stageTaskHandler.saveAllTask(taskList);
		
		stageTaskHandler.setTaskSetToStage(stageUuid, taskList.stream().map(t->{return t.getUuid();}).collect(Collectors.toSet()), expiredTime);
		
		List<Task> taskListInRedis = stageTaskHandler.getAllTaskListBy(stageUuid);
		
		assertEquals(1,taskListInRedis.size());
		
		Task taskInRedis = taskListInRedis.get(0);
		
		assertEquals(requestUuid,taskInRedis.getUuid());
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		assertEquals(request,taskInRedis.getRequest());
		assertEquals(response,taskInRedis.getResponse());
		assertEquals(executingStatus,taskInRedis.getExecutingStatus());
		assertEquals(executingResult,taskInRedis.getExecutingResult());
		assertEquals(workingQueue,taskInRedis.getWorkingQueue());
	}

	@Test
	public void testSetTaskToStage() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		stageTaskHandler.setTaskToStage(stageUuid, task.getUuid(), expiredTime);
		
		List<Task> taskListInRedis = stageTaskHandler.getAllTaskListBy(stageUuid);
		
		assertEquals(1,taskListInRedis.size());
		
		Task taskInRedis = taskListInRedis.get(0);
		
		assertEquals(requestUuid,taskInRedis.getUuid());
		assertEquals(jobUUid,taskInRedis.getJobUUid());
		assertEquals(request,taskInRedis.getRequest());
		assertEquals(response,taskInRedis.getResponse());
		assertEquals(executingStatus,taskInRedis.getExecutingStatus());
		assertEquals(executingResult,taskInRedis.getExecutingResult());
		assertEquals(workingQueue,taskInRedis.getWorkingQueue());
	}

	@Test
	public void testGetAllTaskUuidListBy() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		stageTaskHandler.setTaskToStage(stageUuid, task.getUuid(), expiredTime);
		
		Set<String> taskListInRedis = stageTaskHandler.getAllTaskUuidListBy(stageUuid);
		
		assertEquals(1,taskListInRedis.size());
		assertEquals(requestUuid,taskListInRedis.iterator().next());
		
	}

	@Test
	public void testGetAllTaskListBy() {
		
		String requestUuid = "requestUuid";
		
		taskService.delete(requestUuid);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.ABANDON;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		stageTaskHandler.saveTask(task);
		
		stageTaskHandler.setTaskToStage(stageUuid, task.getUuid(), expiredTime);
		
		List<Task> taskListInRedis = stageTaskHandler.getAllTaskListBy(stageUuid);
		
		assertEquals(1,taskListInRedis.size());
		
	}

	@Test
	public void testIsAllTaskDone() {
		
		String requestUuid = "requestUuid";
		String requestUuid2="requestUuid2";
		
		taskService.delete(requestUuid);
		taskService.delete(requestUuid2);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.DONE;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
	
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		Task task2 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, executingStatus, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		taskList.add(task2);
		
		stageTaskHandler.saveAllTask(taskList);
		
		
		stageTaskHandler.setTaskSetToStage(stageUuid, taskList.stream().map(t->{return t.getUuid();}).collect(Collectors.toSet()), expiredTime);
		
		assertTrue(stageTaskHandler.isAllTaskDone(stageUuid));

	}
	@Test
	public void testIsAllTaskDone_Case2() {
		
		String requestUuid = "requestUuid";
		String requestUuid2="requestUuid2";
		String requestUuid3="requestUuid3";
		String requestUuid4="requestUuid4";
		
		taskService.delete(requestUuid);
		taskService.delete(requestUuid2);
		taskService.delete(requestUuid3);
		taskService.delete(requestUuid4);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.DONE;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.DONE, startTime, endTime, workingQueue, expiredTime);
		
		Task task2 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.CREATE, startTime, endTime, workingQueue, expiredTime);
		
		Task task3 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.ABANDON, startTime, endTime, workingQueue, expiredTime);
		
		Task task4 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.PROCESSING, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		taskList.add(task2);
		taskList.add(task3);
		taskList.add(task4);
		
		stageTaskHandler.saveAllTask(taskList);
		
		stageTaskHandler.setTaskSetToStage(stageUuid, taskList.stream().map(t->{return t.getUuid();}).collect(Collectors.toSet()), expiredTime);
		
		assertFalse(stageTaskHandler.isAllTaskDone(stageUuid));

	}
	@Test
	public void testPurgeAllTaskString() {
		
		String requestUuid = "requestUuid";
		String requestUuid2="requestUuid2";
		String requestUuid3="requestUuid3";
		String requestUuid4="requestUuid4";
		
		taskService.delete(requestUuid);
		taskService.delete(requestUuid2);
		taskService.delete(requestUuid3);
		taskService.delete(requestUuid4);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.DONE;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.DONE, startTime, endTime, workingQueue, expiredTime);
		
		Task task2 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.CREATE, startTime, endTime, workingQueue, expiredTime);
		
		Task task3 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.ABANDON, startTime, endTime, workingQueue, expiredTime);
		
		Task task4 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.PROCESSING, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		taskList.add(task2);
		taskList.add(task3);
		taskList.add(task4);
		
		stageTaskHandler.saveAllTask(taskList);
		
		stageTaskHandler.setTaskSetToStage(stageUuid, taskList.stream().map(t->{return t.getUuid();}).collect(Collectors.toSet()), expiredTime);
		
		stageTaskHandler.purgeAllTask(stageUuid);
		
		assertNull(stageTaskHandler.getOneTaskBy(requestUuid));
		assertNull(stageTaskHandler.getOneTaskBy(requestUuid2));
		assertNull(stageTaskHandler.getOneTaskBy(requestUuid3));
		assertNull(stageTaskHandler.getOneTaskBy(requestUuid4));
		
	}
	
	

	@Test
	public void testPurgeAllTaskListOfStage() {
		
		String requestUuid = "requestUuid";
		String requestUuid2="requestUuid2";
		String requestUuid3="requestUuid3";
		String requestUuid4="requestUuid4";
		
		taskService.delete(requestUuid);
		taskService.delete(requestUuid2);
		taskService.delete(requestUuid3);
		taskService.delete(requestUuid4);
		
		String stageUuid = "stageUuid";
		String jobUUid = "jobUUid";
		String request ="request";
		String response = "response";
		ExecutingStatus executingStatus = ExecutingStatus.DONE;
		Date startTime = DateUtils.parseDate("2015-10-11", "yyyy-MM-dd");
		Date endTime = DateUtils.parseDate("2035-10-11", "yyyy-MM-dd");
		String workingQueue = "workingQueue";
		ExecutingResult executingResult = ExecutingResult.SUC;
		
		Date expiredTime = DateUtils.parseDate("2999-01-01", "yyyy-MM-dd");
		
		Task task = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.DONE, startTime, endTime, workingQueue, expiredTime);
		
		Task task2 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.CREATE, startTime, endTime, workingQueue, expiredTime);
		
		Task task3 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.ABANDON, startTime, endTime, workingQueue, expiredTime);
		
		Task task4 = new Task(requestUuid, stageUuid, jobUUid, request, response, executingResult, ExecutingStatus.PROCESSING, startTime, endTime, workingQueue, expiredTime);
		
		List<Task> taskList = new ArrayList<Task>();
		
		taskList.add(task);
		taskList.add(task2);
		taskList.add(task3);
		taskList.add(task4);
		
		stageTaskHandler.saveAllTask(taskList);
		
		stageTaskHandler.setTaskSetToStage(stageUuid, taskList.stream().map(t->{return t.getUuid();}).collect(Collectors.toSet()), expiredTime);
		
		stageTaskHandler.purgeAllTask(stageUuid);
		
		assertEquals(0,stageTaskHandler.getAllTaskListBy(stageUuid).size());
		
		
	}
	
	@Test
	public void testGetTaskFromRedis() throws Exception {
		
		List<Task> taskList = stageTaskHandler.getAllTaskListBy("5924cfc1-dd82-405a-8692-811fbb0cb45f");
		
		for (Task task : taskList) {
			
			Boolean result = task.parseResponse(Boolean.class);
			
			if(null == result || !result){
				
				System.out.println("taskUuid["+task.getUuid()+"]");
			}
			
		}
		
	}
}
