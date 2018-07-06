package com.zufangbao.earth.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.RequestLog;
import com.zufangbao.sun.yunxin.entity.model.RequestShowModel;

public interface RequestLogService extends GenericService<RequestLog>{

	boolean queryTimesIn3Mins(String sourceUuid, String ip, String url);

	void saveRequestLog(String sourceUuid, String publicKeyUuid, Integer source, String ip, String url);

	RequestLog getRequestLogByPublicKeyUuid(String publicKeyUuid);

	int countRequestLog(String publicKeyUuid);

	List<RequestLog> getRequestLogBy(String publicKeyUuid,Integer offset, Page page);

	List<RequestShowModel> getRequestModelList(String publicKeyUuid,Page page);


}
