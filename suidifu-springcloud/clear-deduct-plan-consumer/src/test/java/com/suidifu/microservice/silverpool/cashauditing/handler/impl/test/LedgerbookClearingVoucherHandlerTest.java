package com.suidifu.microservice.silverpool.cashauditing.handler.impl.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.suidifu.microservice.handler.LedgerBookClearingVoucherHandler;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.TAccountInfo;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LedgerbookClearingVoucherHandlerTest {

	@Autowired
	private LedgerBookClearingVoucherHandler ledgerBookClearingVoucherHandler;
	
	@Autowired
	private BankAccountCache bankAccountCache;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private CashFlowService cashFlowService;
	
	@Autowired
	private LedgerItemService ledgerItemService;
	
	@Autowired
	private LedgerBookService ledgerBookService;
	
	@Test
	@Sql("classpath:test/yunxin/test_ledger_book_clearing_voucher.sql")
	public void testClearingVoucherWriteOff() {
		
		String ledgerBookNo = "ledger_book_no";
		String deductApplicationUuid = "deduct_application_uuid_1";
		
		FinancialContract financialContract = financialContractService.getFinancialContractBy("financial_contract_uuid");
		
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(financialContract);
		
		LedgerTradeParty party = counterPartyAccount.getDeopsite_account_owner_party();
		
		List<LedgerItem> items = ledgerItemService.loadAll(LedgerItem.class);
		
		ledgerBookClearingVoucherHandler.clearing_voucher_write_off(ledgerBookNo, deductApplicationUuid, party, "jjjj", 
				"bbbbb", "ssss", counterPartyAccount, cashFlow);
		
		TAccountInfo accountInfo=null;
	    if(counterPartyAccount!=null)
	    {
	    	String toBankAccoutName = counterPartyAccount.getDeposite_account_name();
	    	accountInfo=ChartOfAccount.EntryBook().get(toBankAccoutName);
	    }
	    
	    LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
	    List<LedgerItem> itemList =  ledgerItemService.loadAll(LedgerItem.class);
	    itemList.removeAll(items);
		
		assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "")));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "")));
		
		
	}
	
}
