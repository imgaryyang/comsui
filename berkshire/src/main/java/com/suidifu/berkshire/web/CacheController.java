/**
 * 
 */
package com.suidifu.berkshire.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wukai
 *
 */
@Controller
@RequestMapping("cache")
public class CacheController {

	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping("/clear-cache")
	public @ResponseBody String clearCache(@RequestParam("cache-name")String name) {
		
		cacheManager.getCache(name).clear();
		
		return "ok";
	}
}
