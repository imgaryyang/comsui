package com.zufangbao.earth.web.controller.system;

import java.util.*;

import com.demo2do.core.utils.EnumUtils;
import com.zufangbao.sun.api.model.systemlogs.QuerySystemLogModel;
import com.zufangbao.sun.api.model.systemlogs.SystemOperateLogModel;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.demo2do.core.persistence.support.Order;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.sun.service.SystemLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;

@Controller
@RequestMapping("logs")
@MenuSetting("menu-system")
public class LogsController {
	
	@Autowired
	private SystemLogService userLoginLogService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private CompanyService companyService;


	@Autowired
	public JsonViewResolver jsonViewResolver;
	
	@RequestMapping(value="user-login-log")
	@MenuSetting("submenu-user-login-log")
	public ModelAndView showLogs(Page page, boolean isAsc){
		ModelAndView result = new ModelAndView("index");
		return result;
	}

	@RequestMapping(value = "user-login-log/query")
	public @ResponseBody String queryLogsOperators(Page page, @ModelAttribute QuerySystemLogModel model, boolean isAsc){
		long size = systemOperateLogService.countLogs(model);
		List<Map<String,Object>> logModels = systemOperateLogService.getSystemOperateLogModelsBy(model, isAsc, page);
		List<SystemOperateLogModel> list = new ArrayList<>();
		for (Map<String,Object> map:logModels) {
			SystemOperateLogModel logModel = new SystemOperateLogModel(map);
			list.add(logModel);
		}
		Map<String, Object> result = new HashMap<>();
		result.put("list",list);
		result.put("size",size);
		return jsonViewResolver.sucJsonResult(result);
	}

	@RequestMapping(value = "user-login-log/options", method=RequestMethod.GET)
	public @ResponseBody String getMenuOptions(){
		try {
			Map<String, Object> result = new HashMap<>();

			List<Company> companies = companyService.getAllCompany();
			result.put("companies", companies);
			result.put("logOperateTypes", EnumUtil.getKVList(LogOperateType.class));
			return jsonViewResolver.sucJsonResult(result);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取系统日志查询列表失败");
		}
	}

//	@RequestMapping(value="user-login-log/query")
//	public @ResponseBody String getLogsOptions(Page page, boolean isAsc){
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<SystemOperateLog> logs = searchLogsByCreateTime(page, isAsc);
//		result.put("list", logs);
//		result.put("size", countLogsize());
//		return jsonViewResolver.sucJsonResult(result);
//	}
//
//	public List<SystemOperateLog> searchLogsByCreateTime(Page page, boolean isAsc) {
//		Order orderByOccurTime = new Order();
//		orderByOccurTime.add("occurTime", isAsc ? "ASC" : "DESC");
//		List<SystemOperateLog> logs = systemOperateLogService.list(SystemOperateLog.class,orderByOccurTime,page);
//		return logs;
//	}
//	private int countLogsize() {
//		Order orderByOccurTime = new Order();
//		List<SystemOperateLog> logs = systemOperateLogService.list(SystemOperateLog.class, orderByOccurTime);
//		if(null==logs)return 0;
//		return logs.size();
//	}
}
