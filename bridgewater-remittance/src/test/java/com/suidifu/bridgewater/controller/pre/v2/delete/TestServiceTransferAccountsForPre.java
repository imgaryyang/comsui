package com.suidifu.bridgewater.controller.pre.v2.delete;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zufangbao.sun.utils.RemittanceDetailBankCodeAutomiticSetterUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategy;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SEND_RECEIVE_CODE_FOR_REMITTANCE;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.Transfer;

@Component("testServiceTransferAccountsForPre")
public class TestServiceTransferAccountsForPre {
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
	                        Map<String, String> postRequest, Log logger) {
		try {
			if (preRequest == null) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "参数不能为空");
				return false;
			}

			String remittanceStrategyString = (String)preRequest.get("remittanceStrategy");
			String requestNo = (String)preRequest.get("requestNo");
			String productCode = (String)preRequest.get("productCode");
			String plannedRemittanceAmount = (String)preRequest.get("plannedRemittanceAmount");
			String remittanceDetails = (String)preRequest.get("remittanceDetails");
			String transferTransactionType = (String)preRequest.get("transferTransactionType");

			BigDecimal BDPlannedRemittanceAmount = BigDecimal.ZERO;
			if (StringUtils.isEmpty(remittanceStrategyString) || RemittanceStrategy.MULTIOBJECT.ordinal() != Integer.valueOf(remittanceStrategyString)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "放款策略［remittanceStrategy］，错误！");
				return false;
			}
			if (StringUtils.isEmpty(requestNo)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "请求唯一标识［requestNo］，不能为空！");
				return false;
			}
			if (StringUtils.isEmpty(productCode)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "信托产品代码［productCode］，不能为空！");
				return false;
			}
			if (StringUtils.isEmpty(transferTransactionType)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "转账交易类型，不能为空！");
				return false;
			}
			
			String transactionType = (String)Transfer.TRANSFER_TYPE_MAP.get(transferTransactionType);
			if(StringUtils.isEmpty(transactionType)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "转账交易类型，转换错误！");
				return false;
			}
			
			postRequest.put("transactionType",transactionType);
			
			try {
				BDPlannedRemittanceAmount = new BigDecimal(plannedRemittanceAmount);

				if (BDPlannedRemittanceAmount.compareTo(BigDecimal.ZERO) <= 0) {
					postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "计划放款金额［plannedRemittanceAmount］，金额需高于0.00！");
					return false;
				}
				if (BDPlannedRemittanceAmount.scale() > 2) {
					postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "计划放款金额［plannedRemittanceAmount］，小数点后只保留2位！");
					return false;
				}
			} catch (Exception e) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "计划放款金额［plannedRemittanceAmount］，格式错误！");
				return false;
			}

			List<RemittanceDetail> remittanceDetailList = JsonUtils.parseArray(remittanceDetails, RemittanceDetail.class);

			if (CollectionUtils.isEmpty(remittanceDetailList)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "放款明细列表［remittanceDetails］，格式错误，不能为空列表！");
				return false;
			}
			
			if (remittanceDetailList.size() != 1) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "放款明细列表［remittanceDetails］，个数只能为1个");
				return false;
			}
			
			RemittanceDetail remittanceDetail = (RemittanceDetail)remittanceDetailList.get(0);
			Map<String, String> cardBinBankCodeMap = sandboxDataSetHandler.getCardBinMap();
			RemittanceDetailBankCodeAutomiticSetterUtil
					.setBankCodeForRemittance(remittanceDetail, cardBinBankCodeMap);
			if (StringUtils.isEmpty(remittanceDetail.getBankCode())) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "放款明细列表［remittanceDetails］，校验失败" + remittanceDetail.getBankCardNo() + "不能匹配到bank code");
				return false;
			}
			if (!remittanceDetail.isValid()) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "放款明细列表［remittanceDetails］，校验失败" + remittanceDetail.getCheckFailedMsg());
				return false;
			}
			String remittanceDetailListJsonString = JsonUtils.toJsonString(remittanceDetailList);
			logger.info("ServiceTransferAccountsForPreAddBankCode" + remittanceDetailListJsonString);
			preRequest.put("remittanceDetails", remittanceDetailListJsonString);
			
			if (!AmountUtils.equals(remittanceDetail.getBDAmount(), BDPlannedRemittanceAmount)) {
				postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "计划放款金额［plannedRemittanceAmount］，与明细金额累计不一致！");
				return false;
			}

			postRequest.putAll(preRequest);
			return true;
		} catch (ApiException e) {
			e.printStackTrace();
			postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, ApiMessageUtil.getMessage(e.getCode()));
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			postRequest.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG, "参数错误");
			return false;
		}
	}
}
