/**
 *
 */
package com.suidifu.owlman.microservice.model;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author hjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferVoucherQueryModel {
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
     * 开始时间 createTime
     */
    private String startCreateTime;
    /**
     * 结束时间 createTime
     */
    private String endCreateTime;
    /**
     * 开始状态变更时间
     */
    private String startModifedTime;
    /**
     * 结束状态变更时间
     */
    private String endModifedTime;
    private String textFiled;

    public Date getStartCreateDate() {
        Date date = null;
        if (this.startCreateTime != null)
            date = DateUtils.parseDate(this.startCreateTime, pattern);
        return date;
    }

    public Date getEndCreateDate() {
        Date date = null;
        if (this.endCreateTime != null)
            date = DateUtils.parseDate(this.endCreateTime, pattern);
        return date;
    }

    public Date getStartModifedDate() {
        Date date = null;
        if (this.startModifedTime != null)
            date = DateUtils.parseDate(this.startModifedTime, pattern);
        return date;
    }

    public Date getEndModifedDate() {
        Date date = null;
        if (this.endModifedTime != null)
            date = DateUtils.parseDate(this.endModifedTime, pattern);
        return date;
    }

    public List<String> getFinancialContractUuidList() {
        if (StringUtils.isEmpty(this.financialContractUuids))
            return null;
        List<String> financialContractUuidList = JsonUtils.parseArray(this.financialContractUuids, String.class);
        if (CollectionUtils.isEmpty(financialContractUuidList))
            return null;
        return financialContractUuidList;
    }

    public PaymentInstitutionName getTranscationGatewayEnum() {
        try {
            return EnumUtil.fromOrdinal(PaymentInstitutionName.class, transcationGateway);
        } catch (Exception e) {
            return null;
        }
    }
}