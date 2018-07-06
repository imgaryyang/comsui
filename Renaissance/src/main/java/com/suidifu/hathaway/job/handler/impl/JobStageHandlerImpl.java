/**
 *
 */
package com.suidifu.hathaway.job.handler.impl;

import com.suidifu.hathaway.job.*;
import com.suidifu.hathaway.job.handler.JobStageHandler;
import com.suidifu.hathaway.job.service.JobService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author wukai
 *
 */
@Component("jobStageHandler")
@PropertySource("classpath:config.properties")
public class JobStageHandlerImpl implements JobStageHandler {

	@Autowired
	private JobService jobService;

	@Value("#{config['issuerIdentity']}")
	private String issuerIdentity;

	@Value("#{config['issuerIP']}")
	private String issuerIP;

	private static Log logger = LogFactory.getLog(JobStageHandlerImpl.class);

	@Override
	public Job registeJob(String fstBusinessCode, JobType jobType) {

		return registeJob(fstBusinessCode, StringUtils.EMPTY, StringUtils.EMPTY, jobType);
	}
	@Override
    public Job registeJob(String fstBusinessCode, String secondBusinessCode, String thirdBusinessCode, JobType jobType) {

		Job job = JobFactory.createNewJob(fstBusinessCode,secondBusinessCode,thirdBusinessCode, jobType, String.format("jobType_%s_fstBusinessCode_%s", jobType.name(),fstBusinessCode), issuerIdentity, issuerIP);

		return job;
	}
	@Override
	public void updateStageDone(Stage stage, List<Task> taskList) {

		if(null == stage){
			return;
		}

		Job job = jobService.getJobBy(stage.getJobUuid());

		if(null == job){
			return;
		}

		ExecutingResult executingResult = calculateStageExecutingResult(taskList);

		job.updateStageDone(stage.getLevel(), executingResult);

		jobService.update(job);
	}

	private ExecutingResult calculateStageExecutingResult(List<Task> taskList) {

		for (Task task : taskList) {

			if(task.getExecutingResult() == ExecutingResult.FAIL){

				return ExecutingResult.FAIL;
			}
		}
		return ExecutingResult.SUC;
	}

	@Override
	public void updateJobDone(Job job) {

		if(null == job){
			return;
		}
		job.updateJobDone();

		jobService.update(job);
	}

	@Override
	public Job registeStage(Job job, Step level, int retryTimes, long timeout, String beanName, String methodName, int chunkSize, Priority priority) {

		if(null == job){
			return null;
		}
		job.registeStage(level, retryTimes, timeout, beanName, methodName, chunkSize, priority.getPriority());

		return job;
	}

	@Override
	public Job registeStage(Job job, Step level, String beanName, String methodName, int chunkSize) {
		return registeStage(job, level, Job.DEFAULT_TRY_TIMES, Job.DEFAULT_TIME_OUT, beanName, methodName, chunkSize, Priority.Low);
	}

	@Override
	public void updateJobProcessing(Job job) {

		if(null == job){
			return;
		}
		job.updateJobProcessing();
		jobService.update(job);
	}

	@Override
	public void updateStageProcessing(Stage stage) {
		// TODO Auto-generated method stub
		if(null == stage){
			return;
		}
		String jobIdentity = stage.getJobUuid();

		Job job = jobService.getJobBy(jobIdentity);

		job.updateStageProcessing(stage.getLevel());

		jobService.update(job);
	}

	//TODO ADD TEST
	@Override
	public Stage findStageBy(Job job,Step step) {
		return job.extractStageBy(step);
	}
	@Override
	public void registeJobStage(Job job) {
		this.jobService.save(job);
	}
	@Override
	public void abandonJob(Job job) {

		if(null == job){
			return;
		}
		job.setExcutingStatus(ExecutingStatus.ABANDON);
		job.setLastModifiedTime(new Date());

		jobService.saveOrUpdate(job);

	}
	@Override
	public Job registeStage(Job job, Step level, String beanName,
			String methodName, int chunkSize, Priority priority) {
		return registeStage(job, level, Job.DEFAULT_TRY_TIMES, Job.DEFAULT_TIME_OUT, beanName, methodName, chunkSize, priority);
	}

}