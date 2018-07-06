package com.suidifu.morganstanley.tasks;

import com.demo2do.core.web.resolver.Page;
import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.controller.task.TaskSpec;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageJob;
import com.zufangbao.sun.service.ImportAssetPackageJobService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageJobHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

import static com.zufangbao.sun.entity.repayment.job.ImportAssetDataRedisSpec.KEY_MODIFY_TIME;
import static com.zufangbao.sun.entity.repayment.job.ImportAssetJobExecuteStatus.EXECUTING;
import static com.zufangbao.sun.entity.repayment.job.ImportAssetJobExecuteStatus.UNEXECUTE;
import static com.zufangbao.sun.entity.repayment.job.ImportAssetJobExecuteStatus.WAITING_CALLBACK;

@Log4j2
@Component("fillAsyncImportAssetPackageTask")
public class FillAsyncImportAssetPackageTask extends BasicTask {
    @Resource
    private ImportAssetPackageJobHandler importAssetPackageJobHandler;

    @Resource
    private MorganStanleyNotifyServer morganStanleyNotifyServer;

    @Resource
    private ImportAssetPackageJobService importAssetPackageJobService;

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    private static final String REDIS_ID = "as_i:fillJobStartIds";
    private static final String EXE_START_ID = "exeStartId";
    private static final String UNDO_START_ID = "undoStartId";

    @Override
    public void run() {
        long sleepMillis = getLongFromWorkParamOrDefault(TaskSpec.PARAM_SLEEP_MILLIS,
                TaskSpec.DEFAULT_SLEEP_MILLIS);
        int limit = getIntegetFromWorkParamOrDefault(TaskSpec.PARAM_LIMIT_SIZE,
                TaskSpec.DEFAULT_LIMIT_SIZE);
        try {
            Long exeStartId = 0L;
            Long undoStartId = 0L;
            Map<String, Object> map = getDataFromRedis(REDIS_ID);
            if (null != map) {
                undoStartId = Long.valueOf((String) map.getOrDefault(UNDO_START_ID, "0"));
                exeStartId = Long.valueOf((String) map.getOrDefault(EXE_START_ID, "0"));
            } else {
                map = new HashMap<>();
            }
            while (true) {
                FillAsyncImportModel model = getExecutingRedisIdForFill(exeStartId, limit);
                List<String> keyList = model.getKeyList();
                exeStartId = model.getNewStartId();
                model = getUndoRedisIdForFill(undoStartId, limit);
                keyList.addAll(model.getKeyList());
                undoStartId = model.getNewStartId();
                map.put(UNDO_START_ID, undoStartId + "");
                map.put(EXE_START_ID, exeStartId + "");
                saveOrUpdateRedis(map, REDIS_ID);
                if (CollectionUtils.isEmpty(keyList)) {
                    break;
                }
                String requestUrl = morganStanleyNotifyConfig.getRequestUrl();
                String groupName = morganStanleyNotifyConfig.getAsyncGroupName();
                List<NotifyApplication> jobList = importAssetPackageJobHandler.createAsyncJobs(keyList, requestUrl, groupName);
                morganStanleyNotifyServer.pushJobs(jobList);
                log.debug("FillAsyncImportAssetPackageTask push job success rebuild [" + jobList.size() + "] jobs");
            }
            Thread.sleep(sleepMillis);
        } catch (Exception e) {
            log.error("FillAsyncImportAssetPackageTask occur error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private FillAsyncImportModel getExecutingRedisIdForFill(Long startId, int limit) {
        Page page = null;
        if (limit > 0) {
            page = new Page(0, limit);
        }
        List<String> resultList = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        List<ImportAssetPackageJob> jobs = importAssetPackageJobService.getImportAssetPackageJobsByExecuteStatus(EXECUTING, startId, page);
        if (!org.springframework.util.CollectionUtils.isEmpty(jobs)) {
            for (ImportAssetPackageJob job : jobs) {
                Date firstProcessTime = job.getFirstProcessTime();
                if (isTimeOut(firstProcessTime)) {
                    List<String> keyList = importAssetPackageJobHandler.getRedisKeyList(job);
                    for (String key : keyList) {
                        Map<String, Object> dataMap = importAssetPackageJobHandler.getDataFromRedis(key);
                        if (importAssetPackageJobHandler.checkExecutedDataInRedis(key)) {
                            continue;
                        }
                        Date startWaitTime = transString2Time((String) dataMap.get(KEY_MODIFY_TIME));
                        if (startWaitTime.getTime() >= currentTime || isTimeOut(startWaitTime)) {
                            resultList.add(key);
                        }
                        log.info("FillAsyncImportAssetPackageTask request detail subRequestNo[" + key + "] has rebuild.");
                    }
                }
                startId = getNewStartId(startId, job);
            }
        }

        return new FillAsyncImportModel(startId, resultList);
    }

    private FillAsyncImportModel getUndoRedisIdForFill(Long startId, int limit) {
        Page page = null;
        if (limit > 0) {
            page = new Page(0, limit);
        }
        List<String> resultList = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        List<ImportAssetPackageJob> jobs = importAssetPackageJobService.getImportAssetPackageJobsByExecuteStatus(UNEXECUTE, startId, page);
        if (!org.springframework.util.CollectionUtils.isEmpty(jobs)) {
            for (ImportAssetPackageJob job : jobs) {
                List<String> keyList = importAssetPackageJobHandler.getRedisKeyList(job);
                for (String key : keyList) {
                    Map<String, Object> dataMap = importAssetPackageJobHandler.getDataFromRedis(key);
                    Date createTime = job.getCreateTime();
                    if (isTimeOut(createTime)) {
                        if (importAssetPackageJobHandler.checkExecutedDataInRedis(key)) {
                            Date firstProcessTime = transString2Time((String) dataMap.get(KEY_MODIFY_TIME));
                            job.setFirstProcessTime(firstProcessTime);
                            if (job.getContractDetailSize() == 1) {
                                job.setExecuteStatus(WAITING_CALLBACK);
                            } else {
                                job.setExecuteStatus(EXECUTING);
                            }
                            importAssetPackageJobService.saveOrUpdate(job);
                            continue;
                        }
                        Date startWaitTime = transString2Time((String) dataMap.get(KEY_MODIFY_TIME));
                        if (startWaitTime.getTime() >= currentTime || isTimeOut(startWaitTime)) {
                            resultList.add(key);
                        }
                        log.info("FillAsyncImportAssetPackageTask request detail subRequestNo[" + key + "] has rebuild.");
                    }
                }
                startId = getNewStartId(startId, job);
            }
        }
        return new FillAsyncImportModel(startId, resultList);
    }

    private Date transString2Time(String dateString) {
        return DateUtils.parseDate(dateString, DateUtils.LONG_DATE_FORMAT);
    }

    private boolean isTimeOut(Date initDate) {
        Date rightNow = new Date();
        long initDateTime = initDate.getTime();
        long rightNowTime = rightNow.getTime();
        return (rightNowTime - initDateTime) > morganStanleyNotifyConfig.getMakeUpPeriod();
    }

    private Long getNewStartId(Long id, ImportAssetPackageJob job) {
        if (job.getId() > id) {
            id = job.getId();
        }
        return id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class FillAsyncImportModel {
        private Long newStartId;
        List<String> keyList;
    }
}
