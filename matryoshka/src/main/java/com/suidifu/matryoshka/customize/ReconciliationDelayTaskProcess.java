package com.suidifu.matryoshka.customize;

import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import java.util.Date;

public interface ReconciliationDelayTaskProcess {

	void processingDelayTask(Date tradeTime, Contract contract, RepaymentChargesDetail detailAmount, AssetSet assetSet, String repurchaseUuid, AssetRecoverType assetRecoverType, String recover_delay_task_uuid);

	void processingRepaymentOrderCheckDelayTask(Date tradeTime, Contract contract, RepaymentChargesDetail detailAmount,
		AssetSet assetSet, int period, String repurchaseUuid, RepaymentBusinessType repaymentBusinessType,
		String configUuid);
}
