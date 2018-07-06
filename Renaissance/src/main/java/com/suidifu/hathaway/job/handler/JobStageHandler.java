/**
 * 
 */
package com.suidifu.hathaway.job.handler;

import java.util.List;

import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.job.Stage;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.Task;

/**
 * @author wukai
 *
 */
public interface JobStageHandler{
	
	public void registeJobStage(Job job);

	public Job registeJob(String fstBusinessCode,JobType jobType);
	
	public Job registeJob(String fstBusinessCode,String secondBusinessCode,String thridBusinessCode,JobType jobType);
	
	public Job registeStage(Job job,Step level,int retryTimes,long timeout, String beanName, String methodName, int chunkSize, Priority priority);
	
	public Job registeStage(Job job,Step level, String beanName, String methodName, int chunkSize);
	
	public Job registeStage(Job job,Step level, String beanName, String methodName, int chunkSize,Priority priority);
	
	public void updateStageDone(Stage stage, List<Task> taskList);
	
	public void updateJobDone(Job job);

	public void updateJobProcessing(Job job);
	
	public void updateStageProcessing(Stage stage);

	public abstract Stage findStageBy(Job job, Step step);
	
	public void abandonJob(Job job);
}
