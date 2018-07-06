package com.suidifu.giotto.keyenum;

import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.Map;

/**
 * FastAssetSetKeyEnum
 *
 * @author whb
 * @date 2017/5/26
 */

public enum FastCustomerKeyEnum implements FastKey {

    CUSTOMER_UUID("customerUuid", "customer_uuid"),
    ACCOUNT("account", "account");

    public static final String TABLE_NAME = "customer";
    public static final String PREFIX_KEY = "fast:customer:";

    private static final String CUSTOMER_QUERY_SQL = "select id, account, mobile, name, source,app_id appId, " +
            "customer_uuid customerUuid, customer_type customerType";

    private String keyName;
    private String columnName;

    private FastCustomerKeyEnum(String keyName, String columnName) {
        this.keyName = keyName;
        this.columnName = columnName;
    }

    @Override
    public String getKey(String keyValue) {
        return PREFIX_KEY.concat(keyValue);
    }

    @Override
    public SqlAndParamTuple getSqlAndParam(String keyValue, boolean isDel) {
        String conditionSql = "delete";
        if (!isDel) {
            conditionSql = CUSTOMER_QUERY_SQL;
        }
        conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").
                concat(this.getColumnName()).concat(" = :").concat(this.getKeyName());
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put(this.getKeyName(), keyValue);
        return new SqlAndParamTuple(conditionSql, paramMap);
    }

    public String getKeyName() {
        return keyName;
    }

    public String getColumnName() {
        return columnName;
    }

}
