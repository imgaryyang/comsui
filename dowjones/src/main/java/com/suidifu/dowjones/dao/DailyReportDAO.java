package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/20 <br>
 * @time: 下午8:52 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface DailyReportDAO {
    void saveData(Dataset<Row> rows, String sql);
}