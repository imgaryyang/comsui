


@PrepaymentKtoM
Feature: 申请提前还款正常流程中参数错误的情况

Scenario Outline: 申请提前还款正常流程中参数错误的情况
Given 有三期未到期的还款计划M
When 申请提前还款且申请日期在当日之前M
And 申请提前还款且申请日期在第一期之后M
And 申请提前还款且提前还款金额小于未还本金M
And 申请提前还款且提前还款金额大于未还本金*1.24M
And 申请提前还款且提前还款金额与明细总额不相等M
And 申请提前还款且各项参数都正确M
Then 返回结果"<result1>"，"<result2>"，"<result3>"，"<result4>"，"<result5>"，"<result6>"M

Examples:
         |result |
         |{code:21014,message:提前还款日期错误!}|
