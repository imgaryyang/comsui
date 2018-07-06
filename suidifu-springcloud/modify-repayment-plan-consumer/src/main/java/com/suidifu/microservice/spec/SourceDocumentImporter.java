package com.suidifu.microservice.spec;

import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.spec.ThirdPartVoucherSourceMapSpec;
import com.suidifu.owlman.microservice.enumation.SettlementModes;
import com.suidifu.owlman.microservice.enumation.SourceDocumentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import java.util.Date;
import java.util.UUID;

/**
 * @author louguanyang at 2018/3/14 11:50
 * @mail louguanyang@hzsuidifu.com
 */
public class SourceDocumentImporter {

    public static SourceDocument createRepaymentOrderDeductionVoucher(Company company, PaymentOrder paymentOrder,
        FastCustomer fastCustomer, String contractUuid, FinancialContract financialContract, String deductCashIdentity,
        Date paymentSucTime) {
        SourceDocument sourceDocument = new SourceDocument();
        createRepaymentDeductSourceDocument(sourceDocument, company == null ? null : company.getId(), paymentOrder,
            financialContract, deductCashIdentity, paymentSucTime);

        sourceDocument.fillBillInfo(financialContract.getFinancialContractUuid(), contractUuid);
        sourceDocument.fillPartyInfo(company, fastCustomer);
        return sourceDocument;
    }

    private static void createRepaymentDeductSourceDocument(SourceDocument srcDoc, Long companyId,
        PaymentOrder paymentOrder, FinancialContract financialContract, String deductCashIdentity,
        Date paymentSucTime) {
        srcDoc.setSourceDocumentUuid(UUID.randomUUID().toString());
        srcDoc.setSourceDocumentType(SourceDocumentType.NOTIFY);
        srcDoc.setCreateTime(new Date());
        srcDoc.setLastModifiedTime(new Date());
        srcDoc.setSourceDocumentStatus(SourceDocumentStatus.CREATE);
        srcDoc.setSourceAccountSide(AccountSide.DEBIT);
        srcDoc.setBookingAmount(paymentOrder.getAmount());
        srcDoc.setPlanBookingAmount(paymentOrder.getAmount());
        srcDoc.setAuditStatus(RepaymentAuditStatus.CREATE);
        srcDoc.setOutlierDocumentUuid(paymentOrder.getOutlierDocumentUuid());
        srcDoc.setOutlierTradeTime(paymentSucTime);
        srcDoc.setOutlierCounterPartyAccount(paymentOrder.getCounterAccountNo());
        srcDoc.setOutlierCounterPartyName(paymentOrder.getCounterAccountName());
        srcDoc.setOutlierAccount(financialContract.getCapitalAccount().getAccountNo());
        srcDoc.setOutlieAccountName(financialContract.getCapitalAccount().getAccountName());
        srcDoc.setOutlierAccountId(financialContract.getCapitalAccount().getId());
        srcDoc.setFinancialContractUuid(financialContract.getFinancialContractUuid());

        srcDoc.setOutlierCompanyId(companyId);
        //TODO 外部流水或者请求号标示
        srcDoc.setOutlierSerialGlobalIdentity(paymentOrder.getOutlierDocumentIdentity());
        srcDoc.setOutlierMemo(deductCashIdentity);
        srcDoc.setOutlierAmount(paymentOrder.getAmount());
        srcDoc.setOutlierSettlementModes(SettlementModes.REMITTANCE);
        srcDoc.setOutlierBreif("");
        srcDoc.setOutlierAccountSide(paymentOrder.getAccountSide());

        srcDoc.setCompanyId(companyId);
        srcDoc.setSourceDocumentNo(GeneratorUtils.generateDudectPlanVoucherNo());
        //外部单据单号及类型
        srcDoc.setFirstOutlierDocType(SourceDocument.FIRSTOUTLIER_PAYMENT_ORDER);
        srcDoc.setFirstAccountId(paymentOrder.getUuid());
        srcDoc.setSecondOutlierDocType(ThirdPartVoucherSourceMapSpec.payWayMapSpec.get(paymentOrder.getPayWay()));
        srcDoc.setSecondAccountId(paymentOrder.getOrderUuid());

    }
}
