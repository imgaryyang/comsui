package com.suidifu.dowjones.service;

import java.util.Date;

/**
 */
public interface DailyRemittanceService {

    /**
     * 统计某天的放款数据
     *
     * @param financialContractUuid 信托uuid
     * @param doDate                日期
     */
    void doRemittanceData(String financialContractUuid, Date doDate);

}
