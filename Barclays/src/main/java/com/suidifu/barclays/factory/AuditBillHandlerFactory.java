package com.suidifu.barclays.factory;

import com.suidifu.barclays.handler.AuditBillHandler;
import com.zufangbao.sun.utils.SpringContextUtil;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AuditBillHandlerFactory {

	public static String CHANNEL_IDENTITY_ElECPAY_BAOFU="CHANNEL_IDENTITY_ElECPAY_BAOFU";
	
	public static String CHANNEL_IDENTITY_ElECPAY_GZUNION="CHANNEL_IDENTITY_ElECPAY_GZUNION";
	
	public static String CHANNEL_IDENTITY_ElECPAY_CPCN="CHANNEL_IDENTITY_ElECPAY_CPCN";

	public static String CHANNEL_IDENTITY_ElECPAY_SINAPAY="CHANNEL_IDENTITY_ElECPAY_SINAPAY";
	
	public static String CHANNEL_IDENTITY_ElECPAY_YIJIFU = "CHANNEL_IDENTITY_ElECPAY_YIJIFU";
	
	public static String CHANNEL_IDENTITY_ElECPAY_TONGLIAN = "CHANNEL_IDENTITY_ElECPAY_TONGLIAN";

    public static String CHANNEL_IDENTITY_ElECPAY_SXCCB = "CHANNEL_IDENTITY_ElECPAY_SXCCB";

	public static String CHANNEL_IDENTITY_ElECPAY_YEEPAY = "CHANNEL_IDENTITY_ElECPAY_YEEPAY";

	public static String CHANNEL_IDENTITY_ElECPAY_UFCPAY = "CHANNEL_IDENTITY_ElECPAY_UFCPAY";

    public static String CHANNEL_IDENTITY_ElECPAY_LDYS = "CHANNEL_IDENTITY_ElECPAY_LDYS";

    public static String CHANNEL_IDENTITY_ElECPAY_EPAY = "CHANNEL_IDENTITY_ElECPAY_EPAY";

    public static String CHANNEL_IDENTITY_ELECPAY_YEEPAY_FTP = "CHANNEL_IDENTITY_ELECPAY_YEEPAY_FTP";

	private final static Map<String, String> channelIdentityAuditBillHandlerBeanNameMapper = new HashMap<String, String>() {

		{
			put(CHANNEL_IDENTITY_ElECPAY_BAOFU, "baofuAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_GZUNION, "gzUnionAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_CPCN, "cpcnAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_SINAPAY, "sinapayAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_YIJIFU, "yijifuAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_TONGLIAN, "tonglianAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_SXCCB, "sxCcbAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_YEEPAY, "yeePayAuditBillHandler");
			put(CHANNEL_IDENTITY_ElECPAY_UFCPAY, "ufcPayAuditBillHandler");
            put(CHANNEL_IDENTITY_ElECPAY_LDYS, "ldysAuditBillHandler");
            put(CHANNEL_IDENTITY_ElECPAY_EPAY, "epayAuditBillHandler");
            put(CHANNEL_IDENTITY_ELECPAY_YEEPAY_FTP, "yeePayFtpPullAuditHandler");
        }
	};

	public static <T extends AuditBillHandler> T newInstance(String channelIdentity) {
		String beanName = channelIdentityAuditBillHandlerBeanNameMapper.getOrDefault(
				channelIdentity, "");
		if(StringUtils.isEmpty(beanName)) return null;
		return SpringContextUtil.getBean(beanName);
	}
}
