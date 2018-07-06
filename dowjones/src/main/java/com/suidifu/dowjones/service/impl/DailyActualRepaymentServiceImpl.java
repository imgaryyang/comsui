package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.DailyActualRepaymentDAO;
import com.suidifu.dowjones.dao.DailyPartRepaymentDAO;
import com.suidifu.dowjones.dao.DailyPreRepaymentDAO;
import com.suidifu.dowjones.dao.QuartzJobDAO;
import com.suidifu.dowjones.service.ActualRepaymentService;
import com.suidifu.dowjones.service.DailyActualRepaymentService;
import com.suidifu.dowjones.service.DailyTaskConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class DailyActualRepaymentServiceImpl implements DailyActualRepaymentService {

    @Autowired
    private QuartzJobDAO quartzJobDAO;
    @Autowired
    private ActualRepaymentService actualRepaymentService;
    @Autowired
    private DailyActualRepaymentDAO dailyActualRepaymentDAO;
    @Autowired
    private DailyPreRepaymentDAO dailyPreRepaymentDAO;
    @Autowired
    private DailyPartRepaymentDAO dailyPartRepaymentDAO;

    @Override
    public void executeDailyActualRepaymentJob(String financialContractUuid, Date doingDay) {
        int status = -1;
        try {

            Integer processStatus = quartzJobDAO
                    .getQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_ACTUAL_REPAYMENT);

            if (Objects.equals(processStatus, DailyTaskConst.STATUS_PROCESSING)) {
                log.info("存在处理中的job...");
                return;
            }

            if (processStatus == null) {
                quartzJobDAO.insertQuartzJob(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_ACTUAL_REPAYMENT,
                        DailyTaskConst.STATUS_PROCESSING);
            } else {
                quartzJobDAO
                        .modifyQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_ACTUAL_REPAYMENT,
                                DailyTaskConst.STATUS_PROCESSING, new Date(), null);
                dailyActualRepaymentDAO.deleteData(financialContractUuid, doingDay);

                dailyPreRepaymentDAO.deleteData(financialContractUuid, doingDay);
                dailyPartRepaymentDAO.deleteData(financialContractUuid, doingDay);
            }

            actualRepaymentService.doOnlineRepaymentData(financialContractUuid, doingDay);
            actualRepaymentService.doOfflineRepaymentData(financialContractUuid, doingDay);
            actualRepaymentService.doOfflinePaymentData(financialContractUuid, doingDay);

            actualRepaymentService.doPrePaymentData(financialContractUuid, doingDay);
            actualRepaymentService.doPartPaymentData(financialContractUuid, doingDay);


            status = DailyTaskConst.STATUS_COMPLETE;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("job执行异常...");
            status = DailyTaskConst.STATUS_ABNORMAL;
        } finally {
            quartzJobDAO
                    .modifyQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_ACTUAL_REPAYMENT,
                            status, null, new Date());
        }
    }

}
