


@PrepaymentFtoJ
Feature: 对首期做部分提前划拨成功后对后面未到期的还款计划做提前还款

Scenario Outline: 对首期做部分提前划拨成功后对后面未到期的还款计划做提前还款
Given 有三期未到期的还款计划G
When 对第一期未到期的还款计划做部分提前划拨且扣款成功G
And 然后对后面的还款计划做提前还款申请且申请日在首期之前G
Then 返回结果"<result>"G

Examples:
         |result |
         |{code:21014,message:提前还款日期错误!}|
