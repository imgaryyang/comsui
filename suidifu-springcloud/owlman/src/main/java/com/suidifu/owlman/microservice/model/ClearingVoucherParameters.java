package com.suidifu.owlman.microservice.model;

public class ClearingVoucherParameters implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8712724603028489594L;

	private String deductPlanUuid;

	private String auditJobUuid;
	
	private String totalReceivableBillsUuid;
	

	public ClearingVoucherParameters() {
		super();
	}

	public ClearingVoucherParameters(String deductPlanUuid, String auditJobUuid, String totalReceivableBillsUuid) {
		super();
		this.deductPlanUuid = deductPlanUuid;
		this.auditJobUuid = auditJobUuid;
		this.totalReceivableBillsUuid = totalReceivableBillsUuid;
	}

	public String getAuditJobUuid() {
		return auditJobUuid;
	}

	public void setAuditJobUuid(String auditJobUuid) {
		this.auditJobUuid = auditJobUuid;
	}

	public String getTotalReceivableBillsUuid() {
		return totalReceivableBillsUuid;
	}

	public void setTotalReceivableBillsUuid(String totalReceivableBillsUuid) {
		this.totalReceivableBillsUuid = totalReceivableBillsUuid;
	}

	public String getDeductPlanUuid() {
		return deductPlanUuid;
	}

	public void setDeductPlanUuid(String deductPlanUuid) {
		this.deductPlanUuid = deductPlanUuid;
	}
	
}
