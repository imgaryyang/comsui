package com.suidifu.matryoshka.service.impl;

import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.service.RepaymentPlanSnapshotService;
import com.suidifu.matryoshka.snapshot.PaymentPlanExtraChargeSnapshot;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.AssetSetExtraCharge;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;

@Service("repaymentPlanSnapshotService")
public class RepaymentPlanSnapshotServiceImpl extends GenericServiceImpl<AssetSet> implements RepaymentPlanSnapshotService {

	@Autowired
    LedgerItemService ledgerItemService;

	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;

	@Override
	@SuppressWarnings("unchecked")
	public List<PaymentPlanSnapshot> get_all_assetSetSnapshot_list(String contractUuid) {
		if (StringUtils.isEmpty(contractUuid)) {
			return Collections.EMPTY_LIST;
		}
		String sql = "FROM AssetSet Where activeStatus =:activeStatus AND contractUuid =:contractUuid order by currentPeriod";
		Map<String, Object> params = new HashMap<>();
		params.put("contractUuid", contractUuid);
		params.put("activeStatus", AssetSetActiveStatus.OPEN);
		List<AssetSet> assetSets = genericDaoSupport.searchForList(sql, params);
		if (CollectionUtils.isEmpty(assetSets)) {
			return Collections.EMPTY_LIST;
		}
        return create_paymentPlanSnapshotList(assetSets);
	}

	private List<PaymentPlanSnapshot> create_paymentPlanSnapshotList(List<AssetSet> assetSets) {
		List<PaymentPlanSnapshot> snapshots = new ArrayList<>();
		for (AssetSet assetSet : assetSets) {
			PaymentPlanSnapshot assetSetSnapshot = create_paymentPlanSnapshot(assetSet);
			snapshots.add(assetSetSnapshot);
		}
		return snapshots;
	}

	private PaymentPlanSnapshot create_paymentPlanSnapshot(AssetSet assetSet) {
	    if (assetSet == null){
	        return null;
        }
		PaymentPlanSnapshot assetSetSnapshot = new PaymentPlanSnapshot(assetSet);
		PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot = getAssetSetExtraChargeSnapshot(assetSet.getAssetUuid());
		assetSetSnapshot.setAssetSetExtraChargeSnapshot(assetSetExtraChargeSnapshot);
		return assetSetSnapshot;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PaymentPlanSnapshot> get_all_unclear_assetSetSnapshot_list(String contractUuid) {
		if (StringUtils.isEmpty(contractUuid)) {
			return Collections.EMPTY_LIST;
		}
		String sql = "FROM AssetSet Where activeStatus =:activeStatus AND contractUuid =:contractUuid AND assetStatus !=:assetStatus order by currentPeriod";
		Map<String, Object> params = new HashMap<>();
		params.put("contractUuid", contractUuid);
		params.put("assetStatus", AssetClearStatus.CLEAR);
		params.put("activeStatus", AssetSetActiveStatus.OPEN);
		List<AssetSet> assetSets = genericDaoSupport.searchForList(sql, params);
		if (CollectionUtils.isEmpty(assetSets)) {
			return Collections.EMPTY_LIST;
		}
        return create_paymentPlanSnapshotList(assetSets);
	}

	private PaymentPlanExtraChargeSnapshot getAssetSetExtraChargeSnapshot(String assetSetUuid) {
		List<AssetSetExtraCharge> assetSetExtraCharges = repaymentPlanExtraChargeService.getExtraChargeList(assetSetUuid);
		PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot = new PaymentPlanExtraChargeSnapshot(assetSetUuid,
                assetSetExtraCharges);
		return assetSetExtraChargeSnapshot;
	}

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Override
	public List<PaymentPlanSnapshot> get_assetSetSnapshot_list_by_no_list(List<String> repaymentPlanNoList) {
		if (CollectionUtils.isEmpty(repaymentPlanNoList)) {
			return Collections.emptyList();
		}
		List<PaymentPlanSnapshot> snapshots = new ArrayList<>();
		for (String repaymentPlanNo: repaymentPlanNoList) {
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
            PaymentPlanSnapshot paymentPlanSnapshot = create_paymentPlanSnapshot(assetSet);
            if (paymentPlanSnapshot != null) {
                snapshots.add(paymentPlanSnapshot);
            }
        }
		return snapshots;
	}

}
