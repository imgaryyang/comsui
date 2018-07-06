/**
 * 
 */
package com.suidifu.hathaway.job;

/**
 * @author wukai
 *
 */
public enum Priority {
	
	Max(10,"最高优先级"),
	
	RealTime(8,"实时"),
	
	High(6,"高"),
	
	Medium(4,"中"),
	
	Low(2,"低"),
	
	Min(0,"最小优先级"),
	
	;
	
	private int priority;
	
	private String alias;
	
	private Priority(int priority,String alias){
		this.priority = priority;
		this.alias = alias;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
