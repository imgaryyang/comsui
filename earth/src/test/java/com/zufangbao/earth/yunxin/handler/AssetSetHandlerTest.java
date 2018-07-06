package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.util.tests.AutoTestUtils;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.AuditOverdueStatus;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetQueryModel;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration()
public class AssetSetHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;

	@Before
	public void setUp() {
	}
	
	@Test
	@Sql("classpath:test/yunxin/settlementOrder/testCreateSettlementOrder.sql")
	public void testCreateOrderListByAssetSetListForEmptyList(){
		//order数没有变
		List<Order> orderList_by_assetSet1 = orderService.getOrderListByAssetSetUuid("asset_uuid_1");
		assertEquals(2,orderList_by_assetSet1.size());
	}

	@Test
	@Sql("classpath:test/yunxin/settlementOrder/TestRepaymentPlanDetailExcel.sql")
    public void testRepaymentPlanDetailExcel() {

        AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
		assetSetQueryModel.setStartDate("2016-05-17");
		assetSetQueryModel.setEndDate("2016-05-17");
		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
        assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1L, 2L)));
        assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
		//过滤掉 invalid asset
		 List<RepaymentPlanDetailExcelVO> repaymentPlanDetailExcels = repaymentPlanHandler.get_repayment_plan_Detail_excel(assetSetQueryModel,null);
		 assertEquals(2, repaymentPlanDetailExcels.size());
		 RepaymentPlanDetailExcelVO repaymentPlanDetailExcel = repaymentPlanDetailExcels.get(1);
		 assertEquals("nongfenqi", repaymentPlanDetailExcel.getAppName());
		 assertEquals("2400.00", repaymentPlanDetailExcel.getAssetInterestValue());
		 assertEquals("0.00", repaymentPlanDetailExcel.getAssetPrincipalValue());
		 assertEquals("2016-05-17", repaymentPlanDetailExcel.getAssetRecycleDate());
		 assertEquals("中国建设银行", repaymentPlanDetailExcel.getBankName());
		 assertEquals("亳州", repaymentPlanDetailExcel.getCity());
		 assertEquals("测试用户3", repaymentPlanDetailExcel.getCustomerName());
		 assertEquals("sadas", repaymentPlanDetailExcel.getFinancialAccountNo());
		 assertEquals("DCF-NFQ-LR903", repaymentPlanDetailExcel.getFinancialContractNo());
		 assertEquals(null, repaymentPlanDetailExcel.getIdCardNo());
		 assertEquals("云信信2016-78-DK(ZQ2016042522502)", repaymentPlanDetailExcel.getLoanContractNo());
		 assertEquals("2016-04-17", repaymentPlanDetailExcel.getLoanDate());
		 assertEquals("621************3015", repaymentPlanDetailExcel.getPayAcNo());
		 assertEquals("安徽省", repaymentPlanDetailExcel.getProvince());
	}
	
	@Test
	@Sql("classpath:test/yunxin/settlementOrder/testrepaymentPlanDetailExcelForWholeFinancialContractId.sql")
	public void  testrepaymentPlanDetailExcelForWholeFinancialContractId(){
	
		
		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
		assetSetQueryModel.setStartDate("2016-05-17");
		assetSetQueryModel.setEndDate("2016-08-17");
		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1L,2L)));
		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
		 List<RepaymentPlanDetailExcelVO> repaymentPlanDetailExcels = repaymentPlanHandler.get_repayment_plan_Detail_excel(assetSetQueryModel,null);
		 
		 assertEquals(3, repaymentPlanDetailExcels.size());
		 RepaymentPlanDetailExcelVO repaymentPlanDetailExcel = repaymentPlanDetailExcels.get(2);
		 assertEquals("nongfenqi", repaymentPlanDetailExcel.getAppName());
		 assertEquals("2400.00", repaymentPlanDetailExcel.getAssetInterestValue());
		 assertEquals("0.00", repaymentPlanDetailExcel.getAssetPrincipalValue());
		 assertEquals("2016-05-17", repaymentPlanDetailExcel.getAssetRecycleDate());
		 assertEquals("中国建设银行", repaymentPlanDetailExcel.getBankName());
		 assertEquals("亳州", repaymentPlanDetailExcel.getCity());
		 assertEquals("测试用户3", repaymentPlanDetailExcel.getCustomerName());
		 assertEquals("sadas", repaymentPlanDetailExcel.getFinancialAccountNo());
		 assertEquals("DCF-NFQ-LR903", repaymentPlanDetailExcel.getFinancialContractNo());
		 assertEquals(null, repaymentPlanDetailExcel.getIdCardNo());
		 assertEquals("云信信2016-78-DK(ZQ2016042522502)", repaymentPlanDetailExcel.getLoanContractNo());
		 assertEquals("2016-04-17", repaymentPlanDetailExcel.getLoanDate());
		 assertEquals("621************3015", repaymentPlanDetailExcel.getPayAcNo());
		 assertEquals("安徽省", repaymentPlanDetailExcel.getProvince());
		 
		 
		 RepaymentPlanDetailExcelVO repaymentPlanDetailExcel1 = repaymentPlanDetailExcels.get(0);
		 assertEquals("云信信2016-79-DK(ZQ2016042522502)", repaymentPlanDetailExcel1.getLoanContractNo());
	}
	
