package com.suidifu.microservice.entity;


import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.microservice.model.ThirdPartVoucherSourceMapSpec;
import com.suidifu.owlman.microservice.enumation.SettlementModes;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import com.suidifu.owlman.microservice.enumation.SourceDocumentType;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.finance.BatchPayRecord;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.TransferApplication;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.entity.OfflineBill;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

public class SourceDocumentImporter {

	public static SourceDocument createSourceDocumentFrom(Long companyId, BatchPayRecord batchPayRecord, TransferApplication transferApplication,Account receiveAccount) {
		return initOnlineIntraSystemDeductSSourceDocument(companyId,batchPayRecord,transferApplication,receiveAccount);
	}

	public static SourceDocument createSourceDocumentFrom(Long companyId, OfflineBill offlineBill){
		return initSourceDocumentFromOfflineBill(companyId, offlineBill);
	}

	public static SourceDocument createDepositReceipt(Long companyId, CashFlow cashFlow, Customer customer,String virtualAccountUuid, String relatedContractUuid,String financialContractUuid, String virtualAccountNo, String remark, BigDecimal bookingAmount){
		SourceDocument sourceDocument = new SourceDocument();
		initDepositReceipt(sourceDocument,cashFlow,companyId, bookingAmount);
		String depositNo = GeneratorUtils.generateDepositNo();
		sourceDocument.initTypePart(ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS, ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT, StringUtils.EMPTY, 
			ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_CODE, ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT_CODE, StringUtils.EMPTY, 
			customer.getCustomerUuid(), String.valueOf(companyId), 
			virtualAccountUuid,relatedContractUuid,depositNo,financialContractUuid,
			customer.getCustomerType()==null?-1:customer.getCustomerType().ordinal(),customer.getName(),virtualAccountNo,
					SourceDocumentExcuteStatus.PREPARE, SourceDocumentExcuteResult.UNSUCCESS, RepaymentAuditStatus.CREATE,remark);
		sourceDocument.setSourceDocumentStatus(SourceDocumentStatus.SIGNED);
		return sourceDocument;
	}
	
	
	public static SourceDocument createClearingSourceDocument(Long companyId, CashFlow cashFlow, String firstPartyId,String secondPartyId, String relatedContractUuid,String financialContractUuid, String remark, BigDecimal bookingAmount){
		SourceDocument sourceDocument = new SourceDocument();
		initDepositReceipt(sourceDocument,cashFlow,companyId, bookingAmount);
		String depositNo = GeneratorUtils.generateDepositNo();
		sourceDocument.initTypePart(SourceDocument.FIRSTOUTLIER_CLEARING, StringUtils.EMPTY, StringUtils.EMPTY, 
				StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, 
				firstPartyId, secondPartyId, 
				StringUtils.EMPTY,relatedContractUuid,depositNo,financialContractUuid,
			CustomerType.COMPANY.ordinal(),StringUtils.EMPTY,StringUtils.EMPTY,
					SourceDocumentExcuteStatus.PREPARE, SourceDocumentExcuteResult.UNSUCCESS, RepaymentAuditStatus.CREATE,remark);
		sourceDocument.setSourceDocumentStatus(SourceDocumentStatus.SIGNED);
		return sourceDocument;
	}

	public static SourceDocument createThirdPartyDeductionVoucher(Company company,DeductApplication deductApplication, DeductPlan deductPlan,Customer customer,String contractUuid, String capitalDocumentSource, FinancialContract financialContract, String deductCashIdentity, Date paymentSusTime){
		SourceDocument sourceDocument = new SourceDocument();
		createDeductSourceDocument(sourceDocument,company==null?null:company.getId(), deductApplication, deductPlan,capitalDocumentSource, financialContract, deductCashIdentity, paymentSusTime);
				
		sourceDocument.fillBillInfo(deductApplication.getFinancialContractUuid(), contractUuid);
		sourceDocument.fillPartyInfo(company, customer);
		return sourceDocument;
	}

