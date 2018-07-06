package com.zufangbao.testAPIWuBo.testAPI.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Cool on 2017/7/19.
 */
public class RepaymentDetailsModel {

    /**
     * 贷款合同唯一编号
     * */
    private String contractNo;
    /**
     * 贷款合同编号
     * */
    private String contractUniqueId;
    /**
     * 还款方式
     * */
    private Long repaymentWay;
    /**
     *业务编号
     * */
    private String repaymentBusinessNo;
    private String repayScheduleNo;

    /**
     *设定还款时间
     * */
    private String plannedDate;
    /**
     * 明细总金额
     * */
    private BigDecimal detailsTotalAmount;
    /**
     * 业务金额明细
     * */
    private List<BusinessAmountModel> detailsAmountJsonList;


    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractUniqueId() {
        return contractUniqueId;
    }

    public void setContractUniqueId(String contractUniqueId) {
        this.contractUniqueId = contractUniqueId;
    }

    public Long getRepaymentWay() {
        return repaymentWay;
    }

    public void setRepaymentWay(Long repaymentWay) {
        this.repaymentWay = repaymentWay;
    }

    public String getRepaymentBusinessNo() {
        return repaymentBusinessNo;
    }

    public void setRepaymentBusinessNo(String repaymentBusinessNo) {
        this.repaymentBusinessNo = repaymentBusinessNo;
    }

    public String getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(String plannedDate) {
        this.plannedDate = plannedDate;
    }

    public BigDecimal getDetailsTotalAmount() {
        return detailsTotalAmount;
    }

    public void setDetailsTotalAmount(BigDecimal detailsTotalAmount) {
        this.detailsTotalAmount = detailsTotalAmount;
    }

    public List<BusinessAmountModel> getDetailsAmountJsonList() {
        return detailsAmountJsonList;
    }

    public void setDetailsAmountJsonList(List<BusinessAmountModel> detailsAmountJsonList) {
        this.detailsAmountJsonList = detailsAmountJsonList;
    }
    public String getRepayScheduleNo() {
        return repayScheduleNo;
    }

    public void setRepayScheduleNo(String repayScheduleNo) {
        this.repayScheduleNo = repayScheduleNo;
    }
}









