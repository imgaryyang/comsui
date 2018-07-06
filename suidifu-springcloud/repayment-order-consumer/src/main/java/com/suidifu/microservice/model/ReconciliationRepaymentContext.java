package com.suidifu.microservice.model;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.model.JournalVoucherResolver;
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
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReconciliationRepaymentContext {
    private String uniqueOfflineCashIdentity; //线下的现金流标志（还款订单匹配单条流水时）
    private TmpDepositDocRecoverParams tmpDepositDocParams;
    private String virtualAccountVersionLock;//虚拟账户的版本锁
    private VirtualAccount remittanceBorrowerVirtualAccount;//借款人虚拟账户
    private DepositeAccountInfo borrowerDepositAccountForLedgerBook;//借款人虚拟账户
    private DepositeAccountInfo financialContractAccountForLedgerBook;//信托合同银行账户
    private ContractAccount contractAccount;//借款人银行账户
    private Account financialContractBankAccount;//信托合同银行账户
    private VirtualAccount remittanceFinancialVirtualAccount;//信托合同虚拟账户
    private Order order;//结算单
    private Contract contract;//合同
    private int currentPeriod;//期数
    private AssetSet assetSet;//待核销资产
    private SourceDocument sourceDocument;//凭证
    private SourceDocumentDetail sourceDocumentDetail;//凭证明细
    private AssetRecoverType recoverType;//核销资产类型
    private String ledgerBookNo;//账本编号
    private FinancialContract financialContract;//信托合同
    private JournalVoucherType journalVoucherType;//销账凭证类型
    private JournalVoucher issuedJournalVoucher;//销账凭证
    private JournalVoucherResolver journalVoucherResolver;//销账凭证参数解决器
    private Date actualRecycleTime;//实际回收时间
    private Customer borrowerCustomer;//借款人客户
    private Customer companyCustomer;//信托合同客户
    private BigDecimal bookingAmount;//待销账金额
    private BigDecimal receivableAmount;//资产应收额
    private BigDecimal guaranteeAmount;//担保应收额
    private BigDecimal unearnedAmount;//未到期额
    private BigDecimal repurchaseAmount;//回购额
    private BigDecimal borrowerCustomerBalance;//借款人账户余额
    private BigDecimal financialAppBalance;//信托虚拟账户余额
    private Company company;//信托客户公司
    private RepurchaseDoc repurchaseDoc;//回购单
    private DeductApplication deductApplication;//第三方扣款请求
    private ReconciliationType reconciliationType;//销账类型
    private boolean alreadyReconciliation = false;//是否已经核销
    private Map<String, BigDecimal> bookingDetailAmount = new HashMap<>();
    private String sourceDocumentNo;
    private CashFlow cashFlow;
    private AppAccount appAccount;
    private AssetCategory assetCategory;
    private Customer cashFlowOwner;
    private Map<String, Object> appendix = new HashMap<>();
    private RepaymentOrderItem repaymentOrderItem;
    private RepaymentOrder repaymentOrder;
    private PaymentOrder paymentOrder;
    private boolean receivableJournalVoucher;

    public String getTmpDepositDocUuidFromTmpDepositRecover() {
        if (tmpDepositDocParams != null) {
            return tmpDepositDocParams.getTmpDepositDocUuidFromTmpDepositRecover();
        }
        return "";
    }

    public String getSecondNoFromTmpDepositRecover() {
        if (tmpDepositDocParams != null) {
            return tmpDepositDocParams.getSecondNoFromTmpDepositRecover();
        }
        return "";
    }

    public Long getCompanyId() {
        return company == null ? null : company.getId();
    }

    public String getSourceDocumentDetailUuid() {
        if (sourceDocumentDetail == null) return "";
        return sourceDocumentDetail.getUuid();
    }

    public String getSourceDocumentUuid() {
        if (sourceDocument == null) return "";
        return sourceDocument.getUuid();
    }

    public void resolveJournalVoucher(AccountSide side, CounterAccountType fromAccountType, String relatedBillUuid) {
        journalVoucherResolver = new JournalVoucherResolver();
        journalVoucherResolver.setAccountSide(side);
        journalVoucherResolver.setFromAccountType(fromAccountType);
        journalVoucherResolver.setRelatedBillUuid(relatedBillUuid);
        resolveCashFlowParties();
        resolveSourceDocumentParties();
    }

    public DepositeAccountInfo getRemittanceAccountForLedgerBook() {
        DepositeAccountInfo ledgerBookBankAccountForFinancialContract = this.getFinancialContractAccountForLedgerBook();
        DepositeAccountInfo ledgerBookBankAccountForBorrow = this.getBorrowerDepositAccountForLedgerBook();
        if (this.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            return ledgerBookBankAccountForBorrow;
        } else {
            return ledgerBookBankAccountForFinancialContract;
        }
    }

    public VirtualAccount getRemittanceVirtualAccount() {
        if (this.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            return getRemittanceBorrowerVirtualAccount();
        } else {
            return getRemittanceFinancialVirtualAccount();
        }
    }

    private void resolveCashFlowParties() {
        if (journalVoucherResolver.getFromAccountType().equals(CounterAccountType.BankAccount)) {
            if (this.receivableJournalVoucher) {
                journalVoucherResolver.setCounterPartyAccount(this.getPaymentOrder().getCounterAccountNo());
                journalVoucherResolver.setCounterPartyAccountName(this.getPaymentOrder().getCounterAccountName());
            } else {
                journalVoucherResolver.setCounterPartyAccount(this.getSourceDocumentDetail().getPaymentAccountNo());
                journalVoucherResolver.setCounterPartyAccountName(getSourceDocumentDetail().getPaymentName());
            }
        } else if (journalVoucherResolver.getFromAccountType().equals(CounterAccountType.VirtualAccount)) {
            if (getJournalVoucherType().isVirtualAccountRemittanceBySelf()) {
                journalVoucherResolver.setCounterPartyAccount(this.getRemittanceBorrowerVirtualAccount().getVirtualAccountNo());
                journalVoucherResolver.setCounterPartyAccountName(this.getRemittanceBorrowerVirtualAccount().getOwnerName());
            } else {
                journalVoucherResolver.setCounterPartyAccount(this.getRemittanceFinancialVirtualAccount().getVirtualAccountNo());
                journalVoucherResolver.setCounterPartyAccountName(getRemittanceFinancialVirtualAccount().getOwnerName());
            }
        }

        if (getJournalVoucherType().equals(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER)
                || getJournalVoucherType().equals(JournalVoucherType.BANK_CASHFLOW_DEPOSIT)) {
            journalVoucherResolver.setLocalPartyAccount(getFinancialContractBankAccount().getAccountNo());
            journalVoucherResolver.setLocalPartyAccountName(this.getFinancialContractBankAccount().getAccountName());
        } else {
            journalVoucherResolver.setLocalPartyAccount(this.getContractAccount().getPayAcNo());
            journalVoucherResolver.setLocalPartyAccountName(getContractAccount().getPayerName());
        }
    }

    private void resolveSourceDocumentParties() {
        if (getJournalVoucherType().equals(JournalVoucherType.BANK_CASHFLOW_DEPOSIT))
            return;

        if (getSourceDocument() != null) {
            journalVoucherResolver.setSourceDocumentLocalPartyAccount(getSourceDocument().getOutlierAccount());
            journalVoucherResolver.setSourceDocumentLocalPartyName(getSourceDocument().getOutlieAccountName());
        }
        if (getSourceDocumentDetail() != null) {
            journalVoucherResolver.setSourceDocumentCounterPartyAccount(getSourceDocumentDetail().getPaymentAccountNo());
            journalVoucherResolver.setSourceDocumentCounterPartyName(getSourceDocumentDetail().getPaymentName());
        }
    }

    public void putKVToAppendix(String key, Object value) {
        this.appendix.put(key, value);
    }

    public Object getValueFromAppendix(String key) {
        return this.appendix.get(key);
    }

    public String getCashFlowIdentity() {
        return getIssuedJournalVoucher() == null ? "" : getIssuedJournalVoucher().getNotificationMemo();
    }

    public boolean isRepaymentPlanTimeInvalid() {
        return !isRepaymentPlanTimeValid(financialContract);
    }

    public boolean isRepaymentPlanTimeValid(FinancialContract financialContract) {
        return financialContract.isRepaymentDayCheck() && repaymentOrderItem.getRepaymentPlanTime() != null;
    }
}