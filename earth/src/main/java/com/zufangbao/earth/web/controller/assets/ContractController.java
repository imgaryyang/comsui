package com.zufangbao.earth.web.controller.assets;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zufangbao.sun.entity.tag.TagConfigVO;
import com.zufangbao.sun.handler.tag.TagOpsHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.InfoMessage;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.ContractListShowModel;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.util.DownloadUtils;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.reapymentInfo.BankCoreCodeMapSpec;
import com.zufangbao.earth.yunxin.handler.RepurchaseHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2.DeductBusinessNotifyJobServer;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec.PrepaymentErrorCode;
import com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.PrepaymentErrorMsgSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractModifyFlag;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.repurchase.FileRepurchaseDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountEnvVar;
import com.zufangbao.sun.entity.repurchase.RepurchaseDetailsModel;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.service.AssetPackageService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FileStorageService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetExtraCharge;
import com.zufangbao.sun.yunxin.entity.ContractStateAdaptaterView;
import com.zufangbao.sun.yunxin.entity.FileStorage;
import com.zufangbao.sun.yunxin.entity.FlieApplicationType;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.excel.LoanContractDetailCheckExcelVO;
import com.zufangbao.sun.yunxin.entity.files.Appendix;
import com.zufangbao.sun.yunxin.entity.model.ContractQueryModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetBillingDetails;
import com.zufangbao.sun.yunxin.entity.model.prepayment.PrepaymentCreateModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.handler.AppendixHandler;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.handler.FileStorageHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.RecordLogCore;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.files.AppendixService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.yunxin.service.v2.SignUpService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentRecordDetail;
import com.zufangbao.wellsfargo.yunxin.handler.AssetSetApiHandler;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PrepaymentHandler;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;

/**
 * @author Downpour
 */
@Controller
@RequestMapping("/contracts")
@MenuSetting("menu-data")
public class ContractController extends BaseController {

	@Autowired
	private RepurchaseService repurchaseService;

	@Autowired
	private ContractService contractService;
	@Autowired
	private ContractAccountService contractAccountService;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private AssetPackageService assetPackageService;
	@Autowired
	private ContractHandler contractHandler;
	@Autowired
	public FinancialContractService financialContractService;
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	@Autowired
	private IRemittancePlanService iRemittancePlanService;
	@Autowired
	private PrepaymentHandler prepaymentHandler;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private RepurchaseHandler repurchaseHandler;
	@Autowired
	private FileStorageHandler fileStorageHandler;
	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private AssetSetApiHandler assetSetApiHandler;
	@Autowired
	private JournalVoucherHandler journalVoucherHandler;
	@Autowired
	private RecordLogCore recordLogCore;
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private SignUpService signUpService;
	
	@Autowired
	private SignUpHandler signUpHandler;
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DeductBusinessNotifyJobServer deductBusinessNotifyJobServer;
	@Autowired
	private AppendixHandler appendixHandler;

	@Autowired
	private AppendixService appendixService;

	@Autowired
	private TagOpsHandler tagOpsHandler;

	@Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
	private String groupNameForChannelsSignUp;
	
	@Value("#{config['zhonghang.signTransType']}")
	private String signTransType;
	
	@Value("#{config['zhonghang.notifyUrlToSignUp']}")
	private String notifyUrlToSignUp;
	
	@Value("#{config['zhonghang.merId']}")
	private String zhonghangMerId;
	
	@Value("#{config['zhonghang.secret']}")
	private String zhonghangSecret;
	
	private static final Log logger = LogFactory.getLog(ContractController.class);
	private static String savePath;

	@Value("#{config['uploadPath']}")
	private void setSavePath(String uploadPath) {
		if (StringUtils.isEmpty(uploadPath)) {
			ContractController.savePath = getClass().getResource(".").getFile() + "contracts" + File.separator;
		} else if (uploadPath.endsWith(File.separator)) {
			ContractController.savePath = uploadPath + "contracts" + File.separator;
		} else {
			ContractController.savePath = uploadPath + File.separator + "contracts" + File.separator;
		}
	}

