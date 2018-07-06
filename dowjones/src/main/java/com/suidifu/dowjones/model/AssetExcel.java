package com.suidifu.dowjones.model;

import com.suidifu.dowjones.utils.ExcelVoAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zxj on 2018/1/18.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetExcel {

    @ExcelVoAttribute(name = "合同号", column = "A")
    private String contractUniqueId;

    @ExcelVoAttribute(name = "客户名称", column = "B")
    private String customerName;

    @ExcelVoAttribute(name = "身份证号", column = "C")
    private String customerAccount;

    @ExcelVoAttribute(name = "手机号码", column = "D")
    private String customerMobile;

    @ExcelVoAttribute(name = "客户年龄", column = "E")
    private String customerAge;

    @ExcelVoAttribute(name = "客户性别", column = "F")
    private String customerSex;

    @ExcelVoAttribute(name = "客户婚姻状态", column = "G")
    private String customerMarriage;

    @ExcelVoAttribute(name = "客户所在省", column = "H")
    private String province;

    @ExcelVoAttribute(name = "客户所在市", column = "I")
    private String city;

    @ExcelVoAttribute(name = "借据状态", column = "J")
    private String iouStatus;

    @ExcelVoAttribute(name = "总期数", column = "K")
    private String totalPeriods;

    @ExcelVoAttribute(name = "借据应收本金（元）", column = "L")
    private String principalValue;

    @ExcelVoAttribute(name = "借据应收利息（元）", column = "M")
    private String interestValue;

    @ExcelVoAttribute(name = "借据应收费用（元）", column = "N")
    private String chargeAmount;

    @ExcelVoAttribute(name = "贷款年利率（%）", column = "O")
    private String interestRate;

    @ExcelVoAttribute(name = "还款方式", column = "P")
    private String repaymentWay;

    @ExcelVoAttribute(name = "手续费率", column = "Q")
    private String formalityRate;

    @ExcelVoAttribute(name = "手续费", column = "R")
    private String formality;

    @ExcelVoAttribute(name = "手续费扣款方式", column = "S")
    private String formalityType;

    @ExcelVoAttribute(name = "放款日期", column = "T")
    private String loanDate;

    @ExcelVoAttribute(name = "合同开始日", column = "U")
    private String beginDate;

    @ExcelVoAttribute(name = "合同结束日", column = "V")
    private String endDate;

    @ExcelVoAttribute(name = "还款日", column = "W")
    private String paymentDay;

    @ExcelVoAttribute(name = "首期应还款日", column = "X")
    private String firstVersionPaymentDate;

    @ExcelVoAttribute(name = "剩余本金", column = "Y")
    private String lastPrincipalValue;

    @ExcelVoAttribute(name = "剩余利息", column = "Z")
    private String lastInterestValue;

    @ExcelVoAttribute(name = "剩余费用", column = "AA")
    private String lastChargeAmount;

    @ExcelVoAttribute(name = "下一期应还款日", column = "AB")
    private String nextAssetRecycleDate;

    @ExcelVoAttribute(name = "已还期数", column = "AC")
    private String clearPeriod;

    @ExcelVoAttribute(name = "剩余期数", column = "AD")
    private String unclearPeriod;

    @ExcelVoAttribute(name = "历史累计逾期天数", column = "AE")
    private String totalOverdue;

    @ExcelVoAttribute(name = "当前逾期天数", column = "AF")
    private String currentOverdue;

}
