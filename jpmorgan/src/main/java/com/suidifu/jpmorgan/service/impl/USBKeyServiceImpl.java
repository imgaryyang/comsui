package com.suidifu.jpmorgan.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.USBKey;
import com.suidifu.jpmorgan.service.USBKeyService;
/**
 * 
 * @author zjm
 *
 */
@Service("usbKeyService")
public class USBKeyServiceImpl extends GenericServiceImpl<USBKey> implements USBKeyService {

	@Override
	public USBKey getUSBKeyByUUID(String uuid) {
		
		if(StringUtils.isEmpty(uuid)) {
			return null;
		}
		
		List<USBKey> keyList = genericDaoSupport.searchForList("FROM USBKey WHERE uuid =:uuid", "uuid", uuid);

		if (CollectionUtils.isEmpty(keyList)) {
			return null;
		}
		return keyList.get(0);
	}


}
