package com.suidifu.bridgewater.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.zufangbao.sun.api.model.deduct.OverDueFeeDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentType;

public class BatchDeductItemHelperTest {
	
	private String buildBatchDeductItemJson() {
		
		Map<String,Object> map = new HashMap<>();;
		
		map.put("deductId","33175be0-8a7d-4aef-89e0-3c32834d5812");
		
		map.put("financialProductCode", "11111111");
		map.put("uniqueId", "702f0dd5-ebca-4073-a201-03be75775e27");
		map.put("contractNo", "TEST-CONTRACT-001");
		map.put("transType", 1);
		map.put("amount", new BigDecimal("200"));
		map.put("repaymentType", 1);
		map.put("mobile", "13732255443");
		map.put("accountName", "xx111111");
		map.put("accountNo", "6214855712109920");
		map.put("gateway", "1");
		
		
		Map<String,Object> repaymentDetailsMap = new HashMap<String,Object>();
		
		repaymentDetailsMap.put("repaymentAmount", new BigDecimal(200));
		repaymentDetailsMap.put("repaymentPrincipal", new BigDecimal(100));
		repaymentDetailsMap.put("repaymentInterest", new BigDecimal(200));
		repaymentDetailsMap.put("loanFee", new BigDecimal(1));
		repaymentDetailsMap.put("techFee", new BigDecimal(2));
		repaymentDetailsMap.put("otherFee", new BigDecimal(3));
		repaymentDetailsMap.put("currentPeriod", 1);
		
		Map<String,Object> overDueFeeDetail = new HashMap<String,Object>();
		
		overDueFeeDetail.put("totalOverdueFee", new BigDecimal(100));
		overDueFeeDetail.put("lateFee", new BigDecimal(3));
		overDueFeeDetail.put("lateOtherCost", new BigDecimal(4));
		overDueFeeDetail.put("penaltyFee", new BigDecimal(5));
		overDueFeeDetail.put("latePenalty", new BigDecimal(6));
		
		List<Map<String,Object>> repaymentDetailsList  = new ArrayList<>();
		
		repaymentDetailsMap.put("overDueFeeDetail",overDueFeeDetail);
		repaymentDetailsList.add(repaymentDetailsMap);
		
		map.put("repaymentDetails",repaymentDetailsList);
		
		return (JsonUtils.toJsonString(map));
	}
	
	@Test
	public void printJsonForBatchItemDeduct() {
		
		Map<String,Object> map = new HashMap<>();;
		
		map.put("deductId","33175be0-8a7d-4aef-89e0-3c32834d5812");
		
		map.put("financialProductCode", "11111111");
		map.put("uniqueId", "702f0dd5-ebca-4073-a201-03be75775e27");
		map.put("contractNo", "TEST-CONTRACT-001");
		map.put("transType", 1);
		map.put("amount", new BigDecimal("200"));
		map.put("repaymentType", 1);
		map.put("mobile", "13732255400");
		map.put("accountName", "范腾1111111");
		map.put("accountNo", "6214855712106564");
		map.put("gateway", "1");
		
		
		Map<String,Object> repaymentDetailsMap = new HashMap<String,Object>();
		
		repaymentDetailsMap.put("repaymentAmount", new BigDecimal(200));
		repaymentDetailsMap.put("repaymentPrincipal", new BigDecimal(100));
		repaymentDetailsMap.put("repaymentInterest", new BigDecimal(200));
		repaymentDetailsMap.put("loanFee", new BigDecimal(1));
		repaymentDetailsMap.put("techFee", new BigDecimal(2));
		repaymentDetailsMap.put("otherFee", new BigDecimal(3));
		repaymentDetailsMap.put("currentPeriod", 1);
		
		Map<String,Object> overDueFeeDetail = new HashMap<String,Object>();
		
		overDueFeeDetail.put("totalOverdueFee", new BigDecimal(100));
		overDueFeeDetail.put("lateFee", new BigDecimal(3));
		overDueFeeDetail.put("lateOtherCost", new BigDecimal(4));
		overDueFeeDetail.put("penaltyFee", new BigDecimal(5));
		overDueFeeDetail.put("latePenalty", new BigDecimal(6));
		
		List<Map<String,Object>> repaymentDetailsList  = new ArrayList<>();
		
		repaymentDetailsMap.put("overDueFeeDetail",overDueFeeDetail);
		repaymentDetailsList.add(repaymentDetailsMap);
		
		map.put("repaymentDetails",repaymentDetailsList);
		
		System.out.println (JsonUtils.toJsonString(map));
	}

	@Test
	public void testBuildBatchDeductItemStringInt() {
		
		String batchDeductItemString = buildBatchDeductItemJson();
		
		int lineNumber = 0;
		
		BatchDeductItem  batchDeductItem = BatchDeductItemHelper.buildBatchDeductItem(batchDeductItemString, lineNumber);
		
		assertEquals("33175be0-8a7d-4aef-89e0-3c32834d5812",batchDeductItem.getDeductId());
		assertEquals("11111111",batchDeductItem.getFinancialProductCode());
		assertEquals("702f0dd5-ebca-4073-a201-03be75775e27",batchDeductItem.getUniqueId());
		assertEquals("TEST-CONTRACT-001",batchDeductItem.getContractNo());
		Integer transType = 1;
		assertEquals(transType,batchDeductItem.getTransType());
		assertEquals(0,new BigDecimal("200").compareTo(batchDeductItem.getAmount()));
		assertEquals("13732255443",batchDeductItem.getMobile());
		assertEquals("xx111111",batchDeductItem.getAccountName());
		assertEquals("6214855712109920",batchDeductItem.getAccountNo());
		assertEquals("1",batchDeductItem.getGateway());
		
		List<RepaymentDetail> repaymentDetails = batchDeductItem.getRepaymentDetails();
		
		assertEquals(1,repaymentDetails.size());
		
		RepaymentDetail repaymentDetail1 = repaymentDetails.get(0);
		
		assertEquals(0,new BigDecimal(200).compareTo(repaymentDetail1.getRepaymentAmount()));
		assertEquals(0,new BigDecimal(200).compareTo(repaymentDetail1.getRepaymentInterest()));
		assertEquals(0,new BigDecimal(100).compareTo(repaymentDetail1.getRepaymentPrincipal()));
		assertEquals(0,new BigDecimal(1).compareTo(repaymentDetail1.getLoanFee()));
		assertEquals(0,new BigDecimal(3).compareTo(repaymentDetail1.getOtherFee()));
		assertEquals(0,new BigDecimal(2).compareTo(repaymentDetail1.getTechFee()));
	
		assertEquals(new Integer("1"),new Integer(repaymentDetail1.getCurrentPeriod()));
		
		
		OverDueFeeDetail overDueFeeDetail = repaymentDetail1.getOverDueFeeDetail();
		
		assertEquals(0,new BigDecimal(100).compareTo(overDueFeeDetail.getTotalOverdueFee()));
		assertEquals(0,new BigDecimal(3).compareTo(overDueFeeDetail.getLateFee()));
		assertEquals(0,new BigDecimal(4).compareTo(overDueFeeDetail.getLateOtherCost()));
		assertEquals(0,new BigDecimal(5).compareTo(overDueFeeDetail.getPenaltyFee()));
		assertEquals(0,new BigDecimal(6).compareTo(overDueFeeDetail.getLatePenalty()));
		
	}

	@Test
	public void testBuildBatchDeductItemMapOfStringString() {
		fail("Not yet implemented");
		
		
	}

	@Test
	public void testBuildBatchDeductItemByModel() {
		fail("Not yet implemented");
	}

}
