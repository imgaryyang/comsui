package com.zufangbao.earth.yunxin.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.support.Filter;
import com.suidifu.giotto.handler.FastHandler;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItem;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.AppArriveRecordService;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemCheckFailLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobRepaymentOrderPlacing;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.AutoRecoverAssesNoSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueV2_0Handler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.BusinessVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DataSyncHandler;
import com.zufangbao.wellsfargo.yunxin.handler.BusinessTaskMessageHandler;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartVoucherV2_0Handler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Rollback(false)
@Transactional
public abstract class AbstractNeverRollBackApplicationTest {
	@Autowired
	protected ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
	@Autowired
	protected GenericDaoSupport genericDaoSupport;
	@Autowired
	protected LedgerItemService ledgerItemService;
	@Autowired
	protected LedgerBookService ledgerBookService;
	@Autowired
	protected SessionFactory sessionFactory;
	@Autowired
	protected CashFlowAutoIssueHandler cashFlowAutoIssueHandler;
	@Autowired
	protected RepaymentPlanService repaymentPlanService;
	@Autowired
	protected JournalVoucherService journalVoucherService;
	@Autowired
	protected BusinessVoucherService businessVoucherService;
	@Autowired
	protected JournalVoucherHandler journalVoucherHandler;
	@Autowired
	protected SourceDocumentService sourceDocumentService;
	@Autowired
	protected CashFlowHandler cashFlowHandler;
	@Autowired
	protected BusinessTaskMessageHandler businessTaskMessageHandler;
	@Autowired
	protected OrderService orderService;
	@Autowired
	protected AppArriveRecordService appArriveRecordService;
	@Autowired
	protected AppService appService;
	@Autowired
	protected CashFlowService cashFlowService;
	@Autowired
	protected DeductApplicationDetailService deductApplicationDetailService;
	@Autowired
	protected SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	protected DeductApplicationService deductApplicationService;
	@Autowired
	protected LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	protected FinancialContractService financialContractService;
	@Autowired
	protected SystemOperateLogService systemOperateLogService;
	@Autowired
	protected DeductPlanService deductPlanService;
	@Autowired
	protected VirtualAccountService virtualAccountService;
	@Autowired
	protected VirtualAccountFlowService virtualAccountFlowService;
	@Autowired
	protected DataSource dataSource;
	@Autowired
	protected AutoRecoverAssesNoSession autoRecoverAssesNoSession;
	@Autowired
	protected BankAccountCache bankAccountCache;
	@Autowired
	protected ContractService contractService;
	@Autowired
	protected LedgerBookHandler ledgerBookHandler;
	@Autowired
	protected RepaymentPlanExtraDataService repaymentPlanExtraDataService;
	@Autowired
	protected RepaymentOrderItemService repaymentOrderItemService;
	@Autowired
	protected DstJobRepaymentOrderPlacing dstJobRepaymentOrderPlacing;
	@Autowired
	protected RepaymentOrderItemCheckFailLogService repaymentOrderItemCheckFailLogService;
	@Autowired
	protected FastHandler fastHandler;
	@Autowired
	protected RepaymentOrderService repaymentOrderService;
	@Autowired
	protected CashFlowAutoIssueV2_0Handler cashFlowAutoIssueV2_0Handler;
	@Autowired
	protected PaymentOrderService paymentOrderService;
	@Autowired
	protected ThirdPartVoucherV2_0Handler thirdPartVoucherV2_0Handler;
	@Autowired 
	protected StringRedisTemplate redisTemplate;
	@Autowired 
	protected BusinessPaymentVoucherSession businessPaymentVoucherSession;
	@Autowired 
	protected RepurchaseService repurchaseService;
	@Autowired
	protected DataSyncHandler dataSyncHandler;
	
	
	protected static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	protected static final String LEDGER_BOOK_NO = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
	protected static final String PRODUCT_NO = "G31700";
	
	protected List<LedgerItem> getLedgerItemList(String accountName,int i) {
		List<LedgerItem> ledgerItems = ledgerItemService.list(LedgerItem.class,new Filter());
		List<LedgerItem> result = new ArrayList<>();
		for (LedgerItem ledgerItem : ledgerItems) {
			if(i==0){
				if (ledgerItem.getFirstAccountName()!=null&&ledgerItem.getFirstAccountName().equals(accountName)) {
					result.add(ledgerItem);
				}
			}else if(i==1){
				if (ledgerItem.getSecondAccountName()!=null&&ledgerItem.getSecondAccountName().equals(accountName)) {
					result.add(ledgerItem);
				}
			}else if(i==2){
				if (ledgerItem.getThirdAccountName()!=null&&ledgerItem.getThirdAccountName().equals(accountName)) {
					result.add(ledgerItem);
				}
			}
		}
		return result;
	}
		
	protected Date addTime(Date date,int unit_of_time,int amount) {
		Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(new Date());    
	    calendar.add(unit_of_time, amount);    
		return calendar.getTime();
	}
	
	protected void sessionClear() {
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}
}
