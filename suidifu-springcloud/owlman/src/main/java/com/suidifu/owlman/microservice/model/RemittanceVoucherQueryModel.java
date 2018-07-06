package com.suidifu.owlman.microservice.model;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemittanceVoucherQueryModel {
    private final String pattern = "yyyy-MM-dd HH:mm:ss";
    /**
     * 项目名称
     */
    private String financialContractUuids;
    /**
     * 交易网关
     */
    private Integer transcationGateway;
    /**
     * 核销状态
     */
    private Integer sourceDocumentStatus;
    /**
     * 机构账户名
     */
    private String paymentName;
    /**
     * 机构账户号
     */
    private String paymentAccountNo;
    /**
     * 排序
     */
    private boolean isAsc;
    /**
     * 排序
     */
    private String orderBy;
    /**
     * 开始时间 createTime
     */
    private String startTime;
    /**
     * 结束时间 createTime
     */
    private String endTime;
    /**
     * 凭证类型
     */
    private Integer voucherType;

    public List<String> getFinancialContractUuidList() {
        if (StringUtils.isEmpty(this.financialContractUuids)) return null;
        List<String> financialContractUuidList = JsonUtils.parseArray(this.financialContractUuids, String.class);
        if (CollectionUtils.isEmpty(financialContractUuidList)) return null;
        return financialContractUuidList;
    }

    public PaymentInstitutionName getTranscationGatewayEnum() {
        try {
            return EnumUtil.fromOrdinal(PaymentInstitutionName.class, transcationGateway);
        } catch (Exception e) {
            return null;
        }
    }

    public SourceDocumentDetailStatus getSourceDocumentDetailStatusEnum() {
        try {
            return EnumUtil.fromOrdinal(SourceDocumentDetailStatus.class, sourceDocumentStatus);
        } catch (Exception e) {
            return null;
        }
    }

    public String getOrderBySetence(String orderBy) {
        String order = this.isAsc ? "ASC" : "DESC";
        return String.format(" order by %s %s ", orderBy, order);
    }

    public Date getStartTimeDate() {
        Date date = null;
        if (this.startTime != null)
            date = DateUtils.parseDate(this.startTime, pattern);
        return date;
    }

    public Date getEndTimeDate() {
        Date date = null;
        if (this.endTime != null)
            date = DateUtils.parseDate(this.endTime, pattern);
        return date;
    }
}