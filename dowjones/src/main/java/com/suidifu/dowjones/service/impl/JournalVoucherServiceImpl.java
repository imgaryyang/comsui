package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.DailyGuaranteeDAO;
import com.suidifu.dowjones.dao.DailyRepurchaseDAO;
import com.suidifu.dowjones.dao.JournalVoucherDAO;
import com.suidifu.dowjones.service.JournalVoucherService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author veda
 * @date 28/12/2017
 */
@Service("journalVoucherService")
@Slf4j
public class JournalVoucherServiceImpl implements JournalVoucherService {

    @Autowired
    private JournalVoucherDAO journalVoucherDAO;
    @Autowired
    private DailyRepurchaseDAO dailyRepurchaseDAO;
    @Autowired
    private DailyGuaranteeDAO dailyGuaranteeDAO;

    @Override
    public void doRepurchaseData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();

        log.info("Start doing repurchase data for [{}, {}], time is {}", financialContractUuid,
                DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = journalVoucherDAO.getRepurchaseData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get repurchase data end, time used :{}ms", getDataEndtime - startTime);

        dailyRepurchaseDAO.saveData(result, "daily_repurchase");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save repurchase data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Repurchase total time Used :{}", savaDataEndtime - startTime);
        log.info("End doing repurchase data for [:{}, :{}], time is :{}", financialContractUuid,
                DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);
    }


    @Override
    public void doGuaranteeData(String financialContractUuid, Date doingDay) {

        long startTime = System.currentTimeMillis();
        log.info("Start doing guarantee data for [:{}, :{}], time is :{}", financialContractUuid,
                DateUtils.getDateFormatYYMMDD(doingDay), startTime);

        Dataset<Row> result = journalVoucherDAO.getGuaranteeData(financialContractUuid, doingDay);

        long getDataEndtime = System.currentTimeMillis();
        log.info("Get guarantee data end, time used :{}ms", getDataEndtime - startTime);

        dailyGuaranteeDAO.saveData(result, "daily_guarantee");

        long savaDataEndtime = System.currentTimeMillis();
        log.info("Save guarantee data end, time used :{}ms", savaDataEndtime - getDataEndtime);

        log.info("Get guarantee data use total time :{}", savaDataEndtime - startTime);
        log.info("End doing guarantee data for [:{}, :{}], time is :{}", financialContractUuid,
                DateUtils.getDateFormatYYMMDD(doingDay), savaDataEndtime);
    }
}
