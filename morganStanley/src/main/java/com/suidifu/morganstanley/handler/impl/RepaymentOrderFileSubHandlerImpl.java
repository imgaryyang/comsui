/**
 *
 */
package com.suidifu.morganstanley.handler.impl;

import static com.zufangbao.sun.yunxin.entity.files.FileProcessStatus.UNPROCESSED;

import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.BatchProcessHandler;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileSubRepaymentOrder;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hjl
 */
@Component("repaymentOrderFileSubHandlerImpl")
public class RepaymentOrderFileSubHandlerImpl extends ModifyRepaymentPlanBatchProcessHandlerImpl implements BatchProcessHandler {
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
            LOGGER.error("RepaymentOrderFileSubHandlerImpl verifySign fail, fileRepository 非(待处理)的, 跳过验签.");
            return;
        }
        boolean verifySign = verifySign(fileRepository, tMerConfigService);
        if (!verifySign) {
            //验签失败, 处理状态: 已处理, 发送状态: 异常中止, 执行状态: 未执行
            LOGGER.error("RepaymentOrderFileSubHandlerImpl 文件验签失败, FileRepository uuid:" + fileRepository.getUuid());
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
            return;
        }

        List<FileSubRepaymentOrder> planList = FileUtils.readJsonList(fileRepository.getPath(), FileSubRepaymentOrder.class);
        if (CollectionUtils.isEmpty(planList)) {
            //文件为空 时， FileRepository 状态更新为 ABANDON
            LOGGER.error("RepaymentOrderFileSubHandlerImpl 业务文件读取失败, FileRepository uuid:" + fileRepository.getUuid());
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
            List<FileSubRepaymentOrder> planList = FileUtils.readJsonList(fileRepository.getPath(), FileSubRepaymentOrder.class);
            String privateKey = fileProcessHandler.getPrivateKey();
            String groupName = morganStanleyNotifyConfig.getFileNotifyGroupName();
            String productCode = fileRepository.getProductCode();
            for (FileSubRepaymentOrder plan : planList) {
                //浦发业务文件里信托产品代码
                if (StringUtils.isEmpty(productCode)) {
                    productCode = plan.getFinancialProductCode();
                }
                String businessType = productCode + FilenameUtils.LOG_SPLIT + fileRepository.getFileTypeCode();
                String bizId = productCode + FilenameUtils.LOG_SPLIT + fileRepository.getUuid() + FilenameUtils.LOG_SPLIT + plan.getOrderRequestNo() +
                        FilenameUtils.LOG_SPLIT + FilenameUtils.LOG_SPLIT + plan.getOrderUniqueId();
                NotifyApplication notifyApplication = fileProcessHandler.buildRepaymentOrderFileSubNotifyApplication(businessType, fileRepository, bizId, plan,
                        privateKey, groupName);
                if (notifyApplication != null) {
                    LOGGER.info("bizId:" + bizId + ",consistenceHashPolicy:" + notifyApplication.getConsistenceHashPolicy() + ".");
                    morganStanleyNotifyServer.pushJob(notifyApplication);
                    bizIdList.add(bizId);
                }
            }

            if (CollectionUtils.isEmpty(bizIdList)) {
                LOGGER.error("sendToNotifyServer fail, bizIdList is Empty.");
                fileProcessHandler.update_fileRepository_abandon(fileRepository);
                return;
            }
            fileProcessHandler.update_fileRepository_send(fileRepository, bizIdList);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ModifyRepaymentPlanBatchProcessHandlerImpl#sendToNotifyServer fail, occur error, error message:" + e.getMessage());
            LOGGER.error("ModifyRepaymentPlanBatchProcessHandlerImpl#sendToNotifyServer fail,fileRepository uuid: " + fileRepository.getUuid() + ", 已处理的 bizIdList:"
                    + JsonUtils.toJSONString(bizIdList));
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
        }
    }
}
