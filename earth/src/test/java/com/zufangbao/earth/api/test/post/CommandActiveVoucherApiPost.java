package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ActivePaymentVoucherDetail;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class CommandActiveVoucherApiPost extends BaseApiTestPost{
	

	@Test
	public void testCommandActivePaymentVoucher() throws IOException {
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300004");
		requestParams.put("transactionType", "0");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("voucherType", "5");//5,6
//		requestParams.put("uniqueId", "妹妹你大胆的往前走28");
//		requestParams.put("receivableAccountNo", "600000000001");
		requestParams.put("paymentBank", "中国工商银行");
		requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
		requestParams.put("voucherAmount", "10080");
		requestParams.put("financialContractNo","CS0001");

		ActivePaymentVoucherDetail detail= new ActivePaymentVoucherDetail() ;
		detail.setUniqueId("b437fe70-30c5-443e-8e1e-87a802632a25");
		detail.setRepaymentPlanNo("ZC183679518823936000");
//		detail.setRepayScheduleNo("7294da7b-42d4-41a0-ba96-0bf482808685");
		detail.setAmount(new BigDecimal("10080"));
		detail.setPrincipal(new BigDecimal("10000"));
		detail.setInterest(new BigDecimal("20"));
		detail.setServiceCharge(new BigDecimal("20"));
		detail.setMaintenanceCharge(new BigDecimal("20"));
		detail.setOtherCharge(new BigDecimal("20"));
//		requestParams.put("contractNo", "");

//		ActivePaymentVoucherDetail detail2= new ActivePaymentVoucherDetail() ;
//		detail2.setUniqueId("妹妹你大胆的往前走818");
//		detail2.setRepaymentPlanNo("ZC130812277111308288");
////		detail.setRepayScheduleNo("TESTDZZ0821_107_DZZ");
//		detail2.setAmount(new BigDecimal("980"));
//		detail2.setPrincipal(new BigDecimal("900"));
//		detail2.setInterest(new BigDecimal("20"));
//		detail2.setServiceCharge(new BigDecimal("20"));
//		detail2.setMaintenanceCharge(new BigDecimal("20"));
//		detail2.setOtherCharge(new BigDecimal("20"));
//
//		ActivePaymentVoucherDetail detail3= new ActivePaymentVoucherDetail() ;
//		detail3.setUniqueId("妹妹你大胆的往前走819");
//		detail3.setRepaymentPlanNo("ZC130812309642330112");
////		detail.setRepayScheduleNo("TESTDZZ0821_107_DZZ");
//		detail3.setAmount(new BigDecimal("980"));
//		detail3.setPrincipal(new BigDecimal("900"));
//		detail3.setInterest(new BigDecimal("20"));
//		detail3.setServiceCharge(new BigDecimal("20"));
//		detail3.setMaintenanceCharge(new BigDecimal("20"));
//		detail3.setOtherCharge(new BigDecimal("20"));

		requestParams.put("detail", JsonUtils.toJsonString(Arrays.asList(detail)));
		requestParams.put("paymentName", "秦萎超");
		requestParams.put("paymentAccountNo", "6217857600016839339");

		CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost(COMMAND_URL_TEST);

//		File file = FileUtils.getFile("/Users/apple/1.png");
//		File file2 = FileUtils.getFile("/Users/apple/2.png");
//		File file5 = FileUtils.getFile("/Users/apple/3.png");

//		CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost("http://127.0.0.1:9090/api/command");  
//		HttpPost httppost = new HttpPost("http://yunxin.zufangbao.cn/api/command"); 
//		HttpPost httppost = new HttpPost("http://yunxin.5veda.net/api/command");
		HttpPost httppost = new HttpPost(AVTIVE_VOUCHER);
//		HttpPost httppost = new HttpPost("http://192.168.0.128/api/command");
        
//		File file = FileUtils.getFile("/Users/louguanyang/Desktop/2.png");
//		File file2 = FileUtils.getFile("/Users/louguanyang/Desktop/gamersky_01origin_01_201412922358DD.jpg");
//		File file3 = FileUtils.getFile("/Users/louguanyang/Desktop/gamersky_21origin_41_201411812152E3.jpg");
//		File file4 = FileUtils.getFile("/Users/louguanyang/Desktop/gamersky_001origin_001_2015117171327B.jpg");
//		File file5 = FileUtils.getFile("/Users/louguanyang/Desktop/pdf.pdf");
		
//		File file = FileUtils.getFile("C:/Users/Public/Pictures/Sample Pictures/1.jpg");
//		File file2 = FileUtils.getFile("C:/Users/Public/Pictures/Sample Pictures/2.jpg");
//		File file5 = FileUtils.getFile("C:/Users/Public/Pictures/Sample Pictures/3.jpg");
//		
//		File file = FileUtils.getFile("D:/Users/louguanyang/Desktop/2016-10-14/1.png");
//		File file2 = FileUtils.getFile("D:/Users/louguanyang/Desktop/2016-10-14/2.png");
//		File file5 = FileUtils.getFile("D:/Users/louguanyang/Desktop/2016-10-14/3.png");
		
		
		
//		FileBody bin = new FileBody(file);
//		FileBody bin2 = new FileBody(file2);
//		FileBody bin3 = new FileBody(file3);
//		FileBody bin4 = new FileBody(file4);
//		FileBody bin5 = new FileBody(file5);

		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();  
		reqEntity.setCharset(Charset.forName(HTTP.UTF_8));
		
//        reqEntity.addPart("file", bin);
//        reqEntity.addPart("file", bin2);
//        reqEntity.addPart("file3", bin3);
//        reqEntity.addPart("file4", bin4);
//        reqEntity.addPart("file5", bin5);
        for (Entry<String, String> e : requestParams.entrySet()) {
        	StringBody stringBody =  new StringBody(e.getValue(), contentType);
			reqEntity.addPart(e.getKey(), stringBody);
		}
        httppost.setEntity(reqEntity.build());  
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        httppost.addHeader("merId", TEST_MERID);
        httppost.addHeader("secret", TEST_SECRET);
        httppost.addHeader("sign", sign);
        System.out.println(sign);
        HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
	}
}
