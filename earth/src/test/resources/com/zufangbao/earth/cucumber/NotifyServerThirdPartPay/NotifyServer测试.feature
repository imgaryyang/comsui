
@Prepayment1
Feature: 从宝付获取流水记录，制造第三方凭证，验证是否能核销

Scenario Outline: 从宝付获取流水记录，制造第三方凭证，验证是否能核销
Given 根据交易请求号从宝付网关获取交易流水
  When 根据交易流水记录制造第三方凭证
  Then 校验应该自动进行,并且都能核销

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|