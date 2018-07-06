package com.suidifu.microservice.handler.impl;


import static com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace.ReconciliationForSubrogation.PARAMS_SOURCE_DOCDUMENT_UUID;
import static com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace.ReconciliationForSubrogation.PARAMS_SOURCE_DOCUMENT_DETAIL_UUID;
import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastSourceDocumentDetailKeyEnum;
import com.suidifu.giotto.model.FastSourceDocumentDetail;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.entity.Voucher;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.Reconciliation;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.microservice.utils.OrikaMapper;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.handler.SourceDocumentReconciliationHandler;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.exception.VirtualAccountLockFailedException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("sourceDocumentReconciliation")
public class SubrogationReconciliationHandlerImpl implements SourceDocumentReconciliationHandler {
    @Resource
    private FastHandler fastHandler;
    @Resource
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;
    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Resource
    private VoucherService voucherService;

    /**
     * stage one
     *
     * @param parametersList 核销明细参数
     * @return
     */
    @Override
    public Map<String, String> criticalMarker(List<SourceDocumentDetailReconciliationParameters> parametersList) {
        long start = System.currentTimeMillis();
        Map<String, String> result = businessPaymentVoucherHandler.getCriticalMarkerV(parametersList);

        long end = System.currentTimeMillis();
        log.info("\ncriticalMarker processing time:{}ms\n", end - start);

        return result;
    }

    /**
     * stage two
     *
     * @param parametersList
     * @return
     */
    @Override
    public boolean validateSourceDocumentDetailList(List<SourceDocumentReconciliationParameters> parametersList) {
        if (CollectionUtils.isEmpty(parametersList)) {
            log.info("\nvalidateSourceDocumentDetailList size is 0\n");
            return false;
        }

        long totalStartTime = System.currentTimeMillis();
        String sourceDocumentUuid = parametersList.get(0).getSourceDocumentUuid();
        Boolean isDetailFile = parametersList.get(0).isDetaislFile();
        log.info("\nvalidateSourceDocumentDetailList of sourceDocumentUuid {}\nstart processing {} item\n",
                sourceDocumentUuid, parametersList.size());

        boolean isAllDetailsValid = true;
        SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
        Date cashFlowTransactionTime = sourceDocument == null ? null : sourceDocument.getOutlierTradeTime();

        long startTime;
        for (SourceDocumentReconciliationParameters parameter : parametersList) {
            startTime = System.currentTimeMillis();
            log.info("\nvalidateSourceDocumentDetail of sourceDocumentUuid {}\n",
                    sourceDocumentUuid);
            businessPaymentVoucherHandler.isDetailValid(parameter,
                    cashFlowTransactionTime,
                    isDetailFile);

            if (parameter.getSourceDocumentDetail().getCheckState() != SourceDocumentDetailCheckState.CHECK_SUCCESS) {
                isAllDetailsValid = false;
                log.info("\nvalidateSourceDocumentDetail of sourceDocumentUuid {}\nisAllDetailsValid is false\n",
                        sourceDocumentUuid);
            }

            log.info("\nvalidateSourceDocumentDetail of sourceDocumentUuid {}\nprocessing time:{}ms\n",
                    sourceDocumentUuid, System.currentTimeMillis() - startTime);
        }

        if (isAllDetailsValid && isDetailFile) {
            for (SourceDocumentReconciliationParameters parameter : parametersList) {
                SourceDocumentDetail detail = sourceDocumentDetailService.
                        getSourceDocumentDetail(parameter.getSourceDocumentDetailUuid());

                if (detail == null) {
                    detail = OrikaMapper.map(parameter.getSourceDocumentDetail(), SourceDocumentDetail.class);
                }

                log.info("checkState key is:{},ordinal is:{}",
                        detail.getCheckState().getKey(),
                        detail.getCheckState().ordinal());
                FastSourceDocumentDetail fastSourceDocumentDetail =
                        new FastSourceDocumentDetail(
                                detail.getUuid(),
                                detail.getSourceDocumentUuid(),
                                detail.getContractUniqueId(),
                                detail.getRepaymentPlanNo(),
                                detail.getAmount(), detail.getStatus().ordinal(),
                                detail.getFirstType(),
                                detail.getFirstNo(), detail.getSecondType(),
                                detail.getSecondNo(),
                                detail.getPayer().ordinal(),
                                detail.getReceivableAccountNo(),
                                detail.getPaymentAccountNo(),
                                detail.getPaymentName(),
                                detail.getPaymentBank(),
                                detail.getCheckState().ordinal(),
                                detail.getComment(),
                                detail.getFinancialContractUuid(),
                                detail.getPrincipal(),
                                detail.getInterest(),
                                detail.getServiceCharge(),
                                detail.getMaintenanceCharge(),
                                detail.getOtherCharge(),
                                detail.getPenaltyFee(),
                                detail.getLatePenalty(),
                                detail.getLateFee(),
                                detail.getLateOtherCost(),
                                detail.getVoucherUuid(),
                                detail.getActualPaymentTime(),
                                detail.getRepayScheduleNo(),
                                detail.getCurrentPeriod(),
                                detail.getOuterRepaymentPlanNo());
                try {
                    fastHandler.add(fastSourceDocumentDetail, true);
                } catch (GiottoException e) {
                    log.error(getStackTrace(e));
                }
            }
        }
        log.info("\nvalidateSourceDocumentDetailList of sourceDocumentUuid {}\n" +
                        "processed item {}\nprocessing time:{}ms\n",
                sourceDocumentUuid, parametersList.size(),
                System.currentTimeMillis() - totalStartTime);

        return isAllDetailsValid;
    }

