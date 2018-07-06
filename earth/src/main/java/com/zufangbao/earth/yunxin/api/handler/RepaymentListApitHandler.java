package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.RepaymentListDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentListQueryModel;

import java.util.List;

public interface RepaymentListApitHandler {

    List<RepaymentListDetail> queryRepaymentList(RepaymentListQueryModel queryModel);

}
