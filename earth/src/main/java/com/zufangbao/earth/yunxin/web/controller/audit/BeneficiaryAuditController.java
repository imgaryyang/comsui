package com.zufangbao.earth.yunxin.web.controller.audit;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.earth.yunxin.handler.impl.LogMapRecordContentSpec;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec.GeneralErrorMsg;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelInformation;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.AuditJobSource;
import com.zufangbao.sun.yunxin.entity.audit.AuditResult;
import com.zufangbao.sun.yunxin.entity.audit.AuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.BeneficiaryAuditResult;
import com.zufangbao.sun.yunxin.entity.audit.ClearingStatus;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBillStat;
import com.zufangbao.sun.yunxin.entity.excel.BeneficiaryAuditResultOfCounter;
import com.zufangbao.sun.yunxin.entity.excel.BeneficiaryAuditResultOfIssued;
import com.zufangbao.sun.yunxin.entity.excel.BeneficiaryAuditResultOfLocal;
import com.zufangbao.sun.yunxin.entity.model.CashFlowQueryModelV2;
import com.zufangbao.sun.yunxin.entity.model.LocalUnissuedVoucheringCheckResultModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.audit.AuditJobQueyResultModel;
import com.zufangbao.sun.yunxin.entity.model.audit.ClearingCashFlowModeV2;
import com.zufangbao.sun.yunxin.entity.model.audit.PaymentChannelAuditJobCreateModel;
import com.zufangbao.sun.yunxin.entity.model.audit.QueryBeneficiaryAuditModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.DeductPlanDetailAdapterModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.DeductPlanDetailShowModel;
import com.zufangbao.sun.yunxin.handler.AuditJobHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import com.zufangbao.sun.yunxin.service.audit.ThirdPartyAuditBillStatService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.model.ClearingVoucherQueryResultModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.TotalReceivableBillsHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.InstitutinReconciliation.BeneficiaryAuditHandler;
import com.zufangbao.wellsfargo.yunxin.model.OppositeQueryResult;
/**
 *
 * 收款对账查询，详情页，核单
 *
 */
@Controller()
@RequestMapping("/audit/beneficiary")
@MenuSetting("menu-capital")
public class BeneficiaryAuditController extends BaseController{

    @Autowired
    private PrincipalHandler principalHandler;

    @Autowired
    private AuditJobService auditJobService;

    @Autowired
    private PaymentChannelInformationService paymentChannelInformationService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Autowired
    private AuditJobHandler auditJobHandler;

    @Autowired
    private BeneficiaryAuditResultService beneficiaryAuditResultService;

    @Autowired
    private ThirdPartyAuditBillService thirdPartyAuditBillService;

    @Autowired
    private DeductPlanService deductPlanService;


    @Autowired
    private TotalReceivableBillsService totalReceivableBillsService;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private CashFlowHandler cashFlowHandler;

    @Autowired
    private ThirdPartyAuditBillStatService thirdPartyAuditBillStatService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private BeneficiaryAuditHandler beneficiaryAuditHandler;

    @Autowired
    private DeductApplicationService deductApplicationService;

    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;

    @Autowired
	private JpmorganApiHelper jpmorganApiHelper;

    @Autowired
    private PrincipalService principalService;
    
    @Autowired
    private TotalReceivableBillsHandler totalReceivableBillsHandler;
    
   

