package com.suidifu.owlman.microservice.handler;

import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.ledgerbook.InvalidWriteOffException;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.exception.DuplicatedDeductionException;
import java.util.List;

/**
 * 微服务处理-变更还款计划
 *
 * @author louguanyang at 2018/3/13 17:15
 * @mail louguanyang@hzsuidifu.com
 */
public interface ModifyRepaymentPlanMicroServiceHandler {

    List<RepaymentPlanModifyDetail> modifyRepaymentPlan(List<RepaymentPlanModifyRequestDataModel> requestDataList,
        String contractUuid, int priority, Integer oldActiveVersionNo,
        Integer modifyReasonCode, String ip)
        throws GlobalRuntimeException, DuplicatedDeductionException, InvalidWriteOffException;
}
