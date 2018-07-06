package com.suidifu.hathaway.job.handler;

import org.apache.commons.lang.StringUtils;

public class CriticalMarkedData<T>
{
		String critialMark;
		
		T taskParams;
		
		public CriticalMarkedData(String criticalMark,
				T taskParams) {
			super();
			this.critialMark = criticalMark;
			this.taskParams = taskParams;
		}
		public CriticalMarkedData(
				T taskParams) {
			this(StringUtils.EMPTY,taskParams);
		}
		public CriticalMarkedData() {
		}

		public String getCritialMark() {
			return critialMark;
		}

		public void setCritialMark(String critialMark) {
			this.critialMark = critialMark;
		}

		public T getTaskParams() {
			return taskParams;
		}

		public void setTaskParams(T taskParams) {
			this.taskParams = taskParams;
		}

		
		
}