package com.suidifu.microservice.handler;

/**
 * 提前还款处理
 *
 * @author zhanghongbing
 */
public interface PrepaymentHandler {

    void processing_one_prepayment_plan(String contractUuid, int priority,
        Long prepaymentApplicationId);

}
