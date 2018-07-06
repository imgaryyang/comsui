package com.zufangbao.earth.api.test.post;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModifyRepaymentInformationApiPostTest extends BaseApiTestPost{

	@Test
	public void  testApi() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo",UUID.randomUUID().toString());
		requestParams.put("uniqueId", "overFXZ22");
//		requestParams.put("contractNo", "f82ee8de-a3ea-4113-b09f-a8e4908e1780");
		requestParams.put("bankCode", "C10105");
		requestParams.put("bankAccount", "6217001210075327592");
		requestParams.put("bankName", "jianzhang");
		requestParams.put("bankProvince", "310000");
		requestParams.put("bankCity", "310100");
		try {
			String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void  testApi_ZH() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo",UUID.randomUUID().toString());
		requestParams.put("uniqueId", "f82ee8de-a3ea-4113-b09f-a8e4908e1780");
//		requestParams.put("contractNo", "f82ee8de-a3ea-4113-b09f-a8e4908e1780");
		requestParams.put("bankCode", "C10105");
		requestParams.put("bankAccount", "6217001210075327591");
		requestParams.put("bankName", "jianzhang");
		requestParams.put("bankProvince", "310000");
		requestParams.put("bankCity", "310100");
		try {
			String sr = PostTestUtil.sendPost("http://avictctest.5veda.net/api/modify", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void  testApiUnqueIdAndContractNo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo", "123456789");
		requestParams.put("uniqueId", "12342354");
		requestParams.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
		requestParams.put("bankCode", "604");
		requestParams.put("bankAccount", "bankAccount");
		try {
			String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void  testApiNoBankNo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo", "1s23456789");
		requestParams.put("uniqueId", "12342354");
		requestParams.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
		requestParams.put("bankCode", "");
		requestParams.put("bankAccount", "bankAccount");
		try {
			String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"3");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void  testApiNoBankAccount() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo", "123d456789");
		requestParams.put("uniqueId", "12342354");
		requestParams.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
		requestParams.put("bankCode", "asdrt");
		requestParams.put("bankAccount", "");
		try {
			String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void  testApiNoBankNoContract() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo", "1234567f89");
		requestParams.put("uniqueId", "");
		requestParams.put("contractNo", "云信信2016-78-DK(ZQ201042522479)");
		requestParams.put("bankCode", "asdrt");
		requestParams.put("bankAccount", "asdas");
		try {
			String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"5");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
