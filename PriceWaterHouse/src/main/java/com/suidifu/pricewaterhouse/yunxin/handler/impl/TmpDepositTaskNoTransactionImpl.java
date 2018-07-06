/**
 * 
 */
package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.StageResult;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.suidifu.pricewaterhouse.yunxin.handler.TmpDepositTaskNoTransaction;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.entity.voucher.SourceDocumentDetailSimpleInfo;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconTarget;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
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
 * @author wukai
 *
 */
@Component("tmpDepositTaskNoTransactionImpl")
public class TmpDepositTaskNoTransactionImpl extends TaskDeploy  implements TmpDepositTaskNoTransaction,CriticalSecionProjector{

	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private TemporaryDepositDocService temporaryDepositDocService;
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private VoucherHandler voucherHandler;
	
	private Log logger = LogFactory.getLog(getClass());
	
	private	 HashMap<String,JobContext<TmpDepositReconTarget>> prepareJobContext(List<TmpDepositReconTarget> registedJobTarget)
	{
		HashMap<String,JobContext<TmpDepositReconTarget>> mapper=new HashMap<>();
		for (TmpDepositReconTarget tmpDepositReconTarget : registedJobTarget) {
			
				logger.info("handler_compensatory_loan_asset_recover TmpDepositDocUuid["+tmpDepositReconTarget.getTmpDepositDocUuid()+"].");
				TemporaryDepositDoc tmpDepositDoc = tmpDepositReconTarget.getTemporaryDepositDoc();
				String sourceDocumentUuid = tmpDepositDoc.getSourceDocumentUuid();
				String financialContractUuid = tmpDepositDoc.getFinancialContractUuid();
				FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
				if(financialContract==null){
					continue;
				}
				String financialContractNo = financialContract.getContractNo();
				String ledgerBookNo = financialContract.getLedgerBookNo();
				
				List<SourceDocumentDetailSimpleInfo> detailInfos = sourceDocumentDetailService.getDetailUuidByVoucherUuid(tmpDepositDoc.getVoucherUuid(),tmpDepositDoc.getUuid(),tmpDepositReconTarget.getRecoverSecondNo());
				logger.info("compensatory_loan_recover details ids size:"+detailInfos.size()+",tmpDepositDocUuid:"+tmpDepositDoc.getUuid());
				Job job = jobService.getJobBy(tmpDepositDoc.getUuid(), JobType.TMP_DEPOSIT_VOUCHER_RECOVER);
				
				JobContext<TmpDepositReconTarget> jobContext=new JobContext<TmpDepositReconTarget>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();
				List<String> detailUuids = detailInfos.stream().map(info->info.getSourceDocumentDetailUuid()).collect(Collectors.toList());
				rawDataTable.put(Step.FST, (List)detailUuids);
				BigDecimal detailTotalAmount = detailInfos.stream().map(info->info.getAmount()).reduce(BigDecimal.ZERO,BigDecimal::add);
				
				List<TmpDepositReconciliationParameters> rawDataList=detailInfos.stream().map(info->{
					return new TmpDepositReconciliationParameters(sourceDocumentUuid,info.getSourceDocumentDetailUuid(),financialContractNo,ledgerBookNo,tmpDepositDoc.getUuid(),info.getSecondType(), detailTotalAmount, tmpDepositReconTarget.getRecoverSecondNo());
					}
						).collect(Collectors.toList());
				rawDataTable.put(Step.SND, (List)rawDataList);
				
				List<TmpDepositReconciliationParameters> thrdDataList=new ArrayList<>();
				TmpDepositReconciliationParameters trdData=new TmpDepositReconciliationParameters(sourceDocumentUuid,"",financialContractNo,ledgerBookNo,tmpDepositDoc.getUuid(), detailTotalAmount, tmpDepositReconTarget.getRecoverSecondNo());
				thrdDataList.add(trdData);
				
				rawDataTable.put(Step.TRD, (List)thrdDataList);
				
				rawDataTable.put(Step.FOURTH, (List)rawDataList);
				//第五步数据与第三步一致
				rawDataTable.put(Step.FIFTH, (List)thrdDataList);
				
				jobContext.setJob(job);
				jobContext.setJobTarget(tmpDepositReconTarget);
				jobContext.setRawDataTable(rawDataTable);
				mapper.put(job.getUuid(), jobContext);
				
		}
		return mapper;
	}
	
	
	@Override
	public void handler_compensatory_loan_asset_recover(){
			
			List<TmpDepositReconTarget> registedJobTarget = collectNeedToDoJobTarget();
			
			HashMap<String,JobContext<TmpDepositReconTarget>> rawDataListOfJob=prepareJobContext(registedJobTarget);
			long startTime_allJobs = System.currentTimeMillis();
			for(String jobIdentity : rawDataListOfJob.keySet())
			{
				long startTime = System.currentTimeMillis();
				JobContext<TmpDepositReconTarget> context=rawDataListOfJob.get(jobIdentity);
				String tmpDepositDocUuid= context.getJobTarget()==null?"":context.getJobTarget().getTmpDepositDocUuid();
				try
				{
					StageResult<Map>  taskResultForFstStep=performStageInRandom(Step.FST,context,Map.class);
					
					if(taskResultForFstStep.isAllTaskDone()==false) {continue;}
					
					if(taskResultForFstStep.isFailDone()){
						
						throw new JobInterruptException("fst step fail done with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
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
						
						throw new JobInterruptException("snd step fail done with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
					}
					
					boolean sndStepResult = true;
					
					
					sndStepResult = collectResult(taskResultForSndStep,sndStepResult);
					
					if(sndStepResult == false){
						
						logger.error("valida result false,jobIdentity["+jobIdentity+"].");
						
						throw new JobInterruptException("snd step sndStepResult is false with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
					}
					
					StageResult<Boolean>  taskResultForTrdStep=performStageInCritical(Step.TRD,context,this,Boolean.class);
					
					if(taskResultForTrdStep.isAllTaskDone() == false) {
						continue;
					}
					
					if(taskResultForTrdStep.isFailDone()) {
						
						throw new JobInterruptException("trd step sndStepResult is false with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
					}
					
					boolean trdStepResult = true;
					
					trdStepResult = collectResult(taskResultForTrdStep,
							trdStepResult);
					
					if(trdStepResult == false){
						
						throw new JobInterruptException("trd step trdStepResult is false with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
					}
					
					StageResult<Boolean>  taskResultForFrthStep=performStageInCritical(Step.FOURTH,context,syncMarks,this,Boolean.class);
							
					if(taskResultForFrthStep.isAllTaskDone() == false) {
						
						continue;
					}
					
					boolean fourthStepResult = true;
					
					fourthStepResult = collectResult(taskResultForFrthStep,
							fourthStepResult);
					
					
					StageResult<Boolean>  taskResultForFifthStep=performStageInCritical(Step.FIFTH,context,this,Boolean.class);
					if(taskResultForFifthStep.isAllTaskDone() == false) {
						continue;
					}
					if(taskResultForFifthStep.isFailDone()) {
						throw new JobInterruptException("fifth step sndStepResult is false with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
					}
					boolean fifthStepResult = true;
					fifthStepResult = collectResult(taskResultForFifthStep,
							fifthStepResult);
					if(fifthStepResult == false){
						throw new JobInterruptException("fifth step trdStepResult is false with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
					}
					
					TmpDepositReconTarget jobTarget = context.getJobTarget();
					TemporaryDepositDoc temporaryDepositDoc = jobTarget.getTemporaryDepositDoc();
					BigDecimal totalIssuedAmount = sourceDocumentDetailService.getTotalAmountOfSourceDocumentDetail(temporaryDepositDoc.getSourceDocumentUuid());
					sourceDocumentService.update_after_inter_account_transfer(temporaryDepositDoc.getSourceDocumentUuid(),totalIssuedAmount);
					voucherHandler.refresh_active_voucher(temporaryDepositDoc.getVoucherUuid());
				}
				catch(JobInterruptException e)
				{
					this.jobHandler.abandonJob(context.job);
					
					logger.error("handler_tmp_deposit_doc_compensatory_loan_asset_recover occour error with jobInterruptException with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
					
				}
				catch(Throwable e)
				{
					
					this.jobHandler.abandonJob(context.job);
				
					logger.error("handler_tmp_deposit_doc_compensatory_loan_asset_recover occour error with exception with jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"],eception stack trace :"+ExceptionUtils.getStackTrace(e));
				
					e.printStackTrace();
				}
				long endTime = System.currentTimeMillis();
				logger.info("handler_tmp_deposit_doc_compensatory_loan_asset_recover job current exe,use time["+(endTime-startTime)+"]ms, jobIdentity["+jobIdentity+"],tmpDepositDocUuid["+tmpDepositDocUuid+"]");
				
			}
			
			long endTime_allJobs = System.currentTimeMillis();
			logger.info("handler_tmp_deposit_doc_compensatory_loan_asset_recover job current exe,use time["+(endTime_allJobs-startTime_allJobs)+"]ms, jobsize["+rawDataListOfJob.keySet().size()+"]");
	}


	private List<TmpDepositReconTarget> collectNeedToDoJobTarget() {
		
		List<TmpDepositReconTarget> temporaryDepositTargetList = new ArrayList<TmpDepositReconTarget>();
		
		List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(JobType.TMP_DEPOSIT_VOUCHER_RECOVER);
		
		logger.info("#createdOrProcessingJobList# create or processing job size["+createdOrProcessingJobList.size()+"]");
		
		for (Job job : createdOrProcessingJobList) {
			TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocService.getTemporaryDepositDocBy(job.getFstBusinessCode());
			if(temporaryDepositDoc==null){
				continue;
			}
			TmpDepositReconTarget target = new TmpDepositReconTarget(temporaryDepositDoc,job.getSndBusinessCode());
			temporaryDepositTargetList.add(target);
		}
		
		logger.info("#createdOrProcessingJobList# need to resolve temporaryDepositDocList size["+temporaryDepositTargetList.size()+"]");
		
		return temporaryDepositTargetList;
	}

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
			List<Object> rawDataLists) throws JobInterruptException {
		List<CriticalMarkedData<?>> critialDataList=new ArrayList();
		
		for(Object params:rawDataLists)
		{
			TmpDepositReconciliationParameters subrogationReconciliationParameters=(TmpDepositReconciliationParameters)params;
			String sourceDocumentUuid=subrogationReconciliationParameters.getSourceDocumentUuid();
			//TODO
			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
			CriticalMarkedData<TmpDepositReconciliationParameters> syncMarkedData =new CriticalMarkedData<>();
			syncMarkedData.setCritialMark(sourceDocument.getFinancialContractUuid());
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
			TmpDepositReconciliationParameters subrogationReconciliationParameters=(TmpDepositReconciliationParameters)params;
			String sourceDetailuuid=subrogationReconciliationParameters.getSourceDocumentDetailUuid();
			String contractUuid=critialMarks.get(sourceDetailuuid);
			CriticalMarkedData<TmpDepositReconciliationParameters> syncMarkedData =new CriticalMarkedData<>();
			
			if(StringUtils.isEmpty(contractUuid)){
				logger.error("this sourceDetailuuid["+sourceDetailuuid+"] reference contractUuid is null");
				throw new JobInterruptException("job target["+sourceDetailuuid+"] criticalMark missing"); 
			}
			
			syncMarkedData.setCritialMark(contractUuid);
			syncMarkedData.setTaskParams(subrogationReconciliationParameters);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
		
	}


}
