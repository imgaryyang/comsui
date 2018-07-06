package com.zufangbao.earth.yunxin.excel;

import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zufangbao.sun.yunxin.entity.SettlementOrder;
import com.zufangbao.sun.yunxin.entity.excel.SettlementOrderExcel;
import com.zufangbao.sun.yunxin.entity.model.SettlementOrderQueryModel;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;

@Component("settlementOrderForExportExcel")
public class SettlementOrderForExportExcel {

	@Autowired
	private SettlementOrderService settlementOrderService;
	@Autowired
    private RepaymentPlanService repaymentPlanService;

	public List<SettlementOrderExcel> createExcel(SettlementOrderQueryModel settlementOrderQueryModel) {

		List<SettlementOrder> settlementOrderList = settlementOrderService.getSettlementOrderListBy(settlementOrderQueryModel, null, 0, 0);

		List<SettlementOrderExcel> settlementOrderExcels = transferSettlementToExcelList(settlementOrderList);
		return settlementOrderExcels;

//		ExcelUtil<SettlementOrderExcel> excelUtil = new ExcelUtil<SettlementOrderExcel>(SettlementOrderExcel.class);
//		return excelUtil.exportDataToHSSFWork(settlementOrderExcels, "结清单详情");
	}

	private List<SettlementOrderExcel> transferSettlementToExcelList(List<SettlementOrder> settlementOrderList) {

		List<SettlementOrderExcel> settlementOrderExcelList = new ArrayList<SettlementOrderExcel>();
		transferDataToExcel(settlementOrderList, settlementOrderExcelList);

		return settlementOrderExcelList;
	}

	private void transferDataToExcel(List<SettlementOrder> settlementOrderList,
			List<SettlementOrderExcel> settlementOrderExcelList) {
		for (SettlementOrder settlementOrder : settlementOrderList) {
            SettlementOrderExcel settlementOrderExcel = new SettlementOrderExcel();
			String assetSetUuid = settlementOrder.getAssetSetUuid();
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
			if(assetSet != null){
                settlementOrderExcel.setRepaymentNo(assetSet.getSingleLoanContractNo());
                settlementOrderExcel.setRecycleDate(assetSet.getAssetRecycleDate());
                settlementOrderExcel.setUniqueId(assetSet.getContract().getUniqueId());
                settlementOrderExcel.setAppId(assetSet.getContract().getApp().getAppId());
                settlementOrderExcel.setPrincipalAndInterestAmount(assetSet.getAssetInitialValue());
                settlementOrderExcel.setSettlementStatus(assetSet.getSettlementStatus());
            }
            settlementOrderExcel.setSettleOrderNo(settlementOrder.getSettleOrderNo());
            settlementOrderExcel.setDueDate(settlementOrder.getDueDate());
            settlementOrderExcel.setOverdueDays(settlementOrder.getOverdueDays());
            settlementOrderExcel.setOverduePenalty(settlementOrder.getOverduePenalty());
            settlementOrderExcel.setModifyTime(settlementOrder.getLastModifyTime());
            settlementOrderExcel.setSettlementAmount(settlementOrder.getSettlementAmount());
			settlementOrderExcel.setComment(settlementOrder.getComment());
			settlementOrderExcelList.add(settlementOrderExcel);
		}
	}

	public List<SettlementOrderExcel> previewSettlementOrderExcel(SettlementOrderQueryModel settlementOrderQueryModel) {
		List<SettlementOrder> settlementOrderList = settlementOrderService.getSettlementOrderListBy(settlementOrderQueryModel, null, 0, 10);

		List<SettlementOrderExcel> settlementOrderExcels = transferSettlementToExcelList(settlementOrderList);
		return settlementOrderExcels;
	}

}
