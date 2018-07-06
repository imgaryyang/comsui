package com.suidifu.owlman.microservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.RepeatedCurrentPeriod;
import com.suidifu.owlman.microservice.annotation.RepeatedRepayScheduleNo;
import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 第三方支付凭证指令模型
 *
 * @author louguanyang
 */
@Data
@ApiModel(value = "第三方付款凭证指令")
@RepeatedRepayScheduleNo(message = "商户还款计划编号[repayScheduleNo]不可重复")
@RepeatedCurrentPeriod(message = "存在相同[currentPeriod]期数")
@RequiredOnlyOneFieldNotEmpty(message = "[repaymentPlanNo],[repayScheduleNo],[currentPeriod]至少一个不为空")
public class ThirdPartVoucher {
    /**
     * 请求唯一标识（必填）
     */
    @NotEmpty(message = "请求唯一标识[requestNo]不能为空")
    @ApiModelProperty(value = "请求唯一标识", required = true)
    private String requestNo;

    /**
     * 信托产品代码（必填）
     */
    @NotEmpty(message = "信托产品代码[financialContractNo]不能为空")
    @ApiModelProperty(value = "信托产品代码", required = true)
    private String financialContractNo;

    /**
     * 第三方凭证明细(必填)
     */
    @Valid
    @NotEmpty(message = "第三方凭证明细[detailList]不能为空")
    @ApiModelProperty(value = "第三方凭证明细", required = true)
    private String detailList;

    private String batchUuid = UUID.randomUUID().toString();

    @Valid
    @JSONField(serialize = false)
    public List<ThirdPartVoucherDetail> getDetailListParseJson() {
        List<ThirdPartVoucherDetail> detailListParseJson = JsonUtils.parseArray(this.detailList, ThirdPartVoucherDetail.class);

        if (CollectionUtils.isEmpty(detailListParseJson)) {
            return Collections.emptyList();
        }
        return detailListParseJson;
    }
}