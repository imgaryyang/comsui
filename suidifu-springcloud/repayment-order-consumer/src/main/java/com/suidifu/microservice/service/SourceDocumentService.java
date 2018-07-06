package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.microservice.entity.SourceDocument;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.math.BigDecimal;
import java.util.List;

public interface SourceDocumentService extends GenericService<SourceDocument> {
    SourceDocument getSourceDocumentBy(String sourceDocumentUuid);

    String getUnWriteOffSourceDocumentUuidBy(String deductApplicationUuid);

    void signSourceDocument(SourceDocument sourceDocument, BigDecimal issuedAmount);

    SourceDocument getActiveVoucher(String cashFlowUuid,
                                    SourceDocumentStatus sourceDocumentStatus,
                                    String contractUuid,
                                    String financialContractUuid);

    List<SourceDocument> getDepositReceipt(String cashFlowUuid, SourceDocumentStatus sourceDocumentStatus, String contractUuid, String financialContractUuid);
}