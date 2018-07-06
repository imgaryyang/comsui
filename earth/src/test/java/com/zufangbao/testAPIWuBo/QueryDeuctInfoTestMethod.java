package com.zufangbao.testAPIWuBo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.DateUtils;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QueryDeuctInfoTestMethod extends BaseApiTestPost{

	/**
	 * 查询该合同下第i期还款计划
	 * @param uniqueId
	 * @return
	 */
	public String query_i_RepaymentPlan(String uniqueId,int i){
		String repaymentNumber = "";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100001");
		requestParams1.put("contractNo", "");
		requestParams1.put("requestNo", UUID.randomUUID().toString());
		requestParams1.put("uniqueId", uniqueId);
		try {
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) result.get("repaymentPlanDetails");
			JSONObject jo = repaymentPlanDetails.getJSONObject(i);
		    repaymentNumber = (String) jo.get("repaymentNumber");
			System.out.println("---------"+sr);
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
	@Test
	public void deductRepaymentPlan(String deductId,String repaymentNumber,String uniqueId, String productCode, String amount, String repaymentType){
		/**/
        Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  deductId);
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
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
	
	//扣款查询
	public void deductInfoList1(String uniqueId,String deductId) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100002");
		requestParams.put("deductId", deductId);
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo",uniqueId);
		try {
			String sr = PostTestUtil.sendPost(DEDUCT_QUERY_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//批量扣款查询
public void batchDedcutStatusApiTestPost1(String deductList,String repaymentPlanCodeList,String repaymentType){
		
		Map<String,String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100006");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		//requestParams.put("deductIdList","[\"1b5588cb-4f4a-478a-897b-c6a658b971cb\"]");
		requestParams.put("deductIdList",deductList);
		requestParams.put("repaymentPlanCodeList",repaymentPlanCodeList);
		requestParams.put("repaymentType",repaymentType);
		try {
			long start = System.currentTimeMillis();
			String sr = PostTestUtil.sendPost(DEDUCT_QUERY_TEST, requestParams, getIdentityInfoMap(requestParams));
		    System.out.println(sr);
			long end = System.currentTimeMillis();
		    System.out.println("=============扣款状态查询时间============"+(end-start)+"[ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
