package com.suidifu.microservice.model;


import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.owlman.microservice.enumation.SettlementModes;
import com.suidifu.owlman.microservice.enumation.SourceDocumentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.yunxin.entity.OfflineBill;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public class SourceDocumentImporter {

  public static SourceDocument createSourceDocumentFrom(Long companyId, OfflineBill offlineBill) {
    return initSourceDocumentFromOfflineBill(companyId, offlineBill);
  }


  public static SourceDocument initSourceDocumentFromOfflineBill(Long companyId, OfflineBill offlineBill) {
    return new SourceDocument(companyId, SourceDocumentType.NOTIFY, new Date(),
        null, SourceDocumentStatus.CREATE,
        AccountSide.DEBIT, BigDecimal.ZERO,
        offlineBill.getOfflineBillUuid(), offlineBill.getTradeTime(),
        offlineBill.getPayerAccountNo(), offlineBill.getPayerAccountName(),
        StringUtils.EMPTY, StringUtils.EMPTY,
        null, companyId,
        offlineBill.getSerialNo(), offlineBill.getAmount(), SettlementModes.REMITTANCE,
        AccountSide.DEBIT, SourceDocument.FIRSTOUTLIER_OFFLINEBILL, StringUtils.EMPTY, StringUtils.EMPTY,
        RepaymentAuditStatus.CREATE,
        StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
        StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
        null, null, StringUtils.EMPTY);
  }

}