    private static final Log logger = LogFactory.getLog(BeneficiaryAuditController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    @MenuSetting("submenu-beneficiary-audit")
    public ModelAndView load(@Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    @MenuSetting("submenu-beneficiary-audit")
    public @ResponseBody String getOption(@Secure Principal principal) {
        HashMap<String, Object> map= new HashMap<String, Object>();
        List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
		List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

		map.put("queryAppModels", queryAppModels);
        map.put("paymentInstitutionNames", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
        map.put("auditResults", EnumUtil.getKVList(AuditResult.class));
        map.put("clearingStatus", EnumUtil.getKVList(ClearingStatus.class));
        map.put("auditResultCode",  EnumUtil.getKVList(AuditResultCode.class));
        return jsonViewResolver.sucJsonResult(map);
    }

    //查询对账任务列表
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody String query(@Secure Principal principal, Page page,QueryBeneficiaryAuditModel queryBeneficiaryAuditModel ) {
        try{
            List<AuditJob> auditJobs = auditJobService.query(queryBeneficiaryAuditModel, page);
            List<AuditJobQueyResultModel> resultModel = auditJobService.convertWithTotalReceivableAmount(auditJobs);
            int jobSize = auditJobService.count(queryBeneficiaryAuditModel);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("size", jobSize);
            params.put("list", resultModel);
            return jsonViewResolver.sucJsonResult(params);
        } catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }   

	  //根据选择网关,查询相应的商户号
	  @RequestMapping(value = "query-payment-channel-pgAccount", method = RequestMethod.GET)
	  public @ResponseBody String queryPaymentChannelPgAccount(@Secure Principal principal,int paymentInstitutionOrdinal) {
	      try{
	    	  	PaymentInstitutionName paymentInstitutionName = EnumUtil.fromOrdinal(PaymentInstitutionName.class, paymentInstitutionOrdinal);
	    	  	if(paymentInstitutionName==null){
	        	  throw new ApiException("通道信息不存在");
	          	}
	    	  	List<PaymentChannelInformation> paymentChannelInformationList =  paymentChannelInformationService.getPaymentChannelInformationListBy(paymentInstitutionName);
	    	  	List<String> pgAccountList = paymentChannelInformationList.stream().map(detail -> detail.getOutlierChannelName()).distinct().collect(Collectors.toList());
	          return jsonViewResolver.sucJsonResult("pgAccountList", pgAccountList);
	      } catch(Exception e){
	          e.printStackTrace();
	          return jsonViewResolver.errorJsonResult(e,GeneralErrorMsg.MSG_SYSTEM_ERROR);
	      }
	  }
	  
	  //根据选择网关和商户号,查询相应的清算号
	  @RequestMapping(value = "query-payment-channel-clearingNo", method = RequestMethod.GET)
	  public @ResponseBody String queryPaymentChannelClearingNo(@Secure Principal principal,int paymentInstitutionOrdinal,String outlierChannelName) {
	      try{
	    	  	PaymentInstitutionName paymentInstitutionName = EnumUtil.fromOrdinal(PaymentInstitutionName.class, paymentInstitutionOrdinal);
	          if(paymentInstitutionName==null){
	        	  throw new ApiException("通道信息不存在");
	          	}
	          List<PaymentChannelInformation> paymentChannelInformationList =  paymentChannelInformationService.getPaymentChannelInformationListBy(paymentInstitutionName,outlierChannelName);
	    	  List<String> clearingNoList = paymentChannelInformationList.stream().map(detail -> detail.getClearingNo()).distinct().collect(Collectors.toList());
	          return jsonViewResolver.sucJsonResult("clearingNoList", clearingNoList);
	      } catch(Exception e){
	          e.printStackTrace();
	          return jsonViewResolver.errorJsonResult(e,GeneralErrorMsg.MSG_SYSTEM_ERROR);
	      }
	  }
   
    //对账任务详情页:导出结果
  	@RequestMapping(value = "{auditJobUuid}/detail/audit-result-export", method = RequestMethod.GET)
  	public void exportAuditResult(@PathVariable("auditJobUuid") String auditJobUuid, HttpServletResponse response){
  		try{

  			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
  			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);

            Date startTime = auditJob.getStartTime();
            Date endTime = auditJob.getEndTime();
            PaymentInstitutionName paymentGateway = auditJob.getPaymentInstitution();
            String merchantNo = auditJob.getMerchantNo();
            String pgClearingAccount = auditJob.getPgClearingAccount();

  			List<BeneficiaryAuditResult> localResultList = beneficiaryAuditResultService.getLocalOrIssuedAuditResult(auditJob.getPaymentChannelServiceUuid(), pgClearingAccount, startTime, endTime, AuditResultCode.LOCAL, null);
  			List<BeneficiaryAuditResultOfLocal> localDataExportModels = new ArrayList<>();
  			if(CollectionUtils.isNotEmpty(localResultList)){
  				localDataExportModels = localResultList.stream().map(a-> new BeneficiaryAuditResultOfLocal(a)).collect(Collectors.toList());
  			}
  			ExcelUtil<BeneficiaryAuditResultOfLocal> localExcelUtil = new ExcelUtil<BeneficiaryAuditResultOfLocal>(BeneficiaryAuditResultOfLocal.class);
  			List<String> localCsvData = localExcelUtil.exportDatasToCSV(localDataExportModels);
  			csvs.put("代收本端多账数据", localCsvData);

  			List<BeneficiaryAuditResult> counterResultList = beneficiaryAuditResultService.getCounterAuditResult(paymentGateway, merchantNo, pgClearingAccount, startTime, endTime,AuditResultCode.COUNTER, null);
  			List<BeneficiaryAuditResultOfCounter> counterDataExportModels = new ArrayList<>();
  			if(CollectionUtils.isNotEmpty(counterResultList)){
  				counterDataExportModels = counterResultList.stream().map(a->new BeneficiaryAuditResultOfCounter(a)).collect(Collectors.toList());
  			}
  			ExcelUtil<BeneficiaryAuditResultOfCounter> counterExcelUtil = new ExcelUtil<BeneficiaryAuditResultOfCounter>(BeneficiaryAuditResultOfCounter.class);
  			List<String> counterCsvData = counterExcelUtil.exportDatasToCSV(counterDataExportModels);
  			csvs.put("代收对端多账数据", counterCsvData);

  			List<BeneficiaryAuditResult> issuedResultList = beneficiaryAuditResultService.getLocalOrIssuedAuditResult(auditJob.getPaymentChannelServiceUuid(), pgClearingAccount, startTime, endTime, AuditResultCode.ISSUED, null);
  			List<BeneficiaryAuditResultOfIssued> issuedDataExportModels = new ArrayList<>();
  			if(CollectionUtils.isNotEmpty(issuedResultList)){
  				issuedDataExportModels = issuedResultList.stream().map(a->new BeneficiaryAuditResultOfIssued(a)).collect(Collectors.toList());
  			}
  			ExcelUtil<BeneficiaryAuditResultOfIssued> issuedExcelUtil = new ExcelUtil<BeneficiaryAuditResultOfIssued>(BeneficiaryAuditResultOfIssued.class);
  			List<String> issuedCsvData = issuedExcelUtil.exportDatasToCSV(issuedDataExportModels);
  			csvs.put("代收平账数据", issuedCsvData);

  			exportZipToClient(response, "对账结果_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + ".zip", GlobalSpec.UTF_8, csvs);

  		} catch (Exception e){
  			logger.error("export audit result error");
  			e.printStackTrace();
  		}
  	}


    //清算单------数据显示
    @RequestMapping(value = "{auditJobUuid}/detail/clearing-cashFlow-result", method = RequestMethod.GET)
    public @ResponseBody String ShowClearingCashFlowResult(@PathVariable("auditJobUuid") String auditJobUuid, Page page) {

        try {

            AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            if(auditJob==null){
                return jsonViewResolver.errorJsonResult("对账任务不存在");
            }

            List<ThirdPartyAuditBillStat> thirdPartyAuditBillStatList = thirdPartyAuditBillStatService.queryThirdPartyAuditBillStat(auditJob.getFinancialContractUuid(), auditJob.getPaymentInstitution(), auditJob.getMerchantNo(),
                    auditJob.getStartTime(), auditJob.getEndTime(), page);

            int count = thirdPartyAuditBillStatService.countThirdPartyAuditBillStat(auditJob.getFinancialContractUuid(), auditJob.getPaymentInstitution(), auditJob.getMerchantNo(),auditJob.getStartTime(), auditJob.getEndTime(), page);

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("list", thirdPartyAuditBillStatList);
            params.put("size", count);
            return jsonViewResolver.sucJsonResult(params);

        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }

    }


    //对账任务重新对账
    @RequestMapping(value = "{auditJobUuid}/redo", method = RequestMethod.POST)
    public @ResponseBody String redo(@Secure Principal principal,@PathVariable("auditJobUuid")String auditJobUuid,HttpServletRequest request) {
        try{
            AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            if(auditJob==null){
                return jsonViewResolver.errorJsonResult("对账任务不存在");
            }
            if(ClearingStatus.DOING == auditJob.getClearingStatus()){
            	 return jsonViewResolver.errorJsonResult("对账任务清算中");
            }
            auditJobHandler.reDoneBeneficiaryAuditResult(auditJob);
            Map<String,Object> params = new HashMap<String,Object>();

            createAuditJobLog(principal, request, auditJob,LogFunctionType.REDO_AUDIT_JOB,LogOperateType.OPERATE);

            return jsonViewResolver.sucJsonResult();
        } catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }

    //清算流水-----查询v2
    @RequestMapping(value = "clearingCashFlowQueryV2")
    public @ResponseBody String clearingCashFlowQueryV2(@Secure Principal principal,CashFlowQueryModelV2 cashFlowQueryModelV2, Page page) {
			try {
				String checkMessage = cashFlowQueryModelV2.checkIsNull();
				if(StringUtils.isNotEmpty(checkMessage)){
					throw new ApiException(checkMessage);
				}
				
				List<CashFlow> cashFlowList = cashFlowService.getClearingCashFlowsByCashFlowQueryModelV2(cashFlowQueryModelV2,page);
				List<ClearingCashFlowModeV2> clearingCashFlowModes = cashFlowHandler.convert(cashFlowList);
				int count = cashFlowService.getClearingCashFlowNumbersByCashFlowQueryModelV2(cashFlowQueryModelV2);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("list", clearingCashFlowModes);
				params.put("size", count);
				return jsonViewResolver.sucJsonResult(params);

			} catch (Exception e) {
				e.printStackTrace();
				return jsonViewResolver.errorJsonResult(e, "系统错误");
			}
		}
    
 	//清算流水---关联V2
	@RequestMapping(value = "reTouchingV2", method = RequestMethod.POST)
	public @ResponseBody String reTouchingV2(@Secure Principal principal,@RequestParam(value = "clearingCashFlowModeList") String clearingCashFlowModeList,@RequestParam("auditJobUuidList")String auditJobUuidList,HttpServletRequest request,String remark) {
		
		try {
			String checkResult = auditJobHandler.checkreTouchingV2Params(clearingCashFlowModeList, auditJobUuidList);
			if(StringUtils.isNotEmpty(checkResult)){
				throw new ApiException(checkResult);
			}
			cashFlowHandler.relatedClearingCashFlowV2(auditJobUuidList, clearingCashFlowModeList,remark);
			List<String> auditJobUuids= JsonUtils.parseArray(auditJobUuidList, String.class);
			List<ClearingCashFlowModeV2> clearingCashFlowModeV2List= JsonUtils.parseArray(clearingCashFlowModeList, ClearingCashFlowModeV2.class);
			StringBuffer recordContent = new StringBuffer();
			recordContent.append("对账任务【 "+auditJobUuidList+" 】");
			for (ClearingCashFlowModeV2 flashFlow : clearingCashFlowModeV2List) {
				recordContent.append("关联流水号【"+flashFlow.getSerialNo()+"】，关联金额" + flashFlow.getRelatedAmount());
			}
			for(String auditJobUuid : auditJobUuids){
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
			systemOperateLogHandler.operateLog(principal,IpUtil.getIpAddress(request), LogFunctionType.THIRD_PARTY_AUDIT_MATCH_CASH_FLOW,
					LogOperateType.ADD, auditJob,auditJobUuid, recordContent.toString());
			}
			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}

		}

		private void createAuditJobLog(Principal principal, HttpServletRequest request, AuditJob auditJob,LogFunctionType logFunctionType,LogOperateType logOperateType) {
			SystemOperateLog systemOperateLog = SystemOperateLog.createLog(
                    principal.getId(), null, IpUtil.getIpAddress(request),
                    logFunctionType,
					logOperateType, auditJob.getUuid(),
					auditJob.getAuditJobNo());
			systemOperateLog.setRecordContent(LogMapRecordContentSpec.logFunctionTypeMatchRecordContentHeadName
					.get(systemOperateLog.getLogFunctionType()) + systemOperateLog.getKeyContent());
			systemOperateLogService.save(systemOperateLog);
		}
		
		private LocalUnissuedVoucheringCheckResultModel simpleCheck(String auditJobUuid,String auditResultUuid){
				LocalUnissuedVoucheringCheckResultModel model = new LocalUnissuedVoucheringCheckResultModel();

				if(StringUtils.isEmpty(auditJobUuid) || StringUtils.isEmpty(auditResultUuid)){
		    		model.setMessage("存在为空参数");
		    		return model;
			   }
		    	AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
		      if(auditJob==null){
		    	  model.setMessage("对账任务不存在");
		    	  return model;
		       }
		      model.setAuditJob(auditJob);
		      BeneficiaryAuditResult beneficiaryAuditResult = beneficiaryAuditResultService.getBeneficiaryAuditResultByUuid(auditResultUuid);
		      if(beneficiaryAuditResult==null){
		    	  model.setMessage("对账结果不存在");
		    	  return model;
		      }
		      model.setBeneficiaryAuditResult(beneficiaryAuditResult);
		      if(StringUtils.isNotEmpty(beneficiaryAuditResult.getCashFlowIdentity())){
		    	  model.setMessage("存在第三方流水");
		    	  return model;
		      }
		      if(StringUtils.isEmpty(beneficiaryAuditResult.getPaymentChannelUuid())){
		    	  model.setMessage("支付通道不存在");
		    	  return model;
		      }
		     if(StringUtils.isEmpty(beneficiaryAuditResult.getTradeUuid())){
		    	  model.setMessage("通道请求号不存在");
		    	  return model;
		      }
		     DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(beneficiaryAuditResult.getSystemBillIdentity());
		     if(deductPlan==null){
		    	  model.setMessage("扣款计划单不存在");
		    	  return model;
		      }
		     model.setDeductPlan(deductPlan);
		    return model ;
		}
		
    //本端多账----核单
    @RequestMapping(value = "{auditJobUuid}/vouching/local", method = RequestMethod.POST)
    public @ResponseBody String local_unissued_vouchering(@Secure Principal principal,@PathVariable("auditJobUuid")String auditJobUuid,@RequestParam("auditResultUuid") String auditResultUuid,HttpServletRequest request) {
        
    	try {
        		LocalUnissuedVoucheringCheckResultModel model = simpleCheck(auditJobUuid, auditResultUuid);
        		if(StringUtils.isNotEmpty(model.getMessage())){
        			 return jsonViewResolver.errorJsonResult(model.getMessage());
        		}
        		//1 置成功为失败，账本回滚:需无对端流水
             AuditJob auditJob = model.getAuditJob();
             BeneficiaryAuditResult beneficiaryAuditResult = model.getBeneficiaryAuditResult();
             DeductPlan deductPlan =	model.getDeductPlan();
             
             Result result = jpmorganApiHelper.queryOppositeStatus(beneficiaryAuditResult.getPaymentChannelUuid(), beneficiaryAuditResult.getTradeUuid());

             OppositeQueryResult oppositeQueryResult = new OppositeQueryResult(result, false);

             if(!oppositeQueryResult.isSuccess()){
             	return jsonViewResolver.errorJsonResult(oppositeQueryResult.getErrorMsg());
             	}
	          if(deductPlan.getExecutionStatus()!=DeductApplicationExecutionStatus.SUCCESS){
                return jsonViewResolver.errorJsonResult("扣款单状态不为成功");
	          	}
             String contractUuid = getContractUuid(deductPlan);
             beneficiaryAuditHandler.lapse_recovered_deduct_application_handler(contractUuid, beneficiaryAuditResult.getSystemBillIdentity(), Priority.High.getPriority());

             	//本端多账  核单 log
            SystemOperateLogRequestParam param = getSystemOperateLocalUnissued(principal, request,beneficiaryAuditResult,auditJob);
            systemOperateLogHandler.generateSystemOperateLog(param);

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }

    
    private String getContractUuid(DeductPlan deductPlan) {
        if(!StringUtils.isEmpty(deductPlan.getContractNo())){
            Contract contract = contractService.getContractByContractNo(deductPlan.getContractNo());
            if(contract!=null){
                return contract.getUuid();
            }
        }
        return contractService.getContractUuidByUniqueId(deductPlan.getContractUniqueId());
    }

    //对端多账----核单
    @RequestMapping(value = "{auditJobUuid}/vouching/counter", method = RequestMethod.POST)
    public @ResponseBody String counter_unissued(@Secure Principal principal,@PathVariable("auditJobUuid")String auditJobUuid, @RequestParam("auditResultUuid") String auditResultUuid,HttpServletRequest request) {

        try {
            //1. 置失败为成功
            AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            if(auditJob==null){
                return jsonViewResolver.errorJsonResult("对账任务不存在");
            }
            BeneficiaryAuditResult beneficiaryAuditResult = beneficiaryAuditResultService.getBeneficiaryAuditResultByUuid(auditResultUuid);
            if(beneficiaryAuditResult==null){
                return jsonViewResolver.errorJsonResult("对账结果不存在");
            }
            ThirdPartyAuditBill thirdPartyAuditBill = thirdPartyAuditBillService.getThirdPartyAuditBillByAuditBillUuid(beneficiaryAuditResult.getCashFlowIdentity());
            if(thirdPartyAuditBill==null){
                return jsonViewResolver.errorJsonResult("第三方流水不存在");
            }

//            ThirdPartyTransactionRecord thirdPartyTransactionRecord = thirdPartyTransactionRecordService.getThirdPartyTransactionRecordBytradeUuid(beneficiaryAuditResult.getTradeUuid());
//            if(thirdPartyTransactionRecord == null){
//                return jsonViewResolver.errorJsonResult("核单失败");
//            }

            Result result = jpmorganApiHelper.queryOppositeStatus(beneficiaryAuditResult.getPaymentChannelUuid(), beneficiaryAuditResult.getTradeUuid());
            OppositeQueryResult oppositeQueryResult = new OppositeQueryResult(result);

            if(!oppositeQueryResult.isSuccess()){

            	return jsonViewResolver.errorJsonResult(oppositeQueryResult.getErrorMsg());
            }

            DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(beneficiaryAuditResult.getSystemBillIdentity());
            if(deductPlan==null){
                return jsonViewResolver.errorJsonResult("扣款计划单不存在");
            }
            if(deductPlan.getExecutionStatus()!=DeductApplicationExecutionStatus.FAIL){
                return jsonViewResolver.errorJsonResult("扣款计划单状态不为失败");
            }
            DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductPlan.getDeductApplicationUuid());
            if(deductApplication==null){
                return jsonViewResolver.errorJsonResult("扣款申请单不存在");
            }
            if(deductApplication.getExecutionStatus()!=DeductApplicationExecutionStatus.FAIL){
                return jsonViewResolver.errorJsonResult("扣款申请单状态不为失败");
            }
            beneficiaryAuditHandler.update_suc_deduct_application_and_deduct_plan(deductPlan, deductApplication, thirdPartyAuditBill.getSettleDate());

            //对端多账  核单 log
            SystemOperateLogRequestParam param = getSystemOperateCounterUnissued(principal, request,beneficiaryAuditResult,auditJob);
            systemOperateLogHandler.generateSystemOperateLog(param);

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }

    private SystemOperateLogRequestParam getSystemOperateLocalUnissued(Principal principal,
                                                                       HttpServletRequest request,BeneficiaryAuditResult beneficiaryAuditResult ,AuditJob auditJob) {
        SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), ",来源单号为[" + beneficiaryAuditResult.getSystemBillIdentity() + "]", LogFunctionType.LOCAL_UNISSUED,
                LogOperateType.ADD, AuditJob.class, auditJob, null, null);
        return param;

    }

    private SystemOperateLogRequestParam getSystemOperateCounterUnissued(Principal principal,
                                                                         HttpServletRequest request,BeneficiaryAuditResult beneficiaryAuditResult  ,AuditJob auditJob) {
        SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), ",来源单号为[" + beneficiaryAuditResult.getCashFlowIdentity() + "]", LogFunctionType.COUNTER_UNISSUED,
                LogOperateType.ADD, AuditJob.class, auditJob, null, null);
        return param;

    }

