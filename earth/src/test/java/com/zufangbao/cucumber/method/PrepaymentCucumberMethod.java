package com.zufangbao.cucumber.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.util.*;

public class PrepaymentCucumberMethod extends BaseApiTestPost{


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
			contractDetail.setLoanCustomerName("李杰");
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
			contractDetail.setIDCardNo("320301198502169142");
			contractDetail.setBankCode("C10105");
			contractDetail.setBankOfTheProvince("110000");
			contractDetail.setBankOfTheCity("110100");
			contractDetail.setRepaymentAccountNo("621485571210652"+repaymentAccountNo);
			contractDetail.setLoanTotalAmount(totalAmount);
			contractDetail.setLoanPeriods(3);
			contractDetail.setEffectDate(com.demo2do.core.utils.DateUtils.format(com.demo2do.core.utils.DateUtils.addMonths(new Date(), -2)));
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
				String sr = PostTestUtil.sendPost(IMPORT_PACKAGE, params, headers);
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
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
			JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
			JSONObject jo = jsonArray.getJSONObject(0);
			repaymentNumber = (String) jo.get("repaymentNumber");
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentNumber;
	}

	public String querySecondRepaymentPlan(String uniqueId){
		String repaymentNumber = "";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100001");
		requestParams1.put("contractNo", "");
		requestParams1.put("requestNo", UUID.randomUUID().toString());
		requestParams1.put("uniqueId", uniqueId);
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
			JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
			JSONObject jo = jsonArray.getJSONObject(1);
			repaymentNumber = (String) jo.get("repaymentNumber");
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentNumber;
	}
	/**
	 * 扣款	
	 * @param repaymentNumber
	 * @param uniqueId
	 * @param productCode
	 * @param amount
	 * @param repaymentType
	 */
	public void deductRepaymentPlan(String repaymentNumber,String uniqueId, String productCode, String amount, String repaymentType){
		/**/
        Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("apiCalledTime", DateUtils.format(new Date()));
		requestParams.put("amount",  amount);
		requestParams.put("repaymentType", repaymentType);//1、正常  0、提前划扣
		requestParams.put("mobile", "13777847783"); 
		requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':"+amount+",'repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+amount+",'techFee':0.00,'overDueFeeDetail':{"
//				+ "'penaltyFee':1.00,'latePenalty':1.00,'lateFee':1.00,'lateOtherCost':1.00,'totalOverdueFee':4.00}}]");
		+ "'totalOverdueFee':0.00}}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams,getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
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
			result = PostTestUtil.sendPost(PREPAYMENT, requestParams,getIdentityInfoMap(requestParams));
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
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1,getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) responsePacket.get("repaymentPlanDetails");
			repaymentPlanCount = repaymentPlanDetails.size()+"";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentPlanCount;
	}

}
