/**
 * 
 */
package com.suidifu.hathaway.job.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo2do.core.persistence.GenericDaoSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.JobType;
import com.suidifu.hathaway.job.service.JobService;

/**
 * @author wukai
 *
 */
@Service("jobService")
public class JobServiceImpl extends GenericServiceImpl<Job> implements JobService {

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	
	@Override
	public Job getJobBy(String jobUuid) {
		
		if(StringUtils.isBlank(jobUuid)){
			return null;
		}
		
		Filter filter = new Filter();
		
		filter.addEquals("uuid", jobUuid);
		
		List<Job> jobList = this.list(Job.class, filter);
		
		return CollectionUtils.isEmpty(jobList) ? null : jobList.get(0);
	}

	@Override
	public Job getJobBy(String fstBusinessCode, String sndBusinessCode,
			String trdBusinessCode, JobType jobType) {
		
		StringBuffer sql = new StringBuffer("FROM Job WHERE  excutingStatus NOT IN (:excutingStatus) ");

		Map<String,Object> parameters = new HashMap<String,Object>();

		parameters.put("excutingStatus",new ArrayList<ExecutingStatus>(){{add(ExecutingStatus.ABANDON);add(ExecutingStatus.DONE);}});

		if(StringUtils.isNotBlank(fstBusinessCode)){

			sql.append(" AND fstBusinessCode =:fstBusinessCode ");

			parameters.put("fstBusinessCode", fstBusinessCode);
		}

		if(StringUtils.isNotBlank(sndBusinessCode)){

			sql.append(" AND sndBusinessCode =:sndBusinessCode ");

			parameters.put("sndBusinessCode", sndBusinessCode);
		}
		if(StringUtils.isNotBlank(trdBusinessCode)){

			sql.append(" AND trdBusinessCode =:trdBusinessCode ");

			parameters.put("trdBusinessCode", trdBusinessCode);
		}
		
		if(null != jobType){

			sql.append(" AND jobType =:jobType ");

			parameters.put("jobType", jobType);
		}

		List<Job> jobList = this.genericDaoSupport.searchForList(sql.toString(),parameters);
		
		return CollectionUtils.isEmpty(jobList) ? null : jobList.get(0);
		
	}

	@Override
	public boolean hasRegistedJob(String fstBusinessCode, JobType jobType) {
		
		String hql = "SELECT COUNT(*) FROM Job where fstBusinessCode =:fstBusinessCode AND jobType =:jobType";
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		
		parameters.put("fstBusinessCode", fstBusinessCode);
		parameters.put("jobType",jobType);
		
		return genericDaoSupport.searchForInt(hql, parameters) > 0;
	}

	@Override
	public Job getJobBy(String fstBusinessCode, JobType jobType) {
		return getJobBy(fstBusinessCode, StringUtils.EMPTY, StringUtils.EMPTY, jobType);
	}

	@Override
	public List<Job> loadAllCreateOrProcessingJob() {
		return loadAllCreateOrProcessingJobBy(null);
	}

	/* (non-Javadoc)
	 * @see com.suidifu.hathaway.job.service.JobService#loadAllCreateOrProcessingJobBy(com.suidifu.hathaway.job.JobType)
	 */
	@Override
	public List<Job> loadAllCreateOrProcessingJobBy(JobType jobType) {
		return loadAllCreateOrProcessingJobBy(jobType, null);
	}

	/* (non-Javadoc)
	 * @see com.suidifu.hathaway.job.service.JobService#loadAllCreateOrProcessingJobBy(com.suidifu.hathaway.job.JobType, java.lang.String)
	 */
	@Override
	public List<Job> loadAllCreateOrProcessingJobBy(JobType jobType, String sndBusinessCode) {
		String hql = "FROM Job WHERE excutingStatus IN ( :excutingStatusList )";

		Map<String, Object> parameters = new HashMap<String, Object>();

		if (null != jobType) {
			hql += " AND jobType = :jobType";
			parameters.put("jobType", jobType);
		}

		if (StringUtils.isNotEmpty(sndBusinessCode)) {
			hql += " AND sndBusinessCode = :sndBusinessCode";
			parameters.put("sndBusinessCode", sndBusinessCode);
		}

		
		List<ExecutingStatus> executingStatusList = Arrays.asList(ExecutingStatus.CREATE,ExecutingStatus.PROCESSING);
		parameters.put("excutingStatusList", executingStatusList);
		
		hql += " Order By id DESC";
		return genericDaoSupport.searchForList(hql,parameters);
	}

	@Override
	public Job getAllJobBy(String fstBusinessCode, String sndBusinessCode,
			String trdBusinessCode, JobType jobType) {
		
		Filter filter = new Filter();
		
		if(StringUtils.isNotBlank(fstBusinessCode)){
			
			filter.addEquals("fstBusinessCode", fstBusinessCode);
		}
		
		if(StringUtils.isNotBlank(sndBusinessCode)){
			
			filter.addEquals("sndBusinessCode", sndBusinessCode);
		}
		if(StringUtils.isNotBlank(trdBusinessCode)){
			
			filter.addEquals("trdBusinessCode", trdBusinessCode);
		}
		
		if(null != jobType){
			
			filter.addEquals("jobType", jobType);
		}
		
		List<Job> jobList = this.list(Job.class, filter);
		
		return CollectionUtils.isEmpty(jobList) ? null : jobList.get(0);
	}
	
	@Override
	public List<Job> getJobListBy(String fstBusinessCode, String sndBusinessCode, String trdBusinessCode, JobType jobType) {
		
		Filter filter = new Filter();
		
		if(StringUtils.isNotBlank(fstBusinessCode)){
			
			filter.addEquals("fstBusinessCode", fstBusinessCode);
		}
		
		if(StringUtils.isNotBlank(sndBusinessCode)){
			
			filter.addEquals("sndBusinessCode", sndBusinessCode);
		}
		if(StringUtils.isNotBlank(trdBusinessCode)){
			
			filter.addEquals("trdBusinessCode", trdBusinessCode);
		}
		
		if(null != jobType){
			
			filter.addEquals("jobType", jobType);
		}
		
		List<Job> jobList = this.list(Job.class, filter);
		
		return jobList;
	}

}
