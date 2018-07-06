/**
 * 
 */
package com.suidifu.berkshire.handler;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.zufangbao.sun.yunxin.entity.Dictionary;

/**
 * @author wukai
 *
 */
@CacheConfig(cacheNames = "dicionary")
public interface DictionaryHandler {

	@Cacheable
	public Dictionary getValueByKey(String key);
}
