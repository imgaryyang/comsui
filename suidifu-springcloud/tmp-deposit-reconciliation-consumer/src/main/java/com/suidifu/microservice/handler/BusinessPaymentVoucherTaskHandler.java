package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author louguanyang at 2018/2/28 17:17
 * @mail louguanyang@hzsuidifu.com
 */
public interface BusinessPaymentVoucherTaskHandler {

  /**
   * 校验明细内容
   *
   * @param detailUuid 明细uuid
   * @param financialContractNo 信托合同产品代码
   * @param ledgerBookNo 账本编号
   * @param cashFlowTransactionTime 交易时间
   */
  boolean isDetailValid(String detailUuid, String financialContractNo, String ledgerBookNo,
      Date cashFlowTransactionTime);

  /**
   *
   * @param ledgerBookNo
   * @param financialContract
   * @param companyCustomerUuid
   * @param jvUuid
   * @param sdUuid
   * @param bookingAmount
   * @param tmpDepositDocUuid
   * @param sndSecondNo
   */
  public void recoverEachFrozenCapitalAmount(String ledgerBookNo, FinancialContract financialContract,
      String companyCustomerUuid, String jvUuid, String sdUuid,
      BigDecimal bookingAmount, String tmpDepositDocUuid, String sndSecondNo);

  /**
   *
   * @param sourceDocumentUuid
   * @param financialContractNo
   * @param book
   * @param tmpDepositDocUuid
   * @param toCreditAccount
   * @return
   */
  public boolean unfreezeCapitalAmountOfVoucher(String sourceDocumentUuid, String financialContractNo, LedgerBook book,
      String tmpDepositDocUuid, String toCreditAccount);

  /**
   *
   * @param sourceDocumentUuid
   * @param bookingAmount
   * @param ledgerBook
   * @param financialContractUuid
   * @param tmpDepositDocUuid
   * @param secondNo
   * @return
   */
  public boolean freezeTmpDeposit(String sourceDocumentUuid, BigDecimal bookingAmount, LedgerBook ledgerBook,
      String financialContractUuid, String tmpDepositDocUuid, String secondNo);

  /**
   *
   * @param sourceDocumentUuid
   * @param financialContractNo
   * @param book
   * @param tmpDepositDocUuid
   * @return
   */
  public boolean unfreezeAmountOfTmpDepositDocAndVirtualAccount(String sourceDocumentUuid,
      String financialContractNo, LedgerBook book, String tmpDepositDocUuid);

  /**
   *
   * @param sourceDocumentDetailList
   * @return
   */
  public Map<String, String> getCriticalMarker(List<String> sourceDocumentDetailList);

}
