package com.suidifu.barclays.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullAuditBillException;
import com.suidifu.barclays.handler.AuditBillGenericHandler;
import com.suidifu.barclays.handler.AuditBillHandler;
import com.suidifu.coffer.entity.PullThirdPartyAuditBillModel;
import com.suidifu.coffer.entity.ThirdPartyAuditBillResultModel;
import com.suidifu.coffer.exception.UnSupportedException;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by qinweichao on 2017/8/24.
 */
@Component("yeePayAuditBillHandler")
public class YeePayAuditBillHandler extends AuditBillGenericHandler implements AuditBillHandler {

    @Autowired
    ThirdPartyPayHandler yeePayHandler;
    @Autowired
    PaymentChannelInformationHandler paymentChannelInformationHandler;

    @Override
    public List<ThirdPartyAuditBill> execPullThirdPartyAuditBill(Map<String, String> workingParms)
            throws PullAuditBillException {
        if (null == workingParms) {
            throw new PullAuditBillException();
        }

        String merchantNo = workingParms.getOrDefault("merchantno", StringUtils.EMPTY);

        if (StringUtils.isEmpty(merchantNo)) {
            throw new PullAuditBillException();
        }

        try {

            int daysBefore = Integer.parseInt(workingParms.getOrDefault("daysBefore", "1"));
            String settleDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyy-MM-dd");
            String enddate = DateUtils.format(DateUtils.addDays(new Date(), -(daysBefore-1)), "yyyy-MM-dd");
            PullThirdPartyAuditBillModel pullThirdPartyAuditBillModel = new PullThirdPartyAuditBillModel(merchantNo, settleDate);
            pullThirdPartyAuditBillModel.setEnddate(enddate);

            List<ThirdPartyAuditBillResultModel> auditBillModelList = yeePayHandler.pullAuditBill(pullThirdPartyAuditBillModel, workingParms);

            Map<String, String> map = paymentChannelInformationHandler.getFinancialContractUuid(merchantNo);

            return super.transferToAuditBillEntity(auditBillModelList, map, PaymentInstitutionName.YEE_PAY);
        } catch (UnSupportedException e) {
            e.printStackTrace();
            return new ArrayList<ThirdPartyAuditBill>();
        }

    }

}
