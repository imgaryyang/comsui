package com.zufangbao.testAPIWuBo;


import com.demo2do.core.entity.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class PostTestUtilhuaruitest {
	
	private static final Log logger = LogFactory.getLog(PostTestUtil.class);
	
	private static final int DEFAULT_TIME_OUT = 60000;
	
	public static final String DATA_RESPONSE_PACKET = "responsePacket";
	
	public static final String DATA_RESPONSE_HTTP_STATUS = "responseHttpStatus";

	public static String sendPost(String url, Map<String, String> params, Map<String, String> headerMap) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(buildParams(params));
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
			throw e;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
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
	
	
//	public static String sendPostWithEncryption(String url, Map<String, String> headerMap, String requestBody) throws Exception {
//		PrintWriter out = null;
//		BufferedReader in = null;
//		String result = "";
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			if(headerMap != null) {
//				for (Entry<String, String> entry : headerMap.entrySet()) {
//					conn.setRequestProperty(entry.getKey(), entry.getValue());
//				}
//			}
//			
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			// 获取URLConnection对象对应的输出流
//			out = new PrintWriter(conn.getOutputStream());
//			// 发送请求参数
//			out.print(buildParams(params));
//			// flush输出流的缓冲
//			out.flush();
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(
//					new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("发送 POST 请求出现异常！" + e);
//			e.printStackTrace();
//			throw e;
//		}
//		// 使用finally块来关闭输出流、输入流
//		finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					in.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
	
	public static Result sendPostWithEncryption(String url, Map<String, String> headerParams, String requestBody) throws Exception {
		
		StringEntity reqEntity = new StringEntity(requestBody, CharSet.UTF8);
		
		HttpPost httpPost = new HttpPost(url);
		Result result = new Result();
		
		try {
			httpPost.setEntity(reqEntity);
			if(headerParams != null) {
				for (Entry<String, String> entry : headerParams.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			//设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_TIME_OUT).setConnectTimeout(DEFAULT_TIME_OUT).setConnectionRequestTimeout(DEFAULT_TIME_OUT).build();
			httpPost.setConfig(requestConfig);

			HttpResponse httpResp = httpclient.execute(httpPost);
			
			int statusCode = httpResp.getStatusLine().getStatusCode();
			HttpEntity rspEntity = httpResp.getEntity();
			
			InputStream in = rspEntity.getContent();
			String strResp = StreamUtils.copyToString(in, Charset.forName(CharSet.UTF8));
			result.data(DATA_RESPONSE_PACKET,strResp);
			result.data(DATA_RESPONSE_HTTP_STATUS, "" + statusCode);
			if (statusCode != HttpStatus.SC_OK
					&& statusCode != HttpStatus.SC_BAD_GATEWAY
					&& statusCode != HttpStatus.SC_GATEWAY_TIMEOUT) {
				logger.error("#executePostRequest,http响应失败！(" + statusCode
						+ ")");
				return result.message("http响应失败！(" + statusCode + ")");
			}
			return result.success();
			
			
		} catch (ConnectException e) {
			logger.error("#executePostRequest,服务器请求超时！");
			return result.message("服务器请求超时！");
		} catch (SocketTimeoutException e) {
			logger.info("#executePostRequest,服务器响应超时!");
			return result.success();
		} catch (IOException e) {
			logger.error("#executePostRequest,网络异常!");
			e.printStackTrace();
			return result.success();
		} catch (Exception e) {
			logger.error("#executePostRequest,系统错误!" + e.getMessage());
			e.printStackTrace();
			return result.success();
		} finally {
			//释放连接
			httpPost.releaseConnection();
		}
		
	}

}
