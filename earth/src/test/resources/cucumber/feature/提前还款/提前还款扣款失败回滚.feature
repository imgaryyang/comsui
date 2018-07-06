

Feature: 提前还款扣款失败后立即回滚

Scenario Outline: 提前还款扣款失败后立即回滚
Given 有一期已到期的提前还款
When 对这一期提前还款进行扣款且扣款失败
Then 提前还款计划的还款状态"<activeStatus>"为作废
Examples:
    | activeStatus |
    | 1						 |
