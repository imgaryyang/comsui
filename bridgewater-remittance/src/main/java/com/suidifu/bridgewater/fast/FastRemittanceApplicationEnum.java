package com.suidifu.bridgewater.fast;

import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;

import java.util.HashMap;
import java.util.Map;

public enum FastRemittanceApplicationEnum implements FastKey {

	REMITTANCE_APPLICATION_UUID("remittanceApplicationUuid", "remittance_application_uuid"),
	REQUEST_NO("requestNo", "request_no"),
	REMITTANCE_ID("remittanceId", "remittance_id");

	public static final String TABLE_NAME = "t_remittance_application";
	public static final String PREFIX_KEY = "fast:ra:";
	private static final String REMITTANCE_APPLICATION_QUERY_SQL = "SELECT id id," +
			"remittance_application_uuid remittanceApplicationUuid," +
			"request_no requestNo," +
			"financial_contract_uuid financialContractUuid," +
			"financial_contract_id financialContractId," +
			"financial_product_code financialProductCode," +
			"contract_unique_id contractUniqueId," +
			"contract_no contractNo," +
			"planned_total_amount plannedTotalAmount," +
			"actual_total_amount actualTotalAmount," +
			"auditor_name auditorName," +
			"audit_time auditTime," +
			"notify_url notifyUrl," +
			"plan_notify_number planNotifyNumber," +
			"actual_notify_number actualNotifyNumber," +
			"remittance_strategy remittanceStrategy," +
			"remark remark," +
			"transaction_recipient transactionRecipient," +
			"execution_status executionStatus," +
			"execution_remark executionRemark," +
			"create_time createTime," +
			"creator_name creatorName," +
			"ip ip," +
			"last_modified_time lastModifiedTime," +
			"opposite_receive_date oppositeReceiveDate," +
			"remittance_id remittanceId," +
			"total_count totalCount," +
			"actual_count actualCount," +
			"version_lock versionLock," + 
			"string_field_1 stringField1 ";

	private String keyName;
	private String columnName;

	FastRemittanceApplicationEnum(String keyName, String columnName) {
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
			throw new RuntimeException("prohibit delete RemittanceApplication");
		String conditionSql = REMITTANCE_APPLICATION_QUERY_SQL;
		conditionSql = conditionSql.concat(" from ".concat(TABLE_NAME)).concat(" where ").concat(this.getColumnName())
				.concat(" = :").concat(this.getKeyName()).concat(" order by id desc ");
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
