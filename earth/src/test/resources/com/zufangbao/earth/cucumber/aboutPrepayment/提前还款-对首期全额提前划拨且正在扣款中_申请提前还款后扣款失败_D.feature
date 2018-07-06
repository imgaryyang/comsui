

@PrepaymentAtoE
Feature: 对首期做全额提前划拨并对后面未到期的还款计划做提前还款申请然后首期扣款失败

Scenario Outline: 对首期做全额提前划拨并对后面未到期的还款计划做提前还款申请然后首期扣款失败
Given 有三期未到期的还款计划D
And 对第一期未到期的还款计划做全额提前划拨且正在扣款中D
When 对后面未到期的还款计划做提前还款申请D
Then 当首期提前划扣失败以后查询提前还款计划的状态为"<activeStatus>"D

Examples:
        |activeStatus|
        |AssetSetActiveStatus.INVALID|