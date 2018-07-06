package com.suidifu.morganstanley.handler;

import com.suidifu.morganstanley.model.request.MutableFee;

/**
 * Created by hwr on 17-10-23.
 */
public interface RepaymentPlanHandlerV2{
    void mutableFee(MutableFee mutableFee, String ip, Long userId, int priority);
}
