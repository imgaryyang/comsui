
@Prepayment1
Feature: 接口扣款销帐顺序：还款计划期数

Scenario Outline: 接口扣款销帐顺序：还款计划期数
  Given 有一个放款计划,对应有三期还款计划
  When 有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证(第三方凭证中有有三期还款计划)
  Then 校验应该自动进行,返回记录

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|