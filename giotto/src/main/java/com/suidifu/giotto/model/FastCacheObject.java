package com.suidifu.giotto.model;


import com.alibaba.fastjson.JSON;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.List;

/**
 * FastCacheObject
 *
 * @author whb
 * @date 2017/5/26
 */

public abstract class FastCacheObject {

    public abstract String obtainAddCacheKey() throws GiottoException;

    public abstract SqlAndParamTuple obtainInsertSqlAndParam();

    public abstract String obtainQueryCheckMD5Sql(String updateSql);

//    public abstract String obtainUpdateCheckMD5Sql(String updateSql);

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss");
//        return JsonUtils.toJsonString(this);
    }
    public abstract FastKey getColumnName();

    public abstract String getColumnValue();

    public abstract List<String> obtainAddCacheKeyList();

    public String obtainQueryListKeyValue() throws GiottoException {
        throw new GiottoException("not support key value list query.");
    }
}
