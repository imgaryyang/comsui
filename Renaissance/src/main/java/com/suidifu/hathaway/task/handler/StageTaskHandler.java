/**
 * 
 */
package com.suidifu.hathaway.task.handler;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.suidifu.hathaway.job.Stage;
import com.suidifu.hathaway.job.Task;

/**
 * @author wukai
 *
 */
public interface StageTaskHandler{
	
	public void saveTask(Task task);
	
	public void saveAllTask(List<Task> taskList);
	
	public void updateTask(Task task, Object exceptionObject, Object resultObject);
	
	public Task getOneTaskBy(String taskUuid);
	
	public void setTaskSetToStage(String stageUuid,Set<String> taskUuidSet, Date expiredTime );
	
	public void setTaskSetToStage(String stageUuid,List<Task> taskList);
	
	public void setTaskToStage(String stageUuid,String taskUuid, Date expiredTime);

	public Set<String> getAllTaskUuidListBy(String stageUuid);
	
	public List<Task> getAllTaskListBy(String stageUuid);
	
	public boolean isAllTaskDone(String stageUuid);
	
	public void purgeAllTask(String stageUuid);
	
	public void purgeAllTask(List<Stage> stageList);
	
}
