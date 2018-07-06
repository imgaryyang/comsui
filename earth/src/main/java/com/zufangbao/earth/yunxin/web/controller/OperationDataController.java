package com.zufangbao.earth.yunxin.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.reportform.OperationReportFormHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.entity.daily.BusinessType;
import com.zufangbao.sun.entity.daily.DailyActualRepayment;
import com.zufangbao.sun.entity.daily.DailyGuarantee;
import com.zufangbao.sun.entity.daily.DailyPartRepayment;
import com.zufangbao.sun.entity.daily.DailyPlanRepayment;
import com.zufangbao.sun.entity.daily.DailyPreRepayment;
import com.zufangbao.sun.entity.daily.DailyRemittance;
import com.zufangbao.sun.entity.daily.DailyRepurchase;
import com.zufangbao.sun.entity.daily.PlanStyle;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.DailyActualRepaymentService;
import com.zufangbao.sun.service.DailyGuaranteeService;
import com.zufangbao.sun.service.DailyPartRepaymentService;
import com.zufangbao.sun.service.DailyPlanRepaymentService;
import com.zufangbao.sun.service.DailyPreRepaymentService;
import com.zufangbao.sun.service.DailyRemittanceService;
import com.zufangbao.sun.service.DailyRepurchaseService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.DailyActualRepaymentModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataExportModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataQueryModel;
import com.zufangbao.sun.yunxin.handler.DailyActualRepaymentHandler;
import com.zufangbao.sun.yunxin.handler.DailyGuaranteeHandler;
import com.zufangbao.sun.yunxin.handler.DailyPartRepaymentHandler;
import com.zufangbao.sun.yunxin.handler.DailyPlanRepaymentHandler;
import com.zufangbao.sun.yunxin.handler.DailyPreRepaymentHandler;
import com.zufangbao.sun.yunxin.handler.DailyRemittanceHandler;
import com.zufangbao.sun.yunxin.handler.DailyRepurchaseHandler;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;


/**
 * 运营数据报表Controller
 * @author wangyuqi
 *
 */

@Controller
@RequestMapping("/operation-data")
@MenuSetting("menu-report")
public class OperationDataController  extends BaseController {
	
	private static final Log logger = LogFactory.getLog(OperationDataController.class);
	private static final String UTF_8 = "UTF-8";
	@Autowired
	private PrincipalHandler principalHandler;
	
	@Autowired
	private OperationReportFormHandler operationReportFormHandler;
	
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private DailyRemittanceHandler dailyRemittanceHandler;
	@Autowired
	private DailyPlanRepaymentHandler dailyPlanRepaymentHandler;
	@Autowired
	private DailyActualRepaymentHandler dailyActualRepaymentHandler;
	@Autowired
	private DailyPreRepaymentHandler dailyPreRepaymentHandler;
	@Autowired
	private DailyPartRepaymentHandler dailyPartRepaymentHandler;
	@Autowired
	private DailyGuaranteeHandler dailyGuaranteeHandler;
	@Autowired
	private DailyRepurchaseHandler dailyRepurchaseHandler;
	@Autowired
	private DailyRemittanceService dailyRemittanceService;
	@Autowired
	private DailyPlanRepaymentService dailyPlanRepaymentService;
	@Autowired
	private DailyActualRepaymentService dailyActualRepaymentService;
	@Autowired
	private DailyPreRepaymentService dailyPreRepaymentService;
	@Autowired
	private DailyPartRepaymentService dailyPartRepaymentService;
	@Autowired
	private DailyGuaranteeService dailyGuaranteeService;
	@Autowired
	private DailyRepurchaseService dailyRepurchaseService;

