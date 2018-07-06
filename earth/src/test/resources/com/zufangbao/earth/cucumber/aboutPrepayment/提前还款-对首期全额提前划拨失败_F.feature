


@PrepaymentFtoJ
Feature: 对首期做全额提前划拨失败后对后面未到期的还款计划做提前还款

Scenario Outline: 对首期做全额提前划拨失败后对后面未到期的还款计划做提前还款
Given 有三期未到期的还款计划F
And 对第一期未到期的还款计划做全额提前划拨且扣款失败F
When 对所有的还款计划做提前还款F
And 对此提前还款进行扣款且扣款失败F
Then 这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划F



Examples:
      | repaymentPlanCount  |repaymentPlanCount 1|
       | 1    		      |3                     |