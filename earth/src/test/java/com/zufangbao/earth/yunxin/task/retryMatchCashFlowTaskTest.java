package com.zufangbao.earth.yunxin.task;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.demo2do.core.persistence.support.Filter;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountFlow;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.OrderPayStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderPayResult;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.webform.CustomerDepositResult;
import com.zufangbao.wellsfargo.yunxin.handler.BusinessTaskMessageHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
public class retryMatchCashFlowTaskTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private RepaymentOrderService  repaymentOrderService;
	
	@Autowired
	private PaymentOrderService  paymentOrderService;
	
	@Autowired
	private RepaymentOrderHandler repaymentOrderHandler;
	
	@Autowired
	private CashFlowService  cashFlowService;
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CashFlowAutoIssueHandler cashFlowAutoIssueHandler;
	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private LedgerItemService ledgerItemService;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;
	@Autowired
	private VirtualAccountFlowService virtualAccountFlowService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private BusinessTaskMessageHandler businessTaskMessageHandler;
	
	@Test
	@Sql("classpath:test/yunxin/repaymentOrder/payment_order_retry_cash_flow.sql")
	public void testPaymentOrderMatchCashFlowTask() {
		
		
		PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid("paymentOrderUuid_1");
		
		Assert.assertEquals(0,(int)paymentOrder.getRetriedNums());
		try {
			
			repaymentOrderHandler.retryMatchCashFlow(paymentOrder);
//			paymentOrderMatchCashFlowTask.retryMatchCashFlow();
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			
			PaymentOrder paymentOrder1 =paymentOrderService.getPaymenrOrderByUuid("paymentOrderUuid_1");
			RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid("order_uuid_1");
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
			
//			Assert.assertEquals(1,(int)paymentOrder1.getRetriedNums());
			
			Assert.assertEquals(PayStatus.PAY_SUCCESS,paymentOrder1.getPayStatus());
			Assert.assertEquals(RepaymentOrderPayResult.PAY_SUCCESS,paymentOrder1.getOrderPayResultStatus());
			Assert.assertEquals("cash_flow_uuid_1",paymentOrder1.getOutlierDocumentUuid());
			
			Assert.assertEquals(OrderPayStatus.PAYING,repaymentOrder.getOrderPayStatus());
			
			Assert.assertEquals("123456",cashFlow.getStringFieldOne());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	@Sql("classpath:test/yunxin/repaymentOrder/cash_flow_auto_charge_for_payment_order.sql")
	public void testCashFlowAutoChargeForRepaymentOrder() {
		
		String businessId = "financial_contract_uuid_1";
		String paymentOrderUuid = "paymentOrderUuid_1";
		String cashFlowUuid = "cash_flow_uuid_1";
		FinancialContract financialContract = financialContractService
				.getFinancialContractBy("financial_contract_uuid_1");
		
		cashFlowAutoIssueHandler.cashFlowAutoChargeForRepaymentOrder(businessId, paymentOrderUuid);
		businessTaskMessageHandler.do_business_task();
		
		List<LedgerItem> ledgerItems = ledgerItemService.loadAll(LedgerItem.class);
		assertEquals(2, ledgerItems.size());
		List<String> ledgerItemUuids = new ArrayList<>();
		for (LedgerItem ledgerItem : ledgerItems) {
			ledgerItemUuids.add(ledgerItem.getLedgerUuid());
			if (StringUtils.equals(ledgerItem.getFirstAccountName(),
					ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS)) {
				assertEquals(new BigDecimal("1000.00"), ledgerItem.getCreditBalance());
			}
			if (StringUtils.equals(ledgerItem.getFirstAccountName(), ChartOfAccount.FST_BANK_SAVING)) {
				assertEquals(new BigDecimal("1000.00"), ledgerItem.getDebitBalance());

			}
		}

		CashFlow cashFlowInDb = cashFlowService.getCashFlowByCashFlowUuid("cash_flow_uuid_1");
		assertEquals(new BigDecimal("1000.00"), cashFlowInDb.getIssuedAmount());
		assertEquals(cashFlowInDb.getIssuedAmount(), cashFlowInDb.getTransactionAmount());
		assertEquals(AuditStatus.ISSUED, cashFlowInDb.getAuditStatus());

		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid("company_customer_uuid_1");
		assertEquals(new BigDecimal("1000.00"), virtualAccount.getTotalBalance());

		List<VirtualAccountFlow> virtualAccountFlowList = virtualAccountFlowService.list(VirtualAccountFlow.class,
				new Filter().addEquals("transactionType", VirtualAccountTransactionType.DEPOSIT));
		assertEquals(1, virtualAccountFlowList.size());
		assertEquals(new BigDecimal("1000.00"), virtualAccountFlowList.get(0).getTransactionAmount());

		List<SourceDocument> sourceDocumentList = sourceDocumentService.getDepositReceipt(cashFlowUuid,
				SourceDocumentStatus.SIGNED, "", financialContract.getFinancialContractUuid());
		String sourceDocumentUuid = sourceDocumentList.get(0).getSourceDocumentUuid();
		List<CustomerDepositResult> depositResults = sourceDocumentHandler.convertToDepositResult(sourceDocumentList);
		assertEquals(1, depositResults.size());
		Map<String, Object> showData = new HashMap<String, Object>();
		CustomerDepositResult customerDepositResult = depositResults.get(0);
		showData = customerDepositResult.getShowData();

		customerDepositResult.getSourceDocumentStatusEnum();
		assertEquals(new BigDecimal("1000.00"), customerDepositResult.getBalance());
		assertEquals(new BigDecimal("1000.00"), customerDepositResult.getDepositAmount());
		assertEquals("系统自动充值", customerDepositResult.getRemark());

	}
}
