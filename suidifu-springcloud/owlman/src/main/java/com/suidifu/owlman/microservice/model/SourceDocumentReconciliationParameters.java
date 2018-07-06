package com.suidifu.owlman.microservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wukai
 */
@Data
@NoArgsConstructor
public class SourceDocumentReconciliationParameters {
    private String sourceDocumentUuid;

    private String sourceDocumentDetailUuid;

    private String financialContractNo;

    private String ledgerBookNo;

    private String secondType;

    private boolean isDetaislFile;

    private SourceDocumentDetail sourceDocumentDetail;

    public SourceDocumentReconciliationParameters(String sourceDocumentUuid,
                                                  String sourceDocumentDetailUuid, String financialContractNo,
                                                  String ledgerBookNo) {
        super();
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.sourceDocumentDetailUuid = sourceDocumentDetailUuid;
        this.financialContractNo = financialContractNo;
        this.ledgerBookNo = ledgerBookNo;
    }
}