package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.Reconciliation;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.handler.TmpDepositReconciliationHandler;
import com.suidifu.owlman.microservice.model.TmpDepositDocRecoverParams;
import com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace;
import com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace.ReconciliationForSubrogation;
import com.suidifu.owlman.microservice.model.TmpDepositReconciliationParameters;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.LedgerBookService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

/**
 * @author louguanyang at 2018/2/28 16:59
 * @mail louguanyang@hzsuidifu.com
 */
@Log4j2
@Component("tmpDepositReconciliationHandler")
public class TmpDepositReconciliationHandlerImpl implements TmpDepositReconciliationHandler {

  @Resource
  private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;
  @Resource
  private SourceDocumentService sourceDocumentService;
  @Resource
  private LedgerBookService ledgerBookService;

  @Override
  public Map<String, String> criticalMarker(List<String> detailUuids) {
    if (log.isDebugEnabled()) {
      log.debug("micro-service start processing method.");
    }
    return businessPaymentVoucherHandler.getCriticalMarker(detailUuids);
  }

  @Override
  public boolean validateDetailList(List<TmpDepositReconciliationParameters> params) {
    if (CollectionUtils.isEmpty(params)) {
      log.info("validateDetailList fail, params is empty");
      return false;
    }

    long startTime = System.currentTimeMillis();
    String sourceDocumentUuid = params.get(0).getSourceDocumentUuid();
    String tmpDepositDocUuid = params.get(0).getTmpDepositDocUuid();

    SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
    Date cashFlowTransactionTime = sourceDocument == null ? null : sourceDocument.getOutlierTradeTime();

    long start = startTime;
    String detailUuid, financialContractNo, ledgerBookNo;
    boolean isAllDetailsValid = true, isDetailValid;
    for (TmpDepositReconciliationParameters param : params) {
      if (log.isDebugEnabled()) {
        start = System.currentTimeMillis();
      }

      detailUuid = param.getSourceDocumentDetailUuid();
      financialContractNo = param.getFinancialContractNo();
      ledgerBookNo = param.getLedgerBookNo();

      isDetailValid = businessPaymentVoucherHandler.isDetailValid(detailUuid, financialContractNo, ledgerBookNo, cashFlowTransactionTime);

      if (log.isDebugEnabled()) {
        log.debug("process method isDetailValid down, detailUuid:{}, item usage:{}ms.", detailUuid,
            (System.currentTimeMillis() - start));
      }

      if (!isDetailValid) {
        isAllDetailsValid = false;
        log.error("isDetailValid false, sourceDocumentUuid:{}, param:{}", sourceDocumentUuid, param.toString());
      }
    }

    if (log.isDebugEnabled()) {
      int size = params.size();
      log.debug("validateDetailList down, sourceDocumentUuid:{}, tmpDepositDocUuid, size:{}, usage:{}ms.",
          sourceDocumentUuid, tmpDepositDocUuid, size, (System.currentTimeMillis() - startTime));
    }
    return isAllDetailsValid;
  }

  @Override
  public boolean virtualAccountTransfer(List<TmpDepositReconciliationParameters> params) {
    if (CollectionUtils.isEmpty(params) || params.size() > 1) {
      return false;
    }
    TmpDepositReconciliationParameters param = params.get(0);

    String sourceDocumentUuid = param.getSourceDocumentUuid();
    SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
    if (sourceDocument == null) {
      log.error("virtualAccountTransfer fail, sourceDocument is null, param:{}", param.toString());
      return false;
    }

    BigDecimal detailTotalAmount = param.getDetailTotalAmount();
    String ledgerBookNo = param.getLedgerBookNo();
    String financialContractUuid = sourceDocument.getFinancialContractUuid();
    String tmpDepositDocUuid = param.getTmpDepositDocUuid();
    String secondNo = param.getSecondNo();

    LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(ledgerBookNo);

    return businessPaymentVoucherHandler.freezeTmpDeposit(sourceDocumentUuid, detailTotalAmount, ledgerBook,
        financialContractUuid, tmpDepositDocUuid, secondNo);
  }

  @Override
  public boolean recoverDetails(List<TmpDepositReconciliationParameters> tmpDepositReconParams) {
    return !CollectionUtils.isEmpty(tmpDepositReconParams) && this.recoverTmpDepositDetail(tmpDepositReconParams);

  }

  private boolean recoverTmpDepositDetail(List<TmpDepositReconciliationParameters> tmpDepositReconParams) {
    boolean isAllDetailRecovered = true;
    String sourceDocumentUuid, sourceDocumentDetailUuid;
    Map<String, Object> objectMap;
    for (TmpDepositReconciliationParameters param : tmpDepositReconParams) {
      try {
        sourceDocumentUuid = param.getSourceDocumentUuid();
        sourceDocumentDetailUuid = param.getSourceDocumentDetailUuid();
        TmpDepositDocRecoverParams recoverParams = new TmpDepositDocRecoverParams(true, param.getTmpDepositDocUuid(),
            param.getSecondNo());

        objectMap = new HashMap<>(4);
        objectMap.put(ReconciliationForSubrogation.PARAMS_SOURCE_DOCDUMENT_UUID, sourceDocumentUuid);
        objectMap.put(ReconciliationForSubrogation.PARAMS_SOURCE_DOCUMENT_DETAIL_UUID, sourceDocumentDetailUuid);
        objectMap.put(ReconciliationParameterNameSpace.PARAMS_TMP_DEPSOIT_RECOVER_PARAMS, recoverParams);

        Reconciliation reconciliation = Reconciliation.reconciliationFactory(param.getSecondType());
        reconciliation.accountReconciliation(objectMap);
      } catch (Exception e) {
        log.error("recoverTmpDepositDetail fail, param:{}, StackTrace:{}", param.toString(),
            ExceptionUtils.getStackTrace(e));
        isAllDetailRecovered = false;
      }
    }
    return isAllDetailRecovered;
  }

  @Override
  public boolean unfreezeCapital(List<TmpDepositReconciliationParameters> tmpDepositRecon5thParams) {
    if (CollectionUtils.isEmpty(tmpDepositRecon5thParams) || tmpDepositRecon5thParams.size() > 1) {
      return false;
    }
    TmpDepositReconciliationParameters param = tmpDepositRecon5thParams.get(0);
    String ledgerBookNo = param.getLedgerBookNo();
    LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(ledgerBookNo);

    String sourceDocumentUuid = param.getSourceDocumentUuid();
    String financialContractNo = param.getFinancialContractNo();
    String tmpDepositDocUuid = param.getTmpDepositDocUuid();

    return businessPaymentVoucherHandler
        .unfreezeAmountOfTmpDepositDocAndVirtualAccount(sourceDocumentUuid, financialContractNo, ledgerBook,
            tmpDepositDocUuid);
  }
}
