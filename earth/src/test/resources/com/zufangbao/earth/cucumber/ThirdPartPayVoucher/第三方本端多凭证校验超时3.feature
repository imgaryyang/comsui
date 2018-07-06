
@Prepayment3
Feature: 校验第三方本端多凭证outtime

Scenario Outline: 校验第三方本端多凭证outtime
Given 有一个放款计划，对应有三期还款计划three
  And 有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证three
  When 第三方交易机构的交易记录outtimethree
Then 凭证应该outtime，并且返回记录three

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|