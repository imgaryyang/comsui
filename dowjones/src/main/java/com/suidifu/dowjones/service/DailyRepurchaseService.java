package com.suidifu.dowjones.service;

import java.util.Date;

public interface DailyRepurchaseService {

    void executeDailyRepurchaseJob(String financialContractUuid, Date doingDay);

}
