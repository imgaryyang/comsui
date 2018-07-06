package com.zufangbao.earth.yunxin.web.controller.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.coffer.entity.BusinessProcessStatus;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.handler.RemittanceProxyHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.earth.yunxin.handler.impl.LogMapRecordContentSpec;
import com.zufangbao.earth.yunxin.handler.remittance.RemittancePlanExecLogHandler;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec.GeneralErrorMsg;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelInformation;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.AuditJobSource;
import com.zufangbao.sun.yunxin.entity.audit.AuditResult;
import com.zufangbao.sun.yunxin.entity.audit.AuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.RemittanceAuditJobStatResult;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.audit.AuditJobCreateModel;
import com.zufangbao.sun.yunxin.entity.model.audit.AuditJobModel;
import com.zufangbao.sun.yunxin.entity.model.audit.AuditJobQueyResultModel;
import com.zufangbao.sun.yunxin.entity.model.audit.CashFlowAdapter;
import com.zufangbao.sun.yunxin.entity.model.audit.CounterDataExportModel;
import com.zufangbao.sun.yunxin.entity.model.audit.IssuedDataExportModel;
import com.zufangbao.sun.yunxin.entity.model.audit.LocalDataExportModel;
import com.zufangbao.sun.yunxin.entity.model.audit.RemittanceAuditResultShowModel;
import com.zufangbao.sun.yunxin.entity.model.audit.SystemBillAdapter;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBill;
import com.zufangbao.sun.yunxin.entity.remittance.ReverseStatus;
import com.zufangbao.sun.yunxin.entity.remittance.VoucherType;
import com.zufangbao.sun.yunxin.handler.AuditJobHandler;
import com.zufangbao.sun.yunxin.handler.RemittanceAuditResultHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.TransferBillService;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.RemittanceAuditJobStatResultService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceRefundBillService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;
import com.zufangbao.wellsfargo.yunxin.handler.TransferBillHandler;
import com.zufangbao.wellsfargo.yunxin.model.RemittanceVoucherQueryModel;
import com.zufangbao.wellsfargo.yunxin.model.RemittanceVoucherShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TransferVoucherBasicShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TransferVoucherCashFlowShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TransferVoucherDetailShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TransferVoucherListModel;
import com.zufangbao.wellsfargo.yunxin.model.TransferVoucherQueryModel;

/**
 *
 * 放款对账查询，详情页，核单
 *
 */
@Controller()
@RequestMapping("/audit/remittance")
@MenuSetting("menu-capital")
public class RemittanceAuditController extends BaseController{
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private AuditJobService auditJobService;
	@Autowired
	private RemittanceAuditJobStatResultService remittanceAuditJobStatResultService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;
	@Autowired
	private AuditJobHandler auditJobHandler;
	@Autowired
	private RemittanceAuditResultHandler remittanceAuditResultHandler;
	@Autowired
	private RemittancePlanExecLogHandler remittancePlanExecLogHandler;
	@Autowired
	private JpmorganApiHelper jpmorganApiHelper;
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;
	@Autowired
	private IRemittanceRefundBillService remittanceRefundBillService;
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private RemittanceProxyHandler remittanceProxyHandler;
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private TransferBillService transferBillService;
	@Autowired
	private TransferBillHandler transferBillHandler;
	
