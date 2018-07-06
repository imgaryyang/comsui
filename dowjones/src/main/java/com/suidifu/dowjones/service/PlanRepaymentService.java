package com.suidifu.dowjones.service;

import java.util.Date;

public interface PlanRepaymentService {
    /**
     * 处理一个信托的某天的应还日计划还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doPlanRepaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的宽限期内计划还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doNotOverduePlanRepaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的待确认计划还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doUnconfirmedPlanRepaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的已逾期计划还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doOverduePlanRepaymentData(String financialContractUuid, Date doingDay);

}
