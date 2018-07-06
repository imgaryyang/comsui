package com.zufangbao.earth.web.controller.financial;

import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.PARAMS_MER_ID;
import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.PARAMS_SECRET;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.StringUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.coffer.entity.BusinessProcessStatus;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.zufangbao.earth.api.GZUnionPayApiHandler;
import com.zufangbao.earth.api.TransactionDetailConstant.FunctionName;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.handler.RemittanceProxyHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.CapitalHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.earth.yunxin.handler.remittance.RemittancePlanExecLogHandler;
import com.zufangbao.earth.yunxin.handler.remittance.RemittanceRefundBillHandler;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.BeanWrapperUtil;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowType;
import com.zufangbao.sun.entity.directbank.business.FlowRecord;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.AccountService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.FlowService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.entity.model.DirectbankBalanceQueryModel;
import com.zufangbao.sun.yunxin.entity.model.DirectbankBalanceQueryResult;
import com.zufangbao.sun.yunxin.entity.model.DirectbankCashFlowQueryModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.remittance.CashFlowExportModel;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogExportModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogShowModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBill;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBillQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBillShowModel;
import com.zufangbao.sun.yunxin.entity.remittance.ReverseStatus;
import com.zufangbao.sun.yunxin.entity.remittance.ReverseType;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceRefundBillService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;

/**
 * @author zjm
 */

@Controller("flowController")
@RequestMapping("/capital")
@MenuSetting("menu-capital")
public class FlowController extends BaseController {

	private static final String SIGN_FAIL_TEMPLATE = "<?xml version='1.0' encoding='UTF-8'?><SDKPGK><INFO><FUNNAM>%s</FUNNAM><RETCOD>-9</RETCOD><ERRMSG>验签失败</ERRMSG></INFO></SDKPGK>";
	private static final String OTHER_FAIL_TEMPLATE = "<?xml version='1.0' encoding='UTF-8'?><SDKPGK><INFO><FUNNAM>%s</FUNNAM><RETCOD>-9</RETCOD><ERRMSG>其他错误</ERRMSG></INFO></SDKPGK>";
	private static final String ERR_MER_INFO_TEMPLATE = "<?xml version='1.0' encoding='UTF-8'?><SDKPGK><INFO><FUNNAM>%s</FUNNAM><RETCOD>-9</RETCOD><ERRMSG>商户信息配置错误，缺少商户号MerId或secret！</ERRMSG></INFO></SDKPGK>";
	private static final Log logger = LogFactory.getLog(FlowController.class);
    @Autowired
    IRemittancePlanService iRemittancePlanService;
    @Autowired
    IRemittanceApplicationDetailService iRemittanceApplicationDetailService;
    @Autowired
    IRemittanceApplicationService iRemittanceApplicationService;
    @Autowired
    CashFlowService cashFlowService;
	@Autowired
	private FlowService flowService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private GZUnionPayApiHandler gzUnionPayApiHandler;
	@Autowired
	private JpmorganApiHelper jpmorganApiHelper;
	@Autowired
	private TMerConfigService tMerConfigService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;
	@Autowired
	private RemittancePlanExecLogHandler remittancePlanExecLogHandler;
	@Autowired
	private  FinancialContractService financialContractService;
	@Autowired
	private CashFlowHandler cashFlowHandler;
	@Autowired
	private RemittanceRefundBillHandler remittanceRefundBillHandler;
	@Autowired
	private IRemittanceRefundBillService remittanceRefundBillService;
	@Autowired
	private CapitalHandler capitalHandler;
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;

