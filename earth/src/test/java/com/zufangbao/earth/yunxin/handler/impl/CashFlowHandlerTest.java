package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.gluon.exception.voucher.CashFlowNotExistException;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.AppArriveRecordService;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateCashFlowInfoModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowCacheHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.BusinessVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class CashFlowHandlerTest {

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private JournalVoucherService journalVoucherService;
	
	@Autowired
	private BusinessVoucherService businessVoucherService;
	
	@Autowired
	private JournalVoucherHandler journalVoucherHandler;
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private CashFlowHandler cashFlowHandler;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AppArriveRecordService appArriveRecordService;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	
	private Page page = new Page(0,10);
	
	@Autowired
	private CashFlowCacheHandler cashFlowCacheHandler;
	
	private Long companyId = 1L;
	
	@Autowired
	private DataSource dataSource;
	
	private static final long TIMEOUT = 1000 * 10 * 2;
	
	private static final long HALF_TIMEOUT = TIMEOUT / 2;
	
	/*************** test getUnattachedCashFlowListBy start ***************/

	@Test
	public void test_paymentAccountNo_empty() {
		try {
			Long financeCompanyId = 1l;
			String paymentAccountNo = null;
			String paymentName = "test_counter_account_name_1";
			BigDecimal voucherAmount = new BigDecimal(10000.00);
			String bankTransactionNo = "test_identity_1";//流水已关联凭证
			List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName,
                    voucherAmount);
			Assert.assertTrue(CollectionUtils.isEmpty(cashFlows));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Long financeCompanyId = 1l;
			String paymentAccountNo = "";
			String paymentName = "test_counter_account_name_1";
			BigDecimal voucherAmount = new BigDecimal(10000.00);
			String bankTransactionNo = "test_identity_1";//流水已关联凭证
			List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName,
					voucherAmount);
			Assert.assertTrue(CollectionUtils.isEmpty(cashFlows));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_paymentName_empty() {
		try {
			Long financeCompanyId = 1l;
			String paymentAccountNo = "123";
			String paymentName = "";
			BigDecimal voucherAmount = new BigDecimal(10000.00);
			String bankTransactionNo = "test_identity_1";//流水已关联凭证
			List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName,
					voucherAmount);
			Assert.assertTrue(CollectionUtils.isEmpty(cashFlows));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Long financeCompanyId = 1l;
			String paymentAccountNo = "124";
			String paymentName = null;
			BigDecimal voucherAmount = new BigDecimal(10000.00);
			String bankTransactionNo = "test_identity_1";//流水已关联凭证
			List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName,
					voucherAmount);
			Assert.assertTrue(CollectionUtils.isEmpty(cashFlows));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/cashFlow/test_cashFlow_noExist.sql")
	public void test_cashFlow_noExist() {
		try {
			Long financeCompanyId = 1l;
			String paymentAccountNo = "paymentAccountNo";
			String paymentName = "paymentName";
			BigDecimal voucherAmount = new BigDecimal(10000.00);
			String bankTransactionNo = "bankTransactionNo";//流水已关联凭证
			List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo,
					paymentName, voucherAmount);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof CashFlowNotExistException);
		}
	}

	//流水已关联凭证
	@Test
	@Sql("classpath:test/yunxin/cashFlow/test_getUnattachedCashFlowListBy.sql")
	public void test_getUnattachedCashFlowListBy1() {
		try {
			Long financeCompanyId = 1l;
			String paymentAccountNo = "test_counter_account_no_1";
			String paymentName = "test_counter_account_name_1";
			BigDecimal voucherAmount = new BigDecimal(10000.00);
			String bankTransactionNo = "test_identity_1";//流水已关联凭证
			List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}
	
	//明细已存在
	@Test
	@Sql("classpath:test/yunxin/cashFlow/test_getUnattachedCashFlowListBy_detailsExisted.sql")
	public void test_getUnattachedCashFlowListBy2() {
		Long financeCompanyId = 1l;
		String paymentAccountNo = "test_counter_account_no_1";
		String paymentName = "test_counter_account_name_1";
		BigDecimal voucherAmount = new BigDecimal(10000.00);
		String bankTransactionNo = "test_identity_2";
		List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
		Assert.assertEquals(0, cashFlows.size());		
	}

	//正常情况
	@Test
	@Sql("classpath:test/yunxin/cashFlow/test_getUnattachedCashFlowListBy.sql")
	public void test_getUnattachedCashFlowListBy() {
		Long financeCompanyId = 1l;
		String paymentAccountNo = "test_counter_account_no_1";
		String paymentName = "test_counter_account_name_1";
		BigDecimal voucherAmount = new BigDecimal(10000.00);
		String bankTransactionNo = "test_identity_2";
		List<CashFlow> cashFlows = cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
		Assert.assertNotEquals(0, cashFlows.size());		
	}
	/*************** test getUnattachedCashFlowListBy end ***************/


	@Test
	@Sql("classpath:test/yunxin/cashFlow/test_getCashFlowModelListForBusinessPaymentVoucher.sql")
	public void test_getCashFlowModelListForBusinessPaymentVoucher(){

		String hostAccountNo="600000000112", counterAccountNo="1000";
		List<VoucherCreateCashFlowInfoModel> models = cashFlowHandler.getCashFlowModelListForBusinessPaymentVoucher(hostAccountNo, counterAccountNo);
		Assert.assertEquals(0, models.size());

		counterAccountNo= "10003";
		models = cashFlowHandler.getCashFlowModelListForBusinessPaymentVoucher(hostAccountNo, counterAccountNo);
		Assert.assertEquals(0, models.size());

		counterAccountNo= "10004";
		models = cashFlowHandler.getCashFlowModelListForBusinessPaymentVoucher(hostAccountNo, counterAccountNo);
		Assert.assertEquals(1, models.size());
		VoucherCreateCashFlowInfoModel model =models.get(0);
		Assert.assertEquals("cash_flow_uuid_17",model.getCashFlowUuid());


	}
	
}
