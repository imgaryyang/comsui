package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.SourceDocumentHandler;
import com.suidifu.microservice.model.SourceDocumentImporter;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("sourceDocumentHandler")
public class SourceDocumentHandlerImpl implements SourceDocumentHandler {
    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private DeductPlanService deductPlanService;
    @Resource
    private VirtualAccountService virtualAccountService;
    @Resource
    private FastHandler fastHandler;

    @Override
    public SourceDocument createUnSignedDeductRepaymentOrderSourceDocument(String customerUuid,
                                                                           FinancialContract financialContract,
                                                                           RepaymentOrder repaymentOrder,
                                                                           PaymentOrder paymentOrder)
            throws GiottoException {
        List<DeductPlan> deductPlanList = deductPlanService.getDedcutPlanListByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid(), DeductApplicationExecutionStatus.SUCCESS);
        String deductCashIdentity = deductPlanList.size() > 1 ? "" : deductPlanList.get(0).getDeductCashIdentity();
        FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, customerUuid, FastCustomer.class, true);
        String contractUuid = repaymentOrder.getFirstContractUuid();
        Date paymentSucTime = deductPlanService.getMaxPaymentSucTime(paymentOrder.getOutlierDocumentUuid());
        SourceDocument sourceDocument = SourceDocumentImporter.createRepaymentOrderDeductionVoucher(financialContract.getCompany(), paymentOrder, fastCustomer, contractUuid, financialContract, deductCashIdentity, paymentSucTime);
        sourceDocumentService.save(sourceDocument);
        return sourceDocument;
    }

    @Override
    public SourceDocument createDepositeReceipt(CashFlow cashFlow, Long companyId, BigDecimal amount, Customer customer,
                                                String relatedContractUuid, String financialContractUuid, String virtualAccountNo, String remark) {
        VirtualAccount virtualAccount = virtualAccountService
                .getVirtualAccountByCustomerUuid(customer.getCustomerUuid());
        SourceDocument sourceDocument = SourceDocumentImporter.createDepositReceipt(companyId, cashFlow, customer,
                virtualAccount == null ? "" : virtualAccount.getVirtualAccountUuid(), relatedContractUuid,
                financialContractUuid, virtualAccountNo, remark, amount);
        sourceDocumentService.save(sourceDocument);
        return sourceDocument;
    }
}