package com.zufangbao.earth.yunxin.api;

import com.zufangbao.earth.yunxin.handler.modifyOverDueFee.ModifyOverDueFeeHandler;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeDetail;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeRequestModel;
import com.zufangbao.sun.yunxin.service.ModifyOverDueFeeLogService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ModifyOverDueFeeHandlerTest {

	
	@Autowired
	private ModifyOverDueFeeHandler modifyOverDueFeeHandler;
	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private ModifyOverDueFeeLogService modifyOverDueFeeLogService;
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	@Test
	@Sql("classpath:test/yunxin/modifyOverDueFee/tetsModifyOverDueFeeHandlerSuccess.sql")
	public void  testModifyOverDueFee(){
		
		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());
		List<ModifyOverDueFeeDetail> modifyOverDueFeeDetails =new ArrayList<ModifyOverDueFeeDetail>();
		ModifyOverDueFeeDetail modifyOverDueFeeDetail =new ModifyOverDueFeeDetail();
		modifyOverDueFeeDetail.setContractUniqueId("1234567890");
		modifyOverDueFeeDetail.setLateFee("100.00");
		modifyOverDueFeeDetail.setLateOtherCost("00.00");
		modifyOverDueFeeDetail.setLatePenalty("100.00");
		modifyOverDueFeeDetail.setOverDueFeeCalcDate("2016-06-08");
		modifyOverDueFeeDetail.setPenaltyFee("100.00");
		modifyOverDueFeeDetail.setRepaymentPlanNo("ZC27375ACFF4234805");
		modifyOverDueFeeDetail.setTotalOverdueFee("140.00");
		modifyOverDueFeeDetails.add(modifyOverDueFeeDetail);

		/*modifyOverDueFeeHandler.modifyOverDueFeeAndCheckData(model, request);
		AssetSet repaymentPlan  = repaymentPlanService.getRepaymentPlanByRepaymentCode( modifyOverDueFeeDetail.getRepaymentPlanNo());
		Map<String, BigDecimal> amountMap = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(repaymentPlan.getAssetUuid());
		
		Assert.assertEquals(new BigDecimal("100.00"), amountMap.get(ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY));
		Assert.assertEquals(new BigDecimal("100.00"),amountMap.get(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION));
		Assert.assertEquals(new BigDecimal("100.00"),amountMap.get(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE));
		Assert.assertEquals(null,amountMap.get(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE));
		
		List<ModifyOverdueFeeLog> modifyOverdueFeeLogList = modifyOverDueFeeLogService.list(ModifyOverdueFeeLog.class, new Filter().addEquals("repaymentPlanNo", "ZC27375ACFF4234805"));
		Assert.assertEquals(new BigDecimal("100.00"),modifyOverdueFeeLogList.get(0).getOverdueFeeObligation());
		Assert.assertEquals(new BigDecimal("00.00"),modifyOverdueFeeLogList.get(0).getOverdueFeeOther());
		Assert.assertEquals(new BigDecimal("100.00"),modifyOverdueFeeLogList.get(0).getOverdueFeeService());
		Assert.assertEquals(new BigDecimal("100.00"),modifyOverdueFeeLogList.get(0).getOverdueFeePenalty());*/
	}
	

	@Test
	@Sql("classpath:test/yunxin/modifyOverDueFee/testModifyOverDueFeeRepaymentPlanNotInContract.sql")
	public void  testModifyOverDueFeeRepaymentPlanNotInContract(){
			
		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());
		List<ModifyOverDueFeeDetail> modifyOverDueFeeDetails =new ArrayList<ModifyOverDueFeeDetail>();
		ModifyOverDueFeeDetail modifyOverDueFeeDetail =new ModifyOverDueFeeDetail();
		modifyOverDueFeeDetail.setContractUniqueId("1234567890");
		modifyOverDueFeeDetail.setLateFee("0.00");
		modifyOverDueFeeDetail.setLateOtherCost("0.00");
		modifyOverDueFeeDetail.setLatePenalty("100.00");
		modifyOverDueFeeDetail.setOverDueFeeCalcDate("2016-06-08");
		modifyOverDueFeeDetail.setPenaltyFee("40.00");
		modifyOverDueFeeDetail.setRepaymentPlanNo("ZC27375ACFF4234805TEST");
		modifyOverDueFeeDetail.setTotalOverdueFee("140.00");
		modifyOverDueFeeDetails.add(modifyOverDueFeeDetail);
		/*try {
			model.setModifyOverDueFeeDetailsParseJson(modifyOverDueFeeDetails);
			modifyOverDueFeeHandler.modifyOverDueFeeAndCheckData(model, request);
			fail();
		} catch (ApiException e) {
			// TODO: handle exception
			System.out.println("a");
			Assert.assertEquals(22203, e.getCode());
		}*/
	}

	
	@Test
	@Sql("classpath:test/yunxin/modifyOverDueFee/testModifyOverDueFeeOverDueFeeCalcDateAfterCalcOverDueFee.sql")
	public void  testModifyOverDueFeeOverDueFeeCalcDateAfterCalcOverDueFee(){
			
		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());
		List<ModifyOverDueFeeDetail> modifyOverDueFeeDetails =new ArrayList<ModifyOverDueFeeDetail>();
		ModifyOverDueFeeDetail modifyOverDueFeeDetail =new ModifyOverDueFeeDetail();
		modifyOverDueFeeDetail.setContractUniqueId("1234567890");
		modifyOverDueFeeDetail.setLateFee("0.00");
		modifyOverDueFeeDetail.setLateOtherCost("0.00");
		modifyOverDueFeeDetail.setLatePenalty("100.00");
		modifyOverDueFeeDetail.setOverDueFeeCalcDate("2016-05-30");
		modifyOverDueFeeDetail.setPenaltyFee("40.00");
		modifyOverDueFeeDetail.setRepaymentPlanNo("ZC27375ACFF4234805");
		modifyOverDueFeeDetail.setTotalOverdueFee("140.00");
		modifyOverDueFeeDetails.add(modifyOverDueFeeDetail);
		/*try {
			model.setModifyOverDueFeeDetailsParseJson(modifyOverDueFeeDetails);
			modifyOverDueFeeHandler.modifyOverDueFeeAndCheckData(model, request);
			fail();
		} catch (ApiException e) {
			// TODO: handle exception
			Assert.assertEquals(23100, e.getCode());
		}*/
		
		
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/modifyOverDueFee/testModifyOverDueFeeRepaymentLocked.sql")
	public void  testModifyOverDueFeeRepaymentLocked(){
		
		ModifyOverDueFeeRequestModel model = new ModifyOverDueFeeRequestModel();
		model.setRequestNo(UUID.randomUUID().toString());
		List<ModifyOverDueFeeDetail> modifyOverDueFeeDetails =new ArrayList<ModifyOverDueFeeDetail>();
		ModifyOverDueFeeDetail modifyOverDueFeeDetail =new ModifyOverDueFeeDetail();
		modifyOverDueFeeDetail.setContractUniqueId("1234567890");
		modifyOverDueFeeDetail.setLateFee("100.00");
		modifyOverDueFeeDetail.setLateOtherCost("00.00");
		modifyOverDueFeeDetail.setLatePenalty("100.00");
		modifyOverDueFeeDetail.setOverDueFeeCalcDate("2016-06-08");
		modifyOverDueFeeDetail.setPenaltyFee("100.00");
		modifyOverDueFeeDetail.setRepaymentPlanNo("ZC27375ACFF4234805");
		modifyOverDueFeeDetail.setTotalOverdueFee("140.00");
		modifyOverDueFeeDetails.add(modifyOverDueFeeDetail);
       /* try{
        	modifyOverDueFeeHandler.modifyOverDueFeeAndCheckData(model, request);
        	fail();
        }catch (ApiException e) {
        	Assert.assertEquals(23105, e.getCode());
        	Assert.assertEquals("还款计划已被锁定", e.getMessage());
		}*/
		
		
	}
}
