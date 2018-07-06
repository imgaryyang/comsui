



@PrepaymentFtoJ
Feature: 对首期做部分提前划拨成功后对后面未到期的还款计划做提前还款且申请日在首期之后

Scenario Outline: 对首期做部分提前划拨成功后对后面未到期的还款计划做提前还款且申请日在首期之后
Given 有三期未到期的还款计划H
When 对第一期未到期的还款计划做部分提前划拨且扣款成功H
And 然后对后面的还款计划做提前还款申请且申请日在首期之后H
Then 查询出这条贷款合同下有"<repaymentPlanCount>"条还款计划且提前还款计划状态为"<activeStatus>"H

Examples:
         |repaymentPlanCount                            |      activeStatus          |
         |2                                             | AssetSetActiveStatus.FROZEN|