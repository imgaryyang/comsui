@PrepaymentOtoQ
Feature: 当天存在一期正在扣款中和一期处理中的还款计划，对此合同申请提前还款并且申请日在当天

  Scenario Outline: 当天存在一期正在扣款中和一期处理中的还款计划，对此合同申请提前还款并且申请日在当天
    Given 有三期还款计划，一期正在扣款中，一期在处理中，一期未到期U
    When 对此贷款合同下未到期的还款计划进行提前还款申请且申请日在当天U
    And 对此贷款合同进行正常的提前还款申请且申请日在当天U
    Then 返回结果"<result1>"，"<result2>"并且查询此贷款合同下一共有"<repaymentPlanCount>"条还款计划U

    Examples:
      |result1                                   |result2                   |repaymentPlanCount|
      |{code:21012,message:提前还款总金额或本金错误!}|{code:21012,message:成功!} |2                 |