package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.RechargeRevokeModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.RechargeRevokeDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentRevokeVO;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class RechargeRevokeController extends BaseController{
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;
	
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	public FinancialContractService financialContractService;
	
	@RequestMapping(value = "customer-account-manage/recharge-revoke-list/show", method = RequestMethod.GET)
	@MenuSetting("submenu-recharge-revoke-list")
	public ModelAndView showRechargeRevoke(@ModelAttribute RechargeRevokeModel rechargeRevokeModel, @Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			ModelAndView modelAndView = new ModelAndView("index");
			return modelAndView;
		} catch(Exception e){
			e.printStackTrace();
		}
		return pageViewResolver.errorSpec();
		
	}
	
	@RequestMapping(value = "customer-account-manage/recharge-revoke-list/show/options", method = RequestMethod.GET)
	public @ResponseBody String getOption(@Secure Principal principal) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("customerTypeList", EnumUtil.getKVList(CustomerType.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
		
	}
	
	/**
	 * 充值撤销 列表
	 * @param principal
	 * @param rechargeRevokeModel
	 * @param page
	 * @return
	 */
	@RequestMapping("customer-account-manage/recharge-revoke-list/query")
	public @ResponseBody String queryRechargeRevoke(@Secure Principal principal,
			@ModelAttribute RechargeRevokeModel rechargeRevokeModel, Page page) {
		try {
			List<SourceDocument> sourceDocumentList = sourceDocumentService.getSourceDocumentRevokeList(rechargeRevokeModel, page);
			List<SourceDocumentRevokeVO> sourceDocumentRevokeVOList = sourceDocumentHandler.castSourceDocumentRevokeVO(sourceDocumentList);
			
			int count = sourceDocumentService.sourceDocumentRevokecount(rechargeRevokeModel);
			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("rechargeRevokeModel", rechargeRevokeModel);
			data.putIfAbsent("list", sourceDocumentRevokeVOList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	
	@RequestMapping(value = "customer-account-manage/recharge-revoke-list/detail-data", method = RequestMethod.GET)
	@MenuSetting("submenu-recharge-revoke-list")
	public @ResponseBody String rechargeRevokeDetail(@Secure Principal principal,@RequestParam("sourceDocumentUuid") String sourceDocumentUuid) {
		try {
			RechargeRevokeDetailModel rechargeRevokeDetailModel = sourceDocumentHandler.getRechargeRevokeDetail(sourceDocumentUuid);
			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("rechargeRevokeDetailModel", rechargeRevokeDetailModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
}
