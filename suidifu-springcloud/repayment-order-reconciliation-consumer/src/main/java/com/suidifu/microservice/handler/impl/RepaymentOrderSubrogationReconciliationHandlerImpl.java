package com.suidifu.microservice.handler.impl;

import static com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_ITEM_UUID;
import static com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_UUID;
import static com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_UNIQUE_CASH_IDENTITY;
import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE;

import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.reconciliation.ReconciliationRepayment;
import com.suidifu.owlman.microservice.handler.RepaymentOrderReconciliationHandler;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("repaymentOrderReconciliation")
public class RepaymentOrderSubrogationReconciliationHandlerImpl implements RepaymentOrderReconciliationHandler {
    @Resource
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private RepaymentOrderItemService repaymentOrderItemService;
    @Resource
    private RepaymentOrderService repaymentOrderService;

    /**
     * stage 1
     *
     * @param list
     * @return
     */
    @Override
    public boolean fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
            List<RepaymentOrderReconciliationParameters> list) {
        log.info("fetchVirtualAccountAndBusinessPaymentVoucherTransfer begin");
        if (CollectionUtils.isEmpty(list) || list.size() > 1) {
            log.info("未进入fetchVirtualAccountAndBusinessPaymentVoucherTransfer");
            return false;
        }
        RepaymentOrderReconciliationParameters parameters = list.get(0);

        RepaymentOrder repaymentOrder = repaymentOrderService.
                getRepaymentOrderByUuid(parameters.getRepaymentOrderUuid());
        LedgerBook ledgerBook = ledgerBookService.
                getBookByBookNo(parameters.getLedgerBookNo());

        return businessPaymentVoucherHandler.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
                repaymentOrder.getOrderUuid(), repaymentOrder.getOrderAmount(), ledgerBook,
                repaymentOrder.getFinancialContractUuid(), true) != null;
    }

    private boolean recoverRepaymentOrder(List<RepaymentOrderReconciliationParameters> parametersList) {
        boolean isAllDetailRecovered = true;
        int index = 0;
        String repaymentOrderItemUuid = "";
        String repaymentOrderUuid;
        long startOneChunk = System.currentTimeMillis();

        for (RepaymentOrderReconciliationParameters parameters : parametersList) {
            try {
                long start = System.currentTimeMillis();

                Map<String, Object> params = new HashMap<>();
                repaymentOrderItemUuid = parameters.getRepaymentOrderItemUuid();
                RepaymentOrderItem repaymentOrderItem = repaymentOrderItemService.
                        getRepaymentOrderItemByUuid(repaymentOrderItemUuid);

                if (repaymentOrderItem == null || !repaymentOrderItem.isValidAndUnpaid()) {
                    log.info("repaymentOrderItem is NUll or PayStatus is not UnPaid," +
                                    "id[{}]repaymentOrderUuid[{}],index[{}]",
                            repaymentOrderItemUuid, parameters.getRepaymentOrderUuid(), index);
                    continue;
                }

                String secondType = repaymentOrderItem.getRepaymentWay().getKey();
                repaymentOrderUuid = parameters.getRepaymentOrderUuid();
                params.put(PARAMS_REPAYMENT_ORDER_ITEM_UUID, repaymentOrderItemUuid);
                params.put(PARAMS_REPAYMENT_ORDER_UUID, repaymentOrderUuid);
                params.put(PARAMS_UNIQUE_CASH_IDENTITY, parameters.getUniqueCashIdentity());

                log.info("begin to recover single repaymentOrderItem with " +
                                "repaymentOrderItemUuid[{}]repaymentOrderUuid[{}]",
                        repaymentOrderItemUuid, parameters.getRepaymentOrderUuid());

                ReconciliationRepayment reconciliation = ReconciliationRepayment.reconciliationFactory(secondType);
                reconciliation.accountReconciliation(params);

                log.info("end to recover single repaymentOrderItem,id[{}],repaymentOrderUuid[{}]," +
                                "index[{}], use times[{}]ms",
                        repaymentOrderItemUuid, parameters.getRepaymentOrderUuid(),
                        index, System.currentTimeMillis() - start);

                index++;
            } catch (Exception e) {
                log.error("recover details, size:{},repaymentOrderItemUuid:{}," +
                                "index:{},repaymentOrderUuid[{}],stack trace[{}]",
                        parametersList.size(), repaymentOrderItemUuid, index,
                        parameters.getRepaymentOrderUuid(), ExceptionUtils.getStackTrace(e));
                org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e);
                isAllDetailRecovered = false;
            }

        }
        double avgUsage = (System.currentTimeMillis() - startOneChunk) / (parametersList.size() * 1.0);

        log.info("#compnesatory_recover_details#avgrecover use times[{}]ms  size[{}]",
                avgUsage, parametersList.size());

        return isAllDetailRecovered;
    }

    @Override
    public boolean repaymentOrderRecoverDetails(List<RepaymentOrderReconciliationParameters> sourceDocumentReconciliationStepThreeParameterList) {
        log.info("repaymentOrderRecoverDetails begin");
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(sourceDocumentReconciliationStepThreeParameterList)) return false;
        RepaymentOrderReconciliationParameters param = sourceDocumentReconciliationStepThreeParameterList.get(0);
        log.info("recoverRepaymentOrder used: {}ms", System.currentTimeMillis() - start);
        return param != null && recoverRepaymentOrder(sourceDocumentReconciliationStepThreeParameterList);
    }

    @Override
    public boolean unfreezeCapital(List<RepaymentOrderReconciliationParameters> parametersList) {
        if (CollectionUtils.isEmpty(parametersList) || parametersList.size() > 1) {
            return false;
        }
        RepaymentOrderReconciliationParameters parameters = parametersList.get(0);

        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(parameters.getLedgerBookNo());

        return businessPaymentVoucherHandler.unfreezeCapitalAmountOfVoucher(parameters.getRepaymentOrderUuid(),
                parameters.getFinancialContractNo(), ledgerBook, "",
                SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);
    }
}