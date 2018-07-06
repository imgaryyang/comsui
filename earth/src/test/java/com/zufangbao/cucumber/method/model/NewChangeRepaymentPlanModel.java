package com.zufangbao.cucumber.method.model;

import java.math.BigDecimal;

/**
 * Created by dzz on 17-5-11.
 */
public class NewChangeRepaymentPlanModel {
    private String assetRecycleDate;
    private BigDecimal assetPrincipal;
    private BigDecimal assetInterest;
    private BigDecimal serviceCharge;
    private BigDecimal maintenanceCharge;
    private BigDecimal otherCharge;
    private int assetType;

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

    public int getAssetType() {
        return assetType;
    }

    public void setAssetType(int assetType) {
        this.assetType = assetType;
    }
}
