package com.suidifu.jpmorgan.spec;

public class TradeScheduleHandlerSpec {
	
	public static final String DistributeContentKey = "tradeInfo";

	public static final String DistributeURLKey = "distributeURLKey";
	
	public static final String BusinessStatusUpdateURLKey = "businessStatusUpdateURL";
	
	public static final String TradeUuidCallBackKey = "tradeUuid";
	
	public static final String PaymentOrderQueueTableNameKey = "paymentOrderQueueTableName";
	
	public static final String PaymentOrderLogTableNameKey = "paymentOrderLogTableName";
	
	public static final String QueueIndexKey = "queueIndex";
	
	public static final String InnerCallbackInfoKey = "innerCallbackInfo";
	
	public static final String GatewayNameKey = "gatewayName";
	
	public class ErrorMsg {
		
		public static final String MSG_EMPTY_PACKET_ERROR = "空报文！";
		
		public static final String MSG_FORMAT_PACKET_ERROR = "报文格式错误！";
		
		public static final String MSG_EMPTY_NECESSSARY_ATTR = "非空字段为空！";
		
		public static final String MSG_NOT_EXIST_CHANNEL_UUID = "channel uuid 不存在！";
		
		public static final String MSG_REJECT_SUBMIT_ERROR = "拒绝提交该支付请求, :";
		
		public static final String MSG_NO_GATEWAY_CONFIG = "找不到对应通道配置！";
		
		public static final String MSG_NO_DISTRIBUTEURL_CONFIG = "找不到distributeURL配置！";
		
		public static final String MSG_NOT_COMPLETE_SLOT_INFO= "不完整的Slot信息！";
		
		public static final String MSG_NOT_EXIST_ERROR = "找不到对应的交易记录！";
		
		public static final String MSG_NO_INFO_ERROR = "找不到查询信息！";
	}

}
