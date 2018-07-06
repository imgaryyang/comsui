package com.zufangbao.earth.api.test.post;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
////
//		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml"})
//@Transactional
@Component
public class CommandDeductApiPost extends BaseApiTestPost{
	
	
	
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
		requestParams.put("financialProductCode", "CS0001");
		requestParams.put("uniqueId", "2600d9a3-e7b2-4329-989f-fe763d19db79");
		requestParams.put("apiCalledTime", "2018-04-20");
		requestParams.put("amount",  "10080");
		requestParams.put("bankCode","C10104");
		requestParams.put("payerName","秦萎超");
		requestParams.put("payAcNo","6217857600016839330"); //6217857600016839330
		requestParams.put("idCardNum","472578425753257425");
		requestParams.put("provinceCode","620000");
		requestParams.put("cityCode","141100");
		requestParams.put("repaymentType", "0");
		requestParams.put("mobile", "15317312520");
		requestParams.put("repaymentDetails", "[{'loanFee':20,'otherFee':20,'repaymentAmount':10080,'repaymentInterest':20,'repaymentPlanNo':'ZC186844602823864320','repaymentPrincipal':10000,'techFee':20,'overDueFeeDetail':{"
//				+ "'penaltyFee':20.00,'latePenalty':20.0,'lateFee':20.00,'lateOtherCost':20.00,'totalOverdueFee':80}}]");
		+ "'totalOverdueFee':0.00}}]");
		try {
			String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
	
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
	

	
	
	


}
