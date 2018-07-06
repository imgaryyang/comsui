package com.suidifu.owlman.microservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 还款计划变更模型
 *
 * @author zhanghongbing
 */
@Data
@RequiredOnlyOneFieldNotEmpty(message = "请选填其中一种编号[uniqueId，contractNo]")
public class ModifyRepaymentPlan {
    @NotEmpty(message = "请求唯一编号[requestNo]不能为空")
    @NotNull(message = "请求唯一编号[requestNo]不能为null")
    @ApiModelProperty(value = "请求唯一编号", required = true)
    private String requestNo;

    @ApiModelProperty(value = "贷款合同唯一编号uniqueId与contractNo选填")
    private String uniqueId;

    @ApiModelProperty(value = "贷款合同编号(uniqueId与contractNo选填)")
    private String contractNo;

    @NotEmpty(message = "请求原因[requestReason]不能为空")
    @NotNull(message = "请求原因[requestReason]不能为null")
    @Min(value = 0, message = "请求原因[requestReason]内容不合法")
    @Max(value = 10, message = "请求原因[requestReason]内容不合法")
    @ApiModelProperty(value = "请求原因", required = true, allowableValues = "1,2,3,10")
    private String requestReason;

    @NotEmpty(message = "具体变更内容[requestData]不能为空")
    @NotNull(message = "具体变更内容[requestData]不能为null")
    @ApiModelProperty(value = "具体变更内容", required = true)
    private String requestData;

    @Min(0)
    @Max(1)
    @ApiModelProperty(value = "还款计划变更类型", allowableValues = "0,1")
    private Integer type;

    /**
     * 贷前要求 信托产品代码 改为 选填
     */
//    @NotEmpty(message = "信托产品代码[financialProductCode]不能为空")
//    @NotNull(message = "信托产品代码[financialProductCode]不能为null")
    @ApiModelProperty(value = "信托产品代码", required = true)
    private String financialProductCode;

    @Valid
    @JSONField(serialize = false)
    public List<RequestData> getRequestDataList() {
        return JsonUtils.parseArray(getRequestData(), RequestData.class);
    }

    public Integer getRepaymentPlanModifyReasonCode() {
        RepaymentPlanModifyReason modifyReasonEnum = getRepaymentPlanModifyReasonEnum();
        if (modifyReasonEnum == null) {
            return null;
        }
        return modifyReasonEnum.getOrdinal();
    }

    private RepaymentPlanModifyReason getRepaymentPlanModifyReasonEnum() {
        try {
            Integer reasonValue = Integer.valueOf(this.requestReason);
            RepaymentPlanModifyReason modifyReasonEnum = EnumUtil
                    .fromOrdinal(RepaymentPlanModifyReason.class, reasonValue);
            return modifyReasonEnum;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}