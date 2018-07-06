/**
 * 
 */
package com.zufangbao.earth.yunxin.web;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbookv2.utils.GeneralAccountTemplateHelperForTest;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.model.deduct.QueryDeductApplicationShowModelDetail;
import com.zufangbao.sun.yunxin.entity.model.voucher.ClearingVoucherListQueryModel;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobClearingDeductPlan;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.service.ClearingVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hjl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Rollback(true)
@WebAppConfiguration(value="webapp")
public class ClearingVoucherTest {
	
	@Autowired
	private DstJobClearingDeductPlan dstJobClearingDeductPlan;
	@Autowired
	private ClearingVoucherService clearingVoucherService;
	@Autowired
	private AuditJobService auditJobService;
	@Autowired
	private DeductPlanService deductPlanService;
	@Autowired
	private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;
	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private BeneficiaryAuditResultService beneficiaryAuditResultService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private DeductApplicationCoreOperationHandler deductApplicationCoreOperationHandler;
	
	// ClearingVoucherController  "/voucherList"
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/voucherList.sql")
	public void voucherListTest1(){
		ClearingVoucherListQueryModel clearingVoucherListQueryModel = new ClearingVoucherListQueryModel();
		clearingVoucherListQueryModel.setPaymentInstitution("[3]");
		Page page = new Page();
		List<ClearingVoucher> clearingVouchers = clearingVoucherService.queryClearingVoucherListByQueryModel(clearingVoucherListQueryModel, page);
		assertEquals(clearingVouchers.size(), 2);
	}
	
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/voucherList.sql")
	public void voucherListTest2(){
		ClearingVoucherListQueryModel clearingVoucherListQueryModel = new ClearingVoucherListQueryModel();
		clearingVoucherListQueryModel.setPaymentInstitution("[3]");
		clearingVoucherListQueryModel.setStartCashFlowTime("2017-09-07 09:29:19");
		clearingVoucherListQueryModel.setEndCashFlowTime("2017-09-07 09:39:19");
		Page page = new Page();
		List<ClearingVoucher> clearingVouchers = clearingVoucherService.queryClearingVoucherListByQueryModel(clearingVoucherListQueryModel, page);
		assertEquals(clearingVouchers.size(), 1);
	}

