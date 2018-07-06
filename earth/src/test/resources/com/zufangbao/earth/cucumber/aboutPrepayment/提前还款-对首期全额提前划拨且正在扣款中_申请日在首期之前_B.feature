


@PrepaymentAtoE
Feature: 对首期做全额提前划拨后对后面未到期的还款计划做提前还款并且申请日期在首期还款计划之前

Scenario Outline: 对首期做全额提前划拨后对后面未到期的还款计划做提前还款并且申请日期在首期还款计划之前
Given 有三期未到期的还款计划B
When 对第一期未到期的还款计划做全额提前划拨B
And 当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之前B
Then 返回结果"<result>"且这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划B

Examples:
          |repaymentPlanCount |
          | 2			      |
          
          
          
          
   
          