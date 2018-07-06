package com.zufangbao.testAPIWuBo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.testAPIWuBo.testAPI.models.ContractDetail;
import com.zufangbao.testAPIWuBo.testAPI.models.ImportAssetPackageContent;
import com.zufangbao.testAPIWuBo.testAPI.models.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.util.*;

public class 	PrepaymentCucumberMethod {
	
	private  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
	/**
	 * 内网测试
	 */
//	String earth_Modify_Url = "http://192.168.0.128/api/modify";
//  	String queryUrl=	"http://192.168.0.128/api/query";
//	String modifyUrl = "http://192.168.0.128:17778/api/v3";
//	String ModifyUrl="http://192.168.0.128/api/modify";
//	String deductUrl = "http://192.168.0.128:8083/api/command";
//	String deductUrl_NEW = "http://192.168.0.128:8083/pre/api/DEDUCT/yunxin/YX-SingleDeduct";
	String INT_URL_PREPAYMENT = "http://192.168.0.128:7778/api/v3/prepayment";
	/*remittance*/
	String remittanceUrl = "http://192.168.0.128:18085/api/command";
//	//中航放款地址
//	String remittanceUrl1 = "http://192.168.0.128:38084";
	/**
	 外网测试
 	 **/
	String ModifyUrl = "http://yunxin.5veda.net/api/modify";
	String queryUrl = "http://yunxin.5veda.net/api/query";
	String modifyUrl = "http://contra.5veda.net/api/v3";
//	String INT_URL_PREPAYMENT = "http://contra.5veda.net/api/v3/prepayment";
	String deductUrl = "http://deduct.5veda.net/api/command";
//	public static final String DEDUCT_QUERY_TEST = "http://121.40.230.133:29084/api/query";
//	String remittanceUrl ="http://remittance.5veda.net/api/command";


	/**     
	 * 本地测试
//	 */
//	String queryUrl="http://192.168.0.176:9090/api/query";
//	String deductUrl="http://192.168.0.176:9091/api/command";
// 	//String modifyUrl = "http://192.168.0.177:9090/api/modify";
//	String remittanceUrl = "http://192.168.0.185:9092/api/command";

	/**
	 work1测试
	 **/
//	String queryUrl = "http://yunxin.5veda.net/api/query";
//	String modifyUrl = "http://contra.5veda.net/api/v3";
//	String INT_URL_PREPAYMENT = "192.168.0.159:38089/api/v3/prepayment";
//	String deductUrl = "http://deduct.5veda.net/api/command";
	//	public static final String DEDUCT_QUERY_TEST = "http://121.40.230.133:29084/api/query";
//	String remittanceUrl ="http://192.168.0.159:38083/api/command";

/**
	 * 变更还款计划
	 */
	public static final String URL_MODIFY_REPAYMENT_PLAN = "/modifyRepaymentPlan";

	/**
	 * 导入资产包
	 */
	public static final String URL_IMPORT_ASSET_PACKAGE = "/importAssetPackage";

	/**
	 * 变更逾期费用
	 */
	public static final String URL_MODIFY_OVER_DUE_FEE = "/modifyOverDueFee";

	/**
	 * 费用浮动
	 */
	public static final String URL_MUTABLE_FEE = "/mutableFee";

	/**
	 * 提前还款
	 */
	public static final String URL_PREPAYMENT = "/prepayment";

