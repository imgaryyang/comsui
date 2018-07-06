package com.zufangbao.earth.yunxin.api.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.model.ThirdPartyVoucherBatchQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherBatchShowModel;

import java.util.List;

public interface ThirdPartyVoucherBatchHandler {

	List<ThirdPartVoucherBatchShowModel> getThirdPartVoucherBatchShowModelList(
			ThirdPartyVoucherBatchQueryModel thirdPartVoucherBatchQueryModel, Page page);

	int countThirdPartVoucherBatchNum(ThirdPartyVoucherBatchQueryModel thirdPartVoucherBatchQueryModel);

	ThirdPartVoucherBatchShowModel getThirdPartVoucherCommandLogShowModel(String requestBatchNo);

}
