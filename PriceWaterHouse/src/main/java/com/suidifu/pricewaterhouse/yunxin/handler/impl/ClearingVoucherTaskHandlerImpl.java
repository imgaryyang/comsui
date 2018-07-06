package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobFactory;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.job.StageResult;
import com.suidifu.hathaway.job.Step;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.pricewaterhouse.yunxin.handler.ClearingVoucherTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.utils.AmountUtil;
import com.zufangbao.sun.utils.ClearingVoucherParameters;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.AuditResult;
import com.zufangbao.sun.yunxin.entity.audit.AuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.ClearingStatus;
import com.zufangbao.sun.yunxin.entity.audit.CommandAuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.service.ClearingVoucherService;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import com.zufangbao.sun.yunxin.entity.audit.ClearSpecialAccountType;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucherStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.TotalReceivableBillsHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

@Component("clearingVoucherTaskHandlerNoTransaction")
public class ClearingVoucherTaskHandlerImpl extends TaskDeploy
		implements CriticalSecionProjector, ClearingVoucherTaskHandler {

	@Autowired
	private AuditJobService auditJobService;

	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;

	@Autowired
	
	private DeductPlanService deductPlanService;
	@Autowired
	private CashFlowService cashFlowService;
	
	@Autowired
	private CashFlowHandler cashFlowHandler;
	
	@Autowired 
	private TotalReceivableBillsHandler totalReceivableBillsHandler;
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private BeneficiaryAuditResultService beneficiaryAuditResultService;
	
	@Autowired
	private ClearingVoucherService clearingVoucherService;

	@Override
	public void handleCreateClearingDeductPlanJvAndLedger() {
		registerJob();
		List<Job> jobs = jobService.loadAllCreateOrProcessingJobBy(JobType.Deduct_Plan_Clearing);
		Map<String, JobContext<AuditJob>> jobContextMap = prepareAllJobContext(jobs);
		if (MapUtils.isEmpty(jobContextMap)) {
			return;
		}
		for (String string : jobContextMap.keySet()) {
			JobContext<AuditJob> context = jobContextMap.get(string);
			Job job = context.getJob();
//			//自动绑定流水
//			if(autoCashFlowReTouching(job) == false){
//				continue;
//			};
			try {
				@SuppressWarnings("rawtypes")
				StageResult<Map> taskResultForFstStep = performStageInRandom(Step.FST, context, Map.class);
				if (!taskResultForFstStep.isAllTaskDone()) {
					continue;
				}
				if (taskResultForFstStep.isFailDone()) {
					throw new JobInterruptException("fst step fail done with jobIdentity[" + job.getUuid() + "]");
				}

				logger.info("result size :" + taskResultForFstStep.getResult().size());

				StageResult<Boolean> taskResultForSndStep = performStageInCritical(Step.SND, context,
						getSyncMarks(taskResultForFstStep), this, Boolean.class);

				logger.info("snd step task result[" + taskResultForSndStep.isAllTaskSucDone() + "]");

				if (taskResultForSndStep.isAllTaskDone() == false) {
					continue;
				}
				if (taskResultForSndStep.isFailDone()) {

					throw new JobInterruptException("snd step fail done with jobIdentity[" + job.getUuid() + "]");
				}
				boolean sndStepResult = true;

				sndStepResult = collectResult(taskResultForSndStep,sndStepResult);

				if (!sndStepResult) {

					throw new JobInterruptException("snd result is false with jobIdentity[" + job.getUuid() + "]");
				}

				AuditJob auditJob = context.getJobTarget();
				if(ClearingStatus.DONE == auditJob.getClearingStatus()){
					continue;
				}
				auditJob.setClearingStatus(ClearingStatus.DONE);
				auditJob.setLastModifiedTime(new Date());
				auditJobService.saveOrUpdate(auditJob);
				updateClearingVoucherStatus(auditJob);
			} catch (JobInterruptException e) {
				this.jobHandler.abandonJob(job);
				logger.error(
						"handleAssetValuationAndCreateNormalOrder occur error with jobInterruptException with jobIdentity["
								+ job.getUuid() + "],exception stack trace :" + ExceptionUtils.getStackTrace(e));
			}
			catch(RedisConnectionFailureException e)
			{
				logger.error("handleAssetValuationAndCreateNormalOrder occur error with RedisConnectionFailureException with jobIdentity["
						+ job.getUuid() + "],exception stack trace :" + ExceptionUtils.getStackTrace(e));
				continue;
			}
			catch (Throwable e) {
				this.jobHandler.abandonJob(job);
				logger.error("handleAssetValuationAndCreateNormalOrder occur error with jobIdentity["
						+ job.getUuid() + "],exception stack trace :" + ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	private void updateClearingVoucherStatus(AuditJob auditJob){
		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(auditJob.getUuid());
		BigDecimal totalClearedDeductPlanAmount = beneficiaryAuditResultService.countDeductPlanTotalAmountByClearingStatus(auditJob, AuditResultCode.ISSUED, ClearingStatus.DONE);
		ClearingVoucher clearingVoucher= totalReceivableBillsHandler.queryClearingVoucherByTotalReceivableBills(totalReceivableBills,ClearingVoucherStatus.DOING);
		logger.info("totalReceivableBills amount["+totalReceivableBills.getTotalReceivableAmount()+"] clearing amount["+totalClearedDeductPlanAmount+"]");
		if(totalReceivableBills.getTotalReceivableAmount().compareTo(totalClearedDeductPlanAmount)!=0){
			clearingVoucher.setClearingVoucherStatus(ClearingVoucherStatus.Fail);
			clearingVoucherService.saveOrUpdate(clearingVoucher);
			return ;
		}
		List<TotalReceivableBills> totalReceivableBillsList= totalReceivableBillsHandler.queryAllOfSameClearingVoucherByOne(totalReceivableBills);
		BigDecimal totalReceivableAmount = BigDecimal.ZERO;
		for(TotalReceivableBills receivableBill : totalReceivableBillsList){
			AuditJob newAuditJob = auditJobService.getUniqueAuditJobByTotalReceivableBills(receivableBill,null);
			if(newAuditJob == null){
				continue;
			}
			if(newAuditJob.getClearingStatus() == ClearingStatus.DONE){
				totalReceivableAmount=totalReceivableAmount.add(receivableBill.getTotalReceivableAmount());
			}
		}
		logger.info("totalReceivableAmount ["+totalReceivableAmount+"] clearingVoucher amount["+clearingVoucher.getVoucherAmount()+"]");
		if(clearingVoucher!=null && totalReceivableAmount.compareTo(clearingVoucher.getVoucherAmount())==0 && clearingVoucher.getClearingVoucherStatus()!=ClearingVoucherStatus.Fail){
			clearingVoucher.setClearingVoucherStatus(ClearingVoucherStatus.DONE);
			clearingVoucher.setSpecialClearType(ClearSpecialAccountType.CREATE);
			clearingVoucher.setCompleteTime(new Date());
			clearingVoucher.setLastModifiedTime(new Date());
			clearingVoucherService.saveOrUpdate(clearingVoucher);
		}
	}

//	private boolean autoCashFlowReTouching(Job job) {
//		String auditJobUuid = job.getSndBusinessCode();
//		AuditJob auditJob = auditJobService.getAuditJob(auditJobUuid);
//		if(auditJob.getClearingStatus()==ClearingStatus.DOING){
//			return true;
//		}
//		CashFlow cashFlow = evaluateSearchCashFlow(job, auditJob);
//		if(cashFlow == null){
//			return false;
//		}
//		List<ClearingCashFlowMode> clearingCashFlowModeList = new ArrayList<>();
//		try{
//			cashFlowHandler.relatedClearingCashFlow(auditJobUuid, clearingCashFlowModeList);
//		}catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
	
	//评估匹配流水
//	private CashFlow evaluateSearchCashFlow(Job job,AuditJob auditJob){
//		String totalReceivableBillsUuid = job.getTrdBusinessCode();
//		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.getTotalReceivableBillsByUuid(totalReceivableBillsUuid);
//		BigDecimal totalReceivableAmount = totalReceivableBills.getTotalReceivableAmount();//待清算金额
//		//根据收付款方信息，找对账任务
//		List<AuditJob> auditJobList=auditJobService.queryAuditJobByAccountMessage(auditJob.getHostAccountNo(), auditJob.getHostAccountName(), auditJob.getCounterAccountNo(), auditJob.getCounterAccountName());
//		if(CollectionUtils.isEmpty(auditJobList) || auditJobList.size()>1){
//			logger.info("auditJobList　is null OR more one JobUuid :"+ job.getUuid());
//			return null;
//		}
//		List<CashFlow> cashFlows= cashFlowService.getCashFlowByByAccountNoAndAmountAndDate(auditJob.getHostAccountNo(), auditJob.getCounterAccountNo(), auditJob.getStartTime(), auditJob.getEndTime(), totalReceivableAmount);
//		if(CollectionUtils.isEmpty(auditJobList) || auditJobList.size()>1){
//			logger.info("cashFlows　is null OR more one JobUuid :"+ job.getUuid());
//			return null;
//		}
//		return cashFlows.get(0);
//	}

	private Map<String, JobContext<AuditJob>> prepareAllJobContext(List<Job> jobs) {
		Map<String, JobContext<AuditJob>> jobContextMap = new HashMap<>();
		for (Job job : jobs) {
			JobContext<AuditJob> context = prepareJobContext(job);
			jobContextMap.put(job.getUuid(), context);
		}
		return jobContextMap;
	}

	private Map<String, String> getSyncMarks(StageResult<Map> taskResultForFstStep) {
		Map<String, String> syncMarks = new HashMap<>();
		while (taskResultForFstStep.hasMoreElements()) {

			Map map = taskResultForFstStep.nextElement();

			if (null != map) {

				syncMarks.putAll(map);
			}
		}
		return syncMarks;
	}

	private JobContext<AuditJob> prepareJobContext(Job job) {
		JobContext<AuditJob> jobContext = new JobContext<>();
		HashMap<Step, List<Object>> rawDataTable = new HashMap<>();

		AuditJob auditJob = auditJobService.getAuditJob(job.getSndBusinessCode());
		jobContext.setJob(job);
		jobContext.setJobTarget(auditJob);
		jobContext.setRawDataTable(rawDataTable);

		if (ExecutingStatus.CREATE.equals(job.getFstStageExcutingStatus())
				|| ExecutingStatus.CREATE.equals(job.getSndStageExcutingStatus())) {
			TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(auditJob.getUuid());
			List<String> deductPlanUuidList = beneficiaryAuditResultService.getDeductPlanUuidsByAuditJob(auditJob, AuditResultCode.ISSUED);
			List<String> filterDeductPlanUuidList = deductPlanService.filterDeductPlanUuidByClearingStatus(deductPlanUuidList, ClearingStatus.UNDO);
			List<ClearingVoucherParameters> clearingVoucherParametersList = new ArrayList<>();
			for (String deductPlanUuid : filterDeductPlanUuidList) {
				ClearingVoucherParameters clearingVoucherParameters = new ClearingVoucherParameters(deductPlanUuid,
						auditJob.getUuid(), totalReceivableBills.getUuid());
				clearingVoucherParametersList.add(clearingVoucherParameters);
			}
			jobContext.getRawDataTable().put(Step.FST, (List) filterDeductPlanUuidList);
			jobContext.getRawDataTable().put(Step.SND, (List) clearingVoucherParametersList);
			if (CollectionUtils.isNotEmpty(filterDeductPlanUuidList)) {
				logger.info("#Step_1收集PgClearingAccount(" + auditJob.getPgClearingAccount()+"),PaymentInstitution("+auditJob.getPaymentInstitution().ordinal()+",MerchantNo("+auditJob.getMerchantNo()+")数据完成,共计:"
						+ filterDeductPlanUuidList.size() + "条,当前时间:" + DateUtils.getCurrentTimeMillis());
			}
		}
		return jobContext;
	}

	private void registerJob() {
		JobType jobType = JobType.Deduct_Plan_Clearing;

		List<AuditJob> auditJobs = auditJobService.getDebitDoingAuditJobList();
		
		if (CollectionUtils.isEmpty(auditJobs)) {
			logger.info("auditJobs is empty");
			return;
		}

		for (AuditJob auditJob : auditJobs) {
			if (null == auditJob)
				continue;

			//check　存在对端多账，不能过
			if(auditJob.getAuditResult()== AuditResult.ISSUING && CommandAuditResultCode.statusIsCounter().contains(auditJob.getAuditResultCode())){
				logger.info("对端多账, auditJobUuid: "+auditJob.getUuid() );
				continue ;
			}
			TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(auditJob.getUuid());
			if (null == totalReceivableBills) {
				logger.info("应收单is empty");
				continue;
			}
			String totalReceivableBillsUuid = totalReceivableBills.getUuid();
			String auditJobUuid = auditJob.getUuid();

			String fstBusinessCode = "";
			String sndBusinessCode = auditJobUuid;
			String trdBusinessCode = totalReceivableBillsUuid;
			Job job = jobService.getAllJobBy(fstBusinessCode, sndBusinessCode, trdBusinessCode, jobType);

			if (JobFactory.isJobDone(job) || JobFactory.isJobAbandon(job)) {
				continue;
			}
			if (null == job) {
				logger.info("begin to registe job with totalReceivableBills[" + totalReceivableBillsUuid + "]");
				Job jobForSubrogation = jobHandler.registeJob(fstBusinessCode, sndBusinessCode, trdBusinessCode,
						jobType);
				String beanName = "clearDeductPlanHandlerNewProxy";
				String fstStageMethodName = "criticalMarker";
				String sndStageMethodName = "reconciliationClearingDeductPlan";

				jobHandler.registeStage(jobForSubrogation, Step.FST, beanName, fstStageMethodName, 20, Priority.Low);
				jobHandler.registeStage(jobForSubrogation, Step.SND, beanName, sndStageMethodName, 20, Priority.Low);


				BigDecimal totalReceivableAmount = totalReceivableBills.getTotalReceivableAmount();
				BigDecimal totalDeductPlanAmount = beneficiaryAuditResultService.CountDeductPlanTotalReceivableAmount(auditJob, AuditResultCode.ISSUED, null);
				if (AmountUtil.notEquals(totalReceivableAmount, totalDeductPlanAmount)) {
					jobForSubrogation.setExcutingStatus(ExecutingStatus.ABANDON);
				}
				jobHandler.registeJobStage(jobForSubrogation);

			}
		}
	}
	
//	private TotalReceivableBills getTotalReceivableBillsByAuditJob(AuditJob auditJob) {
//		return totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(auditJob, AuditStatus.ISSUED);
//	}


	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataLists)
			throws JobInterruptException {
		throw new JobInterruptException();
	}

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataList,
			Map<String, String> critialMarks) throws JobInterruptException {
		if(null==rawDataList)
			return null;
		List<CriticalMarkedData<?>> criticalDataList = new ArrayList();
		int i = 0;
		for (Object params : rawDataList) {
			ClearingVoucherParameters ClearingVoucherParameters = (ClearingVoucherParameters) params;
			String deductPlanUuid = ClearingVoucherParameters.getDeductPlanUuid();
			String contractUuid = critialMarks.get(deductPlanUuid);
			CriticalMarkedData<ClearingVoucherParameters> syncMarkedData = new CriticalMarkedData<>();

			if (StringUtils.isEmpty(contractUuid)) {
				logger.error("this deductPlanUuid[" + deductPlanUuid + "] reference contractUuid is null");
				throw new JobInterruptException("job target[" + deductPlanUuid + "] criticalMark missing");
			}

			syncMarkedData.setCritialMark(contractUuid);
			syncMarkedData.setTaskParams(ClearingVoucherParameters);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
	}

}
