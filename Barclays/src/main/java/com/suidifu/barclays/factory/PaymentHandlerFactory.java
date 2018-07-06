package com.suidifu.barclays.factory;

import java.util.HashMap;
import java.util.Map;

import com.suidifu.coffer.entity.PaymentGateWayNameSpace;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.suidifu.coffer.handler.ThirdPartyPayPacketHandler;


public class PaymentHandlerFactory {
	
	public final static Map<String, String> bankCodeSuperBankHandlerBeanNameMapper = new HashMap<String, String>() {
		{

			put(PaymentGateWayNameSpace.GATEWAY_TYPE_SUPER_BANK_TEST, "TestPaymentGateWaySuperBankHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_UNIONPAY_TEST, "TestPaymentGateWayUnionpayHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_DIRECT_BANK_CMB, "cmbDirectBankPaymentHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_DIRECT_BANK_PAB, "pabDirectBankPaymentHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_UNIONPAY_GZ, "gzUnionPaymentHandlerImpl");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_ElECPAY_BAOFU, "baofuPaymentHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_FAST_PAY_PAB, "pabFastPayPaymentHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_ElECPAY_CPCN, "cpcnPaymentHandler");
			put(PaymentGateWayNameSpace.GATEWAY_TYPE_DIRECT_BANK_CCB, "ccbDirectBankPaymentHandler");
		}
	};


	public static HashMap<String,PaymentGateWayNameSpace> paymentHanlderLib =new HashMap()
	{{
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_BAOFU,new PaymentGateWayNameSpace("baofuHandler", "baofuPacketHandler"));
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_CPCN,new PaymentGateWayNameSpace("cpcnHandler", "cpcnPacketHandler"));
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_UNIONPAYGZ,new PaymentGateWayNameSpace("GZUnoinPayHandler", "gzUnoinPayPacketHanlder"));
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_SINA_PAY,new PaymentGateWayNameSpace("sinapayHandler", "sinapayPacketHandler"));
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_UCF_PAY,new PaymentGateWayNameSpace("ucfPayHandler", "ucfPayPacketHandler"));
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_YIJIFU,new PaymentGateWayNameSpace("yiJiFuHandler", "yiJiFuPacketHandler"));
		put(PaymentGateWayNameSpace.GATEWAY_TYPE_JIANHANG,new PaymentGateWayNameSpace("sxCcb1Handler", "sxCcb1PacketHandler"));
	}};

/*	public static <T extends PaymentHandler> T newInstance(String gatewayName) {
		String beanName = bankCodeSuperBankHandlerBeanNameMapper.getOrDefault(
				gatewayName, "");
		if(StringUtils.isEmpty(beanName)) return null;
		return SpringContextUtil.getBean(beanName);
	}*/

	public static ThirdPartyPayHandler createThirdPartyPay(PaymentGateWayNameSpace paymentGateNameSpace) throws Exception {
		if (null == paymentGateNameSpace) {
			throw new Exception("get ThirdPartyPayQueryHandler error : PaymentGateWayNameSpace is null.");
		}
		String handlerType= paymentGateNameSpace.getHandlerName();
		return getBean(handlerType,ThirdPartyPayHandler.class);
	}

	private static <T> T getBean(String handlerType,Class<T> clazz) throws Exception {
		if(com.demo2do.core.utils.StringUtils.isEmpty(handlerType)) {
			throw new Exception("get " + clazz.getSimpleName() + " error : handlerType is empty.");
		}
		Object handler =  com.suidifu.coffer.util.SpringContextUtil.getApplicationContext().getBean(handlerType);
		if(handler==null) {
			throw new Exception("get " + clazz.getSimpleName() + " error : no bean named [" + handlerType + "].");
		}
		return clazz.cast(handler);
	}

	public static ThirdPartyPayPacketHandler createThirdPartyPayPacket(PaymentGateWayNameSpace paymentGateNameSpace) throws Exception {
		if (null == paymentGateNameSpace) {
			throw new Exception("get ThirdPartyPayPacketHandler error : PaymentGateWayNameSpace is null.");
		}
		String handlerType= paymentGateNameSpace.getPacketHandlerName();
		return getBean(handlerType,ThirdPartyPayPacketHandler.class);
	}

	public static PaymentGateWayNameSpace getPaymentNameSpace(String paymentGateWayName) {
		return paymentHanlderLib.get(paymentGateWayName);
	}
}
