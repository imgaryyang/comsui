package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Collection;
import java.util.Date;

/**
 * Created by zxj on 2018/2/7.
 */
public interface ABS_RepaymentAndContractDAO {
    /**
     * 获取还款信息 uniqueId assetUuid financialContractUuid
     * @param start
     * @param end
     */
    Dataset<Row> getTencentAbsTrailData(Date start, Date end, int fileType);

    /**
     * 获取信托合同
     * @param financialUuids
     * @return
     */
    Dataset<Row> getFinancialContractDataset(Collection<Object> financialUuids);

    /**
     * 取出所有还款计划
     * @param assetUuids
     * @return
     */
    Dataset<Row> getAssetDatasetByAssetUuid(Collection<Object> assetUuids);

    /**
     * 取出所以还款计划
     * @param contractUuids
     * @return
     */
    Dataset<Row> getAssetDatasetByContractUuid(Collection<Object> contractUuids);

    /**
     * 取出所有的贷款合同
     * @param contractUuids
     * @return
     */
    Dataset<Row> getContractDataset(Collection<Object> contractUuids);

    /**
     * 取出应收费用总和
     * @param assetUuids
     * @return
     */
    Dataset<Row> getAssetSetExtraCharge(Collection<Object> assetUuids);

    /**
     * 获取实收本金、利息、七项费用之和
     * @param assetUuids
     * @return
     */
    Dataset<Row> getPaidOffDs(Collection<Object> assetUuids);

    /**
     * 获取客户信息
     * @param customerUuids
     * @return
     */
    Dataset<Row> getCustomerDataset(Collection<Object> customerUuids);

    /**
     * 获取应收未收 本金 利息 其他费用
     * @param contractUuids
     * @return
     */
    Dataset<Row> getRemainingAmountDataset(Collection<Object> contractUuids);
}
