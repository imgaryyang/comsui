package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.persistence.support.Filter;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.RepaymentPlanRecoverHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.OrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
//@Transactional
@Rollback(false)
public class RepaymentPlanRecoverHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;

	@Autowired
	private OrderHandler orderHandler;

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

	private Map<String, FinancialContract> financialContractsMap = new HashMap<String, FinancialContract>();
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private RepaymentPlanRecoverHandler repaymentPlanRecoverHandler;
	@Autowired
	private DeductApplicationService deductApplicationService;
	@Autowired
	private SessionFactory sessionFactory;

	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/repaymentOrder/repayment_plan_recover.sql"})
	public void testthirdPartyVoucherRepaymentOrderWithReconciliationNoSessionImpl(){
		String contractUuid="contract_uuid_1";
		String repaymentOrderUuid="order_uuid_1";

		for (Map.Entry<String,String> Taccount : ExtraChargeSpec.ria_mapping.entrySet()){
			System.err.println("Key:"+Taccount.getKey()+"<---->Value:"+Taccount.getValue());
		}
		try{
			String asset_set_uuid = "asset_uuid_1";
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(asset_set_uuid);
			repaymentPlanRecoverHandler.recover_received_in_advance(assetSet);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}

		String outlierDocumentUuid="deduct_application_uuid_1";
		String firstOutlierDocType= SourceDocument.FIRSTOUTLIER_PAYMENT_ORDER;//payment_order;
		SourceDocument sourcedocument = sourceDocumentService.getSourceDocumentByOutlierDocumentUuid(outlierDocumentUuid, firstOutlierDocType);
//		assertNotNull(sourcedocument);
		
		String sourcedocumentDetailUuid_1=sourcedocument.getUuid();
		List<SourceDocumentDetail> sourcedocumentdetaillist=sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourcedocumentDetailUuid_1);
//		assertEquals(1,sourcedocumentdetaillist.size());
		
		String sourceDocumentDetailUuid_1 =sourcedocumentdetaillist.get(0).getUuid();
		List<JournalVoucher> journalVoucherList_1 =journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetailUuid_1);
//		assertEquals(1,journalVoucherList_1.size());
		
		
		String repayment_order_item_uuid1="order_detail_uuid_1";
		String repayment_order_item_uuid2="order_detail_uuid_2";

		RepaymentOrderItem item1 = repaymentOrderItemService.getRepaymentOrderItemByUuid(repayment_order_item_uuid1);
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		
		String ledgerBookNo="yunxin_ledger_book";
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		//校验资产状态
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(item1.getRepaymentBusinessNo());
		
//		assertEquals(AssetClearStatus.UNCLEAR,assetSet.getAssetStatus());
//		assertEquals(OrderPaymentStatus.UNEXECUTINGORDER,assetSet.getOrderPaymentStatus());
//		Assert.assertNotEquals("11111", assetSet.getVersionLock());
//		assertEquals(OnAccountStatus.PART_WRITE_OFF, assetSet.getOnAccountStatus());
//		assertEquals(DateUtils.parseDate("2017-06-15 10:10:10", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());//资产实际回收时间
		CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		
		//extra Data
		RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetSet.getAssetUuid());
//		assertEquals(0,new BigDecimal("0").compareTo(extraData.getOrderPayingAmount()));
		
		//校验ledger_book的应收资产
		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
//		assertEquals(0,new BigDecimal(100.00).compareTo(receivable_amount));
		
		List<JournalVoucher> jvLists = journalVoucherService.list(JournalVoucher.class,new Filter());
//		assertEquals(1,jvLists.size());
		JournalVoucher jv1 = jvLists.get(0);
//		assertEquals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER,jv1.getJournalVoucherType());
		
//		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv1.getStatus());
		
//		assertEquals(CounterAccountType.BankAccount,jv1.getCounterAccountType());
//		assertEquals(AccountSide.DEBIT,jv1.getAccountSide());
		
//		assertEquals(assetSet.getAssetUuid(),jv1.getBillingPlanUuid());
		
//		assertEquals(item1.getAmount(),jv1.getBookingAmount());
		
		Map<String,BigDecimal> detailAmount = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(ledgerBookNo, jv1.getJournalVoucherUuid(), assetSet.getAssetUuid());
//		assertEquals(0,new BigDecimal("400").compareTo(detailAmount.getOrDefault(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, BigDecimal.ZERO)));
//		assertEquals(0,new BigDecimal("0").compareTo(detailAmount.getOrDefault(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, BigDecimal.ZERO)));
		
		String repaymentPlanDateString="2017-06-15 10:10:10";
		
		Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
//		assertEquals(item1.getAmount(),order.getTotalRent());
//		assertEquals(assetSet,order.getAssetSet());
//		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString, DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getDueDate()));
//		assertEquals(DateUtils.format(DateUtils.parseDate(repaymentPlanDateString,DateUtils.LONG_DATE_FORMAT)),DateUtils.format(order.getPayoutTime()));
//		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
//		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());

		List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
	//	assertEquals(1,ledgers.size());
	
	//	VirtualAccount virtualAccount_after = virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customerUuid);
		
		
		
		assertEquals(DetailPayStatus.PAID, item1.getDetailPayStatus());
		assertEquals(SourceDocumentStatus.SIGNED,sourcedocument.getSourceDocumentStatus());
		String deductApplicationUuid="deduct_application_uuid_1";
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
		assertEquals(RecordStatus.WRITE_OFF,deductApplication.getRecordStatus());
	}
	
}
