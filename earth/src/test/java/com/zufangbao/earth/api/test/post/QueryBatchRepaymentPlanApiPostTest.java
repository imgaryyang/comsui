package com.zufangbao.earth.api.test.post;

import net.sf.json.JSONArray;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class QueryBatchRepaymentPlanApiPostTest extends BaseApiTestPost{
	
//	public static final String QUERY_URL_TEST = "http://127.0.0.1:9090/api/query";
	
	@Test
	public void testApiQueryBatchRepaymentPlan() throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100005");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("productCode", "CS0001");
		List<String> uniqueIds = new ArrayList<>();
		uniqueIds.add("测试还款订单扣款回调(重试5).7099");
		JSONArray array = JSONArray.fromObject(uniqueIds);
		requestParams.put("uniqueIds",array.toString());
		requestParams.put("planRepaymentDate", "2018-11-06");

//		File file = new File("/Users/apple/Desktop/contractUuidG31700.txt");
//		String uniqueIds = FileUtils.readFileToString(file);

//		requestParams.put("uniqueIds", uniqueIds);
//		requestParams.put("uniqueIds", "['987188dd-9553-4273-850d-f7066df02ff3','69a423d0-b4dc-49bd-b958-ceaf34d53a6e','30d82ec5-1b94-44d1-af17-5de6ffb5e1ac']");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
//			File out = new File("/Users/apple/Desktop/contractUuidG31700"+ UUID.randomUUID().toString()+".txt");
//			FileUtils.write(out, sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
