package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Collection;
import java.util.Date;

/**
 * Created by zxj on 2018/3/15.
 */
public interface DailyFirstOverdueRateDAO {

    /**
     * 获取所以的信托合同
     * @return
     */
    Dataset<Row> getAllFinancialContract();

    /**
     * 获取信托合同
     * @param financialContractUuid
     * @return
     */
    Row getFinancialContract(String financialContractUuid);

    /**
     * 获取当天到期的有效的还款计划
     * @param financialContractUuid
     * @param overdueDate
     * @return
     */
    Dataset<Row> getAssetSet(String financialContractUuid, Date overdueDate);

    /**
     * 获取doingDate 之前的所有已还的还款记录
     * @param assetsetUuid
     * @return
     */
    Dataset<Row> getAllRepayment(Collection<Object> assetsetUuid, Date doingDate);

    /**
     * 获取doingDate的线下还款的还款记录
     * @param assetsetUuid
     * @return
     */
    Dataset<Row> getOfflineRepayment(Collection<Object> assetsetUuid, Date doingDate);

    /**
     * 通过 还款记录uuid查询实收 本金 利息
     * @param journalVoucherUuid
     * @return
     */
    Dataset<Row> getPaidData(Collection<Object> journalVoucherUuid);

}
