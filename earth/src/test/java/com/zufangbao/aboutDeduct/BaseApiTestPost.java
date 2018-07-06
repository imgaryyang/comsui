package com.zufangbao.aboutDeduct;

import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.util.*;
import java.util.Map.Entry;


//public class BaseApiTestPost extends AbstractTransactionalJUnit4SpringContextTests{
public class BaseApiTestPost {
//	/** 本地测试用 **/
//	public static final String QUERY_URL_TEST = "http://127.0.0.1:9090/api/query";
//	public static final String MODIFY_URL_TEST = "http://cbhb.5veda.net//api/modify";
//	public static final String COMMAND_URL_TEST = "http://127.0.0.1:9090/api/command";

//	public static final String COMMAND_URL_TEST = "http://cbhb.5veda.net:8082/api/command";
	/** 测试发布用 **/
//	public static final String REPURCHASE=	"http://yunxin.5veda.net/api/v2/repurchase";
//	public static final String QUERY_URL_TEST = "http://yunxin.5veda.net/api/query";
//	public static final String MODIFY_URL_TEST = "http://yunxin.5veda.net/api/modify";
//	public static final String COMMAND_URL_TEST = "http://deduct.5veda.net/api/command";
//	public static final String DEDUCT_QUERY_TEST = "http://121.40.230.133:29084/api/query";
//	public static final String REMITTANCECOMMAND_URL_TEST = "http://remittance.5veda.net/api/command";
//	public static final String REPAYMENTORDER = "http://contra.5veda.net/api/v2/repaymentOrder";
//	public static final String PAYMENTORDERONLINE = "http://contra.5veda.net/api/v2/paymentOrder";
//
//
//	public static final String IMPORT_PACKAGE_ASYNC = "http://contra.5veda.net/api/v3/asyncImportAssetPackage";
//	public static final String MODIFY_OVERDUE_FEE = "http://contra.5veda.net/api/v3/modifyOverDueFee";
//	public static final String MUTABLE_FEE = "http://contra.5veda.net/api/v3/mutableFee";
//	public static final String PREPAYMENT = "http://contra.5veda.net/api/v3/prepayment";
//	public static final String IMPORT_PACKAGE = "http://contra.5veda.net/api/v3/importAssetPackage";
//	public static final String MODIFY_REPAYMENT = "http://contra.5veda.net/api/v3/modifyRepaymentPlan";
//	public static final String MODIFY_REPAYMENT_INFORMATION = "http://contra.5veda.net/api/v3/modifyRepaymentInformation";
//	public static final String AVTIVE_VOUCHER = "http://contra.5veda.net/api/v3/active-payment-vouchers/submit";
//	public static final String ACTIVE_VOUCHER_UNDO = "http://contra.5veda.net/api/v3/active-payment-vouchers/undo";
//	public static final String VOUCHER = "http://contra.5veda.net/api/v3/business-payment-vouchers/submit";
//	public static final String VOUCHER_UNDO = "http://contra.5veda.net/api/v3/business-payment-vouchers/undo";
//	public static final String UPLODE = "http://contra.5veda.net/api/v3/pre/api/upload/{channelCode}/{serviceCode}";
//	public static final String THIRD_VOUCHER = "http://contra.5veda.net/api/v3/third-part-vouchers/submit";

	/**内网测试**/
	public static final String REPURCHASE=	"http://192.168.0.159:30104/api/v2/repurchase";
	public static final String QUERY_URL_TEST=	"http://192.168.0.159:30104/api/query";
	public static final String MODIFY_URL_TEST = "http://192.168.0.159:30104/api/modify";
	public static final String COMMAND_URL_TEST = "http://192.168.0.159:30102/api/command";
//	public static final String COMMAND_URL_TEST = "http://192.168.0.212:8083/api/command";
//	public static final String URL_V3_TEST = "http://192.168.0.212:17778/api/v3";
	/*remittance*/
	public static final String REMITTANCECOMMAND_URL_TEST = "http://192.168.1.211:18085/api/command";
	public static final String DEDUCT_QUERY_URL_TEST=	"http://192.168.1.211:8083/api/query";
	public static final String REPAYMENTORDER = "http://192.168.0.159:30109/api/v2/repaymentOrder";
	public static final String PAYMENTORDERONLINE = "http://192.168.0.159:30109/api/v2/paymentOrder";

	public static final String MODIFY_OVERDUE_FEE = "http://192.168.0.159:30109/api/v3/modifyOverDueFee";
	public static final String MUTABLE_FEE = "http://192.168.0.159:30109/api/v3/mutableFee";
	public static final String PREPAYMENT = "http://192.168.0.159:30109/api/v3/prepayment";
	public static final String IMPORT_PACKAGE = "http://192.168.0.159:30109/api/v3/importAssetPackage";
	public static final String IMPORT_PACKAGE_ASYNC = "http://192.168.0.159:30109/api/v3/asyncImportAssetPackage";
	public static final String MODIFY_REPAYMENT = "http://192.168.0.159:30109/api/v3/modifyRepaymentPlan";
	public static final String MODIFY_REPAYMENT_INFORMATION = "http://192.168.0.159:30109/api/v3/modifyRepaymentInformation";
	public static final String AVTIVE_VOUCHER = "http://192.168.0.159:30109/api/v3/active-payment-vouchers/submit";
	public static final String ACTIVE_VOUCHER_UNDO = "http://192.168.0.159:30109/api/v3/active-payment-vouchers/undo";
	public static final String VOUCHER = "http://192.168.0.159:30109/api/v3/business-payment-vouchers/submit";
	public static final String VOUCHER_UNDO = "http://192.168.0.159:30109/api/v3/business-payment-vouchers/undo";
	public static final String UPLODE = "http://192.168.0.159:30109/api/v3/pre/api/upload/{channelCode}/{serviceCode}";
	public static final String THIRD_VOUCHER = "http://192.168.0.159:30109/api/v3/third-part-vouchers/submit";

//	public static final String THIRD_VOUCHER_2 = "http://192.168.0.159:30109/api/v3/importAssetPackage";
//	public static final String REPAYMENTORDER_2 = "http://192.168.0.159:30109/api/v2/repaymentOrder";




	 
	 
	public static final String TEST_MERID = "t_test_zfb";
	public static final String TEST_SECRET = "123456";
	
	/** t_merchant的私钥 **/
	public  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

	
	public Map<String, String> getIdentityInfoMap(Map<String, String> requestParams) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("merId", TEST_MERID);
		headers.put("secret", TEST_SECRET);
		String signContent = ApiSignUtils.getSignCheckContent(requestParams);
		System.out.println(signContent);
		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
		headers.put("sign", sign);
		return headers;
	}
	
	public String buildParams(Map<String, String> params) {
		StringBuffer buffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (Entry<String, String> entry : params.entrySet()) {
			String value = entry.getValue();
			value = StringUtils.isEmpty(value) ? "" : value;
			buffer.append(entry.getKey() + "=" + value + "&");
		}
		return buffer.toString();
	}
	
}

