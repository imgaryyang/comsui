package com.zufangbao.earth.api.test.post;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.api.model.deduct.OverDueFeeDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
////
//		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml"})
//@Transactional
@Component
public class CommandDeductApiPostZH extends BaseApiTestPost{
	
	
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private ContractService contractService;
	
	
	
	@Test
	@Deprecated
	public void commandDedcutManualApiTestPost() {
		Map<String, String> requestParams = new HashMap<String, String>();
		
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "G31700");
		requestParams.put("uniqueId", "wb1114523483");
		requestParams.put("apiCalledTime", "2017-06-05");
		requestParams.put("amount",  "500.01");
		requestParams.put("repaymentType", "0");
		requestParams.put("mobile", "13777847783");

		List<RepaymentDetail> repaymentDetails = new ArrayList<>();
		OverDueFeeDetail overDueFeeDetail = new OverDueFeeDetail();
		overDueFeeDetail.setLateFee(new BigDecimal("0"));
		overDueFeeDetail.setLateOtherCost(new BigDecimal("0"));
		overDueFeeDetail.setLatePenalty(new BigDecimal("0"));
		overDueFeeDetail.setPenaltyFee(new BigDecimal("0"));
		overDueFeeDetail.setTotalOverdueFee(new BigDecimal("0"));
		RepaymentDetail repaymentDetail = new RepaymentDetail();
		repaymentDetail.setRepaymentAmount(new BigDecimal("500.01"));
		repaymentDetail.setLoanFee(new BigDecimal("0"));
		repaymentDetail.setOtherFee(new BigDecimal("0"));
		repaymentDetail.setRepaymentInterest(new BigDecimal("0"));
		repaymentDetail.setRepaymentPrincipal(new BigDecimal("500.01"));
		repaymentDetail.setTechFee(new BigDecimal("0"));
		repaymentDetail.setRepaymentPlanNo("ZC70623649916076032");

		repaymentDetail.setOverDueFeeDetail(overDueFeeDetail);
		repaymentDetails.add(repaymentDetail);

		requestParams.put("repaymentDetails",JsonUtils.toJSONString(repaymentDetails));


		System.out.println(JsonUtils.toJsonString(requestParams));
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
			String a = new String();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
//	List<RepaymentDetail> repaymentDetails = new ArrayList<>();
//		RepaymentDetail repaymentDetail = new RepaymentDetail();
//		repaymentDetail.setRepaymentAmount(new BigDecimal("500.01"));
//		repaymentDetail.setLoanFee(new BigDecimal("0"));
//		repaymentDetail.setOtherFee(new BigDecimal("0"));
//		repaymentDetail.setRepaymentInterest(new BigDecimal("0"));
//		repaymentDetail.setRepaymentPrincipal(new BigDecimal("500.01"));
//		repaymentDetail.setTechFee(new BigDecimal("0"));
////		repaymentDetail.setOverDueFeeDetail(new OverDueFeeDetail());
//		repaymentDetails.add(repaymentDetail);
//		JSONArray jsonArray = JSONArray.fromObject(repaymentDetails);
//		requestParams.put = ("repaymentDetails",jsonArray.toString());
	
	@Test
	@Deprecated
	public void autoDeductPostNormal(){

		
		List<String> hasDeductedContractUniqueIds = deductApplicationService.loadAll(DeductApplication.class).stream().map(dp -> dp.getContractUniqueId()).distinct().collect(Collectors.toList());
		List<Contract>  notHasDeductedContracts =new ArrayList<Contract>();
		if(hasDeductedContractUniqueIds.size()!= 0){
			notHasDeductedContracts = contractService.list(Contract.class, new Filter());
		}
		else{
			notHasDeductedContracts = contractService.getNotDeductContractForTest(hasDeductedContractUniqueIds);
		}
		
		for(Contract notHasDeductedContract:notHasDeductedContracts){
			
			
			List<AssetSet> repaymentPlans = repaymentPlanService.get_all_open_repayment_plan_list(notHasDeductedContract);
			AssetSet repaymentPlan1 = repaymentPlans.get(0);
			FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentPlan1.getFinancialContractUuid());
			
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300001");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("deductId",  UUID.randomUUID().toString());
			requestParams.put("financialProductCode", financialContract.getContractNo());
			requestParams.put("uniqueId", notHasDeductedContract.getUniqueId());
			requestParams.put("apiCalledTime",DateUtils.format(DateUtils.addDays(repaymentPlan1.getAssetRecycleDate(), 10)));
			requestParams.put("amount", "222");
			requestParams.put("repaymentType", "2");
			requestParams.put("mobile", notHasDeductedContract.getCustomer().getMobile());
			requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':222"+",'repaymentInterest':"+repaymentPlan1.getAssetInterestValue()+",'repaymentPlanNo':\'"+repaymentPlan1.getSingleLoanContractNo()+"\','repaymentPrincipal':222"+",'techFee':0.00,'overDueFeeDetail':{'totalOverdueFee':0.00}}]");
			try {
				String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));


