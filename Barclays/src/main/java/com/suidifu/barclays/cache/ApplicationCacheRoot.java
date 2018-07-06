package com.suidifu.barclays.cache;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.Cacheable;

import com.demo2do.core.utils.EnumUtils;

/**
 * 
 * @author zjm
 */
public class ApplicationCacheRoot {

	private static final Log logger = LogFactory.getLog(ApplicationCacheRoot.class);

	/**
	 * Get all the enums
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Cacheable("enums")
	public Map<String, Enum[]> getEnums() {
		return EnumUtils.scan("com.suidifu");
	}

}
