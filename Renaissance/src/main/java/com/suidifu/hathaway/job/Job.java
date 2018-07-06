/**
 * 
 */
package com.suidifu.hathaway.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author wukai
 *
 */
@Entity
public class Job {
	
	/**
	 * 默认尝试次数 3次
	 */
	@Transient
	public static final int DEFAULT_TRY_TIMES = 3;
	
	/**
	 * 默认超时时间2天，单位毫秒
	 */
	@Transient
	public static final long DEFAULT_TIME_OUT = 1000 * 60 * 60 * 24 * 2;
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String uuid;
	
	private String fstBusinessCode;
	
	private String sndBusinessCode;
	
	private String trdBusinessCode;
	
	@Enumerated(EnumType.ORDINAL)
	private JobType jobType;//voucher_job
	
	private String jobName;//voucher_job_0001
	
	private Date startTime;
	
	private Date endTime;
	
	private String issuerIdentity;
	
	private String issuerIp;
	
	@Enumerated(EnumType.ORDINAL)
	private ExecutingStatus excutingStatus;//created , processing, end abandon
	
	@Enumerated(EnumType.ORDINAL)
	private ExecutingResult excutingResult;//succ failed 
	
	/***********************stage****************************/
	// fst stage 
	private String fstStageUuid;
	
	private int fstStageRetryTimes;
	
	private long fstStageTimeout;
	
	private int fstStageChunkSize;
	
	private String fstBeanName;
	
	private String fstMethodName;
	
	private Date fstStageCurrentTaskExpiredTime;
	
	private ExecutingStatus fstStageExcutingStatus;
	
	private ExecutingResult fstStageExcutingResult;//succ failed 
	
	private Date fstStagecreateTime;
	
	private Date fstStagelastModifiedTime;
	
	private int fstStagePriority;
	
	// snd stage
	private String sndStageUuid;
	
	private int sndStageRetryTimes;
	
	private long sndStageTimeout;
	
	private int sndStageChunkSize;
	
	private String sndBeanName;
	
	private String sndMethodName;
	
	private Date sndStageCurrentTaskExpiredTime;
	
	private ExecutingStatus sndStageExcutingStatus;
	
	private ExecutingResult sndStageExcutingResult;//succ failed 
	
	private Date sndStagecreateTime;
	
	private Date sndStagelastModifiedTime;
	
	private int sndStagePriority;
	
	// trd stage
	private String trdStageUuid;
	
	private int trdStageRetryTimes;
	
	private long trdStageTimeout;
	
	private int trdStageChunkSize;
	
	private String trdBeanName;
	
	private String trdMethodName;
	
	private Date trdStageCurrentTaskExpiredTime;
	
	private ExecutingStatus trdStageExcutingStatus;
	
	private ExecutingResult trdStageExcutingResult;//succ failed 
	
	private Date trdStagecreateTime;
	
	private Date trdStagelastModifiedTime;
	
	private int trdStagePriority;
	
	// fouth stage
	private String fourthStageUuid;
	
	private int fourthStageRetryTimes;
	
	private long fourthStageTimeout;
	
	private int fourthStageChunkSize;
	
	private String fourthBeanName;
	
	private String fourthMethodName;
	
	private Date fourthStageCurrentTaskExpiredTime;
	
	private ExecutingStatus fourthStageExcutingStatus;
	
	private ExecutingResult fourthStageExcutingResult;//succ failed 
	
	private Date fourthStagecreateTime;
	
	private Date fourthStagelastModifiedTime;
	
	private int fourthStagePriority;
	
	// fifth stage
	private String fifthStageUuid;
	
	private int fifthStageRetryTimes;
	
	private long fifthStageTimeout;
	
	private int fifthStageChunkSize;
	
	private String fifthBeanName;
	
	private String fifthMethodName;
	
	private Date fifthStageCurrentTaskExpiredTime;
	
	private ExecutingStatus fifthStageExcutingStatus;
	
	private ExecutingResult fifthStageExcutingResult;//succ failed
	
	private Date fifthStagecreateTime;
	
	private Date fifthStagelastModifiedTime;
	
	private int fifthStagePriority;
	
	/*****************************************************/
	
	private Date createTime;
	
	private Date lastModifiedTime;
	
	public Job() {
		super();
	}
	

