package com.zufangbao.earth.yunxin.handler.remittance;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBillQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBillShowModel;

import java.util.List;

public interface RemittanceRefundBillHandler {

    List<RemittanceRefundBillShowModel> queryShowModelList(RemittanceRefundBillQueryModel queryModel, Page page);

}
