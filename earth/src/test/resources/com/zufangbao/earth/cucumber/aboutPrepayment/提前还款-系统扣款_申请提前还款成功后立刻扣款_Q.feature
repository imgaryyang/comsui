

@PrepaymentOtoQ
Feature: 申请提前还款在当日成功以后立即扣一次款

Scenario Outline: 申请提前还款在当日成功以后立即扣一次款
Given 有三期未到期的还款计划Q
When 对此贷款合同下所有的还款计划进行提前还款申请且申请日期为当日Q
Then 查看此提前还款计划下结算单状态"<orderSource>"Q

Examples:
         |orderSource |
         |OrderSource.MANUAL|