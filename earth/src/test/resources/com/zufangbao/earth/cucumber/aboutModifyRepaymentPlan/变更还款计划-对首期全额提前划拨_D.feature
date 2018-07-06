@tag
Feature: 对一个贷款合同中的首期还款计划进行全额提前划拨后做变更还款计划

  Scenario Outline: 对一个贷款合同中的首期还款计划进行全额提前划拨后做变更还款计划
    Given 有一个贷款合同，包含三期未到期的还款计划D
    When 对第一期做全额提前划拨D
    And 在第一期还在扣款中的时候对后面的还款计划做变更且有一期日期在原首期之前D
    And 在第一期还款成功后对后面的还款计划做变更且有一期日期在原首期之前D
    Then 返回结果"<result1>""<result2>"D

    Examples:
      |result2|
      |{code:0,message:成功!}|