	private static final Log logger = LogFactory.getLog(RemittanceAuditController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	@MenuSetting("submenu-remittance-audit")
	public ModelAndView load(@Secure Principal principal, Page page, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-remittance-audit")
	public @ResponseBody String getOption(@Secure Principal principal) {
		HashMap<String, Object> map= new HashMap<String, Object>();

		List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
		List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

		map.put("queryAppModels", queryAppModels);
//		map.put("paymentInstitutionNames", EnumUtil.getKVList(PaymentInstitutionName.class));
		map.put("paymentInstitutionNames", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
		map.put("auditResults", EnumUtil.getKVList(AuditResult.class));
		map.put("accountSide", AccountSide.values());
		return jsonViewResolver.sucJsonResult(map);
	}

	//查询对账任务列表
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public @ResponseBody String query(@Secure Principal principal, Page page,AuditJobModel auditJobModel ) {
		try{
			auditJobModel.setAccountSide(AccountSide.CREDIT.getOrdinal());
			List<AuditJob> auditJobs = auditJobService.query(auditJobModel, page,true);
			List<AuditJobQueyResultModel> resultModel = auditJobService.convert(auditJobs);
			int jobSize = auditJobService.count(auditJobModel,true);
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("size", jobSize);
			params.put("list", resultModel);
			return jsonViewResolver.sucJsonResult(params);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
	}
	//根据选择的信托账户，显示相应的放款交易通道
	@RequestMapping(value = "query-payment-channel", method = RequestMethod.GET)
	public @ResponseBody String queryRemittancePaymentChannel(@Secure Principal principal,String finnalContractUuid) {
		try{
			List<PaymentChannelInformation> paymentChannelInformationList = paymentChannelInformationService.getPaymentChannelBy(finnalContractUuid);
			return jsonViewResolver.sucJsonResult("paymentChannelInformationList", paymentChannelInformationList);
		} catch(Exception e){
			e.printStackTrace();
		}
		return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);

	}

	//新增对账任务
	@RequestMapping(value = "create-audit-job", method = RequestMethod.GET)
	public @ResponseBody String createNewAuditJob(@Secure Principal principal,HttpServletRequest request, AuditJobCreateModel auditJobCreateModel) {
		try{
			if(!auditJobCreateModel.isValid()){
				return jsonViewResolver.errorJsonResult(auditJobCreateModel.getCheckErrorMsg());
			}
			FinancialContract finnacialContract = financialContractService.getFinancialContractBy(auditJobCreateModel.getFinancialContractUuid());
			if(finnacialContract==null){
				return jsonViewResolver.errorJsonResult("信托合同不存在");
			}
			PaymentChannelInformation paymentChannelInformation = paymentChannelInformationService.getPaymentChannelInformationBy(auditJobCreateModel.getPaymentChannelUuid());
			if(paymentChannelInformation==null){
				return jsonViewResolver.errorJsonResult("通道不存在");
			}
			AccountSide accountSide = auditJobCreateModel.getAccountSideEnum();
			if(accountSide!=AccountSide.CREDIT){
				return jsonViewResolver.errorJsonResult("只能创建代付任务");
			}
			AuditJob auditJob = auditJobService.createAuditJob(auditJobCreateModel.getFinancialContractUuid(), finnacialContract.getCapitalAccount().getAccountNo(), paymentChannelInformation, auditJobCreateModel.getStartTime_date(), auditJobCreateModel.getEndTime_date(), AuditJobSource.MANUAL, accountSide);
			createAuditJobLog(principal, request, auditJob,LogFunctionType.CREATE_AUDIT_JOB,LogOperateType.ADD);
			return jsonViewResolver.sucJsonResult("auditJob", auditJob);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}

	}

	//对账任务详情页:基本信息以及统计结果
	@RequestMapping(value = "{auditJobUuid}/detail/basic-info", method = RequestMethod.GET)
	public @ResponseBody String showBasicInfo(@PathVariable("auditJobUuid") String auditJobUuid, Integer resultCode, Page page) {
		try{
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
			if(auditJob==null){
				return jsonViewResolver.errorJsonResult("对账任务不存在");
			}
			FinancialContract financialContract = financialContractService.getFinancialContractBy(auditJob.getFinancialContractUuid());
			//统计结果
			RemittanceAuditJobStatResult statResult = remittanceAuditJobStatResultService.getByAuditJobUuid(auditJobUuid);
			//通道信息
			PaymentChannelInformation paymentChannel = paymentChannelInformationService.getPaymentChannelInformationBy(auditJob.getPaymentChannelUuid());

			Map<String,Object> params = new HashMap<String,Object>();
			params.put("auditJob", auditJob);
			params.put("financialContract", financialContract);
			params.put("paymentChannel", paymentChannel);
			params.put("statResult", statResult);
			return jsonViewResolver.sucJsonResult(params);
		}catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
	}

	//对账任务详情页:对账结果数据展示
	@RequestMapping(value = "{auditJobUuid}/detail/audit-result", method = RequestMethod.GET)
	public @ResponseBody String ShowAuditResult(@PathVariable("auditJobUuid") String auditJobUuid, Integer resultCode, Page page) {
		try{
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
			List<RemittanceAuditResultShowModel> auditShowModel = remittanceAuditResultHandler.getAuditResult(auditJob, resultCode, page);
			int count = remittanceAuditResultHandler.countAuditResult(auditJob, resultCode);
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("list", auditShowModel);
			params.put("size", count);
			return jsonViewResolver.sucJsonResult(params);
		}catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
	}

	//对账任务详情页:导出结果
	@RequestMapping(value = "{auditJobUuid}/detail/audit-result-export", method = RequestMethod.GET)
	public void exportAuditResult(@PathVariable("auditJobUuid") String auditJobUuid, HttpServletRequest request, HttpServletResponse response, @Secure Principal principal){
		try{

			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);

			List<RemittanceAuditResultShowModel> localAuditResults = remittanceAuditResultHandler.getAuditResult(auditJob, AuditResultCode.LOCAL.getCode(), null);
			List<LocalDataExportModel> localDataExportModels = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(localAuditResults)){
				localDataExportModels = localAuditResults.stream().map(a->genLocalDataExportModel(a.getSystemBill())).collect(Collectors.toList());
			}
			ExcelUtil<LocalDataExportModel> localExcelUtil = new ExcelUtil<LocalDataExportModel>(LocalDataExportModel.class);
			List<String> localCsvData = localExcelUtil.exportDatasToCSV(localDataExportModels);
			csvs.put("代付本端多账数据", localCsvData);

			List<RemittanceAuditResultShowModel> counterAuditResults = remittanceAuditResultHandler.getAuditResult(auditJob, AuditResultCode.COUNTER.getCode(), null);
			List<CounterDataExportModel> counterDataExportModels = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(counterAuditResults)){
				counterDataExportModels = counterAuditResults.stream().map(a->genCounterDataExportModel(a.getCashFlowAdapter())).collect(Collectors.toList());
			}
			ExcelUtil<CounterDataExportModel> counterExcelUtil = new ExcelUtil<CounterDataExportModel>(CounterDataExportModel.class);
			List<String> counterCsvData = counterExcelUtil.exportDatasToCSV(counterDataExportModels);
			csvs.put("代付对端多账数据", counterCsvData);

			List<RemittanceAuditResultShowModel> issuedAuditResults = remittanceAuditResultHandler.getAuditResult(auditJob,AuditResultCode.ISSUED.getCode(), null);
			List<IssuedDataExportModel> issuedDataExportModels = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(issuedAuditResults)){
				issuedDataExportModels = issuedAuditResults.stream().map(this::genIssuedDataExportModel).collect(Collectors.toList());
			}
			ExcelUtil<IssuedDataExportModel> issuedExcelUtil = new ExcelUtil<IssuedDataExportModel>(IssuedDataExportModel.class);
			List<String> issuedCsvData = issuedExcelUtil.exportDatasToCSV(issuedDataExportModels);
			csvs.put("代付平账数据", issuedCsvData);

			exportZipToClient(response, "对账结果_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + ".zip", GlobalSpec.UTF_8, csvs);

			SystemOperateLog log = new SystemOperateLog(principal.getId(),IpUtil.getIpAddress(request),LogFunctionType.EXPORTAUDITRESULT,LogOperateType.EXPORT);
			log.setRecordContent("代付-银行，任务编号："+auditJob.getAuditJobNo()+"，导出对账结果，导出代付本端多账"+(localCsvData.size()-1)+"条记录、代付对端多账"+(counterCsvData.size()-1)+"条记录、代付平账数据"+(issuedCsvData.size()-1)+"条记录。");
			systemOperateLogService.save(log);
		} catch (Exception e){
			logger.error("export audit result error");
			e.printStackTrace();
		}
	}

