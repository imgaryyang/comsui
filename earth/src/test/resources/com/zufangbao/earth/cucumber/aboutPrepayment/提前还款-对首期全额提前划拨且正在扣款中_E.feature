
@PrepaymentAtoE
Feature: 对首期做全额提前划拨后对后面未到期的还款计划做提前还款

Scenario Outline: 提对首期做全额提前划拨后对后面未到期的还款计划做前还款
Given 有三期未到期的还款计划E
When 对第一期未到期的还款计划做全额提前划拨E
And 当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之后E
Then 这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划E

Examples:
         |repaymentPlanCount |
         | 2			      |
         
         
         