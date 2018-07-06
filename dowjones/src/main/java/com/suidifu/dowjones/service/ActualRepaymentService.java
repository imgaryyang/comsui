package com.suidifu.dowjones.service;

import java.util.Date;

/**
 * @author xwq
 */
public interface ActualRepaymentService {
    /**
     * 处理一个信托的某天的实际还款-线上还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doOnlineRepaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的实际还款-线下还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doOfflineRepaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的实际还款-线下支付单数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doOfflinePaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的实际还款-在途资金数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doOnTheWayMoneyData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的实际还款-提前还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doPrePaymentData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的实际还款-部分还款数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doPartPaymentData(String financialContractUuid, Date doingDay);

}
