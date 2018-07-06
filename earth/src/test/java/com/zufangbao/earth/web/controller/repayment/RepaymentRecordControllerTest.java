package com.zufangbao.earth.web.controller.repayment;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zufangbao.sun.entity.security.Principal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.hathaway.job.Priority;

import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.api.controller.Api_V3_Controller;
import com.zufangbao.earth.yunxin.api.handler.PaymentOrderApiHandler;
import com.zufangbao.earth.yunxin.web.controller.RepaymentOrderController;
import com.zufangbao.earth.yunxin.web.controller.RepaymentRecordController;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.payment.order.PaymentOrderCashFlowShowModel;
import com.zufangbao.sun.entity.payment.order.RepaymentOrderVoucherShowModel;
import com.zufangbao.sun.entity.repayment.order.AliveStatus;
import com.zufangbao.sun.entity.repayment.order.OrderPayStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RecoverStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderPayResult;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.AppInfoShow;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentRecordQueryModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentRecordDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandlerProxy;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
public class RepaymentRecordControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private RepaymentOrderController repaymentOrderController;
    @Autowired
    private Api_V3_Controller api_V3_Controller;
    
    @Autowired
    private PaymentOrderApiHandler paymentOrderApiHandler;
    
    @Autowired
    private PrincipalService principalService;
    
    @Autowired
    private PaymentOrderService paymentOrderService; 
    
    @Autowired
    private RepaymentOrderService repaymentOrderService;
    
    @Autowired
    private CashFlowService cashFlowService;
    
    @Autowired
    private PaymentOrderHandler paymentOrderHandler;
    
    @Autowired
    private CashFlowHandler cashFlowHandler;
    
    @Autowired
    private RepaymentRecordController repaymentRecordController;
	
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_repayment_record_test.sql")
    public void testQueryRepaymentRecord(){
    	
    	Principal principal = principalService.load(Principal.class, 1L);
    	Page page = new Page();
		page.setBeginIndex(0);
		page.setCurrentPage(1);
		
    	RepaymentRecordQueryModel repaymentRecordQueryModel = new RepaymentRecordQueryModel(); 
    	List<String> list=new ArrayList<String>();
		list.add("financial_contract_uuid_1");
		String ss=com.zufangbao.sun.utils.JsonUtils.toJSONString(list);
		repaymentRecordQueryModel.setFinancialContractUuids(ss);
    	
    	
    	String result=repaymentRecordController.queryRepaymentRecord(repaymentRecordQueryModel, principal, page);
		Result result2=JsonUtils.parse(result, Result.class);
		Map<String, Object> modelmap=result2.getData();
//		String modeljson=(String)modelmap.get("list");
		
		List<RepaymentRecordDetail> recordDetailList=JsonUtils.parseArray(modelmap.get("list").toString(), RepaymentRecordDetail.class);
    	
		assertEquals(3, recordDetailList.size());
		
		RepaymentRecordDetail detail1 = recordDetailList.get(0);
		assertEquals("repurchase_doc_uuid", detail1.getRepaymentPlanNo());
		assertEquals(DateUtils.parseDate("2017-06-10 18:13:42",DateUtils.LONG_DATE_FORMAT), detail1.getActualRecycleDate());
		assertEquals(DateUtils.parseDate("2016-10-20 16:57:12",DateUtils.LONG_DATE_FORMAT), detail1.getHappenDate());
		assertEquals("回购", detail1.getCapitalType());
		assertEquals(new BigDecimal("500.00"), detail1.getRepurchasePrincipal());
		assertEquals(BigDecimal.ZERO, detail1.getRepurchaseInterest());
		assertEquals(DateUtils.parseDate("2016-11-24",DateUtils.DATE_FORMAT), detail1.getPlanDate());
		assertEquals("", detail1.getPaymentGateway());
		assertEquals(new BigDecimal("500.00"), detail1.getTotalFee());
		assertEquals("V14804964998590464", detail1.getVoucherNo());
		assertEquals(DateUtils.parseDate("2017-06-10 18:13:42",DateUtils.LONG_DATE_FORMAT), detail1.getAccountedDate());
		
		RepaymentRecordDetail detail2 = recordDetailList.get(2);
		assertEquals("repayment_plan_no_1", detail2.getRepaymentPlanNo());
		assertEquals(DateUtils.parseDate("2017-06-08 18:13:42",DateUtils.LONG_DATE_FORMAT), detail2.getActualRecycleDate());
		assertEquals(DateUtils.parseDate("2016-10-20 16:57:12",DateUtils.LONG_DATE_FORMAT), detail2.getHappenDate());
		assertEquals(new BigDecimal("100.00"), detail2.getLoanAssetInterest());
		assertEquals(new BigDecimal("900.00"), detail2.getLoanAssetPrincipal());
		
		assertEquals("线上代扣", detail2.getCapitalType());
		assertEquals(new BigDecimal("1000.00"), detail2.getTotalFee());
		assertEquals("journal_voucher_uuid_1", detail2.getVoucherNo());
		assertEquals(DateUtils.parseDate("2017-06-13 16:57:12",DateUtils.LONG_DATE_FORMAT), detail2.getAccountedDate());
		assertEquals(DateUtils.parseDate("2016-05-01",DateUtils.DATE_FORMAT), detail2.getPlanDate());
		assertEquals("银联广州", detail2.getPaymentGateway());
		
		RepaymentRecordDetail detail3 = recordDetailList.get(1);
		assertEquals("repayment_plan_no_2", detail3.getRepaymentPlanNo());
		assertEquals(DateUtils.parseDate("2017-06-09 18:13:42",DateUtils.LONG_DATE_FORMAT), detail3.getActualRecycleDate());
		assertEquals(DateUtils.parseDate("2016-10-20 16:57:12",DateUtils.LONG_DATE_FORMAT), detail3.getHappenDate());
		assertEquals(BigDecimal.ZERO, detail3.getLoanAssetInterest());
		assertEquals(new BigDecimal("200.00"), detail3.getLoanAssetPrincipal());
		
		assertEquals("商户代扣", detail3.getCapitalType());
		assertEquals(new BigDecimal("200.00"), detail3.getTotalFee());
		assertEquals("journal_voucher_uuid_3", detail3.getVoucherNo());
		assertEquals(DateUtils.parseDate("2017-06-12 16:57:12",DateUtils.LONG_DATE_FORMAT), detail3.getAccountedDate());
		assertEquals(DateUtils.parseDate("2016-05-05",DateUtils.DATE_FORMAT), detail3.getPlanDate());
		assertEquals("银联广州", detail3.getPaymentGateway());
    }
    
}
