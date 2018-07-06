package com.suidifu.microservice.handler;

import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;

/**
 * 提前还款处理
 *
 * @author zhanghongbing
 */
public interface PrepaymentHandler {

    void processing_one_prepayment_plan(String contractUuid, int priority, Long prepaymentApplicationId);

    String invalid_prepayment_and_undo_frozen_be_pred_repayment_plan(PrepaymentApplication application, AssetSet prepaymentPlan);

}
