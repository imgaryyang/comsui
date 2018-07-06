package com.zufangbao.earth.yunxin.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.remittance.RemittanceController;
import com.zufangbao.earth.yunxin.excel.DailyYunxinPaymentFlowCheckExcel;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.interfacc.payment.model.QueryDeductPlanShowModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.deduct.InterfacePaymentQueryModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.DeductPlanDetailShowModel;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import com.zufangbao.sun.yunxin.handler.DeductPlanCoreOperationHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;

import com.zufangbao.wellsfargo.silverpool.cashauditing.service.ThirdPartyVoucherCommandLogService;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherCommandLogHandler;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogShowModel;

/**
* @author 作者 zhenghangbo
* @version 创建时间：Oct 28, 2016 3:12:53 PM
* 类说明
*/

@RestController
@RequestMapping("interfacePayment")
@MenuSetting("menu-capital")
public class InterfacePaymentController  extends BaseController{

	private static final String YYYY_MM_DD = "yyyy-MM-dd";


	public static  final Log logger =LogFactory.getLog(RemittanceController.class);


	private final static String  DAILY_RETURN_LIST_FILE_HEADER = "接口每日还款清单";

	private final static String  ACCOUNT_CHECK_FILE_HEADER ="接口对账单";

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private DeductPlanCoreOperationHandler  deductPlanHandler;

	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private DailyYunxinPaymentFlowCheckExcel dailyYunxinPaymentFlowCheckExcel;

	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@Autowired
	private ThirdPartyVoucherCommandLogHandler thirdPartyVoucherCommandLogHandler;

	@RequestMapping("/list/options")
	@MenuSetting("submenu-interface-payment")
	public @ResponseBody String getInterfacePaymentOptions(@Secure Principal principal){
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);

