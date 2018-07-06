package com.suidifu.jpmorgan.handler.impl;

import java.math.BigDecimal;

import com.suidifu.jpmorgan.entity.unionpay.PaymentDetailInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.RealTimePaymentInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultQueryInfoModel;

public class UnionPayPacketFactory {
	
	/**
	 * 批量代付模版
	 * 依次入参 ：用户名，用户密码，交易流水号，业务代码，商户号，总记录数，总金额，批扣明细列表
	 */
	public static final String REAL_TIME_PAYMENT_PACKET = "<?xml version=\"1.0\" encoding=\"gbk\"?><GZELINK><INFO><TRX_CODE>100005</TRX_CODE><VERSION>05</VERSION><DATA_TYPE>2</DATA_TYPE><LEVEL>5</LEVEL><USER_NAME>%s</USER_NAME><USER_PASS>%s</USER_PASS><REQ_SN>%s</REQ_SN><SIGNED_MSG></SIGNED_MSG></INFO><BODY><TRANS_SUM><BUSINESS_CODE>%s</BUSINESS_CODE><MERCHANT_ID>%s</MERCHANT_ID><TOTAL_ITEM>%s</TOTAL_ITEM><TOTAL_SUM>%s</TOTAL_SUM></TRANS_SUM><TRANS_DETAILS>%s</TRANS_DETAILS></BODY></GZELINK>";
	
	/**
	 * 批量代付数据包传递明细模版
	 * 依次入参数 ：序列号，银行代码，银行卡号，持卡人姓名，开户证件类型，证件号，开户行所在省，开户行所在市，金额，备注
	 */
	public static final String PAYMENT_PACKET_TRANS_DETAIL = "<TRANS_DETAIL><SN>%s</SN><BANK_CODE>%s</BANK_CODE><ACCOUNT_TYPE>00</ACCOUNT_TYPE><ACCOUNT_NO>%s</ACCOUNT_NO><ACCOUNT_NAME>%s</ACCOUNT_NAME><ID_TYPE>%s</ID_TYPE><ID>%s</ID><PROVINCE>%s</PROVINCE><CITY>%s</CITY><AMOUNT>%s</AMOUNT><REMARK>%s</REMARK></TRANS_DETAIL>";
	
	
	/**
	 * 交易结果查询请求报文模版
	 * 依次入参 ：用户名，用户密码，交易流水号，要查询的交易流水号
	 */
	public static final String TRANSACTION_RESULT_QUERY_PACKET = "<?xml version=\"1.0\" encoding=\"gbk\"?><GZELINK><INFO><TRX_CODE>200001</TRX_CODE><VERSION>05</VERSION><DATA_TYPE>2</DATA_TYPE><USER_NAME>%s</USER_NAME><USER_PASS>%s</USER_PASS><REQ_SN>%s</REQ_SN><SIGNED_MSG></SIGNED_MSG></INFO><BODY><QUERY_TRANS><QUERY_SN>%s</QUERY_SN></QUERY_TRANS></BODY></GZELINK>";
	
	
	/**
	 * 生成批量代付请求 
	 */
	public static String generateRealTimePaymentPacket(RealTimePaymentInfoModel realTimePaymentInfoModel ){
		if(realTimePaymentInfoModel == null) {
			realTimePaymentInfoModel = new RealTimePaymentInfoModel();
		}
		
		String userName = realTimePaymentInfoModel.getUserName();
		String userPwd = realTimePaymentInfoModel.getUserPwd();
		String merchantId = realTimePaymentInfoModel.getMerchantId();
		
		String reqNo = realTimePaymentInfoModel.getReqNo();
		String businessCode = realTimePaymentInfoModel.getBusinessCode();
		int totalItem = realTimePaymentInfoModel.getTotalItem();
		String totalSum = convert_yuan_to_cent(realTimePaymentInfoModel.getTotalSum());
		
//		StringBuffer buffer = new StringBuffer();
		PaymentDetailInfoModel detailInfo = realTimePaymentInfoModel.getDetailInfo();
		String sn = detailInfo.getSn();
		String bankCode = detailInfo.getBankCode();
		String accountNo = detailInfo.getAccountNo();
		String accountName = detailInfo.getAccountName();
		String idType = detailInfo.getIdType();
		String idNum = detailInfo.getIdNum();
		String province = detailInfo.getProvince();
		String city = detailInfo.getCity();
			
		String amount = convert_yuan_to_cent(detailInfo.getAmount());
		String remark = detailInfo.getRemark();
		String detailXml = formatEscapeNull(PAYMENT_PACKET_TRANS_DETAIL, sn, bankCode, accountNo, accountName, idType, idNum, province, city, amount, remark);
//			buffer.append(detailXml);
	
		return formatEscapeNull(REAL_TIME_PAYMENT_PACKET, userName, userPwd, reqNo, businessCode, merchantId, totalItem, totalSum, detailXml);
		
	}
	
	public static String genTransactionResultQueryPacket(TransactionResultQueryInfoModel queryInfoModel){
		if(queryInfoModel == null) {
			queryInfoModel = new TransactionResultQueryInfoModel();
		}
		String userName = queryInfoModel.getUserName();
		String userPwd = queryInfoModel.getUserPwd();
		String reqNo = queryInfoModel.getReqNo();
		String queryReqNo = queryInfoModel.getQueryReqNo();
		return formatEscapeNull(TRANSACTION_RESULT_QUERY_PACKET, userName, userPwd, reqNo, queryReqNo);
	}
	
	
	public static String convert_yuan_to_cent(BigDecimal yuan_num) {
		if (yuan_num == null) {
			return "0";
		}
		return yuan_num.movePointRight(2).toString();
	}
	
	private static String formatEscapeNull(String format, Object... args) {
		for (int i = 0; i < args.length; i++) {
			if(args[i] == null) {
				args[i] = "";
			}
		}
		return String.format(format, args);
	}
	
}
