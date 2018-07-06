
@tag
Feature: 使用ATM固定金额方式取款

@tag1
Scenario Outline: 固定金额取款
Given 我的账户中有余额"<accountBalance>"元
When 我选择固定金额取款方式取出"<withdrawAmount>"元
Then 我应该收到现金"<receivedAmount>"元
And 我的账号余额是"<remainingBalance>"元

Examples:
    | accountBalance  | withdrawAmount | receivedAmount | remainingBalance|
    | 100             |  5     				 | 5              | 95              |
    | 10              |  10            | 10					    | 0               |
    | 10              |  11            | 0					    | 10              |