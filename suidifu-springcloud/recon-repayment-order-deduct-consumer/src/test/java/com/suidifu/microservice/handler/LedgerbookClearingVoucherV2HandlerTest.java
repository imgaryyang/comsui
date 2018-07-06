package com.suidifu.microservice.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.demo2do.core.persistence.support.Filter;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(value=false)
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
	private LedgerBookClearingVoucherHandler ledgerBookClearingVoucherHandler;


	@Autowired
	private LedgerBookService ledgerBookService;
	
	@Before
	public void setUp(){
		
		generalAccountTemplateHelperForTest.executeSql("test/yunxin/ledger_book/handlerTest/entry_book.sql");
		
		generalAccountTemplateHelperForTest.prepareEntryBookSql();
		
		generalAccountTemplateHelperForTest.deleteAccountTemplateAndScenarion();
	}
	
	@Test
	@Sql("classpath:test/yunxin/ledger_book/handlerTest/test_ledger_book_clearing_voucher_v2.sql")
	public void testClearingVoucherWriteOff2() {
		
		String ledgerBookNo = "ledger_book_no";
		
		generalAccountTemplateHelperForTest.createTemplateBy(ledgerBookNo, EventType.CLEARING_VOUCHER_WRITE_OFF);
		
		
		String deductApplicationUuid = "deduct_application_uuid_1";
		
		FinancialContract financialContract = financialContractService.getFinancialContractBy("financial_contract_uuid");
		
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(financialContract);
		
		LedgerTradeParty party = counterPartyAccount.getDeopsite_account_owner_party();
		
		List<LedgerItem> items =  ledgerItemService.list(LedgerItem.class,new Filter());

		ledgerBookClearingVoucherHandler.clearing_voucher_write_off(ledgerBookNo, deductApplicationUuid, party, "jjjj",
				"bbbbb", "ssss", counterPartyAccount, cashFlow);

	    LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
	    List<LedgerItem> itemList =  ledgerItemService.get_all_ledgers_of_asset_in_taccount(book, "asset_uuid_1", ChartOfAccount.FST_BANK_SAVING);
	    itemList.removeAll(items);
	    assertEquals(16,itemList.size());
			Map<String,LedgerItem> itemMap = itemList.stream().collect(Collectors.toMap(LedgerItem::getLedgerUuid, Function.identity()));
			for(LedgerItem item:itemList){
				assertTrue(itemMap.get(item.getForwardLedgerUuid())!=null ||itemMap.get(item.getBackwardLedgerUuid())!=null);
			}

	    assertEquals(itemList.get(0).getLedgerUuid(),itemList.get(8).getBackwardLedgerUuid());
	    assertEquals(itemList.get(8).getLedgerUuid(),itemList.get(0).getForwardLedgerUuid());
	    
	    assertEquals(ledgerBookNo,itemList.get(0).getLedgerBookNo());
	    assertEquals("bbbbb",itemList.get(0).getBusinessVoucherUuid());
	    assertEquals("ssss",itemList.get(0).getSourceDocumentUuid());
	    assertEquals("jjjj",itemList.get(0).getJournalVoucherUuid());
	    assertEquals("asset_uuid_1",itemList.get(0).getRelatedLv1AssetUuid());
		
		assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));


		List<LedgerItem> itemsV2 = ledgerItemServiceV2.getLedgerItemListBy(ledgerBookNo);
		ledgerBookClearingVoucherV2Handler.clearing_voucher_write_off(ledgerBookNo, deductApplicationUuid, party, "jjjj",
				"bbbbb", "ssss", counterPartyAccount, cashFlow);

		List<LedgerItem> itemListV2 =  ledgerItemServiceV2.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING),book, "asset_uuid_1" );
		List<String> uuidList = itemsV2.stream().map(i->i.getLedgerUuid()).collect(Collectors.toList());
		itemListV2.removeIf(i-> uuidList.contains(i.getLedgerUuid()));
		assertEquals(16,itemListV2.size());
		Map<String,LedgerItem> itemMapV2 = itemListV2.stream().collect(Collectors.toMap(LedgerItem::getLedgerUuid, Function.identity()));
		for(LedgerItem item:itemListV2){
			assertTrue(itemMapV2.get(item.getForwardLedgerUuid())!=null ||itemMapV2.get(item.getBackwardLedgerUuid())!=null);
		}

		assertEquals(ledgerBookNo,itemListV2.get(1).getLedgerBookNo());
		assertEquals("bbbbb",itemListV2.get(1).getBusinessVoucherUuid());
		assertEquals("ssss",itemListV2.get(1).getSourceDocumentUuid());
		assertEquals("jjjj",itemListV2.get(1).getJournalVoucherUuid());
		assertEquals("asset_uuid_1",itemListV2.get(1).getRelatedLv1AssetUuid());

		assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemServiceV2.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));

	}
	
}
