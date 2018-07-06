package com.zufangbao.earth.web.controller.assets;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepaymentPlanSpec;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanDetailsExportStyle;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.RepaymentPlanShowModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentPlan.RepaymentPlanQueryModel;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author zhouchiji
 *
 */
@Controller
@RequestMapping("/repaymentPlan")
@MenuSetting("menu-data")
public class RepaymentPlanController extends BaseController {
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	public FinancialContractService financialContractService;

	private static final Log logger = LogFactory.getLog(RepaymentPlanController.class);

	@RequestMapping("")
	@MenuSetting("submenu-repayment-plan")
	public @ResponseBody ModelAndView loadAll(@Secure Principal principal) {
		return new ModelAndView("index");
	}

	// 获取下拉框选项
	@RequestMapping("/optionData")
	@MenuSetting("submenu-repayment-plan")
	public @ResponseBody String showAllData(@Secure Principal principal) {
		try {
			Map<String, Object> data = new HashMap<>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			data.put("queryAppModels", queryAppModels);
			data.put("executingStatus", RepaymentPlanSpec.repaymentStatusMap);
			data.put("planStatus", RepaymentPlanSpec.planStatusMap);
			data.put("repaymentType", RepaymentPlanSpec.repaymentTypeMap);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("##RepaymentPlanController-showAllData## get option data error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	// 查询
	@RequestMapping("/query")
	@MenuSetting("submenu-repayment-plan")
	public @ResponseBody String query(@ModelAttribute RepaymentPlanQueryModel queryModel, Page page) {
		try {
			List<RepaymentPlanShowModel> repaymentPlanShowModels = repaymentPlanHandler.allRepaymentPlanAmount(queryModel, page);
			int size = repaymentPlanService.count_all_repayment_plan(queryModel);
			Map<String, Object> data = new HashMap<>();
			data.put("size", size);
			data.put("list", repaymentPlanShowModels);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	// 还款计划-统计金额（应还款金额、实际还款金额、差值（应还款-实际还款））
	@RequestMapping("/amountStatistics")
	@MenuSetting("submenu-repayment-plan")
	public @ResponseBody String amountStatistics(@ModelAttribute RepaymentPlanQueryModel repaymentPlanQueryModel) {
		try {
//			Map<String, Object> data = repaymentPlanService.countAllRepaymentAmount(repaymentPlanQueryModel);
			Map<String, Object> data = repaymentPlanHandler.amountStatisticsForRepaymentPlan(repaymentPlanQueryModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("还款相关金额统计错误");
		}
	}

	// 还款计划-还款计划信息预览
	@Deprecated
	@RequestMapping(value = "/repaymentInfo", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-plan")
	public @ResponseBody String previewRepaymentInfo(@RequestParam(value = "uuid") String assetSetUuid) {
		try {
			Map<String, Object> data = repaymentPlanHandler.previewRepaymentInfo(assetSetUuid);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	// 还款计划-导出还款计划
//	@RequestMapping(value = "export-repayment-plan-detail", method = RequestMethod.GET)
//	@MenuSetting("submenu-repayment-plan")
	@Deprecated
	public @ResponseBody String exportRepaymentPlanDetail(HttpServletRequest request, @Secure Principal principal,
			@ModelAttribute RepaymentPlanQueryModel repaymentPlanQueryModel, HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("3", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();

			List<RepaymentPlanDetailsExportStyle> excel = repaymentPlanHandler.generateRepaymentExportModels(repaymentPlanQueryModel,null, null);

			exportEventLogModel.recordAfterLoadDataComplete(excel.size());

			ExcelUtil<RepaymentPlanDetailsExportStyle> excelUtil = new ExcelUtil<RepaymentPlanDetailsExportStyle>(RepaymentPlanDetailsExportStyle.class);
			List<String> csvData = excelUtil.exportDatasToCSV(excel);
			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("还款计划表", csvData);

			exportZipToClient(response, create_repaymnet_plan_summary_file_name("zip"), GlobalSpec.UTF_8, csvs);

			SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
                    principal.getId(), IpUtil.getIpAddress(request),
                    null, LogFunctionType.EXPORTREPAYEMNTPLAN,
					LogOperateType.EXPORT, null, null, null,Collections.emptyList());
			systemOperateLogHandler.generateSystemOperateLog(param);

			exportEventLogModel.recordEndWriteOutTime();
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
			return jsonViewResolver.errorJsonResult(EXPORT_ERROR);
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
	}

	private String create_repaymnet_plan_summary_file_name(String format) {
		return "还款计划明细汇总表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + "." + format;
	}

	// 还款计划-导出预览
	@RequestMapping(value = "preview-export-repayment-management", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-plan")
	public @ResponseBody String previewExportRepaymentManagementrepayment(@ModelAttribute RepaymentPlanQueryModel repaymentPlanQueryModel) {
		try {
			Page page = new Page(0, 10);
			List<RepaymentPlanDetailsExportStyle> excel = repaymentPlanHandler.generateRepaymentExportModels(repaymentPlanQueryModel,null,  page);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", excel);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("**previewExportRepaymentManagement** occur error!");
			return jsonViewResolver.errorJsonResult("预览失败");
		}
	}
}
