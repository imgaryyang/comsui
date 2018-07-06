package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.text.ParseException;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午10:29 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface RelatedContractDAO {
    Dataset<Row> getRelatedContract(String idNumber) throws ParseException;
}