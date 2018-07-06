package com.suidifu.bridgewater.fast.deductplan;

import java.util.HashMap;
import java.util.Map;

import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;

/**
 * 
 * @author wukai
 *
 */
public enum FastDeductPlanEnum implements FastKey {

    DEDUCT_PLAN_UUID("deductPlanUuid","deduct_plan_uuid"),;

    public static final String TABLE_NAME = "t_deduct_plan";
    public static final String PREFIX_KEY = "fast:dp:";

    private static final String DEDUCT_PLAN_QUERY_SQL = "SELECT `id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `batch_deduct_application_uuid`, `batch_deduct_id`, `none_business_check_status`, `business_check_status`, `notify_status`, `retriable`, `retry_times`, `payment_order_uuid`, `version`, `clearing_status`, `clearing_cash_flow_uuid`, `clearing_time`, `trade_schedule_slot_uuid`, `transaction_uuid`";

    private String keyName;
    private String columnName;

    FastDeductPlanEnum(String keyName, String columnName) {
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
            throw new RuntimeException("prohibit delete t_deduct_plan");
        
        String conditionSql = DEDUCT_PLAN_QUERY_SQL;
        
        conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").concat(this.getColumnName())
                .concat(" = :").concat(this.getKeyName());
        
        Map<String, Object> paramMap = new HashMap<>(1);
        
        paramMap.put(this.getKeyName(), keyValue);
        
        return new SqlAndParamTuple(conditionSql, paramMap);
    }

}
