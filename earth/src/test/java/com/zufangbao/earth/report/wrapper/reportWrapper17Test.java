package com.zufangbao.earth.report.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zufangbao.earth.report.factory.SqlCacheManager;
import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VerificationStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLogSpec;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml"})
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper17Test {
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	 
	@Test
	public void test(){
		ThirdPartyVoucherCommandLogQueryModel queryModel=generateQueryModel();
		
		Map<String, Object> params=buildParams(queryModel);
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper17"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(sql);
		
	}
	@Test
	public void test1(){
		ThirdPartyVoucherCommandLogQueryModel queryModel=new ThirdPartyVoucherCommandLogQueryModel();
		
		Map<String, Object> params=buildParams(queryModel);
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper17"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(sql);
		
	}
	private ThirdPartyVoucherCommandLogQueryModel generateQueryModel() {
		ThirdPartyVoucherCommandLogQueryModel queryModel = new ThirdPartyVoucherCommandLogQueryModel();
		queryModel.setFinancialContractUuids("[]");
		queryModel.setTranscationGateway(2);
		queryModel.setVoucherLogStatus(0);
		queryModel.setVoucherLogIssueStatus(0);
		queryModel.setReceivableAccountNo("receiveable_account_no_1");
		queryModel.setPaymentName("payment_name_1");
		queryModel.setPaymentAccountNo("payment_account_no_1");
		queryModel.setStartTime("2017-04-19 10:00:00");
		queryModel.setEndTime("2017-04-19 12:00:00");
		return queryModel;
	}
	private Map<String, Object> buildParams(ThirdPartyVoucherCommandLogQueryModel paramsBean){
		List<VoucherLogStatus> voucherLogStatus = ThirdPartyVoucherCommandLogSpec.VerificationStatusToVoucherLogStatus
				.get(paramsBean.getVoucherLogStatusEnum());
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", paramsBean.getFinancialContractUuidList());
		params.put("transcationGateway", paramsBean.getTranscationGateway());
		params.put("voucherLogStatus", voucherLogStatus);
		params.put("voucherLogIssueStatus", paramsBean.getVoucherLogIssueStatus());
		params.put("receivableAccountNo", paramsBean.getReceivableAccountNo());
		params.put("paymentName", paramsBean.getPaymentName());
		params.put("paymentAccountNo", paramsBean.getPaymentAccountNo());
		params.put("startTime", paramsBean.getStartTimeDate());
		params.put("endTime", paramsBean.getEndTimeDate());

		return params;
	}
}
