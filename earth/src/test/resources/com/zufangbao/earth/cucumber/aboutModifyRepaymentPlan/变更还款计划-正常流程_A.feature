@tag
Feature: 对一个贷款合同中未到期的还款计划做变更

  Scenario Outline: 对一个贷款合同中未到期的还款计划做变更
    Given 有一个贷款合同，包含三期未到期的还款计划A
    When 对此合同中的还款计划做变更且第一期计划日期在当日之前A
    And 对此合同中的还款计划做变更且有一期计划日子在合同终止日之后A
    And 对此合同中的还款计划做变更且本金总额错误A
    And 将此合同中的还款计划变更未两期并且参数都正确A
    Then 返回结果"<result1>""<result2>""<result3>""<result4>"这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划A

    Examples:
      |repaymentPlanCount |result1|
      | 2			      |{code:21009,message:计划还款日期排序错误，需按计划还款日期递增!}|