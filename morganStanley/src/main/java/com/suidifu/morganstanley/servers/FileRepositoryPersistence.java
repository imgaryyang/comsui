package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.exception.MorganStanleyException;

import java.util.List;

/**
 * 文件仓库redis
 * @author louguanyang at 2017/8/16 13:31
 */
public interface FileRepositoryPersistence {
    void pushToTail(String key, List<String> bizIdList) throws MorganStanleyException;

    void pushToTail(String key, String value) throws MorganStanleyException;

    void deleteAll(String key) throws MorganStanleyException;

    String get(String key) throws MorganStanleyException;

    List<String> peekJobsFromHead(String key) throws MorganStanleyException;
}
