package com.suidifu.dowjones.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 下午6:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class QueryConditionValue implements Serializable {
    private String companyUuid;
    private String ledgerBookNo;
    private String financialContractUuid;
}