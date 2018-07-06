package com.zufangbao.earth.api.test.post;

import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import net.sf.json.JSONArray;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * 提前全额还款post请求
 * 
 * @author zhanghongbing
 *
 */
public class MutableFeeApiPost extends BaseApiTestPost {

	@Test
	public void testApi_V2() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "CS0001");
		requestParams.put("contractNo", "061c609e-d7b3-4e70-857a-08568fb4fb1e");

		requestParams.put("repaymentPlanNo", "ZC184741864820436992");
//		requestParams.put("repayScheduleNo","eb6aa937-b0e8-4e55-812f-b866d7e03692");
		requestParams.put("reasonCode", "0");
		requestParams.put("approver", "樊肖繁");
		requestParams.put("approvedTime", "2017-12-05");
		requestParams.put("comment", "TestInterface");

		MutableFeeDetail detail = new MutableFeeDetail(1,new BigDecimal("20"));
		MutableFeeDetail detail1 = new MutableFeeDetail(2,new BigDecimal("20"));
		MutableFeeDetail detail2 = new MutableFeeDetail(3,new BigDecimal("20"));
		MutableFeeDetail detail3 = new MutableFeeDetail(4,new BigDecimal("20"));

		List<MutableFeeDetail> details=new ArrayList<>();
		details.add(detail);
		details.add(detail1);
		details.add(detail2);
		details.add(detail3);
		JSONArray jsonArray = JSONArray.fromObject(details);
		requestParams.put("details", jsonArray.toString());

		try {
			String sr = PostTestUtil.sendPost(MUTABLE_FEE, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
