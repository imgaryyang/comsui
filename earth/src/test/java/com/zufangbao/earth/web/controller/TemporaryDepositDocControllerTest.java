/**
 * 
 */
package com.zufangbao.earth.web.controller;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.gluon.exception.voucher.TmpDepositDetailCreateException;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.entity.refund.*;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.entity.voucher.TemplatesForPay;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.model.QueryTemporaryDepositDocModel;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.voucher.TmpDepositRecoverModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherTmpDepositCreateRepaymentModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobTmpDepositReconciliation;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import com.zufangbao.wellsfargo.yunxin.handler.TemporaryDepositDocHandler;
import com.zufangbao.wellsfargo.yunxin.model.QueryRepaymentPlanShowModel;
import com.zufangbao.wellsfargo.yunxin.model.QueryRepurchaseShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TemDepositDocShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TmpDepositDocRepayRecordModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *滞留单测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Rollback(false)
@Transactional
public class TemporaryDepositDocControllerTest {
	@Autowired
	private TemporaryDepositDocHandler temporaryDepositDocHandler;
	
	@Autowired
	private TemporaryDepositDocService temporaryDepositDocService;
	
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;
	
	@Autowired
	private VoucherService voucherService;
	
	@Autowired
	private ActivePaymentVoucherProxy activePaymentVoucherProxy;
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private VirtualAccountService virtualAccountService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	
	@Autowired
	private DstJobTmpDepositReconciliation dstJobTmpDepositReconciliation;
	
	@Autowired
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
	
	@Autowired
	private LedgerItemService ledgerItemService;
	
