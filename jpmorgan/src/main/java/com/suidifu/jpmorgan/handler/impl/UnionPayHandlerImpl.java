package com.suidifu.jpmorgan.handler.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.suidifu.jpmorgan.entity.unionpay.RealTimePaymentInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultQueryInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultXmlModel;
import com.suidifu.jpmorgan.entity.unionpay.UnionPayResult;
import com.suidifu.jpmorgan.entity.unionpay.UnionPayRtnInfo;
import com.suidifu.jpmorgan.exception.TransactionResultQueryApiConstant.ErrMsg;
import com.suidifu.jpmorgan.exception.TransactionResultQueryApiConstant.RTNCode;
import com.suidifu.jpmorgan.exception.TransactionResultQueryApiException;
import com.suidifu.jpmorgan.handler.IUnionPayHandler;
import com.suidifu.jpmorgan.util.UnionPayUtil;


@Component("UnionPayHandler")
public class UnionPayHandlerImpl implements IUnionPayHandler{
	
	private static Log logger = LogFactory.getLog(UnionPayHandlerImpl.class);

	@Override
	public UnionPayResult execRealTimePayment(RealTimePaymentInfoModel realTimePaymentInfoModel) {
		String xmlPacket = UnionPayPacketFactory.generateRealTimePaymentPacket(realTimePaymentInfoModel);
//		return UnionPayUtil.executePostRequest(xmlPacket, realTimePaymentInfoModel);
		return UnionPayUtil.executePostRequestByJsoup(xmlPacket, realTimePaymentInfoModel);
	}

	@Override
	public TransactionResultXmlModel execTransactionResultQuery(TransactionResultQueryInfoModel queryInfoModel) throws TransactionResultQueryApiException {
		String xmlPacket = UnionPayPacketFactory.genTransactionResultQueryPacket(queryInfoModel);
		UnionPayResult result = UnionPayUtil.executePostRequest(xmlPacket, queryInfoModel);
		validSuccess(result);
		TransactionResultXmlModel resultXmlModel = TransactionResultXmlModel.initialization(result);
		logger.info("交易结果查询结果（已解析）:" + JSON.toJSONString(resultXmlModel));
		return resultXmlModel;
	}
	
	private void validSuccess(UnionPayResult payResult) throws TransactionResultQueryApiException{
		if(!payResult.isValid()){
			throw new TransactionResultQueryApiException(RTNCode.OTHER_ERROR, payResult.getMessage());
		}
		UnionPayRtnInfo rtnInfo = TransactionResultXmlModel.parseInfo(payResult);
		if(rtnInfo==null){
			throw new TransactionResultQueryApiException(RTNCode.OTHER_ERROR, ErrMsg.ERR_MSG_UNIONPAY_XML_FORMAT);
		}
		if(!rtnInfo.isSuc()){
			throw new TransactionResultQueryApiException(RTNCode.OTHER_ERROR, rtnInfo.getErrMsg());
		}
		
	}
	
	@Override
	public void startRealTimePayment(Serializable id) {
		try {
			// 从表中取出实时代付请求RealTimePaymentRequest xx = transferApplicationService.load(TransferApplication.class, id);
			// 开始记录日志 createRealTimePaymentRecordBy()

			RealTimePaymentInfoModel realTimePaymentInfoModel = new RealTimePaymentInfoModel();
			UnionPayResult unionPayResult = execRealTimePayment(realTimePaymentInfoModel);
			
			analysis_and_record_realtime__result(unionPayResult);
			
		} catch (Exception e) {
			logger.error("startRealTimePayment has error!");
			e.printStackTrace();
		}
		
	}
	
	private void analysis_and_record_realtime__result(UnionPayResult payResult){
		
	}


}
