package com.suidifu.bridgewater.controller.api.remittance;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobSender;
import com.suidifu.bridgewater.handler.RemittancePlanExecLogHandler;
import com.suidifu.bridgewater.handler.RemittancetPlanHandler;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;

@Controller("flowController")
@RequestMapping("/remittance-proxy")
public class FlowController {
	@Autowired
	private RemittancetPlanHandler remittancetPlanHandler;
	@Autowired
	IRemittancePlanService iRemittancePlanService;
	@Autowired
	IRemittanceApplicationDetailService iRemittanceApplicationDetailService;
	@Autowired
	IRemittanceApplicationService iRemittanceApplicationService;
	@Autowired
	CashFlowService cashFlowService;
	@Autowired
	public JsonViewResolver jsonViewResolver;
	@Autowired
	private IRemittanceApplicationHandler iRemittancetApplicationHandler;
	@Autowired
	private RemittancePlanExecLogHandler remittancePlanExecLogHandler;
	@Autowired
	private RemittanceNotifyJobSender remittanceNotifyJobSender;
	
	private static final Log logger = LogFactory.getLog(FlowController.class);

	// 放款单---详情页面---失败再放款
	@RequestMapping(value = "/resend", method = RequestMethod.POST)
	public @ResponseBody
	String reRemittanceForFailedPlan(
			@RequestParam(value = "remittancePlanUuid", required = true) String remittancePlanUuid,
			HttpServletRequest request) {
		try {

			TradeSchedule tradeSchedule = remittancetPlanHandler
					.saveRemittanceInfoBeforeResendForFailedPlan(remittancePlanUuid);
			
			iRemittancetApplicationHandler.deleteAllObjectAndRelationCache(remittancePlanUuid);

			//TODO pushJobToCitigroup 限额
			pushJob(tradeSchedule);

			return jsonViewResolver.sucJsonResult("execReqNo", tradeSchedule.getSourceMessageUuid());

		} catch (CommonException e) {
			logger.error("#reRemittanceForFailedPlan# occur error.");
			return jsonViewResolver.errorJsonResult(e.getErrorMsg());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

	private void pushJob(TradeSchedule tradeSchedule) {
		String remittanceApplicationUuid = tradeSchedule.getBatchUuid();
		String remittancePlanUuid = tradeSchedule.getOutlierTransactionUuid();
		RemittanceApplication application = iRemittanceApplicationService.getRemittanceApplicationBy(remittanceApplicationUuid);
		RemittancePlan plan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
		RemittancePlanInfo remittancePlanInfo = new RemittancePlanInfo(plan.getRemittancePlanUuid(),plan.getPlannedTotalAmount(),null);
		
		remittanceNotifyJobSender.pushJobToCitigroupForResendQuotaValidation(remittanceApplicationUuid, application.getFinancialContractUuid(), remittancePlanInfo, tradeSchedule);
	}

	//退票Remittance代理
	@RequestMapping(value = "/refund")
	public @ResponseBody
	String attachCashFlow(@RequestParam(value = "remittancePlanUuid") String remittancePlanUuid) {

		try {
			String isUpdate = remittancePlanExecLogHandler.updateRemittanceInfo(remittancePlanUuid);

			//清除所有缓存
			iRemittancetApplicationHandler.deleteAllObjectAndRelationCache(remittancePlanUuid);

			return jsonViewResolver.sucJsonResult("successMessage", isUpdate);
		} catch (Exception e) {
			logger.error("#addCashFlow# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("退票错误");
		}

	}
}