    //新增对账任务
    @RequestMapping(value = "create-audit-job", method = RequestMethod.GET)
    public @ResponseBody String createNewAuditJobTrans(@Secure Principal principal,HttpServletRequest request, PaymentChannelAuditJobCreateModel paymentChannelAuditJobCreateModel) {
        try{
            if(!paymentChannelAuditJobCreateModel.isValid()){
                return jsonViewResolver.errorJsonResult(paymentChannelAuditJobCreateModel.getCheckErrorMsg());
            }
            List<PaymentChannelInformation> paymentchannnel = paymentChannelInformationService.getPaymentChannelInformationBy(paymentChannelAuditJobCreateModel.getPaymentInstitutionNameEnum(), paymentChannelAuditJobCreateModel.getPgAccount(), paymentChannelAuditJobCreateModel.getPgClearingAccount());
            if(CollectionUtils.isEmpty(paymentchannnel)){
            	 return jsonViewResolver.errorJsonResult("交易通道信息不存在");
            }
            AccountSide accountSide = paymentChannelAuditJobCreateModel.getAccountSideEnum();
            AuditJob auditJob = auditJobService.createAuditJob(paymentchannnel.get(0), paymentChannelAuditJobCreateModel.getStartTime_date(), paymentChannelAuditJobCreateModel.getEndTime_date(), AuditJobSource.MANUAL,accountSide);
            createAuditJobLog(principal, request, auditJob,LogFunctionType.CREATE_AUDIT_JOB,LogOperateType.ADD);
            return jsonViewResolver.sucJsonResult("auditJob", auditJob);
        } catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
	}

    
    //对账任务详情页:基本信息以及统计结果
    @RequestMapping(value = "{auditJobUuid}/detail/basic-info", method = RequestMethod.GET)
    public @ResponseBody String showBasicInfoTrans(@PathVariable("auditJobUuid") String auditJobUuid) {
        try{
            AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            if(auditJob==null){
                return jsonViewResolver.errorJsonResult("对账任务不存在");
            }
            String pgClearingAccount = auditJob.getPgClearingAccount();
            Date startTime = auditJob.getStartTime();
            Date endTime = auditJob.getEndTime();
            PaymentInstitutionName paymentGateway = auditJob.getPaymentInstitution();
            String merchantNo = auditJob.getMerchantNo();
            //总应收单
            TotalReceivableBills totalReceivableBills = totalReceivableBillsHandler.queryTotalReceivableBillsByAuditJob(auditJob, null);
            if(totalReceivableBills==null){
            	return jsonViewResolver.errorJsonResult("对账任务无对账结果信息");
            }
           
            //对端流水
            int detailNum = 0; //收入明细
            BigDecimal detailAmount = BigDecimal.ZERO; //收入金额
            List<ThirdPartyAuditBillStat> thirdPartyAuditBillStatList = thirdPartyAuditBillStatService.queryThirdPartyAuditBillStat(paymentGateway, merchantNo, pgClearingAccount, startTime, endTime, null);
            if(!CollectionUtils.isEmpty(thirdPartyAuditBillStatList)){
                for (ThirdPartyAuditBillStat thirdPartyAuditBillStat : thirdPartyAuditBillStatList) {
                    detailNum = detailNum+thirdPartyAuditBillStat.getSumCount();
                    detailAmount = thirdPartyAuditBillStat.getSumAmount().add(detailAmount);
                }
            }
            
            //清算凭证
            ClearingVoucher clearingVoucher = totalReceivableBillsHandler.queryClearingVoucherByTotalReceivableBills(totalReceivableBills, null);
            ClearingVoucherQueryResultModel clearingVoucherQueryResultModel = null;
            if(clearingVoucher != null){
            	clearingVoucherQueryResultModel = new ClearingVoucherQueryResultModel(clearingVoucher);
            }
            //通道信息
            PaymentChannelInformation paymentChannel = paymentChannelInformationService.getPaymentChannelInformationBy(auditJob.getPaymentChannelUuid());

            List<AuditJob> auditJobs = new ArrayList<AuditJob>();
            auditJobs.add(auditJob);
            List<AuditJobQueyResultModel> AuditJobQueyResultModel = auditJobService.convert(auditJobs);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("auditJob", AuditJobQueyResultModel.get(0));
            params.put("paymentChannel", paymentChannel);
            params.put("totalReceivableBills", totalReceivableBills);
            params.put("detailNum", detailNum);
            params.put("detailAmount", detailAmount);
            params.put("clearingVoucher", clearingVoucherQueryResultModel);
            return jsonViewResolver.sucJsonResult(params);
        }catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }

