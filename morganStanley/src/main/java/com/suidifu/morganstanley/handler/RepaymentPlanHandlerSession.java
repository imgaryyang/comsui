package com.suidifu.morganstanley.handler;

import com.suidifu.morganstanley.model.request.MutableFee;

/**
 * Created by hwr on 17-10-23.
 */
public interface RepaymentPlanHandlerSession {
    void mutableFeeV2(MutableFee mutableFee, String ip, Long userId, int priority);
}
