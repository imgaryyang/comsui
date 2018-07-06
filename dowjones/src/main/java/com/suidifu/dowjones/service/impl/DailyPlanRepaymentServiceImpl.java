package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.DailyPlanRepaymentDAO;
import com.suidifu.dowjones.dao.QuartzJobDAO;
import com.suidifu.dowjones.service.DailyPlanRepaymentService;
import com.suidifu.dowjones.service.DailyTaskConst;
import com.suidifu.dowjones.service.PlanRepaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class DailyPlanRepaymentServiceImpl implements DailyPlanRepaymentService {
    @Autowired
    private QuartzJobDAO quartzJobDAO;
    @Autowired
    private PlanRepaymentService planRepaymentService;
    @Autowired
    private DailyPlanRepaymentDAO dailyPlanRepaymentDAO;

    @Override
    public void executeDailyPlanRepaymentJob(String financialContractUuid, Date doingDay) {
        int status = -1;
        try {

            Integer processStatus = quartzJobDAO
                    .getQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_PLAN_REPAYMENT);

            if (Objects.equals(processStatus, DailyTaskConst.STATUS_PROCESSING)) {
                log.info("存在处理中的job...");
                return;
            }

            if (processStatus == null) {
                quartzJobDAO.insertQuartzJob(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_PLAN_REPAYMENT,
                        DailyTaskConst.STATUS_PROCESSING);
            } else {
                quartzJobDAO
                        .modifyQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_PLAN_REPAYMENT,
                                DailyTaskConst.STATUS_PROCESSING, new Date(), null);
                dailyPlanRepaymentDAO.deleteData(financialContractUuid, doingDay);
            }

            planRepaymentService.doPlanRepaymentData(financialContractUuid, doingDay);
            planRepaymentService.doNotOverduePlanRepaymentData(financialContractUuid, doingDay);
            planRepaymentService.doUnconfirmedPlanRepaymentData(financialContractUuid, doingDay);
            planRepaymentService.doOverduePlanRepaymentData(financialContractUuid, doingDay);

            status = DailyTaskConst.STATUS_COMPLETE;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("job执行异常...");
            status = DailyTaskConst.STATUS_ABNORMAL;
        } finally {
            quartzJobDAO
                    .modifyQuartzJobProcessStatus(doingDay, financialContractUuid, DailyTaskConst.TASK_DAILY_PLAN_REPAYMENT,
                            status, null, new Date());
        }
    }

}
