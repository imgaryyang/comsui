package com.suidifu.microservice.handler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.DeductDetailAmountCarryOverException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CashFlowAutoIssueHandlerTest {
	
	private static final long TIMEOUT = 1000 * 10 * 2;
	
	private static final long HALF_TIMEOUT = TIMEOUT / 2;


	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private JournalVoucherHandler journalVoucherHandler;

	@Autowired
	private SourceDocumentService sourceDocumentService;

	@Autowired
	private LedgerBookService ledgerBookService;

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ContractService contractService;
	@Autowired
	private LedgerBookHandler ledgerBookHandler;
	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private BankAccountCache bankAccountCache;

	@Test
	@Sql("classpath:test/yunxin/recon/recover_assets_by_prepayment_third_party_deduction2.sql")
	public void test_recover_assets_online_deduct_prepayment_DeductDetailAmountCarryOverException() {
		FinancialContract financialContract = financialContractService.getFinancialContractBy("financial_contract_uuid_1");
		String deductApplicationUuid = "deduct_application_uuid_1";


		ReconciliationContext context = new ReconciliationContext();
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo("repayment_plan_no_1");
		LedgerBook book = ledgerBookService.getBookByBookNo("yunxin_ledger_book");
		Contract contract = contractService.getContract(new Long("7"));
		SourceDocument sd = sourceDocumentService.getSourceDocumentBy("source_document_uuid_1");
		BigDecimal bookingAmount = new BigDecimal("1000");
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);

		String merIdClearingNo = deductPlanService.getMerIdClearingNoKey(deductApplication.getDeductApplicationUuid());
		DepositeAccountInfo unionDepositAccount = bankAccountCache.extractThirdPartyPaymentChannelAccountFrom(financialContract, merIdClearingNo);

		HashMap<String, BigDecimal> feeType = new HashMap<String, BigDecimal>();
		feeType.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY,  new BigDecimal(1000));
		feeType.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY,  new BigDecimal(100));

		context.setRecoverType(AssetRecoverType.LOAN_ASSET);
		context.setContract(contract);
		context.setBookingAmount(bookingAmount);
		context.setBookingDetailAmount(feeType);
		context.setLedgerBookNo("yunxin_ledger_book");
		context.setAssetSet(assetSet);
		context.setSourceDocument(sd);
		context.setFinancialContractAccountForLedgerBook(unionDepositAccount);
		context.setIssuedJournalVoucher(new JournalVoucher());
		context.setReconciliationType(ReconciliationType.RECONCILIATION_THIRDPARTY_DEDUCT);

		try {

//			DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
//
//			List<DeductApplicationRepaymentDetail> dedutRepaymentDetailList = deductApplicationDetailService.getRepaymentDetailsBy(deductApplicationUuid);
//			for (DeductApplicationRepaymentDetail deductDetail : dedutRepaymentDetailList) {
//				Reconciliation accountReconciliation= Reconciliation.reconciliationFactory(VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey());
//				Map<String, Object> inputParams=new HashMap<String,Object>();
//				inputParams.put(ReconciliationForDeductApi.PARAMS_DEDUCT_APPLICATION_DETAIL, deductDetail);
//				inputParams.put(ReconciliationForDeductApi.PARAMS_DEDUCT_APPLICATION, deductApplication);
//				accountReconciliation.accountReconciliation(inputParams);
//			}
//
//			deductApplication.setRecordStatus(RecordStatus.WRITE_OFF);
//			deductApplicationService.save(deductApplication);
//			cashFlowAutoIssueHandler.recover_assets_online_deduct_by_interface_each_deduct_application(deductApplicationUuid);
			ledgerBookHandler.amortize_loan_asset_to_receivable(book, assetSet);
			journalVoucherHandler.recover_asset_by_reconciliation_context(context);

			fail();
		} catch (DeductDetailAmountCarryOverException e) {
			e.printStackTrace();
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
