package com.suidifu.morganstanley.model.request.repayment;

import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 校验变更逾期费用明细
 * @author louguanyang at 2017/10/13 22:25
 */
public class CheckModifyOverdueFee {

    public static void check(@NotNull ModifyOverdueFee details) {
        List<OverdueFeeDetail> overdueFeeDetails = details.getDetailList();
        if (CollectionUtils.isEmpty(overdueFeeDetails)) {
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "具体变更内容[modifyOverDueFeeDetails]格式错误");
        }
        for (OverdueFeeDetail feeDetail : overdueFeeDetails) {
            if (StringUtils.isEmpty(feeDetail.getRepaymentPlanNo()) && StringUtils.isEmpty(feeDetail.getRepayScheduleNo())) {
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "还款计划编号[repaymentPlanNo], 商户还款计划编号[repayScheduleNo]不能都为空");
            }
            Date overDueFeeCalcDate = feeDetail.getCalcDate();
            if (overDueFeeCalcDate == null) {
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "逾期罚息计算日[overDueFeeCalcDate]格式错误, 应为[yyyy-MM-dd]");
            }
            if (Math.abs(DateUtils.compareTwoDatesOnDay(DateUtils.asDay(new Date()), overDueFeeCalcDate)) > 1) {
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "逾期罚息计算日[overDueFeeCalcDate]日期有误");
            }
            if (new BigDecimal(feeDetail.getTotalOverdueFee()).compareTo(feeDetail.calcTotalAmount()) != 0) {
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "修改逾期费用金额明细总额与总金额不相等");
            }
        }
    }
}
