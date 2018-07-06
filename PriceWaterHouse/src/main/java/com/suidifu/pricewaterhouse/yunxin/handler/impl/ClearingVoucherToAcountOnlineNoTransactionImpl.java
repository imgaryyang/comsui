package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobFactory;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.job.StageResult;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.ClearingVoucherToAcountOnlineNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.entity.account.special.account.ClearingExecLog;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.SpecialAccountTaskHandlerModel;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.entity.model.AssetClearingReconParameters;
import com.zufangbao.sun.yunxin.entity.model.SpecialAccountPrepareParamsModel;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountNoTransaction;

@Component("clearingVoucherToAcountOnlineNoTransaction")
public class ClearingVoucherToAcountOnlineNoTransactionImpl extends TaskDeploy implements ClearingVoucherToAcountOnlineNoTransaction, CriticalSecionProjector {

	@Autowired
	private ClearingExecLogService clearingExecLogService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private SpecialAccountNoTransaction specialAccountNoTransaction;
	
	@Override
	public void handlerClearingVoucherToAcountOnline() {
		registSubrogationJob();
		
		List<SpecialAccountTaskHandlerModel> registedJobTarget = collectNeedToDoJobTarget();
		
		HashMap<String,JobContext<SpecialAccountTaskHandlerModel>> rawDataListOfJob=prepareJobContext(registedJobTarget);
		
		for(String jobIdentity : rawDataListOfJob.keySet()){
			JobContext<SpecialAccountTaskHandlerModel> context=rawDataListOfJob.get(jobIdentity);
			try {
				
				Map<String,String> syncMarks=context.getSyncMarks();
				if(MapUtils.isEmpty(syncMarks)){
					throw new JobInterruptException("syncMarks is empty with jobIdentity(bacthuuid)["+jobIdentity+"]");
				}
				
				logger.info("result size :"+syncMarks.size());
				StageResult<Boolean>  taskResultForFstStep=performStageInCritical(Step.FST,context,syncMarks,this,Boolean.class);
				if(taskResultForFstStep.isAllTaskDone() == false) {
					continue;
				}
				if(taskResultForFstStep.isFailDone()) {
					
					throw new JobInterruptException("fst step initialization is false with jobIdentity(batch)["+jobIdentity+"]");
				}
				
				
				boolean fstStepResult = true;
				
				fstStepResult = collectResult(taskResultForFstStep,fstStepResult);
				
				if(fstStepResult == false){
					
					throw new JobInterruptException("fst step fstStepResult is false with jobIdentity(batchuuid)["+jobIdentity+"]");
				}
				StageResult<Boolean>  taskResultForSndStep=performStageInCritical(Step.SND,context,this,Boolean.class);
				if(taskResultForSndStep.isAllTaskDone() == false) {
					continue;
				}
				
				if(taskResultForSndStep.isFailDone()) {
					
					throw new JobInterruptException("snd step fstStepResult is false with jobIdentity(batch)["+jobIdentity+"]");
				}
				
				boolean sndStepResult = true;
				
				sndStepResult = collectResult(taskResultForSndStep,sndStepResult);
				
				if(sndStepResult == false){
					throw new JobInterruptException("snd step fstStepResult is false with jobIdentity(batchuuid)["+jobIdentity+"]");
				}
				
			}catch (Exception e) {
				this.jobHandler.abandonJob(context.job);
				
				logger.error("handle_online_special_account_book_accounts occour error with exception with jobIdentity(batchuuid)["+jobIdentity+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
			
				e.printStackTrace();	
			}catch (Throwable e) {
				this.jobHandler.abandonJob(context.job);
				
				logger.error("handle_online_special_account_book_accounts occour error with exception with jobIdentity(batchuuid)["+jobIdentity+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
			
				e.printStackTrace();
			}
		}

	}
	
	private void registSubrogationJob(){
		List<SpecialAccountPrepareParamsModel> specialAccountPrepareParamsModelList = specialAccountNoTransaction.prepareClearingExecLogForClearingVoucher();
		
		for(SpecialAccountPrepareParamsModel specialAccountPrepareParamsModel : specialAccountPrepareParamsModelList){
			if(specialAccountPrepareParamsModel == null){
				continue;
			}
			JobType jobTypeForSubrogation = JobType.CLEARING_VOUCHER_ACCOUNT_RECORD;
		try{
         
			Job job = jobService.getAllJobBy(specialAccountPrepareParamsModel.getBatchUuid(), specialAccountPrepareParamsModel.getFinancialContratUuid(), specialAccountPrepareParamsModel.getClearingVoucherUuid(), jobTypeForSubrogation);
			if (JobFactory.isJobDone(job) || JobFactory.isJobAbandon(job)) {
               continue;
           	}
           	
          if (null == job) {
               logger.info("begin to registe job with batchUuid[" + specialAccountPrepareParamsModel.getBatchUuid() + "]" );

               Job jobForSubrogation = jobHandler.registeJob(specialAccountPrepareParamsModel.getBatchUuid(), specialAccountPrepareParamsModel.getFinancialContratUuid(), specialAccountPrepareParamsModel.getClearingVoucherUuid(), jobTypeForSubrogation);
               String beanNameForSubrogation = "dstJobClearingVoucherAssetReconciliation";
           	
               String fstStageMethodName = "contract_clearing";
               String sndStageMethodName = "update_financial_contract_account";
       

               jobHandler.registeStage(jobForSubrogation, Step.FST, beanNameForSubrogation, fstStageMethodName, 20, Priority.Low);
               jobHandler.registeStage(jobForSubrogation, Step.SND, beanNameForSubrogation, sndStageMethodName, 20, Priority.Low);
             
               jobHandler.registeJobStage(jobForSubrogation);

               logger.info("end to registe job with batchUuid[" + specialAccountPrepareParamsModel.getBatchUuid() + "]" );
	           }
       
			} catch (Exception e) {

				logger.error("#registSubrogationJob# occur error with bacthUuid[" + specialAccountPrepareParamsModel.getBatchUuid() + "] ,full stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
				continue;
			}

		}
	}


	private List<SpecialAccountTaskHandlerModel> collectNeedToDoJobTarget(){
		List<SpecialAccountTaskHandlerModel> specialAccountTaskHandlerModels = new ArrayList<>();
		
		List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(JobType.CLEARING_VOUCHER_ACCOUNT_RECORD);
		
		logger.info("#createdOrProcessingJobList# create or processing job size["+createdOrProcessingJobList.size()+"]");
		
		for (Job job : createdOrProcessingJobList) {
			SpecialAccountTaskHandlerModel specialAccountTaskHandlerModel = new SpecialAccountTaskHandlerModel(job.getFstBusinessCode());
			specialAccountTaskHandlerModels.add(specialAccountTaskHandlerModel);
		}
		logger.info("#createdOrProcessingJobList# need to resolve batchUuids size["+specialAccountTaskHandlerModels.size()+"]");
		return specialAccountTaskHandlerModels;
	}
	
	private HashMap<String, JobContext<SpecialAccountTaskHandlerModel>> prepareJobContext(List<SpecialAccountTaskHandlerModel> specialAccountTaskHandlerModelList){
		HashMap<String,JobContext<SpecialAccountTaskHandlerModel>> rawDataListOfJob = new HashMap<>();
		if(CollectionUtils.isEmpty(specialAccountTaskHandlerModelList)){
			return rawDataListOfJob;
		}
		for(SpecialAccountTaskHandlerModel specialAccountTaskHandlerModel : specialAccountTaskHandlerModelList){
			logger.info("handle_special_account_book_accounts prepareJobContext batchUuid["+specialAccountTaskHandlerModel.getBatchUuid()+"]" );
			String batchUuid = specialAccountTaskHandlerModel.getBatchUuid();
			
			List<ClearingExecLog> clearingExecLogOfClearingList = clearingExecLogService.queryNeedToRecordOfNeedClear(
					Arrays.asList(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER.ordinal()), batchUuid);
			
			FinancialContract financialContract = financialContractService.getFinancialContractBy(clearingExecLogOfClearingList.get(0).getFinancialContractUuid());
			JobContext<SpecialAccountTaskHandlerModel> jobContext=new JobContext<>();
			
			Job job = jobService.getJobBy(batchUuid, JobType.CLEARING_VOUCHER_ACCOUNT_RECORD);
			
			Map<String, String> syncMarks = new HashMap<>();
			List<AssetClearingReconParameters> assetClearingReconParametersList = new ArrayList<>();
			
			for(ClearingExecLog clearingExecLog : clearingExecLogOfClearingList){
				syncMarks.put(clearingExecLog.getUuid(), StringUtils.isEmpty(clearingExecLog.getContractUuid())?clearingExecLog.getFinancialContractUuid():clearingExecLog.getContractUuid());
				AssetClearingReconParameters assetClearingReconParameters = new AssetClearingReconParameters(clearingExecLog.getUuid(),clearingExecLog.getFinancialContractUuid(),financialContract.getLedgerBookNo(),batchUuid);
				assetClearingReconParametersList.add(assetClearingReconParameters);
			}
			
			HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
			
			rawDataTable.put(Step.FST, (List) assetClearingReconParametersList);
			
			rawDataTable.put(Step.SND, Arrays.asList(assetClearingReconParametersList.get(0)));
			
			jobContext.setJob(job);
			jobContext.setJobTarget(specialAccountTaskHandlerModel);
			jobContext.setSyncMarks(syncMarks);
			jobContext.setRawDataTable(rawDataTable);
			rawDataListOfJob.put(batchUuid, jobContext);
		}
		
		return rawDataListOfJob;
		
	}

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataLists) throws JobInterruptException {
		List<CriticalMarkedData<?>> critialDataList=new ArrayList<>();
		
		for(Object params:rawDataLists)
		{
			AssetClearingReconParameters specialAccountReconciliationParameters=(AssetClearingReconParameters)params;
			
			CriticalMarkedData<AssetClearingReconParameters> syncMarkedData =new CriticalMarkedData<>();
			syncMarkedData.setCritialMark(specialAccountReconciliationParameters.getFinancialContractUuid());
			syncMarkedData.setTaskParams(specialAccountReconciliationParameters);
			critialDataList.add(syncMarkedData);
		}
		return critialDataList;
	}

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataList,
			Map<String, String> critialMarks) throws JobInterruptException {
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList<>();
		int i = 0;
		for(Object params:rawDataList)
		{
			AssetClearingReconParameters specialAccountReconciliationParameters=(AssetClearingReconParameters)params;
			CriticalMarkedData<AssetClearingReconParameters> syncMarkedData =new CriticalMarkedData<>();
			String contractUuid = critialMarks.get(specialAccountReconciliationParameters.getClearingExecLogUuid());
			if(StringUtils.isEmpty(contractUuid)){
				logger.error("this batchUuid["+specialAccountReconciliationParameters.getBatchUuid()+"] reference contractUuid is null");
				throw new JobInterruptException("job target["+specialAccountReconciliationParameters.getBatchUuid()+"] criticalMark missing"); 
			}
			
			syncMarkedData.setCritialMark(contractUuid);
			syncMarkedData.setTaskParams(specialAccountReconciliationParameters);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
	}

}
