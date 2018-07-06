package com.suidifu.microservice.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.microservice.enume.CounterAccountType;
import com.suidifu.microservice.enume.JournalVoucherCheckingLevel;
import com.suidifu.microservice.enume.JournalVoucherCompleteness;
import com.suidifu.microservice.enume.JournalVoucherStatus;
import com.suidifu.microservice.enume.JournalVoucherType;
import com.suidifu.microservice.enume.SecondJournalVoucherType;
import com.suidifu.microservice.enume.SettlementModes;
import com.suidifu.microservice.enume.ThirdJournalVoucherType;
import com.suidifu.microservice.model.ContractCategory;
import com.suidifu.microservice.model.JournalVoucherMapSpec;
import com.suidifu.microservice.model.JournalVoucherResovler;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.microservice.model.ThirdPartVoucherSourceMapSpec;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.directbank.business.AppArriveRecord;
import com.zufangbao.sun.entity.directbank.business.BankSide;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.utils.GeneratorUtils;

@Entity
public class JournalVoucher {

	/* appendix*/
	private static final String APPENDIX_REMARK = "remark";
	private static final String APPENDIX_CLEARING_TIME = "clearing_time";
	@Id
	@GeneratedValue
	private Long id;
	
	private String journalVoucherUuid;
	
	private String batchUuid;	
	
	private Long companyId;
	
	@Enumerated(EnumType.ORDINAL)
	private AccountSide  accountSide;
	
	/********by Accounter********************/
	private BigDecimal bookingAmount;
	
	
	/***********jv凭证状态***********/
	//日记账凭证状态 0:已建,1:已制证,2:凭证作废
	@Enumerated(EnumType.ORDINAL)
	private JournalVoucherStatus status;
	
	// 日记帐凭证完整性 0:现金流条目流缺失,1:交易通知条目缺失,2:交易通知歧义,3:条目完整
	@Enumerated(EnumType.ORDINAL)
	private JournalVoucherCompleteness completeness;
	
	//日记账凭证检查等级 0:自动制证,1:人工二次确认
	@Enumerated(EnumType.ORDINAL)
	private JournalVoucherCheckingLevel checkingLevel;
	
	@Enumerated(EnumType.ORDINAL)
	private SettlementModes settlementModes;
	
	
	
	/************bv相关****************/
	private String businessVoucherUuid;
	
	private String businessVoucherTypeUuid;
	
	
	
	/**************流水信息****************/
	
	private BigDecimal cashFlowAmount;
	private String cashFlowSerialNo;
	/** 上传给支付平台的自定义信息 */
	//现金流标志
	@Column(columnDefinition = "text")
	private String notificationMemo;
	private String notificationIdentity;
	private String bankIdentity;
	/**
	 * 字段意义变更,新（资金入账时间）
	 */
	@Type(type = "timestamp")
	private Date notifiedDate;
	@Column(columnDefinition = "text")
	private String cashFlowBreif;
	//通道流水
	private String cashFlowUuid;
	@Enumerated(EnumType.ORDINAL)
	private CashFlowChannelType cashFlowChannelType;
	/**
	 * 通知变更uuid
	 */
	private String notificationRecordUuid;

	
	/************资金单据***********/
	private String sourceDocumentUuid;
	/** 支付平台的通知标识 */
	private String sourceDocumentIdentity;
	/** 支付平台流水号 */
	private String sourceDocumentCashFlowSerialNo;
	private BigDecimal sourceDocumentAmount;
	@Column(columnDefinition = "text")
	private String sourceDocumentBreif;
	//unique_bill_id
	private String billingPlanUuid;
	@Type(type = "timestamp")
	private Date tradeTime;

	
	
	/*************单据来源*************/
	//一级凭证来源
	private JournalVoucherType journalVoucherType;
	//二级凭证来源：例如系统扣款；接口扣款
	private SecondJournalVoucherType secondJournalVoucherType;
	//三级凭证来源：(资金转移方式)例如 提前划扣，正常，逾期
	private ThirdJournalVoucherType thirdJournalVoucherType;
	
	
	/*********来往账户 fromAccount  to  toAccount  **********/
	private String counterPartyAccount;
	private String counterPartyName;
	private String localPartyAccount;
	private String localPartyName;
	
	private String sourceDocumentCounterPartyUuid;
	private String sourceDocumentCounterPartyAccount;
	private String sourceDocumentCounterPartyName;
	private String sourceDocumentLocalPartyAccount;
	private String sourceDocumentLocalPartyName;
	
	
	/************* 业务单据 *************/
	//financialContractUuid
	private String relatedBillContractInfoLv1;
	//contractUuid
	private String relatedBillContractInfoLv2;
	//assetSetUuid,repurchaseUuid
	private String relatedBillContractInfoLv3;
	//信托项目名称
	private String relatedBillContractNoLv1;
	//合同编号
	private String relatedBillContractNoLv2;
	//还款计划编号,repurchaseUuid(即编号)
	private String relatedBillContractNoLv3;
	//订单编号（orderNo）
	private String relatedBillContractNoLv4;
	//凭证编号
	private String sourceDocumentNo;
	//存放备注等其他信息
	private String appendix;
	
	

	private String cashFlowAccountInfo;
	
	//orderNo of order
	@Enumerated(EnumType.ORDINAL)
	private CounterAccountType counterAccountType;
	/*************new fields end*************/
	
	
	/**
	 * 创建时间
	 */
	@Type(type = "timestamp")
	private Date createdDate;
	
	private Date issuedTime;
	
	private String journalVoucherNo;
	
	private Date lastModifiedTime;
	
	//0 未生成 1 已生成 
	private int  isHasDataSyncLog =0 ;
	
