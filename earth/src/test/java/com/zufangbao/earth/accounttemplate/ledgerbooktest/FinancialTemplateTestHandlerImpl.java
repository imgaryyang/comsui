package com.zufangbao.earth.accounttemplate.ledgerbooktest;

import com.zufangbao.earth.BaseTest;
import com.zufangbao.earth.yunxin.handler.AssetPackageHandler;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.finanialcontract.FinancialContractAndCashFlowHandler;
import com.zufangbao.sun.finanialcontract.FinancialContractInjectionHandler;
import com.zufangbao.sun.finanialcontract.TripartiteClearRemittanceHandler;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.sun.ledgerbookv2.utils.GeneralAccountTemplateHelperForTest;
import com.zufangbao.sun.remittance.handler.RemittanceApplicationHandler;
import com.zufangbao.sun.service.AccountService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.NFQLoanInformation;
import com.zufangbao.sun.yunxin.entity.NFQRepaymentPlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationDetail;
import com.zufangbao.sun.yunxin.service.LoanBatchService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

@Transactional
@Rollback(value = false)
public class FinancialTemplateTestHandlerImpl extends BaseTest{
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private JournalVoucherService JournalVoucherService;
//	@Autowired
//	private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private RemittanceApplicationHandler remittanceApplicationHandler;
	@Autowired
	private FinancialContractInjectionHandler financialContractInjectionHandler;
	@Autowired
	private FinancialContractAndCashFlowHandler financialContractAndCashFlowHandler;
	@Autowired
	private TripartiteClearRemittanceHandler tripartiteClearRemittanceHandler;
	@Autowired
	private IRemittanceApplicationService  iRemittanceApplicationService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private IRemittanceApplicationDetailService iRemittanceApplicationDetailService;
	@Autowired
	private LoanBatchService loanBatchService;
	@Autowired
	private AssetPackageHandler AssetPackageHandler;
	
	private String LedgerBookNo = "YUNXIN_AMEI_ledger_book";
	private String ledgerBookOrgnizationId = "14";
	@Before
	public void setUp() {

//		generalAccountTemplateHelperForTest.prepareEntryBookSql();
//
//		generalAccountTemplateHelperForTest.deleteAccountTemplateAndScenarion();
	}
	
	
	
