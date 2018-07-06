package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午9:18 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface AssetSetDAO {
    Dataset<Row> getAllOpenRepaymentPlanList(String idNumber);

    Dataset<Row> getDailyOverStatusContractNumber();

    /**
     * 实际还款-提前还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getPrePaymentData(String financialContractUuid, Date time);

    /**
     * 实际还款-部分还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getPartPaymentData(String financialContractUuid, Date time);

    /**
     * 应收款-应还日计划还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getPlanRepaymentData(String financialContractUuid, Date time);

    /**
     * 应收款-宽限期内计划还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getNotOverduePlanRepaymentData(String financialContractUuid, Date time);

    /**
     * 应收款-待确认计划还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param doDate
     * @return
     */
    Dataset<Row> getUnconfirmedPlanRepaymentData(String financialContractUuid, Date doDate);

    /**
     * 应收款-已逾期计划还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param doDate
     * @return
     */
    Dataset<Row> getOverduePlanRepaymentData(String financialContractUuid, Date doDate);

}