    /**
     * stage three
     *
     * @param parametersList
     * @return
     */
    @Override
    public boolean fetchVirtualAccountAndBusinessPaymentVoucherTransfer(List<SourceDocumentReconciliationParameters> parametersList) {
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(parametersList) || parametersList.size() > 1) {
            return false;
        }

        SourceDocumentReconciliationParameters parameter = parametersList.get(0);

        int tryTimes = 3;
        while (tryTimes > 0) {
            try {
                SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(parameter.getSourceDocumentUuid());
                Voucher voucher = voucherService.getVoucherByUuid(sourceDocument.getVoucherUuid());
                LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(parameter.getLedgerBookNo());

                log.info("sourceDocument is:{}", sourceDocument.toString());
                log.info("voucher is:{}", voucher.toString());
                log.info("ledgerBook is:{}", ledgerBook.toString());

                return businessPaymentVoucherHandler.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
                        sourceDocument.getSourceDocumentUuid(), voucher.getDetailAmount(),
                        ledgerBook, sourceDocument.getFinancialContractUuid(), false) != null;
            } catch (VirtualAccountLockFailedException e) {
                log.error("fetch_virtual_account_and_business_payment_voucher_transfer end:cashFlowUuid, VirtualAccountLockFailedException, error:", ExceptionUtils.getStackTrace(e));
            }
            tryTimes--;
        }

        long end = System.currentTimeMillis();
        log.info("\nfetchVirtualAccountAndBusinessPayment processing time:{}ms\n", end - start);

        return false;
    }

    /**
     * stage fourth
     *
     * @param parametersList
     * @return
     */
    @Override
    public boolean sourceDocumentRecoverDetails(List<SourceDocumentReconciliationParameters> parametersList) throws JsonProcessingException {
        long start = System.currentTimeMillis();
        boolean flag = !CollectionUtils.isEmpty(parametersList) &&
                recoverSourceDocument(parametersList);
        long end = System.currentTimeMillis();
        log.info("\nsourceDocumentRecoverDetails processing time:{}ms\n", end - start);
        return flag;
    }

    /**
     * stage fifth
     *
     * @param parametersList
     * @return
     */
    @Override
    public boolean unfreezeCapital(List<SourceDocumentReconciliationParameters> parametersList) {
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(parametersList) && parametersList.size() > 1) {
            return false;
        }

        SourceDocumentReconciliationParameters parameters = parametersList.get(0);
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(parameters.getLedgerBookNo());

        boolean flag = businessPaymentVoucherHandler.unfreezeCapitalAmountOfVoucher(parameters.getSourceDocumentUuid(), parameters.getFinancialContractNo(), ledgerBook, "", SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

        long end = System.currentTimeMillis();
        log.info("\nunfreezeCapital processing time:{}ms\n", end - start);
        return flag;
    }

    private boolean recoverSourceDocument(List<SourceDocumentReconciliationParameters> parametersList) throws JsonProcessingException {
        boolean isAllDetailRecovered = true;
        int index = 0;
        String sourceDocumentDetailUuid;
        String sourceDocumentUuid;
        long start;
        long startOneChuck = System.currentTimeMillis();

        for (SourceDocumentReconciliationParameters parameters : parametersList) {
            start = System.currentTimeMillis();
            Map<String, Object> params = new HashMap<>();

            sourceDocumentUuid = parameters.getSourceDocumentUuid();
            sourceDocumentDetailUuid = parameters.getSourceDocumentDetailUuid();

            if (parameters.isDetaislFile()) {
                //从缓存读取
                FastSourceDocumentDetail fastSourceDocumentDetail = null;
                try {
                    fastSourceDocumentDetail = fastHandler.
                            getByKey(FastSourceDocumentDetailKeyEnum.UUID,
                                    sourceDocumentDetailUuid,
                                    FastSourceDocumentDetail.class,
                                    true);
                } catch (GiottoException e) {
                    log.error("\nrecover details, size:{},sourceDocumentDetailUuid:{},index:{}," +
                                    "sourceDocumentUuid[{}],stack trace[{}]\n",
                            parametersList.size(), sourceDocumentDetailUuid,
                            index, parameters.getSourceDocumentUuid(),
                            getStackTrace(e));
                    isAllDetailRecovered = false;
                }

                //缓存无数据，报错
                if (fastSourceDocumentDetail == null) {
                    log.error("\nget FastSourceDocumentDetail for cache is null sourceDocumentDetailUuid:{}\n",
                            sourceDocumentDetailUuid);
                    continue;
                }

                //同步到数据库
                saveSourceDocumentDetailForCache(fastSourceDocumentDetail);
            }

            params.put(PARAMS_SOURCE_DOCDUMENT_UUID, sourceDocumentUuid);
            params.put(PARAMS_SOURCE_DOCUMENT_DETAIL_UUID, sourceDocumentDetailUuid);

            log.info("\nbegin to recover single sourceDocumentDetail with sourceDocumentDetailUuid:[{}]," +
                    "sourceDocumentUuid[{}]\n", sourceDocumentDetailUuid, sourceDocumentUuid);
            log.info("\nsecondType:[{}]", parameters.getSecondType());

            Reconciliation reconciliation = Reconciliation.reconciliationFactory(parameters.getSecondType());
            reconciliation.accountReconciliation(params);

            log.info("\nend to recover single sourceDocumentDetail,sourceDocumentDetailUuid:[{}]," +
                            "index:[{}],use [{}]ms\n", sourceDocumentDetailUuid, index,
                    System.currentTimeMillis() - start);
            index++;
        }

        log.info("\n#compnesatory_recover_details#avgrecover use times[{}]ms,size[{}]\n",
                (System.currentTimeMillis() - startOneChuck) / (parametersList.size() * 1.0),
                parametersList.size());

        return isAllDetailRecovered;
    }

    private void saveSourceDocumentDetailForCache(FastSourceDocumentDetail fastSourceDocumentDetail) {
        try {
            SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(fastSourceDocumentDetail);
            sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
        } catch (Exception e) {
            log.error("exception message is:{}", getStackTrace(e));
        }
    }
}