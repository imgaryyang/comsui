package com.suidifu.microservice.handler;

import static com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState.CHECK_SUCCESS;
import static com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus.SUCCESS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.entity.Voucher;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCheckingLevel;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.model.TmpDepositDocRecoverParams;
import com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace;
import com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace.ReconciliationForRepurchase;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public abstract class ReconciliationForSourceDocument extends ReconciliationBase {
    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private VoucherService voucherService;

    @Override
    public ReconciliationContext preAccountReconciliation(
            Map<String, Object> inputParams) throws AlreadyProcessedException, JsonProcessingException {
        String sourceDocumentDetailUuid = (String) inputParams.get(ReconciliationForRepurchase.PARAMS_SOURCE_DOCUMENT_DETAIL_UUID);
        SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.getSourceDocumentDetail(sourceDocumentDetailUuid);
        String sourceDocumentUuid = (String) inputParams.get(ReconciliationForRepurchase.PARAMS_SOURCE_DOCDUMENT_UUID);

        SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
        ReconciliationContext context = new ReconciliationContext();
        context.setTmpDepositDocParams((TmpDepositDocRecoverParams) inputParams.get(ReconciliationParameterNameSpace.PARAMS_TMP_DEPSOIT_RECOVER_PARAMS));
        context.setBookingAmount(sourceDocumentDetail.getAmount());
        if (sourceDocumentDetail.getStatus() != SourceDocumentDetailStatus.UNSUCCESS) {
            context.setAlreadyReconciliation(true);
            throw new AlreadyProcessedException();

        }
        context.setRecoverType(AssetRecoverType.LOAN_ASSET);
        context.setActualRecycleTime(sourceDocument.getOutlierTradeTime());
        context.setSourceDocument(sourceDocument);
        context.setSourceDocumentDetail(sourceDocumentDetail);
        //设置核销的明细
        context.setBookingDetailAmount(sourceDocumentDetail.getDetailAmountMap());

        long start = System.currentTimeMillis();

        fillVoucherNo(context, sourceDocument);

        long end = System.currentTimeMillis();
        log.debug("#abcdefgh#preAccountReconciliation#super fillVoucherNo use [" + (end - start) + "]ms");
        return context;

    }

    public void fillVoucherNo(ReconciliationContext context, SourceDocument sourceDocument) {
        Voucher voucher = voucherService.getVoucherBySourceDocument(sourceDocument);
        if (voucher != null) {
            context.setSourceDocumentNo(voucher.getVoucherNo());
        } else {
            context.setSourceDocumentNo(sourceDocument.getSourceDocumentNo());
        }
    }

    @Override
    public void relatedDocumentsProcessing(ReconciliationContext context) {
        super.processIfPrepaymentPlan(context);
        Order order = orderService.createAndSaveOrder(context.getBookingAmount(), "", context.getAssetSet(),
                context.getBorrowerCustomer(), context.getFinancialContract(), context.getActualRecycleTime());
        context.setOrder(order);
    }

    @Override
    public void refreshVirtualAccount(ReconciliationContext context) {
        super.refreshVirtualAccount(context);
    }

    @Override
    public void postAccountReconciliation(ReconciliationContext context) {
        SourceDocumentDetail sourceDocumentDetail = context.getSourceDocumentDetail();
        sourceDocumentDetail.setStatus(SUCCESS);
        sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);

        super.postAccountReconciliation(context);
    }

    @Override
    public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {
        JournalVoucher journalVoucher = new JournalVoucher();
        journalVoucher.createFromContext(context);
        journalVoucher.fill_voucher_and_booking_amount(context.getJournalVoucherResolver().getRelatedBillUuid(),
                "",
                "", context.getBookingAmount(), JournalVoucherStatus.VOUCHER_ISSUED,
                JournalVoucherCheckingLevel.AUTO_BOOKING, context.getJournalVoucherType());
        context.setIssuedJournalVoucher(journalVoucher);
        super.issueJournalVoucher(context);
    }

    @Override
    public void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException {
        log.info("checkState key is:{},ordinal is:{}",
                context.getSourceDocumentDetail().getCheckState().getKey(),
                context.getSourceDocumentDetail().getCheckState().ordinal());
        if (context.getFinancialContract() == null) {
            throw new ReconciliationException("empty financialContract");
        }
        if (context.getSourceDocument() == null)
            throw new ReconciliationException("empty sourceDocument");
        SourceDocumentDetail sourceDocumentDetail = context.getSourceDocumentDetail();
        if (sourceDocumentDetail == null)
            throw new ReconciliationException("emtpy sourceDocument detail");

        if (!sourceDocumentDetail.getCheckState().equals(CHECK_SUCCESS))
            throw new ReconciliationException("invalid sourceDocument detail ");

        if (sourceDocumentDetail.getStatus().equals(SUCCESS))
            throw new AlreadyProcessedException();

        if (context.getCompanyCustomer() == null)
            throw new ReconciliationException("Invalid Company customer  ");

    }
}