@tag
Feature: 对一个贷款合同中未到期的还款计划做两次相同的变更

  Scenario Outline: 对一个贷款合同中未到期的还款计划做两次相同的变更
    Given 有一个贷款合同，包含三期未到期的还款计划F
    When 对此合同中的还款计划做一次变更F
    And 对此合同中的还款计划再做一次变更且有一期信息与第一次完全重复F
    Then 返回结果"<result1>""<result2>"F

    Examples:
      |result1|
      |{code:0,message:成功!}|