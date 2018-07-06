package com.suidifu.hathaway.job;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JobFactoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testIsJobCreated() {
		
		assertFalse(JobFactory.isJobCreated(null));
		
		Job job = new Job();
		
		assertFalse(JobFactory.isJobCreated(job));
		
		job.setExcutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isJobCreated(job));
		
		job.setExcutingStatus(ExecutingStatus.DONE);
		
		assertFalse(JobFactory.isJobCreated(job));
		
		job.setExcutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isJobCreated(job));
		
		job.setExcutingStatus(ExecutingStatus.CREATE);
		
		assertTrue(JobFactory.isJobCreated(job));
		
	}

	@Test
	public void testIsJobDone() {
		
		assertFalse(JobFactory.isJobCreated(null));
		
		Job job = new Job();
		
		assertFalse(JobFactory.isJobDone(job));
		
		job.setExcutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isJobDone(job));
		
		job.setExcutingStatus(ExecutingStatus.DONE);
		
		assertTrue(JobFactory.isJobDone(job));
		
		job.setExcutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isJobDone(job));
		
		job.setExcutingStatus(ExecutingStatus.CREATE);
		
		assertFalse(JobFactory.isJobDone(job));
		
	}

	@Test
	public void testIsJobAbandon() {
		
		assertFalse(JobFactory.isJobAbandon(null));
		
		Job job = new Job();
		
		assertFalse(JobFactory.isJobAbandon(job));
		
		job.setExcutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isJobAbandon(job));
		
		job.setExcutingStatus(ExecutingStatus.DONE);
		
		assertFalse(JobFactory.isJobAbandon(job));
		
		job.setExcutingStatus(ExecutingStatus.ABANDON);
		
		assertTrue(JobFactory.isJobAbandon(job));
		
		job.setExcutingStatus(ExecutingStatus.CREATE);
		
		assertFalse(JobFactory.isJobAbandon(job));
	}

	@Test
	public void testIsStageDone() {
		
		assertFalse(JobFactory.isStageDone(null));
		
		Stage stage = new Stage();
		
		assertFalse(JobFactory.isStageDone(stage));
		
		stage.setExecutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isStageDone(stage));
		
		stage.setExecutingStatus(ExecutingStatus.CREATE);
		
		assertFalse(JobFactory.isStageDone(stage));
		
		stage.setExecutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isStageDone(stage));
		
		stage.setExecutingStatus(ExecutingStatus.DONE);
		
		assertTrue(JobFactory.isStageDone(stage));
		
	}

	@Test
	public void testIsStageAbandon() {
		
		assertFalse(JobFactory.isStageAbandon(null));
		
		Stage stage = new Stage();
		
		assertFalse(JobFactory.isStageAbandon(stage));
		
		stage.setExecutingStatus(ExecutingStatus.ABANDON);
		
		assertTrue(JobFactory.isStageAbandon(stage));
		
		stage.setExecutingStatus(ExecutingStatus.CREATE);
		
		assertFalse(JobFactory.isStageAbandon(stage));
		
		stage.setExecutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isStageAbandon(stage));
		
		stage.setExecutingStatus(ExecutingStatus.DONE);
		
		assertFalse(JobFactory.isStageAbandon(stage));
	}

	@Test
	public void testIsStageProcessing() {
		
		assertFalse(JobFactory.isStageProcessing(null));
		
		Stage stage = new Stage();
		
		assertFalse(JobFactory.isStageProcessing(stage));
		
		stage.setExecutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isStageProcessing(stage));
		
		stage.setExecutingStatus(ExecutingStatus.CREATE);
		
		assertFalse(JobFactory.isStageProcessing(stage));
		
		stage.setExecutingStatus(ExecutingStatus.PROCESSING);
		
		assertTrue(JobFactory.isStageProcessing(stage));
		
		stage.setExecutingStatus(ExecutingStatus.DONE);
		
		assertFalse(JobFactory.isStageProcessing(stage));
	}

	@Test
	public void testIsStageStart() {
		
		assertFalse(JobFactory.isStageStart(null));
		
		Stage stage = new Stage();
		
		assertFalse(JobFactory.isStageStart(stage));
		
		stage.setExecutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isStageStart(stage));
		
		stage.setExecutingStatus(ExecutingStatus.CREATE);
		
		assertTrue(JobFactory.isStageStart(stage));
		
		stage.setExecutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isStageStart(stage));
		
		stage.setExecutingStatus(ExecutingStatus.DONE);
		
		assertFalse(JobFactory.isStageStart(stage));
	}

	@Test
	public void testIsAllStageDoneListOfStage() {
		
		List<Stage> stageList = new ArrayList<>();
		
		assertFalse(JobFactory.isAllStageDone(stageList));
		
		Stage stage1 = new Stage();
		stage1.setExecutingStatus(ExecutingStatus.DONE);
		
		Stage stage2 = new Stage();
		stage2.setExecutingStatus(ExecutingStatus.DONE);
		
		stageList.add(stage1);
		stageList.add(stage2);
		
		assertTrue(JobFactory.isAllStageDone(stageList));
		
		Stage stage3 = new Stage();
		stage3.setExecutingStatus(ExecutingStatus.ABANDON);
		stageList.add(stage3);
		
		assertFalse(JobFactory.isAllStageDone(stageList));
		
	}
	
	

	@Test
	public void testIsAllStageDoneJob() {
		
		Job job = new Job();
		
		assertFalse(JobFactory.isAllStageDone(job));
		
		job.setFstStageUuid(UUID.randomUUID().toString());
		job.setFstStageExcutingStatus(ExecutingStatus.DONE);
		
		job.setSndStageUuid(UUID.randomUUID().toString());
		job.setSndStageExcutingStatus(ExecutingStatus.DONE);
		
		assertTrue(JobFactory.isAllStageDone(job));
		
		job.setTrdStageUuid(UUID.randomUUID().toString());
		job.setTrdStageExcutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isAllStageDone(job));
		
		
	}

	@Test
	public void testCreateNewJob() {
		
		String fstBusinessCode = UUID.randomUUID().toString();
		
		String jobName = "jobName";
		String issuerIdentity = "issuerIdentity";
		String issuerIP = "issuerIP";
		Job job = JobFactory.createNewJob(fstBusinessCode, JobType.Account_Reconciliation_Subrogation, jobName, issuerIdentity, issuerIP);
		assertEquals(fstBusinessCode,job.getFstBusinessCode());
		assertEquals(StringUtils.EMPTY,job.getSndBusinessCode());
		assertEquals(StringUtils.EMPTY,job.getTrdBusinessCode());
		assertEquals(jobName,job.getJobName());
		assertEquals(issuerIdentity,job.getIssuerIdentity());
		assertEquals(issuerIP,job.getIssuerIp());
		assertEquals(ExecutingStatus.CREATE,job.getExcutingStatus());
		assertNull(job.getExcutingResult());
		
	}

	@Test
	public void testCreateNewTask() {
		
		String requestUuid = UUID.randomUUID().toString();
		String stageUuid = UUID.randomUUID().toString();
		String jobUuid = UUID.randomUUID().toString();
		String request = "request";
		String workingQueue = "workingQueue";
		Date expiredTime = new Date();
		Task task = JobFactory.createNewTask(requestUuid, stageUuid, jobUuid, request, workingQueue, expiredTime);
		
		assertEquals(requestUuid,task.getUuid());
		assertEquals(stageUuid,task.getStageUuid());
		assertEquals(jobUuid,task.getJobUUid());
		assertEquals(request,task.getRequest());
		assertEquals(workingQueue,task.getWorkingQueue());
		assertEquals(workingQueue,task.getWorkingQueue());
		assertEquals(ExecutingStatus.CREATE,task.getExecutingStatus());
		
		assertNull(task.getResponse());
		assertNull(task.getExecutingResult());
		
	
	}
	

	@Test
	public void testIsAllTaskDone() {
		
		List<Task> taskList = new ArrayList<Task>();
		
		Task task1 = new Task();
		task1.setExecutingStatus(ExecutingStatus.DONE);
		
		Task task2 = new Task();
		task2.setExecutingStatus(ExecutingStatus.DONE);
		
		taskList.add(task1);
		taskList.add(task2);
		
		assertTrue(JobFactory.isAllTaskDone(taskList));
		
		Task task3 = new Task();
		task3.setExecutingStatus(ExecutingStatus.ABANDON);
		
		taskList.add(task3);
		
		assertFalse(JobFactory.isAllTaskDone(taskList));
	}

	@Test
	public void testIsTaskDone() {
		
		Task task1 = new Task();
		task1.setExecutingStatus(ExecutingStatus.DONE);
		
		assertTrue(JobFactory.isTaskDone(task1));
		
		Task task2 = new Task();
		task2.setExecutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isTaskDone(task2));
		
		
		Task task3 = new Task();
		task2.setExecutingStatus(ExecutingStatus.CREATE);
		
		assertFalse(JobFactory.isTaskDone(task3));
		
		Task task4 = new Task();
		task2.setExecutingStatus(ExecutingStatus.PROCESSING);
		
		assertFalse(JobFactory.isTaskDone(task4));
	}

	@Test
	public void testIsAllTaskSucDone() {
		
		List<Task> taskList = new ArrayList<Task>();
		
		Task task1 = new Task();
		task1.setExecutingStatus(ExecutingStatus.DONE);
		task1.setExecutingResult(ExecutingResult.SUC);
		
		Task task2 = new Task();
		task2.setExecutingStatus(ExecutingStatus.DONE);
		task2.setExecutingResult(ExecutingResult.SUC);
		
		taskList.add(task1);
		taskList.add(task2);
		
		assertTrue(JobFactory.isAllTaskDone(taskList));
		
		Task task3 = new Task();
		task3.setExecutingStatus(ExecutingStatus.ABANDON);
		task3.setExecutingResult(ExecutingResult.FAIL);
		
		taskList.add(task3);
		
		assertFalse(JobFactory.isAllTaskDone(taskList));
	}

	@Test
	public void testIsAllTaskSuc() {
		
		List<Task> taskList = new ArrayList<Task>();
		
		Task task1 = new Task();
		task1.setExecutingStatus(ExecutingStatus.DONE);
		task1.setExecutingResult(ExecutingResult.SUC);
		
		Task task2 = new Task();
		task2.setExecutingStatus(ExecutingStatus.DONE);
		task2.setExecutingResult(ExecutingResult.SUC);
		
		taskList.add(task1);
		taskList.add(task2);
		
		assertTrue(JobFactory.isAllTaskSuc(taskList));
		
		Task task3 = new Task();
		task3.setExecutingStatus(ExecutingStatus.ABANDON);
		task3.setExecutingResult(ExecutingResult.FAIL);
		
		taskList.add(task3);
		
		assertFalse(JobFactory.isAllTaskSuc(taskList));
	}

	@Test
	public void testIsTaskSuc() {
		
		Task task1 = new Task();
		task1.setExecutingStatus(ExecutingStatus.DONE);
		task1.setExecutingResult(ExecutingResult.SUC);
		
		assertTrue(JobFactory.isTaskSuc(task1));
		
		Task task2 = new Task();
		task2.setExecutingStatus(ExecutingStatus.ABANDON);
		
		assertFalse(JobFactory.isTaskSuc(task2));
		
		Task task3 = new Task();
		task3.setExecutingResult(ExecutingResult.FAIL);
		
		assertFalse(JobFactory.isTaskSuc(task3));
		
		
	}

	@Test
	public void testCreateNoDoneStageResult() {
		
		String jobUuid = UUID.randomUUID().toString();
		String stageUuid = UUID.randomUUID().toString();
		int leftRetryTimes = 2;
		boolean hasTaskTimeout = true;
		StageResult stageResult = JobFactory.createNoDoneStageResult(jobUuid, stageUuid, leftRetryTimes, hasTaskTimeout);
		
		assertEquals(jobUuid,stageResult.getJobUuid());
		assertEquals(stageUuid,stageResult.getStageUuid());
		assertEquals(false,stageResult.hasTaskReachRetryTimes());
		assertEquals(0,stageResult.getResult().size());
		assertTrue(stageResult.hasTaskTimeOut());
		assertFalse(stageResult.isAllTaskDone());
		assertFalse(stageResult.isAllTaskSuc());
	}

}
