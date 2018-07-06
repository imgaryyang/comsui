
@PrepaymentAtoE
Feature: 对首期做全额提前划拨成功后对后面未到期的还款计划做提前还款

Scenario Outline: 对首期做全额提前划拨成功后对后面未到期的还款计划做提前还款
Given 有三期未到期的还款计划A
When 对第一期未到期的还款计划做全额提前划拨A
And 然后对后面的还款计划做提前还款A
Then 这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划A

Examples:
         |repaymentPlanCount |
         | 2			      |
