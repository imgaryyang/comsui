package com.suidifu.pricewaterhouse.yunxin.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.pricewaterhouse.yunxin.handler.ThirdPartyDeductNoSessionV2_0;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.yunxin.entity.BusinessTaskMessage;
import com.zufangbao.sun.yunxin.service.BusinessTaskMessageService;
import com.zufangbao.sun.yunxin.service.audit.RemittanceAuditResultService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import com.zufangbao.wellsfargo.yunxin.handler.BusinessTaskMessageHandler;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;
import com.zufangbao.wellsfargo.yunxin.handler.vouchertask.ActivePaymentVoucherNoSession;

@Component("systemDeductTaskV2_0")
public class SystemDeductTaskV2_0 {

	@Autowired
	private ThirdPartyDeductNoSessionV2_0 thirdPartyDeductNoSessionV2_0;
	@Autowired
	private ActivePaymentVoucherNoSession activePaymentVoucherNoSession;
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;
	@Autowired
	private RemittanceAuditResultService remittanceAuditResultService;
	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private BusinessTaskMessageService businessTaskMessageService;
	@Autowired
	private BusinessTaskMessageHandler businessTaskMessageHandler;
	
	private static Log logger = LogFactory.getLog(SystemDeductTaskV2_0.class);

	@Value("#{config['sys.deduct.merId']}")
	private String MER_ID = "";

	@Value("#{config['sys.deduct.secret']}")
	private String SECRET = "";

	public void thirdPartyDeductionAssetRecoverV2() {
		try {
			thirdPartyDeductNoSessionV2_0.loopGenerateVoucherWithReconciliation();
		} catch (Exception e) {
			logger.error("occur exception when gengerate third part journal voucher and reconciliation");
			e.printStackTrace();
		}
		
		active_payment_voucher_recover();
		
		handler_cash_flow_recharge_and_active_voucher_status_refresh();
		
		createRemittanceVoucher();
	}
	
	private void active_payment_voucher_recover(){
		long start = System.currentTimeMillis();
		try {
			logger.debug("begin to active_payment_voucher_recover!");
			activePaymentVoucherNoSession.scan_and_pay_by_active_payment_voucher();
		} catch(Exception e){
			logger.error("occur error active_payment_voucher_recover.");
			e.printStackTrace();
		}
		logger.debug("end to active_payment_voucher_recover! use times["+(System.currentTimeMillis()-start)+"]ms");
	}
	
	private void handler_cash_flow_recharge_and_active_voucher_status_refresh(){
		long start = System.currentTimeMillis();
		try {
			logger.info("begin to handler_cash_flow_recharge_and_active_voucher_status_refresh!");
			List<BusinessTaskMessage> cashFlowMessgeList = businessTaskMessageService
					.get_prepare_task_message();
			for (BusinessTaskMessage businessTaskMessage : cashFlowMessgeList) {
				try{
					businessTaskMessageHandler.refresh_cash_flow_or_voucher(businessTaskMessage.getFstContractUuid(), Priority.High.getPriority(),
							businessTaskMessage.getUuid());
				} catch(Exception e){
					logger.error("error,businessTaskMessageUuid["+businessTaskMessage.getUuid()+"]:"+ExceptionUtils.getFullStackTrace(e));
				}
			}
		} catch(Exception e){
			logger.error("occur error handler_cash_flow_recharge_and_active_voucher_status_refresh.");
			e.printStackTrace();
		}
		logger.info("end to handler_cash_flow_recharge_and_active_voucher_status_refresh! use times["+(System.currentTimeMillis()-start)+"]ms");
	}
	private void createRemittanceVoucher() {

		long start = System.currentTimeMillis();

		try {
			logger.info("begin to autoCreateRemittanceVoucher!");
			List<Map<String, Object>> remittanceAuditResults = remittanceAuditResultService
					.getUnCreateVoucherOfRemittanceAuditResultList();
			if (CollectionUtils.isEmpty(remittanceAuditResults))
				return;
			for (Map<String, Object> remittanceAuditResult : remittanceAuditResults) {
				if(MapUtils.isEmpty(remittanceAuditResult))
					continue;
				sourceDocumentHandler.createRemittanceSourceDocument((String)remittanceAuditResult.get("financialContractUuid"),
						Priority.High.ordinal(), (String)remittanceAuditResult.get("redisKey"));
			}
		} catch (Exception e) {
			logger.error(
					"occur error autoCreateRemittanceVoucher. FullStackTrace:" + ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to autoCreateRemittanceVoucher! use times[" + (System.currentTimeMillis() - start) + "]ms");
	}
}


