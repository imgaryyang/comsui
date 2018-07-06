package com.zufangbao.cucumber.method;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.util.*;

public class ImportAndApplyPrepaymentMethod {

	private  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
	
	String modifyUrl = "http://yunxin.5veda.net/api/modify";

//	String modifyUrl = "http://192.168.0.204:80/api/modify";
	
	public String applyPrepaymentPlan(String uniqueId,String assetInitialValue,String assetPrincipal,String assetRecycleDate,String assetInterest, String serviceCharge, String maintenanceCharge, String otherCharge){
		String result = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200002");
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", "");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("assetRecycleDate", assetRecycleDate);
		requestParams.put("assetInitialValue", assetInitialValue);
		requestParams.put("assetPrincipal", assetPrincipal);
		requestParams.put("assetInterest", assetInterest);
		requestParams.put("serviceCharge", serviceCharge);
		requestParams.put("maintenanceCharge", maintenanceCharge);
		requestParams.put("otherCharge", otherCharge);
		requestParams.put("type", "0");
		requestParams.put("payWay","0");
		//requestParams.put("hasDeducted", "-1");
		
		try {
			result = PostTestUtil.sendPost(modifyUrl, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public void importAssetPackage(String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String assetInterest, String serviceCharge, String maintenanceCharge, String otherCharge, String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
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
			contractDetail.setEffectDate(DateUtils.format(new Date()));
			contractDetail.setExpiryDate("2099-01-01");
			contractDetail.setLoanRates("0.156");
			contractDetail.setInterestRateCycle(1);
			contractDetail.setPenalty("0.0005");
			contractDetail.setRepaymentWay(2);
			
			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
			repaymentPlanDetail1.setRepaymentPrincipal(amount);
			repaymentPlanDetail1.setRepaymentInterest(assetInterest);
			repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
			repaymentPlanDetail1.setOtheFee(otherCharge);
			repaymentPlanDetail1.setTechMaintenanceFee(maintenanceCharge);
			repaymentPlanDetail1.setLoanServiceFee(serviceCharge);
			repaymentPlanDetails.add(repaymentPlanDetail1);
			
			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
			repaymentPlanDetail2.setRepaymentPrincipal(amount);
			repaymentPlanDetail2.setRepaymentInterest(assetInterest);
			repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail2.setOtheFee(otherCharge);
			repaymentPlanDetail2.setTechMaintenanceFee(maintenanceCharge);
			repaymentPlanDetail2.setLoanServiceFee(serviceCharge);
			repaymentPlanDetails.add(repaymentPlanDetail2);
			
			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
			repaymentPlanDetail3.setRepaymentPrincipal(amount);
			repaymentPlanDetail3.setRepaymentInterest(assetInterest);
			repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
			repaymentPlanDetail3.setOtheFee(otherCharge);
			repaymentPlanDetail3.setTechMaintenanceFee(maintenanceCharge);
			repaymentPlanDetail3.setLoanServiceFee(serviceCharge);
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
				String sr = PostTestUtil.sendPost(modifyUrl, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
