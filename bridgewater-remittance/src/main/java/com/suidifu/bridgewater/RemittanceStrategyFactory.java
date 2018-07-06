package com.suidifu.bridgewater;

import com.alibaba.fastjson.JSON;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.enums.AccountSide;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.remittance.PaymentChannelSummaryInfo;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.service.UnionpayBankConfigService;
import com.zufangbao.wellsfargo.deduct.handler.IPaymentChannelHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 放款策略工厂
 * @author zhanghongbing
 *
 */
@Component("remittanceStrategyFactory")
public class RemittanceStrategyFactory {

	@Autowired
	private IPaymentChannelHandler remittancePaymentChannelHandler;
	
	@Autowired
	private JpmorganApiHelper jpmorganApiHelper;
	
	@Autowired
	private UnionpayBankConfigService unionpayBankConfigService;

	@Value("#{config['notify_url']}")
	private String notify_url;
	
	/**
	 * 先放后扣放款策略，交易日程构建
	 * @param commandModel 放款数据模型
	 * @param financialContractUuid 信托合同uuid
	 * @return 交易日程列表
	 */
	public List<TradeSchedule> buildTradeScheduleForStrategyDeductAfterRemittance(RemittanceCommandModel commandModel, String financialContractUuid) {
		String remittanceApplicationUuid = commandModel.getRemittanceApplicationUuid();
		RemittanceDetail remittanceDetail = commandModel.getSingleRemittanceDetail();
		String bankCode = remittanceDetail.getBankCode();
		BigDecimal plannedAmount = remittanceDetail.getBDAmount();
		
		//取得自有放款通道
		PaymentChannelSummaryInfo remittanceChannelInfo = remittancePaymentChannelHandler.getFirstPaymentChannelSummaryInfoBy(financialContractUuid, BusinessType.SELF, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.CREDIT, bankCode, plannedAmount);
		if(remittanceChannelInfo == null || StringUtils.isEmpty(remittanceChannelInfo.getChannelServiceUuid())) {
			throw new ApiException(ApiResponseCode.CHANNEL_NOT_FOUND);
		}
		
		//取得委托代扣通道
		PaymentChannelSummaryInfo entrustmentDeductChannelInfo = remittancePaymentChannelHandler.getFirstPaymentChannelSummaryInfoBy(financialContractUuid, BusinessType.ENTRUST, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.DEBIT, bankCode, plannedAmount);
		if(entrustmentDeductChannelInfo == null || StringUtils.isEmpty(entrustmentDeductChannelInfo.getChannelServiceUuid())) {
			throw new ApiException(ApiResponseCode.CHANNEL_NOT_FOUND);
		}
		
		Date fstEffectiveAbsoluteTime = remittanceDetail.getParsedPlannedDate();
		
		String transactionRemark = commandModel.getRemark();
		TradeSchedule remittanceTradeSchedule = remittanceDetail.convertToTradeSchedule(AccountSide.CREDIT, transactionRemark, remittanceChannelInfo, fstEffectiveAbsoluteTime, plannedAmount, remittanceApplicationUuid, null, remittanceDetail, 1, null);
		
		String remittancePlanUuid = remittanceTradeSchedule.getOutlierTransactionUuid();
		String[] remittancePlanUuids = StringUtils.isEmpty(remittancePlanUuid) ? null : new String[]{remittancePlanUuid};
		String deductExecutionPrecond = jpmorganApiHelper.buildPreCond(remittancePlanUuids, remittancePlanUuids);
		
		//指定扣款模式，清算账号
		String debitBankCode = remittanceDetail.getBankCode();
		String reckonAccount = entrustmentDeductChannelInfo.getReckonAccount();
		String fstGateWayRouterInfo = getFstGateWayRouterInfoForDeduct(debitBankCode, reckonAccount);
		TradeSchedule deductTradeSchedule = remittanceDetail.convertToTradeSchedule(AccountSide.DEBIT, "", entrustmentDeductChannelInfo, fstEffectiveAbsoluteTime, plannedAmount, remittanceApplicationUuid, deductExecutionPrecond, remittanceDetail, 2, fstGateWayRouterInfo);
		
		List<TradeSchedule> tradeSchedules = new LinkedList<TradeSchedule>();

		remittanceTradeSchedule.setNotifyUrl(notify_url);
		deductTradeSchedule.setNotifyUrl(notify_url);
		
		tradeSchedules.add(remittanceTradeSchedule);
		tradeSchedules.add(deductTradeSchedule);
		return tradeSchedules;
	}
	
