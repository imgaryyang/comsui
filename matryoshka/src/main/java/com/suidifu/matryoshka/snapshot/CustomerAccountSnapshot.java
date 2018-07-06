package com.suidifu.matryoshka.snapshot;


import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;

/**
 * Created by louguanyang on 2017/4/20.
 */
public class CustomerAccountSnapshot {

    private String customerUuid;

    private String contractUuid;

    private String contractAccountUuid;

    private String virtualAccountUuid;

    /**
     * 客户姓名
     */
    private String name;

    private String mobile;

    /**
     * 客户身份证号
     */
    private String account;

    /**
     * 还款账户卡号
     */
    private String payAcNo;

    /**
     * 开户行名称
     */
    private String bank;

    /**
     * 还款账户银行代码
     */
    private String bankCode;
    /**
     * 还款账户开户行所在省
     */
    private String province;


    private String provinceCode;
    /**
     * 还款账户开户行所在市
     */
    private String city;

    private String cityCode;

    private String standardBankCode;

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractAccountUuid() {
        return contractAccountUuid;
    }

    public void setContractAccountUuid(String contractAccountUuid) {
        this.contractAccountUuid = contractAccountUuid;
    }

    public String getVirtualAccountUuid() {
        return virtualAccountUuid;
    }

    public void setVirtualAccountUuid(String virtualAccountUuid) {
        this.virtualAccountUuid = virtualAccountUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPayAcNo() {
        return payAcNo;
    }

    public void setPayAcNo(String payAcNo) {
        this.payAcNo = payAcNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStandardBankCode() {
        return standardBankCode;
    }

    public void setStandardBankCode(String standardBankCode) {
        this.standardBankCode = standardBankCode;
    }

	public CustomerAccountSnapshot() {
		super();
	}
    
	public CustomerAccountSnapshot(String customerUuid, String contractUuid, String contractAccountUuid,
			String virtualAccountUuid, String name, String mobile, String account, String payAcNo, String bank,
			String bankCode, String province, String provinceCode, String city, String cityCode,
			String standardBankCode) {
		super();
		this.customerUuid = customerUuid;
		this.contractUuid = contractUuid;
		this.contractAccountUuid = contractAccountUuid;
		this.virtualAccountUuid = virtualAccountUuid;
		this.name = name;
		this.mobile = mobile;
		this.account = account;
		this.payAcNo = payAcNo;
		this.bank = bank;
		this.bankCode = bankCode;
		this.province = province;
		this.provinceCode = provinceCode;
		this.city = city;
		this.cityCode = cityCode;
		this.standardBankCode = standardBankCode;
	}

	public CustomerAccountSnapshot(String contractUuid, Customer customer, ContractAccount contractAccount) {
		this.contractUuid = contractUuid;
		if (null != customer) {
			this.customerUuid = customer.getCustomerUuid();
			this.name = customer.getName();
			this.mobile = customer.getMobile();
			this.account = customer.getAccount();
		}
		if (null != contractAccount) {
			this.contractAccountUuid = contractAccount.getContractAccountUuid();
			this.virtualAccountUuid = contractAccount.getVirtualAccountUuid();
			this.payAcNo = contractAccount.getPayAcNo();
			this.bank = contractAccount.getBank();
			this.bankCode = contractAccount.getBankCode();
			this.province = contractAccount.getProvince();
			this.provinceCode = contractAccount.getProvinceCode();
			this.city = contractAccount.getCity();
			this.cityCode = contractAccount.getCityCode();
			this.standardBankCode = contractAccount.getStandardBankCode();
		}
	}
}
