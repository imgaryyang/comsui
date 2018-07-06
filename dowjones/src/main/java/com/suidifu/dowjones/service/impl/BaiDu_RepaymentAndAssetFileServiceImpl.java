package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.BaiDu_RepaymentAndAssetDAO;
import com.suidifu.dowjones.service.BaiDu_RepaymentAndAssetFileService;
import com.suidifu.dowjones.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zxj on 2018/1/22.
 */
@Service("BaiDu_RepaymentAndAssetFileService")
@Slf4j
public class BaiDu_RepaymentAndAssetFileServiceImpl implements BaiDu_RepaymentAndAssetFileService {
    @Autowired
    private BaiDu_RepaymentAndAssetDAO repaymentAndAssetDAO;

    @Override
    public void doFileData(String financialContractUuid, Date yesterday) {
        long startTime = System.currentTimeMillis();

        log.info("Start doing file data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(yesterday), startTime);

        String time = DateUtils.getDateFormatYYMMDD(yesterday);
        if (StringUtils.isBlank(time) || StringUtils.isBlank(financialContractUuid)) {
            log.info("Error params for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(yesterday), startTime);
            return;
        }
        //获取贷款合同uuid数组
        Set<Object> contractUuidList = repaymentAndAssetDAO.getContractUuidListFromJv(financialContractUuid, time);
        //获取贷款合同信息
        Dataset<Row> contractDataset = repaymentAndAssetDAO.getContractDataset(contractUuidList);
        //获取还款计划信息
        Dataset<Row> assetsetDataset = repaymentAndAssetDAO.getAssetDataset(contractUuidList);
        //获取还款计划uuid数组
        List<Object> assetUuidLists = new ArrayList<>();
        if (assetsetDataset != null && assetsetDataset.count() >0) {
            Dataset<Row> assetUuidList = assetsetDataset.select("asset_uuid");
            for (Row row : assetUuidList.collectAsList()) {
                assetUuidLists.add(row.getAs("asset_uuid"));
            }
        }
        //获取还款记录信息
        Dataset<Row> jvDataset = repaymentAndAssetDAO.getJournalVoucherDataset(assetUuidLists);
        //生成还款文件
        repaymentAndAssetDAO.doRepaymentFile(contractDataset, assetsetDataset, jvDataset, assetUuidLists,yesterday);
        //生成资产文件
        repaymentAndAssetDAO.doAssetFile(contractDataset, assetsetDataset, assetUuidLists,contractUuidList, yesterday);

        long saveDataEndTime = System.currentTimeMillis();

        log.info("Total time Used :{}", saveDataEndTime - startTime);
        log.info("End doing file data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(yesterday), saveDataEndTime);
    }

    @Override
    public void doIncrementalFileData(String financialContractUuid, Date yesterday) {
        long startTime = System.currentTimeMillis();

        log.info("Start doing incremental file data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(yesterday), startTime);

        String time = DateUtils.getDateFormatYYMMDD(yesterday);
        if (StringUtils.isBlank(time) || StringUtils.isBlank(financialContractUuid)) {
            log.info("Error params for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(yesterday), startTime);
            return;
        }
        //获取贷款合同uuid数组
        List<Object> contractUuidList = repaymentAndAssetDAO.getContractUuidListFromRapp(financialContractUuid, time);
        //获取贷款合同信息
        Dataset<Row> contractDataset = repaymentAndAssetDAO.getContractDataset(contractUuidList);
        //获取还款计划信息
        Dataset<Row> assetsetDataset = repaymentAndAssetDAO.getAssetDataset(contractUuidList);
        //获取还款计划uuid数组
        List<Object> assetUuidLists = new ArrayList<>();
        if (assetsetDataset != null && assetsetDataset.count() >0) {
            Dataset<Row> assetUuidList = assetsetDataset.select("asset_uuid");
            for (Row row : assetUuidList.collectAsList()) {
                assetUuidLists.add(row.getAs("asset_uuid"));
            }
        }

        //生成新增资产文件
        repaymentAndAssetDAO.doIncrementalAssetFile(contractDataset, assetsetDataset, assetUuidLists,contractUuidList,yesterday);
        //生成新增还款文件
        repaymentAndAssetDAO.doIncrementalRepaymentFile(contractDataset, assetsetDataset, assetUuidLists,contractUuidList,yesterday);


        long saveDataEndTime = System.currentTimeMillis();

        log.info("Total time Used :{}", saveDataEndTime - startTime);
        log.info("End doing incremental file data for [:{}, :{}], time is :{}", financialContractUuid, DateUtils.getDateFormatYYMMDD(yesterday), saveDataEndTime);
    }
}