	private String getFstGateWayRouterInfoForDeduct(String standardBankCode, String reckonAccount) {
		String debitMode = unionpayBankConfigService.isUseBatchDeduct(null, standardBankCode) ? "batchMode" : "realTimeMode";
		
		Map<String,String> fstGateWayRouterInfo = new HashMap<String,String>();
		fstGateWayRouterInfo.put("debitMode",debitMode );
		
		if(StringUtils.isNotEmpty(reckonAccount)) {
			fstGateWayRouterInfo.put("reckonAccount", reckonAccount);
		}
		
		return JSON.toJSONString(fstGateWayRouterInfo);
	}
	
	/**
	 * 多目标放款策略，交易日程构建
	 * @param commandModel 放款数据模型
	 * @param financialContractUuid 信托合同uuid
	 * @return 交易日程列表
	 */
	public List<TradeSchedule> buildTradeScheduleForStrategyMultiobject(RemittanceCommandModel commandModel, String financialContractUuid) {
		String remittanceApplicationUuid = commandModel.getRemittanceApplicationUuid();
		String transactionRemark = commandModel.getRemark();
		List<RemittanceDetail> remittanceDetails = commandModel.getRemittanceDetailListFromJsonString();
		return generateTradeSchedulesForStrategyMultiObject(remittanceDetails,financialContractUuid,remittanceApplicationUuid,transactionRemark);
	}

	public List<TradeSchedule> buildTradeScheduleForStrategyMultiObject(ModifyRemittanceApplicationModel model,RemittanceApplication application) {
		String remittanceApplicationUuid = application.getRemittanceApplicationUuid();
		String transactionRemark = model.getComment();
		String financialContractUuid = application.getFinancialContractUuid();
		List<RemittanceDetail> remittanceDetails = model.getRemittanceDetailList();
		return generateTradeSchedulesForStrategyMultiObject(remittanceDetails,financialContractUuid,remittanceApplicationUuid,transactionRemark);
	}

	private List<TradeSchedule> generateTradeSchedulesForStrategyMultiObject(List<RemittanceDetail> remittanceDetails, String financialContractUuid,
		String remittanceApplicationUuid, String transactionRemark) {
		List<TradeSchedule> tradeSchedules = new LinkedList<TradeSchedule>();
		String executionPrecond = null;
		for (int i = 0; i < remittanceDetails.size(); i++) {
			RemittanceDetail remittanceDetail = remittanceDetails.get(i);
			Date fstEffectiveAbsoluteTime = remittanceDetail.getParsedPlannedDate();
			String bankCode = remittanceDetail.getBankCode();
			BigDecimal plannedAmount = remittanceDetail.getBDAmount();
			PaymentChannelSummaryInfo remittanceChannelInfo = remittancePaymentChannelHandler.getFirstPaymentChannelSummaryInfoBy(
				financialContractUuid, BusinessType.SELF, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.CREDIT, bankCode, plannedAmount);
			if (remittanceChannelInfo == null || StringUtils.isEmpty(remittanceChannelInfo.getChannelServiceUuid())) {
				throw new ApiException(ApiResponseCode.CHANNEL_NOT_FOUND);
			}
			TradeSchedule remittanceTradeSchedule = remittanceDetail.convertToTradeSchedule(AccountSide.CREDIT, transactionRemark, remittanceChannelInfo,
				fstEffectiveAbsoluteTime, plannedAmount, remittanceApplicationUuid, executionPrecond, remittanceDetail, (i + 1), null);

			remittanceTradeSchedule.setNotifyUrl(notify_url);
			
			tradeSchedules.add(remittanceTradeSchedule);
			String remittancePlanUuid = remittanceTradeSchedule.getOutlierTransactionUuid();
			String[] remittancePlanUuids = StringUtils.isEmpty(remittancePlanUuid) ? null : new String[]{remittancePlanUuid};
			executionPrecond = jpmorganApiHelper.buildPreCond(remittancePlanUuids, remittancePlanUuids);
		}
		return tradeSchedules;
	}
	
}
