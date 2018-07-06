package com.suidifu.bridgewater.api.test.post;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.fail;

public class JmTest {
	public String urlPath = "http://www.mocky.io/v2/572ee171120000332519491b";
	public HttpURLConnection connection = null;
	public OutputStream output = null;
	public BufferedReader reader = null;
	public static final String TEST_MERID = "t_test_zfb";
	public static final String TEST_SECRET = "123456";
	public static final String privateKey =
			"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG" +
					"/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK" +
					"+Le7CWKtv8MQL" +
					"+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD" +
					"/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU" +
					"+8KIAQpVflu" +
					"/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV" +
					"+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx" +
					"/eMcITaLq8l1qzZ907UXY+Mfs=";
	/**
	 * 空字符串 “”
	 */
	private static final String BLANK = "";
	/**
	 * 字符 “&”
	 */
	private static final String AND = "&";
	/**
	 * 字符 “=”
	 */
	private static final String EQUAL = "=";

	public static final LongAdder longAdder = new LongAdder();

	@Test
	public void testZhongHang() {

		String url = "http://192.168.0.158:9092/api/command";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", "G31700");
		requestParams.put("uniqueId", UUID.randomUUID().toString());
		requestParams.put("contractNo", "妹妹你大胆的往前走150");
		String amount = "1500";
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2017-10-15 00:00:00");
		requestParams.put("remittanceId", UUID.randomUUID().toString());
		requestParams.put("remark", "交易备注");
		requestParams.put("notifyUrl", "http://www.mocky.io/v2/5185415ba171ea3a00704eed");
		System.out.print(requestParams.get("remittanceId") + "     ");
		String bankCardNo = "6214855712106520";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','bankAliasName':'ICBC','amount':'" + amount + "'," +
				"'bankCode':'102100026864'," +
				"'bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1'," +
				"'bankName':'中国工商银行股份有限公司北京通州支行玉桥西里分理处','idNumber':'idNumber1'}]");
		longAdder.add(1);
		try {
			String sr = sendPost(url, requestParams, getIdentityInfoMap(requestParams));
			Map<String, Object> result = new HashMap<>();
			result = parse(sr);

			Assert.assertEquals(0, result.get("code"));
			Assert.assertEquals("成功!", result.get("message"));
			System.out.println(sr + "   num:[" + longAdder + "].");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parse(String jsonText) {
		try {
			Map<String, Object> object = (Map<String, Object>) JSON.parse(jsonText);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, String> getIdentityInfoMap(Map<String, String> requestParams) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		String signContent = getSignCheckContent(requestParams);
		String sign = rsaSign(signContent, privateKey);
		headers.put("sign", sign);
		return headers;
	}

	public static String getSignCheckContent(Map<String, String> params) {
		if (params == null || params.isEmpty()) {
			return null;
		}
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			value = (value == null || value.equals("")) ? "" : value;
			content.append((i == 0 ? BLANK : AND) + key + EQUAL + value);
		}
		return content.toString();
	}

	public static String rsaSign(String content, String privateKey) {
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", privateKey);

			Signature signature = Signature.getInstance("MD5withRSA");

			signature.initSign(priKey);

			signature.update(content.getBytes());

			byte[] signed = signature.sign();

			return new String(Base64.getEncoder().encode(signed));
		} catch (InvalidKeySpecException e) {
		} catch (Exception e) {
		}
		return null;
	}

	public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, String privateKey) throws Exception {
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance(algorithm);
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);
		return priKey;
	}

	public static String sendPost(String url, Map<String, String> params, Map<String, String> headerMap)
			throws Exception {
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
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (headerMap != null) {
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
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
			value = (value == null || value.equals("")) ? "" : value;
			buffer.append(entry.getKey() + "=" + value + "&");
		}
		return buffer.toString();
	}

}
