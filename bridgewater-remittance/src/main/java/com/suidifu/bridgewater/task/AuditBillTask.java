package com.suidifu.bridgewater.task;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.bridgewater.api.model.BasicTask;
import com.suidifu.bridgewater.handler.RemittanceAuditBillHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("auditBillTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuditBillTask extends BasicTask {

	private static Log logger = LogFactory.getLog(AuditBillTask.class);

	private final static String PARAM_FINANCIAL_CONTRACT_UUID = "financialContractUuid";
	private final static String PARAM_DAYS_BEFORE = "daysBefore";

	@Autowired
	RemittanceAuditBillHandler remittanceAuditBillHandler;

	@Override
	public void run() {
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.GENERATE_REMITTANCE_AUDIT_BILL + "[BEGIN]");

		String financialContractUuid = getWorkParam(PARAM_FINANCIAL_CONTRACT_UUID);
		String daysBeforeStr = getWorkParam(PARAM_DAYS_BEFORE);
		try {

			Date settleDate = DateUtils.addDays(new Date(), -1);
			if (StringUtils.isNotEmpty(daysBeforeStr)) {
				int daysBefore = Integer.parseInt(daysBeforeStr);
				settleDate = DateUtils.addDays(new Date(), -daysBefore);
			}

			remittanceAuditBillHandler.generateAuditBill(financialContractUuid, settleDate);

			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.GENERATE_REMITTANCE_AUDIT_BILL + "[END]");

		} catch (Exception e) {
			logger.error("#对账单生成失败，financialContractUuid［" + financialContractUuid + "］！");
			e.printStackTrace();
		}
	}
}
