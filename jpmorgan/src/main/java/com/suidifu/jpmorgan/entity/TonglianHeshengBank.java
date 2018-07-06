package com.suidifu.jpmorgan.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author dafuchen
 *         2018/1/18
 */
@Entity
@Table(name = "tonglian_hengsheng_bank")
public class TonglianHeshengBank {

    @Id
    @GeneratedValue
    private Long id;
    private String hengshengBankCode;
    private String tonglianBankCode;
    private String bankName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHengshengBankCode() {
        return hengshengBankCode;
    }

    public void setHengshengBankCode(String hengshengBankCode) {
        this.hengshengBankCode = hengshengBankCode;
    }

    public String getTonglianBankCode() {
        return tonglianBankCode;
    }

    public void setTonglianBankCode(String tonglianBankCode) {
        this.tonglianBankCode = tonglianBankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
