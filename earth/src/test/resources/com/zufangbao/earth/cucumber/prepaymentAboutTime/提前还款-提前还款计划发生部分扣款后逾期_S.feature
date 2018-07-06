
@PrepaymentS
Feature: 提前还款计划发生过部分扣款后逾期

Scenario Outline: 提前还款计划发生过部分扣款后逾期
Given 有三期未到期的还款计划S
When 对此贷款合同下所有未到期的还款计划进行提前还款申请S
And 对此提前还款计划进行部分扣S
Then 第二天查询此提前还款计划的状态为"<activeStatus>",执行状态为"<executingStatus>"S

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|