@tag
Feature: TODAY=now，有三期还款计划分别为A:'<2017-02-07>'，B:'<2017-05-06>'，C:'<2017-08-06>'；申请提前还款日期为X='<2017-02-06>'，三期还款计划合并为一期提前还款计划 D:'<2017-02-06>'

@tag2
Scenario Outline: TODAY='<2017-02-06>'，有三期还款计划分别为A:'<2017-02-07>'，B:'<2017-05-06>'，C:'<2017-08-06>'；申请提前还款日期为X='<2017-02-06>'，三期还款计划合并为一期提前还款计划 D:'<2017-02-06>'
Given 为三笔还款计划申请提前还款        
When 根据贷款合同唯一编号"<uniqueId>"、申请的日期"<assetRecycleDate>"、未还总金额"<assetInitialValue>"、未还本金"<assetPricipal>"来申请一期提前还款
Then 至少有"<repaymentCount>"个还款计划合并成一个提前还款计划，返回信息"result"

When 对合同编号为"<>"的还款计划进行扣款
Then 返回信息 "<>"   
Examples:
    | uniqueId  |assetRecycleDate | assetInitialValue | assetPricipal | repaymentCount | prePaymentCount |  result | 
    | TEST200   | 2017-02-06      | 1000				|1000		|	3				| 1				 |{"code":0,"message":"成功!"} |
