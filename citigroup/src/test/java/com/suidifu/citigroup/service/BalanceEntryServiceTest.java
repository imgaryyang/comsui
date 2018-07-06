package com.suidifu.citigroup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import com.suidifu.citigroup.entity.BalanceEntrySqlMode;

import citigroup.service.BaseTest;



@Rollback
public class BalanceEntryServiceTest extends BaseTest{

	
	@Autowired
	@Qualifier("balanceEntryService")
	private BalanceEntryService balanceEntryService;
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-balance-entry.sql")
	public void testGetRemittanceSqlModeBy() {

		String combinePreventReption = "hgtyfghfghghd";
		
		BalanceEntrySqlMode balanceEntrySqlMode = balanceEntryService.getRemittanceSqlModeBy(null, combinePreventReption);
		
		System.out.println(balanceEntrySqlMode);
		
	}
	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-balance-entry.sql")
	public void testSaveBalanceEntry(){
		
		List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<BalanceEntrySqlMode>();
 		
		BalanceEntrySqlMode balanceEntry  = new BalanceEntrySqlMode();
		balanceEntry.setUuid("adfafafadfafafdaffd");
		balanceEntry.setGeneralBalanceUuid("adfafdf3333");
		balanceEntry.setCombinePreventRepetition("adfadfadfadf");
		balanceEntry.setBookInDate(new Date());
		balanceEntry.setDebitBalance(new BigDecimal(50));
		balanceEntry.setCreditBalance(new BigDecimal(-50));
		balanceEntrySqlModes.add(balanceEntry);

		BalanceEntrySqlMode balanceEntry1  = new BalanceEntrySqlMode();
		balanceEntry.setUuid("adfafafadfafafdaffdeeee");
		balanceEntry.setGeneralBalanceUuid("adfafdf44444");
		balanceEntry1.setCombinePreventRepetition("adfadfadfadfe");
		balanceEntry1.setBookInDate(new Date());
		balanceEntry1.setDebitBalance(new BigDecimal(50));
		balanceEntry1.setCreditBalance(new BigDecimal(-50));
		balanceEntrySqlModes.add(balanceEntry1);
		try {
			balanceEntryService.saveBalanceEntry(balanceEntrySqlModes);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(balanceEntry);
		
	}

	
	@Test
	@Sql("classpath:sql/remittance-balance/remittance-balance-entry.sql")
	public void testGetRemittanceSqlModeBy111() {

		String combinePreventReption = "266c5769-c503-423c-a8ff-fac2a6e4c9a5";
		
		boolean b = balanceEntryService.existFreezIngBalanceEntry(combinePreventReption);
		
		System.out.println(b);
		
	}
	
	
	
}
