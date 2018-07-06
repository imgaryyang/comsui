package com.suidifu.owlman.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentOrderLapseParam;
import java.util.List;
import java.util.Map;

/**
 * Created by zhusy on 2017/6/12.
 */
public interface RepaymentOrderCancelHandler {
    Map<String, String> criticalMarker(List<RepaymentOrderLapseParam> repaymentOrderLapseParamList) throws GiottoException;

    boolean deleteRepaymentOrderItem(List<RepaymentOrderLapseParam> repaymentOrderLapseParamList);
}