	@RequestMapping("/directbank-cash-flow/options")
	@MenuSetting("submenu-flow-monitor")
	public @ResponseBody String monitorHistory(@Secure Principal principal,@ModelAttribute DirectbankCashFlowQueryModel directbankCashFlowQueryModel) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			Date today = new Date();
			directbankCashFlowQueryModel.setStartDate(today);
			directbankCashFlowQueryModel.setEndDate(today);
			
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("directbankCashFlowQueryModel", directbankCashFlowQueryModel);
			return jsonViewResolver.sucJsonResult(result, SerializerFeature.DisableCircularReferenceDetect);
		} catch(Exception e){
			logger.error("monitorHistory eccor error");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}

	}
	
	@RequestMapping(value = "balance", method = RequestMethod.GET)
	public @ResponseBody String queryAccountBalance(@Secure Principal principal,@ModelAttribute DirectbankBalanceQueryModel directbankBalanceQueryModel) {
		DirectbankBalanceQueryResult directbankBalanceQueryResult = new DirectbankBalanceQueryResult();

		try {
			FinancialContract financialContract = financialContractService.load(FinancialContract.class, directbankBalanceQueryModel.getFinanceContractId());
			
			directbankBalanceQueryResult.fillFinancialContractInfo(financialContract);
			
//			Account capitalAccount = financialContract.getCapitalAccount();
			Account account = accountService.load(Account.class, directbankBalanceQueryModel.getAccountId());
			
			directbankBalanceQueryResult.fillAccountInfo(account);
			
			directbankBalanceQueryResult.setQueryTime(new Date());
			
			BigDecimal accountBalance = capitalHandler.queryAccountBalance(account);
			directbankBalanceQueryResult.setAccountBalance(accountBalance);
			return jsonViewResolver.sucJsonResult("balanceQueryResult", directbankBalanceQueryResult);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询失败！");
		}
	}

	@RequestMapping(value = "directbank-cash-flow/search", method = RequestMethod.GET)
	@MenuSetting("submenu-flow-monitor")
	public @ResponseBody String flowHistorySearch(@Secure Principal principal, @ModelAttribute DirectbankCashFlowQueryModel directbankCashFlowQueryModel, Page page) {
		try {
			if(!directbankCashFlowQueryModel.isDateValid()){
				return jsonViewResolver.errorJsonResult("请输入日期");
			}
			if(directbankCashFlowQueryModel.getAccountId()==null){
				return jsonViewResolver.errorJsonResult("请选择账户");
			}
			
			Account account = accountService.load(Account.class, directbankCashFlowQueryModel.getAccountId());
			
			List<FlowRecord> records = new ArrayList<FlowRecord>();

			long start_time = System.currentTimeMillis();

			records = flowService.getHistoryFlowsByAccountFromDB(account, directbankCashFlowQueryModel, null);

			long end_time = System.currentTimeMillis();
			logger.info("{flowHistorySearch}-[getHistoryFlowsByAccountFromDB] ," + JsonUtils.toJSONString(directbankCashFlowQueryModel) + " use " + (end_time - start_time) + "ms.");

			Map<String,Object> result = new HashMap<String, Object>();
			result.put("flow_result",records);
			result.put("size",records.size());
			cal_total_borrowings(records, result);
			return jsonViewResolver.sucJsonResult(result, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch(Exception e){
			logger.error("flowHistorySearch eccor error");
			e.printStackTrace();
		}
		return jsonViewResolver.errorJsonResult("系统错误");
	}
	
	private Map<String,Object> cal_total_borrowings(List<FlowRecord> flowList,Map<String,Object> result) {
		BigDecimal debitSum = BigDecimal.ZERO;
		BigDecimal creditSum = BigDecimal.ZERO;
		if(flowList != null){
			for (FlowRecord flowRecord : flowList) {
				debitSum = debitSum.add(flowRecord.getCreditAmount() == null ? BigDecimal.ZERO: flowRecord.getCreditAmount());
				creditSum = creditSum.add(flowRecord.getDebitAmount() == null ? BigDecimal.ZERO: flowRecord.getDebitAmount());
			}
		}
		result.put("debitSum", debitSum);
		result.put("creditSum", creditSum);
		return result;
	}

	public List<FlowRecord> getFilteredResult(String accountSide, String recipAccNo, String recipName, String summary, List<FlowRecord> records) {
		if( null == records){
			return new ArrayList<FlowRecord>();
		}
		Comparator<FlowRecord> sortByTimeDesc = (FlowRecord a,FlowRecord b)->a==null||b==null||a.getTime()==null||b.getTime()==null?0:b.getTime().compareTo(a.getTime());
		return	records.stream().filter(matchRecipName(recipName)).
		                      filter(matchRecipAccNo(recipAccNo)).
		                      filter(matchSummary(summary)).
		                      filter(matchAccountSide(accountSide)).
		                      sorted(sortByTimeDesc).
		                      collect(Collectors.toList());
	}

	private Predicate<? super FlowRecord> matchAccountSide(String accountSide) {
		if(StringUtils.isEmpty(accountSide)){
			return flowRecord->true;
		}
		return flowRecord->!StringUtils.isEmpty(flowRecord.getDrcrf())&&flowRecord.getDrcrf().equals(accountSide);

	}

	private Predicate<? super FlowRecord> matchRecipName(String filter_recipName) {
		if(StringUtils.isEmpty(filter_recipName)){
			return flowRecord->true;
		}
		return flowRecord->!StringUtils.isEmpty(flowRecord.getRecipName())&&flowRecord.getRecipName().contains(filter_recipName);

	}

	private Predicate<? super FlowRecord> matchRecipAccNo(String filter_recipAccNo) {
		if(StringUtils.isEmpty(filter_recipAccNo)){
			return flowRecord->true;
		}
		

		return flowRecord->!StringUtils.isEmpty(flowRecord.getRecipAccNo())&&flowRecord.getRecipAccNo().contains(filter_recipAccNo);

	}

	private Predicate<? super FlowRecord> matchSummary(String summary) {
		if(StringUtils.isEmpty(summary)){
			return flowRecord->true;
		}
		return flowRecord->!StringUtils.isEmpty(flowRecord.getSummary())&&flowRecord.getSummary().contains(summary);
	}
	
	@RequestMapping(value = "transaction-detail")
	public @ResponseBody String queryTransactionDetail(@RequestBody String reqXmlPacket
			, HttpServletRequest request, HttpServletResponse response){
		logger.info("capital: query transaction detail begin");
		logger.info("reqXmlPacket:"+reqXmlPacket);
		
		try {
			verifySign(reqXmlPacket, request);
			String rtnXmlPacket = gzUnionPayApiHandler.queryTransactionDetail(reqXmlPacket);
			logger.info("capital: query transaction detail end");
			return getPriKeyAndSign(response, rtnXmlPacket);
		} catch (RuntimeException e) { 
			e.printStackTrace();
			return processRuntimeException(e, FunctionName.FUNC_TRANSACTION_DETAIL_QUERY);
		} catch (Exception e) {
			e.printStackTrace();
			return otherFailPacket(FunctionName.FUNC_TRANSACTION_DETAIL_QUERY);
		}
	}

	private String processRuntimeException(RuntimeException e, String failedPocket) {
		if( e instanceof ApiException ) { 
			int error_code = ((ApiException) e).getCode();
			switch (error_code) {
			case ApiResponseCode.SIGN_MER_CONFIG_ERROR:
				return errorMerInfoPacket(failedPocket);
			case ApiResponseCode.SIGN_VERIFY_FAIL:
				return verifySignFailedPacket(failedPocket);
			default:
				return otherFailPacket(failedPocket);
			}
		}
		return otherFailPacket(failedPocket);
	}
	
	private String otherFailPacket(String failedPocket) {
		return String.format(OTHER_FAIL_TEMPLATE, failedPocket);
	}
	
	private void verifySign(String reqXmlPacket, HttpServletRequest request) {
		String merId = request.getHeader(PARAMS_MER_ID);
		String secret = request.getHeader(PARAMS_SECRET);
		vaildateHeader(merId, secret);
		TMerConfig merConfig = tMerConfigService.getTMerConfig(merId, secret);
		if (!ApiSignUtils.verifySign(reqXmlPacket, request, merConfig)) {
			throw new ApiException(ApiResponseCode.SIGN_VERIFY_FAIL);
		}
	}
	
	private void vaildateHeader(String merId, String secret) {
		if (StringUtils.isEmpty(merId) || StringUtils.isEmpty(secret)) {
			throw new ApiException(ApiResponseCode.SIGN_MER_CONFIG_ERROR);
		}
	}
	
	@RequestMapping(value = "batch-payment")
	public @ResponseBody String execBatchPayment(@RequestBody String reqXmlPacket
			, HttpServletRequest request, HttpServletResponse response){
		logger.info("capital: execute batch payment begin");
		logger.info("reqXmlPacket:"+reqXmlPacket);
		try {
			verifySign(reqXmlPacket, request);
			String rtnXmlPacket = gzUnionPayApiHandler.executeBatchPayment(reqXmlPacket);
			logger.info("capital: execute batch payment end");
			return getPriKeyAndSign(response, rtnXmlPacket);
		} catch (RuntimeException e) { 
			return processRuntimeException(e, FunctionName.FUNC_BATCH_PAYMENT);
		} catch (Exception e) {
			return otherFailPacket(FunctionName.FUNC_TRANSACTION_DETAIL_QUERY);
		}
	}

	@RequestMapping(value = "query-transaction")
	public @ResponseBody String execQueryTransaction(@RequestBody String reqXmlPacket
			, HttpServletRequest request, HttpServletResponse response){
		logger.info("capital: execute query transaction begin");
		logger.info("reqXmlPacket:"+reqXmlPacket);
		
		try {
			verifySign(reqXmlPacket, request);
			String rtnXmlPacket = gzUnionPayApiHandler.execTransactionResultQuery(reqXmlPacket);
			logger.info("capital: execute query transaction end");
			return getPriKeyAndSign(response, rtnXmlPacket);
		} catch (RuntimeException e) { 
			e.printStackTrace();
			return processRuntimeException(e, FunctionName.FUNC_TRANSACTION_STATUS_QUERY);
		} catch (Exception e) {
			e.printStackTrace();
			return otherFailPacket(FunctionName.FUNC_TRANSACTION_STATUS_QUERY);
		}
	}

	private String getPriKeyAndSign(HttpServletResponse response, String result) {
		String privateKey = dictionaryService.getPlatformPrivateKey();
		return ApiSignUtils.sign_and_return_result(response, result, privateKey);
	}
	
	private String verifySignFailedPacket(String functionName) {
		
		return String.format(SIGN_FAIL_TEMPLATE, functionName);
	}
	
	private String errorMerInfoPacket(String functionName) {
		
		return String.format(ERR_MER_INFO_TEMPLATE, functionName);
	}
	
	
	// 线上代付单---列表页面
	@RequestMapping(value = "/plan/execlog/options")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String getRemittancePlanExecLogPageOptions(@Secure Principal principal) {
		try {
			Map<String, String> executionStatus = ExecutionStatus.getExecutionStatusMap();
			Map<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			result.put("queryAppModels", queryAppModels);			
			result.put("remittanceChannel", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
//			result.put("remittanceChannel", EnumUtil.getKVList(PaymentInstitutionName.class));
			// modelAndView.addObject("executionStatus", Arrays.asList(ExecutionStatus.values()));
			result.put("executionStatus", executionStatus);
			result.put("reverseStatus", EnumUtil.getKVList(ReverseStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getRemittancePlanExecLogPageOptions# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据错误");
		}
	}
	
	// 线上代付单---列表查询
	@RequestMapping(value = "/plan/execlog/query")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String queryRemittancePlanExecLog(@ModelAttribute RemittancePlanExecLogQueryModel queryModel,Page page) {
		try {
			long start_time = System.currentTimeMillis();
			List<RemittancePlanExecLogShowModel> showModels = remittancePlanExecLogHandler.queryShowModelList(queryModel, page);
			long end_time = System.currentTimeMillis();
			logger.info("{queryRemittancePlanExecLog}-[queryShowModelList] 线上代付单---列表查询," + JsonUtils.toJSONString(queryModel) + " use " + (end_time - start_time)
					+ "ms.");

			start_time = System.currentTimeMillis();
			int total = remittancePlanExecLogService.queryRemittacePlanExecLogCount(queryModel);
			end_time = System.currentTimeMillis();
			logger.info("{queryRemittancePlanExecLog}-[queryRemittacePlanExecLogCount] 线上代付单---列表查询 count," + JsonUtils.toJSONString(queryModel) + " use " + (end_time - start_time) + "ms.");

			Map<String, Object> data = new HashMap<>();
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#queryRemittancePlanExecLog#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
	
	// 线上代付单---统计金额(放款金额)
	@RequestMapping(value = "/plan/execlog/amountStatistics")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String execlogAmountStatistics(@ModelAttribute RemittancePlanExecLogQueryModel queryModel) {
		try {
			Map<String, Object> data = remittancePlanExecLogService.amountStatistics(queryModel);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#execlogAmountStatistics#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("放款金额统计错误");
		}
	}
	
	// 线上代付单---统计数量前5的收款方银行
	@RequestMapping(value = "/plan/execlog/dataStatistics")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String dataStatistics(@ModelAttribute RemittancePlanExecLogQueryModel queryModel) {
		try {
			Map<String,Object> data = remittancePlanExecLogHandler.dataStatistics(queryModel, 5);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("dataMap", data);
			result.put("size", data.size());
			return jsonViewResolver.sucJsonResult(result,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#dataStatistics#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("统计数据错误");
		}
	}
	
	// 线上代付单---详情页面
	@RequestMapping(value="/plan/execlog/details/{execReqNo}")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String getRemittancePlanExecLogDetails(@PathVariable(value="execReqNo") String execReqNo){
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			RemittancePlanExecLog remittancePlanExecLog = remittancePlanExecLogService.getRemittancePlanExecLogBy(execReqNo);
			List<RemittanceRefundBill> refundBills = remittanceRefundBillService.listRemittanceRefundBill(execReqNo);
			if (CollectionUtils.isEmpty(refundBills)){
				RemittanceRefundBill remittanceRefundBill = remittanceRefundBillService.getRemittanceRefundBillBy(remittancePlanExecLog.getRemittanceRefundBillUuid());
				if (remittanceRefundBill != null){
					refundBills.add(remittanceRefundBill);
				}
			}
			String remittancePlanUuid = remittancePlanExecLog.getRemittancePlanUuid();
			String remittanceApplicationUuid = remittancePlanExecLog.getRemittanceApplicationUuid();
			RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
			RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
			Map<String, Object>rtnRemittanceApplication = BeanWrapperUtil.wrapperMap(remittanceApplication, "auditorName", "auditTime", "executionStatus","requestNo");
			result.put("remittancePlanExecLog", remittancePlanExecLog);
			result.put("remittanceRefundBills", refundBills);
			result.put("remittancePlan", remittancePlan);
			result.put("remittanceApplication", rtnRemittanceApplication);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getRemittancePlanExecLogDetails# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取详情数据错误");
		}
	}
	
	// 线上代付单详情页面--修改冲账状态
	@RequestMapping(value = "/plan/execlog/editReverseStatus")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String editReverseStatus(@RequestParam(value = "execReqNo") String execReqNo,
			@RequestParam(value = "reverseStatus", required = false, defaultValue = "-1") int reverseStatus,
			@RequestParam(value = "executionRemark") String executionRemark) {
		try {
			if (StringUtils.isEmpty(execReqNo) || reverseStatus == -1 || StringUtils.isEmpty(executionRemark)) {
				return jsonViewResolver.errorJsonResult("参数不能为空");
			}
			RemittancePlanExecLog planExecLog = remittancePlanExecLogService.getRemittancePlanExecLogBy(execReqNo);
			if (planExecLog == null) {
				return jsonViewResolver.errorJsonResult("未找到对应的代付单");
			}

			if (reverseStatus == 2 && planExecLog.getReverseStatus() != ReverseStatus.NOTREVERSE) {// 只允许未冲账改为已冲账
				return jsonViewResolver.errorJsonResult("只允许未冲账改为已冲账");
			}
			// 冲账状态未冲账修改为已冲账时，根据贷记流水反向生成借记流水
			if (reverseStatus == 2 && planExecLog.getReverseStatus() == ReverseStatus.NOTREVERSE) {
				CashFlow creditCashFlow = cashFlowService
						.getCashFlowByCashFlowUuid(planExecLog.getCreditCashFlowUuid());
				if (creditCashFlow == null) {
					return jsonViewResolver.errorJsonResult("未找到对应的贷记流水");
				}
				CashFlow debitCashFlow = new CashFlow(UUID.randomUUID().toString(),
						creditCashFlow.getCashFlowChannelType(), creditCashFlow.getCompanyUuid(),
						creditCashFlow.getHostAccountUuid(), creditCashFlow.getHostAccountNo(),
						creditCashFlow.getHostAccountName(), creditCashFlow.getCounterAccountNo(),
						creditCashFlow.getCounterAccountName(), creditCashFlow.getCounterAccountAppendix(),
						creditCashFlow.getCounterBankInfo(), AccountSide.DEBIT, creditCashFlow.getTransactionTime(),
						creditCashFlow.getTransactionAmount(), creditCashFlow.getBalance(),
						creditCashFlow.getTransactionVoucherNo(), UUID.randomUUID().toString(), "冲账借记流水", null,
						creditCashFlow.getStrikeBalanceStatus(), CashFlowType.Own, creditCashFlow.getTradeUuid());
				cashFlowService.save(debitCashFlow);
				planExecLog.setDebitCashFlowUuid(debitCashFlow.getCashFlowUuid());
				RemittanceRefundBill refundBill = new RemittanceRefundBill(debitCashFlow, planExecLog,
						planExecLog.getCreditCashFlowUuid());
				refundBill.setReverseType(ReverseType.REVERSE);
				remittanceRefundBillService.save(refundBill);

				planExecLog.setRemittanceRefundBillUuid(refundBill.getRemittanceRefundBillUuid());
			}

			planExecLog.setReverseStatus(EnumUtil.fromOrdinal(ReverseStatus.class, reverseStatus));
			planExecLog.setLastModifiedTime(new Date());
			planExecLog.setExecutionRemark(executionRemark);

			remittancePlanExecLogService.update(planExecLog);
			return jsonViewResolver.jsonResult("修改成功");
		} catch (Exception e) {
			logger.error("#editReverseStatus# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("修改冲账状态错误");
		}
	}
	
	//线上代付单详情页面--重新核单
	@RequestMapping(value="/plan/execlog/checkExecutionStatus/{execReqNo}")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String checkOppositeExecutionStatus(@Secure Principal principal, @PathVariable("execReqNo") String execReqNo, HttpServletRequest request){
		try {
			RemittancePlanExecLog planExecLog = remittancePlanExecLogService.getRemittancePlanExecLogBy(execReqNo);
			if (planExecLog == null) {
				return jsonViewResolver.errorJsonResult("未找到对应的代付单");
			}
			String transactionVoucherNo = planExecLog.getExecRspNo();
			Result result = jpmorganApiHelper.queryOppositeStatus(planExecLog.getPaymentChannelUuid(), transactionVoucherNo);
			if (!result.isValid()) {
				String resultStr = JsonUtils.toJsonString(result);
				logger.info("#对端放款核单，通讯失败,代付单号:" + execReqNo + "结果:" + resultStr);
				return jsonViewResolver.errorJsonResult("通讯失败");
			}

			String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
			QueryCreditResult queryCreditResult = null;
			Result responseResult = JsonUtils.parse(responseStr, Result.class);
			if (responseResult != null && responseResult.isValid()) {
				String queryResultStr = String.valueOf(responseResult.get("queryResult"));
				queryCreditResult = JSON.parseObject(queryResultStr, QueryCreditResult.class);
			}
			
			if (queryCreditResult == null) {
				logger.info("#对端放款核单，结果解析失败［" + responseStr + "］");
				return jsonViewResolver.errorJsonResult("结果解析失败");
			}
			BusinessProcessStatus processStatus = queryCreditResult.getProcessStatus();
			if (processStatus == null  || !transactionVoucherNo.equals(queryCreditResult.getTransactionVoucherNo())) {
				logger.info("#对端放款核单，交易状态查询失败");
				return jsonViewResolver.errorJsonResult("查询失败");
			}
			ExecutionStatus executionStatus = processStatus.equals(BusinessProcessStatus.SUCCESS) ? ExecutionStatus.SUCCESS
					: ExecutionStatus.FAIL;
			if (executionStatus == planExecLog.getExecutionStatus()) {
				logger.info("#对端放款核单,交易状态对端和本端一致"+"["+"对端交易状态为:"+executionStatus+"]");
				return jsonViewResolver.jsonResult("核单成功");
			}
			
			RemittancePlanExecLog oldPlanExecLog = planExecLog;
			//更新代付单执行状态
			if (executionStatus == ExecutionStatus.SUCCESS) {// 核单状态为成功。修改冲账状态为未发生
				planExecLog.setReverseStatus(ReverseStatus.UNOCCUR);
			}
			planExecLog.setExecutionStatus(executionStatus);
			remittancePlanExecLogService.update(planExecLog);
			
			//String isUpdate = remittancePlanExecLogHandler.updateRemittanceInfo(planExecLog.getRemittancePlanUuid());
			String isUpdate = remittanceProxyHandler.processingRevokeUpdateRemittanceInfo(planExecLog.getRemittancePlanUuid());if (!"放款信息更新成功".equals(isUpdate)) {
				return jsonViewResolver.errorJsonResult(isUpdate);
			}
			 SystemOperateLogRequestParam param = getSystemOperateCheckOppositeExecutionStatus(principal, request, planExecLog, oldPlanExecLog);
	         systemOperateLogHandler.generateSystemOperateLog(param);

	         return jsonViewResolver.jsonResult("核单成功");
		} catch (Exception e) {
			logger.error("#checkExecutionStatus# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("重新核单错误");
		}

	}

	//线上代付单详情页面--添加流水--查询流水
	@RequestMapping(value="/plan/execlog/searchCashFlow")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String searchCashFlow(@RequestParam(value="bankSequenceNo",required = false) String  bankSequenceNo,
			@RequestParam(value="hostAccountNo",required = false) String  hostAccountNo,
			@RequestParam(value="hostAccountName",required = false) String hostAccountName,
			@RequestParam(value="remark",required = false) String remark){
		try {
			List<CashFlow> list = cashFlowService.getRefundCashFlowList(bankSequenceNo, hostAccountNo, hostAccountName, remark);
			return jsonViewResolver.sucJsonResult("list", list, SerializerFeature.WriteNullListAsEmpty);
		} catch (Exception e) {
			logger.error("#searchCashFlow# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询流水错误");
		}

	}

	// 线上代付单详情页面--添加流水--提交
	@RequestMapping(value = "/plan/execlog/addCashFlow")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String attachCashFlow(@RequestParam(value = "execReqNo") String execReqNo,
			@RequestParam(value = "cashFlowUuids") String cashFlowUuids,
			@RequestParam(value = "executionRemark") String executionRemark) {
		try {
			RemittancePlanExecLog rpel = remittancePlanExecLogService.getRemittancePlanExecLogBy(execReqNo);
			if (rpel == null) {
				return jsonViewResolver.errorJsonResult("未找到对应的代付单");
			}
			if (rpel.getReverseStatus() != ReverseStatus.UNOCCUR) {
				return jsonViewResolver.errorJsonResult("只允许未发生改为已退票");
			}
			List<String> debitCashFlowUuids = JsonUtils.parseArray(cashFlowUuids, String.class);

			List<CashFlow> debitCashFlows = cashFlowService.getCashFlowListByCashFlowUuids(debitCashFlowUuids);
			if (CollectionUtils.isEmpty(debitCashFlows)){
				return jsonViewResolver.errorJsonResult("未找到添加的借记流水");
			}
			BigDecimal totalTransactionAmount = debitCashFlows.stream().map(CashFlow :: getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (!AmountUtils.equals(totalTransactionAmount, rpel.getPlannedAmount())) {
				return jsonViewResolver.errorJsonResult("流水金额与代付金额不等");
			}

			for (CashFlow debitCashFlow: debitCashFlows ) {
				if (debitCashFlow == null || debitCashFlow.getAccountSide() != AccountSide.DEBIT) {
					return jsonViewResolver.errorJsonResult("未找到添加的借记流水");
				}

				RemittanceRefundBill refundBill = new RemittanceRefundBill(debitCashFlow, rpel,
						rpel.getCreditCashFlowUuid());
				refundBill.setReverseType(ReverseType.REFUND);
				remittanceRefundBillService.save(refundBill);

				rpel.setRemittanceRefundBillUuid(refundBill.getRemittanceRefundBillUuid());
			}

			rpel.setDebitCashFlowUuid(debitCashFlowUuids.get(0));
			rpel.setReverseStatus(ReverseStatus.REFUND);
			rpel.setExecutionRemark(executionRemark);
			rpel.setLastModifiedTime(new Date());

			remittancePlanExecLogService.update(rpel);

			String isUpdate = remittanceProxyHandler.processingRevokeUpdateRemittanceInfo(rpel.getRemittancePlanUuid());
			if (!"放款信息更新成功".equals(isUpdate)) {
				return isUpdate;
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#attachCashFlow# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("退票错误");
		}

	}

	// 线上代付单详情页面--合并退票--提交
	@RequestMapping(value = "/plan/execlog/refundCombine")
	@MenuSetting("submenu-remittance-plan-execlog")
	public @ResponseBody String refundCombine(@RequestParam(value = "execReqNos") String execReqNos,
											  @RequestParam(value = "cashFlowUuid") String cashFlowUuid,
											  @RequestParam(value = "executionRemark") String executionRemark){
		try {

			List<RemittancePlanExecLog> rpls = new ArrayList<>();
			List<String> execReqNoList = JSON.parseArray(execReqNos, String.class);
			if (CollectionUtils.isEmpty(execReqNoList)){
				return jsonViewResolver.errorJsonResult("未找到对应的线上代付单");
			}
			BigDecimal totalAmount = BigDecimal.ZERO;
			for (String execReqNo: execReqNoList) {
				RemittancePlanExecLog rpel = remittancePlanExecLogService.getRemittancePlanExecLogBy(execReqNo);
				if (rpel != null && rpel.getReverseStatus() == ReverseStatus.UNOCCUR && rpel.getExecutionStatus() == ExecutionStatus.SUCCESS){
					totalAmount = totalAmount.add(rpel.getActualTotalAmount());
					rpls.add(rpel);
				}
			}
			if (CollectionUtils.isEmpty(rpls)){
				return jsonViewResolver.errorJsonResult("未找到对应的线上代付单");
			}

			CashFlow debitCashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			if (debitCashFlow == null || debitCashFlow.getAccountSide() != AccountSide.DEBIT) {
				return jsonViewResolver.errorJsonResult("未找到添加的借记流水");
			}
			if (!AmountUtils.equals(debitCashFlow.getTransactionAmount(), totalAmount)) {
				return jsonViewResolver.errorJsonResult("流水金额与代付总金额不等");
			}

			RemittancePlanExecLog rpel = rpls.get(0);
			RemittanceRefundBill refundBill = new RemittanceRefundBill(debitCashFlow, rpel, null);
			refundBill.setReverseType(ReverseType.REFUND);
			remittanceRefundBillService.save(refundBill);

			String isUpdate = "";
			for (RemittancePlanExecLog remittancePlanExecLog: rpls ) {
				remittancePlanExecLog.setRemittanceRefundBillUuid(refundBill.getRemittanceRefundBillUuid());
				remittancePlanExecLog.setReverseStatus(ReverseStatus.REFUND);
				remittancePlanExecLog.setExecutionRemark(executionRemark);
				remittancePlanExecLog.setLastModifiedTime(new Date());

				remittancePlanExecLogService.saveOrUpdate(remittancePlanExecLog);

				isUpdate = remittancePlanExecLogHandler.updateRemittanceInfo(remittancePlanExecLog.getRemittancePlanUuid());
			}

			if (!"放款信息更新成功".equals(isUpdate)) {
				return isUpdate;
			}

			return jsonViewResolver.sucJsonResult();
		}catch (Exception e){
			logger.error("#refundCombine# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("退票错误");
		}
	}
	
	@Autowired
	private RemittanceProxyHandler remittanceProxyHandler;// 放款单---详情页面---失败再放款
	@RequestMapping(value = "/plan/execlog/resend", method = RequestMethod.GET)
	public @ResponseBody String reRemittanceForFailedPlan(
			@RequestParam(value = "remittancePlanUuid", required = true) String remittancePlanUuid,
			@RequestParam(value = "comment") String comment, @Secure Principal principal, HttpServletRequest request) {
		try {
			if(!validRemittancePlanAmount(remittancePlanUuid)){
				return jsonViewResolver.errorJsonResult("放款金额错误");
			}
			return remittanceProxyHandler.processingRevokeRemittancePlan(principal, request, remittancePlanUuid, comment);

		} catch (CommonException e) {
			logger.error("#reRemittanceForFailedPlan# occur error.");
			return jsonViewResolver.errorJsonResult(e.getErrorMsg());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

    // 放款金额<=计划放款金额-放款成功金额-放款中金额，若大于则放款失败；
    private boolean validRemittancePlanAmount(String remittancePlanUuid) throws CommonException {
        RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
        if (remittancePlan == null) {
            throw new CommonException(4, "放款单不存在");
        }
        String remittanceApplicationUuid = remittancePlan.getRemittanceApplicationUuid();
        RemittanceApplication application = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
        if (application == null) {
            throw new CommonException(4, "对应的计划订单不存在");
        }
        List<RemittancePlan> remittancePlans = iRemittancePlanService.getRemittancePlanListBy(remittanceApplicationUuid);
        BigDecimal allowMaxRemittanceAmount = application.getPlannedTotalAmount()
                .subtract(getAmountInSuccess(remittancePlans)).subtract(getAmountInFavour(remittancePlans));
        return allowMaxRemittanceAmount.compareTo(remittancePlan.getPlannedTotalAmount()) >= 0;
    }

    // 放款中金额
    private BigDecimal getAmountInFavour(List<RemittancePlan> remittancePlans) {
        if (CollectionUtils.isEmpty(remittancePlans)) {
            return BigDecimal.ZERO;
        }
        return remittancePlans.stream()
                .filter(a -> Arrays.asList(ExecutionStatus.CREATE, ExecutionStatus.PROCESSING, ExecutionStatus.ABNORMAL).contains(a.getExecutionStatus()))
                .map(RemittancePlan::getPlannedTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 放款成功金额
    private BigDecimal getAmountInSuccess(List<RemittancePlan> remittancePlans) {
        if (CollectionUtils.isEmpty(remittancePlans)) {
            return BigDecimal.ZERO;
        }
        return remittancePlans.stream()
                .filter(a -> Collections.singletonList(ExecutionStatus.SUCCESS).contains(a.getExecutionStatus()))
                .map(RemittancePlan::getPlannedTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	// 线上代付单---导出对账单
//	@RequestMapping(value="/plan/execlog/export")
//	@MenuSetting("submenu-remittance-plan-execlog")
	@Deprecated
	public @ResponseBody String remittancePlanExecLogExport(
			@RequestParam(value="startDate",required = false, defaultValue = "") String startDate,
			@RequestParam(value="endDate",required = false, defaultValue = "") String endDate,
			@RequestParam(value = "financialContractUuid", required=false) String financialContractUuid, 
			HttpServletRequest request,
			HttpServletResponse response,
			@Secure Principal principal){
		try{
			Date fromDate = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
			Date toDate = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
			toDate = (toDate == null) ? new Date() : toDate;
			
			String msg = checkDate(fromDate, toDate);
			if(!StringUtils.isEmpty(msg)){
				return jsonViewResolver.errorJsonResult(msg);
			}
			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			if(financialContract == null){
				return jsonViewResolver.errorJsonResult("筛选条件有误，请重新选择。");
			}
			
			// Ajax 请求不提供下载
		    if (StringUtils.isEmpty(request.getHeader("X-Requested-With"))) {
		    	ExportEventLogModel exportEventLogModel = new ExportEventLogModel("9", principal);
		    	try {
					exportEventLogModel.recordStartLoadDataTime();
					
					List<RemittancePlanExecLogExportModel> logExportModels =  remittancePlanExecLogHandler.extractExecLogExportModel(financialContractUuid, fromDate, toDate);
					if(CollectionUtils.isEmpty(logExportModels)){
						logExportModels.add(new RemittancePlanExecLogExportModel());
					}
					
					String accountNo = financialContract.getCapitalAccount().getAccountNo();
					List<CashFlowExportModel> flowExportModels = cashFlowHandler.getCashFlowExportModelsBy(accountNo, fromDate, toDate, com.zufangbao.sun.ledgerbook.AccountSide.CREDIT);
					if(CollectionUtils.isEmpty(flowExportModels)){
						flowExportModels.add(new CashFlowExportModel());
					}
					
					exportEventLogModel.recordAfterLoadDataComplete(logExportModels.size(), flowExportModels.size());
					
					ExcelUtil<RemittancePlanExecLogExportModel> execLogExportModelExcelUtil = new ExcelUtil<RemittancePlanExecLogExportModel>(RemittancePlanExecLogExportModel.class);
					List<String> remittanceExecLogs = execLogExportModelExcelUtil.exportDatasToCSV(logExportModels);

					ExcelUtil<CashFlowExportModel> cashFlowExportModelExcelUtil = new ExcelUtil<CashFlowExportModel>(CashFlowExportModel.class);
					List<String> remittanceCashFlows = cashFlowExportModelExcelUtil.exportDatasToCSV(flowExportModels);
					
					Map<String, List<String>> csvs = new HashMap<String, List<String>>();
					csvs.put("线上代付单", remittanceExecLogs);
					csvs.put("通道流水", remittanceCashFlows);
					exportZipToClient(response, genFileName(fromDate, toDate, financialContract.getContractName()), GlobalSpec.UTF_8, csvs);
  
					exportEventLogModel.recordEndWriteOutTime();
				} catch (Exception e) {
					e.printStackTrace();
					exportEventLogModel.setErrorMsg(e.getMessage());
				} finally {
					logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
				}
		    }
		    return jsonViewResolver.sucJsonResult();
		}catch (Exception e){
			logger.error("#remittancePlanExecLogExport# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("导出失败，请稍后重试!");
		}
	}
	
	private String checkDate(Date begin, Date end){
		if(begin == null){
			return "筛选条件有误，请重新选择。";
		}
		long diff = end.getTime() - begin.getTime();
		if( diff < 0 || diff > 7*24*1000*60*60 ){ // 时间间隔为负OR超过7天
			return "筛选条件有误，请重新选择。";
		}
		return "";
	}
	
	private String genFileName(Date begin, Date end,String contractName) {
		return DateUtils.format(begin, "yyyyMMddHHmmss") + "_" + DateUtils.format(end, "yyyyMMddHHmmss") + "_"+ contractName + " 放款流水对账.zip";
	}
	
	// 代付撤销单---列表页面
	@RequestMapping(value = "/refundbill/options", method = RequestMethod.GET)
	@MenuSetting("submenu-remittance-refund-bill")
	public @ResponseBody String showRemittanceRefundBillPage(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			result.put("queryAppModels", queryAppModels);
//			result.put("paymentInstitutionNames", EnumUtil.getKVList(PaymentInstitutionName.class));
			result.put("paymentInstitutionNames", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据异常");
		}
	}
	
	// 代付撤销单---列表查询
	@RequestMapping(value = "/refundbill/query")
	@MenuSetting("submenu-remittance-refund-bill")
	public @ResponseBody String queryRefundBill(@ModelAttribute RemittanceRefundBillQueryModel queryModel, Page page,@Secure Principal principal){
		try{
			List<RemittanceRefundBillShowModel> showModels = remittanceRefundBillHandler.queryShowModelList(queryModel, page);
			int total = remittanceRefundBillService.queryCount(queryModel);
			Map<String, Object> data = new HashMap<>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			data.put("queryAppModels", queryAppModels);
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e){
			logger.error("#queryRefundBill#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
	
	// 代付撤销单---统计金额（撤销金额）
	@RequestMapping(value = "/refundbill/amountStatistics", method = RequestMethod.GET)
	@MenuSetting("submenu-remittance-refund-bill")
	public @ResponseBody String billAmountStatistics(@ModelAttribute RemittanceRefundBillQueryModel queryModel){
		try{
			Map<String, Object> data = remittanceRefundBillService.amountStatistics(queryModel);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e){
			logger.error("#billAmountStatistics#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
	
	private SystemOperateLogRequestParam getSystemOperateCheckOppositeExecutionStatus(Principal principal,
			HttpServletRequest request, RemittancePlanExecLog planExecLog, RemittancePlanExecLog oldlanExecLog) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
				IpUtil.getIpAddress(request), "代付单编号为[" + planExecLog.getExecReqNo() + "]",
				LogFunctionType.CHECK_OPPOSITE_EXECUTION_STATUS, LogOperateType.UPDATE, RemittancePlanExecLog.class, oldlanExecLog, planExecLog, null);
		return param;

	}
}
