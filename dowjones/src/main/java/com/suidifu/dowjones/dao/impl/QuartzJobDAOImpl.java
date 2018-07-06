package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.QuartzJobDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class QuartzJobDAOImpl extends BaseDAO implements QuartzJobDAO, Serializable {
    @Resource
    private GenericJdbcSupport genericJdbcSupport;

    @Override
    public Integer getQuartzJobProcessStatus(Date constraintDate, String financialContractUuid, Integer jobType) {

        String constraintDateString = DateUtils.getDateFormatYYMMDD(constraintDate);

        if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(constraintDateString) || jobType == null) {
            return null;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("constraintDateString", constraintDateString);
        parameters.put("jobType", jobType);


        String sql = "SELECT job.process_status" +
                "  FROM quartz_job job" +
                "  WHERE job.financial_contract_uuid = :financialContractUuid" +
                "    AND job.constraint_date = :constraintDateString" +
                "    AND job.job_type = :jobType";

        List<Integer> result = genericJdbcSupport.queryForSingleColumnList(sql, parameters, Integer.class);

        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return result.get(0);
    }

    @Override
    public void modifyQuartzJobProcessStatus(Date constraintDate, String financialContractUuid, Integer jobType, Integer processStatus, Date startTime, Date endTime) {

        String constraintDateString = DateUtils.getDateFormatYYMMDD(constraintDate);

        if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(constraintDateString) || jobType == null) {
            return;
        }

        String modifyStartTime = startTime == null ? "" : ",start_time = :startTime";
        String modifyEndTime = endTime == null ? "" : ",end_time = :endTime";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("constraintDateString", constraintDateString);
        parameters.put("jobType", jobType);
        parameters.put("processStatus", processStatus);
        parameters.put("startTime", startTime == null ? null : DateUtils.getDateFormatYYMMDDHHMMSS(startTime));
        parameters.put("endTime", endTime == null ? null : DateUtils.getDateFormatYYMMDDHHMMSS(endTime));

        String sql = "UPDATE quartz_job" +
                "  SET process_status = :processStatus" + modifyStartTime + modifyEndTime +
                "  WHERE financial_contract_uuid = :financialContractUuid" +
                "    AND constraint_date = :constraintDateString" +
                "    AND job_type = :jobType";

        genericJdbcSupport.executeSQL(sql, parameters);
    }

    @Override
    public void insertQuartzJob(Date constraintDate, String financialContractUuid, Integer jobType, Integer processStatus) {
        String constraintDateString = DateUtils.getDateFormatYYMMDD(constraintDate);
        if (StringUtils.isBlank(financialContractUuid) || StringUtils.isBlank(constraintDateString) || jobType == null || processStatus == null) {
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", financialContractUuid);
        parameters.put("constraintDateString", constraintDateString);
        parameters.put("jobType", jobType);
        parameters.put("processStatus", processStatus);

        String sql = "INSERT INTO `quartz_job` (`uuid`,`financial_contract_uuid`, `constraint_date`,  `process_status`, `job_type`, `create_time`, `start_time`)" +
                "  VALUES (uuid(), :financialContractUuid, :constraintDateString, :processStatus, :jobType, now(), now());";

        genericJdbcSupport.executeSQL(sql, parameters);
    }


}
