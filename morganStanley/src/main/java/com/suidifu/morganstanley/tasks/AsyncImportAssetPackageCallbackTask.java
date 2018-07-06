package com.suidifu.morganstanley.tasks;

import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.controller.task.TaskSpec;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.morganstanley.utils.DateUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageJob;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageJobLog;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCacheSpec;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.yunxin.cache.DictionaryCache;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageJobHandler;
import com.zufangbao.wellsfargo.yunxin.model.AsyncImportAssetPackageCallbackModel;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.*;

@Log4j2
@Component("asyncImportAssetPackageCallbackTask")
public class AsyncImportAssetPackageCallbackTask extends BasicTask {

    @Resource
    private ImportAssetPackageJobHandler importAssetPackageJobHandler;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    @Resource
    private MorganStanleyNotifyServer morganStanleyNotifyServer;

    private static final String REDIS_ID = "as_i:callbackJobStartIds";
    private static final String COMP_START_ID = "compStartId";
    private static final String EXE_START_ID = "exeStartId";

    @Override
    public void run() {
        int limit = getIntegetFromWorkParamOrDefault(TaskSpec.PARAM_LIMIT_SIZE,
                TaskSpec.DEFAULT_LIMIT_SIZE);
        long sleepMillis = getLongFromWorkParamOrDefault(TaskSpec.PARAM_SLEEP_MILLIS,
                TaskSpec.DEFAULT_SLEEP_MILLIS);
        try {
            if (limit == 0) {
                limit = -1;
            }
            Long compStartId = 0L;
            Long exeStartId = 0L;
            Map<String, Object> map = getDataFromRedis(REDIS_ID);
            if (null != map) {
                compStartId = Long.valueOf((String) map.getOrDefault(COMP_START_ID,"0"));
                exeStartId = Long.valueOf((String) map.getOrDefault(EXE_START_ID,"0"));
            } else {
                map = new HashMap<>();
            }
            while (true) {
                AsyncImportAssetPackageCallbackModel asyncImportAssetPackageCallbackModel = importAssetPackageJobHandler.callbackOperate(compStartId, exeStartId, limit);
                List<ImportAssetPackageJob> callbackJobList = asyncImportAssetPackageCallbackModel.getCallableJobs();
                if (CollectionUtils.isEmpty(callbackJobList)) {
                    break;
                }
                compStartId = asyncImportAssetPackageCallbackModel.getCompStartId();
                exeStartId = asyncImportAssetPackageCallbackModel.getExeStartId();
                map.put(COMP_START_ID, compStartId + "");
                map.put(EXE_START_ID, exeStartId + "");
                saveOrUpdateRedis(map, REDIS_ID);
                String privateKey = getPrivateKey();
                for (ImportAssetPackageJob job : callbackJobList) {
                    String requestNo = job.getRequestNo();
                    String callbackUrl = job.getCallbackUrl();
                    if (StringUtils.isEmpty(callbackUrl)) {
                        callbackUrl = morganStanleyNotifyConfig.getDefaultCallbackUrl();
                    }
                    if (StringUtils.isEmpty(callbackUrl)) {
                        log.error("AsyncImportAssetPackageCallbackTask There are no callbackUrl and defaultCallbackUrl !");
                        return;
                    }
                    ImportAssetPackageJobLog importAssetPackageJobLog = importAssetPackageJobHandler.recordJobLog(job);
                    String originRequestNo = importAssetPackageJobLog.getRequestNo();
                    String ip = importAssetPackageJobLog.getIp();
                    String requestTime = DateUtils.format(importAssetPackageJobLog.getRequestTime(), DateUtils.LONG_DATE_FORMAT);
                    String completeTime = DateUtils.format(importAssetPackageJobLog.getCompleteTime(), DateUtils.LONG_DATE_FORMAT);
                    String contractDetailSize = importAssetPackageJobLog.getContractDetailSize() + "";
                    String financialProductCode = importAssetPackageJobLog.getFinancialProductCode();
                    String contractsTotalNumber = importAssetPackageJobLog.getContractsTotalNumber() + "";
                    String contractsTotalAmount = importAssetPackageJobLog.getContractsTotalAmount();
                    String successCount = importAssetPackageJobLog.getSuccessSize() + "";
                    String loanBatchId = importAssetPackageJobLog.getLoanBatchCode();

                    HashMap<String, String> requestParameters = new HashMap<>();
                    requestParameters.put("requestNo", originRequestNo);
                    requestParameters.put("requestIp", ip);
                    requestParameters.put("requestTime", requestTime);
                    requestParameters.put("completeTime", completeTime);
                    requestParameters.put("contractDetailSize", contractDetailSize);
                    requestParameters.put("financialProductCode", financialProductCode);
                    requestParameters.put("contractsTotalNumber", contractsTotalNumber);
                    requestParameters.put("contractsTotalAmount", contractsTotalAmount);
                    requestParameters.put("successCount", successCount);
                    requestParameters.put("loanBatchId", loanBatchId);

                    String requestData = JsonUtils.toJSONString(requestParameters);
                    HashMap<String, String> header = new HashMap<>();
                    String sign = ApiSignUtils.rsaSign(requestData, privateKey);

                    header.put(PARAMS_MER_ID, morganStanleyNotifyConfig.getMerId());
                    header.put(PARAMS_SECRET, morganStanleyNotifyConfig.getSecret());
                    header.put(PARAMS_SIGN, sign);

                    buildAsyncCallbackNotifyJob(callbackUrl, requestNo, requestParameters, header);
                    log.debug("AsyncImportAssetPackageCallbackTask push job success callbackUrl=[" + callbackUrl + "],requestNo=[" + requestNo + "]");
                }
            }
            Thread.sleep(sleepMillis);
        } catch (Exception e) {
            log.error("AsyncImportAssetPackageCallbackTask occur error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildAsyncCallbackNotifyJob(String callbackUrl, String requestNo, HashMap<String, String> requestParameters, HashMap<String, String> header) {
        NotifyApplication notifyJob = new NotifyApplication();
        notifyJob.setHttpJobUuid(UUIDUtil.random32UUID());
        notifyJob.setBusinessId(requestNo);
        notifyJob.setRetryTimes(2);
        notifyJob.setTimeOutSetting(60000);
        notifyJob.setRequestMethod(NotifyApplication.POST_METHOD);
        notifyJob.setDelaySecond(0);
        notifyJob.setGroupName(morganStanleyNotifyConfig.getCallbackGroupName());
        notifyJob.setRequestUrl(callbackUrl);
        notifyJob.setRequestParameters(requestParameters);
        HashMap<Integer, Long> retryIntervals = new HashMap<>();
        retryIntervals.put(1, 120000L);
        retryIntervals.put(2, 120000L);
        notifyJob.setRetryIntervals(retryIntervals);
        notifyJob.setHeadParameters(header);
        morganStanleyNotifyServer.pushJob(notifyJob);
    }

    private String getPrivateKey() {
        Cache cache = cacheManager.getCache(DictionaryCacheSpec.CACHE_KEY);
        DictionaryCache dictionaryCache = cache.get(DictionaryCode.PLATFORM_PRI_KEY.getCode(), DictionaryCache.class);
        long timeout = 1000L * 60 * 60 * 24;
        if (null != dictionaryCache && !dictionaryCache.needUpdate(timeout)) {
            return dictionaryCache.getDictionary().getContent();
        }
        Dictionary priKeyDictionary;
        try {
            priKeyDictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
            cache.put(DictionaryCode.PLATFORM_PRI_KEY.getCode(), new DictionaryCache(priKeyDictionary));
        } catch (DictionaryNotExsitException e) {
            e.printStackTrace();
            return null;
        }
        return priKeyDictionary.getContent();
    }

}
