@tag
Feature: 对一个贷款合同中的还款计划做变更且首期已到期

  Scenario Outline: 对一个贷款合同中的还款计划做变更且首期已到期
    Given 有一个贷款合同，包含三期还款计划且首期已到期B
    When 对此合同中的所有计划做变更B
    And 对此合同中的所有计划做变更且变更后有一期在当日B
    And 对此合同中的未到期的计划做变更B
    Then 返回结果"<result1>""<result2>""<result3>"这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划B

    Examples:
      |repaymentPlanCount |result1|
      | 3			      |{code:21004,message:无效的计划本金总额!}|