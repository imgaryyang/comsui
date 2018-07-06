package com.suidifu.owlman.microservice.model;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwr on 17-10-17.
 */
@Data
@ApiModel(value = "浮动费用")
@RequiredOnlyOneFieldNotEmpty(message = "请选填其中一种编号［uniqueId，contractNo］")
public class MutableFee {
    /**
     * 请求唯一编号
     */
    @NotEmpty(message = "请求唯一编号[requestNo]不能为空")
    @NotNull(message = "请求唯一编号[requestNo]不能为null")
    @ApiModelProperty(value = "请求唯一编号", required = true)
    private String requestNo;
    /**
     * 信托产品代码
     */
    @NotEmpty(message = "信托产品代码[financialProductCode]不能为空")
    @NotNull(message = "信托产品代码[financialProductCode]不能为null")
    @ApiModelProperty(value = "信托产品代码", required = true)
    private String financialProductCode;
    /**
     * 贷款合同唯一标识（二选一）
     */
    //@CheckValue
    @ApiModelProperty(value = "贷款合同唯一标识（二选一）")
    private String uniqueId;
    /**
     * 贷款合同编号（二选一）
     */
    //@CheckValue
    @ApiModelProperty(value = "贷款合同编号（二选一）")
    private String contractNo;
    /**
     * 还款计划期数
     */
    @ApiModelProperty(value = "还款计划期数")
    private Integer currentPeriod;
    /**
     * 还款计划编号
     */
    @ApiModelProperty(value = "还款计划编号", required = true)
    private String repaymentPlanNo;
    /**
     * 商户还款计划编号
     */
    @ApiModelProperty(value = "商户还款计划编号", required = true)
    private String repayScheduleNo;
    /**
     * 费用明细jsonList {@link MutableFeeDetail}
     */
    @NotEmpty(message = "费用明细jsonlist不能为空")
    @NotNull(message = "费用明细jsonList不能为null")
    @ApiModelProperty(value = "费用明细jsonList", required = true)
    private String details;
    /**
     * 更新原因 {@link MutableFeeReasonCode}
     */
    @NotEmpty(message = "更新原因[reasonCode]不能为空")
    @NotNull(message = "更新原因[reasonCode]不能为null")
    @Pattern(regexp = "[0-2]", message = "更新原因[reasonCode]内容不合法")
    @ApiModelProperty(value = "更新原因", required = true, allowableValues = "0,1,2")
    private String reasonCode;
    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String approver;
    /**
     * 审核时间（date time）
     */
    @ApiModelProperty(value = "审核时间")
    private String approvedTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;
    /**
     * 校验失败信息
     */
    @ApiModelProperty(hidden = true)
    private String checkFailedMsg;

    public List<MutableFeeDetail> getDetailList() {
        List<MutableFeeDetail> detailList = JsonUtils.parseArray(this.details, MutableFeeDetail.class);
        return CollectionUtils.isEmpty(detailList) ? new ArrayList<>() : detailList;
    }
}