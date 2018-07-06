package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccountFlow;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.AccountFlowModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.VirtualAccountFlowShowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class AccountFlowController extends BaseController{
	
	@Autowired
	private VirtualAccountFlowService virtualAccountFlowService;
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	public FinancialContractService financialContractService;
	
	@RequestMapping(value = "customer-account-manage/account-flow-list/show/options", method = RequestMethod.GET)
	public @ResponseBody String getOption(@Secure Principal principal) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("accountSideList", EnumUtil.getKVList(AccountSide.class));
			result.put("virtualAccountTransactionTypeList", EnumUtil.getKVList(VirtualAccountTransactionType.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
		
	}
	
	@RequestMapping("customer-account-manage/account-flow-list/query")
	public @ResponseBody String queryAccountFlow(@Secure Principal principal,
			@ModelAttribute AccountFlowModel accountFlowModel, Page page) {
		try {
			List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.getVirtualAccountFlowList(accountFlowModel, page);
			
			List<VirtualAccountFlowShowModel> modelList = virtualAccountFlowService.getVirtualAccountFlowShowModel(virtualAccountFlowList);
			int count = virtualAccountFlowService.count(accountFlowModel);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("list", modelList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

}
