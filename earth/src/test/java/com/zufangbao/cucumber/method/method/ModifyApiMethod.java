package com.zufangbao.cucumber.method.method;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.zufangbao.earth.api.test.post.PostTestUtil;

@Component
public class ModifyApiMethod extends BaseApiTestPost{
	/**
	 * 变更还款信息接口
	 */
	public void modifyRepaymentInformation(String uniqueId, String payerName,String bankCode,String bankName){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200003");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", uniqueId);
//		requestParams.put("contractNo", uniqueId);
//		requestParams.put("payerName", payerName);
//		requestParams.put("bankCode", bankCode);
		requestParams.put("bankAccount", "6214855712106530");
		requestParams.put("bankName", bankName);
		requestParams.put("bankProvince", "330000");
		requestParams.put("bankCity", "330100");
//		requestParams.put("repaymentChannel", "");
		try {
			String sr = PostTestUtil.sendPost(MODIFY_URL, requestParams,getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
