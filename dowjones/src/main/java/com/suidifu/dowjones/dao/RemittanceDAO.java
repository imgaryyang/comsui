package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Date;

/**
 * Created by veda on 02/01/2018.
 */
public interface RemittanceDAO {


    /**
     * 更新的时候 删除先前已经存在的数据
     *
     * @param financialContractUuid
     * @param time
     */
    void deleteExistData(String financialContractUuid, Date time);


    /**
     * 计划订单统计
     *
     * @param financialContractUuid
     * @param time
     */
    Dataset<Row> remittanceApplicationStatistics(String financialContractUuid, Date time);


    /**
     * @param financialContractUuid
     * @param time
     * @return
     */
    Dataset<Row> remittancePlanStatistics(String financialContractUuid, Date time);


    /**
     * @param financialContractUuid
     * @param time
     * @return
     */
    Dataset<Row> remittancePlanExecLogStatistics(String financialContractUuid, Date time);


    /**
     * @param financialContractUuid
     * @param time
     * @return
     */
    Dataset<Row> assetStatistics(String financialContractUuid, Date time);


    /**
     * @param applicationResult
     * @param planResult
     * @param assetResult
     */
    void saveResult(String financialContractUuid, Date time, Row applicationResult, Row planResult, Row execLogResult, Row assetResult);


}
