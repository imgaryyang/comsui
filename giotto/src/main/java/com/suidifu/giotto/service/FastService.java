package com.suidifu.giotto.service;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastCacheObject;

import java.util.List;
import java.util.Map;

/**
 * FastService
 *
 * @author whb
 * @date 2017/5/26
 */

public interface FastService {

    <T> Map<String, List<T>> getByKeyList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz) throws GiottoException;

    <T> List<T> getByKey(FastKey keyEnum, String keyValue, Class<T> clazz) throws GiottoException;

    void delByKey(FastKey keyEnum, String keyValue) throws GiottoException;

    void add(FastCacheObject fastObject) throws GiottoException;

    void addNormal(FastCacheObject fastObject);

    void addList(List<FastCacheObject> fastCacheObjectList) throws GiottoException;

    int updateInDB(String updateSql, Map<String, Object> paramMap, FastCacheObject fastCacheObject) throws GiottoException;
    
    
    //FastAccountTemplate 
    default <T> List<T> getByKey(Map<FastKey, String>map, Class<T> clazz) throws GiottoException {
    	 throw new GiottoException("not support method.");
    }
    
}