//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOverDueThanFiveRepaymentDetailExcelForAssetSetActiveStatus.sql")
//	public void testOverDueForAssetSetActiveStatus(){
//		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
//		assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
//		assetSetQueryModel.setStartDate("2016-05-17");
//		assetSetQueryModel.setEndDate("2016-05-17");
//		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
//		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
//		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(new Long(1l))));
//		List<OverDueRepaymentDetailExcelVO> overDueExcels = repaymentPlanHandler.get_overdue_than_over_due_start_day_repayment_detail_excel(assetSetQueryModel);
//		//第三条为作废，不被查出
//		assertEquals(2,overDueExcels.size());
//		assertEquals("2130.45",overDueExcels.get(0).getAssetFairValue());
//		assertEquals("2434.80",overDueExcels.get(1).getAssetFairValue());
//	}
	
//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOverDueThanFiveRepaymentDetailExcel.sql")
//	public void  testOverDueThanFiveRepaymentDetailExcel(){
//		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
//		assetSetQueryModel.setStartDate("2016-05-17");
//		assetSetQueryModel.setEndDate("2016-05-17");
//		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
//		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
//		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1L)));
//		
//		List<OverDueRepaymentDetailExcelVO> OverDueRepaymentDetailExcelVOs = repaymentPlanHandler.get_overdue_than_over_due_start_day_repayment_detail_excel(assetSetQueryModel);
//		assertEquals(2, OverDueRepaymentDetailExcelVOs.size());
//		OverDueRepaymentDetailExcelVO OverDueRepaymentDetailExcelVO = OverDueRepaymentDetailExcelVOs.get(1);
//		
//		assertEquals("nongfenqi", OverDueRepaymentDetailExcelVO.getAppName());
//		assertEquals("云信信2016-78-DK(ZQ2016042522502)", OverDueRepaymentDetailExcelVO.getLoanContractNo());
//		assertEquals("2016-04-17", OverDueRepaymentDetailExcelVO.getLoanDate());
//		assertEquals("测试用户3", OverDueRepaymentDetailExcelVO.getCustomerName());
//		assertEquals("2016-05-17", OverDueRepaymentDetailExcelVO.getAssetRecycleDate());
//		assertEquals(null, OverDueRepaymentDetailExcelVO.getActualRecycleDate());
//		assertEquals("1/2", OverDueRepaymentDetailExcelVO.getCurrentPeriod());
//		assertEquals("2434.80", OverDueRepaymentDetailExcelVO.getAssetFairValue());
//		assertEquals("0.00", OverDueRepaymentDetailExcelVO.getRefundAmount());
//		assertEquals("还款失败", OverDueRepaymentDetailExcelVO.getRepaymentStatus());
//		assertEquals("待补足", OverDueRepaymentDetailExcelVO.getGuaranteeStatus());
//		assertEquals(null, OverDueRepaymentDetailExcelVO.getComment());
//		
//	}
	