	/**
	 * 确认流水  信托注资  信托专户
	 */
	@Test
	@Sql("classpath:test/yunxin/remittanceApplication/test_financial_remittance_loans.sql")
	public void testFinancial_loans_remittance_template(){
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		//根据流水表的uuid获取财务凭证表
		JournalVoucher journalVoucher = JournalVoucherService.getJournalVoucherByCashFlowUid("cash_flow_uuid_1");
		//获取到原始凭证表的uuid
		String sourceDocumentUuid = journalVoucher.getSourceDocumentUuid();
		//获取相对应的原始凭证表对象
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
		//获取到信托合同的对象
		FinancialContract financialContract = financialContractService.getFinancialContractBy(sourceDocument.getFinancialContractUuid());
		//生成脚本
//		generalAccountTemplateHelperForTest.createTemplateBy(financialContract.getLedgerBookNo(), EventType.FINANCIAL_CONTRACT_INJECTION);
		
		LedgerTradeParty ledgerTradeParty = new LedgerTradeParty();
		ledgerTradeParty.setFstParty(sourceDocument.getFirstPartyId());
		ledgerTradeParty.setSndParty(sourceDocument.getSecondPartyId());
		
		try {
			financialContractInjectionHandler.financialCotractInjectionGenerateDataFrame(cashFlow, financialContract.getLedgerBookNo(), ledgerTradeParty,journalVoucher.getJournalVoucherUuid(),sourceDocumentUuid);//-- 财务确认 信托注资  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * 第三方清算打款
	 */
	@Test
	@Sql("classpath:test/yunxin/remittanceApplication/test_financial_remittance_loans.sql")
	public void testClearingRemittance(){
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		JournalVoucher journalVoucher = JournalVoucherService.getJournalVoucherByCashFlowUid("cash_flow_uuid_1");
		String sourceDocumentUuid = journalVoucher.getSourceDocumentUuid();
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
		FinancialContract financialContract = financialContractService.getFinancialContractBy(sourceDocument.getFinancialContractUuid());
	
//		generalAccountTemplateHelperForTest.createTemplateBy(financialContract.getLedgerBookNo(), EventType.TRIPARTITE_CLEARING_REMITTANCE);
		
		LedgerTradeParty ledgerTradeParty = new LedgerTradeParty();
		ledgerTradeParty.setFstParty(sourceDocument.getFirstPartyId());
		ledgerTradeParty.setSndParty(sourceDocument.getSecondPartyId());
		
		try {
			tripartiteClearRemittanceHandler.clearingRemittance(cashFlow, financialContract.getLedgerBookNo(), ledgerTradeParty);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * 财务确认流水 信托专户   放款
	 */
	@Test
	@Sql("classpath:test/yunxin/remittanceApplication/test_financial_remittance_loans.sql")
	public void testRemittanceApplicationFInancial(){
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		JournalVoucher journalVoucher = JournalVoucherService.getJournalVoucherByCashFlowUid("cash_flow_uuid_1");
		String sourceDocumentUuid = journalVoucher.getSourceDocumentUuid();
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
		FinancialContract financialContract = financialContractService.getFinancialContractBy(sourceDocument.getFinancialContractUuid());
	
//		generalAccountTemplateHelperForTest.createTemplateBy(financialContract.getLedgerBookNo(), EventType.FINANCIAL_REMITTANCE_LOANS);
		
		LedgerTradeParty ledgerTradeParty = new LedgerTradeParty();
		ledgerTradeParty.setFstParty(sourceDocument.getFirstPartyId());
		ledgerTradeParty.setSndParty(sourceDocument.getSecondPartyId());
		
		try {
			remittanceApplicationHandler.remittance_Application_Financial(financialContract.getLedgerBookNo(), cashFlow, ledgerTradeParty); //-- 财务确认    放款 信托合同专户
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * 放款  贷款专户
	 */
	@Test
	@Sql("classpath:test/yunxin/remittanceApplication/test_remittance_application.sql")
	public void testRemittnaceLoanstemplate(){
		
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getRemittanceApplicationByRequestNo("33");
		
		FinancialContract financialContract = financialContractService.getFinancialContractBy(remittanceApplication.getFinancialContractUuid());
		
//		generalAccountTemplateHelperForTest.createTemplateBy(LedgerBookNo, EventType.REMITTANCE_LOANS);

		List<RemittanceApplicationDetail> applicationDetailByApplication = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid(remittanceApplication.getRemittanceApplicationUuid());
		
		LedgerBook ledgerBook = new LedgerBook(LedgerBookNo, ledgerBookOrgnizationId);
		
		LedgerTradeParty ledgerTradeParty = new LedgerTradeParty();
		
		ledgerTradeParty.setFstParty(ledgerBook.getLedgerBookOrgnizationId());
		ledgerTradeParty.setSndParty(applicationDetailByApplication.get(0).getRemittanceApplicationDetailUuid());
		
		try {
//			remittanceApplicationHandler.remittance_Application_V(remittanceApplication, ledgerBook.getLedgerBookNo(), ledgerTradeParty);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}
	/**
	 * 不确定流水
	 */
	@Test
	@Sql("classpath:test/yunxin/financialContract/testFinancial_Contract_Tentative.sql")
	public void testFinancialContractTentativetemplate(){
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("33fds32fsd324vxfdsf324");
		String hostAccountNo = cashFlow.getHostAccountNo();
		String counterAccountNo = cashFlow.getCounterAccountNo();
		Account hostAccount = accountService.getAccountByAccountNo(hostAccountNo);
		Account counterAccount = accountService.getAccountByAccountNo(counterAccountNo);
		
		
		LedgerTradeParty ledgerTradeParty = new LedgerTradeParty();
		
		ledgerTradeParty.setFstParty(hostAccount.getUuid());
		
		ledgerTradeParty.setSndParty(counterAccount.getUuid());
		
//		generalAccountTemplateHelperForTest.createTemplateBy(LedgerBookNo, EventType.FINANCIAL_TENTATIVE);
		
		try {
			financialContractAndCashFlowHandler.cashFlow_Resolve(cashFlow, ledgerTradeParty, LedgerBookNo);
		} catch (Exception e) {
			e.printStackTrace();
			
			fail();
		}
	}
	/**
	 * 流水冲销
	 */
	@Test
	@Sql("classpath:test/yunxin/financialContract/testFinancial_Contract_Tentative.sql")
	public void testCashFlowTentativeRinse(){
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("33fds32fsd324vxfdsf324");
		String hostAccountNo = cashFlow.getHostAccountNo();
		String counterAccountNo = cashFlow.getCounterAccountNo();
		Account hostAccount = accountService.getAccountByAccountNo(hostAccountNo);
		Account counterAccount = accountService.getAccountByAccountNo(counterAccountNo);
		
		
		LedgerTradeParty ledgerTradeParty = new LedgerTradeParty();
		
		ledgerTradeParty.setFstParty(hostAccount.getUuid());
		
		ledgerTradeParty.setSndParty(counterAccount.getUuid());
		
//		generalAccountTemplateHelperForTest.createTemplateBy(LedgerBookNo, EventType.CASH_FLOW_RINSE);
		
		try {
			financialContractAndCashFlowHandler.cashFlow_Resolve_Rinse(cashFlow, ledgerTradeParty, LedgerBookNo);
		} catch (Exception e) {
			e.printStackTrace();
			
			fail();
		}
	}
	@Test
	@Sql("classpath:test/yunxin/financialContract/testFinancialContract_remittanceloan_CashFlow.sql")
	public void testAssetSetPackageAndRemittanceLoanCashFlow(){
		NFQLoanInformation nfqLoanInformation = new NFQLoanInformation();
		nfqLoanInformation.setContractUniqueId("01c7d76d5a0144e68bbd34fcd0b82d5f");
		nfqLoanInformation.setAdvaMatuterm("0");
		nfqLoanInformation.setAppId("1");
		nfqLoanInformation.setCustomerName("ZJ");
		nfqLoanInformation.setCustomerIDNo("123784324");
		nfqLoanInformation.setCustomerCode("3324");
		nfqLoanInformation.setLoanCapitalSum("1000");
		nfqLoanInformation.setPeriods("12");
		nfqLoanInformation.setStartDate("2017-01-01");
		nfqLoanInformation.setInterestRate("2121");
		nfqLoanInformation.setPenaltyInterest("3424");
		
		
		
		
		NFQRepaymentPlan nfqRepaymentPlan = new NFQRepaymentPlan();
		nfqRepaymentPlan.setContractNo("云信信2017-883-DK（623）号 ");
		nfqRepaymentPlan.setAssetRecycleDate("2018-01-01");
		nfqRepaymentPlan.setAssetPrincipalValue("1000");
		nfqRepaymentPlan.setAssetInterestValue("500");
//		nfqRepaymentPlan.setRepayScheduleNo("213124");
		
		List<NFQLoanInformation> NFQLoanInformationList = new ArrayList<NFQLoanInformation>();
		NFQLoanInformationList.add(nfqLoanInformation);
		List<NFQRepaymentPlan> NFQRepaymentPlanList = new ArrayList<NFQRepaymentPlan>();
		NFQRepaymentPlanList.add(nfqRepaymentPlan);
		
		FinancialContract financialContract = financialContractService.getFinancialContractBy("2d380fe1-7157-490d-9474-12c5a9901e29");
		
//		generalAccountTemplateHelperForTest.createTemplateBy(financialContract.getLedgerBookNo(), EventType.FINANCIAL_REMITTANCE_LOANS);
		
		LoanBatch loanBatch = loanBatchService.getLoanBatchById(23L);
		
//		AssetPackageHandler.Testimport_asset_package_excel_for_yunxin(NFQLoanInformationList, NFQRepaymentPlanList, financialContract, loanBatch);
	}
}

