package com.suidifu.microservice.handler;

import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;

/**
 * 提前还款处理
 *
 * @author zhanghongbing
 */
public interface PrepaymentHandler {
    void processingOnePrepaymentPlan(String contractUuid, int priority, Long prepaymentApplicationId);

    String invalidPrepaymentAndUndoFrozenBePreRepaymentPlan(PrepaymentApplication application, AssetSet prepaymentPlan);
}