	//查询选项
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public @ResponseBody String getOptions(@Secure Principal principal) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);

			return jsonViewResolver.sucJsonResult(result, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getOptions# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}
	
	// 通道类型
	@RequestMapping("/cashFlowChannelType")
	public @ResponseBody String getCashFlowChannelType() {
		try {
			return jsonViewResolver.sucJsonResult("cashFlowChannelType", EnumUtil.getKVList(CashFlowChannelType.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getCashFlowChannelType occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取通道类型列表失败！！！");
		}
	}
	
	// 凭证来源
	@RequestMapping("/journalVoucherType")
	public @ResponseBody String getJournalVoucherType() {
		try {
			return jsonViewResolver.sucJsonResult("journalVoucherType", EnumUtil.getKVList(JournalVoucherType.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getJournalVoucherType occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取凭证来源列表失败！！！");
		}
	}
	
	//页面查询--总额
	@RequestMapping(value = "/query/totalAmount", method = RequestMethod.GET)
	public @ResponseBody String queryTotalAmount(@ModelAttribute OperationDataQueryModel queryModel) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = operationReportFormHandler.queryTotalAmount(queryModel);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryTotalAmount occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--放款
	@RequestMapping(value = "/query/remittance", method = RequestMethod.GET)
	public @ResponseBody String queryRemittance(@ModelAttribute OperationDataQueryModel queryModel, Page page) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			int size = dailyRemittanceService.count(queryModel);
            List<DailyRemittance> list = dailyRemittanceService.queryDailyRemittanceListBy(queryModel, page);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", list);
            data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryRemittance occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--放款
	@RequestMapping(value = "/statistics/remittance", method = RequestMethod.GET)
	public @ResponseBody String statisticsRemittance(@ModelAttribute OperationDataQueryModel queryModel) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = dailyRemittanceHandler.statisticsRemittance(queryModel);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsRemittance occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--应收款
	@RequestMapping(value = "/query/planRepayment", method = RequestMethod.GET)
	public @ResponseBody String queryPlanRepayment(@ModelAttribute OperationDataQueryModel queryModel,
			@RequestParam(value = "planStyleOrdinal", required = false) Integer planStyleOrdinal, Page page) {
		
		try {
			PlanStyle planStyle = planStyleOrdinal == null ? null : PlanStyle.formValue(planStyleOrdinal);
			if (queryModel == null || !queryModel.validParameter() || planStyle == null) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			int size = dailyPlanRepaymentService.count(queryModel, planStyle);
			List<DailyPlanRepayment> list = dailyPlanRepaymentService.queryDailyPlanRepaymentListBy(queryModel, planStyle, page);
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryPlanRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--应收款
	@RequestMapping(value = "/statistics/planRepayment", method = RequestMethod.GET)
	public @ResponseBody String statisticsPlanRepayment(@ModelAttribute OperationDataQueryModel queryModel,
			@RequestParam(value = "planStyleOrdinal", required = false) Integer planStyleOrdinal) {
		try {
			PlanStyle planStyle = planStyleOrdinal == null ? null : PlanStyle.formValue(planStyleOrdinal);
			if (queryModel == null || !queryModel.validParameter() || planStyle == null) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = dailyPlanRepaymentHandler.statisticsPlanRepayment(queryModel, planStyle);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsPlanRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--实际还款
	@RequestMapping(value = "/query/actualRepayment", method = RequestMethod.GET)
	public @ResponseBody String queryActualRepayment(@ModelAttribute OperationDataQueryModel queryModel,
			@RequestParam(value = "businessTypeOrdinal", required = false) Integer businessTypeOrdinal, Page page) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			BusinessType businessType = businessTypeOrdinal == null ? null : BusinessType.formValue(businessTypeOrdinal);
			int size = dailyActualRepaymentService.count(queryModel, businessType);
			List<DailyActualRepaymentModel> list = dailyActualRepaymentHandler.queryDailyActualRepaymentModel(queryModel, businessType, page);
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryActualRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--实际还款
	@RequestMapping(value = "/statistics/actualRepayment", method = RequestMethod.GET)
	public @ResponseBody String statisticsActualRepayment(@ModelAttribute OperationDataQueryModel queryModel,
			@RequestParam(value = "businessTypeOrdinal", required = false) Integer businessTypeOrdinal) {
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			BusinessType businessType = businessTypeOrdinal == null ? null : BusinessType.formValue(businessTypeOrdinal);
			Map<String, Object> data = dailyActualRepaymentHandler.statisticsActualRepayment(queryModel, businessType);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsActualRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//浮框--线上还款
	@RequestMapping(value = "/float/online", method = RequestMethod.GET)
	public @ResponseBody String floatOnline(@RequestParam(value = "financialContractUuid") String financialContractUuid,
			@RequestParam(value = "queryDate") String queryDate) {
		try {
			if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(queryDate)) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			List<DailyActualRepayment> list = dailyActualRepaymentHandler.getOnlineRepaymentList(financialContractUuid, queryDate);
			
			return jsonViewResolver.sucJsonResult("list", list, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#floatOnline occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//浮框--线下还款
	@RequestMapping(value = "/float/offline", method = RequestMethod.GET)
	public @ResponseBody String floatOffline(@RequestParam(value = "financialContractUuid") String financialContractUuid,
			@RequestParam(value = "queryDate") String queryDate) {
		try {
			if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(queryDate)) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			List<DailyActualRepayment> list = dailyActualRepaymentHandler.getOfflineRepaymentList(financialContractUuid, queryDate);
			
			return jsonViewResolver.sucJsonResult("list", list, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#floatOffline occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--提前还款
	@RequestMapping(value = "/query/preRepayment", method = RequestMethod.GET)
	public @ResponseBody String queryPreRepayment(@ModelAttribute OperationDataQueryModel queryModel, Page page) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			int size = dailyPreRepaymentService.count(queryModel);
			List<DailyPreRepayment> list = dailyPreRepaymentService.queryDailyPreRepaymentListBy(queryModel, page);
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryPreRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--提前还款
	@RequestMapping(value = "/statistics/preRepayment", method = RequestMethod.GET)
	public @ResponseBody String statisticsPreRepayment(@ModelAttribute OperationDataQueryModel queryModel) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = dailyPreRepaymentHandler.statisticsPreRepayment(queryModel);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsPreRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--部分还款
	@RequestMapping(value = "/query/partRepayment", method = RequestMethod.GET)
	public @ResponseBody String queryPartRepayment(@ModelAttribute OperationDataQueryModel queryModel, Page page) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			int size = dailyPartRepaymentService.count(queryModel);
			List<DailyPartRepayment> list = dailyPartRepaymentService.queryDailyPartRepaymentListBy(queryModel, page);
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryPartRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--部分还款
	@RequestMapping(value = "/statistics/partRepayment", method = RequestMethod.GET)
	public @ResponseBody String statisticsPartRepayment(@ModelAttribute OperationDataQueryModel queryModel) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = dailyPartRepaymentHandler.statisticsPartRepayment(queryModel);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsPartRepayment occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--担保
	@RequestMapping(value = "/query/guarantee", method = RequestMethod.GET)
	public @ResponseBody String queryGuarantee(@ModelAttribute OperationDataQueryModel queryModel, Page page) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			int size = dailyGuaranteeService.count(queryModel);
            List<DailyGuarantee> list = dailyGuaranteeService.queryDailyGuaranteeListBy(queryModel, page);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", list);
            data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryGuarantee occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--担保
	@RequestMapping(value = "/statistics/guarantee", method = RequestMethod.GET)
	public @ResponseBody String statisticsGuarantee(@ModelAttribute OperationDataQueryModel queryModel) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = dailyGuaranteeHandler.statisticsGuarantee(queryModel);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsGuarantee occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//页面查询--回购
	@RequestMapping(value = "/query/repurchase", method = RequestMethod.GET)
	public @ResponseBody String queryRepurchase(@ModelAttribute OperationDataQueryModel queryModel, Page page) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			int size = dailyRepurchaseService.count(queryModel);
			List<DailyRepurchase> list = dailyRepurchaseService.queryDailyRepurchaseListBy(queryModel, page);
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#queryRepurchase occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	//统计--回购
	@RequestMapping(value = "/statistics/repurchase", method = RequestMethod.GET)
	public @ResponseBody String statisticsRepurchase(@ModelAttribute OperationDataQueryModel queryModel) {
		
		try {
			if (queryModel == null || !queryModel.validParameter()) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			
			Map<String, Object> data = dailyRepurchaseHandler.statisticsRepurchase(queryModel);
			
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#statisticsRepurchase occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
//	//导出报表
//	@RequestMapping(value = "/export", method = RequestMethod.GET)
//	public @ResponseBody String export(@ModelAttribute OperationDataQueryModel operationDataQueryModel, HttpServletRequest request, HttpServletResponse response, @Secure Principal principal) {
//		
//		try {
//			
//			List<OperationalDataPageShowModel> models = operationReportFormHandler.query(operationDataQueryModel, null);
//			
//			Map<String, Map<String, List<Object>>> excelMap = models.stream()
//					.filter(m -> m != null)
//					.collect(Collectors.toMap(OperationalDataPageShowModel::getFileName, OperationalDataPageShowModel::getFormReportMap));
//			
//			Map<String, HSSFWorkbook> bookMap = new HashMap<>();
//			
//			for(Map.Entry<String,Map<String, List<Object>>> entry:excelMap.entrySet()){
//				
//				HSSFWorkbook book = ExcelUtilForSheets.exportExcelForSheets(entry.getValue());
//				
//				bookMap.put(entry.getKey(), book);
//			}
//			String zipFileName = "运营数据报表_"+DateUtils.format(new Date(), "yyyy_MM_dd_HH_mm_ss")+".zip";
//			
//			OutputStream out = openZipOutputStream(response, zipFileName);
//			
//			exportZipToClient(bookMap, out);
//			
//			StringBuffer selectString = extractSelectString(operationDataQueryModel);
//			SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTOPERATIONALDATA,LogOperateType.EXPORT);
//			log.setRecordContent("导出运营数据报表，导出记录"+models.size()+"条。筛选条件："+selectString);
//			systemOperateLogService.save(log);
//			return jsonViewResolver.sucJsonResult();
//		} catch (Exception e) {
//			
//			logger.error("#exportOperationData occur error.");
//			
//			e.printStackTrace();
//			
//			return jsonViewResolver.errorJsonResult("系统错误");
//		}
//	}
	
//	private StringBuffer extractSelectString(OperationDataQueryModel queryModel) {
//		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
//		if (StringUtils.isNotEmpty(queryModel.getQueryDate())) {
//			selectString.append("，查询日期【" + queryModel.getQueryDate() + "】");
//		}
//		return selectString;
//	}
//
//	private void exportZipToClient(Map<String,HSSFWorkbook> workBookMap,OutputStream out) throws IOException{
//		ZipOutputStream zip = new ZipOutputStream(out);
//		for(Map.Entry<String,HSSFWorkbook> entry: workBookMap.entrySet()){
//			String entryFileName = entry.getKey();
//			HSSFWorkbook work = entry.getValue();
//			ZipEntry zipEntry = new ZipEntry(entryFileName);
//			zip.putNextEntry(zipEntry);
//			work.write(zip);
//			work.close();
//			zip.flush();
//		}
//		zip.flush();
//		zip.close();
//	}
//	
//	private OutputStream openZipOutputStream(HttpServletResponse response, String fileName) throws IOException {
//		String zipFileName=URLEncoder.encode(fileName, UTF_8);
//		response.setContentType("application/octet-stream");
//		response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName + "; filename*="+ UTF_8 +"''"+ zipFileName);
//		return response.getOutputStream();
//	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletRequest request, @Secure Principal principal, 
    		@ModelAttribute OperationDataExportModel exportModel,
    		HttpServletResponse response) {
        try {
            if (exportModel == null || exportModel.getOperationDataQueryModel() == null) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
            FinancialContract fc = financialContractService.getFinancialContractBy(exportModel.getOperationDataQueryModel().getFinancialContractUuid());
            if (fc == null) {
            	return jsonViewResolver.errorJsonResult("参数错误！");
			}
            HSSFWorkbook workBook = operationReportFormHandler.buildExcel(exportModel);
			
            exportExcelToClient(response, fc.getContractName()+"运营数据.xls", GlobalSpec.UTF_8, workBook);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#export# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误,请重试");
        }
    }
    
}
