@PrepaymentN
Feature: 对首期已逾期的贷款合同进行提前还款申请

Scenario Outline: 对首期已逾期的贷款合同进行提前还款申请
Given 有三期还款计划且首期已逾期N
When 对此贷款合同下所有的还款计划进行提前还款申请N
And 对此贷款合同下所有未到期的还款计划进行提前还款申请N
Then 返回结果"<result>"并且查询此提前还款计划的状态为"<activeStatus>"N

Examples:
         |result                                   |activeStatus|
         |{code:21012,message:提前还款总金额或本金错误!}|AssetSetActiveStatus.FROZEN |
