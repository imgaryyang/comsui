package com.suidifu.dowjones.model;


import com.suidifu.dowjones.utils.ExcelVoAttribute;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentExcel implements Serializable{

    @ExcelVoAttribute(name = "合同号", column = "A")
    private String contractUniqueId;

    @ExcelVoAttribute(name = "期次", column = "B")
    private String currentPeriod;

    @ExcelVoAttribute(name = "应还款日", column = "C")
    private String assetRecycleDate;

    @ExcelVoAttribute(name = "应还本金（元）", column = "D")
    private String assetPrincipalValue;

    @ExcelVoAttribute(name = "应还利息（元）", column = "E")
    private String assetInterestValue;

    @ExcelVoAttribute(name = "应还费用（元）", column = "F")
    private String chargeAmount;

    @ExcelVoAttribute(name = "实还本金（元）", column = "G")
    private String loanAssetPrincipal;

    @ExcelVoAttribute(name = "实还利息（元）", column = "H")
    private String loanAssetInterest;

    @ExcelVoAttribute(name = "实还费用（元）", column = "I")
    private String loanAssetAmount;

    @ExcelVoAttribute(name = "实际还清日期", column = "J")
    private String actualRecycleDate;

    @ExcelVoAttribute(name = "实际还款日", column = "K")
    private String tradeTime;

    @ExcelVoAttribute(name = "资金类型", column = "L")
    private String capitalType;

}
