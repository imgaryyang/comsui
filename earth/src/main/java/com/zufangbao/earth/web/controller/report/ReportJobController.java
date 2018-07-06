package com.zufangbao.earth.web.controller.report;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.report.wrapper.async.IAsyncReportWrapper;
import com.zufangbao.earth.report.wrapper.handler.ReportJobHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.gluon.util.SpringContextUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.reportJob.ReportJob;
import com.zufangbao.sun.entity.reportJob.ReportJobDisplayModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.ReportJobStatus;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.ReportJobQueryModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.ReportJobService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zufangbao.earth.report.factory.ExportConfiguration.REPORT_CODES_ASYNC_WRAPPER_BEAN_MAPPER;
import static com.zufangbao.earth.report.factory.ExportConfiguration.REPORT_CODES_PARAMS_CLASS_MAPPER;

/**
 * 报表任务控制器
 * 
 * @author zhanghongbing
 *
 */
@Controller
@RequestMapping("/report/job")
public class ReportJobController extends ReportBaseController {

	private static final Log logger = LogFactory.getLog(ReportJobController.class);

	@Autowired
	private AsyncReportJobWriter reportJobWriter;

	@Autowired
	private ReportJobHandler reportJobHandler;

	@Autowired
	private ReportJobService reportJobService;

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private PrincipalService principalService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private FinancialContractService financialContractService;

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public @ResponseBody String getOption(@Secure Principal principal) {
		try {

			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("requestId", getRequestId());
			result.put("queryAppModels", queryAppModels);
			result.put("reportJobStatusList", EnumUtil.getKVList(ReportJobStatus.class));
			result.put("financialContractUuidMap",
					financialContracts.stream().collect(Collectors.toMap(FinancialContract::getFinancialContractUuid, FinancialContract::getContractName)));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#getOption occured error" + e.getMessage());
			return errorJsonResult(e);
		}
	}

	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public @ResponseBody String queryReportJob(@Secure Principal principal,@ModelAttribute ReportJobQueryModel reportJobModel,
			HttpServletRequest request,HttpServletResponse response, Page page) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			if(CollectionUtils.isEmpty(reportJobModel.getFinancialContractUuidList())) {
				result.put("size", 0);
				result.put("list", Collections.emptyList());
				return jsonViewResolver.sucJsonResult(result);
			}
			reportJobModel.setUserId(principal.getId());
			int size = reportJobService.countReportJobBy(reportJobModel);
			List<ReportJob> reportJobList = reportJobService.getReportJobBy(reportJobModel, page);
			List<ReportJobDisplayModel> reportJobQueryList = reportJobList.stream().map(rb -> new ReportJobDisplayModel(rb))
					.collect(Collectors.toList());
			result.put("list", reportJobQueryList);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryReportJob occured error" + e.getMessage());
			return errorJsonResult(e);
		}
	}

	private Map<String, String> getRequestId() {
		Map<String, String> requestId = new HashMap<String, String>();
		// requestId.put("14", "贷款规模");
		// requestId.put("15", "应收利息");
		requestId.put("16", "部分还款");
		return requestId;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public @ResponseBody String createReportJob(@Secure Principal principal, HttpServletRequest request,
			HttpServletResponse response, String reportId) {
		try {
			if (StringUtils.isBlank(reportId) || !REPORT_CODES_PARAMS_CLASS_MAPPER.containsKey(reportId)
					|| !REPORT_CODES_ASYNC_WRAPPER_BEAN_MAPPER.containsKey(reportId)) {
				return jsonViewResolver.errorJsonResult("无效的报表编号！");
			}
			// 根据报表编号，获取查询参数类
			Class<?> paramsClass = REPORT_CODES_PARAMS_CLASS_MAPPER.get(reportId);
			// 根据报表编号，获取报表包装类
			Class<? extends IAsyncReportWrapper<?>> wrapperClass = REPORT_CODES_ASYNC_WRAPPER_BEAN_MAPPER.get(reportId);

			if (paramsClass == null || wrapperClass == null) {
				return jsonViewResolver.errorJsonResult("未找到该报表编号对应的服务！");
			}
			// 将查询参数封装至bean
			Object paramsBean = paramsClass.newInstance();
			Map<String, String[]> requestParams = request.getParameterMap();
			BeanUtils.populate(paramsBean, requestParams);

			// 实例化报表包装类
			IAsyncReportWrapper wrapper = SpringContextUtil.getBean(wrapperClass);
			// 校验不通过返回给前端错误信息
			wrapper.checkParams(paramsBean);

			List<String> financialContractUuids = getFinancialContractUuidList(requestParams);

			JSONObject reportParams = buildReportParams(requestParams);
			String reportJobUuid = reportJobHandler.createReportJob(reportId, financialContractUuids, reportParams,
					principal.getId());
			// 异步报表写出
			reportJobWriter.writeZipFileToDisk(principal, reportId, reportJobUuid, paramsBean, wrapper);

			SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request),LogFunctionType.EXPORTREPORTJOB,LogOperateType.EXPORT);
			log.setRecordContent("导出报表任务，任务标识【"+ reportId +"】。");
			systemOperateLogService.save(log);
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#createReportJob occur error." + e.getMessage());
			return errorJsonResult(e);
		}
	}

	private List<String> getFinancialContractUuidList(Map<String, String[]> requestParams) {
		String[] values = requestParams.get("financialContractUuids");
		if (ArrayUtils.isNotEmpty(values)) {
			List<String> list = JsonUtils.parseArray(values[0], String.class);
			if (CollectionUtils.isEmpty(list)) {
				return Collections.emptyList();
			}
			return list;
		}
		return null;
	}

	private JSONObject buildReportParams(Map<String, String[]> requestParams)
			throws IllegalAccessException, InvocationTargetException {
		Map<String, String> params = new HashMap<String, String>();
		BeanUtils.populate(params, requestParams);

		JSONObject reportParams = new JSONObject();
		reportParams.put("queryStartDate", params.get("queryStartDate"));
		reportParams.put("queryEndDate", params.get("queryEndDate"));
		return reportParams;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ModelAndView downloadReportJobAttach(@RequestParam(name = "reportJobUuid") String reportJobUuid,
			HttpServletResponse response) {
		try {
			ReportJob reportJob = reportJobService.getByUuid(reportJobUuid);
			if (reportJob == null) {
				return pageViewResolver.errorSpec("所选下载文件不存在！");
			}
			String reportPath = String.format("%s/report", this.uploadPath);
			String filePath = reportPath + reportJob.getFilePath();
			exportDiskFileToClient(response, filePath);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#downloadReportJobAttach occur error." + e.getMessage());
			return pageViewResolver.errorSpec();
		}
	}

}