	public JournalVoucher(){
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString().replace("-", "");
	}
	@Deprecated
	public JournalVoucher(AccountSide acountSide){
		this();
		this.accountSide = acountSide;
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.journalVoucherUuid = UUID.randomUUID().toString().replace("-", "");
		this.checkingLevel = getDefaultCheckingLevel();
	}
	@Deprecated
	public JournalVoucher(String notificationRecordUuid, Date notifiedDate,
			String notifiedIdentity, String notifiedCashFlowSerialNo,
			BigDecimal notifiedAmount, Long companyId,
			String virtualAccountUuid) {
		this();
		this.notificationRecordUuid = notificationRecordUuid;
		this.notifiedDate = notifiedDate;
		this.sourceDocumentIdentity = notifiedIdentity;
		this.sourceDocumentCashFlowSerialNo = notifiedCashFlowSerialNo;
		this.sourceDocumentAmount = notifiedAmount;
		this.companyId = companyId;
		this.batchUuid = UUID.randomUUID().toString().replace("-", "");
	}
	/**
	 * 利用现金流的信息创建journal_voucher
	 * @param appArriveRecord
	 * @return
	 */
	@Deprecated
	public JournalVoucher createFromCashFlow(AppArriveRecord appArriveRecord){
		
		this.cashFlowUuid = appArriveRecord.getCashFlowUid();
		this.counterAccountType=CounterAccountType.BankAccount;
		this.counterPartyAccount = appArriveRecord.getPayAcNo();
		this.counterPartyName = appArriveRecord.getPayName();
		this.tradeTime = appArriveRecord.getTime();
		this.cashFlowSerialNo = appArriveRecord.getVouhNo();
		this.notificationMemo = appArriveRecord.getSummary();
		this.notificationIdentity = appArriveRecord.getSerialNo();
		this.bankIdentity = appArriveRecord.getPartnerId();
		this.cashFlowAmount = appArriveRecord.getAmount();
		this.cashFlowChannelType = appArriveRecord.getCashFlowChannelType();
		this.cashFlowBreif = StringUtils.EMPTY;
		this.journalVoucherUuid = UUID.randomUUID().toString();
		App app = appArriveRecord.getApp();
		if (app == null){
			this.companyId = null;
		} else {
			Company company = app.getCompany();
			if (company == null){
				this.companyId = null;
			}
			this.companyId = company.getId();
		}
		this.checkingLevel = getDefaultCheckingLevel();
		this.accountSide = AccountSide.reverse(BankSide.fromValue(appArriveRecord.getDrcrf()));
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		
		return this;
	}
	/** directbank cashflow */
	public JournalVoucher createFromCashFlow(CashFlow cashFlow,Company company){
		
		this.cashFlowUuid = cashFlow.getCashFlowUuid();
		this.counterAccountType=CounterAccountType.BankAccount;
		this.counterPartyAccount = cashFlow.getCounterAccountNo();
		this.counterPartyName = cashFlow.getCounterAccountName();
		this.tradeTime = cashFlow.getTransactionTime();
		this.cashFlowSerialNo = cashFlow.getBankSequenceNo();
		//现金流标志
		this.notificationMemo = cashFlow.getCashFlowIdentity();
		this.notificationIdentity = cashFlow.getBankSequenceNo();
		this.bankIdentity = cashFlow.getCounterAccountNo();
		this.cashFlowAmount = cashFlow.getTransactionAmount();
		this.cashFlowChannelType = cashFlow.getCashFlowChannelType();
		this.cashFlowBreif = cashFlow.getRemark();
		this.journalVoucherUuid = UUID.randomUUID().toString();
			if(company != null){
		this.companyId = company.getId();
		this.cashFlowAccountInfo = company.getUuid();
		}
		this.checkingLevel = getDefaultCheckingLevel();
		this.accountSide = AccountSide.fromLedgerBookAccountSide(cashFlow.getAccountSide());
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		//新增自己入账时间
		this.notifiedDate = cashFlow.getTransactionTime();
		return this;
	}
	
	
	/**
	 * 
	 * @param sourceDocument
	 * @return
	 */
	public JournalVoucher copyFromSourceDocument(SourceDocument sourceDocument){
		this.sourceDocumentUuid = sourceDocument.getSourceDocumentUuid();
		this.sourceDocumentNo = sourceDocument.getSourceDocumentNo();
		this.sourceDocumentIdentity = sourceDocument.getOutlierDocumentUuid();
		this.sourceDocumentCashFlowSerialNo = sourceDocument.getOutlierSerialGlobalIdentity();
		this.sourceDocumentAmount = sourceDocument.getOutlierAmount();
		this.sourceDocumentBreif = sourceDocument.getOutlierBreif();
		this.sourceDocumentCounterPartyAccount = sourceDocument.getOutlierCounterPartyAccount();
		this.sourceDocumentCounterPartyName = sourceDocument.getOutlierCounterPartyName();
		this.notifiedDate = sourceDocument.getOutlierTradeTime();
		this.tradeTime = sourceDocument.getOutlierTradeTime();
		this.companyId = sourceDocument.getCompanyId();
		this.checkingLevel = getDefaultCheckingLevel();
		this.accountSide = sourceDocument.getSourceAccountSide();
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.settlementModes = sourceDocument.getOutlierSettlementModes();
		return this;
	}
	
