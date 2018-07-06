package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.SourceDocumentHandler;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.spec.SourceDocumentImporter;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author louguanyang at 2018/3/14 11:49
 * @mail louguanyang@hzsuidifu.com
 */
@Component("sourceDocumentHandler")
public class SourceDocumentHandlerImpl implements SourceDocumentHandler {

    @Resource
    private DeductPlanService deductPlanService;
    @Resource
    private FastHandler fastHandler;
    @Resource
    private SourceDocumentService sourceDocumentService;

    @Override
    public SourceDocument createUnSignedDeductRepaymentOrderSourceDocument(String customerUuid, FinancialContract financialContract, RepaymentOrder repaymentOrder, PaymentOrder paymentOrder) throws GiottoException {
        List<DeductPlan> deductPlanList = deductPlanService.getDedcutPlanListByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid(), DeductApplicationExecutionStatus.SUCCESS);
        String deductCashIdentity = deductPlanList.size()>1?"":deductPlanList.get(0).getDeductCashIdentity();
        FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID,customerUuid,FastCustomer.class,true);
        String contractUuid=repaymentOrder.getFirstContractUuid();
        Date paymentSucTime = deductPlanService.getMaxPaymentSucTime(paymentOrder.getOutlierDocumentUuid());
        SourceDocument sourceDocument = SourceDocumentImporter
            .createRepaymentOrderDeductionVoucher(financialContract.getCompany(), paymentOrder, fastCustomer, contractUuid, financialContract, deductCashIdentity, paymentSucTime);
        sourceDocumentService.save(sourceDocument);
        return sourceDocument;
    }
}
