package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.suidifu.hathaway.job.*;
import com.suidifu.hathaway.job.handler.CriticalMarkedData;
import com.suidifu.hathaway.job.handler.JobStageHandler;
import com.suidifu.hathaway.job.service.JobService;
import com.suidifu.pricewaterhouse.yunxin.handler.AssetTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.CriticalSecionProjector;
import com.suidifu.pricewaterhouse.yunxin.handler.TaskDeploy;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSetValuationParameter;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 资产评估
 * 
 * @author louguanyang
 *
 */
@Component("assetTaskHandlerNoTransaction")
public class AssetTaskHandlerImpl  extends TaskDeploy implements CriticalSecionProjector,AssetTaskHandler{

	@Autowired
	private JobService jobService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private JobStageHandler jobHandler;
	
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	private static Log logger = LogFactory.getLog(AssetTaskHandlerImpl.class);

	@Override
	public void handleAssetValuationAndCreateNormalOrder(Date currentValuationDate){

		registerJob(currentValuationDate);

		List<Job> registeredJobTarget = collectNeedToDoJobTarget(currentValuationDate);

		for (Job job : registeredJobTarget) {

			try {
				if (null == job) continue;

				JobContext<String> context = preContext(job);

				populateFstStageData(context);

				if (needToDoThisStep(context, Step.FST)) {

					StageResult<Boolean> taskResultForFstStep = performStageInCritical(Step.FST, context, this, Boolean.class);

					if (!taskResultForFstStep.isAllTaskDone()) {
						continue;
					}

					if (taskResultForFstStep.isFailDone()) {
						throw new JobInterruptException("fst step fail done with jobIdentity[" + job.getUuid() + "]");
					}
				}

				populateSndStageData(context, currentValuationDate);

				if (needToDoThisStep(context, Step.SND)) {

					StageResult<Boolean> taskResultForSndStep = performStageInCritical(Step.SND, context, this, Boolean.class);

					if (!taskResultForSndStep.isAllTaskDone()) {
						continue;
					}

					if (taskResultForSndStep.isFailDone()) {
						throw new JobInterruptException("snd step fail done with jobIdentity[" + job.getUuid() + "]");
					}
				}

			} catch (JobInterruptException e) {
				this.jobHandler.abandonJob(job);

				logger.error("handleAssetValuationAndCreateNormalOrder occur error with jobInterruptException with jobIdentity[" + job.getUuid() + "],exception stack trace :" + ExceptionUtils.getStackTrace(e));

			} catch (Throwable e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 *
	 * @param context Job上下文
	 */
	private void populateFstStageData(JobContext<String> context) {
		Stage first_stage = context.getJob().extractStageBy(Step.FST);
		if (first_stage == null || first_stage.getExecutingStatus() != ExecutingStatus.CREATE) {
			return;
		}
		String financialContractUuid = context.getJobTarget();
		List<AssetSetValuationParameter> step_first_parameter_list = repaymentPlanHandler.getPrepaymentFailRepaymentPlanUuids(financialContractUuid);
		context.getRawDataTable().put(Step.FST, (List) step_first_parameter_list);
		if (CollectionUtils.isNotEmpty(step_first_parameter_list)) {
			logger.info("#Step_1收集financialContractUuid(" + financialContractUuid + ")数据完成," + "共计:" + step_first_parameter_list.size() + "条,当前时间:" + DateUtils.getCurrentTimeMillis());
		}
	}

	/**
	 *
	 * @param context	Job上下文
	 * @param currentValuationDate 评估日期
	 */
	private void populateSndStageData(JobContext<String> context,Date currentValuationDate) {
		Stage snd_stage = context.getJob().extractStageBy(Step.SND);
		if (snd_stage == null || snd_stage.getExecutingStatus() != ExecutingStatus.CREATE) {
			return;
		}
		String financialContractUuid = context.getJobTarget();
		List<AssetSetValuationParameter> step_two_parameter_list = repaymentPlanService.get_all_need_valuation_repayment_plan_by(financialContractUuid, currentValuationDate);
		context.getRawDataTable().put(Step.SND, (List)step_two_parameter_list);
		if (CollectionUtils.isNotEmpty(step_two_parameter_list)) {
			logger.info("#Step_2收集financialContractUuid(" + financialContractUuid + ")数据完成," + "共计:" + step_two_parameter_list.size() + "条,当前时间:" + DateUtils.getCurrentTimeMillis());
		}
	}
	
	private boolean needToDoThisStep(JobContext<String> context,Step step){

		try {
			Stage stage = context.getJob().extractStageBy(step);

			return null != stage && (CollectionUtils.isNotEmpty(context.getRawDataTable().getOrDefault(step, Collections.emptyList())) || stage.getExecutingStatus() == ExecutingStatus.PROCESSING || stage.getExecutingStatus() == ExecutingStatus.DONE);

		} catch (Exception e) {
			e.printStackTrace();

			logger.error("#needToDoThisStep# occur exception with stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");

			return false;
		}
	}
	
	/**
	 * @param currentValuationDate 日切执行日期
	 * @return 当日需要评估的日切任务
	 */
	private List<Job> collectNeedToDoJobTarget(Date currentValuationDate) {
		return jobService.loadAllCreateOrProcessingJobBy(JobType.Asset_Valuation, DateUtils.format(currentValuationDate));
	}

	private JobContext<String> preContext(Job job) {
		JobContext<String> jobContext = new JobContext<>();
		HashMap<Step, List<Object>> rawDataTable = new HashMap<>();

		String financialContractUuid = job.getFstBusinessCode();
		jobContext.setJob(job);
		jobContext.setJobTarget(financialContractUuid);
		jobContext.setRawDataTable(rawDataTable);
		return jobContext;
	}

	/**
	 * 注册日切Job
	 * @param currentValuationDate 日切时间
	 */
	private void registerJob(Date currentValuationDate) {

		List<String> financialContracts = financialContractService.getAllFinancialContractUuid(currentValuationDate);

		String sndBusinessCode = DateUtils.format(currentValuationDate);
		String trdBusinessCode = org.apache.commons.lang.StringUtils.EMPTY;
		JobType jobType = JobType.Asset_Valuation;
		String beanNameForSubrogation = "dstJobAssetValuation";
		String fstStageMethodName = "processing_failed_prepayment_application";
		String sndStageMethodName = "valuate_repayment_plan_and_system_create_order";

		List<Job> jobList = new ArrayList<>();
		int maxJobSize = 0;
		boolean isMaxJob;

		for (String financialContractUuid : financialContracts) {

			if (no_need_register_job(financialContractUuid, sndBusinessCode, trdBusinessCode, jobType)) continue;

			List<AssetSetValuationParameter> step_first_parameter_list = repaymentPlanHandler.getPrepaymentFailRepaymentPlanUuids(financialContractUuid);
			List<AssetSetValuationParameter> step_two_parameter_list = repaymentPlanService.get_all_need_valuation_repayment_plan_by(financialContractUuid, currentValuationDate);

			if (CollectionUtils.isEmpty(step_first_parameter_list) && CollectionUtils.isEmpty(step_two_parameter_list)) {
				continue;
			}

			isMaxJob = false;
			int jobSize = 0;
			Job jobForSubrogation = jobHandler.registeJob(financialContractUuid, sndBusinessCode, trdBusinessCode, jobType);

			if (CollectionUtils.isNotEmpty(step_first_parameter_list)) {
				jobHandler.registeStage(jobForSubrogation, Step.FST, beanNameForSubrogation, fstStageMethodName, 50, Priority.Medium);
				jobSize += step_first_parameter_list.size();
				logger.info("registerStage_Step_1, financialContractUuid(" + financialContractUuid + ")需执行" + fstStageMethodName + "共计:" + step_first_parameter_list.size() + "条,当前时间:" + DateUtils.getCurrentTimeMillis());
			}

			if (CollectionUtils.isNotEmpty(step_two_parameter_list)) {
				jobHandler.registeStage(jobForSubrogation, Step.SND, beanNameForSubrogation, sndStageMethodName, 50, Priority.Medium);
				jobSize += step_two_parameter_list.size();
				logger.info("registerStage_Step_2, financialContractUuid(" + financialContractUuid + ")需执行" + sndStageMethodName + "共计:" + step_two_parameter_list.size() + "条,当前时间:" + DateUtils.getCurrentTimeMillis());
			}

			if (jobSize > maxJobSize) {
				isMaxJob = true;
				maxJobSize = jobSize;
			}

			if (isMaxJob && CollectionUtils.isNotEmpty(jobList)) {
				jobList.add(0, jobForSubrogation);
			} else {
				jobList.add(jobForSubrogation);
			}
		}

		for (Job job: jobList ) {
			jobHandler.registeJobStage(job);
		}

		if (jobList.size() > 0) {
			logger.info("#registerJob_" + DateUtils.format(currentValuationDate) + "共有 " + jobList.size() + " 个信托合同需要日切,当前时间:" + DateUtils.getCurrentTimeMillis());
		}
	}

	private boolean no_need_register_job(String financialContractUuid, String sndBusinessCode, String trdBusinessCode, JobType jobType) {
		if (StringUtils.isEmpty(financialContractUuid)) return true;

		Job jobInDB = jobService.getAllJobBy(financialContractUuid, sndBusinessCode, trdBusinessCode, jobType);

		return JobFactory.isJobDone(jobInDB) || JobFactory.isJobAbandon(jobInDB) || jobInDB != null;

	}

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataLists)
			throws JobInterruptException {
		
		List<CriticalMarkedData<?>> criticalDataList=new ArrayList<>();

		for(Object params:rawDataLists)
		{
			AssetSetValuationParameter assetSetValuationParameter =(AssetSetValuationParameter)params;
			
			CriticalMarkedData<AssetSetValuationParameter> syncMarkedData =new CriticalMarkedData<>();
			
			String contractUuid = assetSetValuationParameter.getContractUuid();
			
			String assetSetUuid = assetSetValuationParameter.getAssetUuid();
			
			if(StringUtils.isEmpty(contractUuid)){
				throw new JobInterruptException("job target["+assetSetUuid+"] criticalMark missing"); 
			}
			
			syncMarkedData.setCritialMark(contractUuid);
			syncMarkedData.setTaskParams(assetSetValuationParameter);
			criticalDataList.add(syncMarkedData);
		}
		return criticalDataList;
	}

	@Override
	public List<CriticalMarkedData<?>> projectDataToCriticalSecion(List<Object> rawDataList,
			Map<String, String> critialMarks) throws JobInterruptException {
		
		return null;
	}
	
}
