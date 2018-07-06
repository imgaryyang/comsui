package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.QuartzJobDAO;
import com.suidifu.dowjones.dao.RemittanceDAO;
import com.suidifu.dowjones.service.DailyRemittanceService;
import com.suidifu.dowjones.service.DailyTaskConst;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author veda
 */
@Slf4j
@Service("DailyRemittanceService")
public class DailyRemittanceServiceImpl implements DailyRemittanceService {

    @Autowired
    private transient RemittanceDAO remittanceDAO;

    @Autowired
    private transient QuartzJobDAO quartzJobDAO;


    @Override
    public void doRemittanceData(String financialContractUuid, Date doDate) {

        int status = -1;
        int taskType = DailyTaskConst.TASK_DAILY_REMITTANCE;

        try {

            Integer processStatus = quartzJobDAO.getQuartzJobProcessStatus(doDate, financialContractUuid, taskType);

            if (Objects.equals(processStatus, DailyTaskConst.STATUS_PROCESSING)) {
                log.info("存在处理中的job...");
                return;
            }

            if (processStatus == null) {
                quartzJobDAO.insertQuartzJob(doDate, financialContractUuid, taskType, DailyTaskConst.STATUS_PROCESSING);
            } else {
                quartzJobDAO.modifyQuartzJobProcessStatus(doDate, financialContractUuid, taskType,
                        DailyTaskConst.STATUS_PROCESSING, new Date(), null);
                remittanceDAO.deleteExistData(financialContractUuid, doDate);
            }

            doWork(financialContractUuid, doDate);

            status = DailyTaskConst.STATUS_COMPLETE;

        } catch (Exception e) {
            e.printStackTrace();
            log.info("job执行异常...");
            status = DailyTaskConst.STATUS_ABNORMAL;
        } finally {
            quartzJobDAO
                    .modifyQuartzJobProcessStatus(doDate, financialContractUuid, taskType, status, null, new Date());
        }
    }

    private void doWork(String financialContractUuid, Date doDate) {

        log.info("Start remittance job, parameter[{},{}]", financialContractUuid, DateUtils.getDateFormatYYMMDD(doDate));

        Long startTime = System.currentTimeMillis();

        Dataset<Row> applicationResults = remittanceDAO.remittanceApplicationStatistics(financialContractUuid, doDate);

        Long getRemittanceApplicationData = System.currentTimeMillis();
        log.info("Get remittance application date use time :{}", getRemittanceApplicationData - startTime);

        Row applicationResult = applicationResults.count() == 0 ? null : applicationResults.first();

        Dataset<Row> planResults = remittanceDAO.remittancePlanStatistics(financialContractUuid, doDate);

        Row planResult = planResults.count() == 0 ? null : planResults.first();
        Long getRemittancePlanData = System.currentTimeMillis();
        log.info("Get remittance plan data use time :{}", getRemittancePlanData - getRemittanceApplicationData);

        Dataset<Row> execLogResults = remittanceDAO.remittancePlanExecLogStatistics(financialContractUuid, doDate);

        Row execLogResult = execLogResults.count() == 0 ? null : execLogResults.first();

        Long getExecLogResult = System.currentTimeMillis();
        log.info("Get remittance execLog data use time :{}", getExecLogResult - getRemittancePlanData);

        Dataset<Row> assetResults = remittanceDAO.assetStatistics(financialContractUuid, doDate);

        Row assetResult = assetResults.count() == 0 ? null : assetResults.first();

        Long getAssetDate = System.currentTimeMillis();
        log.info("Get remittance asset data use time :{}", getAssetDate - getExecLogResult);

        remittanceDAO
                .saveResult(financialContractUuid, doDate, applicationResult, planResult, execLogResult, assetResult);

        log.info("Remittance job use time :{}");

        log.info("End remittance job, parameter[{},{}]", financialContractUuid, DateUtils.getDateFormatYYMMDD(doDate));

    }
}
