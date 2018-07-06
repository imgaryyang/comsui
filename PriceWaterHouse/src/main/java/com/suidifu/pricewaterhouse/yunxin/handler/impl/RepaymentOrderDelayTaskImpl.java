/**
 * 
 */
package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobFactory;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.job.StageResult;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderDelayTask;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderDataSyncStatus;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.model.parameters.DstRepaymentDataSyncParameters;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderDetailCacheHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 *
 */
@Component("repaymentOrderDelayTask")
public class RepaymentOrderDelayTaskImpl extends TaskDeploy  implements RepaymentOrderDelayTask,CriticalSecionProjector{
	
	private static Log logger = LogFactory.getLog(RepaymentOrderDelayTaskImpl.class);
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private RepaymentOrderService repaymentOrderService;
	
	@Autowired
	private LedgerBookService ledgerBookService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private PaymentOrderService paymentOrderService;

	@Autowired
	private RepaymentOrderDetailCacheHandler repaymentOrderDetailCacheHandler;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;

	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	
	@Autowired
	private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataLists) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList();
		return criticalDataList;
	}


	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataList, Map<String, String> critialMarks) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList();
		return criticalDataList;
		
	}

	@Override
	public void repaymentOrderDataSync() {
		List<RepaymentOrder> registeredJobTarget = collectNeedToDoJobTarget();
		HashMap<String,JobContext<RepaymentOrder>> rawDataListOfJob = prepareJobContext(registeredJobTarget);
		for(String jobIdentity : rawDataListOfJob.keySet())
		{
			JobContext<RepaymentOrder> context=rawDataListOfJob.get(jobIdentity);
			String repaymentOrderUuid = context.getJobTarget()==null?"":context.getJobTarget().getOrderUuid();
			try
			{
				StageResult<Boolean>  taskResultForFstStep=performStageInRandom(Step.FST,context,Boolean.class);
				if(taskResultForFstStep.isAllTaskDone()==false) {
					continue;
				}

				if(taskResultForFstStep.isFailDone()){
					throw new JobInterruptException("repaymentOrderDataSync fst step fail done with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
				boolean fstStepResult = true;

                fstStepResult = collectResult(taskResultForFstStep,fstStepResult);

				if(fstStepResult == false){
					throw new JobInterruptException("repaymentOrderDataSync trd step sndStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
                repaymentOrderService.updateRepaymentOrder(repaymentOrderUuid, RepaymentOrderDataSyncStatus.DONE.getCode());
			} catch(JobInterruptException e) {
				this.jobHandler.abandonJob(context.job);
				logger.error("repaymentOrderDataSync occur error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],exception stack trace :"+ExceptionUtils.getStackTrace(e));
			} catch(RedisConnectionFailureException e) {
				logger.error("repaymentOrderDataSync occur error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],exception stack trace :"+ExceptionUtils.getStackTrace(e));
				continue;
			} catch(Throwable e) {
				this.jobHandler.abandonJob(context.job);
				logger.error("repaymentOrderDataSync occur error with exception with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],exception stack trace :"+ExceptionUtils.getStackTrace(e));
				e.printStackTrace();
			}

		}
	}

	@Override
	public void registerDataSyncJob(String repaymentOrderUuid) {
		try {

			JobType jobTypeForDataSync = JobType.REPAYMENT_ORDER_BQ_DATA_SYNC;

			Job job = jobService.getAllJobBy(repaymentOrderUuid,StringUtils.EMPTY,StringUtils.EMPTY, jobTypeForDataSync);

			if(JobFactory.isJobDone(job) || JobFactory.isJobAbandon(job)){
				return;
			}
			if(null == job)
			{
				logger.info("begin to register repaymentOrderDataSync job with repaymentOrderUuid["+repaymentOrderUuid+"]");

				Job jobForDataSync = jobHandler.registeJob(repaymentOrderUuid,StringUtils.EMPTY,StringUtils.EMPTY,jobTypeForDataSync);

				String beanNameForRepaymentOrder = "dstJobRepaymentOrderDataSyncDelayTask";

				String fstStageMethodName = "repaymentOrderDelayTask";

				jobHandler.registeStage(jobForDataSync, Step.FST, beanNameForRepaymentOrder, fstStageMethodName, 20,Priority.Low);

				jobHandler.registeJobStage(jobForDataSync);

				logger.info("end to register job with repaymentOrderUuid["+repaymentOrderUuid+"]");

			}
		} catch (Exception e) {
			logger.error("#registerOrderDataSyncJob# occur error with repaymentOrderUuid["+repaymentOrderUuid+"] ,full stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
	}

	private	 HashMap<String,JobContext<RepaymentOrder>> prepareJobContext(List<RepaymentOrder> registedJobTarget)
	{
		HashMap<String,JobContext<RepaymentOrder>> mapper=new HashMap<>();
		for (RepaymentOrder repaymentOrder : registedJobTarget) {
			try{

				Job job = jobService.getJobBy(repaymentOrder.getOrderUuid(), JobType.REPAYMENT_ORDER_BQ_DATA_SYNC);
				logger.info("#repaymentOrder#prepareJobContext#repaymentOrderDataSync repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"].");
				List<String> itemUuids = repaymentOrderItemService.getRepaymentOrderItemUuids(repaymentOrder.getOrderUuid());
				String delayConfigUuid = financialContractConfigurationService.getFinancialContractConfigContentContent(repaymentOrder.getFinancialContractUuid(),
					FinancialContractConfigurationCode.RECOVER_DELAY_TASK_UUID.getCode());
				String overdueDelayConfigUuid = financialContractConfigurationService.getFinancialContractConfigContentContent(repaymentOrder.getFinancialContractUuid(),FinancialContractConfigurationCode.BQ_OVER_RECOVER_DELAY_TASK_UUID.getCode());

				List<DstRepaymentDataSyncParameters> params = itemUuids.stream().map(itemUuid->
					new DstRepaymentDataSyncParameters(itemUuid, delayConfigUuid, overdueDelayConfigUuid)).collect(Collectors.toList());

				logger.info("#repaymentOrder#repaymentOrderDataSync#prepareJobContext details ids size:"+params.size()+",repaymentOrderUuid:"+repaymentOrder.getOrderUuid());

				JobContext<RepaymentOrder> jobContext=new JobContext<RepaymentOrder>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
				rawDataTable.put(Step.FST, (List)params);

				jobContext.setJob(job);
				jobContext.setJobTarget(repaymentOrder);
				jobContext.setRawDataTable(rawDataTable);
				mapper.put(job.getUuid(), jobContext);
			} catch (Exception e){
				logger.error("repaymentOrderDataSync error create repayment_job,repaymentOrderUuid["+(repaymentOrder==null?"":repaymentOrder.getOrderUuid())+"],msg:"+ExceptionUtils.getFullStackTrace(e));
			}

		}
		return mapper;
	}

	private List<RepaymentOrder> collectNeedToDoJobTarget() {

		List<RepaymentOrder> repaymentOrders = new ArrayList<RepaymentOrder>();

		List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(JobType.REPAYMENT_ORDER_BQ_DATA_SYNC);

		logger.info("#RepaymentOrder#createdOrProcessingJobList# create or processing job size["+createdOrProcessingJobList.size()+"]");

		for (Job job : createdOrProcessingJobList) {

			String fstBusinessCode = job.getFstBusinessCode();

			RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(fstBusinessCode);
			if(repaymentOrder==null){
				logger.info("#createdOrProcessingJobList# repaymentOrder is null,fstBusinessCode["+fstBusinessCode+"]");
				continue;
			}
			repaymentOrders.add(repaymentOrder);

		}

		logger.info("#createdOrProcessingJobList# need to resolve repaymentOrders size["+repaymentOrders.size()+"]");

		return repaymentOrders;
	}

}
