package com.suidifu.bridgewater.handler;

import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductApplicationDetailInfoModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/9/18.
 */
public interface DeductApplicationDetailHandler {

    public void generateByRepaymentDetailList(DeductRequestModel deductRequestModel, DeductApplication deductApplication);

    public void updateDeductApplicationDetailByAsseSet(Map<String, DeductApplicationDetailInfoModel> deductApplicationDetailInfoMap);

}
