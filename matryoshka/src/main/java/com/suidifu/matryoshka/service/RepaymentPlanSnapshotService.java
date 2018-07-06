package com.suidifu.matryoshka.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.zufangbao.sun.yunxin.entity.AssetSet;

/**
 * 还款计划表(AssetSet)，CRUD
 * 
 * @author zhanghongbing
 *
 */
public interface RepaymentPlanSnapshotService extends GenericService<AssetSet> {

	List<PaymentPlanSnapshot> get_all_assetSetSnapshot_list(String contractUuid);

	List<PaymentPlanSnapshot> get_all_unclear_assetSetSnapshot_list(String contractUuid);

	List<PaymentPlanSnapshot> get_assetSetSnapshot_list_by_no_list(List<String> repaymentPlanNoList);

}
