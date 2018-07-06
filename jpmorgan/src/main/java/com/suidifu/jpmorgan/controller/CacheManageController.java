package com.suidifu.jpmorgan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.jpmorgan.service.BankService;
import com.zufangbao.gluon.resolver.JsonViewResolver;

@Controller
@RequestMapping("/cache")
public class CacheManageController {

	@Autowired
	BankService bankService;
	@Autowired
	JsonViewResolver jsonViewResolver;
	
	
	@RequestMapping(value = "banks/evict", method = RequestMethod.POST)
	public @ResponseBody String banksEvict() {
		try {
			bankService.evictCachedBanks();
			return jsonViewResolver.jsonResult("cache清除成功...");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误!");
		}
	}
}
