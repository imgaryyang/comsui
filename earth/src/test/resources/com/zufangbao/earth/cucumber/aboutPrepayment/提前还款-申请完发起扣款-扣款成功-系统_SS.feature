@PrepaymentKtoM
Feature: 提前还款，调用系统扣款，TODAY=02-07若有三期还款计划分别为A:02-07，B:03-07，C:04-07；且在本端处理中

  Scenario Outline: 提前还款，调用系统扣款，TODAY=02-07若有三期还款计划分别为A:02-07，B:03-07，C:04-07；且在本端处理中
    Given 申请完发起扣款_SS
    When  扣款成功_SS
    And   D不回滚_SS
    Then  查询到D还款计划完成_SS

    Examples: 
      |  |
      |  |
