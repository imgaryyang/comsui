/**
 *
 */
package com.suidifu.hathaway.job.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobFactory;
import com.suidifu.hathaway.job.Stage;
import com.suidifu.hathaway.job.StageResult;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.hathaway.job.handler.JobStageHandler;
import com.suidifu.hathaway.job.handler.JobTaskHandler;
import com.suidifu.hathaway.job.service.JobService;
import com.suidifu.hathaway.mq.sender.MessageSender;
import com.suidifu.hathaway.task.handler.StageTaskHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wukai
 *
 */
public class JobTaskHandlerImpl implements JobTaskHandler {

	@Autowired
	private JobStageHandler jobHandler;

	@Autowired
	private StageTaskHandler taskHandler;

	@Autowired
	private JobService jobService;

	@Autowired
	private MessageSender messageSender;

	private static Log logger = LogFactory.getLog(JobTaskHandlerImpl.class);

	private List<Task> criticallyTaskPlanning(String jobIdentity, Stage stage, List<CriticalMarkedData<?>> data) throws Throwable {

		Collection<Task> taskList = new ConcurrentLinkedDeque<Task>();

		if(stage.getChunkSize()>0)
		{
			//Map<String,Collection<CriticalMarkedData<?>>> dataRegroupByQueue=new ConcurrentHashMap<>();
			Map<String,List<CriticalMarkedData<?>>> dataRegroupByQueue=new HashMap<String,List<CriticalMarkedData<?>>>();
			Collection<String> queueIndexes=messageSender.selectAllQueueIndexes();

			for(String index:queueIndexes)
			{
				//dataRegroupByQueue.put(index, new ConcurrentLinkedDeque<>());
				dataRegroupByQueue.put(index, new ArrayList<CriticalMarkedData<?>>());
			}


			data.stream().forEach(workingParams->
			{
				//TODO THROW EXCEPTION
				if(workingParams==null)
				{
					logger.error("#synchronizedTaskPlanning#jobIdentity["+jobIdentity+"],workingParams"+workingParams+"is null");
				}
				if(workingParams.getCritialMark()==null){
					logger.error("#synchronizedTaskPlanning#jobIdentity["+jobIdentity+"],workingParams.getCritialMark is null"+JsonUtils.toJsonString(workingParams.getTaskParams()));
				}
				String queueIndex=this.messageSender.selectQueue(workingParams.getCritialMark());
				List<CriticalMarkedData<?>> queueList=dataRegroupByQueue.get(queueIndex);

				queueList.add(workingParams);
			}
			);

			List<List<?>> regroupedBufferedData=new ArrayList<List<?>>();

			dataRegroupByQueue.keySet().stream().forEach(queueIndex->
			{
				List<CriticalMarkedData<?>> dataInThisQueue=dataRegroupByQueue.get(queueIndex);
				List<List<?>> bufferedDataInThisQueue=splitDataList(dataInThisQueue,stage.getChunkSize());
				regroupedBufferedData.addAll(bufferedDataInThisQueue);
			});

			regroupedBufferedData.stream().forEach(bufferedData->
			{
				if(CollectionUtils.isEmpty(bufferedData))return;

				CriticalMarkedData<?> syncData=(CriticalMarkedData<?>)bufferedData.get(0);

				List<Object> unboxedBufferedData=new ArrayList<>();
				for(Object obj:bufferedData)
				{
					CriticalMarkedData<?> boxedData=(CriticalMarkedData<?>) obj;
					unboxedBufferedData.add(boxedData.getTaskParams());
				}

				Task task = deployTask(syncData.getCritialMark(), unboxedBufferedData, stage);
				taskList.add(task);

			}
			);
		}
		else
		{
			for (CriticalMarkedData<?> workingParams : data) {

				Task task = deployTask(workingParams.getCritialMark(), workingParams.getTaskParams(), stage);

				taskList.add(task);
			}
		}
		logger.info("#synchronizedTaskPlanning# with jobIdentity[" + jobIdentity + "],stage uuid[" + stage.getUuid() + "] with taskList size[" + taskList.size() + "]");

		return taskList.stream().collect(Collectors.toList());
	}

	private List<List<?>> splitDataList(List<?> data,int chunckSize)
	{
		List<List<?>> chunckedList=new ArrayList<>();
		List<Object> chunck=new ArrayList<>();
		for(Object obj:data)
		{
			chunck.add(obj);
			if(chunck.size()>=chunckSize)
			{
				chunckedList.add(chunck);
				chunck=new ArrayList<>();
			}
		}
		if(chunck.size()!=0)
			chunckedList.add(chunck);

		return chunckedList;
	}

	private List<Task> randomTaskPlanning(String jobIdentity, Stage stage,List<Object> data) throws Throwable {

		List<Task> taskList = new ArrayList<Task>();
		int chunckSize=stage.getChunkSize();

		if(stage.getChunkSize()<=0)
		{
			for(Object object:data)
			{
				Task task = deployTask(UUID.randomUUID().toString(), object, stage);
				taskList.add(task);
			}
		}
		else
		{
		List<List<?>> chunckedList=splitDataList(data,chunckSize);
		//TODO
		chunckedList.parallelStream().forEach(bufferedData->
		{
			Task task=null;
			task = deployTask(UUID.randomUUID().toString(), bufferedData, stage);
			taskList.add(task);
		});

		}
		logger.debug("#randomTaskPlanning# with jobIdentity[" + jobIdentity + "],stage uuid[" + stage.getUuid() + "] with taskList size[" + taskList.size() + "]");

		return taskList;
	}

