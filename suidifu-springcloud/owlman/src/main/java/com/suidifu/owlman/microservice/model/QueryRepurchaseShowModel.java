/**
 *
 */
package com.suidifu.owlman.microservice.model;

import com.suidifu.hathaway.util.JsonUtils;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hjl
 */
@Data
@NoArgsConstructor
public class QueryRepurchaseShowModel {
    //回购编号
    private String repurchaseDocUuid;
    //商户名称
    private String appName;
    //贷款合同唯一编号
    private String contractUniqueId;
    //回购本金
    private BigDecimal repurchasePrincipal;
    //回购利息
    private BigDecimal repurchaseInterest;
    //回购罚息
    private BigDecimal repurchasePenalty;
    //回购其他费用
    private BigDecimal repurchaseOtherFee;
    //回购明细
    private String detailAmount;
    //回购天数
    private int repurchaseDays;
    //状态变更时间
    private Date lastModifedTime;
    //回购状态
    private String repurchaseStatus;

    public QueryRepurchaseShowModel(String repurchaseDocUuid, String appName, BigDecimal repurchasePrincipal,
                                    BigDecimal repurchaseInterest, BigDecimal repurchasePenalty, BigDecimal repurchaseOtherCharges,
                                    int repurchaseDays, Date lastModifedTime, String repurchaseStatus, String detailAmount, String contractUniqueId) {
        super();
        this.repurchaseDocUuid = repurchaseDocUuid;
        this.appName = appName;
        this.repurchasePrincipal = repurchasePrincipal;
        this.repurchaseInterest = repurchaseInterest;
        this.repurchasePenalty = repurchasePenalty;
        this.repurchaseOtherFee = repurchaseOtherCharges;
        this.detailAmount = detailAmount;
        this.repurchaseDays = repurchaseDays;
        this.lastModifedTime = lastModifedTime;
        this.repurchaseStatus = repurchaseStatus;
        this.contractUniqueId = contractUniqueId;
    }

    public QueryRepurchaseShowModel(RepurchaseDoc repurchaseDoc, RepaymentChargesDetail repaymentChargesDetail, String contractUniqueId) {
        this.repurchaseDocUuid = repurchaseDoc.getUuid();
        this.appName = repurchaseDoc.getAppName();
        this.repurchasePrincipal = repaymentChargesDetail.getRepurchasePrincipal();
        this.repurchaseInterest = repaymentChargesDetail.getRepurchaseInterest();
        this.repurchasePenalty = repaymentChargesDetail.getRepurchasePenalty();
        this.repurchaseOtherFee = repaymentChargesDetail.getRepurchaseOtherFee();
        this.detailAmount = JsonUtils.toJsonString(repaymentChargesDetail);
        this.repurchaseDays = repurchaseDoc.getRepoDays();
        this.lastModifedTime = repurchaseDoc.getLastModifedTime();
        this.repurchaseStatus = repurchaseDoc.getRepurchaseStatus().getChineseMessage();
        this.contractUniqueId = contractUniqueId;
    }
}