package com.suidifu.morganstanley.model.request.repayment;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.validation.AmountCheck;
import com.zufangbao.sun.utils.validation.DateCheck;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 逾期费用修改Model
 *
 * @author louguanyang
 */
@Data
@NoArgsConstructor
public class OverdueFeeDetail {
    /**
     * 贷款合同唯一标识
     */
    private String contractUniqueId = StringUtils.EMPTY;

    /**
     * 五维还款计划编号
     */
    private String repaymentPlanNo = StringUtils.EMPTY;

    /**
     * 逾期罚息计算日
     */
    @DateCheck(message = "逾期罚息计算日[overDueFeeCalcDate]格式错误,日期格式[yyyy-MM-dd]")
    private String overDueFeeCalcDate = StringUtils.EMPTY;

    @AmountCheck
    private String penaltyFee = BigDecimal.ZERO.toString();

    @AmountCheck
    private String latePenalty = BigDecimal.ZERO.toString();

    @AmountCheck
    private String lateFee = BigDecimal.ZERO.toString();

    @AmountCheck
    private String lateOtherCost = BigDecimal.ZERO.toString();

//    @DecimalMin("0.01")
    @AmountCheck
    private String totalOverdueFee = BigDecimal.ZERO.toString();

    /**
     * 商户还款计划编号
     */
    private String repayScheduleNo = StringUtils.EMPTY;

    /**
     * 贷款合同编号
     */
    private String contractNo = StringUtils.EMPTY;

    /**
     * 还款计划期号
     */
    private Integer currentPeriod = BigDecimal.ZERO.intValue();

    /**
     * 信托产品代码
     * 贷前要求 信托产品代码 改为 选填
     */
//    @NotEmpty(message = "信托产品代码[financialProductCode]不能为空")
    private String financialProductCode = StringUtils.EMPTY;

    @JSONField(serialize = false)
    BigDecimal calcTotalAmount() {
        try {
            return new BigDecimal(this.penaltyFee).add(new BigDecimal(this.latePenalty)).add(new BigDecimal(this.lateOtherCost)).add(new BigDecimal(this.lateFee));
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @JSONField(serialize = false)
    public ModifyOverdueParams convertParameter(String contractUuid, String repaymentPlanUuid) {
        return new ModifyOverdueParams(contractUuid, repaymentPlanUuid, this.overDueFeeCalcDate,
                new BigDecimal(this.penaltyFee), new BigDecimal(this.latePenalty),
                new BigDecimal(this.lateFee), new BigDecimal(this.lateOtherCost), getRepaymentKeyNo());
    }

    /**
     * @return 逾期罚息计算日
     */
    @JSONField(serialize = false)
    public Date getCalcDate() {
        return DateUtils.asDay(getOverDueFeeCalcDate());
    }

    /**
     * @return 优先返回商户还款计划编号, 其次五维商户还款计划编号
     */
    @JSONField(serialize = false)
    private String getRepaymentKeyNo() {
        if (StringUtils.isNotEmpty(this.repayScheduleNo)) {
            return repayScheduleNo;
        }
        return repaymentPlanNo;
    }

}
