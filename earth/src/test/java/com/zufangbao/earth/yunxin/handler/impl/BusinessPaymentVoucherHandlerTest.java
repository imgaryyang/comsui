package com.zufangbao.earth.yunxin.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.handler.BusinessPaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.model.command.BusinessPaymentVoucherCommandModel;
import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.voucher.CashFlowNotExistException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountFlow;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.entity.refund.TmpDepositStatus;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.entity.voucher.CreateBusinessPaymentVoucherModel;
import com.zufangbao.sun.entity.voucher.TemplatesForAll;
import com.zufangbao.sun.entity.voucher.TemplatesForPay;
import com.zufangbao.sun.entity.voucher.TemplatesForRepurchase;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.*;

import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.audit.ClearingCashFlowMode;
import com.zufangbao.sun.yunxin.entity.model.voucher.AppAccountModelForVoucherController;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAccountInfoModel;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Rollback(false)
@Transactional
public class BusinessPaymentVoucherHandlerTest extends
	AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private BusinessPaymentVoucherHandler businessPaymentVoucherHandler;
	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private VirtualAccountFlowService virtualAccountFlowService;
	@Autowired
	private BusinessPaymentVoucherSession businessPaymentVoucherSession;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private RepurchaseService repurchaseServicep;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private CashFlowHandler cashFlowHandler;
	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private TemporaryDepositDocService temporaryDepositDocService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void aa(){
		
	}
	
	