	//报错注释
	// Voucher_Task_V2 "handleCreateClearingDeductPlanJvAndLedger"
//	@Test
//	@Sql("classpath:test/yunxin/clearingVoucher/handleCreateClearingDeductPlanJvAndLedger.sql")
//	public void handleCreateClearingDeductPlanJvAndLedgerTest(){
//
//		String ledgerBookNo = "ledger_book_no";
//
////		generalAccountTemplateHelperForTest.createTemplateBy(ledgerBookNo, EventType.CLEARING_VOUCHER_WRITE_OFF);
//
//		AuditJob auditJob = auditJobService.getAuditJob("6f8e224b-9a99-4734-b5f2-bf02b2e9cf7f");
//		String auditJobUuid = auditJob.getUuid();
//
//		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(
//				auditJob);
//		String totalReceivableBillsUuid = totalReceivableBills.getUuid();
//		List<String> deductPlanUuidList = beneficiaryAuditResultService.getDeductPlanUuidsByAuditJob(auditJob, AuditResultCode.ISSUED);
//
//		List<ClearingVoucherParameters> clearingVoucherParametersList = new ArrayList<ClearingVoucherParameters>();
//		Map<String, String> result = dstJobClearingDeductPlan.criticalMarker(deductPlanUuidList);
//		assertEquals(1, result.size());
//		for (String string : deductPlanUuidList) {
//			ClearingVoucherParameters clearingVoucherParameters = new ClearingVoucherParameters();
//			clearingVoucherParameters.setAuditJobUuid(auditJobUuid);
//			clearingVoucherParameters.setDeductPlanUuid(string);
//			clearingVoucherParameters.setTotalReceivableBillsUuid(totalReceivableBillsUuid);
//			clearingVoucherParametersList.add(clearingVoucherParameters);
//		}
//
//		List<LedgerItem> items =  ledgerItemService.loadAll(LedgerItem.class);
//
//
//		dstJobClearingDeductPlan.reconciliationClearingDeductPlan(clearingVoucherParametersList);
//
//		List<JournalVoucher> jvList = journalVoucherService.loadAll(JournalVoucher.class);
//
//		List<LedgerItem> itemList =  ledgerItemService.get_ledgers_by_voucher(ledgerBookNo, null, null, "source_document_uuid_2");
//		itemList.removeAll(items);
//
//		assertEquals(16,itemList.size());
//
////		assertEquals(itemList.get(8).getLedgerUuid(),itemList.get(0).getForwardLedgerUuid());
////		assertEquals(itemList.get(0).getLedgerUuid(),itemList.get(8).getBackwardLedgerUuid());
//
//	   assertEquals(ledgerBookNo,itemList.get(1).getLedgerBookNo());
//	   assertEquals("",itemList.get(1).getBusinessVoucherUuid());
//	   assertEquals("source_document_uuid_2",itemList.get(1).getSourceDocumentUuid());
//	   assertEquals(jvList.get(1).getJournalVoucherUuid(),itemList.get(1).getJournalVoucherUuid());
//	   assertEquals("asset_uuid_1",itemList.get(1).getRelatedLv1AssetUuid());
//
//		assertEquals(0, new BigDecimal("900.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("100.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_INTEREST, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("20.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("30.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION, "", "", "asset_uuid_1", "", "", "", "").abs()));
//		assertEquals(0, new BigDecimal("10.00").compareTo(ledgerItemService.getBalancedAmount(ledgerBookNo, ChartOfAccount.TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE, "", "", "asset_uuid_1", "", "", "", "").abs()));
//
//
//		List<JournalVoucher> journalVoucherList = journalVoucherService.getJournalVoucherListByTypeAndBillingPlanUuid("fe6aec82-3052-459e-86d1-341e60d2958c", JournalVoucherType.TRANSFER_BILL_BY_CLEARING_DEDUCT_PLAN);
//		assertEquals(journalVoucherList.size(),1);
//		JournalVoucher journalVoucher = journalVoucherList.get(0);
//		assertEquals(journalVoucher.getBookingAmount(),new BigDecimal("1090.00"));
//		assertEquals(journalVoucher.getStatus(),JournalVoucherStatus.VOUCHER_ISSUED);
//		assertEquals(journalVoucher.getSourceDocumentAmount(),new BigDecimal("1090.00"));
//
//	}
	
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/showBasicInfoTrans.sql")
	public void showBasicInfoTrans(){
		ClearingVoucher clearingVoucher = clearingVoucherService.getClearingVoucherByUuid("f6623e33-02a8-497a-b576-f9641376b6bd");
		 //auditJob 
        List<String> auditJobUuids = clearingVoucher.getAuditJobListFromAppendix();
        //相关流水
        List<String> cashFlowUuids = clearingVoucher.getCashFlowListFromAppendix();
        assertEquals(3,auditJobUuids.size());
        assertEquals(1,cashFlowUuids.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/clearingVoucherQueryDetail.sql")
	public void queryDeductApplicationInfo(){
		AuditJob auditJob = auditJobService.getAuditJob("audit_job_uuid_1");
		 List<String> uniqueDeductApplicationUuidList = deductPlanService.getUniqueDeductApplicationUuidsByAuditJob(auditJob, new Page(1,10));
		 List<QueryDeductApplicationShowModelDetail> deductApplicationShowModelDetailList = deductApplicationCoreOperationHandler.QueryDeductApplicationShowModelDetail(uniqueDeductApplicationUuidList);
		 int size = deductPlanService.countClearingDeductApplication(auditJob);
		 assertEquals(2, deductApplicationShowModelDetailList.size());
         assertEquals(2, uniqueDeductApplicationUuidList.size());
         assertEquals(2, size);
	}


//	@Test
//	@Sql("classpath:test/yunxin/clearingVoucher/clearingVoucherQueryDetail.sql")
//	public void queryFairDeductPlan(){
//		AuditJob auditJob = auditJobService.getAuditJob("audit_job_uuid_1");
//		List<String> deductPlanUuidList= deductPlanService.queryOutClearingDeductPlan(auditJob,new Page(0,12));
//		List<DeductPlan> deductPlanList = deductPlanService.getDeductPlanByDeductPlanuidList(deductPlanUuidList);
//		int countSize = deductPlanService.countOutClearingDeductPlan(auditJob);
//
//		assertEquals(deductPlanUuidList.get(0), "systembillidentity001");
//		assertEquals(1, deductPlanUuidList.size());
//		assertEquals(1, deductPlanList.size());
//        assertEquals(1, countSize);
//	}

}
