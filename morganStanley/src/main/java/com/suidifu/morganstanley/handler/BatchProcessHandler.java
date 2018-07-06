package com.suidifu.morganstanley.handler;

import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileSignal;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

public interface BatchProcessHandler {
    Log LOGGER = LogFactory.getLog(BatchProcessHandler.class);

    default boolean verifySign(FileRepository fileRepository, TMerConfigService tMerConfigService) {
        try {
            String bizFilePath = fileRepository.getPath();
            String signalFilePath = bizFilePath + FilenameUtils.SIGNAL_FILE_SPLIT;
            File bizFile = new File(bizFilePath);
            FileSignal fileSignal = FileUtils.readJsonObject(signalFilePath, FileSignal.class);
            if (fileSignal == null) {
                LOGGER.error("process_file_first_time occur error, 信号文件读取失败.");
                return false;
            }

            String content = FileUtils.readFileToString(bizFile, FilenameUtils.UTF_8);

            TMerConfig merConfig = tMerConfigService.getTMerConfig(fileSignal.getMerId(), fileSignal.getSecret());
            String sign = fileSignal.getSign();
            String publicKey = merConfig.getPubKey();

            boolean verifyResult = ApiSignUtils.rsaCheckContent(content, sign, publicKey);
            if (!verifyResult) {
                LOGGER.error("process_file_first_time occur error, 验签失败.");
            }
            return verifyResult;
        } catch (Exception e) {
            LOGGER.error("verify sign occur error, error msg:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    void verifySignUpdateProcessStatus(FileRepository fileRepository) throws MorganStanleyException;

    void sendToNotifyServer(FileRepository fileRepository) throws MorganStanleyException;
}