	@Deprecated
	public JournalVoucher createFromComposentoryVoucher(SourceDocumentDetail sourceDocumentDetail,String sourceDocumentNo,AccountSide accountSide, Company company, ContractAccount contractAccount, VirtualAccount virtualAccount){
		this.sourceDocumentUuid = sourceDocumentDetail.getUuid();
		this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
		this.counterAccountType = CounterAccountType.VirtualAccount;
		if(virtualAccount!=null){
			this.counterPartyAccount = virtualAccount.getVirtualAccountNo();
			this.counterPartyName = virtualAccount.getOwnerName();
		}
		if(company!=null){
			this.companyId = company.getId();
			this.cashFlowAccountInfo = company.getUuid();
		}
		this.sourceDocumentAmount = sourceDocumentDetail.getAmount();
		if(contractAccount!=null){
			this.sourceDocumentCounterPartyAccount = contractAccount.getPayAcNo();
			this.sourceDocumentCounterPartyName = contractAccount.getPayerName();
		}
				
		this.checkingLevel = getDefaultCheckingLevel();
		this.accountSide = accountSide;
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.sourceDocumentNo = sourceDocumentNo;
		return this;
	}
	public JournalVoucher createFromThirdPartyDeductionVoucher(SourceDocumentDetail sourceDocumentDetail,AccountSide accountSide, Company company, Account receiveaccount, SourceDocument sourceDocument){
		this.sourceDocumentUuid = sourceDocumentDetail.getUuid();
		this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
		if(company!=null){
			this.companyId = company.getId();
			this.cashFlowAccountInfo = company.getUuid();
		}
		this.counterAccountType = CounterAccountType.BankAccount;
		this.counterPartyAccount = sourceDocumentDetail.getPaymentAccountNo();
		this.counterPartyName = sourceDocumentDetail.getPaymentName();
		this.sourceDocumentAmount = sourceDocumentDetail.getAmount();
		this.sourceDocumentCounterPartyAccount = sourceDocumentDetail.getPaymentAccountNo();
		this.sourceDocumentCounterPartyName = sourceDocumentDetail.getPaymentName();
		if(receiveaccount!=null){
			this.localPartyAccount=receiveaccount.getAccountNo();
			this.localPartyName=receiveaccount.getAccountName();
			this.sourceDocumentLocalPartyAccount= receiveaccount.getAccountNo();
			this.sourceDocumentLocalPartyName= receiveaccount.getAccountName();
		}
		//现金流标志
		this.notificationMemo=sourceDocument.getOutlierMemo();
		this.checkingLevel = getDefaultCheckingLevel();
		this.accountSide = accountSide;
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.tradeTime = sourceDocument.getOutlierTradeTime();
		this.completeness = JournalVoucherCompleteness.COMPLETE;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.sourceDocumentNo = sourceDocument.getSourceDocumentNo();
		this.notificationRecordUuid = sourceDocumentDetail.getSecondNo();
		return this;
	}
	
	private Date getOutlierCapitalTime(ReconciliationContext context){
		if(JournalVoucherType.JournalVoucherTypeIsOnline(context.getJournalVoucherType())){
			return null;
		}
		if(context.getSourceDocument()==null){
			return null;
		}
		return context.getSourceDocument().getOutlierTradeTime();
	}
	
	public JournalVoucher createFromContext(ReconciliationContext context){
		SourceDocumentDetail sourceDocumentDetail=context.getSourceDocumentDetail();
		if(sourceDocumentDetail!=null)
		{
			this.sourceDocumentUuid =sourceDocumentDetail.getUuid();
			this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
			this.sourceDocumentAmount = sourceDocumentDetail.getAmount();
			this.bookingAmount=sourceDocumentDetail.getAmount();
		}
		else
			this.bookingAmount=context.getBookingAmount();
	
		resovleJournalVoucherParties(context.getJournalVoucherResovler());
		
		if(context.getCompany()!=null){
			this.companyId = context.getCompany().getId();
			this.cashFlowAccountInfo = context.getCompany().getUuid();
		}
				
		this.checkingLevel = getDefaultCheckingLevel();
		
		if(context.getSourceDocument()!=null){
			this.tradeTime = context.getActualRecycleTime();
			this.notificationMemo = context.getSourceDocument().getOutlierMemo();
			
		}else {
			this.tradeTime = new Date();
		}
		this.status = JournalVoucherStatus.VOUCHER_ISSUED;
		this.issuedTime = new Date();
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.sourceDocumentNo = context.getSourceDocumentNo();
		this.journalVoucherType=context.getJournalVoucherType();
		this.notifiedDate = getOutlierCapitalTime(context);
		return this;
	}
	

	public JournalVoucher createFromContext(ReconciliationRepaymentContext context){
		SourceDocumentDetail sourceDocumentDetail=context.getSourceDocumentDetail();
		if(sourceDocumentDetail!=null)
		{
			this.sourceDocumentUuid =sourceDocumentDetail.getUuid();
			this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
			this.sourceDocumentAmount = sourceDocumentDetail.getAmount();
		}
		this.bookingAmount=context.getBookingAmount();
	
		resovleJournalVoucherParties(context.getJournalVoucherResovler());
		
		if(context.getCompany()!=null){
			this.companyId = context.getCompany().getId();
			this.cashFlowAccountInfo = context.getCompany().getUuid();
		}
				
		this.checkingLevel = getDefaultCheckingLevel();
		if(context.getSourceDocument()!=null){
			this.notificationMemo = context.getSourceDocument().getOutlierMemo();
		} else{
			this.notificationMemo = context.getUniqueOfflineCashIdentity();
		}
		this.tradeTime = context.getActualRecycleTime();
		this.status = JournalVoucherStatus.VOUCHER_ISSUED;
		this.issuedTime = new Date();
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.journalVoucherType=context.getJournalVoucherType();
		this.notificationRecordUuid = context.getRepaymentOrderItem()==null?"":context.getRepaymentOrderItem().getOrderDetailUuid();
		this.notifiedDate = context.getRepaymentOrder().getCashFlowTime();
		return this;
	}
	