	@Autowired
	private JournalVoucherService journalVoucherService;
	/**
	 *　还款弹框查询条件为贷款合同号 
	 */
	
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepaymentPlanTest.sql")
	public void queryRepaymentPlanForContractnoTest(){
		QueryRepaymentPlanModel temDptQueryRepaymentPlanModel = new QueryRepaymentPlanModel();
		String contractNo = "contract_no_1";
		temDptQueryRepaymentPlanModel.setContractNo(contractNo);
		Page page = new Page();
		
		List<QueryRepaymentPlanShowModel> temDptQueryRepaymentPlanShowModels= temporaryDepositDocHandler.queryRepaymentPlanShowModels(temDptQueryRepaymentPlanModel, page);
	
		QueryRepaymentPlanShowModel temDptQueryRepaymentPlanShowModel=temDptQueryRepaymentPlanShowModels.get(0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getRepaymentPlanNo() , "repayment_plan_no");
		Assert.assertEquals(DateUtils.format(temDptQueryRepaymentPlanShowModel.getAssetRecycleDate()) , "3016-10-20");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getContractNo() , "contract_no_1");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getCustomerName() , "测试用户1");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getOverdueDays() , 0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getPlanRecycleAmount(), new BigDecimal("1000.00"));
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getActualRecycleAmount() , new BigDecimal("500.00"));
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getAssetStatus() , AssetClearStatus.UNCLEAR.getChineseMessage());
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getOverdueStatus() ,AuditOverdueStatus.NORMAL.getChineseMessage());
		RepaymentChargesDetail repaymentChargesDetail = JsonUtils.parse(temDptQueryRepaymentPlanShowModel.getDetailAmounts(), RepaymentChargesDetail.class);
		Assert.assertEquals(repaymentChargesDetail.getLoanAssetPrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(repaymentChargesDetail.getLoanAssetInterest(), new BigDecimal("50.00"));
	}
	
	/**
	 * 还款弹框查询条件为客户姓名 
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepaymentPlanTest.sql")
	public void queryRepaymentPlanForCustomerTest(){
		QueryRepaymentPlanModel temDptQueryRepaymentPlanModel = new QueryRepaymentPlanModel();
		String Customer = "测试用户1";
		temDptQueryRepaymentPlanModel.setCustomerName(Customer);
		Page page = new Page();
		
		List<QueryRepaymentPlanShowModel> temDptQueryRepaymentPlanShowModels= temporaryDepositDocHandler.queryRepaymentPlanShowModels(temDptQueryRepaymentPlanModel, page);
	
		QueryRepaymentPlanShowModel temDptQueryRepaymentPlanShowModel=temDptQueryRepaymentPlanShowModels.get(0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getRepaymentPlanNo() , "repayment_plan_no");
		Assert.assertEquals(DateUtils.format(temDptQueryRepaymentPlanShowModel.getAssetRecycleDate()) , "3016-10-20");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getContractNo() , "contract_no_1");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getCustomerName() , "测试用户1");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getOverdueDays() , 0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getPlanRecycleAmount(), new BigDecimal("1000.00"));
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getActualRecycleAmount() , new BigDecimal("500.00"));
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getAssetStatus() , AssetClearStatus.UNCLEAR.getChineseMessage());
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getOverdueStatus() ,AuditOverdueStatus.NORMAL.getChineseMessage());
		RepaymentChargesDetail repaymentChargesDetail = JsonUtils.parse(temDptQueryRepaymentPlanShowModel.getDetailAmounts(), RepaymentChargesDetail.class);
		Assert.assertEquals(repaymentChargesDetail.getLoanAssetPrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(repaymentChargesDetail.getLoanAssetInterest(), new BigDecimal("50.00"));

	}
	
	/**
	 * 还款弹框查询条件为还款编号
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepaymentPlanTest.sql")
	public void queryRepaymentPlanForRepaymentPlanNoTest(){
		QueryRepaymentPlanModel temDptQueryRepaymentPlanModel = new QueryRepaymentPlanModel();
		String repaymentPlanNo = "repayment_plan_no";
		temDptQueryRepaymentPlanModel.setRepaymentPlanNo(repaymentPlanNo);
		Page page = new Page();
		
		List<QueryRepaymentPlanShowModel> temDptQueryRepaymentPlanShowModels= temporaryDepositDocHandler.queryRepaymentPlanShowModels(temDptQueryRepaymentPlanModel, page);
	
		QueryRepaymentPlanShowModel temDptQueryRepaymentPlanShowModel=temDptQueryRepaymentPlanShowModels.get(0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getRepaymentPlanNo() , "repayment_plan_no");
		Assert.assertEquals(DateUtils.format(temDptQueryRepaymentPlanShowModel.getAssetRecycleDate()) , "3016-10-20");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getContractNo() , "contract_no_1");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getCustomerName() , "测试用户1");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getOverdueDays() , 0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getPlanRecycleAmount(), new BigDecimal("1000.00"));
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getActualRecycleAmount() , new BigDecimal("500.00"));
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getAssetStatus() , AssetClearStatus.UNCLEAR.getChineseMessage());
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getOverdueStatus() ,AuditOverdueStatus.NORMAL.getChineseMessage());
		RepaymentChargesDetail repaymentChargesDetail = JsonUtils.parse(temDptQueryRepaymentPlanShowModel.getDetailAmounts(), RepaymentChargesDetail.class);
		Assert.assertEquals(repaymentChargesDetail.getLoanAssetPrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(repaymentChargesDetail.getLoanAssetInterest(), new BigDecimal("50.00"));
	}
	
	/**
	 * 还款弹框查询条件为贷款合同唯一编号
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepaymentPlanTest.sql")
	public void queryRepaymentPlanForContractUniqueIdTest(){
		QueryRepaymentPlanModel temDptQueryRepaymentPlanModel = new QueryRepaymentPlanModel();
		String contractUniqueId = "unique_id_1";
		temDptQueryRepaymentPlanModel.setContractUniqueId(contractUniqueId);
		Page page = new Page();
		
		List<QueryRepaymentPlanShowModel> temDptQueryRepaymentPlanShowModels= temporaryDepositDocHandler.queryRepaymentPlanShowModels(temDptQueryRepaymentPlanModel, page);
	
		QueryRepaymentPlanShowModel temDptQueryRepaymentPlanShowModel=temDptQueryRepaymentPlanShowModels.get(0);
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getRepaymentPlanNo() , "repayment_plan_no");
		Assert.assertEquals(DateUtils.format(temDptQueryRepaymentPlanShowModel.getAssetRecycleDate()) , "3016-10-20");
		Assert.assertEquals(temDptQueryRepaymentPlanShowModel.getContractNo() , "contract_no_1");
		
	}
	
	/**
	 * 回购弹框查询条件为贷款合同唯一编号
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepurchaseDocTest.sql")
	public void queryRepurchaseDocForContractUniqueIdTest(){
		QueryRepurchaseModel temDptQueryRepurchaseModel = new QueryRepurchaseModel();
		String contractUniqueId = "unique_id_1";
		temDptQueryRepurchaseModel.setContractUniqueId(contractUniqueId);
		Page page = new Page();
		
		List<QueryRepurchaseShowModel> temDptQueryRepurchaseShowModels= temporaryDepositDocHandler.queryRepurchaseShowModel(temDptQueryRepurchaseModel, page);
	
		QueryRepurchaseShowModel temDptQueryRepurchaseShowModel=temDptQueryRepurchaseShowModels.get(0);
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDocUuid() , "repurchase_doc_uuid_1");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getAppName(),"ppd");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseInterest(), new BigDecimal("50.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePenalty(),new BigDecimal("0"));
	//	Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseOtherCharges(),new BigDecimal("0"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDays(), 1);
		Assert.assertEquals(DateUtils.format(temDptQueryRepurchaseShowModel.getLastModifedTime(),DateUtils.LONG_DATE_FORMAT), "2016-10-31 20:20:41");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseStatus(),RepurchaseStatus.REPURCHASING.getChineseMessage());
		
	}
	
	/**
	 * 回购弹框查询条件为回购编号
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepurchaseDocTest.sql")
	public void queryRepurchaseDocForRepurchaseNoTest(){
		QueryRepurchaseModel temDptQueryRepurchaseModel = new QueryRepurchaseModel();
		String repurchaseNo = "repurchase_doc_uuid_1";
		temDptQueryRepurchaseModel.setRepurchaseNo(repurchaseNo);
		Page page = new Page();
		
		List<QueryRepurchaseShowModel> temDptQueryRepurchaseShowModels= temporaryDepositDocHandler.queryRepurchaseShowModel(temDptQueryRepurchaseModel, page);
	
		QueryRepurchaseShowModel temDptQueryRepurchaseShowModel=temDptQueryRepurchaseShowModels.get(0);
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDocUuid() , "repurchase_doc_uuid_1");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getAppName(),"ppd");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseInterest(), new BigDecimal("50.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePenalty(),new BigDecimal("0"));
	//	Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseOtherCharges(),new BigDecimal("0"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDays(), 1);
		Assert.assertEquals(DateUtils.format(temDptQueryRepurchaseShowModel.getLastModifedTime(),DateUtils.LONG_DATE_FORMAT), "2016-10-31 20:20:41");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseStatus(),RepurchaseStatus.REPURCHASING.getChineseMessage());
	}
	
	/**
	 * 回购弹框查询条件为客户姓名
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepurchaseDocTest.sql")
	public void queryRepurchaseDocForCustomerNameTest(){
		QueryRepurchaseModel temDptQueryRepurchaseModel = new QueryRepurchaseModel();
		String CustomerName = "测试用户1";
		temDptQueryRepurchaseModel.setCustomerName(CustomerName);
		Page page = new Page();
		
		List<QueryRepurchaseShowModel> temDptQueryRepurchaseShowModels= temporaryDepositDocHandler.queryRepurchaseShowModel(temDptQueryRepurchaseModel, page);
	
		QueryRepurchaseShowModel temDptQueryRepurchaseShowModel=temDptQueryRepurchaseShowModels.get(0);
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDocUuid() , "repurchase_doc_uuid_1");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getAppName(),"ppd");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseInterest(), new BigDecimal("50.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePenalty(),new BigDecimal("0"));
	//	Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseOtherCharges(),new BigDecimal("0"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDays(), 1);
		Assert.assertEquals(DateUtils.format(temDptQueryRepurchaseShowModel.getLastModifedTime(),DateUtils.LONG_DATE_FORMAT), "2016-10-31 20:20:41");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseStatus(),RepurchaseStatus.REPURCHASING.getChineseMessage());
	}
	
	/**
	 * 回购弹框查询条件为贷款合同编号
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryRepurchaseDocTest.sql")
	public void queryRepurchaseDocForContractNoTest(){
		QueryRepurchaseModel temDptQueryRepurchaseModel = new QueryRepurchaseModel();
		String ContractNo = "contract_no_1";
		temDptQueryRepurchaseModel.setContractNo(ContractNo);
		Page page = new Page();
		
		List<QueryRepurchaseShowModel> temDptQueryRepurchaseShowModels= temporaryDepositDocHandler.queryRepurchaseShowModel(temDptQueryRepurchaseModel, page);
	
		QueryRepurchaseShowModel temDptQueryRepurchaseShowModel=temDptQueryRepurchaseShowModels.get(0);
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDocUuid() , "repurchase_doc_uuid_1");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getAppName(),"ppd");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePrincipal(), new BigDecimal("450.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseInterest(), new BigDecimal("50.00"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchasePenalty(),new BigDecimal("0"));
	//	Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseOtherCharges(),new BigDecimal("0"));
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseDays(), 1);
		Assert.assertEquals(DateUtils.format(temDptQueryRepurchaseShowModel.getLastModifedTime(),DateUtils.LONG_DATE_FORMAT), "2016-10-31 20:20:41");
		Assert.assertEquals(temDptQueryRepurchaseShowModel.getRepurchaseStatus(),RepurchaseStatus.REPURCHASING.getChineseMessage());
	}
	
	/**
	 * 滞留单详情页面的计划还款框
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryTmpDepositDocRepayRecordModelTest.sql")
	public void queryTmpDepositDocRepayRecordModelTest(){
		String temDepositDocUuid="temporary_deposit_doc_1";
		Page page = new Page();
		List<TmpDepositDocRepayRecordModel> tmpDepositDocRepayRecordModelList = temporaryDepositDocHandler.queryTmpDepositDocRepayRecordModel(temDepositDocUuid,page);
		
		Assert.assertEquals(tmpDepositDocRepayRecordModelList.size(),4);
		TmpDepositDocRepayRecordModel tmpDepositDocRepayRecordModel = tmpDepositDocRepayRecordModelList.get(0);
			
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getRepaymentPlanNo(),"repayment_plan_no_1");
		Assert.assertEquals(DateUtils.format(tmpDepositDocRepayRecordModel.getAssetRecycleDate(),DateUtils.LONG_DATE_FORMAT),"2016-05-17 00:00:00");
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getPrincipal(),new BigDecimal("800.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getInterest(),new BigDecimal("60.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getServiceCharge(),new BigDecimal("20.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getMaintenanceCharge(),new BigDecimal("10.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getOtherCharge(),new BigDecimal("0.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getPenaltyFee(),new BigDecimal("0.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getLatePenalty(),new BigDecimal("0.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getLateFee(),new BigDecimal("0.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getLateOtherCost(),new BigDecimal("0.00"));
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getStatus(),SourceDocumentDetailStatus.UNSUCCESS.getChineseName());
		Assert.assertEquals(tmpDepositDocRepayRecordModel.getCheckState(),SourceDocumentDetailCheckState.CHECK_SUCCESS.getChineseName());
	}
	
	/**
	 * 查询滞留单(不传信托，结果为空)
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryTemDepositDocByQueryModelTest.sql")
	public void queryTemDepositDocByQueryModelNotFinancialContractUuidTest(){
		QueryTemporaryDepositDocModel queryTemporaryDepositDocModel =new QueryTemporaryDepositDocModel();
		Page page = new Page();
		
		List<TemporaryDepositDoc> temporaryDepositDocList= temporaryDepositDocService.queryTemDepositDocByQueryModel(queryTemporaryDepositDocModel, page);
		
		Assert.assertEquals(temporaryDepositDocList.size(), 0);
	}
	
	/**
	 * 查询滞留单（客户类型）
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryTemDepositDocByQueryModelTest.sql")
	public void queryTemDepositDocByQueryModelForCustomerTypeTest(){
		QueryTemporaryDepositDocModel queryTemporaryDepositDocModel =new QueryTemporaryDepositDocModel();
		List<String> financialUuidlist= new ArrayList<>();
		financialUuidlist.add("financial_contract_uuid_1");
		String financialContractUuidJson=JsonUtils.toJsonString(financialUuidlist);
		List<Integer> customerTypelist= new ArrayList<>();
		customerTypelist.add(0);
		String customerTypelistJson=JsonUtils.toJsonString(customerTypelist);
		queryTemporaryDepositDocModel.setCustomerTypeList(customerTypelistJson);
		queryTemporaryDepositDocModel.setFinancialContractUuids(financialContractUuidJson);
		Page page = new Page();
		
		List<TemporaryDepositDoc> temporaryDepositDocList= temporaryDepositDocService.queryTemDepositDocByQueryModel(queryTemporaryDepositDocModel, page);
		
		Assert.assertEquals(temporaryDepositDocList.size(),5);
		
		TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocList.get(2);
		Assert.assertEquals(temporaryDepositDoc.getDocNo(),"doc_co_3");
		Assert.assertEquals(temporaryDepositDoc.getOwnerName(),"owner_name_3");
		Assert.assertEquals(temporaryDepositDoc.getOwnerType().getChineseName(),"贷款人");
		Assert.assertEquals(temporaryDepositDoc.getTotalAmount(),new BigDecimal("3000.00"));
		Assert.assertEquals(temporaryDepositDoc.getBookingAmount(),new BigDecimal("1800.00"));
		Assert.assertEquals(DateUtils.format(temporaryDepositDoc.getCreatedTime(),DateUtils.LONG_DATE_FORMAT),"2017-09-30 00:00:00");
		Assert.assertEquals(temporaryDepositDoc.getStatus().getChineseName(),"部分使用");
		
	}
	
	/**
	 * 查询滞留单总数量
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryTemDepositDocByQueryModelTest.sql")
	public void queryTemDepositDocNumberByQueryModelTest(){
		QueryTemporaryDepositDocModel queryTemporaryDepositDocModel =new QueryTemporaryDepositDocModel();
		List<String> financialUuidlist= new ArrayList<>();
		financialUuidlist.add("financial_contract_uuid_1");
		String financialContractUuidJson=JsonUtils.toJsonString(financialUuidlist);
		List<Integer> customerTypelist= new ArrayList<>();
		customerTypelist.add(0);
		String customerTypelistJson=JsonUtils.toJsonString(customerTypelist);
		queryTemporaryDepositDocModel.setCustomerTypeList(customerTypelistJson);
		queryTemporaryDepositDocModel.setFinancialContractUuids(financialContractUuidJson);
		
		int queryTemDepositDocNumber= temporaryDepositDocService.queryTemDepositDocNumberByQueryModel(queryTemporaryDepositDocModel);
		Assert.assertEquals(queryTemDepositDocNumber,5);
	}
	
	/**
	 * 查询滞留单
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/queryTemDepositDocByQueryModelTest.sql")
	public void queryTemDepositDocByQueryModelTest(){
		QueryTemporaryDepositDocModel queryTemporaryDepositDocModel =new QueryTemporaryDepositDocModel();
		List<String> financialUuidlist= new ArrayList<>();
		financialUuidlist.add("financial_contract_uuid_1");
		String financialContractUuidJson=JsonUtils.toJsonString(financialUuidlist);
		queryTemporaryDepositDocModel.setFinancialContractUuids(financialContractUuidJson);
		
		List<Integer> useStatusList= new ArrayList<>();
		useStatusList.add(0);
		String status=JsonUtils.toJsonString(useStatusList);
		queryTemporaryDepositDocModel.setTmpDepositUseStatus(status);
		
		String customerName="owner_name_5";
		queryTemporaryDepositDocModel.setCustomerName(customerName);
	
		Page page = new Page();
		
		List<TemporaryDepositDoc> temporaryDepositDocList= temporaryDepositDocService.queryTemDepositDocByQueryModel(queryTemporaryDepositDocModel, page);
		
		Assert.assertEquals(temporaryDepositDocList.size(),1);
		
		TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocList.get(0);
		Assert.assertEquals(temporaryDepositDoc.getDocNo(),"doc_co_5");
		Assert.assertEquals(temporaryDepositDoc.getOwnerName(),"owner_name_5");
		Assert.assertEquals(temporaryDepositDoc.getOwnerType().getChineseName(),"贷款人");
		Assert.assertEquals(temporaryDepositDoc.getTotalAmount(),new BigDecimal("5000.00"));
		Assert.assertEquals(temporaryDepositDoc.getBookingAmount(),new BigDecimal("0.00"));
		Assert.assertEquals(DateUtils.format(temporaryDepositDoc.getCreatedTime(),DateUtils.LONG_DATE_FORMAT),"2017-10-30 01:00:00");
		Assert.assertEquals(temporaryDepositDoc.getStatus().getChineseName(),"未使用");
		
	}
	
	/**
	 * 滞留单还款(主动付款凭证－主动还款)
	 * @throws TmpDepositDetailCreateException 
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/createSourceDocumentDetailTest.sql")
	public void createSourceDocumentDetailTest() throws TmpDepositDetailCreateException{
		//VoucherTmpDepositCreateRepaymentModel的集合
		List<VoucherTmpDepositCreateRepaymentModel> repaymentModelList=new ArrayList<>();
		VoucherTmpDepositCreateRepaymentModel voucherTmpDepositCreateRepaymentModel = new VoucherTmpDepositCreateRepaymentModel();
		voucherTmpDepositCreateRepaymentModel.setContractUniqueId("contractUniqueId");;
		voucherTmpDepositCreateRepaymentModel.setRepaymentBusinessNo("repaymentBusinessNo");
		RepaymentChargesDetail repaymentChargesDetail =new RepaymentChargesDetail();
		repaymentChargesDetail.setLoanAssetPrincipal(new BigDecimal("400.00"));
		repaymentChargesDetail.setLoanAssetInterest(new BigDecimal("50.00"));
		
		repaymentModelList.add(voucherTmpDepositCreateRepaymentModel);
		//滞留单号
		String temDepositDocUuid="TemDptDoc_uuid_1";
		
		//   /* 还款类型分组：0：还款计划 1：回购单*/ 
		RepaymentBusinessType repaymentBusinessType=RepaymentBusinessType.REPAYMENT_PLAN;//==0
		TmpDepositRecoverModel recoverModel = temporaryDepositDocHandler.createSourceDocumentDetail(repaymentModelList, temDepositDocUuid, repaymentBusinessType);
		Assert.assertEquals(recoverModel==null, false);
		
		//返回TmpDepositRecoverModel
		Assert.assertEquals(recoverModel.getSourceDocumentUuid(),"source_document_uuid_1");
		Assert.assertEquals(recoverModel.getBusinessUuid(), "relatedContractUuid");
		Assert.assertEquals(recoverModel.getTmpDepositDocUuid(), "TemDptDoc_uuid_1");
		Assert.assertEquals(StringUtils.isEmpty(recoverModel.getSecondNo()),false);
		Assert.assertEquals(recoverModel.getOwnerType(), CustomerType.PERSON);
		//生成sourceDocumentDetail
		
		List<SourceDocumentDetail> sourceDocumentDetails =sourceDocumentDetailService.querySourceDocumentDetialsByfirstNo("TemDptDoc_uuid_1", null);
		TemporaryDepositDoc depositDoc =temporaryDepositDocService.getTemporaryDepositDocBy("TemDptDoc_uuid_1");
		Voucher voucher =voucherService.get_voucher_by_uuid("voucher_uuid_1");
		
		
		Assert.assertEquals(sourceDocumentDetails.size(), 1);
		SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetails.get(0);
		Assert.assertEquals(sourceDocumentDetail.getSourceDocumentUuid(), depositDoc.getSourceDocumentUuid());
		Assert.assertEquals(sourceDocumentDetail.getAmount(), voucherTmpDepositCreateRepaymentModel.getTotalAmount());
		Assert.assertEquals(sourceDocumentDetail.getFirstNo(), depositDoc.getUuid());
		Assert.assertEquals(sourceDocumentDetail.getContractUniqueId(), voucherTmpDepositCreateRepaymentModel.getContractUniqueId());
		Assert.assertEquals(sourceDocumentDetail.getRepaymentPlanNo(), "repaymentBusinessNo");
		Assert.assertEquals(sourceDocumentDetail.getFinancialContractUuid(), depositDoc.getFinancialContractUuid());
		
		
		Assert.assertEquals(sourceDocumentDetail.getSecondType(), voucher.getSecondType());
		Assert.assertEquals(sourceDocumentDetail.getPaymentBank(), voucher.getPaymentBank());
		Assert.assertEquals(sourceDocumentDetail.getReceivableAccountNo(), voucher.getReceivableAccountNo());
		Assert.assertEquals(sourceDocumentDetail.getPaymentAccountNo(), voucher.getPaymentAccountNo());
		Assert.assertEquals(sourceDocumentDetail.getPaymentName(), voucher.getPaymentName());
		Assert.assertEquals(sourceDocumentDetail.getPrincipal(), new BigDecimal("400.00"));
		Assert.assertEquals(sourceDocumentDetail.getInterest(), new BigDecimal("50.00"));
		Assert.assertEquals(sourceDocumentDetail.getServiceCharge(), new BigDecimal("0.00"));
	}
	/**
	 *滞留单还款（主动付款凭证－委托转付） 
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/recover_tmp_deposit_docTest.sql")
	public void recover_tmp_deposit_docTest(){
		TmpDepositRecoverModel recoverModel = new TmpDepositRecoverModel();
		recoverModel.setSourceDocumentUuid("source_document_uuid_1");
		recoverModel.setBusinessUuid("business_uuid_1");
		recoverModel.setTmpDepositDocUuid("TemDptDoc_uuid_1");
		recoverModel.setSecondNo("random_uuid");
		recoverModel.setOwnerType(CustomerType.PERSON);
		activePaymentVoucherProxy.recover_tmp_deposit_doc(recoverModel.getBusinessUuid(), recoverModel.getTmpDepositDocUuid(), recoverModel.getSourceDocumentUuid(), Priority.High.getPriority(), recoverModel.getSecondNo());
		
		TemporaryDepositDoc temporaryDepositDoc= temporaryDepositDocService.getTemporaryDepositDocBy("TemDptDoc_uuid_1");
		
		//滞留单
		Assert.assertEquals(temporaryDepositDoc.getTotalAmount(),new BigDecimal("1000.00"));
		Assert.assertEquals(temporaryDepositDoc.getBalance(),new BigDecimal("550.00"));
		Assert.assertEquals(temporaryDepositDoc.getStatus(),TmpDepositStatus.PART);
		Assert.assertEquals(temporaryDepositDoc.getAliveStatus(),TmpDptDocAliveStatus.CREATE);
		//sourceDoumentDertail
		String detailUuid="detail_uuid_1";
		SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.getSourceDocumentDetail(detailUuid);
		Assert.assertEquals(sourceDocumentDetail.getStatus(),SourceDocumentDetailStatus.SUCCESS);
		//sourceDoument
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy("source_document_uuid_1");
		Assert.assertEquals(sourceDocument.getSourceDocumentStatus(), SourceDocumentStatus.SIGNED);
		Assert.assertEquals(sourceDocument.getAuditStatus(), RepaymentAuditStatus.ISSUED);
		Assert.assertEquals(sourceDocument.getExcuteStatus(), SourceDocumentExcuteStatus.DONE);
		Assert.assertEquals(sourceDocument.getExcuteResult(), SourceDocumentExcuteResult.SUCCESS);
		//virtual_account
		VirtualAccount  virtualAccount = virtualAccountService.getVirtualAccountByVirtualAccountUuid("4b6e315a-7f95-4203-b081-efc0d3b28f9e");
		Assert.assertEquals(virtualAccount.getTotalBalance(), new BigDecimal("1010.00"));
		Assert.assertEquals(virtualAccount.getVirtualAccountStatus()==0,true);
		
		//AssertSet
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid("asset_uuid");
		assertEquals(AssetClearStatus.UNCLEAR, assetSet.getAssetStatus());
		assertEquals(ExecutingStatus.PROCESSING, assetSet.getExecutingStatus());
		assertEquals(OnAccountStatus.PART_WRITE_OFF, assetSet.getOnAccountStatus());
		
		//ledgerBookItem
		BigDecimal reciveAmount = ledgerBookStatHandler.unrecovered_asset_snapshot("ledger_book_no", "asset_uuid", "customer_uuid_1", true);
		Assert.assertEquals(reciveAmount,new BigDecimal("600.00") );
	}
	
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/create_job_source_document_detailTest.sql")
	public void create_job_source_document_detailTest(){
		String secondNo="random_uuid";
		TmpDepositRecoverModel recoverModel = new TmpDepositRecoverModel();
		recoverModel.setTmpDepositDocUuid("TemDptDoc_uuid_1");
		recoverModel.setSecondNo(secondNo);
		BigDecimal detailTotalAmount = new BigDecimal("450");
		
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy("source_document_uuid_1");
		/**
		 * 第一步//走队列
		 */
		List<String> detailUuids = sourceDocumentDetailService.getDetailUuidByVoucherUuid(sourceDocument.getVoucherUuid(),"TemDptDoc_uuid_1");
		Map<String, String> detailUuidAndContractUuid = dstJobTmpDepositReconciliation.criticalMarker(detailUuids);
		
		Assert.assertEquals(detailUuidAndContractUuid.size(), 1);
		Assert.assertEquals(detailUuidAndContractUuid.get("detail_uuid_1"), "relatedContractUuid");
		
		//===
		
		TmpDepositReconciliationParameters sourceDocumentReconciliationParameters=new TmpDepositReconciliationParameters();
		sourceDocumentReconciliationParameters.setFinancialContractNo("financial_contract_no");
		sourceDocumentReconciliationParameters.setLedgerBookNo("ledger_book_no");
		sourceDocumentReconciliationParameters.setSourceDocumentDetailUuid("detail_uuid_1");
		sourceDocumentReconciliationParameters.setSourceDocumentUuid("source_document_uuid_1");
		sourceDocumentReconciliationParameters.setSecondNo(secondNo);
		sourceDocumentReconciliationParameters.setDetailTotalAmount(detailTotalAmount);
		List<TmpDepositReconciliationParameters> ParametersList= new ArrayList<>();
		ParametersList.add(sourceDocumentReconciliationParameters);
		/**
		 * 第二步//效验金额明细
		 */
		//TmpDepositReconciliationParameters Model的List（sourceDocumentUuid;sourceDocumentDetailUuid;financialContractNo;ledgerBookNo;tmpDepositDocUuid;secondType;）
