package com.suidifu.morganstanley.handler;

import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.yunxin.entity.files.*;

import java.util.List;

/**
 * @author louguanyang on 2017/5/23.
 */
public interface FileProcessHandler {
    NotifyApplication build_BQJR_modify_repaymentPlan_notifyApplication(String businessType, FileRepository file, String bizId, List<FileTmp_ModifyRepaymentPlan_CSV> sameTradeNoList, String
            privateKey, String groupNameForModifyPlan);

    NotifyApplication build_mutable_NotifyApplication(String businessType, FileRepository file, String bizId, List<FileTmp_MutableFee_CSV> sameTradeNoList, String
            privateKey, String groupNameForModifyPlan);

    NotifyApplication build_ZHXT_notifyApplication(String businessType, FileRepository file, String bizId, FileTmp_ModifyRepaymentPlan_ZHXT fileTmp, String
            privateKey, String groupNameForModifyPlan);

//    Boolean status_in_cache(String businessId);

    String getPrivateKey();

    NotifyApplication buildSPDBankNotifyApplication(FileRepository file, FileTmp_ModifyRepaymentPlan fileTmp,
                                                    String privateKey, String groupNameForModifyPlan);

    void scan_signal_file(String scanPath, String tradeDate);

//    void processBatchFile(ProductCategory productCategory, String businessType, FileRepository fileRepository, BatchProcessHandler batchProcessHandler);

    NotifyApplication buildSPDBankOverDueFeeNotifyApplication(FileRepository file, FileTmp_OverdueFee fileTmp,
                                                              String privateKey, String groupNameForModifyPlan);

    /**
     * hjl
     * 2017年8月10日
     *
     * @param groupNameForModifyPlan TODO
     */
    NotifyApplication buildRepaymentOrderFileSubNotifyApplication(String businessType, FileRepository file, String bizId,
                                                                  FileSubRepaymentOrder fileTmp, String privateKey, String groupNameForModifyPlan);

    void sendToNotifyServer(String fileRepositoryUuid, List<String> continueNoList) throws MorganStanleyException;

    void sendFileContentToNotifyServer(FileRepository fileRepository, BatchProcessHandler batchProcessHandler) throws MorganStanleyException;

    void update_fileRepository_abandon(FileRepository fileRepository) throws MorganStanleyException;

    void update_fileRepository_processed(FileRepository fileRepository, Integer tradeSize) throws MorganStanleyException;

    void update_fileRepository_send(FileRepository fileRepository, List<String> bizIdList) throws MorganStanleyException;

    void update_fileRepository_executed(FileRepository fileRepository, Integer processSize) throws MorganStanleyException;

    void queryNotifyServerResult(String fileRepositoryUuid) throws MorganStanleyException;
}
