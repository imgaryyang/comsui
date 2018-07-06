package com.zufangbao.earth.yunxin.api.model.query;

import com.zufangbao.sun.yunxin.log.MutableFeeDetailLogVO;

import java.util.List;

public class MutableFeeDetailsResultModel {

	private List<MutableFeeDetailLogVO> mutableFeeDetailLogs;

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MutableFeeDetailLogVO> getMutableFeeDetailLogs() {
		return mutableFeeDetailLogs;
	}

	public void setMutableFeeDetailLogs(List<MutableFeeDetailLogVO> mutableFeeDetailLogs) {
		this.mutableFeeDetailLogs = mutableFeeDetailLogs;
	}

	public MutableFeeDetailsResultModel(List<MutableFeeDetailLogVO> mutableFeeDetailLogs, int count) {
		super();
		this.mutableFeeDetailLogs = mutableFeeDetailLogs;
		this.count = count;
	}

	public MutableFeeDetailsResultModel() {
		super();
	}

}