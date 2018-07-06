package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.AssetSetDAO;
import com.suidifu.dowjones.dao.DailyPlanRepaymentDAO;
import com.suidifu.dowjones.service.PlanRepaymentService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xwq
 */
@Service("planRepaymentService")
@Slf4j
public class PlanRepaymentServiceImpl implements PlanRepaymentService {
    @Resource
    private AssetSetDAO assetSetDAO;
    @Resource
    private DailyPlanRepaymentDAO dailyPlanRepaymentDAO;

    //应收款-应还日计划还款
    @Override
    public void doPlanRepaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing plan repayment data for [{}, {}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = assetSetDAO.getPlanRepaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get plan repayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyPlanRepaymentDAO.saveData(result, "daily_plan_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save plan repayment data end, time used {}ms", savaDataEndtime - getDataEndtime);

        log.info("Total time Used :{}", savaDataEndtime - startTime);
        log.info("End doing plan repayment data for [{}, {}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //应收款-宽限期内计划还款
    @Override
    public void doNotOverduePlanRepaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing notOverdue plan repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = assetSetDAO.getNotOverduePlanRepaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get not overdue plan repayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyPlanRepaymentDAO.saveData(result, "daily_plan_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save not overdue plan repayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get not overdue plan repayment data use time:{}", savaDataEndtime - startTime);
        log.info("End doing notOverdue plan repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //应收款-待确认计划还款
    @Override
    public void doUnconfirmedPlanRepaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing unconfirmed plan repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = assetSetDAO.getUnconfirmedPlanRepaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get unconfirmed plan repayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyPlanRepaymentDAO.saveData(result, "daily_plan_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save unconfirmed plan repayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get unconfirmed plan repayment data use time :{}", savaDataEndtime - startTime);
        log.info("End doing unconfirmed plan repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //应收款-已逾期计划还款
    @Override
    public void doOverduePlanRepaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing overdue plan repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = assetSetDAO.getOverduePlanRepaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get overdue plan repayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyPlanRepaymentDAO.saveData(result, "daily_plan_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save overdue plan repayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get overdue plan repayment data use total time:{}", savaDataEndtime - startTime);
        log.info("End doing overdue plan repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }
}
