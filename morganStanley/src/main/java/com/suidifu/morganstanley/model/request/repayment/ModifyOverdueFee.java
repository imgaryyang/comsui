package com.suidifu.morganstanley.model.request.repayment;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * 更新逾期费用明细接口 Model
 *
 * @author louguanyang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "修改逾期费用")
public class ModifyOverdueFee {
    @ApiModelProperty(value = "请求唯一编号", required = true)
    @NotEmpty(message = "请求唯一编号[requestNo]不能为空")
    private String requestNo = StringUtils.EMPTY;

    @ApiModelProperty(value = "请求修改参数", required = true)
    @NotBlank(message = "具体变更内容[modifyOverDueFeeDetails]不能为空")
    private String modifyOverDueFeeDetails = StringUtils.EMPTY;

    @JSONField(serialize = false)
    public @Valid
    List<OverdueFeeDetail> getDetailList() {
        return JsonUtils.parseArray(modifyOverDueFeeDetails, OverdueFeeDetail.class);
    }
}