	 /**
	 * 放款
	 * @param productCode
	 * @param uniqueId
	 * @param amount
	 */
	public void makeLoan(String productCode,String uniqueId,String amount){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", uniqueId);
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditor1");
		requestParams.put("auditTime", "2016-08-20 00:00:00");
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6228480444455553333";//宝付的账户6228480444455553333  中金的账户 6222020200002432
//		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10103','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'王宝','bankProvince':'310000','bankCity':'310100','bankName':'中国农业银行','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	广银联通道放款
 	*/
	public void makeLoan1(String productCode,String uniqueId,String amount){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", uniqueId);
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2016-08-20 00:00:00");
		requestParams.put("remark", "交易备注");
		requestParams.put("notifyUrl","http://101.52.128.166/Loan/PaidNotic");
		String bankCardNo = "6214855712106520";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	中金通道放款
 	*/

	public void makeLoan2(String productCode,String uniqueId,String amount){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", uniqueId);
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditorName1");
		requestParams.put("auditTime", "2016-08-20 00:00:00");
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6222020200002430";
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//宝付放款
	public void makeLoan3(String productCode,String uniqueId,String amount){
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("remittanceStrategy", "0");
		requestParams.put("productCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", uniqueId);
		requestParams.put("plannedRemittanceAmount", amount);
		requestParams.put("auditorName", "auditor1");
		requestParams.put("auditTime", "2016-08-20 00:00:00");
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6228480444455553330";
//		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10103','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'王宝','bankProvince':'310000','bankCity':'310100','bankName':'中国农业银行','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//中行广银联放款
	public void testApiZHCommandRemittance(String requestNo,String remittanceId,String uniqueId,String amount,String productCode) {
		for(int i = 0; i<1; i++) {
			String out_url = "http://avictc.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application";
			String in_url = "http://192.168.0.128:38084/pre/api/remittance/zhonghang/remittance-application";
			String local_url = "http://192.168.0.185:9092/pre/api/remittance/zhonghang/remittance-application";
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", requestNo);
			requestParams.put("remittanceId",remittanceId);
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("contractAmount",amount);
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-06-19 00:00:00");
			requestParams.put("remark", "321");
			requestParams.put("notifyUrl","http://www.mocky.io/v2/5185415ba171ea3a00704eed");
			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankAliasName':'NJCB','bankCardNo':'6228481198120540000','bankAccountHolder':'WUBO','bankProvince':'410000','bankCity':'410000','idNumber':'idNumber1'}]");
			try {
				String sr = PostTestUtil.sendPost(out_url, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
				System.out.println(sr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void testApiZHCommandRemittance1(String requestNo,String uniqueId,String amount,String productCode) {
		for(int i = 0; i<1; i++) {
			String out_url = "http://avictc.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application";
			String in_url = "http://192.168.0.128:38084/pre/api/remittance/zhonghang/remittance-application";
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo", requestNo);
			requestParams.put("remittanceId","zhanglongfei1133");
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("contractAmount","1");
			requestParams.put("plannedRemittanceAmount", "1");
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2017-06-19 00:00:00");
			requestParams.put("remark", "321");
			requestParams.put("notifyUrl","http://www.baidu.com");
			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankAliasName':'CMB','bankCardNo':'6214855712106521','bankAccountHolder':'范腾','bankProvince':'bankProvince1','bankCity':'bankCity1','idNumber':'idNumber1'}]");
			try {
				String sr = PostTestUtil.sendPost("http://avictc.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application", requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
				System.out.println(sr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/*
	浦发银企直联放款
 	*/

	public void makeLoan4(String productCode,String uniqueId,String amount){
		for (int i = 0; i <1 ; i++) {
			String requestNo = UUID.randomUUID().toString().substring(0,16);
			uniqueId = UUID.randomUUID().toString().substring(0,16);
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo",requestNo );
			requestParams.put("remittanceId","rmrf001");
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2016-08-20 00:00:00");
			requestParams.put("remark", "交易备注");
			String bankCardNo = "6224080002395";//浦发个人 6225213600006188 浦发对公账户 6224080002395
//			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),"yyyy-MM-dd HH:mm:ss")+"','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'wubo111111','bankProvince':'340000','bankCity':'340100','bankName':'中国工商银行','idNumber':'idNumber1'}]");
//			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),"yyyy-MM-dd HH:mm:ss")+"','bankCode':'C10310','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'浦发1228469525','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]");
			requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),"yyyy-MM-dd HH:mm:ss")+"','bankCode':'C10310','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'浦发2000939176','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]");

			try {
				String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://192.168.0.105:9092/api/command", requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));

				System.out.println("放款请求号："+requestNo);
				System.out.println("uniqueId："+uniqueId);
				System.out.println(sr);
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	平安银企直联放款
 	*/

	public void makeLoan5(String productCode,String uniqueId,String amount){
		for (int i = 0; i <1 ; i++) {
			String requestNo = UUID.randomUUID().toString().substring(0,16);
			uniqueId = UUID.randomUUID().toString().substring(0,16);
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo",requestNo );
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2016-08-20 00:00:00");
			requestParams.put("remark", "交易备注");
			String bankCardNo = "62240806079534689";//浦发个人 6225213600006188 浦发对公账户 62240806079534689
			//requestParams.put("remittanceDetails", "[{\"detailNo\":\"detailNo1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"stdBankCode\":\"102100031681\",\"bankCode\":\"C10102\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"wubo\",\"bankProvince\":\"340000\",\"bankCity\":\"340100\",\"bankName\":\"中国工商银行\",\"idNumber\":\"idNumber1\"}]");
//			requestParams.put("remittanceDetails", "[{\"detailNo\":\"detailNo1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"bankCode\":\"C10102\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"wubo\",\"bankProvince\":\"340000\",\"bankCity\":\"340100\",\"bankName\":\"中国工商银行股份有限公司北京玉林支行\",\"idNumber\":\"idNumber1\"}]");
			//requestParams.put(\"remittanceDetails\", \"[{\"detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发1228469525','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");
			//requestParams.put(\"remittanceDetails\", \"[{'detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发2000040179','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");

			try {
				String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
				System.out.println("放款请求号："+requestNo);
				System.out.println("uniqueId："+uniqueId);
				System.out.println(sr);
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	*民生银企直联放款
 	*/

	public void makeLoan6(String productCode,String uniqueId,String amount){
		for (int i = 0; i <1 ; i++) {
			String requestNo = UUID.randomUUID().toString();
			uniqueId = UUID.randomUUID().toString();
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo",requestNo );
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2016-08-20 00:00:00");
			requestParams.put("remark", "交易备注");
			String bankCardNo = "600023820";//浦发个人 6225213600006188 浦发对公账户 62240806079534689
			requestParams.put("remittanceDetails", "[{\"detailNo\":\"Minsheng1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"stdBankCode\":\"102100031681\",\"bankCode\":\"C10305\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"测试2200002697\",\"bankProvince\":\"330000\",\"bankCity\":\"330100\",\"bankName\":\"民生银行\",\"idNumber\":\"idNumber1\"}]");
//			requestParams.put("remittanceDetails", "[{\"detailNo\":\"detailNo1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"bankCode\":\"C10102\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"wubo\",\"bankProvince\":\"340000\",\"bankCity\":\"340100\",\"bankName\":\"中国工商银行股份有限公司北京玉林支行\",\"idNumber\":\"idNumber1\"}]");
			//requestParams.put(\"remittanceDetails\", \"[{\"detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发1228469525','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");
			//requestParams.put(\"remittanceDetails\", \"[{'detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发2000040179','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");

			try {
				String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
				System.out.println("放款请求号："+requestNo);
				System.out.println("uniqueId："+uniqueId);
				System.out.println(sr);
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	*上海银行银企直联放款
 	*/

	public void makeLoan7(String productCode,String uniqueId,String amount){
		for (int i = 0; i <1 ; i++) {
			String requestNo = UUID.randomUUID().toString();
			uniqueId = UUID.randomUUID().toString();
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo",requestNo );
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2016-08-20 00:00:00");
			requestParams.put("remark", "交易备注");
			String bankCardNo = "31663803002771248";//浦发个人 6225213600006188 浦发对公账户 62240806079534689
			requestParams.put("remittanceDetails", "[{\"detailNo\":\"shanghaiytest1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"bankCode\":\"C10912\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"宁波惠康国际工业有限公司\",\"bankProvince\":\"330000\",\"bankCity\":\"330100\",\"bankName\":\"上海银行股份有限公司\",\"idNumber\":\"idNumber1\"}]");
//			requestParams.put("remittanceDetails", "[{\"detailNo\":\"detailNo1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"bankCode\":\"C10102\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"wubo\",\"bankProvince\":\"340000\",\"bankCity\":\"340100\",\"bankName\":\"中国工商银行股份有限公司北京玉林支行\",\"idNumber\":\"idNumber1\"}]");
			//requestParams.put(\"remittanceDetails\", \"[{\"detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发1228469525','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");
			//requestParams.put(\"remittanceDetails\", \"[{'detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发2000040179','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");

			try {
				String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
				System.out.println("放款请求号："+requestNo);
				System.out.println("uniqueId："+uniqueId);
				System.out.println(sr);
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	*南京银行银企直联放款
 	*/

	public void makeLoan9(String productCode,String uniqueId,String amount){
		for (int i = 0; i <1 ; i++) {
			String requestNo = UUID.randomUUID().toString();
			uniqueId = UUID.randomUUID().toString();
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "300002");
			requestParams.put("requestNo",requestNo );
			requestParams.put("remittanceStrategy", "0");
			requestParams.put("productCode", productCode);
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("contractNo", uniqueId);
			requestParams.put("plannedRemittanceAmount", amount);
			requestParams.put("auditorName", "auditorName1");
			requestParams.put("auditTime", "2016-08-20 00:00:00");
			requestParams.put("remark", "交易备注");
			String bankCardNo = "6221730001000637823";//浦发个人 6225213600006188 浦发对公账户 62240806079534689
			requestParams.put("remittanceDetails", "[{\"detailNo\":\"shanghaiytest1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"bankCode\":\"C10868\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"里孝厌\",\"bankProvince\":\"330000\",\"bankCity\":\"330100\",\"bankName\":\"上海银行股份有限公司\",\"idNumber\":\"idNumber1\"}]");
//			requestParams.put("remittanceDetails", "[{\"detailNo\":\"detailNo1\",\"recordSn\":\"1\",\"amount\":\""+amount+"\",\"plannedDate\":\"\",\"bankCode\":\"C10102\",\"bankCardNo\":\""+bankCardNo+"\",\"bankAccountHolder\":\"wubo\",\"bankProvince\":\"340000\",\"bankCity\":\"340100\",\"bankName\":\"中国工商银行股份有限公司北京玉林支行\",\"idNumber\":\"idNumber1\"}]");
			//requestParams.put(\"remittanceDetails\", \"[{\"detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发1228469525','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");
			//requestParams.put(\"remittanceDetails\", \"[{'detailNo':'detailNo1','recordSn':'1','amount':'\"+amount+\"','plannedDate':'\"+DateUtils.format(new Date(System.currentTimeMillis() + 10000),\"yyyy-MM-dd HH:mm:ss\")+\"','bankCode':'C10310','bankCardNo':'\"+bankCardNo+\"','bankAccountHolder':'浦发2000040179','bankProvince':'340000','bankCity':'340100','bankName':'上海浦东发展银行','idNumber':'idNumber1'}]\");

			try {
				String sr = PostTestUtil.sendPost(remittanceUrl, requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
				System.out.println("放款请求号："+requestNo);
				System.out.println("uniqueId："+uniqueId);
				System.out.println(sr);
				//Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 导入资产包
	 * @param totalAmount
	 * @param productCode
	 * @param uniqueId
	 * @param repaymentAccountNo
	 * @param amount
	 * @param firstRepaymentDate
	 * @param secondRepaymentDate
	 * @param thirdRepaymentDate
	 */
	public void importAssetPackage(String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
		for (int index = 0; index < 1; index++) {
			ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
			importAssetPackageContent.setThisBatchContractsTotalNumber(1);
			importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
			importAssetPackageContent.setFinancialProductCode(productCode);

			List<ContractDetail> contracts = new ArrayList<ContractDetail>();
			ContractDetail contractDetail = new ContractDetail();
			contractDetail.setUniqueId(uniqueId);
			contractDetail.setLoanContractNo(uniqueId);

			contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
			contractDetail.setLoanCustomerName("王宝");
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
			contractDetail.setIDCardNo("320301198502169142");
			contractDetail.setBankCode("C10103");
			contractDetail.setBankOfTheProvince("110000");
			contractDetail.setBankOfTheCity("110100");
			contractDetail.setRepaymentAccountNo("622848044445555333"+repaymentAccountNo);
			contractDetail.setLoanTotalAmount(totalAmount);
			contractDetail.setLoanPeriods(3);
			contractDetail.setEffectDate(DateUtils.format(new Date()));
			contractDetail.setExpiryDate("2018-01-01");
			contractDetail.setLoanRates("0.156");
			contractDetail.setInterestRateCycle(1);
			contractDetail.setPenalty("0.0005");
			contractDetail.setRepaymentWay(2);

			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
			repaymentPlanDetail1.setRepaymentPrincipal(amount);
			repaymentPlanDetail1.setRepaymentInterest("0.00");
			repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
			repaymentPlanDetail1.setOtheFee("0.00");
			repaymentPlanDetail1.setTechMaintenanceFee("0.00");
			repaymentPlanDetail1.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail1);
			
			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
			repaymentPlanDetail2.setRepaymentPrincipal(amount);
			repaymentPlanDetail2.setRepaymentInterest("0.00");
			repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail2.setOtheFee("0.00");
			repaymentPlanDetail2.setTechMaintenanceFee("0.00");
			repaymentPlanDetail2.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail2);
			
			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
			repaymentPlanDetail3.setRepaymentPrincipal(amount);
			repaymentPlanDetail3.setRepaymentInterest("0.00");
			repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
			repaymentPlanDetail3.setOtheFee("0.00");
			repaymentPlanDetail3.setTechMaintenanceFee("0.00");
			repaymentPlanDetail3.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail3);
			
			contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

			contracts.add(contractDetail);
			importAssetPackageContent.setContractDetails(contracts);
			
			String param =  JsonUtils.toJsonString(importAssetPackageContent);
			System.out.println(param);
			Map<String,String> params =new HashMap<String,String>(); 
			params.put("fn", "200004");
			params.put("requestNo", UUID.randomUUID().toString());
			params.put("importAssetPackageContent", param); 
					
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("merId", "t_test_zfb");
			headers.put("secret", "123456");
			String signContent = ApiSignUtils.getSignCheckContent(params);
			String sign = ApiSignUtils.rsaSign(signContent, privateKey);
			System.out.println(sign);
			headers.put("sign", sign);
			
			try {
				String sr = PostTestUtil.sendPost(modifyUrl+URL_IMPORT_ASSET_PACKAGE, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	/*
	广银联通道
	 */
	public void importAssetPackage1(String repayScheduleNo,String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
		for (int index = 0; index < 1; index++) {
			ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
			importAssetPackageContent.setThisBatchContractsTotalNumber(1);
			importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
			importAssetPackageContent.setFinancialProductCode(productCode);

			List<ContractDetail> contracts = new ArrayList<ContractDetail>();
			ContractDetail contractDetail = new ContractDetail();
			contractDetail.setUniqueId(uniqueId);
			contractDetail.setLoanContractNo(uniqueId);

			contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
			contractDetail.setLoanCustomerName("章光");
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
			contractDetail.setIDCardNo("320301198502169142");
			contractDetail.setBankCode("C10103");//C10105 C10403
			contractDetail.setBankOfTheProvince("110000");
			contractDetail.setBankOfTheCity("110100");
			contractDetail.setRepaymentAccountNo("622588121251757643"+repaymentAccountNo);
			contractDetail.setLoanTotalAmount(totalAmount);
			contractDetail.setLoanPeriods(3);
			contractDetail.setEffectDate(DateUtils.format(new Date()));
			contractDetail.setExpiryDate("2099-01-01");
			contractDetail.setLoanRates("0");
			contractDetail.setInterestRateCycle(1);
			contractDetail.setPenalty("0.0005");
			contractDetail.setRepaymentWay(2);

			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
//			repaymentPlanDetail1.setRepayScheduleNo(repayScheduleNo);
			repaymentPlanDetail1.setRepaymentPrincipal(amount);
			repaymentPlanDetail1.setRepaymentInterest("0.00");
			repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
			repaymentPlanDetail1.setOtheFee("0.00");
			repaymentPlanDetail1.setTechMaintenanceFee("0.00");
			repaymentPlanDetail1.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail1);

			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
//			repaymentPlanDetail2.setRepayScheduleNo(repayScheduleNo+"1");
			repaymentPlanDetail2.setRepaymentPrincipal(amount);
			repaymentPlanDetail2.setRepaymentInterest("0.00");
			repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail2.setOtheFee("0.00");
			repaymentPlanDetail2.setTechMaintenanceFee("0.00");
			repaymentPlanDetail2.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail2);

			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
//			repaymentPlanDetail3.setRepayScheduleNo(repayScheduleNo+"2");
			repaymentPlanDetail3.setRepaymentPrincipal(amount);
			repaymentPlanDetail3.setRepaymentInterest("0.00");
			repaymentPlanDetail3.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail3.setOtheFee("0.00");
			repaymentPlanDetail3.setTechMaintenanceFee("0.00");
			repaymentPlanDetail3.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail3);

			contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

			contracts.add(contractDetail);
			importAssetPackageContent.setContractDetails(contracts);

			String param =  JsonUtils.toJsonString(importAssetPackageContent);
			System.out.println(param);
			Map<String,String> params =new HashMap<String,String>();
			params.put("fn", "200004");
			params.put("requestNo", UUID.randomUUID().toString());
			params.put("importAssetPackageContent", param);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("merId", "t_test_zfb");
			headers.put("secret", "123456");
			String signContent = ApiSignUtils.getSignCheckContent(params);
			String sign = ApiSignUtils.rsaSign(signContent, privateKey);
			System.out.println(sign);
			headers.put("sign", sign);



			try {
//				String sr = PostTestUtil.sendPost(ModifyUrl,params,headers);
				String sr = PostTestUtil.sendPost(modifyUrl+URL_IMPORT_ASSET_PACKAGE, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void importAssetPackage2(String totalAmount,String productCode,String uniqueId,String repayScheduleNo, String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
		for (int index = 0; index < 1; index++) {
			ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
			importAssetPackageContent.setThisBatchContractsTotalNumber(1);
			importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
			importAssetPackageContent.setFinancialProductCode(productCode);

			List<ContractDetail> contracts = new ArrayList<ContractDetail>();
			ContractDetail contractDetail = new ContractDetail();
			contractDetail.setUniqueId(uniqueId);
			contractDetail.setLoanContractNo(uniqueId);

			contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
			contractDetail.setLoanCustomerName("WUBO");
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
			contractDetail.setIDCardNo("320301198502169142");
			contractDetail.setBankCode("C10105");
			contractDetail.setBankOfTheProvince("110000");
			contractDetail.setBankOfTheCity("110100");
//			contractDetail.setRepaymentAccountNo("621485571210652"+repaymentAccountNo);
			contractDetail.setRepaymentAccountNo("6222020200002432");
			contractDetail.setLoanTotalAmount(totalAmount);
			contractDetail.setLoanPeriods(3);
			contractDetail.setEffectDate(DateUtils.format(new Date()));
			contractDetail.setExpiryDate("2099-01-01");
			contractDetail.setLoanRates("0.156");
			contractDetail.setInterestRateCycle(1);
			contractDetail.setPenalty("0.0005");
			contractDetail.setRepaymentWay(2);

			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
			repaymentPlanDetail1.setRepayScheduleNo(repayScheduleNo+"1");
			repaymentPlanDetail1.setRepaymentPrincipal(amount);
			repaymentPlanDetail1.setRepaymentInterest("0.00");
			repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
			repaymentPlanDetail1.setOtheFee("0.00");
			repaymentPlanDetail1.setTechMaintenanceFee("0.00");
			repaymentPlanDetail1.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail1);

			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
			repaymentPlanDetail2.setRepayScheduleNo(repayScheduleNo+"2");
			repaymentPlanDetail2.setRepaymentPrincipal(amount);
			repaymentPlanDetail2.setRepaymentInterest("0.00");
			repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail2.setOtheFee("0.00");
			repaymentPlanDetail2.setTechMaintenanceFee("0.00");
			repaymentPlanDetail2.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail2);

			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
			repaymentPlanDetail3.setRepayScheduleNo(repayScheduleNo+"3");
			repaymentPlanDetail3.setRepaymentPrincipal(amount);
			repaymentPlanDetail3.setRepaymentInterest("0.00");
			repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
			repaymentPlanDetail3.setOtheFee("0.00");
			repaymentPlanDetail3.setTechMaintenanceFee("0.00");
			repaymentPlanDetail3.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail3);

			contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

			contracts.add(contractDetail);
			importAssetPackageContent.setContractDetails(contracts);

			String param =  JsonUtils.toJsonString(importAssetPackageContent);
			System.out.println(param);
			Map<String,String> params =new HashMap<String,String>();
			params.put("fn", "200004");
			params.put("requestNo", UUID.randomUUID().toString());
			params.put("importAssetPackageContent", param);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("merId", "t_test_zfb");
			headers.put("secret", "123456");
			String signContent = ApiSignUtils.getSignCheckContent(params);
			String sign = ApiSignUtils.rsaSign(signContent, privateKey);
			System.out.println(sign);
			headers.put("sign", sign);

			try {
				String sr = PostTestUtil.sendPost(ModifyUrl, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//宝付-导入资产包
	public void importAssetPackage3(String totalAmount,String productCode,String uniqueId,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
		for (int index = 0; index < 1; index++) {
			ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
			importAssetPackageContent.setThisBatchContractsTotalNumber(1);
			importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
			importAssetPackageContent.setFinancialProductCode(productCode);

			List<ContractDetail> contracts = new ArrayList<ContractDetail>();
			ContractDetail contractDetail = new ContractDetail();
			contractDetail.setUniqueId(uniqueId);
			contractDetail.setLoanContractNo(uniqueId);

			contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
			contractDetail.setLoanCustomerName("王宝");
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
			contractDetail.setIDCardNo("320301198502169142");
			contractDetail.setBankCode("C10103");
			contractDetail.setBankOfTheProvince("110000");
			contractDetail.setBankOfTheCity("110100");
			contractDetail.setRepaymentAccountNo("622848044445555333"+repaymentAccountNo);
			contractDetail.setLoanTotalAmount(totalAmount);
			contractDetail.setLoanPeriods(3);
			//contractDetail.setEffectDate(DateUtils.format(new Date()));
			contractDetail.setEffectDate("2017-06-10");
			contractDetail.setExpiryDate("2018-01-01");
			contractDetail.setLoanRates("0.156");
			contractDetail.setInterestRateCycle(1);
			contractDetail.setPenalty("0.0005");
			contractDetail.setRepaymentWay(2);

			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
			repaymentPlanDetail1.setRepaymentPrincipal(amount);
			repaymentPlanDetail1.setRepaymentInterest("0.00");
			repaymentPlanDetail1.setRepaymentDate(firstRepaymentDate);
			repaymentPlanDetail1.setOtheFee("0.00");
			repaymentPlanDetail1.setTechMaintenanceFee("0.00");
			repaymentPlanDetail1.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail1);

			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
			repaymentPlanDetail2.setRepaymentPrincipal(amount);
			repaymentPlanDetail2.setRepaymentInterest("0.00");
			repaymentPlanDetail2.setRepaymentDate(secondRepaymentDate);
			repaymentPlanDetail2.setOtheFee("0.00");
			repaymentPlanDetail2.setTechMaintenanceFee("0.00");
			repaymentPlanDetail2.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail2);

			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
			repaymentPlanDetail3.setRepaymentPrincipal(amount);
			repaymentPlanDetail3.setRepaymentInterest("0.00");
			repaymentPlanDetail3.setRepaymentDate(thirdRepaymentDate);
			repaymentPlanDetail3.setOtheFee("0.00");
			repaymentPlanDetail3.setTechMaintenanceFee("0.00");
			repaymentPlanDetail3.setLoanServiceFee("0.00");
			repaymentPlanDetails.add(repaymentPlanDetail3);

			contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

			contracts.add(contractDetail);
			importAssetPackageContent.setContractDetails(contracts);

			String param =  JsonUtils.toJsonString(importAssetPackageContent);
			System.out.println(param);
			Map<String,String> params =new HashMap<String,String>();
			params.put("fn", "200004");
			params.put("requestNo", UUID.randomUUID().toString());
			params.put("importAssetPackageContent", param);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("merId", "t_test_zfb");
			headers.put("secret", "123456");
			String signContent = ApiSignUtils.getSignCheckContent(params);
			String sign = ApiSignUtils.rsaSign(signContent, privateKey);
			System.out.println(sign);
			headers.put("sign", sign);

			try {
				String sr = PostTestUtil.sendPost(modifyUrl+URL_IMPORT_ASSET_PACKAGE, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询该合同下第一期还款计划
	 * @param uniqueId
	 * @return
	 */
	public String queryFirstRepaymentPlan(String uniqueId){
		String repaymentNumber = "";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100001");
		requestParams1.put("contractNo", "");
		requestParams1.put("requestNo", UUID.randomUUID().toString());
		requestParams1.put("uniqueId", uniqueId);
		try {
			String sr = PostTestUtil.sendPost(queryUrl, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) result.get("repaymentPlanDetails");
			JSONObject jo = repaymentPlanDetails.getJSONObject(0);
		    repaymentNumber = (String) jo.get("repaymentNumber");
			System.out.println(sr+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentNumber;
	}
	/**
	 * 扣款	
	 * @param repaymentNumber
	 * @param uniqueId
	 * @param productCode
	 * @param amount
	 * @param repaymentType
	 */
	public void deductRepaymentPlan(String repaymentNumber,String uniqueId, String productCode, String amount, String repaymentType){
		/**/
        Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", productCode);
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("apiCalledTime", DateUtils.format(new Date()));
		requestParams.put("amount",  amount);
		requestParams.put("repaymentType", repaymentType);//1、正常  0、提前划扣
		requestParams.put("mobile", "13777847783"); 
		requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':"+amount+",'repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+amount+",'techFee':0.00,'overDueFeeDetail':{"
//				+ "'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':4.00}}]");
		+ "'totalOverdueFee':0.00}}]");
		try {
			String sr = PostTestUtil.sendPost(deductUrl, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
	/**
	 * 扣款新接口
	 * @param repaymentNumber
	 * @param uniqueId
	 * @param productCode
	 * @param repaymentType
	 */
	public void deductRepaymentPlanNEW(String repaymentNumber,String repayScheduleNo,String uniqueId, String productCode, String repaymentTotalamount,String repaymentPrincipal ,String repaymentType){
		/**/
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductId",  UUID.randomUUID().toString());
		requestParams.put("financialProductCode", productCode);
		requestParams.put("uniqueId", uniqueId);
//		requestParams.put("contractNo",uniqueId);
		requestParams.put("apiCalledTime", DateUtils.format(new Date()));
		requestParams.put("amount",  repaymentTotalamount);
		requestParams.put("repaymentType", repaymentType);//1、正常  0、提前划扣
//		requestParams.put("notifyUrl","http://192.168.0.212:7778/api/notify/internal/deduct");
//		requestParams.put("payerName", "测试007");
//		requestParams.put("bankCode", "testBank");//
//		requestParams.put("mobile","13258446545");
//		requestParams.put("idCardNum","330381199305101470");
//		requestParams.put("payAcNo","666662518000519910");
//		requestParams.put("payerName", "秦伟超");
//		requestParams.put("bankCode", "C101041);
//		requestParams.put("mobile","17682481004");
//		requestParams.put("idCardNum","421182199204114115");
//		requestParams.put("payAcNo","6217857600016839339");
//		requestParams.put("provinceCode","620000");
//		requestParams.put("cityCode","141100");
//		requestParams.put("payerName", "吴波");
//		requestParams.put("bankCode", "C10102");
//		requestParams.put("mobile","17682481004");
//		requestParams.put("idCardNum","3408231945143445435");
//		requestParams.put("payAcNo","66660123456789990");
//		requestParams.put("mobile","13258446545");
//		requestParams.put("idCardNum","132032512010000359");
//		requestParams.put("payAcNo","6258101645858218");


//		  		requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repayScheduleNo':'"+repayScheduleNo+"','repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':'"+repaymentPrincipal+"','techFee':0.00,'overDueFeeDetail':{"
//				//requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':"+repaymentPrincipal+",'techFee':0.00,'overDueFeeDetail':{"
//	//			+ "'penaltyFee':0.00,'latePenalty':0.00,'lateFee':0.00,'lateOtherCost':0.00,'totalOverdueFee':4.00"
//				+ "'totalOverdueFee':0.00}}]");

		requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':'"+repaymentTotalamount+"','repaymentInterest':0.00,'repayScheduleNo':'"+repayScheduleNo+"','repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':'"+repaymentPrincipal+"','techFee':0.00,'overDueFeeDetail':{"
				+ "'totalOverdueFee':0.00}}]");
		try {
			String sr = PostTestUtil.sendPost(deductUrl, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 申请提前还款
	 * @param uniqueId
	 * @param assetInitialValue
	 * @param assetPrincipal
	 * @param assetRecycleDate
	 * @return
	 */
	public String applyPrepaymentPlan(String uniqueId,String repayScheduleNo,String assetInitialValue,String assetPrincipal,String assetRecycleDate){
		String result = null;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "200002");
		requestParams.put("uniqueId", uniqueId);
		requestParams.put("contractNo", "");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("assetRecycleDate", assetRecycleDate);
		requestParams.put("assetInitialValue", assetInitialValue);
		requestParams.put("repayScheduleNo",repayScheduleNo);
		requestParams.put("assetPrincipal", assetPrincipal);
		requestParams.put("assetInterest","0.00");
		requestParams.put("serviceCharge","0.00");
		requestParams.put("maintenanceCharge","0.00");
		requestParams.put("otherCharge","0.00");
		requestParams.put("type", "0");
		requestParams.put("payWay","0");
		//requestParams.put("hasDeducted", "-1");
		
		try {
			result = PostTestUtil.sendPost(INT_URL_PREPAYMENT, requestParams, new BaseApiTestPost().getIdentityInfoMap(requestParams));
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询该合同下有效的还款计划
	 * @param uniqueId
	 * @return
	 */
	public String queryRepaymentPlanCount(String uniqueId){
		String repaymentPlanCount = "";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100001");
		requestParams1.put("contractNo", "");
		requestParams1.put("requestNo", UUID.randomUUID().toString());
		requestParams1.put("uniqueId", uniqueId);
		try {
			String sr = PostTestUtil.sendPost(queryUrl, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
			Result result = JsonUtils.parse(sr, Result.class);
			JSONArray repaymentPlanDetails =  (JSONArray) result.get("repaymentPlanDetails");
			repaymentPlanCount = repaymentPlanDetails.size()+"";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repaymentPlanCount;
	}
	/**
	 * 根据查询时间进行资产包批量查询
	 * @param startTime
	 * @param endTime
	 * @param productCode
	 * @return
	 */
	public String BatchQueryAssertPackage(String startTime,String endTime,String productCode){
		String result="";
		Map<String, String> requestParams1 = new HashMap<String, String>();
		requestParams1.put("fn", "100007");
		requestParams1.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
		requestParams1.put("productCode",productCode);
		requestParams1.put("startTime", startTime);
		requestParams1.put("endTime", endTime);
		try {
			result = PostTestUtil.sendPost("http://192.168.0.204/api/query", requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