	public void resovleJournalVoucherParties(JournalVoucherResovler resovler) {
		this.counterAccountType =resovler.getFromAccountType();
		this.counterPartyAccount=resovler.getCounterPartyAccount();
		this.counterPartyName=resovler.getCounterPartyAccountName();
		this.sourceDocumentCounterPartyAccount=resovler.getSourceDocumentCounterPartyAccount();
		this.sourceDocumentCounterPartyName=resovler.getSourceDocumentCounterPartyName();
		this.sourceDocumentLocalPartyAccount=resovler.getSourceDocumentLocalPartyAccount();
		this.sourceDocumentLocalPartyName=resovler.getSourceDocumentLocalPartyName();
		this.localPartyAccount=resovler.getLocalPartyAccount();
		this.localPartyName=resovler.getLocalPartyAccountName();
		this.accountSide = resovler.getAccountSide();
	}
	@Deprecated
	public JournalVoucher createFromActivePaymentVoucher(SourceDocumentDetail sourceDocumentDetail,AccountSide accountSide, Company company, VirtualAccount virtualAccount, String sourceDocumentNo){
		this.sourceDocumentUuid = sourceDocumentDetail.getUuid();
		this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
		this.counterAccountType = CounterAccountType.VirtualAccount;
		this.counterPartyAccount = virtualAccount.getVirtualAccountNo();
		this.counterPartyName = virtualAccount.getVirtualAccountAlias();
		if(company!=null){
			this.companyId = company.getId();
			this.cashFlowAccountInfo = company.getUuid();
		}
		this.sourceDocumentAmount = sourceDocumentDetail.getAmount();
		this.sourceDocumentCounterPartyAccount = sourceDocumentDetail.getPaymentAccountNo();
		this.sourceDocumentCounterPartyName = sourceDocumentDetail.getPaymentName();
				
		this.checkingLevel = getDefaultCheckingLevel();
		this.accountSide = accountSide;
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.sourceDocumentNo = sourceDocumentNo;
		return this;
	}
	@Deprecated
	public JournalVoucher create_from_auto_recover(VirtualAccount virtualAccount, Company company, AccountSide accountSide){
		this.counterAccountType = CounterAccountType.VirtualAccount;
		this.counterPartyAccount = virtualAccount.getVirtualAccountNo();
		this.counterPartyName = virtualAccount.getVirtualAccountAlias();
		if(company!=null){
			this.companyId = company.getId();
			this.cashFlowAccountInfo = company.getUuid();
		}
		this.accountSide = accountSide;
		this.status = JournalVoucherStatus.VOUCHER_CREATED;
		this.createdDate = new Date();
		this.lastModifiedTime = new Date();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		return this;
	}
	@Deprecated
	public JournalVoucher fill_active_payment_voucher_info_after_auto_remittance(SourceDocumentDetail sourceDocumentDetail, String sourceDocumentNo){
		//TODO 余额支付与主动还款凭证的对应。
		this.sourceDocumentUuid = sourceDocumentDetail.getUuid();
		this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
		this.counterAccountType = CounterAccountType.VirtualAccount;
		this.sourceDocumentAmount = sourceDocumentDetail.getAmount();
		this.sourceDocumentCounterPartyAccount = sourceDocumentDetail.getPaymentAccountNo();
		this.sourceDocumentCounterPartyName = sourceDocumentDetail.getPaymentName();
		this.sourceDocumentNo = sourceDocumentNo;
		return this;
	}
	
	public JournalVoucher compensate_active_payment_info_into_existed_auto_remittance(SourceDocumentDetail sourceDocumentDetail, String sourceDocumentNo, JournalVoucherType journalVoucherType, JournalVoucherResovler journalVoucherResovler, SourceDocument sourceDocument){
		//TODO 余额支付与主动还款凭证的对应。
		
		this.sourceDocumentUuid =sourceDocumentDetail.getUuid();
		this.sourceDocumentIdentity = sourceDocumentDetail.getSourceDocumentUuid();
		resovleJournalVoucherParties(journalVoucherResovler);
		this.sourceDocumentNo = sourceDocumentNo;
		this.setJournalVoucherType(journalVoucherType);
		if(sourceDocument!=null){
			this.tradeTime = sourceDocument.getOutlierTradeTime();
			//资金入账时间
			this.notifiedDate = sourceDocument.getOutlierTradeTime();
			//现金流标志
			this.notificationMemo = sourceDocument.getOutlierMemo();
		}
		this.issuedTime = new Date();
		this.lastModifiedTime = new Date();
		return this;
	}
	
