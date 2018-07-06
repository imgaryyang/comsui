/**
 *
 */
package com.suidifu.microservice.silverpool.cashauditing.service.impl;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.handler.ClearDeductPlanHandler;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.utils.ClearingVoucherParameters;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.AuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hjl
 *
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Rollback(true)
@WebAppConfiguration(value="webapp")*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClearingVoucherTest {

	@Autowired
	private ClearDeductPlanHandler clearDeductPlanHandler;
	@Autowired
	private AuditJobService auditJobService;
	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private BeneficiaryAuditResultService beneficiaryAuditResultService;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private JournalVoucherService journalVoucherService;

	// Voucher_Task_V2 "handleCreateClearingDeductPlanJvAndLedger"
	@Test
	@Sql("classpath:test/yunxin/handleCreateClearingDeductPlanJvAndLedger.sql")
	public void handleCreateClearingDeductPlanJvAndLedgerTest(){

		String ledgerBookNo = "ledger_book_no";

//		generalAccountTemplateHelperForTest.createTemplateBy(ledgerBookNo, EventType.CLEARING_VOUCHER_WRITE_OFF);

		AuditJob auditJob = auditJobService.getAuditJob("6f8e224b-9a99-4734-b5f2-bf02b2e9cf7f");
		String auditJobUuid = auditJob.getUuid();

		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(
				auditJob, null);
		String totalReceivableBillsUuid = totalReceivableBills.getUuid();
		List<String> deductPlanUuidList = beneficiaryAuditResultService.getDeductPlanUuidsByAuditJob(auditJob, AuditResultCode.ISSUED);

		List<ClearingVoucherParameters> clearingVoucherParametersList = new ArrayList<ClearingVoucherParameters>();
		Map<String, String> result = clearDeductPlanHandler.criticalMarker(deductPlanUuidList);
		assertEquals(1, result.size());
		for (String string : deductPlanUuidList) {
			ClearingVoucherParameters clearingVoucherParameters = new ClearingVoucherParameters();
			clearingVoucherParameters.setAuditJobUuid(auditJobUuid);
			clearingVoucherParameters.setDeductPlanUuid(string);
			clearingVoucherParameters.setTotalReceivableBillsUuid(totalReceivableBillsUuid);
			clearingVoucherParametersList.add(clearingVoucherParameters);
		}

		List<LedgerItem> items =  ledgerItemService.loadAll(LedgerItem.class);


		clearDeductPlanHandler.reconciliationClearingDeductPlan(clearingVoucherParametersList);

		List<JournalVoucher> jvList = journalVoucherService.loadAll(JournalVoucher.class);

		List<LedgerItem> itemList =  ledgerItemService.get_ledgers_by_voucher(ledgerBookNo, null, null, "source_document_uuid_2");
		itemList.removeAll(items);

		assertEquals(16,itemList.size());

//		assertEquals(itemList.get(8).getLedgerUuid(),itemList.get(0).getForwardLedgerUuid());
//		assertEquals(itemList.get(0).getLedgerUuid(),itemList.get(8).getBackwardLedgerUuid());

	   assertEquals(ledgerBookNo,itemList.get(1).getLedgerBookNo());
	   assertEquals("",itemList.get(1).getBusinessVoucherUuid());
	   assertEquals("source_document_uuid_2",itemList.get(1).getSourceDocumentUuid());
	   assertEquals(jvList.get(1).getJournalVoucherUuid(),itemList.get(1).getJournalVoucherUuid());
	   assertEquals("asset_uuid_1",itemList.get(1).getRelatedLv1AssetUuid());

		assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));


//		List<JournalVoucher> journalVoucherList = journalVoucherService.getJournalVoucherListByTypeAndBillingPlanUuid("fe6aec82-3052-459e-86d1-341e60d2958c", JournalVoucherType.TRANSFER_BILL_BY_CLEARING_DEDUCT_PLAN);
//		assertEquals(journalVoucherList.size(),1);
//		JournalVoucher journalVoucher = journalVoucherList.get(0);
//		assertEquals(journalVoucher.getBookingAmount(),new BigDecimal("1090.00"));
//		assertEquals(journalVoucher.getStatus(),JournalVoucherStatus.VOUCHER_ISSUED);
//		assertEquals(journalVoucher.getSourceDocumentAmount(),new BigDecimal("1090.00"));

	}
}
