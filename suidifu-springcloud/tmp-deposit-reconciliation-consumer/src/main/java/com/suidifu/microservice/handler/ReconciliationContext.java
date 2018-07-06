package com.suidifu.microservice.handler;

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
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ReconciliationContext {

  private TmpDepositDocRecoverParams tmpDepositDocParams;
  /**
   * 虚拟账户的版本锁
   */
  private String virutalAccountVersionLock;
  /**
   * 借款人虚拟账户
   */
  private VirtualAccount remittanceBorrowerVirtuaAccount;
  /**
   * 借款人虚拟账户
   */
  private DepositeAccountInfo borrowerDepositeAccountForLedegerBook;
  /**
   * 信托合同银行账户
   */
  private DepositeAccountInfo financialContractAccountForLedgerBook;
  /**
   * 借款人银行账户
   */
  private ContractAccount contractAccount;
  /**
   * 信托合同银行账户
   */
  private Account financialContractBankAccount;
  /**
   * 信托合同虚拟账户
   */
  private VirtualAccount remittanceFinancialVirtualAccount;
  /**
   * 结算单
   */
  private Order order;
  /**
   * 合同
   */
  private Contract contract;
  /**
   * 待核销资产
   */
  private AssetSet assetSet;
  /**
   * 凭证
   */
  private SourceDocument sourceDocument;
  /**
   * 凭证明细
   */
  private SourceDocumentDetail sourceDocumentDetail;
  /**
   * 核销资产类型
   */
  private AssetRecoverType recoverType;
  /**
   * 账本编号
   */
  private String ledgerBookNo;
  /**
   * 信托合同
   */
  private FinancialContract financialContract;
  /**
   * 销账凭证类型
   */
  private JournalVoucherType journalVoucherType;
  /**
   * 销账凭证
   */
  private JournalVoucher issuedJournalVoucher;
  /**
   * 销账凭证参数解决器
   */
  private JournalVoucherResolver resovler;
  /**
   * 实际回收时间
   */
  private Date actualRecycleTime;
  /**
   * 借款人客户
   */
  private Customer borrowerCustomer;
  /**
   * 信托合同客户
   */
  private Customer companyCustomer;
  /**
   * 待销账金额
   */
  private BigDecimal bookingAmount;
  /**
   * 资产应收额
   */
  private BigDecimal receivalbeAmount;
  /**
   * 担保应收额
   */
  private BigDecimal guranteeAmount;
  /**
   * 未到期额
   */
  private BigDecimal unearnedAmount;
  /**
   * 回购额
   */
  private BigDecimal repurchaseAmount;
  /**
   * 借款人账户余额
   */
  private BigDecimal borrowerCustomerBalance;
  /**
   * 信托虚拟账户余额
   */
  private BigDecimal financialAppBalance;
  /**
   * 信托客户公司
   */
  private Company company;
  /**
   * 回购单
   */
  private RepurchaseDoc repurchaseDoc;
  /**
   * 第三方扣款请求
   */
  private DeductApplication deductApplication;
  /**
   * 销账类型
   */
  private ReconciliationType reconciliationType;
  /**
   * 是否已经核销
   */
  private boolean alreadyReconciliation = false;

  private Map<String, BigDecimal> bookingDetailAmount = new HashMap<>();
  private String sourceDocumentNo;
  private CashFlow cashFlow;
  private AppAccount appAccount;
  private AssetCategory assetCategory;
  private Customer cashFlowOwner;
  private Map<String, Object> appendix = new HashMap<>();
  private DeductPlan deductPlan;

  public boolean isFromTmpDepositVirtualAccount() {
    return tmpDepositDocParams != null && tmpDepositDocParams.isFromTmpDepost();
  }

  public String getTmpDepositDocUuidFromTmpDepositRecover() {
    if (tmpDepositDocParams == null) {
      return "";
    }
    return tmpDepositDocParams.getTmpDepositDocUuidFromTmpDepositRecover();
  }

  public String getSecondNoFromTmpDepositRecover() {
    if (tmpDepositDocParams == null) {
      return "";
    }
    return tmpDepositDocParams.getSecondNoFromTmpDepositRecover();
  }


  public Long getCompanyId() {
    return company == null ? null : company.getId();
  }

  public String getSourceDocumentDetailUuid() {
    if (sourceDocumentDetail == null) {
      return "";
    }
    return sourceDocumentDetail.getUuid();
  }

  public String getSourceDocumentUuid() {
    if (sourceDocument == null) {
      return "";
    }
    return sourceDocument.getSourceDocumentUuid();
  }

  public void setSourceDocumentDetail(SourceDocumentDetail sourceDocumentDetail) {
    this.sourceDocumentDetail = sourceDocumentDetail;
  }

  public void resovleJournalVoucher(AccountSide side, CounterAccountType fromAccountType, String relatedBillUuid) {
    resovler = new JournalVoucherResolver();
    resovler.setAccountSide(side);
    resovler.setFromAccountType(fromAccountType);
    resovler.setRelatedBillUuid(relatedBillUuid);
    resovleCashFlowParties();
    resolveSourceDocumentParties();
  }

  public DepositeAccountInfo getRemittanceAccountForLedgerBook() {
    DepositeAccountInfo ledgerBookBankAccountForFinancialContract = this.getFinancialContractAccountForLedgerBook();
    DepositeAccountInfo ledgerBookBankAccountForBorrow = this.getBorrowerDepositeAccountForLedegerBook();
    if (this.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
      return ledgerBookBankAccountForBorrow;
    } else {
      return ledgerBookBankAccountForFinancialContract;
    }
  }

  public VirtualAccount getRemittanceVirtualAccount() {
    if (this.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
      return getRemittanceBorrowerVirtuaAccount();
    } else {
      return getRemittanceFinancialVirtualAccount();
    }
  }

  public String getSourceDocumentNo() {
    return sourceDocumentNo;
  }

  private void resovleCashFlowParties() {
    if (CounterAccountType.BankAccount.equals(resovler.getFromAccountType())) {
      resovler.setCounterPartyAccount(this.getSourceDocumentDetail().getPaymentAccountNo());
      resovler.setCounterPartyAccountName(getSourceDocumentDetail().getPaymentName());
    } else if (CounterAccountType.VirtualAccount.equals(resovler.getFromAccountType())) {
      if (getJournalVoucherType().isVirtualAccountRemittanceBySelf()) {
        resovler.setCounterPartyAccount(this.getRemittanceBorrowerVirtuaAccount().getVirtualAccountNo());
        resovler.setCounterPartyAccountName(this.getRemittanceBorrowerVirtuaAccount().getOwnerName());
      } else {
        resovler.setCounterPartyAccount(this.getRemittanceFinancialVirtualAccount().getVirtualAccountNo());
        resovler.setCounterPartyAccountName(getRemittanceFinancialVirtualAccount().getOwnerName());
      }
    }

    if (JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER.equals(getJournalVoucherType())
        || JournalVoucherType.BANK_CASHFLOW_DEPOSIT.equals(getJournalVoucherType())) {
      resovler.setLocalPartyAccount(getFinancialContractBankAccount().getAccountNo());
      resovler.setLocalPartyAccountName(this.getFinancialContractBankAccount().getAccountName());
    } else {
      resovler.setLocalPartyAccount(this.getContractAccount().getPayAcNo());
      resovler.setLocalPartyAccountName(getContractAccount().getPayerName());
    }
  }

  private void resolveSourceDocumentParties() {
    if (JournalVoucherType.BANK_CASHFLOW_DEPOSIT.equals(getJournalVoucherType())) {
      return;
    }
    if (getSourceDocument() != null) {
      resovler.setSourceDocumentLocalPartyAccount(getSourceDocument().getOutlierAccount());
      resovler.setSourceDocumentLocalPartyName(getSourceDocument().getOutlieAccountName());
    }
    if (getSourceDocumentDetail() != null) {
      resovler.setSourceDocumentCounterPartyAccount(getSourceDocumentDetail().getPaymentAccountNo());
      resovler.setSourceDocumentCounterPartyName(getSourceDocumentDetail().getPaymentName());
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
}