
@PrepaymentFtoJ
Feature: 首期正在扣款然后对该贷款合同内未到期的还款计划进行提前还款申请且申请成功后首期扣款失败

Scenario Outline: 首期正在扣款然后对该贷款合同内未到期的还款计划进行提前还款申请且申请成功后首期扣款失败
Given 有三期还款计划且首期已到期J
When 对第一期还款计划进行扣款J
And 对后面未到期的还款计划做提前还款申请J
Then 返回结果"<result>"并且查询出首期还款失败后提前还款计划状态为"<activeStatus>"J

Examples:
         |result                     |      activeStatus          |
         |{code:21012,message:成功!}  | AssetSetActiveStatus.FROZEN  |