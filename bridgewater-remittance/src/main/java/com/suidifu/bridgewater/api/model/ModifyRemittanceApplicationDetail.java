package com.suidifu.bridgewater.api.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ModifyRemittanceApplicationDetail implements Comparable<ModifyRemittanceApplicationDetail> {

    // 明细记录号
    private String detailNo;
    // 记录序号
    private String recordSn;
    // 金额
    private String amount;
    // 计划执行日期
    private String plannedDate;
    // 开户行编号
    private String bankCode;
    // 交易方银行卡号
    private String bankCardNo;
    // 交易方银行卡开户人
    private String bankAccountHolder;
    // 交易方银行卡开户行所在省
    private String bankProvince;
    // 交易方银行卡开户行所在市
    private String bankCity;
    // 交易方开户支行名称
    private String bankName;
    // 交易方银行卡开户证件号
    private String idNumber;


    @JSONField(serialize = false)
    public Integer getIntRecordSn() {
        return Integer.parseInt(this.recordSn);
    }


    @Override
    public int compareTo(ModifyRemittanceApplicationDetail o) {
        return this.getIntRecordSn().compareTo(o.getIntRecordSn());
    }
}
