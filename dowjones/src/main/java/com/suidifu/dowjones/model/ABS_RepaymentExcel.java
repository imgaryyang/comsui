package com.suidifu.dowjones.model;

import com.suidifu.dowjones.utils.ExcelVoAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zxj on 2018/2/7.
 * ABS-实际还款文件
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ABS_RepaymentExcel {

    @ExcelVoAttribute(name = "项目编号",column = "A")
    private String projectId;

    @ExcelVoAttribute(name = "机构编号",column = "B")
    private String agencyId;

    @ExcelVoAttribute(name = "借据号",column = "C")
    private String uniqueId;

    @ExcelVoAttribute(name = "期次",column = "D")
    private String currentPeriod;

    @ExcelVoAttribute(name = "应还款日",column = "E")
    private String assetRecycleDate;

    @ExcelVoAttribute(name = "应还本金(元)",column = "F")
    private String assetPrincipalValue;

    @ExcelVoAttribute(name = "应还利息(元)",column = "G")
    private String assetInterestValue;

    @ExcelVoAttribute(name = "应还费用(元)",column = "H")
    private String assetChangeAmount;

    @ExcelVoAttribute(name = "实还本金(元)",column = "I")
    private String loanAssetPrincipal;

    @ExcelVoAttribute(name = "实还利息(元)",column = "J")
    private String loanAssetInterest;

    @ExcelVoAttribute(name = "实还费用(元)",column = "K")
    private String loanAssetAmount;

    @ExcelVoAttribute(name = "实际还清日期",column = "L")
    private String actualRecycleDate;

    @ExcelVoAttribute(name = "当期贷款余额",column = "M")
    private String currentPeriodBalance;

    @ExcelVoAttribute(name = "当期账号状态",column = "N")
    private String currentPeriodStatus;
}
