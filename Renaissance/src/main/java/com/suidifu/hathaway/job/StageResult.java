/**
 * 
 */
package com.suidifu.hathaway.job;

import java.util.Enumeration;
import java.util.List;

import com.demo2do.core.utils.JsonUtils;

/**
 * @author wukai
 * @param <T>
 *
 */
public class StageResult<T> implements java.io.Serializable,Enumeration<T>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3497867058040106614L;

	private String stageUuid;
	
	private String jobUuid;
	
	private List<String> result;
	
	private int size;
	
	private Class<T> clazz;
	
	private boolean isAllTaskDone;
	
	private boolean isAllTaskSuc;
	
	private boolean hasReachMaxRetryTime;
	
	private boolean hasTaskTimeout;
	
	public StageResult(String stageUuid, String jobUuid,
			List<String> result, boolean isAllTaskDone, boolean isAllTaskSuc, int leftReryTimes, boolean hasTaskTimeout) {
		super();
		this.stageUuid = stageUuid;
		this.jobUuid = jobUuid;
		this.result = result;
		this.size = result.size();
		this.isAllTaskDone = isAllTaskDone;
		this.isAllTaskSuc = isAllTaskSuc;
		this.hasReachMaxRetryTime = leftReryTimes<=0;
		this.hasTaskTimeout =hasTaskTimeout;
	}

	public List<String> getResult() {
		return result;
	}
	public String getStageUuid() {
		return stageUuid;
	}

	public void setStageUuid(String stageUuid) {
		this.stageUuid = stageUuid;
	}

	public String getJobUuid() {
		return jobUuid;
	}

	public void setJobUuid(String jobUuid) {
		this.jobUuid = jobUuid;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	

	public boolean isAllTaskSucDone() {
		return isAllTaskSuc() && isAllTaskDone();
	}

	public boolean isAllTaskSuc() {
		return isAllTaskSuc;
	}
	
	public boolean isFailDone(){
		return isAllTaskDone() == true && !isAllTaskSuc();
	}

	public void setAllTaskSuc(boolean isAllTaskSuc) {
		this.isAllTaskSuc = isAllTaskSuc;
	}

	public boolean isAllTaskDone() {
		return isAllTaskDone;
	}

	@Override
	public boolean hasMoreElements() {
		return this.size > 0;
	}
	@Override
	public T nextElement() {
		
		if(hasMoreElements()){
			
			T t = (T) JsonUtils.parse(result.get(this.size -1 ), this.getClazz());
			
			--this.size;
					
			return t;
			
		}else{
			return null;
		}
	}

	public boolean hasTaskReachRetryTimes() {
		return hasReachMaxRetryTime;
	}

	public boolean hasTaskTimeOut() {
		return hasTaskTimeout;
	}

	public int getSize() {
		return size;
	}

	
	
}
