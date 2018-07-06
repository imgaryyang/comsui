package com.zufangbao.earth.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zufangbao.earth.handler.SecretKeyHandler;
import com.zufangbao.earth.service.SecretKeyService;
import com.zufangbao.sun.yunxin.entity.SecretKey;

@Component("secretKeyHandler")
public class SecretKeyHandlerImpl implements SecretKeyHandler{
	@Autowired
	private SecretKeyService secretKeyService;
	@Override
	public void createSecretKey(Long principalId,String title,String publicKey) {
		SecretKey secretKey = new SecretKey(); 
		secretKey.setPrincipalId(principalId);
		secretKey.setTitle(title);
		secretKey.setPublicKey(publicKey);;
		secretKeyService.save(secretKey);
	}

}
