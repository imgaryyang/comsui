package com.suidifu.microservice.model;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.customize.ReconciliationDelayTaskProcess;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.JournalVoucherHandler;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.PrepaymentHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.AssetSetLockException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.model.ContractCategory;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.DepositeAccountHandler;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public abstract class ReconciliationBase  implements Reconciliation{

	@Autowired
	private JournalVoucherHandler journalVoucherHandler;

	@Autowired
	private VirtualAccountFlowService virtualAccountFlowService;
	@Autowired
	private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
	@Autowired
	private VirtualAccountHandler virtualAccountHandler;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	private LedgerBookHandler ledgerBookHandler;
	@Autowired
	private ContractAccountService contractAccountService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private VirtualAccountService virtualAccountService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private BankAccountCache bankAccountCache;

	@Autowired
	private  DepositeAccountHandler depositeAccountHandler;

	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;

	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;

	@Autowired
	private SettlementOrderService settlementOrderService;

	@Autowired
	private PrepaymentHandler prepaymentHandler;

	@Autowired
	private PrepaymentApplicationService prepaymentApplicationService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;

	@Autowired
	@Qualifier("reconciliationDelayTaskProcess")
	private ReconciliationDelayTaskProcess reconciliationDelayTaskProcess;

	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;

	@Autowired
	private LedgerBookV2Handler ledgerBookV2Handler;

	@Autowired
	private ClearingExecLogService clearingExecLogService;

	private static final Log logger = LogFactory.getLog(ReconciliationBase.class);


	public boolean accountReconciliation(Map<String,Object> params)
	{

		try
		{
			long start = System.currentTimeMillis();
			ReconciliationContext context=preAccountReconciliation(params);
			long end_1 = System.currentTimeMillis();
			logger.debug("#abcdefgh#preAccountReconciliation use ["+(end_1-start)+"]ms");

			validateReconciliationContext(context);
			long end_2 = System.currentTimeMillis();
			logger.debug("#abcdefgh#validateReconciliationContext use ["+(end_2-end_1)+"]ms");

			relatedDocumentsProcessing(context);
			long end_3 = System.currentTimeMillis();
			logger.debug("#abcdefgh#relatedDocumentsProcessing use ["+(end_3-end_2)+"]ms");

			issueJournalVoucher(context);
			long end_4 = System.currentTimeMillis();
			logger.debug("#abcdefgh#issueJournalVoucher use ["+(end_4-end_3)+"]ms");
//			logger.info("#abcdefgh#ledger_book_reconciliation type"+ context.getReconciliationType());
//			logger.info("#abcdefgh#issueJournalVoucher journalVoucherUuid"+context.getIssuedJournalVoucher().getJournalVoucherUuid());
//			logger.info("#abcdefgh#issueJournalVoucher tradeTime"+context.getIssuedJournalVoucher().getTradeTime());

			ledger_book_reconciliation(context);
			long end_5 = System.currentTimeMillis();
			logger.debug("#abcdefgh#ledger_book_reconciliation use ["+(end_5-end_4)+"]ms");

			refreshVirtualAccount(context);
			long end_6 = System.currentTimeMillis();
			logger.debug("#abcdefgh#refreshVirtualAccount use ["+(end_6-end_5)+"]ms");

			refreshAsset(context);
			long end_7 = System.currentTimeMillis();
			logger.debug("#abcdefgh#refreshAsset use ["+(end_7-end_6)+"]ms");

			postAccountReconciliation(context);
			long end_8 = System.currentTimeMillis();
			logger.debug("#abcdefgh#postAccountReconciliation use ["+(end_8-end_7)+"]ms");

			return true;

		}catch (MisMatchedConditionException e){

			logger.info("#accountReconciliation# MisMatchedConditionException occur exception,error message :"+e);

			return false;
		}
		catch(AlreadyProcessedException e)
		{

			logger.info("#accountReconciliation# AlreadyProcessedException occur exception,error message :"+e);

			return true;
		}

	}

	public abstract void relatedDocumentsProcessing(ReconciliationContext context);

	//提前还款解冻
	public void process_if_prepayment_plan(ReconciliationContext context){
		AssetRecoverType recoverType = context.getRecoverType();
		if(recoverType ==null || !recoverType.isLoanAsset()) {
			return;
		}

		AssetSet assetSet = context.getAssetSet();
		if(assetSet ==null || !assetSet.isPrepaymentPlan()) {
			return;
		}
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(assetSet.getAssetUuid());
		if(prepaymentApplication ==null) {
			return;
		}
		prepaymentHandler.processing_one_prepayment_plan(assetSet.getContractUuid(), Priority.High.getPriority(), prepaymentApplication.getId());
	}

	public void ledger_book_reconciliation(ReconciliationContext context)
	{
		String configureContext = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
			FinancialContractConfigurationCode.CROSS_PERIOD_CANCEL_AFTER_VERIFICATION.getCode());
		if(StringUtils.isBlank(configureContext)){
			if(context.getRecoverType().isLoanAsset()){
				boolean isDeblock = repaymentPlanHandler.repaymentPlanDeblocking(context.getAssetSet());
				if(!isDeblock){
					throw new AssetSetLockException();
				}
			}
		}
		long start = System.currentTimeMillis();
		journalVoucherHandler.recover_asset_by_reconciliation_context(context);
		logger.info("recover_asset_by_reconciliation_context used : "+(System.currentTimeMillis()-start));
	}

	public  ContractCategory extractContractCategory(ReconciliationContext context)
	{
		ContractCategory contractCategory=new ContractCategory();
		contractCategory.setRelatedBillContractInfoLv1(context.getFinancialContract().getUuid());
		contractCategory.setRelatedBillContractInfoLv2(context.getContract().getUuid());

		contractCategory.setRelatedBillContractNoLv1(context.getFinancialContract().getContractName());
		contractCategory.setRelatedBillContractNoLv2(context.getContract().getContractNo());

		if(context.getReconciliationType() == ReconciliationType.RECONCILIATION_REPURCHASE){
			contractCategory.setRelatedBillContractNoLv4(context.getRepurchaseDoc().getRepurchaseDocUuid());
			contractCategory.setRelatedBillContractInfoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
			contractCategory.setRelatedBillContractNoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
		}else {
			contractCategory.setRelatedBillContractNoLv4(context.getOrder().getOrderNo());
			contractCategory.setRelatedBillContractInfoLv3(context.getAssetSet().getAssetUuid());
			contractCategory.setRelatedBillContractNoLv3(context.getAssetSet().getSingleLoanContractNo());
		}
		return contractCategory;
	}



	public abstract ReconciliationContext preAccountReconciliation(Map<String,Object> inputParams) throws AlreadyProcessedException;

	public  void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException
	{
		JournalVoucher journalVoucher=context.getIssuedJournalVoucher();
		ContractCategory contractCategory=extractContractCategory(context);
		journalVoucher.fillBillContractInfo(contractCategory);
		journalVoucherService.saveOrUpdate(journalVoucher);
		return ;

	}

	public void refreshVirtualAccount(ReconciliationContext context)
	{
		FinancialContract financialContract=context.getFinancialContract();
		if(context.getRemittanceVirtualAccount()==null){
			throw new ReconciliationException("remittanceVirtualAccount is null.");
		}
		BigDecimal currentBalance = null;
		VirtualAccount virtualAccount = context.getRemittanceVirtualAccount();
		if(context.getReconciliationType().isRecoveredByVirtualAccountSelf()){
			VirtualAccount refreshedVirtualAccount = virtualAccountHandler.refreshVirtualAccountBalance(financialContract.getLedgerBookNo(), context.getRemittanceVirtualAccount().getOwnerUuid(), financialContract.getFinancialContractUuid(), context.getVirutalAccountVersionLock());
			currentBalance = refreshedVirtualAccount.getTotalBalance();
		}
		long start = System.currentTimeMillis();
		//新增账户流水 (瞬时余额为null) ，但不刷新 账户余额
		virtualAccountFlowService.addAccountFlow(context.getIssuedJournalVoucher().getJournalVoucherNo(), virtualAccount, context.getBookingAmount(),AccountSide.CREDIT,VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,context.getIssuedJournalVoucher().getJournalVoucherUuid(), currentBalance);
		long end = System.currentTimeMillis();
		logger.debug("#abcdefgh#addAccountFlow use ["+(end-start)+"]ms");
	}


	public void extractReconciliationContextFromAssetSet(ReconciliationContext context,AssetSet assetSet)
	{
		long start = System.currentTimeMillis();
		if(assetSet==null){
			throw new ReconciliationException("assetSet is null");
		}
		Contract contract = assetSet.getContract();

		if(contract==null){
			throw new ReconciliationException("contract is null");
		}
		long end = System.currentTimeMillis();
		logger.debug("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract asset,contract use ["+(end-start)+"]ms");

		extractReconciliationContextFrom(context,assetSet,contract);

	}
	public void extractReconciliationContextFromRepaymentPlanNo(ReconciliationContext context,String RepaymentPlanNo)
	{
		AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(RepaymentPlanNo);
		extractReconciliationContextFromAssetSet(context, assetSet);
	}


	public void extractReconciliationContextFromContractUuid(ReconciliationContext context,String contractUuid)
	{
		Contract contract = contractService.getContract(contractUuid);
		if(contract==null){
			throw new ReconciliationException("contract is null");
		}
		AssetSet assetSet = repaymentPlanService.getFirstUnClearAsset(contract);

		if (assetSet == null || !(assetSet.isAssetRecycleDate(new Date()) || assetSet.isOverdueDate(new Date()))) {
			throw new MisMatchedConditionException("assetSet is null  or assetSet is in assetRecycleDate ");
		}
		extractReconciliationContextFrom(context,assetSet,contract);
	}
	public void extractReconciliationContextFromContractUuidWithoutAsset(ReconciliationContext context,Contract contract)
	{
		if(contract==null){
			throw new ReconciliationException("contract is null");
		}
		extractReconciliationContextFrom(context,null,contract);
	}


	private void extractReconciliationContextFrom(ReconciliationContext context,AssetSet assetSet,Contract contract)
	{
		long start = System.currentTimeMillis();
		//TODO in cache
		ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
		long end_1 = System.currentTimeMillis();
		logger.debug("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract ContractAccount use ["+(end_1-start)+"]ms");

		//TODO in cache
		FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
		//TODO in cache
		Company company = financialContract.getCompany();

		Customer borrower_customer = contract.getCustomer();
		Customer company_customer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);


		long end_2 = System.currentTimeMillis();
		logger.debug("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract fc,customer use ["+(end_2-end_1)+"]ms");

		if(assetSet != null){
			DepositeAccountInfo borrowerDepositeAccountForLedegerBook = depositeAccountHandler.extractCustomerVirtualAccount(assetSet);
			context.setBorrowerDepositeAccountForLedegerBook(borrowerDepositeAccountForLedegerBook);
		}

		long end_3 = System.currentTimeMillis();
		logger.debug("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract DepositeAccountInfo use ["+(end_3-end_2)+"]ms");


		context.setAssetSet(assetSet);
		context.setContract(contract);
		context.setFinancialContract(financialContract);
		context.setBorrower_customer(borrower_customer);
		context.setCompany_customer(company_customer);
		context.setLedgerBookNo(financialContract.getLedgerBookNo());

		context.setCompany(company);

		context.setContractAccount(contractAccount);
		VirtualAccount remittanceFinancialVirtualAccount=null;
		if(context.getCompany_customer()!=null)
			remittanceFinancialVirtualAccount=virtualAccountService.getVirtualAccountByCustomerUuid(context.getCompany_customer().getCustomerUuid());
		VirtualAccount remittanceBorrowerVirtualAccount=null;
		if(context.getBorrower_customer()!=null)
			remittanceBorrowerVirtualAccount=virtualAccountService.getVirtualAccountByCustomerUuid(borrower_customer.getCustomerUuid());

		DepositeAccountInfo counterPartyAccount = bankAccountCache.extractFirstBankAccountFrom(context.getFinancialContract());

		context.setRemittanceFinancialVirtualAccount(remittanceFinancialVirtualAccount);
		context.setRemittanceBorrowerVirtuaAccount(remittanceBorrowerVirtualAccount);
		context.setFinancialContractAccountForLedgerBook(counterPartyAccount);
		context.setFinancialContractBankAccount(context.getFinancialContract().getCapitalAccount());
		long end_4 = System.currentTimeMillis();
		logger.debug("#abcdefgh#preAccountReconciliation#extractReconciliationContextFromAssetSet#extract rest use ["+(end_4-end_3)+"]ms");

		extract_related_balnace_from_ledger_book(context);
	}

	public void extract_related_balnace_from_ledger_book(ReconciliationContext context){
		String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
		Contract contract = context.getContract();
		Customer borrower_customer = context.getBorrower_customer();
		Customer company_customer = context.getCompany_customer();
		AssetSet assetSet = context.getAssetSet();
		if(assetSet!=null){
			BigDecimal receivalbeAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customer.getCustomerUuid());
			BigDecimal guranteeAmount=ledgerBookStatHandler.get_gurantee_amount(ledgerBookNo, assetSet.getAssetUuid());
			BigDecimal unearnedAmount=ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
			context.setReceivalbeAmount(receivalbeAmount);
			context.setGuranteeAmount(guranteeAmount);
			context.setUnearnedAmount(unearnedAmount);
		}
		if(context.getReconciliationType().isRecoveredByVirtualAccountSelf()){
			BigDecimal borrower_customer_balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo,borrower_customer.getCustomerUuid());
			context.setBorrowerCustomerBalance(borrower_customer_balance);
		} else {
			BigDecimal financial_app_balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo,company_customer.getCustomerUuid());
			context.setFinancialAppBalance(financial_app_balance);
		}

		BigDecimal repurchaseAmount= ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo,contract.getUuid());
		context.setRepurchaseAmount(repurchaseAmount);
	}

	public abstract void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException;



	public  void refreshAsset(ReconciliationContext context)
	{

		RepaymentType repaymentType = RepaymentType.NORMAL;
		if(context.getDeductApplication() != null){
			repaymentType = context.getDeductApplication().getRepaymentType();
		}

		updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(context.getLedgerBookNo(),Arrays.asList(context.getAssetSet().getAssetUuid()) , context.getActualRecycleTime(), repaymentType, context.getAssetSet().getExecutingStatus());


	}

	@Override
	public void postAccountReconciliation(ReconciliationContext context){
		AssetRecoverType recoverType = context.getRecoverType();

		if(recoverType!=null && recoverType.isLoanAssetOrGuranteeAsset()){
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(context.getAssetSet().getAssetUuid());
			settlementOrderService.createSettlementOrder(assetSet);
		}

		Order order = context.getOrder();
		if(recoverType!=null && recoverType.isLoanAsset() && order!=null && StringUtils.isBlank(order.getChargesDetail()) ){
			RepaymentChargesDetail chargesDetail = getChargesDetailByLoanAsset(context);
			order.setChargesDetail(chargesDetail.toJsonString());
			orderService.save(order);
		}
		create_reconciliation_delay_task(recoverType, context);
		if(com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType
			.JournalVoucherTypeUsedSpecialAccount().contains(context.getJournalVoucherType().ordinal())){
			if(financialContractService.isSpecialAccountConfigured(context.getFinancialContract().getFinancialContractUuid())){
				clearingExecLogService.createClearingExecLog(context.getAssetSet(), getChargesDetailMapByLoanAsset(context), context.getIssuedJournalVoucher().getUuid(), context.getJournalVoucherType().ordinal(), context.getBookingAmount(), context.getContract().getId(),"");
			}
		}
	}

	private void create_reconciliation_delay_task(AssetRecoverType recoverType, ReconciliationContext context) {
		try {
			logger.info("create_reconciliation_delay_task start:recoverType["+recoverType+"],assetsetuuid["+(context.getAssetSet()==null?null:context.getAssetSet().getAssetUuid())+"],"
				+ "repurchaseUuid["+(context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid())+"]");
			if(recoverType==null || !recoverType.isLoanAssetOrRepurchaseAsset()){
				logger.info("create_reconciliation_delay_task recoverType is not loanRepurAssset");
				return;
			}
//			String taskUuid = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
//					FinancialContractConfigurationCode.RECOVER_DELAY_TASK_UUID.toString());
//			if (StringUtils.isBlank(taskUuid)){
//				logger.info("taskUuid:"+taskUuid);
//				return;
//			}

			boolean isRepaymentOrderBQOverdueFileConfigured = financialContractConfigurationService.isFinancialContractConfigCodeConfiged(context.getFinancialContract().getUuid(),
				FinancialContractConfigurationCode.REPAYMENT_ORDER_CHECK_DELAY_TASK.getCode());
			if(isRepaymentOrderBQOverdueFileConfigured==true){
				//佰仟二三期逾期文件则过滤
				return;
			}

			RepaymentChargesDetail chargesDetail = null;
			if(recoverType.isLoanAsset()){
				chargesDetail = getChargesDetailByLoanAsset(context);
			}else if(recoverType.isRepurchaseAsset()){
				chargesDetail = getChargesDetailByRepurchaseDoc(context);
			}

			//正常核销
			String taskUuidForRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
				FinancialContractConfigurationCode.RECOVER_DELAY_TASK_UUID.getCode());
			if (StringUtils.isNotBlank(taskUuidForRecover)){
				logger.info("taskUuid:["+taskUuidForRecover+"]");
				reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForRecover);
			}

			//逾期核销
			String taskUuidForOverRecover = financialContractConfigurationService.getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
				FinancialContractConfigurationCode.BQ_OVER_RECOVER_DELAY_TASK_UUID.getCode());
			if (StringUtils.isNotBlank(taskUuidForOverRecover)){
				logger.info("taskUuid:["+taskUuidForOverRecover+"]");
				reconciliationDelayTaskProcess.processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail, context.getAssetSet(), context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid(), recoverType, taskUuidForOverRecover);
			}

			logger.info("create_reconciliation_delay_task end:recoverType["+recoverType+"],assetsetuuid["+(context.getAssetSet()==null?null:context.getAssetSet().getAssetUuid())+"],"
				+ "repurchaseUuid["+(context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid())+"]");
		} catch(Exception e){
			logger.error("create_reconciliation_delay_task error:recoverType["+recoverType+"],assetsetuuid["+(context.getAssetSet()==null?null:context.getAssetSet().getAssetUuid())+"],"
				+ "repurchaseUuid["+(context.getRepurchaseDoc()==null?null:context.getRepurchaseDoc().getRepurchaseDocUuid())+"],msg["+ExceptionUtils.getFullStackTrace(e)+"].");
		}
	}
	private LedgerBook getLedgerBook(String ledgerBookNo){
		if (com.zufangbao.sun.utils.StringUtils.isEmpty(ledgerBookNo)){
			return null;
		}
		LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
		if (null == book){
			return null;
		}
		return book;
	}

	public void recover_each_frozen_capital_amount(ReconciliationContext context){
		logger.info("begin to [ledgerBookNo]:"+context.getLedgerBookNo()
			+"[FinancialContractUUID]:"+context.getFinancialContract()
			.getUuid()+"[JournalVoucherUuid]:"+context
			.getIssuedJournalVoucher().getJournalVoucherUuid()
			+"[SourceDocumentUuid]:"+context.getSourceDocumentDetailUuid
			()+" [核销]  ");
		if (ledgerBookV2Handler.checkLegderBookVersion(getLedgerBook(context.getLedgerBookNo()))){
			logger.info("#begin to " +
				"recover_ReconciliationBase#Each_Frozen_Capital_Amount " +
				"[AccountTemplate] [核销]");
			ledgerBookV2Handler.recover_Each_Frozen_Capital_Amount(context.getLedgerBookNo(), context.getFinancialContract(), context.getCompany_customer().getCustomerUuid(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getSourceDocumentUuid(), context.getBookingAmount());
			logger.info("#end to " +
				"recover_ReconciliationBase#Each_Frozen_Capital_Amount " +
				"[AccountTemplate] [核销]");
		}
		if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(context.getLedgerBookNo())){
			businessPaymentVoucherHandler.recover_each_frozen_capital_amount(context.getLedgerBookNo(), context.getFinancialContract(), context.getCompany_customer().getCustomerUuid(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getSourceDocumentUuid(), context.getBookingAmount(), context.getTmpDepositDocUuidFromTmpDepositRecover(), context.getSecondNoFromTmpDepositRecover());
			logger.info("#end " +
				"recover_ReconciliationBase#Each_Frozen_Capital_Amount [预收] ");
		}

	}

	private RepaymentChargesDetail getChargesDetailByLoanAsset(ReconciliationContext context){
		Map<String,BigDecimal> detailAmountMap = context.getBookingDetailAmount();
		if(MapUtils.isEmpty(detailAmountMap)){
			detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getAssetSet().getAssetUuid());
		}
		return new RepaymentChargesDetail(detailAmountMap);
	}

	private Map<String, BigDecimal> getChargesDetailMapByLoanAsset(ReconciliationContext context){
		Map<String,BigDecimal> detailAmountMap = context.getBookingDetailAmount();
		if(MapUtils.isEmpty(detailAmountMap)){
			detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(), context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getAssetSet().getAssetUuid());
		}
		return detailAmountMap;
	}
	private RepaymentChargesDetail getChargesDetailByRepurchaseDoc(ReconciliationContext context){
		RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
		Map<String,BigDecimal> detailAmount = ledgerBookStatHandler.get_jv_repurchase_detail_amount(context.getLedgerBookNo(),context.getIssuedJournalVoucher().getJournalVoucherUuid(),context.getContract().getUuid());
		RepaymentChargesDetail repurchaseDetail =  new RepaymentChargesDetail();
		if(repurchaseDoc!=null){
			repurchaseDetail.fillRepurchaseAmount(detailAmount);
		}
		return repurchaseDetail;
	}

}