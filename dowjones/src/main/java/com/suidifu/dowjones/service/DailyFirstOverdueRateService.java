package com.suidifu.dowjones.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zxj on 2018/3/15.
 */
public interface DailyFirstOverdueRateService {

     void doFile(String financialContractUuid, Date yesterday);
}
