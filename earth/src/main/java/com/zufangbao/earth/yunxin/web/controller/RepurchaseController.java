package com.zufangbao.earth.yunxin.web.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.RepurchaseHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseDocShowModel;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.excel.RepurchaseDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseShowModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/repurchasedoc")
@MenuSetting("menu-finance")
public class RepurchaseController extends BaseController {

	@Autowired
	private RepurchaseHandler repurchaseHandler;

	@Autowired
	private RepurchaseService repurchaseService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private PrincipalService principalService;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private VoucherHandler voucherHandler;

	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;

	@Autowired
	private VoucherService voucherService;

	private static final Log logger = LogFactory.getLog(RepurchaseController.class);

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public String getRepurchaseListPageOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			List<App> appList = principalService.get_can_access_app_list(principal);
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("appList", appList);
			result.put("repoStatus", EnumUtil.getKVList(RepurchaseStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("##getRepurchaseListPageOptions## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据异常");
		}
	}

	// 回购单列表页 -- 查询操作
	@RequestMapping("/query")
	public String queryRepurchase(@ModelAttribute RepurchaseQueryModel queryModel, Page page) {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			List<RepurchaseShowModel> showModels = repurchaseHandler.query(queryModel, page);
			data.put("list", showModels);
			data.put("size", repurchaseService.count(queryModel));
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##queryRepurchase## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询失败，请稍后重试");
		}
	}

	// 回购单列表页 -- 统计金额（回购金额）
	@RequestMapping("/amountStatistics")
	public String amountStatistics(@ModelAttribute RepurchaseQueryModel queryModel) {
		try {
			Map<String, Object> data = repurchaseService.amountStatistics(queryModel);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##amountStatistics## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("回购金额统计错误");
		}
	}

	// 回购单列表页 -- 违约操作
	@RequestMapping("/default")
	public String defaultRepurchaseDoc(@RequestParam(value = "rduid") String repurchaseDocUuid,
			HttpServletRequest request, @Secure Principal principal) {
		try {
            String errMsg = repurchaseHandler.repurchaseDefault(repurchaseDocUuid, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(errMsg)) {
				return jsonViewResolver.errorJsonResult(errMsg);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (RuntimeException re) {
			logger.error("##defaultRepurchaseDoc## occur error.");
			re.printStackTrace();
			return jsonViewResolver.errorJsonResult("违约操作失败，请稍后重试");
		} catch (Exception e) {
			logger.error("##defaultRepurchaseDoc## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("违约操作失败，请稍后重试");
		}
	}

	// 回购单列表页-- 导出操作
//	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@Deprecated
	public String exportRepaymentPlanDetail(HttpServletRequest request, @Secure Principal principal,
			@ModelAttribute RepurchaseQueryModel queryModel, HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("8", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			List<RepurchaseShowModel> showModels = repurchaseHandler.query(queryModel, null);
			exportEventLogModel.recordAfterLoadDataComplete(showModels.size());
			ExcelUtil<RepurchaseShowModel> excelUtil = new ExcelUtil<RepurchaseShowModel>(RepurchaseShowModel.class);
			List<String> csvData = excelUtil.exportDatasToCSV(showModels);
			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("回购信息表", csvData);
			exportZipToClient(response, create_repurchase_detail_file_name("zip"), GlobalSpec.UTF_8, csvs);
			SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                    IpUtil.getIpAddress(request), null, LogFunctionType.REPURCHASEDOCEXPORT, LogOperateType.EXPORT,
                    null, null, null, Collections.emptyList());
			systemOperateLogHandler.generateSystemOperateLog(param);

			exportEventLogModel.recordEndWriteOutTime();
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#exportRepurchasePlanDetail# occur error.");
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
			return jsonViewResolver.errorJsonResult("导出失败");
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
	}

	// 回购汇总表-- 导出预览
	@RequestMapping(value = "/preview-export", method = RequestMethod.GET)
	public String previewExportRepaymentPlanDetail(HttpServletRequest request,
			@Secure Principal principal, @ModelAttribute RepurchaseQueryModel queryModel,
			HttpServletResponse response) {
		try {
			Page page = new Page(0, 10);
			List<RepurchaseDetailExcelVO> repurchaseDetailExcelVOs = repurchaseHandler.previewRepurchaseDetail(queryModel, page);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", repurchaseDetailExcelVOs);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#previewExportRepaymentPlanDetail# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("回购汇总表导出预览失败");
		}
	}

	// 回购单 -- 详情页面
	@RequestMapping("/detail")
	public String getRepurchaseDocDetail(@RequestParam(value = "rduid") String repurchaseDocUuid) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
			if (repurchaseDoc == null) {
				return jsonViewResolver.errorJsonResult("请求参数错误");
			}
			Contract contract = contractService.getContract(repurchaseDoc.getContractId());
			if (contract == null) {
				return jsonViewResolver.errorJsonResult("系统数据错误，请联系管理员");
			}
			RepurchaseDocShowModel model = new RepurchaseDocShowModel(repurchaseDoc, contract);
			result.put("repurchaseDoc", model);
			return jsonViewResolver.sucJsonResult(result, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##getRepurchaseDocDetail## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误，请稍后重试");
		}
	}

	@RequestMapping("/voucher")
	public String getRepurchaseDocVoucherDetail(@RequestParam(value = "rduid") String repurchaseDocUuid,Page page) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
			if (repurchaseDoc == null) {
				return jsonViewResolver.errorJsonResult("请求参数错误");
			}
			List<Voucher> voucherList = new ArrayList<Voucher>();

	        List<String> voucherUuidList = sourceDocumentDetailService.getSourceDocumentDetailByRepaymentPlanNo(repurchaseDoc.getRepurchaseDocUuid());

	        voucherList = voucherService.getVoucherList(voucherUuidList, page);
	        int size = voucherService.getVoucherListSum(voucherUuidList);

			result.put("voucherList", voucherList);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##getRepurchaseDocDetail## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误，请稍后重试");
		}
	}


	// 回购单详情页 -- 激活操作
	@RequestMapping("/activate")
	public String activateRepurchaseDoc(@RequestParam(value = "rduid") String repurchaseDocUuid,
			HttpServletRequest request, @Secure Principal principal) {
		try {
			String errMsg = repurchaseHandler.activateRepurchaseDoc(repurchaseDocUuid, principal.getId(),
                    IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(errMsg)) {
				return jsonViewResolver.errorJsonResult(errMsg);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("##activateRepurchaseDoc## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("操作失败，请稍后重试");
		}
	}

	// 回购单详情页 -- 作废操作
	@RequestMapping("/nullify")
	public String nullifyRepurchaseDoc(@RequestParam(value = "rduid") String repurchaseDocUuid,
			HttpServletRequest request, @Secure Principal principal) {
		try {

			RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
	        if (repurchaseDoc == null) {
	            return jsonViewResolver.errorJsonResult("请求参数错误");
	        }
	        if (!repurchaseHandler.canNullifyRepurchaseDoc(repurchaseDocUuid)) {
	        	 return jsonViewResolver.errorJsonResult("该回购单处于不可作废状态");
	        }
	        Contract contract = contractService.getContract(repurchaseDoc.getContractId());
	        if (contract == null) {
	            return jsonViewResolver.errorJsonResult("该回购单对应的贷款合同不存在");
	        }

	        FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());

            repurchaseHandler.nullifyRepurchaseDoc(repurchaseDoc, contract, financialContract, principal.getId(), IpUtil.getIpAddress(request));
            return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("##nullifyRepurchaseDoc## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("操作失败，请稍后重试");
		}
	}

	private String create_repurchase_detail_file_name(String format) {
		return "回购信息表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + "." + format;
	}

	@RequestMapping("/can-nullify")
	public String canNullifyRepurchaseDoc(@RequestParam(value = "rduid") String repurchaseDocUuid,
			HttpServletRequest request, @Secure Principal principal){
			try {
				boolean repurchaseDoc = repurchaseHandler.canNullifyRepurchaseDoc(repurchaseDocUuid);
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("canNullify", repurchaseDoc);
				return jsonViewResolver.sucJsonResult(result);
			} catch (Exception e) {
				logger.error("##nullifyRepurchaseDoc## occur error.");
				e.printStackTrace();
			return jsonViewResolver.errorJsonResult("操作失败，请稍后重试");
			}
	}
}