	public JournalVoucher compensate_active_payment_info_into_existed_auto_remittance_repayment_order( JournalVoucherType journalVoucherType, JournalVoucherResovler journalVoucherResovler, Date actualRecycleTime, String cashFlowIdentity){
		//TODO 余额支付与主动还款凭证的对应。
		
		resovleJournalVoucherParties(journalVoucherResovler);
		this.setJournalVoucherType(journalVoucherType);
		this.tradeTime = actualRecycleTime;
		this.notificationMemo = cashFlowIdentity;
		this.issuedTime = new Date();
		this.lastModifiedTime = new Date();
		return this;
	}
		
		
	/**
	 * copy现金流的数据到journal_voucher
	 * @param appArriveRecord
	 * @return
	 */
	public JournalVoucher copyFromCashFlow(CashFlow cashFlow,Company company){
		
		this.cashFlowUuid = cashFlow.getCashFlowUuid();
		this.counterAccountType=CounterAccountType.BankAccount;
		this.counterPartyAccount = cashFlow.getCounterAccountNo();
		this.counterPartyName = cashFlow.getCounterAccountName();
		this.tradeTime = cashFlow.getTransactionTime();
		this.cashFlowSerialNo = cashFlow.getBankSequenceNo();
		//现金流标志
		this.notificationMemo = cashFlow.getCashFlowIdentity();
		this.notificationIdentity = cashFlow.getBankSequenceNo();
		this.bankIdentity = cashFlow.getCounterAccountNo();
		this.cashFlowAmount = cashFlow.getTransactionAmount();
		this.cashFlowChannelType = CashFlowChannelType.DirectBank;
		this.cashFlowBreif = cashFlow.getRemark();
		this.journalVoucherUuid = UUID.randomUUID().toString();
		this.companyId = company.getId();
		this.cashFlowAccountInfo = company.getUuid();
		this.accountSide = AccountSide.fromLedgerBookAccountSide(cashFlow.getAccountSide());
		this.notifiedDate = cashFlow.getTransactionTime();
		return this;
	}
	@Deprecated
	public JournalVoucher copyDataFromCashFlow(AppArriveRecord appArriveRecord){
		
		this.cashFlowUuid = appArriveRecord.getCashFlowUid();
		this.counterAccountType=CounterAccountType.BankAccount;
		this.counterPartyAccount = appArriveRecord.getPayAcNo();
		this.counterPartyName = appArriveRecord.getPayName();
		this.tradeTime = appArriveRecord.getTime();
		this.cashFlowSerialNo = appArriveRecord.getVouhNo();
		this.notificationMemo = appArriveRecord.getSummary();
		this.notificationIdentity = appArriveRecord.getSerialNo();
		this.bankIdentity = appArriveRecord.getPartnerId();
		this.cashFlowAmount = appArriveRecord.getAmount();
		this.cashFlowBreif = StringUtils.EMPTY;
		this.accountSide = AccountSide.reverse(BankSide.fromValue(appArriveRecord.getDrcrf()));
		this.cashFlowChannelType = appArriveRecord.getCashFlowChannelType();
		
		return this;
	}
	
