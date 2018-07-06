package com.zufangbao.privilege.service;

import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherCommandHandler;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })
public class TestThirdPartyVoucher{

	@Autowired
	ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;
	
	@Autowired
	ThirdPartVoucherCommandHandler thirdPartVoucherCommandHandler;
	//completed
	@Test
	public void test1 () {
		ThirdPartyVoucherCommandLog log = thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid("111f");
		System.out.println(log.getRepaymentNoJsonList()+"-----------------------------------");
	}
	
	//completed
	@Test
	public void test2(){
		ThirdPartyVoucherCommandLog log = new ThirdPartyVoucherCommandLog();
		log.setVersionNo("2");
		thirdPartVoucherCommandLogService.save(log);
	}
	
	@Test
//	public void test3(){
//		ThirdPartVoucherRepayDetailModel model = thirdPartVoucherCommandHandler.redThirdPartVoucherRepayDetail("111f", "1");
//		System.out.println(model+"--------------------");
//	}
	
//	@Test
	public void testGetFileNameList() {
/*		List<String> fileNameList = thirdPartVoucherCommandHandler.getThirdPartVoucherRepayDetailList("111f");
		ThirdPartVoucherRepayDetailModel model = thirdPartVoucherCommandHandler.redThirdPartVoucherRepayDetail("111f", fileNameList.get(0));
		Map<String, Object> resMap = new HashMap<String, Object>(3);
		resMap.put("versionNameList", fileNameList);
		ThirdPartVoucherRepayDetailModel his = new ThirdPartVoucherRepayDetailModel();
		his.setRepaymentPlanNo("12312312312312321");
		his.setPrincipal(new BigDecimal(1.0));
		his.setInterest(new BigDecimal(1.0));
		his.setServiceCharge(new BigDecimal(4.0));
		his.setMaintenanceCharge(new BigDecimal(0.0));
		his.setOtherCharge(new BigDecimal(7.0));
		his.setPenaltyFee(new BigDecimal(0.0));
		his.setLatePenalty(new BigDecimal(0.0));
		his.setLateFee(new BigDecimal(0.0));
		his.setOtherCharge(new BigDecimal(0.0));
		his.setAmount(new BigDecimal(0.0));
		resMap.put("historyDetail", his);
		ThirdPartyVoucherCommandLog log = thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid("111f");
		resMap.put("voucher", log);
		System.out.println(JsonUtils.toJSONString(resMap));*/
	}
}
