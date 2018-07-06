package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.CashFlowAutoIssueV2_0Handler;
import com.suidifu.microservice.model.ReconciliationRepayment;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.ReceivableInAdvanceStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.DepositeAccountHandler;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("cashFlowAutoIssueV2_0Handler")
public class CashFlowAutoIssueV2_0HandlerImpl implements CashFlowAutoIssueV2_0Handler {

	private static final Log logger = LogFactory.getLog(CashFlowAutoIssueV2_0HandlerImpl.class);
	
	@Autowired
	private SourceDocumentService sourceDocumentService;

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	BankAccountCache bankAccountCache;
	@Autowired
	DepositeAccountHandler depositeAccountHandler;

	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;

	@Autowired
	private RepaymentOrderService repaymentOrderService;
	
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	@Autowired
	private PaymentOrderService paymentOrderService;

	@Override
	public void recover_assets_repayment_order_deduct(String sourceDocumentUuid){
		logger.info("recover_assets_repayment_order_deduct start, sourceDocumentUuid["+sourceDocumentUuid+"].");
		SourceDocument deductSourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
		DeductApplication deductApplication = deductApplicationService
				.getDeductApplicationByDeductApplicationUuid(deductSourceDocument.getOutlierDocumentUuid());
		if(deductApplication==null || deductApplication.isPartSucOrSus()==false){
			logger.info("recover_assets_repayment_order_deduct start, deductApplication is null or not success, sourceDocumentUuid["+sourceDocumentUuid+"],deductApplicationUuid["+(deductApplication==null?"":deductApplication.getDeductApplicationUuid())+"].");
			return;
		}
		List<String> sourceDocumentDetailUuids = sourceDocumentDetailService
				.getValidDeductSourceDocumentDetailUuidsBySourceDocumentUuid(sourceDocumentUuid);
		for (String sourceDocumentDetailUuid : sourceDocumentDetailUuids) {
			SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.getSourceDocumentDetail(sourceDocumentDetailUuid);
			
			RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(sourceDocumentDetail.getSecondNo());

			ReconciliationRepayment accountReconciliation = ReconciliationRepayment
					.reconciliationFactory(item.getRepaymentWay().getKey());//reconciliationRepaymentOrderForDeductApiDocumentHandler
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentForDeductApi.PARAMS_DEDUCT_APPLICATION, deductApplication);
			inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_SOURCE_DOCUMENT, deductSourceDocument);
			inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_SOURCE_DOCUMENT_DETAIL_UUID,
					sourceDocumentDetailUuid);
			inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_UUID,
					sourceDocumentDetail.getFirstNo());
			inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_ITEM_UUID,
					sourceDocumentDetail.getSecondNo());
			logger.info("recover_assets_repayment_order_deduct by each item start, itemUuid["+sourceDocumentDetail.getSecondNo()+"], sourceDocumentUuid["+sourceDocumentUuid+"].");
			accountReconciliation.accountReconciliation(inputParams);
			logger.info("recover_assets_repayment_order_deduct by each item end, itemUuid["+sourceDocumentDetail.getSecondNo()+"], sourceDocumentUuid["+sourceDocumentUuid+"].");
		}
		deductSourceDocument.updateSourceDocumetStatus(SourceDocumentStatus.SIGNED);
		sourceDocumentService.saveOrUpdate(deductSourceDocument);
		deductApplication.setRecordStatus(RecordStatus.WRITE_OFF);
		deductApplicationService.saveOrUpdate(deductApplication);
		RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(deductSourceDocument.getSecondAccountId());
		repaymentOrder.updateRecoverStatusSuc();
		repaymentOrderService.saveOrUpdate(repaymentOrder);
		logger.info("recover_assets_repayment_order_deduct end, sourceDocumentUuid["+sourceDocumentUuid+"].");
	}
	
	@Override
	public void recover_receivable_in_advance(String orderUuid,String paymentOrderUuid) {
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

		List<RepaymentOrderItem> items=repaymentOrderItemService.getRepaymentOrderItems(orderUuid);
        RepaymentOrderItem item=items.get(0); 
		if(items.size()!=1||item.getReceivableInAdvanceStatus()==ReceivableInAdvanceStatus.RECEIVED){
    			throw new RuntimeException("进预收，明细应只有一条");
    		}

		ReconciliationRepayment accountReconciliation = ReconciliationRepayment.receivableInAdvanceBean();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_ITEM_UUID,item.getOrderDetailUuid());
		inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_UUID, repaymentOrder.getOrderUuid());
		inputParams.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER, paymentOrder);
		accountReconciliation.accountReconciliation(inputParams);
		repaymentOrder.setReceivableInAdvanceStatus(ReceivableInAdvanceStatus.RECEIVED);
		repaymentOrderService.update(repaymentOrder);
	}
}
