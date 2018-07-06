package com.suidifu.bridgewater.handler.common.impl.v2;

import com.suidifu.bridgewater.handler.common.v2.IPaymentChannelHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.remittance.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.*;
import com.zufangbao.sun.handler.FinancialContractConfigHandler;
import com.zufangbao.sun.service.FinancialContractConfigService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("zhRemittancePaymentChannelHandler")
public class RemittancePaymentChannelHandlerImpl implements IPaymentChannelHandler {

	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;

	@Autowired
	private FinancialContractConfigHandler financialContractConfigHandler;

	@Autowired
	private FinancialContractConfigService financialContractConfigService;

	@Override
	public List<PaymentChannelSummaryInfo> getPaymentChannelSummaryInfosBy(String financialContractUuid,
			BusinessType businessType, AccountSide accountSide, String bankCode, BigDecimal plannedAmount) {

		if (StringUtils.isBlank(financialContractUuid) || businessType == null || accountSide == null) {
			return Collections.emptyList();
		}

		List<PaymentChannelSummaryInfo> rtnList = new ArrayList<>();

		List<String> paymentChannelUuids = financialContractConfigHandler.getAvailablePaymentChannelUuidList(
				financialContractUuid, businessType, accountSide, bankCode, plannedAmount);
		for (String uuid : paymentChannelUuids) {
			PaymentChannelInformation pcInfo = paymentChannelInformationService.getPaymentChannelInformationBy(uuid);
			if (pcInfo == null) {
				continue;
			}

			ChannelWorkingStatus workingStatus = pcInfo.getChannelWorkingStatusBy(accountSide);
			if (workingStatus != ChannelWorkingStatus.ON) {
				continue;
			}

			String paymentChannelServiceUuid = pcInfo.getPaymentChannelServiceUuidBy(accountSide);
			if (StringUtils.isBlank(paymentChannelServiceUuid)) {
				continue;
			}

			PaymentChannelSummaryInfo summaryInfo = new PaymentChannelSummaryInfo();
			summaryInfo.setPaymentGateway(pcInfo.getPaymentGateway());
			summaryInfo.setPaymentChannelName(pcInfo.getPaymentChannelName());
			summaryInfo.setReckonAccount(pcInfo.getClearingNo());
			summaryInfo.setChannelServiceUuid(paymentChannelServiceUuid);
			rtnList.add(summaryInfo);
		}
		return rtnList;
	}

	@Override
	public PaymentChannelSummaryInfo getFirstPaymentChannelSummaryInfoBy(String financialContractUuid,
			BusinessType businessType, AccountSide accountSide, String bankCode, BigDecimal plannedAmount) {
		List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos = getPaymentChannelSummaryInfosBy(
				financialContractUuid, businessType, accountSide, bankCode, plannedAmount);
		FinancialContractConfig fcc = financialContractConfigService.getFinancialContractConfigBy(financialContractUuid,
				businessType);
		if (fcc == null) {
			return null;
		}
		PaymentStrategyMode paymentStrategyMode = fcc.getPaymentChannelModeBy(accountSide);
		if ((paymentStrategyMode == PaymentStrategyMode.QUOTAPRIORITY
				|| paymentStrategyMode == PaymentStrategyMode.SINGLECHANNELMODE)
				&& CollectionUtils.isEmpty(paymentChannelSummaryInfos)) {
			throw new ApiException(ApiResponseCode.NOT_IN_SINGLE_TRANSACTION_LIMIT);
		}

		if (CollectionUtils.isNotEmpty(paymentChannelSummaryInfos)) {
			return paymentChannelSummaryInfos.get(0);
		}
		return null;
	}

}
