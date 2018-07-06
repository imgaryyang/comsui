package com.suidifu.owlman.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by qinweichao on 2017/12/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDocumentDetailReconciliationParameters {
    private String sourceDocumentDetailUuid;

    private String contractUniqueId;

    private String financialContractUuid;

      //    bankTransactionNo=UUID.randomUUID().toString()&detail=[{"amount":1000,"interest":0,"lateFee":0,"lateOtherCost":0,"latePenalty":0,"maintenanceCharge":0,"otherCharge":0,"payer":1,"penaltyFee":0,"principal":1000,"repaymentPlanNo":"ZC168444895007657984","serviceCharge":0,"transactionTime":"","uniqueId":" test_0313"}]&financialContractNo=G31700&fn=300003&paymentAccountNo=1001133419006708190&paymentBank=宁波银行&paymentName=上海拍拍贷金融信息服务有限公司&receivableAccountNo=600000000001&requestNo=UUID.randomUUID().toString()&transactionType=0&voucherAmount=1000&voucherType=7
}