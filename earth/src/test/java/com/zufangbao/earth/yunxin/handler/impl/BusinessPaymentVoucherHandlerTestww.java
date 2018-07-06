package com.zufangbao.earth.yunxin.handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountFlow;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.datasync.DataSyncLogModel;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucherCheckingLevel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucherStatus;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailCheckState;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentExcuteResult;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentExcuteStatus;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//		"classpath:/local/applicationContext-*.xml" })
//@Transactional
public class BusinessPaymentVoucherHandlerTestww extends AbstractNeverRollBackApplicationTest {
	/*
	 * 回购销账
	 *
	 * 足额 有明细
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/businessPaymentVoucher/scansd_and_repurchase_by_business_pay_voucherww1.sql"})
	public void test_check_repurchase_write_offww1() {
		try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class, 1L);
		BigDecimal amount = repurchaseDoc.getAmount();
		assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(
				sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1, details.size());
		SourceDocumentDetail sourceDocumentDetail = details.get(0);

		String contractUuid = "contract_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		String customerUuid = "company_customer_uuid_1";
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

		// sourceDocumentDetail 和 repurchaseDoc 状态更改
		assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
		assertEquals(RepurchaseStatus.REPURCHASED, repurchaseDoc.getRepurchaseStatus());
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getLastModifedTime()));
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getVerificationTime()));

		// 校验ledger_book的应收资产
		BigDecimal receivable = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, "", customerUuid);
		// assertEquals(0,BigDecimal.ZERO.compareTo(receivable));

		// 校验JournalVoucher
		List<JournalVoucher> jvLists = journalVoucherService
				.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
		assertEquals(1, jvLists.size());

		// 校验virtualAccount
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		assertEquals(0, new BigDecimal("7305.00").compareTo(virtualAccount.getTotalBalance()));

		// 校验virtualAccountFlow
		List<VirtualAccountFlow> list = virtualAccountFlowService.loadAll(VirtualAccountFlow.class);
		assertEquals(1, list.size());
		VirtualAccountFlow virtualAccountFlow = list.get(0);
		assertEquals(jvLists.get(0).getJournalVoucherNo(), virtualAccountFlow.getBusinessDocumentNo());
		assertEquals(virtualAccount.getVirtualAccountUuid(), virtualAccountFlow.getVirtualAccountUuid());
		assertEquals(sourceDocument.getBookingAmount(), virtualAccountFlow.getTransactionAmount());
		assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherUuid(), virtualAccountFlow.getBusinessDocumentUuid());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow.getTransactionType());
		assertEquals(null, virtualAccountFlow.getBalance());

		JournalVoucher journalVoucher = jvLists.get(0);
		assertEquals(AccountSide.DEBIT, journalVoucher.getAccountSide());
		assertEquals(journalVoucher, journalVoucher);
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, journalVoucher.getStatus());
		assertEquals(JournalVoucherCheckingLevel.AUTO_BOOKING, journalVoucher.getCheckingLevel());
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE, journalVoucher.getJournalVoucherType());
		assertEquals(repurchaseDoc.getRepurchaseDocUuid(), journalVoucher.getBillingPlanUuid());
		assertEquals(repurchaseDoc.getAmount(), journalVoucher.getBookingAmount());

		assertEquals(sourceDocumentDetail.getPaymentAccountNo(), journalVoucher.getSourceDocumentCounterPartyAccount());
		assertEquals(sourceDocumentDetail.getPaymentName(), journalVoucher.getSourceDocumentCounterPartyName());
		assertEquals(sourceDocument.getOutlierAccount(), journalVoucher.getSourceDocumentLocalPartyAccount());
		assertEquals(sourceDocument.getOutlieAccountName(), journalVoucher.getSourceDocumentLocalPartyName());

		assertEquals("10001", journalVoucher.getLocalPartyAccount());
		assertEquals("测试7", journalVoucher.getLocalPartyName());
		assertEquals(virtualAccount.getVirtualAccountNo(), journalVoucher.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(), journalVoucher.getCounterPartyName());

		Map<String, BigDecimal> map = ledgerBookStatHandler.get_jv_repurchase_detail_amount(ledgerBookNo,
				journalVoucher.getJournalVoucherUuid(), contractUuid);
		assertEquals(Boolean.TRUE,
				new BigDecimal("2100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PRINCIPAL)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_INTEREST)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("200.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PENALTY)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("300.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_OTHER_FEE)) == 0);

		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getPrincipal().compareTo(map.get(ExtraChargeSpec.REPURCHASE_PRINCIPAL)) == 0);
		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getInterest().compareTo(map.get(ExtraChargeSpec.REPURCHASE_INTEREST)) == 0);
		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getPenaltyFee().compareTo(map.get(ExtraChargeSpec.REPURCHASE_PENALTY)) == 0);
		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getOtherCharge().compareTo(map.get(ExtraChargeSpec.REPURCHASE_OTHER_FEE)) == 0);
		DataSyncLogModel dataSyncLog = null;
		try {
			dataSyncLog = dataSyncHandler.generateDataSyncLog(journalVoucher);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(dataSyncLog);
		// SyncDataDecimalModel
		// model=dataSyncLog.getDataSyncBigDecimalDetailsJson();
		assertEquals(Boolean.TRUE,
				new BigDecimal("2100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PRINCIPAL)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_INTEREST)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("200.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PENALTY)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("300.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_OTHER_FEE)) == 0);
	}

	/*
	 * 回购销账
	 * 
	 * 足额 无明细
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/businessPaymentVoucher/scansd_and_repurchase_by_business_pay_voucherww2.sql"})
	public void test_check_repurchase_write_offww2() {
		try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class, 1L);
		BigDecimal amount = repurchaseDoc.getAmount();
		assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(
				sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1, details.size());
		SourceDocumentDetail sourceDocumentDetail = details.get(0);

		String contractUuid = "contract_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		String customerUuid = "company_customer_uuid_1";
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

		// sourceDocumentDetail 和 repurchaseDoc 状态更改
		assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
		assertEquals(RepurchaseStatus.REPURCHASED, repurchaseDoc.getRepurchaseStatus());
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getLastModifedTime()));
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getVerificationTime()));

		// 校验ledger_book的应收资产
		BigDecimal receivable = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, "", customerUuid);
		// assertEquals(0,BigDecimal.ZERO.compareTo(receivable));

		// 校验JournalVoucher
		List<JournalVoucher> jvLists = journalVoucherService
				.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
		assertEquals(1, jvLists.size());

		// 校验virtualAccount
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		assertEquals(0, new BigDecimal("7305.00").compareTo(virtualAccount.getTotalBalance()));

		// 校验virtualAccountFlow
		List<VirtualAccountFlow> list = virtualAccountFlowService.loadAll(VirtualAccountFlow.class);
		assertEquals(1, list.size());
		VirtualAccountFlow virtualAccountFlow = list.get(0);
		assertEquals(jvLists.get(0).getJournalVoucherNo(), virtualAccountFlow.getBusinessDocumentNo());
		assertEquals(virtualAccount.getVirtualAccountUuid(), virtualAccountFlow.getVirtualAccountUuid());
		assertEquals(sourceDocument.getBookingAmount(), virtualAccountFlow.getTransactionAmount());
		assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherUuid(), virtualAccountFlow.getBusinessDocumentUuid());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow.getTransactionType());
		assertEquals(null, virtualAccountFlow.getBalance());

		JournalVoucher journalVoucher = jvLists.get(0);
		assertEquals(AccountSide.DEBIT, journalVoucher.getAccountSide());
		assertEquals(journalVoucher, journalVoucher);
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, journalVoucher.getStatus());
		assertEquals(JournalVoucherCheckingLevel.AUTO_BOOKING, journalVoucher.getCheckingLevel());
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE, journalVoucher.getJournalVoucherType());
		assertEquals(repurchaseDoc.getRepurchaseDocUuid(), journalVoucher.getBillingPlanUuid());
		assertEquals(repurchaseDoc.getAmount(), journalVoucher.getBookingAmount());

		assertEquals(sourceDocumentDetail.getPaymentAccountNo(), journalVoucher.getSourceDocumentCounterPartyAccount());
		assertEquals(sourceDocumentDetail.getPaymentName(), journalVoucher.getSourceDocumentCounterPartyName());
		assertEquals(sourceDocument.getOutlierAccount(), journalVoucher.getSourceDocumentLocalPartyAccount());
		assertEquals(sourceDocument.getOutlieAccountName(), journalVoucher.getSourceDocumentLocalPartyName());

		assertEquals("10001", journalVoucher.getLocalPartyAccount());
		assertEquals("测试7", journalVoucher.getLocalPartyName());
		assertEquals(virtualAccount.getVirtualAccountNo(), journalVoucher.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(), journalVoucher.getCounterPartyName());

		Map<String, BigDecimal> map = ledgerBookStatHandler.get_jv_repurchase_detail_amount(ledgerBookNo,
				journalVoucher.getJournalVoucherUuid(), contractUuid);
		assertEquals(Boolean.TRUE,
				new BigDecimal("2100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PRINCIPAL)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_INTEREST)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("200.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PENALTY)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("300.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_OTHER_FEE)) == 0);

		// 无明细
		// assertEquals(Boolean.TRUE,sourceDocumentDetail.getPrincipal().compareTo(map.get(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY))==0);
		// assertEquals(Boolean.TRUE,sourceDocumentDetail.getInterest().compareTo(map.get(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY))==0);
		// assertEquals(Boolean.TRUE,sourceDocumentDetail.getPenaltyFee().compareTo(map.get(ExtraChargeSpec.PENALTY_KEY))==0);
		// assertEquals(Boolean.TRUE,sourceDocumentDetail.getOtherCharge().compareTo(map.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY))==0);
	}

	/*
	 * 回购销账
	 * 
	 * 不足额 有明细
	 */
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/scansd_and_repurchase_by_business_pay_voucherww3.sql")
	public void test_check_repurchase_write_offww3() {
		try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class, 1L);
		BigDecimal amount = repurchaseDoc.getAmount();
		assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(
				sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1, details.size());
		SourceDocumentDetail sourceDocumentDetail = details.get(0);

