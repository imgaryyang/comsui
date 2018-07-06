package com.zufangbao.earth.yunxin.web.controller.voucher;

import com.zufangbao.sun.service.PaymentChannelInformationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.StringUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.web.controller.VoucherController;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec.GeneralErrorMsg;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.AuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.ClearingStatus;
import com.zufangbao.sun.yunxin.entity.model.audit.ClearingVoucherCapitalDetail;
import com.zufangbao.sun.yunxin.entity.model.deduct.DeductPlanDetailShowModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.ClearingVoucherListQueryModel;
import com.zufangbao.sun.yunxin.handler.AuditCapitalDetailHandler;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.model.ClearingVoucherQueryResultModel;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucherStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucherStatus;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentRecordDetailForDeduct;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.sun.yunxin.handler.ClearingVoucherHandler;
import com.zufangbao.sun.yunxin.service.ClearingVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;


/**
 * 清算凭证页面
 * 
 * @author hjl
 *  
 */
@Controller("clearingVoucherController")
@RequestMapping("/clearingVoucher")
@MenuSetting("menu-capital")
public class ClearingVoucherController extends BaseController {
	private static final Log logger = LogFactory.getLog(VoucherController.class);
	
	@Autowired
	private DeductPlanService deductPlanService;
	
	@Autowired
	private ClearingVoucherService clearingVoucherService;

	@Autowired
	private ClearingVoucherHandler clearingVoucherHandler;

	@Autowired
	private AuditJobService auditJobService;
	
	@Autowired
	private CashFlowService cashFlowService;

	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;
	
	@Autowired
	private DeductApplicationCoreOperationHandler deductApplicationCoreOperationHandler;
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private JournalVoucherService journalVoucherService;
	
	@Autowired
	private JournalVoucherHandler journalVoucherHandler;
	
	@Autowired
	private BeneficiaryAuditResultService beneficiaryAuditResultService;
	
	@Autowired
	private AuditCapitalDetailHandler auditCapitalDetailHandler;
	
	

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-clearing-voucher")
	 public @ResponseBody String getOption(@Secure Principal principal) {
        HashMap<String, Object> map= new HashMap<String, Object>();
        map.put("paymentInstitutionNames", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
//        map.put("paymentInstitutionNames", EnumUtil.getKVList(PaymentInstitutionName.class));
        map.put("clearingVoucherStatus", EnumUtil.getKVList(ClearingVoucherStatus.class));
        return jsonViewResolver.sucJsonResult(map);
    }
	
	@RequestMapping(value = "/voucherList", method = RequestMethod.GET)
	public @ResponseBody String clearingVoucherControllerList(@ModelAttribute ClearingVoucherListQueryModel clearingVoucherListQueryModel,Page page){
		try{
			Map<String, Object> data = new HashMap<>();
			List<ClearingVoucherQueryResultModel> clearingVoucherQueryResultModelList = new ArrayList<>();
			
			if(clearingVoucherListQueryModel == null || clearingVoucherListQueryModel.paymentInstitutionIsNull()){
				data.put("clearingVoucherQueryResultModelList", clearingVoucherQueryResultModelList);
				data.put("size",0);
				return jsonViewResolver.sucJsonResult(data);
			}
			List<ClearingVoucher> clearingVocuhers = clearingVoucherService.queryClearingVoucherListByQueryModel(clearingVoucherListQueryModel, page);
			clearingVoucherQueryResultModelList = clearingVoucherService.convert(clearingVocuhers);
			int size = clearingVoucherService.countClearingVoucherListByQueryModel(clearingVoucherListQueryModel);
			
			data.put("list", clearingVoucherQueryResultModelList);
			data.put("size",size);
			return jsonViewResolver.sucJsonResult(data);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, QUERY_ERROR);
		}
	}
	
