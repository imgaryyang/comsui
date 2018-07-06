package com.suidifu.barclays.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.suidifu.barclays.handler.AuditBillGenericHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullAuditBillException;
import com.suidifu.barclays.handler.AuditBillHandler;
import com.suidifu.coffer.entity.PullThirdPartyAuditBillModel;
import com.suidifu.coffer.entity.ThirdPartyAuditBillResultModel;
import com.suidifu.coffer.handler.sxccb.SxCcbHandler;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;

@Component("sxCcbAuditBillHandler")
public class SxCcbAuditBillHandler extends AuditBillGenericHandler implements AuditBillHandler {
	
	@Autowired
	SxCcbHandler sxCcbHandler;
	@Autowired
	PaymentChannelInformationHandler paymentChannelInformationHandler;

	@Override
	public List<ThirdPartyAuditBill> execPullThirdPartyAuditBill(Map<String, String> workingParms)
			throws PullAuditBillException {
		if(null == workingParms) {
			throw new PullAuditBillException();
		}
		
		String merchantNo = workingParms.getOrDefault("userName", StringUtils.EMPTY);
		
		if(StringUtils.isEmpty(merchantNo)) {
			throw new PullAuditBillException();
		}

		try {

			int daysBefore = Integer.parseInt(workingParms.getOrDefault("daysBefore", "1"));
			String settleDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyyMMdd");

			PullThirdPartyAuditBillModel pullThirdPartyAuditBillModel = new PullThirdPartyAuditBillModel(merchantNo, settleDate, new Date());

			List<ThirdPartyAuditBillResultModel> auditBillModelList = sxCcbHandler.pullAuditBill(pullThirdPartyAuditBillModel, workingParms);
			Map<String, String> map = paymentChannelInformationHandler.getFinancialContractUuid(merchantNo);
			return  super.transferToAuditBillEntity(auditBillModelList, map, PaymentInstitutionName.JIANHANG);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ThirdPartyAuditBill>();
		}

	}

}