		String contractUuid = "contract_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		String customerUuid = "company_customer_uuid_1";
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

		// sourceDocumentDetail 和 repurchaseDoc 状态更改
		assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
		assertEquals(RepurchaseStatus.REPURCHASING, repurchaseDoc.getRepurchaseStatus());
		// 校验ledger_book的应收资产
		BigDecimal receivable = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, "", customerUuid);
		// assertEquals(0,BigDecimal.ZERO.compareTo(receivable));

		// 校验JournalVoucher
		List<JournalVoucher> jvLists = journalVoucherService
				.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
		assertEquals(1, jvLists.size());

		// 校验virtualAccount
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		assertEquals(0, new BigDecimal("8705.00").compareTo(virtualAccount.getTotalBalance()));

		// 校验virtualAccountFlow
		List<VirtualAccountFlow> list = virtualAccountFlowService.loadAll(VirtualAccountFlow.class);
		assertEquals(1, list.size());
		VirtualAccountFlow virtualAccountFlow = list.get(0);
		assertEquals(jvLists.get(0).getJournalVoucherNo(), virtualAccountFlow.getBusinessDocumentNo());
		assertEquals(virtualAccount.getVirtualAccountUuid(), virtualAccountFlow.getVirtualAccountUuid());
		assertEquals(sourceDocument.getBookingAmount(), virtualAccountFlow.getTransactionAmount());
		assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherUuid(), virtualAccountFlow.getBusinessDocumentUuid());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow.getTransactionType());
		assertEquals(null, virtualAccountFlow.getBalance());

		JournalVoucher journalVoucher = jvLists.get(0);
		assertEquals(AccountSide.DEBIT, journalVoucher.getAccountSide());
		assertEquals(journalVoucher, journalVoucher);
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, journalVoucher.getStatus());
		assertEquals(JournalVoucherCheckingLevel.AUTO_BOOKING, journalVoucher.getCheckingLevel());
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE, journalVoucher.getJournalVoucherType());
		assertEquals(repurchaseDoc.getRepurchaseDocUuid(), journalVoucher.getBillingPlanUuid());
		assertEquals(sourceDocument.getBookingAmount(), journalVoucher.getBookingAmount());

		assertEquals(sourceDocumentDetail.getPaymentAccountNo(), journalVoucher.getSourceDocumentCounterPartyAccount());
		assertEquals(sourceDocumentDetail.getPaymentName(), journalVoucher.getSourceDocumentCounterPartyName());
		assertEquals(sourceDocument.getOutlierAccount(), journalVoucher.getSourceDocumentLocalPartyAccount());
		assertEquals(sourceDocument.getOutlieAccountName(), journalVoucher.getSourceDocumentLocalPartyName());

		assertEquals("10001", journalVoucher.getLocalPartyAccount());
		assertEquals("测试7", journalVoucher.getLocalPartyName());
		assertEquals(virtualAccount.getVirtualAccountNo(), journalVoucher.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(), journalVoucher.getCounterPartyName());

		Map<String, BigDecimal> map = ledgerBookStatHandler.get_jv_repurchase_detail_amount(ledgerBookNo,
				journalVoucher.getJournalVoucherUuid(), contractUuid);
		assertEquals(Boolean.TRUE,
				new BigDecimal("1000.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PRINCIPAL)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("50.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_INTEREST)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("100.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_PENALTY)) == 0);
		assertEquals(Boolean.TRUE,
				new BigDecimal("150.00").compareTo(map.get(ExtraChargeSpec.REPURCHASE_OTHER_FEE)) == 0);

		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getPrincipal().compareTo(map.get(ExtraChargeSpec.REPURCHASE_PRINCIPAL)) == 0);
		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getInterest().compareTo(map.get(ExtraChargeSpec.REPURCHASE_INTEREST)) == 0);
		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getPenaltyFee().compareTo(map.get(ExtraChargeSpec.REPURCHASE_PENALTY)) == 0);
		assertEquals(Boolean.TRUE,
				sourceDocumentDetail.getOtherCharge().compareTo(map.get(ExtraChargeSpec.REPURCHASE_OTHER_FEE)) == 0);

	}

	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/scansd_and_repurchase_by_business_pay_voucherww4.sql")
	public void test_check_repurchase_write_offww4() {
		try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class, 1L);
		assertEquals(SourceDocumentExcuteResult.UNSUCCESS, sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.PREPARE, sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(
				sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1, details.size());
		SourceDocumentDetail sourceDocumentDetail = details.get(0);
		assertEquals(SourceDocumentDetailStatus.UNSUCCESS, sourceDocumentDetail.getStatus());
		assertEquals(SourceDocumentDetailCheckState.CHECK_FAILS, sourceDocumentDetail.getCheckState());

		assertEquals(RepurchaseStatus.REPURCHASING, repurchaseDoc.getRepurchaseStatus());
	}

	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/scansd_and_repurchase_by_business_pay_voucherww5.sql")
	public void test_check_repurchase_write_offww5() {
		try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class, 1L);
		assertEquals(SourceDocumentExcuteResult.UNSUCCESS, sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.PREPARE, sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(
				sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1, details.size());
		SourceDocumentDetail sourceDocumentDetail = details.get(0);
		assertEquals(SourceDocumentDetailStatus.UNSUCCESS, sourceDocumentDetail.getStatus());
		assertEquals(SourceDocumentDetailCheckState.CHECK_FAILS, sourceDocumentDetail.getCheckState());

		assertEquals(RepurchaseStatus.REPURCHASING, repurchaseDoc.getRepurchaseStatus());
	}
}
