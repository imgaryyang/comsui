

@PrepaymentOtoQ
Feature: 提前还款计划扣款失败后回滚

Scenario Outline: 提前还款计划扣款失败后回滚
Given 有三期未到期的还款计划O
When 对此贷款合同下所有的还款计划进行提前还款申请O
And 对此提前还款计划进行扣款且扣款失败O
Then 查询此贷款合同下在提前还款计划扣款前有"<repaymentPlanCountBeforeDeduct>"条还款计划和扣款失败后有"<repaymentPlanCountAfterDeduct>"条还款计划O 

Examples:
         |repaymentPlanCountBeforeDeduct |repaymentPlanCountAfterDeduct|
         |1                              |3                            |