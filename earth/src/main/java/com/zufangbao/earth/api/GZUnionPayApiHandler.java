package com.zufangbao.earth.api;

import com.zufangbao.earth.api.exception.TransactionDetailApiException;
import com.zufangbao.earth.api.xml.TransactionDetail;
import com.zufangbao.earth.api.xml.TransactionDetailQueryParams;
import com.zufangbao.earth.yunxin.unionpay.model.TransactionDetailQueryInfoModel;
import com.zufangbao.sun.entity.financial.PaymentChannelType;

import java.util.List;

public interface GZUnionPayApiHandler {

    String executeBatchPayment(String reqXmlPacket);

    String queryTransactionDetail(String reqPacketXml);

    /**
	 * 查询交易明细
	 * @return 银联交易明细查询结果
	 * @throws TransactionDetailApiException 
	 * 
	 */
    List<TransactionDetail> execTransactionDetailQuery(TransactionDetailQueryParams transactionDetailOutQueryParams) throws TransactionDetailApiException;

    List<TransactionDetail> execDirectBankTransactionDetailQuery(TransactionDetailQueryParams transactionDetailQueryParams) throws TransactionDetailApiException;

    TransactionDetailQueryInfoModel convert(TransactionDetailQueryParams transactionDetailOutQueryParams, PaymentChannelType paymentChannelType) throws
            TransactionDetailApiException;

    String execTransactionResultQuery(String reqXmlPacket);
}
