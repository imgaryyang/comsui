package com.zufangbao.earth.api.test.post;


import com.demo2do.core.entity.Result;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PostTestUtil {

	public static String sendPost(String url, Map<String, String> params, Map<String, String> headerMap) throws Exception {
		Result result = HttpClientUtils.executePostRequest(url, params, headerMap);
		String resultStr = JsonUtils.toJSONString(result);
//		System.out.println("post result string:" + resultStr);
		return resultStr;
	}
	
	private static String buildParams(Map<String, String> params) {
		StringBuffer buffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (Entry<String, String> entry : params.entrySet()) {
			String value = entry.getValue();
			value = StringUtils.isEmpty(value) ? "" : value;
			buffer.append(entry.getKey() + "=" + value + "&");
		}
		return buffer.toString();
	}
	
}
