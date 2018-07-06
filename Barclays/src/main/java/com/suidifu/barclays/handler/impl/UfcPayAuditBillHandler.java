package com.suidifu.barclays.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullAuditBillException;
import com.suidifu.barclays.handler.AuditBillGenericHandler;
import com.suidifu.barclays.handler.AuditBillHandler;
import com.suidifu.coffer.entity.PullThirdPartyAuditBillModel;
import com.suidifu.coffer.entity.ThirdPartyAuditBillResultModel;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/10/20.
 */

@Component("ufcPayAuditBillHandler")
public class UfcPayAuditBillHandler extends AuditBillGenericHandler implements AuditBillHandler {

    @Autowired
    @Qualifier("ucfPayHandler")
    ThirdPartyPayHandler ucfPayHandler;

    @Autowired
    PaymentChannelInformationHandler paymentChannelInformationHandler;

    @Override
    public List<ThirdPartyAuditBill> execPullThirdPartyAuditBill(Map<String, String> workingParms) throws PullAuditBillException {
        if(null == workingParms) {
            throw new PullAuditBillException();
        }

        String merchantNo = workingParms.getOrDefault("memberId", StringUtils.EMPTY);

        if(StringUtils.isEmpty(merchantNo)) {
            throw new PullAuditBillException();
        }

        try{

            int daysBefore = Integer.parseInt(workingParms.getOrDefault("daysBefore", "1"));
            String settleDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyyMMdd");
            String filePathDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "/yyyy/MM/");
            workingParms.put("filePathDate", filePathDate);

            PullThirdPartyAuditBillModel pullThirdPartyAuditBillModel = new PullThirdPartyAuditBillModel(merchantNo, settleDate);

            List<ThirdPartyAuditBillResultModel> auditBillModelList = ucfPayHandler.pullAuditBill(pullThirdPartyAuditBillModel, workingParms);

            Map<String, String> map = paymentChannelInformationHandler.getFinancialContractUuid(merchantNo);

            return super.transferToAuditBillEntity(auditBillModelList, map, PaymentInstitutionName.UCF_PAY);
        }catch (Exception e){
            e.printStackTrace();
            return  new ArrayList<>();
        }

    }
}
