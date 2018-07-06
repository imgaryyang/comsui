

package com.suidifu.hathaway.job.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.test.BaseTestContext;

public class JobServiceTest extends BaseTestContext{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Autowired
	private JobService jobService;
	

	@Test
	@Sql("classpath:test/job/service/job_service.sql")
	public void testGetJobByString() {
		
		String jobUuid = "6dfb7580-ff7e-4b59-8c87-f15bb47af6b0";
		
		Job job = jobService.getJobBy(jobUuid);
		
		String jobName = "jobName1";
		
		assertEquals(jobName,job.getJobName());;
		
		assertNull(jobService.getJobBy("xxxxxxxx"));
		assertNull(jobService.getJobBy("    "));
		assertNull(jobService.getJobBy(""));
		
	}

	@Test
	@Sql("classpath:test/job/service/job_service.sql")
	public void testGetJobByStringStringStringJobType() {
		
		String fstBusinessCode = "fstBusinessCode1";
		String sndBusinessCode = "sndBusinessCode1";
		String trdBusinessCode = "trdBusinessCode";
		
		Job job = jobService.getJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, JobType.Voucher);
	
		assertEquals("6dfb7580-ff7e-4b59-8c87-f15bb47af6b0",job.getUuid());
		
		fstBusinessCode = "fstBusinessCode2";
		sndBusinessCode = "sndBusinessCode2";
		trdBusinessCode = "";
		
		job = jobService.getJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, JobType.Voucher);
	
		assertEquals("154cfd47-e5b6-456e-b422-7a882411ee88",job.getUuid());
		
		fstBusinessCode = "fstBusinessCode3";
		sndBusinessCode = "";
		trdBusinessCode = "";
		
		job = jobService.getJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, JobType.Voucher);
		
		assertNull(job);
		
		fstBusinessCode = "";
		sndBusinessCode = "";
		trdBusinessCode = "xxx";
		
		job = jobService.getJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, JobType.Voucher);
		
		assertNull(job);
		
		fstBusinessCode = "fstBusinessCode3";
		sndBusinessCode = "";
		trdBusinessCode = "";

		job = jobService.getJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, JobType.Other);

		assertNull(job);

		fstBusinessCode = "fstBusinessCode4";
		sndBusinessCode = "";
		trdBusinessCode = "";

		job = jobService.getJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, JobType.Voucher);

		assertNull(job);

	}
	
	@Test
	@Sql("classpath:test/job/service/job_service.sql")
	public void testhasRegeisterJob() {
		
		String fstBusinessCode = "fstBusinessCode1";
		
		fstBusinessCode = "fstBusinessCode4";
		
		assertTrue(jobService.hasRegistedJob(fstBusinessCode, JobType.Voucher));
		
	}
	@Test
	@Sql("classpath:test/job/service/job_service_testloadAllCreateOrProcessingJob.sql")
	public void testloadAllCreateOrProcessingJob(){
		
		List<Job> jobList = jobService.loadAllCreateOrProcessingJob();
		
		assertEquals(2,jobList.size());
		
		boolean _createSuc = false;
		
		boolean _processingSuc = false;
		
		for (Job job : jobList) {
			
			if(job.getExcutingStatus() == ExecutingStatus.CREATE){
				
				if(job.getUuid().equals("6dfb7580-ff7e-4b59-8c87-f15bb47af6b0")){
					
					_createSuc = true;
				}
			}
			if(job.getExcutingStatus() == ExecutingStatus.PROCESSING){
				
				if(job.getUuid().equals("198d194f-d0c4-4845-b881-06f481ddc518")){
					
					_processingSuc = true;
				}
			}
		}
		assertTrue(_createSuc);
		assertTrue(_processingSuc);
	}

	@Test
	@Sql("classpath:test/job/service/job_service_testloadAllCreateOrProcessingJob_jobType.sql")
	public void testloadAllCreateOrProcessingJob_jobType(){
		
		List<Job> jobList = jobService.loadAllCreateOrProcessingJobBy(JobType.Asset_Valuation);
		
		assertEquals(1,jobList.size());
		assertEquals(ExecutingStatus.CREATE,jobList.get(0).getExcutingStatus());
		assertEquals("2017-02-17",jobList.get(0).getSndBusinessCode());
		assertEquals("6dfb7580-ff7e-4b59-8c87-f15bb47af6b0",jobList.get(0).getUuid());
	}
	
	@Test
	@Sql("classpath:test/job/service/job_service_testloadAllCreateOrProcessingJob_jobType_2.sql")
	public void testloadAllCreateOrProcessingJob_jobType_2(){
		
		List<Job> jobList = jobService.loadAllCreateOrProcessingJobBy(JobType.Asset_Valuation);
		
		assertEquals(2,jobList.size());
		assertEquals(ExecutingStatus.PROCESSING,jobList.get(0).getExcutingStatus());
		assertEquals("2017-02-17",jobList.get(0).getSndBusinessCode());
		assertEquals("198d194f-d0c4-4845-b881-06f481ddc518",jobList.get(0).getUuid());

		assertEquals(ExecutingStatus.CREATE,jobList.get(1).getExcutingStatus());
		assertEquals("2017-02-17",jobList.get(1).getSndBusinessCode());
		assertEquals("6dfb7580-ff7e-4b59-8c87-f15bb47af6b0",jobList.get(1).getUuid());
	}
	
	
	@Test
	@Sql("classpath:test/job/service/job_service_testloadAllCreateOrProcessingJob_jobType_sndCode.sql")
	public void testloadAllCreateOrProcessingJob_jobType_sndCode(){
		
		List<Job> jobList = jobService.loadAllCreateOrProcessingJobBy(JobType.Asset_Valuation, "2017-02-17");
		
		assertEquals(1,jobList.size());
		assertEquals(ExecutingStatus.CREATE,jobList.get(0).getExcutingStatus());
		assertEquals("2017-02-17",jobList.get(0).getSndBusinessCode());
		assertEquals("6dfb7580-ff7e-4b59-8c87-f15bb47af6b0",jobList.get(0).getUuid());
	}

	@Test
	@Sql("classpath:test/job/service/job_service_testloadAllCreateOrProcessingJob_jobType_today.sql")
	public void testloadAllCreateOrProcessingJob_jobType_today(){
		
		Date today = DateUtils.asDay(new Date());
		String sndBusinessCode = DateUtils.format(today);
		List<Job> jobList = jobService.loadAllCreateOrProcessingJobBy(JobType.Asset_Valuation, sndBusinessCode);
		
		assertEquals(1,jobList.size());
		assertEquals(ExecutingStatus.CREATE,jobList.get(0).getExcutingStatus());
		assertEquals(sndBusinessCode,jobList.get(0).getSndBusinessCode());
		assertEquals("6dfb7580-ff7e-4b59-8c87-f15bb47af6b0",jobList.get(0).getUuid());
		
		sndBusinessCode = "2015-10-10";
		jobList = jobService.loadAllCreateOrProcessingJobBy(JobType.Asset_Valuation, sndBusinessCode);
		assertEquals(0,jobList.size());
		
	}

}