//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOverDueThanFiveRepaymentDetailExcelForNoThanFive.sql")
//	public void testOverDueThanFiveRepaymentDetailExcelForNoThanFive(){
//		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
//		assetSetQueryModel.setStartDate("2016-05-17");
//		assetSetQueryModel.setEndDate("2016-05-17");
//		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
//		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
//		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1l)));
//		List<OverDueRepaymentDetailExcelVO> OverDueRepaymentDetailExcelVOs = repaymentPlanHandler.get_overdue_than_over_due_start_day_repayment_detail_excel(assetSetQueryModel);
//		assertEquals(0, OverDueRepaymentDetailExcelVOs.size());
//	}
	
	
//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOverDueThanFiveRepaymentDetailExcelForisFive.sql")
//	public void testOverDueThanFiveRepaymentDetailExcelForisFive(){
//		
//		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
//		assetSetQueryModel.setStartDate("2016-05-17");
//		assetSetQueryModel.setEndDate("2016-05-17");
//		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
//		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
//		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1L)));
//		assetSetQueryModel.setFinancialContractsMap(financialContractsMap);
//		List<OverDueRepaymentDetailExcelVO> OverDueRepaymentDetailExcelVOs = repaymentPlanHandler.get_overdue_than_over_due_start_day_repayment_detail_excel(assetSetQueryModel);
//		assertEquals(1, OverDueRepaymentDetailExcelVOs.size());
//		
//		
//		OverDueRepaymentDetailExcelVO OverDueRepaymentDetailExcelVO = OverDueRepaymentDetailExcelVOs.get(0);
//		assertEquals("nongfenqi", OverDueRepaymentDetailExcelVO.getAppName());
//		assertEquals("云信信2016-78-DK(ZQ2016042522502)", OverDueRepaymentDetailExcelVO.getLoanContractNo());
//		assertEquals("2016-04-17", OverDueRepaymentDetailExcelVO.getLoanDate());
//		assertEquals("测试用户3", OverDueRepaymentDetailExcelVO.getCustomerName());
//		assertEquals("2016-05-17", OverDueRepaymentDetailExcelVO.getAssetRecycleDate());
//		assertEquals("2016-05-22", OverDueRepaymentDetailExcelVO.getActualRecycleDate());
//		assertEquals("2/2", OverDueRepaymentDetailExcelVO.getCurrentPeriod());
//		assertEquals(5, OverDueRepaymentDetailExcelVO.getDaysDifference());
//		assertEquals("2130.45", OverDueRepaymentDetailExcelVO.getAssetFairValue());
//		assertEquals("0.00", OverDueRepaymentDetailExcelVO.getRefundAmount());
//		assertEquals("还款成功", OverDueRepaymentDetailExcelVO.getRepaymentStatus());
//		assertEquals("已补足", OverDueRepaymentDetailExcelVO.getGuaranteeStatus());
//		assertEquals(null, OverDueRepaymentDetailExcelVO.getComment());
//	}
	
//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOverDueThanFiveRepaymentDetailExcelForNotPaidOffThanFiveDay.sql")
//	public void testOverDueThanFiveRepaymentDetailExcelForNotPaidOffThanFiveDay(){
//		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
//		assetSetQueryModel.setStartDate("2016-05-17");
//		assetSetQueryModel.setEndDate("2016-05-17");
//		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
//		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
//		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1l)));
//		List<OverDueRepaymentDetailExcelVO> OverDueRepaymentDetailExcelVOs = repaymentPlanHandler.get_overdue_than_over_due_start_day_repayment_detail_excel(assetSetQueryModel);
//		assertEquals(1, OverDueRepaymentDetailExcelVOs.size());
//		
//		OverDueRepaymentDetailExcelVO OverDueRepaymentDetailExcelVO = OverDueRepaymentDetailExcelVOs.get(0);
//		assertEquals("nongfenqi", OverDueRepaymentDetailExcelVO.getAppName());
//		assertEquals("云信信2016-78-DK(ZQ2016042522502)", OverDueRepaymentDetailExcelVO.getLoanContractNo());
//		assertEquals("2016-04-17", OverDueRepaymentDetailExcelVO.getLoanDate());
//		assertEquals("测试用户3", OverDueRepaymentDetailExcelVO.getCustomerName());
//		assertEquals("2016-05-17", OverDueRepaymentDetailExcelVO.getAssetRecycleDate());
//		assertEquals(null, OverDueRepaymentDetailExcelVO.getActualRecycleDate());
//		assertEquals("1/2", OverDueRepaymentDetailExcelVO.getCurrentPeriod());
//		assertEquals("2434.80", OverDueRepaymentDetailExcelVO.getAssetFairValue());
//		assertEquals("0.00", OverDueRepaymentDetailExcelVO.getRefundAmount());
//		assertEquals("还款失败", OverDueRepaymentDetailExcelVO.getRepaymentStatus());
//		assertEquals("待补足", OverDueRepaymentDetailExcelVO.getGuaranteeStatus());
//		assertEquals("逾期", OverDueRepaymentDetailExcelVO.getComment());
//		
//	}
	
