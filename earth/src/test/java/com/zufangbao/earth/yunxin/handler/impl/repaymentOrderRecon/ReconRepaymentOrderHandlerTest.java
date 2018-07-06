package com.zufangbao.earth.yunxin.handler.impl.repaymentOrderRecon;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.support.Filter;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountFlow;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.ledgerbook.*;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobRepaymentOrderActiveVoucherReconciliation;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobRepaymentOrderReconciliation;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueV2_0Handler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.impl.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Rollback(true)
@Transactional
public class ReconRepaymentOrderHandlerTest {
	
	@Autowired
	private BusinessPaymentVoucherSession businessPaymentVoucherSession;
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
	private DstJobRepaymentOrderReconciliation dstJobRepaymentOrderReconciliation;
	
	@Autowired
	private DstJobRepaymentOrderActiveVoucherReconciliation dstJobRepaymentOrderActiveVoucherReconciliation;
	
	@Autowired
	private RepaymentOrderService repaymentOrderService;
	
	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
	
	@Autowired
	private CashFlowAutoIssueV2_0Handler cashFlowAutoIssueV2_0Handler;
	@Autowired
	private DeductPlanService deductPlanService;

//	@Autowired
//	private GeneralAccountTemplateHelperForTest generalAccountTemplateHelperForTest;

	@Autowired
	private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl thirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl;

