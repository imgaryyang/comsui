package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.sun.api.model.repayment.QueryPaymentOrderRequestModel;

import java.util.List;
import java.util.Map;

public interface PaymentOrderApiHandler {

    List<Map<String, Object>> queryPaymentOrderResponeData(QueryPaymentOrderRequestModel commandModel);

}
