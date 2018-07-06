package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.VO.ProjectInformationSQLReturnData;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.excel.ProjectInformationExeclVo;
import com.zufangbao.sun.yunxin.entity.model.ProjectInformationQueryModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

@Controller
@RequestMapping(ProjectInformationControllerSpec.NAME)
@MenuSetting("menu-report")
public class ProjectInformationController extends BaseController {

	private static final Log logger = LogFactory.getLog(ProjectInformationController.class);

	@Autowired
	private PrincipalService principalService;

	@Autowired
	private ContractService contractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired 
	private ContractHandler contractHandler;
	
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private FinancialContractService financialContractService;
	
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-project-information")
	public @ResponseBody String getOption(@Secure Principal principal){
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}

	}

	@RequestMapping(value = ProjectInformationControllerSpec.QUERY, method = RequestMethod.GET)
	@MenuSetting("submenu-project-information")
	public @ResponseBody String queryList(
			@ModelAttribute ProjectInformationQueryModel projectInformationQueryModel, Page page) {
		try {
			int size = contractService.countContractListBy(projectInformationQueryModel);
			List<ProjectInformationSQLReturnData> pagedDatas = contractService.getContractListBy(projectInformationQueryModel, page);
			
			List<ProjectInformationExeclVo> pagedShowVOs = contractHandler.castProjectInformationShowVOs(pagedDatas);
			
			Map<String, Object> data = new HashMap<>();
			data.put("list", pagedShowVOs);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	@RequestMapping(value = ProjectInformationControllerSpec.EXPORTEXCEL, method = RequestMethod.GET)
	@MenuSetting("submenu-project-information")
	public @ResponseBody String exportProjectInformation(
			@Secure Principal principal,
			@ModelAttribute ProjectInformationQueryModel projectInformationQueryModel, HttpServletRequest request, HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("13", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			
			List<ProjectInformationSQLReturnData> pagedDatas = contractService.getContractListBy(projectInformationQueryModel, null);
			
			List<ProjectInformationExeclVo> excelVOs = contractHandler.castProjectInformationShowVOs(pagedDatas);
			
			exportEventLogModel.recordAfterLoadDataComplete(excelVOs.size());
			
			ExcelUtil<ProjectInformationExeclVo> excelUtil = new ExcelUtil<ProjectInformationExeclVo>(ProjectInformationExeclVo.class);
			List<String> csvData = excelUtil.exportDatasToCSV(excelVOs);

			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("项目信息表", csvData);
			exportZipToClient(response, create_download_file_name(), GlobalSpec.UTF_8, csvs);
			
			exportEventLogModel.recordEndWriteOutTime();

			StringBuffer selectString = extractSelectString(projectInformationQueryModel);
			SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTPROJECTINFORMATION,LogOperateType.EXPORT);
			log.setRecordContent("导出项目信息报表，导出记录"+(csvData.size()-1)+"条。筛选条件："+selectString);
			systemOperateLogService.save(log);

		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
		return null;
	}

	private StringBuffer extractSelectString(ProjectInformationQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		if (StringUtils.isNotEmpty(queryModel.getLoanEffectStartDate())) {
			selectString.append("，生效起始日期【" + queryModel.getLoanEffectStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getLoanEffectEndDate())) {
			selectString.append("，生效终止日期【" + queryModel.getLoanEffectEndDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getLoanExpectTerminateStartDate())) {
			selectString.append("，预计终止起始日期【" + queryModel.getLoanExpectTerminateStartDate() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getLoanExpectTerminateEndDate())) {
			selectString.append("，预计终止终止日期【" + queryModel.getLoanExpectTerminateEndDate() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getContractNo())) {
			selectString.append("，贷款合同编号【" + queryModel.getContractNo() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getCustomerName())) {
			selectString.append("，客户姓名【" + queryModel.getCustomerName() + "】");
		}
		return selectString;
	}

	private String create_download_file_name() {
		return "项目信息表" +"_"+DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") +".zip";
	}
}
