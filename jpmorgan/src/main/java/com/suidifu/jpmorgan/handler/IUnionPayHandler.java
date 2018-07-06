package com.suidifu.jpmorgan.handler;

import java.io.Serializable;

import com.suidifu.jpmorgan.entity.unionpay.RealTimePaymentInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultQueryInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultXmlModel;
import com.suidifu.jpmorgan.entity.unionpay.UnionPayResult;
import com.suidifu.jpmorgan.exception.TransactionResultQueryApiException;

public interface IUnionPayHandler {

	/**
	 * 执行实时代付
	 */
	public UnionPayResult execRealTimePayment(RealTimePaymentInfoModel realTimePaymentInfoModel);
	
	/**
	 * 开始执行实时代付
	 */
	public void startRealTimePayment(Serializable id);
	
	/**
	 * 交易结果查询请求
	 */
	public TransactionResultXmlModel execTransactionResultQuery(TransactionResultQueryInfoModel queryInfoModel) throws TransactionResultQueryApiException;
}
