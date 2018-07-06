package com.suidifu.dowjones.vo.request;

import lombok.Data;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/10 <br>
 * @time: 16:37 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class QueryParameter {
    private String financialContractUuid;
    private String ledgerBookNO;
    private String accountNO;
    private String hostAccountNo;
}