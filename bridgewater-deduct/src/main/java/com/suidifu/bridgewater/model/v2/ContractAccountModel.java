package com.suidifu.bridgewater.model.v2;

import com.zufangbao.sun.entity.directbank.business.ContractAccount;

/**
 * Created by zhenghangbo on 10/05/2017.
 */
public class ContractAccountModel {


    private String bankCode;


    private String standardBankCode;

    private String idCardNum;

    private String payAcNo;

    private String payerName;

    private String provinceCode;

    private String cityCode;

    private String bank;

    public ContractAccountModel(ContractAccount contractAccount) {
        this.bankCode = contractAccount.getBankCode();
        this.standardBankCode = contractAccount.getStandardBankCode();
        this.idCardNum = contractAccount.getIdCardNum();
        this.payAcNo = contractAccount.getPayAcNo();
        this.payerName = contractAccount.getPayerName();
        this.provinceCode = contractAccount.getProvinceCode();
        this.cityCode = contractAccount.getCityCode();
        this.bank = contractAccount.getBank();
    }

    public ContractAccountModel(DeductCommandModel commandModel) {
        this.bankCode = commandModel.getBankCode();
        this.standardBankCode = commandModel.getBankCode();
        this.idCardNum = commandModel.getIdCardNo();
        this.payAcNo = commandModel.getAccountNo();
        this.payerName = commandModel.getAccountName();
        this.provinceCode = commandModel.getBankProvince();
        this.cityCode = commandModel.getBankCity();
        this.bank = commandModel.getBankName();
    }
    
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getStandardBankCode() {
        return standardBankCode;
    }

    public void setStandardBankCode(String standardBankCode) {
        this.standardBankCode = standardBankCode;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getPayAcNo() {
        return payAcNo;
    }

    public void setPayAcNo(String payAcNo) {
        this.payAcNo = payAcNo;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
