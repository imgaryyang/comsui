package com.suidifu.hathaway.job.handler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mockit.Mock;
import mockit.MockUp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.hathaway.job.ExecutingResult;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.job.Stage;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.job.service.JobService;
import com.suidifu.hathaway.test.BaseTestContext;

public class JobStageHandlerTest extends BaseTestContext{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	private JobStageHandler jobStageHandler;
	
	@Autowired
	private JobService jobService;
	
	@Test
	@Sql("classpath:test/job/service/job_service.sql")
	public void testRegisteJobStage() {
		
		Job job = new Job();
		
		String uuid = UUID.randomUUID().toString();
		
		job.setUuid(uuid);
		
		jobStageHandler.registeJobStage(job);
		
		Job jobInDb = jobService.getJobBy(uuid);
		
		assertEquals(uuid,jobInDb.getUuid());
		
	}
	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testRegisteStageJobStepStringStringInt() {
		
		new MockUp<System>(){
			
			@Mock public long currentTimeMillis(){return 0;}
		};
	
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		
		Job job = jobService.getJobBy(jobUuid);
		
		int retryTimes = 2333;
		long timeout = 3600*24*1000L;
		String beanName = "beanNamexxx";
		String methodName = "methodNamexxxx";
		int chunkSize = 300;
		Priority priority = Priority.High;
		job = jobStageHandler.registeStage(job, Step.FST, retryTimes, timeout, beanName, methodName, chunkSize, priority);
		
		Stage frtStage = job.extractStageBy(Step.FST);
		
		assertEquals(beanName,frtStage.getBeanName());
		
		assertEquals(chunkSize,frtStage.getChunkSize());
		
		
		assertEquals("now date : "+DateUtils.format(new Date()),"1970-01-02",DateUtils.format(frtStage.getCurrentTaskExpiredTime()));
		assertEquals(ExecutingResult.SUC,frtStage.getExecutingResult());
		assertEquals(ExecutingStatus.CREATE,frtStage.getExecutingStatus());
		assertEquals(jobUuid,frtStage.getJobUuid());
		assertEquals(retryTimes,frtStage.getLeftRetryTimes());
		assertEquals(Step.FST,frtStage.getLevel());
		assertEquals(methodName,frtStage.getMethodName());
		assertEquals(priority.getPriority(),frtStage.getPriority());
	
	}

	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testUpdateStageDone() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		
		Stage frsStage = job.extractStageBy(Step.FST);
		
		List<Task> taskList = new ArrayList<Task>();
		
		Task task1 = new Task();
		task1.setExecutingResult(ExecutingResult.SUC);
		
		Task task2 = new Task();
		task2.setExecutingResult(ExecutingResult.FAIL);
		
		taskList.add(task1);
		taskList.add(task2);
		
		jobStageHandler.updateStageDone(frsStage, taskList);
		
		job = jobService.getJobBy(jobUuid);
		
		frsStage = job.extractStageBy(Step.FST);
		
