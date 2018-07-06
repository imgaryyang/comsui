
@PrepaymentKtoM
Feature: 首期部分扣款成功后对该贷款合同进行提前还款

Scenario Outline: 首期部分扣款成功后对该贷款合同进行提前还款
Given 有三期还款计划且首期已到期L
When 对第一期还款计划进行部分扣款L
And 然后对所有的还款计划做提前还款申请L
And 对后面未到期的还款计划做提前还款申请L
Then 返回结果"<result>"并且查询提前还款计划状态为"<activeStatus>"L

Examples:
         |result                                       |      activeStatus          |
         |{code:21012,message:提前还款总金额或本金错误!}    | AssetSetActiveStatus.FROZEN  |