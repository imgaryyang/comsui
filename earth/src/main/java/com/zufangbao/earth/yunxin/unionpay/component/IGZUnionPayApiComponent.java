package com.zufangbao.earth.yunxin.unionpay.component;

import com.gnete.security.crypt.CryptException;
import com.zufangbao.earth.api.exception.TransactionDetailApiException;
import com.zufangbao.earth.yunxin.unionpay.model.*;
import com.zufangbao.earth.yunxin.unionpay.model.basic.GZUnionPayResult;

public interface IGZUnionPayApiComponent {

	/**
	 * 批扣执行
	 * @param batchDeductInfoModel 银联批量扣款信息模型
	 * @return 结果信息
	 * @throws CryptException 签名异常
	 */
    GZUnionPayResult execBatchDeduct(BatchDeductInfoModel batchDeductInfoModel);

    /**
	 * 实时单笔扣执行
	 * @param realTimeDeductInfoModel 银联实时单笔扣款信息模型
	 * @return 结果信息
	 * @throws CryptException 签名异常
	 */
    GZUnionPayResult execRealTimeDeductPacket(RealTimeDeductInfoModel realTimeDeductInfoModel);

    /**
	 * 批查询执行
	 * @param batchQueryInfoModel 银联交易批量查询信息模型
	 * @return 结果信息
	 * @throws CryptException 签名异常
	 */
    GZUnionPayResult execBatchQuery(BatchQueryInfoModel batchQueryInfoModel);

    /**
	 * 查询交易明细
	 * @return 交易明细查询结果
	 * @throws TransactionDetailApiException 
	 * @throws CryptException
	 */
    TransactionDetailQueryResult execTransactionDetailQuery(TransactionDetailQueryInfoModel transactionDetailQueryInfoModel) throws TransactionDetailApiException;

    /**
	 * 查询交易明细，返回结果可修改，现暂时简单处理
	 * @return 账户明细查询结果
	 * @throws CryptException
	 * 
	 */
    GZUnionPayResult execAccountDetailQuery(AccountDetailQueryInfoModel accountDetailQueryInfoModel);

    /**
	 * 执行实时代付
	 */
    GZUnionPayResult execRealTimePayment(RealTimePaymentInfoModel batchPaymentInfoModel);

/*	
	 * 执行交易结果查询
	public GZUnionPayResult execTransactionResultQuery(BatchQueryInfoModel queryModel);
*/
	
}
