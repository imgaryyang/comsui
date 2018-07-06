package com.suidifu.bridgewater.model.v2;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.utils.AttributeAnnotationValidatorUtils;
import com.zufangbao.sun.utils.ObjectAttributeValidator;
import com.zufangbao.sun.yunxin.entity.deduct.DeductGatewayMapSpec;

/**
 * Created by zhenghangbo on 10/05/2017.
 */
public class StandardDeductModel {

    private String deductApplicationUuid = UUID.randomUUID().toString();

	/**
	 * 功能代码
	 */
	private String fn;

    @ObjectAttributeValidator("String")
    private String requestNo;
    
    @ObjectAttributeValidator("String")
    private String deductId;

    @ObjectAttributeValidator("String")
    private String financialProductCode;
    
    @ObjectAttributeValidator("String")
    private String apiCalledTime;
    
	/**
	 * 贷款合同唯一编号
	 */
	private String uniqueId;
	
	/**
	 * 贷款合同编号
	 */
	private String contractNo;
	
	/**
	 * 交易类型
	 */
	private String transType;
    

    @ObjectAttributeValidator("BigDecimal")
    private String deductAmount;
	
	/**
	 * 还款类型
	 */
	private int repaymentType;
	
    @ObjectAttributeValidator("String")
    private String mobile;
    
    @ObjectAttributeValidator("String")
    private String accountHolderName;
	

    @ObjectAttributeValidator("String")
    private String deductAccountNo;
	
    @ObjectAttributeValidator("String")
    private String standardBankCode;
	
    @ObjectAttributeValidator("String")
    private String IDCardNo;

    @ObjectAttributeValidator("String")
    private String cpBankProvince;

    @ObjectAttributeValidator("String")
    private String cpBankCity;

    @ObjectAttributeValidator("String")
    private String accountBankName;
	
    /* 0 广银联  1 宝付 2 陕西建行代收付 3 好易联 4 陕西建行银企直联 5 江西建行银企直联*/
    @ObjectAttributeValidator("String")
    private String gateway;
    
	/**
	 * 回调地址
	 */
	private String notifyUrl;
	
    private String financialContractUuid;

    private String repayDetailInfo;
    
    
    private String batchDeductId;
    private String batchDeductApplicationUuid;
    
    private String protocolNo;
    

    private String  checkFailedMsg;

    private String creatorName;

    public StandardDeductModel() {
		super();
	}

	public StandardDeductModel(Map<String, String> delayPostRequestParams) {
		super();
		this.fn = delayPostRequestParams.getOrDefault("fn", StringUtils.EMPTY);
		this.requestNo = delayPostRequestParams.getOrDefault("requestNo", StringUtils.EMPTY);
		this.deductId = delayPostRequestParams.getOrDefault("deductId", StringUtils.EMPTY);
		this.financialProductCode = delayPostRequestParams.getOrDefault("financialProductCode", StringUtils.EMPTY);
		this.uniqueId = delayPostRequestParams.getOrDefault("uniqueId", StringUtils.EMPTY);
		this.contractNo = delayPostRequestParams.getOrDefault("contractNo", StringUtils.EMPTY);
		this.apiCalledTime = delayPostRequestParams.getOrDefault("apiCalledTime", StringUtils.EMPTY);
		this.transType = delayPostRequestParams.getOrDefault("transType", StringUtils.EMPTY);
		this.deductAmount = delayPostRequestParams.getOrDefault("deductAmount", StringUtils.EMPTY);
		this.accountHolderName = delayPostRequestParams.getOrDefault("accountHolderName", StringUtils.EMPTY);
		this.deductAccountNo = delayPostRequestParams.getOrDefault("deductAccountNo", StringUtils.EMPTY);
		this.standardBankCode = delayPostRequestParams.getOrDefault("standardBankCode", StringUtils.EMPTY);
		this.notifyUrl = delayPostRequestParams.getOrDefault("notifyUrl", StringUtils.EMPTY);
		
		this.batchDeductApplicationUuid = delayPostRequestParams.getOrDefault("batchDeductApplicationUuid", null);
		this.batchDeductId = delayPostRequestParams.getOrDefault("batchDeductId", null);
		
		this.protocolNo = delayPostRequestParams.getOrDefault("protocolNo", StringUtils.EMPTY);
		
		this.creatorName = delayPostRequestParams.getOrDefault("creatorName", StringUtils.EMPTY);
	}

	public String getDeductApplicationUuid() {
        return deductApplicationUuid;
    }

    public void setDeductApplicationUuid(String deductApplicationUuid) {
        this.deductApplicationUuid = deductApplicationUuid;
    }

    public String getStandardBankCode() {
        return standardBankCode;
    }

    public void setStandardBankCode(String standardBankCode) {
        this.standardBankCode = standardBankCode;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getFinancialContractUuid() {
        return financialContractUuid;
    }

    public void setFinancialContractUuid(String financialContractUuid) {
        this.financialContractUuid = financialContractUuid;
    }

    public String getDeductAccountNo() {
        return deductAccountNo;
    }

    public void setDeductAccountNo(String deductAccountNo) {
        this.deductAccountNo = deductAccountNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getIDCardNo() {
        return IDCardNo;
    }

    public void setIDCardNo(String IDCardNo) {
        this.IDCardNo = IDCardNo;
    }

    public String getCpBankProvince() {
        return cpBankProvince;
    }

    public void setCpBankProvince(String cpBankProvince) {
        this.cpBankProvince = cpBankProvince;
    }

    public String getCpBankCity() {
        return cpBankCity;
    }

    public void setCpBankCity(String cpBankCity) {
        this.cpBankCity = cpBankCity;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(String deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFinancialProductCode() {
        return financialProductCode;
    }

    public void setFinancialProductCode(String financialProductCode) {
        this.financialProductCode = financialProductCode;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public PaymentInstitutionName getStandardPaymentInstitutionName(){
        return   DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP.getOrDefault(this.gateway, null);
    }

    public String getDeductId() {
        return deductId;
    }

    public void setDeductId(String deductId) {
        this.deductId = deductId;
    }

    public String getApiCalledTime() {

        return apiCalledTime;
    }

    public void setApiCalledTime(String apiCalledTime) {
        this.apiCalledTime = apiCalledTime;
    }


    public String getRepayDetailInfo() {
        return repayDetailInfo;
    }

    public void setRepayDetailInfo(String repayDetailInfo) {
        this.repayDetailInfo = repayDetailInfo;
    }
    
    public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public int getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(int repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getBatchDeductId() {
		return batchDeductId;
	}

	public void setBatchDeductId(String batchDeductId) {
		this.batchDeductId = batchDeductId;
	}

	public String getBatchDeductApplicationUuid() {
		return batchDeductApplicationUuid;
	}

	public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
		this.batchDeductApplicationUuid = batchDeductApplicationUuid;
	}

	public String getProtocolNo() {
		return protocolNo;
	}

	public void setProtocolNo(String protocolNo) {
		this.protocolNo = protocolNo;
	}

	
	
	
	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCheckFailedMsg() {return checkFailedMsg;}

    public void setCheckFailedMsg(String checkFailedMsg) { this.checkFailedMsg = checkFailedMsg;}

    public boolean isValid(){

        List<String> errorStringFields = AttributeAnnotationValidatorUtils.getAllErrorFieldsName(this);
        if(!CollectionUtils.isEmpty(errorStringFields)){
            setCheckFailedMsg(errorStringFields.get(0)+"不能为空！！！");
            return false;
        }
        return true;
    }
}