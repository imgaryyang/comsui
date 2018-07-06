package com.suidifu.jpmorgan.constant;

import com.suidifu.jpmorgan.factory.PaymentHandlerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dafuchen
 *         2018/1/26
 */
public class JudgeStatusWhileSendingPayRequestGateWaySet {
    public static Set<String> judgeStatusWhileSendingPayRequestGateWaySet;
    static {
        judgeStatusWhileSendingPayRequestGateWaySet = new HashSet<>();
        judgeStatusWhileSendingPayRequestGateWaySet.add(PaymentHandlerFactory.GATEWAY_TYPE_ElECPAY_TONGLIAN);
        judgeStatusWhileSendingPayRequestGateWaySet.add(PaymentHandlerFactory.GATEWAY_TYPE_DIRECT_BANK_WFCCB);
    }
}
