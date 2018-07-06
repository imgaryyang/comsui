package com.zufangbao.aboutDeduct;

import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.aboutDeduct.BaseApiTestPost.COMMAND_URL_TEST;

/**
 * Created by wubo on 2018/4/10.
 */
public class DeductBaseMethod {
	public String deductRepaymentPlanNEW(String requestNo, String deductId, String financialProductCode,
			String uniqueId, String contractNo, String apiCalledTime, String amount, String repaymentType,
			String notifyUrl, String payAcNo, String payerName, String bankCode, String mobile, String idCardNum,
			String loanFee, String otherFee, String repaymentAmount, String repaymentInterest, String repayScheduleNo,
			String repaymentPlanNo, String repaymentPrincipal, String techFee, String totalOverdueFee) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", requestNo);
		requestParams.put("deductId", deductId);
		requestParams.put("financialProductCode", financialProductCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", contractNo);
		requestParams.put("apiCalledTime", apiCalledTime);
		requestParams.put("amount", amount);
		requestParams.put("repaymentType", repaymentType);// 1、正常 0、提前划扣
		// requestParams.put("notifyUrl","http://192.168.0.212:7778/api/notify/internal/deduct");
		// requestParams.put("payerName", "测试007");
		// requestParams.put("bankCode", "testBank");//
		// requestParams.put("mobile","13258446545");
		// requestParams.put("idCardNum","330381199305101470");
		// requestParams.put("payAcNo","666662518000519910");
		// requestParams.put("payerName", "秦伟超");
		// requestParams.put("bankCode", "C101041);
		// requestParams.put("mobile","17682481004");
		// requestParams.put("idCardNum","421182199204114115");
		// requestParams.put("payAcNo","6217857600016839339");
		// requestParams.put("provinceCode","620000");
		// requestParams.put("cityCode","141100");
		// requestParams.put("payerName", "吴波");
		// requestParams.put("bankCode", "C10102");
		// requestParams.put("mobile","17682481004");
		// requestParams.put("idCardNum","3408231945143445435");
		// requestParams.put("payAcNo","66660123456789990");
		// requestParams.put("mobile","13258446545");
		// requestParams.put("idCardNum","132032512010000359");
		// requestParams.put("payAcNo","6258101645858218");

		// requestParams.put("repaymentDetails",
		// "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repayScheduleNo':'"+repayScheduleNo+"','repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':'"+repaymentPrincipal+"','techFee':0.00,'overDueFeeDetail':{"
		// requestParams.put("repaymentDetails",
		// "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+repaymentPrincipal+",'techFee':0.00,'overDueFeeDetail':{"
		// +
		// "'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':4.00"
		// + "'totalOverdueFee':0.00}}]");

		requestParams.put("repaymentDetails",
				"[{'loanFee':'" + loanFee + "'," + "'otherFee':'" + otherFee + "'," + "'repaymentAmount':'"
						+ repaymentAmount + "'," + "'repaymentInterest':'" + repaymentInterest + "',"
						+ "'repayScheduleNo':'" + repayScheduleNo + "'," + "'repaymentPlanNo':'" + repaymentPlanNo
						+ "'," + "'repaymentPrincipal':'" + repaymentPrincipal + "'," + "'techFee':'" + techFee + "',"
						+ "'overDueFeeDetail':{" + "'totalOverdueFee':'" + totalOverdueFee + "'}}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams,
					new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
			return sr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 和deductRepaymentPlanNEW的差异在方法中去掉了notifyUrl参数
	public String deductRepaymentPlanNEW1(String requestNo, String deductId, String financialProductCode,
			String uniqueId, String contractNo, String apiCalledTime, String amount, String repaymentType,
			String payAcNo, String payerName, String bankCode, String mobile, String idCardNum, String loanFee,
			String otherFee, String repaymentAmount, String repaymentInterest, String repayScheduleNo,
			String repaymentPlanNo, String repaymentPrincipal, String techFee, String totalOverdueFee) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", requestNo);
		requestParams.put("deductId", deductId);
		requestParams.put("financialProductCode", financialProductCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", contractNo);
		requestParams.put("apiCalledTime", apiCalledTime);
		requestParams.put("amount", amount);
		requestParams.put("repaymentType", repaymentType);// 1、正常 0、提前划扣 2逾期

		requestParams.put("repaymentDetails",
				"[{'loanFee':'" + loanFee + "'," + "'otherFee':'" + otherFee + "'," + "'repaymentAmount':'"
						+ repaymentAmount + "'," + "'repaymentInterest':'" + repaymentInterest + "',"
						+ "'repayScheduleNo':'" + repayScheduleNo + "'," + "'repaymentPlanNo':'" + repaymentPlanNo
						+ "'," + "'repaymentPrincipal':'" + repaymentPrincipal + "'," + "'techFee':'" + techFee + "',"
						+ "'overDueFeeDetail':{" + "'totalOverdueFee':'" + totalOverdueFee + "'}}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams,
					new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
			return sr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 包含两个还款计划
	public String deductRepaymentPlanNEW3(String requestNo, String deductId, String financialProductCode,
			String uniqueId, String contractNo, String apiCalledTime, String amount, String repaymentType,
			String payAcNo, String payerName, String bankCode, String mobile, String idCardNum, String loanFee,
			String otherFee, String repaymentAmount, String repaymentInterest, String repayScheduleNo,
			String repaymentPlanNo, String repaymentPrincipal, String techFee, String totalOverdueFee) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", requestNo);
		requestParams.put("deductId", deductId);
		requestParams.put("financialProductCode", financialProductCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", contractNo);
		requestParams.put("apiCalledTime", apiCalledTime);
		requestParams.put("amount", amount);
		requestParams.put("repaymentType", repaymentType);// 1、正常 0、提前划扣 2逾期

		requestParams.put("repaymentDetails",
				"[{'loanFee':'" + loanFee + "'," + "'otherFee':'" + otherFee + "'," + "'repaymentAmount':'"
						+ repaymentAmount + "'," + "'repaymentInterest':'" + repaymentInterest + "',"
						+ "'repayScheduleNo':'" + repayScheduleNo + "'," + "'repaymentPlanNo':'" + repaymentPlanNo
						+ "'," + "'repaymentPrincipal':'" + repaymentPrincipal + "'," + "'techFee':'" + techFee + "',"
						+ "'overDueFeeDetail':{" + "'totalOverdueFee':'" + totalOverdueFee + "'}}," + "{'loanFee':'"
						+ loanFee + "'," + "'otherFee':'" + otherFee + "'," + "'repaymentAmount':'" + repaymentAmount
						+ "'," + "'repaymentInterest':'" + repaymentInterest + "'," + "'repayScheduleNo':'"
						+ repayScheduleNo + "'," + "'repaymentPlanNo':'" + repaymentPlanNo + "',"
						+ "'repaymentPrincipal':'" + repaymentPrincipal + "'," + "'techFee':'" + techFee + "',"
						+ "'overDueFeeDetail':{" + "'totalOverdueFee':'" + totalOverdueFee + "'}}]" );
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams,
					new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
			return sr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String deductRepaymentPlanNEW2(String requestNo, String deductId, String financialProductCode,
			String uniqueId, String contractNo, String apiCalledTime, String amount, String repaymentType,
			String payAcNo, String payerName, String bankCode, String mobile, String idCardNum, String loanFee,
			String otherFee, String repaymentAmount, String repaymentInterest, String repayScheduleNo,
			String repaymentPlanNo, String repaymentPrincipal, String techFee, String totalOverdueFee,
			String provinceCode, String cityCode, String gateway, String penaltyFee, String latePenalty, String lateFee,
			String lateOtherCost) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", requestNo);
		requestParams.put("deductId", deductId);
		requestParams.put("financialProductCode", financialProductCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", contractNo);
		requestParams.put("apiCalledTime", apiCalledTime);
		requestParams.put("amount", amount);
		requestParams.put("repaymentType", repaymentType);// 1、正常 0、提前划扣 2逾期

		requestParams.put("payerName", payerName);
		requestParams.put("idCardNum", idCardNum);
		requestParams.put("payAcNo", payAcNo);
		requestParams.put("bankCode", bankCode);
		requestParams.put("provinceCode", provinceCode);
		requestParams.put("cityCode", cityCode);
		requestParams.put("mobile", mobile);
		requestParams.put("gateway", gateway);

		requestParams.put("penaltyFee", penaltyFee);
		requestParams.put("latePenalty", latePenalty);
		requestParams.put("lateFee", lateFee);
		requestParams.put("lateOtherCost", lateOtherCost);

		requestParams.put("repaymentDetails",
				"[{'loanFee':'" + loanFee + "'," + "'otherFee':'" + otherFee + "'," + "'repaymentAmount':'"
						+ repaymentAmount + "'," + "'repaymentInterest':'" + repaymentInterest + "',"
						+ "'repayScheduleNo':'" + repayScheduleNo + "'," + "'repaymentPlanNo':'" + repaymentPlanNo
						+ "'," + "'repaymentPrincipal':'" + repaymentPrincipal + "'," + "'techFee':'" + techFee + "',"
						+ "'overDueFeeDetail':{" + "'penaltyFee':'" + penaltyFee + "'," + "'latePenalty':'"
						+ latePenalty + "'," + "'lateFee':'" + lateFee + "'," + "'lateOtherCost':'" + lateOtherCost
						+ "'," + "'totalOverdueFee':'" + totalOverdueFee + "'}}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams,
					new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
			return sr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
