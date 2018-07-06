package com.suidifu.jpmorgan.entity.unionpay;


public class TransactionResultQueryInfoModel  extends UnionPayBasicInfoModel{
	
	private String queryReqNo; //要查询的交易流水号

	public String getQueryReqNo() {
		return queryReqNo;
	}

	public void setQueryReqNo(String queryReqNo) {
		this.queryReqNo = queryReqNo;
	}

	public TransactionResultQueryInfoModel() {
		super();
	}
	
}
