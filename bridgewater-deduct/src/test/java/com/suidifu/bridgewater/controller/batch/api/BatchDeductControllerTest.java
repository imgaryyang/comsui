/**
 * 
 */
package com.suidifu.bridgewater.controller.batch.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.zufangbao.gluon.opensdk.HttpClientUtils;

/**
 * @author wukai
 *
 */
public class BatchDeductControllerTest {
	
	public static final String TEST_MERID = "t_test_zfb";
	public static final String TEST_SECRET = "123456";
	
	/** t_merchant的私钥 **/
	public  String privateKey =     "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJhkaTjdalOUBGXtx8MRvgWr7xbo1+94qOd4qEqh7gdpcy/QwByXw7yvAxAS+vrXnjn7+Ho9yMcCri9R9FdK6Z5eyfyPWqu0MNfTNhw2GIu5EA/QQmomuOqfxA1R9vrThqfcM/nBR/W08QlZ+W/qbGI7QE2iJ22JlAN9Qwe6rGoJAgMBAAECgYEAhsTWIUn+zCcstI/Sh/ra190ztkj5HnjQttQH0OjfvoJNMc4k96RnoCgOR1rhxpCFB7aECmc1dnoyXPrXYZOIgf7rohBlwHz6o4LBsyb0QyVJ4a50DOPCRNja2L1witUpP3i3H9qIEU9BXKVDhzwgTyI02VijuPfu+AR9QNheVvkCQQDFm7/tnQwa8e2iuXZ/X7CdeP+A/fClsbxDLIefVOC3j/QimU57WMnZSghLvzNMu6tS3mcQuRj5LU9bXCAUbmJHAkEAxWw/Pxwv85X/f+hsHQEpXrlMyR2ksbdB4387CB0vRtoxJmsmBdyaXYSNUZzCjvCGjpyPbpOBZeazDpydMH0pLwJBAKC7dhrVQjJslGlmx58Fe4grElCevW5ZKpPNFaehB4PnwKGf53lnGA/5KtRJ+nUwtUMbiePNWyXbGBBs198mhiMCQQCNMlYptOc2t9j8iiaIuAP2k2CvllvsHr0pEB4QN49QhU0RxSB0oMpmiB7qd8tOoAgchyyuUSEC2HTHxotF2r5RAkBrpDxk4yErWYNJu8DPBvkbyZP12OfjiIeZ7UeNA6Ri4UsAqLS0d9ieoAwIzS1VrV1p8sd2gwHPpdkZHgmqtfua";

	public String zhongPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

