

Feature: 提前还款逾期后立即回滚

Scenario Outline: 提前还款逾期后立即回滚
Given 有一期已到期的提前还款
When 当这笔提前还款逾期
Then 提前还款计划的还款状态"<activeStatus>"为作废
Examples:
    | activeStatus |
    | 1						 |
