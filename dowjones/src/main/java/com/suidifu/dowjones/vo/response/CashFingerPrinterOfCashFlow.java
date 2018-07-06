package com.suidifu.dowjones.vo.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 14:16 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class CashFingerPrinterOfCashFlow {
    private String bankCard;
    private String fingerPrinter;
    private String orderNo;
    private BigDecimal totalAmount;
}