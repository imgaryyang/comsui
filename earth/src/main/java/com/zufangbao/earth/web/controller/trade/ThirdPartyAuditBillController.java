package com.zufangbao.earth.web.controller.trade;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.trade.ThirdPartyAuditBillDisplayModel;
import com.zufangbao.sun.entity.trade.ThirdPartyAuditBillMapSpec;
import com.zufangbao.sun.entity.trade.ThirdPartyAuditBillQueryModel;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* 
*  @author ociz: 
* 
*/
@RestController
@RequestMapping("bill/third-party")
@MenuSetting("menu-capital")
public class ThirdPartyAuditBillController extends BaseController{

	@Autowired
	private PrincipalHandler principalHandler;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private ThirdPartyAuditBillService thirdPartyAuditBillService;

	@Autowired
	private PrincipalService principalService;

	private static final Log logger = LogFactory.getLog(ExternalTradeBatchController.class);
	
	@RequestMapping(value = "")
	@MenuSetting("submenu-third-party-audit-bill")
	public ModelAndView showThirdPartyAuditBillPage(@Secure Principal principal) {
		return new ModelAndView("index");
	}
	

	@RequestMapping("/optionData")
	public @ResponseBody String getOptionDataForThirdPartyAuditBillPage(@Secure Principal principal) {
		try {
			Map<String, Object> data = new HashMap<>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			data.put("queryAppModels", queryAppModels);
			data.put("queryMap", ThirdPartyAuditBillMapSpec.thirdPartyAuditBillQueryShowStringMap);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#getOptionDataForThirdPartyAuditBillPage occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody String queryThirdPartyAuditBillList(ThirdPartyAuditBillQueryModel queryModel,
			Page page) {
		try {
			List<String> financialContractUuids = financialContractService.getFinancialContractUuidsByIds(queryModel.getFinancialContractIdList());
			queryModel.setFinancialContractUuids(financialContractUuids);
			
			int total = thirdPartyAuditBillService.count(queryModel);
			List<ThirdPartyAuditBill> thirdPartyAuditBills = thirdPartyAuditBillService.getThirdPartyAuditBillListBy(queryModel, page);
			List<ThirdPartyAuditBillDisplayModel> list = new ArrayList<>();
			for (ThirdPartyAuditBill thirdPartyAuditBill : thirdPartyAuditBills) {
				list.add(new ThirdPartyAuditBillDisplayModel(thirdPartyAuditBill));
			}
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#queryThirdPartyAuditBillList occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

}
