package com.suidifu.dowjones.model;

import com.suidifu.dowjones.utils.ExcelVoAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *ABS-贷款合同变更文件
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ABS_CurrentContractDetailExcel {
    @ExcelVoAttribute(name = "项目编号",column = "A")
    private String projectId;

    @ExcelVoAttribute(name = "机构编号",column = "B")
    private String agencyId;

    @ExcelVoAttribute(name = "借据号",column = "C")
    private String uniqueId;

    @ExcelVoAttribute(name = "客户姓名",column = "D")
    private String customerName;

    @ExcelVoAttribute(name = "身份证号",column = "E")
    private String idCard;

    @ExcelVoAttribute(name = "手机号",column = "F")
    private String mobile;

    @ExcelVoAttribute(name = "贷款总金额(元)",column = "G")
    private String totalAmount;

    @ExcelVoAttribute(name = "贷款年利率(%)",column = "H")
    private String interestRate;

    @ExcelVoAttribute(name = "还款方式",column = "I")
    private String paymentWay;

    @ExcelVoAttribute(name = "总期数",column = "J")
    private String periods;

    @ExcelVoAttribute(name = "已还期数",column = "K")
    private String loanPeriods;

    @ExcelVoAttribute(name = "剩余期数",column = "L")
    private String lastPeriods;

    @ExcelVoAttribute(name = "剩余本金(元)",column = "M")
    private String lastPrincipalValue;

    @ExcelVoAttribute(name = "剩余利息(元)",column = "N")
    private String lastInterestValue;

    @ExcelVoAttribute(name = "剩余其他费用(元)",column = "O")
    private String lastOtherFee;

    @ExcelVoAttribute(name = "下一期应还款日",column = "P")
    private String nestPaymentDate;

    @ExcelVoAttribute(name = "是否变更还款计划",column = "Q")
    private String isChangeAssetset;

    @ExcelVoAttribute(name = "变更时间",column = "R")
    private String changeTime;

    @ExcelVoAttribute(name = "预存款余额(元)",column = "S")
    private String lastPreSave ;

    @ExcelVoAttribute(name = "首付款金额(元)",column = "T")
    private String downPaymentAmount;

    @ExcelVoAttribute(name = "首付比例(%)",column = "U")
    private String downPaymentRatio;

    @ExcelVoAttribute(name = "月还款额(元)",column = "V")
    private String monthlyRepayments;

    @ExcelVoAttribute(name = "合同开始时间",column = "W")
    private String beginDate;

    @ExcelVoAttribute(name = "合同结束时间",column = "X")
    private String endDate;

    @ExcelVoAttribute(name = "实际放款时间",column = "Y")
    private String actualLoanDate;

    @ExcelVoAttribute(name = "首次还款时间",column = "Z")
    private String firstRepaymentDate;

    @ExcelVoAttribute(name = "当前逾期天数(天)",column = "AA")
    private String currentOverdueDays;

    @ExcelVoAttribute(name = "历史单次最长逾期天数(天)",column = "AB")
    private String maxOverdueDays;

    @ExcelVoAttribute(name = "当天实还本金(元)",column = "AC")
    private String loanPrincipalValue;

    @ExcelVoAttribute(name = "当天实还利息(元)",column = "AD")
    private String loanInterestValue;

    @ExcelVoAttribute(name = "当天实还费用(元)",column = "AE")
    private String loanOtherFee;
}
