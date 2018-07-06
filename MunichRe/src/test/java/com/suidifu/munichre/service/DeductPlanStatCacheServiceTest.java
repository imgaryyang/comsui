package com.suidifu.munichre.service;

import com.suidifu.munichre.MunichRe;
import com.suidifu.munichre.TestMunichRe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;



//@ActiveProfiles(profiles={"dev"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MunichRe.class })
@WebIntegrationTest
public class DeductPlanStatCacheServiceTest{
	@Resource(name="deductPlanStatCacheService")
	private DeductPlanStatCacheService deductPlanStatCacheService;
	
	@Resource(name="remittancePlanStatCacheService")
	private RemittancePlanStatCacheService remittancePlanStatCacheService;
	@Resource(name="contractStatCacheService")
	private ContractStatCacheService contractStatCacheService;

	private static Log syncLog = LogFactory.getLog("syncLog");
	private static Log repaymentLog = LogFactory.getLog("repaymentLog");

	@Test
	public void testSyncLog(){

		syncLog.info("test sync log");

		repaymentLog.info("test repayment log");
	}

	@Test
	@Sql("")
	public void test_exists_deduct_plan_stat_cache_interval(){
		try{
			String financialContractUuid ="financialContractUuid";
			String paymentChanelUuid = "paymentChanelUuid";
			String pgClearingAccount = "pgClearingAccount";// maybe null;
			Date startTime = null;
			Date endTime = null;
			String uuid = deductPlanStatCacheService.get_deduct_plan_stat_cache_interval_uuid(financialContractUuid, paymentChanelUuid, pgClearingAccount, endTime);
			assertEquals("",uuid);
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void test_aaa() {
		
		System.out.println("aaaaa");
		
		deductPlanStatCacheService.getDetail("xxx");
	}
	
	@Test
	public void test_remittance_plan_stat_cache_interval_uuid() {
		remittancePlanStatCacheService.get_remittance_plan_stat_cache_interval_uuid("xxx", "xxx", new Date());
	}
	
	@Test
	public void test_update_count_amount() {
		remittancePlanStatCacheService.update_count_amount("xxx", BigDecimal.ZERO, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	@Test
	public void test_create_remittance_plan_stat_cache() {
		remittancePlanStatCacheService.create_remittance_plan_stat_cache("xxx", "xxx", 0, new Date(), BigDecimal.ZERO, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	@Test
	public void test_get_loan_service_fee() {
		List<String> assetSetUuids = new ArrayList<>();
		assetSetUuids.add("621bb72f-fc00-478f-a33f-ce520a5d7f6f");
		BigDecimal aaa= contractStatCacheService.getLoanServiceFee(assetSetUuids);
		System.out.println(aaa);
	}
	
	@Test
	public void test_bbb() {
		contractStatCacheService.create_contract_stat_cache("xxx", new Date(), BigDecimal.ZERO, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	@Test
	public void test_update() {
		contractStatCacheService.update_count_amount("xxx", BigDecimal.ZERO, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
}
