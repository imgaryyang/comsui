package com.suidifu.owlman.microservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.zufangbao.sun.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 主动付款凭证明细相关字段
 *
 * @author louguanyang on 2017/2/28.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("主动付款凭证指令详情")
@RequiredOnlyOneFieldNotEmpty(message = "至少选填一种编号[repayScheduleNo，repaymentPlanNo,currentPeriod]")
public class ActivePaymentVoucherDetail extends RepaymentPlanDetailAmount {
    /**
     * 还款计划编号
     */
    private String repaymentPlanNo;

    private String uniqueId;

    private String contractNo;
    /**
     * 商户还款计划编号
     */
    private String repayScheduleNo;
    /**
     * 期数
     */
    private Integer currentPeriod;

    @JSONField(serialize = false)
    public boolean paramsError() {
        if (StringUtils.isEmpty(this.uniqueId) == StringUtils.isEmpty(this.contractNo)) {
            return true;
        }
        return false;
    }

    public boolean isNotEmptyRepaymentPlanNoAndRepayScheduleNoAndrepaymentNumber() {

        if (StringUtils.isEmpty(this.repayScheduleNo) && StringUtils.isEmpty(this.repaymentPlanNo) && (this.currentPeriod == null)) {
            return true;
        }
        return false;
    }

    @JSONField(serialize = false)
    public boolean isValid() {
        return super.isValid();
    }

}
