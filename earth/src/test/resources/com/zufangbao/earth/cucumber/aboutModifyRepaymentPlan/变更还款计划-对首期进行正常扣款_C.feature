@tag

Feature: 对一个贷款合同中的首期还款计划进行正常扣款后做变更还款计划

  Scenario Outline: 对一个贷款合同中的首期还款计划进行正常扣款后做变更还款计划
    Given 有一个贷款合同，包含三期还款计划且首期已到期C
    When 对首期进行部分扣款C
    And 对此合同中的未到期的计划做变更且有一期在当日C
    And 对未到期的计划做正常变更C
    And 首期部分扣款成功后对未到期的计划做变更且有一期在当日C
    And 对首期进行全额扣款，扣款成功后对未到期的计划做变更且有一期在当日C
    Then 返回结果"<result1>""<result2>""<result3>""<result4>"这个贷款合同下可查询出"<repaymentPlanCount>"条还款计划C

    Examples:
      |repaymentPlanCount |result1|
      | 3			      |{code:21004,message:无效的计划本金总额!}|