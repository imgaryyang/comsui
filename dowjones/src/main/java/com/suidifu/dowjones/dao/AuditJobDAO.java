package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface AuditJobDAO {
    /**
     * 实际还款-在途资金
     *
     * @return
     */
    Dataset<Row> getOnTheWayMoneyData();

}
