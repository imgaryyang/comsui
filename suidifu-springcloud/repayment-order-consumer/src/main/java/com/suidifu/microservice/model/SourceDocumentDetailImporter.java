package com.suidifu.microservice.model;

import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import java.math.BigDecimal;
import java.util.Map;

public class SourceDocumentDetailImporter {
    public static SourceDocumentDetail createSourceDocumentDetail(String sourceDocumentUuid,
                                                                  RepaymentOrderItem repaymentOrderItem,
                                                                  PaymentOrder paymentOrder,
                                                                  Map<String, BigDecimal> detailAmount,
                                                                  String repaymentPlanNo) {
        SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(sourceDocumentUuid,
                repaymentOrderItem.getContractUniqueId(),
                repaymentPlanNo,
                repaymentOrderItem.getAmount(),
                SourceDocumentDetailStatus.UNSUCCESS,
                VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(),
                repaymentOrderItem.getOrderUuid(),
                VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(),
                repaymentOrderItem.getOrderDetailUuid(),
                VoucherPayer.LOANER,
                paymentOrder.getHostAccountNo(),
                paymentOrder.getCounterAccountNo(),
                paymentOrder.getCounterAccountName(),
                paymentOrder.getCounterAccountBankName(),
                repaymentOrderItem.getFinancialContractUuid());
        sourceDocumentDetail.setActualPaymentTime(repaymentOrderItem.getRepaymentPlanTime());
        sourceDocumentDetail.setDetailAmount(detailAmount);

        return sourceDocumentDetail;
    }
}