//	@Test
//	@Sql("classpath:test/yunxin/businessPaymentVoucher/outer_transfer_assets_by_business_pay_voucher.sql")
//	public void test_outer_transfer_loan_assets_full(){
//		SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.load(SourceDocumentDetail.class, 1L);
//		String ledgerBookNo = "yunxin_ledger_book";
//		String assetSetUuid = "asset_uuid_1";
//		String borrower_customerUuid = "customerUuid1";
//		String company_customerUuid = "company_customer_uuid_1";
//		BigDecimal bookingAmount = new BigDecimal("1000");
//		VirtualAccount companyVirtualAccount = virtualAccountService.load(VirtualAccount.class, 1L);
//		String sourcreDocumentNo = "sourcreDocumentNo";
//		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
//		try {
//			
//			businessPaymentVoucherHandler.compensatory_recover_loan_asset_detail(new Long(1), sourcreDocumentNo, book, companyVirtualAccount, null);
//		} catch(Exception e){
//			e.printStackTrace();
//			fail();
//		}
//
//		//校验明细状态
//		assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());
//		//校验资产状态
//		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
//		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
//		
//		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
//		//校验余额不变
//		assertEquals(0,new BigDecimal("10005").compareTo(virtualAccount.getTotalBalance()));
//		
//		//校验ledger book明细余额,不变
//		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
//		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
//		
//		//校验ledger_book的应收资产
//		BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSetUuid, borrower_customerUuid);
//		assertEquals(0,BigDecimal.ZERO.compareTo(receivable_amount));
//		
//		//校验jv
//		List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
//		assertEquals(1,jvList.size());
//		JournalVoucher jv = jvList.get(0);
//		assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
//		assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());
//		assertEquals(0,sourceDocumentDetail.getAmount().compareTo(jv.getBookingAmount()));
//		
//		//校验流水,无流水
//		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
//		assertEquals(0,virtualAccountFlowList.size());
//	}
	
	
	@Test
	@Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql"})
	public void  test_businessPaymentVoucherNoDoc(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("500"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("600"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("500"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("486411356");
		model.setPaymentAccountNo("146474156");
		model.setPaymentName("张三");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("serial_no_1");
		model.setDetail(jsonString);
		model.setCheckFailedMsg("校验失败信息");
		
		try
		{
		List<CashFlow> cashFlowList = businessPaymentVoucherHandler.businessPaymentVoucher(model,"127.0.0.1");
		fail();
		}
		catch(ApiException e )
		{
			assertTrue(true);	
		}
		
	}
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
	public void  test_businessPaymentVoucherHaveDocumentNoDetail(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("0.10"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("0.10"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("0.10"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("955103657777777");
		model.setPaymentAccountNo("serial_no");
		model.setPaymentName("张建明");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("112049");
		model.setDetail(jsonString);
		model.setCheckFailedMsg("校验失败信息");
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(1L, "batch_pay_record_uuid_1", "xxx");
		
		List<SourceDocumentDetail> details1 = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(0, details1.size());
		List<CashFlow> cashFlowList = businessPaymentVoucherHandler.businessPaymentVoucher(model,"127.0.0.1");
		SourceDocument sourceDocument2 = sourceDocumentService.getSourceDocumentBy(1L, cashFlowList.get(0).getCashFlowUuid(), "xxx");
		
		List<SourceDocumentDetail> details2 = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument2.getSourceDocumentUuid(), sourceDocument2.getOutlierSerialGlobalIdentity());
		assertEquals(1, cashFlowList.size());
		//assertEquals(1, details2.size());
		
	
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
	public void  test_businessPaymentVoucherHave2CashFlow(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("0.10"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("0.10"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("0.10"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("955103657777777");
		model.setPaymentAccountNo("serial_no_3");
		model.setPaymentName("张建明");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("1122049");
		model.setDetail(jsonString);
		model.setCheckFailedMsg("校验失败信息");
		List<CashFlow> cashFlowList1 = cashFlowService.getCashFlowListBy("serial_no_3", "张建明", new BigDecimal("0.10"));
		assertEquals(2, cashFlowList1.size());
		try {
			List<CashFlow> cashFlowList = businessPaymentVoucherHandler.businessPaymentVoucher(model,"127.0.0.1");
			assertEquals(2,cashFlowList.size());
		} catch (ApiException e) {
			fail();
			e.printStackTrace();
		}
		catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
		
		
	
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
	public void  test_businessPaymentVoucherHaveDetail(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("0.10"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("0.10"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("0.10"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("156a15");
		model.setPaymentAccountNo("serial_no_2");
		model.setPaymentName("张建明");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("1122049");
		model.setDetail(jsonString);
		model.setCheckFailedMsg("校验失败信息");
		try {
			List<CashFlow> cashFlowList = businessPaymentVoucherHandler.businessPaymentVoucher(model,"127.0.0.1");
			fail();
		} catch (ApiException e) {
			assertTrue(true);
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
	public void  test_businessPaymentVoucherDetail(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("0.10"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("0.10"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		detail1.setTransactionTime("2016-03-30 18:23-00");
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("0.10"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("156a15");
		model.setPaymentAccountNo("serial_no_2");
		model.setPaymentName("张建明");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("1122049");
		model.setDetail(jsonString);
//		boolean  result=model.checkSubmitParams();
//		assertEquals(false,result);
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
	public void  test_businessPaymentVoucherDetail2(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("0.10"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("0.10"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		detail1.setTransactionTime("");
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("0.10"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("156a15");
		model.setPaymentAccountNo("serial_no_2");
		model.setPaymentName("张建明");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("1122049");
		model.setDetail(jsonString);
//		boolean  result=model.checkSubmitParams();
//		assertEquals(true,result);
	}
	
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
	public void  test_businessPaymentVoucherDetail3(){
		List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
		
		BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
		detail.setAmount(new BigDecimal("0.10"));
		detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail.setRepaymentPlanNo("plan_no1");
		detail.setPayer(0);
		
		BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
		detail1.setAmount(new BigDecimal("0.10"));
		detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
		detail1.setRepaymentPlanNo("plan_no1");
		detail1.setPayer(0);
		detail1.setTransactionTime(null);
		details.add(detail1);
		String jsonString = JsonUtils.toJsonString(details);
		
		BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
		model.setFn("10002");
		model.setRequestNo("1323456");
		model.setTransactionType(0);
		model.setVoucherType(0);
		model.setVoucherAmount(new BigDecimal("0.10"));
		model.setFinancialContractNo("YX_AMT_001");
		model.setReceivableAccountNo("156a15");
		model.setPaymentAccountNo("serial_no_2");
		model.setPaymentName("张建明");
		model.setPaymentBank("招商银行");
		model.setBankTransactionNo("1122049");
		model.setDetail(jsonString);
//		boolean  result=model.checkSubmitParams();
//		assertEquals(true,result);
	}


    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_business_payment_voucher.sql")
    public void test_businessPaymentVoucherDetail4() {
        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();

        BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
        detail.setAmount(new BigDecimal("0.10"));
        detail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        detail.setRepaymentPlanNo("plan_no1");
        detail.setPayer(0);

        BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
        detail1.setAmount(new BigDecimal("0.10"));
        detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        detail1.setRepaymentPlanNo("plan_no1");
        detail1.setPayer(0);
        detail1.setTransactionTime("0001-01-01 00:00:00");
        details.add(detail1);
        String jsonString = JsonUtils.toJsonString(details);

        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setFn("10002");
        model.setRequestNo("1323456");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setVoucherAmount(new BigDecimal("0.10"));
        model.setFinancialContractNo("YX_AMT_001");
        model.setReceivableAccountNo("156a15");
        model.setPaymentAccountNo("serial_no_2");
        model.setPaymentName("张建明");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("1122049");
        model.setDetail(jsonString);
        //boolean  result=model.checkSubmitParams();
        //assertEquals(true,result);
    }

	/*
	 * 商户付款  代偿   核销
	 */
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher.sql")
	//@Ignore("不明原因，等待后续修复")
	public void test_check_and_transfer_asstes(){
		try {
			
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class,1L);
		assertEquals(SourceDocumentExcuteResult.SUCCESS,sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE,sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(2,details.size());
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		BigDecimal total_bookingAmount = new BigDecimal("1500");
		
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		
		List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
		assertEquals(2,jvList.size());
		
		for (SourceDocumentDetail sourceDocumentDetail : details) {
			assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());
			
			//校验明细状态
			assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());
			//校验资产状态
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
			assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
			assertEquals(DateUtils.parseDate("2016-10-27 20:00:17", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());
			CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
			assertEquals(cashFlow.getTransactionTime(),assetSet.getActualRecycleDate());

			VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
			//校验余额
			assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
			
			//校验ledger book明细余额,不变
			BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
			assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
			
			//校验ledger_book的应收资产
			BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
			assertEquals(0,BigDecimal.ZERO.compareTo(receivable_amount));
			
			
			List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
			assertEquals(1,jvLists.size());
			JournalVoucher jv = jvLists.get(0);
			assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
			assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());

            Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
            assertNotNull(order);

            assertEquals(OrderClearStatus.CLEAR, order.getClearingStatus());
            assertEquals(ExecutingSettlingStatus.SUCCESS, order.getExecutingSettlingStatus());
            assertEquals(DateUtils.format(jv.getTradeTime()), DateUtils.format(order.getDueDate()));
            assertEquals(jv.getTradeTime(), order.getPayoutTime());
            assertEquals(sourceDocument.getOutlierAccount(), jv.getSourceDocumentLocalPartyAccount());
            assertEquals(sourceDocument.getOutlieAccountName(), jv.getSourceDocumentLocalPartyName());
            assertEquals("10001", jv.getLocalPartyAccount());
            assertEquals("测试7", jv.getLocalPartyName());

            assertEquals(sourceDocumentDetail.getPaymentAccountNo(), jv.getSourceDocumentCounterPartyAccount());
            assertEquals(sourceDocumentDetail.getPaymentName(), jv.getSourceDocumentCounterPartyName());
            assertEquals(virtualAccount.getVirtualAccountNo(), jv.getCounterPartyAccount());
            assertEquals(virtualAccount.getOwnerName(), jv.getCounterPartyName());


            assertEquals(0, sourceDocumentDetail.getAmount().compareTo(jv.getBookingAmount()));

            List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
            assertEquals(1, ledgers.size());
            assertEquals(0, sourceDocumentDetail.getAmount().compareTo(ledgers.get(0).getDebitBalance()));
        }


        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
        //校验余额不变
        assertEquals(0, new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));

        //校验ledger book明细余额,不变
        BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
        assertEquals(0, balance.compareTo(virtualAccount.getTotalBalance()));


        //校验流水
        List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
        assertEquals(2, virtualAccountFlowList.size());

        VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
        assertEquals(0, new BigDecimal("1000.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
        assertEquals(jvList.get(0).getJournalVoucherUuid(), virtualAccountFlow1.getBusinessDocumentUuid());
        assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow1.getVirtualAccountNo());
        assertEquals(jvList.get(0).getJournalVoucherNo(), virtualAccountFlow1.getBusinessDocumentNo());
        assertEquals(AccountSide.CREDIT, virtualAccountFlow1.getAccountSide());
        assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow1.getTransactionType());
        assertEquals(virtualAccount.getFstLevelContractUuid(), virtualAccountFlow1.getFinancialContractUuid());

        VirtualAccountFlow virtualAccountFlow2 = virtualAccountFlowList.get(1);
        assertEquals(0, new BigDecimal("500.00").compareTo(virtualAccountFlow2.getTransactionAmount()));
        assertEquals(jvList.get(1).getJournalVoucherUuid(), virtualAccountFlow2.getBusinessDocumentUuid());
        assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow2.getVirtualAccountNo());
        assertEquals(jvList.get(1).getJournalVoucherNo(), virtualAccountFlow2.getBusinessDocumentNo());
        assertEquals(AccountSide.CREDIT, virtualAccountFlow2.getAccountSide());
        assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow2.getTransactionType());
        assertEquals(virtualAccount.getFstLevelContractUuid(), virtualAccountFlow2.getFinancialContractUuid());


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("firstAccountName", ChartOfAccount.FST_BANK_SAVING);
        params.put("sourceDocumentUuid", sourceDocument.getSourceDocumentUuid());
        List<LedgerItem> bankledgerItems = genericDaoSupport.searchForList("From LedgerItem where firstAccountName=:firstAccountName and sourceDocumentUuid=:sourceDocumentUuid", params);
        assertEquals(2, bankledgerItems.size());
        assertEquals(0, total_bookingAmount.compareTo(bankledgerItems.get(0).getCreditBalance().add(bankledgerItems.get(1).getCreditBalance())));
    }


    /*
     * 商户付款  代偿   校验实际还款时间 （入账时间-设定还款时间 大于七天）
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher2.sql")
    //@Ignore("不明原因，等待后续修复")
    public void test_check_and_transfer_asstes2() {


        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        assertEquals(SourceDocumentExcuteResult.UNSUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.PREPARE, sourceDocument.getExcuteStatus());

        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(2, details.size());

        for (SourceDocumentDetail sourceDocumentDetail : details) {

            assertEquals(SourceDocumentDetailCheckState.CHECK_FAILS, sourceDocumentDetail.getCheckState());
        }


    }

    /*
     * 商户付款  代偿   校验实际还款时间  （入账时间小于设定还款时间）
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_repayment_cycle_date.sql")
    //@Ignore("不明原因，等待后续修复")
    public void test_check_and_transfer_asstes_repayment_cycle_date() {


        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        assertEquals(SourceDocumentExcuteResult.UNSUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.PREPARE, sourceDocument.getExcuteStatus());

        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(2, details.size());

        for (SourceDocumentDetail sourceDocumentDetail : details) {

            assertEquals(SourceDocumentDetailCheckState.CHECK_FAILS, sourceDocumentDetail.getCheckState());
        }


    }

    /*
     * 商户付款  代偿   校验实际还款时间  （设定还款时间大于合同起始日）
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_business_pay_voucher_repayment_cycle_date_contract_begin_date.sql")
    //@Ignore("不明原因，等待后续修复")
    public void test_check_asstes_repayment_cycle_date_and_contract_begin_date() {


        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        assertEquals(SourceDocumentExcuteResult.UNSUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.PREPARE, sourceDocument.getExcuteStatus());

        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(2, details.size());

        for (SourceDocumentDetail sourceDocumentDetail : details) {

            assertEquals(SourceDocumentDetailCheckState.CHECK_FAILS, sourceDocumentDetail.getCheckState());
        }


    }


    /*
     * 回购销账
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_repurchase_by_business_pay_voucher.sql")
    //@Ignore("不明原因，等待后续修复")
    public void test_check_repurchase_write_off() {
        try {
            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        RepurchaseDoc repurchaseDoc = repurchaseServicep.load(RepurchaseDoc.class, 1L);
        BigDecimal amount = repurchaseDoc.getAmount();
        assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(1, details.size());
        SourceDocumentDetail sourceDocumentDetail = details.get(0);

        String contractUuid = "contract_uuid_1";
        String ledgerBookNo = "yunxin_ledger_book";
        String customerUuid = "company_customer_uuid_1";
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        //sourceDocumentDetail 和  repurchaseDoc 状态更改
        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
        assertEquals(RepurchaseStatus.REPURCHASED, repurchaseDoc.getRepurchaseStatus());
        assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getLastModifedTime()));
        assertEquals(Boolean.TRUE, DateUtils.isSameDay(new Date(), repurchaseDoc.getVerificationTime()));

        List<LedgerItem> items = ledgerItemService.list(LedgerItem.class, new Filter());
        assertEquals(11, items.size());

        //校验 repurchase
        BigDecimal repurchase_amount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contractUuid);
        assertEquals(0, BigDecimal.ZERO.compareTo(repurchase_amount));

        //校验ledger_book的应收资产
        BigDecimal receivable = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, "", customerUuid);
		assertEquals(0,BigDecimal.ZERO.compareTo(receivable));

        //校验JournalVoucher
        List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
        assertEquals(1, jvLists.size());

        //校验virtualAccount
        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
        assertEquals(0, new BigDecimal("9505.00").compareTo(virtualAccount.getTotalBalance()));

        //校验virtualAccountFlow
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

    }

    /*************** TEST businessPaymentVoucher START ***************/
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher_repeatRequestNo() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_1");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("请求编号重复!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher_financialContractNotExist() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_12");//数据库中无数据
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("test_transaction_no_1");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("收款账户错误，收款账户不是贷款合同的回款账户!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher_noSuchVoucher() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setVoucherAmount(new BigDecimal("10000.00"));
        model.setBankTransactionNo("test_transaction_no_1");//流水已关联凭证
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.assertEquals("请求参数解析错误!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher_noSuchVoucherType() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(5);//不存在的凭证类型
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setVoucherAmount(new BigDecimal("10000.00"));
        model.setBankTransactionNo("test_transaction_no_2");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("不支持的凭证类型!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher_wrongFormat() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setVoucherAmount(new BigDecimal("10000.00"));
        model.setBankTransactionNo("test_transaction_no_2");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("请求参数解析错误!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher() {

        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
        BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
        detail1.setAmount(new BigDecimal("10000.00"));
        detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        detail1.setRepaymentPlanNo("plan_no1");
        detail1.setPayer(0);
        details.add(detail1);
        String jsonString = JsonUtils.toJsonString(details);

        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setVoucherAmount(new BigDecimal("10000.00"));
        model.setBankTransactionNo("test_transaction_no_2");
        model.setDetail(jsonString);
        String ip = "127.0.0.1";
        List<CashFlow> cashFlows = businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
        CashFlow cashFlow = cashFlows.get(0);

        Assert.assertEquals(model.getVoucherAmount(), cashFlow.getTransactionAmount());
        Assert.assertEquals(model.getPaymentAccountNo(), cashFlow.getCounterAccountNo());
        Assert.assertEquals(model.getPaymentName(), cashFlow.getCounterAccountName());

        List<Voucher> vouchers = voucherService.loadAll(Voucher.class);
        Voucher voucher = vouchers.get(0);
        assertEquals(cashFlow.getUuid(), voucher.getCashFlowUuid());
        assertEquals(cashFlow.getTransactionTime(), voucher.getTransactionTime());
        assertEquals(model.getVoucherAmount(), voucher.getAmount());

        List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
        for (SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails) {
            assertEquals(voucher.getUuid(), sourceDocumentDetail.getVoucherUuid());
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher_cashFlow_not_exist.sql")
    public void test_businessPaymentVoucher_cashFlow_not_exist() {
        try {
            List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
            BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
            detail1.setAmount(new BigDecimal("10000.00"));
            detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
            detail1.setRepaymentPlanNo("plan_no1");
            detail1.setPayer(0);
            details.add(detail1);
            String jsonString = JsonUtils.toJsonString(details);

            BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
            model.setRequestNo(UUID.randomUUID().toString());
            model.setTransactionType(0);
            model.setVoucherType(0);
            model.setFinancialContractNo("G31700");
            model.setReceivableAccountNo("test_receivable_account_no_1");
            model.setPaymentAccountNo("test_payment_account_no_1");
            model.setPaymentName("test_payment_name_1");
            model.setPaymentBank("招商银行");
            model.setVoucherAmount(new BigDecimal("10000.00"));
            model.setBankTransactionNo("test_transaction_no_2");
            model.setDetail(jsonString);
            String ip = "127.0.0.1";
            List<CashFlow> cashFlows = businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof CashFlowNotExistException);
        }
    }


    /*************** TEST businessPaymentVoucher END ***************/

    /*************** TEST undoBusinessPaymentVoucher START ***************/
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher.sql")
    public void test_undoBusinessPaymentVoucher_repeatRequestNo() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_1");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("请求编号重复!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher.sql")
    public void test_undoBusinessPaymentVoucher_financialContractNotExist() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_12");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("test_transaction_no_2");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("信托合同不存在", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher.sql")
    public void test_undoBusinessPaymentVoucher_noSuchCashFlow() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("test_transaction_no_2");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.assertEquals("凭证对应流水不存在", ApiMessageUtil.getMessage(e.getCode()));
        }
    }


    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher_noDetails.sql")
    public void test_undoBusinessPaymentVoucher_noDetails() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("test_transaction_no_1");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("凭证对应流水不存在", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher_voucherCanotCancel.sql")
    public void test_undoBusinessPaymentVoucher_voucherCanotCancel() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("test_transaction_no_1");
        String ip = "127.0.0.1";
        try {
            businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, ip);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("当前凭证无法撤销!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_undoBusinessPaymentVoucher.sql")
    public void test_undoBusinessPaymentVoucher() {
        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo("test_request_no_2");
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setBankTransactionNo("test_transaction_no_1");
        String ip = "127.0.0.1";
        businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, ip);

        CashFlow cashFlow = cashFlowService.loadAll(CashFlow.class).get(0);
        assertTrue(StringUtils.isEmpty(cashFlow.getStringFieldOne()));

        SourceDocument sourceDocument = sourceDocumentService.loadAll(SourceDocument.class).get(0);
        assertTrue(StringUtils.isEmpty(sourceDocument.getVoucherUuid()));
//		assertEquals(BigDecimal.ZERO, sourceDocument.getPlanBookingAmount());

        List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
        for (SourceDocumentDetail detail : sourceDocumentDetails) {
            assertEquals(SourceDocumentDetailStatus.INVALID, detail.getStatus());
        }


    }
    /*************** TEST undoBusinessPaymentVoucher END ***************/

    /*************** test invalidSourceDocument start***************/
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_invalidSourceDocument.sql")
    public void test_invalidSourceDocument() {
        businessPaymentVoucherHandler.invalidSourceDocument(1l);

        SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.load(SourceDocumentDetail.class, 1l);
        assertEquals(SourceDocumentDetailStatus.INVALID, sourceDocumentDetail.getStatus());

//		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1l);
//		assertEquals(null, sourceDocument.getOutlierSerialGlobalIdentity());
    }
    /*************** test invalidSourceDocument end***************/

    /*************** test matchCashflow start ***************/
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_matchCashflow.sql")
    public void test_matchCashflow() {
        List<CashFlow> cashFlows = businessPaymentVoucherHandler.matchCashflow(1l);
        Assert.assertTrue(!CollectionUtils.isEmpty(cashFlows));
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_matchCashflow_voucherHasMatchCashFlow.sql")
    public void test_matchCashflow_voucherHasMatchCashFlow() {
        List<CashFlow> cashFlows = businessPaymentVoucherHandler.matchCashflow(1l);
        Assert.assertTrue(CollectionUtils.isEmpty(cashFlows));
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_matchCashflow_voucherAmountZero.sql")
    public void test_matchCashflow_voucherAmountZero() {
        List<CashFlow> cashFlows = businessPaymentVoucherHandler.matchCashflow(1l);
        Assert.assertTrue(CollectionUtils.isEmpty(cashFlows));
    }

    /*************** test matchCashflow end ***************/

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_connectionCashFlow_handler.sql")
    public void test_connectionCashFlow() {
        ContractActiveSourceDocumentMapper mapper = businessPaymentVoucherHandler.connectionCashFlow(1l, "test_cash_flow_uuid_1");
        assertTrue(mapper == null);
        SourceDocumentDetail detail = sourceDocumentDetailService.load(SourceDocumentDetail.class, 1l);
        assertEquals("test_source_document_uuid_1", detail.getSourceDocumentUuid());
    }


    /*
     * 商户付款核销    代偿  部分核销（ 按明细销）
     */
    @Test
    @Sql(value = {"classpath:test/yunxin/delete_all_table.sql", "classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_detail.sql"})
    //@Ignore("不明原因，等待后续修复")
    public void test_check_and_business_pay_detail() {
        try {
            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
//		assertEquals(SourceDocumentExcuteResult.SUCCESS,sourceDocument.getExcuteResult());
//		assertEquals(SourceDocumentExcuteStatus.DONE,sourceDocument.getExcuteStatus());
        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(1, details.size());

        String borrower_customerUuid = "customerUuid1";
        String company_customerUuid = "company_customer_uuid_1";
        String ledgerBookNo = "yunxin_ledger_book";
        BigDecimal total_bookingAmount = new BigDecimal("1500");

        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());

        List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
        assertEquals(1, jvList.size());

        //校验明细状态
        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
        //校验资产状态
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(details.get(0).getRepaymentPlanNo());
        assertEquals(AssetClearStatus.UNCLEAR, assetSet.getAssetStatus());
        assertEquals("2016-10-27 20:00:17", DateUtils.format(assetSet.getActualRecycleDate(), DateUtils.LONG_DATE_FORMAT));
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
//			assertEquals(null,assetSet.getActualRecycleDate());

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
        //校验余额
//			assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
//			assertEquals(0,virtualAccount.getTotalBalance());

        //校验ledger_book的应收资产
        BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
        assertEquals(new BigDecimal("405.00"), receivable_amount);


        List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(details.get(0).getUuid());
        assertEquals(1, jvLists.size());
        JournalVoucher jv = jvLists.get(0);
        assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, jv.getJournalVoucherType());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv.getStatus());

        List<LedgerItem> ledgers_jv = ledgerItemService.get_jv_asset_accountName_related_ledgers(book.getLedgerBookNo(), jv.getRelatedBillContractInfoLv3(), jvList.get(0).getJournalVoucherUuid(), Arrays.asList(ChartOfAccount.FST_BANK_SAVING));
        assertTrue(ledgers_jv.size() > 0);
        for (LedgerItem ledgerItem : ledgers_jv) {
            assertEquals(DateUtils.format(jv.getTradeTime(), DateUtils.LONG_DATE_FORMAT), DateUtils.format(ledgerItem.getCarriedOverDate(), DateUtils.LONG_DATE_FORMAT));
            assertEquals(cashFlow.getCashFlowIdentity(), ledgerItem.getRelatedLv3AssetUuid());
        }


        Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
        assertNotNull(order);

        assertEquals(OrderClearStatus.CLEAR, order.getClearingStatus());
        assertEquals(ExecutingSettlingStatus.SUCCESS, order.getExecutingSettlingStatus());
        assertEquals(DateUtils.format(jv.getTradeTime()), DateUtils.format(order.getDueDate()));
        assertEquals(jv.getTradeTime(), order.getPayoutTime());

        assertEquals(sourceDocument.getOutlierAccount(), jv.getSourceDocumentLocalPartyAccount());
        assertEquals(sourceDocument.getOutlieAccountName(), jv.getSourceDocumentLocalPartyName());
        assertEquals("10001", jv.getLocalPartyAccount());
        assertEquals("测试7", jv.getLocalPartyName());

        assertEquals(details.get(0).getPaymentAccountNo(), jv.getSourceDocumentCounterPartyAccount());
        assertEquals(details.get(0).getPaymentName(), jv.getSourceDocumentCounterPartyName());
        assertEquals(virtualAccount.getVirtualAccountNo(), jv.getCounterPartyAccount());
        assertEquals(virtualAccount.getOwnerName(), jv.getCounterPartyName());


        assertEquals(0, details.get(0).getAmount().compareTo(jv.getBookingAmount()));

        List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
        assertEquals(4, ledgers.size());
        BigDecimal total = BigDecimal.ZERO;
        for (LedgerItem ledgerItem : ledgers) {
            total = total.add(ledgerItem.getDebitBalance());
        }
        assertEquals(0, details.get(0).getAmount().compareTo(total));

    }


    /*
     * 商户付款核销    代偿   按明细销(两次)
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_detail_twice.sql")
    //@Ignore("不明原因，等待后续修复")
    public void test_check_and_business_pay_detail_twice() {
        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        SourceDocument sourceDocument2 = sourceDocumentService.load(SourceDocument.class, 2L);
        assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        List<SourceDocumentDetail> details2 = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument2.getSourceDocumentUuid(), sourceDocument2.getOutlierSerialGlobalIdentity());
        assertEquals(1, details.size());
        assertEquals(1, details2.size());

        String borrower_customerUuid = "customerUuid1";
        String company_customerUuid = "company_customer_uuid_1";
        String ledgerBookNo = "yunxin_ledger_book";
        BigDecimal total_bookingAmount = new BigDecimal("1500");

        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());

        List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
        assertEquals(2, jvList.size());

        //校验明细状态
        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
        assertEquals(SourceDocumentDetailStatus.SUCCESS, details2.get(0).getStatus());
        //校验资产状态
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(details.get(0).getRepaymentPlanNo());
        assertEquals(AssetClearStatus.CLEAR, assetSet.getAssetStatus());
        assertEquals(DateUtils.parseDate("2016-10-27 20:00:17", "yyyy-MM-dd HH:mm:ss"), assetSet.getActualRecycleDate());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
        assertEquals(cashFlow.getTransactionTime(), assetSet.getActualRecycleDate());

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);

        //校验ledger_book的应收资产
        BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
        assertEquals(new BigDecimal("0.00"), receivable_amount);


        List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(details.get(0).getUuid());
        List<JournalVoucher> jvLists2 = journalVoucherService.getJournalVoucherBySourceDocumentUuid(details2.get(0).getUuid());
        assertEquals(1, jvLists.size());
        JournalVoucher jv = jvLists.get(0);
        Order order = orderService.getOrder(jv.getRelatedBillContractNoLv4());
        assertNotNull(order);
        assertEquals(OrderClearStatus.CLEAR, order.getClearingStatus());
        assertEquals(ExecutingSettlingStatus.SUCCESS, order.getExecutingSettlingStatus());
        assertEquals(DateUtils.format(jv.getTradeTime()), DateUtils.format(order.getDueDate()));
        assertEquals(jv.getTradeTime(), order.getPayoutTime());
        assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, jv.getJournalVoucherType());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv.getStatus());

        assertEquals(sourceDocument.getOutlierAccount(), jv.getSourceDocumentLocalPartyAccount());
        assertEquals(sourceDocument.getOutlieAccountName(), jv.getSourceDocumentLocalPartyName());
        assertEquals("10001", jv.getLocalPartyAccount());
        assertEquals("测试7", jv.getLocalPartyName());

        assertEquals(details.get(0).getPaymentAccountNo(), jv.getSourceDocumentCounterPartyAccount());
        assertEquals(details.get(0).getPaymentName(), jv.getSourceDocumentCounterPartyName());
        assertEquals(virtualAccount.getVirtualAccountNo(), jv.getCounterPartyAccount());
        assertEquals(virtualAccount.getOwnerName(), jv.getCounterPartyName());


        assertEquals(0, details.get(0).getAmount().compareTo(jv.getBookingAmount()));
        assertEquals(0, details2.get(0).getAmount().compareTo(jvLists2.get(0).getBookingAmount()));

        List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
        assertEquals(13, ledgers.size());
        BigDecimal total = BigDecimal.ZERO;
        for (LedgerItem ledgerItem : ledgers) {
            total = total.add(ledgerItem.getDebitBalance());
        }
        assertEquals(0, details.get(0).getAmount().add(details2.get(0).getAmount()).compareTo(total));

    }

    /*
     * 商户付款核销    代偿   部分核销（不按明细）
     */
    @Test
    @Sql(value = {"classpath:test/yunxin/delete_all_table.sql", "classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_part.sql"})
    //@Ignore("不明原因，等待后续修复")
    public void test_check_and_business_pay_part() {
        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(1, details.size());

        String borrower_customerUuid = "customerUuid1";
        String company_customerUuid = "company_customer_uuid_1";
        String ledgerBookNo = "yunxin_ledger_book";

        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());

        List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
        assertEquals(1, jvList.size());

        //校验明细状态
        assertEquals(SourceDocumentDetailStatus.SUCCESS, details.get(0).getStatus());
        //校验资产状态
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(details.get(0).getRepaymentPlanNo());
        assertEquals(AssetClearStatus.UNCLEAR, assetSet.getAssetStatus());
        assertEquals(DateUtils.parseDate("2016-10-27 20:00:17", "yyyy-MM-dd HH:mm:ss"), assetSet.getActualRecycleDate());
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
        //校验余额
//			assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
//			assertEquals(0,virtualAccount.getTotalBalance());

        //校验ledger_book的应收资产
        BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
        assertEquals(new BigDecimal("420.00"), receivable_amount);


        List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(details.get(0).getUuid());
        assertEquals(1, jvLists.size());
        JournalVoucher jv = jvLists.get(0);
        assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, jv.getJournalVoucherType());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv.getStatus());

        Order order = orderService.getOrder(jv.getRelatedBillContractNoLv4());
        assertNotNull(order);
        assertEquals(OrderClearStatus.CLEAR, order.getClearingStatus());
        assertEquals(ExecutingSettlingStatus.SUCCESS, order.getExecutingSettlingStatus());
        assertEquals(DateUtils.format(jv.getTradeTime()), DateUtils.format(order.getDueDate()));
        assertEquals(jv.getTradeTime(), order.getPayoutTime());
        assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, jv.getJournalVoucherType());
        assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv.getStatus());

        assertEquals(sourceDocument.getOutlierAccount(), jv.getSourceDocumentLocalPartyAccount());
        assertEquals(sourceDocument.getOutlieAccountName(), jv.getSourceDocumentLocalPartyName());
        assertEquals("10001", jv.getLocalPartyAccount());
        assertEquals("测试7", jv.getLocalPartyName());

        assertEquals(details.get(0).getPaymentAccountNo(), jv.getSourceDocumentCounterPartyAccount());
        assertEquals(details.get(0).getPaymentName(), jv.getSourceDocumentCounterPartyName());
        assertEquals(virtualAccount.getVirtualAccountNo(), jv.getCounterPartyAccount());
        assertEquals(virtualAccount.getOwnerName(), jv.getCounterPartyName());


        assertEquals(0, details.get(0).getAmount().compareTo(jv.getBookingAmount()));

        List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
        assertEquals(3, ledgers.size());
        BigDecimal total = BigDecimal.ZERO;
        for (LedgerItem ledgerItem : ledgers) {
            total = total.add(ledgerItem.getDebitBalance());
        }
        assertEquals(0, details.get(0).getAmount().compareTo(total));

    }


    /*
     * 商户付款  担保凭证  核销
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_guarantee_voucher.sql")
    //@Ignore("不明原因，等待后续修复")
    public void test_check_and_recover_guarantee_voucher() {
        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Order guaranteeOrder = orderService.load(Order.class, 1L);
        assertEquals(OrderClearStatus.CLEAR, guaranteeOrder.getClearingStatus());

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        assertEquals(SourceDocumentExcuteResult.SUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.DONE, sourceDocument.getExcuteStatus());
        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(2, details.size());

        String borrower_customerUuid = "customerUuid1";
        String company_customerUuid = "company_customer_uuid_1";
        String ledgerBookNo = "yunxin_ledger_book";
        BigDecimal total_bookingAmount = new BigDecimal("1500");

        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);

        List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
        assertEquals(2, jvList.size());

        for (SourceDocumentDetail sourceDocumentDetail : details) {

            //校验明细状态
            assertEquals(SourceDocumentDetailStatus.SUCCESS, sourceDocumentDetail.getStatus());
            //校验资产状态
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
            assertEquals(GuaranteeStatus.HAS_GUARANTEE, assetSet.getGuaranteeStatus());

            //校验余额
            assertEquals(0, new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
            assertEquals(new BigDecimal("8505.00"), virtualAccount.getTotalBalance());

            //校验ledger book明细余额,不变
            BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
            assertEquals(0, balance.compareTo(virtualAccount.getTotalBalance()));

            //校验ledger_book的应收资产
            BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, "asset_uuid_1", borrower_customerUuid);
            assertEquals(0, BigDecimal.ZERO.compareTo(receivable_amount));


            List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
            assertEquals(1, jvLists.size());
            JournalVoucher jv = jvLists.get(0);
            assertEquals(JournalVoucherType.TRANSFER_BILL_BY_GUARANTEE_VOUCHER, jv.getJournalVoucherType());
            assertEquals(JournalVoucherStatus.VOUCHER_ISSUED, jv.getStatus());


            Order order = orderService.getOrder(jv.getRelatedBillContractNoLv4());
            assertNotNull(order);
            assertEquals(OrderClearStatus.CLEAR, order.getClearingStatus());
            assertEquals(ExecutingSettlingStatus.SUCCESS, order.getExecutingSettlingStatus());
            assertEquals(jv.getTradeTime(), order.getPayoutTime());

            assertEquals(sourceDocument.getOutlierAccount(), jv.getSourceDocumentLocalPartyAccount());
            assertEquals(sourceDocument.getOutlieAccountName(), jv.getSourceDocumentLocalPartyName());
            assertEquals("10001", jv.getLocalPartyAccount());
            assertEquals("测试7", jv.getLocalPartyName());
            assertEquals(new BigDecimal("1000.00"), jvList.get(0).getBookingAmount());
            assertEquals(new BigDecimal("500.00"), jvList.get(1).getBookingAmount());

            assertEquals(sourceDocumentDetail.getPaymentAccountNo(), jv.getSourceDocumentCounterPartyAccount());
            assertEquals(sourceDocumentDetail.getPaymentName(), jv.getSourceDocumentCounterPartyName());
            assertEquals(virtualAccount.getVirtualAccountNo(), jv.getCounterPartyAccount());
            assertEquals(virtualAccount.getOwnerName(), jv.getCounterPartyName());
            assertEquals(CounterAccountType.VirtualAccount, jv.getCounterAccountType());

            List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
            assertEquals(1, ledgers.size());
            assertEquals(0, sourceDocumentDetail.getAmount().compareTo(ledgers.get(0).getDebitBalance()));
        }


        //校验流水
        List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
        assertEquals(2, virtualAccountFlowList.size());

        VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
        assertEquals(0, new BigDecimal("1000.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
        assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow1.getVirtualAccountNo());
        assertEquals(jvList.get(0).getJournalVoucherNo(), virtualAccountFlow1.getBusinessDocumentNo());
        assertEquals(jvList.get(0).getJournalVoucherUuid(), virtualAccountFlow1.getBusinessDocumentUuid());
        assertEquals(AccountSide.CREDIT, virtualAccountFlow1.getAccountSide());
        assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow1.getTransactionType());
        assertEquals(virtualAccount.getFstLevelContractUuid(), virtualAccountFlow1.getFinancialContractUuid());

        VirtualAccountFlow virtualAccountFlow2 = virtualAccountFlowList.get(1);
        assertEquals(0, new BigDecimal("500.00").compareTo(virtualAccountFlow2.getTransactionAmount()));
        assertEquals(virtualAccount.getVirtualAccountNo(), virtualAccountFlow2.getVirtualAccountNo());
        assertEquals(jvList.get(1).getJournalVoucherNo(), virtualAccountFlow2.getBusinessDocumentNo());
        assertEquals(jvList.get(1).getJournalVoucherUuid(), virtualAccountFlow2.getBusinessDocumentUuid());
        assertEquals(AccountSide.CREDIT, virtualAccountFlow2.getAccountSide());
        assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE, virtualAccountFlow2.getTransactionType());
        assertEquals(virtualAccount.getFstLevelContractUuid(), virtualAccountFlow2.getFinancialContractUuid());


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("firstAccountName", ChartOfAccount.FST_BANK_SAVING);
        params.put("sourceDocumentUuid", sourceDocument.getSourceDocumentUuid());
        List<LedgerItem> bankledgerItems = genericDaoSupport.searchForList("From LedgerItem where firstAccountName=:firstAccountName and sourceDocumentUuid=:sourceDocumentUuid", params);
        assertEquals(4, bankledgerItems.size());

    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_clearing_cash_flow_related.sql")
    //@Ignore("请注意测试不通")
    public void test_getRelatedClearingCashFlow() {

        String auditJobUuid = "f0106ff3-92f6-4609-9415-9f88cfc43c62";
        List<ClearingCashFlowMode> clearingCashFlowModes = new ArrayList<ClearingCashFlowMode>();

        String cashFlowUuid = "5e1a7e88-0a07-11e7-bf99-00163e00283912erf";
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

        FinancialContract financialContract = financialContractService.getFinancialContractBy("b674a323-0c30-4e4b-9eba-b14e05a9d80a");

        String bankName = cashFlow.getCounterBankName();
        BigDecimal relatedAmount = cashFlow.getTransactionAmount().subtract(cashFlow.getIssuedAmount() == null ? BigDecimal.ZERO : cashFlow.getIssuedAmount());

        ClearingCashFlowMode clearingCashFlowMode = new ClearingCashFlowMode(cashFlow.getAccountSide().getOrdinal(), cashFlow.getTransactionVoucherNo(), cashFlow.getTransactionAmount(), null, bankName, cashFlow.getCounterAccountNo(), cashFlow.getCounterAccountName(), cashFlow.getRemark(), "", cashFlow.getOtherRemark(), cashFlow.getTransactionTime(), cashFlow.getBalance(),
                cashFlow.getBankSequenceNo(), cashFlow.getTransactionVoucherNo(), cashFlow.getCashFlowUuid(), relatedAmount);
        clearingCashFlowModes.add(clearingCashFlowMode);

        cashFlowHandler.relatedClearingCashFlow(auditJobUuid, clearingCashFlowModes);

        SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByOutlierDocumentUuid(cashFlowUuid, SourceDocument.FIRSTOUTLIER_CLEARING);

        String firstPartyId = financialContract.getCompany() == null ? "" : financialContract.getCompany().getUuid();
        String secondPartyId = financialContract == null ? "" : financialContract.getApp().getCompany().getUuid();

        Assert.assertEquals(financialContract.getCompany().getId(), sourceDocument.getCompanyId());
        Assert.assertEquals(firstPartyId, sourceDocument.getFirstPartyId());
        Assert.assertEquals(secondPartyId, sourceDocument.getSecondPartyId());
        Assert.assertEquals("", sourceDocument.getRelatedContractUuid());
        Assert.assertEquals("b674a323-0c30-4e4b-9eba-b14e05a9d80a", sourceDocument.getFinancialContractUuid());
        Assert.assertEquals(cashFlow.getStringFieldTwo(), sourceDocument.getOutlierMemo());
        Assert.assertEquals(clearingCashFlowMode.getRelatedAmount(), sourceDocument.getBookingAmount());

        Assert.assertEquals(SourceDocumentStatus.SIGNED, sourceDocument.getSourceDocumentStatus());
        Assert.assertEquals(cashFlow.getCashFlowUuid(), sourceDocument.getOutlierDocumentUuid());
        Assert.assertEquals(cashFlow.getTransactionTime(), sourceDocument.getOutlierTradeTime());
        Assert.assertEquals(cashFlow.getCounterAccountNo(), sourceDocument.getOutlierCounterPartyAccount());
        Assert.assertEquals(cashFlow.getCounterAccountName(), sourceDocument.getOutlierCounterPartyName());
        Assert.assertEquals(cashFlow.getHostAccountNo(), sourceDocument.getOutlierAccount());
        Assert.assertEquals(cashFlow.getHostAccountName(), sourceDocument.getOutlieAccountName());

        List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
        JournalVoucher journalVoucher = jvList.get(0);

        Assert.assertEquals(cashFlow.getCashFlowUuid(), journalVoucher.getCashFlowUuid());
        Assert.assertEquals(cashFlow.getCounterAccountNo(), journalVoucher.getCounterPartyAccount());
        Assert.assertEquals(cashFlow.getCounterAccountName(), journalVoucher.getCounterPartyName());
        Assert.assertEquals(cashFlow.getTransactionTime(), journalVoucher.getTradeTime());

        Assert.assertEquals(cashFlow.getBankSequenceNo(), journalVoucher.getCashFlowSerialNo());
        Assert.assertEquals(cashFlow.getCashFlowIdentity(), journalVoucher.getNotificationMemo());
        Assert.assertEquals(cashFlow.getTransactionAmount(), journalVoucher.getCashFlowAmount());
        Assert.assertEquals(cashFlow.getCashFlowChannelType(), journalVoucher.getCashFlowChannelType());

        Assert.assertEquals(sourceDocument.getSourceDocumentUuid(), journalVoucher.getSourceDocumentUuid());
        Assert.assertEquals(sourceDocument.getSourceDocumentNo(), journalVoucher.getSourceDocumentNo());
        Assert.assertEquals(sourceDocument.getOutlierDocumentUuid(), journalVoucher.getSourceDocumentIdentity());
        Assert.assertEquals(sourceDocument.getOutlierSerialGlobalIdentity(), journalVoucher.getSourceDocumentCashFlowSerialNo());
//		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.getTotalReceivableBillsByUuid("dfvbfb");
//		Assert.assertEquals("123",totalReceivableBills.getLastClearingCashFlowIdentity());
//		Assert.assertEquals(cashFlow.getTransactionTime(),totalReceivableBills.getLastClearingTime() );
    }

//	@Test
//	@Ignore("try")
//	public void a(){
//		Date date = new Date();
//		Date t_add_1_day = DateUtils.addDays(date, 1);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(t_add_1_day);
//
//		calendar.set(Calendar.SECOND, 00);
//		calendar.set(Calendar.MINUTE, 00);
//		calendar.set(Calendar.HOUR_OF_DAY, 11);
//
//		Assert.assertEquals(2017,calendar.get(Calendar.YEAR));
//		Assert.assertEquals(3,calendar.get(Calendar.MONTH)+1);
//		Assert.assertEquals(25,calendar.get(Calendar.DATE));
//		Assert.assertEquals(00,calendar.get(Calendar.SECOND));
//		Assert.assertEquals(00,calendar.get(Calendar.MINUTE));
//		Assert.assertEquals(11,calendar.get(Calendar.HOUR));
//
//		Date startTime = calendar.getTime();
//		calendar.add(Calendar.HOUR_OF_DAY, 1);
//		Date endTime = calendar.getTime();
//
//		Assert.assertEquals("2017-03-25 11:00:00",DateUtils.format(startTime,DateUtils.LONG_DATE_FORMAT));
//		Assert.assertEquals("2017-03-25 12:00:00",DateUtils.format(endTime,DateUtils.LONG_DATE_FORMAT));
//
//		Date a = calendar.getTime();
//		int i = 1;
//	}

    @Test
    public void b() {

        Integer dateCounter = new Integer(3);

        Date endTime = null;
        Calendar calendar = com.zufangbao.sun.utils.DateUtils.getTimeQuantumByDay(new Date(), 00, 00, 00);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);

        if (dateCounter.equals(new Integer(0))) {
            endTime = calendar.getTime();
        } else if (dateCounter.equals(new Integer(1))) {
            calendar.add(Calendar.DATE, 2);
            endTime = calendar.getTime();
        } else if (dateCounter.equals(new Integer(2))) {
            calendar.add(Calendar.DATE, 4);
            endTime = calendar.getTime();
        } else if (dateCounter.equals(new Integer(3))) {
            calendar.add(Calendar.DATE, 9);
            endTime = calendar.getTime();
        }

//		Assert.assertEquals("2017-04-11 00:00:00",DateUtils.format(startTime,DateUtils.LONG_DATE_FORMAT));
//		Assert.assertEquals("2017-04-20 23:59:59",DateUtils.format(endTime,DateUtils.LONG_DATE_FORMAT));
    }


    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test4CreateBusinessPaymentVoucher.sql")
    public void test_createBusinessPaymentVoucher() {

//		BusinessPaymentVoucherHandler businessPaymentVoucherHandler = (BusinessPaymentVoucherHandler)this.applicationContext.getBean("businessPaymentVoucherHandler");
//		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("batch_pay_record_uuid_1");
        CreateBusinessPaymentVoucherModel voucherModel = new CreateBusinessPaymentVoucherModel();
        voucherModel.setFinancialContractUuid("x12323ww");
        voucherModel.setPaymentName("上海拍拍贷金融信息服务有限公司");
        voucherModel.setPaymentAccountNo("1001133419006708197");
        voucherModel.setPaymentBank("工商银行");
        voucherModel.setAppAccountUuid("3db60870add840668fba06718e4c3ddd");
        voucherModel.setCreditSerialNumber("123456");
        voucherModel.setRemark("备注 我是备注");
        voucherModel.setCashFlowUuid("batch_pay_record_uuid_1");
        voucherModel.setVoucherAmount(new BigDecimal("123"));
        voucherModel.setCashFlowAmount(new BigDecimal("234"));
        voucherModel.setVoucherType(VoucherType.PAY.ordinal());

        List<TemplatesForAll> details = new ArrayList<TemplatesForAll>();
        TemplatesForPay templatesForPay = new TemplatesForPay();
        templatesForPay.setRepaymentNo("DKHD-001-01");
        templatesForPay.setContractNo("DKHD-001");
        templatesForPay.setRecycleDate("2015-10-19");
        templatesForPay.setCustomerName("张三");
        templatesForPay.setPrincipal("50");
        templatesForPay.setInterest("50");
        templatesForPay.setLoanServiceFee("0");
        templatesForPay.setTechServiceFee("0");
        templatesForPay.setOtherFee("0");
        templatesForPay.setOverduePenalty("0");
        templatesForPay.setOverdueDamages("0");
        templatesForPay.setOverdueServiceFee("0");
        templatesForPay.setOverdueOtherFee("0");

        details.add(templatesForPay);
        voucherModel.setAmountDetail(JSON.toJSONString(details, SerializerFeature.DisableCircularReferenceDetect));

        businessPaymentVoucherHandler.createBusinessPaymentVoucher(voucherModel);

        boolean isCashFlowExistVoucher = voucherService.isCashFlowExistVoucher("batch_pay_record_uuid_1");
        assertTrue(isCashFlowExistVoucher);

        sessionFactory.getCurrentSession().clear();

        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("batch_pay_record_uuid_1");

        Assert.assertEquals("123456", cashFlow.getStringFieldOne());

    }


    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test4CreateBusinessPaymentVoucher.sql")
    public void test_createBusinessPaymentVoucher_repurchase() {

        CreateBusinessPaymentVoucherModel voucherModel = new CreateBusinessPaymentVoucherModel();
        voucherModel.setFinancialContractUuid("x12323ww");
        voucherModel.setPaymentName("上海拍拍贷金融信息服务有限公司");
        voucherModel.setPaymentAccountNo("1001133419006708197");
        voucherModel.setPaymentBank("工商银行");
        voucherModel.setAppAccountUuid("3db60870add840668fba06718e4c3ddd");
        voucherModel.setCreditSerialNumber("123456");
        voucherModel.setRemark("备注 我是备注");
        voucherModel.setCashFlowUuid("batch_pay_record_uuid_1");
        voucherModel.setVoucherAmount(new BigDecimal("123"));
        voucherModel.setCashFlowAmount(new BigDecimal("234"));
        voucherModel.setVoucherType(VoucherType.REPURCHASE.ordinal());

		List<TemplatesForAll> details = new ArrayList<TemplatesForAll>();
		TemplatesForRepurchase templatesForRepurchase = new TemplatesForRepurchase();
		templatesForRepurchase.setContractNo("DKHD-001");
//		templatesForRepurchase.setRepurchaseAmount("123");

        details.add(templatesForRepurchase);
        voucherModel.setAmountDetail(JSON.toJSONString(details, SerializerFeature.DisableCircularReferenceDetect));

        businessPaymentVoucherHandler.createBusinessPaymentVoucher(voucherModel);

        boolean isCashFlowExistVoucher = voucherService.isCashFlowExistVoucher("batch_pay_record_uuid_1");
        assertTrue(isCashFlowExistVoucher);
        sessionFactory.getCurrentSession().clear();
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("batch_pay_record_uuid_1");
        Assert.assertEquals("123456", cashFlow.getStringFieldOne());
    }


    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test4GetAppAccountInfos.sql")
    public void test_getAppAccountInfos() {
        AppAccountModelForVoucherController appAccountModel = new AppAccountModelForVoucherController();
        appAccountModel.setFinancialContractUuid("2d380fe1-7157-490d-9474-12c5a9901e29");
        appAccountModel.setAccountNo("10002");
        List<Map<String, Object>> infoMaps = businessPaymentVoucherHandler.getAppAccountInfos(appAccountModel);
        assertEquals(1, infoMaps.size());
        Map<String, Object> info0 = infoMaps.get(0);
        assertEquals("10002", info0.get("paymentAccountNo"));
        assertEquals("农分期", info0.get("paymentName"));
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test4GetAppAccountInfos.sql")
    public void test_getAppAccountInfoClasses() {
        AppAccountModelForVoucherController appAccountModel = new AppAccountModelForVoucherController();
        appAccountModel.setFinancialContractUuid("2d380fe1-7157-490d-9474-12c5a9901e29");
        appAccountModel.setAccountNo("10002");
        List<VoucherCreateAccountInfoModel> infoMaps = businessPaymentVoucherHandler.getAppAccountInfoClasses(appAccountModel);
        assertEquals(1, infoMaps.size());
        VoucherCreateAccountInfoModel info0 = infoMaps.get(0);
        assertEquals("10002", info0.getPaymentAccountNo());
        assertEquals("农分期", info0.getPaymentName());

        appAccountModel.setAccountNo("100046578");
        infoMaps = businessPaymentVoucherHandler.getAppAccountInfoClasses(appAccountModel);
        assertEquals(0, infoMaps.size());

        appAccountModel.setAccountNo("10004");
        infoMaps = businessPaymentVoucherHandler.getAppAccountInfoClasses(appAccountModel);
        assertEquals(1, infoMaps.size());
    }

    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/test_businessPaymentVoucher.sql")
    public void test_businessPaymentVoucher_same() {

        List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
        BusinessPaymentVoucherDetail detail1 = new BusinessPaymentVoucherDetail();
        detail1.setAmount(new BigDecimal("10000.00"));
        detail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        detail1.setRepaymentPlanNo("plan_no1");
        detail1.setPayer(0);
        details.add(detail1);
        String jsonString = JsonUtils.toJsonString(details);

        BusinessPaymentVoucherCommandModel model1 = new BusinessPaymentVoucherCommandModel();
        model1.setRequestNo(UUID.randomUUID().toString());
        model1.setTransactionType(0);
        model1.setVoucherType(0);
        model1.setFinancialContractNo("test_contract_no_1");
        model1.setReceivableAccountNo("test_receivable_account_no_1");
        model1.setPaymentAccountNo("test_payment_account_no_1");
        model1.setPaymentName("test_payment_name_1");
        model1.setPaymentBank("招商银行");
        model1.setVoucherAmount(new BigDecimal("10000.00"));
        model1.setBankTransactionNo(UUID.randomUUID().toString());
        model1.setDetail(jsonString);
        String ip = "127.0.0.1";
        List<CashFlow> cashFlows = businessPaymentVoucherHandler.businessPaymentVoucher(model1, ip);
        CashFlow cashFlow = cashFlows.get(0);

        Assert.assertEquals(model1.getVoucherAmount(), cashFlow.getTransactionAmount());
        Assert.assertEquals(model1.getPaymentAccountNo(), cashFlow.getCounterAccountNo());
        Assert.assertEquals(model1.getPaymentName(), cashFlow.getCounterAccountName());

        List<Voucher> vouchers = voucherService.loadAll(Voucher.class);
        Voucher voucher = vouchers.get(0);
        assertEquals(cashFlow.getUuid(), voucher.getCashFlowUuid());
        assertEquals(cashFlow.getTransactionTime(), voucher.getTransactionTime());
        assertEquals(model1.getVoucherAmount(), voucher.getAmount());

        List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
        for (SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails) {
            assertEquals(voucher.getUuid(), sourceDocumentDetail.getVoucherUuid());
        }


        BusinessPaymentVoucherCommandModel model = new BusinessPaymentVoucherCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setVoucherType(0);
        model.setFinancialContractNo("test_contract_no_1");
        model.setReceivableAccountNo("test_receivable_account_no_1");
        model.setPaymentAccountNo("test_payment_account_no_1");
        model.setPaymentName("test_payment_name_1");
        model.setPaymentBank("招商银行");
        model.setVoucherAmount(new BigDecimal("10000.00"));
        model.setBankTransactionNo(UUID.randomUUID().toString());
        model.setDetail(jsonString);
        try {
            businessPaymentVoucherHandler.businessPaymentVoucher(model, ip);
            fail();
        } catch (ApiException e) {
            Assert.assertEquals("凭证对应流水不存在或已提交!", ApiMessageUtil.getMessage(e.getCode()));
        }
    }

    /*
     * 商户付款  代偿   校验是否存在支付中的还款计划
     */
    @Test
    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_paymenting_assetset.sql")
    public void test_check_if_exist_paymenting_assetset() {


        try {

            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 1L);
        assertEquals(SourceDocumentExcuteResult.UNSUCCESS, sourceDocument.getExcuteResult());
        assertEquals(SourceDocumentExcuteStatus.PREPARE, sourceDocument.getExcuteStatus());

        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(2, details.size());

		
		assertEquals(SourceDocumentDetailCheckState.CHECK_FAILS,details.get(0).getCheckState());
		
		
	}
	
	/*
	 * 商户付款  代偿   跨期核销+++++++++2017-07-10
	 */
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_cross_period.sql")
	public void test_check_and_transfer_asstes_cross_period(){
		try {
			
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class,1L);
		//assertEquals(SourceDocumentExcuteResult.SUCCESS,sourceDocument.getExcuteResult());
		//assertEquals(SourceDocumentExcuteStatus.DONE,sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1,details.size());
		
		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		BigDecimal total_bookingAmount = new BigDecimal("500");

		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

		List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
		assertEquals(1,jvList.size());
		
		for (SourceDocumentDetail sourceDocumentDetail : details) {
			assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());
			
			//校验明细状态
			assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());
			//校验资产状态
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
			assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
			assertEquals(DateUtils.parseDate("2016-10-27 20:00:17", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());
			CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
			assertEquals(cashFlow.getTransactionTime(),assetSet.getActualRecycleDate());
			
			VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
			//校验余额
			assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
			
			//校验ledger book明细余额,不变
			BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
			assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
			
			//校验ledger_book的应收资产
			BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
			assertEquals(0,BigDecimal.ZERO.compareTo(receivable_amount));
			
			
			List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
			assertEquals(1,jvLists.size());
			JournalVoucher jv = jvLists.get(0);
			assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
			assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());

			Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
			assertNotNull(order);

			assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
			assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
			assertEquals(DateUtils.format(jv.getTradeTime()),DateUtils.format(order.getDueDate()));
			assertEquals(jv.getTradeTime(),order.getPayoutTime());
			assertEquals(sourceDocument.getOutlierAccount(),jv.getSourceDocumentLocalPartyAccount());
			assertEquals(sourceDocument.getOutlieAccountName(),jv.getSourceDocumentLocalPartyName());
			assertEquals("10001",jv.getLocalPartyAccount());
			assertEquals("测试7",jv.getLocalPartyName());

			assertEquals(sourceDocumentDetail.getPaymentAccountNo(),jv.getSourceDocumentCounterPartyAccount());
			assertEquals(sourceDocumentDetail.getPaymentName(),jv.getSourceDocumentCounterPartyName());
			assertEquals(virtualAccount.getVirtualAccountNo(),jv.getCounterPartyAccount());
			assertEquals(virtualAccount.getOwnerName(),jv.getCounterPartyName());


			assertEquals(0,sourceDocumentDetail.getAmount().compareTo(jv.getBookingAmount()));
			
			List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
			assertEquals(1,ledgers.size());
			assertEquals(0,sourceDocumentDetail.getAmount().compareTo(ledgers.get(0).getDebitBalance()));
		}


		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		//校验余额不变
		assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));
		
		//校验ledger book明细余额,不变
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));
		
		
		//校验流水
		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
		assertEquals(1,virtualAccountFlowList.size());
		
		VirtualAccountFlow virtualAccountFlow= virtualAccountFlowList.get(0);
		assertEquals(0,new BigDecimal("500.00").compareTo(virtualAccountFlow.getTransactionAmount()));
		assertEquals(jvList.get(0).getJournalVoucherUuid(),virtualAccountFlow.getBusinessDocumentUuid());
		assertEquals(virtualAccount.getVirtualAccountNo(),virtualAccountFlow.getVirtualAccountNo());
		assertEquals(jvList.get(0).getJournalVoucherNo(),virtualAccountFlow.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow.getTransactionType());
		assertEquals(virtualAccount.getFstLevelContractUuid(),virtualAccountFlow.getFinancialContractUuid());
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("firstAccountName", ChartOfAccount.FST_BANK_SAVING);
		params.put("sourceDocumentUuid", sourceDocument.getSourceDocumentUuid());
		List<LedgerItem> bankledgerItems = genericDaoSupport.searchForList("From LedgerItem where firstAccountName=:firstAccountName and sourceDocumentUuid=:sourceDocumentUuid",params);
		assertEquals(1,bankledgerItems.size());
		assertEquals(0,total_bookingAmount.compareTo(bankledgerItems.get(0).getDebitBalance().add(bankledgerItems.get(0).getCreditBalance())));
	}


	//@Test
	public void a(){

		Integer dateCounter = 2;

		Date startTime = null;
		Calendar calendar = com.zufangbao.sun.utils.DateUtils.getTimeQuantumByDay(new Date(), 59, 59, 23);
		Date endTime = calendar.getTime();
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.HOUR_OF_DAY, 00);

		if(dateCounter.equals(new Integer(0))){
			startTime = calendar.getTime();
		}

		else if(dateCounter.equals(new Integer(1))){
			calendar.add(Calendar.DATE, -2);
			startTime = calendar.getTime();
		}
		else if(dateCounter.equals(new Integer(2))){
			calendar.add(Calendar.DATE, -4);
			startTime = calendar.getTime();

		}else if(dateCounter.equals(new Integer(3))){
			calendar.add(Calendar.DATE, -9);
			startTime = calendar.getTime();
		}
		
		Assert.assertEquals("2017-07-20 00:00:00",DateUtils.format(startTime,DateUtils.LONG_DATE_FORMAT));
		Assert.assertEquals("2017-07-24 23:59:59",DateUtils.format(endTime,DateUtils.LONG_DATE_FORMAT));
		
	}


	/*
	 * 商户付款  代偿   核销,滞留单
	 */
	@Test
	@Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher_temp_doc.sql")
	public void test_check_and_transfer_asstes_tmp_deposit_doc(){
		//生成500元滞留单
		String customerUuid1 = "company_customer_uuid_1";
		String voucher_uuid = "voucher_uuid_1";
		String sourceDocumentUuid = "source_document_uuid_1";
		try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class,1L);
		assertEquals(SourceDocumentExcuteResult.UNSUCCESS,sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE,sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(2,details.size());

		String borrower_customerUuid = "customerUuid1";
		String company_customerUuid = "company_customer_uuid_1";
		String ledgerBookNo = "yunxin_ledger_book";
		BigDecimal total_bookingAmount = new BigDecimal("1500");

		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

		List<JournalVoucher> jvList = journalVoucherService.list(JournalVoucher.class, new Filter());
		assertEquals(2,jvList.size());

		for (SourceDocumentDetail sourceDocumentDetail : details) {
			assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());

			//校验明细状态
			assertEquals(SourceDocumentDetailStatus.SUCCESS,sourceDocumentDetail.getStatus());
			//校验资产状态
			AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
			assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
			assertEquals(DateUtils.parseDate("2016-10-27 20:00:17", "yyyy-MM-dd HH:mm:ss"),assetSet.getActualRecycleDate());
			CashFlow cashFlow=cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
			assertEquals(cashFlow.getTransactionTime(),assetSet.getActualRecycleDate());
			VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
			//校验余额
			assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));

			//校验ledger book明细余额,不变
			BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
			assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));

			//校验ledger_book的应收资产
			BigDecimal receivable_amount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customerUuid);
			assertEquals(0,BigDecimal.ZERO.compareTo(receivable_amount));


			List<JournalVoucher> jvLists = journalVoucherService.getJournalVoucherBySourceDocumentUuid(sourceDocumentDetail.getUuid());
			assertEquals(1,jvLists.size());
			JournalVoucher jv = jvLists.get(0);
			assertEquals(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER,jv.getJournalVoucherType());
			assertEquals(JournalVoucherStatus.VOUCHER_ISSUED,jv.getStatus());

			Order order = orderService.getOrder(jvLists.get(0).getRelatedBillContractNoLv4());
			assertNotNull(order);

			assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
			assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
			assertEquals(DateUtils.format(jv.getTradeTime()),DateUtils.format(order.getDueDate()));
			assertEquals(jv.getTradeTime(),order.getPayoutTime());
			assertEquals(sourceDocument.getOutlierAccount(),jv.getSourceDocumentLocalPartyAccount());
			assertEquals(sourceDocument.getOutlieAccountName(),jv.getSourceDocumentLocalPartyName());
			assertEquals("10001",jv.getLocalPartyAccount());
			assertEquals("测试7",jv.getLocalPartyName());

			assertEquals(sourceDocumentDetail.getPaymentAccountNo(),jv.getSourceDocumentCounterPartyAccount());
			assertEquals(sourceDocumentDetail.getPaymentName(),jv.getSourceDocumentCounterPartyName());
			assertEquals(virtualAccount.getVirtualAccountNo(),jv.getCounterPartyAccount());
			assertEquals(virtualAccount.getOwnerName(),jv.getCounterPartyName());


			assertEquals(0,sourceDocumentDetail.getAmount().compareTo(jv.getBookingAmount()));

			List<LedgerItem> ledgers = ledgerItemService.get_booked_ledgers_of_asset_in_taccounts(Arrays.asList(ChartOfAccount.FST_BANK_SAVING), book, assetSet.getAssetUuid(), "");
			assertEquals(1,ledgers.size());
			assertEquals(0,sourceDocumentDetail.getAmount().compareTo(ledgers.get(0).getDebitBalance()));
		}


		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(company_customerUuid);
		//校验余额不变
		assertEquals(0,new BigDecimal("10005").subtract(total_bookingAmount).compareTo(virtualAccount.getTotalBalance()));

		//校验ledger book明细余额,不变
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, company_customerUuid);
		assertEquals(0,balance.compareTo(virtualAccount.getTotalBalance()));


		//校验流水
		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class, new Filter());
		assertEquals(2,virtualAccountFlowList.size());

		VirtualAccountFlow virtualAccountFlow1 = virtualAccountFlowList.get(0);
		assertEquals(0,new BigDecimal("1000.00").compareTo(virtualAccountFlow1.getTransactionAmount()));
		assertEquals(jvList.get(0).getJournalVoucherUuid(),virtualAccountFlow1.getBusinessDocumentUuid());
		assertEquals(virtualAccount.getVirtualAccountNo(),virtualAccountFlow1.getVirtualAccountNo());
		assertEquals(jvList.get(0).getJournalVoucherNo(),virtualAccountFlow1.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow1.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow1.getTransactionType());
		assertEquals(virtualAccount.getFstLevelContractUuid(),virtualAccountFlow1.getFinancialContractUuid());

		VirtualAccountFlow virtualAccountFlow2 = virtualAccountFlowList.get(1);
		assertEquals(0,new BigDecimal("500.00").compareTo(virtualAccountFlow2.getTransactionAmount()));
		assertEquals(jvList.get(1).getJournalVoucherUuid(),virtualAccountFlow2.getBusinessDocumentUuid());
		assertEquals(virtualAccount.getVirtualAccountNo(),virtualAccountFlow2.getVirtualAccountNo());
		assertEquals(jvList.get(1).getJournalVoucherNo(),virtualAccountFlow2.getBusinessDocumentNo());
		assertEquals(AccountSide.CREDIT,virtualAccountFlow2.getAccountSide());
		assertEquals(VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,virtualAccountFlow2.getTransactionType());
		assertEquals(virtualAccount.getFstLevelContractUuid(),virtualAccountFlow2.getFinancialContractUuid());


		Map<String,Object> params = new HashMap<String,Object>();
		params.put("firstAccountName", ChartOfAccount.FST_BANK_SAVING);
		params.put("sourceDocumentUuid", sourceDocument.getSourceDocumentUuid());
		List<LedgerItem> bankledgerItems = genericDaoSupport.searchForList("From LedgerItem where firstAccountName=:firstAccountName and sourceDocumentUuid=:sourceDocumentUuid",params);
		assertEquals(2,bankledgerItems.size());
		assertEquals(0,total_bookingAmount.compareTo(bankledgerItems.get(0).getCreditBalance().add(bankledgerItems.get(1).getCreditBalance())));


		TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocService.getTemporaryDepositDocBySourceDocumentUuid(sourceDocumentUuid);
		assertEquals(0,temporaryDepositDoc.getTotalAmount().compareTo(new BigDecimal("500")));
		assertEquals(0,temporaryDepositDoc.getBookingAmount().compareTo(new BigDecimal("0")));

		BigDecimal amount = ledgerBookVirtualAccountHandler.get_balance_of_tmp_deposit_doc(ledgerBookNo,temporaryDepositDoc.getUuid());
		assertEquals(0,temporaryDepositDoc.getBalance().compareTo(amount));


		assertEquals(customerUuid1,temporaryDepositDoc.getOwnerUuid());
		assertEquals(CustomerType.COMPANY,temporaryDepositDoc.getOwnerType());
		assertTrue(StringUtils.isEmpty(temporaryDepositDoc.getRelatedContractUuid()));
		assertEquals(TmpDepositStatus.UNCLEAR,temporaryDepositDoc.getStatus());
		assertEquals("9126313e-f89d-4222-847c-4e36331cb787",temporaryDepositDoc.getVirtualAccountUuid());

		Voucher voucher = voucherService.get_voucher_by_uuid(voucher_uuid);
		//凭证为未核销
		assertEquals(SourceDocumentDetailStatus.UNSUCCESS, voucher.getStatus());
	}


}