				System.out.println(sr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	//逾期扣款（同时扣两个还款计划）
	@Test
	public void autoDeductPostOverDue(){
		
		
		
		List<String> hasDeductedContractUniqueIds = deductApplicationService.loadAll(DeductApplication.class).stream().map(dp -> dp.getContractUniqueId()).distinct().collect(Collectors.toList());
		List<Contract>  notHasDeductedContracts =new ArrayList<Contract>();
		if(hasDeductedContractUniqueIds.size()== 0){
			notHasDeductedContracts = contractService.list(Contract.class, new Filter());
		}
		else{
			notHasDeductedContracts = contractService.getNotDeductContractForTest(hasDeductedContractUniqueIds);
		}
		
		for(Contract notHasDeductedContract:notHasDeductedContracts){
			
			
			List<AssetSet> repaymentPlans = repaymentPlanService.get_all_open_repayment_plan_list(notHasDeductedContract);
			AssetSet repaymentPlan1 = repaymentPlans.get(0);
			AssetSet repaymentPlan2 = repaymentPlans.get(1);
			FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentPlan1.getFinancialContractUuid());
			
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300001");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("deductId",  UUID.randomUUID().toString());
			requestParams.put("financialProductCode", financialContract.getContractNo());
			requestParams.put("uniqueId", notHasDeductedContract.getUniqueId());
			requestParams.put("apiCalledTime",DateUtils.format(DateUtils.addDays(repaymentPlan1.getAssetRecycleDate(), 100)));
			requestParams.put("amount",  notHasDeductedContract.getTotalAmount().toString());
			requestParams.put("repaymentType", "2");
			requestParams.put("mobile", "13777846671");
			requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':"+ repaymentPlan1.getAmount()+",'repaymentInterest':"+repaymentPlan1.getAssetInterestValue()+",'repaymentPlanNo':\'"+repaymentPlan1.getSingleLoanContractNo()+"\','repaymentPrincipal':"+repaymentPlan1.getAssetPrincipalValue()+",'techFee':0.00,'overDueFeeDetail':{'totalOverdueFee':0.00}}"
					+ ",{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':"+ repaymentPlan2.getAmount()+",'repaymentInterest':"+repaymentPlan2.getAssetInterestValue()+",'repaymentPlanNo':\'"+repaymentPlan2.getSingleLoanContractNo()+"\','repaymentPrincipal':"+repaymentPlan2.getAssetPrincipalValue()+",'techFee':0.00,'overDueFeeDetail':{'totalOverdueFee':0.00}}]");
			try {
				String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
				System.out.println(sr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	@Test
	@Deprecated
	public void commandZHDedcutManualApiTestPost() {
		Map<String, String> requestParams = new HashMap<String, String>();

		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", "11111111");
		requestParams.put("uniqueId", "zhanglongfei13");
		requestParams.put("contractNo","zhanglongfei13");
		requestParams.put("apiCalledTime", "2018-02-09");
		requestParams.put("transType","0");//0:B2C  1:B2B
		requestParams.put("accountName"," 高渐");
		requestParams.put("accountNo","6214855712117670");
		requestParams.put("notifyUrl","http://server.ngrok.cc:17865/mock/deduct/single/success");
//		requestParams.put("gateway","10");
		requestParams.put("amount",  "10080");
		requestParams.put("repaymentType", "1");
		requestParams.put("mobile", "");
		requestParams.put("repaymentDetails", "[{\"currentPeriod\":1,\"loanFee\":0,\"otherFee\":0,\"totalAmount\":10080,\"overDueFeeDetail\":{\"lateFee\":0,\"lateOtherCost\":0,\"latePenalty\":0,\"penaltyFee\":0,\"totalOverdueFee\":0},\"repaymentAmount\":0,\"repaymentInterest\":0,\"repaymentPrincipal\":0,\"techFee\":0}]");
//				+ "'totalOverdueFee':0.00}}]");

		System.out.println(JsonUtils.toJsonString(requestParams));
		try {
			String sr = PostTestUtil.sendPost("http://avictctest.5veda.net:8887/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct", requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
			String a = new String();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}






}
