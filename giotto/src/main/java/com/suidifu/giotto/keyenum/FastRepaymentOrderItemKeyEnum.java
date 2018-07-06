package com.suidifu.giotto.keyenum;

import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FastRepaymentOrderItemKeyEnum
 *
 * @author whb
 * @date 2017/5/26
 */

public enum FastRepaymentOrderItemKeyEnum implements FastKey {

    ORDER_DETAIL_UUID("orderDetailUuid", "order_detail_uuid"),
    MER_ID("merId", "mer_id"),
    ORDER_UUID("orderUuid", "order_uuid"),
    REPAYMENT_BUSINESS_UUID("repaymentBusinessUuid", "repayment_business_uuid");

    public static final String TABLE_NAME = "repayment_order_item";
    public static final String PREFIX_KEY = "fast:orderItem:";

    private static final String ITEM_QUERY_SQL = "select id," +
            "order_detail_uuid orderDetailUuid, contract_unique_id contractUniqueId, " +
            "contract_no contractNo," +
            "contract_uuid contractUuid,amount amount, detail_alive_status detailAliveStatus, " +
            "detail_pay_status detailPayStatus," +
            "repayment_way repaymentWay, " +
            "repayment_business_no repaymentBusinessNo," +
            "repayment_business_type repaymentBusinessType, " +
            "repayment_plan_time repaymentPlanTime, order_uuid orderUuid," +
            "order_unique_id orderUniqueId, string_field_1 stringField1, " +
            "string_field_2 stringField2," +
            "string_field_3 stringField3, date_field_1 dateField1, " +
            "date_field_2 dateField2," +
            "date_field_3 dateField3, decimal_field_1 decimalField1, " +
            "decimal_field_2 decimalField2," +
            "decimal_field_3 decimalField3, remark, " +
            "repayment_business_uuid repaymentBusinessUuid, mer_id merId, financial_contract_uuid financialContractUuid, "
            + "create_time createTime, last_modified_time lastModifiedTime"
            +",current_period currentPeriod,repay_schedule_no repayScheduleNo,identification_mode identificationMode"
            + ",receivable_in_advance_status receivableInAdvanceStatus,charge_detail chargeDetail";

    private String keyName;
    private String columnName;

    private FastRepaymentOrderItemKeyEnum(String keyName, String columnName) {
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
            conditionSql = ITEM_QUERY_SQL;
        }
        conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").
                concat(this.getColumnName()).concat(" = :").concat(this.getKeyName());
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put(this.getKeyName(), keyValue);
        return new SqlAndParamTuple(conditionSql, paramMap);
    }

    @Override
    public SqlAndParamTuple getSqlAndParamByList(List<String> keyValList, boolean isDel) {
        String conditionSql = "delete";
        if (!isDel) {
            conditionSql = ITEM_QUERY_SQL;
        }
        conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").
                concat(this.getColumnName()).concat(" in (:").concat(this.getKeyName()).concat(")");
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put(this.getKeyName(), keyValList);
        return new SqlAndParamTuple(conditionSql, paramMap);
    }


    public String getKeyName() {
        return keyName;
    }

    public String getColumnName() {
        return columnName;
    }
}
