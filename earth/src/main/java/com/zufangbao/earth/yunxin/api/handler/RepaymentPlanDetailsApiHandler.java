package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetailsQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetailsResultModel;

import java.util.Date;

public interface RepaymentPlanDetailsApiHandler {

    RepaymentPlanDetailsResultModel queryRepaymentPlanDetails(RepaymentPlanDetailsQueryModel queryModel, Date queryTime);

}
