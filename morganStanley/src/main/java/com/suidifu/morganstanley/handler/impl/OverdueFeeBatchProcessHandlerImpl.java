package com.suidifu.morganstanley.handler.impl;

import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_OverdueFee;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.zufangbao.sun.yunxin.entity.files.FileProcessStatus.UNPROCESSED;

/**
 * Created by hwr on 17-8-8.
 */
@Component("OverdueFeeBatchProcessHandlerImpl")
public class OverdueFeeBatchProcessHandlerImpl extends BaseBatchProcessHandlerImpl {

    @Autowired
    private FileProcessHandler fileProcessHandler;

    @Autowired
    private MorganStanleyNotifyServer morganStanleyNotifyServer;

    @Autowired
    private TMerConfigService tMerConfigService;

    @Override
    public void verifySignUpdateProcessStatus(FileRepository fileRepository) throws MorganStanleyException {
        if (fileRepository == null) {
            throw new MorganStanleyException("fileRepository is null.");
        }
        if (!UNPROCESSED.getCode().equals(fileRepository.getProcessStatus())) {
            throw new MorganStanleyException("fileRepository 非(待处理)的, 跳过验签.");
        }
        boolean verifySign = verifySign(fileRepository, tMerConfigService);
        if (!verifySign) {
            //验签失败, 处理状态: 已处理, 发送状态: 异常中止, 执行状态: 未执行
            LOGGER.error("OverdueFeeBatchProcessHandlerImpl 文件验签失败, FileRepository uuid:" + fileRepository.getUuid());
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
            return;
        }
        List<FileTmp_OverdueFee> planList = FileUtils.readJsonList(fileRepository.getPath(), FileTmp_OverdueFee.class);
        if (CollectionUtils.isEmpty(planList)) {
            //文件为空 时， FileRepository 状态更新为 ABANDON
            LOGGER.error("OverdueFeeBatchProcessHandlerImpl 业务文件读取失败, FileRepository uuid:" + fileRepository.getUuid());
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
            return;
        }
        fileProcessHandler.update_fileRepository_processed(fileRepository, planList.size());
    }

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    @Override
    public void sendToNotifyServer(FileRepository fileRepository) throws MorganStanleyException {
        List<String> bizIdList = new ArrayList<>();
        try {
            noNeedSend(fileRepository);
            List<FileTmp_OverdueFee> planList = FileUtils.readJsonList(fileRepository.getPath(), FileTmp_OverdueFee.class);
            String privateKey = fileProcessHandler.getPrivateKey();
            String groupName = morganStanleyNotifyConfig.getFileNotifyGroupName();
            for (FileTmp_OverdueFee plan : planList) {
                String bizId = fileRepository.buildBizId(plan.getTradeNo());
                NotifyApplication notifyApplication = fileProcessHandler.buildSPDBankOverDueFeeNotifyApplication(fileRepository, plan, privateKey, groupName);
                if (notifyApplication != null) {
                    LOGGER.info("OverdueFeeBatchProcessHandlerImpl#fileUuid:" + fileRepository.getUuid() + " 处理中, 开始push job 到 notifyServer, bizId:" + bizId
                            + ", consistenceHashPolicy:" + notifyApplication.getConsistenceHashPolicy());
                    morganStanleyNotifyServer.pushJob(notifyApplication);
                    bizIdList.add(bizId);
                }
            }
            if (CollectionUtils.isEmpty(bizIdList)) {
                LOGGER.error("OverdueFeeBatchProcessHandlerImpl#sendToNotifyServer fail, bizIdList is Empty.");
                fileProcessHandler.update_fileRepository_abandon(fileRepository);
                return;
            }
            fileProcessHandler.update_fileRepository_send(fileRepository, bizIdList);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("OverdueFeeBatchProcessHandlerImpl#sendToNotifyServer fail, occur error, error message:" + e.getMessage());
            LOGGER.error("OverdueFeeBatchProcessHandlerImpl#sendToNotifyServer fail,fileRepository uuid: " + fileRepository.getUuid() + ", 已处理的 bizIdList:" +
                    JsonUtils.toJSONString(bizIdList));
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
        }
    }
}
