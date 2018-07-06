package com.suidifu.microservice.model;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.model.JournalVoucherResovler;
import com.suidifu.owlman.microservice.model.TmpDepositDocRecoverParams;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.AppAccount;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PlanRepaymentTimeConfiguration;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class ReconciliationRepaymentContext
{
	private String uniqueOfflineCashIdentity; //线下的现金流标志（还款订单匹配单条流水时）
	private TmpDepositDocRecoverParams tmpDepositDocParams;
	private String virutalAccountVersionLock;//虚拟账户的版本锁  none-optional;
	private VirtualAccount remittanceBorrowerVirtuaAccount;//借款人虚拟账户 none-optional;
	private DepositeAccountInfo borrowerDepositeAccountForLedegerBook;//借款人虚拟账户 ledgerbook adapter none-optional;
	private DepositeAccountInfo financialContractAccountForLedgerBook;//信托合同银行账户 ledgerbook adapter none-optional;
	private ContractAccount contractAccount;//借款人银行账户
	private Account financialContractBankAccount;//信托合同银行账户
	private VirtualAccount remittanceFinancialVirtualAccount;//信托合同虚拟账户 ledgerbook adapter none-optional;
	private Order order;//结算单
	private Contract contract;//合同
	private int currentPeriod;//期数
	private AssetSet assetSet;//待核销资产
	private SourceDocument sourceDocument;//凭证
	private SourceDocumentDetail sourceDocumentDetail;//凭证明细
	private AssetRecoverType recoverType;//核销资产类型
	private String ledgerBookNo;//账本编号
	private FinancialContract financialContract;//信托合同
	//TODO rename
	private JournalVoucherType journalVoucherType;//销账凭证类型
	private JournalVoucher issuedJournalVoucher;//销账凭证
	private JournalVoucherResovler resovler;//销账凭证参数解决器
	private Date actualRecycleTime;//实际回收时间
	private Customer borrower_customer;//借款人客户
	private Customer company_customer;//信托合同客户
	private BigDecimal bookingAmount;//待销账金额

	private BigDecimal receivalbeAmount;//资产应收额
	private BigDecimal guranteeAmount;//担保应收额
	private BigDecimal unearnedAmount;//未到期额
	private BigDecimal repurchaseAmount;//回购额
	private BigDecimal borrowerCustomerBalance;//借款人账户余额
	private BigDecimal financialAppBalance;//信托虚拟账户余额
	private Company company;//信托客户公司
	private RepurchaseDoc repurchaseDoc;//回购单
	private DeductApplication deductApplication;//第三方扣款请求
	private ReconciliationType reconciliationType;//销账类型
	private boolean alreadyReconciliation=false;//是否已经核销
	private Map<String,BigDecimal> bookingDetailAmount = new HashMap<String,BigDecimal>();
	private String sourceDocumentNo;
	private CashFlow cashFlow;
	private AppAccount appAccount;
	private AssetCategory assetCategory;
	private Customer cashFlowOwner;
	private Map<String,Object> appendix = new HashMap<String,Object>();





	private RepaymentOrderItem repaymentOrderItem;
	private RepaymentOrder repaymentOrder;
	private PaymentOrder paymentOrder;
	private boolean receivableJournalVoucher;
	private PlanRepaymentTimeConfiguration planRepaymentTimeLock;



	public PlanRepaymentTimeConfiguration getPlanRepaymentTimeLock() {
		return planRepaymentTimeLock;
	}
	public void setPlanRepaymentTimeLock(PlanRepaymentTimeConfiguration planRepaymentTimeLock) {
		this.planRepaymentTimeLock = planRepaymentTimeLock;
	}
	public String getUniqueOfflineCashIdentity() {
		return uniqueOfflineCashIdentity;
	}
	public void setUniqueOfflineCashIdentity(String uniqueOfflineCashIdentity) {
		this.uniqueOfflineCashIdentity = uniqueOfflineCashIdentity;
	}
	public String getTmpDepositDocUuidFromTmpDepositRecover(){
		if(tmpDepositDocParams!=null){
			return tmpDepositDocParams.getTmpDepositDocUuidFromTmpDepositRecover();
		}
		return "";
	}
	public String getSecondNoFromTmpDepositRecover(){
		if(tmpDepositDocParams!=null ){
			return tmpDepositDocParams.getSecondNoFromTmpDepositRecover();
		}
		return "";
	}

	public TmpDepositDocRecoverParams getTmpDepositDocParams() {
		return tmpDepositDocParams;
	}
	public void setTmpDepositDocParams(TmpDepositDocRecoverParams tmpDepositDocParams) {
		this.tmpDepositDocParams = tmpDepositDocParams;
	}
	public RepaymentOrderItem getRepaymentOrderItem() {
		return repaymentOrderItem;
	}
	public RepaymentOrder getRepaymentOrder() {
		return repaymentOrder;
	}
	public void setRepaymentOrderItem(RepaymentOrderItem repaymentOrderItem) {
		this.repaymentOrderItem = repaymentOrderItem;
	}
	public void setRepaymentOrder(RepaymentOrder repaymentOrder) {
		this.repaymentOrder = repaymentOrder;
	}

	public PaymentOrder getPaymentOrder() {
		return paymentOrder;
	}

	public void setPaymentOrder(PaymentOrder paymentOrder) {
		this.paymentOrder = paymentOrder;
	}

	public boolean isReceivableJournalVoucher() {
		return receivableJournalVoucher;
	}
	public void setReceivableJournalVoucher(boolean receivableJournalVoucher) {
		this.receivableJournalVoucher = receivableJournalVoucher;
	}
	public Customer getCashFlowOwner() {
		return cashFlowOwner;
	}
	public void setCashFlowOwner(Customer cashFlowOwner) {
		this.cashFlowOwner = cashFlowOwner;
	}
	public BigDecimal getBorrowerCustomerBalance() {
		return borrowerCustomerBalance;
	}
	public void setBorrowerCustomerBalance(BigDecimal borrowerCustomerBalance) {
		this.borrowerCustomerBalance = borrowerCustomerBalance;
	}
	public BigDecimal getFinancialAppBalance() {
		return financialAppBalance;
	}
	public void setFinancialAppBalance(BigDecimal financialAppBalance) {
		this.financialAppBalance = financialAppBalance;
	}
	public DepositeAccountInfo getBorrowerDepositeAccountForLedegerBook() {
		return borrowerDepositeAccountForLedegerBook;
	}
	public void setBorrowerDepositeAccountForLedegerBook(
		DepositeAccountInfo remittanceBorrowerDepositeAccountInfo) {
		this.borrowerDepositeAccountForLedegerBook = remittanceBorrowerDepositeAccountInfo;
	}
	public boolean isAlreadyReconciliation() {
		return alreadyReconciliation;
	}
	public void setAlreadyReconciliation(boolean alreadyReconciliation) {
		this.alreadyReconciliation = alreadyReconciliation;
	}
	public ReconciliationType getReconciliationType() {
		return reconciliationType;

	}
	public void setReconciliationType(ReconciliationType reconciliationType) {
		this.reconciliationType = reconciliationType;
	}
	public RepurchaseDoc getRepurchaseDoc() {
		return repurchaseDoc;
	}
	public void setRepurchaseDoc(RepurchaseDoc repurchaseDoc) {
		this.repurchaseDoc = repurchaseDoc;
	}
	public ContractAccount getContractAccount() {
		return contractAccount;
	}
	public void setContractAccount(ContractAccount contractAccount) {
		this.contractAccount = contractAccount;
	}
	public com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType getJournalVoucherType() {
		return journalVoucherType;
	}
	public void setJournalVoucherType(com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType journalVoucherType) {
		this.journalVoucherType = journalVoucherType;
	}
	public Long getCompanyId() {
		return company==null?null:company.getId();
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public BigDecimal getReceivalbeAmount() {
		return receivalbeAmount;
	}
	public void setReceivalbeAmount(BigDecimal receivalbeAmount) {
		this.receivalbeAmount = receivalbeAmount;
	}
	public BigDecimal getGuranteeAmount() {
		return guranteeAmount;
	}
	public void setGuranteeAmount(BigDecimal guranteeAmount) {
		this.guranteeAmount = guranteeAmount;
	}
	public BigDecimal getUnearnedAmount() {
		return unearnedAmount;
	}
	public void setUnearnedAmount(BigDecimal unearnedAmount) {
		this.unearnedAmount = unearnedAmount;
	}
	public BigDecimal getRepurchaseAmount() {
		return repurchaseAmount;
	}
	public void setRepurchaseAmount(BigDecimal repurchaseAmount) {
		this.repurchaseAmount = repurchaseAmount;
	}
	public Customer getBorrower_customer() {
		return borrower_customer;
	}
	public void setBorrower_customer(Customer borrower_customer) {
		this.borrower_customer = borrower_customer;
	}
	public Customer getCompany_customer() {
		return company_customer;
	}
	public void setCompany_customer(Customer company_customer) {
		this.company_customer = company_customer;
	}
	public BigDecimal getBookingAmount() {
		return bookingAmount;
	}
	public void setBookingAmount(BigDecimal bookingAmount) {
		this.bookingAmount = bookingAmount;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Contract getContract() {
		return contract;
	}
	public void setContract(Contract contract) {
		this.contract = contract;
	}
	public int getCurrentPeriod() {
		return currentPeriod;
	}
	public void setCurrentPeriod(int currentPeriod) {
		this.currentPeriod = currentPeriod;
	}
	public AssetSet getAssetSet() {
		return assetSet;
	}
	public void setAssetSet(AssetSet assetSet) {
		this.assetSet = assetSet;
	}

	public void setSourceDocument(SourceDocument sourceDocument) {
		this.sourceDocument = sourceDocument;
	}
	public SourceDocumentDetail getSourceDocumentDetail() {
		return sourceDocumentDetail;
	}
	public SourceDocument getSourceDocument() {
		return sourceDocument;
	}

	public String getSourceDocumentDetailUuid() {
		if(sourceDocumentDetail==null) return "";
		return sourceDocumentDetail.getUuid();
	}
	public String getSourceDocumentUuid() {
		if(sourceDocument==null) return "";
		return sourceDocument.getUuid();
	}
	public void setSourceDocumentDetail(SourceDocumentDetail sourceDocumentDetail) {
		this.sourceDocumentDetail = sourceDocumentDetail;


	}

	public AssetRecoverType getRecoverType() {
		return recoverType;
	}
	public void setRecoverType(AssetRecoverType recoverType) {
		this.recoverType = recoverType;
	}
	public DepositeAccountInfo getFinancialContractAccountForLedgerBook() {
		return financialContractAccountForLedgerBook;
	}
	public void setFinancialContractAccountForLedgerBook(DepositeAccountInfo financialContractAccountForLedgerBook) {
		this.financialContractAccountForLedgerBook = financialContractAccountForLedgerBook;
	}
	public String getVirutalAccountVersionLock() {
		return virutalAccountVersionLock;
	}
	public void setVirutalAccountVersionLock(String virutalAccountVersionLock) {
		this.virutalAccountVersionLock = virutalAccountVersionLock;
	}

	public String getLedgerBookNo() {
		return ledgerBookNo;
	}
	public void setLedgerBookNo(String ledgerBookNo) {
		this.ledgerBookNo = ledgerBookNo;
	}
	public FinancialContract getFinancialContract() {
		return financialContract;
	}
	public void setFinancialContract(FinancialContract financialContract) {
		this.financialContract = financialContract;
	}
	public JournalVoucher getIssuedJournalVoucher() {
		return issuedJournalVoucher;
	}
	public void setIssuedJournalVoucher(JournalVoucher issuedJournalVoucher) {
		this.issuedJournalVoucher = issuedJournalVoucher;
	}
	public Date getActualRecycleTime() {
		return actualRecycleTime;
	}
	public void setActualRecycleTime(Date actualRecycleTime) {
		this.actualRecycleTime = actualRecycleTime;
	}

	public DeductApplication getDeductApplication() {
		return deductApplication;
	}
	public void setDeductApplication(DeductApplication deductApplication) {
		this.deductApplication = deductApplication;
	}
	public void resovleJournalVoucher(AccountSide side,CounterAccountType fromAccountType,String relatedBillUuid)
	{
		resovler=new JournalVoucherResovler();
		resovler.setAccountSide(side);
		resovler.setFromAccountType(fromAccountType);
		resovler.setRelatedBillUuid(relatedBillUuid);
		resovleCashFlowParties();
		resolveSourceDocumentParties();
	}

	public JournalVoucherResovler getJournalVoucherResovler() {
		return resovler;
	}
	public void setJournalVoucherResovler(JournalVoucherResovler resovler) {
		this.resovler = resovler;
	}
	public VirtualAccount getRemittanceBorrowerVirtuaAccount() {
		return remittanceBorrowerVirtuaAccount;
	}
	public void setRemittanceBorrowerVirtuaAccount(
		VirtualAccount remittanceBorrowerVirtuaAccount) {
		this.remittanceBorrowerVirtuaAccount = remittanceBorrowerVirtuaAccount;
	}
	public VirtualAccount getRemittanceFinancialVirtualAccount() {
		return remittanceFinancialVirtualAccount;
	}
	public void setRemittanceFinancialVirtualAccount(
		VirtualAccount remittanceFinancialVirtualAccount) {
		this.remittanceFinancialVirtualAccount = remittanceFinancialVirtualAccount;
	}
	public Account getFinancialContractBankAccount() {
		return financialContractBankAccount;
	}
	public void setFinancialContractBankAccount(Account financialContractBankAccount) {
		this.financialContractBankAccount = financialContractBankAccount;
	}
	public Map<String, BigDecimal> getBookingDetailAmount() {
		return bookingDetailAmount;
	}
	public void setBookingDetailAmount(Map<String, BigDecimal> bookingDetailAmount) {
		this.bookingDetailAmount = bookingDetailAmount;
	}

	public void setSourceDocumentNo(String sourceDocumentNo) {
		this.sourceDocumentNo = sourceDocumentNo;
	}
	public DepositeAccountInfo getRemittanceAccountForLedgerBook()
	{
		DepositeAccountInfo ledgerBookBankAccountForFinancialContract=this.getFinancialContractAccountForLedgerBook();
		DepositeAccountInfo ledgerBookBankAccountForBorrow=this.getBorrowerDepositeAccountForLedegerBook();
		if(this.getReconciliationType().isRecoveredByVirtualAccountSelf()){
			return ledgerBookBankAccountForBorrow;
		}else {
			return ledgerBookBankAccountForFinancialContract;
		}
	}
	public VirtualAccount getRemittanceVirtualAccount()
	{
		if(this.getReconciliationType().isRecoveredByVirtualAccountSelf()){
			return getRemittanceBorrowerVirtuaAccount();
		}else {
			return getRemittanceFinancialVirtualAccount();
		}
	}

	public String getSourceDocumentNo() {
		return sourceDocumentNo;
	}
	private void resovleCashFlowParties() {

		if(resovler.getFromAccountType().equals(CounterAccountType.BankAccount))
		{
			if(this.receivableJournalVoucher){
				resovler.setCounterPartyAccount(this.getPaymentOrder().getCounterAccountNo());
				resovler.setCounterPartyAccountName(this.getPaymentOrder().getCounterAccountName());
			}else{
				resovler.setCounterPartyAccount(this.getSourceDocumentDetail().getPaymentAccountNo());
				resovler.setCounterPartyAccountName(getSourceDocumentDetail().getPaymentName());
			}
		}
		else if(resovler.getFromAccountType().equals(CounterAccountType.VirtualAccount))
		{
//			resovler.setCounterPartyAccount(this.getRemittanceVirtualAccount().getVirtualAccountNo());
//			resovler.setCounterPartyAccountName(this.getRemittanceVirtualAccount().getOwnerName());
			if(getJournalVoucherType().isVirtualAccountRemittanceBySelf())
			{
				resovler.setCounterPartyAccount(this.getRemittanceBorrowerVirtuaAccount().getVirtualAccountNo());
				resovler.setCounterPartyAccountName(this.getRemittanceBorrowerVirtuaAccount().getOwnerName());
			}
			else
			{
				resovler.setCounterPartyAccount(this.getRemittanceFinancialVirtualAccount().getVirtualAccountNo());
				resovler.setCounterPartyAccountName(getRemittanceFinancialVirtualAccount().getOwnerName());
			}
		}


		if(getJournalVoucherType().equals(com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER)
			||getJournalVoucherType().equals(com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType.BANK_CASHFLOW_DEPOSIT)){
			resovler.setLocalPartyAccount(getFinancialContractBankAccount().getAccountNo());
			resovler.setLocalPartyAccountName(this.getFinancialContractBankAccount().getAccountName());
		}
		else
		{
			resovler.setLocalPartyAccount(this.getContractAccount().getPayAcNo());
			resovler.setLocalPartyAccountName(getContractAccount().getPayerName());
		}
	}
	private void resolveSourceDocumentParties() {

		if(getJournalVoucherType().equals(com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType.BANK_CASHFLOW_DEPOSIT)==true)
			return;

		if(getSourceDocument()!=null){
			resovler.setSourceDocumentLocalPartyAccount(getSourceDocument().getOutlierAccount());
			resovler.setSourceDocumentLocalPartyName(getSourceDocument().getOutlieAccountName());
		}
		if(getSourceDocumentDetail()!=null) {
			resovler.setSourceDocumentCounterPartyAccount(getSourceDocumentDetail().getPaymentAccountNo());
			resovler.setSourceDocumentCounterPartyName(getSourceDocumentDetail().getPaymentName());
		}

	}
	public CashFlow getCashFlow() {
		return cashFlow;
	}
	public void setCashFlow(CashFlow cashFlow) {
		this.cashFlow = cashFlow;
	}
	public AppAccount getAppAccount() {
		return appAccount;
	}
	public void setAppAccount(AppAccount appAccount) {
		this.appAccount = appAccount;
	}
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}
	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}
	public Map<String, Object> getAppendix() {
		return appendix;
	}
	public void setAppendix(Map<String, Object> appendix) {
		this.appendix = appendix;
	}
	public void putKVToAppendix(String key,Object value){

		this.appendix.put(key, value);
	}
	public Object getValueFromAppendix(String key){

		return this.appendix.get(key);
	}

	public String getCashFlowIdentity(){
		return getIssuedJournalVoucher()==null?"":getIssuedJournalVoucher().getNotificationMemo();
	}
	public boolean isRepaymentPlanTimeInvalid(PlanRepaymentTimeConfiguration planRepaymentTimeLock){
		return isRepaymentPlanTimeValid(financialContract, planRepaymentTimeLock)==false;
	}

	public boolean isRepaymentPlanTimeValid(FinancialContract financialContract, PlanRepaymentTimeConfiguration planRepaymentTimeLock){
		if(planRepaymentTimeLock == PlanRepaymentTimeConfiguration.NEITHER || repaymentOrderItem.getRepaymentPlanTime() == null || financialContract.isRepaymentDayCheck()==false){
			return false;
		}
		if(PlanRepaymentTimeConfiguration.planRepaymentTimeisOpen(planRepaymentTimeLock, repaymentOrderItem.getRepaymentWay())){
			return true;
		}
		return false;
	}
}