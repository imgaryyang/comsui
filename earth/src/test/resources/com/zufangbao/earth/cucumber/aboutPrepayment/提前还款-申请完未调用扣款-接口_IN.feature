@PrepaymentOtoQ
Feature: 提前还款，调用接口扣款,TODAY=02-07若有三期还款计划分别为A:02-07，B:03-07，C:04-07；A已发起正常扣款，且在本端处理中

  Scenario Outline: 提前还款，调用接口扣款,TODAY=02-07若有三期还款计划分别为A:02-07，B:03-07，C:04-07；A已发起正常扣款,且在本端处理中
    Given 申请提前还款_IN
    When  未调用扣款_IN
    And   D还款计划不回滚_IN
    Then  查询到D还款计划未完成_IN

    Examples: 
      |  |
      |  |
