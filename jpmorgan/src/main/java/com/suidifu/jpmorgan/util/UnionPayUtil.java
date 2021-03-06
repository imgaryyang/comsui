package com.suidifu.jpmorgan.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.gnete.security.crypt.Crypt;
import com.gnete.security.crypt.CryptException;
import com.suidifu.jpmorgan.entity.unionpay.IUnionPayApiParams;
import com.suidifu.jpmorgan.entity.unionpay.UnionPayResult;


public class UnionPayUtil {
	
private static final int DEFAULT_TIME_OUT = 30000;
	
	private static Log log = LogFactory.getLog(UnionPayUtil.class);
	
	/**
	 * 验证签名信息
	 * @param strXML
	 * @return
	 * @throws CryptException
	 */
	public static boolean verifySign(String strXML, String cerFilePath) throws CryptException {
		//签名(代收付系统JDK环境使用GBK编码，商户使用签名包时需传送GBK编码集)
		Crypt crypt = new Crypt("gbk");
		int iStart = strXML.indexOf("<SIGNED_MSG>");
		if (iStart != -1) {
			int end = strXML.indexOf("</SIGNED_MSG>");
			String signedMsg = strXML.substring(iStart+12, end);
			String strMsg = strXML.substring(0, iStart) + strXML.substring(end+13);
			//调用签名包验签接口(原文,签名,公钥路径)
			if (crypt.verify(strMsg,signedMsg, cerFilePath)){
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 对数据进行签名
	 * @param strData 签名前XML数据
	 * @return 签名后XML数据
	 * @throws CryptException
	 */
	public static String signMsg(String strData, String pfxFilePath, String pfxFileKey) throws CryptException {
		//签名(代收付系统JDK环境使用GBK编码，商户使用签名包时需传送GBK编码集)
		Crypt  crypt = new Crypt("gbk");
		String strMsg = strData.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
		//调用签名包签名接口(原文,私钥路径,密码)
		String signedMsg = crypt.sign(strMsg, pfxFilePath, pfxFileKey);
		return  strData.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "<SIGNED_MSG>"+signedMsg+"</SIGNED_MSG>");
	}
	
	/**
	 * 执行HttpPost请求
	 * @param xmlPacket XML数据包
	 * @param params 参数包
	 * @return 结果
	 * @throws CryptException
	 */
	public static UnionPayResult executePostRequest(String xmlPacket, IUnionPayApiParams params){
		String apiUrl = params.getApiUrl();
		String pfxFilePath = params.getPfxFilePath();
		String cerFilePath = params.getCerFilePath();
		String pfxFileKey = params.getPfxFileKey();
		UnionPayResult result = new UnionPayResult();
		HttpPost httpPost = new HttpPost(apiUrl);

		try {
			xmlPacket = UnionPayUtil.signMsg(xmlPacket, pfxFilePath, pfxFileKey);
			log.info(xmlPacket);
			result.setRequestPacket(xmlPacket);
	
			StringEntity reqEntity = new StringEntity(xmlPacket, "GBK");
			httpPost.setEntity(reqEntity);
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			result.setSendTime(DateUtils.getNowFullDateTime());
			//设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_TIME_OUT).setConnectTimeout(DEFAULT_TIME_OUT).setConnectionRequestTimeout(DEFAULT_TIME_OUT).build();
			httpPost.setConfig(requestConfig);

			HttpResponse httpResp = httpclient.execute(httpPost);
			result.setReceiveTime(DateUtils.getNowFullDateTime());
			
			int statusCode = httpResp.getStatusLine().getStatusCode();
			HttpEntity rspEntity = httpResp.getEntity();
			
			InputStream in = rspEntity.getContent();
			String strResp = StreamUtils.copyToString(in, Charset.forName("GBK"));
			result.setResponsePacket(strResp);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + httpResp.getStatusLine());
				log.error(strResp);
				return result.message("http响应失败！(" + statusCode + ")");
			}else {
				log.info("服务器返回:" + strResp);
				//验证签名
				if (UnionPayUtil.verifySign(strResp, cerFilePath)) {
					log.info("验签正确，处理服务器返回的报文!");
					return result.success();
				}else {
					return result.message("验签失败");
				}
			}
		} catch (IOException e) {
			log.error("网络异常",e);
			return result.message("网络异常!");
		} catch (CryptException e) {
			log.error("签名工具类异常！(" + e.getMessage() + ")",e);
			return result.message("签名工具类异常");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return result.message("其他错误");
		} finally {
			//释放连接
			httpPost.releaseConnection();
			log.info(JSON.toJSONString(result));
		}
	}
	
	
	
	public static UnionPayResult executePostRequestByJsoup(String xmlPacket, IUnionPayApiParams params){
		String apiUrl = params.getApiUrl();
		String pfxFilePath = params.getPfxFilePath();
		String cerFilePath = params.getCerFilePath();
		String pfxFileKey = params.getPfxFileKey();
		UnionPayResult result = new UnionPayResult();
		try {
			xmlPacket = UnionPayUtil.signMsg(xmlPacket, pfxFilePath, pfxFileKey);
			log.info(xmlPacket);
			result.setRequestPacket(xmlPacket);
			result.setSendTime(DateUtils.getNowFullDateTime());
			Response response = Jsoup.connect(apiUrl).requestBody(xmlPacket)
					.header("Content-Type", "text/xml;charset=UTF-8")
					.timeout(DEFAULT_TIME_OUT).method(Method.POST).execute();
			String strResp = new String(response.bodyAsBytes(),"UTF-8");
			int statusCode = response.statusCode();
			result.setReceiveTime(DateUtils.getNowFullDateTime());
			result.setResponsePacket(strResp);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + response.statusMessage());
				log.error(strResp);
				return result.message("http响应失败！(" + statusCode + ")");
			}else {
				log.info("服务器返回:" + strResp);
				//验证签名
				if (UnionPayUtil.verifySign(strResp, cerFilePath)) {
					log.info("验签正确，处理服务器返回的报文!");
					return result.success();
				}else {
					return result.message("验签失败");
				}
			}
		} catch (IOException e) {
			log.error("网络异常",e);
			return result.message("网络异常!");
		} catch (CryptException e) {
			log.error("签名工具类异常！(" + e.getMessage() + ")",e);
			return result.message("签名工具类异常");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return result.message("其他错误");
		} finally {
			log.info(JSON.toJSONString(result));
		}
	}
}
