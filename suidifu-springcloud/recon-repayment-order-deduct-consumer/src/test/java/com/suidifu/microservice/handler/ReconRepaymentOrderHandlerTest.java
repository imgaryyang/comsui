package com.suidifu.microservice.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.support.Filter;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.impl.ThirdPartyVoucherRepaymentOrderWithReconciliationImpl;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.DeductDetailAmountCarryOverException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingSettlingStatus;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.OrderClearStatus;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReconRepaymentOrderHandlerTest {
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
	@Autowired
	private OrderService orderService;
	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private VirtualAccountFlowService virtualAccountFlowService;
	@Autowired
	private RepurchaseService repurchaseService;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private CashFlowService cashFlowService;
 	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;

	@Autowired
	private RepaymentOrderService repaymentOrderService;

	@Autowired
	private SessionFactory sessionFactory;


	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
	
	@Autowired
	private CashFlowAutoIssueV2_0Handler cashFlowAutoIssueV2_0Handler;
	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private ThirdPartyVoucherRepaymentOrderWithReconciliationImpl thirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl;


	
	/*
	 * 还款订单一个Item的核销 
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/TestrepaymentOrder_OneItemrecover.sql"})
	public void test_repaymentOrder_oneItem_recover(){
		//String repaymentOrderUuid = "order_uuid_1";
		String repayment_order_item_uuid1 = "order_detail_uuid_1";
		String repayment_order_item_uuid2 = "order_detail_uuid_2";
		//String financialContractNo = "financial_contract_code_1";
		String ledgerBookNo = "yunxin_ledger_book";
		String sourceDocumentUuid="source_document_uuid_1";
		//RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(repaymentOrderUuid,
		//		repayment_order_item_uuid, financialContractNo,
		//		ledgerBookNo);
		try {

			cashFlowAutoIssueV2_0Handler.recover_assets_repayment_order_deduct(sourceDocumentUuid);

		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
//		sessionFactory.getCurrentSession().flush();
//		sessionFactory.getCurrentSession().clear();
//		
		RepaymentOrderItem item1 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid1);
		RepaymentOrderItem item2 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid2);

		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		BigDecimal total_bookingAmount = new BigDecimal("500");
		String repaymentPlanDateString = "2017-06-15 10:10:10";
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item1.getRepaymentBusinessNo());

		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
		Assert.assertNotEquals("11111", assetSet.getVersionLock());
		assertEquals(OnAccountStatus.WRITE_OFF, assetSet.getOnAccountStatus());
		assertEquals(DateUtils.parseDate("2017-06-15 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());//资产实际回收时间
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");

		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
		assertEquals(0,new BigDecimal("0").compareTo(extraData.getOrderPayingAmount()));

		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);

		//校验ledger book明细余额,不变
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));

		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
		assertEquals(0,new BigDecimal(0).compareTo(receivable_amount));

		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
		assertEquals(2,jvLists.size());
		JournalVoucher jv1 = jvLists.get(0);
		JournalVoucher jv2 = jvLists.get(1);
		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv1.getJournalVoucherType());
		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv2.getJournalVoucherType());

		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv1.getStatus());
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv2.getStatus());

		assertEquals(assetSet.getAssetUuid(),jv1.getBillingPlanUuid());
		assertEquals(assetSet.getAssetUuid(),jv2.getBillingPlanUuid());

		assertEquals(item1.getAmount(),jv1.getBookingAmount());
		assertEquals(item2.getAmount(),jv2.getBookingAmount());


		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
		assertNotNull(order);

		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getDueDate()));
		assertEquals(repaymentPlanDateString,DateUtils.longDateFormat(order.getPayoutTime()));


		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(),"");
		assertEquals(2,ledgers.size());

		assertEquals(DetailPayStatus.PAID, item1.getDetailPayStatus());

		String SourceDocumentUuid="source_document_uuid_1";
		SourceDocument source_document=sourceDocumentService.getSourceDocumentBy(SourceDocumentUuid);
		assertEquals(SourceDocumentStatus.SIGNED,source_document.getSourceDocumentStatus());
		String deductApplicationUuid="deduct_application_uuid_1";
//		//DeductApplication deductapplition= deductApplitionService.
//		
		
	}
	
	/*
	 * 还款计划与应收资本不对应 
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/TestrepaymentOrder_TwoItemsrecover.sql"})
	public void test_repaymentOrder_TwoItems_recover(){
				String sourceDocumentUuid="source_document_uuid_1";
				try {
					cashFlowAutoIssueV2_0Handler.recover_assets_repayment_order_deduct(sourceDocumentUuid);
					fail();
				} catch(DeductDetailAmountCarryOverException e){
					assertTrue(true);
				}catch (Exception e) {
					fail();
				}
//				sessionFactory.getCurrentSession().flush();
//				sessionFactory.getCurrentSession().clear();
	}

	/**
	 * hjl：扣款
	 * 2017年8月2日
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/TestgenerateThirdPartVoucherWithReconciliationMethods.sql"})
	public void testthirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl(){
		String contractUuid="contract_uuid_1";
		String repaymentOrderUuid="order_uuid_1";
		try{
				thirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl.generateThirdPartVoucherWithReconciliation(contractUuid, repaymentOrderUuid,"deduct_application_uuid_1",Priority.High.getPriority());
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
//		sessionFactory.getCurrentSession().flush();
//		sessionFactory.getCurrentSession().clear();
		
		String outlierDocumentUuid="deduct_application_uuid_1";
		String firstOutlierDocType= SourceDocument.FIRSTOUTLIER_PAYMENT_ORDER;//payment_order;
		SourceDocument sourcedocument = sourceDocumentService.getSourceDocumentByOutlierDocumentUuid(outlierDocumentUuid, firstOutlierDocType);
//		assertNotEquals(null,sourcedocument);
		
		String sourcedocumentDetailUuid_1=sourcedocument.getUuid();
		List<SourceDocumentDetail> sourcedocumentdetaillist=sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourcedocumentDetailUuid_1);
//		assertEquals(2,sourcedocumentdetaillist.size());
		
		String sourceDocumentDetailUuid_1 =sourcedocumentdetaillist.get(0).getUuid();
		String sourceDocumentDetailUuid_2 =sourcedocumentdetaillist.get(1).getUuid();
		List<JournalVoucher> journalVoucherList_1 =journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetailUuid_1);
//		assertEquals(1,journalVoucherList_1.size());
		List<JournalVoucher> journalVoucherList_2 =journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetailUuid_2);
//		assertEquals(1,journalVoucherList_2.size());
		
		
		String repayment_order_item_uuid1="order_detail_uuid_1";
		String repayment_order_item_uuid2="order_detail_uuid_2";
		RepaymentOrderItem item1 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid1);
		RepaymentOrderItem item2 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid2);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		
		String ledgerBookNo="yunxin_ledger_book";
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item1.getRepaymentBusinessNo());
		
//		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
//		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
//		Assert.assertNotEquals("11111", assetSet.getVersionLock());
//		assertEquals(OnAccountStatus.WRITE_OFF, assetSet.getOnAccountStatus());
//		assertEquals(DateUtils.parseDate("2017-09-20 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());//资产实际回收时间
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
//		assertTrue(new BigDecimal("0").compareTo(extraData.getOrderPayingAmount())==0);
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
		
		//校验ledger book明细余额
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
//		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
//		assertEquals(0,new BigDecimal(100.00).compareTo(receivable_amount));
		
		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
//		assertEquals(2,jvLists.size());
		JournalVoucher jv1 = jvLists.get(0);
		JournalVoucher jv2 = jvLists.get(1);
//		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv1.getJournalVoucherType());
//		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv2.getJournalVoucherType());
//
//		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv1.getStatus());
//		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv2.getStatus());
//
//		assertEquals(CounterAccountType.BankAccount,jv1.getCounterAccountType());
//		assertEquals(AccountSide.DEBIT,jv1.getAccountSide());
//
//		assertEquals(assetSet.getAssetUuid(),jv1.getBillingPlanUuid());
//		assertEquals(assetSet.getAssetUuid(),jv2.getBillingPlanUuid());
//
//		assertEquals(item1.getAmount(),jv1.getBookingAmount());
//		assertEquals(item2.getAmount(),jv2.getBookingAmount());
		String repaymentPlanDateString="2017-09-20 10:10:10";
		List<LedgerItem> ledgers_1 = ledgerItemService.get_jv_asset_accountName_related_ledgers(ledgerBookNo,assetSet.getAssetUuid(), jv1.getJournalVoucherUuid(),Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
//		assertTrue(ledgers_1.size()>0);
//		for(LedgerItem ledgerItem :ledgers_1){
//			assertEquals(repaymentPlanDateString,DateUtils.format(ledgerItem.getCarriedOverDate(),DateUtils.LONG_DATE_FORMAT));
//		}
		List<LedgerItem> ledgers_2 = ledgerItemService.get_jv_asset_accountName_related_ledgers(ledgerBookNo,assetSet.getAssetUuid(), jv1.getJournalVoucherUuid(),Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
//		assertTrue(ledgers_2.size()>0);
//		for(LedgerItem ledgerItem :ledgers_2){
//			assertEquals(repaymentPlanDateString,DateUtils.format(ledgerItem.getCarriedOverDate(),DateUtils.LONG_DATE_FORMAT));
//		}
		
		
		
		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
//		assertEquals(item1.getAmount(),order.getTotalRent());
//		assertEquals(assetSet,order.getAssetSet());
//		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getDueDate()));
//		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getPayoutTime()));
//		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
//		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());

//		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid());
//		assertEquals(2,ledgers.size());
		//扣款现金流标志
		DeductPlan deductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid(outlierDocumentUuid, DeductApplicationExecutionStatus.SUCCESS);
//		assertEquals("deduct_cash_identity_1",deductPlan.getDeductCashIdentity());
////		for (LedgerItem ledgerItem : ledgers) {
//			assertEquals(deductPlan.getDeductCashIdentity(),ledgerItem.getRelatedLv3AssetUuid());
//			assertEquals(repaymentOrderUuid,ledgerItem.getBusinessVoucherUuid());
//		}
//		//	VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
//
//		//校验ledger book明细余额
//		balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, borrower_customerUuid);
//		BigDecimal bg= new BigDecimal("0");
//		assertEquals(0,balance.compareTo(bg));
//
//		assertEquals(DetailPayStatus.PAID, item1.getDetailPayStatus());
//		assertEquals(SourceDocumentStatus.SIGNED,sourcedocument.getSourceDocumentStatus());

		
	}
	/**
	 * 2017年8月3日
	 * 
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/TestAssetSet_activeStatusISinvalidMethods.sql"})
	public void testAssetSet_activeStatusISinvalid(){
		String repayment_order_item_uuid1 = "order_detail_uuid_1";
		String repayment_order_item_uuid2 = "order_detail_uuid_2";
		String ledgerBookNo = "yunxin_ledger_book";
		String sourceDocumentUuid="source_document_uuid_1";
		try {

			cashFlowAutoIssueV2_0Handler.recover_assets_repayment_order_deduct(sourceDocumentUuid);

		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
//		sessionFactory.getCurrentSession().flush();
//		sessionFactory.getCurrentSession().clear();
		
		RepaymentOrderItem item1 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid1);
		RepaymentOrderItem item2 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid2);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item1.getRepaymentBusinessNo());
		
		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
		Assert.assertNotEquals("11111", assetSet.getVersionLock());
		assertEquals(OnAccountStatus.WRITE_OFF, assetSet.getOnAccountStatus());
		assertNotEquals(DateUtils.parseDate("2016-05-17 14:26:50", DateUtils.LONG_DATE_FORMAT),assetSet.getActualRecycleDate());//资产实际回收时间
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
		BigDecimal bigd=new BigDecimal("0.00");
		assertEquals(bigd,extraData.getOrderPayingAmount());
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
		
		//校验ledger book明细余额
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
		assertEquals(0,new BigDecimal("0.00").compareTo(receivable_amount));
		
		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
		assertEquals(2,jvLists.size());
		JournalVoucher jv1 = jvLists.get(0);
		JournalVoucher jv2 = jvLists.get(1);
		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv1.getJournalVoucherType());
		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv2.getJournalVoucherType());
		
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv1.getStatus());
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv2.getStatus());
		
		assertEquals(CounterAccountType.VirtualAccount,jv1.getCounterAccountType());
		assertEquals(AccountSide.DEBIT,jv1.getAccountSide());
		
		assertEquals(assetSet.getAssetUuid(),jv1.getBillingPlanUuid());
		assertEquals(assetSet.getAssetUuid(),jv2.getBillingPlanUuid());
		
		assertEquals(item1.getAmount(),jv1.getBookingAmount());
		assertEquals(item2.getAmount(),jv2.getBookingAmount());
		
		String repaymentPlanDateString="2017-09-20 10:10:10";
		
		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
		assertEquals(item1.getAmount(),order.getTotalRent());
		assertEquals(assetSet.getAssetUuid(),order.getAssetSetUuid());
		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getDueDate()));
		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getPayoutTime()));
		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());

//		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid());
//		assertEquals(2,ledgers.size());
	
	//	VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
		
		//校验ledger book明细余额
		balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, borrower_customerUuid);
		BigDecimal bg= new BigDecimal("0");
		assertEquals(0,balance.compareTo(bg));
		
		SourceDocument source_document=sourceDocumentService.getSourceDocumentBy("source_document_uuid_1");
		assertEquals(DetailPayStatus.PAID, item1.getDetailPayStatus());
		assertEquals(SourceDocumentStatus.SIGNED,source_document.getSourceDocumentStatus());

	}
}