package com.suidifu.matryoshaka.test.scripts;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.gluon.opensdk.DateUtils;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ZhongHangSignService implements CustomizeServices {

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
			Map<String, String> postRequest, Log logger) {
		try {
			String reg = "^\\d+$";
			
			if (null == preRequest || null == postRequest) {
				postRequest.put("errMsg", "参数获取出错");
				return false;
			}

			String productCode = preRequest.get("productCode");
			
			if (StringUtils.isEmpty(productCode)) {
				postRequest.put("errMsg", "信托产品代码必填");
				return false;
			}
			
			FinancialContract financialContract = sandboxDataSetHandler.getFinancialContractByProductCode(productCode);
			
			if (null == financialContract) {
				postRequest.put("errMsg", "没有对应的信托合同");
				return false;
			}
			String financialContractUuid =financialContract.getFinancialContractUuid();
//			
			postRequest.put("financialContractUuid", financialContractUuid);
//			String bankMerId = sandboxDataSetHandler.getBankMerId(financialContractUuid);
//			
//			if (StringUtils.isEmpty(bankMerId)) {
//				postRequest.put("errMsg", "没有信托合同对应的商户号");
//				return false;
//			}
//			
//			String signKey = sandboxDataSetHandler.getBankSignKey(financialContractUuid);
//			
//			if (StringUtils.isEmpty(signKey)) {
//				postRequest.put("errMsg", "没有信托合同对应的银行端signKey");
//				return false;
//			}


			if (StringUtils.isEmpty(preRequest.get("merId"))) {
				postRequest.put("errMsg", "商户代码必填");
				return false;
			}
			String bankAliasName = preRequest.get("bankAliasName");
			String bankFullName = preRequest.get("bankFullName");
			String stdBankCode = null;
			PedestrianBankCode code = null;
			if (StringUtils.isEmpty(bankAliasName)) {
				postRequest.put("errMsg", "行别必填");
				return false;
			}
			if (StringUtils.isNotEmpty(bankFullName)) {
				code = sandboxDataSetHandler.existBank(bankFullName, 0);
				if (null == code||StringUtils.isEmpty(code.getStdBankCode())) {
					postRequest.put("errMsg", "支行名称没有对应的联行号！");
					return false;
				}
				stdBankCode = code.getStdBankCode();
			} else {
//				stdBankCode = (String)BANK_CODE_NAME.get(bankAliasName);
				stdBankCode = sandboxDataSetHandler.getBankCodeByBankAliasName(bankAliasName);
				if (StringUtils.isEmpty(stdBankCode)||!stdBankCode.matches(reg)) {
//					String bankName = (String)HEAD_OFFICE_ENGLISH_ABBR_CN_MAP.get(bankAliasName);
					code = sandboxDataSetHandler.existBank(stdBankCode, 1);
					if (null == code||StringUtils.isEmpty(code.getStdBankCode())) {
						postRequest.put("errMsg", "行别没有对应的联行号！");
						return false;
					}
					stdBankCode = code.getStdBankCode();
				}
			}
			postRequest.put("stdBankCode", stdBankCode);
			if (StringUtils.isEmpty(preRequest.get("tranType"))) {
				postRequest.put("errMsg", "交易类型必填");
				return false;
			}
			if (StringUtils.isEmpty(preRequest.get("opType"))) {
				postRequest.put("errMsg", "操作类型必填");
				return false;
			}
			if (StringUtils.isEmpty(preRequest.get("accName"))) {
				postRequest.put("errMsg", "户名必填");
				return false;
			}
			String accNo = preRequest.get("accNo");
			if (StringUtils.isEmpty(accNo)) {
				postRequest.put("errMsg", "银行账号必填");
				return false;
			}
			if (StringUtils.isEmpty(preRequest.get("certType"))) {
				postRequest.put("errMsg", "证件类型必填");
				return false;
			}
			String certId = preRequest.get("certId");
			if (StringUtils.isEmpty(certId)) {
				postRequest.put("errMsg", "证件号码必填");
				return false;
			}
			if (certId.length()!=18) {
				postRequest.put("errMsg", "证件号码为18位数的字符串！");
				return false;
			}
			if (!accNo.matches(reg)) {
				postRequest.put("errMsg", "银行帐号为纯数字字符串！");
				return false;
			}
//			ExecutionStatus status = sandboxDataSetHandler.getStatusFromRemittanceApplicationDetail(
//					accNo, (String) preRequest.get("accName"));

			String paymentInstitution = preRequest.get("paymentInstitution");
			
//			if (StringUtils.isEmpty(paymentInstitution)) {
//				postRequest.put("errMsg","支付通道必填");
//				return false;
//			}
			
			boolean signStatus = sandboxDataSetHandler.existSignUpAndPutPaymentInstitutionsInPostRequest(null,accNo, productCode, paymentInstitution, postRequest);

			switch (preRequest.get("opType")) {
			case "2":
				if (StringUtils.isEmpty(preRequest.get("proNo"))) {
					postRequest.put("errMsg", "解约时协议号必填");
					return false;
				}
				if (!signStatus) {
					postRequest.put("errMsg", "银行账号未签约,无法进行解约操作");
					return false;
				}
				break;

			case "1":
//				if (!ExecutionStatus.SUCCESS.equals(status)) {
//					postRequest.put("errMsg", status.getChineseMessage());
//					return false;
//				}
				if (signStatus) {
					postRequest.put("errMsg", "银行账号已经签约");
					return false;
				}
				String phoneNo = preRequest.get("phoneNo");
				String proBeg = preRequest.get("proBeg");
				String proEnd = preRequest.get("proEnd");
				String tranMaxAmt = preRequest.get("tranMaxAmt");
				if (StringUtils.isEmpty(phoneNo)) {
					postRequest.put("errMsg", "签约时手机号必填");
					return false;
				}
				if (StringUtils.isEmpty(proBeg)) {
					postRequest.put("errMsg", "签约时协议开始日期必填");
					return false;
				}
				if (StringUtils.isEmpty(proEnd)) {
					postRequest.put("errMsg", "签约时协议截止日期必填");
					return false;
				}
				if (StringUtils.isEmpty(tranMaxAmt)) {
					postRequest.put("errMsg", "签约时单笔金额上限必填");
					return false;
				}
				try{
					BigDecimal amount = new BigDecimal(tranMaxAmt);
					if (amount.scale()>2) {
						postRequest.put("errMsg", "单笔金额精确到分！");
						return false;
					}
				}catch (Exception e) {
					postRequest.put("errMsg", "单笔金额必须为有效的金额格式！");
					return false;
				}
				if (!phoneNo.matches(reg)) {
					postRequest.put("errMsg", "手机号为纯数字！");
					return false;
				}
				try{
				Date begDate  = DateUtils.parseDate(proBeg,"yyyyMMdd");
				Date endDate  = DateUtils.parseDate(proEnd,"yyyyMMdd");
				if(null==endDate||null==begDate){
					postRequest.put("errMsg", "日期格式有误!");
					return false;
				}
				if (begDate.after(endDate)) {
					postRequest.put("errMsg", "截止日期应大于开始日期!");
					return false;
				}
				}catch (Exception e) {
					postRequest.put("errMsg", "日期格式有误!");
					return false;
				}
				
				break;
			default:
				postRequest.put("errMsg", "操作类型未知");
				return false;
			}
			
			
//			postRequest.put("bankMerId",bankMerId);
//			postRequest.put("signKey", signKey);
			postRequest.putAll(preRequest);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			postRequest.put("errMsg", "系统错误:"+e.getMessage());
			return false;
		}
	}
}