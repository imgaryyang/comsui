@PrepaymentOtoQ
Feature: 当天存在多笔扣款中的还款计划，对此合同申请提前还款并且申请日在当天

  Scenario Outline: 当天存在多笔扣款中的还款计划，对此合同申请提前还款并且申请日在当天
    Given 有三期还款计划，前两期已到期V
    When 对前两期进行扣款V
    And 对此贷款合同下未到期的还款计划进行提前还款申请且申请日在当天V
    Then 返回结果"<result>"并且查询此贷款合同下一共有"<repaymentPlanCount>"条还款计划V

    Examples:
      |result                    |repaymentPlanCount|
      |{code:21012,message:成功!} |3                 |