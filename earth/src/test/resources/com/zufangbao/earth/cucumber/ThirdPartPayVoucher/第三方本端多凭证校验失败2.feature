
@Prepayment2
Feature: 校验第三方本端多凭证if messsage error,shifouhuibaocuo

Scenario Outline: 校验第三方本端多凭证if messsage error,shifouhuibaocuo
  Given 有一个放款计划，对应有三期还款计划two
  And 有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证two
  When 获取xinxibuduide第三方交易机构的交易记录,第三方凭证和交易记录进行校验two
Then 校验应该自动进行，并且返回resulttwo

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|