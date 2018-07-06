package com.zufangbao.earth.yunxin.customize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.hathaway.util.JsonUtils;
import com.suidifu.matryoshka.customize.ReconciliationDelayTaskProcess;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentExcuteResult;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentExcuteStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:/local/applicationContext-*.xml"})
@Rollback(true)
public class ReconciliationDelayTaskProcessTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

    @Autowired
    private ContractService contractService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private ReconciliationDelayTaskProcess reconciliationDelayTaskProcess;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    @Qualifier("delayProcessingTaskDBService")
    private DelayProcessingTaskService delayProcessingTaskService;
    @Autowired
    private BusinessPaymentVoucherSession businessPaymentVoucherSession;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private RepurchaseService repurchaseService;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Autowired
	private LedgerBookService ledgerBookService;
    @Autowired
	private JournalVoucherService journalVoucherService;
    @Autowired
	private VirtualAccountService virtualAccountService;
    @Autowired
	private VirtualAccountFlowService virtualAccountFlowService;

    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskProcessorTest.sql",
    	"classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
    public void test_processing_delay_task() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String recover_delay_task_uuid = "delay_task_config_uuid_01";
    	String customerAccount = "340826199407034015";
        Date tradeTime = DateUtils.parseDate("2017-01-01 01:01:01", DateUtils.LONG_DATE_FORMAT);
        String assetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        Contract contract = contractService.getContract(1L);
        Map<String, BigDecimal> chargesMap = new HashMap<String,BigDecimal>();
        chargesMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY,new BigDecimal(100));
		chargesMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY,new BigDecimal(1));
		chargesMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY,new BigDecimal(2));
		chargesMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY,new BigDecimal(3));
		chargesMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY,new BigDecimal(4));
		chargesMap.put(ExtraChargeSpec.PENALTY_KEY,new BigDecimal(5));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY,new BigDecimal(6));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY,new BigDecimal(7));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY,new BigDecimal(8));
        RepaymentChargesDetail chargesDetail = new RepaymentChargesDetail(chargesMap);
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
        try {
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet, null, AssetRecoverType.LOAN_ASSET, recover_delay_task_uuid);
        } catch(Exception e){
        	e.printStackTrace();
        	fail();
        }
        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repaymentPlanUuid(assetUuid);
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertEquals(assetUuid,task.getRepaymentPlanUuid());

        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertTrue(StringUtils.isNotEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(), workParams.get("loanNo"));
        assertEquals(customerAccount,workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("100.00",workParams.get("paidPrincipal")+"");
        assertEquals("1.00",workParams.get("paidInterest"));
        assertEquals("35.00",workParams.get("paidRepayCharge")+"");
        assertEquals("4",workParams.get("repayType")+"");
        assertEquals(DateUtils.format(tradeTime,com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT),workParams.get("tradeTime")+"");
    }

    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskProcessorTest.sql",
    	"classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
    public void test_processing_delay_task_for_repurchase() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String recover_delay_task_uuid = "delay_task_config_uuid_01";
    	String customerAccount = "340826199407034015";
    	Date tradeTime = DateUtils.parseDate("2017-01-01 01:01:01", DateUtils.LONG_DATE_FORMAT);
        String assetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        String repurchase_doc_1 = "repurchase_doc_uuid_1";
        Contract contract = contractService.getContract(1L);
        Map<String, BigDecimal> chargesMap = new HashMap<String,BigDecimal>();
        chargesMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY,new BigDecimal(100));
		chargesMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY,new BigDecimal(1));
		chargesMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY,new BigDecimal(2));
		chargesMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY,new BigDecimal(3));
		chargesMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY,new BigDecimal(4));
		chargesMap.put(ExtraChargeSpec.PENALTY_KEY,new BigDecimal(5));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY,new BigDecimal(6));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY,new BigDecimal(7));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY,new BigDecimal(8));
        RepaymentChargesDetail chargesDetail = new RepaymentChargesDetail(chargesMap);
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
        try {
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, null, repurchase_doc_1, AssetRecoverType.REPURCHASE_ASSET, recover_delay_task_uuid);
        } catch(Exception e){
        	e.printStackTrace();
        	fail();
        }

        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repurchaseDocUuid(repurchase_doc_1);
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertTrue(StringUtils.isEmpty(task.getRepaymentPlanUuid()));

        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertTrue(StringUtils.isNotEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(), workParams.get("loanNo"));
        assertEquals(customerAccount,workParams.get("thirdUserNo")+"");
        assertEquals("0",workParams.get("installmentNo")+"");
        assertEquals("100.00",workParams.get("paidPrincipal")+"");
        assertEquals("1.00",workParams.get("paidInterest"));
        assertEquals("35.00",workParams.get("paidRepayCharge")+"");
        assertEquals("3",workParams.get("repayType")+"");
        assertEquals(DateUtils.format(tradeTime,com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT),workParams.get("tradeTime")+"");

    }

    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskProcessor_test_repay_type.sql",
    	"classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
    public void test_processing_delay_task_test_repayment_type() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String recover_delay_task_uuid = "delay_task_config_uuid_01";
    	Date tradeTime = DateUtils.parseDate("2017-01-01 01:01:01", DateUtils.LONG_DATE_FORMAT);
        String assetUuid_1 = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        String assetUuid_2 = "asset_set_uuid_2";
        String assetUuid_3 = "asset_set_uuid_3";
        String assetUuid_4 = "asset_set_uuid_4";
        String assetUuid_5 = "asset_set_uuid_5";
        Contract contract = contractService.getContract(1L);
        RepaymentChargesDetail chargesDetail = new RepaymentChargesDetail(new HashMap<String,BigDecimal>());
        AssetSet assetSet_1 = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid_1);
        AssetSet assetSet_2 = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid_2);
        AssetSet assetSet_3 = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid_3);
        AssetSet assetSet_4 = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid_4);
        AssetSet assetSet_5 = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid_5);
        String repurchaseUuid = "repurchase_doc_uuid_1";
        try {
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet_1, null, AssetRecoverType.LOAN_ASSET, recover_delay_task_uuid);
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet_2, null, AssetRecoverType.LOAN_ASSET, recover_delay_task_uuid);
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet_3, null, AssetRecoverType.LOAN_ASSET, recover_delay_task_uuid);
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet_4, null, AssetRecoverType.LOAN_ASSET, recover_delay_task_uuid);
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet_5, null, AssetRecoverType.LOAN_ASSET, recover_delay_task_uuid);
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, null, repurchaseUuid, AssetRecoverType.REPURCHASE_ASSET, recover_delay_task_uuid);

        } catch(Exception e){
        	e.printStackTrace();
        	fail();
        }
        assetRepayType("1","1", assetUuid_1);//6
        assetRepayType("4","2", assetUuid_2);//7
        assetRepayType("1","3", assetUuid_3);//8
        assetRepayType("2","4", assetUuid_4);//9
        assetRepayType("4","5", assetUuid_5);//1

        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repurchaseDocUuid(repurchaseUuid);

        Map<String,Object> workParams = JsonUtils.parse(delayTasks.get(0).getWorkParams());
        assertEquals("3",workParams.get("repayType")+"");
        assertEquals("0",workParams.get("installmentNo")+"");
    }

    private void assetRepayType (String repaymentType,String period, String assetUuid){
    	List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repaymentPlanUuid(assetUuid);
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertEquals(repaymentType,workParams.get("repayType")+"");
    }

    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskProcessorTest.sql",
    	"classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
    public void test_processing_delay_task_for_exception() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String recover_delay_task_uuid = "delay_task_config_uuid_01";
    	Date tradeTime = DateUtils.parseDate("2017-01-01 01:01:01", DateUtils.LONG_DATE_FORMAT);
        String assetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        Contract contract = contractService.getContract(1L);
        Map<String, BigDecimal> chargesMap = new HashMap<String,BigDecimal>();
        chargesMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY,new BigDecimal(100));
		chargesMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY,new BigDecimal(1));
		chargesMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY,new BigDecimal(2));
		chargesMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY,new BigDecimal(3));
		chargesMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY,new BigDecimal(4));
		chargesMap.put(ExtraChargeSpec.PENALTY_KEY,new BigDecimal(5));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY,new BigDecimal(6));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY,new BigDecimal(7));
		chargesMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY,new BigDecimal(8));
        RepaymentChargesDetail chargesDetail = new RepaymentChargesDetail(chargesMap);
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
        try {
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet, null, null, recover_delay_task_uuid);
        } catch(Exception e){
        	e.printStackTrace();
        	fail();
        }

        try {
        	reconciliationDelayTaskProcess.processingDelayTask(tradeTime, contract, chargesDetail, assetSet, null, AssetRecoverType.GUARANTEE_ASSET, recover_delay_task_uuid);
        } catch(Exception e){
        	e.printStackTrace();
        	fail();
        }

    }

    
    @Test
	@Sql({"classpath:test/yunxin/customize/test_delay_task_repurchase_reconciliation.sql",
            "classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
	public void test_check_repurchase_write_off(){
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String repurchase_doc_uuid = "repayment_plan_no_1";
		String contract_unique_id = "contract_unique_id_1";
		String customerAccount = "customer_account_2";
        try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class,1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class,1L);
		BigDecimal amount = repurchaseDoc.getAmount();
		assertEquals(SourceDocumentExcuteResult.SUCCESS,sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE,sourceDocument.getExcuteStatus());

        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repurchaseDocUuid(repurchase_doc_uuid);
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_2",task.getCustomerUuid());
        assertTrue(StringUtils.isEmpty(task.getRepaymentPlanUuid()));

        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertTrue(StringUtils.isNotEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract_unique_id, workParams.get("loanNo"));
        assertEquals(customerAccount,workParams.get("thirdUserNo")+"");
        assertEquals("0",workParams.get("installmentNo")+"");
        assertEquals("500.00",workParams.get("paidPrincipal")+"");
        assertEquals("0.00",workParams.get("paidInterest"));
        assertEquals("0.00",workParams.get("paidRepayCharge")+"");
        assertEquals("3",workParams.get("repayType")+"");
        assertEquals("2016-10-27 20:00:17",workParams.get("tradeTime")+"");

    }

    /*
	 * 商户付款核销    代偿  部分核销（ 按明细销）
	 */
    @Test
    @Sql(value={"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/customize/business_pay_voucher_detail_asset.sql",
            "classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
    public void test_check_and_business_pay_detail(){
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");

        String repaymentPlanUuid = "asset_uuid_1";
        String contract_unique_id = "contract_unique_id_2";
        String customerAccount = "customer_account_1";
        try {
            businessPaymentVoucherSession.handler_recover_asset_by_source_document();
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }

        SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class,1L);

        List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
        assertEquals(1,details.size());

        String borrower_customerUuid = "customerUuid1";
        String company_customerUuid = "company_customer_uuid_1";
        String ledgerBookNo = "yunxin_ledger_book";
        BigDecimal total_bookingAmount = new BigDecimal("1500");

        assertEquals(SourceDocumentDetailStatus.SUCCESS,details.get(0).getStatus());

        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repaymentPlanUuid(repaymentPlanUuid);
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_2",task.getContractUuid());
        assertEquals("customerUuid1",task.getCustomerUuid());
        assertEquals(repaymentPlanUuid,task.getRepaymentPlanUuid());

        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertTrue(StringUtils.isNotEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract_unique_id, workParams.get("loanNo"));
        assertEquals(customerAccount,workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("800.00",workParams.get("paidPrincipal")+"");
        assertEquals("50.00",workParams.get("paidInterest"));
        assertEquals("25.00",workParams.get("paidRepayCharge")+"");
        assertEquals("4",workParams.get("repayType")+"");
        assertEquals("2016-10-27 20:00:17",workParams.get("tradeTime")+"");

    }
    
    /*
	 * 回购销账
	 * 
	 * 不足额 有明细
	 */
	@Test
	@Sql({"classpath:test/yunxin/delete_all_table.sql","classpath:test/yunxin/customize/test_recover_repurchase_part.sql",
        "classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
	public void test_check_repurchase_write_off_ww(){
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");
	    try {
			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
		} catch(Exception e){
			e.printStackTrace();
			fail();
		}
		
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class,1L);
		RepurchaseDoc repurchaseDoc = repurchaseService.load(RepurchaseDoc.class,1L);
		assertEquals(SourceDocumentExcuteResult.SUCCESS,sourceDocument.getExcuteResult());
		assertEquals(SourceDocumentExcuteStatus.DONE,sourceDocument.getExcuteStatus());
		List<SourceDocumentDetail> details = sourceDocumentDetailService.getDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid(), sourceDocument.getOutlierSerialGlobalIdentity());
		assertEquals(1,details.size());
		
		String contract_unique_id="contract_unique_id_1";
		String customerAccount="customer_account_2";
		String repurchase_doc_uuid="repayment_plan_no_1";
		//sourceDocumentDetail 和  repurchaseDoc 状态更改
		assertEquals(SourceDocumentDetailStatus.SUCCESS,details.get(0).getStatus());
		assertEquals(RepurchaseStatus.REPURCHASING, repurchaseDoc.getRepurchaseStatus());

        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repurchaseDocUuid(repurchase_doc_uuid);
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_2",task.getCustomerUuid());
        assertTrue(StringUtils.isEmpty(task.getRepaymentPlanUuid()));

        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertTrue(StringUtils.isNotEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract_unique_id, workParams.get("loanNo"));
        assertEquals(customerAccount,workParams.get("thirdUserNo")+"");
        assertEquals("0",workParams.get("installmentNo")+"");
        assertEquals("1000.00",workParams.get("paidPrincipal")+"");
        assertEquals("50.00",workParams.get("paidInterest"));
        assertEquals("250.00",workParams.get("paidRepayCharge")+"");
        assertEquals("3",workParams.get("repayType")+"");
        assertEquals("2016-08-24 16:56:18",workParams.get("tradeTime")+"");
	}


}
