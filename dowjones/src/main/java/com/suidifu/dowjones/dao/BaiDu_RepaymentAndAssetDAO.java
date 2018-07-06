package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by zxj on 2018/1/22.
 */
public interface BaiDu_RepaymentAndAssetDAO {

    void saveData(Dataset<Row> rows, String sql);

    /**
     * 从当日生成的还款记录中获取贷款合同uuid数组
     *
     * @param financialContractUuid
     * @param yesterday
     * @return
     */
    Set<Object> getContractUuidListFromJv(String financialContractUuid, String yesterday);

    /**
     * 当日放款成功的所有的贷款合同uuid
     *
     * @param financialContractUuid
     * @param yesterday
     * @return
     */
    List<Object> getContractUuidListFromRapp(String financialContractUuid, String yesterday);

    /**
     * 取出所有贷款合同dataset
     *
     * @param contractUuids
     * @return
     */
    Dataset<Row> getContractDataset(Collection<Object> contractUuids);

    /**
     * 取出所有还款计划dataset
     *
     * @param contractUuids
     * @return
     */
    Dataset<Row> getAssetDataset(Collection<Object> contractUuids);

    /**
     * 取出所有还款记录dataset
     *
     * @param assetUuids
     * @return
     */
    Dataset<Row> getJournalVoucherDataset(Collection<Object> assetUuids);

    /**
     * 生成还款文件
     *
     * @param contractDataSet
     * @param assetDataSet
     * @param jvDataSet
     */
    void doRepaymentFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Dataset<Row> jvDataSet, Collection<Object> assetUuids, Date time);

    /**
     * 生成新增还款文件
     *
     * @param contractDataSet
     * @param assetDataSet
     * @param assetUuids
     * @param contractUuidList
     * @param time
     */
    void doIncrementalRepaymentFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Collection<Object> assetUuids, Collection<Object> contractUuidList, Date time);

    /**
     * 生成资产文件
     *
     * @param contractDataSet
     * @param assetDataSet
     * @param assetUuids
     * @param time
     */
    void doAssetFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Collection<Object> assetUuids, Collection<Object> contractUuidList, Date time);

    /**
     * 生成新增资产文件
     *
     * @param contractDataSet
     * @param assetDataSet
     * @param assetUuids
     * @param time
     */
    void doIncrementalAssetFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Collection<Object> assetUuids, Collection<Object> contractUuidList, Date time);
}
