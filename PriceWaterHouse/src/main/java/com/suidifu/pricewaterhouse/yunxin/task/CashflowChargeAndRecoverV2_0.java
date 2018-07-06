package com.suidifu.pricewaterhouse.yunxin.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.hathaway.job.Priority;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.CashFlowQueryModel;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.AutoRecoverAssesNoSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowChargeProxy;

/**
 * 
 * @author wukai
 * 余额销账 关闭
 *
 */
//@Component("cashflowChargeAndRecoverV2_0")
public class CashflowChargeAndRecoverV2_0 {
	
	private static Log logger = LogFactory.getLog(CashflowChargeAndRecoverV2_0.class);
	
	@Autowired
	private AutoRecoverAssesNoSession autoRecoverAssesNoSession;
	
	/**
	 *  余额核销 关闭
	 */
	private void scan_cashflow_charge_virtual_account_auto_recover(){
		try {
			logger.info("autoRecoverAssets start");
			autoRecoverAssesNoSession.auto_recover_assets_and_guarantess_by_virtual_account();
		} catch(Exception e){
			logger.error("autoRecoverAssets error");
			e.printStackTrace();
		}
		logger.info("autoRecoverAssets end");
	}
}
