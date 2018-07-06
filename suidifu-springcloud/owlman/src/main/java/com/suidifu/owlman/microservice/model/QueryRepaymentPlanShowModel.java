/**
 *
 */
package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hjl
 */
@Data
@NoArgsConstructor
public class QueryRepaymentPlanShowModel {
    //还款编号
    private String repaymentPlanNo;
    //计划还款日期
    private Date assetRecycleDate;
    //贷款合同编号
    private String contractNo;
    //贷款合同唯一编号
    private String contractUniqueId;
    //客户姓名
    private String customerName;
    //逾期天数
    private int overdueDays;
    //应还款金额
    private BigDecimal planRecycleAmount;
    //实际还款金额
    private BigDecimal actualRecycleAmount;
    //还款状态
    private String assetStatus;
    //逾期状态
    private String overdueStatus;
    //未还明细金额
    private String detailAmounts;

    public QueryRepaymentPlanShowModel(String repaymentPlanNo, Date assetRecycleDate, String contractNo,
                                       String customerName, int overdueDays, BigDecimal planRecycleAmount, BigDecimal actualRecycleAmount,
                                       String assetStatus, String overdueStatus, String detailAmounts) {
        super();
        this.repaymentPlanNo = repaymentPlanNo;
        this.assetRecycleDate = assetRecycleDate;
        this.contractNo = contractNo;
        this.customerName = customerName;
        this.overdueDays = overdueDays;
        this.planRecycleAmount = planRecycleAmount;
        this.actualRecycleAmount = actualRecycleAmount;
        this.assetStatus = assetStatus;
        this.overdueStatus = overdueStatus;
        this.detailAmounts = detailAmounts;
    }

    public QueryRepaymentPlanShowModel(AssetSet assetSet, Contract contract, Customer customer, BigDecimal receivableAmount, String repaymentChargesDetailJson) {
        this.repaymentPlanNo = assetSet.getSingleLoanContractNo();
        this.assetRecycleDate = assetSet.getAssetRecycleDate();
        if (contract != null) {
            this.contractUniqueId = contract.getUniqueId();
            this.contractNo = contract.getContractNo();
        }
        if (customer != null) {
            this.customerName = customer.getName();
        }
        this.overdueDays = assetSet.getOverDueDays();
        this.planRecycleAmount = assetSet.getAssetFairValue();
        this.actualRecycleAmount = receivableAmount;
        this.assetStatus = assetSet.getAssetStatus().getChineseMessage();
        this.overdueStatus = assetSet.getOverdueStatus().getChineseMessage();
        this.detailAmounts = repaymentChargesDetailJson;
    }
}