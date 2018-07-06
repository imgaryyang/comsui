package com.suidifu.bridgewater.fast;

import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/9/8.
 */
public enum FastDeductApplicationEnum implements FastKey {

    DEDUCT_APPLICATION_UUID("deductApplicationUuid","deduct_application_uuid"),
    REQUEST_NO("requestNo","request_no"),
    DEDUCT_ID("deductId","deduct_id");

    public static final String TABLE_NAME = "t_deduct_application";
    public static final String PREFIX_KEY = "fast:ra:";

    private static final String REMITTANCE_APPLICATION_QUERY_SQL = "SELECT deduct_application_uuid , deduct_id , " +
            "request_no , financial_contract_uuid , financial_product_code , contract_unique_id , repayment_plan_code_list " +
            ", contract_no , planned_deduct_total_amount , actual_deduct_total_amount , notify_url , transcation_type " +
            ", repayment_type , execution_status , execution_remark , create_time , creator_name , ip , last_modified_time " +
            ", record_status , is_available , api_called_time , transaction_recipient , customer_name , mobile , gateway " +
            ", source_type , third_part_voucher_status , complete_time , transaction_time , business_check_status , version " +
            ", plan_notify_number , batch_deduct_id , none_business_check_status , batch_deduct_application_uuid , " +
            "retry_times , actual_notify_number , payment_order_uuid , retriable , notify_status , total_count , " +
            "executed_count , receive_status ";

    private String keyName;
    private String columnName;

    FastDeductApplicationEnum(String keyName, String columnName) {
        this.keyName = keyName;
        this.columnName = columnName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getKey(String keyValue) {
        return PREFIX_KEY.concat(keyValue);
    }

    @Override
    public SqlAndParamTuple getSqlAndParam(String keyValue, boolean isDel) {
        if (isDel)
            throw new RuntimeException("prohibit delete RemittanceApplication");
        String conditionSql = REMITTANCE_APPLICATION_QUERY_SQL;
        conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").concat(this.getColumnName())
                .concat(" = :").concat(this.getKeyName());
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put(this.getKeyName(), keyValue);
        return new SqlAndParamTuple(conditionSql, paramMap);
    }

}