	private LocalDataExportModel genLocalDataExportModel(SystemBillAdapter systemBill){
		LocalDataExportModel model = new LocalDataExportModel(systemBill);
		RemittancePlanExecLog execLog = findRemittancePlanExecLog(systemBill);
		if(execLog == null){ return model;}

		RemittanceApplication remittanceApplication = remittanceApplicationService.getUniqueRemittanceApplicationByUuid(execLog.getRemittanceApplicationUuid());
		if(remittanceApplication != null){
			model.setUniqueId(remittanceApplication.getContractUniqueId());
		}
		return model;
	}


	private CounterDataExportModel genCounterDataExportModel(CashFlowAdapter cashFlowAdapter){
		return new CounterDataExportModel(cashFlowAdapter);

	}

	private IssuedDataExportModel genIssuedDataExportModel(RemittanceAuditResultShowModel showModel){
		IssuedDataExportModel model  =  new IssuedDataExportModel(showModel);
		RemittancePlanExecLog execLog = findRemittancePlanExecLog(showModel.getSystemBill());
		if(execLog == null){ return model;}
		RemittanceApplication remittanceApplication = remittanceApplicationService.getUniqueRemittanceApplicationByUuid(execLog.getRemittanceApplicationUuid());
		if(remittanceApplication != null){
			model.setUniqueId(remittanceApplication.getContractUniqueId());
		}
		return model;
	}

