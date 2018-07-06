

@PrepaymentOtoQ
Feature: 提前还款计划扣款成功后查询还款计划总条数

Scenario Outline: 提前还款计划扣款成功后查询还款计划总条数
Given 有三期未到期的还款计划P
When 对此贷款合同下所有的还款计划进行提前还款申请P
And 对此提前还款计划进行扣款且扣款成功P
Then 查询此贷款合同下一共有"<repaymentPlanCount>"条还款计划P 

Examples:
         |repaymentPlanCount|
         |1                 |