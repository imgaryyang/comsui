package com.suidifu.giotto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastAccountTemplateKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import org.apache.commons.lang.StringUtils;

public class FastAccountTemplate extends FastCacheObject {

	private Long id;

	private String uuid;

	private String scenarioId;

	private Integer eventType;

	private String ledgerBookNo;

	private Date defaultDate;

	private String templateSignature;

	@Override
	public String obtainAddCacheKey() throws GiottoException {
		if (StringUtils.isBlank(getLedgerBookNo()) && null == getEventType()) {
			throw new GiottoException("all keys value is null.");
		}
		String result = FastAccountTemplateKeyEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getLedgerBookNo())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getLedgerBookNo()).concat(":");
		}
		if (null == getEventType()) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getEventType().toString());
		}
		return result;
//		return FastAccountTemplateKeyEnum.PREFIX_KEY.concat(this.getLedgerBookNo()).concat(":").concat(this.getEventType().toString());
	}

	@Override
	public SqlAndParamTuple obtainInsertSqlAndParam() {
		String addsql = "insert into t_account_template (id,default_date," +
				"enent_type,ledger_book_no,uuid,scenario_id,template_signature) values"
				+ "(:id,:default_date,:enent_type,:ledger_book_no,:uuid," +
				":scenario_id,:template_signature)";
		String fastJson = JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteMapNullValue);
		return new SqlAndParamTuple(addsql, JsonUtils.parse(fastJson));
	}
	/**
	 * 暂未实现
	 */
	@Override
	public String obtainQueryCheckMD5Sql(String updateSql) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public FastKey getColumnName() {
		return FastAccountTemplateKeyEnum.LEDGER_BOOK_NO;
	}
	
	@Override
	public String getColumnValue() {
		return ledgerBookNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public String getLedgerBookNo() {
		return ledgerBookNo;
	}

	public void setLedgerBookNo(String ledgerBookNo) {
		this.ledgerBookNo = ledgerBookNo;
	}

	public Date getDefaultDate() {
		return defaultDate;
	}

	public void setDefaultDate(Date defaultDate) {
		this.defaultDate = defaultDate;
	}

	public String getTemplateSignature() {
		return templateSignature;
	}

	public void setTemplateSignature(String templateSignature) {
		this.templateSignature = templateSignature;
	}

	public String getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {{
			if (StringUtils.isNotBlank(getLedgerBookNo())) {
				add(FastAccountTemplateKeyEnum.PREFIX_KEY.concat(getLedgerBookNo()));
			}
			if (null != getEventType()) {
				add(FastAccountTemplateKeyEnum.PREFIX_KEY.concat(getEventType().toString()));
			}
		}};
	}

}
