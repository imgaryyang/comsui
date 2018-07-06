package com.suidifu.giotto.keyenum;

import java.util.Map;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.List;

/**
 * FastKey
 *
 * @author whb
 * @date 2017/5/29
 */

public interface FastKey {

    default String getKeyPattern(String keyValue) throws GiottoException {
    	throw new GiottoException("not support method.");
    }

    default SqlAndParamTuple getSqlAndParam(String keyValue, boolean isDel) throws GiottoException {
    	throw new GiottoException("not support method.");
    }
    
    default String getKeyPattern(Map<FastKey, String> keyValue) throws GiottoException {
    	throw new GiottoException("not support method.");
    }
    
    default SqlAndParamTuple getSqlAndParam(Map<FastKey, String> keyValue, boolean isDel) throws GiottoException {
    	throw new GiottoException("not support method.");
    }
    String getKey(String keyValue);


    default SqlAndParamTuple getSqlAndParamByList(List<String> keyValList, boolean isDel) throws GiottoException {
        throw new GiottoException("not support key value list query.");
    }

}
