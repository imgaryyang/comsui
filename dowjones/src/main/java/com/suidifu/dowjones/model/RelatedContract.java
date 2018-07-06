package com.suidifu.dowjones.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午7:22 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class RelatedContract implements Serializable {
    private long id;
    private String contractNo;
    //贷款利率
    private BigDecimal interestRate;
    //生效日期
    private Date beginDate;
    //截止日期
    private Date endDate;
    //期数
    private int periods;
    //还款周期
    private int paymentFrequency;
    //客户姓名
    private String customerName;
    //贷款总额
    private BigDecimal totalAmount;
    //合同状态
    private int state;
    private String financialContractName;
    private String financialContractUuid;
    ////账户uuid
    //private String virtualAccountUuid;
    ////账户编号
    //private String virtualAccountNo;
    //资产方客户编号
    private String customerNo;
}