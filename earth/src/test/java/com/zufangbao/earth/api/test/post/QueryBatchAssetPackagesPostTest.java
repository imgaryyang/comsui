package com.zufangbao.earth.api.test.post;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.yunxin.service.impl.remittance.RemittanceApplicationServiceImpl;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//		"classpath:/local/applicationContext-*.xml" })
public class QueryBatchAssetPackagesPostTest extends BaseApiTestPost{
//	@Autowired
//	private IRemittanceApplicationService remittanceApplicationService;
	
	private String productCode = "G31700";
	
    private String uniqueId = UUID.randomUUID().toString();
	
	private String totalAmount = "1800";
	
	private String amount = "600";
	
	private String startTime;
	
	private String endTime;
	
	String result = "";
	String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
	String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
	String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
	PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
	@Test
	public void testQueryBatchAssetPackagesByContractNos() throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
		
		/*List<String> arr = new ArrayList<String>();
		for(int i = 0;i<1000;i++){
			arr.add(createRandom(100000,1400000)+"");
		}
		String jsonString = JsonUtils.toJsonString(arr);*/

		requestParams.put("fn", "100007");
		requestParams.put("contractNos","['FANT014444']");
		requestParams.put("productCode", "G31700");
		//requestParams.put("startTime","2017-03-13");
		//requestParams.put("endTime","2017-03-13");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
			/*File file = new File("result.text");
			FileUtils.write(file, sr);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryBatchAssetPackagesByUniqueIds() throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
		
		/*List<String> arr = new ArrayList<String>();
		for(int i = 0;i<1000;i++){
			arr.add(createRandom(100000,1400000)+"");
		}
		String jsonString = JsonUtils.toJsonString(arr);*/

		requestParams.put("fn", "100007");
		requestParams.put("productCode", "G31700");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueIds","['fdea147e-be83-484b-b40d-7e9424d2c7f6','unique_id300000','FANT014']");
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
			/*File file = new File("result.text");
			FileUtils.write(file, sr);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryBatchAssetPackagesByProductCode() throws IOException {
		//放款
//		prepaymentCucumberMethod.makeLoan(productCode,uniqueId,totalAmount);
//		while(true){
//			   //int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
//			RemittanceApplicationServiceImpl re=new RemittanceApplicationServiceImpl(); 
//			   int executionStatus=re.queryExecutionStatus(uniqueId);
//			   if(executionStatus == 2){
//				   break;
//			   }
//			   try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		   }
//		
//		      System.out.println("导入资产包");
//		//导入资产包
//		prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate,  secondPlanDate,  thirdPlanDate);
	 //资产包批量查询
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100007");
		requestParams.put("productCode", productCode);
		requestParams.put("contractNos", "7fb05781-94c5-4664-bc58-e0f5cd397c11");
		requestParams.put("startTime","2017-3-16");
		requestParams.put("endTime","2017-3-16");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int createRandom(int max,int min){
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
	}

}