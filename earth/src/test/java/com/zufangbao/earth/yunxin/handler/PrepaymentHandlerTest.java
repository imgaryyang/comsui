package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.persistence.support.Filter;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.PlanType;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanType;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentStatus;
import com.zufangbao.sun.yunxin.entity.model.api.PrepaymentModifyModel;
import com.zufangbao.sun.yunxin.entity.model.prepayment.PrepaymentCreateAssetInfoModel;
import com.zufangbao.sun.yunxin.entity.model.prepayment.PrepaymentCreateModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.PrepaymentHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })

@Transactional

@Rollback
public class PrepaymentHandlerTest {
	
	@Autowired
	PrepaymentHandler prepaymentHandler;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	RepaymentPlanService repaymentPlanService;
	
	@Autowired
	PrepaymentApplicationService prepaymentApplicationService;
	
	@Autowired
	RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
	
	
	/*************** test prepayment start ****************/
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_prepayment() {
		try {
			Contract contract = contractService.getContract(1l);
			String ipAddress = "127.0.0.1";
			PrepaymentModifyModel model = new PrepaymentModifyModel();
			model.setUniqueId("test_unique_id");
			model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
			model.setRequestNo("test_request_no");
			model.setAssetInitialValue("37200.00");
			model.setAssetPrincipal("30000.00");
			model.setAssetInterest("2000.00");
			model.setOtherCharge("100.00");
			model.setType(0);
			model.setServiceCharge("100.00");
			model.setMaintenanceCharge("100.00");
			model.setPayWay(3);
			//prepaymentHandler.prepayment(contract, model, ipAddress);
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
			AssetSet prepaymentplan = repaymentPlanService.load(AssetSet.class, prepaymentApplications.get(0).getAssetSetId());
			
			Assert.assertEquals(DateUtils.getToday(), prepaymentplan.getAssetRecycleDate());
			Assert.assertEquals(new BigDecimal("30000.00"), prepaymentplan.getAssetPrincipalValue());
			Assert.assertEquals(new BigDecimal("2000.00"), prepaymentplan.getAssetInterestValue());
			Assert.assertEquals(AssetSetActiveStatus.OPEN, prepaymentplan.getActiveStatus());
			Assert.assertEquals(RepaymentPlanType.PREPAYMENT, prepaymentplan.getRepaymentPlanType());
			Assert.assertEquals(PlanType.PREPAYMENT	, prepaymentplan.getPlanType());
			Assert.assertEquals(AssetSetActiveStatus.OPEN, prepaymentplan.getActiveStatus());
			Assert.assertEquals(contract.getPeriods(), prepaymentplan.getCurrentPeriod());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	//@Ignore("不明原因，等待后续修复")
	public void test_prepayment_wrongDeductionStatus() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(1);
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("处理中的还款计划扣款状态与请求的状态不匹配", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_prepayment_contractNotExisted() {
		//Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate("2017-1-1");
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(-1);
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment("noresult", model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("贷款合同不存在!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_prepayment_wrongDate() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate("2018-01-01");//错误的日期
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("33200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("提前还款日期错误!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	//@Ignore("不明原因，等待后续修复")
	public void test_prepayment_amountInvalid() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("47200.00");//错误的还款金额
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("提前还款总金额或本金错误!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_localProcessingRepaymentPlanExisted1() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(0);
		model.setPayWay(0);
		prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		AssetSet prepaymentplan = repaymentPlanService.load(AssetSet.class, prepaymentApplications.get(0).getAssetSetId());
		
		Assert.assertEquals(DateUtils.getToday(), prepaymentplan.getAssetRecycleDate());
		Assert.assertEquals(new BigDecimal("30000.00"), prepaymentplan.getAssetPrincipalValue());
		Assert.assertEquals(new BigDecimal("2000.00"), prepaymentplan.getAssetInterestValue());
		Assert.assertEquals(RepaymentPlanType.PREPAYMENT, prepaymentplan.getRepaymentPlanType());
		Assert.assertEquals(PlanType.PREPAYMENT	, prepaymentplan.getPlanType());
		
		Assert.assertEquals(AssetSetActiveStatus.OPEN, prepaymentplan.getActiveStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_localProcessingRepaymentPlanExisted2() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("24800.00");
		model.setAssetPrincipal("20000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(0);
		model.setPayWay(0);
		prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		AssetSet prepaymentplan = repaymentPlanService.load(AssetSet.class, prepaymentApplications.get(0).getAssetSetId());
		
		Assert.assertEquals(DateUtils.addDays(DateUtils.getToday(), 1), prepaymentplan.getAssetRecycleDate());
		Assert.assertEquals(new BigDecimal("20000.00"), prepaymentplan.getAssetPrincipalValue());
		Assert.assertEquals(new BigDecimal("2000.00"), prepaymentplan.getAssetInterestValue());
		Assert.assertEquals(RepaymentPlanType.PREPAYMENT, prepaymentplan.getRepaymentPlanType());
		Assert.assertEquals(PlanType.PREPAYMENT	, prepaymentplan.getPlanType());
		
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentplan.getActiveStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_localProcessingRepaymentPlanExisted_wrongDeductionStatus() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(1);
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("处理中的还款计划扣款状态与请求的状态不匹配", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_localProcessingRepaymentPlanExisted_amountInvalid() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("47200.00");//错误的还款金额
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(0);
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("提前还款总金额或本金错误!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_localProcessingRepaymentPlanExisted_wrongDate() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate("2020-6-1");//错误的日期
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(0);
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("提前还款日期错误!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_overdueRepaymentPlanExisted.sql")
	public void test_prepayment_overdueRepaymentPlanExisted() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("23000.00");
		model.setAssetPrincipal("20000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		AssetSet prepaymentplan = repaymentPlanService.load(AssetSet.class, prepaymentApplications.get(0).getAssetSetId());
		
		Assert.assertEquals(DateUtils.getToday(), prepaymentplan.getAssetRecycleDate());
		Assert.assertEquals(new BigDecimal("20000.00"), prepaymentplan.getAssetPrincipalValue());
		Assert.assertEquals(new BigDecimal("2000.00"), prepaymentplan.getAssetInterestValue());
		Assert.assertEquals(RepaymentPlanType.PREPAYMENT, prepaymentplan.getRepaymentPlanType());
		Assert.assertEquals(PlanType.PREPAYMENT	, prepaymentplan.getPlanType());
		
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentplan.getActiveStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_oppositeProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_oppositeProcessingRepaymentPlanExisted() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("23000.00");
		model.setAssetPrincipal("20000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		//model.setHasDeducted(1);
		model.setPayWay(0);
		prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		AssetSet prepaymentplan = repaymentPlanService.load(AssetSet.class, prepaymentApplications.get(0).getAssetSetId());
		
		Assert.assertEquals(DateUtils.getToday(), prepaymentplan.getAssetRecycleDate());
		Assert.assertEquals(new BigDecimal("20000.00"), prepaymentplan.getAssetPrincipalValue());
		Assert.assertEquals(new BigDecimal("2000.00"), prepaymentplan.getAssetInterestValue());
		Assert.assertEquals(RepaymentPlanType.PREPAYMENT, prepaymentplan.getRepaymentPlanType());
		Assert.assertEquals(PlanType.PREPAYMENT	, prepaymentplan.getPlanType());
		
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentplan.getActiveStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_oppositeProcessingRepaymentPlanExisted.sql")
	public void test_prepayment_oppositeProcessingRepaymentPlanExisted_wrongDeductionStatus() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("23000.00");
		model.setAssetPrincipal("20000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setHasDeducted(2);
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("处理中的还款计划扣款状态与请求的状态不匹配", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_noAvailableAssetSet.sql")
	public void test_prepayment_noAvailableAssetSet() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("不存在可进行提前还款的还款计划!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_prepaymentPlanExist.sql")
	public void test_prepayment_prepaymentPlanExist() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate("2017-1-1");
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("37200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("存在未执行的提前还款!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	/*************** test prepayment end ****************/
	
	/*************** test processingPrepaymentPlan start ****************/
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_undo_frozen_prepayment_plan_overdue_rollback.sql")
	public void test_undo_frozen_prepayment_plan_overdue_rollback() {
		AssetSet prepaymentPlan = repaymentPlanService.load(AssetSet.class, 148685l);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentPlan.getActiveStatus());
		prepaymentHandler.processingPrepaymentPlan();
		//Assert.assertEquals(AssetSetActiveStatus.INVALID, prepaymentPlan.getActiveStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_undo_frozen_prepayment_plan.sql")
	public void test_undo_frozen_prepayment_plan() {
		AssetSet prepaymentPlan = repaymentPlanService.load(AssetSet.class, 148685l);
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentPlan.getActiveStatus());
		prepaymentHandler.processingPrepaymentPlan();
		//Assert.assertEquals(AssetSetActiveStatus.OPEN, prepaymentPlan.getActiveStatus());
	}
	/*************** test processingPrepaymentPlan end ****************/
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_processingPrepaymentApplicationAfterSuccess.sql")
	public void test_processingPrepaymentApplicationAfterSuccess() {
		String repaymentPlanUuid = "56bd878c-4166-410b-aa92-87794b9848d3";
		prepaymentHandler.processingPrepaymentApplicationAfterSuccess(repaymentPlanUuid);
		PrepaymentApplication application = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(repaymentPlanUuid);
		AssetSet prepaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(repaymentPlanUuid);
		
		Assert.assertEquals(AssetSetActiveStatus.OPEN, prepaymentPlan.getActiveStatus());
		Assert.assertEquals(prepaymentPlan.getActualRecycleDate(), application.getCompletedTime());
		Assert.assertEquals(PrepaymentStatus.SUCCESS, application.getPrepaymentStatus());
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_processingPrepaymentApplicationAfterFail.sql")
	public void test_processingPrepaymentApplicationAfterFail() {
		String repaymentPlanUuid = "56bd878c-4166-410b-aa92-87794b9848d3";
		List<String> repaymentPlanUuids = new ArrayList<>();
		repaymentPlanUuids.add(repaymentPlanUuid);
		prepaymentHandler.processingPrepaymentApplicationAfterFail(repaymentPlanUuids);
		PrepaymentApplication application = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(repaymentPlanUuid);
		Assert.assertEquals(PrepaymentStatus.FAIL, application.getPrepaymentStatus());
	}
	
	/*************** test submitPrepaymentPlan start ****************/
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	//@Ignore("不明原因，等待后续修复")
	public void test_submitPrepaymentPlan_true() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate("2017-09-30");
		model.setAssetInitialValue("30000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		model.setComment("test");
		prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
		List<PrepaymentApplication> applications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		PrepaymentApplication application = applications.get(0);
		AssetSet prepaymentPlan = repaymentPlanService.load(AssetSet.class, application.getAssetSetId());
		Assert.assertEquals(new BigDecimal("30000"), prepaymentPlan.getAssetInitialValue());
		Assert.assertEquals(new BigDecimal("30000"), prepaymentPlan.getAssetPrincipalValue());
		Assert.assertEquals(DateUtils.parseDate("2017-09-30"), prepaymentPlan.getAssetRecycleDate());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_submitPrepaymentPlan_wrongDate1() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate("2017-1-1");
		model.setAssetInitialValue("30000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			//e.printStackTrace();
			Assert.assertTrue(e.getMessage().contains("提前还款日期错误"));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_submitPrepaymentPlan_wrongDate2() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate("2018-01-01");
		model.setAssetInitialValue("30000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("提前还款日期错误"));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_submitPrepaymentPlan_wrongAmount() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setAssetInitialValue("3000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
			Assert.fail();
		} catch (RuntimeException e) {
			//e.printStackTrace();
			Assert.assertTrue(e.getMessage().contains("提前还款金额错误"));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_noAvailableAssetSet.sql")
	public void test_submitPrepaymentPlan_noAvailableAssetSet() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.list(AssetSet.class, new Filter());
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate("2017-3-1");
		model.setAssetInitialValue("30000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("不存在可提前还款的还款计划"));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_prepaymentPlanExist.sql")
	public void test_submitPrepaymentPlan_prepaymentPlanExist() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate("2017-3-1");
		model.setAssetInitialValue("30000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
			Assert.fail();
		} catch (RuntimeException e) {
			//e.printStackTrace();
			Assert.assertTrue(e.getMessage().contains("存在未执行的提前还款"));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_submitPrepaymentPlan_localProcessingRepaymentPlanExisted1() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = repaymentPlanService.loadAll(AssetSet.class);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setAssetInitialValue("30000");
		model.setAssetPrincipal("30000");
		//model.setAssetInfoModelList(assetInfoModelList);
		model.setComment("test");
		prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
		List<PrepaymentApplication> applications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		PrepaymentApplication application = applications.get(0);
		AssetSet prepaymentPlan = repaymentPlanService.load(AssetSet.class, application.getAssetSetId());
		Assert.assertEquals(new BigDecimal("30000"), prepaymentPlan.getAssetInitialValue());
		Assert.assertEquals(new BigDecimal("30000"), prepaymentPlan.getAssetPrincipalValue());
		Assert.assertEquals(DateUtils.getToday(), prepaymentPlan.getAssetRecycleDate());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_submitPrepaymentPlan_localProcessingRepaymentPlanExisted2() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = new ArrayList<>();
		AssetSet assetSet1 = repaymentPlanService.load(AssetSet.class, 2l);
		AssetSet assetSet2 = repaymentPlanService.load(AssetSet.class, 3l);
		assetSets.add(assetSet1);
		assetSets.add(assetSet2);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setAssetInitialValue("20000");
		model.setAssetPrincipal("20000");
		//model.setAssetInfoModelList(assetInfoModelList);
		model.setComment("test");
		prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
		List<PrepaymentApplication> applications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		PrepaymentApplication application = applications.get(0);
		AssetSet prepaymentPlan = repaymentPlanService.load(AssetSet.class, application.getAssetSetId());
		Assert.assertEquals(new BigDecimal("20000"), prepaymentPlan.getAssetInitialValue());
		Assert.assertEquals(new BigDecimal("20000"), prepaymentPlan.getAssetPrincipalValue());
		Assert.assertEquals(DateUtils.addDays(DateUtils.getToday(), 1), prepaymentPlan.getAssetRecycleDate());
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentPlan.getActiveStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_oppositeProcessingRepaymentPlanExisted.sql")
	public void test_submitPrepaymentPlan_oppositeProcessingRepaymentPlanExisted() {
		Contract contract = contractService.getContract(1l);
		List<PrepaymentCreateAssetInfoModel> assetInfoModelList = new ArrayList<>();
		List<AssetSet> assetSets = new ArrayList<>();
		AssetSet assetSet1 = repaymentPlanService.load(AssetSet.class, 2l);
		AssetSet assetSet2 = repaymentPlanService.load(AssetSet.class, 3l);
		assetSets.add(assetSet1);
		assetSets.add(assetSet2);
		BigDecimal principal = BigDecimal.ZERO;
		for(AssetSet assetSet:assetSets) {
			Map<String, BigDecimal> assetSetExtraChargeModel = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSet.getAssetUuid());
			PrepaymentCreateAssetInfoModel assetInfoModel = new PrepaymentCreateAssetInfoModel(assetSet, assetSetExtraChargeModel);
			assetInfoModelList.add(assetInfoModel);
			principal = principal.add(assetSet.getAssetPrincipalValue());
		}
		PrepaymentCreateModel model = new PrepaymentCreateModel();
		model.setContractId(1l);
		model.setAssetRecycleDate(DateUtils.format(DateUtils.getToday()));
		model.setAssetInitialValue("20000");
		model.setAssetPrincipal("20000");
		//model.setAssetInfoModelList(assetInfoModelList);
		model.setComment("test");
		prepaymentHandler.new_prepayment(contract.getUuid(), model, "127.0.0.1", 123l, Priority.RealTime.getPriority());
		List<PrepaymentApplication> applications = prepaymentApplicationService.loadAll(PrepaymentApplication.class);
		PrepaymentApplication application = applications.get(0);
		AssetSet prepaymentPlan = repaymentPlanService.load(AssetSet.class, application.getAssetSetId());
		Assert.assertEquals(new BigDecimal("20000"), prepaymentPlan.getAssetInitialValue());
		Assert.assertEquals(new BigDecimal("20000"), prepaymentPlan.getAssetPrincipalValue());
		Assert.assertEquals(DateUtils.getToday(), prepaymentPlan.getAssetRecycleDate());
		Assert.assertEquals(AssetSetActiveStatus.FROZEN, prepaymentPlan.getActiveStatus());
	}
	/*************** test submitPrepaymentPlan end ****************/
	
	/*************** test get_be_perd_repayment_plan_and_prepayment_principal start ****************/
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_get_be_perd_repayment_plan_and_prepayment_principal() {
		Map<String, String> map = prepaymentHandler.get_be_perd_repayment_plan_and_prepayment_principal(DateUtils.format(DateUtils.getToday()), 1l);
		Assert.assertEquals("30000.00", map.get("assetPrincipal"));
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_localProcessingRepaymentPlanExisted.sql")
	public void test_get_be_perd_repayment_plan_and_prepayment_principal_2() {
		Map<String, String> map = prepaymentHandler.get_be_perd_repayment_plan_and_prepayment_principal(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)), 1l);
		Assert.assertEquals("20000.00", map.get("assetPrincipal"));
	}
	/*************** test get_be_perd_repayment_plan_and_prepayment_principal end ****************/
	
	
	
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment.sql")
	public void test_checkPrepaymentApplication() {
		Map<String, Date> dateMap = prepaymentHandler.checkPrepaymentApplication(1l);
		Assert.assertTrue(DateUtils.getToday().equals(dateMap.get("minRecycleDate")));
		Assert.assertTrue(DateUtils.parseDate("2017-10-01").equals(dateMap.get("maxRecycleDate")));
	}
	
	//存在支付中的还款计划
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_prepayment_exist_paymenting_assetset.sql")
	public void test_prepayment_exist_paymenting_status() {
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("33200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		try {
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("申请中包含支付中的还款计划", ApiMessageUtil.getMessage(e.getCode()));
		}
	}

	//商户还款计划编号重复异常(repayScheduleNoMD5值唯一性校验)
	@Test
	@Sql("classpath:test/yunxin/prepayment/test_repeat_outer_repayment_plan_no.sql")
	public void test_repeat_outer_repayment_plan_no(){
		Contract contract = contractService.getContract(1l);
		String ipAddress = "127.0.0.1";
		PrepaymentModifyModel model = new PrepaymentModifyModel();
		model.setUniqueId("test_unique_id");
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setRequestNo("test_request_no");
		model.setAssetInitialValue("33200.00");
		model.setAssetPrincipal("30000.00");
		model.setAssetInterest("2000.00");
		model.setOtherCharge("100.00");
		model.setType(0);
		model.setServiceCharge("100.00");
		model.setMaintenanceCharge("100.00");
		model.setPayWay(0);
		model.setRepayScheduleNo("outer1");
		model.setFinancialProductCode("G32000");
		try {
			System.out.println(repaymentPlanService.getRepayScheduleNoMD5("G32000", "outer1", StringUtils.EMPTY));
			prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null, Priority.High.getPriority());
			Assert.fail();
		} catch (GlobalRuntimeException e) {
			Assert.assertEquals("商户还款计划编号重复", e.getMessage());
		}
	}
	
}
