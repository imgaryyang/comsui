
@Prepayment9
Feature: 校验第三方凭证中包含提前还款计划和逾期还款计划是否能执行

Scenario Outline: 校验第三方凭证中包含提前还款计划和逾期还款计划是否能执行
Given 有一个放款计划，对应有三期还款计划nine
  And 有一期逾期，有一期还款计划申请提前还款,还款人去第三方机构还款，并且产生一个第三方凭证nine
  When 获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验nine
Then 校验应该自动进行，检验是否能执行nine

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|