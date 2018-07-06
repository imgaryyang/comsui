package com.suidifu.bridgewater.fast;

import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.Map;

public enum FastRemittancePlanEnum implements FastKey {

	REMITTANCE_PLAN_UUID("remittancePlanUuid", "remittance_plan_uuid");

	public static final String TABLE_NAME = "t_remittance_plan";
	public static final String PREFIX_KEY = "fast:rp:";
	private static final String REMITTANCE_PLAN_QUERY_SQL = "SELECT id id," +
			"remittance_plan_uuid remittancePlanUuid," +
			"remittance_application_uuid remittanceApplicationUuid," +
			"remittance_application_detail_uuid remittanceApplicationDetailUuid," +
			"business_record_no businessRecordNo," +
			"financial_contract_uuid financialContractUuid," +
			"financial_contract_id financialContractId," +
			"contract_unique_id contractUniqueId," +
			"contract_no contractNo," +
			"payment_gateway paymentGateway," +
			"payment_channel_uuid paymentChannelUuid," +
			"payment_channel_name paymentChannelName," +
			"pg_account_name pgAccountName," +
			"pg_account_no pgAccountNo," +
			"pg_clearing_account pgClearingAccount," +
			"transaction_type transactionType," +
			"transaction_remark transactionRemark," +
			"priority_level priorityLevel," +
			"cp_bank_code cpBankCode," +
			"cp_bank_card_no cpBankCardNo," +
			"cp_bank_account_holder cpBankAccountHolder," +
			"cp_id_type cpIdType," +
			"cp_id_number cpIdNumber," +
			"cp_bank_province cpBankProvince," +
			"cp_bank_city cpBankCity," +
			"cp_bank_name cpBankName," +
			"planned_payment_date plannedPaymentDate," +
			"complete_payment_date completePaymentDate," +
			"planned_total_amount plannedTotalAmount," +
			"actual_total_amount actualTotalAmount," +
			"execution_precond executionPrecond," +
			"execution_status executionStatus," +
			"execution_remark executionRemark," +
			"transaction_serial_no transactionSerialNo," +
			"create_time createTime," +
			"creator_name creatorName," +
			"last_modified_time lastModifiedTime";

	private String keyName;
	private String columnName;

	FastRemittancePlanEnum(String keyName, String columnName) {
		this.keyName = keyName;
		this.columnName = columnName;
	}

	@Override
	public String getKey(String keyValue) {
		return PREFIX_KEY.concat(keyValue);
	}

	@Override
	public SqlAndParamTuple getSqlAndParam(String keyValue, boolean isDel) {
		if (isDel)
			throw new RuntimeException("prohibit delete RemittancePlan");
		String conditionSql = REMITTANCE_PLAN_QUERY_SQL;
		conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").concat(this.getColumnName())
				.concat(" = :").concat(this.getKeyName());
		Map<String, Object> paramMap = new HashMap<>(1);
		paramMap.put(this.getKeyName(), keyValue);
		return new SqlAndParamTuple(conditionSql, paramMap);
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
