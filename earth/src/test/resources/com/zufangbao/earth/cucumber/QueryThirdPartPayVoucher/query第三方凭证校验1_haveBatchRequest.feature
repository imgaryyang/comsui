
@Prepayment1
Feature: query第三方凭证haveBatchRequest

Scenario Outline: query第三方凭证have BatchRequest
Given 有一个放款计划，对应有三期还款计划one
  And 有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证one
  And 获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验one
  When give BatchRequest to query one
Then 校验应该自动进行，并且返回记录one

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|