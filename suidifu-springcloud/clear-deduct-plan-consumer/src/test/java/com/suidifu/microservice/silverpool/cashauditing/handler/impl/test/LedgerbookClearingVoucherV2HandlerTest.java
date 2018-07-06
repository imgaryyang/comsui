package com.suidifu.microservice.silverpool.cashauditing.handler.impl.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.transform.ToListResultTransformer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.suidifu.microservice.handler.LedgerBookClearingVoucherV2Handler;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.service.LedgerItemServiceV2;
import com.zufangbao.sun.ledgerbookv2.utils.GeneralAccountTemplateHelperForTest;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LedgerbookClearingVoucherV2HandlerTest {

	@Autowired
	private BankAccountCache bankAccountCache;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private CashFlowService cashFlowService;
	
	@Autowired
	private LedgerItemService ledgerItemService;
	
	@Autowired
	private LedgerItemServiceV2 ledgerItemServiceV2;
	
	@Autowired
	private LedgerBookClearingVoucherV2Handler ledgerBookClearingVoucherV2Handler;
	
	@Autowired
	private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;
	
	@Autowired
	private LedgerBookService ledgerBookService;
	
	@Before
	public void setUp(){
		
		generalAccountTemplateHelperForTest.executeSql("test/yunxin/entry_book.sql");
		
//		generalAccountTemplateHelperForTest.prepareEntryBookSql();
		
		generalAccountTemplateHelperForTest.deleteAccountTemplateAndScenarion();
	}
	
	@Test
	@Sql("classpath:test/yunxin/test_ledger_book_clearing_voucher.sql")
	public void testClearingVoucherWriteOff2() {
		
		String ledgerBookNo = "ledger_book_no";
		
		generalAccountTemplateHelperForTest.createTemplateBy(ledgerBookNo, EventType.CLEARING_VOUCHER_WRITE_OFF);
		
		
		String deductApplicationUuid = "deduct_application_uuid_1";
		
		FinancialContract financialContract = financialContractService.getFinancialContractBy("financial_contract_uuid");
		
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(financialContract);
		
		LedgerTradeParty party = counterPartyAccount.getDeopsite_account_owner_party();
		
		List<LedgerItem> items =  ledgerItemServiceV2.getLedgerItemListBy(ledgerBookNo);
		
		ledgerBookClearingVoucherV2Handler.clearing_voucher_write_off(ledgerBookNo, deductApplicationUuid, party, "jjjj", 
				"bbbbb", "ssss", counterPartyAccount, cashFlow);
		
	    LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
	    List<LedgerItem> itemList =   ledgerItemServiceV2.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING),book, "asset_uuid_1" );
//	    itemList.removeAll(items);
    
	    List<String> newItems =  items.stream().map(p -> p.getLedgerUuid()).collect(Collectors.toList());
	    
	    List<LedgerItem> itemLists = itemList.stream().filter(p -> !newItems.contains(p.getLedgerUuid())).collect(Collectors.toList());
    
//	    assertEquals(itemList.get(1).getLedgerUuid(),itemList.get(0).getBackwardLedgerUuid());
//	    assertEquals(itemList.get(0).getLedgerUuid(),itemList.get(1).getForwardLedgerUuid());
	    
	    assertEquals(ledgerBookNo,itemLists.get(1).getLedgerBookNo());
	    assertEquals("bbbbb",itemLists.get(1).getBusinessVoucherUuid());
	    assertEquals("ssss",itemLists.get(1).getSourceDocumentUuid());
	    assertEquals("jjjj",itemLists.get(1).getJournalVoucherUuid());
	    assertEquals("asset_uuid_1",itemLists.get(1).getRelatedLv1AssetUuid());
		
		assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		
		
	}
	
}
