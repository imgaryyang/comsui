package com.suidifu.matryoshaka.test.scripts;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.remittance.RemittanceConstants;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.*;

public class YunXinRemittanceService implements CustomizeServices{

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
			Map<String, String> postRequest, Log logger) {
		try {
			if (preRequest == null) {
				postRequest.put("errorMsg", "参数不能为空");
				return false;
			}

			String remittanceStrategyString = preRequest.get("remittanceStrategy");
			String requestNo = preRequest.get("requestNo");
			String productCode = preRequest.get("productCode");
			String uniqueId = preRequest.get("uniqueId");
			String plannedRemittanceAmount = preRequest.get("plannedRemittanceAmount");
			String remittanceDetails = preRequest.get("remittanceDetails");
			BigDecimal BDPlannedRemittanceAmount = BigDecimal.ZERO;
			if (StringUtils.isEmpty(remittanceStrategyString) || !RemittanceConstants.REMITTANCE_STRATEGY_MAPPER.containsKey(remittanceStrategyString)) {
				postRequest.put("errorMsg", "放款策略［remittanceStrategy］，不存在！");
				return false;
			}
			if (StringUtils.isEmpty(requestNo)) {
				postRequest.put("errorMsg", "请求唯一标识［requestNo］，不能为空！");
				return false;
			}
			if (StringUtils.isEmpty(productCode)) {
				postRequest.put("errorMsg", "信托产品代码［productCode］，不能为空！");
				return false;
			}
			if (StringUtils.isEmpty(uniqueId)) {
				postRequest.put("errorMsg", "贷款合同唯一编号［uniqueId］，不能为空！");
				return false;
			}

			try {
				BDPlannedRemittanceAmount = new BigDecimal(plannedRemittanceAmount);

				if (BDPlannedRemittanceAmount.compareTo(BigDecimal.ZERO) <= 0) {
					postRequest.put("errorMsg", "计划放款金额［plannedRemittanceAmount］，金额需高于0.00！");
					return false;
				}
				if (BDPlannedRemittanceAmount.scale() > 2) {
					postRequest.put("errorMsg", "计划放款金额［plannedRemittanceAmount］，小数点后只保留2位！");
					return false;
				}
			} catch (Exception e) {
				postRequest.put("errorMsg", "计划放款金额［plannedRemittanceAmount］，格式错误！");
				return false;
			}

			List<RemittanceDetail> remittanceDetailList = JsonUtils.parseArray(remittanceDetails, RemittanceDetail.class);
			if (CollectionUtils.isNotEmpty(remittanceDetailList)) {
				Collections.sort(remittanceDetailList);
			}


			if (CollectionUtils.isEmpty(remittanceDetailList)) {
				postRequest.put("errorMsg", "放款明细列表［remittanceDetails］，格式错误，不能为空列表！");
				return false;
			}

			int ordinal = Integer.parseInt(remittanceStrategyString);
			RemittanceStrategy remittanceStrategy = RemittanceStrategy.fromValue(ordinal);
			boolean validateRemittanceDetailsByStrategy = false;

			if (remittanceStrategy == RemittanceStrategy.MULTIOBJECT) {
				validateRemittanceDetailsByStrategy = true;
			} else if (remittanceStrategy == RemittanceStrategy.DEDUCT_AFTER_REMITTANCE) {
				validateRemittanceDetailsByStrategy = (remittanceDetailList.size() == 1);
			} else {
				throw new ApiException(ApiResponseCode.INVALID_PARAMS, "放款策略［remittanceStrategy］，不存在！");
			}

			if (!validateRemittanceDetailsByStrategy) {
				postRequest.put("errorMsg", "放款明细列表［remittanceDetails］，与策略不符！");
				return false;
			}

			Set<String> detailNoSet = new HashSet<String>();
			BigDecimal detailsTotalAmount = BigDecimal.ZERO;
			for (int i = 0; i < remittanceDetailList.size(); i++) {
				RemittanceDetail remittanceDetail = remittanceDetailList.get(i);
				if (!remittanceDetail.isValid()) {
					postRequest.put("errorMsg", "放款明细列表［remittanceDetails］，行［" + i + "］，" + remittanceDetail.getCheckFailedMsg());
					return false;
				}
				detailsTotalAmount = detailsTotalAmount.add(remittanceDetail.getBDAmount());
				detailNoSet.add(remittanceDetail.getDetailNo());
			}
			//放款明细内，记录号重复性校验
			if (detailNoSet.size() != remittanceDetailList.size()) {
				postRequest.put("errorMsg", "放款明细列表［remittanceDetails］内，明细记录号［detailNo］有重复！");
				return false;
			}
			//计划总金额，明细累计金额校验
			if (!AmountUtils.equals(detailsTotalAmount, BDPlannedRemittanceAmount)) {
				postRequest.put("errorMsg", "计划放款金额［plannedRemittanceAmount］，与明细金额累计不一致！");
				return false;
			}
			
			return true;
		} catch (Exception e) {
			postRequest.put("errorMsg", "参数错误");
			return false;
		}
	}
		
}




