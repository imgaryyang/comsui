package com.suidifu.matryoshka.delayPosition.handler.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.suidifu.matryoshka.cache.DelayTaskConfigCache;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigSourceCodeCompilerHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;
import com.suidifu.matryoshka.delayTask.DelayTaskConfigCacheSpec;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskConfigDBService;
import com.suidifu.xcode.exception.XcodeException;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;
import com.suidifu.xcode.handler.XcodeServerHandler;
import com.suidifu.xcode.util.CompilerContainer;
import com.zufangbao.gluon.exception.caches.CacheNotConfigException;
import com.zufangbao.sun.utils.StringUtils;

/**
 * 后置任务配置handler
 * Created by louguanyang on 2017/5/6.
 */
public class DelayTaskConfigCacheHandlerImpl implements DelayTaskConfigCacheHandler {

    private static final Log LOGGER = LogFactory.getLog(DelayTaskConfigCacheHandlerImpl.class);

    @Autowired
    private DelayProcessingTaskConfigDBService delayProcessingTaskConfigService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private XcodeServerHandler xcodeServerHandler;

    @Override
    public DelayProcessingTaskConfig getConfigByUuid(String delayTaskConfigUuid) {
        if (StringUtils.isEmpty(delayTaskConfigUuid)) {
            return null;
        }
        Cache cache = getDelayTaskConfigCache();
        DelayTaskConfigCache delayTaskConfigCache = cache.get(delayTaskConfigUuid, DelayTaskConfigCache.class);
        if (null != delayTaskConfigCache && !delayTaskConfigCache.needUpdate()) {
            return delayTaskConfigCache.getDelayProcessingTaskConfig();
        }
        DelayProcessingTaskConfig taskConfig = delayProcessingTaskConfigService.getValidConfig(delayTaskConfigUuid);
        if (taskConfig == null) {
            LOGGER.info("config not found or invalid");
            return null;
        }
        DelayTaskConfigCache newDelayTaskConfigCache =new DelayTaskConfigCache(taskConfig);

        cache.put(delayTaskConfigUuid, newDelayTaskConfigCache);

        return taskConfig;
    }

    @Override
    public Object getCompiledObject(DelayProcessingTaskConfig config) {
        try {
            if (null == config) {
                LOGGER.warn("DelayProcessingTaskConfig is null");
                return null;
            }
            String businessType = config.getUuid();
            String executeCodeVersion = config.getExecuteCodeVersion();
            register(businessType, new DelayTaskConfigSourceCodeCompilerHandler());
            return xcodeServerHandler.getNewest(businessType, executeCodeVersion);
        } catch (XcodeException e) {
            e.printStackTrace();
            LOGGER.warn("xcodeServerHandler getNewest has error, error msg:" + e.getMessage());
            return null;
        }
    }

    @Override
    public Object getCompiledObjectDelayTaskConfigUuid(String delayTaskConfigUuid) {
        DelayProcessingTaskConfig taskConfig = getConfigByUuid(delayTaskConfigUuid);
        return getCompiledObject(taskConfig);
    }

    @Override
    public void register(String businessType, SourceCodeCompilerHandler sourceCodeCompilerHandler) {
        if (CompilerContainer.compilerContainer.containsKey(businessType)) {
            return;
        }
        xcodeServerHandler.register(businessType, sourceCodeCompilerHandler);
    }

    @Override
    public void clearAll() {
        Cache cache = getDelayTaskConfigCache();
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void clearByUrl(String url) {
        Cache cache = getDelayTaskConfigCache();
        if (null != cache.get(url)) {
            cache.evict(url);
        }
    }

    private Cache getDelayTaskConfigCache() {
        Cache cache = cacheManager.getCache(DelayTaskConfigCacheSpec.CACHE_KEY);
        if (cache == null) {
            LOGGER.warn("DelayTaskConfigCache not found, please check ehcache config!");
            throw new CacheNotConfigException();
        }
        return cache;
    }

}
