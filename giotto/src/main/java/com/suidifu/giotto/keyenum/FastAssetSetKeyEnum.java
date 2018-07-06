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

public enum FastAssetSetKeyEnum implements FastKey {

    ASSET_SET_UUID("assetSetUuid", "asset_uuid"),
    SINGLE_LOAN_CONTRACT_NO("singleLoanContractNo", "single_loan_contract_no"),
    REPAY_SCHEDULE_NO("repayScheduleNo","repay_schedule_no");

    public static final String TABLE_NAME = "asset_set";
    public static final String PREFIX_KEY = "fast:asset:";
    private static final String ASSET_QUERY_SQL = "select id,guarantee_status guaranteeStatus,settlement_status settlementStatus," +
            "asset_fair_value assetFairValue," +
            "  asset_principal_value assetPrincipalValue," +
            "  asset_interest_value assetInterestValue,asset_initial_value assetInitialValue," +
            "  asset_recycle_date assetRecycleDate," +
            "  confirm_recycle_date confirmRecycleDate," +
            "  refund_amount refundAmount," +
            "  asset_status assetStatus," +
            "  on_account_status onAccountStatus," +
            "  repayment_plan_type repaymentPlanType," +
            "  last_valuation_time lastValuationTime," +
            "  asset_uuid assetUuid," +
            "  create_time createTime,last_modified_time lastModifiedTime," +
            "comment,single_loan_contract_no singleLoanContractNo," +
            "contract_id contractId,actual_recycle_date actualRecycleDate," +
            "current_period currentPeriod,overdue_status overdueStatus," +
            "overdue_date overdueDate,version_no versionNo,active_status activeStatus," +
            "sync_status syncStatus,active_deduct_application_uuid activeDeductApplicationUuid," +
            "financial_contract_uuid financialContractUuid," +
            "asset_finger_print assetFingerPrint,asset_extra_fee_finger_print assetExtraFeeFingerPrint," +
            "asset_finger_print_update_time assetFingerPrintUpdateTime," +
            "asset_extra_fee_finger_print_update_time assetExtraFeeFingerPrintUpdateTime," +
            "plan_type planType,write_off_reason writeOffReason," +
            "can_be_rollbacked canBeRollbacked,time_interval timeInterval," +
            "deduction_status deductionStatus,executing_status executingStatus," +
            "executing_status_bak executingStatusBak,customer_uuid customerUuid," +
            "contract_uuid contractUuid,contract_funding_status contractFundingStatus,"+
            "version_lock versionLock,order_payment_status orderPaymentStatus ,repay_schedule_no repayScheduleNo,outer_repayment_plan_no outerRepaymentPlanNo";

    private String keyName;
    private String columnName;

    private FastAssetSetKeyEnum(String keyName, String columnName) {
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
            conditionSql = ASSET_QUERY_SQL;
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
