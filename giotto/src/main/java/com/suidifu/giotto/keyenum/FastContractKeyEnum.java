package com.suidifu.giotto.keyenum;

import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.Map;

/**
 * FastContractKeyEnum
 *
 * @author whb
 * @date 2017/5/26
 */

public enum FastContractKeyEnum implements FastKey {

    CONTRACT_NO("contractNo", "contract_no"),
    UNIQUE_ID("uniqueId", "unique_id"),
    UUID("uuid", "uuid");

    public static final String TABLE_NAME = "contract";
    public static final String PREFIX_KEY = "fast:contract:";

    private static final String CONTRACT_QUERY_SQL = "select id,uuid, unique_id uniqueId, begin_date beginDate, " +
            "contract_no contractNo, end_date endDate, asset_type assetType," +
            "month_fee monthFee, app_id appId, customer_id customerId," +
            "house_id houseId, actual_end_date actualEndDate," +
            "create_time createTime, interest_rate interestRate," +
            "payment_day_in_month paymentDayInMonth, payment_frequency paymentFrequency," +
            "periods, repayment_way repaymentWay, total_amount totalAmount," +
            "penalty_interest penaltyInterest, active_version_no activeVersionNo," +
            "repayment_plan_operate_logs repaymentPlanOperateLogs, state," +
            "financial_contract_uuid financialContractUuid," +
            "interest_rate_cycle interestRateCycle, customer_uuid customerUuid ";

    private String keyName;
    private String columnName;

    private FastContractKeyEnum(String keyName, String columnName) {
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
            conditionSql = CONTRACT_QUERY_SQL;
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
