package com.suidifu.dowjones.dao;

import java.util.Date;

public interface QuartzJobDAO {
    /**
     * 获取job状态
     *
     * @param constraintDate        统计日期
     * @param financialContractUuid 信托uuid
     * @param jobType               job类型
     * @return
     */
    Integer getQuartzJobProcessStatus(Date constraintDate, String financialContractUuid, Integer jobType);

    /**
     * 修改job状态
     *
     * @param constraintDate        统计日期
     * @param financialContractUuid 信托uuid
     * @param jobType               job类型
     * @param processStatus         job状态
     * @param startTime             job开始时间
     * @param endTime               job结束时间
     */
    void modifyQuartzJobProcessStatus(Date constraintDate, String financialContractUuid, Integer jobType,
                                      Integer processStatus, Date startTime, Date endTime);

    /**
     * 插入一条新job
     *
     * @param constraintDate        统计日期
     * @param financialContractUuid 信托uuid
     * @param jobType               job类型
     * @param processStatus         job状态
     */
    void insertQuartzJob(Date constraintDate, String financialContractUuid, Integer jobType, Integer processStatus);

}
