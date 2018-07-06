package com.suidifu.bridgewater.api.test.post;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.mielong.AbstractNotRollBackBaseTest;
import com.zufangbao.gluon.spec.earth.v3.CommandOpsFunctionCodes;

/**
 * 
 * 贵族专用测试类（POST）
 *
 */
public class RemittanceMieLongPost extends AbstractNotRollBackBaseTest {

	/**
	 * 二次放款POST
	 */
	@Test
	public void te() {}
	

	@Test
	public void testModifyRemittanceApplication() {
		String url = "http://127.0.0.1:9092/api/v2/modify-remittanceApplication";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("tradeNo", UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "G31700");
		requestParams.put("remittanceTradeNo", "d7502e5a-e37d-42bc-ace1-b5a4144fc18a");
		requestParams.put("approver", "auditorName1");
		requestParams.put("approvedTime", "2016-08-20 17:34:33");
		requestParams.put("comment", "ercifk");
		String bankCardNo = "6214855712106520";
		String amount = "2000";

		requestParams.put("remittanceDetails",
				"["
				+ "{'detailNo':'detailNo2','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount
					+ "','bankCode':'C10305','bankCardNo':'" + bankCardNo
					+ "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}"+
						"]");
		try {
			String sr = PostTestUtil.sendPost(url, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *云信首次放款1条明细 
	 */
	public void testApiCommandRemittanceYX11111(String url, int execCount, String notifyUrl, List<String> productCodes) {
		for (int i = 0; i < execCount; i++) {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", "requestNo"+UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			int index = (int)(Math.random()*(productCodes.size()));
			requestParams.put("productCode", productCodes.get(index));
			requestParams.put("uniqueId", "uniqueId"+UUID.randomUUID().toString());
			requestParams.put("contractNo", "contractNo"+UUID.randomUUID().toString());
			String amount = "1500";
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "remittanceId"+UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", notifyUrl);
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails",
					"[{'detailNo':'detailNo2','recordSn':'2','bankAliasName':'ICBC','amount':'" + amount
							+ "','bankCode':'C10305','bankCardNo':'" + bankCardNo
							+ "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}]");
			try {
				String sr = PostTestUtil.sendPost(url, requestParams, getIdentityInfoMap(requestParams));
				Map<String, Object> result = new HashMap<>();
				result = JsonUtils.parse(sr);
				Assert.assertEquals(0, result.get("code"));
				Assert.assertEquals("成功!", result.get("message"));
				System.out.println(sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *云信首次放款2条明细 
	 */
	public void testApiCommandRemittanceYX22222(String url, int execCount, String notifyUrl, List<String> productCodes) {
		for (int i = 0; i < execCount; i++) {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			int index = (int)(Math.random()*(productCodes.size()));
			requestParams.put("productCode", productCodes.get(index));
			requestParams.put("uniqueId", UUID.randomUUID().toString());
			requestParams.put("contractNo", UUID.randomUUID().toString());
			String amount = "1300";
			requestParams.put("plannedRemittanceAmount",
					new BigDecimal(amount).multiply(new BigDecimal("2")).toString());
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "wsh" + UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", notifyUrl);
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails",
					"[{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount
							+ "','bankCode':'C10305','bankCardNo':'" + bankCardNo
							+ "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'},"
							+ "{'detailNo':'detailNo2','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount
							+ "','bankCode':'C10305','bankCardNo':'" + bankCardNo
							+ "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}"
							+ "]");
			try {
				String sr = PostTestUtil.sendPost(url, requestParams, getIdentityInfoMap(requestParams));
				Map<String, Object> result = new HashMap<>();
				result = JsonUtils.parse(sr);
				Assert.assertEquals(0, result.get("code"));
				Assert.assertEquals("成功!", result.get("message"));
				System.out.println(sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *转账
	 * @param notifyUrl TODO
	 * @param productCodes TODO
	 */
	public void testApiCommandRemittanceYX33333(String url, int execCount, String notifyUrl, List<String> productCodes) {
		for (int i = 0; i < execCount; i++) {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", CommandOpsFunctionCodes.COMMAND_TRANSFER_COMMAND);
			requestParams.put("requestNo", "requestNo"+UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			int index = (int)(Math.random()*(productCodes.size()));
			requestParams.put("productCode", productCodes.get(index));
			String amount = "1200";
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "remittanceId"+UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", notifyUrl);
			requestParams.put("transferTransactionType", "0");
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails",
					"[{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount
							+ "','bankCode':'C10305','bankCardNo':'" + bankCardNo
							+ "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}]");
			try {
				String sr = PostTestUtil.sendPost(url, requestParams, getIdentityInfoMap(requestParams));
				Map<String, Object> result = new HashMap<>();
				result = JsonUtils.parse(sr);
				Assert.assertEquals(0, result.get("code"));
				Assert.assertEquals("成功!", result.get("message"));
				System.out.println(sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
