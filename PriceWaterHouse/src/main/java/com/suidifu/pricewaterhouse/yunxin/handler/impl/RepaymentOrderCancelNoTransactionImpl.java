/**
 * 
 */
package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.StageResult;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderCancelNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.entity.repayment.order.OrderAliveStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentOrderLapseParam;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DataSyncBaseHttpContent;
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
@Component("repaymentOrderCancelNoTransaction")
public class RepaymentOrderCancelNoTransactionImpl extends TaskDeploy  implements RepaymentOrderCancelNoTransaction,CriticalSecionProjector{
	
	private static Log logger = LogFactory.getLog(RepaymentOrderCancelNoTransactionImpl.class);
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private RepaymentOrderService repaymentOrderService;
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;


	@Autowired
	private LedgerBookService ledgerBookService;
	
	@Autowired
	private ContractService contractService;

	@Autowired
	private FastHandler fastHandler;

	@Autowired
	private DataSyncBaseHttpContent dataSyncBaseHttpContent;

	private	 HashMap<String,JobContext<RepaymentOrder>> prepareJobContext(List<RepaymentOrder> registedJobTarget)
	{
		HashMap<String,JobContext<RepaymentOrder>> mapper=new HashMap<>();
		for (RepaymentOrder repaymentOrder : registedJobTarget) {
			Job job = jobService.getJobBy(repaymentOrder.getOrderUuid(), JobType.REPAYMENT_ORDER_CANCEL);
			logger.info("#repaymentOrder#prepareJobContext#lapse repayment_order repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"].");
				List<RepaymentOrderLapseParam> repaymentOrderLapseParamList = repaymentOrderItemService.get_repayment_order_lapse_params(repaymentOrder.getOrderUuid());
				
				//empty时，继续，更新job状态

				logger.info("#repaymentOrder#prepareJobContext details ids size:"+repaymentOrderLapseParamList.size()+",repaymentOrderUuid:"+repaymentOrder.getOrderUuid());

				JobContext<RepaymentOrder> jobContext=new JobContext<RepaymentOrder>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
				rawDataTable.put(Step.SND, (List)repaymentOrderLapseParamList);
				
				jobContext.setJob(job);
				jobContext.setJobTarget(repaymentOrder);
				jobContext.setRawDataTable(rawDataTable);
				Map<String,String> criticalMarker= repaymentOrderLapseParamList.stream().collect(Collectors.toMap(RepaymentOrderLapseParam::getRepaymentOrderItemUuid, RepaymentOrderLapseParam::getBusinessId));
				jobContext.setSyncMarks(criticalMarker);
				mapper.put(job.getUuid(), jobContext);
				
		}
		return mapper;
	}


	@Override
	public void lapse_repayment_order(){

			List<RepaymentOrder> registeredJobTarget = collectNeedToDoJobTarget();
			
			HashMap<String,JobContext<RepaymentOrder>> rawDataListOfJob=prepareJobContext(registeredJobTarget);
			
			for(String jobIdentity : rawDataListOfJob.keySet())
			{
				JobContext<RepaymentOrder> context=rawDataListOfJob.get(jobIdentity);
				String repaymentOrderUuid = context.getJobTarget()==null?"":context.getJobTarget().getOrderUuid();
				try
				{
					Map<String,String> syncMarks=context.getSyncMarks();
					
					logger.info("result size :"+syncMarks.size());
				
					StageResult<Boolean>  taskResultForSndStep=performStageInCritical(Step.SND,context,syncMarks,this,Boolean.class);
							
					logger.info("snd step task result["+taskResultForSndStep.isAllTaskSucDone()+"]");
				
					if(taskResultForSndStep.isAllTaskDone()==false) {
						continue;
					}
					if(taskResultForSndStep.isFailDone()){

						throw new JobInterruptException("snd step fail done with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					boolean sndStepResult = true;
					
					sndStepResult = collectResult(taskResultForSndStep,sndStepResult);
					
					if(sndStepResult == true){

						repaymentOrderService.update_repayment_order_lock_and_alive_status(OrderAliveStatus.PAYMENT_ORDER_CANCEL,context.getJobTarget().getOrderUuid(),RepaymentOrder.EMPTY,RepaymentOrder.LAPSE);
						continue;
					}
					logger.error("valida snd result false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
				catch(JobInterruptException e)
				{
					this.jobHandler.abandonJob(context.job);
					logger.error("lapse repayment_order_items occour error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
					
				}
				catch(RedisConnectionFailureException e)
				{
					logger.error("lapse repayment_order_items occour error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
					continue;
				}
				catch(Throwable e)
				{
					
					this.jobHandler.abandonJob(context.job);
				
					logger.error("lapse repayment_order_items occour error with exception with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
				
					e.printStackTrace();
				}
				
			}
	}


	private List<RepaymentOrder> collectNeedToDoJobTarget() {
		
		List<RepaymentOrder> repaymentOrders = new ArrayList<RepaymentOrder>();
		
		List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(JobType.REPAYMENT_ORDER_CANCEL);
		
		logger.info("#RepaymentOrder#createdOrProcessingJobList# create or processing job size["+createdOrProcessingJobList.size()+"]");
		
		for (Job job : createdOrProcessingJobList) {
			
			String fstBusinessCode = job.getFstBusinessCode();

			RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(fstBusinessCode);
			if(repaymentOrder==null){
				logger.info("#createdOrProcessingJobList#lapse repaymentOrder# repaymentOrder is null,fstBusinessCode["+fstBusinessCode+"]");
				continue;
			}
			repaymentOrders.add(repaymentOrder);
			
		}
		
		logger.info("#createdOrProcessingJobList#lapse repaymentOrder# need to resolve repaymentOrders size["+repaymentOrders.size()+"]");
		
		return repaymentOrders;
	}

	
	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataLists) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList();

		for(Object params:rawDataLists)
		{
			RepaymentOrderLapseParam repaymentOrderParameters=(RepaymentOrderLapseParam)params;
			String financialContractUuid=repaymentOrderParameters.getFinancialContractUuid();
			//TODO
			CriticalMarkedData<RepaymentOrderLapseParam> syncMarkedData =new CriticalMarkedData<>();
			syncMarkedData.setCritialMark(financialContractUuid);
			syncMarkedData.setTaskParams(repaymentOrderParameters);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
	}


	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataList, Map<String, String> critialMarks) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList();
		int i = 0;
		for(Object params:rawDataList)
		{
			RepaymentOrderLapseParam repaymentOrderParameters=(RepaymentOrderLapseParam)params;
			String repaymentDetailUuid=repaymentOrderParameters.getRepaymentOrderItemUuid();
			String contractUuid=critialMarks.get(repaymentDetailUuid);
			CriticalMarkedData<RepaymentOrderLapseParam> syncMarkedData =new CriticalMarkedData<>();
			
			if(StringUtils.isEmpty(contractUuid)){
				logger.error("this repaymentDetailUuid["+repaymentDetailUuid+"] reference contractUuid is null");
				throw new JobInterruptException("job target["+repaymentOrderParameters.getRepaymentOrderUuid()+"] criticalMark missing");
			}
			
			syncMarkedData.setCritialMark(contractUuid);
			syncMarkedData.setTaskParams(repaymentOrderParameters);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
		
	}

}
