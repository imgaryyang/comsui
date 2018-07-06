/**
 *
 */
package com.suidifu.morganstanley.tasks;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.gluon.util.SpringContextUtil;
import com.zufangbao.sun.entity.task.DynamicBusinessTask;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wukai
 */
public class BasicTask implements Runnable {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final static Map<String, String> TASK_GROUP_MAPPER = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("asyncImportAssetPackageCallbackTask", "asyncImportAssetPackageCallbackTask");
            put("fillAsyncImportAssetPackageTask", "fillAsyncImportAssetPackageTask");
//            put("dailyReconciliationFileTask", "dailyReconciliationFileTask");
        }
    };

    /**
     * 服务主机
     */
    private String serverHost;

    /**
     * 服务组别
     */
    private int serverGroup;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务组别
     */
    private String taskGroup;

    /**
     * 定时表达式
     */
    private String cronExpression;

    /**
     * 周期性频率
     */
    private Long periodicTrigger = 0L;

    /**
     * 是否启用
     */
    private boolean isEnabled = false;

    /**
     * 工作参数
     */
    private Map<String, String> workParams = new HashMap<String, String>();

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(int serverGroup) {
        this.serverGroup = serverGroup;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String id) {
        this.taskId = id;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getPeriodicTrigger() {
        return periodicTrigger;
    }

    public void setPeriodicTrigger(Long periodicTrigger) {
        this.periodicTrigger = periodicTrigger;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Map<String, String> getWorkParams() {
        return workParams;
    }

    public void setWorkParams(Map<String, String> params) {
        this.workParams = params;
    }

    @JSONField(serialize = false)
    public String getWorkParam(String key) {
        if (StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        return this.workParams.getOrDefault(key, StringUtils.EMPTY);
    }

    @JSONField(serialize = false)
    public boolean isValid() {
        if (StringUtils.isBlank(this.taskGroup)) {
            return false;
        }
        if (!TASK_GROUP_MAPPER.containsKey(this.taskGroup)) {
            return false;
        }
        if (StringUtils.isBlank(this.cronExpression) && periodicTrigger == 0L) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static BasicTask convertToRunnableTask(DynamicBusinessTask task) {
        String beanName = TASK_GROUP_MAPPER.getOrDefault(task.getTaskGroup(), StringUtils.EMPTY);
        BasicTask runnableTask = SpringContextUtil.getBean(beanName);
        runnableTask.setServerHost(task.getServerHost());
        runnableTask.setServerGroup(task.getServerGroup());
        runnableTask.setTaskId(task.getTaskId());
        runnableTask.setTaskGroup(task.getTaskGroup());
        runnableTask.setIsEnabled(task.isEnabled());
        runnableTask.setPeriodicTrigger(task.getPeriodicTrigger());
        runnableTask.setCronExpression(task.getCronExpression());
        Map<String, String> workParams = JsonUtils.parse(task.getWorkParams(), Map.class);
        runnableTask.setWorkParams(workParams);
        return runnableTask;
    }

    @JSONField(serialize = false)
    public int getIntegetFromWorkParamOrDefault(String key, int defaultValue) {
        int value;
        try {
            value = Integer.valueOf(getWorkParam(key));
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    @JSONField(serialize = false)
    public long getLongFromWorkParamOrDefault(String key, long defaultValue) {
        long value;
        try {
            value = Long.valueOf(getWorkParam(key));
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    @Override
    public void run() {

    }

    protected Map<String, Object> getDataFromRedis(String redisId) {
        Map<String, Object> keyListMap = getRedisById(redisId);
        if (CollectionUtils.isEmpty(keyListMap)) {
            return null;
        }
        return keyListMap;
    }

    private Map<String, Object> getRedisById(String redisId) {
        Map<String, Object> resultMap = null;
        try {
            BoundHashOperations<String, String, Object> hashOperations =
                    stringRedisTemplate.boundHashOps(redisId);
            resultMap = hashOperations.entries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(resultMap)) {
            return null;
        }
        return resultMap;
    }

    protected void initDataRedis(String redisId, Map<String, Object> paramMap) {
        cleanRedisById(redisId);
        saveOrUpdateRedis(paramMap, redisId);
    }

    private void cleanRedisById(String redisId) {
        stringRedisTemplate.delete(redisId);
    }

    protected void saveOrUpdateRedis(Map<String, Object> paramMap, String redisId) {
        BoundHashOperations<String, String, Object> hashOperations = stringRedisTemplate.boundHashOps(redisId);
//        hashOperations.expire(2, TimeUnit.HOURS);
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            hashOperations.put(entry.getKey(), entry.getValue());
        }
    }
}
