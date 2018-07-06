package com.zufangbao.earth.yunxin.handler.modifyOverDueFee;

import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.OverdueChargesModifyModel;

public interface ModifyOverDueFeeHandler {
	/**
	 * 更新逾期费用
	 * @param repaymentPlan 还款计划
	 * @param overdueChargesDetail 逾期费用明细
	 */
	public void modifyOverdueCharges(AssetSet repaymentPlan, OverdueChargesModifyModel overdueChargesDetail);

	
}

