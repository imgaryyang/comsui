/**
 * 
 */
package com.suidifu.hathaway.task.handler.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.suidifu.hathaway.job.ExecutingResult;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.JobFactory;
import com.suidifu.hathaway.job.Stage;
import com.suidifu.hathaway.job.Task;
import com.suidifu.hathaway.task.handler.StageTaskHandler;
import com.suidifu.hathaway.task.service.StageTaskService;
import com.suidifu.hathaway.task.service.TaskService;

/**
 * @author wukai
 *
 */
@Component("taskHandlerNoTransaction")
public class StageTaskHandlerImpl implements StageTaskHandler {
	
	
	@Autowired
	private StageTaskService stageTaskService;
	
	@Autowired
	private TaskService taskService;
	
	@Override
	public void saveTask(Task task) {
		taskService.save(task.getUuid(), task,task.getExpiredTime());
	}
	

	@Override
	public Task getOneTaskBy(String taskUuid) {
		return taskService.findOne(taskUuid);
	}


	@Override
	public void updateTask(Task taskInRedis, Object exceptionObject, Object resultObject) {
		
		if(null == taskInRedis){
			return;
		}
		
		String taskUuid = taskInRedis.getUuid();
		
		Boolean taskExist = taskService.hasKey(taskUuid);
		
		if(null == taskExist || taskExist == false){
			return;
		}
		
		taskInRedis.setEndTime(new Date());
		taskInRedis.setLastModifiedTime(new Date());
	
		
		ExecutingResult executingResult  = null;
		ExecutingStatus executingStatus =  ExecutingStatus.DONE;
		
		if(null != exceptionObject){
			executingResult = ExecutingResult.FAIL;
			taskInRedis.setResponse(JSON.toJSONString(exceptionObject));
		}else{
			executingResult = ExecutingResult.SUC;
			taskInRedis.setResponse(JSON.toJSONString(resultObject));
		}
		taskInRedis.setExecutingResult(executingResult);
		taskInRedis.setExecutingStatus(executingStatus);
		
		taskService.delete(taskUuid);;
		
		this.saveTask(taskInRedis);
		
	}
	
	@Override
	public void setTaskSetToStage(String stageUuid, Set<String> taskUuidSet, Date expiredTime) {
		stageTaskService.save(stageUuid, taskUuidSet,expiredTime);
	}

	@Override
	public Set<String> getAllTaskUuidListBy(String stageUuid) {
		return stageTaskService.findOne(stageUuid);
	}
	@Override
	public void setTaskToStage(String stageUuid, String taskUuid, Date expiredTime) {
		
		if(StringUtils.isBlank(stageUuid) || StringUtils.isBlank(taskUuid)){
			return;
		}
		if(stageTaskService.hasKey(stageUuid)){
			
			Set<String> valueInRedis = getAllTaskUuidListBy(stageUuid);
			
			if(CollectionUtils.isEmpty(valueInRedis)){
				valueInRedis = new HashSet<String>();
			}
			
			valueInRedis.add(taskUuid);
			
			stageTaskService.delete(stageUuid);
			
			setTaskSetToStage(stageUuid, valueInRedis, expiredTime);
			 
		}else{
			
			setTaskSetToStage(stageUuid, new HashSet<String>(){{add(taskUuid);}}, expiredTime);
		}
		
	}

	@Override
	public void purgeAllTask(String stageUuid) {
		
		if(!stageTaskService.hasKey(stageUuid)){
			return;
		}
		Set<String> taskUuids =  getAllTaskUuidListBy(stageUuid);
		
		taskService.delete(taskUuids);
		
		stageTaskService.delete(stageUuid);
		
	}

	@Override
	public void saveAllTask(List<Task> taskList) {
		
		for (Task task : taskList) {
			saveTask(task);
		}
	}

	@Override
	public void setTaskSetToStage(String stageUuid, List<Task> taskList) {
		
		if(CollectionUtils.isEmpty(taskList)){
			return;
		}
		
		Date expiredTime = taskList.get(0).getExpiredTime();
		
		Set<String> taskUuidSet = extractTaskUuidSetFrom(taskList);
		
		setTaskSetToStage(stageUuid, taskUuidSet, expiredTime);;
	}

	private Set<String> extractTaskUuidSetFrom(List<Task> taskList) {
		
		Set<String> taskUuidSet = new HashSet<String>();
		
		for (Task task : taskList) {
			
			taskUuidSet.add(task.getUuid());
		}
		return taskUuidSet;
		
	}

	@Override
	public List<Task> getAllTaskListBy(String stageUuid) {
		
		Set<String> taskUuidSet = getAllTaskUuidListBy(stageUuid);
		
		if(CollectionUtils.isEmpty(taskUuidSet)){
			return Collections.emptyList();
		}
		
		List<Task> taskList = new ArrayList<Task>();
		
		for (String taskUuid : taskUuidSet) {
			
			taskList.add(getOneTaskBy(taskUuid));
		}
		return taskList;
	}

	@Override
	public boolean isAllTaskDone(String stageUuid) {
		
		List<Task> taskList = getAllTaskListBy(stageUuid);
		
		return JobFactory.isAllTaskDone(taskList);
	}

	@Override
	public void purgeAllTask(List<Stage> stageList) {
		
		for (Stage stage : stageList) {
			purgeAllTask(stage.getUuid());
		}
		
	}

}
