package com.zufangbao.cucumber.method.model;

import java.math.BigDecimal;

/**
 * Created by dzz on 17-6-6.
 */
public class ChangeRepayMentModel {
    /**
     * 计划还款日期
     * */
    private String assetRecycleDate;
    /**
     * 计划还款本金
     * */
    private BigDecimal assetPrincipal;
    /**
     * 计划还款利息
     * */
    private BigDecimal assetInterest;
    /**
     * 贷款服务费
     * */
    private BigDecimal serviceCharge;
    /**
     * 技术维护费
     * */
    private BigDecimal maintenanceCharge;
    /**
     * 其他费用
     * */
    private BigDecimal otherCharge;
    /**
     *还款计划类型
     * */
    private Integer assetType;


    public String getAssetRecycleDate() {
        return assetRecycleDate;
    }

    public void setAssetRecycleDate(String assetRecycleDate) {
        this.assetRecycleDate = assetRecycleDate;
    }

    public BigDecimal getAssetPrincipal() {
        return assetPrincipal;
    }

    public void setAssetPrincipal(BigDecimal assetPrincipal) {
        this.assetPrincipal = assetPrincipal;
    }

    public BigDecimal getAssetInterest() {
        return assetInterest;
    }

    public void setAssetInterest(BigDecimal assetInterest) {
        this.assetInterest = assetInterest;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getMaintenanceCharge() {
        return maintenanceCharge;
    }

    public void setMaintenanceCharge(BigDecimal maintenanceCharge) {
        this.maintenanceCharge = maintenanceCharge;
    }

    public BigDecimal getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(BigDecimal otherCharge) {
        this.otherCharge = otherCharge;
    }

    public Integer getAssetType() {
        return assetType;
    }

    public void setAssetType(Integer assetType) {
        this.assetType = assetType;
    }
}
