

@PrepaymentKtoM
Feature: 首期正在部分扣款然后对该贷款合同内未到期的还款计划进行提前还款申请且申请成功后首期部分扣款成功

Scenario Outline: 首期正在部分扣款然后对该贷款合同内未到期的还款计划进行提前还款申请且申请成功后首期部分扣款成功
Given 有三期还款计划且首期已到期K
When 对第一期还款计划进行部分扣款K
And 对后面未到期的还款计划做提前还款申请K
Then 查询出首期部分还款成功后提前还款计划状态为"<activeStatus>"K

Examples:
         |      activeStatus          |
         | AssetSetActiveStatus.FROZEN  |