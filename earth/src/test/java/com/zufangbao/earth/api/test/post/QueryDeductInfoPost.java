package com.zufangbao.earth.api.test.post;

import net.sf.json.JSONArray;
import org.junit.Test;

import java.util.*;

public class QueryDeductInfoPost extends BaseApiTestPost{
	
	
	
	
	@Test
	public void testDeductInfoList() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100002");
		requestParams.put("deductId", "6bac002b-3d28-4780-b016-96c2f9f3ec3b");
		requestParams.put("financialProductCode","HZN321");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", "overFXZ46");
		try {
			String sr = PostTestUtil.sendPost("http://192.168.0.128:8083/api/query", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testBatchDeductInfoList() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100006");
		requestParams.put("financialProductCode","HE0900");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		List<String> deductIdLists = new ArrayList<>();
		deductIdLists.add("b3fc005e-c71f-4205-8efe-b4697f6a69f0");
		deductIdLists.add("0a2db376-269d-46f4-ae0e-98c36a60d923");
		JSONArray deductIdList = JSONArray.fromObject(deductIdLists);
		requestParams.put("deductIdList",deductIdList.toString());
		try {
			String sr = PostTestUtil.sendPost("http://192.168.0.128:8083/api/query", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeductInfoList1() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100002");
		requestParams.put("deductId", "");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", "TEST_ZSH003");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