	/*
	 * 主动还款  核销
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/recon_payment_order_active.sql"})
	public void test_check_and_transfer_asstes(){
		String repaymentOrderUuid = "order_uuid_1";
		String repayment_order_item_uuid = "order_detail_uuid_1";
		String financialContractNo = "financial_contract_code_1";
		String ledgerBookNo = "yunxin_ledger_book";
		RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(repaymentOrderUuid,
				repayment_order_item_uuid, financialContractNo,
				ledgerBookNo, null);
		try {

			dstJobRepaymentOrderActiveVoucherReconciliation.repayment_order_recover_details(Arrays.asList(reconparams));

		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		BigDecimal total_bookingAmount = new BigDecimal("1500");
		
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1"); 
			String asset_uuid_1 = "asset_uuid_1";
			BigDecimal unearned_amount_asset = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, asset_uuid_1);
			BigDecimal receivble_amount_asset = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, asset_uuid_1, borrower_customerUuid);
			assertEquals(0,BigDecimal.ZERO.compareTo(unearned_amount_asset));
//			assertEquals(0,BigDecimal.ZERO.compareTo(receivble_amount_asset));
			AssetSet asset = repaymentPlanService.getUniqueRepaymentPlanByUuid(asset_uuid_1);
			assertEquals(AssetClearStatus.CLEAR,asset.getAssetStatus());
			assertEquals(RepaymentPlanType.NORMAL,asset.getRepaymentPlanType());
			assertEquals(ExecutingStatus.SUCCESSFUL,asset.getExecutingStatus());
			assertEquals(OnAccountStatus.WRITE_OFF,asset.getOnAccountStatus());
			assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,asset.getOrderPaymentStatus());
		
			VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
			
			AssetSet asset1 = repaymentPlanService.getUniqueRepaymentPlanByUuid(asset_uuid_1);
			assertEquals(ContractFundingStatus.ALL,asset1.getContractFundingStatus());
		
			List<JournalVoucher> jounalVoucherList = journalVoucherService.list(JournalVoucher.class,new Filter());
			RepaymentOrder repaymentOrder=repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
			assertEquals(1,jounalVoucherList.size());
			JournalVoucher jv = jounalVoucherList.get(0);
			//资金入账时间
			assertEquals(false,repaymentOrder.getCashFlowTime()==null);
			//设定还款时间
			assertEquals(true, DateUtils.isSameDay(jv.getTradeTime(), repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid).getRepaymentPlanTime()));
			//assertEquals(jv.getCashFlowTime(),repaymentOrder.getCashFlowTime());
			assertEquals(item.getAmount(),jv.getBookingAmount());
			assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());
			assertEquals(JournalVoucherType.TRANSFER_BILL_INITIATIVE,jv.getJournalVoucherType());
			assertEquals(item.getOrderUuid(),jv.getBusinessVoucherUuid());
			Order order = orderService.getOrder(jv.getRelatedBillContractNoLv4());
			assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
			assertEquals(item.getAmount(),order.getTotalRent());
			
			
			assertEquals("pay_ac_no_1",jv.getLocalPartyAccount());
			assertEquals("测试用户1",jv.getLocalPartyName());
			assertEquals(virtualAccount.getVirtualAccountNo(),jv.getCounterPartyAccount());
			assertEquals(virtualAccount.getOwnerName(),jv.getCounterPartyName());
		
			//校验流水
			List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
			assertEquals(1,virtualAccountFlowList.size());
			VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
			assertEquals(0,new BigDecimal("1500.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
			assertEquals(jounalVoucherList.get(0).getJournalVoucherUuid(),virtualAccountFlow1.getBusinessDocumentUuid());
			assertEquals(virtualAccount.getVirtualAccountNo(),virtualAccountFlow1.getVirtualAccountNo());
			assertEquals(jounalVoucherList.get(0).getJournalVoucherNo(),virtualAccountFlow1.getBusinessDocumentNo());
			assertEquals(AccountSide.CREDIT,virtualAccountFlow1.getAccountSide());
			assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow1.getTransactionType());
			assertEquals(virtualAccount.getFstLevelContractUuid(),virtualAccountFlow1.getFinancialContractUuid());
			assertEquals(item.getAmount(),virtualAccountFlow1.getTransactionAmount());
			assertEquals(virtualAccount.getVirtualAccountUuid(),virtualAccountFlow1.getVirtualAccountUuid());
			assertEquals(virtualAccount.getVirtualAccountAlias(),virtualAccountFlow1.getVirtualAccountAlias());
			
			assertEquals(DetailPayStatus.PAID, item.getDetailPayStatus());

			List<LedgerItem> items = ledgerItemService.get_jv_asset_accountName_related_ledgers(ledgerBookNo, asset_uuid_1, jv.getJournalVoucherUuid(), Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
			for (LedgerItem ledgerItem : items) {
				assertEquals(repaymentOrderUuid,ledgerItem.getBusinessVoucherUuid());
			}
	}
	
	/**
	 * 商户代偿
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/recon_payment_order_app_pay.sql"})
	public void test_check_and_Repayment_order_recover(){
		String repaymentOrderUuid = "order_uuid_1";
		String repayment_order_item_uuid = "order_detail_uuid_1";
		String financialContractNo = "financial_contract_code_1";
		String ledgerBookNo = "yunxin_ledger_book";
		RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(repaymentOrderUuid,
				repayment_order_item_uuid, financialContractNo,
				ledgerBookNo, null);
		try {

			dstJobRepaymentOrderReconciliation.repayment_order_recover_details(Arrays.asList(reconparams));
			
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		BigDecimal total_bookingAmount = new BigDecimal("1500");
		
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item.getRepaymentBusinessNo());
		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
		assertEquals("11111", assetSet.getVersionLock());
		//assertEquals(DateUtils.parseDate("2017-06-15 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
		assertEquals(0,BigDecimal.ZERO.compareTo(extraData.getOrderPayingAmount()));
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		//校验余额
//		assertEquals(new BigDecimal("10005").subtract(total_bookingAmount),virtualAccount.getTotalBalance());
//		assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger book明细余额,不变
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
		assertEquals(0,BigDecimal.ZERO.compareTo(receivable_amount));
		
		
		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
		RepaymentOrder repaymentOrder=repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
		assertEquals(1,jvLists.size());
		JournalVoucher jv = jvLists.get(0);
		//资金入账时间
		assertEquals(false,repaymentOrder.getCashFlowTime()==null);
		//assertEquals(jv.getCashFlowTime(),repaymentOrder.getCashFlowTime());
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());
		assertEquals("order_uuid_1",jv.getBusinessVoucherUuid());
		assertEquals(assetSet.getAssetUuid(),jv.getBillingPlanUuid());
		assertEquals(item.getAmount(),jv.getBookingAmount());
		assertEquals("pay_ac_no_1",jv.getLocalPartyAccount());
		assertEquals("测试用户1",jv.getLocalPartyName());
		
		assertEquals(virtualAccount.getVirtualAccountNo(),jv.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(),jv.getCounterPartyName());
		
		
		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
		assertNotNull(order);

		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
		assertEquals(DateUtils.format(jv.getTradeTime()),DateUtils.format(order.getDueDate()));
		assertEquals(jv.getTradeTime(),order.getPayoutTime());
		

//		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid());
//		assertEquals(2,ledgers.size());

	
		VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		
		//校验ledger book明细余额,不变
		balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount_after.getTotalBalance()));
		
		
		//校验流水
		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
		assertEquals(1,virtualAccountFlowList.size());
		
		VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
		assertEquals(0,new BigDecimal("1500.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
		assertEquals(jvLists.get(0).getJournalVoucherUuid(),virtualAccountFlow1.getBusinessDocumentUuid());
		assertEquals(virtualAccount_after.getVirtualAccountNo(),virtualAccountFlow1.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherNo(),virtualAccountFlow1.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow1.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow1.getTransactionType());
		assertEquals(virtualAccount_after.getFstLevelContractUuid(),virtualAccountFlow1.getFinancialContractUuid());
		assertEquals(item.getAmount(),virtualAccountFlow1.getTransactionAmount());
		assertEquals(virtualAccount_after.getVirtualAccountUuid(),virtualAccountFlow1.getVirtualAccountUuid());
		assertEquals(virtualAccount_after.getVirtualAccountAlias(),virtualAccountFlow1.getVirtualAccountAlias());
		
		assertEquals(DetailPayStatus.PAID, item.getDetailPayStatus());
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("firstAccountName", ChartOfAccount.FST_BANK_SAVING);
		params.put("sourceDocumentUuid", repaymentOrderUuid);
		List<LedgerItem> bankledgerItems = genericDaoSupport.searchForList("From LedgerItem where firstAccountName=:firstAccountName and sourceDocumentUuid=:sourceDocumentUuid",params);
		assertEquals(1,bankledgerItems.size());
		assertEquals(0,total_bookingAmount.compareTo(bankledgerItems.get(0).getCreditBalance()));
	
		assertEquals(item.getOrderUuid(),jv.getBusinessVoucherUuid());
		List<LedgerItem> items = ledgerItemService.get_jv_asset_accountName_related_ledgers(ledgerBookNo, assetSet.getAssetUuid(), jv.getJournalVoucherUuid(), Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
		for (LedgerItem ledgerItem : items) {
			assertEquals(repaymentOrderUuid,ledgerItem.getBusinessVoucherUuid());
		}
	}
	
	/**
	 * 回购核销
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/recon_payment_order_repurchase.sql"})
	public void test_check_and_Repayment_order_recover_for_repurchase(){
		String repaymentOrderUuid = "order_uuid_1";
		String repayment_order_item_uuid = "order_detail_uuid_1";
		String financialContractNo = "financial_contract_code_1";
		String ledgerBookNo = "yunxin_ledger_book";
		RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(repaymentOrderUuid,
				repayment_order_item_uuid, financialContractNo,
				ledgerBookNo, null);

		try {

			dstJobRepaymentOrderReconciliation.repayment_order_recover_details(Arrays.asList(reconparams));

		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid);
		
		BigDecimal total_bookingAmount = new BigDecimal("1500");
		
		
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class,1L);
		BigDecimal amount = repurchaseDoc.getAmount();
		
		String contractUuid = "contract_uuid_1";
		String customerUuid = "company_customer_uuid_1";
		
		assertEquals(RepurchaseStatus.REPURCHASED, repurchaseDoc.getRepurchaseStatus());
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getLastModifedTime()));
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getVerificationTime()));
		
		List<LedgerItem> ledgers = ledgerItemService.get_batch_ledgers_related_with_repurchase_docV2(ledgerBookNo, contractUuid, repurchaseDoc.getRepurchaseDocUuid(), Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
		assertEquals(2,ledgers.size());
		for (LedgerItem ledgerItem : ledgers) {
			assertEquals(repaymentOrderUuid,ledgerItem.getBusinessVoucherUuid());
		}
		//校验 repurchase
		BigDecimal repurchase_amount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contractUuid);
		assertEquals(0,BigDecimal.ZERO.compareTo(repurchase_amount));
		
		//校验ledger_book的应收资产
		BigDecimal receivable = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, "", customerUuid);
//		assertEquals(0,BigDecimal.ZERO.compareTo(receivable));
		
		//校验JournalVoucher	
		List<JournalVoucher> jvLists =  journalVoucherService.list(JournalVoucher.class,new Filter());
		assertEquals(1,jvLists.size());
		
		//校验virtualAccount
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		
		//校验virtualAccountFlow
		List<VirtualAccountFlow> list = virtualAccountFlowService.loadAll(VirtualAccountFlow.class);
		assertEquals(1, list.size());
		VirtualAccountFlow virtualAccountFlow1 = list.get(0);
		assertEquals(0,new BigDecimal("1500.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
		assertEquals(jvLists.get(0).getJournalVoucherUuid(),virtualAccountFlow1.getBusinessDocumentUuid());
		assertEquals(virtualAccount.getVirtualAccountNo(),virtualAccountFlow1.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherNo(),virtualAccountFlow1.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow1.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow1.getTransactionType());
		assertEquals(virtualAccount.getFstLevelContractUuid(),virtualAccountFlow1.getFinancialContractUuid());
		assertEquals(item.getAmount(),virtualAccountFlow1.getTransactionAmount());
		assertEquals(virtualAccount.getVirtualAccountUuid(),virtualAccountFlow1.getVirtualAccountUuid());
		assertEquals(virtualAccount.getVirtualAccountAlias(),virtualAccountFlow1.getVirtualAccountAlias());
		
		assertEquals(DetailPayStatus.PAID, item.getDetailPayStatus());
		
		
		JournalVoucher journalVoucher=jvLists.get(0);
		RepaymentOrder repaymentOrder =repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
		assertEquals(false, repaymentOrder.getCashFlowTime()==null);
		//assertEquals(repaymentOrder.getCashFlowTime(), journalVoucher.getCashFlowTime());
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE,journalVoucher.getJournalVoucherType());
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,journalVoucher.getStatus());
		assertEquals(JournalVoucherCheckingLevel.AUTO_BOOKING,journalVoucher.getCheckingLevel());
		assertEquals("order_uuid_1",journalVoucher.getBusinessVoucherUuid());
		assertEquals(repurchaseDoc.getRepurchaseDocUuid(),journalVoucher.getBillingPlanUuid());
		assertEquals(item.getAmount(),journalVoucher.getBookingAmount());
		assertEquals("pay_ac_no_1",journalVoucher.getLocalPartyAccount());
		assertEquals("测试用户1",journalVoucher.getLocalPartyName());
		
		assertEquals(virtualAccount.getVirtualAccountNo(),journalVoucher.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(),journalVoucher.getCounterPartyName());
		
		assertEquals(item.getOrderUuid(),journalVoucher.getBusinessVoucherUuid());
	}
	
	
	/**
	 * 还款订单商户代偿
	 */
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/recon_payment_order_app_pay2.sql"})
	public void test_check_and_Repayment_order_recover2(){
		String repaymentOrderUuid = "order_uuid_1";
		String repayment_order_item_uuid = "order_detail_uuid_1";
		String financialContractNo = "financial_contract_code_1";
		String ledgerBookNo = "yunxin_ledger_book";
		RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(repaymentOrderUuid,
				repayment_order_item_uuid, financialContractNo,
				ledgerBookNo, null);
		try {

			businessPaymentVoucherSession.handler_recover_asset_by_repayment_order();

		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid);
		RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		BigDecimal total_bookingAmount = new BigDecimal("1500");
		
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item.getRepaymentBusinessNo());
		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
		assertEquals("11111", assetSet.getVersionLock());
		assertEquals(DateUtils.parseDate("2017-06-15 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
		assertEquals(true,BigDecimal.ZERO.compareTo(extraData.getOrderPayingAmount())==0);
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		//校验余额
		assertEquals(new BigDecimal("10005").subtract(total_bookingAmount),virtualAccount.getTotalBalance());
		assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger book明细余额,不变
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
		assertEquals(0,BigDecimal.ZERO.compareTo(receivable_amount));
		
		
		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
		assertEquals(1,jvLists.size());
		JournalVoucher jv = jvLists.get(0);
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());
		assertEquals("order_uuid_1",jv.getBusinessVoucherUuid());
		assertEquals(assetSet.getAssetUuid(),jv.getBillingPlanUuid());
		assertEquals(item.getAmount(),jv.getBookingAmount());
		assertEquals("pay_ac_no_1",jv.getLocalPartyAccount());
		assertEquals("测试用户1",jv.getLocalPartyName());

		assertEquals(virtualAccount.getVirtualAccountNo(),jv.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(),jv.getCounterPartyName());
		
		
		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
		assertNotNull(order);

		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
		assertEquals(DateUtils.format(jv.getTradeTime()),DateUtils.format(order.getDueDate()));
		assertEquals(jv.getTradeTime(),order.getPayoutTime());
		assertEquals(jv.getBusinessVoucherUuid(),repaymentOrder.getOrderUuid());

		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(),"");
		assertEquals(2,ledgers.size());
		for (LedgerItem ledgerItem : ledgers) {
			assertEquals(repaymentOrderUuid,ledgerItem.getBusinessVoucherUuid());
		}

		VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		
		//校验ledger book明细余额,不变
		balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount_after.getTotalBalance()));
		
		
		//校验流水
		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
		assertEquals(1,virtualAccountFlowList.size());
		
		VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
		assertEquals(0,new BigDecimal("1500.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
		assertEquals(jvLists.get(0).getJournalVoucherUuid(),virtualAccountFlow1.getBusinessDocumentUuid());
		assertEquals(virtualAccount_after.getVirtualAccountNo(),virtualAccountFlow1.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherNo(),virtualAccountFlow1.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow1.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow1.getTransactionType());
		assertEquals(virtualAccount_after.getFstLevelContractUuid(),virtualAccountFlow1.getFinancialContractUuid());
		assertEquals(item.getAmount(),virtualAccountFlow1.getTransactionAmount());
		assertEquals(virtualAccount_after.getVirtualAccountUuid(),virtualAccountFlow1.getVirtualAccountUuid());
		assertEquals(virtualAccount_after.getVirtualAccountAlias(),virtualAccountFlow1.getVirtualAccountAlias());
		
		assertEquals(DetailPayStatus.PAID, item.getDetailPayStatus());
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("firstAccountName", ChartOfAccount.FST_BANK_SAVING);
		params.put("sourceDocumentUuid", repaymentOrderUuid);
		List<LedgerItem> bankledgerItems = genericDaoSupport.searchForList("From LedgerItem where firstAccountName=:firstAccountName and sourceDocumentUuid=:sourceDocumentUuid",params);
		assertEquals(1,bankledgerItems.size());
		assertEquals(0,total_bookingAmount.compareTo(bankledgerItems.get(0).getCreditBalance()));
		
		
		assertEquals(OrderRecoverStatus.PAY_END,repaymentOrder.getOrderRecoverStatus());
		assertEquals(OrderRecoverResult.ALL,repaymentOrder.getOrderRecoverResult());
		assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repaymentOrder.getOrderLastModifiedTime()));
	}
	
	
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/recon_payment_order_app_pay3.sql"})
	public void test_check_and_Repayment_order_recover3(){
		String repaymentOrderUuid = "order_uuid_1";
		String repayment_order_item_uuid = "order_detail_uuid_1";
		String financialContractNo = "financial_contract_code_1";
		String ledgerBookNo = "yunxin_ledger_book";
		RepaymentOrderReconciliationParameters reconparams = new RepaymentOrderReconciliationParameters(repaymentOrderUuid,
				repayment_order_item_uuid, financialContractNo,
				ledgerBookNo, null);
		try {

			dstJobRepaymentOrderReconciliation.repayment_order_recover_details(Arrays.asList(reconparams));

		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		BigDecimal total_bookingAmount = new BigDecimal("1500");
		
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item.getRepaymentBusinessNo());
		assertEquals(AssetClearStatus.UNCLEAR,assetSet.getAssetStatus());
		assertEquals(OrderPaymentStatus.ORDERPAYMENTING,assetSet.getOrderPaymentStatus());
		Assert.assertNotEquals("11111", assetSet.getVersionLock());
		//assertEquals(DateUtils.parseDate("2017-06-15 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
///////		assertEquals(new BigDecimal("500.00"),extraData.getOrderPayingAmount());
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		
		//校验ledger book明细余额,不变
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
		assertEquals(0,new BigDecimal(500.00).compareTo(receivable_amount));
		
		
		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
		RepaymentOrder repaymentOrder=repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
		assertEquals(1,jvLists.size());
		JournalVoucher jv = jvLists.get(0);
		//资金入账时间
		assertEquals(false, repaymentOrder.getCashFlowTime()==null);
		//assertEquals(jv.getCashFlowTime(), repaymentOrder.getCashFlowTime());
		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());
		assertEquals("order_uuid_1",jv.getBusinessVoucherUuid());
		assertEquals(assetSet.getAssetUuid(),jv.getBillingPlanUuid());
		assertEquals(item.getAmount(),jv.getBookingAmount());
		assertEquals("pay_ac_no_1",jv.getLocalPartyAccount());
		assertEquals("测试用户1",jv.getLocalPartyName());
		
		assertEquals(virtualAccount.getVirtualAccountNo(),jv.getCounterPartyAccount());
		assertEquals(virtualAccount.getOwnerName(),jv.getCounterPartyName());
		
		
		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
		assertNotNull(order);

		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
		assertEquals(DateUtils.format(jv.getTradeTime()),DateUtils.format(order.getDueDate()));
		assertEquals(jv.getTradeTime(),order.getPayoutTime());
		