		assertEquals(ExecutingResult.FAIL,frsStage.getExecutingResult());
		assertEquals(ExecutingStatus.DONE,frsStage.getExecutingStatus());
	}
	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testUpdateStageDone2() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		
		Stage frsStage = job.extractStageBy(Step.FST);
		
		List<Task> taskList = new ArrayList<Task>();
		
		Task task1 = new Task();
		task1.setExecutingResult(ExecutingResult.SUC);
		
		Task task2 = new Task();
		task2.setExecutingResult(ExecutingResult.SUC);
		
		taskList.add(task1);
		taskList.add(task2);
		
		jobStageHandler.updateStageDone(frsStage, taskList);
		
		job = jobService.getJobBy(jobUuid);
		
		frsStage = job.extractStageBy(Step.FST);
		
		assertEquals(ExecutingResult.SUC,frsStage.getExecutingResult());
		assertEquals(ExecutingStatus.DONE,frsStage.getExecutingStatus());
	
		
	}

	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testUpdateJobDone() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		
		assertEquals(ExecutingStatus.DONE,job.getExcutingStatus());
		
		jobStageHandler.updateJobDone(job);
		
		job = jobService.getJobBy(jobUuid);
		
		assertEquals(ExecutingStatus.DONE,job.getExcutingStatus());
	
	}

	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testUpdateJobProcessing() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		
		assertEquals(ExecutingStatus.DONE,job.getExcutingStatus());
		
		jobStageHandler.updateJobProcessing(job);
		
		job = jobService.getJobBy(jobUuid);
		
		assertEquals(ExecutingStatus.PROCESSING,job.getExcutingStatus());
	
	}

	/**
	 * 
	 */
	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testUpdateStageProcessing() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		
		Stage frsStage = job.extractStageBy(Step.FST);
		//TODO 需要防止状态回转，比如有ExecutingStatus.DONE->ExecutingStatus.PROCESSING
		assertEquals(ExecutingStatus.DONE,frsStage.getExecutingStatus());
		
		jobStageHandler.updateStageProcessing(frsStage);
		
		job = jobService.getJobBy(jobUuid);
		
		frsStage = job.extractStageBy(Step.FST);
		
		assertEquals(ExecutingStatus.PROCESSING,frsStage.getExecutingStatus());
		
	}
	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testAbandonJob() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		assertEquals(ExecutingStatus.DONE,job.getExcutingStatus());
		
		jobStageHandler.abandonJob(null);
		
		Job job2 = jobService.getJobBy(jobUuid);
		assertEquals(ExecutingStatus.DONE,job2.getExcutingStatus());
		
		jobStageHandler.abandonJob(job);
		
		assertEquals(ExecutingStatus.ABANDON,job.getExcutingStatus());
		
	}

	@Test
	@Sql("classpath:test/job/stage/handler/job_service.sql")
	public void testFindStageBy() {
		
		String jobUuid = "2723c2d8-5602-4ecb-98d8-05de6b9e4656";
		Job job = jobService.getJobBy(jobUuid);
		assertNull(jobStageHandler.findStageBy(job, Step.FIFTH));
		
		Stage fstStage = jobStageHandler.findStageBy(job, Step.FST);
		
		assertEquals("e260c0dc-a709-4e01-82f7-41925d8cf09d",fstStage.getUuid());
		assertEquals("dstJobSourceDocumentReconciliation",fstStage.getBeanName());
		assertEquals(20,fstStage.getChunkSize());
		assertEquals(DateUtils.parseDate("2017-01-20 02:05:03", "yyyy-MM-dd HH:mm:ss"),fstStage.getCreateTime());
		assertEquals(DateUtils.parseDate("2017-01-22 02:05:03", "yyyy-MM-dd HH:mm:ss"),fstStage.getCurrentTaskExpiredTime());
		assertEquals(ExecutingResult.SUC,fstStage.getExecutingResult());
		assertEquals(ExecutingStatus.DONE,fstStage.getExecutingStatus());
		assertEquals(jobUuid,fstStage.getJobUuid());
		assertEquals(DateUtils.parseDate("2017-01-20 02:09:27", "yyyy-MM-dd HH:mm:ss"),fstStage.getLastModifiedTime());
		assertEquals(3,fstStage.getLeftRetryTimes());
		assertEquals(Step.FST,fstStage.getLevel());
		assertEquals("criticalMarker",fstStage.getMethodName());
		assertEquals(2,fstStage.getPriority());
		
		Stage sndStage = jobStageHandler.findStageBy(job, Step.SND);
		
		assertEquals("5c05ffb7-0738-4723-91f8-98bfabf620e3",sndStage.getUuid());
		assertEquals("dstJobSourceDocumentReconciliation",sndStage.getBeanName());
		assertEquals(20,sndStage.getChunkSize());
		assertEquals(DateUtils.parseDate("2017-01-20 02:09:27", "yyyy-MM-dd HH:mm:ss"),sndStage.getCreateTime());
		assertEquals(DateUtils.parseDate("2017-01-22 02:05:03", "yyyy-MM-dd HH:mm:ss"),sndStage.getCurrentTaskExpiredTime());
		assertEquals(ExecutingResult.SUC,sndStage.getExecutingResult());
		assertEquals(ExecutingStatus.DONE,sndStage.getExecutingStatus());
		assertEquals(jobUuid,sndStage.getJobUuid());
		assertEquals(DateUtils.parseDate("2017-01-20 02:11:22", "yyyy-MM-dd HH:mm:ss"),sndStage.getLastModifiedTime());
		assertEquals(3,sndStage.getLeftRetryTimes());
		assertEquals(Step.SND,sndStage.getLevel());
		assertEquals("validateSourceDocumentDetailList",sndStage.getMethodName());
		assertEquals(2,sndStage.getPriority());
		
		Stage trdStage = jobStageHandler.findStageBy(job, Step.TRD);
		
		assertEquals("b1aae825-b77e-483e-ac64-d82baa967ffa",trdStage.getUuid());
		assertEquals("dstJobSourceDocumentReconciliation",trdStage.getBeanName());
		assertEquals(20,trdStage.getChunkSize());
		assertEquals(DateUtils.parseDate("2017-01-20 02:11:22", "yyyy-MM-dd HH:mm:ss"),trdStage.getCreateTime());
		assertEquals(DateUtils.parseDate("2017-01-22 02:05:03", "yyyy-MM-dd HH:mm:ss"),trdStage.getCurrentTaskExpiredTime());
		assertEquals(ExecutingResult.SUC,trdStage.getExecutingResult());
		assertEquals(ExecutingStatus.DONE,trdStage.getExecutingStatus());
		assertEquals(jobUuid,trdStage.getJobUuid());
		assertEquals(DateUtils.parseDate("2017-01-20 02:12:30", "yyyy-MM-dd HH:mm:ss"),trdStage.getLastModifiedTime());
		assertEquals(3,trdStage.getLeftRetryTimes());
		assertEquals(Step.TRD,trdStage.getLevel());
		assertEquals("fetch_virtual_account_and_business_payment_voucher_transfer",trdStage.getMethodName());
		assertEquals(2,trdStage.getPriority());
		
		
		Stage fourthStage = jobStageHandler.findStageBy(job, Step.FOURTH);
		
		assertEquals("9a4dbee2-d019-447d-862a-049f7a4b42c7",fourthStage.getUuid());
		assertEquals("dstJobSourceDocumentReconciliation",fourthStage.getBeanName());
		assertEquals(20,fourthStage.getChunkSize());
		assertEquals(DateUtils.parseDate("2017-01-20 02:12:30", "yyyy-MM-dd HH:mm:ss"),fourthStage.getCreateTime());
		assertEquals(DateUtils.parseDate("2017-01-22 02:05:03", "yyyy-MM-dd HH:mm:ss"),fourthStage.getCurrentTaskExpiredTime());
		assertEquals(ExecutingResult.SUC,fourthStage.getExecutingResult());
		assertEquals(ExecutingStatus.DONE,fourthStage.getExecutingStatus());
		assertEquals(jobUuid,fourthStage.getJobUuid());
		assertEquals(DateUtils.parseDate("2017-01-20 02:12:31", "yyyy-MM-dd HH:mm:ss"),fourthStage.getLastModifiedTime());
		assertEquals(3,fourthStage.getLeftRetryTimes());
		assertEquals(Step.FOURTH,fourthStage.getLevel());
		assertEquals("source_document_recover_details",fourthStage.getMethodName());
		assertEquals(2,fourthStage.getPriority());
		
	}

}
