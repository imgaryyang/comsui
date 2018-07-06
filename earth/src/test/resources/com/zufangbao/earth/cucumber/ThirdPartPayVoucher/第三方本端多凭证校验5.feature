
@Prepayment5
Feature: 校验第三方本端多凭证校验是否能执行,two same request

Scenario Outline: 校验第三方本端多凭证校验是否能执行,two same request
Given 有一个放款计划，对应有三期还款计划5five
  And 有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证,two same requestfive
  When 获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验five
Then 校验应该自动进行，并且返回记录five

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|