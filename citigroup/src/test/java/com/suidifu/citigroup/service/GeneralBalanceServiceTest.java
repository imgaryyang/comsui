package com.suidifu.citigroup.service;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.suidifu.citigroup.entity.GeneralBalanceSqlMode;

import citigroup.service.BaseTest;

public class GeneralBalanceServiceTest extends BaseTest {

	@Autowired
	private GeneralBalanceService generalBalanceService;
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-general-balance.sql")
	public void testSaveGeneralBalance(){
		GeneralBalanceSqlMode GeneralBalanceSqlMode = new GeneralBalanceSqlMode();
		GeneralBalanceSqlMode.setBankSavingFreeze(new BigDecimal(60));
		GeneralBalanceSqlMode.setPayAble(new BigDecimal(-120));
		GeneralBalanceSqlMode.setBankSavingLoan(new BigDecimal(60));
	
		generalBalanceService.saveGeneralBalance(null);
		
		System.out.println(GeneralBalanceSqlMode);
	}
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-general-balance.sql")
	public void testIncreaseBankSavingFreeze(){
		
		String financialContractUuid = "1111111111";
//		GeneralBalanceSqlMode generalBalanceSqlMode = generalBalanceService.getRemittanceSqlModeBy(financialContractUuid);
		
		generalBalanceService.updateBankSavingFreezeForFreezing(financialContractUuid, new BigDecimal(60));
		
	}
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-general-balance.sql")
	public void testDeduceBankSavingFreeze(){
		
		String financialContractUuid = "1111111111";
//		GeneralBalanceSqlMode generalBalanceSqlMode = generalBalanceService.getRemittanceSqlModeBy(financialContractUuid);
		
		generalBalanceService.updateBankSavingFreezeForFreezing(financialContractUuid, new BigDecimal(-60));
		
	}
	
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-general-balance.sql")
	public void testGetRemittanceSqlModeBy(){
		
		String financialContractUuid = "1111111111";
		
		GeneralBalanceSqlMode generalBalanceSqlMode = generalBalanceService.getGeneralBalanceSqlModeBy(financialContractUuid);
		
		Assert.assertEquals(null!=generalBalanceSqlMode, true);
		
	}
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-general-balance.sql")
	public void testUpdatePayAbleAndBankSavingLoan(){
		
		String financialContractUuid = "1111111111";
		
		generalBalanceService.updatePayAbleAndBankSavingLoan(financialContractUuid, new BigDecimal(60));
		
	}
	
}