//		String reconciliationStepTwoParameterList = JsonUtils.toJsonString(ParametersList);
		dstJobTmpDepositReconciliation.validateDetailList(ParametersList);
		SourceDocumentDetail sourceDocumentDetail =sourceDocumentDetailService.getSourceDocumentDetail("detail_uuid_1");
		
		Assert.assertEquals(sourceDocumentDetail.getCheckState(), SourceDocumentDetailCheckState.CHECK_SUCCESS);
		//===
		
		TmpDepositReconciliationParameters sourceDocumentReconciliationParameters1=new TmpDepositReconciliationParameters();
		sourceDocumentReconciliationParameters1.setFinancialContractNo("financial_contract_no");
		sourceDocumentReconciliationParameters1.setLedgerBookNo("ledger_book_no");
		sourceDocumentReconciliationParameters1.setSourceDocumentDetailUuid("detail_uuid_1");
		sourceDocumentReconciliationParameters1.setSourceDocumentUuid("source_document_uuid_1");
		sourceDocumentReconciliationParameters1.setTmpDepositDocUuid("TemDptDoc_uuid_1");
		sourceDocumentReconciliationParameters1.setSecondType("enum.voucher-type.pay");
		sourceDocumentReconciliationParameters1.setDetailTotalAmount(detailTotalAmount);
		sourceDocumentReconciliationParameters1.setSecondNo(secondNo);
		List<TmpDepositReconciliationParameters> ParametersList1= new ArrayList<>();
		ParametersList1.add(sourceDocumentReconciliationParameters1);
		/**
		 * 第三步
		 */
		//TmpDepositReconciliationParameters Model的list
