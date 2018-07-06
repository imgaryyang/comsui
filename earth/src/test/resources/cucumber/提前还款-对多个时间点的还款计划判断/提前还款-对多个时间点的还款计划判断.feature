

Feature: 测试多种时间节点下还款计划的变更情况

Scenario Outline: 测试多种时间节点下还款计划的变更情况
Given 有三期还款计划的还款时间<repaymentplanReciveDay>
When 对<repaymentplanReciveDay>进行扣款
And 然后对后面的还款计划做提前还款
Then 这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划

Examples:
         |repaymentplanReciveDay |
         | 02-07			      |
         | 03-07                 |
         | 04-07                 |