	@Test  
	public void testbatchDeduct() throws Exception {
		
		Map<String, String> requestParams = new HashMap<String, String>();
		
		requestParams.put("fn", "500002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("batchDeductId",  UUID.randomUUID().toString());
		requestParams.put("notifyUrl", "http://127.0.0.1:");
		requestParams.put("md5", "");
		
	    String url = "http://127.0.0.1:8080/test/upload.do";  
	    String filePath = "C:\\Users\\MikanMu\\Deskto@Test\n" +
				"\tpublic void teststandardBatchDeduct() throws Exception{\n" +
				"\t\t\n" +
				"\t\tHttpHeaders requestHeaders = new HttpHeaders();\n" +
				"\t\trequestHeaders.set(\"merId\", TEST_MERID);\n" +
				"\t\trequestHeaders.set(\"secret\",TEST_SECRET);\n" +
				"\t\t\n" +
				"\t\t\n" +
				"\t\tString requestNo =  UUID.randomUUID().toString();\n" +
				"\t\tString batchDeductId = UUID.randomUUID().toString();\n" +
				"\t\tString notifyUrl = \"http://219.143.184.27:10000/ecp/wwjrPayNotify\";\n" +
				"\t\t\n" +
				"\t\tString originName = \"batchDeduct-8855258000001-0000000001-20150415-01.csv\";\n" +
				"\t\t\n" +
				"\t\tResource resource =  new ClassPathResource(\"test/yunxin/standardBatchDeduct/\"+originName);\n" +
				"\t\t\n" +
				"\t\tFile file = resource.getFile();\n" +
				"\t\t\n" +
				"\t\tString md5 = DigestUtils.md5Hex(new FileInputStream(file));;\n" +
				"\t\t\n" +
				"\t\tMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<String, String>();\n" +
				"\t\t\n" +
				"\t\trequestParams.add(\"requestNo\",requestNo);\n" +
				"\t\trequestParams.add(\"batchDeductId\", batchDeductId);\n" +
				"\t\trequestParams.add(\"notifyUrl\", notifyUrl);\n" +
				"\t\trequestParams.add(\"md5\", md5);\n" +
				"\t\trequestParams.add(\"fn\", \"500002\");\n" +
				"\t\t\n" +
				"\t\tMockMultipartFile filePart = new MockMultipartFile(\"file\",originName,\"csv\", resource.getInputStream());\n" +
				"\t\t\n" +
				"\t\tResultActions perform = mockMvc.\n" +
				"\t\t\n" +
				"\t\tperform(MockMvcRequestBuilders.fileUpload(\"/api/command\").file(filePart).headers(requestHeaders).params(requestParams));\n" +
				"\t\tSystem.out.println(perform);\n" +
				"\t}\n" +
				"\n" +
				"\n" +
				"\tp\\test.txt";
	  
	    RestTemplate rest = new RestTemplate();  
	    FileSystemResource resource = new FileSystemResource(new File(filePath));  
	    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();  
	    param.add("jarFile", resource);  
	    param.add("fileName", "test.txt");  
	  
	    String string = rest.postForObject(url, param, String.class);  
	    System.out.println(string);  
	}
	
	private Map<String, String> getIdentityInfoMap(Map<String, String> requestParams) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
		headers.put("sign", sign);
		return headers;
	}
	/**
	 * 使用中航的工具测试外网
	 * @throws Exception
	 */
	@Test
	public void testHttpUtilsForZhonghangWaiWang() throws Exception{
		
		String requestNo =  UUID.randomUUID().toString();
		String batchDeductId = UUID.randomUUID().toString();
		
//		String requestNo = "P2017080500000010";
//		String batchDeductId ="TESTBACTHDEDUCT000001";
		
		
		System.out.println("requestNo="+requestNo+"batchDeductId["+batchDeductId+"]");
		
//		String notifyUrl = "http://219.143.184.27:10000/ecp/wwjrBatchDeductNotify";
		
//		String notifyUrl = "http://xxx:10000/ecp/wwjrBatchDeductNotify";
		
		String notifyUrl = "http://yx_earth.ngrok.cc/mock/batch/deduct/success";
		
		String originName = "batchDeduct-systemdeduct-000000001-2017-08-05-08-bak-bak-bak2.txt";
		
//		String originName = "batchDeduct-systemdeduct-000000001-2017-08-01.csv";
		
		Resource resource =  new ClassPathResource("test/yunxin/standardBatchDeduct/"+originName);
		
		File file = resource.getFile();
		
		Map<String,String> requestParams  = new HashMap<String,String>();
		
		String md5 = DigestUtils.md5Hex(new FileInputStream(file));
		
		requestParams.put("requestNo",requestNo);
		requestParams.put("batchDeductId", batchDeductId);
		requestParams.put("notifyUrl", notifyUrl);
		requestParams.put("md5", md5);
		requestParams.put("fn", "500002");
		
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		
		System.out.println("signContent["+signContent+"]");
		
		String sign = ApiSignUtils.rsaSign(signContent,zhongPrivateKey );
		
		
		String requsturl = "?";
		
		for (String key : requestParams.keySet()) {
			requsturl+=key+"="+requestParams.get(key)+"&";
		}
		
//		String requestURL = "http://60.190.243.66:8887/api/command"+requsturl;
		
		String requestURL = "http://avictctest.5veda.net:8887/api/command"+requsturl;
	
		Map<String,String> headers = new HashMap<String,String>();
		
//		headers.put("merId", "systemdeduct");
//		headers.put("secret", "628c8b28bb6fdf5c5add6f18da47f1a6");
		
		headers.put("merId",TEST_MERID);
		headers.put("secret", TEST_SECRET);
		
		headers.put("sign", sign);;
		
		
		System.out.println("requestURL:["+requestURL+"],headers["+JsonUtils.toJsonString(headers)+"]");
		
		MultipartUtility utility = new MultipartUtility(requestURL, "utf-8",headers);
		
		utility.addFilePart("file",file);
		
		List<String> response = utility.finish();
		
		for (String r : response) {
			
			System.out.println(r);
			
		}
	}
	/**
	 * 使用中航的工具测试内网
	 * 3条数据
	 * 1、单条数据少于12字段
	 * 2、数据类型转换出错
	 * 3、正确数据
	 * 4、空行
	 * 
	 * 测试结果
	 * 1、3条t_deduct_check_log 1条是http请求jpmorgan失败 2条校验失败
	 * 2、t_batch_deduct_application中fail_count=2,total_count=3,
	 */
	@Test
	@Sql("classpath:test/yunxin/standardBatchDeduct/test_prepare_batch_deduct.sql")
	public void testHttpUtilsForZhonghanglocalCase1() throws Exception{
		
		String requestNo =  UUID.randomUUID().toString();
		
		String batchDeductId =  UUID.randomUUID().toString();
		//模拟中航回调地址
		String notifyUrl = "http://127.0.0.1:9091/mock/batch/deduct/success";
		
		String fileForClassPath = "test/yunxin/standardBatchDeduct/batchDeduct-systemdeduct-000000001-2017-08-02.csv";
		
		String response = httpUtilsForZhonghang(requestNo, batchDeductId, notifyUrl, fileForClassPath);
		
		System.out.println(response);
		
//		List<DeductPlanApplicationCheckLog> DeductPlanApplicationCheckLogList =  deductPlanApplicationCheckLogService.loadAll(DeductPlanApplicationCheckLog.class);
//		
//		assertEquals(2,DeductPlanApplicationCheckLogList.size())
	}
	private String httpUtilsForZhonghang(String requestNo,String batchDeductId ,String notifyUrl,String fileForClassPath) throws IOException{
		
		Resource resource =  new ClassPathResource(fileForClassPath);
		
		File file = resource.getFile();
		
		Map<String,String> requestParams  = new HashMap<String,String>();
		
		String md5 = DigestUtils.md5Hex(new FileInputStream(file));
		
		System.out.println("md5:"+md5);
		
		requestParams.put("requestNo",requestNo);
		requestParams.put("batchDeductId", batchDeductId);
		requestParams.put("notifyUrl", notifyUrl);
		requestParams.put("md5", md5);
		requestParams.put("fn", "500002");
		
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = ApiSignUtils.rsaSign(signContent,zhongPrivateKey );
		
		String requsturl = "?";
		
		for (String key : requestParams.keySet()) {
			requsturl+=key+"="+requestParams.get(key)+"&";
		}
		
		String requestURL = "http://127.0.0.1:9091/api/command"+requsturl;
	
		Map<String,String> headers = new HashMap<String,String>();
		
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		headers.put("sign", sign);;
		
		MultipartUtility utility = new MultipartUtility(requestURL, "utf-8",headers);
		
		utility.addFilePart("file",file);
		
		List<String> response = utility.finish();
		
		return CollectionUtils.isEmpty(response) ? null : response.get(0);
		
	}
	
