package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductReturnModel {
    private String requestId;
    private String referenceId;
    private String orderNo;
    private BigDecimal amount;
    private Integer status;
    private String comment;
    private String paidNoticInfos;
    private String lastModifiedTime;

    public DeductApplicationExecutionStatus getDeductStatusEnum() {
        return EnumUtil.fromOrdinal(DeductApplicationExecutionStatus.class, getStatus());
    }

    public Date getLastModifiedTimeDate() {
        try {
            return lastModifiedTime == null ? null : DateUtils.parseDate(lastModifiedTime, DateUtils.LONG_DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }

    }
}