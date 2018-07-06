package com.zufangbao.earth.yunxin.web.controller.reportform;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.reportform.LoansReportFormHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansShowModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 云信贷款规模报表Controller
 * @author louguanyang
 *
 */
@Controller
@RequestMapping("/reportform/loans")
@MenuSetting("menu-report")
public class LoansReportFormController extends BaseController {
	
	@Autowired
	private LoansReportFormHandler loansReportFormHandler;
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	private static final Log logger = LogFactory.getLog(LoansReportFormController.class);
	
 	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-report-form-loans")
	public@ResponseBody String getOption(@Secure Principal principal) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#LoansReportFormController# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	} 

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@MenuSetting("submenu-report-form-loans")
	public @ResponseBody String query(@Secure Principal principal, Page page, HttpServletRequest request,
			@ModelAttribute LoansQueryModel queryModel) {
		try {
			List<LoansShowModel> list = loansReportFormHandler.query(queryModel, page);
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
	@MenuSetting("submenu-report-form-loans")
	public String export(
			@Secure Principal principal,
			HttpServletRequest request,
			@ModelAttribute LoansQueryModel queryModel,
			HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("14", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			
			List<LoansShowModel> list = loansReportFormHandler.query(queryModel, null);
			
			exportEventLogModel.recordAfterLoadDataComplete(list.size());
			
			ExcelUtil<LoansShowModel> excelUtil = new ExcelUtil<LoansShowModel>(LoansShowModel.class);
			HSSFWorkbook workBook = excelUtil.exportDataToHSSFWork(list, "贷款规模报表");

			exportExcelToClient(response, create_download_file_name(), GlobalSpec.UTF_8, workBook);
			
			exportEventLogModel.recordEndWriteOutTime();

			StringBuffer selectString = extractSelectString(queryModel);
			SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTLOANS,LogOperateType.EXPORT);
			log.setRecordContent("导出贷款规模报表，导出记录"+list.size()+"条。筛选条件："+selectString);
			systemOperateLogService.save(log);
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
		return null;
	}

	private StringBuffer extractSelectString(LoansQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		if (StringUtils.isNotEmpty(queryModel.getStartDateString())) {
			selectString.append("，起始日期【" + queryModel.getStartDateString() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndDateString())) {
			selectString.append("，终止日期【" + queryModel.getEndDateString() + "】");
		}
		return selectString;
	}

	private String create_download_file_name() {
		return "贷款规模报表" +"_"+DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") +".xls";
	}
}
