
@Prepayment4
Feature: 校验第三方dui端多凭证校验是否能执行

Scenario Outline: 校验第三方dui端多凭证校验是否能执行
Given 有一个放款计划，对应有三期还款计划four
  And 有一期还款计划到期了,还款人去第三方机构还款，chansheng第三方交易机构的交易记录,four
  When 产生一个第三方凭证,第三方凭证和交易记录进行校验four
Then 校验应该自动进行，并且返回记录four

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|