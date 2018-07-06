package com.suidifu.matryoshka.service.delayTask;

import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog;
import com.zufangbao.sun.yunxin.exception.SunException;

import java.util.List;

/**
 * Created by louguanyang on 2017/5/3.
 */
public interface DelayProcessingTaskService {
    void saveOrUpdate(DelayProcessingTask task) throws SunException;

    void delete(String delayProcessingTaskUuid) throws SunException;

    DelayProcessingTask getByUuid(String delayProcessingTaskUuid);

    List<DelayProcessingTask> get_by_repaymentPlanUuid(String repaymentPlanUuid);

    List<DelayProcessingTask> get_by_repurchaseDocUuid(String repurchaseDocUuid);

    public List<DelayProcessingTask> getByConfigUuid(String configUuid, String financialContractUuid);

    void updateByTask(DelayProcessingTask task) throws Exception;

    List<DelayProcessingTaskLog> getDelayProcessingTaskLogByConfigUuid();

    int saveDelayProcessingTaskLog(List<DelayProcessingTaskLog> logs) throws Exception;

    void delDelayProcessingTask(List<DelayProcessingTaskLog> logs) throws Exception;

    List<DelayProcessingTask> getDelayProcessingTaskByFinancialContract(String financialContractUuid);
}
