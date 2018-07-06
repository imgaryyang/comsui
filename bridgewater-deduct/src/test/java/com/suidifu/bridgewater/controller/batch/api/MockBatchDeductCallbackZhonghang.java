/**
 * 
 */
package com.suidifu.bridgewater.controller.batch.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;

/**
 * @author wukai
 *
 */
@Controller
@RequestMapping(value="/mock/deduct/")
public class MockBatchDeductCallbackZhonghang {

	@ResponseBody
	@RequestMapping(value="batch/success")
	public String callback(HttpServletRequest request) throws IOException{
		
		Enumeration<String> headers = request.getHeaderNames();
		
//		System.out.println("request header begin");
		
		while(headers.hasMoreElements()){
			
			System.out.println(headers.nextElement());
		}
//		System.out.println("request header end");
//		
//		System.out.println("request parameters:"+JsonUtils.toJsonString(RequestUtil.getRequestParams(request)));
		
		String bodyContent = "";
		
		StringBuffer sb = new StringBuffer();
		
		BufferedReader reader = request.getReader();
		
		while((bodyContent=reader.readLine())!=null){
			
			sb.append(bodyContent);
		}
		
		String content = sb.toString();
		
		System.out.println("body content:["+content+"]");
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		Map<String,Object> repsonseMap = new HashMap<String,Object>();
		
		repsonseMap.put("isSuccess", true);
		repsonseMap.put("responseCode", "0000");
		
		data.put("status", repsonseMap);
		
		return JsonUtils.toJsonString(data);
	}
	@ResponseBody
	@RequestMapping(value="single/success")
	public String callbackForSingle(HttpServletRequest request) throws IOException{
		
		Enumeration<String> headers = request.getHeaderNames();
		
//		System.out.println("request header begin");
		
		while(headers.hasMoreElements()){
			
			System.out.println(headers.nextElement());
		}
//		System.out.println("request header end");
//		
//		System.out.println("request parameters:"+JsonUtils.toJsonString(RequestUtil.getRequestParams(request)));
		
		String bodyContent = "";
		
		StringBuffer sb = 	new StringBuffer();
		
		BufferedReader reader = request.getReader();
		
		while((bodyContent=reader.readLine())!=null){
			
			sb.append(bodyContent);
		}
		
		String content = sb.toString();
		
		System.out.println("body content:["+content+"]");
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		Map<String,Object> repsonseMap = new HashMap<String,Object>();
		
		repsonseMap.put("isSuccess", true);
		repsonseMap.put("responseCode", "0000");
		
		data.put("status", repsonseMap);
		
		return JsonUtils.toJsonString(data);
	}
}
