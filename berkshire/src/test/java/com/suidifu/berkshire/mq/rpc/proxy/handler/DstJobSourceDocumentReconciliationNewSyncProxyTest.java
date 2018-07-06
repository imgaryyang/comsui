package com.suidifu.berkshire.mq.rpc.proxy.handler;

import com.suidifu.berkshire.utils.OrikaMapper;
import com.suidifu.owlman.microservice.model.SourceDocumentDetail;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/14 <br>
 * @time: 13:05 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Log4j2
public class DstJobSourceDocumentReconciliationNewSyncProxyTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void criticalMarker() {
    }

    @Test
    public void validateSourceDocumentDetailList() {
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransfer() {
    }

    @Test
    public void sourceDocumentRecoverDetails() {
    }

    @Test
    public void unfreezeCapital() {
    }

    //    bankTransactionNo=UUID.randomUUID().toString()&detail=[{"amount":1000,"interest":0,"lateFee":0,"lateOtherCost":0,"latePenalty":0,"maintenanceCharge":0,"otherCharge":0,"payer":1,"penaltyFee":0,"principal":1000,"repaymentPlanNo":"ZC168444895007657984","serviceCharge":0,"transactionTime":"","uniqueId":" test_0313"}]&financialContractNo=G31700&fn=300003&paymentAccountNo=1001133419006708190&paymentBank=宁波银行&paymentName=上海拍拍贷金融信息服务有限公司&receivableAccountNo=600000000001&requestNo=UUID.randomUUID().toString()&transactionType=0&voucherAmount=1000&voucherType=7

    @Test
    public void copyBean() {
        List<com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters> from = new ArrayList<>();
        com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters fromElement = new com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters();

        fromElement.setFinancialContractNo("G31700");
        fromElement.setSourceDocumentUuid(UUID.randomUUID().toString());
        fromElement.setDetaislFile(true);

        SourceDocumentDetail sourceDocumentDetail =
                new SourceDocumentDetail();
        sourceDocumentDetail.setAmount(new BigDecimal("1000"));
        sourceDocumentDetail.setInterest(new BigDecimal("0"));
        sourceDocumentDetail.setLateFee(new BigDecimal("0"));
        sourceDocumentDetail.setLateOtherCost(new BigDecimal("0"));
        sourceDocumentDetail.setLatePenalty(new BigDecimal("0"));
        sourceDocumentDetail.setMaintenanceCharge(new BigDecimal("0"));
        sourceDocumentDetail.setOtherCharge(new BigDecimal("0"));
        sourceDocumentDetail.setPayer(VoucherPayer.fromValue(1));
        sourceDocumentDetail.setPenaltyFee(new BigDecimal("0"));
        sourceDocumentDetail.setPrincipal(new BigDecimal("1000"));
        sourceDocumentDetail.setRepaymentPlanNo("ZC168444895007657984");
        sourceDocumentDetail.setServiceCharge(new BigDecimal("0"));
        sourceDocumentDetail.setContractUniqueId("test_0313");
        sourceDocumentDetail.setPaymentBank("宁波银行");
        sourceDocumentDetail.setPaymentAccountNo("1001133419006708190");
        sourceDocumentDetail.setPaymentName("上海拍拍贷金融信息服务有限公司");
        sourceDocumentDetail.setReceivableAccountNo("600000000001");
        fromElement.setSourceDocumentDetail(sourceDocumentDetail);
        from.add(fromElement);

        List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters>
                to = OrikaMapper.mapAsList(from,
                com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters.class);

//        [{"amount":1000,"interest":0,"lateFee":0,"lateOtherCost":0,"latePenalty":0,"maintenanceCharge":0,"otherCharge":0,"payer":1,"penaltyFee":0,"principal":1000,"repaymentPlanNo":"ZC168444895007657984","serviceCharge":0,"transactionTime":"","uniqueId":" test_0313"}]
        for (com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters element : to) {
            log.info("uuid is:{}", element.getSourceDocumentUuid());
            log.info("detail uuid is:{}", element.getSourceDocumentDetailUuid());
            log.info("detail uuid is:{}", element.getSourceDocumentDetail().getUuid());
            log.info("amount is:{}", element.getSourceDocumentDetail().getAmount());
            log.info("interest is:{}", element.getSourceDocumentDetail().getInterest());
            log.info("lateFee is:{}", element.getSourceDocumentDetail().getLateFee());
            log.info("lateOtherCost is:{}", element.getSourceDocumentDetail().getLateOtherCost());
            log.info("latePenalty is:{}", element.getSourceDocumentDetail().getLatePenalty());
            log.info("maintenanceCharge is:{}", element.getSourceDocumentDetail().getMaintenanceCharge());
            log.info("otherCharge is:{}", element.getSourceDocumentDetail().getOtherCharge());
            log.info("payer is:{}", element.getSourceDocumentDetail().getPayer().ordinal());
            log.info("penaltyFee is:{}", element.getSourceDocumentDetail().getPenaltyFee());
            log.info("principal is:{}", element.getSourceDocumentDetail().getPrincipal());
            log.info("repaymentPlanNo is:{}", element.getSourceDocumentDetail().getRepaymentPlanNo());
            log.info("serviceCharge is:{}", element.getSourceDocumentDetail().getServiceCharge());
            log.info("uniqueId is:{}", element.getSourceDocumentDetail().getContractUniqueId());
            log.info("transactionTime is:{}", element.getSourceDocumentDetail().getActualPaymentTime());
            log.info("paymentBank is:{}", element.getSourceDocumentDetail().getPaymentBank());
            log.info("paymentAccountNo is:{}", element.getSourceDocumentDetail().getPaymentAccountNo());
            log.info("paymentName is:{}", element.getSourceDocumentDetail().getPaymentName());
            log.info("receivableAccountNo is:{}", element.getSourceDocumentDetail().getReceivableAccountNo());
        }
    }
}