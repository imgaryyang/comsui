package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.microservice.model.JournalVoucher;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.microservice.model.ReconciliationRepaymentOrderParameterNameSpace;
import com.suidifu.microservice.model.SourceDocument;
import com.suidifu.microservice.model.SourceDocumentDetail;
import com.suidifu.microservice.model.Voucher;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCheckingLevel;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.model.TmpDepositDocRecoverParams;
import com.suidifu.owlman.microservice.spec.GeneralLeasingDocumentTypeDictionary.BusinessDocumentTypeUuid;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PlanRepaymentTimeConfiguration;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ReconciliationRepaymentOrderForSourceDocument extends ReconciliationRepaymentBase {
	
	@Autowired
	private RepaymentOrderService repaymentOrderService;
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
	
	@Autowired
	private ContractService contractService;

	@Autowired
	private VoucherService voucherService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private FastHandler fastHandler;
	@Autowired
	private FinancialContractService financialContractService;
    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    private static final Log logger = LogFactory.getLog(ReconciliationRepaymentOrderForSourceDocument.class);

	@Override
	public ReconciliationRepaymentContext preAccountReconciliation(
			Map<String, Object> inputParams) throws AlreadyProcessedException {
	
		String repaymentOrderUuid=(String)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_UUID);
		RepaymentOrder repaymentOrder=repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
		String repaymentOrderItemUuid=(String)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_ITEM_UUID);
		String offline_unique_cash_identity = (String)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_UNIQUE_CASH_IDENTITY);
		//TODO in cache
		RepaymentOrderItem repaymentOrderItem = repaymentOrderItemService.getRepaymentOrderItemByUuid(repaymentOrderItemUuid);
		//获取了repaymentOrder,repaymentOederItem(no.1,no.2)
		if(repaymentOrderItem==null ||  !repaymentOrderItem.isValidAndUnpaid()){
			throw new MisMatchedConditionException(" repaymentOrderItem is null or not valid, unpaid");
		}
		ReconciliationRepaymentContext context=new ReconciliationRepaymentContext();
		context.setTmpDepositDocParams((TmpDepositDocRecoverParams)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_TMPDEPOSIT_RECOVER_PARAM));
		context.setRepaymentOrderItem(repaymentOrderItem);
		context.setRepaymentOrder(repaymentOrder);
		context.setBookingAmount(repaymentOrderItem.getAmount());
		if(repaymentOrderItem.getDetailPayStatus()!=DetailPayStatus.UNPAID){//核销状态:0: 未核销，1:已核销
			context.setAlreadyReconciliation(true);
			throw new AlreadyProcessedException();
		}
		context.setUniqueOfflineCashIdentity(offline_unique_cash_identity);
		Contract contract = contractService.getContract(context.getRepaymentOrderItem().getContractUuid());
		context.setContract(contract);
		FinancialContract financialContract=financialContractService.getFinancialContractBy(repaymentOrderItem.getFinancialContractUuid());
		if(financialContract==null){
			throw new ReconciliationException("financialContract by repaymentOrderItem is null");
		}

        PlanRepaymentTimeConfiguration configuration = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContract.getFinancialContractUuid());
        context.setPlanRepaymentTimeLock(configuration);
		
		if(context.isRepaymentPlanTimeValid(financialContract,configuration)){
			context.setActualRecycleTime(repaymentOrderItem.getRepaymentPlanTime());
		}else{
			context.setActualRecycleTime(context.getRepaymentOrder().getCashFlowTime());
		}
		//设置核销的明细
		context.setBookingDetailAmount(repaymentOrderItemChargeService.getRepaymentOrderChargeMapByItemUuid(repaymentOrderItemUuid));
		return context;
		
	}

	public void fillVoucherNo(ReconciliationRepaymentContext context, SourceDocument sourceDocument){
		if(sourceDocument==null){
			return;
		}
		Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);
		if(voucher!=null){
			context.setSourceDocumentNo(voucher.getVoucherNo());
		}else {
			context.setSourceDocumentNo(sourceDocument.getSourceDocumentNo());
		}
	}
	
	@Override
	public void relatedDocumentsProcessing(ReconciliationRepaymentContext context){
		super.process_if_prepayment_plan(context);
		Order order = orderService.createAndSaveOrder(context.getBookingAmount(), "", context.getAssetSet(), context.getBorrower_customer(), context.getFinancialContract(), context.getActualRecycleTime());
		context.setOrder(order);
	}
	
	
	@Override
	public  void refreshVirtualAccount(ReconciliationRepaymentContext context)
	{
		super.refreshVirtualAccount(context);
	}

	@Override
	public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
		RepaymentOrderItem repaymentOrderItem = context.getRepaymentOrderItem();
		repaymentOrderItemService.update_pay_status_by(repaymentOrderItem.getOrderDetailUuid(), DetailPayStatus.PAID);
		fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID, repaymentOrderItem.getOrderDetailUuid(), FastRepaymentOrderItem.class, false);
		SourceDocumentDetail sourceDocumentDetail=context.getSourceDocumentDetail();
		if(sourceDocumentDetail!=null){
			sourceDocumentDetail.setStatus(SourceDocumentDetailStatus.SUCCESS);
			sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
		}
		
		super.postAccountReconciliation(context);
	}

	@Override
	public void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
		JournalVoucher journalVoucher = new JournalVoucher();
		journalVoucher.createFromContext(context);
		journalVoucher.fill_voucher_and_booking_amount(context.getJournalVoucherResovler().getRelatedBillUuid(), BusinessDocumentTypeUuid.REPAYMENT_ORDER_BV_TYPE,
					context.getRepaymentOrder().getOrderUuid(), context.getBookingAmount(), JournalVoucherStatus.VOUCHER_ISSUED,
					JournalVoucherCheckingLevel.AUTO_BOOKING, context.getJournalVoucherType());
		context.setIssuedJournalVoucher(journalVoucher);
		super.issueJournalVoucher(context);
		return ;
		
	}

	
	@Override
	public void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException{
		
		if(context.getFinancialContract()== null){
			throw new ReconciliationException("empty financialContract");
		}
		if(context.getRepaymentOrder()==null)
			throw new ReconciliationException("empty repaymentOrder");
		RepaymentOrderItem repaymentOrderItem = context.getRepaymentOrderItem();
		if(repaymentOrderItem==null)
			throw new ReconciliationException("emtpy repaymentOrderItem detail");
		if(repaymentOrderItem.isValid()==false){
			throw new ReconciliationException("repaymentOrderItem is invalid,repaymentOrderItemUuid["+repaymentOrderItem.getOrderDetailUuid()+"]");
		}
		if(repaymentOrderItem.getDetailPayStatus().equals(DetailPayStatus.PAID)==true)
			throw new AlreadyProcessedException();
		
		if(context.getCompany_customer()==null)
			throw new ReconciliationException("Invalid Company customer  ");
		
	}
}
