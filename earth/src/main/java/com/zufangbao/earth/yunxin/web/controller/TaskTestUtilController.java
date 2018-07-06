package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.persistence.support.Order;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.dataSync.task.DataSyncTask;
import com.zufangbao.earth.yunxin.task.*;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.unionpay.UnionpayManualTransaction;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanValuationHandler;
import com.zufangbao.sun.yunxin.service.UnionpayBankConfigService;
import com.zufangbao.sun.yunxin.service.UnionpayManualTransactionService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.AutoRecoverAssesNoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="manual-tasks")
public class TaskTestUtilController extends BaseController{
	
	@Autowired
	private AssetTask assetTask;
	
	@Autowired
	private SystemDeductTask systemDeductTask;
	
	@Autowired
	private SettlementOrderTask settlementOrderTask;
	
	@Autowired
	private SmsTask smsTask;
	
	@Autowired
	private AutoRecoverAssesNoSession autoRecoverAssesNoSession;
	
	@Autowired
	private RepaymentPlanValuationHandler repaymentPlanValuationHandler;
	
	@Autowired
	private UnionpayBankConfigService unionpayBankConfigService;
	
	@Autowired
	private UnionpayManualTransactionService unionpayManualTransactionService;
	
	@Autowired
	private FinancialContractService  financialContractService;
	
	@Autowired
	private DataSyncTask dataSyncTask;
	
	@Autowired
	private RepurchaseTask repurchaseTask;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView showTaskTestUtilView() {
		ModelAndView mav = new ModelAndView("manual-tasks");
		List<FinancialContract> financialContractList = financialContractService.loadAll(FinancialContract.class);
		mav.addObject("financialContractList" , financialContractList);
		return mav;
	}
	
	@RequestMapping(value = "evaluate-asset", method = RequestMethod.GET)
	public @ResponseBody String evaluateAsset() {
		try {
			assetTask.endYesterdayWorkAndStartTodayWork();
			return jsonViewResolver.jsonResult("［提前还款失败次日同步］&［评估资产、生成结算单］正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("［提前还款失败次日同步］&［评估资产、生成结算单］异常！");
		}
	}
	
	@RequestMapping(value = "trtask-today", method = RequestMethod.GET)
	public @ResponseBody String trtaskToday() {
		try {
			systemDeductTask.sysDeductOnPlannedRepaymentDate();
			return jsonViewResolver.jsonResult("计划还款日扣款正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("计划还款日扣款异常！");
		}
	}
	
	@RequestMapping(value = "trtask_overdue", method = RequestMethod.GET)
	public @ResponseBody String trtaskOverdue() {
		try {
			systemDeductTask.sysDeductAfterPlannedRepaymentDate();
			return jsonViewResolver.jsonResult("逾期扣款正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("逾期扣款异常！");
		}
	}
	
	@RequestMapping(value = "trtask_prepayment", method = RequestMethod.GET)
	public @ResponseBody String trtaskPrepayment() {
		try {
			systemDeductTask.sysDeductForPrepaymentApplication();
			return jsonViewResolver.jsonResult("提前还款扣款正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("提前还款逾期扣款异常！");
		}
	}
	
	@RequestMapping(value = "sync-order-status", method = RequestMethod.GET)
	public @ResponseBody String syncOrderStatus() {
		try {
			systemDeductTask.syncSysDeductStatus();
			return jsonViewResolver.jsonResult("结算单状态同步&提前还款成功同步，正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("结算单状态同步&提前还款成功同步，异常！");
		}
	}
	
	@RequestMapping(value = "settlement-order", method = RequestMethod.GET)
	public @ResponseBody String settlementOrder() {
		try {
			settlementOrderTask.createSettlementOrders();
			return jsonViewResolver.jsonResult("生成清算单正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("生成清算单异常！");
		}
	}
	
	@RequestMapping(value = "deduct-write-off", method = RequestMethod.GET)
	public @ResponseBody String queryOrderResult() {
		try {
			systemDeductTask.thirdPartyDeductionAssetRecover();
			return jsonViewResolver.jsonResult("扣款核销，正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("扣款核销，异常！");
		}
	}
	
//	@RequestMapping(value = "create-sms", method = RequestMethod.GET)
//	public @ResponseBody String createSms() {
//		try {
//			smsTask.createRemindAndOverDueSmsQuene();
//			return jsonViewResolver.jsonResult("生成还款提醒短信正常！");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonViewResolver.errorJsonResult("生成还款提醒短信异常！");
//		}
//	}
	
	@RequestMapping(value = "send-sms", method = RequestMethod.GET)
	public @ResponseBody String sendSms() {
		try {
			smsTask.sendSmsQuene();
			return jsonViewResolver.jsonResult("发送短信正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("发送短信异常！");
		}
	}
	
	@RequestMapping(value = "refresh-unionpay-bank-config", method = RequestMethod.GET)
	public @ResponseBody String refreshUnionpayBankConfig() {
		try {
			unionpayBankConfigService.cacheEvictUnionpayBankConfig();
			return jsonViewResolver.jsonResult("刷新银联银行配置正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("刷新银联银行配置异常！");
		}
	}
	
	@RequestMapping(value = "manual-transactions", method = RequestMethod.GET)
	public ModelAndView manualTransactions() {
		try {
			ModelAndView mav = new ModelAndView("manual-transactions");
			Order order = new Order("id", "DESC");
			List<UnionpayManualTransaction> transactions = unionpayManualTransactionService.list(UnionpayManualTransaction.class, order);
			mav.addObject("transactions", transactions);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			return pageViewResolver.errorSpec();
		}
	}
	
	// 执行回购
	@RequestMapping(value = "exec-repurchasing", method = RequestMethod.GET)
	public @ResponseBody String syncDatas() {
		try {
			repurchaseTask.genRepurchaseDoc();
			return jsonViewResolver.jsonResult("回购执行正常！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.jsonResult("回购失败！");
		}
	}
	
	// 自动核销
	@RequestMapping(value = "exec-auto-recover", method = RequestMethod.GET)
	public @ResponseBody String autoRecover() {
		try {
			autoRecoverAssesNoSession.auto_recover_assets_and_guarantess_by_virtual_account();
			return jsonViewResolver.jsonResult("自动核销！");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.jsonResult("自动核销失败！");
		}
	}
	

	@RequestMapping("valuate-asset")
	public @ResponseBody String valuateSingleAsset(
			@RequestParam("assetSetUuid") String assetSetUuid
			)
	{
		try{
			
			repaymentPlanValuationHandler.valuate_repayment_plan_and_system_create_order(assetSetUuid, new Date());
			
			return jsonViewResolver.sucJsonResult(); 
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}		
		 
		
	}
	
}