//		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid());
//		assertEquals(1,ledgers.size());

	
		VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		
		//校验ledger book明细余额,不变
		balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount_after.getTotalBalance()));
		
		
		//校验流水
		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
		assertEquals(1,virtualAccountFlowList.size());
		
		VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
		assertEquals(0,new BigDecimal("1000.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
		assertEquals(jvLists.get(0).getJournalVoucherUuid(),virtualAccountFlow1.getBusinessDocumentUuid());
		assertEquals(virtualAccount_after.getVirtualAccountNo(),virtualAccountFlow1.getVirtualAccountNo());
		assertEquals(jvLists.get(0).getJournalVoucherNo(),virtualAccountFlow1.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow1.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow1.getTransactionType());
		assertEquals(virtualAccount_after.getFstLevelContractUuid(),virtualAccountFlow1.getFinancialContractUuid());
		assertEquals(item.getAmount(),virtualAccountFlow1.getTransactionAmount());
		assertEquals(virtualAccount_after.getVirtualAccountUuid(),virtualAccountFlow1.getVirtualAccountUuid());
		assertEquals(virtualAccount_after.getVirtualAccountAlias(),virtualAccountFlow1.getVirtualAccountAlias());
		
		assertEquals(DetailPayStatus.PAID, item.getDetailPayStatus());
		
	}
	
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
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
//		
//		RepaymentOrderItem item1 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid1);
//		RepaymentOrderItem item2 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid2);
//		
//		String borrower_customerUuid = "customerUuid1";
//		String company_customerUuid = "company_customer_uuid_1";
//		BigDecimal total_bookingAmount = new BigDecimal("500");
//		String repaymentPlanDateString = "2017-06-15 10:10:10";
//		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
//		
//		//校验资产状态
//		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item1.getRepaymentBusinessNo());
//		
//		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
//		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
//		Assert.assertNotEquals("11111", assetSet.getVersionLock());
//		assertEquals(OnAccountStatus.WRITE_OFF, assetSet.getOnAccountStatus());
//		assertEquals(DateUtils.parseDate("2017-06-15 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());//资产实际回收时间
//		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
//		
//		//extra Data
//		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
//		assertEquals(new BigDecimal("0"),extraData.getOrderPayingAmount());
//		
//		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
//		
//		//校验ledger book明细余额,不变
//		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
//		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
//		
//		//校验ledger_book的应收资产
//		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
//		assertEquals(0,new BigDecimal(0).compareTo(receivable_amount));
//		
//		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
//		assertEquals(2,jvLists.size());
//		JournalVoucher jv1 = jvLists.get(0);
//		JournalVoucher jv2 = jvLists.get(1);
//		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv1.getJournalVoucherType());
//		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv2.getJournalVoucherType());
//		
//		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv1.getStatus());
//		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv2.getStatus());
//		
//		assertEquals(assetSet.getAssetUuid(),jv1.getBillingPlanUuid());
//		assertEquals(assetSet.getAssetUuid(),jv2.getBillingPlanUuid());
//		
//		assertEquals(item1.getAmount(),jv1.getBookingAmount());
//		assertEquals(item2.getAmount(),jv2.getBookingAmount());
//		
//		
//		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
//		assertNotNull(order);
//
//		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
//		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
//		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getDueDate()));
//		assertEquals(repaymentPlanDateString,order.getPayoutTime());
//		
//
//		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid());
//		assertEquals(2,ledgers.size());										
//	
//		VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
//		
//		//校验ledger book明细余额
//		balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
//		assertEquals(0,balance.compareTo(virtualAccount_after.getTotalBalance()));
//		
//		assertEquals(DetailPayStatus.PAID, item1.getDetailPayStatus());
//		
//		String SourceDocumentUuid="source_document_uuid_1";
//		SourceDocument source_document=sourceDocumentService.getSourceDocumentBy(SourceDocumentUuid);
//		assertEquals(SourceDocumentStatus.SIGNED,source_document.getSourceDocumentStatus());
//		String deductApplicationUuid="deduct_application_uuid_1";
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
				sessionFactory.getCurrentSession().flush();
				sessionFactory.getCurrentSession().clear();
	}
	@Autowired
	private SessionFactory sessionFactory;
	
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
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		
		String outlierDocumentUuid="deduct_application_uuid_1";
		String firstOutlierDocType=SourceDocument.FIRSTOUTLIER_PAYMENT_ORDER;//payment_order;
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
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		
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