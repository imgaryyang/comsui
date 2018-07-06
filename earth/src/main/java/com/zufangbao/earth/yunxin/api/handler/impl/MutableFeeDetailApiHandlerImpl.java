package com.zufangbao.earth.yunxin.api.handler.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.handler.MutableFeeDetailApiHandler;
import com.zufangbao.earth.yunxin.api.model.query.MutableFeeDetailsResultModel;
import com.zufangbao.sun.yunxin.log.MutableFeeDetailLog;
import com.zufangbao.sun.yunxin.log.MutableFeeDetailLogVO;
import com.zufangbao.sun.yunxin.service.MutableFeeDetailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("MutableFeeDetailApiHandler")
public class MutableFeeDetailApiHandlerImpl implements MutableFeeDetailApiHandler {
	@Autowired
	MutableFeeDetailLogService mutableFeeDetailLogService;
	@Override
	public MutableFeeDetailsResultModel queryMutableFeeDetails(String singleLoanContractNo, Page page) {
		List<MutableFeeDetailLog> mutableFeeDetailLogList = mutableFeeDetailLogService.queryMutableFeeDetailLogsByRepaymentPlanNo(singleLoanContractNo, page);
		List<MutableFeeDetailLogVO> mutableFeeDetailLogs=new ArrayList<>();
		for (MutableFeeDetailLog mutableFeeDetailLog : mutableFeeDetailLogList) {
			mutableFeeDetailLogs.add(new MutableFeeDetailLogVO(mutableFeeDetailLog));
		}
		int count = mutableFeeDetailLogService.queryMutableFeeDetailLogsCount(singleLoanContractNo);
		MutableFeeDetailsResultModel resultModel = new MutableFeeDetailsResultModel(mutableFeeDetailLogs, count);
		return resultModel;
	}
	@Override
	public MutableFeeDetailLog getMutableFeeDetailLog(String singleLoanContractNo) {
		// TODO Auto-generated method stub
		return mutableFeeDetailLogService.getMutableFeeDetailLog(singleLoanContractNo);
	}

}