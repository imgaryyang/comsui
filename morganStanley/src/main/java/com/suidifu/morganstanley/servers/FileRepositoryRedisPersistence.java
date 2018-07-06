package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 文件仓库redis缓存
 * @author louguanyang at 2017/8/16 13:33
 */
@Component("FileRepositoryRedisPersistence")
@Log4j2
public class FileRepositoryRedisPersistence implements FileRepositoryPersistence {
    private static final String T_FILE_REPOSITORY = "t_file_repository:";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void pushToTail(String key, List<String> bizIdList) throws MorganStanleyException {
        validateParams(key, bizIdList);
        try {
            String redisKey = T_FILE_REPOSITORY.concat(key);
            redisTemplate.execute(connection -> {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyByte = serializer.serialize(redisKey);
                for (String bizId : bizIdList) {
                    byte[] bizIdByte = serializer.serialize(bizId);
                    connection.rPush(keyByte, bizIdByte);
                }
                return true;
            }, false, true);
        } catch (Exception e) {
            log.error("save job to redis error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public void pushToTail(String key, String value) throws MorganStanleyException {
        validateParams(key, Collections.singletonList(value));
        try {
            String redisKey = T_FILE_REPOSITORY.concat(key);
            BoundListOperations<String, String> ops = redisTemplate.boundListOps(redisKey);
            ops.rightPush(value);
        } catch (Exception e) {
            log.error("save job to redis error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public void deleteAll(String key) throws MorganStanleyException {
        if (StringUtils.isEmpty(key)) {
            throw new MorganStanleyException("key is null.");
        }
        String redisKey = T_FILE_REPOSITORY.concat(key);
        try {
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            log.error("del all jobs by redis error : " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public String get(String key) throws MorganStanleyException {
        try {
            if (StringUtils.isEmpty(key)) {
                throw new MorganStanleyException("key is null.");
            }
            String redisKey = T_FILE_REPOSITORY.concat(key);
            BoundListOperations<String, String> ops = redisTemplate.boundListOps(redisKey);
            List<String> bizIdList = ops.range(0, 1);
            if (CollectionUtils.isEmpty(bizIdList)) {
                return null;
            }
            return bizIdList.get(0);
        } catch (MorganStanleyException e) {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public List<String> peekJobsFromHead(String key) throws MorganStanleyException {
        try {
            if (StringUtils.isEmpty(key)) {
                throw new MorganStanleyException("key is null.");
            }
            String redisKey = T_FILE_REPOSITORY.concat(key);
            BoundListOperations<String, String> ops = redisTemplate.boundListOps(redisKey);
            List<String> bizIdList = ops.range(0, -1);
            return bizIdList;
        } catch (MorganStanleyException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void validateParams(String key, List<String> bizIdList) throws MorganStanleyException {
        if (StringUtils.isEmpty(key)) {
            throw new MorganStanleyException("key is empty");
        }
        if (CollectionUtils.isEmpty(bizIdList)) {
            throw new MorganStanleyException("bizIdList is empty");
        }
    }
}
