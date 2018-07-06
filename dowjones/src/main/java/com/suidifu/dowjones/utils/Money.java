package com.suidifu.dowjones.utils;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午7:19 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class Money {
    private BigDecimal paidPrinciple = BigDecimal.ZERO;
    private BigDecimal paidInterest = BigDecimal.ZERO;
    private BigDecimal notPaidPrinciple = BigDecimal.ZERO;
    private BigDecimal notPaidInterest = BigDecimal.ZERO;
}