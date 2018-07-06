package com.zufangbao.earth.api;

import com.demo2do.core.service.GenericService;

import java.util.List;

public interface RequestRecordService extends GenericService<RequestRecord>{
	
	boolean isReqNoExist(String reqNo);

	List<RequestRecord> getRequestRecordsBy(String reqNo);
}
