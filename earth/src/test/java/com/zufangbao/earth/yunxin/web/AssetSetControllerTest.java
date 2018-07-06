package com.zufangbao.earth.yunxin.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.web.controller.AssetSetController;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.log.MutableFeeDetailLog;
import com.zufangbao.sun.yunxin.service.ExtraChargeSnapShotService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback=true)
@WebAppConfiguration(value="webapp")
public class AssetSetControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PrincipalService principalService;
	@Autowired
	private AssetSetController assetSetController;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired 
	private ExtraChargeSnapShotService	extraChargeSnapShotService;
//	@Test
//	@Sql("classpath:test/yunxin/assets/test_asset_refund.sql")
//	public void testPreUpdateRefund(){
//		Long assetSetId = 2L;
//		ModelAndView modelAndView = assetSetController.preUpdateRefund(assetSetId);
//		assertEquals("assets/assets-pre-update-refund",modelAndView.getViewName());
//		AssetSet assetSet = (AssetSet)modelAndView.getModel().get("assetSet");
//		assertEquals(new Long(assetSetId),assetSet.getId());
//	}

	@Test
	@Sql("classpath:test/yunxin/assets/test_asset_refund.sql")
	public void testUpdateRefund(){
		//asset1未结转不可操作
		Long assetSetId = 1L;
		Principal principal = principalService.load(Principal.class, 1L);
		String comment = "comment";
		BigDecimal refundAmount = new BigDecimal("10.00");
		String resultJson = assetSetController.updateRefund(principal, assetSetId, refundAmount, comment);
		Result result = JsonUtils.parse(resultJson,Result.class);
		assertFalse(result.isValid());
		
		//asset2已结转
		assetSetId = 2L;
		principal = principalService.load(Principal.class, 1L);
		comment = "comment";
		refundAmount = new BigDecimal("10.00");
		resultJson = assetSetController.updateRefund(principal, assetSetId, refundAmount, comment);
		result = JsonUtils.parse(resultJson,Result.class);
		assertTrue(result.isValid());
		AssetSet assetSet = repaymentPlanService.load(AssetSet.class, assetSetId);
		assertTrue(StringUtils.equals(comment,assetSet.getComment()));
		assertEquals(0,refundAmount.compareTo(assetSet.getRefundAmount()));
	}
	
	
