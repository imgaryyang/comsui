package com.zufangbao.earth.yunxin.api.handler.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartyVoucherBatchHandler;
import com.zufangbao.sun.service.ThirdPartyVoucherBatchService;
import com.zufangbao.sun.yunxin.entity.model.ThirdPartyVoucherBatchQueryModel;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayVoucherBatch;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherBatchShowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("thirdPartyVoucherBatchHandler")
public class ThirdPartyVoucherBatchHandlerImpl implements ThirdPartyVoucherBatchHandler {
	@Autowired
	private ThirdPartyVoucherBatchService thirdPartVoucherBatchService;

	@Override
	public List<ThirdPartVoucherBatchShowModel> getThirdPartVoucherBatchShowModelList(
			ThirdPartyVoucherBatchQueryModel thirdPartVoucherCommandLogQueryModel, Page page) {
		List<ThirdPartyPayVoucherBatch> thirdPartVoucherBatchs = thirdPartVoucherBatchService
				.getThirdPartVoucherBatchList(thirdPartVoucherCommandLogQueryModel, page);
		List<ThirdPartVoucherBatchShowModel> list = new ArrayList<>();
		if (null == thirdPartVoucherBatchs)
			return null;
		for (ThirdPartyPayVoucherBatch thirdPartVoucherBatch : thirdPartVoucherBatchs) {
			if (null == thirdPartVoucherBatch)
				continue;
			list.add(covert(thirdPartVoucherBatch));
		}
		return list;
	}

	@Override
	public int countThirdPartVoucherBatchNum(ThirdPartyVoucherBatchQueryModel thirdPartVoucherCommandLogQueryModel) {
		return thirdPartVoucherBatchService.countThirdPartyVoucherBatchNum(thirdPartVoucherCommandLogQueryModel);
	}

	private ThirdPartVoucherBatchShowModel covert(ThirdPartyPayVoucherBatch thirdPartVoucherBatch) {
		if(null==thirdPartVoucherBatch)
			return null;
		ThirdPartVoucherBatchShowModel showModel = new ThirdPartVoucherBatchShowModel();
		showModel.setBatchUuid(thirdPartVoucherBatch.getBatchUuid());
		showModel.setRequestNo(thirdPartVoucherBatch.getRequestNo());
		showModel.setFinancialContractNo(thirdPartVoucherBatch.getFinancialContractNo());
		showModel.setSize(thirdPartVoucherBatch.getSize());
		showModel.setCreateTime(thirdPartVoucherBatch.getCreateTime());
		return showModel;
	}
	@Override
	public ThirdPartVoucherBatchShowModel getThirdPartVoucherCommandLogShowModel(String batchUuid){
		return covert(thirdPartVoucherBatchService.getThirdPartVoucherBatchByUuid(batchUuid));
	}

}
