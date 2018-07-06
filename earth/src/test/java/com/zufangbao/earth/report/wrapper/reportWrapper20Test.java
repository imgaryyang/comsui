/**
 * 
 */
package com.zufangbao.earth.report.wrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.zufangbao.sun.entity.security.Principal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.factory.SqlCacheManager;
import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentRecordQueryModel;

/**
 * @author hjl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml"})
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper20Test {
	@Autowired
	private SqlCacheManager sqlCacheManager;
	@Autowired
	private MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
	@Autowired
	private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
	@Autowired
	private ReportWrapper20 reportWrapper20;
	@Autowired
	private ReportWrapper21 reportWrapper21;
	
	@Test
	public void testSql2() {
//		String sqlTest = "select orderUuid,orderUniqueId,firstCustomerName,orderAmount,orderCreateTime,orderLastModifiedTime,repaymentBusinessNo,contractNo,paymentPlanTime,repaymentWay,"+
//					"repaymentAmount,orderCheckStatus,orderRecoverStatus,orderAliveStatus,	orderPayStatus,remark,ODITCH.first_account_name as FirstAccountName,ODITCH.second_account_name as SecondAccountName,"+
//					"ODITCH.third_account_name as ThirdAccountName,ODITCH.account_amount as Amountfrom (select OD.order_uuid as orderUuid,OD.order_unique_id as orderUniqueId,OD.first_customer_name as firstCustomerName,OD.order_amount as orderAmount,"+
//					"OD.order_create_time as orderCreateTime,OD.order_last_modified_time as orderLastModifiedTime,OD.order_check_status as orderCheckStatus,OD.order_recover_status as orderRecoverStatus,OD.order_alive_status as orderAliveStatus,OD.order_pay_status as orderPayStatus,"+
//					"ODIT.order_detail_uuid as ORDERDETAILUUID,ODIT.repayment_business_no as repaymentBusinessNo,ODIT.contract_no as contractNo,ODIT.repayment_plan_time as repaymentPlanTime,ODIT.repayment_way as repaymentWay,ODIT.amount as repaymentAmount ,ODIT.remark as remark from repayment_order OD,repayment_order_item ODIT "+
//					"where OD.order_uuid=ODIT.order_uuid and OD.financial_contract_uuid IN (:financialContractUuids) AND orderCheckStatus = :checkStatus AND orderAliveStatus = :aliveStatus AND orderPayStatus = :orderPayStatus AND orderCreateTime >= :repaymentOrderstartDate "+
//					"AND orderLastModifiedTime <= :repaymentOrderendDate AND orderUuid like %:repaymentsortField%) AS combination,repayment_order_item_charge ODITCH "+ 
//					"where ODITCH.repayment_order_item_uuid=combination.ORDERDETAILUUID AND repaymentWay= :repaymentWay";
		Map<String, Object> params = new HashMap<>();

		params.put("financialContractUuids", "G31700");
		params.put("checkStatus", 1);
		params.put("aliveStatus", 0);
		params.put("orderPayStatus", 0);
		params.put("repaymentOrderstartDate", "2017-02-16 23:59:59");
		params.put("repaymentOrderendDate", "2017-05-16 23:59:59");
		params.put("repaymentOrderuuid", "0");
		params.put("repaymentsortField", "f28e785f4feb");
		params.put("repaymentWay", 1);

		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper20"), params);
			System.out.println("============================"+sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	//	Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	
	@Test
	public void abc() throws Exception{
		
		RepaymentOrderQueryModel paramsBean=new RepaymentOrderQueryModel();
		
		List<String> list=new ArrayList<String>();
		list.add("c31ac9c0-801f-44b4-9072-eb824aa478bd");
		String ss=JsonUtils.toJSONString(list);
		paramsBean.setFinancialContractUuids(ss);
		paramsBean.setOrderUuid("1");
		paramsBean.setSortField("653f6fa1-ab79-401f-ac75-f337f05e4055");
		ExportEventLogModel exportEventLogModel=new ExportEventLogModel();
		reportWrapper20.wrap(paramsBean, mockHttpServletRequest, mockHttpServletResponse, exportEventLogModel,new Principal());
		mockHttpServletResponse.getContentAsByteArray();
		mockHttpServletResponse.getOutputStream();
		File file = new File("/home/hjl/桌面/a.zip");
		OutputStream os = new FileOutputStream(file);
		os.write(mockHttpServletResponse.getContentAsByteArray());
//		OutputStream stream = mockHttpServletResponse.getOutputStream();
		os.close();
		
	}
	
	@Test
	public void reportWrapper21() throws Exception{
		
		RepaymentRecordQueryModel paramsBean=new RepaymentRecordQueryModel();
		
		List<String> list=new ArrayList<String>();
		list.add("8a295897-14f1-4805-8351-26179fbb7b45");
		String ss=JsonUtils.toJSONString(list);
		paramsBean.setFinancialContractUuids(ss);
		
		ExportEventLogModel exportEventLogModel=new ExportEventLogModel();
		reportWrapper21.wrap(paramsBean, mockHttpServletRequest, mockHttpServletResponse, exportEventLogModel,new Principal());
		mockHttpServletResponse.getContentAsByteArray();
		mockHttpServletResponse.getOutputStream();
		File file = new File("/E:/data/a.zip");
		OutputStream os = new FileOutputStream(file);
		os.write(mockHttpServletResponse.getContentAsByteArray());
//		OutputStream stream = mockHttpServletResponse.getOutputStream();
		os.close();
		
	}
	
	@Test
	public void test_reportWrapper21() throws Exception{
		
		RepaymentRecordQueryModel paramsBean=new RepaymentRecordQueryModel();
		
		List<String> list=new ArrayList<String>();
		list.add("d2812bc5-5057-4a91-b3fd-9019506f0499");
		String ss=JsonUtils.toJSONString(list);
		paramsBean.setFinancialContractUuids(ss);
//		paramsBean.setRepaymentBusinessNo("ZC124953667938623488");
		
		ExportEventLogModel exportEventLogModel=new ExportEventLogModel();
		reportWrapper21.wrap(paramsBean, mockHttpServletRequest, mockHttpServletResponse, exportEventLogModel,new Principal());
		mockHttpServletResponse.getContentAsByteArray();
		mockHttpServletResponse.getOutputStream();
		File file = new File("/E:/data/a.zip");
		OutputStream os = new FileOutputStream(file);
		os.write(mockHttpServletResponse.getContentAsByteArray());
//		OutputStream stream = mockHttpServletResponse.getOutputStream();
		os.close();
		
	}
	
	@Test
	public void test_reportWrapper21_() throws Exception{
		
		RepaymentRecordQueryModel paramsBean=new RepaymentRecordQueryModel();
		
		List<String> list=new ArrayList<String>();
		list.add("d2812bc5-5057-4a91-b3fd-9019506f0499");
		String ss=JsonUtils.toJSONString(list);
		paramsBean.setFinancialContractUuids(ss);
		paramsBean.setContractNo("妹妹你大胆的往前走712");
//		paramsBean.setRepaymentBusinessNo("bad6543e-87d5-459c-8cab-498c6125d6c0");
		
		ExportEventLogModel exportEventLogModel=new ExportEventLogModel();
		reportWrapper21.wrap(paramsBean, mockHttpServletRequest, mockHttpServletResponse, exportEventLogModel,new Principal());
		mockHttpServletResponse.getContentAsByteArray();
		mockHttpServletResponse.getOutputStream();
		File file = new File("/E:/data/a.zip");
		OutputStream os = new FileOutputStream(file);
		os.write(mockHttpServletResponse.getContentAsByteArray());
//		OutputStream stream = mockHttpServletResponse.getOutputStream();
		os.close();
		
	}
}
