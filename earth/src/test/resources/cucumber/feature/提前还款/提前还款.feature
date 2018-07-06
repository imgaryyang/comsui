@tag
Feature: 对所有未到期的还款计划申请一期提前还款

@tag2
Scenario Outline: 对所有未到期的还款计划申请一期提前还款
Given 存在一期未到期的还款计划
When 根据贷款合同唯一编号"<uniqueId>"、申请的日期"<assetRecycleDate>"、未还总金额"<assetInitialValue>"、未还本金"<assetPricipal>"来申请一期提前还款
Then 至少有"<repaymentCount>"个还款计划合并成1个提前还款计划，返回信息"<result>"

Examples:
    | uniqueId  |assetRecycleDate | assetInitialValue | assetPricipal | repaymentCount | prePaymentCount |  result | 
    | TEST200   | 2017-02-20      | 1000					    |	1000					|	2			 				 | 1							 |  {"code":0,"message":"成功!"} |
    | TEST200   | 2017-02-20      | 1000					    |	1000					|	2			 				 | 1							 |  {"code":21011,"message":"存在未执行的提前还款!"} |
    
 
