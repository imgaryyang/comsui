/**
 * 
 */
package com.suidifu.hathaway.job.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobType;

/**
 * @author wukai
 *
 */
public interface JobService extends GenericService<Job> {

	public Job getJobBy(String jobUuid);
	
	public Job getJobBy(String fstBusinessCode,JobType jobType);
	
	public Job getJobBy(String fstBusinessCode,String sndBusinessCode,String trdBusinessCode,JobType jobType);
	
	public Job getAllJobBy(String fstBusinessCode,String sndBusinessCode,String trdBusinessCode,JobType jobType);
	
	public boolean hasRegistedJob(String fstBusinessCode,JobType jobType);
	
	public List<Job> loadAllCreateOrProcessingJob();
	
	public List<Job> loadAllCreateOrProcessingJobBy(JobType jobType); 
	
	public List<Job> loadAllCreateOrProcessingJobBy(JobType jobType, String sndBusinessCode);

	public List<Job> getJobListBy(String fstBusinessCode, String sndBusinessCode, String trdBusinessCode, JobType jobType); 
		
}
