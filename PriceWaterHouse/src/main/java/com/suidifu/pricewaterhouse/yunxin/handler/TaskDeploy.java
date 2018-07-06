/**
 * 
 */
package com.suidifu.pricewaterhouse.yunxin.handler;

import com.suidifu.hathaway.job.*;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.hathaway.job.handler.JobStageHandler;
import com.suidifu.hathaway.job.handler.JobTaskHandler;
import com.suidifu.hathaway.job.service.JobService;
import com.suidifu.hathaway.task.handler.StageTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.impl.JobContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author wukai
 *
 */
public abstract class TaskDeploy {
	
	@Autowired
	protected JobStageHandler jobHandler;
	
	@Autowired
	protected JobService jobService;
	
	@Autowired
	protected StageTaskHandler taskHandler;
	
	@Autowired
	protected JobTaskHandler jobTaskHandler;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public  void pushDataToCritialSecion(Step step,String jobIdentity, List<CriticalMarkedData<?>> rawData) throws Throwable{
		
		Job job = jobService.getJobBy(jobIdentity);
		
		Stage stage= locateStage(step, jobIdentity, job);
		
		if(stage==null) return;
		
		jobTaskHandler.doStageInCritical(jobIdentity, stage, (List)rawData);
		
		flushStageStatusToJob(jobIdentity, job, stage);
	}
	
	
public <T>  StageResult<T> performStageInRandom(Step step,JobContext<?> context,Class<T> resultClazz) throws Throwable{
		long startTime = System.currentTimeMillis();
		String jobIdentity=context.getJob().getUuid();
		
		List<Object> data=context.getRawDataTable().get(step);
		this.pushDataRandomly(step,jobIdentity,data);
		
		StageResult<T>  taskResult = jobTaskHandler.collectTaskResult(context.getJob(), resultClazz,step);
		long endTime = System.currentTimeMillis();
		logger.info("collectTaskResult:step "+ step.getKey()+" result["+taskResult.isAllTaskSucDone()+"],use["+(endTime-startTime)+"]ms");
		
		return taskResult;
		
	}
	
	public <T>  StageResult<T> performStageInCritical(Step step,JobContext<?> context,Map<String,String> cirtialMarks,CriticalSecionProjector projector,Class<T> resultClazz) throws Throwable{
		List<Object> data=context.getRawDataTable().get(step);
		
		List<CriticalMarkedData<?>> critialData=projector.projectDataToCriticalSecion(data, cirtialMarks);
		
		return performStageInCriticalSection(step,context,critialData,resultClazz);
				
	}

	public <T> StageResult<T> performStageInCritical(Step step, JobContext<?> context, CriticalSecionProjector projector, Class<T> resultClazz) throws Throwable {
		long startTime = System.currentTimeMillis();
		List<Object> data = context.getRawDataTable().getOrDefault(step, Collections.emptyList());

		List<CriticalMarkedData<?>> critialData = projector.projectDataToCriticalSecion(data);

		StageResult<T> stageResult =  performStageInCriticalSection(step, context, critialData, resultClazz);
		long endTime = System.currentTimeMillis();
		logger.info("performStageInCritical:step "+ step.getKey()+",use["+(endTime-startTime)+"]ms");
		return stageResult;
	}
	
	
	public <T>  StageResult<T> performStageInCriticalSection(Step step,JobContext<?> context,List<CriticalMarkedData<?>> critialData,Class<T> resultClazz) throws Throwable{
		
		long startTime = System.currentTimeMillis();
		
		 pushDataToCritialSecion( step,context.getJob().getUuid(), critialData);
		 
		StageResult<T> taskResult = jobTaskHandler.collectTaskResult(context.getJob(), resultClazz,step);
				
		long endTime = System.currentTimeMillis();
		
		logger.info("performStageInCriticalSection: step "+ step.getKey()+" result["+taskResult.isAllTaskSucDone()+"],use["+(endTime-startTime)+"]ms");
		
		return taskResult;
	}
	
	
	
	public  void pushDataToCritialSecion(Step step,JobContext<?> context,CriticalSecionProjector projector) throws Throwable{
		
		String jobIdentity=context.getJob().getUuid();
		
		List<Object> data=context.getRawDataTable().get(step);
		
		List<CriticalMarkedData<?>> syncRawDataSnd=projector.projectDataToCriticalSecion(data);
		Job job = jobService.getJobBy(jobIdentity);
		
		Stage stage= locateStage(step, jobIdentity, job);
		
		if(stage==null) return;
		
		jobTaskHandler.doStageInCritical(jobIdentity, stage, syncRawDataSnd);
		
		flushStageStatusToJob(jobIdentity, job, stage);
	}



	private void flushStageStatusToJob(String jobIdentity, Job job, Stage stage) {
		logger.info("end to do stage with stage uuid["+stage.getUuid()+"],with jobIdentity["+jobIdentity+"]");
		
		if(JobFactory.isAllStageDone(job)){
			
			logger.info("all task is done,begin to update job with jobIdentity["+jobIdentity+"]");
			
			//TODO EXPIRE
//			taskHandler.purgeAllTask(job.extractStageList());
			
			jobHandler.updateJobDone(job);
		}
	}



	private Stage  locateStage(Step step, String jobIdentity, Job job) {
		
		if(JobFactory.isJobDone(job)){
			
			logger.info("the job is done with jobIdentity["+jobIdentity+"]");
			
			return null;
		}
		if(JobFactory.isJobAbandon(job)){
			
			logger.info("the job is abandon with jobIdentity["+jobIdentity+"]");
			
			return null;
		}
		
		if(JobFactory.isJobCreated(job)){
			
			jobHandler.updateJobProcessing(job);
		}
		
		Stage stage = jobHandler.findStageBy(job,step);
		
		logger.info("begin to do stage with stage uuid["+stage.getUuid()+"]");
		return stage;
	}

	
	
	public  void pushDataRandomly(Step step,String jobIdentity, List<Object> rawData) throws Throwable{
		
		Job job = jobService.getJobBy(jobIdentity);
		
		Stage stage = locateStage(step, jobIdentity, job);
		
		if(stage==null) return;
		
		
		
		jobTaskHandler.doStageRandomly(jobIdentity, stage, (List)rawData);
		
		flushStageStatusToJob(jobIdentity, job, stage);
	}

	public boolean collectResult(StageResult<Boolean> results,boolean initial) {


		while (results.hasMoreElements()){

			Boolean result = results.nextElement();

			if(result == null){

				result = false;
			}
			initial = initial && result;

			if(initial == false){
				return false;
			}

		}
		return initial;
	}
	
	

	

}