//	@Test
//	@Sql("classpath:test/yunxin/assets/testQueryOverdueFee.sql")
//	public void testQueryOverdueFee(){
//		Principal principal = new Principal();
//		principal.setCreatorId(3l);
//		Page rawpage = new Page();
//		rawpage.setBeginIndex(0);
//		rawpage.setCurrentPage(1);
//		Long assetSetId =  6221l;
//		int pageNumber = 4;
//		String resultString = assetSetController.queryOverdueFee(principal, assetSetId, rawpage, pageNumber);
//		Result result = JsonUtils.parse(resultString, Result.class);
//		Map<String,Object> map =result.getData();
//		JSONArray overdueFeeArray =  (JSONArray) map.get("list");
//		List<OverdueFeeShowModel> OverdueFeeShowModelList = JsonUtils.parseArray(JSONObject.toJSONString(overdueFeeArray),OverdueFeeShowModel.class);
//		assertEquals(4,OverdueFeeShowModelList.size());
//		assertEquals(new BigDecimal("4.00"),OverdueFeeShowModelList.get(0).getOverdueFeeOther());
//	}
	
	
	@Test
	@Sql("classpath:test/yunxin/assets/testEditOverdueCharges.sql")
	public void testEditOverdueCharges(){
		String repaymentPlanUuid = null;
		String overdueCharges = "";
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultString = assetSetController.editOverdueCharges(repaymentPlanUuid, overdueCharges, principal, request);
		Result result = JsonUtils.parse(resultString, Result.class);
		assertEquals("逾期费用更新失败，原因［还款计划不存在］", result.getMessage());
		
		//asset2作废
		repaymentPlanUuid = "5a0017c2-7521-4640-aebe-5bfdc1708d8b";
		resultString = assetSetController.editOverdueCharges(repaymentPlanUuid, overdueCharges, principal, request);
		result = JsonUtils.parse(resultString, Result.class);
		assertEquals("逾期费用更新失败，原因［非执行中的还款计划］", result.getMessage());
	    
		//asset1 activeDeductApplicationUuid不为empty_deduct_uuid
		repaymentPlanUuid = "5a0017c2-7521-4640-aebe-5bfdc1708d8a";
		resultString = assetSetController.editOverdueCharges(repaymentPlanUuid, overdueCharges, principal, request);
		result = JsonUtils.parse(resultString, Result.class);
		assertEquals("逾期费用更新失败，原因［还款计划已被锁定］", result.getMessage());
		
		
		repaymentPlanUuid = "5a0017c2-7521-4640-aebe-5bfdc1708d8c";
		resultString = assetSetController.editOverdueCharges(repaymentPlanUuid, overdueCharges, principal, request);
		result = JsonUtils.parse(resultString, Result.class);
		assertEquals("逾期费用更新失败，原因［逾期费用明细格式有误］", result.getMessage());
		
		
		overdueCharges = "{overdueFeePenalty:\"1.00\",overdueFeeObligation:\"1.00\",overdueFeeService:\"1.00\",overdueFeeOther:\"1.00\"}";
		repaymentPlanUuid = "5a0017c2-7521-4640-aebe-5bfdc1708d8c";
		resultString = assetSetController.editOverdueCharges(repaymentPlanUuid, overdueCharges, principal, request);
		result = JsonUtils.parse(resultString, Result.class);
		assertEquals("逾期费用更新失败，原因[接口传输,不允许更改逾期费用］", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/assets/testEditOverdueCharges_1.sql")
	public void testEditOverdueCharges_1(){
		String repaymentPlanUuid = null;
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String overdueCharges = "{overdueFeePenalty:\"1.00\",overdueFeeObligation:\"1.00\",overdueFeeService:\"1.00\",overdueFeeOther:\"1.00\"}";
		repaymentPlanUuid = "5a0017c2-7521-4640-aebe-5bfdc1708d8c";
		String resultString = assetSetController.editOverdueCharges(repaymentPlanUuid, overdueCharges, principal, request);
		Result result = JsonUtils.parse(resultString, Result.class);
		/*assertEquals("成功", result.getMessage());
		
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid("5a0017c2-7521-4640-aebe-5bfdc1708d8c");
		
		ExtraChargeSnapShot extraChargeSnapShot = extraChargeSnapShotService.list(ExtraChargeSnapShot.class,new Filter().addEquals("repaymentPlanNo",assetSet.getSingleLoanContractNo())).get(0);
		assertEquals(new BigDecimal("1.00"), extraChargeSnapShot.getOverdueFeeObligation());
		assertEquals(new BigDecimal("1.00"), extraChargeSnapShot.getOverdueFeeOther());
		assertEquals(new BigDecimal("1.00"), extraChargeSnapShot.getOverdueFeePenalty());
		assertEquals(new BigDecimal("1.00"), extraChargeSnapShot.getOverdueFeeService());*/
	}
	
	@Test
	@Sql("classpath:test/yunxin/mutableFee/testQueryMutableFeeDetails.sql")
	public void testQueryMutableFeeDetails(){
		Principal principal = new Principal();
		principal.setCreatorId(3L);
		String singleLoanContractNo = "ZC1561074340376481792";
		Page page = new Page(1, 2);
		String resultString = assetSetController.queryMutableFeeDetails(principal, singleLoanContractNo, page,"1","5");
		Result result = JsonUtils.parse(resultString, Result.class);

		Map<String, Object> map = result.getData();
		JSONArray mutableFeeDetailList = (JSONArray) map.get("list");
		List<MutableFeeDetailLog> mutableFeeDetailLogs = JsonUtils.parseArray(JSONObject.toJSONString(mutableFeeDetailList),MutableFeeDetailLog.class);
		assertEquals(4,mutableFeeDetailLogs.size());
		
		assertEquals(0,DateUtils.parseDate("2017-04-10 14:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-10 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getApprovedTime()));
		assertEquals("MF1492105792654",mutableFeeDetailLogs.get(0).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(0).getReasonCode());
		assertEquals(new BigDecimal("80.00"),mutableFeeDetailLogs.get(0).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(0).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 16:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(0).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(0).getComment());
		
		int last = mutableFeeDetailLogs.size()-1;
		assertEquals(0,DateUtils.parseDate("2017-04-10 12:12:40", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-08 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getApprovedTime()));
		assertEquals("MF1491797560886",mutableFeeDetailLogs.get(last).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(last).getReasonCode());
		assertEquals(new BigDecimal("20.00"),mutableFeeDetailLogs.get(last).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(last).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 12:12:40", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(last).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(last).getComment());
	}
	
	@Test
	@Sql("classpath:test/yunxin/mutableFee/testPagedQueryMutableFeeDetails.sql")
	public void testPagedQueryMutableFeeDetails(){
		Principal principal = new Principal();
		principal.setCreatorId(3L);
		Page page = new Page(1, 2);
		String singleLoanContractNo = "ZC1561074340376481792";
		String resultString = assetSetController.queryMutableFeeDetails(principal, singleLoanContractNo, page,"1","2");
		Result result = JsonUtils.parse(resultString, Result.class);

		Map<String, Object> map = result.getData();
		JSONArray mutableFeeDetailList = (JSONArray) map.get("list");
		List<MutableFeeDetailLog> mutableFeeDetailLogs = JsonUtils.parseArray(JSONObject.toJSONString(mutableFeeDetailList),MutableFeeDetailLog.class);
		assertEquals(2,mutableFeeDetailLogs.size());
		
		assertEquals(0,DateUtils.parseDate("2017-04-10 14:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-10 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getApprovedTime()));
		assertEquals("MF1492105792654",mutableFeeDetailLogs.get(0).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(0).getReasonCode());
		assertEquals(new BigDecimal("80.00"),mutableFeeDetailLogs.get(0).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(0).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 16:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(0).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(0).getComment());
		
		int last = mutableFeeDetailLogs.size()-1;
		assertEquals(0,DateUtils.parseDate("2017-04-10 14:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-10 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getApprovedTime()));
		assertEquals("MF1491905792280",mutableFeeDetailLogs.get(last).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(last).getReasonCode());
		assertEquals(new BigDecimal("80.00"),mutableFeeDetailLogs.get(last).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(last).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 15:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(last).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(last).getComment());
	}
	
	@Test
	@Sql("classpath:test/yunxin/mutableFee/testQueryMutableFeeDetailsFailed.sql")
	public void testQueryMutableFeeDetailsFailed() {
		Principal principal = new Principal();
		principal.setCreatorId(3L);
		String singleLoanContractNo = null;
		Page page = new Page(1, 2);
		String resultString = assetSetController.queryMutableFeeDetails(principal, singleLoanContractNo, page,"1","5");
		Result result = JsonUtils.parse(resultString, Result.class);
		assertEquals("浮动费用明细查询费用错误", result.getMessage());
		assertEquals("-1", result.getCode());
	}
}
