package com.suidifu.giotto.keyenum;

import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/12/14.
 */
public enum FastSourceDocumentDetailKeyEnum implements FastKey{

    UUID("uuid", "uuid");

    public static final String TABLE_NAME = "source_document_detail";
    public static final String PREFIX_KEY = "fast:sourceDocumentDetail";

    private static final String SourceDocumentDetai_QUERY_SQL = "select id,uuid,source_document_uuid,contract_unique_id,repayment_plan_no,amount," +
            "status,first_type,first_no,second_type,second_no,payer,receivable_account_no,payment_account_no,payment_name,payment_bank," +
            "check_state,comment,financial_contract_uuid,principal,interest,service_charge,maintenance_charge,other_charge,penalty_fee," +
            "late_penalty,late_fee,late_other_cost,voucher_uuid,actual_payment_time,repay_schedule_no,current_period,outer_repayment_plan_no ";

    private String keyName;
    private String columnName;

    private FastSourceDocumentDetailKeyEnum(String keyName, String columnName) {
        this.keyName = keyName;
        this.columnName = columnName;
    }

    @Override
    public String getKey(String keyValue) {
        return PREFIX_KEY.concat(keyValue);
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
}
