package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.DailyRepurchaseDAO;
import com.suidifu.dowjones.dao.QuartzJobDAO;
import com.suidifu.dowjones.service.DailyRepurchaseService;
import com.suidifu.dowjones.service.DailyTaskConst;
import com.suidifu.dowjones.service.JournalVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class DailyRepurchaseServiceImpl implements DailyRepurchaseService {


    @Autowired
    private QuartzJobDAO quartzJobDAO;
    @Autowired
    private JournalVoucherService journalVoucherService;
    @Autowired
    private DailyRepurchaseDAO dailyRepurchaseDAO;

    @Override
    public void executeDailyRepurchaseJob(String financialContractUuid, Date doingDay) {

        int status = -1;

        try {

            Integer processStatus = quartzJobDAO
                    .getQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_REPURCHASE);

            if (Objects.equals(processStatus, DailyTaskConst.STATUS_PROCESSING)) {
                log.info("存在处理中的job...");
                return;
            }

            if (processStatus == null) {
                quartzJobDAO.insertQuartzJob(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_REPURCHASE,
                        DailyTaskConst.STATUS_PROCESSING);
            } else {
                quartzJobDAO
                        .modifyQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_REPURCHASE,
                                DailyTaskConst.STATUS_PROCESSING, new Date(), null);
                dailyRepurchaseDAO.deleteData(financialContractUuid, doingDay);
            }

            journalVoucherService.doRepurchaseData(financialContractUuid, doingDay);

            status = DailyTaskConst.STATUS_COMPLETE;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("job执行异常...");
            status = DailyTaskConst.STATUS_ABNORMAL;
        } finally {
            quartzJobDAO
                    .modifyQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_REPURCHASE,
                            status, null, new Date());
        }
    }
}
