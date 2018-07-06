@PrepaymentR
Feature: 提前还款计划未发生过扣款且逾期

Scenario Outline: 提前还款计划未发生过扣款且逾期
Given 有三期未到期的还款计划R
When 对此贷款合同下所有未到期的还款计划进行提前还款申请R
Then 第二天查询此提前还款计划的状态为"<activeStatus>"R

Examples:
         |activeStatus|
         |AssetSetActiveStatus.INVALID |
