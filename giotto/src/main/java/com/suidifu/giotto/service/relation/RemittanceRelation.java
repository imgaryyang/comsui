package com.suidifu.giotto.service.relation;

public enum RemittanceRelation {
	RA_RD("remittanceApplicationUuid",":RA_RD"," select remittance_application_detail_uuid from t_remittance_application_detail where remittance_application_uuid=:remittanceApplicationUuid "),
	RD_RP("remittanceApplicationDetailUuid",":RD_RP"," select remittance_plan_uuid from t_remittance_plan where remittance_application_detail_uuid=:remittanceApplicationDetailUuid "),
	RA_RP("remittanceApplicationUuid",":RA_RP"," select remittance_plan_uuid from t_remittance_plan where remittance_application_uuid=:remittanceApplicationUuid ");
	private String key;
	private String suffix;
	private String sql;
	private RemittanceRelation(String key, String suffix,String sql) {
		this.key = key;
		this.suffix = suffix;
		this.sql = sql;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}

}