	private static void createDeductSourceDocument(SourceDocument srcDoc,Long commpanyId,
			DeductApplication deductionApplication,
			DeductPlan deductPlan, String capitalDocumentSource,FinancialContract financialContract, String deductCashIdentity, Date paymentSusTime) {
		srcDoc.setSourceDocumentUuid(UUID.randomUUID().toString());
		srcDoc.setSourceDocumentType(SourceDocumentType.NOTIFY);
		srcDoc.setCreateTime(new Date());
		srcDoc.setLastModifiedTime(new Date());
		srcDoc.setSourceDocumentStatus(SourceDocumentStatus.CREATE);
		srcDoc.setSourceAccountSide(AccountSide.DEBIT);
		srcDoc.setBookingAmount(deductionApplication.getActualDeductTotalAmount());
		srcDoc.setPlanBookingAmount(deductionApplication.getPlannedDeductTotalAmount());
		srcDoc.setAuditStatus(RepaymentAuditStatus.CREATE);
		srcDoc.setOutlierDocumentUuid(deductionApplication.getDeductApplicationUuid());
		srcDoc.setOutlierTradeTime(paymentSusTime);
		if(deductPlan!=null){
			srcDoc.setOutlierCounterPartyAccount(deductPlan.getCpBankCardNo());
			srcDoc.setOutlierCounterPartyName(deductPlan.getCpBankAccountHolder());
		}
		srcDoc.setOutlierAccount(financialContract.getCapitalAccount().getAccountNo());
		srcDoc.setOutlieAccountName(financialContract.getCapitalAccount().getAccountName());
		srcDoc.setOutlierAccountId(financialContract.getCapitalAccount().getId());
		srcDoc.setFinancialContractUuid(financialContract.getFinancialContractUuid());
		
		srcDoc.setOutlierCompanyId(commpanyId);
		//TODO 外部流水或者请求号标示
		srcDoc.setOutlierSerialGlobalIdentity(deductionApplication.getRequestNo());
		//现金流标志
		srcDoc.setOutlierMemo(deductCashIdentity);
		srcDoc.setOutlierAmount(deductionApplication.getPlannedDeductTotalAmount());
		srcDoc.setOutlierSettlementModes(SettlementModes.REMITTANCE);
		srcDoc.setOutlierBreif(deductionApplication.getRepaymentPlanCodeList());
		srcDoc.setOutlierAccountSide(AccountSide.fromAccountSide(deductionApplication.getTranscationType()));
	
		srcDoc.setCompanyId(commpanyId);
		srcDoc.setSourceDocumentNo(GeneratorUtils.generateDudectPlanVoucherNo());
		//外部单据单号及类型
		srcDoc.setFirstOutlierDocType(SourceDocument.FIRSTOUTLIER_DEDUCTAPPLICATION);
		srcDoc.setFirstAccountId(deductionApplication.getDeductApplicationUuid());
		srcDoc.setSecondOutlierDocType(capitalDocumentSource);
		srcDoc.setSecondAccountId(deductPlan.getDeductPlanUuid());

	}

	public static SourceDocument initSourceDocumentFromOfflineBill(Long companyId, OfflineBill offlineBill)
	{
		return new SourceDocument(companyId,SourceDocumentType.NOTIFY, new Date(),
				null, SourceDocumentStatus.CREATE,
				AccountSide.DEBIT, BigDecimal.ZERO,
				offlineBill.getOfflineBillUuid(), offlineBill.getTradeTime(),
				offlineBill.getPayerAccountNo(), offlineBill.getPayerAccountName(),
				StringUtils.EMPTY, StringUtils.EMPTY,
				null, companyId,
				offlineBill.getSerialNo(), offlineBill.getAmount(), SettlementModes.REMITTANCE,
				AccountSide.DEBIT,SourceDocument.FIRSTOUTLIER_OFFLINEBILL,StringUtils.EMPTY,StringUtils.EMPTY,RepaymentAuditStatus.CREATE,
				StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,
				StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,
				null,null,StringUtils.EMPTY);
	}

