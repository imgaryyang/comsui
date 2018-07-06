package com.suidifu.giotto.util;

import java.util.Map;

/**
 * SqlAndParamTuple
 *
 * @author whb
 * @date 2017/5/26
 */

public class SqlAndParamTuple {

    public String sql;

    public Map<String, Object> paramMap;

    public SqlAndParamTuple(String sql, Map<String, Object> paramMap) {
        this.sql = sql;
        this.paramMap = paramMap;
    }
}