	@RequestMapping(value = "/{clearingVoucherUuid}/queryAuditJob", method = RequestMethod.GET)
	public @ResponseBody String queryAuditJob(@PathVariable("clearingVoucherUuid") String clearingVoucherUuid){
		try{
			if(StringUtils.isEmpty(clearingVoucherUuid)){
				throw new ApiException("清算凭证号不能为空");
			}
			List<AuditJob> auditJobList= clearingVoucherHandler.queryAuditJobByVoucherUuid(clearingVoucherUuid);
			
			return jsonViewResolver.sucJsonResult("auditJobList",auditJobList);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, QUERY_ERROR);
		}
	}
	//清算凭证详情页:清算失败的线上支付单信息
	@RequestMapping(value = "/{auditJobUuid}/queryFairDeductPlan", method = RequestMethod.GET)
	public @ResponseBody String queryFairDeductPlan(@RequestParam String clearingVoucherUuid,@PathVariable("auditJobUuid") String auditJobUuid,Page page){
		try{
			ClearingVoucher clearingVoucher = clearingVoucherService.getClearingVoucherByUuid(clearingVoucherUuid);
            if(clearingVoucher==null){
                return jsonViewResolver.errorJsonResult("清算凭证不存在");
            }
            if(ClearingVoucherStatus.DONE != clearingVoucher.getClearingVoucherStatus()){
            	return jsonViewResolver.errorJsonResult("清算凭证处理中");
            }
			if(StringUtils.isEmpty(auditJobUuid)){
				throw new ApiException("对账任务Uuid不能为空");
			} 
			AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
			if(auditJob == null){
				throw new ApiException("对账任务不存在");
			}
			 List<String> deductPlanUuidList= beneficiaryAuditResultService.queryDeductPlanByAuditJob(auditJob, AuditResultCode.ISSUED, null, page);
 			 List<DeductPlan> deductPlanList = deductPlanService.getDeductPlanByDeductPlanUuidList(deductPlanUuidList);
 			 List<DeductPlanDetailShowModel> deductPlanDetailShowModelList = new ArrayList<DeductPlanDetailShowModel>();
 			 for(DeductPlan deductPlan : deductPlanList){
 			 	 DeductPlanDetailShowModel detailModel = new DeductPlanDetailShowModel(deductPlan);
 				 deductPlanDetailShowModelList.add(detailModel);
 			 }
 			 int countSize = beneficiaryAuditResultService.countDeductPlanByAuditJob(auditJob, AuditResultCode.ISSUED, null);
			Map<String,Object> params = new HashMap<String,Object>();
            params.put("list", deductPlanDetailShowModelList);
            params.put("size", countSize);
			return jsonViewResolver.sucJsonResult(params);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, QUERY_ERROR);
		}
	}
	 
    //清算凭证详情页:基本信息以及流水
    @RequestMapping(value = "{clearingVoucherUuid}/detail/basic-info", method = RequestMethod.GET)
    public @ResponseBody String showBasicInfoTrans(@PathVariable("clearingVoucherUuid") String clearingVoucherUuid) {
        try{
        	ClearingVoucher clearingVoucher = clearingVoucherService.getClearingVoucherByUuid(clearingVoucherUuid);
            if(clearingVoucher==null){
                return jsonViewResolver.errorJsonResult("清算凭证不存在");
            }
            //auditJob 
            List<String> auditJobUuids = clearingVoucher.getAuditJobListFromAppendix();
          
            List<Map<String, String>> auditJobIdList = new ArrayList<Map<String, String>>();
            List<Map<String, String>> failAuditJobUuids = new ArrayList<Map<String, String>>();
            for(String auditJobUuid : auditJobUuids){
            	AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
            	Map<String, String> auditJobIdMap = new HashMap<String, String>();
            	auditJobIdMap.put("key",auditJobUuid);
            	auditJobIdMap.put("value", auditJob.getAuditJobNo());
            	int countSize = beneficiaryAuditResultService.countDeductPlanByAuditJob(auditJob, AuditResultCode.ISSUED, null);
            	if(countSize>0){
            		Map<String, String> failAuiditJobIdMap = new HashMap<String, String>();
            		failAuiditJobIdMap.put("key",auditJobUuid);
            		failAuiditJobIdMap.put("value", auditJob.getAuditJobNo());
            		failAuditJobUuids.add(failAuiditJobIdMap);
            	}
            	auditJobIdList.add(auditJobIdMap);
            }
           
            //相关流水
            List<String> cashFlowUuids = clearingVoucher.getCashFlowListFromAppendix();
            List<CashFlow> cashFlows = cashFlowService.getCashFlowListByCashFlowUuids(cashFlowUuids);
            List<ClearingVoucherCapitalDetail> clearingVoucherCapitalDetailList = auditCapitalDetailHandler.queryClearingVoucherCapitalDetail(auditJobUuids);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("clearingVoucher", clearingVoucher);
            params.put("cashFlows", cashFlows);
            params.put("auditJobIdList", auditJobIdList);
            params.put("failAuditJobUuids", failAuditJobUuids);
            params.put("capitalDetailList", clearingVoucherCapitalDetailList);
            return jsonViewResolver.sucJsonResult(params); 
        }catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }
    
    //清算凭证详情页:清算成功的线上支付单信息
    @RequestMapping(value = "{auditJobUuid}/querySucDeductPlan", method = RequestMethod.GET)
    public @ResponseBody String queryDeductApplicationInfo(@PathVariable("auditJobUuid") String auditJobUuid, Page page) {
        try{
        	 AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
             if(auditJob==null){
                 return jsonViewResolver.errorJsonResult("对账任务不存在");
             }
             List<String> deductPlanUuidList= beneficiaryAuditResultService.queryDeductPlanByAuditJob(auditJob, AuditResultCode.ISSUED, ClearingStatus.DONE, page);
 			 List<DeductPlan> deductPlanList = deductPlanService.getDeductPlanByDeductPlanUuidList(deductPlanUuidList);
 			 List<DeductPlanDetailShowModel> deductPlanDetailShowModelList = new ArrayList<DeductPlanDetailShowModel>();
 			 for(DeductPlan deductPlan : deductPlanList){
 			 	 DeductPlanDetailShowModel detailModel = new DeductPlanDetailShowModel(deductPlan);
 				 deductPlanDetailShowModelList.add(detailModel);
 			 }
 			 int countSize = beneficiaryAuditResultService.countDeductPlanByAuditJob(auditJob, AuditResultCode.ISSUED, ClearingStatus.DONE);
 			 Map<String,Object> params = new HashMap<String,Object>();
             params.put("list", deductPlanDetailShowModelList);
             params.put("size", countSize);
 			return jsonViewResolver.sucJsonResult(params);
        }catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
        }
    }
    
    @RequestMapping(value = "{deductPlanUuid}/detail/repayment-record", method = RequestMethod.GET)
    public @ResponseBody String queryRepaymentRecordDetails(@PathVariable("deductPlanUuid") String deductPlanUuid){
    	
    	DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(deductPlanUuid);
    	if(deductPlan==null){
    		return jsonViewResolver.errorJsonResult("线上支付单不存在");
    	}
    	// 根据扣款申请订单uuid查出扣款申请订单
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductPlan.getDeductApplicationUuid());
		
		//根据扣款申请订单UUid 查出原始凭证表对象
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByOutlierDocumentUuid(deductPlan.getDeductApplicationUuid(), SourceDocument.FIRSTOUTLIER_DEDUCTAPPLICATION);

		List<SourceDocumentDetail> detailList = new ArrayList<SourceDocumentDetail>();
		List<RepaymentRecordDetailForDeduct> repaymentRecordDetails = new ArrayList<RepaymentRecordDetailForDeduct>();
		if(sourceDocument != null ){
			//用当前原始凭证表UUid,原始凭证明细表UUID,资产UUid查出财务凭证表
			List<JournalVoucher> journalVoucherList = journalVoucherService.getJournalVouchersBySourceDocumentUuidAndType(sourceDocument.getSourceDocumentUuid(), 
					JournalVoucherStatus.VOUCHER_ISSUED, JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER);
			repaymentRecordDetails = journalVoucherHandler.getRepaymentRecordDetailForDeduct(journalVoucherList);
		}
		return  jsonViewResolver.sucJsonResult("list", repaymentRecordDetails);

    }
    
    
}
