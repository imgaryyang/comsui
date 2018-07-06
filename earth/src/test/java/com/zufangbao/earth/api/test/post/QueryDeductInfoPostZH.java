package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import net.sf.json.JSONArray;
import org.junit.Test;

import java.util.*;

public class QueryDeductInfoPostZH extends BaseApiTestPost{
	
	
	
	//扣款查询
	@Test
	public void testDeductInfo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100002");
		requestParams.put("deductId", "fantengwar019");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", "fanteng");
		requestParams.put("merId","t_test_zfb");
		requestParams.put("secret","123456");
		try {
			String sr = PostTestUtil.sendPost("http://avictctest.5veda.net:8887/api/query/deduct_status", requestParams, getIdentityInfoMap(requestParams));//http://60.190.243.66:8887/api/query/deduct_status
			//192.168.0.206:3080
			System.out.println(sr+1);
			System.out.print(JsonUtils.toJsonString(requestParams));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//批量放款查询
	@Test
	public void testRemittanceInfoList() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100011");
		requestParams.put("requestNo", UUID.randomUUID().toString());

		String str = "[{\"oriRequestNo\":\"20170825152137\",\"uniqueId\":\"systemdeductHT201705101800000029970\"}]";
		requestParams.put("remittanceResultBatchQueryModels", str);

		requestParams.put("merId","t_test_zfb");
		requestParams.put("secret","123456");
		try {
			String sr = PostTestUtil.sendPost("http://avictctest.5veda.net:8886/api/query/remittance_status_list", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+1);             //http://60.190.243.66:8886/api/query/remittance_status_list
			System.out.print(JsonUtils.toJsonString(requestParams));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//批量扣款查询
	@Test
	public void testDeductInfoList() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100006");
//		requestParams.put("deductId", "");
		requestParams.put("requestNo", UUID.randomUUID().toString());
//		requestParams.put("uniqueId", "TEST_ZSH003");
//		String deductIdList = "[{\"deductId\":\"fantengwar1111\"}]";
		List<String> deductIdList = new ArrayList<>();
		deductIdList.add("33175be0-8a7d-4aef-89e0-3c32834d5671");
		deductIdList.add("33175be0-8a7d-4aef-89e0-3c32834d5672");
//		deductIdList.add("fantengwar1113");


//		List<String> repaymentNoList = new ArrayList<>();
//		repaymentNoList.add("ZC11223344556677");
//		repaymentNoList.add("ZC11223344556678");
//		repaymentNoList.add("ZC11223344556679");
//		JSONArray repaymentNoLists = JSONArray.fromObject(repaymentNoList);
		JSONArray deductIdLists = JSONArray.fromObject(deductIdList);

		requestParams.put("deductIdList",deductIdLists.toString());
//		requestParams.put("repaymentPlanCodeList","123321");
		requestParams.put("repaymentType","2");
		try {
			String sr = PostTestUtil.sendPost("http://avictctest.5veda.net:8887/api/query/deduct_status_list", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//专户实时余额查询
	@Test
	public void queryFinancialContract(){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100010");
		requestParams.put("requestNo",UUID.randomUUID().toString());
		requestParams.put("productCode","11111111");
		requestParams.put("accountNo","600000000001");

		try {
			String sr = PostTestUtil.sendPost("http://avictctest.5veda.net/api/query", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
