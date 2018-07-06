package com.zufangbao.earth.api.test.post;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })

public class QueryRepaymentPlanDetailsApiPostTest extends BaseApiTestPost{
	
	@Test
	public void testApiQueryRepaymentPlan() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100009");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("contractNo", "G31700");
		requestParams.put("singleLoanContractNo", "ZC27563C12B29C44D9");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"jiangxu");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApiQueryRepaymentPlan_noRequestNo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100009");
		requestParams.put("requestNo", "");
		requestParams.put("contractNo", "G31700");
		requestParams.put("singleLoanContractNo", "ZC1355632970713235456");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"jiangxu1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApiQueryRepaymentPlan_noContractNo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100009");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("contractNo", "");
		requestParams.put("singleLoanContractNo", "ZC1283400030079578112");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"jiangxu2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApiQueryRepaymentPlan_noSingleLoanContractNo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100009");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("contractNo", "G31700");
		requestParams.put("singleLoanContractNo", "");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"jiangxu3");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApiQueryRepaymentPlan_contractNoNotAgreeWithSingleLoanContractNo() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100009");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("contractNo", "G08200");
		requestParams.put("singleLoanContractNo", "ZC1283400030079578112");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr+"jiangxu4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
