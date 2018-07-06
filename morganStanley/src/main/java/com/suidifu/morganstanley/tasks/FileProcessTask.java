package com.suidifu.morganstanley.tasks;

import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorCode4File.ERROR_CODE_WAITING_BATCH;

import com.google.common.base.Preconditions;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.perInterface.ProductLv1Code;
import com.zufangbao.sun.entity.perInterface.ProductLv2Code;
import com.zufangbao.sun.entity.sftp.SftpInfoModel;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.csv.CsvUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_AssetSetDetail_EXCEL;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_ModifyRepaymentPlan_CSV;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_ModifyRepaymentPlan_ZHXT;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_MutableFee_CSV;
import com.zufangbao.sun.yunxin.entity.files.FileType;
import com.zufangbao.sun.yunxin.handler.CustomerHandler;
import com.zufangbao.sun.yunxin.handler.SftpHandler;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.AbsFileHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;


/**
 * 文件处理Task
 *
 * @author louguanyang on 2017/5/22.
 */
@Component("fileProcessTask")
public class FileProcessTask {
    private static final Log LOGGER = LogFactory.getLog(FileProcessTask.class);
    @Resource
    private FileRepositoryService fileRepositoryService;
    @Resource
    private ProductCategoryCacheHandler productCategoryCacheHandler;
    @Resource
    private FileProcessHandler fileProcessHandler;
    @Resource
    private MorganStanleyNotifyServer morganStanleyNotifyServer;
    @Resource
    private CustomerHandler customerHandler;
    @Resource
    private SftpHandler sftpHandler;
    @Resource
    private AbsFileHandler absFileHandler;

    /**
     * 处理佰仟变更还款计划文件
     */
    public void processBQJRModifyRepaymentFile() {
        LOGGER.info("process_BQJR_modify_repayment_file");
        for (ProductLv1Code code : ProductLv1Code.values()) {
            if (ProductLv1Code.HA0100.getCode().equals(code.getCode()) || ProductLv1Code.HG4100.getCode().equals(code.getCode()) || ProductLv1Code.I05600.getCode().equals(code.getCode())) {
                LOGGER.info("ProductLv1Code code : " + ProductLv1Code.HA0100.getCode());
                Product3lvl product3lvl = new Product3lvl(code.getCode(), ProductLv2Code.UPLOAD.getCode(), FileType.MODIFY_REPAYMENT.getTypeCode());

                ProductCategory category = productCategoryCacheHandler.get(product3lvl);
                if (null == category) {
                    LOGGER.warn("process_BQJR_modify_repayment_file, ProductCategory not config");
                    continue;
                }
                LOGGER.info("ProductLv1Code code : " + category.getProductLv1Code());

                String productCode = category.getProductLv1Code();
                String businessType = category.transfer().shortName();

                List<FileRepository> file_list = fileRepositoryService.get_file_by_type_code(productCode, FileType.MODIFY_REPAYMENT.getTypeCode());
                if (CollectionUtils.isEmpty(file_list)) {
                    continue;
                }
                LOGGER.info("process_BQJR_modify_repayment_file, 共计[" + file_list.size() + "]个变更还款计划批量文件需要处理!");
                for (FileRepository file : file_list) {
                    try {
                        process_BQJR_modify_file(businessType, file);
                    } catch (MorganStanleyException e) {
                        e.printStackTrace();
                        LOGGER.error("process_BQJR_modify_repayment_file occur error, error message:" + e.getMessage());
                    }
                }
            }
        }
    }

