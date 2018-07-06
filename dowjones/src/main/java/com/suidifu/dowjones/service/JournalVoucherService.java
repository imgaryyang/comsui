package com.suidifu.dowjones.service;

import java.util.Date;

/**
 * code is far away from bug with the animal protecting
 *
 * @author veda
 * @date 28/12/2017
 */
public interface JournalVoucherService {

    /**
     * 处理一个信托的某天的回购数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doRepurchaseData(String financialContractUuid, Date doingDay);

    /**
     * 处理一个信托的某天的担保数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param doingDay              某天
     */
    void doGuaranteeData(String uuid, Date yesterday);
}