//		String reconciliationStepThreeParameterList = JsonUtils.toJsonString(ParametersList1);
		dstJobTmpDepositReconciliation.virtual_account_transfer(ParametersList1);
		
		//虚户余额
		VirtualAccount VirtualAccount3 = virtualAccountService.getVirtualAccountByVirtualAccountUuid("4b6e315a-7f95-4203-b081-efc0d3b28f99");
		Assert.assertEquals(VirtualAccount3.getTotalBalance(),new BigDecimal("1010.00") );
		//账本资金变化
		BigDecimal VirtualAccountAmount = ledgerBookVirtualAccountHandler.get_balance_of_customer("ledger_book_no", "company_customer_uuid_1");
		Assert.assertEquals(VirtualAccountAmount,new BigDecimal("1010.00") );
		BigDecimal frozenAmount= ledgerItemService.getBalancedAmount("ledger_book_no", ChartOfAccount.FST_FROZEN_CAPITAL, "company_customer_uuid_1", "", null, null, "", "", "");
		Assert.assertEquals(frozenAmount, new BigDecimal("-450.00"));
		//===

		/**
		 * 第四步
		 */
		//TmpDepositReconciliationParameters　Model的list
//		String reconciliationStepFourParameterList = JsonUtils.toJsonString(ParametersList1);
		dstJobTmpDepositReconciliation.recover_details(ParametersList1);
		//还款计划
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid("asset_uuid");
		assertEquals(AssetClearStatus.UNCLEAR, assetSet.getAssetStatus());
		assertEquals(ExecutingStatus.PROCESSING, assetSet.getExecutingStatus());
		assertEquals(OnAccountStatus.PART_WRITE_OFF, assetSet.getOnAccountStatus());
		//journalvoucher
		JournalVoucher journalvoucher =journalVoucherService.getInforceJournalVoucher("detail_uuid_1");
		Assert.assertEquals(journalvoucher!=null, true);
		Assert.assertEquals(journalvoucher.getStatus(),JournalVoucherStatus.VOUCHER_ISSUED);
		//账本明细ledgerbookItem	
		BigDecimal VirtualAccountAmount4 = ledgerBookVirtualAccountHandler.get_balance_of_customer("ledger_book_no", "company_customer_uuid_1");
		Assert.assertEquals(VirtualAccountAmount4,new BigDecimal("1010.00") );
		
		/**
		 * 第五步
		 */
		//TmpDepositReconciliationParameters　Model的list
