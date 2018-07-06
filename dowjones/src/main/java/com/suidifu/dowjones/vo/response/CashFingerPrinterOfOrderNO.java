package com.suidifu.dowjones.vo.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 15:29 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class CashFingerPrinterOfOrderNO {
    private String orderNo;
    private BigDecimal totalAmount;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal loanServiceFee;
    private BigDecimal loanTechFee;
    private BigDecimal loanOtherFee;
    private BigDecimal punishment;
    private BigDecimal overdueFee;
    private BigDecimal overdueServiceFee;
    private BigDecimal overdueOtherFee;
}