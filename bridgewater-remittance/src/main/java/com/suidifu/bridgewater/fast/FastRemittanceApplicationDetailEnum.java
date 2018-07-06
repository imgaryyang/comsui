package com.suidifu.bridgewater.fast;

import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.Map;

public enum FastRemittanceApplicationDetailEnum implements FastKey {

	REMITTANCE_APPLICATION_DETAIL_UUID("remittanceApplicationDetailUuid", "remittance_application_detail_uuid");

	public static final String TABLE_NAME = "t_remittance_application_detail";
	public static final String PREFIX_KEY = "fast:rd:";
	private static final String REMITTANCE_DETAIL_QUERY_SQL = "SELECT id id," +
			"remittance_application_detail_uuid remittanceApplicationDetailUuid," +
			"remittance_application_uuid remittanceApplicationUuid," +
			"financial_contract_uuid financialContractUuid," +
			"financial_contract_id financialContractId," +
			"business_record_no businessRecordNo," +
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
			"execution_status executionStatus," +
			"execution_remark executionRemark," +
			"create_time createTime," +
			"creator_name creatorName," +
			"last_modified_time lastModifiedTime, " +
			"total_count totalCount, " +
			"actual_count actualCount, " +
			"version_lock versionLock ";

	private String keyName;
	private String columnName;

	FastRemittanceApplicationDetailEnum(String keyName, String columnName) {
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
			throw new RuntimeException("prohibit delete RemittanceApplicationDetail");
		String conditionSql = REMITTANCE_DETAIL_QUERY_SQL;
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