//		String reconciliationStepfiveParameterList = JsonUtils.toJsonString(ParametersList1);
		dstJobTmpDepositReconciliation.unfreeze_capical(ParametersList1);
		//ledgerBookItem
		BigDecimal VirtualAccountAmount5 = ledgerBookVirtualAccountHandler.get_balance_of_customer("ledger_book_no", "company_customer_uuid_1");
		Assert.assertEquals(VirtualAccountAmount5,new BigDecimal("1010.00") );
		VirtualAccount VirtualAccount = virtualAccountService.getVirtualAccountByVirtualAccountUuid("4b6e315a-7f95-4203-b081-efc0d3b28f99");
		Assert.assertEquals(VirtualAccount.getTotalBalance(),new BigDecimal("1010.00") );
		
		//sourceDoumentDertail
		String detailUuid="detail_uuid_1";
		SourceDocumentDetail sourceDocumentDetail1 = sourceDocumentDetailService.getSourceDocumentDetail(detailUuid);
		Assert.assertEquals(sourceDocumentDetail1.getStatus(),SourceDocumentDetailStatus.SUCCESS);

		//滞留单
		TemporaryDepositDoc temporaryDepositDoc= temporaryDepositDocService.getTemporaryDepositDocBy("TemDptDoc_uuid_1");
		Assert.assertEquals(temporaryDepositDoc.getTotalAmount(),new BigDecimal("1000.00"));
		Assert.assertEquals(temporaryDepositDoc.getBalance(),new BigDecimal("550.00"));
		Assert.assertEquals(temporaryDepositDoc.getStatus(),TmpDepositStatus.PART);
		Assert.assertEquals(temporaryDepositDoc.getAliveStatus(),TmpDptDocAliveStatus.CREATE);
		
		BigDecimal reciveAmount = ledgerBookStatHandler.unrecovered_asset_snapshot("ledger_book_no", "asset_uuid", "company_customer_uuid_1", true);
		Assert.assertEquals(reciveAmount,new BigDecimal("600.00") );
	}
	
	/**
	 * sourceDocumentDetail的金额明细与账本的应收还款明细不符
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/create_job_source_document_detail_for_detail_errorTest.sql")
	public void create_job_source_document_detail_for_detail_errorTest(){
			TmpDepositRecoverModel recoverModel = new TmpDepositRecoverModel();
			recoverModel.setTmpDepositDocUuid("TemDptDoc_uuid_1");
			recoverModel.setSecondNo("random_uuid");
			
			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy("source_document_uuid_1");
			/**
			 * 第一步//走队列
			 */
			List<String> detailUuids = sourceDocumentDetailService.getDetailUuidByVoucherUuid(sourceDocument.getVoucherUuid(),"TemDptDoc_uuid_1");
			Map<String, String> detailUuidAndContractUuid = dstJobTmpDepositReconciliation.criticalMarker(detailUuids);
			Assert.assertEquals(detailUuidAndContractUuid.size(), 1);
			Assert.assertEquals(detailUuidAndContractUuid.get("detail_uuid_1"), "relatedContractUuid");
			//===
			
			TmpDepositReconciliationParameters sourceDocumentReconciliationParameters=new TmpDepositReconciliationParameters();
			sourceDocumentReconciliationParameters.setFinancialContractNo("financial_contract_no");
			sourceDocumentReconciliationParameters.setLedgerBookNo("ledger_book_no");
			sourceDocumentReconciliationParameters.setSourceDocumentDetailUuid("detail_uuid_1");
			sourceDocumentReconciliationParameters.setSourceDocumentUuid("source_document_uuid_1");
			List<TmpDepositReconciliationParameters> ParametersList= new ArrayList<>();
			ParametersList.add(sourceDocumentReconciliationParameters);
			/**
			 * 第二步//效验金额明细
			 */
			//TmpDepositReconciliationParameters Model的List（sourceDocumentUuid;sourceDocumentDetailUuid;financialContractNo;ledgerBookNo;tmpDepositDocUuid;secondType;）
