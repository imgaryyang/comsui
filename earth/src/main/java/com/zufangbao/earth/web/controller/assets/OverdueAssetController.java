package com.zufangbao.earth.web.controller.assets;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AuditOverdueStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentExecutionState;
import com.zufangbao.sun.yunxin.entity.excel.OverDueRepaymentDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.OverdueAssetShowModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentPlan.OverdueAssetQueryModel;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/overdueAsset")
@MenuSetting("menu-data")
public class OverdueAssetController extends BaseController {
	
	private static final Log logger = LogFactory.getLog(OverdueAssetController.class);
	
	@Autowired
	PrincipalHandler principalHandler;
	
	@Autowired
	RepaymentPlanHandler repaymentPlanHandler;
	
	@Autowired
	RepaymentPlanService repaymentPlanService;

	@Autowired
	private PrincipalService principalService;

	@Autowired
	public FinancialContractService financialContractService;

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-overdue-asset")
	public @ResponseBody String getOptions(@Secure Principal principal) {
		
		try {
			Map<String, Object> options = new HashMap<>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);

			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			options.put("queryAppModels", queryAppModels);

			options.put("paymentStatusList", EnumUtil.getKVListIncludes(RepaymentExecutionState.class, Arrays.asList(RepaymentExecutionState.PROCESSING, RepaymentExecutionState.DEDUCTING, RepaymentExecutionState.SUCCESS, RepaymentExecutionState.REPURCHASING, RepaymentExecutionState.REPURCHASED)));

			options.put("auditOverdueStatusList", EnumUtil.getKVListIncludes(AuditOverdueStatus.class, Arrays.asList(AuditOverdueStatus.UNCONFIRMED, AuditOverdueStatus.OVERDUE)));
			
			return jsonViewResolver.sucJsonResult(options);
			
		} catch (Exception e) {
			
			logger.error("##OverdueAssetController-getOptions ## get option data error!!");
			
			e.printStackTrace();
			
			return jsonViewResolver.errorJsonResult("系统错误");
		}
		
	}
	
	@RequestMapping("/query")
	public @ResponseBody String query(@ModelAttribute OverdueAssetQueryModel model, Page page) {
		
		try {
			List<OverdueAssetShowModel> showModels = repaymentPlanHandler.getOverDueAssetShowModelList(model, page);
			
			Integer size = repaymentPlanService.countOverdueAsset(model);
			
			Map<String, Object> data = new HashMap<>();
			
			data.put("size", size);
			
			data.put("list", showModels);
			
			return jsonViewResolver.sucJsonResult(data);
			
		} catch (Exception e) {
			
			logger.error("##OverdueAssetController-query ## query data error!!");
			
			e.printStackTrace();
			
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	// 逾期还款计划-统计金额（应还款金额、实际还款金额、差值（应还款-实际还款））
		@RequestMapping("/amountStatistics")
		@MenuSetting("submenu-overdue-asset")
		public @ResponseBody String amountStatistics(@ModelAttribute OverdueAssetQueryModel model) {
			
			try {
				
				Map<String, Object> data = repaymentPlanService.countAllOverdueAssetAmount(model);
				
				return jsonViewResolver.sucJsonResult(data);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				return jsonViewResolver.errorJsonResult("统计相关金额错误");
			}
		}

		// 逾期还款明细表--导出预览
		@RequestMapping(value = "preview-export-overdue-management", method = RequestMethod.GET)
		@MenuSetting("submenu-overdue-asset")
		public @ResponseBody String previewExportOverDueRepaymentPlan(@ModelAttribute OverdueAssetQueryModel queryModel) {
			
			try {
				
				List<OverDueRepaymentDetailExcelVO> overDueRepaymentDetailExcelVOs = repaymentPlanHandler.previewOverDueRepaymentDetailExcelVO(queryModel, 10);
				
				Map<String, Object> data = new HashMap<String, Object>();
				
				data.put("list", overDueRepaymentDetailExcelVOs);
				
				return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
				
			} catch (Exception e) {
				
				logger.error("#previewExportOverDueRepaymentPlan  occur error.");
				
				e.printStackTrace();
				
				return jsonViewResolver.errorJsonResult("逾期还款明细表导出预览失败");
				
			}
		}
}
