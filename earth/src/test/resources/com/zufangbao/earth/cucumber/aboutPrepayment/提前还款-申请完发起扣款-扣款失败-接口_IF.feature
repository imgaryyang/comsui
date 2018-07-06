@PrepaymentOtoQ
Feature: 提前还款，调用接口扣款，TODAY=02-07若有三期还款计划分别为A:02-07，B:03-07，C:04-07；A已发起正常扣款，且在本端处理中

  Scenario Outline: 提前还款，调用接口扣款，TODAY=02-07若有三期还款计划分别为A:02-07，B:03-07，C:04-07；且在本端处理中
    Given 申请完立即扣款_IF
    When  扣款失败_IF
    And   D还款计划回滚_IF
    Then  查询到ABC还款计划_IF

    Examples: 
      |  |
      |  |
