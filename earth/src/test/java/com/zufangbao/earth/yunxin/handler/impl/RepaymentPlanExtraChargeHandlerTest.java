package com.zufangbao.earth.yunxin.handler.impl;

import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanExtraChargeHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
public class RepaymentPlanExtraChargeHandlerTest {
	@Autowired
	private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;
	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
	@Test
	@Sql("classpath:test/yunxin/testGetAssetSetsExtraChargesMapBy.sql")
	public void testGetAssetSetsExtraChargesMapBy(){
		List<String> assetSetUuids = Arrays.asList("1","2","3");
		Map<String, Map<String, BigDecimal>> AssetSetsExtraChargesMap = repaymentPlanExtraChargeHandler.getAssetSetsExtraChargesMapBy(assetSetUuids);
		assertEquals(3,AssetSetsExtraChargesMap.size());
		assertEquals(new BigDecimal("100.00"),AssetSetsExtraChargesMap.get("1").get("THIRD_NAME"));
		assertEquals(new BigDecimal("100.00"),AssetSetsExtraChargesMap.get("2").get("SECOND_NAME"));
		assertEquals(new BigDecimal("100.00"),AssetSetsExtraChargesMap.get("3").get("FIRST_NAME"));
		
		
		assetSetUuids = Arrays.asList("");
		AssetSetsExtraChargesMap = repaymentPlanExtraChargeHandler.getAssetSetsExtraChargesMapBy(assetSetUuids);
		assertEquals(0,AssetSetsExtraChargesMap.size());
		
		assetSetUuids = null;
		AssetSetsExtraChargesMap = repaymentPlanExtraChargeHandler.getAssetSetsExtraChargesMapBy(assetSetUuids);
		assertEquals(0,AssetSetsExtraChargesMap.size());	
	}
	
	@Test
	@Sql("classpath:test/yunxin/testModifyFeeInAssetSetExtraCharge.sql")
	public void testModifyFeeInAssetSetExtraCharge(){
		AssetSet repaymentPlan = new AssetSet();
		repaymentPlan.setAssetUuid("e0fdcb1e-68cb-45c8-a1c7-82d6dca09879");
		String charString = "TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION";
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, new BigDecimal("120.00"), charString);
		Assert.assertEquals(new BigDecimal("120.00"),repaymentPlanExtraChargeService.getAssetSetExtraChargeByAssetSetUuidAndCharString("e0fdcb1e-68cb-45c8-a1c7-82d6dca09879", charString).getAccountAmount());
	   
		
		String charString1 = "TRD_DEFERRED_INCOME_LOAN_OTHER_FEE";
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, new BigDecimal("121.00"), charString1);
		Assert.assertEquals(new BigDecimal("121.00"),repaymentPlanExtraChargeService.getAssetSetExtraChargeByAssetSetUuidAndCharString("e0fdcb1e-68cb-45c8-a1c7-82d6dca09879", charString1).getAccountAmount());
	
		
		String charString2 = "TRD";
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, new BigDecimal("122.00"), charString2);
		Assert.assertEquals(null,repaymentPlanExtraChargeService.getAssetSetExtraChargeByAssetSetUuidAndCharString("e0fdcb1e-68cb-45c8-a1c7-82d6dca09879", charString2));
	}
}
