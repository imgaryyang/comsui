package com.zufangbao.earth.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.zufangbao.earth.service.RequestLogService;
import com.zufangbao.earth.service.SecretKeyService;
import com.zufangbao.sun.yunxin.entity.RequestLog;
import com.zufangbao.sun.yunxin.entity.SecretKey;
import com.zufangbao.sun.yunxin.entity.model.SecretKeyShowModel;


@Service("secretKeyService")
public class SecretKeyServiceImpl extends GenericServiceImpl<SecretKey> implements SecretKeyService{
	
	@Autowired
	private RequestLogService requestLogService;
	
	
	@Override
	public Integer countSecretKeyBy(Long principalId) {
		if(principalId == null || principalId < 0){
			return null;
		}
		String sql = "select count(id) from t_secret_key where principal_id =:principalId";
		Map<String, Object> params = new HashMap<>();
		params.put("principalId", principalId);
		return this.genericDaoSupport.queryForInt(sql,params);
	}
	
	@Override
	public List<SecretKeyShowModel> getSecretKeyModelList(Long principalId) {
		if (principalId == null) {
			return Collections.emptyList();
		}
		List<SecretKey> secretKeys = getSecretKeyByPrincipalId(principalId);
		List<SecretKeyShowModel> messageModelList = new ArrayList<>();
		for (SecretKey secretKey : secretKeys) {
			RequestLog requestLog = requestLogService.getRequestLogByPublicKeyUuid(secretKey.getPublicKeyUuid());
			SecretKeyShowModel showModel = new SecretKeyShowModel(secretKey, requestLog);
			messageModelList.add(showModel);
		}
		return messageModelList;

	}

	@Override
	public List<SecretKey> getSecretKeyByPrincipalId(Long principalId) {
		if(principalId == null){
			return null;
		}
		String sql = "select * from t_secret_key where principal_id =:principalId";
		Map<String, Object> params = new HashMap<>();
		params.put("principalId", principalId);
		return this.genericDaoSupport.queryForList(sql, params, SecretKey.class);
	}
	
	@Override
	public SecretKey getSecretKeyByUuid(String publicKeyUuid) {
		if(publicKeyUuid == null){
			return null;
		}
		String sql = "select * from t_secret_key where public_key_uuid =:publicKeyUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("publicKeyUuid", publicKeyUuid);
		List<SecretKey>secretKeys = this.genericDaoSupport.queryForList(sql, params, SecretKey.class);
		return secretKeys.get(0);
	}	
}
