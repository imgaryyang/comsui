@PrepaymentOtoQ
Feature: 当天存在多期已到期并且在处理中的还款计划，申请提前还款在当天

  Scenario Outline: 当天存在多期已到期并且在处理中的还款计划，申请提前还款在当天
    Given 有三期还款计划，前两期已到期并且在处理中T
    When 对此贷款合同下所有的还款计划进行提前还款申请且申请日在当天T
    Then 返回结果"<result>"并且查询此贷款合同下一共有"<repaymentPlanCount>"条还款计划T

    Examples:
      |result                    |repaymentPlanCount|
      |{code:21012,message:成功!} |1                 |