			Map<String, Object> resultMap = new HashMap<>();
			for(FinancialContract financialContract : financialContracts){
				List<Integer> ordinals = paymentChannelInformationService.getPaymentInstitutionNameBy(financialContract.getFinancialContractUuid());
				resultMap.put(String.valueOf(financialContract.getUuid()), EnumUtil.getKVListIncludes(PaymentInstitutionName.class,
						ordinals.stream().map(a -> EnumUtil.fromOrdinal(PaymentInstitutionName.class, a)).collect(Collectors.toList())));
			}

			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			result.put("queryAppModels", queryAppModels);
			result.put("orderStatus", EnumUtil.getKVList(DeductApplicationExecutionStatus.class));
			result.put("paymentGateway", EnumUtil.getKVList(PaymentInstitutionName.class));
			result.put("paymentGateways", resultMap);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getInterfacePaymentOptions# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据错误");
		}
	}

	@RequestMapping("/list/query")
	@MenuSetting("submenu-interface-payment")
	public @ResponseBody String queryInterafacePayment(InterfacePaymentQueryModel interfacePaymentQueryModel,Page page) {
		try {
			long start_time = System.currentTimeMillis();

			List<QueryDeductPlanShowModel>  showModels = deductPlanHandler.getInterfacePaymentShowModel(interfacePaymentQueryModel, page);

			long end_time = System.currentTimeMillis();
			logger.info("{queryInterafacePayment}-[getInterfacePaymentShowModel](" + JsonUtils.toJSONString(interfacePaymentQueryModel) + "), use " + (end_time - start_time) + "ms.");

			start_time = System.currentTimeMillis();
			int total = deductPlanHandler.countDeductPlanSize(interfacePaymentQueryModel);
			end_time = System.currentTimeMillis();
			logger.info("{queryInterafacePayment}-[countDeductPlanSize] use " + (end_time - start_time) + "ms.");


			Map<String, Object> data = new HashMap<>();
			data.put("deductApplicationQeuryModel",interfacePaymentQueryModel);
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);

		} catch (Exception e) {
			logger.error("#queryInterafacePayment# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}

	//接口线上支付单--统计金额（代扣金额）
	@RequestMapping("/list/amountStatistics")
	@MenuSetting("submenu-interface-payment")
	public @ResponseBody String amountStatistics(InterfacePaymentQueryModel interfacePaymentQueryModel) {
		try {
			Map<String, Object> data = deductPlanHandler.amountStatistics(interfacePaymentQueryModel);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);

		} catch (Exception e) {
			logger.error("#amountStatistics# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("代扣金额统计错误");
		}
	}


	@RequestMapping("/list/{deductPlanId}/detail")
	@MenuSetting("submenu-interface-payment")
	public @ResponseBody String getInterfacePaymentDetail(@PathVariable(value = "deductPlanId") long deductPlanId,@Secure Principal principal){
		try {
			DeductPlan deductPlan  =  deductPlanService.getDeductPlanById(deductPlanId);
			if(deductPlan == null){
				return jsonViewResolver.errorJsonResult("接口支付单不存在！！！！！");
			}
			logger.info(JsonUtils.toJSONString(deductPlan));
			DeductPlanDetailShowModel detailModel = new DeductPlanDetailShowModel(deductPlan);
			DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductPlan.getDeductApplicationUuid());
			ThirdPartyVoucherCommandLogShowModel  voucherModel = new ThirdPartyVoucherCommandLogShowModel();
			if(deductApplication != null){
				detailModel.setDeudctNo(deductApplication.getDeductId());
				voucherModel = thirdPartyVoucherCommandLogHandler.getThirdPartyVoucherCommandLogShowModel(null, deductApplication.getDeductId());
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("detailModel", detailModel);
			result.put("voucherModel", voucherModel);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#showInterfacePaymentDetail occur error.");
			return jsonViewResolver.errorJsonResult("系统错误！！！");
		}
	}

//	@RequestMapping("/exportDailyList")
//	@MenuSetting("submenu-interface-payment")
	@Deprecated
	public void exportDailyList(@RequestParam("startDateString") String startDateString,@RequestParam("endDateString") String endDateString,
			@RequestParam("financialContractId")long  financialContractId,
			@RequestParam("paymentGateway")int paymentGateway,
			@Secure Principal principal, HttpServletRequest request,
			HttpServletResponse response){
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("11", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();

			Date startDate = getDate(startDateString);
			Date endDate = getDate(endDateString);
			PaymentInstitutionName paymentGatewayEnum = EnumUtil.fromOrdinal(PaymentInstitutionName.class, paymentGateway);
            List<String> csvData = dailyYunxinPaymentFlowCheckExcel.dailyInterfaceReturnListExcel(principal, IpUtil.getIpAddress(request), startDate,
                    endDate, financialContractId, paymentGatewayEnum);

			exportEventLogModel.recordAfterLoadDataComplete(csvData.size());

			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("接口每日还款清单表", csvData);
			String fileName = create_download_name(startDate,endDate, DAILY_RETURN_LIST_FILE_HEADER);
			exportZipToClient(response, fileName, GlobalSpec.UTF_8, csvs);

			exportEventLogModel.recordEndWriteOutTime();
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}

	}

	@RequestMapping("/exportAccountChecking")
	@MenuSetting("submenu-interface-payment")
	public void exportAccountChecking(@RequestParam("startDateString") String startDateString,@RequestParam("endDateString") String endDateString,
			@RequestParam("financialContractId")long financialContractId,
			@RequestParam("paymentGateways")String paymentGateways,
			@Secure Principal principal, HttpServletRequest request,
			HttpServletResponse response){
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("10", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();

			Date startDate = getDate(startDateString);
			Date endDate = getDate(endDateString);
			List<PaymentInstitutionName> paymentGatewayEnums = EnumUtil.fromOrdinals(PaymentInstitutionName.class, paymentGateways);
			FinancialContract financialContract  =  financialContractService.load(FinancialContract.class, financialContractId);
			Map<String,List<String>> csvs = dailyYunxinPaymentFlowCheckExcel.interfaceCheckingCsv(principal, IpUtil.getIpAddress(request), startDate, DateUtils.addDays(endDate, 1), financialContractId, paymentGatewayEnums);
			int account_checking_flow = CollectionUtils.isEmpty(csvs.get("对账流水")) ? 0 : csvs.get("对账流水").size();
			int interface_payment_order_detail = CollectionUtils.isEmpty(csvs.get("接口线上支付单详情")) ? 0 : csvs.get("接口线上支付单详情").size();
			List<Integer> sizes = csvs.values().stream().map(List::size).collect(Collectors.toList());
			exportEventLogModel.recordAfterLoadDataComplete(sizes.toArray(new Integer[csvs.size()]));

			String fileName = create_download_name(startDate, endDate, ACCOUNT_CHECK_FILE_HEADER);
			exportZipToClient(response, fileName, GlobalSpec.UTF_8, csvs);

			exportEventLogModel.recordEndWriteOutTime();

			StringBuffer selectString = extractSelectString(startDateString,endDateString,paymentGatewayEnums, financialContract);
			SystemOperateLog log = new SystemOperateLog(principal.getId(),IpUtil.getIpAddress(request),LogFunctionType.ONLINEBILLEXPORTCHECKING,LogOperateType.EXPORT);
			log.setRecordContent("线上支付单，导出对账单，导出对账流水"+(account_checking_flow-1)+"条、接口线上支付单详情"+(interface_payment_order_detail-1)+"条。筛选条件："+selectString);
			systemOperateLogService.save(log);
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
		}

	private StringBuffer extractSelectString(String startDateString, String endDateString,List<PaymentInstitutionName> paymentGatewayEnums, FinancialContract financialContract) {
		StringBuffer selectString = new StringBuffer();
		if (StringUtils.isNotEmpty(startDateString)) {
			selectString.append("起始时间【" + startDateString + "】");
		}
		if (StringUtils.isNotEmpty(endDateString)) {
			selectString.append("结束时间【" + endDateString + "】");
		}
		if (financialContract != null) {
			selectString.append("，信托合同【" + financialContract.getContractNo() + "】");
		}
		if (CollectionUtils.isNotEmpty(paymentGatewayEnums)) {
			selectString.append("，通道");
			for (PaymentInstitutionName paymentInstitutionName : paymentGatewayEnums) {
				selectString.append("【" + paymentInstitutionName.getChineseMessage() + "】");
			}
		}
		return selectString;
	}

	private String create_download_name(Date startDate,Date endDate, String fileHeader) {
		return fileHeader +"_"+DateUtils.format(startDate, YYYY_MM_DD) +"-"+DateUtils.format(endDate, YYYY_MM_DD)+".zip";
	}

	private Date getDate(String Date) {
		if (StringUtils.isEmpty(Date)
				|| DateUtils.asDay(Date) == null) {
			return  DateUtils.parseDate(DateUtils.today(), YYYY_MM_DD);
		}
		return DateUtils.parseDate(Date, YYYY_MM_DD);
	}


}
