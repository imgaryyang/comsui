
@Prepayment1
Feature: 校验新增放款回调的补充机制

Scenario Outline: 校验新增放款回调的补充机制
  Given 有一个放款计划,云信使用接口进行放款
  When jiang fang kuan xin xi tong guo yun xin jie kou chuan di
  Then 返回记录

Examples:
         |activeStatus              |executingStatus|
         |AssetSetActiveStatus.OPEN |ExecutingStatus.PROCESSING|