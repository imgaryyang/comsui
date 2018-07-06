
package com.zufangbao.earth.api.test.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//		"classpath:/local/applicationContext-*.xml" })
public class BatchQueryPakcgaeApiPostTest extends BaseApiTestPost {

	private  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
	String BatchQueryPackage="http://192.168.0.204:27082/api/query";
	
	// importType  0 一个合同2个还款计划  1 一个合同1个还款计划
	@Test
	public void  testApi() {
		//放款
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "300002");
		requestParams.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
		requestParams.put("remittanceStrategy", "0");//放款策略
		requestParams.put("productCode", "G31700");//信托产品代码
		requestParams.put("uniqueId", "FANT100");//贷款合同唯一编号
		requestParams.put("contractNo", "FANT100");//贷款合同编号
		String amount = "1500";//本金
		requestParams.put("plannedRemittanceAmount", amount);//计划放款金额
		requestParams.put("auditorName", "auditorName1");//审核人
		requestParams.put("auditTime", "2016-08-20 00:00:00");//审核时间
		requestParams.put("remark", "交易备注");
		String bankCardNo = "6214855712106520";	//交易方银行卡号
		requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'2016-08-20 00:00:00','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		try {
			String sr = PostTestUtil.sendPost(REMITTANCECOMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//资产包导入
		for (int index = 0; index < 1; index++) {
			ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
			importAssetPackageContent.setThisBatchContractsTotalNumber(1);//批次合同总条数
			importAssetPackageContent.setThisBatchContractsTotalAmount("1500");//批次合同本金总额
			importAssetPackageContent.setFinancialProductCode("G31700");//信托产品编号

			List<ContractDetail> contracts = new ArrayList<ContractDetail>();
			ContractDetail contractDetail = new ContractDetail();
			contractDetail.setUniqueId("FANT100");//贷款合同唯一识别编号
			contractDetail.setLoanContractNo("FANT100");//贷款合同编号

			contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());//贷款客户编号
			contractDetail.setLoanCustomerName("这是测试");//贷款客户姓名
			contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());//资产编号
			contractDetail.setIDCardNo("320301198502169142");//客户身份证编号
			contractDetail.setBankCode("C10105");//还款账户编号
			contractDetail.setBankOfTheProvince("110000");//开户行所在省编号
			contractDetail.setBankOfTheCity("110100");//开户行所在城市编号
			contractDetail.setRepaymentAccountNo("6214855712106520");//还款账户号
			contractDetail.setLoanTotalAmount("1500");//贷款金额
			contractDetail.setLoanPeriods(3);//贷款期数
			contractDetail.setEffectDate("2017-01-07");//生效日期
			contractDetail.setExpiryDate("2099-01-01");//到期日期
			contractDetail.setLoanRates("0.156");//贷款利率
			contractDetail.setInterestRateCycle(1);//利率周期，1：月化
			contractDetail.setPenalty("0.0005");//罚息
			contractDetail.setRepaymentWay(2);//还款方式，2：锯齿型

			List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();//还款计划细节

			ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
			repaymentPlanDetail1.setRepaymentPrincipal("500");
			repaymentPlanDetail1.setRepaymentInterest("20.00");
			repaymentPlanDetail1.setRepaymentDate("2017-03-14");//首期还款日必须大于等于放款日当天（今天）
			repaymentPlanDetail1.setOtheFee("20.00");
			repaymentPlanDetail1.setTechMaintenanceFee("20.00");
			repaymentPlanDetail1.setLoanServiceFee("20.00");
			repaymentPlanDetails.add(repaymentPlanDetail1);
			
			ImportRepaymentPlanDetail repaymentPlanDetail2= new ImportRepaymentPlanDetail();
			repaymentPlanDetail2.setRepaymentPrincipal("500");
			repaymentPlanDetail2.setRepaymentInterest("20.00");
			repaymentPlanDetail2.setRepaymentDate("2017-05-01");
			repaymentPlanDetail2.setOtheFee("20.00");
			repaymentPlanDetail2.setTechMaintenanceFee("20.00");
			repaymentPlanDetail2.setLoanServiceFee("20.00");
			repaymentPlanDetails.add(repaymentPlanDetail2);
			
			ImportRepaymentPlanDetail repaymentPlanDetail3= new ImportRepaymentPlanDetail();
			repaymentPlanDetail3.setRepaymentPrincipal("500");
			repaymentPlanDetail3.setRepaymentInterest("20.00");
			repaymentPlanDetail3.setRepaymentDate("2017-06-01");
			repaymentPlanDetail3.setOtheFee("20.00");
			repaymentPlanDetail3.setTechMaintenanceFee("20.00");
			repaymentPlanDetail3.setLoanServiceFee("20.00");
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
				String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, params, headers);
				System.out.println( "===========" + index + sr);
			} catch (Exception e) {
				e.printStackTrace();
			}	
			//资产包批量查询
			Map<String, String> requestParams1 = new HashMap<String, String>();
			requestParams1.put("fn", "100007");
			requestParams1.put("requestNo", UUID.randomUUID().toString());//请求唯一标识
			requestParams1.put("productCode","G31700");
			requestParams1.put("startTime", "2017-3-14");
			requestParams1.put("endTime", "2017-6-17");
			try {
				String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
				System.out.println(sr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