	public static void initDepositReceipt(SourceDocument srcDoc,CashFlow cashFlow, Long outlierCompanyId,
			BigDecimal bookingAmount) {
		srcDoc.setSourceDocumentUuid(UUID.randomUUID().toString());
		srcDoc.setSourceDocumentType(SourceDocumentType.NOTIFY);
		srcDoc.setCreateTime(new Date());
		srcDoc.setLastModifiedTime(new Date());
		srcDoc.setSourceDocumentStatus(SourceDocumentStatus.CREATE);
		srcDoc.setSourceAccountSide(AccountSide.fromLedgerBookAccountSide(cashFlow.getAccountSide()));
		srcDoc.setBookingAmount(bookingAmount);
		srcDoc.setAuditStatus(RepaymentAuditStatus.CREATE);
		srcDoc.setOutlierDocumentUuid(cashFlow.getCashFlowUuid());
		srcDoc.setOutlierTradeTime(cashFlow.getTransactionTime());
		srcDoc.setOutlierCounterPartyAccount(cashFlow.getCounterAccountNo());
		srcDoc.setOutlierCounterPartyName(cashFlow.getCounterAccountName());
		srcDoc.setOutlierAccount(cashFlow.getHostAccountNo());
		srcDoc.setOutlieAccountName(cashFlow.getHostAccountName());
		srcDoc.setOutlierCompanyId(outlierCompanyId);
		srcDoc.setOutlierSerialGlobalIdentity(cashFlow.getBankSequenceNo());
		//现金流标志
		srcDoc.setOutlierMemo(cashFlow.getCashFlowIdentity());
		srcDoc.setOutlierAmount(cashFlow.getTransactionAmount());
		srcDoc.setPlanBookingAmount(cashFlow.getTransactionAmount());
		srcDoc.setOutlierSettlementModes(SettlementModes.REMITTANCE);
		srcDoc.setOutlierBreif(cashFlow.getRemark());
		srcDoc.setOutlierAccountSide(AccountSide.fromLedgerBookAccountSide(cashFlow.getAccountSide()));
		
		srcDoc.setCompanyId(outlierCompanyId);
	}
	
	public static SourceDocument initOnlineIntraSystemDeductSSourceDocument(Long companyId, BatchPayRecord batchPayRecord, TransferApplication transferApplication,Account receiveAccount){
		return new SourceDocument(companyId,SourceDocumentType.NOTIFY, new Date(),
				null, SourceDocumentStatus.CREATE,
				AccountSide.DEBIT, BigDecimal.ZERO,
				batchPayRecord.getBatchPayRecordUuid(), batchPayRecord.getTransDateTimeDateValue(),
				transferApplication.getContractAccount().getPayAcNo(), transferApplication.getContractAccount().getPayerName(),
				receiveAccount.getAccountNo(), receiveAccount.getAccountName(),
				receiveAccount.getId(), companyId,
				batchPayRecord.getSerialNo(), transferApplication.getAmount(), SettlementModes.REMITTANCE,
				AccountSide.DEBIT,SourceDocument.FIRSTOUTLIER_BATCHPAY_RECORD,StringUtils.EMPTY,StringUtils.EMPTY,RepaymentAuditStatus.CREATE,
				StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,
				StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,
				null,null,StringUtils.EMPTY);
	}

