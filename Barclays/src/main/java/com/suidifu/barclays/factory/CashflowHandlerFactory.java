package com.suidifu.barclays.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.suidifu.barclays.handler.CashflowHandler;
import com.zufangbao.sun.utils.SpringContextUtil;

public class CashflowHandlerFactory {

	public static String CHANNEL_IDENTITY_BANKCORP_PAB = "CHANNEL_IDENTITY_BANKCORP_PAB";
	public static String CHANNEL_IDENTITY_BANKCORP_CMB = "CHANNEL_IDENTITY_BANKCORP_CMB";
	public static String CHANNEL_IDENTITY_BANKCORP_CCB = "CHANNEL_IDENTITY_BANKCORP_CCB";
	public static String CHANNEL_IDENTITY_BANKCORP_SPDB = "CHANNEL_IDENTITY_BANKCORP_SPDB";
	public static String CHANNEL_IDENTITY_BANKCORP_HUAXING = "CHANNEL_IDENTITY_BANKCORP_HUAXING";
	public static String CHANNEL_IDENTITY_BANKCORP_CMBC = "CHANNEL_IDENTITY_BANKCORP_CMBC";
	public static String CHANNEL_IDENTITY_BANKCORP_BOSC = "CHANNEL_IDENTITY_BANKCORP_BOSC";
	public static String CHANNEL_IDENTITY_BANKCORP_NJBC = "CHANNEL_IDENTITY_BANKCORP_NJBC";
	public static String CHANNEL_IDENTITY_BANKCORP_WFCCB = "CHANNEL_IDENTITY_BANKCORP_WFCCB";

	public final static Map<String, String> channelIdentityCashflowHandlerBeanNameMapper = new HashMap<String, String>() {

		{
			put(CHANNEL_IDENTITY_BANKCORP_PAB, "pabCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_CMB, "cmbCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_CCB, "ccbCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_SPDB, "spdbCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_HUAXING, "huaxingCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_CMBC, "cmbcCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_BOSC, "boscCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_NJBC, "njbcCashflowHandler");
			put(CHANNEL_IDENTITY_BANKCORP_WFCCB, "wfccbCashflowHandler");
		}
	};

	public static <T extends CashflowHandler> T newInstance(String channelIdentity) {
		String beanName = channelIdentityCashflowHandlerBeanNameMapper.getOrDefault(
				channelIdentity, "");
		if(StringUtils.isEmpty(beanName)) return null;
		return SpringContextUtil.getBean(beanName);
	}
}