	public Job(String fstBusinessCode, String sndBusinessCode,
			String trdBusinessCode, JobType jobType, String jobName,
			Date startTime, Date endTime, String issuerIdentity, String issuerIP) {
		super();
		this.uuid = UUID.randomUUID().toString();
		
		this.fstBusinessCode = fstBusinessCode;
		this.sndBusinessCode = sndBusinessCode;
		this.trdBusinessCode = trdBusinessCode;
		this.jobType = jobType;
		this.jobName = jobName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.issuerIdentity = issuerIdentity;
		this.issuerIp = issuerIP;
		
		this.excutingStatus = ExecutingStatus.CREATE;
		this.createTime = new Date();
		this.lastModifiedTime = new Date();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFstBusinessCode() {
		return fstBusinessCode;
	}

	public void setFstBusinessCode(String fstBusinessCode) {
		this.fstBusinessCode = fstBusinessCode;
	}

	public String getSndBusinessCode() {
		return sndBusinessCode;
	}

	public void setSndBusinessCode(String sndBusinessCode) {
		this.sndBusinessCode = sndBusinessCode;
	}

	public String getTrdBusinessCode() {
		return trdBusinessCode;
	}

	public void setTrdBusinessCode(String trdBusinessCode) {
		this.trdBusinessCode = trdBusinessCode;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIssuerIdentity() {
		return issuerIdentity;
	}

	public void setIssuerIdentity(String issuerIdentity) {
		this.issuerIdentity = issuerIdentity;
	}

	public String getIssuerIp() {
		return issuerIp;
	}

	public void setIssuerIp(String issuerIP) {
		this.issuerIp = issuerIP;
	}

	public ExecutingStatus getExcutingStatus() {
		return excutingStatus;
	}

	public void setExcutingStatus(ExecutingStatus excutingStatus) {
		this.excutingStatus = excutingStatus;
	}

	public ExecutingResult getExcutingResult() {
		return excutingResult;
	}

	public void setExcutingResult(ExecutingResult excutingResult) {
		this.excutingResult = excutingResult;
	}

	public String getFstStageUuid() {
		return fstStageUuid;
	}

	public void setFstStageUuid(String fstStageUuid) {
		this.fstStageUuid = fstStageUuid;
	}

	public int getFstStageRetryTimes() {
		return fstStageRetryTimes;
	}

	public void setFstStageRetryTimes(int fstStageRetryTimes) {
		this.fstStageRetryTimes = fstStageRetryTimes;
	}

	public Date getFstStageCurrentTaskExpiredTime() {
		return fstStageCurrentTaskExpiredTime;
	}

	public void setFstStageCurrentTaskExpiredTime(
			Date fstStageCurrentTaskExpiredTime) {
		this.fstStageCurrentTaskExpiredTime = fstStageCurrentTaskExpiredTime;
	}

	public ExecutingStatus getFstStageExcutingStatus() {
		return fstStageExcutingStatus;
	}

	public void setFstStageExcutingStatus(ExecutingStatus fstStageExcutingStatus) {
		this.fstStageExcutingStatus = fstStageExcutingStatus;
	}

	public ExecutingResult getFstStageExcutingResult() {
		return fstStageExcutingResult;
	}

	public void setFstStageExcutingResult(ExecutingResult fstStageExcutingResult) {
		this.fstStageExcutingResult = fstStageExcutingResult;
	}

	public Date getFstStagecreateTime() {
		return fstStagecreateTime;
	}

	public void setFstStagecreateTime(Date fstStagecreateTime) {
		this.fstStagecreateTime = fstStagecreateTime;
	}

	public Date getFstStagelastModifiedTime() {
		return fstStagelastModifiedTime;
	}

	public void setFstStagelastModifiedTime(Date fstStagelastModifiedTime) {
		this.fstStagelastModifiedTime = fstStagelastModifiedTime;
	}

	public String getSndStageUuid() {
		return sndStageUuid;
	}

	public void setSndStageUuid(String sndStageUuid) {
		this.sndStageUuid = sndStageUuid;
	}

	public int getSndStageRetryTimes() {
		return sndStageRetryTimes;
	}

	public void setSndStageRetryTimes(int sndStageRetryTimes) {
		this.sndStageRetryTimes = sndStageRetryTimes;
	}

	public Date getSndStageCurrentTaskExpiredTime() {
		return sndStageCurrentTaskExpiredTime;
	}

	public void setSndStageCurrentTaskExpiredTime(
			Date sndStageCurrentTaskExpiredTime) {
		this.sndStageCurrentTaskExpiredTime = sndStageCurrentTaskExpiredTime;
	}

	public ExecutingStatus getSndStageExcutingStatus() {
		return sndStageExcutingStatus;
	}

	public void setSndStageExcutingStatus(ExecutingStatus sndStageExcutingStatus) {
		this.sndStageExcutingStatus = sndStageExcutingStatus;
	}

	public ExecutingResult getSndStageExcutingResult() {
		return sndStageExcutingResult;
	}

	public void setSndStageExcutingResult(ExecutingResult sndStageExcutingResult) {
		this.sndStageExcutingResult = sndStageExcutingResult;
	}

	public Date getSndStagecreateTime() {
		return sndStagecreateTime;
	}

	public void setSndStagecreateTime(Date sndStagecreateTime) {
		this.sndStagecreateTime = sndStagecreateTime;
	}

	public Date getSndStagelastModifiedTime() {
		return sndStagelastModifiedTime;
	}

	public void setSndStagelastModifiedTime(Date sndStagelastModifiedTime) {
		this.sndStagelastModifiedTime = sndStagelastModifiedTime;
	}

	public String getTrdStageUuid() {
		return trdStageUuid;
	}

	public void setTrdStageUuid(String trdStageUuid) {
		this.trdStageUuid = trdStageUuid;
	}

	public int getTrdStageRetryTimes() {
		return trdStageRetryTimes;
	}

	public void setTrdStageRetryTimes(int trdStageRetryTimes) {
		this.trdStageRetryTimes = trdStageRetryTimes;
	}

	public Date getTrdStageCurrentTaskExpiredTime() {
		return trdStageCurrentTaskExpiredTime;
	}

	public void setTrdStageCurrentTaskExpiredTime(
			Date trdStageCurrentTaskExpiredTime) {
		this.trdStageCurrentTaskExpiredTime = trdStageCurrentTaskExpiredTime;
	}

	public ExecutingStatus getTrdStageExcutingStatus() {
		return trdStageExcutingStatus;
	}

	public void setTrdStageExcutingStatus(ExecutingStatus trdStageExcutingStatus) {
		this.trdStageExcutingStatus = trdStageExcutingStatus;
	}

	public ExecutingResult getTrdStageExcutingResult() {
		return trdStageExcutingResult;
	}

	public void setTrdStageExcutingResult(ExecutingResult trdStageExcutingResult) {
		this.trdStageExcutingResult = trdStageExcutingResult;
	}

	public Date getTrdStagecreateTime() {
		return trdStagecreateTime;
	}

	public void setTrdStagecreateTime(Date trdStagecreateTime) {
		this.trdStagecreateTime = trdStagecreateTime;
	}

	public Date getTrdStagelastModifiedTime() {
		return trdStagelastModifiedTime;
	}

	public void setTrdStagelastModifiedTime(Date trdStagelastModifiedTime) {
		this.trdStagelastModifiedTime = trdStagelastModifiedTime;
	}

	public String getFourthStageUuid() {
		return fourthStageUuid;
	}

	public void setFourthStageUuid(String fourthStageUuid) {
		this.fourthStageUuid = fourthStageUuid;
	}

	public int getFourthStageRetryTimes() {
		return fourthStageRetryTimes;
	}

	public void setFourthStageRetryTimes(int fourthStageRetryTimes) {
		this.fourthStageRetryTimes = fourthStageRetryTimes;
	}

	public Date getFourthStageCurrentTaskExpiredTime() {
		return fourthStageCurrentTaskExpiredTime;
	}

	public void setFourthStageCurrentTaskExpiredTime(
			Date fourthStageCurrentTaskExpiredTime) {
		this.fourthStageCurrentTaskExpiredTime = fourthStageCurrentTaskExpiredTime;
	}

	public ExecutingStatus getFourthStageExcutingStatus() {
		return fourthStageExcutingStatus;
	}

	public void setFourthStageExcutingStatus(
			ExecutingStatus fourthStageExcutingStatus) {
		this.fourthStageExcutingStatus = fourthStageExcutingStatus;
	}

	public ExecutingResult getFourthStageExcutingResult() {
		return fourthStageExcutingResult;
	}

	public void setFourthStageExcutingResult(
			ExecutingResult fourthStageExcutingResult) {
		this.fourthStageExcutingResult = fourthStageExcutingResult;
	}

	public Date getFourthStagecreateTime() {
		return fourthStagecreateTime;
	}

	public void setFourthStagecreateTime(Date fourthStagecreateTime) {
		this.fourthStagecreateTime = fourthStagecreateTime;
	}

	public Date getFourthStagelastModifiedTime() {
		return fourthStagelastModifiedTime;
	}

	public void setFourthStagelastModifiedTime(Date fourthStagelastModifiedTime) {
		this.fourthStagelastModifiedTime = fourthStagelastModifiedTime;
	}

	public String getFifthStageUuid() {
		return fifthStageUuid;
	}

	public void setFifthStageUuid(String fifthStageUuid) {
		this.fifthStageUuid = fifthStageUuid;
	}

	public int getFifthStageRetryTimes() {
		return fifthStageRetryTimes;
	}

	public void setFifthStageRetryTimes(int fifthStageRetryTimes) {
		this.fifthStageRetryTimes = fifthStageRetryTimes;
	}

	public Date getFifthStageCurrentTaskExpiredTime() {
		return fifthStageCurrentTaskExpiredTime;
	}

	public void setFifthStageCurrentTaskExpiredTime(
			Date fifthStageCurrentTaskExpiredTime) {
		this.fifthStageCurrentTaskExpiredTime = fifthStageCurrentTaskExpiredTime;
	}

	public ExecutingStatus getFifthStageExcutingStatus() {
		return fifthStageExcutingStatus;
	}

	public void setFifthStageExcutingStatus(ExecutingStatus fifthStageExcutingStatus) {
		this.fifthStageExcutingStatus = fifthStageExcutingStatus;
	}

	public ExecutingResult getFifthStageExcutingResult() {
		return fifthStageExcutingResult;
	}

	public void setFifthStageExcutingResult(ExecutingResult fifthStageExcutingResult) {
		this.fifthStageExcutingResult = fifthStageExcutingResult;
	}

	public Date getFifthStagecreateTime() {
		return fifthStagecreateTime;
	}

	public void setFifthStagecreateTime(Date fifthStagecreateTime) {
		this.fifthStagecreateTime = fifthStagecreateTime;
	}

	public Date getFifthStagelastModifiedTime() {
		return fifthStagelastModifiedTime;
	}

	public void setFifthStagelastModifiedTime(Date fifthStagelastModifiedTime) {
		this.fifthStagelastModifiedTime = fifthStagelastModifiedTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public long getFstStageTimeout() {
		return fstStageTimeout;
	}


	public void setFstStageTimeout(long fstStageTimeout) {
		this.fstStageTimeout = fstStageTimeout;
	}


	public long getSndStageTimeout() {
		return sndStageTimeout;
	}


	public void setSndStageTimeout(long sndStageTimeout) {
		this.sndStageTimeout = sndStageTimeout;
	}


	public long getTrdStageTimeout() {
		return trdStageTimeout;
	}


	public void setTrdStageTimeout(long trdStageTimeout) {
		this.trdStageTimeout = trdStageTimeout;
	}


	public long getFourthStageTimeout() {
		return fourthStageTimeout;
	}


	public void setFourthStageTimeout(long fourthStageTimeout) {
		this.fourthStageTimeout = fourthStageTimeout;
	}


	public long getFifthStageTimeout() {
		return fifthStageTimeout;
	}


	public void setFifthStageTimeout(long fifthStageTimeout) {
		this.fifthStageTimeout = fifthStageTimeout;
	}

	public int getFstStageChunkSize() {
		return fstStageChunkSize;
	}


	public void setFstStageChunkSize(int fstStageChunkSize) {
		this.fstStageChunkSize = fstStageChunkSize;
	}


	public String getFstBeanName() {
		return fstBeanName;
	}


	public void setFstBeanName(String fstBeanName) {
		this.fstBeanName = fstBeanName;
	}


	public String getFstMethodName() {
		return fstMethodName;
	}


	public void setFstMethodName(String fstMethodName) {
		this.fstMethodName = fstMethodName;
	}


	public int getSndStageChunkSize() {
		return sndStageChunkSize;
	}


	public void setSndStageChunkSize(int sndStageChunkSize) {
		this.sndStageChunkSize = sndStageChunkSize;
	}


	public String getSndBeanName() {
		return sndBeanName;
	}


	public void setSndBeanName(String sndBeanName) {
		this.sndBeanName = sndBeanName;
	}


	public String getSndMethodName() {
		return sndMethodName;
	}


	public void setSndMethodName(String sndMethodName) {
		this.sndMethodName = sndMethodName;
	}


	public int getTrdStageChunkSize() {
		return trdStageChunkSize;
	}


	public void setTrdStageChunkSize(int trdStageChunkSize) {
		this.trdStageChunkSize = trdStageChunkSize;
	}


	public String getTrdBeanName() {
		return trdBeanName;
	}


	public void setTrdBeanName(String trdBeanName) {
		this.trdBeanName = trdBeanName;
	}


	public String getTrdMethodName() {
		return trdMethodName;
	}


	public void setTrdMethodName(String trdMethodName) {
		this.trdMethodName = trdMethodName;
	}


	public int getFourthStageChunkSize() {
		return fourthStageChunkSize;
	}


	public void setFourthStageChunkSize(int fourthStageChunkSize) {
		this.fourthStageChunkSize = fourthStageChunkSize;
	}


	public String getFourthBeanName() {
		return fourthBeanName;
	}


	public void setFourthBeanName(String fourthBeanName) {
		this.fourthBeanName = fourthBeanName;
	}


	public String getFourthMethodName() {
		return fourthMethodName;
	}


	public void setFourthMethodName(String fourthMethodName) {
		this.fourthMethodName = fourthMethodName;
	}


	public int getFifthStageChunkSize() {
		return fifthStageChunkSize;
	}


	public void setFifthStageChunkSize(int fifthStageChunkSize) {
		this.fifthStageChunkSize = fifthStageChunkSize;
	}


	public String getFifthBeanName() {
		return fifthBeanName;
	}


	public void setFifthBeanName(String fifthBeanName) {
		this.fifthBeanName = fifthBeanName;
	}


	public String getFifthMethodName() {
		return fifthMethodName;
	}


	public void setFifthMethodName(String fifthMethodName) {
		this.fifthMethodName = fifthMethodName;
	}


	public void newFstStage(Date currentTaskExpiredTime) {
		
		this.setFstStagecreateTime(new Date());
		this.setFstStageCurrentTaskExpiredTime(currentTaskExpiredTime);
		this.setFstStageExcutingStatus(ExecutingStatus.CREATE);
		this.setFstStagelastModifiedTime(new Date());
		this.setFstStageRetryTimes(0);;
		this.setFstStageUuid(UUID.randomUUID().toString());
		
	}
	public void newSndStage(Date currentTaskExpiredTime) {
		
		this.setSndStagecreateTime(new Date());
		this.setSndStageCurrentTaskExpiredTime(currentTaskExpiredTime);
		this.setSndStageExcutingStatus(ExecutingStatus.CREATE);
		this.setSndStagelastModifiedTime(new Date());
		this.setSndStageRetryTimes(0);;
		this.setSndStageUuid(UUID.randomUUID().toString());
		
	}
	public void newTrdStage(Date currentTaskExpiredTime) {
		
		this.setTrdStagecreateTime(new Date());
		this.setTrdStageCurrentTaskExpiredTime(currentTaskExpiredTime);
		this.setTrdStageExcutingStatus(ExecutingStatus.CREATE);
		this.setTrdStagelastModifiedTime(new Date());
		this.setTrdStageRetryTimes(0);;
		this.setTrdStageUuid(UUID.randomUUID().toString());
		
	}
	public void newFourthStage(Date currentTaskExpiredTime) {
		
		this.setFourthStagecreateTime(new Date());
		this.setFourthStageCurrentTaskExpiredTime(currentTaskExpiredTime);
		this.setFourthStageExcutingStatus(ExecutingStatus.CREATE);
		this.setFourthStagelastModifiedTime(new Date());
		this.setFourthStageRetryTimes(0);;
		this.setFourthStageUuid(UUID.randomUUID().toString());
		
	}
	public void newFifthStage(Date currentTaskExpiredTime) {
		
		this.setFifthStagecreateTime(new Date());
		this.setFifthStageCurrentTaskExpiredTime(currentTaskExpiredTime);
		this.setFifthStageExcutingStatus(ExecutingStatus.CREATE);
		this.setFifthStagelastModifiedTime(new Date());
		this.setFifthStageRetryTimes(0);;
		this.setFifthStageUuid(UUID.randomUUID().toString());
		
	}
	
	public void dumpFstStage(Stage stage) {
		
		if(!StringUtils.equals(stage.getUuid(), this.getFstStageUuid()) || stage.getLevel() != Step.FST){
			return;
		}
		this.setFstStageCurrentTaskExpiredTime(stage.getCurrentTaskExpiredTime());
		this.setFstStageExcutingStatus(stage.getExecutingStatus());
		this.setFstStagelastModifiedTime(stage.getLastModifiedTime());
		this.setFstStageRetryTimes(stage.getLeftRetryTimes());
		this.setFstStageExcutingResult(stage.getExecutingResult());
		
		this.setFstBeanName(stage.getBeanName());
		this.setFstMethodName(stage.getMethodName());
		this.setFstStageChunkSize(stage.getChunkSize());
		this.setFstStagePriority(stage.getPriority());
		
	}
	public void dumpSndStage(Stage stage) {
		
		if(!StringUtils.equals(stage.getUuid(), this.getSndStageUuid()) || stage.getLevel() != Step.SND){
			return;
		}
		this.setSndStageCurrentTaskExpiredTime(stage.getCurrentTaskExpiredTime());
		this.setSndStageExcutingStatus(stage.getExecutingStatus());
		this.setSndStagelastModifiedTime(stage.getLastModifiedTime());
		this.setSndStageRetryTimes(stage.getLeftRetryTimes());
		this.setSndStageExcutingResult(stage.getExecutingResult());
		
		this.setSndBeanName(stage.getBeanName());
		this.setSndMethodName(stage.getMethodName());
		this.setSndStageChunkSize(stage.getChunkSize());
		this.setSndStagePriority(stage.getPriority());
		
	}
	public void dumpTrdStage(Stage stage) {
		
		if(!StringUtils.equals(stage.getUuid(), this.getTrdStageUuid()) || stage.getLevel() != Step.TRD){
			return;
		}
		this.setTrdStageCurrentTaskExpiredTime(stage.getCurrentTaskExpiredTime());
		this.setTrdStageExcutingStatus(stage.getExecutingStatus());
		this.setTrdStagelastModifiedTime(stage.getLastModifiedTime());
		this.setTrdStageRetryTimes(stage.getLeftRetryTimes());
		this.setTrdStageExcutingResult(stage.getExecutingResult());
		
		this.setTrdBeanName(stage.getBeanName());
		this.setTrdMethodName(stage.getMethodName());
		this.setTrdStageChunkSize(stage.getChunkSize());
		this.setTrdStagePriority(stage.getPriority());
		
	}
	public void dumpFourthStage(Stage stage) {
		
		if(!StringUtils.equals(stage.getUuid(), this.getFourthStageUuid()) || stage.getLevel() != Step.FOURTH){
			return;
		}
		this.setFourthStageCurrentTaskExpiredTime(stage.getCurrentTaskExpiredTime());
		this.setFourthStageExcutingStatus(stage.getExecutingStatus());
		this.setFourthStagelastModifiedTime(stage.getLastModifiedTime());
		this.setFourthStageRetryTimes(stage.getLeftRetryTimes());
		this.setFourthStageExcutingResult(stage.getExecutingResult());
		
		this.setFourthBeanName(stage.getBeanName());
		this.setFourthMethodName(stage.getMethodName());
		this.setFourthStageChunkSize(stage.getChunkSize());
		this.setFourthStagePriority(stage.getPriority());
		
	}
	public void dumpFifthStage(Stage stage) {
		
		if(!StringUtils.equals(stage.getUuid(), this.getFifthStageUuid()) || stage.getLevel() != Step.FIFTH){
			return;
		}
		this.setFifthStageCurrentTaskExpiredTime(stage.getCurrentTaskExpiredTime());
		this.setFifthStageExcutingStatus(stage.getExecutingStatus());
		this.setFifthStagelastModifiedTime(stage.getLastModifiedTime());
		this.setFifthStageRetryTimes(stage.getLeftRetryTimes());
		this.setFifthStageExcutingResult(stage.getExecutingResult());
		
		this.setFifthBeanName(stage.getBeanName());
		this.setFifthMethodName(stage.getMethodName());
		this.setFifthStageChunkSize(stage.getChunkSize());
		this.setFifthStagePriority(stage.getPriority());
		
	}
	
	public List<Stage> extractStageList()
	{
		Map<Step,Stage> map = extractStepStageMap();
		
		return map.values().stream().collect(Collectors.toList());
	}
	public Stage extractStageBy(Step level){
		
		Map<Step,Stage> map = extractStepStageMap();
		
		return map.get(level);
	}
	public Map<Step,Stage> extractStepStageMap(){
		
		Map<Step,Stage> map = new HashMap<>();
		
		Stage stage = null;
		
		if(StringUtils.isNotBlank(this.getFstStageUuid()) ){
			
			stage = new Stage(this.getFstStageUuid(), this.getUuid(), Step.FST, this.getFstStageRetryTimes(), this.getFstStageExcutingStatus(), this.getFstStageExcutingResult(), this.getFstStagecreateTime(), this.getFstStagelastModifiedTime(), this.getFstStageCurrentTaskExpiredTime(), this.getFstBeanName(), this.getFstMethodName(), this.getFstStageChunkSize(), this.getFstStagePriority());
	
			map.put(Step.FST, stage);
		}
		if(StringUtils.isNotBlank(this.getSndStageUuid())){
			
			stage = new Stage(this.getSndStageUuid(), this.getUuid(), Step.SND, this.getSndStageRetryTimes(), this.getSndStageExcutingStatus(), this.getSndStageExcutingResult(), this.getSndStagecreateTime(), this.getSndStagelastModifiedTime(), this.getSndStageCurrentTaskExpiredTime(),  this.getSndBeanName(), this.getSndMethodName(), this.getSndStageChunkSize(), this.getSndStagePriority());
	
			map.put(Step.SND, stage);
		
		}
		if(StringUtils.isNotBlank(this.getTrdStageUuid())){
			
			stage = new Stage(this.getTrdStageUuid(), this.getUuid(), Step.TRD, this.getTrdStageRetryTimes(), this.getTrdStageExcutingStatus(), this.getTrdStageExcutingResult(), this.getTrdStagecreateTime(), this.getTrdStagelastModifiedTime(), this.getTrdStageCurrentTaskExpiredTime(),  this.getTrdBeanName(), this.getTrdMethodName(), this.getTrdStageChunkSize(), this.getTrdStagePriority());
		
			map.put(Step.TRD, stage);
		}
		
		if(StringUtils.isNotBlank(this.getFourthStageUuid())){
			
			stage = new Stage(this.getFourthStageUuid(), this.getUuid(), Step.FOURTH, this.getFourthStageRetryTimes(), this.getFourthStageExcutingStatus(), this.getFourthStageExcutingResult(), this.getFourthStagecreateTime(), this.getFourthStagelastModifiedTime(), this.getFourthStageCurrentTaskExpiredTime(),  this.getFourthBeanName(), this.getFourthMethodName(), this.getFourthStageChunkSize(), this.getFourthStagePriority());
		
			map.put(Step.FOURTH, stage);
		}
		
		if(StringUtils.isNotBlank(this.getFifthStageUuid())){
			
			stage = new Stage(this.getFifthStageUuid(), this.getUuid(), Step.FIFTH, this.getFifthStageRetryTimes(), this.getFifthStageExcutingStatus(), this.getFifthStageExcutingResult(), this.getFifthStagecreateTime(), this.getFifthStagelastModifiedTime(), this.getFifthStageCurrentTaskExpiredTime(), this.getFifthBeanName(), this.getFifthMethodName(), this.getFifthStageChunkSize(), this.getFifthStagePriority());
			map.put(Step.FIFTH, stage);
		}
		return map;
	}
	public void dumpStage(Stage stage)
	{
		dumpFstStage(stage);
		dumpSndStage(stage);
		dumpTrdStage(stage);
		dumpFourthStage(stage);
		dumpFifthStage(stage);
		
	}
	public void updateJobDone(){
		this.setLastModifiedTime(new Date());
		this.endTime = new Date();
		this.excutingStatus = ExecutingStatus.DONE;
		this.excutingResult = calculateJobExecutingResult();
	}
	
	public void updateJobProcessing(){
		this.setLastModifiedTime(new Date());
		this.excutingStatus = ExecutingStatus.PROCESSING;
	}
	
	public void updateStageProcessing(Step level){
		
		if(level == Step.FST){
			this.fstStagecreateTime = new Date();
			this.fstStagelastModifiedTime = new Date();
			this.fstStageExcutingStatus = ExecutingStatus.PROCESSING;
		}
		if(level == Step.SND){
			this.sndStagecreateTime = new Date();
			this.sndStagelastModifiedTime = new Date();
			this.sndStageExcutingStatus = ExecutingStatus.PROCESSING;
		}
		if(level == Step.TRD){
			this.trdStagecreateTime = new Date();
			this.trdStagelastModifiedTime = new Date();
			this.trdStageExcutingStatus = ExecutingStatus.PROCESSING;
		}
		if(level == Step.FOURTH){
			this.fourthStagecreateTime = new Date();
			this.fourthStagelastModifiedTime = new Date();
			this.fourthStageExcutingStatus = ExecutingStatus.PROCESSING;
		}
		if(level == Step.FIFTH){
			this.fifthStagecreateTime = new Date();
			this.fifthStagelastModifiedTime = new Date();
			this.fifthStageExcutingStatus = ExecutingStatus.PROCESSING;
		}
	
	}
	private ExecutingResult calculateJobExecutingResult(){
		
		List<ExecutingResult> executingResultList = new ArrayList<ExecutingResult>();
		
		if(StringUtils.isNotBlank(this.getFstStageUuid())){
			executingResultList.add(this.getFstStageExcutingResult());
		}
		if(StringUtils.isNotBlank(this.getSndStageUuid())){
			executingResultList.add(this.getSndStageExcutingResult());
		}
		if(StringUtils.isNotBlank(this.getTrdStageUuid())){
			executingResultList.add(this.getTrdStageExcutingResult());
		}
		if(StringUtils.isNotBlank(this.getFourthStageUuid())){
			executingResultList.add(this.getFourthStageExcutingResult());
		}
		if(StringUtils.isNotBlank(this.getFifthStageUuid())){
			executingResultList.add(this.getFifthStageExcutingResult());
		}
		
		boolean suc = isAllExecutingResultSuc(executingResultList);
		
		return ExecutingResult.fromValue(suc);
	}
	private boolean isAllExecutingResultSuc(List<ExecutingResult> executingResults){
		
		for (ExecutingResult executingResult : executingResults) {
			
			if(ExecutingResult.FAIL == executingResult ){
				return false;
			}
		}
		return true;
	}
	public void updateStageDone(Step level, ExecutingResult executingResult){
		if(level == Step.FST){
			this.fstStagelastModifiedTime = new Date();
			this.fstStageExcutingStatus = ExecutingStatus.DONE;
			this.fstStageExcutingResult = executingResult;
		}
		if(level == Step.SND){
			this.sndStagelastModifiedTime = new Date();
			this.sndStageExcutingStatus = ExecutingStatus.DONE;
			this.sndStageExcutingResult = executingResult;
		}
		if(level == Step.TRD){
			this.trdStagelastModifiedTime = new Date();
			this.trdStageExcutingStatus = ExecutingStatus.DONE;
			this.trdStageExcutingResult = executingResult;
		}
		if(level == Step.FOURTH){
			this.fourthStagelastModifiedTime = new Date();
			this.fourthStageExcutingStatus = ExecutingStatus.DONE;
			this.fourthStageExcutingResult = executingResult;
		}
		if(level == Step.FIFTH){
			this.fifthStagelastModifiedTime = new Date();
			this.fifthStageExcutingStatus = ExecutingStatus.DONE;
			this.fifthStageExcutingResult = executingResult;
		}
	}
	public void registeFstStage(int retryTimes, long timeout, String beanName, String methodName, int chunkSize, int priority){
		
		this.fstStageExcutingStatus = ExecutingStatus.CREATE;
		this.fstStagelastModifiedTime = new Date();
		this.fstStageRetryTimes = retryTimes;
		this.fstStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), (int) timeout);
		this.fstStageTimeout = timeout;
		this.fstStageUuid = UUID.randomUUID().toString();
		
		this.fstBeanName = beanName;
		this.fstMethodName = methodName;
		this.fstStageChunkSize = chunkSize;
		this.fstStagePriority = priority;
		
	}
	public void registeSndStage(int retryTimes, long timeout, String beanName, String methodName, int chunkSize, int priority){
		
		this.sndStageExcutingStatus = ExecutingStatus.CREATE;
		this.sndStagelastModifiedTime = new Date();
		this.sndStageRetryTimes = retryTimes;
		this.sndStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), (int) timeout);
		this.sndStageTimeout = timeout;
		this.sndStageUuid = UUID.randomUUID().toString();
		
