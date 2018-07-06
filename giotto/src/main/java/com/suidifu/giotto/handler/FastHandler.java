package com.suidifu.giotto.handler;

import java.util.List;
import java.util.Map;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastCacheObject;

/**FastHandler
 * 
 * @author zfj
 * @date  2017/5/30
 */
public interface FastHandler{

    <T> List<T> getListByKeyList(FastKey keyEnum, List<String> keyValueList, Class<T> clazz, boolean isDirtyRead) throws GiottoException;

    <T> List<T> getByKeyList(FastKey keyEnum, String keyValue, Class<T> clazz, boolean isDirtyRead) throws GiottoException;
    
    <T> T getByKey(FastKey keyEnum, String keyValue, Class<T> clazz, boolean isDirtyRead) throws GiottoException;
    
    
    <T> List<T> getByKeyList(Map<FastKey, String> keyValueMap, Class<T> clazz, boolean isDirtyRead) throws GiottoException;
    
    <T> T getByKey(Map<FastKey, String> keyValueMap, Class<T> clazz, boolean isDirtyRead) throws GiottoException;

    
    
    void delByKey(FastKey keyEnum, String keyValue, boolean isOnlyDelCache) throws GiottoException;

    void add(FastCacheObject fastObject, boolean isOnlyAddCache) throws GiottoException;

    
    
//    int update(String updateSql, Map<String, Object> paramMap, FastCacheObject fastCacheObject);
}
