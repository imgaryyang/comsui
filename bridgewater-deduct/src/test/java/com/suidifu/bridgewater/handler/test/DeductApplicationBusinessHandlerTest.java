package com.suidifu.bridgewater.handler.test;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.DeductCommandRequestModel;
import com.zufangbao.sun.api.model.deduct.OverDueFeeDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationSqlModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.RepaymentPlanCodeAndDeductApplicationObject;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Oct 24, 2016 9:06:27 PM 
* 类说明 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class DeductApplicationBusinessHandlerTest {

	
	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
	
	@Autowired
	private DeductApplicationCoreOperationHandler deductApplicationCoreOperationHandler;
	@Autowired
	private ContractService contractService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private DeductApplicationService deductApplicationService;
	
	
	
	@Test
	@Sql("classpath:test/yunxin/api/testConcurrentDeductAmount.sql")
	public void testConcurrentDeductAmount(){
		
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,\"totalOverdueFee\":0.00}]");
		commandModel.setRepaymentType(0);
		
		Contract contract = contractService.getContract(24l);
		FinancialContract financialContract = financialContractService.getFinancialContractBy("d2812bc5-5057-4a91-b3fd-9019506f0499");
		
		deductApplicationBusinessHandler.saveDeductInfoBeforeProcessing(commandModel, "127.0.0.1", "zfb_name", contract, financialContract);
		
		String newVersionUUid = repaymentPlanService.queryActiveDeductApplicationUuidBySql("asset_uuid_1");
		Assert.assertNotEquals(AssetSet.EMPTY_UUID, newVersionUUid);
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testConcurrentDeductAmountNotEmpty.sql")
	public void testConcurrentDeductAmountNotEmpty(){
		
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		commandModel.setRequestNo(UUID.randomUUID().toString());
		commandModel.setDeductId(UUID.randomUUID().toString());
		commandModel.setContractNo("629测试(ZQ2016002000001)");
		commandModel.setAmount("100");
		commandModel.setApiCalledTime("2016-03-05");
		commandModel.setFinancialProductCode("G31700");
		commandModel.setRepaymentDetails("[{\"loanFee\":0.00,\"otherFee\":0.00,\"repaymentAmount\":100.00,\"repaymentInterest\":100.00,\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"repaymentPrincipal\":0.00,\"techFee\":0.00,\"totalOverdueFee\":0.00}]");
		commandModel.setRepaymentType(0);
		
		Contract contract = contractService.getContract(24l);
		FinancialContract financialContract = financialContractService.getFinancialContractBy("d2812bc5-5057-4a91-b3fd-9019506f0499");
		try{
		deductApplicationBusinessHandler.saveDeductInfoBeforeProcessing(commandModel, "127.0.0.1", "zfb_name", contract, financialContract);
		}
		catch (ApiException e) {
			assertEquals(22100, e.getCode());
		}
				
	}
	@Test
	@Sql("classpath:test/yunxin/api/testConcurrentDeductAmountNotEmpty.sql")
	public void testCheckConcurrent(){
		DeductApplication deductApplication =new DeductApplication();
		RepaymentDetail repaymentDetail = new RepaymentDetail();
		repaymentDetail.setRepaymentPlanNo("ZC27375ACFF4234805");
		try {
			
		deductApplicationBusinessHandler.checkConcurrent(deductApplication, repaymentDetail);
		} catch (ApiException e) {
			assertEquals(22211, e.getCode());
		}
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testConcurrentDeductNoLock.sql")
	public void testCheckConcurrentNoLock(){
		DeductApplication deductApplication =new DeductApplication();
		deductApplication.setDeductApplicationUuid("12345678");
		RepaymentDetail repaymentDetail = new RepaymentDetail();
		repaymentDetail.setRepaymentPlanNo("ZC27375ACFF4234805");
		deductApplicationBusinessHandler.checkConcurrent(deductApplication, repaymentDetail);
		String  activeDeductApplicationUuid = repaymentPlanService.queryActiveDeductApplicationUuidBySql("asset_uuid_1");
		assertEquals("12345678", activeDeductApplicationUuid);
	}	
	
	@Test
	@Sql("classpath:test/yunxin/api/testUnlockRepaymentPlans.sql")
	public void testUnlockRepaymentPlans(){
		String deductApplicationUuid ="51610829-01f9-400b-adaa-5f7a9545441a";
		
		try {
		deductApplicationBusinessHandler.unlockRepaymentPlans(deductApplicationUuid);
		} catch (ApiException e) {
			assertEquals(22211, e.getCode());
		}
	}
	@Test
	@Sql("classpath:test/yunxin/api/testUnlockRepaymentPlans1.sql")
	public void testUnlockRepaymentPlans1(){
		String deductApplicationUuid ="51610829-01f9-400b-adaa-5f7a9545441a";
		deductApplicationBusinessHandler.unlockRepaymentPlans(deductApplicationUuid);
		String assetUuid = repaymentPlanService.queryActiveDeductApplicationUuidBySql("asset_uuid_1");
		assertEquals("empty_deduct_uuid", assetUuid);
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testGetDeductApplicationDetailInRepaymentPlanCodeList.sql")
	public void testGetDeductApplicationDetailInRepaymentPlanCodeList(){
		Map<String, List<DeductApplication>> duductApplicationMap = deductApplicationCoreOperationHandler.getDeductApplicationDetailInRepaymentPlanCodeList(null);
		assertEquals(0, duductApplicationMap.size());
		
		List<String> repaymentPlanCodeList = Arrays.asList("ZC27438B14F806E86C","ZC2748A7ECBDC97EAC");
		duductApplicationMap = deductApplicationCoreOperationHandler.getDeductApplicationDetailInRepaymentPlanCodeList(repaymentPlanCodeList);
		assertEquals(2, duductApplicationMap.size());		
	}
	
	@Test
	public void test(){
		List<String> repaymentPlanCodeList = new ArrayList<String>();
		repaymentPlanCodeList.add("ZC27438A3C7D00C129");
		String queryString = "select distinct(deductApplicationUuid),repaymentPlanCode from DeductApplicationDetail where repaymentPlanCode IN(:repaymentPlanCodeList) " ;
		List<Object[]> deductApplicationDetailList = this.genericDaoSupport.searchForList(queryString, "repaymentPlanCodeList", repaymentPlanCodeList);

		List<RepaymentPlanCodeAndDeductApplicationObject> paramsObject = new ArrayList<RepaymentPlanCodeAndDeductApplicationObject>();
		for(Object[] objectArray:deductApplicationDetailList){
			paramsObject.add(new RepaymentPlanCodeAndDeductApplicationObject(objectArray));
		}
		for(RepaymentPlanCodeAndDeductApplicationObject object:paramsObject){
			System.out.println(object.getDeductApplicationUuid());
		}
		
	}
	
	
	
	@Sql("classpath:test/yunxin/api/testResultListNotContainNullElement.sql")
	public void testResultListNotContainNull(){
		Map<String, List<DeductApplication>> repayment_code_duduct_app_map = deductApplicationCoreOperationHandler.getDeductApplicationDetailInRepaymentPlanCodeList(Arrays.asList("ZC27438B14F806E86C","ZC275026A1838523D2","ZC2754DDB97B3B8DB4"));
		for(String code:repayment_code_duduct_app_map.keySet()){
			for(DeductApplication deductapp:repayment_code_duduct_app_map.get(code)){
				Assert.assertNotNull(deductapp);
			}
		}	
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testResultListNotContainNullElement.sql")
	public void testResultListNum(){
		Map<String, List<DeductApplication>> repayment_code_duduct_app_map = deductApplicationCoreOperationHandler.getDeductApplicationDetailInRepaymentPlanCodeList(Arrays.asList("ZC27438B14F806E86C","ZC275026A1838523D2","ZC2754DDB97B3B8DB4"));
		List<DeductApplication> deductAppList = new ArrayList<DeductApplication>();
		for(String code:repayment_code_duduct_app_map.keySet()){
			deductAppList.addAll(repayment_code_duduct_app_map.get(code));
		}	
		Assert.assertEquals(2, deductAppList.size());
	}
	@Test
	@Sql("classpath:test/yunxin/api/testResultListNotContainNullElement.sql")
	public void test_CheckRepaymentDetailAmount_AllNUll(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		
		OverDueFeeDetail overDueFeeDetail = new	OverDueFeeDetail();
		overDueFeeDetail.setTotalOverdueFee(new BigDecimal(4));
		RepaymentDetail repaymentDetail = new RepaymentDetail();
		repaymentDetail.setOverDueFeeDetail(overDueFeeDetail);
		repaymentDetail.setLoanFee(new BigDecimal(1));
		repaymentDetail.setOtherFee(new BigDecimal(1));
		repaymentDetail.setRepaymentAmount(new BigDecimal(9));
		repaymentDetail.setRepaymentPlanNo("12345");
		repaymentDetail.setRepaymentPrincipal(new BigDecimal(1));
		repaymentDetail.setTechFee(new BigDecimal(1));
		repaymentDetail.setRepaymentInterest(new BigDecimal(1));
		List<RepaymentDetail> repaymentDetails = new ArrayList<RepaymentDetail>();
		repaymentDetails.add(repaymentDetail);
		try {
			deductApplicationBusinessHandler.checkRepaymentDetailAmount(commandModel, repaymentDetails);
		} catch (ApiException e) {
			
			Assert.assertEquals(ApiResponseCode.REPAYMENT_DETAILS_AMOUNT_ERROR, e.getCode());
		}
		
	}
	@Test
	@Sql("classpath:test/yunxin/api/testResultListNotContainNullElement.sql")
	public void test_CheckRepaymentDetailAmount(){
		DeductCommandRequestModel commandModel = new DeductCommandRequestModel();
		
		OverDueFeeDetail overDueFeeDetail = new	OverDueFeeDetail();
		overDueFeeDetail.setLateFee(new BigDecimal(1));
		overDueFeeDetail.setLateOtherCost(new BigDecimal(1));
		overDueFeeDetail.setLatePenalty(new BigDecimal(1));
		overDueFeeDetail.setPenaltyFee(new BigDecimal(1));
		overDueFeeDetail.setTotalOverdueFee(new BigDecimal(4));
		RepaymentDetail repaymentDetail = new RepaymentDetail();
		repaymentDetail.setOverDueFeeDetail(overDueFeeDetail);
		repaymentDetail.setLoanFee(new BigDecimal(1));
		repaymentDetail.setOtherFee(new BigDecimal(1));
		repaymentDetail.setRepaymentAmount(new BigDecimal(9));
		repaymentDetail.setRepaymentPlanNo("12345");
		repaymentDetail.setRepaymentPrincipal(new BigDecimal(1));
		repaymentDetail.setTechFee(new BigDecimal(1));
		repaymentDetail.setRepaymentInterest(new BigDecimal(1));
		List<RepaymentDetail> repaymentDetails = new ArrayList<RepaymentDetail>();
		repaymentDetails.add(repaymentDetail);
		
		System.out.println(JsonUtils.toJSONString(repaymentDetails));		
		try {
			deductApplicationBusinessHandler.checkRepaymentDetailAmount(commandModel, repaymentDetails);
		} catch (ApiException e) {
			
			Assert.assertEquals(ApiResponseCode.REPAYMENT_DETAILS_AMOUNT_ERROR, e.getCode());
		}
		
	}

	@Test
	@Sql("classpath:test/yunxin/deductQuery/test_DeductApplication.sql")
	public void testDeductApplicationSqlModel(){

		DeductApplicationSqlModel sqlModel = deductApplicationService.getDeductApplicationSqlModelByDeducApplicationUuid("7254294a-f1ba-4a46-b978-31241e23d0d7");

		Assert.assertEquals("556acc90-9dcd-4446-9fc0-47d26244b050", sqlModel.getCheckResponseNo());
		Assert.assertEquals(1, sqlModel.getExecutionStatus().intValue());
		Assert.assertEquals(4, sqlModel.getReceiveStatus().intValue());
		Assert.assertEquals(DateUtils.parseDate("2017-11-27 20:06:27", "yyyy-MM-dd HH:mm:ss"), sqlModel.getLastModifiedTime());
	}
}
