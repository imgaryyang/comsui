package com.suidifu.matryoshaka.test.scripts;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategy;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

/**
 * Created by zsh2014 on 17-5-13.
 */
public class ZhongHangRemittanceService implements CustomizeServices {

    @Override
    public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
		try {
			if (preRequest == null) {
				postRequest.put("errorMsg", "参数不能为空");
				return false;
			}
			Integer remittanceStrategy = null;
			try {
				remittanceStrategy = Integer.valueOf((String) preRequest.get("remittanceStrategy"));
			}catch (NumberFormatException e){
				postRequest.put("errMsg", "放款策略［remittanceStrategy］，格式不正确！");
				return false;
			}
			if (!RemittanceStrategy.containOrdinal(remittanceStrategy)) {
				postRequest.put("errMsg", "放款策略［remittanceStrategy］，不存在！");
				return false;
			}
			String requestNo = (String) preRequest.get("requestNo");
			if (StringUtils.isEmpty(requestNo)) {
				postRequest.put("errMsg", "请求唯一标识［requestNo］，不能为空！");
				return false;
			}
			String productCode = (String) preRequest.get("productCode");
			if (StringUtils.isEmpty(productCode)) {
				postRequest.put("errMsg", "信托产品代码［productCode］，不能为空！");
				return false;
			}
			String remittanceId = (String) preRequest.get("remittanceId");
			if (StringUtils.isEmpty(remittanceId)) {
				postRequest.put("errMsg", "放款订单唯一编号［remittanceId］，不能为空！");
				return false;
			}
			String notifyUrl = (String) preRequest.get("notifyUrl");
			if (StringUtils.isEmpty(notifyUrl)) {
				postRequest.put("errMsg", "回调地址［notifyUrl］，不能为空！");
				return false;
			}
			String uniqueId = (String) preRequest.get("uniqueId");
			if (StringUtils.isEmpty(uniqueId)) {
				postRequest.put("errMsg", "贷款合同唯一编号［uniqueId］，不能为空！");
				return false;
			}
			String contractNo = (String) preRequest.get("contractNo");
			if (StringUtils.isEmpty(contractNo)) {
				postRequest.put("errMsg", "贷款合同编号［contractNo］，不能为空！");
				return false;
			}
			String plannedRemittanceAmount = (String) preRequest.get("plannedRemittanceAmount");
			BigDecimal bdPlannedRemittanceAmount = BigDecimal.ZERO;
			try {
				bdPlannedRemittanceAmount = new BigDecimal(plannedRemittanceAmount);
				if (bdPlannedRemittanceAmount.compareTo(BigDecimal.ZERO) <= 0) {
					postRequest.put("errMsg", "计划放款金额［plannedRemittanceAmount］，金额需高于0.00！");
					return false;
				}
				if (bdPlannedRemittanceAmount.scale() > 2) {
					postRequest.put("errMsg", "计划放款金额［plannedRemittanceAmount］，小数点后只保留2位！");
					return false;
				}
			} catch (Exception e) {
				postRequest.put("errMsg", "计划放款金额［plannedRemittanceAmount］，格式错误！");
				return false;
			}

			String remittanceDetails = (String) preRequest.get("remittanceDetails");
			List<Map<String, Object>> remittanceDetailList = JsonUtils.parseArray(remittanceDetails);
			if (CollectionUtils.isEmpty(remittanceDetailList)) {
				postRequest.put("errMsg", "放款明细表［remittanceDetails］，格式错误！");
				return false;
			}
			if (remittanceStrategy == RemittanceStrategy.DEDUCT_AFTER_REMITTANCE.ordinal()
				&& remittanceDetailList.size() != 1) {
				postRequest.put("errMsg", "放款明细列表［remittanceDetails］，与策略不符！");
				return false;
			}
			Set<String> detailNoSet = new HashSet<String>();
			BigDecimal detailsTotalAmount = BigDecimal.ZERO;
			for (int i = 0; i < remittanceDetailList.size(); i++) {
				Map<String, Object> remittanceDetail = (Map<String, Object>) remittanceDetailList.get(i);
				String errMsg = (String) sandboxDataSetHandler.checkRemittanceDetail(remittanceDetail);
				if (StringUtils.isNotEmpty(errMsg)) {
					postRequest.put("errMsg", "放款明细列表［remittanceDetails］，行［" + (i + 1) + "］，" + errMsg);
					return false;
				}
				String amount = (String) remittanceDetail.get("amount");
				detailsTotalAmount = detailsTotalAmount.add(new BigDecimal(amount));
				String detailNo = (String) remittanceDetail.get("detailNo");
				detailNoSet.add(detailNo);
			}
			if (detailNoSet.size() != remittanceDetailList.size()) {
				postRequest.put("errMsg", "放款明细列表［remittanceDetails］内，明细记录号［detailNo］有重复！");
				return false;
			}
			if (detailsTotalAmount.compareTo(bdPlannedRemittanceAmount) != 0) {
				postRequest.put("errMsg", "计划放款金额［plannedRemittanceAmount］，与明细金额累计不一致！");
				return false;
			}
			postRequest.putAll(preRequest);
			postRequest.put("remittanceDetails", JsonUtils.toJsonString(remittanceDetailList));
			return true;
		} catch (Exception e) {
			postRequest.put("errMsg", "参数错误");
			return false;
		}
	}
}