		this.sndBeanName = beanName;
		this.sndMethodName = methodName;
		this.sndStageChunkSize = chunkSize;
		
		this.sndStagePriority = priority;
		
	}
	public void registeTrdStage(int retryTimes, long timeout, String beanName, String methodName, int chunkSize, int priority){
		
		this.trdStageExcutingStatus = ExecutingStatus.CREATE;
		this.trdStagelastModifiedTime = new Date();
		this.trdStageRetryTimes = retryTimes;
		this.trdStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), (int) timeout);
		this.trdStageTimeout = timeout;
		this.trdStageUuid = UUID.randomUUID().toString();
		
		this.trdBeanName = beanName;
		this.trdMethodName = methodName;
		this.trdStageChunkSize = chunkSize;
		
		this.trdStagePriority =priority;
	}
	public void registeFourthStage(int retryTimes, long timeout, String beanName, String methodName, int chunkSize, int priority){
		this.fourthStageExcutingStatus = ExecutingStatus.CREATE;
		this.fourthStagelastModifiedTime = new Date();
		this.fourthStageRetryTimes = retryTimes;
		this.fourthStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), (int) timeout);
		this.fourthStageTimeout = timeout;
		this.fourthStageUuid = UUID.randomUUID().toString();
		
		this.fourthBeanName = beanName;
		this.fourthMethodName = methodName;
		this.fourthStageChunkSize = chunkSize;
		
		this.fourthStagePriority = priority;
	}
	public void registeFifthStage(int retryTimes, long timeout, String beanName, String methodName, int chunkSize){
		this.fifthStageExcutingStatus = ExecutingStatus.CREATE;
		this.fifthStagelastModifiedTime = new Date();
		this.fifthStageRetryTimes = retryTimes;
		this.fifthStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), (int) timeout);
		this.fifthStageTimeout = timeout;
		this.fifthStageUuid = UUID.randomUUID().toString();
		
		this.fifthBeanName = beanName;
		this.fifthMethodName = methodName;
		this.fifthStageChunkSize = chunkSize;
	}
	public void registeStage(Step level, int retryTimes, long timeout, String beanName, String methodName, int chunkSize, int priority){
			
		if(level == Step.FST){
			registeFstStage(retryTimes, timeout, beanName, methodName, chunkSize, priority);
		}
		if(level == Step.SND){
			registeSndStage(retryTimes, timeout, beanName, methodName, chunkSize, priority);
		}
		if(level == Step.TRD){
			registeTrdStage(retryTimes, timeout, beanName, methodName, chunkSize, priority);
		}
		if(level == Step.FOURTH){
			registeFourthStage(retryTimes, timeout, beanName, methodName, chunkSize, priority);
		}
		if(level == Step.FIFTH){
			registeFifthStage(retryTimes, timeout, beanName, methodName, chunkSize);
		}
	}
	public String getStageUuid(Step level){
		
		if(level == Step.FST){
			return this.getFstStageUuid();
		}
		if(level == Step.SND){
			return this.getSndStageUuid();
		}
		if(level == Step.TRD){
			return this.getTrdStageUuid();
		}
		if(level == Step.FOURTH){
			return this.getFourthStageUuid();
		}
		return this.getFifthStageUuid();
	}
	public void retryStage(Step level, int timeOut) {
		
		if(level == Step.FST){
			
			this.fstStagelastModifiedTime = new Date();
			this.fstStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), timeOut);
			this.fstStageRetryTimes--;
			this.fstStageTimeout = timeOut;
			this.fstStageExcutingResult = null;
			this.fstStageExcutingStatus = ExecutingStatus.CREATE;
			
		}
		if(level == Step.SND){
					
			this.sndStagelastModifiedTime = new Date();
			this.sndStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), timeOut);
			this.sndStageRetryTimes--;
			this.sndStageTimeout = timeOut;
			this.sndStageExcutingResult = null;
			this.sndStageExcutingStatus = ExecutingStatus.CREATE;
					
		}
		if(level == Step.TRD){
			
			this.trdStagelastModifiedTime = new Date();
			this.trdStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), timeOut);
			this.trdStageRetryTimes--;
			this.trdStageTimeout = timeOut;
			this.trdStageExcutingResult = null;
			this.trdStageExcutingStatus = ExecutingStatus.CREATE;
			
		}
		if(level == Step.FOURTH){
			
			this.fourthStagelastModifiedTime = new Date();
			this.fourthStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), timeOut);
			this.fourthStageRetryTimes--;
			this.fourthStageTimeout = timeOut;
			this.fourthStageExcutingResult = null;
			this.fourthStageExcutingStatus = ExecutingStatus.CREATE;
			
		}
		if(level == Step.FIFTH){
			
			this.fifthStagelastModifiedTime = new Date();
			this.fifthStageCurrentTaskExpiredTime = DateUtils.addMilliseconds(new Date(), timeOut);
			this.fifthStageRetryTimes--;
			this.fifthStageTimeout = timeOut;
			this.fifthStageExcutingResult = null;
			this.fifthStageExcutingStatus = ExecutingStatus.CREATE;
			
		}
		
	}


	public int getFstStagePriority() {
		return fstStagePriority;
	}


	public void setFstStagePriority(int fstStagePriority) {
		this.fstStagePriority = fstStagePriority;
	}


	public int getSndStagePriority() {
		return sndStagePriority;
	}


	public void setSndStagePriority(int sndStagePriority) {
		this.sndStagePriority = sndStagePriority;
	}


	public int getTrdStagePriority() {
		return trdStagePriority;
	}


	public void setTrdStagePriority(int trdStagePriority) {
		this.trdStagePriority = trdStagePriority;
	}


	public int getFourthStagePriority() {
		return fourthStagePriority;
	}


	public void setFourthStagePriority(int fourthStagePriority) {
		this.fourthStagePriority = fourthStagePriority;
	}


	public int getFifthStagePriority() {
		return fifthStagePriority;
	}


	public void setFifthStagePriority(int fifthStagePriority) {
		this.fifthStagePriority = fifthStagePriority;
	}
	
}