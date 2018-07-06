/**
 * 
 */
package com.suidifu.berkshire.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.berkshire.handler.DictionaryHandler;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;

/**
 * @author wukai
 *
 */
@Component(value="cacheableDictionaryHandler")
public class DictionaryHandlerImpl implements DictionaryHandler {
	
	@Autowired
	private DictionaryService dictionaryService;

	/* (non-Javadoc)
	 * @see com.suidifu.berkshire.handler.DictionaryHandler#getValueByKey(java.lang.String)
	 */
	@Override
	public Dictionary getValueByKey(String key) {
		try {
			return dictionaryService.getDictionaryByCode(key);
		} catch (DictionaryNotExsitException e) {
			
			return null;
		}
	}

}
