package com.suidifu.giotto.keyenum;

import java.util.HashMap;
import java.util.Map;

import com.suidifu.giotto.util.SqlAndParamTuple;

public enum FastAccountTemplateKeyEnum implements FastKey {

	LEDGER_BOOK_NO("ledgerBookNo", "ledger_book_no"),
	EVENT_TYPE("eventType","event_Type");
	
	private String keyName;
	private String columnName;

	private final String TABLE_NAME = "t_account_template";
	public static final String PREFIX_KEY = "fast:accountTemplate:";
	private final String ACCOUNT_TEMPLATE_QUERY_SQL = "select id," +
			"default_date,event_type,ledger_book_no,uuid,scenario_id,template_signature";

	private FastAccountTemplateKeyEnum(String keyName, String columnName) {
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

	/*@Override
	public String getKeyPattern(String keyValue) {
		String keypatten = null;
		String[] split = keyValue.split("\\|");
		keypatten = PREFIX_KEY.concat(split[0]).concat(":").concat(split[1]);
		return keypatten;
	}*/

	
	@Override
	public String getKeyPattern(Map<FastKey, String> keyValue) {
		String keyPatten = null;
		String ledgerBookNo = "";
		String eventType = "";
		for(Map.Entry<FastKey, String> map : keyValue.entrySet()){
			FastKey key = map.getKey();
			if(key == FastAccountTemplateKeyEnum.LEDGER_BOOK_NO){
				ledgerBookNo = map.getValue();
			}
			if(key == FastAccountTemplateKeyEnum.EVENT_TYPE){
				eventType = map.getValue();
			}
		}
		keyPatten = PREFIX_KEY.concat(ledgerBookNo).concat(":").concat(eventType);
		
		return keyPatten;
	}

	
	@Override
	public SqlAndParamTuple getSqlAndParam(Map<FastKey, String> keyValue, boolean isDel) {
		String conditionSql = ACCOUNT_TEMPLATE_QUERY_SQL;
		String ledgerBookNo = "";
		String eventType = "";
		for(Map.Entry<FastKey, String> map : keyValue.entrySet()){
			FastKey key = map.getKey();
			if(key == FastAccountTemplateKeyEnum.LEDGER_BOOK_NO){
				ledgerBookNo = map.getValue();
			}
			if(key == FastAccountTemplateKeyEnum.EVENT_TYPE){
				eventType = map.getValue();
			}
		}
		conditionSql = conditionSql.concat(" from ").concat(TABLE_NAME).concat(" where ").concat(LEDGER_BOOK_NO.getColumnName())
				.concat(" = :").concat(LEDGER_BOOK_NO.getKeyName()).concat(" and ").concat(EVENT_TYPE.getColumnName()).concat(" = :").concat(EVENT_TYPE.getKeyName());
		Map<String,Object> map = new HashMap<>(2);
		map.put(LEDGER_BOOK_NO.getKeyName(), ledgerBookNo);
		map.put(EVENT_TYPE.getKeyName(), eventType);
		return new SqlAndParamTuple(conditionSql, map);
	}

	@Override
	public String getKey(String keyValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public SqlAndParamTuple getSqlAndParam(String keyValue, boolean isDel) {
		String conditionSql = ACCOUNT_TEMPLATE_QUERY_SQL;
		String[] split = keyValue.split("\\|");
		conditionSql = conditionSql.concat(" from ").concat(TABLE_NAME).concat(" where ").concat(LEDGER_BOOK_NO.getColumnName())
				.concat(" = :").concat(LEDGER_BOOK_NO.getKeyName()).concat(" and ").concat(EVENT_TYPE.getColumnName()).concat(" = :").concat(EVENT_TYPE.getKeyName());
		Map<String, Object> map = new HashMap<>(2);
		map.put(LEDGER_BOOK_NO.getKeyName(), split[0]);
		map.put(EVENT_TYPE.getKeyName(), split[1]);

		return new SqlAndParamTuple(conditionSql, map);
	}
*/

}
