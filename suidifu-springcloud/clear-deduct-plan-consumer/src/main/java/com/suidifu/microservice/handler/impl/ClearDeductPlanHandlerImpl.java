package com.suidifu.microservice.handler.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.suidifu.microservice.entity.Reconciliation;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.handler.ClearDeductPlanHandler;
import com.suidifu.owlman.microservice.spec.ReconciliationParameterNameSpace.ReconciliationForClearingVoucher;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.utils.ClearingVoucherParameters;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;

@Component("clearDeductPlanHandlerProxy")
public class ClearDeductPlanHandlerImpl implements ClearDeductPlanHandler {

	private static Logger logger = LoggerFactory.getLogger(ClearDeductPlanHandlerImpl.class);

	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private AuditJobService auditJobService;

	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;

	@Autowired
	private SourceDocumentService sourceDocumentService;

	@Autowired
	private CashFlowService cashFlowService;


	@Override
	public Map<String, String> criticalMarker(List<String> deductPlanUuidList) {
		if (CollectionUtils.isEmpty(deductPlanUuidList)) {
			logger.info("#sychronizedMarker# deductPlanUuid size is 0");
			return Collections.emptyMap();
		}
		return deductPlanService.getDeductPlanWithContractUuidMap(deductPlanUuidList);
	}

	@Override
	public boolean reconciliationClearingDeductPlan(List<ClearingVoucherParameters> clearingVoucherParametersList) {
		if (CollectionUtils.isEmpty(clearingVoucherParametersList))
			return false;
		AuditJob auditJob = auditJobService.getAuditJob(clearingVoucherParametersList.get(0).getAuditJobUuid());
		TotalReceivableBills totalReceivableBills = totalReceivableBillsService
				.getTotalReceivableBillsByUuid(clearingVoucherParametersList.get(0).getTotalReceivableBillsUuid());
		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(totalReceivableBills==null?null:totalReceivableBills.getClearingCashFlowIdentity());
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByCashFlow(cashFlow.getUuid(),SourceDocument.FIRSTOUTLIER_CLEARING);
		if (null == auditJob || null == totalReceivableBills || null == cashFlow || null == sourceDocument){
			logger.info("#reconciliationClearingDeductPlan#auditJobUuid:["+auditJob.getUuid()+"].参数错误"+(null == auditJob)+(null == totalReceivableBills)+(null == cashFlow )+(null == sourceDocument));
			return false;
		}
		boolean flag=true;
		for (ClearingVoucherParameters parameters : clearingVoucherParametersList) {
			try {
				String deductPlanUuid = parameters.getDeductPlanUuid();
				DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(deductPlanUuid);
				if (null == deductPlan) {
					logger.info("#reconciliationClearingDeductPlan#auditJobUuid:["+auditJob.getUuid()+"].参数错误,deductPlan is null");
					continue;
				}
				Map<String, Object> params = new HashMap<>();
				params.put(ReconciliationForClearingVoucher.PARAMS_DEDUCT_PLAN, deductPlan);
				params.put(ReconciliationForClearingVoucher.PARAMS_AUDIT_JOB, auditJob);
				params.put(ReconciliationForClearingVoucher.PARAMS_TOTAL_RECEIVABLE_BILLS, totalReceivableBills);
				params.put(ReconciliationForClearingVoucher.PARAMS_CASH_FLOW, cashFlow);
				params.put(ReconciliationForClearingVoucher.PARAMS_SOURCE_DOCUMENT, sourceDocument);
				Reconciliation reconciliation = Reconciliation
						.reconciliationFactory(ReconciliationType.RECONCILIATION_Clearing_Voucher.getKey());
				reconciliation.accountReconciliation(params);
			} catch (Exception e) {
				logger.info("#reconciliationClearingDeductPlan#auditJobUuid:["+auditJob.getUuid()+"].with the full error stack:::" + ExceptionUtils.getFullStackTrace(e));
				flag=false;
				continue;
			}
		}

		return flag;
	}

}
