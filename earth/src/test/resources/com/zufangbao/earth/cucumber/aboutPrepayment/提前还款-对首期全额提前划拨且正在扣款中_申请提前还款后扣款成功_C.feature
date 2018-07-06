



@PrepaymentAtoE
Feature: 对首期做全额提前划拨并对后面未到期的还款计划做提前还款申请然后首期扣款成功

Scenario Outline: 对首期做全额提前划拨并对后面未到期的还款计划做提前还款申请然后首期扣款成功
Given 有三期未到期的还款计划C
And 对第一期未到期的还款计划做全额提前划拨且正在扣款中C
When 对后面未到期的还款计划做提前还款申请C
Then 当首期提前划扣成功以后查询提前还款计划的状态为"<activeStatus>"C

Examples:
        |activeStatus|
        |AssetSetActiveStatus.OPEN|