package com.zufangbao.cucumber.method.method;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

@Component("prepaymentCucumberMethod")
public class PrepaymentCucumberMethod extends BaseApiTestPost{
	
//	private  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
//	/**
//	 * 本地测试
//	 */
//	String queryUrl= "http://127.0.0.1:9090/api/query";
//	String modifyUrl = "http://127.0.0.1:9090/api/modify";
//	String deductUrl = "http://127.0.0.1:9090/api/command";
//	String remittanceUrl = "http://127.0.0.1:9092/api/command";
	
	/**
	 * 内网测试
	 */
//  String queryUrl= "http://192.168.0.204/api/query";
//  String modifyUrl = "http://192.168.0.204:80/api/modify";
//	String deductUrl = "http://192.168.0.204:83/api/command";
//	String remittanceUrl = "http://192.168.0.204:27082/api/command";

	/**
	 * 外网测试
 	 */
//	String queryUrl = "http://yunxin.5veda.net/api/query";
//  String modifyUrl = "http://yunxin.5veda.net/api/modify";
//	String deductUrl = "http://121.40.230.133:29084/api/command";
//	public static final String DEDUCT_QUERY_TEST = "http://121.40.230.133:29084/api/query";
//	String remittanceUrl = "http://120.55.85.54:27082/api/command";


	 /**
	 * 放款
	 * @param productCode
	 * @param uniqueId
	 * @param amount
	 */
	public void makeLoan(String productCode,String uniqueId,String amount){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", uniqueId);
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2017-08-20 00:00:00");
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6214855712106520";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'2017-08-20 00:00:00','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(REMITTANCE_URL, requestParams,getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除已插入的数据
	 */
//	public void connectJDBC(){
//        PoolConfiguration poolProperties = new PoolProperties();
//		
//		String url = "jdbc:mysql://192.168.0.201:3306/yunxin?useUnicode=true&autoReconnect=true&characterEncoding=UTF-8";
//		
//		poolProperties.setUrl(url);
//		poolProperties.setUsername("yunxin");
//		poolProperties.setPassword("yunxin69fc");
//		poolProperties.setDriverClassName("com.mysql.jdbc.Driver");
//		
//		DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
//		AutoTestUtils.dbExecute("cucumberSql/deleteAboutPrepayment.sql", dataSource);
//	}
	
	/**
	 * 导入资产包
	 * @param totalAmount
	 * @param productCode
	 * @param uniqueId
	 * @param repaymentAccountNo
	 * @param amount
	 * @param firstRepaymentDate
	 * @param secondRepaymentDate
	 * @param thirdRepaymentDate
	 */
	public void importAssetPackage(String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
		for (int index = 0; index < 1; index++) {
			ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
			importAssetPackageContent.setThisBatchContractsTotalNumber(1);
			importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
			importAssetPackageContent.setFinancialProductCode(productCode);

			List<ContractDetail> contracts = new ArrayList<ContractDetail>();
			ContractDetail contractDetail = new ContractDetail();
			contractDetail.setUniqueId(uniqueId);
			contractDetail.setLoanContractNo(uniqueId);

			contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
			contractDetail.setLoanCustomerName("WUBO");
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
			contractDetail.setIDCardNo("320301198502169142");
			contractDetail.setBankCode("C10105");
			contractDetail.setBankOfTheProvince("110000");
			contractDetail.setBankOfTheCity("110100");
			contractDetail.setRepaymentAccountNo("621485571210652"+repaymentAccountNo);
			contractDetail.setLoanTotalAmount(totalAmount);
			contractDetail.setLoanPeriods(3);
			contractDetail.setEffectDate(DateUtils.format(new Date()));
			contractDetail.setExpiryDate("2099-01-01");
			contractDetail.setLoanRates("0.156");
			contractDetail.setInterestRateCycle(1);
			contractDetail.setPenalty("0.0005");
			contractDetail.setRepaymentWay(2);

			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
			repaymentPlanDetail1.setRepaymentPrincipal(amount);
			repaymentPlanDetail1.setRepaymentInterest("0.00");
			repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
			repaymentPlanDetail1.setOtheFee("0.00");
			repaymentPlanDetail1.setTechMaintenanceFee("0.00");
			repaymentPlanDetail1.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail1);
			
			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
			repaymentPlanDetail2.setRepaymentPrincipal(amount);
			repaymentPlanDetail2.setRepaymentInterest("0.00");
			repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail2.setOtheFee("0.00");
			repaymentPlanDetail2.setTechMaintenanceFee("0.00");
			repaymentPlanDetail2.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail2);
			
			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
			repaymentPlanDetail3.setRepaymentPrincipal(amount);
			repaymentPlanDetail3.setRepaymentInterest("0.00");
			repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
			repaymentPlanDetail3.setOtheFee("0.00");
			repaymentPlanDetail3.setTechMaintenanceFee("0.00");
			repaymentPlanDetail3.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail3);
			
			contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

			contracts.add(contractDetail);
			importAssetPackageContent.setContractDetails(contracts);
			
			String param =  JsonUtils.toJsonString(importAssetPackageContent);
			System.out.println(param);
			Map<String,String> params =new HashMap<String,String>(); 
			params.put("fn", "200004");
			params.put("requestNo", UUID.randomUUID().toString());
			params.put("importAssetPackageContent", param); 
					
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("merId", "t_test_zfb");
			headers.put("secret", "123456");
			String signContent = ApiSignUtils.getSignCheckContent(params);
			String sign = ApiSignUtils.rsaSign(signContent, privateKey);
			System.out.println(sign);
			headers.put("sign", sign);
			
			try {
				String sr = PostTestUtil.sendPost(MODIFY_URL, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 查询该合同下第一期还款计划
	 * @param uniqueId
	 * @return
	 */
	public String queryFirstRepaymentPlan(String uniqueId){
		String repaymentNumber = "";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100001");
		requestParams1.put("contractNo", "");
		requestParams1.put("requestNo", UUID.randomUUID().toString());
		requestParams1.put("uniqueId", uniqueId);
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) result.get("repaymentPlanDetails");
			JSONObject jo = repaymentPlanDetails.getJSONObject(0);
		    repaymentNumber = (String) jo.get("repaymentNumber");
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentNumber;
	}
	
	/**
	 * 申请提前还款
	 * @param uniqueId
	 * @param assetInitialValue
	 * @param assetPrincipal
	 * @param assetRecycleDate
	 * @return
	 */
	public String applyPrepaymentPlan(String uniqueId,String assetInitialValue,String assetPrincipal,String assetRecycleDate){
		String result = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200002");
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", "");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("assetRecycleDate", assetRecycleDate);
		requestParams.put("assetInitialValue", assetInitialValue);
		requestParams.put("assetPrincipal", assetPrincipal);
		requestParams.put("type", "0");
		requestParams.put("payWay","0");
		//requestParams.put("hasDeducted", "-1");
		
		try {
			result = PostTestUtil.sendPost(MODIFY_URL, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询该合同下有效的还款计划
	 * @param uniqueId
	 * @return
	 */
	public String queryRepaymentPlanCount(String uniqueId){
		String repaymentPlanCount = "";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100001");
		requestParams1.put("contractNo", "");
		requestParams1.put("requestNo", UUID.randomUUID().toString());
		requestParams1.put("uniqueId", uniqueId);
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) result.get("repaymentPlanDetails");
			repaymentPlanCount = repaymentPlanDetails.size()+"";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentPlanCount;
	}
	/**
	 * 根据查询时间进行资产包批量查询
	 * @param startTime
	 * @param endTime
	 * @param productCode
	 * @return
	 */
	public String BatchQueryAssertPackage(String startTime,String endTime,String productCode){
		String result="";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100007");
		requestParams1.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
		requestParams1.put("productCode",productCode);
		requestParams1.put("startTime", startTime);
		requestParams1.put("endTime", endTime);
		try {
			result = PostTestUtil.sendPost("http://192.168.0.204/api/query", requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
