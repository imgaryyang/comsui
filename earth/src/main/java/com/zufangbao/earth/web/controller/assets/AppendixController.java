package com.zufangbao.earth.web.controller.assets;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.appendix.AppendixQueryModel;
import com.zufangbao.sun.yunxin.entity.model.appendix.AppendixShowModel;
import com.zufangbao.sun.yunxin.handler.AppendixLogHandler;
import com.zufangbao.sun.yunxin.service.files.AppendixLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/appendix")
@MenuSetting("menu-data")
public class AppendixController extends BaseController {
	
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	public FinancialContractService financialContractService;
	@Autowired
	private AppendixLogService appendixLogService;
	@Autowired
	private AppendixLogHandler appendixLogHandler;
	
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public @ResponseBody String getAppendixListOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			result.put("queryAppModels", queryAppModels);
		return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public @ResponseBody String query(Page page, @ModelAttribute AppendixQueryModel appendixQueryModel) {
		try {
			int size = appendixLogService.countAppendixList(appendixQueryModel);
			List<AppendixShowModel> appendixShowModelList = appendixLogHandler.showAppendixLogs(appendixQueryModel, page);
			Map<String, Object> data = new HashMap<>();
			data.put("list", appendixShowModelList);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

}
