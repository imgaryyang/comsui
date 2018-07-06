package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Date;

public interface DailyGuaranteeDAO {

    void saveData(Dataset<Row> rows, String sql);

    void deleteData(String financialContractUuid, Date doingDay);

}
