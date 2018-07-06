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
public class ABS_ContractChangeExcel {

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

    @ExcelVoAttribute(name = "是否变更还款日",column = "G")
    private String isChangeRecycleDate;

    @ExcelVoAttribute(name = "变更后第一个还款日",column = "H")
    private String changeRecycleDate;

    @ExcelVoAttribute(name = "变更还款日申请时间",column = "I")
    private String changeRecycleDateModifyDate;

    @ExcelVoAttribute(name = "是否退货",column = "J")
    private String isReturn;

    @ExcelVoAttribute(name = "实际退货日期",column = "K")
    private String returnDate;

    @ExcelVoAttribute(name = "退货资金实际到账日期",column = "L")
    private String returnActualRecycleDate;

    @ExcelVoAttribute(name = "退货申请时间",column = "M")
    private String returnDateOfApplication ;

    @ExcelVoAttribute(name = "是否提前还款",column = "N")
    private String isPrepayment;

    @ExcelVoAttribute(name = "是否犹豫期内提前还款",column = "O")
    private String isHesitationPrepayment;

    @ExcelVoAttribute(name = "提前还款日",column = "P")
    private String prepaymentDate ;

    @ExcelVoAttribute(name = "提前还款申请时间",column = "Q")
    private String prepaymentDateOfApplication;

    @ExcelVoAttribute(name = "是否延期还款",column = "R")
    private String isDelayRepayment;

    @ExcelVoAttribute(name = "延期后第一个还款日",column = "S")
    private String repaymentDateOfDelay ;

    @ExcelVoAttribute(name = "延期还款后合同结束日",column = "T")
    private String delayRepaymentContractEndDate;

    @ExcelVoAttribute(name = "延期还款申请时间",column = "U")
    private String delayRepaymentOfApplication;

    @ExcelVoAttribute(name = "是否其他费用变更",column = "V")
    private String isOtherFeeChange ;

    @ExcelVoAttribute(name = "其他费用变更申请日期",column = "W")
    private String OtherFeeChangeOfApplication;

    @ExcelVoAttribute(name = "是否强制取消分期",column = "X")
    private String isForcedToCancelTheStaging;

    @ExcelVoAttribute(name = "是否为90天逾期强制取消",column = "Y")
    private String isOverdueMandatoryCancellation;

    @ExcelVoAttribute(name = "强制取消时间",column = "Z")
    private String forcedToCancelTheTime;

    @ExcelVoAttribute(name = "是否有存入预存款",column = "AA")
    private String isSavePreDeposit;

    @ExcelVoAttribute(name = "存入金额",column = "AB")
    private String saveAmount ;

    @ExcelVoAttribute(name = "存入时间",column = "AC")
    private String saveTime;
}
