package com.suidifu.pricewaterhouse.yunxin.handler;

import java.util.List;
import java.util.Map;

import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.impl.JobInterruptException;

public interface CriticalSecionProjector {
	
	
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataLists ) throws JobInterruptException;
	
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataList,Map<String,String> critialMarks ) throws JobInterruptException;
		

}
