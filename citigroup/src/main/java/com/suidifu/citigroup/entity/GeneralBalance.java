package com.suidifu.citigroup.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wukai on 2017/11/23.
 */
@Entity
@Table(name = "general_balance")
public class GeneralBalance {

	 @Id
	 @GeneratedValue
    private Long id;

    private String uuid;

    private String relatedFinancialContractUuid;

    private String relatedContractName;

    //银存-放款
    private BigDecimal bankSavingLoan= new BigDecimal(0);

    //银存-冻结
    private BigDecimal bankSavingFreeze= new BigDecimal(0);

    // 应付
    private BigDecimal payAble= new BigDecimal(0);

    private String versionLock  = UUID.randomUUID().toString();

    private Date createTime = new Date();

    private Date lastestModifiedTime = new Date();
    
    private Integer intField1;

	private Integer intField2;

	private Integer intField3;

	private String stringField1;

	private String stringField2;

	private String stringField3;

	private BigDecimal decimalField1;

	private BigDecimal decimalField2;

	private BigDecimal decimalField3;

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

	public String getRelatedFinancialContractUuid() {
		return relatedFinancialContractUuid;
	}

	public void setRelatedFinancialContractUuid(String relatedFinancialContractUuid) {
		this.relatedFinancialContractUuid = relatedFinancialContractUuid;
	}

	public String getRelatedContractName() {
		return relatedContractName;
	}

	public void setRelatedContractName(String relatedContractName) {
		this.relatedContractName = relatedContractName;
	}

	public BigDecimal getBankSavingLoan() {
		return bankSavingLoan;
	}

	public void setBankSavingLoan(BigDecimal bankSavingLoan) {
		this.bankSavingLoan = bankSavingLoan;
	}

	public BigDecimal getBankSavingFreeze() {
		return bankSavingFreeze;
	}

	public void setBankSavingFreeze(BigDecimal bankSavingFreeze) {
		this.bankSavingFreeze = bankSavingFreeze;
	}

	public BigDecimal getPayAble() {
		return payAble;
	}

	public void setPayAble(BigDecimal payAble) {
		this.payAble = payAble;
	}

	public String getVersionLock() {
		return versionLock;
	}

	public void setVersionLock(String versionLock) {
		this.versionLock = versionLock;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastestModifiedTime() {
		return lastestModifiedTime;
	}

	public void setLastestModifiedTime(Date lastestModifiedTime) {
		this.lastestModifiedTime = lastestModifiedTime;
	}

	public Integer getIntField1() {
		return intField1;
	}

	public void setIntField1(Integer intField1) {
		this.intField1 = intField1;
	}

	public Integer getIntField2() {
		return intField2;
	}

	public void setIntField2(Integer intField2) {
		this.intField2 = intField2;
	}

	public Integer getIntField3() {
		return intField3;
	}

	public void setIntField3(Integer intField3) {
		this.intField3 = intField3;
	}

	public String getStringField1() {
		return stringField1;
	}

	public void setStringField1(String stringField1) {
		this.stringField1 = stringField1;
	}

	public String getStringField2() {
		return stringField2;
	}

	public void setStringField2(String stringField2) {
		this.stringField2 = stringField2;
	}

	public String getStringField3() {
		return stringField3;
	}

	public void setStringField3(String stringField3) {
		this.stringField3 = stringField3;
	}

	public BigDecimal getDecimalField1() {
		return decimalField1;
	}

	public void setDecimalField1(BigDecimal decimalField1) {
		this.decimalField1 = decimalField1;
	}

	public BigDecimal getDecimalField2() {
		return decimalField2;
	}

	public void setDecimalField2(BigDecimal decimalField2) {
		this.decimalField2 = decimalField2;
	}

	public BigDecimal getDecimalField3() {
		return decimalField3;
	}

	public void setDecimalField3(BigDecimal decimalField3) {
		this.decimalField3 = decimalField3;
	}
    
    
}
