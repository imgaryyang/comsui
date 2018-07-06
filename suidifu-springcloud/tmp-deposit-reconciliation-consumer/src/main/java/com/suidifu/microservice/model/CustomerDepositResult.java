package com.suidifu.microservice.model;

import com.suidifu.microservice.entity.SourceDocument;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
public class CustomerDepositResult {

  /**
   * 充值单号
   */
  private static final String KEY_DEPOSIT_NO = "depositNo";
  /**
   * 账户编号
   */
  private static final String KEY_VIRTUAL_ACCOUNT_NO = "virtualAccountNo";
  /**
   * 客户名称
   */
  private static final String KEY_CUSTOMER_NAME = "customerName";
  /**
   * 客户类型
   */
  private static final String KEY_CUSTOMER_TYPE_MSG = "customerTypeMsg";
  /**
   * 贷款合同编号
   */
  private static final String KEY_CONTRACT_NO = "contractNo";
  /**
   * 信托项目名称
   */
  private static final String KEY_FINANCIAL_CONTRACT_NAME = "financialContractName";

  private String sourceDocumentUuid;
  /**
   * 账户余额
   */
  private BigDecimal balance;
  /**
   * 充值金额
   */
  private BigDecimal depositAmount;
  /**
   * 状态
   */
  private int sourceDocumentStatusInt;
  /**
   * 备注
   */
  private String remark;
  private String virtualAccountUuid;
  private Map<String, Object> showData = new HashMap<>();


  public String getSourceDocumentStatusEnum() {
    return EnumUtil.fromOrdinal(SourceDocumentStatus.class, sourceDocumentStatusInt)
        .getChineseName();
  }

  public CustomerDepositResult(String depositNo, String virtualAccountNo,
      String customerName, int customerTypeInt, String contractNo,
      String financialContractName, BigDecimal balance,
      BigDecimal depositAmount, String remark,
      int sourceDocumentStatusInt, String sourceDocumentUuid, String virtualAccountUuid) {
    super();
    this.showData.put(KEY_DEPOSIT_NO, depositNo);
    this.showData.put(KEY_VIRTUAL_ACCOUNT_NO, virtualAccountNo);
    this.showData.put(KEY_CUSTOMER_NAME, customerName);
    CustomerType customerType = EnumUtil.fromOrdinal(CustomerType.class, customerTypeInt);
    this.showData.put(KEY_CUSTOMER_TYPE_MSG, customerType == null ? "" : customerType.getChineseName());
    this.showData.put(KEY_CONTRACT_NO, contractNo);
    this.showData.put(KEY_FINANCIAL_CONTRACT_NAME, financialContractName);
    this.remark = remark;
    this.balance = balance;
    this.depositAmount = depositAmount;
    this.sourceDocumentStatusInt = sourceDocumentStatusInt;
    this.sourceDocumentUuid = sourceDocumentUuid;
    this.virtualAccountUuid = virtualAccountUuid;
  }

  public CustomerDepositResult(SourceDocument sourceDocument, String contractNo,
      String financialContractName, String virtualAccountNo, BigDecimal balance,
      String virtualAccountUuid) {
    this(sourceDocument.getSourceDocumentNo(), virtualAccountNo,
        sourceDocument.getFirstPartyName(),
        sourceDocument.getFirstPartyType() == null ? -1 : sourceDocument.getFirstPartyType(),
        contractNo,
        financialContractName, balance,
        sourceDocument.getBookingAmount(), sourceDocument.getRemarkInAppendix(),
        sourceDocument.getSourceDocumentStatus() == null ? -1
            : sourceDocument.getSourceDocumentStatus().ordinal(),
        sourceDocument.getSourceDocumentUuid(), virtualAccountUuid);
  }

  public static CustomerDepositResult buildToDeposit(FinancialContract financialContract,
      Contract contract, BigDecimal balance, VirtualAccount virtualAccount, BigDecimal issuedAmount,
      BigDecimal transactionAmount) {
    return new CustomerDepositResult(StringUtils.EMPTY,
        virtualAccount.getVirtualAccountNo(),
        virtualAccount.getOwnerName(),
        virtualAccount.getCustomerType() == null ? -1 : virtualAccount.getCustomerType(),
        contract == null ? StringUtils.EMPTY : contract.getContractNo(),
        financialContract == null ? StringUtils.EMPTY : financialContract.getContractName(),
        balance,
        transactionAmount.subtract(issuedAmount), StringUtils.EMPTY,
        SourceDocumentStatus.CREATE.ordinal(), StringUtils.EMPTY,
        virtualAccount.getVirtualAccountUuid());
  }
}
