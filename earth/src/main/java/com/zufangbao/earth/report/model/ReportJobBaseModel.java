package com.zufangbao.earth.report.model;

import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ReportJobBaseModel {

	/**
	 * 报表编号
	 */
	private String reportId;
	
	/**
	 * 查询开始日期（yyyy-MM-dd）
	 */
	private String queryStartDate;
	
	/**
	 * 查询截止日期（yyyy-MM-dd）
	 */
	private String queryEndDate;
	
	private String financialContractUuids;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getQueryStartDate() {
		return queryStartDate;
	}
	
	public Date getQueryStartDateAsDay() {
		return queryStartDate == null ? null : DateUtils.asDay(queryStartDate);
	}

	public void setQueryStartDate(String queryStartDate) {
		this.queryStartDate = queryStartDate;
	}

	public String getQueryEndDate() {
		return queryEndDate;
	}
	
	public Date getQueryEndDateAsDay() {
		return queryEndDate == null ? null : DateUtils.asDay(queryEndDate);
	}

	public void setQueryEndDate(String queryEndDate) {
		this.queryEndDate = queryEndDate;
	}

	public String getFinancialContractUuids() {
		return financialContractUuids;
	}

	public void setFinancialContractUuids(String financialContractUuids) {
		this.financialContractUuids = financialContractUuids;
	}
	
	public List<String> getFinancialContractUuidList() {
		List<String> list = JsonUtils.parseArray(this.financialContractUuids, String.class);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	public ReportJobBaseModel() {
		super();
	}
	
}
