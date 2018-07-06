/**
 * 
 */
package com.suidifu.hathaway.job.handler;

import java.util.List;

import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.Stage;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.StageResult;


/**
 * @author wukai
 *
 */
public interface JobTaskHandler {
	
	public <T> StageResult<T> collectTaskResult(Job job,Class<T> clazz, Step level);

	public abstract void doStageInCritical(String jobIdentity, Stage stage, List<CriticalMarkedData<?>> data) throws Throwable;
	
	public abstract void doStageRandomly(String jobIdentity, Stage stage,List<Object> data) throws Throwable;
	
	public void reDeploy(Job job, Step level, int seconds);
}
