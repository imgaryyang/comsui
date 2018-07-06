package com.zufangbao.earth.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.zufangbao.sun.yunxin.entity.SecretKey;
import com.zufangbao.sun.yunxin.entity.model.SecretKeyShowModel;

public interface SecretKeyService extends GenericService<SecretKey>  {
	
	static int PUBLIC_KEY_UPPER_LIMIT = 5; 
	

	Integer countSecretKeyBy(Long principalId);

	List<SecretKeyShowModel> getSecretKeyModelList(Long principalId);

	List<SecretKey> getSecretKeyByPrincipalId(Long principalId);

	SecretKey getSecretKeyByUuid(String publicKeyUuid);

}