	/**
	 * 已还本金returnedPrincipal和未还本金planningPrincipal
	 * 已还利息returnedInterest和未还利息planningInterest
	 * 已还期数returnedPeriod和未还期数planningPeriod 已还 本金+利息
	 * returnedPrincipalAndInterest 未还 本金+利息 planningPrincipalAndInterest
	 */
	private Map<String, Object> getReturnedAndPlanedField(
			List<AssetSet> assetSets) {
		BigDecimal returnedPrincipal = new BigDecimal("0.00");
		BigDecimal returnedInterest = new BigDecimal("0.00");
		BigDecimal totalPrincipalAndInterest = new BigDecimal("0.00");
		
		int returnedPeriod = 0;
		if(CollectionUtils.isNotEmpty(assetSets)){
			for (AssetSet assetSet : assetSets) {
				RepaymentChargesDetail returnedChargesDetail = repaymentPlanHandler.getPaidChargesDetail(assetSet);
				returnedPrincipal = returnedPrincipal.add(returnedChargesDetail.getLoanAssetPrincipal());
				returnedInterest = returnedInterest.add(returnedChargesDetail.getLoanAssetInterest());
				
				totalPrincipalAndInterest = totalPrincipalAndInterest.add(assetSet.getAssetInitialValue());
				if (assetSet.getAssetStatus() == AssetClearStatus.CLEAR && assetSet.getOnAccountStatus() == OnAccountStatus.WRITE_OFF) {
					returnedPeriod++;
				}
			}
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("returnedPrincipal", returnedPrincipal);
		result.put("returnedInterest", returnedInterest);
		result.put("returnedPeriod", returnedPeriod);
		result.put("planningPeriod", assetSets.size() - returnedPeriod);
		result.put("returnedPrincipalAndInterest", returnedPrincipal.add(returnedInterest));
		result.put("planningPrincipalAndInterest", totalPrincipalAndInterest.subtract(returnedPrincipal.add(returnedInterest)));
		return result;
	}

	// 贷款合同详情
	@RequestMapping("/getDetailData")
	public @ResponseBody String getDetailData(
			@RequestParam(value = "id", required = false) Long contractId,
			@RequestParam(value = "uid", required = false) String uniqueId,
			@RequestParam(value = "uuid", required = false) String uuid) {
		try {
			Contract contract = null;
			List<RemittanceApplication> remittanceApplications = null;
			ContractAccount contractAccount = null;
			FinancialContract financialContract = null;
			List<AssetSet> assetSetList = null;
			RepurchaseDoc rDoc = null;
			Map<String, Object> fields = null;
			// List<Integer> allVersion = null;
			List<Map<String, Object>> allVersion = null;
			LoanBatch loanBatch = null;

			if (contractId != null) {
				contract = contractService.getContract(contractId);
			} else if (StringUtils.isNotEmpty(uniqueId)) {
				contract = contractService.getContractByUniqueId(uniqueId);
			}else if(StringUtils.isNotEmpty(uuid)){
				contract = contractService.getContract(uuid);
			}
			if (contract != null) {
				contractAccount = contractAccountService
						.getTheEfficientContractAccountBy(contract);
				financialContract = assetPackageService
						.getFinancialContract(contract);
				assetSetList = repaymentPlanService
						.get_all_open_repayment_plan_list(contract);
				rDoc = repurchaseService.getRepurchaseDocBy(contract.getId());
				
				fields = this.getReturnedAndPlanedField(assetSetList);
				// allVersion = repaymentPlanService.getAllVersionNo(contract);
				allVersion = repaymentPlanService
						.getAllVersionNoWithCreateTime(contract);
				uniqueId = StringUtils.isNotEmpty(uniqueId) ? uniqueId
						: contract.getUniqueId();
				loanBatch = contractHandler.getLoanBathByContract(contract.getId());
			}
			remittanceApplications = remittanceApplicationService
					.getRemittanceApplicationsBy(uniqueId);
			return getDatas(contract, contractAccount, financialContract,
					assetSetList, fields, remittanceApplications, allVersion,
					rDoc,loanBatch);
		} catch (Exception e) {
			logger.error("#getDetailData  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	private String getDatas(Contract contract, ContractAccount contractAccount,
			FinancialContract financialContract, List<AssetSet> assetSetList,
			Map<String, Object> fields,
			List<RemittanceApplication> remittanceApplications,
			List<Map<String, Object>> allVersion, RepurchaseDoc rDoc,LoanBatch loanBatch) {
		HashMap<String, Object> results = new HashMap<String, Object>();
		BigDecimal raTotalAmount = BigDecimal.ZERO; // 放款总额
		BigDecimal raPaidAmount = BigDecimal.ZERO; // 已放
		BigDecimal raNotPaidAmount = BigDecimal.ZERO; // 未放
		BigDecimal total = BigDecimal.ZERO; // 总计 以上和

		if (CollectionUtils.isNotEmpty(remittanceApplications)) {
			for (RemittanceApplication remittanceApplication : remittanceApplications) {
				raTotalAmount = raTotalAmount.add(remittanceApplication
						.getPlannedTotalAmount());
				raPaidAmount = raPaidAmount.add(remittanceApplication
						.getActualTotalAmount());
			}
			raNotPaidAmount = raTotalAmount.subtract(raPaidAmount);
		}
		long canInvalidateCount = 0L;
		boolean canPrepayment = false;
		if(contract != null && contract.getState() == ContractState.EFFECTIVE){
			if (CollectionUtils.isNotEmpty(assetSetList)) {
				for(AssetSet assetSet : assetSetList){
					if(assetSet.getCurrentPeriod() == contract.getPeriods() && new Date().before(assetSet.getAssetRecycleDate())){
						canPrepayment = true;
					}
//					if(assetSet.getExecutingStatus() != ExecutingStatus.UNEXECUTED || new Date().after(assetSet.getAssetRecycleDate())){
					if(!repaymentPlanHandler.ownedContractCanBeInvalidated(assetSet)){
						canInvalidateCount++;
					}
				}
			}
		}
//		if(financialContract.isAllowFreewheelingRepayment()){
//			results.put("modifyReason", EnumUtil.getKVList(RepaymentPlanModifyReason.class));
//		}else {
//			results.put("modifyReason", EnumUtil.getKVListIncludes(RepaymentPlanModifyReason.class, RepaymentPlanModifyReason.modifyReasonOfCommon()));
//		}

		results.put("modifyReason", EnumUtil.getKVListIncludes(RepaymentPlanModifyReason.class, RepaymentPlanModifyReason.modifyReasonOfCommon()));
		results.put("canPrepayment", canPrepayment);
		results.put("canInvalidate", canInvalidateCount == 0L);
		results.put("contract", contract);
		results.put("contractAccount", contractAccount);
		results.put("financialContract", financialContract);
		results.put("repurchaseDoc", rDoc);
		results.put("fields", fields);
		if (contract!=null && !signUpHandler.judgeZhongHangByfinancialContractCode(contract.getFinancialContractUuid())) {
			results.put("coreBanks", BankCoreCodeMapSpec.coreBankMap);
		}else{
			results.put("coreBanks", ZhonghangResponseMapSpec.ENERGENCY_BANK_NAME_FOR_SHORT);
		}
		results.put("remittanceApplications", remittanceApplications);
		results.put("raTotalAmount", raTotalAmount);
		results.put("raPaidAmount", raPaidAmount);
		results.put("raNotPaidAmount", raNotPaidAmount);
		results.put("total", total);
		results.put("allVersion", allVersion);
		results.put("mobile", contract == null ? "" : contract.getCustomer().getMobile());
		results.put("loanBatchCode",loanBatch == null ? "": loanBatch.getCode());
		return jsonViewResolver.sucJsonResult(results);
	}

	@RequestMapping(value = "/detail/planlist")
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String getRemittancePlanList(
			@RequestParam(value = "remittanceApplicationUuid") String remittanceApplicationUuid) {
		try {
			List<RemittancePlan> remittancePlans = iRemittancePlanService
					.getRemittancePlanListBy(remittanceApplicationUuid);
			return jsonViewResolver.sucJsonResult("remittancePlans",
					remittancePlans,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.WriteNullStringAsEmpty);
		} catch (Exception e) {
			logger.error("#getRemittancePlanList# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取列表错误");
		}
	}

	// 获取回购详情
	@RequestMapping(value = "/detail/repurchaseDetail")
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String getRepurchaseDetail(
			@RequestParam(value = "id") Long contractId,@RequestParam(value = "init", required=false) Integer init,
			@ModelAttribute RepurchaseAmountDetail repurchaseAmountDetail) {
		try {
			Contract contract = contractService.getContract(contractId);
			FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
			if(!financialContract.canBeRepurchasedViaManual()){
				return jsonViewResolver.errorJsonResult("该贷款合同不允许进行回购操作，请修改后重试");
			}
			List<String> assetSetUuids = repaymentPlanService.getExecutingAssetSetUuids(contractId);
			if (CollectionUtils.isNotEmpty(assetSetUuids)) {
				return jsonViewResolver.errorJsonResult("该贷款合同下有正在执行的还款计划，请稍后再进行回购操作");
			}
			repurchaseAmountDetail.convertRepurchaseAlgorithmCnToEn();
			if(init == null){
				contractHandler.cal_repurchase_detail_algorithm(repurchaseAmountDetail, financialContract);
			}
			Map<String, Object> result = contractHandler.calculateRepurchaseDetail(contract, financialContract, repurchaseAmountDetail);
			return jsonViewResolver.sucJsonResult(result,SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.WriteNullStringAsEmpty);
		} catch (CompileExpressionErrorException e){
			logger.error("#getRemittancePlanList# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("算法错误");
		}catch (Exception e) {
			logger.error("#getRemittancePlanList# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	// 确定回购
	@RequestMapping(value = "/detail/repurchase")
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String doRepurchase(
			@RequestParam(value="id", required=false) Long contractId,@ModelAttribute RepurchaseAmountDetail repurchaseAmountDetail,
			HttpServletRequest request, @Secure Principal principal){
		try{
			Contract contract = contractService.getContract(contractId);
			List<String> assetSetUuids = repaymentPlanService.getExecutingAssetSetUuids(contractId);
			if(CollectionUtils.isNotEmpty(assetSetUuids)){
				return jsonViewResolver.errorJsonResult("该贷款合同下有正在执行的还款计划，请稍后再进行回购操作");
			}
			
			//未偿本金校验
			List<AssetSet> assetSets = contractHandler.getNeedRepurchasedAssetSetList(contractId);
			RepurchaseAmountEnvVar envVar = contractHandler.parseRepurchaseEnvironmentVariables(assetSets);
			if(!AmountUtils.equals(envVar.getOutstandingPrincipal(), repurchaseAmountDetail.getRepurchasePrincipal())){
				return jsonViewResolver.errorJsonResult("回购本金错误");
			}
			
			repurchaseAmountDetail.convertRepurchaseAlgorithmCnToEn();
			repurchaseHandler.conductRepurchaseViaManual(contractId, repurchaseAmountDetail,GeneratorUtils.generateManualBatchNo());
			Contract editContract = contractService.getContract(contractId);
			RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocBy(contract.getId());
			if(repurchaseDoc != null) {
				SystemOperateLog log = recordLogCore.insertNormalRecordLog(
						principal.getId(), IpUtil.getIpAddress(request),LogFunctionType.REPURCHASECONTRACT,
						LogOperateType.UPDATE,contract);
				log.setKeyContent(contract.getUuid());
				log.setRecordContent("提前回购，回购单号：【"+ repurchaseDoc.getRepurchaseDocUuid() +"】，贷款合同状态由【"+contract.getState().getChineseMessage()
										+"】变更为【"+editContract.getState().getChineseMessage()+"】");
				systemOperateLogService.saveOrUpdate(log);
			}

			return jsonViewResolver.sucJsonResult();
		} catch (CompileExpressionErrorException e){
			logger.error("#doRepurchase# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("算法错误");
		}catch (Exception e){
			logger.error("#doRepurchase# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("回购失败 ，请稍后重试！");
		}
	}

	// 获取某一版本下的还款计划列表
	@RequestMapping(value = "/detail/{contractId}")
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String getAssetSetWithVersionNo(
			@PathVariable(value = "contractId") Long contractId,
			@RequestParam(value = "versionNo") Integer versionNo) {
		try {
			Contract contract = contractService.getContract(contractId);
			if (contract == null) {
				return jsonViewResolver.errorJsonResult("参数错误");
			}
			FinancialContract financialContract = financialContractService
					.getFinancialContractBy(contract.getFinancialContractUuid());
			if (financialContract == null) {
				return jsonViewResolver.errorJsonResult("没有找到所属的信托合同");
			}
			Map<String, Object> result = new HashMap<String, Object>();
			// 该版本下的所有的
			String assetSetUuid = repaymentPlanService.get_first_asset_set_uuid_given_version_no_belong_to_contract(contract.getId(), versionNo);
			RepaymentPlanExtraData repaymentPlanExtraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSetUuid);
			if (repaymentPlanExtraData != null) {
				RepaymentPlanModifyReason modifyReason = repaymentPlanExtraData.getReasonCode();
				result.put("modifyReason", modifyReason == null ? null : modifyReason.getChineseMessage());
			}
			List<AssetSetBillingDetails> billingDetails = repaymentPlanHandler
					.getAssetsetListGivenVersionNoUnderContract(contract,
							versionNo, financialContract);
			
			boolean isYunXin = !signUpHandler.judgeZhongHangByfinancialContractCode(financialContract.getFinancialContractUuid());
			
			
			if (!isYunXin) {
				boolean isNeedReSignUp = isNeedReSignUp(contract,financialContract.getFinancialContractUuid(),financialContract.getContractNo());
				
				result.put("isNeedReSignUp", isNeedReSignUp);
				
				result.put("opType", isNeedReSignUp? ZhonghangResponseMapSpec.SIGN_UP: ZhonghangResponseMapSpec.SIGN_OFF);
			}
			result.put("isYunXin", isYunXin);
			
			result.put("list", billingDetails);
			return jsonViewResolver.sucJsonResult(result,
					SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##getAssetSetWithVersionNo## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("操作失败，请稍后重试");
		}
	}
	
	
	private boolean isNeedReSignUp(Contract contract,String financialContractUuid,String productCode){
		
		List<String> contents = financialContractConfigurationService.getFinancialContractConfigContentContentByCode(financialContractUuid,FinancialContractConfigurationCode.SIGN_UP_CHANNEL_INFORMATION_AND_FINANCIALCONTRACT_PAYTYPE.getCode());
		
		List<Integer> paymentInstitutionList = new ArrayList<>();
		
		if (CollectionUtils.isEmpty(contents)) {
			
			logger.info(".#pushJobFromImportAssetAndContract occour  error reason is :[信托合同没有对应的签约通道配置或者支付卡类型] financialContractUuid：["+financialContractUuid+"]");
			
			return true;
			
		}
		
		for(String content:contents){
		
			Map<String,String> channel = JsonUtils.parse(content, Map.class);
			
			if (MapUtils.isNotEmpty(channel)) {
				
				paymentInstitutionList.add(Integer.valueOf(channel.get("paymentInstitution")));
				
			}
		}
		
		ContractAccount contractAccount = contractAccountService
				.getTheEfficientContractAccountBy(contract);
		
		if (contractAccount==null) {
			
			return false;
			
		}
		
		List<SignUp> signUps = signUpService.getSignUps(null, contractAccount.getPayAcNo(), null,productCode);
		
		if (CollectionUtils.isEmpty(signUps)) {
			
			return true;
			
		}
		
		List<Integer> signUpPaymentInstitutionList = signUps.stream().map(signUp ->signUp.getPaymentInstitution().ordinal()).collect(Collectors.toList());
		
		if (!signUpPaymentInstitutionList.containsAll(paymentInstitutionList)) {
			
			return true;
		}
		
		List<SignUp> signUpInNotSuccess = signUps.stream().filter(sign -> sign.getSignUpStatus()!=SignUpStatus.SUCCESS).collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(signUpInNotSuccess);

    }



	@RequestMapping(value="/signUp")
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String signUpForChannels(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "contractId") Long contractId,
			@RequestParam(value = "opType") String opType
			){
		try{
		
		Contract contract = contractService.getContract(contractId);
		
		if (contract == null) {
			
			return jsonViewResolver.errorJsonResult("参数错误");
			
		}
		
		Customer customer = customerService.getCustomer(contract.getCustomerUuid());
		
		if (customer == null) {
			
			return jsonViewResolver.errorJsonResult("没有找到所属的贷款人信息");
			
		}
		
		FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
		
		if (financialContract == null) {
			
			return jsonViewResolver.errorJsonResult("没有找到所属的信托合同");
			
		}
		
		ContractAccount contractAccount = contractAccountService
				.getTheEfficientContractAccountBy(contract);
		
		if (contractAccount==null) {
			
			return jsonViewResolver.errorJsonResult("没有找到所属的帐号信息");
			
		}
		
		String message = buildParamAndPushJob(deductBusinessNotifyJobServer, request, contractAccount, customer, financialContract, groupNameForChannelsSignUp, signTransType, notifyUrlToSignUp,opType);
		
		Map<String, Object> result = new HashMap<String,Object>(){{
			put("message", message);
		}}; 
		
		return jsonViewResolver.sucJsonResult(result);
		
		}catch (Exception e) {
			logger.error("##signUpForChannels## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("操作失败，请稍后重试");
			
		}
		
	}
	
	
	private String buildParamAndPushJob(DeductBusinessNotifyJobServer deductBusinessNotifyJobServer,HttpServletRequest request,ContractAccount contractAccount,Customer customer,FinancialContract financialContract,String groupName,String signTransType,String notifyUrlToSignUp,String opType){
		
		Map<String, String> sendToSignUpInterfaceParam = new HashMap<>();
		
		String message = "请求操作成功，请稍后查看结果";
		
		sendToSignUpInterfaceParam.put( "accName",contractAccount.getPayerName());
		
		sendToSignUpInterfaceParam.put( "accNo",contractAccount.getPayAcNo());
		
		sendToSignUpInterfaceParam.put( "certId",contractAccount.getIdCardNum());
		
		sendToSignUpInterfaceParam.put("bankAliasName", contractAccount.getBankCode());
		
		sendToSignUpInterfaceParam.put("bankFullName", StringUtils.EMPTY);
		
		sendToSignUpInterfaceParam.put( "phoneNo",customer.getMobile()==null?StringUtils.EMPTY:customer.getMobile());
		
		List<NotifyApplication> jobList = signUpHandler.pushJobFromImportAssetAndContract(sendToSignUpInterfaceParam, financialContract.getFinancialContractUuid(), financialContract.getContractNo(), notifyUrlToSignUp, zhonghangMerId, zhonghangSecret, signTransType, groupNameForChannelsSignUp,opType);
		
		if (CollectionUtils.isEmpty(jobList)) {
			
			message = ".#手动签约或解约失败, 贷款合同id为：["+contractAccount.getContract().getId()+"] 卡号为["+contractAccount.getPayAcNo()+"]";  
			
			logger.error(message);
			
			return message;
			
		}
		
		deductBusinessNotifyJobServer.pushJobs(jobList);
		
		return message;
			
		
	}

	//还款记录
	@RequestMapping(value = "/detail/repaymentRecordDetail/{contractId}")
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String showRepaymentHistoryList(
			@PathVariable(value = "contractId") Long contractId) {
		try {
			List<RepaymentRecordDetail> repaymentRecordDetails= journalVoucherHandler
					.getRepaymentRecordDetailList(contractId);
			return jsonViewResolver.sucJsonResult("list", repaymentRecordDetails,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("##getRepaymentHistoryList## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询失败，请稍后重试");
		}
	}
	

	// 更新还款计划的修改
	@RequestMapping(value = "/detail/updateRepaymentPlan", method = RequestMethod.POST)
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String updateRepaymentPlan(
			@RequestParam(value = "contractId") Long contractId,
			@RequestParam("modifyCode") Integer modifyCode,
			@RequestParam("data") String modelsStr, HttpServletRequest request,
			@Secure Principal principal) {
		try {
			if(modifyCode == null){
				return jsonViewResolver.errorJsonResult("请选择变更原因！");
			}
			RepaymentPlanModifyReason modifyReason = RepaymentPlanModifyReason.fromValue(modifyCode);
			if(modifyReason == null){
				return jsonViewResolver.errorJsonResult("变更原因错误！");
			}
			List<RepaymentPlanModifyRequestDataModel> models = JSON.parseArray(modelsStr, RepaymentPlanModifyRequestDataModel.class);
			if (CollectionUtils.isEmpty(models)) {
				return jsonViewResolver.errorJsonResult("提交的数据为空，修改失败");
			}
			models = models.stream().sorted(Comparator
					.comparing(RepaymentPlanModifyRequestDataModel::getDate))
					.collect(Collectors.toList());
			Contract contract = contractService.getContract(contractId);
			if (contract == null) {
				return jsonViewResolver.errorJsonResult("参数错误，请修改后重试");
			}
			FinancialContract financialContract = financialContractService
					.getFinancialContractBy(contract.getFinancialContractUuid());
			if (financialContract == null) {
				return jsonViewResolver.errorJsonResult("该贷款合同对应的信托合同不存在");
			}
			// 判断该贷款合同是否允许变更当日的还款计划
			boolean unusualModifyFlag = financialContract.isUnusualModifyFlag();
			Date startDate = getStartDate(financialContract);
			// 判断贷款合同允许变更当日，且存在变更当日的还款计划。
//			boolean isIncludeToday = unusualModifyFlag
//					&& hasModifyTodayPlan(models);
			String errMsg = data_Check(contract, models, startDate);
			if (StringUtils.isNotBlank(errMsg)) {
				return jsonViewResolver.errorJsonResult(errMsg);
			}
			 //剩余未还的还款计划（还款计划分为几期，这里表示后面几期还没有偿还的还款计划）
			List<AssetSet> assetSets = Collections.emptyList();
			if(contract != null){
				assetSets = repaymentPlanService
						.getAllAssetListBy(contract.getUuid(), startDate);
			}
			if (CollectionUtils.isEmpty(assetSets)) {
				return jsonViewResolver.errorJsonResult("当前贷款合同无法变更");
			}
			long count = assetSets
					.parallelStream()
					.filter(a -> !StringUtils.equals(AssetSet.EMPTY_UUID,
							(a.getActiveDeductApplicationUuid()))).count();
			if (count > 0) {
				return jsonViewResolver.errorJsonResult("存在当日扣款成功或处理中的还款计划！");
			}
			if(isInPaying(assetSets)){
				return jsonViewResolver.errorJsonResult("存在处于\"支付中\"的还款计划，不允许变更");
			}
			if(modifyReason == RepaymentPlanModifyReason.REASON_1){
				if(models.size() > 1){
					return jsonViewResolver.errorJsonResult("变更原因为提前结清时，未偿还款计划只能有一条！");
				}
			}
			int cnt = getCount(assetSets, models);
			if(cnt == 0){
				return jsonViewResolver.errorJsonResult("与原计划一致，不予变更!");
			}
			Integer oldVersion = assetSetApiHandler.modifyRepaymentPlanManual(contract, models, startDate, modifyCode, IpUtil.getIpAddress(request));
			SystemOperateLog log = recordLogCore.insertNormalRecordLog(
					principal.getId(), IpUtil.getIpAddress(request),
					LogFunctionType.ASSET_SET_MODIFY, LogOperateType.UPDATE,
					contract);
			log.setKeyContent(contract.getUuid());
			Integer contractNewVersion = contractService.getActiveVersionNo(contract.getUuid());
			StringBuffer recordContentBuffer = new StringBuffer();
			recordContentBuffer.append("变更还款计划，计划版本由【 版本" + oldVersion + "】变更为【 版本"
					+ contractNewVersion + "】" + "，变更原因为：" + modifyReason.getChineseMessage());
			log.setRecordContent(recordContentBuffer.toString());
			systemOperateLogService.saveOrUpdate(log);
			return jsonViewResolver.sucJsonResult();
		} catch (ApiException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (GlobalRuntimeException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			logger.error("##updateRepaymentPlan## occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("提交失败，请稍后重试");
		}
	}

	private boolean isInPaying(List<AssetSet> assetSets) {
		if(CollectionUtils.isEmpty(assetSets)){
			return false;
		}
		for (AssetSet assetSet: assetSets){
			if(repaymentPlanExtraDataService.isInPayingStatus(assetSet.getAssetUuid())){
				return true;
			}
		}
		return false;
	}

	private boolean hasModifyTodayPlan(
			List<RepaymentPlanModifyRequestDataModel> models) {
		return models
				.stream()
				.filter(model -> DateUtils.isSameDay(DateUtils.getToday(),
						model.getDate())).count() > 0;
	}

	private String data_Check(Contract contract,
			List<RepaymentPlanModifyRequestDataModel> models,
							  Date startDate) {
		Date contractBeginDate = contract.getBeginDate();
		Date planBeginDate = models.get(0).getDate();
		// 计划还款日期不能早于贷款合同开始日期，否则抛出异常
		if (planBeginDate == null || planBeginDate.before(contractBeginDate)) {
			return "计划还款日期不能为空或早于贷款合同开始日期 ";
		}
		Date contractEndDate = contract.getEndDate();
		if (contractEndDate != null) {
			Date planEndDate = models.get(models.size() - 1).getDate();
			// 2017-05-18加限制，变更计划还款日期在贷款合同终止日期后108天内
			if (planEndDate.after(DateUtils.addDays(contractEndDate, 108))) {
				return "计划还款日期不能为空或晚于贷款合同终止日期后108天 ";
			}
		}

		// 本金校验
		BigDecimal planPrincipalAmount = BigDecimal.ZERO;
//		Date startDate = isIncludeToday ? DateUtils.getToday() : DateUtils
//				.addDays(DateUtils.getToday(), 1);
		// 查询修改日期及以后的剩余未还本金总额
		BigDecimal principal_amount_of_outstanding = repaymentPlanService
				.get_the_outstanding_principal_amount_of_contract(contract,
						startDate);
		for (RepaymentPlanModifyRequestDataModel assetSetUpdateModel : models) {
			BigDecimal assetPrincipal = assetSetUpdateModel.getAssetPrincipal();
			if (assetPrincipal == null
					|| BigDecimal.ZERO.compareTo(assetPrincipal) == 1) {
				return "无效的计划本金总额 ";
			}
			BigDecimal assetInterest = assetSetUpdateModel.getAssetInterest();
			if (assetInterest == null
					|| BigDecimal.ZERO.compareTo(assetInterest) == 1) {
				return "无效的计划利息总额 ";
			}
			planPrincipalAmount = planPrincipalAmount.add(assetSetUpdateModel
					.getAssetPrincipal());
			Date assetRecycleDate = assetSetUpdateModel.getDate();
			if (startDate.after(assetRecycleDate)) {
				return "该贷款合同不允许变更当日还款计划或计划还款日期排序错误，需按计划还款日期递增";
			}
			startDate = assetRecycleDate;
		}

		// 变更的划款计划的本金总额与剩余本金总额是否相同，若不同，则抛出异常
		if (planPrincipalAmount.compareTo(principal_amount_of_outstanding) != 0) {
			return "计划还款本金与原版本不相等！ ";
		}
		return "";
	}

	// 贷款合同列表页
	@RequestMapping(value = "", method = RequestMethod.GET)
	@MenuSetting("submenu-assets-contract")
	public ModelAndView showContractsPage(HttpServletRequest request,
			@Secure Principal principal,
			@ModelAttribute ContractQueryModel contractQueryModel, Page page) {
		try {
			ModelAndView modelAndView = new ModelAndView("index");
			return modelAndView;
		} catch (Exception e) {
			logger.error("#showContractsPage occur error.");
			e.printStackTrace();
			return pageViewResolver.errorSpec();
		}
	}

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public @ResponseBody String getContractListOptions(
			@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			List<App> appList = principalService.get_can_access_app_list(principal);
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("appList", appList);
			result.put("contractStates",
					EnumUtil.getKVList(ContractStateAdaptaterView.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getContractListOptions occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	// 贷款合同列表页 -- 查询
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String searchContracts(
			@ModelAttribute ContractQueryModel queryModel, Page page) {
		try {
			int size = contractService.queryContractCountBy(queryModel);
			List<Contract> contractInfos = contractService.queryContractListBy(queryModel, page);

			List<ContractListShowModel> list = new ArrayList<ContractListShowModel>();
			for (Contract contract : contractInfos) {
                List<TagConfigVO> totalTags = tagOpsHandler.getTagInfosByOuterIdentifier(contract.getUniqueId(), null);
                List<TagConfigVO> tags = totalTags.stream().filter(tagConfigVO -> !tagConfigVO.getTransitivity()).collect(Collectors.toList());
                List<TagConfigVO> transitivityTags = totalTags.stream().filter(TagConfigVO::getTransitivity).collect(Collectors.toList());

                ContractListShowModel contractListShowModel = new ContractListShowModel(contract,null, tags, transitivityTags);
				list.add(contractListShowModel);
			}

			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("size", size);

			return jsonViewResolver.sucJsonResult(data,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

//	@RequestMapping(value = "exprot-loan-contract", method = RequestMethod.GET)
//	@MenuSetting("submenu-assets-contract")
	@Deprecated
	public String exportLoanContract(@Secure Principal principal,
			HttpServletRequest request,
			@ModelAttribute ContractQueryModel queryModel,
			HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("1", principal);
		try {
			queryAndSetFinancialContractsMap(queryModel);

			exportEventLogModel.recordStartLoadDataTime();

			List<LoanContractDetailCheckExcelVO> loanContractDetailCheckExcelVOs = contractHandler.getLoanContractExcelVO(queryModel, null);

			exportEventLogModel.recordAfterLoadDataComplete(loanContractDetailCheckExcelVOs.size());

			ExcelUtil<LoanContractDetailCheckExcelVO> excelUtil = new ExcelUtil<LoanContractDetailCheckExcelVO>(LoanContractDetailCheckExcelVO.class);
			List<String> csvData = excelUtil.exportDatasToCSV(loanContractDetailCheckExcelVOs);

			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("贷款信息表", csvData);
			exportZipToClient(response, create_download_file_name(), GlobalSpec.UTF_8, csvs);

			exportEventLogModel.recordEndWriteOutTime();
		} catch (Exception e) {
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
		return null;
	}

	private void queryAndSetFinancialContractsMap(ContractQueryModel queryModel) {
		List<FinancialContract> financialContracts = financialContractService
				.getFinancialContractsByUuids(queryModel
						.getFinancialContractUuidList());
		Map<String, FinancialContract> financialContractsMap = financialContracts
				.stream().collect(
						Collectors.toMap(
								FinancialContract::getFinancialContractUuid,
								fc -> fc));
		queryModel.setFinancialContractsMap(financialContractsMap);
	}

	private String create_download_file_name() {
		return "贷款信息表" + "_"
				+ DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + ".zip";
	}

	// 导出预览
	@RequestMapping(value = "preview-exprot-loan-contract", method = RequestMethod.GET)
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String previewExportLoanContract(
			HttpServletRequest request,
			@ModelAttribute ContractQueryModel queryModel,
			HttpServletResponse response) {
		try {
			queryAndSetFinancialContractsMap(queryModel);

			Page page = new Page(0, 10);
			List<LoanContractDetailCheckExcelVO> loanContractDetailCheckExcelVOs = contractHandler
					.getLoanContractExcelVO(queryModel, page);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", loanContractDetailCheckExcelVOs);
			return jsonViewResolver.sucJsonResult(data,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#previewExportLoanContract  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("导出预览失败");
		}
	}

	@RequestMapping(value = "/add-success")
	@InfoMessage("info.contract.create.success")
	public String addSuccess() {
		return "redirect:/contracts";
	}

	@RequestMapping(value = "/invalidate", method = RequestMethod.POST)
	@MenuSetting("submenu-assets-contract")
	public @ResponseBody String invalidate(HttpServletRequest request,
			@Secure Principal principal,
			@RequestParam(value = "contractId") long contractId,
			@RequestParam(value = "comment", required = false) String comment) {
		try {
			Contract contract = contractService.getContract(contractId);
			if (contract == null) {
				return jsonViewResolver.errorJsonResult("贷款合同不存在！");
			}
			String fileName = "";

			if (ServletFileUpload.isMultipartContent(request)) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile file = multipartRequest.getFile("file");
				if(file != null){
					fileStorageHandler.uploadFile(file, principal.getName(),
							contract.getUuid(), savePath,
							FlieApplicationType.InvalidateContractAttachment);
					fileName  = file.getName();
				}
			}
			contractHandler.invalidateContract(contract, principal.getId(),
					principal.getName(), comment, IpUtil.getIpAddress(request), fileName);

			return jsonViewResolver.sucJsonResult();
		} catch (RuntimeException e) {
			logger.error("#invalidate  occur error." + e.getMessage());
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			logger.error("#invalidate  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	// 下载附件 操作
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@MenuSetting("submenu-channel-config")
	public @ResponseBody String downloadFile(
			@RequestParam(value = "contractId") long contractId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Contract contract = contractService.getContract(contractId);
			// 处理文件名
			FileStorage fileStorage = fileStorageService
					.getValidFileStorageByRelatedUuid(contract.getUuid(),
							FlieApplicationType.InvalidateContractAttachment);
			if (fileStorage == null) {
				return jsonViewResolver.errorJsonResult("没有有效附件");
			}
			if (StringUtils.isEmpty(request.getHeader("X-Requested-With"))) {
				DownloadUtils.flushFileIntoHttp(
						fileStorage.getOriginFileName(),
						fileStorage.getSaveFileName(), savePath, response);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#downloadFile# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("下载附件失败");
		}
	}

	/**
	 * 校验提前还款资格
	 * 
	 * @param contractId
	 *            贷款合同id
	 * @return
	 */
	@RequestMapping(value = "/prepayment/check", method = RequestMethod.POST)
	public @ResponseBody String checkPrepaymentQualification(
			@RequestParam("contractId") Long contractId) {
		try {
			Map<String, Date> dateMap = prepaymentHandler.checkPrepaymentApplication(contractId);
			return jsonViewResolver.sucJsonResult("dateMap", dateMap);
		} catch (GlobalRuntimeException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 存入提前还款申请
	 * 
	 * @param model
	 *            提前还款申请校验模型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/prepayment/save", method = RequestMethod.POST)
	public @ResponseBody String savePrepaymentApplication(
			@Secure Principal principal,
			@ModelAttribute PrepaymentCreateModel model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!model.isValid()) {
				return jsonViewResolver.errorJsonResult("参数无效: "
						+ model.getCheckFailedMsg());
			}
			String ipAddress = IpUtil.getIpAddress(request);
			Contract contract = contractService.load(Contract.class, model.getContractId());
			if(contract == null) {
				throw new GlobalRuntimeException(PrepaymentErrorCode.CONTRACT_NOT_EXIST, PrepaymentErrorMsgSpec.CONTRACT_NOT_EXIST);
			}
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, principal.getId(),
					Priority.RealTime.getPriority());
			return jsonViewResolver.sucJsonResult();
		} catch (GlobalRuntimeException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (ApiException e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	@RequestMapping(value = "/prepayment/search-asset", method = RequestMethod.GET)
	public @ResponseBody String searchAssetInfo(@Secure Principal principal, @RequestParam("assetRecycleDate") String assetRecycleDate, @RequestParam("contractId") Long contractId) {
		try {
			Map<String, String> assetInfoAndPrincipalMap = prepaymentHandler.get_be_perd_repayment_plan_and_prepayment_principal(assetRecycleDate, contractId);
			return jsonViewResolver.sucJsonResult("assetInfoAndPrincipalMap", assetInfoAndPrincipalMap);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	private String getZero(BigDecimal num){
		if(num == null || new BigDecimal(0).equals(num)){
			return "0.00";
		}
		return num.toString()+".00";
	}

	private Date getStartDate(FinancialContract financialContract) {
		//默认正常变更最小日期为明日
		Date startDate = DateUtils.addDays(DateUtils.getToday(), 1);
		//TODO
//		List<FinancialContractConfiguration> configurations = financialContractConfigurationService.get_financialContractConfiguration_list(financialContract.getUuid());
//		String modifyFlagContent = null;
//		for(FinancialContractConfiguration configuration : configurations) {
//			if(configuration.getCode().equals(FinancialContractConfigurationCode.ALLOW_MODIFY_REPAYMENT_PLAN.getCode())) {
//				modifyFlagContent = configuration.getContent();
//			}
//		}

		String modifyFlagContent = financialContractConfigurationService.getFinancialContractConfigContentContent(
				financialContract.getUuid(),FinancialContractConfigurationCode.ALLOW_MODIFY_REPAYMENT_PLAN.getCode());

		if(org.apache.commons.lang.StringUtils.isBlank(modifyFlagContent)) {
			if(financialContract.isUnusualModifyFlag()) {
				startDate = DateUtils.getToday();
			}
		} else {
			if(modifyFlagContent.equals(FinancialContractModifyFlag.ABNORMAL.ordinal()+"")) {
				//该贷款合同允许变更当日的还款计划
				startDate = DateUtils.getToday();
			} else if (modifyFlagContent.equals(FinancialContractModifyFlag.GRACE_DAY.ordinal()+"")){
				//该贷款合同允许变更宽限日中的还款计划
				startDate = DateUtils.addDays(DateUtils.getToday(), financialContract.getAdvaRepaymentTerm() * (-1));
			}
		}
		return startDate;
	}

	private int getCount(List<AssetSet> assetSets, List<RepaymentPlanModifyRequestDataModel> models){
		Map<String, String> map = new HashMap<>();
		assetSets.stream().forEach(assetSet -> {
			List<AssetSetExtraCharge> result = this.repaymentPlanService.getAssetSetExtraChargeByUuid(assetSet.getAssetUuid());
			StringBuffer value = new StringBuffer(assetSet.getAssetRecycleDate() + "" + assetSet.getAssetPrincipalValue() + assetSet.getAssetInterestValue());
			Map<String, String> mapCase = new HashMap<>();
			result.stream().forEach(r -> {
				if(r.getSecondAccountName().equals(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE)){
					mapCase.put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE, r.getAccountAmount()+"");
				}
				if(r.getSecondAccountName().equals(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE)){
					mapCase.put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE, r.getAccountAmount()+"");
				}
				if(r.getSecondAccountName().equals(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE)){
					mapCase.put(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE, r.getAccountAmount()+"");
				}
			});
			value.append(mapCase.getOrDefault(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE,"0.00"));
			value.append(mapCase.getOrDefault(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE,"0.00"));
			value.append(mapCase.getOrDefault(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE,"0.00"));
			map.put(assetSet.getAssetUuid(), value.toString());
		});
		int cnt = models.size();
		for(RepaymentPlanModifyRequestDataModel model : models){

			String match = model.getAssetRecycleDate() + "" +
					getZero(model.getAssetPrincipal()) +
					getZero(model.getAssetInterest()) +
					getZero(model.getOtherCharge()) +
					getZero(model.getServiceCharge()) +
					getZero(model.getMaintenanceCharge());
			for(String key: map.keySet()){
				String value = map.getOrDefault(key,"");

				if(value.equals(match)){
					cnt --;
					continue;
				}
			}

		}
		return cnt;
	}


	@RequestMapping(value = "/appendices", method = RequestMethod.GET)
	@ResponseBody
	public String getAppendices(@RequestParam(value = "contractUniqueId") String objectUuid){
		try {
			List<Map<String, Object>> appendicesInfo = appendixHandler.showAppendices(objectUuid);
			return jsonViewResolver.sucJsonResult("list", appendicesInfo, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	@RequestMapping(value = "/appendices/{appendixUuid}", method = RequestMethod.GET)
	@ResponseBody
	public void downloadAppendix(@PathVariable(value = "appendixUuid") String appendixUuid,HttpServletResponse response){
		try {
			Appendix appendix = appendixService.getAppendixBy(appendixUuid);
			if(appendix == null){
				logger.error("系统无此记录");
				return;
			}
			DownloadUtils
				.flushFileIntoHttp(appendix.getFileNameForShow(),"", appendix.getFilePath(), response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@RequestMapping(value = "/appendices/{appendixUuid}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteAppendix(@PathVariable(value = "appendixUuid") String appendixUuid, @Secure Principal principal,HttpServletRequest request){
		try {
			Appendix appendix = appendixService.getAppendixBy(appendixUuid);
			if(appendix == null){
				return jsonViewResolver.errorJsonResult("数据不正确");
			}
			Contract contract = contractService.getContractByUniqueId(appendix.getObjectUuid());
			if(contract == null){
				return jsonViewResolver.errorJsonResult("数据不正确");
			}
			String appendixName = appendix.getFileNameForShow();
			appendixHandler.deleteAppendix(appendixUuid);
			SystemOperateLog log = recordLogCore.insertNormalRecordLog(
			principal.getId(), IpUtil.getIpAddress(request),LogFunctionType.REPURCHASECONTRACT,
				LogOperateType.UPDATE,contract);
			log.setKeyContent(contract.getUuid());
			log.setRecordContent("删除合同附件:" + appendixName);
			systemOperateLogService.saveOrUpdate(log);
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	/**
	 * 批量回购--下载模板
	 */
	@RequestMapping(value = "batchRepurchase/export", method = RequestMethod.GET)
	public @ResponseBody String exportRepurchaseDetails(@Secure Principal principal,HttpServletRequest request,HttpServletResponse response){
		try{
			String fileName = "批量回购模板文件.xls";
			genResponse(response,fileName);
			ExcelUtil<FileRepurchaseDetail> excelUtil = new ExcelUtil<FileRepurchaseDetail>(FileRepurchaseDetail.class);
			excelUtil.exportExcel(new ArrayList<FileRepurchaseDetail>(){{new FileRepurchaseDetail();}}, "回购模板",response.getOutputStream());

			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#exportRepurchaseDetails occur error ]: " + errorMsg);
			return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
		}
	}

	private void genResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException{
		String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + "; filename*="+ "UTF-8" +"''"+ encodeFileName);
	}

	/**
	 * 批量回购--导入文件
	 */
	@RequestMapping(value = "batchRepurchase/import", method = RequestMethod.POST)
	public @ResponseBody String importRepurchaseDetails(@Secure Principal principal,HttpServletRequest request,HttpServletResponse response){
		try{
				return jsonViewResolver.sucJsonResult(genRepurchaseDetails(request),SerializerFeature.DisableCircularReferenceDetect);
		}catch (NumberFormatException e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#importRepurchaseDetails occur error ]: " + errorMsg);
			return jsonViewResolver.errorJsonResult("文档内容错误");
		}catch (Exception e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#importRepurchaseDetails occur error ]: " + errorMsg);
			return jsonViewResolver.errorJsonResult(errorMsg);
		}
	}

	private Map<String,Object> genRepurchaseDetails(HttpServletRequest request) throws Exception{
		List<FileRepurchaseDetail> dataList = new ArrayList<>();
		List<String> names = new ArrayList<>();
		BigDecimal principal = new BigDecimal(0);
		BigDecimal interest = new BigDecimal(0);
		BigDecimal penaltyInterest = new BigDecimal(0);
		BigDecimal repurchaseOtherFee = new BigDecimal(0);
		BigDecimal amount = new BigDecimal(0);
		MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
		Iterator<String> fileNames = fileRequest.getFileNames();
		ExcelUtil<FileRepurchaseDetail> excelUtil = new ExcelUtil<FileRepurchaseDetail>(FileRepurchaseDetail.class);
		Map<String,String> headers = excelUtil.buildCsvHeader();
		while (fileNames.hasNext()) {
			List<MultipartFile> files = fileRequest.getFiles(fileNames.next());
			for (MultipartFile multipartFile : files) {
				List<FileRepurchaseDetail> modelList = excelUtil.importExcelWithinLimit(0, multipartFile.getInputStream(), 500);
				if(CollectionUtils.isEmpty(modelList))
					throw new Exception("文档无内容或内容超限(500行)");
				for (FileRepurchaseDetail t : modelList) {
					principal = principal.add(t.getPrincipal());
					interest = interest.add(t.getInterest());
					penaltyInterest = penaltyInterest.add(t.getPenaltyInterest());
					repurchaseOtherFee = repurchaseOtherFee.add(t.getRepurchaseOtherFee());
					amount = amount.add(t.getAmount());
				}
				names.add(multipartFile.getOriginalFilename());
				dataList.addAll(modelList);
			}
		}

		Map<String,Object> dataMap = new HashMap<>();

		dataMap.put("nameList", names);

		dataMap.put("headers", headers);

		dataMap.put("dataList", dataList);

		dataMap.put("principal", principal);
		dataMap.put("interest", interest);
		dataMap.put("penaltyInterest", penaltyInterest);
		dataMap.put("repurchaseOtherFee", repurchaseOtherFee);
		dataMap.put("amount", amount);

		return dataMap;
	}

	/**
	 * 批量回购--提交
	 */
	@RequestMapping(value = "batchRepurchase/submit", method = RequestMethod.POST)
	public @ResponseBody String submitBatchRepurchase(@ModelAttribute RepurchaseDetailsModel model,
			@Secure Principal principal,HttpServletRequest request){
		try{
			if (model == null || CollectionUtils.isEmpty(model.getRepurchaseDetailList())) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			FinancialContract fc = financialContractService.getFinancialContractBy(model.getFinancialContractUuid());
			if (fc == null) {
				return jsonViewResolver.errorJsonResult("信托合同不存在");
			}
			if(!fc.canBeRepurchasedViaManual()){
				return jsonViewResolver.errorJsonResult("该信托不允许回购申请");
			}
			Map<String, Object> data = new HashMap<>();
			String batchNo = GeneratorUtils.generateManualBatchNo();
			List<FileRepurchaseDetail> repurchaseDetails = repurchaseHandler.batchRepurchase(fc,model.getRepurchaseDetailList(),batchNo);
			if (CollectionUtils.isNotEmpty(repurchaseDetails)) {
				createdFile(repurchaseDetails, batchNo);
			}
			data.put("batchNo", batchNo);
			data.put("list", repurchaseDetails);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#submitBatchRepurchase occur error ]: " + errorMsg);
			return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
		}
	}

	private void createdFile(List<FileRepurchaseDetail> repurchaseDetails, String batchNo) throws IOException {
		ExcelUtil<FileRepurchaseDetail> excelUtil = new ExcelUtil<FileRepurchaseDetail>(FileRepurchaseDetail.class);
		List<String> csvData = excelUtil.exportDatasToCSV(repurchaseDetails);

		String saveFilename = genSaveFilename(batchNo);
		File fileTmp = new File(savePath);
		if (!fileTmp.exists() && !fileTmp.isDirectory()) { // 目录不存在，需要创建
			fileTmp.mkdir();  //创建目录
		}
		File file = new File((savePath + saveFilename).toString());

		FileOutputStream outputStream = new FileOutputStream(file);
		writeCSVToOutputStream(csvData, outputStream);
	}

	private String genSaveFilename(String batchNo){
		return batchNo + "_" + "回购申请处理结果.csv";
	}

	private void writeCSVToOutputStream(List<String> csvData, FileOutputStream outputStream) throws IOException {
		BufferedOutputStream bufStream = new BufferedOutputStream(outputStream);
		PrintWriter outPutStream = new PrintWriter(bufStream);
		bufStream.flush();
		bufStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
		for (String str : csvData) {
			outPutStream.println(str);
		}
		outPutStream.flush();
		outPutStream.close();
	}

	/**
	 * 批量回购--下载回购申请处理结果
	 */
	@RequestMapping(value = "batchRepurchase/download", method = RequestMethod.GET)
	public @ResponseBody String downloadBatchRepurchaseResult(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "batchNo", required = true) String batchNo){
		try{
			String fileKey = genSaveFilename(batchNo);
			File file = new File(savePath + fileKey);
			if(!file.exists()){
				logger.error("Downloading file error. FilePath: "+ savePath + fileKey);
				return jsonViewResolver.errorJsonResult("文件不存在！");
			}
			DownloadUtils.flushFileIntoHttp(fileKey, fileKey, savePath, response);
			return jsonViewResolver.sucJsonResult();
		}catch(Exception e){
			logger.error("#downloadBatchRepurchaseResult# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("文件下载失败！");
		}
	}
}

