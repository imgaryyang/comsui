package com.zufangbao.earth.api;

import com.zufangbao.earth.api.xml.BatchPaymentProcessingResultXml;
import com.zufangbao.earth.api.xml.BatchPaymentReqXml;
import com.zufangbao.earth.api.xml.TransactionStatusQueryRspDetailXml;

import java.util.List;

public interface IPaymentAndQuery {

    List<BatchPaymentProcessingResultXml> execBatchPayment(BatchPaymentReqXml reqXml, String reqXmlPacket) throws Exception;

    TransactionStatusQueryRspDetailXml execTransactionQuery(RequestRecord record) throws Exception;
}
