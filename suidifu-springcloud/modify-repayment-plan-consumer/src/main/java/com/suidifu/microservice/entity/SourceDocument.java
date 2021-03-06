package com.suidifu.microservice.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.owlman.microservice.enumation.Currency;
import com.suidifu.owlman.microservice.enumation.SettlementModes;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import com.suidifu.owlman.microservice.enumation.SourceDocumentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.AppArriveRecord;
import com.zufangbao.sun.entity.directbank.business.BankSide;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.ledgerbookv2.common.annotation.LedgerKey;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.io.Serializable;
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
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

/**
 * 交易完成的原始凭证
 *
 * @author wukai
 */
@Entity
@Table(name = "source_document")
public class SourceDocument implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -784271588498757345L;
  /* firstOutlierDocType，除了这几种外，其他的为ledger_book的一级科目*/
  public static final String FIRSTOUTLIER_BATCHPAY_RECORD = "batch_pay_record";
  public static final String FIRSTOUTLIER_OFFLINEBILL = "offline_bill";
  public static final String FIRSTOUTLIER_DEDUCTAPPLICATION = "deduct_application";
  public static final String FIRSTOUTLIER_CLEARING = "clearing_cash_flow";
  public static final String FIRSTOUTLIER_REMITTANCE = "remittance_cash_flow";
  public static final String FIRSTOUTLIER_PAYMENT_ORDER = "payment_order";

  /* secondOutlierDocType, 扣款单来源：1系统发起线上支付单 2 接口发起扣款单*/
  public static final String SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT = "system_on_line_payment";
  public static final String SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET = "interface_on_line_payment";
  public static final String SECONDOUTLIER_APP_UPLOAD_UPLOAD = "app_upload_payment";


  /* appendix*/
  private static final String APPENDIX_REMARK = "remark";
  @Id
  @GeneratedValue
  private Long id;

  private Long companyId;

  /**
   * 原始凭证号
   */
  private String sourceDocumentUuid;

  @Enumerated(EnumType.ORDINAL)
  private SourceDocumentType sourceDocumentType;

  private Date createTime;

  private Date issuedTime;

  @Enumerated(EnumType.ORDINAL)
  private SourceDocumentStatus sourceDocumentStatus;

  @Enumerated(EnumType.ORDINAL)
  private AccountSide sourceAccountSide;
  /**
   * 已制证金额
   */
  @LedgerKey(name = "bookingAmount", valueRef = "bookingAmount")
  private BigDecimal bookingAmount;

  @Enumerated(EnumType.ORDINAL)
  private RepaymentAuditStatus auditStatus;
  /**
   * 外部凭据号(batch_pay_recordUuid, offline_billUuid,deduct_applicationUuid, cashFLowUuid)
   */
  private String outlierDocumentUuid;
  /**
   * 外部凭据交易时间
   */
  private Date outlierTradeTime;


  /**
   * ====外部凭据发生账号 fromAccount -> toAccount=====
   */
  private String outlierCounterPartyAccount;

  private String outlierCounterPartyName;

  /**
   * toAccount
   */
  private String outlierAccount;

  private String outlieAccountName;

  private Long outlierAccountId;


  /**
   * 外部凭据所属公司
   */
  private Long outlierCompanyId;
  /**
   * 外部凭据全局标识(例如：请求编号)
   */
  private String outlierSerialGlobalIdentity;
  //外部传送附带信息：cashFlow的现金流标志
  private String outlierMemo;
  /**
   * 外部凭据金额
   */
  private BigDecimal outlierAmount;

  @Enumerated(EnumType.ORDINAL)
  private Currency currencyType;
  /**
   * 外部凭据类型
   */
  @Enumerated(EnumType.ORDINAL)
  private SettlementModes outlierSettlementModes;

  @Column(columnDefinition = "text")
  private String outlierBreif;

  @Enumerated(EnumType.ORDINAL)
  private AccountSide outlierAccountSide;

  @Column(columnDefinition = "text")
  private String appendix;

  /* start */
  private String firstPartyId;
  private String secondPartyId;

  private String virtualAccountUuid;


  /**
   * 一级外部单据类型
   */
  private String firstOutlierDocType;
  private String secondOutlierDocType;
  private String thirdOutlierDocType;
  /**
   * 外部单据id
   **/
  private String firstAccountId;
  private String secondAccountId;
  private String thirdAccountId;

  private SourceDocumentExcuteStatus excuteStatus;
  private SourceDocumentExcuteResult excuteResult;
  private String relatedContractUuid;
  /**
   * OfflineBill和batch_pay_record生成时没有financialContractUuid
   */
  private String financialContractUuid;
  /**
   * OfflineBill和batch_pay_record生成时没有sourceDocumentNo
   */
  private String sourceDocumentNo;

  private Integer firstPartyType;
  private String firstPartyName;
  private String virtualAccountNo;

  private Date lastModifiedTime;
  /* end */

  /**
   * 新增凭证uuid字段
   */
  private String voucherUuid;

  /**
   * 新增计划充值金额字段
   */
  private BigDecimal planBookingAmount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public String getSourceDocumentUuid() {
    return sourceDocumentUuid;
  }

  public void setSourceDocumentUuid(String sourceDocumentUuid) {
    this.sourceDocumentUuid = sourceDocumentUuid;
  }

  public SourceDocumentType getSourceDocumentType() {
    return sourceDocumentType;
  }

  public void setSourceDocumentType(SourceDocumentType sourceDocumentType) {
    this.sourceDocumentType = sourceDocumentType;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getIssuedTime() {
    return issuedTime;
  }

  public void setIssuedTime(Date issuedTime) {
    this.issuedTime = issuedTime;
  }

  public SourceDocumentStatus getSourceDocumentStatus() {
    return sourceDocumentStatus;
  }

  public void setSourceDocumentStatus(SourceDocumentStatus sourceDocumentStatus) {
    this.sourceDocumentStatus = sourceDocumentStatus;
  }

  public AccountSide getSourceAccountSide() {
    return sourceAccountSide;
  }


  public String getFinancialContractUuid() {
    return financialContractUuid;
  }

  public void setFinancialContractUuid(String financialContractUuid) {
    this.financialContractUuid = financialContractUuid;
  }

  public String getSourceDocumentNo() {
    return sourceDocumentNo;
  }

  public void setSourceDocumentNo(String sourceDocumentNo) {
    this.sourceDocumentNo = sourceDocumentNo;
  }

  public void setSourceAccountSide(AccountSide sourceAccountSide) {
    this.sourceAccountSide = sourceAccountSide;
  }

  public BigDecimal getBookingAmount() {
    return bookingAmount;
  }

  public void setBookingAmount(BigDecimal bookingAmount) {
    this.bookingAmount = bookingAmount;
  }

  public String getOutlierDocumentUuid() {
    return outlierDocumentUuid;
  }

  public void setOutlierDocumentUuid(String outlierDocumentUuid) {
    this.outlierDocumentUuid = outlierDocumentUuid;
  }

  public Date getOutlierTradeTime() {
    return outlierTradeTime;
  }

  public void setOutlierTradeTime(Date outlierTradeTime) {
    this.outlierTradeTime = outlierTradeTime;
  }

  public String getOutlierCounterPartyAccount() {
    return outlierCounterPartyAccount;
  }

  public void setOutlierCounterPartyAccount(String outlierCounterPartyAccount) {
    this.outlierCounterPartyAccount = outlierCounterPartyAccount;
  }

  public String getOutlierCounterPartyName() {
    return outlierCounterPartyName;
  }

  public void setOutlierCounterPartyName(String outlierCounterPartyName) {
    this.outlierCounterPartyName = outlierCounterPartyName;
  }

  public String getOutlierAccount() {
    return outlierAccount;
  }

  public void setOutlierAccount(String outlierAccount) {
    this.outlierAccount = outlierAccount;
  }

  public String getOutlieAccountName() {
    return outlieAccountName;
  }

  public void setOutlieAccountName(String outlieAccountName) {
    this.outlieAccountName = outlieAccountName;
  }

  public Long getOutlierAccountId() {
    return outlierAccountId;
  }

  public void setOutlierAccountId(Long outlierAccountId) {
    this.outlierAccountId = outlierAccountId;
  }

  public Long getOutlierCompanyId() {
    return outlierCompanyId;
  }

  public void setOutlierCompanyId(Long outlierCompanyId) {
    this.outlierCompanyId = outlierCompanyId;
  }

  public String getOutlierSerialGlobalIdentity() {
    return outlierSerialGlobalIdentity;
  }

  public void setOutlierSerialGlobalIdentity(String outlierSerialGlobalIdentity) {
    this.outlierSerialGlobalIdentity = outlierSerialGlobalIdentity;
  }

  public String getOutlierMemo() {
    return outlierMemo;
  }

  public void setOutlierMemo(String outlierMemo) {
    this.outlierMemo = outlierMemo;
  }

  public BigDecimal getOutlierAmount() {
    return outlierAmount;
  }

  public void setOutlierAmount(BigDecimal outlierAmount) {
    this.outlierAmount = outlierAmount;
  }

  public SettlementModes getOutlierSettlementModes() {
    return outlierSettlementModes;
  }

  public void setOutlierSettlementModes(SettlementModes outlierSettlementModes) {
    this.outlierSettlementModes = outlierSettlementModes;
  }

  public String getOutlierBreif() {
    return outlierBreif;
  }

  public void setOutlierBreif(String outlierBreif) {
    this.outlierBreif = outlierBreif;
  }

  public AccountSide getOutlierAccountSide() {
    return outlierAccountSide;
  }

  public void setOutlierAccountSide(AccountSide outlierAccountSide) {
    this.outlierAccountSide = outlierAccountSide;
  }

  public String getAppendix() {
    return appendix;
  }

  public void setAppendix(String appendix) {
    this.appendix = appendix;
  }

  public String getRemarkInAppendix() {
    Map<String, Object> appendixMap = parseAppendix();
    return (String) appendixMap.get(APPENDIX_REMARK);
  }

  public void setRemarkInAppendix(String remark) {
    Map<String, Object> appendixMap = parseAppendix();

    appendixMap.put(APPENDIX_REMARK, remark);
    this.appendix = JSON.toJSONString(appendixMap);
  }

  private Map<String, Object> parseAppendix() {
    Map<String, Object> appendixMap = new HashMap<String, Object>();
    try {
      appendixMap = JSON.parseObject(appendix, new TypeReference<Map<String, Object>>() {
      });
    } catch (Exception e) {

    }
    if (appendixMap == null) {
      appendixMap = new HashMap<String, Object>();
    }
    return appendixMap;
  }

  public String getFirstOutlierDocType() {
    return firstOutlierDocType;
  }

  public void setFirstOutlierDocType(String firstOutlierDocType) {
    this.firstOutlierDocType = firstOutlierDocType;
  }

  public String getSecondOutlierDocType() {
    return secondOutlierDocType;
  }

  public void setSecondOutlierDocType(String secondOutlierDocType) {
    this.secondOutlierDocType = secondOutlierDocType;
  }

  public String getThirdOutlierDocType() {
    return thirdOutlierDocType;
  }

  public void setThirdOutlierDocType(String thirdOutlierDocType) {
    this.thirdOutlierDocType = thirdOutlierDocType;
  }

  public RepaymentAuditStatus getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(RepaymentAuditStatus auditStatus) {
    this.auditStatus = auditStatus;
  }

  public Currency getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(Currency currencyType) {
    this.currencyType = currencyType;
  }

  public String getFirstPartyId() {
    return firstPartyId;
  }

  public void setFirstPartyId(String firstPartyId) {
    this.firstPartyId = firstPartyId;
  }

  public String getSecondPartyId() {
    return secondPartyId;
  }

  public void setSecondPartyId(String secondPartyId) {
    this.secondPartyId = secondPartyId;
  }

  public String getVirtualAccountUuid() {
    return virtualAccountUuid;
  }

  public void setVirtualAccountUuid(String virtualAccountUuid) {
    this.virtualAccountUuid = virtualAccountUuid;
  }

  public String getFirstAccountId() {
    return firstAccountId;
  }

  public void setFirstAccountId(String firstAccountId) {
    this.firstAccountId = firstAccountId;
  }

  public String getSecondAccountId() {
    return secondAccountId;
  }

  public void setSecondAccountId(String secondAccountId) {
    this.secondAccountId = secondAccountId;
  }

  public String getThirdAccountId() {
    return thirdAccountId;
  }

  public void setThirdAccountId(String thirdAccountId) {
    this.thirdAccountId = thirdAccountId;
  }

  public SourceDocumentExcuteStatus getExcuteStatus() {
    return excuteStatus;
  }

  public void setExcuteStatus(SourceDocumentExcuteStatus excuteStatus) {
    this.excuteStatus = excuteStatus;
  }

  public SourceDocumentExcuteResult getExcuteResult() {
    return excuteResult;
  }

  public void setExcuteResult(SourceDocumentExcuteResult excuteResult) {
    this.excuteResult = excuteResult;
  }

  public String getRelatedContractUuid() {
    return relatedContractUuid;
  }

  public void setRelatedContractUuid(String relatedContractUuid) {
    this.relatedContractUuid = relatedContractUuid;
  }

  public Integer getFirstPartyType() {
    return firstPartyType;
  }

  public void setFirstPartyType(Integer firstPartyType) {
    this.firstPartyType = firstPartyType;
  }

  public String getFirstPartyName() {
    return firstPartyName;
  }

  public void setFirstPartyName(String firstPartyName) {
    this.firstPartyName = firstPartyName;
  }

  public String getVirtualAccountNo() {
    return virtualAccountNo;
  }

  public void setVirtualAccountNo(String virtualAccountNo) {
    this.virtualAccountNo = virtualAccountNo;
  }

  public Date getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(Date lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }


  public String getVoucherUuid() {
    return voucherUuid;
  }

  public void setVoucherUuid(String voucherUuid) {
    this.voucherUuid = voucherUuid;
  }


  public BigDecimal getPlanBookingAmount() {
    return planBookingAmount;
  }

  public void setPlanBookingAmount(BigDecimal planBookingAmount) {
    this.planBookingAmount = planBookingAmount;
  }

  public SourceDocument() {
  }

  @Deprecated
  public SourceDocument(AppArriveRecord cashFlow) {
    super();
    this.setSourceDocumentUuid(UUID.randomUUID().toString());
    this.setSourceDocumentType(SourceDocumentType.NOTIFY);
    this.setCreateTime(new Date());
    this.setSourceDocumentStatus(SourceDocumentStatus.CREATE);
    this.setSourceAccountSide(AccountSide.reverse(BankSide.fromValue(cashFlow.getDrcrf())));
    this.setBookingAmount(BigDecimal.ZERO);
    this.setAuditStatus(RepaymentAuditStatus.CREATE);
    this.setOutlierDocumentUuid(cashFlow.getCashFlowUid());
    this.setOutlierTradeTime(cashFlow.getTime());
    this.setOutlierCounterPartyAccount(cashFlow.getPayAcNo());
    this.setOutlierCounterPartyName(cashFlow.getPayName());
    this.setOutlierAccount(cashFlow.getReceiveAcNo());
    if (null != cashFlow.getApp() && null != cashFlow.getApp().getCompany()) {
      this.setOutlierCompanyId(cashFlow.getApp().getCompany().getId());
    } else {
      this.setOutlierCompanyId(null);
    }
    this.setOutlierSerialGlobalIdentity(cashFlow.getSerialNo());
    this.setOutlierMemo(cashFlow.getSummary());
    this.setOutlierAmount(cashFlow.getAmount());
    this.setOutlierSettlementModes(SettlementModes.REMITTANCE);
    this.setOutlierBreif(cashFlow.getRemark());
    this.setOutlierAccountSide(AccountSide.transform(BankSide.fromValue(cashFlow.getDrcrf())));

  }

  public void initBasePart(SourceDocumentType sourceDocumentType, SourceDocumentStatus sourceDocumentStatus,
      AccountSide sourceAccountSide,
      BigDecimal bookingAmount, RepaymentAuditStatus auditStatus, String outlierDocumentUuid, Date outlierTradeTime,
      String outlierCounterPartyAccount,
      String outlierCounterPartyName, String outlierAccount, String outlieAccountName, Long outlierAccountId,
      Long companyId, String outlierSerialGlobalIdentity, String outlierMemo, BigDecimal outlierAmount,
      Currency currencyType,
      SettlementModes outlierSettlementModes, String outlierBreif, AccountSide outlierAccountSide) {
    this.setSourceDocumentType(sourceDocumentType);
    this.setSourceDocumentStatus(sourceDocumentStatus);
    this.setSourceAccountSide(sourceAccountSide);
    this.setBookingAmount(bookingAmount);
    this.setAuditStatus(auditStatus);
    this.setOutlierDocumentUuid(outlierDocumentUuid);
    this.setOutlierTradeTime(outlierTradeTime);
    this.setOutlierCounterPartyAccount(outlierCounterPartyAccount);
    this.setOutlierCounterPartyName(outlierCounterPartyName);
    this.setOutlierAccount(outlierAccount);
    this.outlieAccountName = outlieAccountName;
    this.outlierAccountId = outlierAccountId;
    this.setCompanyId(companyId);
    this.setOutlierSerialGlobalIdentity(outlierSerialGlobalIdentity);
    this.setOutlierMemo(outlierMemo);
    this.setOutlierAmount(outlierAmount);
    this.currencyType = currencyType;
    this.setOutlierSettlementModes(outlierSettlementModes);
    this.setOutlierBreif(outlierBreif);
    this.setOutlierAccountSide(outlierAccountSide);

  }

  public void initTypePart(String firstOutlierDocType, String secondOutlierDocType, String thirdOutlierDocType,
      String firstAccountId, String secondAccountId, String thirdAccountId,
      String firstPartyId, String secondPartyId, String virtualAccountUuid,
      String relatedContractUuid, String sourceDocumentNo, String financialContractUuid,
      Integer firstPartyType, String firstPartyName, String virtualAccountNo,
      SourceDocumentExcuteStatus excuteStatus, SourceDocumentExcuteResult excuteResult,
      RepaymentAuditStatus auditStatus, String remark) {
    this.firstOutlierDocType = firstOutlierDocType;
    this.secondOutlierDocType = secondOutlierDocType;
    this.thirdOutlierDocType = thirdOutlierDocType;
    this.firstAccountId = firstAccountId;
    this.secondAccountId = secondAccountId;
    this.thirdAccountId = thirdAccountId;
    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;
    this.virtualAccountUuid = virtualAccountUuid;
    this.relatedContractUuid = relatedContractUuid;
    this.sourceDocumentNo = sourceDocumentNo;
    this.financialContractUuid = financialContractUuid;
    this.firstPartyType = firstPartyType;
    this.firstPartyName = firstPartyName;
    this.virtualAccountNo = virtualAccountNo;
    this.excuteResult = excuteResult;
    this.excuteStatus = excuteStatus;
    this.setAuditStatus(auditStatus);
    this.setRemarkInAppendix(remark);

  }

  public void changeBookingAmountAndAuditStatus(BigDecimal bookingAmount) {
    if (null == bookingAmount) {
      return;
    }
    if (null == this.getOutlierAmount()) {
      this.setOutlierAmount(BigDecimal.ZERO);
    }
    if (null == this.getBookingAmount()) {
      this.setBookingAmount(BigDecimal.ZERO);
    }
    this.setBookingAmount(this.getBookingAmount().add(bookingAmount));
    if (this.getBookingAmount().compareTo(BigDecimal.ZERO) < 0) {
      this.setBookingAmount(BigDecimal.ZERO);
    } else if (this.getBookingAmount().compareTo(BigDecimal.ZERO) == 0) {
      this.setAuditStatus(RepaymentAuditStatus.CREATE);
    } else if (this.getBookingAmount().compareTo(this.getOutlierAmount()) < 0) {
      this.setAuditStatus(RepaymentAuditStatus.ISSUING);
    } else {
      this.setAuditStatus(RepaymentAuditStatus.ISSUED);
    }
  }

  public void updateAuditStatus(BigDecimal comparedAmount) {
    if (comparedAmount == null) {
      comparedAmount = BigDecimal.ZERO;
    }
    if (this.getBookingAmount().compareTo(comparedAmount) < 0) {
      this.setAuditStatus(RepaymentAuditStatus.ISSUING);
    } else if (this.getBookingAmount().compareTo(comparedAmount) == 0) {
      this.setAuditStatus(RepaymentAuditStatus.ISSUED);
    } else {
      this.setAuditStatus(RepaymentAuditStatus.ISSUING);
    }
  }

  public void update_after_inter_account_transfer(boolean result) {
    if (result) {
      setExcuteResult(SourceDocumentExcuteResult.SUCCESS);
      setExcuteStatus(SourceDocumentExcuteStatus.DONE);
    }
    this.lastModifiedTime = new Date();
  }

  public void fillBillInfo(String financialContractUuid, String contractUuid) {
    this.financialContractUuid = financialContractUuid;
    this.relatedContractUuid = contractUuid;
  }

  public void fillPartyInfo(Company company, Customer customer) {
    if (company != null) {
      this.firstPartyId = company.getUuid();
      this.firstPartyName = company.getShortName();
    }
    if (customer != null) {
      this.secondPartyId = customer.getCustomerUuid();
    }
  }

  public void fillPartyInfo(Company company, FastCustomer fastCustomer) {
    if (company != null) {
      this.firstPartyId = company.getUuid();
      this.firstPartyName = company.getShortName();
    }
    if (fastCustomer != null) {
      this.secondPartyId = fastCustomer.getCustomerUuid();
    }
  }

  public SourceDocument(Long companyId, SourceDocumentType sourceDocumentType, Date createTime,
      Date issuedTime, SourceDocumentStatus sourceDocumentStatus,
      AccountSide sourceAccountSide, BigDecimal bookingAmount,
      String outlierDocumentUuid, Date outlierTradeTime,
      String outlierCounterPartyAccount, String outlierCounterPartyName,
      String outlierAccount, String outlieAccountName,
      Long outlierAccountId, Long outlierCompanyId,
      String outlierSerialGlobalIdentity, String outlierMemo,
      BigDecimal outlierAmount, SettlementModes outlierSettlementModes,
      String outlierBreif, AccountSide outlierAccountSide, String appendix,
      String firstOutlierDocType, String secondOutlierDocType, String thirdOutlierDocType,
      RepaymentAuditStatus auditStatus,
      String firstPartyId, String secondPartyId, String virtualAccountUuid,
      String firstAccountId, String secondAccountId, String thirdAccountId,
      SourceDocumentExcuteStatus excuteStatus, SourceDocumentExcuteResult excuteResult, String relatedContractUuid) {
    super();
    this.setCompanyId(companyId);
    this.setSourceDocumentUuid(UUID.randomUUID().toString().replace("-", ""));
    this.setSourceDocumentType(sourceDocumentType);
    this.setCreateTime(createTime);
    this.setLastModifiedTime(createTime);
    this.issuedTime = issuedTime;
    this.setSourceDocumentStatus(sourceDocumentStatus);
    this.setSourceAccountSide(sourceAccountSide);
    this.setBookingAmount(bookingAmount);
    this.setOutlierDocumentUuid(outlierDocumentUuid);
    this.setOutlierTradeTime(outlierTradeTime);
    this.setOutlierCounterPartyAccount(outlierCounterPartyAccount);
    this.setOutlierCounterPartyName(outlierCounterPartyName);
    this.setOutlierAccount(outlierAccount);
    this.outlieAccountName = outlieAccountName;
    this.outlierAccountId = outlierAccountId;
    this.setOutlierCompanyId(outlierCompanyId);
    this.setOutlierSerialGlobalIdentity(outlierSerialGlobalIdentity);
    this.setOutlierMemo(outlierMemo);
    this.setOutlierAmount(outlierAmount);
    this.setOutlierSettlementModes(outlierSettlementModes);
    this.setOutlierBreif(outlierBreif);
    this.setOutlierAccountSide(outlierAccountSide);
    this.appendix = appendix;
    this.firstOutlierDocType = firstOutlierDocType;
    this.secondOutlierDocType = secondOutlierDocType;
    this.thirdOutlierDocType = thirdOutlierDocType;
    this.setAuditStatus(auditStatus);

    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;

    this.virtualAccountUuid = virtualAccountUuid;

    this.firstAccountId = firstAccountId;
    this.secondAccountId = secondAccountId;
    this.thirdAccountId = thirdAccountId;

    this.excuteStatus = excuteStatus;
    this.excuteResult = excuteResult;
    this.relatedContractUuid = relatedContractUuid;
  }

  public SourceDocument(Long companyId, SourceDocumentType sourceDocumentType, Date createTime,
      Date issuedTime, SourceDocumentStatus sourceDocumentStatus,
      AccountSide sourceAccountSide, BigDecimal bookingAmount,
      String outlierDocumentUuid, Date outlierTradeTime,
      String outlierCounterPartyAccount, String outlierCounterPartyName,
      String outlierAccount, String outlieAccountName,
      Long outlierAccountId, Long outlierCompanyId,
      String outlierSerialGlobalIdentity, BigDecimal outlierAmount,
      SettlementModes outlierSettlementModes,
      AccountSide outlierAccountSide, String firstOutlierDocType, String secondOutlierDocType,
      String thirdOutlierDocType, RepaymentAuditStatus auditStatus,
      String firstPartyId, String secondPartyId, String virtualAccountUuid,
      String firstAccountId, String secondAccountId, String thirdAccountId,
      SourceDocumentExcuteStatus excuteStatus, SourceDocumentExcuteResult excuteResult, String relatedContractUuid) {
    this(companyId, sourceDocumentType, createTime,
        issuedTime, sourceDocumentStatus,
        sourceAccountSide, bookingAmount,
        outlierDocumentUuid, outlierTradeTime,
        outlierCounterPartyAccount, outlierCounterPartyName,
        outlierAccount, outlieAccountName,
        outlierAccountId, outlierCompanyId,
        outlierSerialGlobalIdentity, StringUtils.EMPTY,
        outlierAmount, outlierSettlementModes,
        StringUtils.EMPTY, outlierAccountSide, StringUtils.EMPTY, firstOutlierDocType, secondOutlierDocType,
        thirdOutlierDocType, auditStatus,
        firstPartyId,
        secondPartyId, virtualAccountUuid,
        firstAccountId, secondAccountId, thirdAccountId,
        excuteStatus, excuteResult, relatedContractUuid);
  }

  public String getUuid() {
    return this.sourceDocumentUuid;
  }

  public void updateSourceDocumetStatus(SourceDocumentStatus sourceDocumentStatus) {
    this.sourceDocumentStatus = sourceDocumentStatus;
    this.lastModifiedTime = new Date();
  }

  public String getsourceDocumentStatusName() {
    return sourceDocumentStatus == null ? "" : sourceDocumentStatus.getChineseName();
  }

  public boolean isSigned() {
    return sourceDocumentStatus == SourceDocumentStatus.SIGNED;
  }

  public boolean sourceDocumentBeAbleToExcute() {
    if (excuteStatus == SourceDocumentExcuteStatus.PREPARE || excuteStatus == SourceDocumentExcuteStatus.DONE) {
      return true;
    }
    return false;
  }

  public String getMqBusinessId() {
    if (StringUtils.isNotEmpty(relatedContractUuid)) {
      return relatedContractUuid;
    }
    return financialContractUuid;
  }

}