//	@Test
//	@Sql("classpath:test/yunxin/settlementOrder/testOverDueThanFiveRepaymentDetailExcelForTheWholeFinancialContractId.sql")
//	public void testOverDueThanFiveRepaymentDetailExcelForTheWholeFinancialContractId(){
//		AssetSetQueryModel assetSetQueryModel =new AssetSetQueryModel();
//		assetSetQueryModel.setStartDate("2016-05-17");
//		assetSetQueryModel.setEndDate("2016-05-17");
//		assetSetQueryModel.setPaymentStatusOrdinals(AutoTestUtils.getOrdinals(PaymentStatus.values()));
//		assetSetQueryModel.setAuditOverDueStatusOrdinals(AutoTestUtils.getOrdinals(AuditOverdueStatus.values()));
//		assetSetQueryModel.setFinancialContractIds(JsonUtils.toJsonString(Arrays.asList(1l,2L)));
//		List<OverDueRepaymentDetailExcelVO> OverDueRepaymentDetailExcelVOs = repaymentPlanHandler.get_overdue_than_over_due_start_day_repayment_detail_excel(assetSetQueryModel);
//		assertEquals(3, OverDueRepaymentDetailExcelVOs.size());
//		
//		OverDueRepaymentDetailExcelVO OverDueRepaymentDetailExcelVO = OverDueRepaymentDetailExcelVOs.get(2);
//		assertEquals("nongfenqi", OverDueRepaymentDetailExcelVO.getAppName());
//		assertEquals("云信信2016-78-DK(ZQ2016042522502)", OverDueRepaymentDetailExcelVO.getLoanContractNo());
//		assertEquals("2016-04-17", OverDueRepaymentDetailExcelVO.getLoanDate());
//		assertEquals("测试用户3", OverDueRepaymentDetailExcelVO.getCustomerName());
//		assertEquals("2016-05-17", OverDueRepaymentDetailExcelVO.getAssetRecycleDate());
//		assertEquals(null, OverDueRepaymentDetailExcelVO.getActualRecycleDate());
//		assertEquals("1/2", OverDueRepaymentDetailExcelVO.getCurrentPeriod());
//		assertEquals("2434.80", OverDueRepaymentDetailExcelVO.getAssetFairValue());
//		assertEquals("0.00", OverDueRepaymentDetailExcelVO.getRefundAmount());
//		assertEquals("还款失败", OverDueRepaymentDetailExcelVO.getRepaymentStatus());
//		assertEquals("待补足", OverDueRepaymentDetailExcelVO.getGuaranteeStatus());
//		assertEquals(null, OverDueRepaymentDetailExcelVO.getComment());
//		
//		OverDueRepaymentDetailExcelVO OverDueRepaymentDetailExcelVO1 = OverDueRepaymentDetailExcelVOs.get(0);
//		assertEquals("nongfenqi", OverDueRepaymentDetailExcelVO1.getAppName());
//		assertEquals("云信信2016-79-DK(ZQ2016042522502)", OverDueRepaymentDetailExcelVO1.getLoanContractNo());
//		
//	}
	
}
