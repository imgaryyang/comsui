package com.suidifu.bridgewater.handler.test.scripts;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.utils.AttributeAnnotationValidatorUtils;
import com.zufangbao.sun.utils.CheckFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/8/1.
 */
public class YunXinDeductServices implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
			Map<String, String> postRequest, Log logger) {

		try {
			//必填
			String fn = (String) preRequest.getOrDefault("fn", StringUtils.EMPTY);
			String requestNo = (String) preRequest.getOrDefault("requestNo", StringUtils.EMPTY);
			String deductId = (String) preRequest.getOrDefault("deductId", StringUtils.EMPTY);
			String financialProductCode = (String) preRequest.getOrDefault("financialProductCode", StringUtils.EMPTY);
			String apiCalledTime = (String) preRequest.getOrDefault("apiCalledTime", StringUtils.EMPTY);
			String uniqueId = (String) preRequest.getOrDefault("uniqueId", StringUtils.EMPTY);
			String contractNo = (String) preRequest.getOrDefault("contractNo", StringUtils.EMPTY);
			String amount = (String) preRequest.getOrDefault("amount", StringUtils.EMPTY);
			String repaymentDetails = (String) preRequest.getOrDefault("repaymentDetails", StringUtils.EMPTY);
			//选填
			String payerName = (String) preRequest.getOrDefault("payerName", StringUtils.EMPTY);
			String payAcNo = (String) preRequest.getOrDefault("payAcNo", StringUtils.EMPTY);
			String idCardNum = (String) preRequest.getOrDefault("idCardNum", StringUtils.EMPTY);
			String bankCode = (String) preRequest.getOrDefault("bankCode", StringUtils.EMPTY);
			String provinceCode = (String) preRequest.getOrDefault("provinceCode", StringUtils.EMPTY);
			String cityCode = (String) preRequest.getOrDefault("cityCode", StringUtils.EMPTY);
			String mobile = (String) preRequest.getOrDefault("mobile", StringUtils.EMPTY);
			String gateway = (String) preRequest.getOrDefault("gateway", StringUtils.EMPTY);
			String notifyUrl = (String) preRequest.getOrDefault("notifyUrl", null);
			
			if (StringUtils.isEmpty(fn)) {
				postRequest.put("errorMsg", "功能代码不能为空");
				return false;
			}
			if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {
				postRequest.put("errorMsg", "请选填其中一种编号［uniqueId，contractNo］！");
				return false;
			}
			if (StringUtils.isEmpty(requestNo)) {
				postRequest.put("errorMsg", "请求唯一标识［requestNo］，不能为空！");
				return false;
			}
			if(sandboxDataSetHandler.existRequest(requestNo)){
				postRequest.put("errorMsg", "请求编号重复!");
				return false;
			}
			if (StringUtils.isEmpty(deductId)) {
				postRequest.put("errorMsg", "扣款唯一编号不能为空！！！");
				return false;
			}
			if (sandboxDataSetHandler.existDeduct(deductId)) {
				postRequest.put("errorMsg", "扣款唯一编号重复!");
				return false;
			}
			if (StringUtils.isEmpty(financialProductCode)) {
				postRequest.put("errorMsg", "信托产品代码不能为空!");
				return false;
			}
			if (DateUtils.asDay(apiCalledTime) == null) {
				postRequest.put("errorMsg", "接口调用时间,时间格式有误！！！");
				return false;
			}
			if (!CheckFormatUtils.checkRMBCurrency(amount)) {
				postRequest.put("errorMsg", "扣款金额格式有误！！！");
				return false;
			}
			Integer repaymentType =null;
			try{
				repaymentType = Integer.valueOf((String)preRequest.get("repaymentType"));
			}catch (NumberFormatException e){
                postRequest.put("errMsg", "还款类型格式错误！！！");
                return false;
            }
			
			if (RepaymentType.fromValue(repaymentType) == null) {
				postRequest.put("errorMsg", "还款类型格式错误！！！");
				return false;
			}
			List<RepaymentDetail> parseRepaymentDetails = JsonUtils.parseArray(repaymentDetails, RepaymentDetail.class);
			if (CollectionUtils.isEmpty(parseRepaymentDetails)) {
				postRequest.put("errorMsg", "还款明细不能为空！！！");
				return false;
			}
			for (RepaymentDetail checkedRepayment : parseRepaymentDetails) {
				if (StringUtils.isEmpty(checkedRepayment.getRepaymentPlanNo()) && StringUtils.isEmpty(checkedRepayment.getRepayScheduleNo())){
					postRequest.put("errorMsg", "商户还款计划编号、还款计划编号，二选一，必须填写其中一项");
					return false;
				}
				if (!AttributeAnnotationValidatorUtils.isValidInAmount(checkedRepayment)) {
					postRequest.put("errorMsg", "还款计划明细金额不能为空！！！");
					return false;
				}
				if (checkedRepayment.getOverDueFeeDetail().overDueFeeDetailRule() == 2) {
					postRequest.put("errorMsg", "还款计划逾期费用明细金额不合法");
					return false;
				}
			}

			/*Set<String> repaymentPlanCodeSet = parseRepaymentDetails.stream()
					.collect(Collectors.mapping(RepaymentDetail::getRepaymentPlanNo, Collectors.toSet()));
			if (repaymentPlanCodeSet.size() != parseRepaymentDetails.size()) {
				postRequest.put("errorMsg", "还款明细中有重复还款计划编号！！！");
				return false;
			}*/
			
			postRequest.put("fn",fn);
			postRequest.put("requestNo",requestNo);
	        postRequest.put("uniqueId",uniqueId);
	        postRequest.put("contractNo",contractNo);
	        postRequest.put("apiCalledTime", apiCalledTime);
	        postRequest.put("deductId",deductId);
	        postRequest.put("repaymentType", repaymentType.toString());
	        postRequest.put("financialProductCode",financialProductCode);
	        postRequest.put("deductAmount",amount);
	        postRequest.put("accountHolderName", payerName);
	        postRequest.put("deductAccountNo", payAcNo);
	        postRequest.put("idCardNo", idCardNum);
	        postRequest.put("standardBankCode", bankCode);
	        postRequest.put("cpBankProvince", provinceCode);
	        postRequest.put("cpBankCity", cityCode);
	        postRequest.put("repaymentDetails", repaymentDetails);
	        postRequest.put("mobile", mobile);
	        postRequest.put("gateway", gateway);
	        postRequest.put("notifyUrl", notifyUrl);
	        
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			postRequest.put("errorMsg", "system error[" + e.getMessage() + "]");
			return false;
		}
	}

}
