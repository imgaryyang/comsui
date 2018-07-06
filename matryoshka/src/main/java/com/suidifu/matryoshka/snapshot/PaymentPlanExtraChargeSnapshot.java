package com.suidifu.matryoshka.snapshot;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.sun.utils.Md5Util;
import com.zufangbao.sun.yunxin.entity.AssetSetExtraCharge;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 其它费用
 * Created by louguanyang on 2017/4/20.
 */
public class PaymentPlanExtraChargeSnapshot {

    private String assetSetUuid;

    /**
     * 贷款服务费
     */
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    /**
     * 技术维护费
     */
    private BigDecimal maintenanceCharge = BigDecimal.ZERO;
    /**
     * 其他费用
     */
    private BigDecimal otherCharge = BigDecimal.ZERO;

    /******   逾期相关费用  ****/
    /**
     * 罚息
     */
    private BigDecimal penaltyFee = BigDecimal.ZERO;
    /**
     * 逾期违约金
     */
    private BigDecimal latePenalty = BigDecimal.ZERO;
    /**
     * 逾期服务费
     */
    private BigDecimal lateFee = BigDecimal.ZERO;
    /**
     * 逾期其他费用
     */
    private BigDecimal lateOtherCost = BigDecimal.ZERO;
    
    /**
     * 实际还款金额
     */
    private BigDecimal actualAmount = BigDecimal.ZERO;

    public PaymentPlanExtraChargeSnapshot() {
        super();
    }

    public PaymentPlanExtraChargeSnapshot(String assetSetUuid, BigDecimal serviceCharge, BigDecimal maintenanceCharge,
                                       BigDecimal otherCharge, BigDecimal penaltyFee, BigDecimal latePenalty,
                                       BigDecimal lateFee, BigDecimal lateOtherCost, BigDecimal actualAmount) {
        super();
        this.assetSetUuid = assetSetUuid;
        this.serviceCharge = serviceCharge;
        this.maintenanceCharge = maintenanceCharge;
        this.otherCharge = otherCharge;
        this.penaltyFee = penaltyFee;
        this.latePenalty = latePenalty;
        this.lateFee = lateFee;
        this.lateOtherCost = lateOtherCost;
        this.actualAmount=actualAmount;
    }

    public String getAssetSetUuid() {
        return assetSetUuid;
    }

    public void setAssetSetUuid(String assetSetUuid) {
        this.assetSetUuid = assetSetUuid;
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

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public BigDecimal getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(BigDecimal latePenalty) {
        this.latePenalty = latePenalty;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getLateOtherCost() {
        return lateOtherCost;
    }

    public void setLateOtherCost(BigDecimal lateOtherCost) {
        this.lateOtherCost = lateOtherCost;
    }

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public PaymentPlanExtraChargeSnapshot(String assetSetUuid,List<AssetSetExtraCharge> assetSetExtraCharges) {
		super();
		this.assetSetUuid=assetSetUuid;
		if (CollectionUtils.isEmpty(assetSetExtraCharges)) {
			return;
		}
		Map<String, BigDecimal> assetSetExtraChargeMap = assetSetExtraCharges.stream().collect(Collectors.toMap(AssetSetExtraCharge::lastAccountName,AssetSetExtraCharge::getAccountAmount));
		this.serviceCharge = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY,BigDecimal.ZERO);
		this.maintenanceCharge = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.LOAN_TECH_FEE_KEY,BigDecimal.ZERO);
		this.otherCharge = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.LOAN_OTHER_FEE_KEY,BigDecimal.ZERO);
		this.penaltyFee = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.PENALTY_KEY,BigDecimal.ZERO);
		this.latePenalty = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY,BigDecimal.ZERO);
		this.lateFee = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY,BigDecimal.ZERO);
		this.lateOtherCost = assetSetExtraChargeMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY,BigDecimal.ZERO);
	}
    /**
     * MD5(ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE=XXX;ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE=XXX;......)
     */
    @JSONField(serialize = false)
    public String assetExtraFeeFingerPrint() {
        try {
            return Md5Util.assetExtraFeeFingerPrint(maintenanceCharge, serviceCharge, otherCharge);
        } catch (Exception e) {
            return "";
        }
    }

    @JSONField(serialize = false)
    public boolean check_assetExtraFeeFingerPrint(String assetExtraFeeFingerPrint) {
        return StringUtils.equals(assetExtraFeeFingerPrint(), assetExtraFeeFingerPrint);
    }
    
	public BigDecimal getRepayCharge() {
		return serviceCharge.add(maintenanceCharge).add(otherCharge).add(penaltyFee).add(latePenalty)
				.add(lateFee).add(lateOtherCost);
	}
}