	public static SourceDocument createActivePaymentVoucherSourceDocument(FinancialContract financialContract, Contract contract, CashFlow cashFlow, VirtualAccount virtualAccount, BigDecimal planBookingAmount, String voucherUuid){
		Long companyId = financialContract.getCompany().getId();
		String virtualAccountUuid = virtualAccount.getVirtualAccountUuid();
		String virtualAccountNo = virtualAccount.getVirtualAccountNo();
		String contractUuid = contract.getUuid();
		Customer customer = contract.getCustomer();
		String financialContractUuid = financialContract.getUuid();

		SourceDocument sourceDocument = new SourceDocument();
		initDepositReceipt(sourceDocument, cashFlow, companyId, BigDecimal.ZERO);
		String sourceDocumentNo = GeneratorUtils.generateDepositNo();
		sourceDocument.initTypePart(ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS,
				ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT, StringUtils.EMPTY,
				ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_CODE,
				ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT_CODE, StringUtils.EMPTY,
				customer.getCustomerUuid(), String.valueOf(companyId), virtualAccountUuid, contractUuid,
				sourceDocumentNo, financialContractUuid, customer.getCustomerType().ordinal(), customer.getName(),
				virtualAccountNo, SourceDocumentExcuteStatus.PREPARE, SourceDocumentExcuteResult.UNSUCCESS,
				RepaymentAuditStatus.CREATE, "");
		sourceDocument.setPlanBookingAmount(planBookingAmount);
		sourceDocument.setVoucherUuid(voucherUuid);
		return sourceDocument;
	}
	
	public static SourceDocument createRemittanceSourceDocument(String redisKey, CashFlow cashFlow , Integer paymentInstitutionName, String contractUuid,
																FinancialContract financialContract, String appendix, RemittancePlanExecLog remittancePlanExecLog){
		
		Account account = financialContract.getCapitalAccount();
		Company company=financialContract.getCompany();
		
		Long companyId=company.getId();
		Date createTime=new Date();
		Date issuedTime=new Date();
		AccountSide sourceAccountSide=EnumUtil.fromOrdinal(AccountSide.class, cashFlow.getAccountSide().ordinal());
		BigDecimal bookingAmount=cashFlow.getTransactionAmount();
		String outlierDocumentUuid=cashFlow.getUuid();
		Date outlierTradeTime=cashFlow.getTransactionTime();
		String outlierCounterPartyAccount=cashFlow.getCounterAccountNo();
		String outlierCounterPartyName=cashFlow.getCounterAccountName();
		String outlierAccount=cashFlow.getHostAccountNo();
		String outlieAccountName=cashFlow.getHostAccountName();
		Long outlierAccountId=account.getId();
		Long outlierCompanyId=company.getId();
		String outlierSerialGlobalIdentity=redisKey;
		//现金流标志
		String outlierMemo=cashFlow.getCashFlowIdentity();
		BigDecimal outlierAmount=cashFlow.getTransactionAmount();
		String outlierBreif=cashFlow.getRemark();
		AccountSide outlierAccountSide=EnumUtil.fromOrdinal(AccountSide.class, cashFlow.getAccountSide().ordinal());
		String secondAccountId=paymentInstitutionName!=null?paymentInstitutionName.toString():null;
		String firstPartyId=company.getUuid();

		String secondOutlierDocType = remittancePlanExecLog.getVoucherTypeKey();

		SourceDocument sourceDocument = new SourceDocument( companyId, SourceDocumentType.NOTIFY,  createTime,
				 issuedTime,  SourceDocumentStatus.SIGNED,
				 sourceAccountSide,  bookingAmount,
				 outlierDocumentUuid,  outlierTradeTime,
				 outlierCounterPartyAccount,  outlierCounterPartyName,
				 outlierAccount,  outlieAccountName,
				 outlierAccountId,  outlierCompanyId,
				 outlierSerialGlobalIdentity,  outlierMemo,
				 outlierAmount,  SettlementModes.REMITTANCE,
				 outlierBreif,  outlierAccountSide,  appendix,
				 SourceDocument.FIRSTOUTLIER_REMITTANCE, secondOutlierDocType,  "", RepaymentAuditStatus.CREATE,
				 firstPartyId, "", "",
				 "", secondAccountId, "",
				 null, null, contractUuid);
		sourceDocument.setPlanBookingAmount(bookingAmount);
		sourceDocument.setFinancialContractUuid(financialContract.getUuid());
		sourceDocument.setSourceDocumentNo(GeneratorUtils.generateRemittanceVoucherNo());
		Integer firstPartyType=CustomerType.COMPANY.ordinal();
		String firstPartyName=company.getFullName();
		sourceDocument.setFirstPartyType(firstPartyType);
		sourceDocument.setFirstPartyName(firstPartyName);
		return sourceDocument;
	}

