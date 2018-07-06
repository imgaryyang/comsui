package com.zufangbao.earth.yunxin.web.controller.reportform;

import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.reportform.InterestReportFormHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestShowModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
/**
 * 应收利息管理
 * @author louguanyang
 *
 */
@Controller
@RequestMapping("/reportform/interest")
@MenuSetting("menu-report")
public class InterestReportFormController extends BaseController{

	@Autowired
	private InterestReportFormHandler interestReportFormHandler;
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private FinancialContractService financialContractService;
	
	private static final Log logger = LogFactory.getLog(InterestReportFormController.class);
	
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-report-form-interest")
	public @ResponseBody String getOption(@Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#InterestReportFormController# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@MenuSetting("submenu-report-form-interest")
	public @ResponseBody String query(@Secure Principal principal, Page page, HttpServletRequest request,
			@ModelAttribute InterestQueryModel queryModel) {
		try {
			List<InterestShowModel> list = interestReportFormHandler.query(queryModel, page);
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", queryModel.getFinancialContractUuidList().size());
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "/exprot", method = RequestMethod.GET)
	@MenuSetting("submenu-report-form-interest")
	public String export(
			@Secure Principal principal,
			HttpServletRequest request,
			@ModelAttribute InterestQueryModel queryModel,
			HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("15", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			
			List<InterestShowModel> list = interestReportFormHandler.query(queryModel, null);
			
			exportEventLogModel.recordAfterLoadDataComplete(list.size());
			
			ExcelUtil<InterestShowModel> excelUtil = new ExcelUtil<InterestShowModel>(InterestShowModel.class);
			HSSFWorkbook workBook = excelUtil.exportDataToHSSFWork(list, "应收利息报表");

			exportExcelToClient(response, create_download_file_name(), GlobalSpec.UTF_8, workBook);
			
			exportEventLogModel.recordEndWriteOutTime();

			StringBuffer selectString = extractSelectString(queryModel);
			SystemOperateLog log = new SystemOperateLog(principal.getId(),IpUtils.getIpAddress(request),LogFunctionType.EXPORTINTEREST,LogOperateType.EXPORT);
			log.setRecordContent("导出应收利息报表，导出记录"+list.size()+"条。筛选条件："+selectString);
			systemOperateLogService.save(log);
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
		return null;
	}

	private StringBuffer extractSelectString(InterestQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		if (StringUtils.isNotEmpty(queryModel.getStartDateString())) {
			selectString.append("，查询起始日期【" + queryModel.getStartDateString() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndDateString())) {
			selectString.append("，查询终止日期【" + queryModel.getEndDateString() + "】");
		}
		return selectString;
	}

	private String create_download_file_name() {
		return "应收利息报表" +"_"+DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") +".xls";
	}
}
