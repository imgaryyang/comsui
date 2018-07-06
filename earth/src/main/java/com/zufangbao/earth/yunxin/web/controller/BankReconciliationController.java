package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.GlobalSpec4Earth;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.account.ConflictBankAccountException;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.excel.CashFlowExcelVO;
import com.zufangbao.sun.yunxin.entity.model.BankReconciliationQueryModel;
import com.zufangbao.sun.yunxin.entity.model.ContractInfoModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.wellsfargo.exception.BankRechargeAmountException;
import com.zufangbao.wellsfargo.exception.BankRechargeAmountException;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentExcuteStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentVirtualAccountResovler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.webform.CashFlowDetailShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.webform.CustomerDepositResult;
import com.zufangbao.wellsfargo.silverpool.cashauditing.webform.RelatedVoucherShowModel;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author dcc
 *
 */

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class BankReconciliationController extends BaseController {

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CashFlowAutoIssueHandler cashFlowAutoIssueHandler;
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private VirtualAccountHandler virtualAccountHandler;
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private ActivePaymentVoucherProxy activePaymentVoucherProxyHandler;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;

	private static final Log logger = LogFactory.getLog(BankReconciliationController.class);

	@RequestMapping(value = "account-manager/cash-flow-audit/options", method = RequestMethod.GET)
	public @ResponseBody String getBankReconciliationOptions(@ModelAttribute BankReconciliationQueryModel bankReconciliationQueryModel, @Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("accountSideList", EnumUtil.getKVList(AccountSide.class));
			result.put("auditStatusList", EnumUtil.getKVList(AuditStatus.class));
//			result.put("bankReconciliationQueryModel",bankReconciliationQueryModel);
			result.put("customerTypeList",EnumUtil.getKVList(CustomerType.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取参数错误");
		}
	}

	@RequestMapping(value="account-manager/cash-flow-audit/query" )
	public @ResponseBody String queryBankReconciliation(@Secure Principal principal,
			@ModelAttribute BankReconciliationQueryModel bankReconciliationQueryModel, Page page) {
		try {
			List<CashFlow> cashFlowList = cashFlowService.getCashFlowList(bankReconciliationQueryModel, page);
			int count = cashFlowService.count(bankReconciliationQueryModel);
			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("list", cashFlowList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	//流水详情页
	@RequestMapping(value="account-manager/cash-flow-audit/detail/{cashFlowUuid}", method = RequestMethod.GET)
	public @ResponseBody String getCashFlowDetail(@PathVariable(value = "cashFlowUuid")String cashFlowUuid){
		try {
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			if(cashFlow == null){
				return jsonViewResolver.errorJsonResult("流水信息错误！");
			}
			List<FinancialContract> fcs = financialContractService.getFinancialContractListBy(cashFlow.getHostAccountNo());
			if(CollectionUtils.isEmpty(fcs)){
				return jsonViewResolver.errorJsonResult("信托合同不存在！");
			}
			CashFlowDetailShowModel detail = new CashFlowDetailShowModel(cashFlow, fcs.get(0));

			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("detail", detail);

			Voucher voucher = voucherService.getVoucherByCashFlowUuid(cashFlowUuid);
			if(voucher != null){
				RelatedVoucherShowModel relatedVoucherShowModel = new RelatedVoucherShowModel(voucher);
				data.putIfAbsent("voucher", relatedVoucherShowModel);
				return jsonViewResolver.sucJsonResult(data);
			}
			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByCashFlow(cashFlowUuid, SourceDocument.FIRSTOUTLIER_REMITTANCE);
			if(sourceDocument != null){
				RelatedVoucherShowModel relatedVoucherShowModel = new RelatedVoucherShowModel(sourceDocument);
				data.putIfAbsent("voucher", relatedVoucherShowModel);
				return jsonViewResolver.sucJsonResult(data);
			}
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("流水详情获取失败！");
		}
	}

	//list
	@RequestMapping(value="account-manager/cash-flow-audit/show-deposit-result", method = RequestMethod.POST)
	public @ResponseBody String queryDepositResult(@RequestParam(value = "cashFlowUuid")String cashFlowUuid) {
		try {

			List<SourceDocument> sourceDocumentList = sourceDocumentService.getDepositReceipt(cashFlowUuid, SourceDocumentStatus.SIGNED, "", "");
			List<CustomerDepositResult> depositResults = sourceDocumentHandler.convertToDepositResult(sourceDocumentList);
			return jsonViewResolver.sucJsonResult("depositResults",depositResults);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	//根据customerName模糊查
	@RequestMapping(value="account-manager/cash-flow-audit/query-customer",method = RequestMethod.GET)
	public @ResponseBody String queryCustomers(@RequestParam(value = "name")String name,
			@RequestParam(value = "financialContractUuid")String financialContractUuid,@RequestParam(value = "customerType")Integer customerType, Page page) {
		try {
			CustomerType type = EnumUtil.fromOrdinal(CustomerType.class, customerType);

			List<ContractInfoModel> modelList  = null;
			if(type == CustomerType.PERSON){
				modelList  = customerService.getContractByNameOrContractNo(financialContractUuid, customerType, name, page);
			}else if(type == CustomerType.COMPANY){
				FinancialContract financialContract =  financialContractService.getFinancialContractBy(financialContractUuid);
				if(financialContract != null){
					App app = financialContract.getApp();
					modelList = customerService.getContractInfoModelByApp(app);
				}
			}

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("modelList", modelList);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	//充值提交
	@RequestMapping(value= "account-manager/cash-flow-audit/deposit", method = RequestMethod.POST)
	public @ResponseBody String deposit(@RequestParam(value = "cashFlowUuid")String cashFlowUuid,
			@RequestParam(value = "customerUuid")String customerUuid,
			@RequestParam(value = "financialContractUuid")String financialContractUuid,
			@RequestParam(value = "depositAmount")BigDecimal depositAmount,
			@RequestParam(value = "remark")String remark,
			@RequestParam(value = "binding")boolean binding,
			@Secure Principal principal,HttpServletRequest request) {
		try {
			if(depositAmount == null || StringUtils.isBlank(remark)){
				return jsonViewResolver.errorJsonResult("请填写充值金额和备注！");
			}

			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			Customer customer = customerService.getCustomer(customerUuid);
			if(customer==null){
				logger.info("customer is not exist");
				return jsonViewResolver.errorJsonResult("没有该客户");
			}
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			AssetCategory assetCategory = getAssetGategoryByCustomer(customer, cashFlow.getCashFlowIdentity());
			if(!cashFlow.isCanBeDeposited()){
				logger.info("cashFlow is not can deposite， cashFlowUuid is ["+cashFlow.getCashFlowUuid()+"]");
				return jsonViewResolver.errorJsonResult("不可充值");
			}

			Contract contract = contractService.getContractByCustomer(customer);

			SourceDocument activeSourceDocument = sourceDocumentService.getActiveVoucher(cashFlow.getCashFlowUuid(), SourceDocumentStatus.CREATE, contract==null?"":contract.getUuid(), financialContractUuid);
			//如果存在主动还款凭证，则传入
			SourceDocumentVirtualAccountResovler resover = cashFlowAutoIssueHandler.charge_cash_into_virtual_account(cashFlow, customer, depositAmount, financialContract, assetCategory, remark,activeSourceDocument,contract,binding);
			if(resover.getSourceDocument()==null){
				logger.info("sourceDocument is not exist");
				return jsonViewResolver.errorJsonResult("充值出错");
			}

			//cashFlow  log  提交充值单
			SystemOperateLogRequestParam param_cashFlow = getSystemOperateLogrequestParamAdd(principal, request, cashFlow, resover.getSourceDocument());
			systemOperateLogHandler.generateSystemOperateLog(param_cashFlow);

			//sourceDocument  log  创建充值单
			SystemOperateLogRequestParam param_sourceDocument = getSystemOperateLogrequestParamCreate(principal, request, resover.getVirtualAccount(), resover.getSourceDocument());
			systemOperateLogHandler.generateSystemOperateLog(param_sourceDocument);

			sendMsgToRecoverIfActiveVoucher(resover.getSourceDocument());

			return jsonViewResolver.sucJsonResult();
		}catch (ConflictBankAccountException e) {

			Map<String,Object> mapData = new HashMap<String,Object>();
			mapData.put("virtualAccountUuid", e.getMsg());
			return jsonViewResolver.jsonResult(GlobalSpec4Earth.ErrorCode4Yunxin.ERROR_BANK_CARD_CONFILICT,e.getMsg(),mapData);
		}catch (BankRechargeAmountException e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("充值金额不得大于存疑金额！");
		}
		catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	//新增充值单 提交  显示列表
	@Deprecated
	@RequestMapping(value="account-manager/cash-flow-audit/build-deposit-result", method = RequestMethod.POST)
	public @ResponseBody String buildDepositResult(@RequestParam(value = "customerUuid")String customerUuid,
			@RequestParam(value = "cashFlowUuid")String cashFlowUuid,@RequestParam(value = "financialContractUuid")String financialContractUuid,
			@Secure Principal principal,HttpServletRequest request) {
		try {
			Customer customer = customerService.getCustomer(customerUuid);
			Contract contract = contractService.getContractByCustomer(customer);
			String contractUuid = "";
			if(customer != null){
				if(customer.isCompanyType()){
					contractUuid = "";
				}else{
					contractUuid = contract.getUuid();
				}
			}
			VirtualAccount virtualAccount = virtualAccountService.create_if_not_exist_virtual_account(customerUuid, financialContractUuid, contractUuid);
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			BigDecimal issuedAmount =  cashFlow.getIssuedAmount();
			BigDecimal transactionAmount = cashFlow.getTransactionAmount();

			CustomerDepositResult depositResult = virtualAccountHandler.buildDeposit(virtualAccount.getVirtualAccountUuid(), issuedAmount, transactionAmount);

			// virtualAccount
			VirtualAccount virtualAccountInDb = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
			if(virtualAccountInDb == null){

				SystemOperateLogRequestParam param = getSystemOperateLogrequestParamVirtualAccount(principal, request,
						virtualAccount);
				systemOperateLogHandler.generateSystemOperateLog(param);

			}
			return jsonViewResolver.sucJsonResult("depositResult",depositResult);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}

	}

//	@RequestMapping("account-manager/cash-flow-audit/export-csv")
//	@ResponseBody
	@Deprecated
	public String exportCashFlowCSV(
			@Secure Principal principal,
			@ModelAttribute BankReconciliationQueryModel bankReconciliationQueryModel,
			HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("12", principal);
		try {
			if(!bankReconciliationQueryModel.valid_4export_csv()){
				return jsonViewResolver.errorJsonResult(bankReconciliationQueryModel.getCheckErrorMsg());
			}
			exportEventLogModel.recordStartLoadDataTime();

			List<CashFlow> cashFlowList = cashFlowService.getCashFlowList(bankReconciliationQueryModel, null);

			exportEventLogModel.recordAfterLoadDataComplete(cashFlowList.size());

			List<CashFlowExcelVO> cashFlowCSV = cashFlowList.stream().map(cashflow -> new CashFlowExcelVO(cashflow)).collect(Collectors.toList());
			ExcelUtil<CashFlowExcelVO> excelUtil = new ExcelUtil<CashFlowExcelVO>(CashFlowExcelVO.class);
			List<String> CSVData = excelUtil.exportDatasToCSV(cashFlowCSV);
			Map<String,List<String>> csvs = new HashMap<String,List<String>>();
			String fileName = "银行流水" + "_"+ DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS");
			csvs.put(fileName, CSVData);
			exportZipToClient(response, fileName+".zip", GlobalSpec.UTF_8, csvs);

			exportEventLogModel.recordEndWriteOutTime();
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
			return jsonViewResolver.errorJsonResult(GlobalMsgSpec.GeneralErrorMsg.MSG_SYSTEM_ERROR);
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
		return jsonViewResolver.sucJsonResult();

	}

	private void sendMsgToRecoverIfActiveVoucher(SourceDocument sourceDocument){
		try {
			if(sourceDocument==null || StringUtils.isEmpty(sourceDocument.getRelatedContractUuid())){
				return;
			}
			String voucherUuid = sourceDocument.getVoucherUuid();
			if(StringUtils.isEmpty(voucherUuid)){
				return;
			}
			Voucher voucher = voucherService.get_voucher_by_uuid(voucherUuid);
			if(voucher==null || !voucher.isActiveVoucher()){
				return;
			}
			logger.info("#页面充值后，发送待核销主动还款凭证消息，contractUuid["+sourceDocument.getRelatedContractUuid()+"],sourceDocumentUuid["+sourceDocument.getSourceDocumentUuid()+"]");
			activePaymentVoucherProxyHandler.recover_active_payment_voucher(sourceDocument.getRelatedContractUuid(), sourceDocument.getSourceDocumentUuid(), Priority.Medium.getPriority());
		} catch (Exception e) {
			logger.error("#页面充值后，发送待核销主动还款凭证消息，contractUuid["+sourceDocument.getRelatedContractUuid()+"],sourceDocumentUuid["+sourceDocument.getSourceDocumentUuid()+"],with exception stack trace["+ ExceptionUtils.getFullStackTrace(e)+"]");
		}

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamCreate(Principal principal,
			HttpServletRequest request,VirtualAccount virtualAccount,SourceDocument sourceDocument ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "充值金额为" + sourceDocument.getBookingAmount() + ",充值单号[" + sourceDocument.getSourceDocumentNo() + "]", LogFunctionType.ADDPREPAIDORDER,

                LogOperateType.ADD, SourceDocument.class, sourceDocument, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamAdd(Principal principal,
			HttpServletRequest request,CashFlow cashFlow,SourceDocument sourceDocument ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "充值金额为" + sourceDocument.getBookingAmount() + ",充值单号[" + sourceDocument.getSourceDocumentNo() + "]", LogFunctionType.ADDPREPAIDORDER,
                LogOperateType.ADD, CashFlow.class, cashFlow, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamVirtualAccount(Principal principal,
			HttpServletRequest request,VirtualAccount virtualAccount ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), virtualAccount.getVirtualAccountNo(), LogFunctionType.ADDVIRTUALACCOUNT,
                LogOperateType.ADD, VirtualAccount.class, virtualAccount, null, null);
		return param;

	}

	private AssetCategory getAssetGategoryByCustomer(Customer customer, String cashFlowIdentity) {
		if(customer.isCompanyType()){
			return  AssetConvertor.convertAppDepositAssetCategory(cashFlowIdentity);
		}else{
			Contract contract = contractService.getContractByCustomer(customer);
			return AssetConvertor.convertDepositAssetCategory(contract, cashFlowIdentity);
		}
	}

	//作废
	@RequestMapping(value ="account-manager/cash-flow-audit/deposit-cancel", method = RequestMethod.POST)
	public @ResponseBody String depositCancel(@ModelAttribute CustomerDepositResult depositResult,
			@RequestParam(value = "cashFlowUuid")String cashFlowUuid,@Secure Principal principal,HttpServletRequest request) {
		try {

			String sourceDocumentUuid = depositResult.getSourceDocumentUuid();
			if (StringUtils.isEmpty(sourceDocumentUuid)){
				logger.info("sourceDocumentUuid is null");
				return jsonViewResolver.errorJsonResult("充值单号为空");
			}

			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
			if(sourceDocument.sourceDocumentBeAbleToExcute()==false){
				throw new ApiException("充值单当前不可做操作");
			}
			//变更充值单执行状态为执行中
			sourceDocumentService.updateExcuteStatus(sourceDocumentUuid, SourceDocumentExcuteStatus.DOING);
			List<SourceDocumentDetail> sourceDocumentDetailList = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocumentUuid);
			for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetailList){
				if(SourceDocumentDetailStatus.SUCCESS == sourceDocumentDetail.getStatus()){
					throw new ApiException("该充值单已经使用");
				}
			}
			BigDecimal bookingAmount = depositResult.getDepositAmount();
//			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
//			Customer customer = customerService.getCustomer(customerUuid);
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			SourceDocument sourceDocumentRevoke = null;

			sourceDocumentRevoke = cashFlowAutoIssueHandler.deposit_cancel(sourceDocumentUuid,cashFlowUuid, bookingAmount, null, sourceDocument.getMqBusinessId());

			//创建撤销单  sourceDocumentRevoke log
			SystemOperateLogRequestParam param = getSystemOperateLogrequestParamCreateRevoke(principal, request,sourceDocumentRevoke);
			systemOperateLogHandler.generateSystemOperateLog(param);

			//充值作废 sourceDocument log
			SystemOperateLogRequestParam paramer = getSystemOperateLogrequestParamRevoke(principal, request,sourceDocument,sourceDocumentRevoke);
			systemOperateLogHandler.generateSystemOperateLog(paramer);

			Map<String,Object> data = new HashMap<String,Object>();
			data.put("depositResult", depositResult);
			data.put("cashFlow", cashFlow);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			//变更充值单执行状态为执行结束
			sourceDocumentService.updateExcuteStatus(depositResult.getSourceDocumentUuid(), SourceDocumentExcuteStatus.DONE);
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamCreateRevoke(Principal principal,
			HttpServletRequest request,SourceDocument sourceDocument ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), ",撤销金额为" + sourceDocument.getBookingAmount() + ",撤销单号为【" + sourceDocument.getSourceDocumentNo() + "】", LogFunctionType.ADDREVOKEORDER,
                LogOperateType.ADD, SourceDocument.class, sourceDocument, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamRevoke(Principal principal,
			HttpServletRequest request,SourceDocument sourceDocument ,SourceDocument sourceDocumentRevoke ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), ",作废金额为" + sourceDocument.getBookingAmount() + ",撤销单号为【"
                + sourceDocumentRevoke.getSourceDocumentNo() + "】", LogFunctionType.REVOKERECHARGEORDER,
				LogOperateType.ADD, SourceDocument.class, sourceDocument, null, null);
		return param;

	}
}