    private void process_BQJR_modify_file(String businessType, FileRepository file) throws MorganStanleyException {
        try {
            if (file == null) {
                return;
            }
            List<FileTmp_ModifyRepaymentPlan_CSV> planList = CsvUtils.readFromPath(file.getPath(), FileTmp_ModifyRepaymentPlan_CSV.class);
            List<String> tradeNoList = new ArrayList<>();
            for (FileTmp_ModifyRepaymentPlan_CSV plan : planList) {
                String tradeNo = plan.getTradeNo();
                if (StringUtils.isNotEmpty(tradeNo) && !tradeNoList.contains(tradeNo)) {
                    tradeNoList.add(tradeNo);
                }
            }
            if (CollectionUtils.isEmpty(tradeNoList)) { // CSV 文件为空 时， FileRepository 状态更新为 ABANDON
                LOGGER.info("process_BQJR_modify_file, fileUuid:" + file.getUuid() + ", plan list is empty!");
                fileProcessHandler.update_fileRepository_abandon(file);
                return;
            }
            // 根据 tradeNo 分组 CSV 数据
            ConcurrentMap<String, List<FileTmp_ModifyRepaymentPlan_CSV>> collect = planList.stream().collect(Collectors.groupingByConcurrent
                    (FileTmp_ModifyRepaymentPlan_CSV::getTradeNo));
            if (file.is_CREATE()) {
                // update to process
                fileProcessHandler.update_fileRepository_processed(file, tradeNoList.size());
            }
            if (file.is_PROCESS()) {
                List<String> bizIdList = new ArrayList<>();
                String privateKey = fileProcessHandler.getPrivateKey();
                String groupName = getGroupName();
                for (String tradeNo : tradeNoList) {
                    List<FileTmp_ModifyRepaymentPlan_CSV> sameTradeNoList = collect.getOrDefault(tradeNo, Collections.emptyList());
                    if (CollectionUtils.isEmpty(sameTradeNoList)) {
                        continue;
                    }
                    String bizId = file.buildBizId(tradeNo);
                    NotifyApplication notifyApplication = fileProcessHandler.build_BQJR_modify_repaymentPlan_notifyApplication(businessType, file, bizId,
                            sameTradeNoList, privateKey, groupName);
                    if (notifyApplication != null) {
                        LOGGER.info("processBQJRModifyFile, fileUuid【" + file.getUuid() + "】处理中, 开始push job 到 notifyServer,bizId:" + bizId + "," +
                                "consistenceHashPolicy:" + notifyApplication.getConsistenceHashPolicy() + ".");
                        morganStanleyNotifyServer.pushJob(notifyApplication);
                        bizIdList.add(bizId);
                    }
                }
                if (CollectionUtils.isNotEmpty(bizIdList)) {
                    LOGGER.info("processBQJRModifyFile, 全部发送到 NotifyServer, uuid:[" + file.getUuid() + "]");
                    fileProcessHandler.update_fileRepository_send(file, bizIdList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("processBQJRModifyFile，uuid:[" + file.getUuid() + "], 处理异常, error message:[" + e.getMessage() + "]");
            fileProcessHandler.update_fileRepository_abandon(file);
        }
    }

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    private String getGroupName() {
        return morganStanleyNotifyConfig.getFileNotifyGroupName();
    }

    /**
     * 处理费用浮动文件
     */
    public void processBQJRMutableFile() {
        LOGGER.info("process_BQJR_mutable_file");
        for (ProductLv1Code code : ProductLv1Code.values()) {
            if (code.getCode().equals(ProductLv1Code.HA0100.getCode()) || code.getCode().equals(ProductLv1Code.HG4100.getCode()) || code.getCode().equals(ProductLv1Code.I05600.getCode())) {
                Product3lvl product3lvl = new Product3lvl(code.getCode(), ProductLv2Code.UPLOAD.getCode(), FileType.MUTABLE_FEE.getTypeCode());
                ProductCategory category = productCategoryCacheHandler.get(product3lvl);
                if (null == category) {
                    LOGGER.warn("process_BQJR_mutable_file, ProductCategory not config");
                    continue;
                }
                String productCode = category.getProductLv1Code();
                String businessType = category.transfer().shortName();
                List<FileRepository> file_list = fileRepositoryService.get_file_by_type_code(productCode, FileType.MUTABLE_FEE.getTypeCode());
                if (CollectionUtils.isEmpty(file_list)) {
                    continue;
                }
                LOGGER.info("process_BQJR_mutable_file, 共计[" + file_list.size() + "]个浮动费用批量文件需要处理!");
                for (FileRepository file : file_list) {
                    try {
                        process_single_mutable_file(businessType, file);
                    } catch (MorganStanleyException e) {
                        e.printStackTrace();
                        LOGGER.error("process_BQJR_mutable_file occur error, error message:" + e.getMessage());
                    }
                }
            }
        }
    }

    private void process_single_mutable_file(String businessType, FileRepository file) throws MorganStanleyException {
        try {
            if (file == null) {
                return;
            }
            List<FileTmp_MutableFee_CSV> planList = CsvUtils.readFromPath(file.getPath(), FileTmp_MutableFee_CSV.class);
            List<String> tradeNoList = new ArrayList<>();
            for (FileTmp_MutableFee_CSV plan : planList) {
                String tradeNo = plan.getTradeNo();
                if (StringUtils.isNotEmpty(tradeNo) && !tradeNoList.contains(tradeNo)) {
                    tradeNoList.add(tradeNo);
                }
            }
            if (CollectionUtils.isEmpty(tradeNoList)) {
                // CSV 文件为空 时， FileRepository 状态更新为 ABANDON
                LOGGER.info("processBQJRMutableFile, fileUuid:" + file.getUuid() + ", plan list is empty!");
                fileProcessHandler.update_fileRepository_abandon(file);
                return;
            }
            // 根据 tradeNo 分组 CSV 数据
            ConcurrentMap<String, List<FileTmp_MutableFee_CSV>> collect = planList.stream().collect(Collectors.groupingByConcurrent
                    (FileTmp_MutableFee_CSV::getTradeNo));
            if (file.is_CREATE()) {
                // update to process
                fileProcessHandler.update_fileRepository_processed(file, tradeNoList.size());
            }
            if (file.is_PROCESS()) {
                List<String> bizIdList = new ArrayList<>();
                String privateKey = fileProcessHandler.getPrivateKey();
                String groupName = getGroupName();
                for (String tradeNo : tradeNoList) {
                    List<FileTmp_MutableFee_CSV> sameTradeNoList = collect.getOrDefault(tradeNo, Collections.emptyList());
                    if (CollectionUtils.isEmpty(sameTradeNoList)) {
                        continue;
                    }
                    String bizId = file.buildBizId(tradeNo);
                    NotifyApplication notifyApplication = fileProcessHandler.build_mutable_NotifyApplication(businessType, file, bizId, sameTradeNoList,
                            privateKey, groupName);
                    if (notifyApplication != null) {
                        LOGGER.info("processBQJRMutableFile, fileUuid【" + file.getUuid() + "】处理中, 开始push job 到 notifyServer, bizId:" + bizId + "," +
                                "consistenceHashPolicy:" + notifyApplication.getConsistenceHashPolicy() + ".");
                        morganStanleyNotifyServer.pushJob(notifyApplication);
                        bizIdList.add(bizId);
                    }
                }
                if (CollectionUtils.isNotEmpty(bizIdList)) {
                    LOGGER.info("processBQJRMutableFile, 全部发送到 NotifyServer, uuid:[" + file.getUuid() + "]");
                    fileProcessHandler.update_fileRepository_send(file, bizIdList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("processBQJRMutableFile，uuid:[" + file.getUuid() + "], 处理异常, error message:[" + e.getMessage() + "]");
            fileProcessHandler.update_fileRepository_abandon(file);
        }
    }

    /**
     * 处理中航信托变更还款计划文件
     */
    public void processZHXTModifyRepaymentFile() {
        Product3lvl product3lvl = new Product3lvl(ProductLv1Code.ZHONG_HANG.getCode(), ProductLv2Code.UPLOAD.getCode(), FileType.MODIFY_REPAYMENT.getTypeCode());
        ProductCategory category = null;
        try {
            category = productCategoryCacheHandler.get(product3lvl);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("processZHXTModifyRepaymentFile, occur error, error message:" + e.getMessage());
        }
        if (null == category) {
            LOGGER.warn("processZHXTModifyRepaymentFile, ProductCategory not config");
            return;
        }
        String productCode = category.getProductLv1Code();
        String businessType = category.transfer().shortName();

        List<FileRepository> fileList = fileRepositoryService.get_file_by_type_code(productCode, FileType.MODIFY_REPAYMENT.getTypeCode());
        if (CollectionUtils.isEmpty(fileList)) {
            return;
        }
        LOGGER.info("processZHXTModifyRepaymentFile, 共计[" + fileList.size() + "]个变更还款计划批量文件需要处理!");
        for (FileRepository file : fileList) {
            try {
                processZHXTModifyFile(businessType, file);
            } catch (MorganStanleyException e) {
                e.printStackTrace();
                LOGGER.error("processZHXTModifyFile, occur error, error message:" + e.getMessage());
            }
        }
    }

    /**
     * 处理中航信托单个变更还款计划文件
     *
     * @param businessType   业务类型
     * @param fileRepository 文件仓库
     * @throws MorganStanleyException 异常
     */
    private void processZHXTModifyFile(String businessType, FileRepository fileRepository) throws MorganStanleyException {
        try {
            if (fileRepository == null) {
                return;
            }
            List<FileTmp_ModifyRepaymentPlan_ZHXT> planList = FileUtils.readJsonList(fileRepository.getPath(), FileTmp_ModifyRepaymentPlan_ZHXT.class);
            if (CollectionUtils.isEmpty(planList)) {
                //文件为空 时， FileRepository 状态更新为 ABANDON
                LOGGER.info("processZHXTModifyFile, fileUuid:" + fileRepository.getUuid() + ", plan list is empty!");
                fileProcessHandler.update_fileRepository_abandon(fileRepository);
                return;
            }
            if (fileRepository.is_CREATE()) {
                // update to process
                fileProcessHandler.update_fileRepository_processed(fileRepository, planList.size());
            }
            if (fileRepository.is_PROCESS()) {
                List<String> bizIdList = new ArrayList<>();
                String privateKey = fileProcessHandler.getPrivateKey();
                String groupName = getGroupName();
                // 交易时间
                Date tradeTime = fileRepository.getUploadTime();
                for (FileTmp_ModifyRepaymentPlan_ZHXT plan : planList) {
                    if (isNoNeedProcess(plan, tradeTime, fileRepository.getUuid(), plan.getTradeNo())) {
                        continue;
                    }
                    String bizId = fileRepository.buildBizId(plan.getTradeNo());
                    NotifyApplication notifyApplication = fileProcessHandler.build_ZHXT_notifyApplication(businessType, fileRepository, bizId, plan, privateKey, groupName);
                    if (notifyApplication != null) {
                        LOGGER.info("processZHXTModifyFile, fileUuid【" + fileRepository.getUuid() + "】处理中, 开始push job 到 notifyServer, bizId:" + bizId +
                                ",consistenceHashPolicy:(" + notifyApplication.getConsistenceHashPolicy() + ").");
                        morganStanleyNotifyServer.pushJob(notifyApplication);
                        bizIdList.add(bizId);
                    }
                }
                if (CollectionUtils.isNotEmpty(bizIdList)) {
                    LOGGER.info("processZHXTModifyFile, 全部发送到 NotifyServer, uuid:[" + fileRepository.getUuid() + "]");
                    fileProcessHandler.update_fileRepository_send(fileRepository, bizIdList);
                } else {
                    LOGGER.info("processZHXTModifyFile, fileUuid:" + fileRepository.getUuid() + ", bizIdList is empty, update to executed!");
                    fileProcessHandler.update_fileRepository_executed(fileRepository, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("processZHXTModifyFile, uuid:[" + fileRepository.getUuid() + "], 处理异常, error " + "message:[" + e.getMessage() + "]");
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
        }
    }

    private boolean isNoNeedProcess(FileTmp_ModifyRepaymentPlan_ZHXT plan, Date tradeTime, String fileRepositoryUuid, String tradeNo) {
        if (plan.getTradeTimeDate() == null) {
            LOGGER.warn("#fileUuid【" + fileRepositoryUuid + "】,交易时间格式错误, tradeNo:" + tradeNo);
            return true;
        }
        if (!DateUtils.isSameHour(tradeTime, plan.getTradeTimeDate())) {
            LOGGER.warn("#fileUuid【" + fileRepositoryUuid + "】,【历史数据，不做处理】, tradeNo:" + tradeNo);
            return true;
        }
        return false;
    }

    /**
     * 处理百度资产包附加信息文件
     */
    @Deprecated
    public void processBaiDuFile_src() {
        List<FileRepository> fileList = fileRepositoryService.get_file_by_type_code(ProductLv1Code.I02900.getCode(), FileType.ASSETSET_DETAIL_BAIDU.getTypeCode());
        if (CollectionUtils.isEmpty(fileList)) {
            LOGGER.warn("processBaiDuFile, fileRepository has null data, ProductLv1Code [I02900]");
            return;
        }
        LOGGER.info("processBaiDuFile, 共计[" + fileList.size() + "]个百度资产包附加信息文件需要处理!");
        for (FileRepository file : fileList) {
            try {
                processBaiDuDetailFile(file);
            } catch (MorganStanleyException e) {
                e.printStackTrace();
                LOGGER.error("processBaiDuFile, occur error, error message:" + e.getMessage());
            }
        }
    }

    /**
     * 处理百度资产包单个附加信息文件
     *
     * @param fileRepository
     * @throws MorganStanleyException
     */
    private void processBaiDuDetailFile(FileRepository fileRepository) throws MorganStanleyException {
        try {
            if (null == fileRepository) {
                LOGGER.warn("fileRepository is null");
                return;
            }

            File file = new File(fileRepository.getPath());
            FileInputStream input = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(input);
            List<FileTmp_AssetSetDetail_EXCEL> fileDataList = new ExcelUtil<>(FileTmp_AssetSetDetail_EXCEL.class).importExcelHighVersion(0, workbook);

            if (CollectionUtils.isEmpty(fileDataList)) {
                //文件为空时， FileRepository 状态更新为 ABANDON
                LOGGER.info("processBaiDuDetailFile, fileUuid:" + fileRepository.getUuid() + ", fileDataList is empty!");
                fileProcessHandler.update_fileRepository_abandon(fileRepository);
                return;
            }
            if (fileRepository.is_CREATE()) {
                for (FileTmp_AssetSetDetail_EXCEL assetSetDetail : fileDataList) {
                    try {
                        //发生异常时,记录对应的fileRepository的uuid方便排查,同时状态置为ABANDON,文件对应的其他数据继续执行
                        customerHandler.fillCustomerData(assetSetDetail.getUniqueId(), assetSetDetail.getMobile(), assetSetDetail.extractMaritalStatusIntValue());
                    } catch (ApiException apiException) {
                        LOGGER.error("processBaiDuDetailFile has apiException, fileUuid: [" + fileRepository.getUuid() + "], uniqueId is: [" + assetSetDetail.getUniqueId() + "], message is: " + apiException.getMessage());
                        //置为 ABANDON--异常终止(提示有相应数据处理失败了)
                        fileProcessHandler.update_fileRepository_abandon(fileRepository);
                    }
                }
                LOGGER.info("processBaiDuDetailFile, fileUuid:" + fileRepository.getUuid() + ", update to DONE!");
                fileProcessHandler.update_fileRepository_executed(fileRepository, fileDataList.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("processBaiDuDetailFile has Exception, uuid:[" + fileRepository.getUuid() + "], 处理异常, error " + "message:[" + e.getMessage() + "]");
            fileProcessHandler.update_fileRepository_abandon(fileRepository);
        }
    }

    @Resource
    private YntrustFileTask yntrustFileTask;

    /**
     * 扫描文件夹，保存路径到文件仓库
     */
    public void scanSftpFile() {
        try {
            String scanPath = yntrustFileTask.getScanPath();
            if (StringUtils.isEmpty(scanPath)) {
                LOGGER.error("文件扫描路径未配置, 请检查配置文件项[yntrust.file-task.scan-path].");
                return;
            }
            String tradeDate = DateUtils.today();
            fileProcessHandler.scan_signal_file(scanPath, tradeDate);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("scanSftpFile occur error, error message:" + e.getMessage());
        }
    }

    /**
     * 批量文件发送服务
     */
    public void fileSendServers() {
        try {
            List<String> fileRepositoryList = fileRepositoryService.get_all_need_send_file_uuid_list();
            List<String> continueNoList = new ArrayList<>();
            for (String fileRepositoryUuid : fileRepositoryList) {
                try {
                    LOGGER.info("开始处理批量文件, 文件仓库uuid:" + fileRepositoryUuid);
                    fileProcessHandler.sendToNotifyServer(fileRepositoryUuid, continueNoList);
                } catch (MorganStanleyException e) {
                    LOGGER.error("sendToNotifyServer occur error, MorganStanleyException , uuid: " + fileRepositoryUuid + ", error message:" + e.getMessage());
                    if (e.getCode() == ERROR_CODE_WAITING_BATCH) {
                        String merIdBatchNo = e.getMessage();
                        if (!continueNoList.contains(merIdBatchNo)) {
                            continueNoList.add(merIdBatchNo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("sendToNotifyServer occur error, fileRepositoryUuid: " + fileRepositoryUuid + ", error message:" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("fileSendServers occur error, error message:" + e.getMessage());
        }
    }

    /**
     * 查询文件处理结果
     */
    public void querySendServersResult() {
        try {
            List<String> fileRepositoryUuidList = fileRepositoryService.get_all_send_file_uuid_list();
            for (String fileRepositoryUuid : fileRepositoryUuidList) {
                LOGGER.info("开始查询批量文件处理结果, 文件仓库uuid:" + fileRepositoryUuid);
                try {
                    fileProcessHandler.queryNotifyServerResult(fileRepositoryUuid);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("queryNotifyServerResult occur error, fileRepositoryUuid: " + fileRepositoryUuid + ", error message:" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("querySendServersResult occur error, error message:" + e.getMessage());
        }
    }

    /**
     * 获取百度资产包附加信息文件
     */
    public void processBaiDuFile() {
        List<String> fileProcessPaths = acquireBDFileProcessPath();
        for (String fileProcessPath : fileProcessPaths) {
            Preconditions.checkArgument(StringUtils.isNotEmpty(fileProcessPath), "processBaiDuDetailFile occur error , have not valid file !");
            File file = new File(fileProcessPath);
            Workbook workbook = null;
            try {
                FileInputStream input = new FileInputStream(file);
                workbook = WorkbookFactory.create(input);
            } catch (InvalidFormatException | IOException e) {
                LOGGER.error("processBaiDuDetailFile has Exception, fileProcessPath: [" + fileProcessPath + "], 处理异常, error message:[" + e.getMessage() + "]");
                e.printStackTrace();
                continue;
            }
            List<FileTmp_AssetSetDetail_EXCEL> fileDataList = new ExcelUtil<>(FileTmp_AssetSetDetail_EXCEL.class).importExcelHighVersion(0, workbook);

            if (CollectionUtils.isEmpty(fileDataList)) {
                LOGGER.error("acquireBaiDuDetailFile occur error, fileDataList is empty!");
                return;
            }
            for (FileTmp_AssetSetDetail_EXCEL assetSetDetail : fileDataList) {
                try {
                    customerHandler.fillCustomerData(assetSetDetail.getUniqueId(), assetSetDetail.getMobile(), assetSetDetail.extractMaritalStatusIntValue());
                } catch (ApiException apiException) {
                    LOGGER.error("processBaiDuDetailFile has apiException, fileProcessPath: [" + fileProcessPath + "], uniqueId is: [" + assetSetDetail.getUniqueId() + "],error message is: " + apiException.getMessage());
                }
            }
        }

    }

    private List<String> acquireBDFileProcessPath() {
        List<SftpInfoModel> sftpInfoModels = sftpHandler.getSftpConfigsByFileType(SftpInfoModel.ASSET_SET_ADDITIONAL_INFO);
        List<String> result = new ArrayList<>();
        for (SftpInfoModel model : sftpInfoModels) {
            ChannelSftp chSftp = sftpHandler.getChannelSftpBySftpInfoModel(model);
            if (null == chSftp) {
                LOGGER.error("processBaiDuDetailFile acquire file failed, sftp cannot connect");
                return result;
            }
            String today = DateUtils.format(DateUtils.addDays(new Date(), -1), DateUtils.DATE_FORMAT_YYYYMMDD);
            String fileName = model.getPerfix() + "-" + today + FilenameUtils.EXTENSION_SEPARATOR;
            // 目标文件名
            String src = model.getSrcPath() + fileName;
            // 本地文件名
            String dst = model.getDstPath() + fileName;
            for (String suffix : model.getFormats()) {
                try {
                    chSftp.get(src + suffix, dst + suffix);
                    result.add(dst + suffix);
                } catch (SftpException e) {
                    LOGGER.warn("processBaiDuDetailFile acquire file failed, file:[" + fileName + suffix + "] is not exist");
                }
            }
        }
        sftpHandler.closeSFTPChannel();
        return result;
    }

    /**
     * 同步腾讯ABS文件到FTP
     */
    public void syncTencentAbsFileToFtp() {
        List<SftpInfoModel> sftpInfoModels = sftpHandler.getSftpConfigsByFileType(SftpInfoModel.TENCENT_ABS_FILE);
        for (SftpInfoModel model : sftpInfoModels) {
            ChannelSftp chSftp = sftpHandler.getChannelSftpBySftpInfoModel(model);
            if (null == chSftp) {
                LOGGER.error("syncTencentAbsFileToFtp failed, sftp cannot connect");
            }
            String[] fileNames = getTencentAbsFileNames();
            for (String fileName : fileNames) {
                // 目标文件名
                String src = model.getSrcPath() + fileName;
                // 本地文件名
                String dst = model.getDstPath() + fileName;
                File file = new File(dst);
                if (!file.exists()) {
                    makeBlankFile(file);
                }
                try {
                    chSftp.put(src, dst);
                } catch (SftpException e) {
                    LOGGER.warn("syncTencentAbsFileToFtp failed, file:[" + fileName + "] is not exist");
                }
            }
        }
        sftpHandler.closeSFTPChannel();
    }

    private void makeBlankFile(File file) {
        try {
            FileUtils.writeStringToFile(file, StringUtils.EMPTY, FilenameUtils.UTF_8, Boolean.FALSE);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("makeBlankFile failed, file:[" + file.getName() + "] can not build");
        }
    }

    private String[] getTencentAbsFileNames() {
        Date sysDate = new Date();
        Date fileNameDate = DateUtils.addDays(sysDate, -1);
        String date = DateUtils.format(fileNameDate, DateUtils.DATE_FORMAT_YYYYMMDD);
        return new String[]{absFileHandler.getFileName(AbsFileHandler.FILE_TYPE_FUNDS, date)};
    }
}