	public static SourceDocument createRepaymentOrderDeductionVoucher(Company company, PaymentOrder paymentOrder, FastCustomer fastCustomer, String contractUuid, FinancialContract financialContract, String deductCashIdentity, Date paymentSucTime){
		SourceDocument sourceDocument = new SourceDocument();
		createRepaymentDeductSourceDocument(sourceDocument,company==null?null:company.getId(), paymentOrder, financialContract, deductCashIdentity, paymentSucTime);

		sourceDocument.fillBillInfo(financialContract.getFinancialContractUuid(), contractUuid);
		sourceDocument.fillPartyInfo(company, fastCustomer);
		return sourceDocument;
	}

	private static void createRepaymentDeductSourceDocument(SourceDocument srcDoc, Long companyId,
															PaymentOrder paymentOrder, FinancialContract financialContract, String deductCashIdentity, Date paymentSucTime) {
		srcDoc.setSourceDocumentUuid(UUID.randomUUID().toString());
		srcDoc.setSourceDocumentType(SourceDocumentType.NOTIFY);
		srcDoc.setCreateTime(new Date());
		srcDoc.setLastModifiedTime(new Date());
		srcDoc.setSourceDocumentStatus(SourceDocumentStatus.CREATE);
		srcDoc.setSourceAccountSide(AccountSide.DEBIT);
		srcDoc.setBookingAmount(paymentOrder.getAmount());
		srcDoc.setPlanBookingAmount(paymentOrder.getAmount());
		srcDoc.setAuditStatus(RepaymentAuditStatus.CREATE);
		srcDoc.setOutlierDocumentUuid(paymentOrder.getOutlierDocumentUuid());
		srcDoc.setOutlierTradeTime(paymentSucTime);
		srcDoc.setOutlierCounterPartyAccount(paymentOrder.getCounterAccountNo());
		srcDoc.setOutlierCounterPartyName(paymentOrder.getCounterAccountName());
		srcDoc.setOutlierAccount(financialContract.getCapitalAccount().getAccountNo());
		srcDoc.setOutlieAccountName(financialContract.getCapitalAccount().getAccountName());
		srcDoc.setOutlierAccountId(financialContract.getCapitalAccount().getId());
		srcDoc.setFinancialContractUuid(financialContract.getFinancialContractUuid());

		srcDoc.setOutlierCompanyId(companyId);
		//TODO 外部流水或者请求号标示
		srcDoc.setOutlierSerialGlobalIdentity(paymentOrder.getOutlierDocumentIdentity());
		srcDoc.setOutlierMemo(deductCashIdentity);
		srcDoc.setOutlierAmount(paymentOrder.getAmount());
		srcDoc.setOutlierSettlementModes(SettlementModes.REMITTANCE);
		srcDoc.setOutlierBreif("");
		srcDoc.setOutlierAccountSide(paymentOrder.getAccountSide());

		srcDoc.setCompanyId(companyId);
		srcDoc.setSourceDocumentNo(GeneratorUtils.generateDudectPlanVoucherNo());
		//外部单据单号及类型
		srcDoc.setFirstOutlierDocType(SourceDocument.FIRSTOUTLIER_PAYMENT_ORDER);
		srcDoc.setFirstAccountId(paymentOrder.getUuid());
		srcDoc.setSecondOutlierDocType(ThirdPartVoucherSourceMapSpec.payWayMapSpec.get(paymentOrder.getPayWay()));
		srcDoc.setSecondAccountId(paymentOrder.getOrderUuid());
 
	}
}
