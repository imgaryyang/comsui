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
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderTaskNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.PlanRepaymentTimeConfiguration;
import com.zufangbao.sun.entity.repayment.order.FastRepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.OrderAliveStatus;
import com.zufangbao.sun.entity.repayment.order.OrderCheckStatus;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.RepaymentOrderModifyParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.RepaymentOrderParameters;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderDetailCacheHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
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
@Component("repaymentOrderTaskNoTransaction")
public class RepaymentOrderTaskNoTransactionImpl extends TaskDeploy  implements RepaymentOrderTaskNoTransaction,CriticalSecionProjector{
	
	private static Log logger = LogFactory.getLog(RepaymentOrderTaskNoTransactionImpl.class);
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
	private RepaymentOrderDelayTask repaymentOrderDelayTask;

	@Autowired
	private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
	
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	
	private	 HashMap<String,JobContext<RepaymentOrder>> prepareJobContext(List<RepaymentOrder> registedJobTarget)
	{
		HashMap<String,JobContext<RepaymentOrder>> mapper=new HashMap<>();
		for (RepaymentOrder repaymentOrder : registedJobTarget) {
			try{

			Job job = jobService.getJobBy(repaymentOrder.getOrderUuid(), JobType.REPAYMENT_ORDER_PLACING);
			logger.info("#repaymentOrder#prepareJobContext#check_and_save_repayment_order_items repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"].");
				String orderDetailFilePath = repaymentOrder.getOrderDetailFilePath();
				List<RepaymentOrderDetail> repaymentOrderDetailList = new ArrayList<RepaymentOrderDetail>();
				try {
					repaymentOrderDetailList=repaymentOrderDetailCacheHandler.getRepaymentOrderDetailListFromFile(repaymentOrder.getOrderUuid(),orderDetailFilePath);
				} catch (Exception e){
					logger.error("#repaymentOrder#parse items from csv error,repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"],msg:"+ExceptionUtils.getFullStackTrace(e));
					jobHandler.abandonJob(job);
					continue;
				}
				if(CollectionUtils.isEmpty(repaymentOrderDetailList)){
					logger.info("#repaymentOrder#parse items from csv get repaymentOrderDetailList is empty.repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"].");
					jobHandler.abandonJob(job);
					continue;
				}
				
				String financialContractUuid = repaymentOrder.getFinancialContractUuid();
				FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
				if(financialContract==null){
					continue;
				}

				RepaymentOrderDetail firstDetail=repaymentOrderDetailList.get(0);
			
				RepaymentWay repaymentWay=firstDetail.getRepaymentWayEnum();
				String customerSource = getCustomerSource(firstDetail);
				String contractNo = firstDetail.getContractNo();
				String contractUniqueId = firstDetail.getContractUniqueId();

				PlanRepaymentTimeConfiguration planRepaymentTimeConfiguration = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContractUuid);
				
				int repaymentCheckDays=financialContract.getRepaymentCheckDays();
				LedgerBook ledgerBook=ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
				String financialContractNo=repaymentOrder.getFinancialContractNo();
				
				int size = repaymentOrderDetailList.size();
				List<RepaymentOrderParameters> parameterss = repaymentOrderDetailList.parallelStream().map(repaymentDetail->
						new RepaymentOrderParameters(repaymentOrder, repaymentDetail,repaymentWay,customerSource,repaymentCheckDays,ledgerBook,contractNo,contractUniqueId,size, planRepaymentTimeConfiguration,"")).collect(Collectors.toList());
				
				logger.info("#repaymentOrder#prepareJobContext details ids size:"+parameterss.size()+",repaymentOrderUuid:"+repaymentOrder.getOrderUuid());

				JobContext<RepaymentOrder> jobContext=new JobContext<RepaymentOrder>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
				rawDataTable.put(Step.FST, (List)parameterss);

				rawDataTable.put(Step.SND, (List)parameterss);
				
				rawDataTable.put(Step.TRD, (List)parameterss);
				
				jobContext.setJob(job);
				jobContext.setJobTarget(repaymentOrder);
				jobContext.setRawDataTable(rawDataTable);
				mapper.put(job.getUuid(), jobContext);
			} catch (Exception e){
				logger.error("error create repayment_job,repaymentOrderUuid["+(repaymentOrder==null?"":repaymentOrder.getOrderUuid())+"],msg:"+ExceptionUtils.getFullStackTrace(e));
			}
				
		}
		return mapper;
	}

	private String getCustomerSource(RepaymentOrderDetail firstDetail) {
		String customerSource = "";
		Contract contract=contractService.getContractBy(firstDetail.getContractUniqueId(), firstDetail.getContractNo());
		if(contract!=null && StringUtils.isNotBlank(contract.getCustomerUuid())){

			Customer customer = customerService.getCustomer(contract.getCustomerUuid());
            if(customer!=null){
				customerSource = customer.getSource();
			}

        }
		return customerSource;
	}


	@Override
	public void check_and_save_repayment_order_items(){
		
			registerOrderPlacingJob();

			List<RepaymentOrder> registeredJobTarget = collectNeedToDoJobTarget(JobType.REPAYMENT_ORDER_PLACING);
			
			HashMap<String,JobContext<RepaymentOrder>> rawDataListOfJob=prepareJobContext(registeredJobTarget);
			
			for(String jobIdentity : rawDataListOfJob.keySet())
			{
				JobContext<RepaymentOrder> context=rawDataListOfJob.get(jobIdentity);
				String repaymentOrderUuid = context.getJobTarget()==null?"":context.getJobTarget().getOrderUuid();
				try
				{
					StageResult<Map>  taskResultForFstStep=performStageInRandom(Step.FST,context,Map.class);
					
					if(taskResultForFstStep.isAllTaskDone()==false) {continue;}
					
					if(taskResultForFstStep.isFailDone()){
						
						throw new JobInterruptException("fst step fail done with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					Map<String,String> syncMarks=new HashMap<>();
					
					while (taskResultForFstStep.hasMoreElements()) {
						
						Map map = taskResultForFstStep.nextElement();
						
						if(null != map){
							
							syncMarks.putAll(map);
						}
					}
					logger.info("result size :"+taskResultForFstStep.getResult().size());
				
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

						//第二步成功，第三步不执行
						jobHandler.updateJobDone(context.getJob());
						repaymentOrderService.update_repayment_order_check_status_and_placing_status(context.getJobTarget().getOrderUuid(), OrderCheckStatus.VERIFICATION_SUCCESS);
						registerDataSyncJob(context.getJobTarget().getOrderUuid(),context.getJobTarget().getFinancialContractUuid());
						continue;
					}
					logger.error("valida snd result false.");

					StageResult<Boolean>  taskResultForTrdStep=performStageInCritical(Step.TRD,context,syncMarks,this,Boolean.class);
					
					if(taskResultForTrdStep.isAllTaskDone() == false) {
						continue;
					}
					
					if(taskResultForTrdStep.isFailDone()) {
						
						throw new JobInterruptException("trd step sndStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					boolean trdStepResult = true;
					
					trdStepResult = collectResult(taskResultForTrdStep,
							trdStepResult);



					if(trdStepResult == false){
						throw new JobInterruptException("trd step trdStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					repaymentOrderService.update_repayment_order_check_status_and_placing_status(context.getJobTarget().getOrderUuid(), OrderCheckStatus.VERIFICATION_FAILURE);

				}
				catch(JobInterruptException e)
				{
					this.jobHandler.abandonJob(context.job);
					
					logger.error("check_and_save_repayment_order_items occour error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
					
				}
				catch(RedisConnectionFailureException e)
				{
					logger.error("check_and_save_repayment_order_items occour error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
					continue;
				}
				catch(Throwable e)
				{
					
					this.jobHandler.abandonJob(context.job);
				
					logger.error("check_and_save_repayment_order_items occour error with exception with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
				
					e.printStackTrace();
				}
				
			}
	}

	private void registerDataSyncJob(String orderUuid, String financialContractUuid) {
		boolean isConfigured = financialContractConfigurationService.isFinancialContractConfigCodeConfiged(financialContractUuid,
			FinancialContractConfigurationCode.REPAYMENT_ORDER_CHECK_DELAY_TASK.getCode());
		if(isConfigured){
			repaymentOrderDelayTask.registerDataSyncJob(orderUuid);
		}
	}


	private List<RepaymentOrder> collectNeedToDoJobTarget(JobType jobType) {
		
		List<RepaymentOrder> repaymentOrders = new ArrayList<RepaymentOrder>();
		
		List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(jobType);
		
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


	private void registerOrderPlacingJob() {

		List<FastRepaymentOrder> fastRepaymentOrders =  repaymentOrderService.getNeedCheckMultiContractTypeRepaymentOrderList();
		logger.info("#RepaymentOrderTaskNoTransactionImpl# getNeedCheckRepaymentOrderList size["+fastRepaymentOrders.size()+"]");
		
		for (FastRepaymentOrder fastRepaymentOrder : fastRepaymentOrders) {
			
			try {
				String repaymentOrderUuid = fastRepaymentOrder.getOrderUuid();
				if(StringUtils.isEmpty(repaymentOrderUuid)){
					continue;
				}

				JobType jobTypeForPlacingOrder = JobType.REPAYMENT_ORDER_PLACING;
				
				String firstNo = fastRepaymentOrder.getOrderUniqueId();
				
				Job job = jobService.getAllJobBy(repaymentOrderUuid,firstNo,StringUtils.EMPTY, jobTypeForPlacingOrder);
				
				if(JobFactory.isJobDone(job) || JobFactory.isJobAbandon(job)){
					
					continue;
				}
				if(null == job)
				{
					logger.info("begin to register job with repaymentOrderUuid["+repaymentOrderUuid+"]");
					
					Job jobForPlacingOrder = jobHandler.registeJob(repaymentOrderUuid,firstNo,StringUtils.EMPTY,jobTypeForPlacingOrder);
					
					String beanNameForRepaymentOrder = "dstJobRepaymentOrderPlacing";
					
					String fstStageMethodName = "criticalMarker";
					String sndStageMethodName = "check_and_save";
					String trdStageMethodName = "roll_back";

					jobHandler.registeStage(jobForPlacingOrder, Step.FST, beanNameForRepaymentOrder, fstStageMethodName, 20,Priority.Low);
					jobHandler.registeStage(jobForPlacingOrder, Step.SND, beanNameForRepaymentOrder, sndStageMethodName, 20,Priority.Low);
					jobHandler.registeStage(jobForPlacingOrder, Step.TRD, beanNameForRepaymentOrder, trdStageMethodName, 20,Priority.Low);

					jobHandler.registeJobStage(jobForPlacingOrder);
					
					logger.info("end to register job with repaymentOrderUuid["+repaymentOrderUuid+"]");

					logger.info("update_repayment_order in checking repaymentOrderUuid["+repaymentOrderUuid+"]");
					repaymentOrderService.update_repayment_order_check_status_and_placing_status(repaymentOrderUuid, OrderCheckStatus.IN_VERIFICATION);
					
					RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
					repaymentOrder.setPlacingJobUuid(jobForPlacingOrder.getUuid());
					repaymentOrderService.saveOrUpdate(repaymentOrder);
				}
			} catch (Exception e) {
				
				logger.error("#registerOrderPlacingJob# occur error with repaymentOrderUuid["+fastRepaymentOrder.getOrderUuid()+"] ,full stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
				
				continue;
			}
			
		}

	}
	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataLists) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList();

		for(Object params:rawDataLists)
		{
			
			if (params instanceof  RepaymentOrderParameters) {
				
				RepaymentOrderParameters repaymentOrderParameters=(RepaymentOrderParameters)params;
				
				String financialContractUuid=repaymentOrderParameters.getFinancialContractUuid();
				//TODO
				CriticalMarkedData<RepaymentOrderParameters> syncMarkedData =new CriticalMarkedData<>();
				syncMarkedData.setCritialMark(financialContractUuid);
				syncMarkedData.setTaskParams(repaymentOrderParameters);
				criticalDataList.add(syncMarkedData);
				
			}else if(params instanceof  RepaymentOrderModifyParameters){
				
				RepaymentOrderModifyParameters repaymentOrderModifyParameters=(RepaymentOrderModifyParameters)params;
				
				String financialContractUuid=repaymentOrderModifyParameters.getFinancialContractUuid();
				//TODO
				CriticalMarkedData<RepaymentOrderModifyParameters> syncMarkedData =new CriticalMarkedData<>();
				syncMarkedData.setCritialMark(financialContractUuid);
				syncMarkedData.setTaskParams(repaymentOrderModifyParameters);
				criticalDataList.add(syncMarkedData);
			}
			
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
			
			if (params instanceof  RepaymentOrderParameters) {
			
				RepaymentOrderParameters repaymentOrderParameters=(RepaymentOrderParameters)params;
				String repaymentDetailUuid=repaymentOrderParameters.getRepaymentOrderDetailUuid();
				String contractUuid=critialMarks.get(repaymentDetailUuid);
				
				CriticalMarkedData<RepaymentOrderParameters> syncMarkedData =new CriticalMarkedData<>();
				
				if(StringUtils.isEmpty(contractUuid)){
					logger.error("this repaymentDetailUuid["+repaymentDetailUuid+"] reference contractUuid is null");
					throw new JobInterruptException("job target["+repaymentOrderParameters.getRepaymentOrderUuid()+"] criticalMark missing");
				}
				
				syncMarkedData.setCritialMark(contractUuid);
				syncMarkedData.setTaskParams(repaymentOrderParameters);
				criticalDataList.add(syncMarkedData);
				
			}else if(params instanceof  RepaymentOrderModifyParameters){
				
				RepaymentOrderModifyParameters repaymentOrderModifyParameters=(RepaymentOrderModifyParameters)params;
				String repaymentDetailUuid=repaymentOrderModifyParameters.getRepaymentOrderDetailUuid();
				String contractUuid=critialMarks.get(repaymentDetailUuid);
				
				CriticalMarkedData<RepaymentOrderModifyParameters> syncMarkedData =new CriticalMarkedData<>();
				
				if(StringUtils.isEmpty(contractUuid)){
					logger.error("this repaymentDetailUuid["+repaymentDetailUuid+"] reference contractUuid is null");
					throw new JobInterruptException("job target["+repaymentOrderModifyParameters.getRepaymentOrderUuid()+"] criticalMark missing");
				}
				
				syncMarkedData.setCritialMark(contractUuid);
				syncMarkedData.setTaskParams(repaymentOrderModifyParameters);
				criticalDataList.add(syncMarkedData);
			}
			
		}
		return criticalDataList;
		
	}

	@Override
	public void repaymentOrderGenerateThirdPartVoucherWithReconciliationTrap() {
		List<RepaymentOrder> repaymentOrderList = repaymentOrderService.getWriteOffRepaymentOrderSingleContractByStatus();
		if(CollectionUtils.isEmpty(repaymentOrderList)) return;
	
		for (RepaymentOrder repaymentOrder : repaymentOrderList) {
			if (null == repaymentOrder || repaymentOrder.getOrderRecoverResult()==OrderRecoverResult.ALL ){
				 continue;
			}
			try {
				
				PaymentOrder paymentOrder = paymentOrderService.getSuccessPaymentOrderOrderUuid(repaymentOrder.getOrderUuid());
				if(paymentOrder == null) continue;
	
				thirdPartyVoucherRepaymentOrderWithReconciliationNoSession.generateThirdPartVoucherWithReconciliation(repaymentOrder.getFirstContractUuid(),repaymentOrder.getOrderUuid(),paymentOrder.getUuid(), Priority.High.getPriority());
			}catch(Exception e){
				logger.error("repaymentOrderGenerateThirdPartVoucherWithReconciliationTrap error,orderuuid["+repaymentOrder.getOrderUuid()+"],orderUniqueId["+repaymentOrder.getOrderUniqueId()+"],firstContractuuid["+repaymentOrder.getFirstContractUuid()+"],msg:"+ExceptionUtils.getFullStackTrace(e));
			}
		}
	}

	
	/**
	 * 变更 订单 
	 */
	@Override
	public void modify_order_check_and_save_repayment_order_items() {
		
		List<RepaymentOrder> registeredJobTarget = collectNeedToDoJobTarget(JobType.REPAYMENT_ORDER_MODIFY);
		
		HashMap<String,JobContext<RepaymentOrder>> rawDataListOfJob=prepareModifyJobContext(registeredJobTarget);
		
		for(String jobIdentity : rawDataListOfJob.keySet())
		{
			JobContext<RepaymentOrder> context=rawDataListOfJob.get(jobIdentity);
			String repaymentOrderUuid = context.getJobTarget()==null?"":context.getJobTarget().getOrderUuid();
			try
			{
				StageResult<Map>  taskResultForFstStep=performStageInRandom(Step.FST,context,Map.class);
				
				if(taskResultForFstStep.isAllTaskDone()==false) {continue;}
				
				if(taskResultForFstStep.isFailDone()){
					
					throw new JobInterruptException("fst step fail done with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
				
				Map<String,String> syncMarks=new HashMap<>();
				
				while (taskResultForFstStep.hasMoreElements()) {
					
					Map map = taskResultForFstStep.nextElement();
					
					if(null != map){
						
						syncMarks.putAll(map);
					}
				}
				logger.info("result size :"+taskResultForFstStep.getResult().size());
			
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
					
					//第二步成功，才做第三步和第四步 ，第五步不做
					
					//////////////////////////////////////第三步 关联支付单到    变更订单上////////////////////////////////////////
					
					StageResult<Boolean>  taskResultForTrdStep=performStageInCritical(Step.TRD,context,this,Boolean.class);
					
					if(taskResultForTrdStep.isAllTaskDone() == false) {
						continue;
					}
					
					if(taskResultForTrdStep.isFailDone()) {
						
						throw new JobInterruptException("trd step sndStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					boolean trdStepResult = true;
					
					trdStepResult = collectResult(taskResultForTrdStep,
							trdStepResult);
					
					if(trdStepResult == false){
						
						throw new JobInterruptException("trd step trdStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					///////////////////////////////////////// 第四步  作废老订单////////////////////////////////////
					
					StageResult<Boolean>  taskResultForFrthStep=performStageInCritical(Step.FOURTH,context,this,Boolean.class);
					
					if(taskResultForFrthStep.isAllTaskDone() == false) {
						continue;
					}
					
					if(taskResultForFrthStep.isFailDone()) {
						
						throw new JobInterruptException("fourth step trdStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					boolean fourthStepResult = true;
					
					fourthStepResult = collectResult(taskResultForFrthStep,
							fourthStepResult);
					
					if(fourthStepResult == false){
						
						throw new JobInterruptException("fourth step fourthStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
					}
					
					//订单置校验成功
					repaymentOrderService.update_repayment_order_check_status_and_placing_status(context.getJobTarget().getOrderUuid(), OrderCheckStatus.VERIFICATION_SUCCESS);
					
					//订单  解锁
					RepaymentOrder oldRepaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueIdAndNotCreateStatus(context.getJobTarget().getOrderUniqueId());
					if(oldRepaymentOrder == null) continue;
					
					//作废老订单   解锁
					repaymentOrderService.update_repayment_order_lock_and_alive_status(OrderAliveStatus.PAYMENT_ORDER_CANCEL,oldRepaymentOrder.getOrderUuid(),RepaymentOrder.EMPTY,RepaymentOrder.CHECK);
					
					repaymentOrderService.update_repayment_order_lock(context.getJobTarget().getOrderUuid(),RepaymentOrder.EMPTY,RepaymentOrder.CHECK);
					
					//job done
					jobHandler.updateJobDone(context.getJob());
					
					continue;
				}
				
				
				logger.error("valida snd result false.");

				StageResult<Boolean>  taskResultForFifthStep=performStageInCritical(Step.FIFTH,context,syncMarks,this,Boolean.class);
				
				if(taskResultForFifthStep.isAllTaskDone() == false) {
					continue;
				}
				
				if(taskResultForFifthStep.isFailDone()) {
					
					throw new JobInterruptException("fifth step fourthStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
				
				boolean fifthStepResult = true;
				
				fifthStepResult = collectResult(taskResultForFifthStep,
						fifthStepResult);

				if(fifthStepResult == false){
					throw new JobInterruptException("fifth step fifthStepResult is false with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"]");
				}
				repaymentOrderService.update_repayment_order_check_status_and_placing_status(context.getJobTarget().getOrderUuid(), OrderCheckStatus.VERIFICATION_FAILURE);
				
				//校验失败 老订单改为创建状态
				updateRepaymentOrderStatus(context);
				
			}
			catch(JobInterruptException e)
			{
				this.jobHandler.abandonJob(context.job);
				
				logger.error("check_and_save_repayment_order_items occour error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
				
			}
			catch(RedisConnectionFailureException e)
			{
				logger.error("check_and_save_repayment_order_items occour error with jobInterruptException with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
				continue;
			}
			catch(Throwable e)
			{
				
				this.jobHandler.abandonJob(context.job);
				
				logger.error("check_and_save_repayment_order_items occour error with exception with jobIdentity["+jobIdentity+"],repaymentOrderUuid["+repaymentOrderUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
			
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void updateRepaymentOrderStatus(JobContext<RepaymentOrder> context) {
		
		RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueIdAndNotCreateStatus(context.getJobTarget().getOrderUniqueId());
		
		if(repaymentOrder != null) {
			repaymentOrderService.update_repayment_order_lock(context.getJobTarget().getOrderUuid(),RepaymentOrder.EMPTY,RepaymentOrder.CHECK);
			repaymentOrderService.update_repayment_order_lock_and_alive_status(OrderAliveStatus.PAYMENT_ORDER_CREATE,repaymentOrder.getOrderUuid(),RepaymentOrder.EMPTY,RepaymentOrder.CHECK);
		}
		
	}
		
	private	 HashMap<String,JobContext<RepaymentOrder>> prepareModifyJobContext(List<RepaymentOrder> registedJobTarget)
	{
		HashMap<String,JobContext<RepaymentOrder>> mapper=new HashMap<>();
		for (RepaymentOrder repaymentOrder : registedJobTarget) {
			try{

			Job job = jobService.getJobBy(repaymentOrder.getOrderUuid(), JobType.REPAYMENT_ORDER_MODIFY);
			logger.info("#repaymentOrder#prepareJobContext#check_and_save_repayment_order_items repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"].");
				String orderDetailFilePath = repaymentOrder.getOrderDetailFilePath();
				List<RepaymentOrderDetail> repaymentOrderDetailList = new ArrayList<RepaymentOrderDetail>();
				try {
					repaymentOrderDetailList=repaymentOrderDetailCacheHandler.getRepaymentOrderDetailListFromFile(repaymentOrder.getOrderUuid(),orderDetailFilePath);
				} catch (Exception e){
					logger.error("#repaymentOrder#parse items from csv error,repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"],msg:"+ExceptionUtils.getFullStackTrace(e));
					jobHandler.abandonJob(job);
					continue;
				}
				if(CollectionUtils.isEmpty(repaymentOrderDetailList)){
					logger.info("#repaymentOrder#parse items from csv get repaymentOrderDetailList is empty.repaymentOrderUuid["+repaymentOrder.getOrderUuid()+"].");
					jobHandler.abandonJob(job);
					continue;
				}
				
				String financialContractUuid = repaymentOrder.getFinancialContractUuid();
				FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
				if(financialContract==null){
					continue;
				}

				RepaymentOrderDetail firstDetail=repaymentOrderDetailList.get(0);
			
				RepaymentWay repaymentWay=firstDetail.getRepaymentWayEnum();
				String customerSource = getCustomerSource(firstDetail);
				String contractNo = firstDetail.getContractNo();
				String contractUniqueId = firstDetail.getContractUniqueId();

				PlanRepaymentTimeConfiguration planRepaymentTimeConfiguration = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContractUuid);
				
				int repaymentCheckDays=financialContract.getRepaymentCheckDays();
				LedgerBook ledgerBook=ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
				
				//老订单
				RepaymentOrder oldRepaymentOrder = repaymentOrderService.getRepaymentOrderByUniqueIdAndNotCreateStatus(repaymentOrder.getOrderUniqueId());
				if(oldRepaymentOrder == null) continue;
				
				//老订单的parameter
				List<RepaymentOrderModifyParameters> modifyParametersList = new ArrayList<RepaymentOrderModifyParameters>();
				List<String> oldOrderItemUuids = repaymentOrderItemService.getRepaymentOrderItemByOrderUuid(oldRepaymentOrder.getOrderUuid());
				if(CollectionUtils.isNotEmpty(oldOrderItemUuids)){
					for (String itemUuid : oldOrderItemUuids) {
						RepaymentOrderModifyParameters modifyParameters = new RepaymentOrderModifyParameters(oldRepaymentOrder.getOrderUuid(), financialContractUuid, itemUuid);
					 	modifyParametersList.add(modifyParameters);
					}
				}
				
				int size = repaymentOrderDetailList.size();
				List<RepaymentOrderParameters> parameterss = repaymentOrderDetailList.parallelStream().map(repaymentDetail->
						new RepaymentOrderParameters(repaymentOrder, repaymentDetail,repaymentWay,customerSource,repaymentCheckDays,ledgerBook,contractNo,contractUniqueId,size, planRepaymentTimeConfiguration,oldRepaymentOrder.getOrderUuid())).collect(Collectors.toList());
				
				logger.info("#repaymentOrder#prepareModifyJobContext details ids size:"+parameterss.size()+",repaymentOrderUuid:"+repaymentOrder.getOrderUuid());

				JobContext<RepaymentOrder> jobContext=new JobContext<RepaymentOrder>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
				rawDataTable.put(Step.FST, (List)parameterss);

				rawDataTable.put(Step.SND, (List)parameterss);
				
				rawDataTable.put(Step.TRD, (List)parameterss);
				
				rawDataTable.put(Step.FOURTH, (List)modifyParametersList);
				
				rawDataTable.put(Step.FIFTH, (List)parameterss);
				
				jobContext.setJob(job);
				jobContext.setJobTarget(repaymentOrder);
				jobContext.setRawDataTable(rawDataTable);
				mapper.put(job.getUuid(), jobContext);
			} catch (Exception e){
				logger.error("error create repayment_job,repaymentOrderUuid["+(repaymentOrder==null?"":repaymentOrder.getOrderUuid())+"],msg:"+ExceptionUtils.getFullStackTrace(e));
			}
				
		}
		return mapper;
	}
	
	
}
