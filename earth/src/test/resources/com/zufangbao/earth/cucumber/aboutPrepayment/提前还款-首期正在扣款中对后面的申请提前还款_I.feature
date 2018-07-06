
@PrepaymentFtoJ
Feature: 首期正在扣款然后对该贷款合同进行提前还款

Scenario Outline: 首期正在扣款然后对该贷款合同进行提前还款
Given 有三期还款计划且首期已到期I
When 对第一期还款计划进行扣款I
And 然后对所有的还款计划做提前还款申请I
And 对后面未到期的还款计划做提前还款申请I
Then 返回结果"<result>"并且查询出这条贷款合同下有"<repaymentPlanCount>"条还款计划和首期还款成功后提前还款计划状态为"<activeStatus>"I

Examples:
         |result                                       |repaymentPlanCount                            |      activeStatus          |
         |{code:21012,message:提前还款总金额或本金错误!}    |2                                             | AssetSetActiveStatus.OPEN  |