    //对账任务详情页:对账结果数据展示
    @RequestMapping(value = "{auditJobUuid}/detail/audit-result", method = RequestMethod.GET)
    public @ResponseBody String ShowAuditResultTrans(@PathVariable("auditJobUuid") String auditJobUuid, Integer resultCode, Page page) {
        try{
            AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            if(auditJob == null){
            	return jsonViewResolver.errorJsonResult("对账任务不存在");
            }
            AuditResultCode auditResultCode = EnumUtil.fromCode(AuditResultCode.class, resultCode);
            int count = 0;
            String pgClearingAccount = auditJob.getPgClearingAccount();
            Date startTime = auditJob.getStartTime();
            Date endTime = auditJob.getEndTime();

            PaymentInstitutionName paymentGateway = auditJob.getPaymentInstitution();
            String merchantNo = auditJob.getMerchantNo();

            List<BeneficiaryAuditResult> beneficiaryAuditResultList = null;
            if(auditResultCode==AuditResultCode.LOCAL||auditResultCode==AuditResultCode.ISSUED){
            	beneficiaryAuditResultList = beneficiaryAuditResultService.getLocalOrIssuedAuditResult(auditJob.getPaymentChannelServiceUuid(), pgClearingAccount, startTime, endTime, auditResultCode, page);
                count = beneficiaryAuditResultService.countLocalOrIssueAuditResult(auditJob.getPaymentChannelServiceUuid(), pgClearingAccount, startTime, endTime, auditResultCode);
            } else if(auditResultCode==AuditResultCode.COUNTER){
            	beneficiaryAuditResultList = beneficiaryAuditResultService.getCounterAuditResult(paymentGateway, merchantNo, pgClearingAccount, startTime, endTime,auditResultCode, page);
                count = beneficiaryAuditResultService.countCounterAuditResult(paymentGateway , merchantNo, pgClearingAccount, startTime, endTime, auditResultCode, page);
            }
            
    		List<DeductPlanDetailAdapterModel> showModelList = new ArrayList<DeductPlanDetailAdapterModel>();
    		for (BeneficiaryAuditResult beneficiaryAuditResult : beneficiaryAuditResultList) {
    			if(beneficiaryAuditResult==null){
    				continue;
    			}
    			DeductPlanDetailAdapterModel adapterModel = null;
    			if(beneficiaryAuditResult.getSystemBillIdentity()!=null){
    				DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(beneficiaryAuditResult.getSystemBillIdentity());
    				DeductPlanDetailShowModel detailModel = new DeductPlanDetailShowModel(deductPlan);
    				adapterModel = new DeductPlanDetailAdapterModel(detailModel, beneficiaryAuditResult);
    			}else{
    				ThirdPartyAuditBill thirdPartyAuditBill = thirdPartyAuditBillService.getThirdPartyAuditBill(beneficiaryAuditResult.getCashFlowFstMerchantNo(), beneficiaryAuditResult.getTradeUuid());
    				adapterModel = new DeductPlanDetailAdapterModel();
    				adapterModel.setBeneficiaryAuditResult(beneficiaryAuditResult);
    				adapterModel.setCounterAccountNo(thirdPartyAuditBill.getCounterAccountNo());
    				adapterModel.setCounterAccountName(thirdPartyAuditBill.getCounterAccountName());
    			}
    			showModelList.add(adapterModel);
    		}
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("list", showModelList);
            params.put("size", count);
            return jsonViewResolver.sucJsonResult(params);
        }catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }
    
