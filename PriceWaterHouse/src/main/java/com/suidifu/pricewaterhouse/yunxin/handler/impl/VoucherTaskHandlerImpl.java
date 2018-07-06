/**
 *
 */
package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.job.*;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.suidifu.pricewaterhouse.yunxin.handler.VoucherTaskHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentDetailReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wukai
 */
@Component("voucherTaskHandlerNoTransaction")
public class VoucherTaskHandlerImpl extends TaskDeploy implements VoucherTaskHandler, CriticalSecionProjector {

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
	
	private Log logger = LogFactory.getLog(getClass());
	
	private	 HashMap<String,JobContext<SourceDocument>> prepareJobContext(List<SourceDocument> registedJobTarget){

		HashMap<String,JobContext<SourceDocument>> mapper=new HashMap<>();
		for (SourceDocument sourceDocument : registedJobTarget) {
			try{
				logger.info("handler_compensatory_loan_asset_recover sourceDocumentid["+sourceDocument.getSourceDocumentUuid()+"].");
				String sourceDocumentUuid = sourceDocument.getSourceDocumentUuid();
				String financialContractUuid = sourceDocument.getFinancialContractUuid();
				FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
				Voucher voucher = voucherService.get_voucher_by_uuid(sourceDocument.getVoucherUuid());
				List<SourceDocumentDetail> sourceDocumentDetails = new ArrayList<>();
				Boolean isDetaislFile = true;
				if(financialContract==null){
					continue;
				}
				if (StringUtils.isEmpty(voucher.getSourceDocumentDetailsFilePath())){
					isDetaislFile = false;
					sourceDocumentDetails = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocumentUuid);
				}else {
					sourceDocumentDetails = FileUtils.readJsonList(voucher.getSourceDocumentDetailsFilePath(), SourceDocumentDetail.class);
				}

				if (CollectionUtils.isEmpty(sourceDocumentDetails)){
					logger.info("compensatory_loan_recover  get sourceDocumentDetails error sourceDocumentUuid:"+sourceDocumentUuid);
					continue;
				}
				String financialContractNo = financialContract.getContractNo();
				String ledgerBookNo = financialContract.getLedgerBookNo();
				logger.info("compensatory_loan_recover details ids size:"+sourceDocumentDetails.size()+",sourceDocumenId:"+sourceDocument.getId());
				Job job = jobService.getJobBy(sourceDocumentUuid, JobType.Account_Reconciliation_Subrogation);

				JobContext<SourceDocument> jobContext=new JobContext<SourceDocument>();
				HashMap<Step,List<Object>> rawDataTable=new HashMap<>();

				List<SourceDocumentDetailReconciliationParameters> detailReconciliationParameters = new ArrayList<>();
				List<SourceDocumentReconciliationParameters> sndRawDataList = new ArrayList<>();
				List<SourceDocumentReconciliationParameters> fourthRawDataList = new ArrayList<>();

				Boolean finalIsDetaislFile = isDetaislFile;
				sourceDocumentDetails.stream().forEach(s ->{
					if (StringUtils.isEmpty(s.getSourceDocumentUuid())){
						s.setSourceDocumentUuid(sourceDocumentUuid);
					}

					detailReconciliationParameters.add(new SourceDocumentDetailReconciliationParameters(s.getUuid(), s.getContractUniqueId(), s.getFinancialContractUuid()));
					sndRawDataList.add(new SourceDocumentReconciliationParameters(sourceDocumentUuid, s.getUuid(),financialContractNo,ledgerBookNo,s, finalIsDetaislFile));
					fourthRawDataList.add(new SourceDocumentReconciliationParameters(sourceDocumentUuid, s.getUuid(),financialContractNo,ledgerBookNo, s.getSecondType(), finalIsDetaislFile));
				});


				rawDataTable.put(Step.FST, (List)detailReconciliationParameters);

				rawDataTable.put(Step.SND, (List)sndRawDataList);

				List<SourceDocumentReconciliationParameters> thrdDataList=new ArrayList<>();
				SourceDocumentReconciliationParameters trdData=new SourceDocumentReconciliationParameters(sourceDocumentUuid,"",financialContractNo,ledgerBookNo);
				thrdDataList.add(trdData);

				rawDataTable.put(Step.TRD, (List)thrdDataList);

				rawDataTable.put(Step.FOURTH, (List)fourthRawDataList);
				//第五步数据与第三步一致
				rawDataTable.put(Step.FIFTH, (List)thrdDataList);

				jobContext.setJob(job);
				jobContext.setJobTarget(sourceDocument);
				jobContext.setRawDataTable(rawDataTable);
				mapper.put(job.getUuid(), jobContext);
			}catch (Exception e){
				e.printStackTrace();
				logger.error("sourceDocumentid["+(sourceDocument==null?"":sourceDocument.getSourceDocumentUuid())+"]"+ExceptionUtils.getFullStackTrace(e));

			}
		}
		return mapper;
	}


	@Override
	public void handler_compensatory_loan_asset_recover(){

			registSubrogationJob();

			List<SourceDocument> registedJobTarget = collectNeedToDoJobTarget();

			HashMap<String,JobContext<SourceDocument>> rawDataListOfJob=prepareJobContext(registedJobTarget);
			long startTime_allJobs = System.currentTimeMillis();
			for(String jobIdentity : rawDataListOfJob.keySet()){

				long startTime = System.currentTimeMillis();
				JobContext<SourceDocument> context=rawDataListOfJob.get(jobIdentity);

				try{

					StageResult<Map>  taskResultForFstStep=performStageInRandom(Step.FST,context,Map.class);
					
					if(taskResultForFstStep.isAllTaskDone()==false) {continue;}
					
					if(taskResultForFstStep.isFailDone()){
						
						throw new JobInterruptException("fst step fail done with jobIdentity["+jobIdentity+"]");
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
						
						throw new JobInterruptException("snd step fail done with jobIdentity["+jobIdentity+"]");
					}
					
					boolean sndStepResult = true;
					
					
					sndStepResult = collectResult(taskResultForSndStep,sndStepResult);
					
					if(sndStepResult == false){
						
						logger.error("valida result false.");
						// TODO 跟新voucher状态
						voucherService.update_checkState_voucherState(context.getJobTarget().getVoucherUuid(), SourceDocumentDetailCheckState.CHECK_FAILS, null);
						
						throw new JobInterruptException("snd step sndStepResult is false with jobIdentity["+jobIdentity+"]");
					}
					
					//更新凭证校验状态
					voucherService.update_checkState_voucherState(context.getJobTarget().getVoucherUuid(), SourceDocumentDetailCheckState.CHECK_SUCCESS, null);
					
					StageResult<Boolean>  taskResultForTrdStep=performStageInCritical(Step.TRD,context,this,Boolean.class);
					
					if(taskResultForTrdStep.isAllTaskDone() == false) {
						continue;
					}
					
					if(taskResultForTrdStep.isFailDone()) {
						
						throw new JobInterruptException("trd step sndStepResult is false with jobIdentity["+jobIdentity+"]");
					}
					
					boolean trdStepResult = true;
					
					trdStepResult = collectResult(taskResultForTrdStep,
							trdStepResult);
					
					if(trdStepResult == false){
						
						throw new JobInterruptException("trd step trdStepResult is false with jobIdentity["+jobIdentity+"]");
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
						throw new JobInterruptException("fifth step sndStepResult is false with jobIdentity["+jobIdentity+"]");
					}
					boolean fifthStepResult = true;
					fifthStepResult = collectResult(taskResultForFifthStep,
							fifthStepResult);
					if(fifthStepResult == false){
						throw new JobInterruptException("fifth step trdStepResult is false with jobIdentity["+jobIdentity+"]");
					}
					
					SourceDocument jobTarget = context.getJobTarget();
					BigDecimal totalIssuedAmount = sourceDocumentDetailService.getTotalAmountOfSourceDocumentDetail(jobTarget.getSourceDocumentUuid());

                sourceDocumentService.update_after_inter_account_transfer(jobTarget.getSourceDocumentUuid(), totalIssuedAmount);
                voucherHandler.refresh_active_voucher(jobTarget.getVoucherUuid());
            } catch (JobInterruptException e) {
                this.jobHandler.abandonJob(context.job);

                logger.error("handler_compensatory_loan_asset_recover occour error with jobInterruptException with jobIdentity[" + jobIdentity + "],eception stack trace :" + ExceptionUtils.getStackTrace(e));

            } catch (RedisConnectionFailureException e) {
                logger.error("handler_compensatory_loan_asset_recover occour error with jobInterruptException with jobIdentity[" + jobIdentity + "],eception stack trace :" + ExceptionUtils.getStackTrace(e));
                continue;
            } catch (Throwable e) {

                this.jobHandler.abandonJob(context.job);

                logger.error("handler_compensatory_loan_asset_recover occour error with exception with jobIdentity[" + jobIdentity + "],eception stack trace :" + ExceptionUtils.getStackTrace(e));

                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            logger.info("handler_compensatory_loan_asset_recover job current exe,use time[" + (endTime - startTime) + "]ms, jobIdentity[" + jobIdentity + "]");

        }

        long endTime_allJobs = System.currentTimeMillis();
        logger.info("handler_compensatory_loan_asset_recover job current exe,use time[" + (endTime_allJobs - startTime_allJobs) + "]ms, jobsize[" + rawDataListOfJob.keySet().size() + "]");
    }


    private List<SourceDocument> collectNeedToDoJobTarget() {

        List<SourceDocument> sourceDocuments = new ArrayList<SourceDocument>();

        List<Job> createdOrProcessingJobList = jobService.loadAllCreateOrProcessingJobBy(JobType.Account_Reconciliation_Subrogation);

        logger.info("#createdOrProcessingJobList# create or processing job size[" + createdOrProcessingJobList.size() + "]");

        for (Job job : createdOrProcessingJobList) {

            String fstBusinessCode = job.getFstBusinessCode();
            //TODO
            SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(fstBusinessCode);
            if (sourceDocument == null) {
                logger.info("#createdOrProcessingJobList# create or processing,fstBusinessCode[" + fstBusinessCode + "]");
                continue;
            }
            sourceDocuments.add(sourceDocument);

        }

        logger.info("#createdOrProcessingJobList# need to resolve sourceDocuments size[" + sourceDocuments.size() + "]");

        return sourceDocuments;
    }



	
	
	private void registSubrogationJob() {
		
		List<FastVoucher> needToWriteOffVouches = voucherService.getAllNeedToWriteOffVouchers();
		
		logger.info("#get_deposit_source_document_list_connected_by# needToWriteOffVouches size["+needToWriteOffVouches.size()+"]");
		
		for (FastVoucher fastVoucher : needToWriteOffVouches) {
			
			try {
				//voucher的uuid查询等于voucherUUid的sourceDocumentDetail的sourceDocumentUuid
				String sourceDocumentUuid = sourceDocumentService.getSourceDocumentUuidByCashFlowUuid(fastVoucher.getCashFlowUuid(), fastVoucher.getFinancialContractUuid());

                if (StringUtils.isEmpty(sourceDocumentUuid)) {
                    continue;
                }

                JobType jobTypeForSubrogation = JobType.Account_Reconciliation_Subrogation;

                String firstNo = fastVoucher.getFirstNo();

                Job job = jobService.getAllJobBy(sourceDocumentUuid, firstNo, StringUtils.EMPTY, jobTypeForSubrogation);

                if (JobFactory.isJobDone(job) || JobFactory.isJobAbandon(job)) {

                    continue;
                }
                if (null == job) {
                    logger.info("begin to registe job with sourceDocumentUuid[" + sourceDocumentUuid + "]");

                    Job jobForSubrogation = jobHandler.registeJob(sourceDocumentUuid, firstNo, StringUtils.EMPTY, jobTypeForSubrogation);

                    String beanNameForSubrogation = "dstJobSourceDocumentReconciliationNewSyncProxy";

                    String fstStageMethodName = "criticalMarker";
                    String sndStageMethodName = "validateSourceDocumentDetailList";
                    String trdStageMethodName = "fetchVirtualAccountAndBusinessPaymentVoucherTransfer";
                    String fourthStageMethodName = "sourceDocumentRecoverDetails";
                    String fifthStageMethodName = "unfreezeCapital";

                    jobHandler.registeStage(jobForSubrogation, Step.FST, beanNameForSubrogation, fstStageMethodName, 20, Priority.Low);
                    jobHandler.registeStage(jobForSubrogation, Step.SND, beanNameForSubrogation, sndStageMethodName, 20, Priority.Low);
                    jobHandler.registeStage(jobForSubrogation, Step.TRD, beanNameForSubrogation, trdStageMethodName, 20, Priority.Low);
                    jobHandler.registeStage(jobForSubrogation, Step.FOURTH, beanNameForSubrogation, fourthStageMethodName, 20, Priority.Low);
                    jobHandler.registeStage(jobForSubrogation, Step.FIFTH, beanNameForSubrogation, fifthStageMethodName, 20, Priority.Low);

                    jobHandler.registeJobStage(jobForSubrogation);

                    logger.info("end to registe job with sourceDocumentUuid[" + sourceDocumentUuid + "]");
                }
            } catch (Exception e) {

                logger.error("#registSubrogationJob# occur error with voucherUuid[" + fastVoucher.getUuid() + "] ,full stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");

                continue;
            }

        }

    }

    @Override
    public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
            List<Object> rawDataLists) throws JobInterruptException {
        List<CriticalMarkedData<?>> critialDataList = new ArrayList();

        for (Object params : rawDataLists) {
            SourceDocumentReconciliationParameters subrogationReconciliationParameters = (SourceDocumentReconciliationParameters) params;
            String sourceDocumentUuid = subrogationReconciliationParameters.getSourceDocumentUuid();
            //TODO
            SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
            CriticalMarkedData<SourceDocumentReconciliationParameters> syncMarkedData = new CriticalMarkedData<>();
            syncMarkedData.setCritialMark(sourceDocument.getFinancialContractUuid());
            syncMarkedData.setTaskParams(subrogationReconciliationParameters);
            critialDataList.add(syncMarkedData);
        }
        return critialDataList;
    }


    @Override
    public List<CriticalMarkedData<?>> projectDataToCriticalSecion(
            List<Object> rawDataList, Map<String, String> critialMarks) throws JobInterruptException {
        List<CriticalMarkedData<?>> criticalDataList = new ArrayList();
        int i = 0;
        for (Object params : rawDataList) {
            SourceDocumentReconciliationParameters subrogationReconciliationParameters = (SourceDocumentReconciliationParameters) params;
            String sourceDetailuuid = subrogationReconciliationParameters.getSourceDocumentDetailUuid();
            String contractUuid = critialMarks.get(sourceDetailuuid);
            CriticalMarkedData<SourceDocumentReconciliationParameters> syncMarkedData = new CriticalMarkedData<>();

            if (StringUtils.isEmpty(contractUuid)) {
                logger.error("this sourceDetailuuid[" + sourceDetailuuid + "] reference contractUuid is null");
                throw new JobInterruptException("job target[" + sourceDetailuuid + "] criticalMark missing");
            }

            syncMarkedData.setCritialMark(contractUuid);
            syncMarkedData.setTaskParams(subrogationReconciliationParameters);
            criticalDataList.add(syncMarkedData);
        }
        return criticalDataList;

    }


}
