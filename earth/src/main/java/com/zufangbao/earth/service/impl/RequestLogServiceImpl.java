package com.zufangbao.earth.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.service.RequestLogService;
import com.zufangbao.sun.yunxin.entity.RequestLog;
import com.zufangbao.sun.yunxin.entity.model.RequestShowModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("requestLogServiceImpl")
public class RequestLogServiceImpl  extends GenericServiceImpl<RequestLog> implements RequestLogService{
	@Override
	public void saveRequestLog(String sourceUuid, String publicKeyUuid, Integer source, String ip, String url) {
		RequestLog requestLog = new RequestLog(ip, sourceUuid,publicKeyUuid, source, url);
		this.save(requestLog);
		
	}

	@Override
	public boolean queryTimesIn3Mins(String sourceUuid, String ip, String url) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -3);
		Date threeMinutesAgo = calendar.getTime();

		String sql = "select count(id) from t_request_log" 
					+ " where ip = :clientIp"
					+ " and source_uuid =:sourceUuid"
					+ " and url =:url"
					+ " and time >= :threeMinutesAgo";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clientIp", ip);
		params.put("threeMinutesAgo", threeMinutesAgo);
		params.put("sourceUuid", sourceUuid);
		params.put("url",url );
		
		return genericDaoSupport.queryForInt(sql, params) > 0;
	}
	
	@Override
	public RequestLog getRequestLogByPublicKeyUuid(String publicKeyUuid) {
		String sql = "select * from t_request_log where public_key_uuid =:publicKeyUuid  ORDER BY time DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("publicKeyUuid", publicKeyUuid);
		List<RequestLog> requestLogs = this.genericDaoSupport.queryForList(sql, params, RequestLog.class, 0,  1);
		if (CollectionUtils.isEmpty(requestLogs)) {
			return null;
		}
		return requestLogs.get(0);
	}
	
	@Override
	public int countRequestLog(String publicKeyUuid) {
		String sql = "select count(id) from t_request_log where public_key_uuid =:publicKeyUuid ORDER BY time DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("publicKeyUuid", publicKeyUuid);
		return this.genericDaoSupport.queryForInt(sql,params);
	}
	
	@Override
	public List<RequestLog> getRequestLogBy(String publicKeyUuid, Integer offset, Page page) {
		String sql = "select * from t_request_log  where public_key_uuid =:publicKeyUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("publicKeyUuid", publicKeyUuid);
		
		if(offset != null){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, offset);
			sql += " and time > :offset";
			params.put("offset", calendar.getTime());
		}
		
		  sql += " ORDER BY time DESC";

		if (page == null) {
			return this.genericDaoSupport.queryForList(sql.toString(), params, RequestLog.class);
		}
		return this.genericDaoSupport.queryForList(sql.toString(), params, RequestLog.class, page.getBeginIndex(),
				page.getEveryPage());
	}

	@Override
	public List<RequestShowModel>getRequestModelList(String publicKeyUuid,Page page) {
		List<RequestLog> RequestLogs = getRequestLogBy(publicKeyUuid,-30, page);
		List<RequestShowModel> requestModelList = new  ArrayList<>();
		for(RequestLog requestLog:RequestLogs){
			RequestShowModel showModel = new RequestShowModel(requestLog);
			requestModelList.add(showModel);
		}
		return requestModelList;

	}
	
}
