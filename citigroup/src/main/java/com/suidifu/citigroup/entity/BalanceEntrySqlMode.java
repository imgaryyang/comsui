package com.suidifu.citigroup.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.suidifu.citigroup.api.util.SqlVoAttribute;
import com.suidifu.citigroup.exception.CitiGroupRuntimeException;
import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;

/**
 * Created by wukai on 2017/11/23.
 */
public class BalanceEntrySqlMode {

//	@SqlVoAttribute(name = "")
    private long id;

	@SqlVoAttribute(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

	@SqlVoAttribute(name = "general_balance_uuid")
    private  String generalBalanceUuid;
	
	@SqlVoAttribute(name = "financial_contract_uuid")
	private String financialContractUuid;
	
	@SqlVoAttribute(name = "remittance_application_uuid")
	 private String remittanceApplicationUuid;
	    
	@SqlVoAttribute(name = "remittance_plan_uuid")
	private String remittancePlanUuid;

    //balance sector

	@SqlVoAttribute(name = "debit_balance")
    private BigDecimal debitBalance = new BigDecimal(0);

	@SqlVoAttribute(name = "credit_balance")
    private BigDecimal creditBalance = new BigDecimal(0);
    //account sector

	@SqlVoAttribute(name = "first_account_name")
    private String firstAccountName;

	@SqlVoAttribute(name = "first_account_uuid")
    private String firstAccountUuid;

	@SqlVoAttribute(name = "second_account_name")
    private String secondAccountName;

	@SqlVoAttribute(name = "second_account_uuid")
    private String secondAccountUuid;

	@SqlVoAttribute(name = "third_account_name")
    private String thirdAccountName;

	@SqlVoAttribute(name = "third_account_uuid")
    private String thirdAccountUuid;

	@SqlVoAttribute(name = "account_side")
    private int accountSide;

	@SqlVoAttribute(name = "book_in_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookInDate = new Date();

	@SqlVoAttribute(name = "combine_prevent_repetition")
	 private String combinePreventRepetition;
	
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
	
	public static void validateRemittanceRequestParam(RemittanceResponsePacket remittanceResponsePacket){
    	
    	if (null==remittanceResponsePacket) {
			
    		throw new CitiGroupRuntimeException("请求参数不匹配");
		}
    	
    	if ( StringUtils.isEmpty(remittanceResponsePacket.getFinancialContractUuid())) {
			
    		throw new CitiGroupRuntimeException("financialContractUuid不能为空");
		}
    	if (StringUtils.isEmpty(remittanceResponsePacket.getRemittanceApplicationUuid())) {
			
    		throw new CitiGroupRuntimeException("remittanceApplicationUuid不能为空");
		}
    	
    	List<RemittancePlanInfo>  remittancePlanInfos = remittanceResponsePacket.getRemittancePlanInfoList();

    	if (CollectionUtils.isEmpty(remittanceResponsePacket.getRemittancePlanInfoList())) {
	
    		throw new CitiGroupRuntimeException("RemittancePlanInfo不能为空");
    	}
    	
    	boolean valideAmount = remittancePlanInfos.stream().filter(remittancePlanInfo -> remittancePlanInfo.getAmount().compareTo(new BigDecimal(0))>0)!=null;
    	
    	if (!valideAmount) {
    		throw new CitiGroupRuntimeException("放款金额不能等于０或小于０");
    	}
    	
    }
	
	
public static boolean validateBalanceRequestModelForInsert(BalanceRequestModel balanceRequestModel){
    	
    	if (null==balanceRequestModel) {
			
    		throw new CitiGroupRuntimeException("请求参数不匹配");
		}
    	
    	if (StringUtils.isEmpty(balanceRequestModel.getFinancialContractUuid())) {
			
    		throw new CitiGroupRuntimeException("financialContractUuid不能为空");
		}
    	
    	if (StringUtils.isEmpty(balanceRequestModel.getFinancialContractName())) {
			
    		throw new CitiGroupRuntimeException("financialContractName不能为空");
		}
    	
    	if (balanceRequestModel.getAmount().compareTo(new BigDecimal(0))<=0) {
    		throw new CitiGroupRuntimeException("额度金额不能等于０或小于０");
    	}
    	return true;
    }


public static boolean validateBalanceRequestModelForUpdate(BalanceRequestModel balanceRequestModel){
	
	if (null==balanceRequestModel) {
		
		throw new CitiGroupRuntimeException("请求参数不匹配");
	}
	
	if (StringUtils.isEmpty(balanceRequestModel.getFinancialContractUuid())) {
		
		throw new CitiGroupRuntimeException("financialContractUuid不能为空");
	}
	
	if (balanceRequestModel.getAmount().compareTo(new BigDecimal(0))<=0) {
		throw new CitiGroupRuntimeException("额度金额不能等于０或小于０");
	}
	return true;
}
	
	
	//冻结
	public static List<BalanceEntrySqlMode> combineBalanceEntrySqlModeForFreezing(RemittanceResponsePacket remittanceResponsePacket){
		List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<>();
		List<BalanceRequestModel> balanceRequestModels = conventPackageToBalanceRequestModelForFreezing(remittanceResponsePacket);
		
		for(BalanceRequestModel balanceRequestModel:balanceRequestModels){
		BalanceEntrySqlMode balanceEntrySqlMode =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BALANCE,RemittanceChartOfAccounts.FREEZING);
		balanceEntrySqlMode.setCreditBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode);
		
		BalanceEntrySqlMode balanceEntrySqlMode1 = BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel,RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND,RemittanceChartOfAccounts.FREEZING);
		balanceEntrySqlMode1.setDebitBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode1);
		}
		return balanceEntrySqlModes;
	}
	
	public static List<BalanceEntrySqlMode> combineBalanceEntrySqlModeForUnFreez(RemittanceResponsePacket remittanceResponsePacket){
		
		List<RemittancePlanInfo> remittancePlanInfos = remittanceResponsePacket.getRemittancePlanInfoList();
		
		List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<>();
		
		for (RemittancePlanInfo remittancePlanInfo:remittancePlanInfos) {
			
			if (null==remittancePlanInfo.getIsSuccess()) {
				
				throw new CitiGroupRuntimeException("放款单状态不能为空!");
			}
			
			if (remittancePlanInfo.getIsSuccess()) {
			
				balanceEntrySqlModes.addAll(combineBalanceEntrySqlModeForRemittanceSuccess(remittanceResponsePacket,remittancePlanInfo));
			}else{
			
				balanceEntrySqlModes.addAll(combineBalanceEntrySqlModeForRemittanceFail(remittanceResponsePacket,remittancePlanInfo));
			}
		}
		return balanceEntrySqlModes;
	}
	
	
	//放款成功
	public static List<BalanceEntrySqlMode> combineBalanceEntrySqlModeForRemittanceSuccess(RemittanceResponsePacket remittanceResponsePacket,RemittancePlanInfo remittancePlanInfo){
		List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<>();
		BalanceRequestModel balanceRequestModel = conventPackageToBalanceRequestModel(remittanceResponsePacket,remittancePlanInfo);
		
		BalanceEntrySqlMode balanceEntrySqlMode =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND,RemittanceChartOfAccounts.UNFREEZING);
		balanceEntrySqlMode.setCreditBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode);
		
		BalanceEntrySqlMode balanceEntrySqlMode1 = BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel,RemittanceChartOfAccounts.TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT,RemittanceChartOfAccounts.UNFREEZING);
		balanceEntrySqlMode1.setDebitBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode1);
		
		return balanceEntrySqlModes;
	}
	
	//冻结撤销
	public static List<BalanceEntrySqlMode> combineBalanceEntrySqlModeForRemittanceFail(RemittanceResponsePacket remittanceResponsePacket,RemittancePlanInfo remittancePlanInfo){
		
		List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<>();
		BalanceRequestModel balanceRequestModel = conventPackageToBalanceRequestModel(remittanceResponsePacket,remittancePlanInfo);
		
		BalanceEntrySqlMode balanceEntrySqlMode =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND,RemittanceChartOfAccounts.UNFREEZING);
		balanceEntrySqlMode.setCreditBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode);
		
		BalanceEntrySqlMode balanceEntrySqlMode1 =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BALANCE,RemittanceChartOfAccounts.UNFREEZING);
		balanceEntrySqlMode1.setDebitBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode1);
		return balanceEntrySqlModes;
		
	}
	
	//充值
	public static List<BalanceEntrySqlMode> combineBalanceEntrySqlModeForIncreaseOrInsertPayAble(BalanceRequestModel balanceRequestModel){
		
		List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<>();
		BalanceEntrySqlMode balanceEntrySqlMode =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BALANCE,RemittanceChartOfAccounts.RECHARGE);
		balanceEntrySqlMode.setDebitBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode);
		
		BalanceEntrySqlMode balanceEntrySqlMode1 =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT,RemittanceChartOfAccounts.RECHARGE);
		balanceEntrySqlMode1.setCreditBalance(balanceRequestModel.getAmount());
		balanceEntrySqlModes.add(balanceEntrySqlMode1);
		
		return balanceEntrySqlModes;
		
	}
	
	
	//充值撤销
		public static List<BalanceEntrySqlMode> combineBalanceEntrySqlModeForReducePayAble(BalanceRequestModel balanceRequestModel){

			List<BalanceEntrySqlMode> balanceEntrySqlModes = new ArrayList<>();
			BalanceEntrySqlMode balanceEntrySqlMode =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BALANCE,RemittanceChartOfAccounts.UNRECHARGE);
			balanceEntrySqlMode.setCreditBalance(balanceRequestModel.getAmount());
			balanceEntrySqlModes.add(balanceEntrySqlMode);
			
			BalanceEntrySqlMode balanceEntrySqlMode1 =BalanceEntrySqlMode.combineAccountInfo(balanceRequestModel, RemittanceChartOfAccounts.TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT,RemittanceChartOfAccounts.UNRECHARGE);
			balanceEntrySqlMode1.setDebitBalance(balanceRequestModel.getAmount());
			balanceEntrySqlModes.add(balanceEntrySqlMode1);
			
			return balanceEntrySqlModes;
		}
	
		
	public static BalanceEntrySqlMode combineAccountInfo(BalanceRequestModel balanceRequestModel,String type,String freezeType){
		BalanceEntrySqlMode balanceEntrySqlMode = new BalanceEntrySqlMode();
		
		String combinePreventRepetition = StringUtils.isEmpty(balanceRequestModel.getRemittancePlanUuid())?UUID.randomUUID().toString():balanceRequestModel.getRemittancePlanUuid();
		RemittanceAccountInfo remittanceAccountInfo = RemittanceChartOfAccounts.EntryBook().get(type);
		balanceEntrySqlMode.setFinancialContractUuid(balanceRequestModel.getFinancialContractUuid());
		balanceEntrySqlMode.setRemittanceApplicationUuid(balanceRequestModel.getRemittanceApplicationUuid());
		balanceEntrySqlMode.setRemittancePlanUuid(balanceRequestModel.getRemittancePlanUuid());
		balanceEntrySqlMode.setFirstAccountName(remittanceAccountInfo.getFirstAccountName());
		balanceEntrySqlMode.setFirstAccountUuid(remittanceAccountInfo.getFirstAccountUuid());
		balanceEntrySqlMode.setSecondAccountName(remittanceAccountInfo.getSecondAccountName());
		balanceEntrySqlMode.setSecondAccountUuid(remittanceAccountInfo.getSecondAccountUuid());
		balanceEntrySqlMode.setThirdAccountName(remittanceAccountInfo.getThirdAccountName());
		balanceEntrySqlMode.setThirdAccountUuid(remittanceAccountInfo.getThirdAccountUuid());
		balanceEntrySqlMode.setAccountSide( remittanceAccountInfo.getAccountSide());
		balanceEntrySqlMode.setCombinePreventRepetition(combinePreventRepetition+"_"+remittanceAccountInfo.getThirdAccountUuid()+"_"+freezeType);
		return balanceEntrySqlMode;
		
	}
	
	public static BalanceRequestModel conventPackageToBalanceRequestModel(RemittanceResponsePacket remittanceResponsePacket,RemittancePlanInfo remittancePlanInfo){
		
			BalanceRequestModel balanceRequestModel = new BalanceRequestModel();
			balanceRequestModel.setFinancialContractUuid(remittanceResponsePacket.getFinancialContractUuid());
			balanceRequestModel.setRemittanceApplicationUuid(remittanceResponsePacket.getRemittanceApplicationUuid());
			balanceRequestModel.setRemittancePlanUuid(remittancePlanInfo.getRemittancePlanUuid());
			balanceRequestModel.setAmount(remittancePlanInfo.getAmount());
			balanceRequestModel.setIsSuccess(remittancePlanInfo.getIsSuccess());
			return balanceRequestModel;
	}
	
	
	
	public static List<BalanceRequestModel> conventPackageToBalanceRequestModelForFreezing(RemittanceResponsePacket remittanceResponsePacket){
		
		List<BalanceRequestModel> balanceRequestModels = new ArrayList<BalanceRequestModel>();
		
		List<RemittancePlanInfo> remittancePlanInfos = remittanceResponsePacket.getRemittancePlanInfoList();
		
		for (RemittancePlanInfo remittancePlanInfo:remittancePlanInfos) {
			
			BalanceRequestModel balanceRequestModel = new BalanceRequestModel();
			balanceRequestModel.setFinancialContractUuid(remittanceResponsePacket.getFinancialContractUuid());
			balanceRequestModel.setRemittanceApplicationUuid(remittanceResponsePacket.getRemittanceApplicationUuid());
			balanceRequestModel.setRemittancePlanUuid(remittancePlanInfo.getRemittancePlanUuid());
			balanceRequestModel.setAmount(remittancePlanInfo.getAmount());
			balanceRequestModel.setIsSuccess(remittancePlanInfo.getIsSuccess());
			balanceRequestModels.add(balanceRequestModel);
		}
		return balanceRequestModels;
	}
	
	public BalanceEntrySqlMode(){
		super();
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
