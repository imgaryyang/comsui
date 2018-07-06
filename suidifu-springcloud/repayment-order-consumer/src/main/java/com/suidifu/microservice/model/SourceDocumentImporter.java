package com.suidifu.microservice.model;


import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.owlman.microservice.enumation.SettlementModes;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import com.suidifu.owlman.microservice.enumation.SourceDocumentType;
import com.suidifu.owlman.microservice.spec.ThirdPartVoucherSourceMapSpec;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

public class SourceDocumentImporter {
    public static SourceDocument createRepaymentOrderDeductionVoucher(Company company, PaymentOrder paymentOrder, FastCustomer fastCustomer, String contractUuid, FinancialContract financialContract, String deductCashIdentity, Date paymentSucTime) {
        SourceDocument sourceDocument = new SourceDocument();
        createRepaymentDeductSourceDocument(sourceDocument, company == null ? null : company.getId(), paymentOrder, financialContract, deductCashIdentity, paymentSucTime);

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

    public static SourceDocument createDepositReceipt(Long companyId, CashFlow cashFlow, Customer customer, String virtualAccountUuid, String relatedContractUuid, String financialContractUuid, String virtualAccountNo, String remark, BigDecimal bookingAmount) {
        SourceDocument sourceDocument = new SourceDocument();
        initDepositReceipt(sourceDocument, cashFlow, companyId, bookingAmount);
        String depositNo = GeneratorUtils.generateDepositNo();
        sourceDocument.initTypePart(ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS, ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT, StringUtils.EMPTY,
                ChartOfAccount.FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_CODE, ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT_CODE, StringUtils.EMPTY,
                customer.getCustomerUuid(), String.valueOf(companyId),
                virtualAccountUuid, relatedContractUuid, depositNo, financialContractUuid,
                customer.getCustomerType() == null ? -1 : customer.getCustomerType().ordinal(), customer.getName(), virtualAccountNo,
                SourceDocumentExcuteStatus.PREPARE, SourceDocumentExcuteResult.UNSUCCESS, RepaymentAuditStatus.CREATE, remark);
        sourceDocument.setSourceDocumentStatus(SourceDocumentStatus.SIGNED);
        return sourceDocument;
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
}