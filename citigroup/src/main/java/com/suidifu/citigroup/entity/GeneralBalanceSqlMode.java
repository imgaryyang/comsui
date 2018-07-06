package com.suidifu.citigroup.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.suidifu.citigroup.api.util.SqlVoAttribute;
import com.suidifu.citigroup.exception.CitiGroupRuntimeException;
import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;

/**
 * Created by wukai on 2017/11/23.
 */
public class GeneralBalanceSqlMode {

    private Long id;
    
    @SqlVoAttribute(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @SqlVoAttribute(name = "related_financial_contract_uuid")
    private String relatedFinancialContractUuid;

    @SqlVoAttribute(name = "related_contract_name")
    private String relatedContractName;

    //银存-放款
    @SqlVoAttribute(name = "bank_saving_loan")
    private BigDecimal bankSavingLoan= new BigDecimal(0);

    //银存-冻结
    @SqlVoAttribute(name = "bank_saving_freeze")
    private BigDecimal bankSavingFreeze= new BigDecimal(0);

    // 应付
    @SqlVoAttribute(name = "pay_able")
    private BigDecimal payAble= new BigDecimal(0);

    @SqlVoAttribute(name = "version_lock")
    private String versionLock = UUID.randomUUID().toString();

    @SqlVoAttribute(name = "create_time")
    private Date createTime = new Date();

    @SqlVoAttribute(name = "lastest_modified_time")
    private Date lastestModifiedTime = new Date();
    
    @SqlVoAttribute(name = "int_field_1")
    private Integer intField1;

    @SqlVoAttribute(name = "int_field_2")
	private Integer intField2;

    @SqlVoAttribute(name = "int_field_3")
	private Integer intField3;

    @SqlVoAttribute(name = "string_field_1")
	private String stringField1;

    @SqlVoAttribute(name = "string_field_2")
	private String stringField2;

    @SqlVoAttribute(name = "string_field_3")
	private String stringField3;

    @SqlVoAttribute(name = "decimal_field_1")
	private BigDecimal decimalField1;

    @SqlVoAttribute(name = "decimal_field_2")
	private BigDecimal decimalField2;

    @SqlVoAttribute(name = "decimal_field_3")
	private BigDecimal decimalField3;

    public GeneralBalanceSqlMode(BalanceRequestModel balanceRequestModel){
    	
    	relatedFinancialContractUuid =balanceRequestModel.getFinancialContractUuid();
    	relatedContractName = balanceRequestModel.getFinancialContractName();
    	bankSavingLoan =balanceRequestModel.getAmount();
    	bankSavingFreeze = new BigDecimal(0);
    	payAble =balanceRequestModel.getAmount().multiply(new BigDecimal(-1));
    	createTime = new Date();
    	lastestModifiedTime = new Date();
    }
    
    public GeneralBalanceSqlMode(){
    	super();
    }
    
    public static void validateForUpdateBankSavingFreeze(Map<String, String> params){
    	
    	if (MapUtils.isEmpty(params)) {
			
    		throw new CitiGroupRuntimeException("请求参数不能为空");
		}
    	
    	if (StringUtils.isEmpty( params.get("related_financial_contract_uuid"))) {
			
    		throw new CitiGroupRuntimeException("信托合同Uuid不能为空");
		}
    	
    	if (StringUtils.isEmpty( params.get("pay_able"))) {
			
    		throw new CitiGroupRuntimeException("放款额度不能为空");
		}
    	
    	if (new BigDecimal(params.get("pay_able")).compareTo(new BigDecimal(0))<0) {
			
    		throw new CitiGroupRuntimeException("放款金额不能为负");
		}
    	
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
