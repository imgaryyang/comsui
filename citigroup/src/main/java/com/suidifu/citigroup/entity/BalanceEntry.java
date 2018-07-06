package com.suidifu.citigroup.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;

/**
 * Created by wukai on 2017/11/23.
 */
@Entity
@Table(name = "balance_entry")
public class BalanceEntry {

    @Id
    @GeneratedValue
    private long id;

    private String uuid;

    private  String generalBalanceUuid;
    
    private String financialContractUuid;
    
    private String remittanceApplicationUuid;
    
    private String remittancePlanUuid;

    //balance sector

    private BigDecimal debitBalance= new BigDecimal(0);

    private BigDecimal creditBalance= new BigDecimal(0);
    //account sector

    private String firstAccountName;

    private String firstAccountUuid;

    private String secondAccountName;

    private String secondAccountUuid;

    private String thirdAccountName;

    private String thirdAccountUuid;

    private int accountSide;

    
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookInDate = new Date();

    private String combinePreventRepetition;
 
    private Integer intField1;

   	private Integer intField2;

   	private Integer intField3;

   	private String stringField1;

   	private String stringField2;

   	private String stringField3;

   	private BigDecimal decimalField1;

   	private BigDecimal decimalField2;

   	private BigDecimal decimalField3;
    
    public BalanceEntry(BalanceRequestModel balanceRequestModel){
    	
    }
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGeneralBalanceUuid() {
		return generalBalanceUuid;
	}

	public void setGeneralBalanceUuid(String generalBalanceUuid) {
		this.generalBalanceUuid = generalBalanceUuid;
	}

	public BigDecimal getDebitBalance() {
		return debitBalance;
	}

	public void setDebitBalance(BigDecimal debitBalance) {
		this.debitBalance = debitBalance;
	}

	public BigDecimal getCreditBalance() {
		return creditBalance;
	}

	public void setCreditBalance(BigDecimal creditBalance) {
		this.creditBalance = creditBalance;
	}

	public String getFirstAccountName() {
		return firstAccountName;
	}

	public void setFirstAccountName(String firstAccountName) {
		this.firstAccountName = firstAccountName;
	}

	public String getFirstAccountUuid() {
		return firstAccountUuid;
	}

	public void setFirstAccountUuid(String firstAccountUuid) {
		this.firstAccountUuid = firstAccountUuid;
	}

	public String getSecondAccountName() {
		return secondAccountName;
	}

	public void setSecondAccountName(String secondAccountName) {
		this.secondAccountName = secondAccountName;
	}

	public String getSecondAccountUuid() {
		return secondAccountUuid;
	}

	public void setSecondAccountUuid(String secondAccountUuid) {
		this.secondAccountUuid = secondAccountUuid;
	}

	public String getThirdAccountName() {
		return thirdAccountName;
	}

	public void setThirdAccountName(String thirdAccountName) {
		this.thirdAccountName = thirdAccountName;
	}

	public String getThirdAccountUuid() {
		return thirdAccountUuid;
	}

	public void setThirdAccountUuid(String thirdAccountUuid) {
		this.thirdAccountUuid = thirdAccountUuid;
	}

	public int getAccountSide() {
		return accountSide;
	}

	public void setAccountSide(int accountSide) {
		this.accountSide = accountSide;
	}

	public Date getBookInDate() {
		return bookInDate;
	}

	public void setBookInDate(Date bookInDate) {
		this.bookInDate = bookInDate;
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getRemittancePlanUuid() {
		return remittancePlanUuid;
	}

	public void setRemittancePlanUuid(String remittancePlanUuid) {
		this.remittancePlanUuid = remittancePlanUuid;
	}

	public String getCombinePreventRepetition() {
		return combinePreventRepetition;
	}

	public void setCombinePreventRepetition(String combinePreventRepetition) {
		this.combinePreventRepetition = combinePreventRepetition;
	}

	public String getRemittanceApplicationUuid() {
		return remittanceApplicationUuid;
	}

	public void setRemittanceApplicationUuid(String remittanceApplicationUuid) {
		this.remittanceApplicationUuid = remittanceApplicationUuid;
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