	/**
	 * 使用中航的工具测试内网
	 * @throws Exception
	 */
	@Test
	public void testHttpUtilsForZhonghanglocal() throws Exception{
		
		String requestNo =  UUID.randomUUID().toString();
		String batchDeductId = "ef1cdf95-88aa-492d-8472-b1eb9b6a525c";
		//模拟中航回调地址
//		String notifyUrl = "http://219.143.184.27:10000/ecp/wwjrPayNotify";
		
		String notifyUrl = "http://127.0.0.1:9091/mock/batch/deduct/success";
		
		String originName = "batchDeduct-systemdeduct-000000001-2017-07-17-023.csv";
		
		Resource resource =  new ClassPathResource("test/yunxin/standardBatchDeduct/"+originName);
		
		File file = resource.getFile();
		
		Map<String,String> requestParams  = new HashMap<String,String>();
		
		String md5 = DigestUtils.md5Hex(new FileInputStream(file));
		
		System.out.println("md5:"+md5);
		
		requestParams.put("requestNo",requestNo);
		requestParams.put("batchDeductId", batchDeductId);
		requestParams.put("notifyUrl", notifyUrl);
		requestParams.put("md5", md5);
		requestParams.put("fn", "500002");
		
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = ApiSignUtils.rsaSign(signContent,privateKey );
		
		
		String requsturl = "?";
		
		for (String key : requestParams.keySet()) {
			requsturl+=key+"="+requestParams.get(key)+"&";
		}
		
		String requestURL = "http://192.168.0.116:9091/api/command"+requsturl;
	
		Map<String,String> headers = new HashMap<String,String>();
		
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		headers.put("sign", sign);;
		
		MultipartUtility utility = new MultipartUtility(requestURL, "utf-8",headers);
		
		utility.addFilePart("file",file);
		
		List<String> response = utility.finish();
		
		StringBuffer sb = new StringBuffer();
		
		for (String r : response) {
			
			sb.append(r);
			
		}
		
		System.out.println("result:["+sb+"]");
	}
	/**
	 * 使用中航的工具测试内网
	 * @throws Exception
	 */
	@Test
	public void testHttpUtilsForZhonghanglocal2() throws Exception{
		
		String requestNo =  UUID.randomUUID().toString();
		String batchDeductId = UUID.randomUUID().toString();
		//模拟中航回调地址
//		String notifyUrl = "http://219.143.184.27:10000/ecp/wwjrPayNotify";
		
		String notifyUrl = "http://127.0.0.1:9091/mock/batch/deduct/success";
		
//		String originName = "batchDeduct-8855258000001-0000000001-20150415-01-case1.csv";
		
		String originName = "batchDeduct-t_test_zfb-000000001-2017-08-04-12.txt";
		
		Resource resource =  new ClassPathResource("test/yunxin/standardBatchDeduct/"+originName);
		
		File file = resource.getFile();
		
		Map<String,String> requestParams  = new HashMap<String,String>();
		
		String md5 = DigestUtils.md5Hex(new FileInputStream(file));
		
		System.out.println("md5:"+md5);
		
		requestParams.put("requestNo",requestNo);
		requestParams.put("batchDeductId", batchDeductId);
		requestParams.put("notifyUrl", notifyUrl);
		requestParams.put("md5", md5);
		requestParams.put("fn", "500002");
		
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		String sign = ApiSignUtils.rsaSign(signContent,privateKey );
		
		
		String requsturl = "?";
		
		for (String key : requestParams.keySet()) {
			requsturl+=key+"="+requestParams.get(key)+"&";
		}
		
		String requestURL = "http://192.168.0.172:9091/api/command"+requsturl;
		
//		String requestURL = "http://avictctest.5veda.net:8887/api/command"+requsturl;
	
		Map<String,String> headers = new HashMap<String,String>();
		
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		headers.put("sign", sign);;
		
		MultipartUtility utility = new MultipartUtility(requestURL, "utf-8",headers);
		
		utility.addFilePart("file",file);
		
		List<String> response = utility.finish();
		
		for (String r : response) {
			
			System.out.println(r);
		}
	}
	@Test
	public void testParseJson() throws Exception {
		
		Map<String,Object> map = new HashMap<>();;
		
		map.put("deductId","33175be0-8a7d-4aef-89e0-3c32834d5812");
		
		map.put("financialProductCode", "11111111");
		map.put("uniqueId", "702f0dd5-ebca-4073-a201-03be75775e27");
		map.put("contractNo", "TEST-CONTRACT-001");
		map.put("transType", 1);
		map.put("amount", new BigDecimal("200"));
		map.put("repaymentType", 1);
		map.put("mobile", "13732255443");
		map.put("accountName", "xx111111");
		map.put("accountNo", "6214855712109920");
		map.put("gateway", "1");
		
		
		Map<String,Object> repaymentDetailsMap = new HashMap<String,Object>();
		
		repaymentDetailsMap.put("repaymentAmount", new BigDecimal(200));
		repaymentDetailsMap.put("repaymentPrincipal", new BigDecimal(100));
		repaymentDetailsMap.put("repaymentInterest", new BigDecimal(200));
		repaymentDetailsMap.put("loanFee", new BigDecimal(0));
		repaymentDetailsMap.put("techFee", new BigDecimal(0));
		repaymentDetailsMap.put("otherFee", new BigDecimal(0));
		repaymentDetailsMap.put("repaymentInterest", new BigDecimal(0));
		repaymentDetailsMap.put("currentPeroid", 1);
		
		List<Map<String,Object>> repaymentDetailsList  = new ArrayList<>();
		
		repaymentDetailsList.add(repaymentDetailsMap);
		
		map.put("repaymentDetails",repaymentDetailsList);
		
		List<Map<String,Object>> overDueFeeDetailList = new ArrayList<>();
		
		Map<String,Object> overDueFeeDetail = new HashMap<String,Object>();
		
		overDueFeeDetail.put("totalOverdueFee", new BigDecimal(100));
		
		overDueFeeDetailList.add(overDueFeeDetail);
		
		map.put("overDueFeeDetails",overDueFeeDetailList);
		
		System.out.println(JsonUtils.toJsonString(map));
	}
	@Test
	public void testName() throws Exception {
		
		String jsonText = "{\"accountName\":\"代永奎\",\"accountNo\":\"620056321478965235\",\"amount\":22222.16,\"deductId\":\"PK201708040000004375\",\"financialProductCode\":\"1111111\",\"repaymentDetails\":[{\"currentPeriod\":1,\"otherFee\":0,\"repaymentAmount\":22222.16,\"repaymentInterest\":0.00,\"repaymentPrincipal\":22222.16}],\"transType\":0,\"uniqueId\":\"PK201708040000004375\"}";
		
		BatchDeductItem batchDeductItem  = JsonUtils.parse(jsonText, BatchDeductItem.class);
		
		System.out.println(JsonUtils.toJsonString(batchDeductItem));
		
	}


	

}