//			String reconciliationStepTwoParameterList = JsonUtils.toJsonString(ParametersList);
			dstJobTmpDepositReconciliation.validateDetailList(ParametersList);
		
			SourceDocumentDetail sourceDocumentDetail =sourceDocumentDetailService.getSourceDocumentDetail("detail_uuid_1");
			
			Assert.assertEquals(sourceDocumentDetail.getCheckState(), SourceDocumentDetailCheckState.CHECK_FAILS);
	
		}
	
	/**
	 * 滞留单查询详情
	 */
	@Test
	@Sql("classpath:/test/yunxin/temporaryDepositDoc/showTmpDepositDocInfo.sql")
	public void showTmpDepositDocInfo(){
		String temDeporaryDocUuid="TemDptDoc_uuid_1";
		TemDepositDocShowModel temDepositDocShowModel = temporaryDepositDocHandler.queryTemDeporaryDocInformation(temDeporaryDocUuid);
		
		Assert.assertEquals(temDepositDocShowModel.getTemDepositDocNo(),"doc_co_1");
		Assert.assertEquals(temDepositDocShowModel.getTemDepositTotalAmount(),new BigDecimal("1000.00"));
		Assert.assertEquals(temDepositDocShowModel.getTemDepositUsedAmount(),new BigDecimal("0.00"));
		Assert.assertEquals(DateUtils.format(temDepositDocShowModel.getTemDepositCreateTime(),DateUtils.LONG_DATE_FORMAT),"2017-10-30 00:00:00");
		Assert.assertEquals(temDepositDocShowModel.getTmpDepositUseStatus(),"创建");
		Assert.assertEquals(DateUtils.format(temDepositDocShowModel.getTmpDepositLastModifyTime(), DateUtils.LONG_DATE_FORMAT),"2017-10-30 00:00:00");
		Assert.assertEquals(temDepositDocShowModel.getFinacialContractName(),"sasad");
		
		Assert.assertEquals(temDepositDocShowModel.getContractNo(),"contract_no_1" );
		Assert.assertEquals(temDepositDocShowModel.getContractId(),"1" );
		Assert.assertEquals(temDepositDocShowModel.getCustomerNo(),"VACC275B96CD27E64F02");
		Assert.assertEquals(temDepositDocShowModel.getCustomerName(),"测试员1");
		Assert.assertEquals(temDepositDocShowModel.getCustomerType(),"个人" );
		Assert.assertEquals(temDepositDocShowModel.getPayCustomerName(),"范腾" );
		Assert.assertEquals(temDepositDocShowModel.getBankNo(), "6214855712107780");
		Assert.assertEquals(temDepositDocShowModel.getBankName(), "");
		Assert.assertEquals(temDepositDocShowModel.getBankLocal(), "暂不显示");
		
		Assert.assertEquals(temDepositDocShowModel.getVoucherNo(), "voucher_no_1");
		Assert.assertEquals(temDepositDocShowModel.getReceivableAccountNo(), null);
		Assert.assertEquals(temDepositDocShowModel.getPaymentAccountNo(), "6214855712106520");
		Assert.assertEquals(temDepositDocShowModel.getVoucherCustomerName(), "夏侯你惇哥");
		Assert.assertEquals(temDepositDocShowModel.getVoucherAmount(), "570.00");
		Assert.assertEquals(temDepositDocShowModel.getFirstType(), "商户付款凭证");
		Assert.assertEquals(temDepositDocShowModel.getSecondType(), "主动付款");
		Assert.assertEquals(temDepositDocShowModel.getVoucherLastModifyTime(), "2017-05-09 22:40:02");
		Assert.assertEquals(temDepositDocShowModel.getCashFlowTime(), "2017-05-09 22:39:23");
		
		Assert.assertEquals(temDepositDocShowModel.getVoucherStatus(), "未核销");
		Assert.assertEquals(temDepositDocShowModel.getVirtualAccountUuid(), "virtual_account_uuid_1");
		Assert.assertEquals(temDepositDocShowModel.getVoucherId(), "１");
		
	}
	
	@Test
	public void testA(){
		List<TemplatesForPay> detailForPays=new ArrayList<>();
		String aa = "[{\"amount\":980,\"contractNo\":\"妹妹你大胆的往前走11\",\"customerName\":\"高渐离啊\",\"interest\":\"20.0\",\"loanServiceFee\":\"20.0\",\"otherFee\":\"20.0\",\"principal\":\"900.0\",\"repaymentNo\":\"ZC129957414701965312\",\"techServiceFee\":\"20.0\"}]";
		detailForPays = JsonUtils.parseArray(aa,TemplatesForPay.class);
		
		   BigDecimal detailAmountTotal=BigDecimal.ZERO;
		   for(TemplatesForPay templatesForPay: detailForPays){
			   detailAmountTotal = detailAmountTotal.add(templatesForPay.getAmount());
			}
		   System.out.println(detailAmountTotal);
	}
	
}