	private RemittancePlanExecLog findRemittancePlanExecLog(SystemBillAdapter systemBill){
		if(systemBill == null){
			return null;
		}
		String execReqNo = "";
		switch (systemBill.getSystemBillType()){
			case T_REMITTANCE_PLAN_EXEC_LOG:
				execReqNo = systemBill.getSystemBillNo();
				break;
			case T_REMITTANCE_REFUND_BILL:
				RemittanceRefundBill refundBill = remittanceRefundBillService.getRemittanceRefundBillBy(systemBill.getSystemBillNo());
				if(refundBill==null){ break; }
				execReqNo = refundBill.getRelatedExecReqNo();
		}
		return remittancePlanExecLogService.getRemittancePlanExecLogBy(execReqNo);
	}


	//对账任务重新对账
	@RequestMapping(value = "{auditJobUuid}/redo", method = RequestMethod.POST)
	public @ResponseBody String redo(@Secure Principal principal,@PathVariable("auditJobUuid")String auditJobUuid,HttpServletRequest request) {
		try{
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
			if(auditJob==null){
				return jsonViewResolver.errorJsonResult("对账任务不存在");
			}
			auditJob.doing();
			auditJobService.update(auditJob);

			auditJobHandler.doneRemittanceJob(auditJob, new Date());

			createAuditJobLog(principal, request, auditJob,LogFunctionType.REDO_AUDIT_JOB,LogOperateType.OPERATE);
			return jsonViewResolver.sucJsonResult();
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
	}

	// 对账任务详情页-重新核单
	@RequestMapping(value="{systemBillTradeUuid}/detail/audit-result")
	public @ResponseBody String checkOppositeExecutionStatus(@Secure Principal principal, @PathVariable("systemBillTradeUuid") String systemBillTradeUuid, HttpServletRequest request){
		try {
			RemittancePlanExecLog planExecLog = remittancePlanExecLogService.getRemittancePlanExecLogByExecRspNo(systemBillTradeUuid);
			if (planExecLog == null) {
				return jsonViewResolver.errorJsonResult("未找到对应的代付单");
			}
			String transactionVoucherNo = planExecLog.getExecRspNo();
			Result result = jpmorganApiHelper.queryOppositeStatus(planExecLog.getPaymentChannelUuid(), transactionVoucherNo);
			if (!result.isValid()) {
				String resultStr = JsonUtils.toJsonString(result);
				logger.info("#对端放款核单，通讯失败,代付单号:" + planExecLog.getExecReqNo() + "结果:" + resultStr);
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

			// 冲账状态未发生改为已退票 对应的放款计划单、放款明细单、放款计划单状态更新
//			String isUpdate = remittancePlanExecLogHandler.updateRemittanceInfo(planExecLog.getRemittancePlanUuid());
			String isUpdate = remittanceProxyHandler.processingRevokeUpdateRemittanceInfo(planExecLog.getRemittancePlanUuid());
			if (!"放款信息更新成功".equals(isUpdate)) {
				return jsonViewResolver.errorJsonResult(isUpdate);
			}

			SystemOperateLogRequestParam param = getSystemOperateCheckOppositeExecutionStatus(principal, request, planExecLog, oldPlanExecLog);
	        systemOperateLogHandler.generateSystemOperateLog(param);

			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#checkExecutionStatus# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("重新核单错误");
		}
	}

	private void createAuditJobLog(Principal principal, HttpServletRequest request, AuditJob auditJob,LogFunctionType logFunctionType,LogOperateType logOperateType) {
		SystemOperateLog systemOperateLog = SystemOperateLog.createLog(
                principal.getId(), null, IpUtil.getIpAddress(request),
                logFunctionType,
				logOperateType, auditJob.getUuid(),
				auditJob.getAuditJobNo());
		systemOperateLog.setRecordContent(LogMapRecordContentSpec.logFunctionTypeMatchRecordContentHeadName
				.get(systemOperateLog.getLogFunctionType()) + "【"+systemOperateLog.getKeyContent()+"】");
		systemOperateLogService.save(systemOperateLog);
	}
	
	@RequestMapping(value= "{sourceDocumentUuid}/detail/remittanceVoucher",method = RequestMethod.GET)
	public @ResponseBody String voucherDetail(@Secure Principal principal,@PathVariable("sourceDocumentUuid")String sourceDocumentUuid,HttpServletRequest request){
		try {
			RemittanceVoucherShowModel model=sourceDocumentHandler.getRemittanceVoucherShowModel(sourceDocumentUuid);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("showModel", model);
			return jsonViewResolver.sucJsonResult(result);		}
		catch (Exception e) {
			logger.error("#RemittanceAuditController#voucherDetail query remittanceVoucherShowModel detail error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	private SystemOperateLogRequestParam getSystemOperateCheckOppositeExecutionStatus(Principal principal,
			HttpServletRequest request, RemittancePlanExecLog planExecLog, RemittancePlanExecLog oldlanExecLog) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "代付单编号为[" + planExecLog.getExecReqNo() + "]",
                LogFunctionType.CHECK_OPPOSITE_EXECUTION_STATUS, LogOperateType.UPDATE, RemittancePlanExecLog.class, oldlanExecLog, planExecLog, null);
		return param;

	}

	@RequestMapping(value= "/list/remittanceVoucher",method = RequestMethod.GET)
	public @ResponseBody String voucherList(@Secure Principal principal,HttpServletRequest request,
			@ModelAttribute RemittanceVoucherQueryModel queryModel,Page page){
		try {
			List<RemittanceVoucherShowModel> list=sourceDocumentHandler.getRemittanceVoucherShowModelList(queryModel, page);
			int size = sourceDocumentHandler.countRemittanceVoucherSize(queryModel);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", list);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#RemittanceAuditController#voucherList query remittanceVoucherShowModel list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	@RequestMapping(value = "/options/remittanceVoucher", method = RequestMethod.GET)
	@MenuSetting("submenu-remittance-audit")
	public @ResponseBody String getRemittanceVoucherOption(@Secure Principal principal) {
		HashMap<String, Object> map= new HashMap<String, Object>();

		List<FinancialContract> financialContractList = financialContractService.loadAll(FinancialContract.class);
		List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContractList);

		map.put("queryAppModels", queryAppModels);
//		map.put("paymentInstitutionNames", EnumUtil.getKVList(PaymentInstitutionName.class));
		map.put("paymentInstitutionNames", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
		map.put("auditResults", EnumUtil.getKVList(SourceDocumentDetailStatus.class));
		map.put("voucherTypes", EnumUtil.getKVList(VoucherType.class));
		return jsonViewResolver.sucJsonResult(map);
	}

	//转账凭证列表页
	@RequestMapping(value= "/list/transferVoucher",method = RequestMethod.GET)
	public @ResponseBody String transferVoucherList(@ModelAttribute TransferVoucherQueryModel queryModel,Page page){
		try {
			List<SourceDocument> sourceDocuments=sourceDocumentService.querySourceDocumentByTransferVoucherQueryModel(queryModel, page);
			List<TransferVoucherListModel> transferVoucherListModels = sourceDocuments.stream().map(sd->new TransferVoucherListModel(sd)).collect(Collectors.toList());
			int size = sourceDocumentService.queryTransferVoucherQueryModelSize(queryModel);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", transferVoucherListModels);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#RemittanceAuditController#voucherList query TransferVoucherQueryModel list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	//转账凭证初始化
	@RequestMapping(value= "/options/transferVoucher",method = RequestMethod.GET)
	public @ResponseBody String getTransferVoucherOption(@Secure Principal principal){
		HashMap<String, Object> map= new HashMap<String, Object>();
		List<FinancialContract> financialContractList = financialContractService.loadAll(FinancialContract.class);
		List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContractList);
		map.put("queryAppModels", queryAppModels);
		map.put("paymentInstitutionNames", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
		map.put("sourceDocumentStatus", EnumUtil.getKVList(SourceDocumentDetailStatus.class));
		return jsonViewResolver.sucJsonResult(map);
	}
	
	//转账凭证明细
	@RequestMapping(value= "/{sourceDocumentUuid}/queryTransferVoucherDetail",method = RequestMethod.GET)
	public @ResponseBody String queryTransferVoucherDetail(@PathVariable("sourceDocumentUuid")String sourceDocumentUuid){
		try {
			SourceDocument sourceDocument= sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
			if(sourceDocument == null ){
				return jsonViewResolver.errorJsonResult("转账凭证不存在");
			}
			FinancialContract financialContract = financialContractService.getCacheableFinancialContracBy(sourceDocument.getFinancialContractUuid());
			List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocumentUuid);
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(sourceDocument.getOutlierDocumentUuid());
			if(cashFlow == null){
				throw new ApiException("流水信息不存在");
			}
			List<TransferVoucherDetailShowModel> transferVoucherDetailShowModels = transferBillHandler.createTransferVoucherDetailShowModel(sourceDocumentDetails);
			TransferVoucherBasicShowModel transferVoucherBasicShowModel = new TransferVoucherBasicShowModel(financialContract==null?"":financialContract.getContractNo(),sourceDocument);
			TransferVoucherCashFlowShowModel transferVoucherCashFlowShowModel =new TransferVoucherCashFlowShowModel(cashFlow,sourceDocument.getSecondAccountId());
			
			Map<String, Object> data = new HashMap<>();
			data.put("transferVoucherBasicShowModel", transferVoucherBasicShowModel);
			data.put("transferVoucherDetailShowModel", JsonUtils.toJSONString(transferVoucherDetailShowModels));
			data.put("transferVoucherCashFlowShowModel", transferVoucherCashFlowShowModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#RemittanceAuditController#voucherList query TransferVoucherQueryModel detail error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,QUERY_ERROR);
		}
	}
	
}
