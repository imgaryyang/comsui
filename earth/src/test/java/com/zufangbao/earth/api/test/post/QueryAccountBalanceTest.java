package com.zufangbao.earth.api.test.post;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.coffer.GlobalSpec.BankCorpEps;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "classpath:/local/applicationContext-*.xml" })*/
public class QueryAccountBalanceTest extends BaseApiTestPost {

	@Test
	public void testApiQueryAccountBalance() {
		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("fn", "100010");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("productCode", "G08201");
		requestParams.put("accountNo", "11007187041901");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.err.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApiQueryAccountBalance1() {
		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("fn", "100010");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("productCode", "G08201");
		requestParams.put("accountNo", "5");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		
	}
}
