
@Prepayment7
Feature: 校验第三方本端多凭证校验是否能执行two期还款计划

Scenario Outline: 校验第三方本端多凭证校验是否能执行two期还款计划
Given 有一个放款计划，对应有two期还款计划seven
  And two期还款计划到期了,还款人去第三方机构还款，并且产生two三方凭证seven
  When 获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验seven
Then 校验应该自动进行，并且返回记录seven

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|