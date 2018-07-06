package com.suidifu.barclays.handler;

import com.suidifu.coffer.entity.ThirdPartyAuditBillResultModel;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by qinweichao on 2017/10/20.
 */
public class AuditBillGenericHandler {

    public List<ThirdPartyAuditBill> transferToAuditBillEntity(
            List<ThirdPartyAuditBillResultModel> auditBillModelList, Map<String, String> map,
            PaymentInstitutionName paymentInstitutionName) {
        List<ThirdPartyAuditBill> auditBillList = new ArrayList<ThirdPartyAuditBill>();

        for(ThirdPartyAuditBillResultModel auditBillModel : auditBillModelList) {

            ThirdPartyAuditBill thirdPartyAuditBill = new ThirdPartyAuditBill();

            thirdPartyAuditBill.setMerchantNo(auditBillModel.getMerchantNo());
            thirdPartyAuditBill.setSndLvlMerchantNo(auditBillModel.getSndLvlMerchantNo());

            AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(auditBillModel.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
            thirdPartyAuditBill.setAccountSide(accountSide);

            if (StringUtils.isNotEmpty(auditBillModel.getReckonAccount())) {
                thirdPartyAuditBill.setFinancialContractUuid(map.get(auditBillModel.getReckonAccount()));
            }else {
                thirdPartyAuditBill.setFinancialContractUuid(map.get("noClearingNo"));
            }
            thirdPartyAuditBill.setReckonAccount(auditBillModel.getReckonAccount());
            thirdPartyAuditBill.setAuditBillUuid(UUID.randomUUID().toString());
            thirdPartyAuditBill.setAuditStatus(AuditStatus.CREATE);
            thirdPartyAuditBill.setChannelSequenceNo(auditBillModel.getChannelSequenceNo());
            thirdPartyAuditBill.setCounterAccountNo(auditBillModel.getCounterAccountNo());
            thirdPartyAuditBill.setCounterAccountName(auditBillModel.getCounterAccountName());
            thirdPartyAuditBill.setCounterAccountAppendix(auditBillModel.getCounterAccountAppendix());
            thirdPartyAuditBill.setCreateTime(new Date());
            thirdPartyAuditBill.setIssuedAmount(BigDecimal.ZERO);
            thirdPartyAuditBill.setMerchantOrderNo(auditBillModel.getMerchantOrderNo());
            thirdPartyAuditBill.setBatchNo(auditBillModel.getBatchNo());
            thirdPartyAuditBill.setOrderCreateTime(auditBillModel.getOrderCreateTime());
            thirdPartyAuditBill.setPaymentGateway(paymentInstitutionName);
            thirdPartyAuditBill.setRemark(auditBillModel.getRemark());
            thirdPartyAuditBill.setServiceFee(auditBillModel.getServiceFee());
            thirdPartyAuditBill.setSettleDate(auditBillModel.getSettleDate());
            thirdPartyAuditBill.setTransactionAmount(auditBillModel.getTransactionAmount());
            thirdPartyAuditBill.setTransactionTime(auditBillModel.getTransactionTime());

            auditBillList.add(thirdPartyAuditBill);
        }
        return auditBillList;
    }

}
