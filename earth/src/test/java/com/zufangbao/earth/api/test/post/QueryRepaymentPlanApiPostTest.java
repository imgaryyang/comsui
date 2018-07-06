package com.zufangbao.earth.api.test.post;


import com.alibaba.fastjson.JSONArray;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//		"classpath:/local/applicationContext-*.xml" })

public class QueryRepaymentPlanApiPostTest extends BaseApiTestPost{
	
	@Test
	public void testApiQueryRepaymentPlan() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100001");
//		requestParams.put("productCode", "CS0001");
//		requestParams.put("contractNo", "overFZZ299");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", "a8b81dc0-9ea6-4a04-9fce-c7a055a80fb6");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			Result result = JsonUtils.parse(sr, Result.class);
			Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) responsePacket.get("repaymentPlanDetails");
			String repaymentPlanCount = repaymentPlanDetails.size()+"";
			System.out.println(repaymentPlanCount);
			System.out.println(responsePacket);
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testBatchApiQueryRepaymentPlan() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100001");
		requestParams.put("productCode", "CS0001");
//		requestParams.put("contractNo", "['2016-236-DK(30325121787007217ht)号']");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", "08d2d80b-eff3-4bee-8704-f4dbd7dbb0401");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApiQueryRepaymentPlanUniqueId() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100001");
		requestParams.put("contractNo", "TEST008");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", "");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public  void testApiQueryRepaymentError(){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100001");
		requestParams.put("contractNo", "2016-78-DK(ZQ2016042522479)");
		requestParams.put("requestNo", "456");
		requestParams.put("uniqueId", "");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
    public  void testApiQueryRepaymentError_no(){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "100001");
        requestParams.put("requestNo", "");
        try {
            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr+4);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
}
