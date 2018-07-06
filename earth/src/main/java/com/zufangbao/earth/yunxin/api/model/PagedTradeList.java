package com.zufangbao.earth.yunxin.api.model;

import com.zufangbao.earth.yunxin.api.model.query.AccountTradeList;

import java.util.List;

public class PagedTradeList {
	
	private List<AccountTradeList> tradeList;
	private boolean hasNextPage;
	public List<AccountTradeList> getTradeList() {
		return tradeList;
	}
	public void setTradeList(List<AccountTradeList> tradeList) {
		this.tradeList = tradeList;
	}
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
	

}
