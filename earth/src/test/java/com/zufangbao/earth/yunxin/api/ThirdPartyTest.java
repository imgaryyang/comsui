package com.zufangbao.earth.yunxin.api;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.sun.yunxin.service.ThirdVoucherService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;


//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class ThirdPartyTest {
//	@Autowired
//	private IRemittanceApplicationService remittanceApplicationService;
//	@Autowired
//	private ThirdVoucherService thirdVoucherService;
	/*
 * 外网地址
 * */
	public  String OuterInternat_queryUrl = "http://yunxin.5veda.net/api/query";
    public  String OuterInternat_command="http://120.55.85.54:27082/api/command";
    public  String OuterInternat_modifyUrl = "http://yunxin.5veda.net/api/modify";
    public  String NewOuterInternat_command = "http://yunxin.5veda.net/api/command";
 /*
    * 内网地址
    * */
//	public String OuterInternat_queryUrl = "http://192.168.0.204/api/query";
//	public String OuterInternat_command = "http://192.168.0.204:27082/api/command";
//	public String OuterInternat_modifyUrl = "http://192.168.0.204:80/api/modify";
//	public String NewOuterInternat_command = "http://192.168.0.204:80/api/command";
	BaseTestMethod baseTestMethod = new BaseTestMethod();
	private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	@Autowired
	private ThirdVoucherService thirdVoucherService;

	String uniqueId= UUID.randomUUID().toString();//合同编号
	String transactionRequestNo=uniqueId+1;//交易请求号

	/*
	* 第三方凭证查询
	* */
	@Test
	public void testImportAssetPackageApi_normal() throws Throwable {
		System.out.println("result:");
//		baseTestMethod.queryThirdVoucherBybatchRequest(OuterInternat_queryUrl, "334185130248380013", "");
	}
	/*×
	* 模拟云信放款
	* */
	@Test
	public void testLoan()throws Throwable{
		System.out.println("开始模拟云信放款");
		String url="http://120.55.85.54:27082/api/command";
		String notifyUrl="http://192.168.0.138:8888/testUrl";
		String uniqueId= UUID.randomUUID().toString();//合同编号
		baseTestMethod.LoanCallBack(url,uniqueId,"1500",notifyUrl);
		System.out.println("end");
	}
	/*
	* 查询扣款接口
	* */
	@Test
	public void testDeduct()throws Throwable{
		//对还款计划进行部分扣款
		System.out.println("对还款计划进行部分扣款:");
//		baseTestMethod.deductRepaymentPlan("ZC1595808786920964096","2329f2d7-3efd-47af-9287-505d63602bb4","G31700","200","1");

	}
	/*
	* 测试NotifyServer第三方凭证单条查询配置优化
	* */
	@Test
	public void testNotifyServer()throws Throwable{
		//通过交易请求号获取宝付的流水记录

		//根据交易请求号制造第三方凭证


	}
}