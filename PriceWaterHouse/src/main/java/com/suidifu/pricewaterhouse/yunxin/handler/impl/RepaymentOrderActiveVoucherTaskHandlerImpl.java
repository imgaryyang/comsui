/**
 * 
 */
package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.job.*;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderActiveVoucherTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.contract.ContractRepaymentOrderItemMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 *
 */
@Component("repaymentOrderActiveVoucherTaskHandlerNoTransaction")
public class RepaymentOrderActiveVoucherTaskHandlerImpl extends TaskDeploy  implements RepaymentOrderActiveVoucherTaskHandler,CriticalSecionProjector{

	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private VoucherHandler voucherHandler;
	
	@Autowired
	private RepaymentOrderService repaymentOrderService;
	
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private CashFlowService cashFlowService;
	
	@Autowired
	private CashFlowHandler cashFlowHandler;
	
	private Log logger = LogFactory.getLog(getClass());
	
	private	 HashMap<String,JobContext<RepaymentOrder>> prepareJobContext(List<RepaymentOrder> registedJobTarget)
	{
		HashMap<String,JobContext<RepaymentOrder>> mapper=new HashMap<>();
		for (RepaymentOrder repaymentOrder : registedJobTarget) {
			
				logger.info("active_voucher_repayment_order_recover repaymentOrderUuId["+repaymentOrder.getOrderUuid()+"].");
				String repaymentOrderUuid = repaymentOrder.getOrderUuid();
				String financialContractUuid = repaymentOrder.getFinancialContractUuid();
				FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
				if(financialContract==null){
					continue;
				}
				String financialContractNo = financialContract.getContractNo();
				String ledgerBookNo = financialContract.getLedgerBookNo();
				String cashIdentity = cashFlowHandler.getUnqueCashIdentity(repaymentOrderUuid);
				List<ContractRepaymentOrderItemMapper> itemMapper = repaymentOrderItemService.getCriticalMarkerByOrderUuidPeriodASC(repaymentOrderUuid);
				
				logger.info("active_voucher_repayment_order_recover repaymentOrderItemUuids ids size:"+itemMapper.size()+",repaymentOrderUuId:"+repaymentOrder.getOrderUuid());
				Job job = jobService.getJobBy(repaymentOrderUuid, JobType.ACTIVE_VOUCHER_RECOVER);
				
				JobContext<RepaymentOrder> jobContext=new JobContext<RepaymentOrder>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
			
				List<RepaymentOrderReconciliationParameters> rawDataList=itemMapper.stream().map(item->{
					return new RepaymentOrderReconciliationParameters(repaymentOrderUuid,item.getRepaymentOrderItemUuid(),financialContractNo,ledgerBookNo, cashIdentity);
					}
						).collect(Collectors.toList());
				
				Map<String,String> criticalMarker= itemMapper.stream().collect(Collectors.toMap(ContractRepaymentOrderItemMapper::getRepaymentOrderItemUuid, ContractRepaymentOrderItemMapper::getContractUuid));
				rawDataTable.put(Step.SND, (List)rawDataList);
				jobContext.setJob(job);
				jobContext.setJobTarget(repaymentOrder);
				jobContext.setRawDataTable(rawDataTable);
				jobContext.setSyncMarks(criticalMarker);
				mapper.put(job.getUuid(), jobContext);
				
		}
		return mapper;
	}
	
	
	@Override
	public void handler_repayment_order_active_voucher_recover(){
		
			registSubrogationJob();
			
			List<RepaymentOrder> registedJobTarget = collectNeedToDoJobTarget();
			
			HashMap<String,JobContext<RepaymentOrder>> rawDataListOfJob=prepareJobContext(registedJobTarget);
			
			for(String jobIdentity : rawDataListOfJob.keySet())
			{
				JobContext<RepaymentOrder> context=rawDataListOfJob.get(jobIdentity);
				String orderUuid = context.getJobTarget()==null?"":context.getJobTarget().getOrderUuid();
				try
				{
					Map<String,String> syncMarks=context.getSyncMarks();
					if(MapUtils.isEmpty(syncMarks)){
						throw new JobInterruptException("syncMarks is empty with jobIdentity["+jobIdentity+"],orderUuid["+orderUuid+"]");
					}
					
					logger.info("result size :"+syncMarks.size());
					StageResult<Boolean>  taskResultForFrthStep=performStageInCritical(Step.SND,context,syncMarks,this,Boolean.class);
							
					if(taskResultForFrthStep.isAllTaskDone() == false) {
						
						continue;
					}
					
					boolean fourthStepResult = true;
					
					fourthStepResult = collectResult(taskResultForFrthStep,
							fourthStepResult);
					
					RepaymentOrder jobTarget = context.getJobTarget();
					if(jobTarget.getOrderRecoverResult()==OrderRecoverResult.ALL) continue;

					BigDecimal totalAmount = repaymentOrderItemService.getTotalAmountRepaymentOrderItems(jobTarget.getOrderUuid());
					
					repaymentOrderService.updateRepaymentOrderStatus(totalAmount, jobTarget);
					
				}
				catch(JobInterruptException e)
				{
					this.jobHandler.abandonJob(context.job);
					
					logger.error("active_voucher_repayment_order_recover occour error with jobInterruptException with jobIdentity["+jobIdentity+"],orderUuid["+orderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
					
				}
				catch(Throwable e)
				{
					
					this.jobHandler.abandonJob(context.job);
				
					logger.error("active_voucher_repayment_order_recover occour error with exception with jobIdentity["+jobIdentity+"],orderUuid["+orderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
				
					e.printStackTrace();
				}
				
			}
	}


	private List<RepaymentOrder> collectNeedToDoJobTarget() {
		
		List<RepaymentOrder> repaymentOrders = new ArrayList<RepaymentOrder>();
		
		List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(JobType.ACTIVE_VOUCHER_RECOVER);
		
		logger.info("#createdOrProcessingJobList# create or processing job size["+createdOrProcessingJobList.size()+"]");
		
		for (Job job : createdOrProcessingJobList) {
			
			String fstBusinessCode = job.getFstBusinessCode();
			
			RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(fstBusinessCode);
			if(repaymentOrder==null){
				logger.info("#createdOrProcessingJobList# create or processing,fstBusinessCode["+fstBusinessCode+"]");
				continue;
			}
			repaymentOrders.add(repaymentOrder);
			
		}
		
		logger.info("#createdOrProcessingJobList# need to resolve repaymentOrders size["+repaymentOrders.size()+"]");
		
		return repaymentOrders;
	}


	
	
	private void registSubrogationJob() {
		
		
		List<RepaymentOrder> needWriteOffRepaymentOrderList = repaymentOrderService.getWriteOffRepaymentOrderByStatus();
		
		if (CollectionUtils.isEmpty(needWriteOffRepaymentOrderList)) {
			logger.info("needWriteOffRepaymentOrderList is empty");
			return;
		}
		
		logger.info("#get_deposit_repayment_order_list_connected_by# needWriteOffRepaymentOrderList size["+needWriteOffRepaymentOrderList.size()+"]");
		
		for (RepaymentOrder repaymentOrder : needWriteOffRepaymentOrderList) {
			
			try {
				
				if (null == repaymentOrder)
				continue;
				
				List<String> cashFlowUuidList = paymentOrderService.getPaymentOrderListByOrderUuid(repaymentOrder.getOrderUuid());
				
				if(CollectionUtils.isEmpty(cashFlowUuidList)) continue;
				
				boolean flag = true;
				
				for (String cashFlowUuid : cashFlowUuidList) {
					CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
					if(cashFlow.getAuditStatus() != AuditStatus.ISSUED) flag = false;
				}
				
				if(!flag) continue;

				String repaymentOrderUuid = repaymentOrder.getOrderUuid();
				
				JobType jobTypeForSubrogation = JobType.ACTIVE_VOUCHER_RECOVER;
				
				String financialContractUuid = repaymentOrder.getFinancialContractUuid();
				
				Job job = jobService.getAllJobBy(repaymentOrderUuid,financialContractUuid,StringUtils.EMPTY, jobTypeForSubrogation);
				
				if(JobFactory.isJobDone(job) || JobFactory.isJobAbandon(job)){
					
					continue;
				}
				if(null == job)
				{
					logger.info("begin to registe job with repaymentOrderUuid["+repaymentOrderUuid+"]");
					
					Job jobForSubrogation = jobHandler.registeJob(repaymentOrderUuid,financialContractUuid,StringUtils.EMPTY,jobTypeForSubrogation);
					
					String beanNameForSubrogation = "repaymentOrderActiveVoucherReconciliationProxy";
					
					String fourthStageMethodName = "repaymentOrderRecoverDetails";
					
					jobHandler.registeStage(jobForSubrogation, Step.SND, beanNameForSubrogation, fourthStageMethodName, 20,Priority.Low);
					
					jobHandler.registeJobStage(jobForSubrogation);
					
					logger.info("end to registe job with repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
			} catch (Exception e) {
				
				logger.error("#registActiveVoucherJob# occur error with repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"] ,full stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
				
				continue;
			}
			
		}

	}
	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataLists) throws JobInterruptException {
		List<CriticalMarkedData<?>> critialDataList=new ArrayList();
		
		for(Object params:rawDataLists)
		{
			RepaymentOrderReconciliationParameters subrogationReconciliationParameters=(RepaymentOrderReconciliationParameters)params;
			String repaymentOrderUuid = subrogationReconciliationParameters.getRepaymentOrderUuid();
			RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
			
			CriticalMarkedData<RepaymentOrderReconciliationParameters> syncMarkedData =new CriticalMarkedData<>();
			syncMarkedData.setCritialMark(repaymentOrder.getFinancialContractUuid());
			syncMarkedData.setTaskParams(subrogationReconciliationParameters);
			critialDataList.add(syncMarkedData);
		}
		return critialDataList;
	}

	
	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataList, Map<String, String> critialMarks) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList();
		int i = 0;
		for(Object params:rawDataList)
		{
			RepaymentOrderReconciliationParameters subrogationReconciliationParameters=(RepaymentOrderReconciliationParameters)params;
			String repaymentOrderItemUuid=subrogationReconciliationParameters.getRepaymentOrderItemUuid();
			String contractUuid=critialMarks.get(repaymentOrderItemUuid);
			CriticalMarkedData<RepaymentOrderReconciliationParameters> syncMarkedData =new CriticalMarkedData<>();
			
			if(StringUtils.isEmpty(contractUuid)){
				logger.error("this repaymentOrderItemUuid["+repaymentOrderItemUuid+"] reference contractUuid is null");
				throw new JobInterruptException("job target["+repaymentOrderItemUuid+"] criticalMark missing"); 
			}
			
			syncMarkedData.setCritialMark(contractUuid);
			syncMarkedData.setTaskParams(subrogationReconciliationParameters);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
		
	}
	

}
