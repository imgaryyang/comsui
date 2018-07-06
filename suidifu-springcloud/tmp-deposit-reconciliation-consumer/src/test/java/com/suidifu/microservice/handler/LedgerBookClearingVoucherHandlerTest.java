package com.suidifu.microservice.handler;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.ConsumerTest;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/3/7 10:50
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
@Transactional
public class LedgerBookClearingVoucherHandlerTest {

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

  @Test
  @Sql("classpath:test/sql/ledgerBook/test_ledger_book_clearing_voucher.sql")
  public void testClearingVoucherWriteOff() {

    String ledgerBookNo = "ledger_book_no";
    String deductApplicationUuid = "deduct_application_uuid_1";

    FinancialContract financialContract = financialContractService.getFinancialContractBy("financial_contract_uuid");

    CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");

    DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(financialContract);

    LedgerTradeParty party = counterPartyAccount.getDeopsite_account_owner_party();

    List<LedgerItem> items = ledgerItemService.loadAll(LedgerItem.class);

    ledgerBookClearingVoucherHandler.clearingVoucherWriteOff(ledgerBookNo, deductApplicationUuid, party, "jjjj",
        "bbbbb", "ssss", counterPartyAccount, cashFlow);

    List<LedgerItem> itemList = ledgerItemService.loadAll(LedgerItem.class);
    itemList.removeAll(items);

    assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "",
            "", "", "")));
    assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "",
            "", "", "")));
    assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "",
            "asset_uuid_1", "", "", "", "")));
    assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "",
            "asset_uuid_1", "", "", "", "")));
    assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "",
            "", "asset_uuid_1", "", "", "", "")));
    assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "",
            "asset_uuid_1", "", "", "", "")));
    assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "",
            "", "asset_uuid_1", "", "", "", "")));
    assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService
        .getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "",
            "", "asset_uuid_1", "", "", "", "")));


  }

}