	@Override
	public <T> StageResult<T> collectTaskResult(Job job, Class<T> clazz, Step level) {

		Stage stage = job.extractStageBy(level);

		String stageUuid = stage.getUuid();

		String jobUuid = job.getUuid();

		List<Task> taskList = taskHandler.getAllTaskListBy(stageUuid);

		logger.debug("taskList size :" + taskList.size() + " with jobIdentity[" + job.getUuid() + "]");

		boolean isAllTaskDone = JobFactory.isAllTaskDone(taskList);

		boolean hasTaskTimeout = stage.hasTaskTimeout(isAllTaskDone);

		boolean isAllTaskSuc = JobFactory.isAllTaskSuc(taskList);

		if (!isAllTaskDone) {

			return JobFactory.createNoDoneStageResult(jobUuid, stageUuid, stage.getLeftRetryTimes(), hasTaskTimeout);
		}
		List<String> result = collectResultFrom(taskList);

		logger.debug("result size :" + taskList.size() + " with jobIdentity[" + job.getUuid() + "]");

		StageResult<T> stageResult = new StageResult<T>(stageUuid, jobUuid, result, isAllTaskDone,isAllTaskSuc,stage.getLeftRetryTimes(), hasTaskTimeout);

		return stageResult;

	}

	private List<String> collectResultFrom(List<Task> taskList) {

		List<String> result = new ArrayList<String>();

		for (Task task : taskList) {

			result.add(task.getResponse());
		}

		return result;
	}

	private Task deployTask(String businessUuid, Object data, Stage stage)  {

		long start_time=System.currentTimeMillis();

		logger.debug("#deployTask# with parameters businessUuid[" + businessUuid + "],stage uuid[" + stage.getUuid() + "]");

		logger.debug("#deployTask# begin to create task");

		String requestUuid = UUID.randomUUID().toString();

		long start_time1=System.currentTimeMillis();

		Task task = JobFactory.createNewTask(requestUuid, stage.getUuid(), stage.getJobUuid(), JsonUtils.toJsonString(data), "businessUuid_" + businessUuid, stage.getCurrentTaskExpiredTime());

		logger.debug("#deployTask#create task end businessUuid["+businessUuid +"]consume time "+(System.currentTimeMillis()-start_time1));

		long start_time2=System.currentTimeMillis();
		//REDIS
		taskHandler.saveTask(task);

		logger.debug("#deployTask# save redis Task end  businessUuid["+businessUuid +"] at "+(System.currentTimeMillis()-start_time2));

		long start_time3=System.currentTimeMillis();

		logger.debug("#deployTask#send mq  start  businessUuid["+businessUuid +"] at "+start_time3);

		messageSender.publishASyncRPCMessage(requestUuid, businessUuid, stage.getBeanName(), stage.getMethodName(), data, stage.getPriority());

		logger.info("#deployTask#send mq  end  businessUuid["+businessUuid +"] consume time "+(System.currentTimeMillis()-start_time3));

		logger.debug("#deployTask#deploy task  end  businessUuid["+businessUuid+"] at "+System.currentTimeMillis()+"usage "+(System.currentTimeMillis()-start_time));

		return task;

	}

	@Override
	public void reDeploy(Job job, Step level, int seconds) {

		Stage stage = job.extractStageBy(level);

		if (null == stage) {
			return;
		}

		taskHandler.purgeAllTask(stage.getUuid());

		job.retryStage(level, seconds);

		jobService.update(job);

	}

	@Override
	public void doStageInCritical(String jobIdentity, Stage stage,List<CriticalMarkedData<?>> data) throws Throwable {


		if (JobFactory.isStageDone(stage)) {

			logger.info("#doStage# the stage is done with jobIdentity[" + jobIdentity + "],stage uuid[" + stage.getUuid() + "]");

			return;
		}
		if (JobFactory.isStageAbandon(stage)) {

			logger.info("#doStage# the stage is abandon with jobIdentity[" + jobIdentity + "],stage uuid[" + stage.getUuid() + "]");

			return;
		}

		if (JobFactory.isStageProcessing(stage)) {

			String stageUuid = stage.getUuid();

			List<Task> taskList = taskHandler.getAllTaskListBy(stageUuid);

			if (JobFactory.isAllTaskDone(taskList)) {
				jobHandler.updateStageDone(stage, taskList);

				// taskHandler.purgeAllTask(stageUuid);
			}
		}
		if (JobFactory.isStageStart(stage)) {

			jobHandler.updateStageProcessing(stage);

			List<Task> taskList = criticallyTaskPlanning(jobIdentity, stage, data);

			taskHandler.setTaskSetToStage(stage.getUuid(), taskList);
		}

	}

	@Override
    public  void doStageRandomly(String jobIdentity, Stage stage, List<Object> data) throws Throwable
	{
		if (JobFactory.isStageDone(stage)) {

			logger.info("#doStage# the stage is done with jobIdentity[" + jobIdentity + "],stage uuid[" + stage.getUuid() + "]");

			return;
		}
		if (JobFactory.isStageAbandon(stage)) {

			logger.info("#doStage# the stage is abandon with jobIdentity[" + jobIdentity + "],stage uuid[" + stage.getUuid() + "]");

			return;
		}

		if (JobFactory.isStageProcessing(stage)) {

			String stageUuid = stage.getUuid();

			List<Task> taskList = taskHandler.getAllTaskListBy(stageUuid);

			if (JobFactory.isAllTaskDone(taskList)) {

				jobHandler.updateStageDone(stage, taskList);
			}
		}
		if (JobFactory.isStageStart(stage)) {

			jobHandler.updateStageProcessing(stage);

			List<Task> taskList = randomTaskPlanning(jobIdentity, stage, data);

			taskHandler.setTaskSetToStage(stage.getUuid(), taskList);
		}
	}
}
