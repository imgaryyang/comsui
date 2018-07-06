package com.zufangbao.earth.web.controller.thirdPartVoucher;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherFrontEndShowHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ThirdPartVoucherSourceMapSpec;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherDetailShowModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherQueryShowModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Nov 21, 2016 6:54:27 PM 
* 类说明 
*/
@RestController
@RequestMapping("voucher/thirdParty")
@MenuSetting("menu-capital")
public class ThirdPartVoucherController  extends BaseController{
	
	private static final Log logger = LogFactory.getLog(ThirdPartVoucherController.class);
	 		
	@Autowired
	private PrincipalHandler principalHandler;
	
	@Autowired
	private ThirdPartVoucherFrontEndShowHandler  thirdPartVoucherHandler;
	@Autowired
	private FinancialContractService financialContractService;
     
     
	@RequestMapping("")
	@MenuSetting("submenu-voucher-thirdpart")
	public @ResponseBody ModelAndView loadAll(@Secure Principal principal) {
			ModelAndView modelAndView = new ModelAndView("index");
			return modelAndView;
	}
	@RequestMapping("/optionData")
	@MenuSetting("submenu-voucher-thirdpart")
	public @ResponseBody String getOptionData(@Secure Principal principal){
		 
	 try {
			Map<String, Object> data = new HashMap<>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			data.put("queryAppModels", queryAppModels);
			data.put("repayChannel",  ThirdPartVoucherSourceMapSpec.cashFlowFrontEndShowStringMap);
			data.put("repaymentType", ThirdPartVoucherSourceMapSpec.repayTypeStringFrontEndShowMap);
			data.put("voucherSource", ThirdPartVoucherSourceMapSpec.voucherSourceStringFrontEndShowMap);
			data.put("voucherStatus", ThirdPartVoucherSourceMapSpec.voucherStatusStringFrontEndShowMap);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		
		} catch (Exception e) {
			logger.error("#=====get option data error ==== ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	
 
	@RequestMapping(value = "/query")
	@MenuSetting("submenu-voucher-thirdpart")
	public @ResponseBody String queryThirdPartVoucherList(ThirdPartVoucherQueryModel queryModel,@Secure Principal principal, HttpServletRequest request,Page page) {
		try {
			
			Map<String, Object> data = new HashMap<>();
			List<ThirdPartVoucherQueryShowModel> showModels = thirdPartVoucherHandler.getThirdPartVoucherShowModel(queryModel, page);
			int total  = thirdPartVoucherHandler.countThirdVocuherSize(queryModel);
			
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#query third part voucher error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	@RequestMapping(value ="/detail/{journalVoucherUuid}")
	@MenuSetting("submenu-voucher-thirdpart")
	public @ResponseBody String  queryThirdPartVoucherList(@PathVariable(value = "journalVoucherUuid") String journalVoucherUuid){
	try {
			Map<String, Object> data = new HashMap<>();
			
			ThirdPartVoucherDetailShowModel detailShowModel = thirdPartVoucherHandler.fetch_detail_show_model(journalVoucherUuid);
			data.put("detailShowModel", detailShowModel);
			
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#query third part voucher error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
}