    //对账任务金额更新
    @RequestMapping(value = "{auditJobUuid}/update/auditJob-amount", method = RequestMethod.GET)
    public @ResponseBody String updateAuditJob(@PathVariable("auditJobUuid") String auditJobUuid) {
        try{
            AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            if(auditJob==null){
                return jsonViewResolver.errorJsonResult("对账任务不存在");
            }
            if(ClearingStatus.UNDO != auditJob.getClearingStatus()){
            	return jsonViewResolver.errorJsonResult("对账任务处于清算状态,无法更新");
            }
            auditJobHandler.doneBeneficiaryAuditResultTrans(auditJob);
            //总应收单
            TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(auditJob.getUuid());
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("totalReceivableBills", totalReceivableBills);
            return jsonViewResolver.sucJsonResult(params);
        }catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }
    
    @RequestMapping(value = "{auditJobUuid}/flow/reFresh", method = RequestMethod.GET)
    public @ResponseBody String reFreshFlowInfo(@PathVariable("auditJobUuid") String auditJobUuid) {
    	try {
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
			if(auditJob==null){
			    return jsonViewResolver.errorJsonResult("对账任务不存在");
			}
			int detailNum = 0; //收入明细
			BigDecimal detailAmount = BigDecimal.ZERO; //收入金额
			//对端流水
			List<ThirdPartyAuditBillStat> thirdPartyAuditBillStatList = thirdPartyAuditBillStatService.queryThirdPartyAuditBillStat(auditJob.getPaymentInstitution(), auditJob.getMerchantNo(),
					auditJob.getPgClearingAccount(), auditJob.getStartTime(), auditJob.getEndTime(), null);
			if(!CollectionUtils.isEmpty(thirdPartyAuditBillStatList)){
			    for (ThirdPartyAuditBillStat thirdPartyAuditBillStat : thirdPartyAuditBillStatList) {
			        detailNum = detailNum+thirdPartyAuditBillStat.getSumCount();
			        detailAmount = thirdPartyAuditBillStat.getSumAmount().add(detailAmount);
			    }
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("detailNum", detailNum);
			params.put("detailAmount", detailAmount);
			return jsonViewResolver.sucJsonResult(params);
		} catch (Exception e) {
			 e.printStackTrace();
	         return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
    	
    }

}