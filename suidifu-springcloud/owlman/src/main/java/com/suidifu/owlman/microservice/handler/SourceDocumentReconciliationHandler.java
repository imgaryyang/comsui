package com.suidifu.owlman.microservice.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters;
import java.util.List;
import java.util.Map;

public interface SourceDocumentReconciliationHandler {
    // stage one
    Map<String, String> criticalMarker(List<SourceDocumentDetailReconciliationParameters> detailReconciliationParameters);

    //stage two
    boolean validateSourceDocumentDetailList(List<SourceDocumentReconciliationParameters> sourceDocumentReconciliationStepThreeParameterList);

    //stage three
    boolean fetchVirtualAccountAndBusinessPaymentVoucherTransfer
    (List<SourceDocumentReconciliationParameters> sourceDocumentReconciliationStepTwoParameterList);

    //stage fourth
    boolean sourceDocumentRecoverDetails(List<SourceDocumentReconciliationParameters> sourceDocumentReconciliationStepThreeParameterList) throws JsonProcessingException;

    //stage fifth
    boolean unfreezeCapital(List<SourceDocumentReconciliationParameters> sourceDocumentReconciliationStepFiveParameterList);
}