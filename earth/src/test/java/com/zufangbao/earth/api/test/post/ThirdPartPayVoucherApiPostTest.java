package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.hathaway.util.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherRepayDetailModel;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class ThirdPartPayVoucherApiPostTest extends BaseApiTestPost  {

	
	private  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

	@Test
	public void test() {
//		importAssetPackage("http://localhost:9090/api/modify","G31700",
//				"TESTTT33","33",1,1);

		MakeVOucher2("http://localhost:9090/api/command", "72149f12-34ee-4d;72149f12-34ee-4dqwc", "TESTTT33", "G31700", "40", "40");
	}


	/**
	 * 制造第三方凭证，其中包含三个还款计划
	 *
	 * @param url
	 * @param transactionRequestNo
	 * @param uniqueId
	 * @return
	 */
	public void MakeVOucher2(String url, String transactionRequestNo, String uniqueId, String productCode, String totalamount, String amount) {
		List<ThirdPartVoucherDetailModel> models = new ArrayList<ThirdPartVoucherDetailModel>();
		
		for(int i =1 ;i<=1;i++){
			
			for(int y=1 ;y<=1;y++){
				ThirdPartVoucherDetailModel model = new ThirdPartVoucherDetailModel();
				model.setVoucherNo("asdkjkalsdaklsdjkalsdj112112111");
				model.setBankTransactionNo("123456");
				model.setTransactionRequestNo("1494132386615");
				model.setTransactionTime(DateUtils.format(new Date()));
				model.setTransactionGateway(5);
				model.setCurrency(0);
				model.setAmount(new BigDecimal("980"));
				model.setContractUniqueId("ZHENGHANGBO100000");
				model.setReceivableAccountNo("12345tyui");
				model.setPaymentBank("23erhy");
				model.setPaymentName("qwertyu");
				model.setPaymentAccountNo("23r4t5y6i8o9");
				model.setCustomerName("1234567890");
				model.setCustomerIdNo("1234567890-");
				model.setComment("213456");
				model.setCompleteTime(DateUtils.format(new Date()));

				ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
				detailModel.setAmount(new BigDecimal("980"));
				detailModel.setInterest(new BigDecimal("20"));
				detailModel.setLateFee(BigDecimal.ZERO);
				detailModel.setLateOtherCost(BigDecimal.ZERO);
				detailModel.setLatePenalty(BigDecimal.ZERO);
				detailModel.setMaintenanceCharge(new BigDecimal("20"));
				detailModel.setOtherCharge(new BigDecimal("20"));
				detailModel.setPenaltyFee(BigDecimal.ZERO);
				detailModel.setPrincipal(new BigDecimal("900"));
				detailModel.setRepaymentPlanNo("ZC1943627878499213312");
				detailModel.setServiceCharge(new BigDecimal("20"));
				List<ThirdPartVoucherRepayDetailModel> detailModels = new ArrayList<ThirdPartVoucherRepayDetailModel>();
				detailModels.add(detailModel);

				model.setRepayDetailList(detailModels);
				models.add(model);
			}
			
			
			
		}
		
		String content = JsonUtils.toJsonString(models);
		System.out.println(content);
		Map<String,String> params =new HashMap<String,String>(); 
		params.put("fn", "300006");
		params.put("requestNo", UUID.randomUUID().toString());
		params.put("financialContractNo","H26800");
		params.put("detailList", content); 
				
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("merId", "t_test_zfb");
		headers.put("secret", "123456");
		String signContent = ApiSignUtils.getSignCheckContent(params);
		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
		headers.put("sign", sign);
		
		try {
//			String sr = PostTestUtil.sendPost(COMMAND_THIRD_PARTY_URL_TEST, params, headers);
//			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
