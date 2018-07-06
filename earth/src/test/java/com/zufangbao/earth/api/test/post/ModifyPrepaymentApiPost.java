package com.zufangbao.earth.api.test.post;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 提前全额还款post请求
 * @author zhanghongbing
 *
 */
public class ModifyPrepaymentApiPost extends BaseApiTestPost{
	
	@Test
	public void testApiModifyPrepaymentPlan() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200002");
//		requestParams.put("financialProductCode","G31700");
		requestParams.put("uniqueId", "86f5b89e-b05b-428e-a3d3-52df7a0558d0");
//		requestParams.put("contractNo", "overWrite2");
//		requestParams.put("repayScheduleNo","332221");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("assetRecycleDate", "2018-04-26");
		requestParams.put("assetInitialValue", "20000");
		requestParams.put("assetPrincipal", "20000");
		requestParams.put("assetInterest", "0");
		requestParams.put("serviceCharge", "0");
		requestParams.put("maintenanceCharge", "0");
		requestParams.put("otherCharge", "0");
		requestParams.put("type", "0");
		requestParams.put("payWay","0");
		//requestParams.put("hasDeducted", "-1");
		
		try {
			String sr = PostTestUtil.sendPost(PREPAYMENT, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
