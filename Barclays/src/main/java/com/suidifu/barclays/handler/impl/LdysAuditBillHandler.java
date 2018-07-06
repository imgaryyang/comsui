package com.suidifu.barclays.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullAuditBillException;
import com.suidifu.barclays.handler.AuditBillGenericHandler;
import com.suidifu.barclays.handler.AuditBillHandler;
import com.suidifu.coffer.entity.PullThirdPartyAuditBillModel;
import com.suidifu.coffer.entity.ThirdPartyAuditBillResultModel;
import com.suidifu.coffer.exception.UnSupportedException;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author penghk
 */
@Component("ldysAuditBillHandler")
public class LdysAuditBillHandler extends AuditBillGenericHandler implements AuditBillHandler {
    private static Logger logger = LoggerFactory.getLogger(LdysAuditBillHandler.class);


    @Autowired
    ThirdPartyPayHandler ldysHandler;

    @Autowired
    PaymentChannelInformationHandler paymentChannelInformationHandler;

    @Override
    public List<ThirdPartyAuditBill> execPullThirdPartyAuditBill(Map<String, String> workingParms) throws PullAuditBillException {
        if (null == workingParms) {
            throw new PullAuditBillException();
        }

        String merchantNo = workingParms.getOrDefault("merchantno", StringUtils.EMPTY);

        if (StringUtils.isEmpty(merchantNo)) {
            throw new PullAuditBillException();
        }

        try {
            int daysBefore = Integer.parseInt(workingParms.getOrDefault("daysBefore", "1"));
            String settleDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyyMMdd");
            String enddate = DateUtils.format(DateUtils.addDays(new Date(), -(daysBefore - 1)), "yyyyMMdd");
            PullThirdPartyAuditBillModel pullThirdPartyAuditBillModel = new PullThirdPartyAuditBillModel(merchantNo, settleDate);
            pullThirdPartyAuditBillModel.setEnddate(enddate);

            List<ThirdPartyAuditBillResultModel> auditBillModelList = ldysHandler.pullAuditBill(pullThirdPartyAuditBillModel, workingParms);

            Map<String, String> map = paymentChannelInformationHandler.getFinancialContractUuid(merchantNo);
            return super.transferToAuditBillEntity(auditBillModelList, map, PaymentInstitutionName.LDYS);
        } catch (UnSupportedException e) {
            logger.error("");
            e.printStackTrace();
            return new ArrayList<ThirdPartyAuditBill>();
        }
    }
}
