package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.*;
import com.suidifu.dowjones.service.ActualRepaymentService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author xwq
 */
@Service("actualRepaymentService")
@Slf4j
public class ActualRepaymentServiceImpl implements ActualRepaymentService {
    @Autowired
    private JournalVoucherDAO journalVoucherDAO;
    @Autowired
    private AssetSetDAO assetSetDAO;
    @Autowired
    private DailyActualRepaymentDAO dailyActualRepaymentDAO;
    @Autowired
    private DailyPreRepaymentDAO dailyPreRepaymentDAO;
    @Autowired
    private DailyPartRepaymentDAO dailyPartRepaymentDAO;

    //实际还款-线上还款
    @Override
    public void doOnlineRepaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing online repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = journalVoucherDAO.getOnlineRepaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get online repayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyActualRepaymentDAO.saveData(result, "daily_actual_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save online repayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get online repayment data use time:{}", savaDataEndtime - startTime);
        log.info("End doing online repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //实际还款-线下还款
    @Override
    public void doOfflineRepaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing offline repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = journalVoucherDAO.getOfflineRepaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get offline repayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyActualRepaymentDAO.saveData(result, "daily_actual_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save offline repayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get offline repayment data use total time:{}", savaDataEndtime - startTime);
        log.info("End doing offline repayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //实际还款-线下支付单
    @Override
    public void doOfflinePaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing offline payment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = journalVoucherDAO.getOfflinePaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get offline payment data end, time used :{}ms", getDataEndtime - startTime);

        dailyActualRepaymentDAO.saveData(result, "daily_actual_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save offline payment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get offline payment data total use total time:{}", savaDataEndtime - startTime);
        log.info("End doing offline payment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //实际还款-在途资金
    @Override
    public void doOnTheWayMoneyData(String financialContractUuid, Date doingDay) {
//		Dataset<Row> result = auditJobDAO.getOnTheWayMoneyData();
    }

    //实际还款-提前还款
    @Override
    public void doPrePaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing prePayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = assetSetDAO.getPrePaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get prePayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyPreRepaymentDAO.saveData(result, "daily_pre_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save prePayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get pre payment data use total time:{}", savaDataEndtime - startTime);
        log.info("End doing prePayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }

    //实际还款-部分还款
    @Override
    public void doPartPaymentData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing partPayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = assetSetDAO.getPartPaymentData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get partPayment data end, time used :{}ms", getDataEndtime - startTime);

        dailyPartRepaymentDAO.saveData(result, "daily_part_repayment");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save partPayment data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get part payment data use total time:{}", savaDataEndtime - startTime);
        log.info("End doing partPayment data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);

    }
}