	public JournalVoucher fill_voucher_and_booking_amount(String billingPlanUuid, String businessVoucherTypeUuid,
			String businessVoucherUuid, BigDecimal bookingAmount, JournalVoucherStatus status,
			JournalVoucherCheckingLevel journalVoucherCheckingLevel, JournalVoucherType journalVoucherType){
		this.billingPlanUuid = billingPlanUuid;
		this.businessVoucherTypeUuid = businessVoucherTypeUuid;
		this.businessVoucherUuid = businessVoucherUuid;
		if(journalVoucherType.equals(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE)){
			
		}else{
			
			this.bookingAmount = bookingAmount;
		}
		this.status = status;
		this.checkingLevel = journalVoucherCheckingLevel;
		this.journalVoucherType = journalVoucherType;
		if(journalVoucherType != null ){
			if(journalVoucherType.isJournalVoucherTypeOfVirtualAccount()){
				this.journalVoucherNo = GeneratorUtils.generatePaymentNo();
			}else{
				this.journalVoucherNo = UUID.randomUUID().toString();
			}
		}
		if(status==JournalVoucherStatus.VOUCHER_ISSUED){
			this.issuedTime=new Date();
			this.lastModifiedTime= new Date();
		}
		return this;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getBookingAmount() {
		return bookingAmount;
	}
	public void setBookingAmount(BigDecimal bookingAmount) {
		this.bookingAmount = bookingAmount;
	}
	public AccountSide getAccountSide() {
		return accountSide;
	}
	public void setAccountSide(AccountSide accountSide) {
		this.accountSide = accountSide;
	}
	public String getJournalVoucherUuid() {
		return journalVoucherUuid;
	}
	public void setJournalVoucherUuid(String journalVoucherUuid) {
		this.journalVoucherUuid = journalVoucherUuid;
	}
	public JournalVoucherCompleteness getCompleteness() {
		return completeness;
	}
	public void setCompleteness(JournalVoucherCompleteness completeness) {
		this.completeness = completeness;
	}
	public JournalVoucherStatus getStatus() {
		return status;
	}
	public void setStatus(JournalVoucherStatus status) {
		this.status = status;
	}
	public String getStatusName(){
		return status==null?"":status.getChineseMessage();
	}
	public String getStatusNameOfRefund(){
		return status==null?"":status.getChineseMessage();
	}
	
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public JournalVoucherCheckingLevel getCheckingLevel() {
		return checkingLevel;
	}
	public void setCheckingLevel(JournalVoucherCheckingLevel checkingLevel) {
		this.checkingLevel = checkingLevel;
	}
	public String getCashFlowUuid() {
		return cashFlowUuid;
	}
	public void setCashFlowUuid(String cashFlowUuid) {
		this.cashFlowUuid = cashFlowUuid;
	}
	public Date getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getCashFlowSerialNo() {
		return cashFlowSerialNo;
	}
	public void setCashFlowSerialNo(String cashFlowSerialNo) {
		this.cashFlowSerialNo = cashFlowSerialNo;
	}
	public String getNotificationMemo() {
		return notificationMemo;
	}
	public void setNotificationMemo(String notificationMemo) {
		this.notificationMemo = notificationMemo;
	}
	public String getNotificationIdentity() {
		return notificationIdentity;
	}
	public void setNotificationIdentity(String notificationIdentity) {
		this.notificationIdentity = notificationIdentity;
	}
	public String getBankIdentity() {
		return bankIdentity;
	}
	public void setBankIdentity(String bankIdentity) {
		this.bankIdentity = bankIdentity;
	}
	public BigDecimal getCashFlowAmount() {
		return cashFlowAmount;
	}
	public void setCashFlowAmount(BigDecimal cashFlowAmount) {
		this.cashFlowAmount = cashFlowAmount;
	}
	public String getCashFlowBreif() {
		return cashFlowBreif;
	}
	public void setCashFlowBreif(String cashFlowBreif) {
		this.cashFlowBreif = cashFlowBreif;
	}
	public String getNotificationRecordUuid() {
		return notificationRecordUuid;
	}
	public void setNotificationRecordUuid(String notificationRecordUuid) {
		this.notificationRecordUuid = notificationRecordUuid;
	}
	public Date getNotifiedDate() {
		return notifiedDate;
	}
	public void setNotifiedDate(Date notifiedDate) {
		this.notifiedDate = notifiedDate;
	}
	public String getSourceDocumentIdentity() {
		return sourceDocumentIdentity;
	}
	public void setSourceDocumentIdentity(String notifiedIdentity) {
		this.sourceDocumentIdentity = notifiedIdentity;
	}
	public String getSourceDocumentCashFlowSerialNo() {
		return sourceDocumentCashFlowSerialNo;
	}
	public void setSourceDocumentCashFlowSerialNo(String notifiedCashFlowSerialNo) {
		this.sourceDocumentCashFlowSerialNo = notifiedCashFlowSerialNo;
	}
	public BigDecimal getSourceDocumentAmount() {
		return sourceDocumentAmount;
	}
	public void setSourceDocumentAmount(BigDecimal notifiedAmount) {
		this.sourceDocumentAmount = notifiedAmount;
	}
	public Long getCompanyId() {
		return this.companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getSourceDocumentBreif() {
		return sourceDocumentBreif;
	}
	public void setSourceDocumentBreif(String notificationBreif) {
		this.sourceDocumentBreif = notificationBreif;
	}
	public String getBatchUuid() {
		return batchUuid;
	}
	public void setBatchUuid(String batchUuid) {
		this.batchUuid = batchUuid;
	}
	public String getBusinessVoucherUuid() {
		return businessVoucherUuid;
	}
	public void setBusinessVoucherUuid(String businessVoucherUuid) {
		this.businessVoucherUuid = businessVoucherUuid;
	}
	public String getBillingPlanUuid() {
		return billingPlanUuid;
	}
	public void setBillingPlanUuid(String billingPlanUuid) {
		this.billingPlanUuid = billingPlanUuid;
	}

	public String getCounterPartyAccount() {
		return counterPartyAccount;
	}

	public void setCounterPartyAccount(String counterPartyAccount) {
		this.counterPartyAccount = counterPartyAccount;
	}

	public String getCounterPartyName() {
		return counterPartyName;
	}

	public void setCounterPartyName(String counterPartyName) {
		this.counterPartyName = counterPartyName;
	}

	public SettlementModes getSettlementModes() {
		return settlementModes;
	}

	public void setSettlementModes(SettlementModes settlementModes) {
		this.settlementModes = settlementModes;
	}

	public String getBusinessVoucherTypeUuid() {
		return businessVoucherTypeUuid;
	}

	public void setBusinessVoucherTypeUuid(String businessVoucherTypeUuid) {
		this.businessVoucherTypeUuid = businessVoucherTypeUuid;
	}

	public String getSourceDocumentUuid() {
		return sourceDocumentUuid;
	}

	public void setSourceDocumentUuid(String sourceDocumentUuid) {
		this.sourceDocumentUuid = sourceDocumentUuid;
	}
	
	public String getSourceDocumentCounterPartyUuid() {
		return sourceDocumentCounterPartyUuid;
	}

	public void setSourceDocumentCounterPartyUuid(
			String sourceDocumentCounterPartyUuid) {
		this.sourceDocumentCounterPartyUuid = sourceDocumentCounterPartyUuid;
	}
	
	public String getSourceDocumentCounterPartyAccount() {
		return sourceDocumentCounterPartyAccount;
	}

	public void setSourceDocumentCounterPartyAccount(
			String sourceDocumentCounterPartyAccount) {
		this.sourceDocumentCounterPartyAccount = sourceDocumentCounterPartyAccount;
	}

	public String getSourceDocumentCounterPartyName() {
		return sourceDocumentCounterPartyName;
	}

	public void setSourceDocumentCounterPartyName(
			String sourceDocumentCounterPartyName) {
		this.sourceDocumentCounterPartyName = sourceDocumentCounterPartyName;
	}

	public CashFlowChannelType getCashFlowChannelType() {
		return cashFlowChannelType;
	}

	public void setCashFlowChannelType(CashFlowChannelType cashFlowChannelType) {
		this.cashFlowChannelType = cashFlowChannelType;
	}
	
	public JournalVoucherType getJournalVoucherType() {
		return journalVoucherType;
	}

	public void setJournalVoucherType(JournalVoucherType journalVoucherType) {
		this.journalVoucherType = journalVoucherType;
	}

	public String getRelatedBillContractInfoLv1() {
		return relatedBillContractInfoLv1;
	}

	public void setRelatedBillContractInfoLv1(String relatedBillContractInfoLv1) {
		this.relatedBillContractInfoLv1 = relatedBillContractInfoLv1;
	}

	public String getRelatedBillContractInfoLv2() {
		return relatedBillContractInfoLv2;
	}

	public void setRelatedBillContractInfoLv2(String relatedBillContractInfoLv2) {
		this.relatedBillContractInfoLv2 = relatedBillContractInfoLv2;
	}

	public String getRelatedBillContractInfoLv3() {
		return relatedBillContractInfoLv3;
	}

	public void setRelatedBillContractInfoLv3(String relatedBillContractInfoLv3) {
		this.relatedBillContractInfoLv3 = relatedBillContractInfoLv3;
	}

	public String getCashFlowAccountInfo() {
		return cashFlowAccountInfo;
	}

	public void setCashFlowAccountInfo(String cashFlowAccountInfo) {
		this.cashFlowAccountInfo = cashFlowAccountInfo;
	}
	
	public CounterAccountType getCounterAccountType() {
		return counterAccountType;
	}

	public void setCounterAccountType(CounterAccountType counterAccountType) {
		this.counterAccountType = counterAccountType;
	}
	
	public String getRelatedBillContractNoLv1() {
		return relatedBillContractNoLv1;
	}
	public void setRelatedBillContractNoLv1(String relatedBillContractNoLv1) {
		this.relatedBillContractNoLv1 = relatedBillContractNoLv1;
	}
	public String getRelatedBillContractNoLv2() {
		return relatedBillContractNoLv2;
	}
	public void setRelatedBillContractNoLv2(String relatedBillContractNoLv2) {
		this.relatedBillContractNoLv2 = relatedBillContractNoLv2;
	}
	public String getRelatedBillContractNoLv3() {
		return relatedBillContractNoLv3;
	}
	public void setRelatedBillContractNoLv3(String relatedBillContractNoLv3) {
		this.relatedBillContractNoLv3 = relatedBillContractNoLv3;
	}
	public String getRelatedBillContractNoLv4() {
		return relatedBillContractNoLv4;
	}
	public void setRelatedBillContractNoLv4(String relatedBillContractNoLv4) {
		this.relatedBillContractNoLv4 = relatedBillContractNoLv4;
	}
	public String getSourceDocumentNo() {
		return sourceDocumentNo;
	}
	public void setSourceDocumentNo(String sourceDocumentNo) {
		this.sourceDocumentNo = sourceDocumentNo;
	}
	public String getAppendix() {
		return appendix;
	}
	public void setAppendix(String appendix) {
		this.appendix = appendix;
	}
	public static JournalVoucherCheckingLevel getDefaultCheckingLevel(){
		
		return JournalVoucherCheckingLevel.DOUBLE_CHECKING;
	}
	
	
	public SecondJournalVoucherType getSecondJournalVoucherType() {
		return secondJournalVoucherType;
	}
	public void setSecondJournalVoucherType(SecondJournalVoucherType secondJournalVoucherType) {
		this.secondJournalVoucherType = secondJournalVoucherType;
	}
	public ThirdJournalVoucherType getThirdJournalVoucherType() {
		return thirdJournalVoucherType;
	}
	public void setThirdJournalVoucherType(ThirdJournalVoucherType thirdJournalVoucherType) {
		this.thirdJournalVoucherType = thirdJournalVoucherType;
	}
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}
	
	public String getJournalVoucherNo() {
		return journalVoucherNo;
	}
	public void setJournalVoucherNo(String journalVoucherNo) {
		this.journalVoucherNo = journalVoucherNo;
	}
	
	public String getRemarkInAppendix(){
		Map<String,Object> appendixMap = parseAppendix();
		return (String)appendixMap.get(APPENDIX_REMARK);
	}
	
	public String getClearingTimeInAppendix(){
		Map<String,Object> appendixMap = parseAppendix();
		return (String)appendixMap.get(APPENDIX_CLEARING_TIME);
	}
	
	public void setRemarkInAppendix(String remark) {
		Map<String,Object> appendixMap = parseAppendix();
		
		appendixMap.put(APPENDIX_REMARK, remark);
		this.appendix = JsonUtils.toJsonString(appendixMap);
	}
	
	public void setClearingTimeInAppendix(String remark) {
		Map<String,Object> appendixMap = parseAppendix();
		
		appendixMap.put(APPENDIX_CLEARING_TIME, remark);
		this.appendix = JsonUtils.toJsonString(appendixMap);
	}
	
	private Map<String,Object> parseAppendix(){
		Map<String,Object> appendixMap = new HashMap<String,Object>();
		try {
			appendixMap = JSON.parseObject(appendix,new TypeReference<Map<String,Object>>() {});
		} catch(Exception e){
			
		}
		if(appendixMap==null){
			appendixMap = new HashMap<String,Object>();
		}
		return appendixMap;
	}
	
	public void fillBillContractInfo(String relatedBillContractInfoLv1,String relatedBillContractInfoLv2,String relatedBillContractInfoLv3, String relatedBillContractNoLv1, String relatedBillContractNoLv2, String relatedBillContractNoLv3, String relatedBillContractNoLv4){
		this.relatedBillContractInfoLv1 = relatedBillContractInfoLv1;
		this.relatedBillContractInfoLv2 = relatedBillContractInfoLv2;
		this.relatedBillContractInfoLv3 = relatedBillContractInfoLv3;
		this.relatedBillContractNoLv1 = relatedBillContractNoLv1;
		this.relatedBillContractNoLv2 = relatedBillContractNoLv2;
		this.relatedBillContractNoLv3 = relatedBillContractNoLv3;
		this.relatedBillContractNoLv4 = relatedBillContractNoLv4;
	}
	
	
	public void fillBillContractInfo(ContractCategory contractCategory){
		this.relatedBillContractInfoLv1 = contractCategory.getRelatedBillContractInfoLv1();
		this.relatedBillContractInfoLv2 = contractCategory.getRelatedBillContractInfoLv2();
		this.relatedBillContractInfoLv3 = contractCategory.getRelatedBillContractInfoLv3();
		this.relatedBillContractNoLv1 = contractCategory.getRelatedBillContractNoLv1();
		this.relatedBillContractNoLv2 = contractCategory.getRelatedBillContractNoLv2();
		this.relatedBillContractNoLv3 = contractCategory.getRelatedBillContractNoLv3();
		this.relatedBillContractNoLv4 = contractCategory.getRelatedBillContractNoLv4();
	}
	public void fillCashFlowAccountInfo(String cashFlowAccountInfo){
		this.cashFlowAccountInfo = cashFlowAccountInfo;
	}
	
	public void fillCounterInfo(CounterAccountType counterAccountType, String counterNo,String counterName){
		this.counterAccountType = counterAccountType;
		this.counterPartyAccount = counterNo;
		this.counterPartyName = counterName;
	}
	
	public void fillVoucherSource(SourceDocument sourceDocument, RepaymentType repaymentType) {
		setJournalVoucherType(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER);
		setSecondJournalVoucherType(ThirdPartVoucherSourceMapSpec.SecondJournalVoucherTypeMap
				.get(sourceDocument.getSecondOutlierDocType()));
		setThirdJournalVoucherType(ThirdPartVoucherSourceMapSpec.repaymentTypeToThirdJournalVoucherTypeMap
						.get(repaymentType));
	}
	
	public boolean isCanRefund(){
		return (this.journalVoucherType==JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT ||this.journalVoucherType==JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE) 
				&& this.status == JournalVoucherStatus.VOUCHER_ISSUED;
	}
	
	public String getUuid(){
		return this.journalVoucherUuid;
	}
	public String getLocalPartyAccount() {
		return localPartyAccount;
	}
	public void setLocalPartyAccount(String localPartyAccount) {
		this.localPartyAccount = localPartyAccount;
	}
	public String getLocalPartyName() {
		return localPartyName;
	}
	public void setLocalPartyName(String localPartyName) {
		this.localPartyName = localPartyName;
	}
	public String getSourceDocumentLocalPartyAccount() {
		return sourceDocumentLocalPartyAccount;
	}
	public void setSourceDocumentLocalPartyAccount(
			String sourceDocumentLocalPartyAccount) {
		this.sourceDocumentLocalPartyAccount = sourceDocumentLocalPartyAccount;
	}
	public String getSourceDocumentLocalPartyName() {
		return sourceDocumentLocalPartyName;
	}
	public void setSourceDocumentLocalPartyName(String sourceDocumentLoalPartyName) {
		this.sourceDocumentLocalPartyName = sourceDocumentLoalPartyName;
	}
	public JournalVoucher generateRefundJvFromPaymentJournalVoucher(){
		
		JournalVoucher journalVoucher = new JournalVoucher();
		BeanUtils.copyProperties(this, journalVoucher, "id","journalVoucherUuid","journalVoucherNo","createdDate","lastModifiedTime","issuedTime","counterAccountType",
				"journalVoucherType","batchUuid","appendix","accountSide","billingPlanUuid",
				"localPartyAccount","localPartyName","sourceDocumentLocalPartyAccount","sourceDocumentLocalPartyName");
		journalVoucher.setJournalVoucherType(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_ROLL_BACK);
		journalVoucher.setJournalVoucherNo(GeneratorUtils.generateRefundNo());
		journalVoucher.fillCounterInfo(CounterAccountType.Bill,getJournalVoucherUuid() ,getJournalVoucherNo());
		journalVoucher.setLocalPartyAccount(getCounterPartyAccount());
		journalVoucher.setLocalPartyName(getCounterPartyName());
		journalVoucher.setSourceDocumentCounterPartyAccount(getSourceDocumentLocalPartyAccount());
		journalVoucher.setSourceDocumentCounterPartyName(getSourceDocumentLocalPartyName());
		journalVoucher.setSourceDocumentLocalPartyAccount(getSourceDocumentCounterPartyAccount());
		journalVoucher.setSourceDocumentLocalPartyName(getSourceDocumentCounterPartyName());
		journalVoucher.setStatus(JournalVoucherStatus.VOUCHER_ISSUED);
		journalVoucher.setAccountSide(AccountSide.CREDIT);
		journalVoucher.setTradeTime(new Date());
		journalVoucher.setIssuedTime(new Date());
		journalVoucher.setLastModifiedTime(new Date());
		
		journalVoucher.setJournalVoucherUuid(UUID.randomUUID().toString().replace("-", "")); 
		journalVoucher.setBatchUuid(UUID.randomUUID().toString().replace("-", ""));
		return journalVoucher;
	}
	public int getIsHasDataSyncLog() {
		return isHasDataSyncLog;
	}
	public void setIsHasDataSyncLog(int isHasDataSyncLog) {
		this.isHasDataSyncLog = isHasDataSyncLog;
	}
	public String getUsedSourceDocumentUuid(){
		Boolean isSourceDocumentIndentity = JournalVoucherMapSpec.sourceDocumentUuidIsSourceDocumentIndentityMap.get(getJournalVoucherType());
		if(isSourceDocumentIndentity == null){
			return null;
		}
		return isSourceDocumentIndentity?getSourceDocumentIdentity():getSourceDocumentUuid();
	}
	public String getUsedSourceDocumentDetailUuid(){
		Boolean isSourceDocumentIndentity = JournalVoucherMapSpec.sourceDocumentUuidIsSourceDocumentIndentityMap.get(getJournalVoucherType());
		if(isSourceDocumentIndentity == null){
			return null;
		}
		return isSourceDocumentIndentity?getSourceDocumentUuid():"";
	}

	public void fillFlowInformation(BigDecimal cashFlowAmount, String sourceDocumentCashFlowSerialNo, CashFlowChannelType cashFlowChannelType, String cashFlowAccountInfo, String bankIdentity){
		setCashFlowAmount(cashFlowAmount);
		setSourceDocumentCashFlowSerialNo(sourceDocumentCashFlowSerialNo);
		setCashFlowChannelType(cashFlowChannelType);
		fillCashFlowAccountInfo(cashFlowAccountInfo);
		setBankIdentity(bankIdentity);
	}
	
}