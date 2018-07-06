package com.zufangbao.earth.api.test.post;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={
//		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml"
//})
public class CommandVoucerApiPost extends BaseApiTestPost {

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	
	@Test
	public void testCommandBusinessPaymentVoucher() throws IOException {
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
//		List<Contract> contractList = genericDaoSupport.searchForList("From Contract where contractNo like :contractNo","contractNo","test-2016-11-09-06-%");
//		assertEquals(10,contractList.size());
//		for (int i = 0; i < contractList.size(); i++) {
//			Contract contract = contractList.get(i);
//			List<AssetSet> assets = repaymentPlanService.getRepaymentPlansByContractAndActiveStatus(contract, AssetSetActiveStatus.OPEN);
			
			BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();

			detail.setAmount(new BigDecimal("100080"));
			detail.setUniqueId("overFD112");
			
//			detail.setRepaymentPlanNo("ZC181782517919236096");
//			detail.setUniqueId("cfe9fb12-b089-4c3b-8a41-f6662a44bd92");
//			detail.setRepaymentPlanNo("ZC131420176718045184");
			detail.setRepayScheduleNo("3f002de8-1e67-4349-809f-e76b1a4639df");
//			detail.setCurrentPeriod(2);
			detail.setPrincipal(new BigDecimal("100000"));
			detail.setInterest(new BigDecimal("20"));
			detail.setServiceCharge(new BigDecimal("20"));
			detail.setMaintenanceCharge(new BigDecimal("20"));
			detail.setOtherCharge(new BigDecimal("20"));
//			detail.setLateFee(new BigDecimal("10"));
//			detail.setLateOtherCost(new BigDecimal("10"));
//			detail.setPenaltyFee(new BigDecimal("120"));
//			detail.setLatePenalty(new BigDecimal("10"));
			detail.setTransactionTime("");
			detail.setPayer(1);
			details.add(detail);

//		BusinessPaymentVoucherDetail detail2 = new BusinessPaymentVoucherDetail();
//
//		detail2.setAmount(new BigDecimal("100080"));
//		detail2.setUniqueId("overFD63");
//
//		detail2.setRepaymentPlanNo("ZC173891054641221632");
////			detail.setUniqueId("cfe9fb12-b089-4c3b-8a41-f6662a44bd92");
////			detail.setRepaymentPlanNo("ZC621932498483068928");
//		detail2.setPrincipal(new BigDecimal("100000"));
//		detail2.setInterest(new BigDecimal("20"));
//		detail2.setServiceCharge(new BigDecimal("20"));
//		detail2.setMaintenanceCharge(new BigDecimal("20"));
//		detail2.setOtherCharge(new BigDecimal("20"));
//		detail2.setTransactionTime("");
//		detail2.setPayer(1);
//		details.add(detail2);


//
//			BusinessPaymentVoucherDetail detail2 = new BusinessPaymentVoucherDetail();
//			detail2.setAmount(new BigDecimal("2000"));
//			detail2.setUniqueId("9b2402f4-f999-48ee-a4c2-8d1dd204dbe9");
//			detail2.setRepaymentPlanNo("060eedd0-1332-49ca-8a2c-352766aed5db");
////			detail.setUniqueId("cfe9fb12-b089-4c3b-8a41-f6662a44bd92");
////			detail.setRepaymentPlanNo("ZC2743ECD5DAEB2ABA");
//			detail2.setPayer(0);
//			details.add(detail2);
//		}
		
		String amount = "100080";

//		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
//		File file = new File("/Users/louguanyang/Desktop/test-voucher/test_"+ amount + "_1013.txt");//测试用json文件地址
//		List<CommandVoucerParam> list = JsonUtils.parseArray(file, CommandVoucerParam.class); 
//		
//		for (CommandVoucerParam param : list) {
//			BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
//			detail.setAmount(new BigDecimal(600));
//			detail.setUniqueId("a435467e-c9b4-45a0-b2ab-c7eae02e9b3a");
//			detail.setRepaymentPlanNo("[\"ZC27502A7CC6F08690\"]");
//			detail.setPayer(0);
//			details.add(detail);
//		}
		String jsonString = JsonUtils.toJsonString(details);
		System.out.println(jsonString);
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300003");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("transactionType", "0");
		requestParams.put("voucherType", "0");
		requestParams.put("voucherAmount", amount);
		requestParams.put("financialContractNo", "CS0001");
		requestParams.put("receivableAccountNo", "1220127571120");
		requestParams.put("paymentAccountNo", "6217857600016839339");
		//requestParams.put("paymentAccountNo", "10003");
		requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
		requestParams.put("paymentName", "秦萎超");
		//requestParams.put("paymentName", "ppd_user");
		requestParams.put("paymentBank", "bank");
		
		requestParams.put("detail", jsonString);
		
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost("http://192.168.1.33:8080/earth-yunxin-0.1.0/api/command");  
//		HttpPost httppost = new HttpPost("http://192.168.1.145:9090/api/command");
//		HttpPost httppost = new HttpPost("http://yunxin.5veda.net/api/command");
//		HttpPost httppost = new HttpPost("http://192.168.0.128/api/command");
		HttpPost httppost = new HttpPost(VOUCHER);
		
        
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();  
        reqEntity.setCharset(Charset.forName("UTF-8"));
		
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
        HttpResponse response = client.execute(httppost); 
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
	}

}
