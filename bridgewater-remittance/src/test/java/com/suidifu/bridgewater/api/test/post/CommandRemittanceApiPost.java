package com.suidifu.bridgewater.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.LongAdder;


@Rollback(false)
public class CommandRemittanceApiPost extends BaseApiTestPost{

//	@Test
//	public void test() {
//		String b1 = " http://192.168.0.183:9092/api/command";
//		String b2 = "5";
//		String b3 = "10";
//		try {
//			ThreadTestX.run_start(b1, b2, b3);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Test
	//@Sql("classpath:test/remittance/test_qutoaPriority.sql")
	public void testApiCommandRemittanceZH() {
		for (int i = 0; i < 1; i++) {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", "G31700");
			requestParams.put("uniqueId", UUID.randomUUID().toString());
			requestParams.put("contractNo", "妹妹你大胆的往前走153");
			String amount = "1500.00";
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "wsh" + UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", "http://hahahah");
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo2','recordSn':'1','bankAliasName':'ICBC','amount':'1500','bankCode':'102100026864'," +
					"'bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1'," +
					"'bankName':'中国工商银行股份有限公司北京通州支行玉桥西里分理处','idNumber':'idNumber1'}]");
			try {
				String sr = PostTestUtil.sendPost("http://192.168.0.168:9092/api/command", requestParams, getIdentityInfoMap(requestParams));
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

	@Test
	public void testApiCommandRemittanceYX11111(String url,int execCount) {
		for (int i = 0; i < execCount; i++) {
//			String id = "153";
			String id = UUID.randomUUID().toString();
			
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", "G31700");
			requestParams.put("uniqueId", "妹妹你大胆的往前走"+id);
			requestParams.put("contractNo", "妹妹你大胆的往前走"+id);
			String amount = "1500";
			requestParams.put("plannedRemittanceAmount", "1500");
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "wsh" + UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", "http://hahahah");
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount + "','bankCode':'C10305','bankCardNo':'" + bankCardNo +
					"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}]");
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
	
	@Test
	public void testApiCommandRemittanceYX22222(String url,int execCount) {
		for (int i = 0; i < execCount; i++) {
			String id = UUID.randomUUID().toString();
			
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", "G31700");
			requestParams.put("uniqueId", "妹妹你大胆的往前走"+id);
			requestParams.put("contractNo", "妹妹你大胆的往前走"+id);
			String amount = "2000";
			requestParams.put("plannedRemittanceAmount", new BigDecimal(amount).multiply(new BigDecimal("2")).toString());
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "wsh" + UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", "http://hahahah");
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount + "','bankCode':'C10305','bankCardNo':'" + bankCardNo +
					"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'},"
					+"{'detailNo':'detailNo2','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount + "','bankCode':'C10305','bankCardNo':'" + bankCardNo +
						"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}"
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

	LongAdder longAdder = new LongAdder();

	@Test
	public void test11111() {
		testApiCommandRemittanceZH2("http://192.168.0.159:38083/api/command", 1);
	}

	public void testApiCommandRemittanceZH2(String url, int execCount) {
		for (int i = 0; i < execCount; i++) {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", "G31700");
			requestParams.put("uniqueId", UUID.randomUUID().toString());
			requestParams.put("contractNo", "妹妹你大胆的往前走150");
			String amount = "1500";
			requestParams.put("plannedRemittanceAmount", "1500");
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-10-15 00:00:00");
			requestParams.put("remittanceId", "wsh" + UUID.randomUUID().toString());
			requestParams.put("remark", "交易备注");
			requestParams.put("notifyUrl", "http://www.mocky.io/v2/5185415ba171ea3a00704eed");
			System.out.print(requestParams.get("remittanceId") + "     ");
			String bankCardNo = "6214855712106520";
			requestParams.put("remittanceDetails", "["
					+ "{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'CMBC','amount':'" + amount + "','bankCode':'C10305','bankCardNo':'" + bankCardNo +
					"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国民生银行','idNumber':'idNumber1'}]");
//				+ "{'detailNo':'detailNo2','recordSn':'1','bankAliasName':'ICBC','amount':'"+amount+"','bankCode':'102100026864','bankCardNo':'"+bankCardNo+"',
// 'bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'中国工商银行股份有限公司北京通州支行玉桥西里分理处','idNumber':'idNumber1'}");
			try {
				String sr = PostTestUtil.sendPost(url, requestParams, getIdentityInfoMap(requestParams));
				Map<String, Object> result = JsonUtils.parse(sr);
				longAdder.add(1);
//				if (result.get("message").equals("成功!")) {
//					System.out.println(longAdder + "  " + "成功！");
//					ThreadTestX.sucLongAdder.add(1);
//				} else {
//					System.out.println(longAdder + "  " + "失败！" + result.get("message"));
//					ThreadTestX.failLongAdder.add(1);
//				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(longAdder + "  " + "失败！");
			}
		}
	}

	@Test
	//@Sql("classpath:test/remittance/test_qutoaPriority.sql")
	public void testApiCommandRemittanceYunXin() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", "11111111");
		requestParams.put("uniqueId", "EW207896");
		requestParams.put("contractNo", "82a93986-d12b-4eaa-9d57-cfbfbbf1e829");
		String amount = "1500";
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2017-10-15 00:00:00");
		requestParams.put("remittanceId", "wshbest123");
		requestParams.put("remark", "交易备注");
		requestParams.put("notifyUrl", "http://hahahah");
		String bankCardNo = "6214855712106520";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'ZHTOT','amount':'" + amount + "'," +
				"'plannedDate':'2017-10-16" +
				" " +
				"00:00:00','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1'," +
				"'bankCity':'bankCity1'," +
				"'bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost("http://127.0.0.1:9092/pre/api/remittance/remittance-pre/zhonghang", requestParams, getIdentityInfoMap(requestParams));
			Map<String, Object> result = new HashMap<>();
			result = JsonUtils.parse(sr);
			Assert.assertEquals(0, result.get("code"));
			Assert.assertEquals("成功!", result.get("message"));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/remittance/test_qutoaPriority.sql")
	public void testApiCommandRemittance1() {//配置限额超出最高限额
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", "G40900");
		requestParams.put("uniqueId", "FANT077q");
		requestParams.put("contractNo", "FANT077q");
		String amount = "1600.01";
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2017-04-15 00:00:00");
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6214855712106520";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'2017-04-14 00:00:00'," +
				"'bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1'," +
				"'bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			Map<String, Object> result = new HashMap<>();
			result = JsonUtils.parse(sr);
			Assert.assertEquals(22100, result.get("code"));
			Assert.assertEquals("支付通道不存在", result.get("message"));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/remittance/test_qutoaPriority.sql")
	public void testApiCommandRemittance2() {//配置限额超出最低限额
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", "G40900");
		requestParams.put("uniqueId", "FAS078");
		requestParams.put("contractNo", "FAS078");
		String amount = "1499.99";
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2017-04-15 00:00:00");
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6214855712106520";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'2017-04-14 00:00:00'," +
				"'bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1'," +
				"'bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			Map<String, Object> result = new HashMap<>();
			result = JsonUtils.parse(sr);
			Assert.assertEquals(22100, result.get("code"));
			Assert.assertEquals("支付通道不存在", result.get("message"));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/* 二次放款回调*/
	@Test
	public void testApiCommandRemittanceFF() {
		String int_url = "http://192.168.0.128:8084/api/command";
		String out_url = "http://remittance.5veda.net/api/command";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", "4326a757-7d98-41ab-abcd-ttkh");//每次请求都需要变动
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", "G31700");
		requestParams.put("uniqueId", UUID.randomUUID().toString());
		requestParams.put("contractNo", UUID.randomUUID().toString());
		requestParams.put("plannedRemittanceAmount", "3.33");
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2016-08-20 00:00:00");
		requestParams.put("remark", "交易备注");
		requestParams.put("remittanceDetails",
				"[{'detailNo':'detailNo1','recordSn':'1','amount':'1.11','bankCode':'C10102','bankCardNo':'5685968545868856','bankAccountHolder':'汪水'," +
						"'bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}"
						+ ",{'detailNo':'detailNo2','recordSn':'2','amount':'2.22','bankCode':'C10102','bankCardNo':'123456789','bankAccountHolder':'测试用户2'," +
						"'bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}"
//				+ ",{'detailNo':'detailNo3','recordSn':'3','amount':'1.11','bankCode':'C10102','bankCardNo':'5685968545868856','bankAccountHolder':'测试用户2',
// 'bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}"
						+ "]");
		try {
			String sr = PostTestUtil.sendPost(out_url, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testModifyRemittanceApplication() {
		String int_url = "http://192.168.0.128:8084/api/v2/modify-remittanceApplication";
		String out_url = "http://remittance.5veda.net/api/v2/modify-remittanceApplication";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("tradeNo", UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "G31700");
		requestParams.put("remittanceTradeNo", "4326a757-7d98-41ab-abcd-ttkh");
		requestParams.put("approver", "auditorName1");
		requestParams.put("approvedTime", "2016-08-20 17:34:33");
		requestParams.put("comment", "ercifk");
		requestParams.put("remittanceDetails",
				"[{'detailNo':'detailNo1','recordSn':'1','amount':'2.22','bankCode':'C10102','bankCardNo':'5685968545868858','bankAccountHolder':'汪水'," +
						"'bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}" +
//                            + ",{'detailNo':5veda'detailNo2','recordSn':'2','amount':'1.11','bankCode':'C10102','bankCardNo':'56859685458688',
// 'bankAccountHolder':'汪水','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}" +
						"]");
		try {
			String sr = PostTestUtil.sendPost(out_url, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
