package com.zufangbao.earth.yunxin.customize;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.hathaway.util.JsonUtils;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.earth.yunxin.customize.scripts.ReconciliationDelayTaskServices;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:/local/applicationContext-*.xml"})
@Rollback(false)
public class ReconciliationDelayTaskServicesTest extends AbstractTransactionalJUnit4SpringContextTests {

    private final static Log logger = LogFactory.getLog(ReconciliationDelayTaskServicesTest.class);
    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;
    @Autowired
    @Qualifier("delayProcessingTaskDBService")
    private DelayProcessingTaskService delayProcessingTaskService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

   @Autowired
   private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @Sql("classpath:test/yunxin/customize/reconciliationDelayTaskServicesTest.sql")
    public void test_evaluate() {
        Result processedResult = new Result();
        Contract contract = contractService.getContract(1L);
        String assetSetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        String configUuid = "config_uuid_1";
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
        Date tradeTime = new Date();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("configUuid",configUuid);
        inputMap.put("uniqueId", contract.getUniqueId());
        inputMap.put("contractNo", contract.getContractNo());
        inputMap.put("repaymentPlanUuid", assetSet==null?null:assetSet.getAssetUuid());
        inputMap.put("repurchaseDocUuid", "");
        inputMap.put("assetRecoverType", AssetRecoverType.LOAN_ASSET);
        inputMap.put("tradeTime",tradeTime);

        RepaymentChargesDetail detailAmount = new RepaymentChargesDetail();
        detailAmount.setLoanAssetPrincipal(new BigDecimal("100"));
        detailAmount.setLoanAssetInterest(new BigDecimal("10"));
        detailAmount.setLoanTechFee(new BigDecimal("20"));
        detailAmount.setLoanOtherFee(new BigDecimal("20"));

        inputMap.put("detailAmount",detailAmount);
        Integer currentPeriod = null;
        inputMap.put("repaymentPlanModifyReason",null);
        if(assetSet!=null){
            currentPeriod = assetSet.getCurrentPeriod();
            inputMap.put("assetSetCurrentPeriod",currentPeriod);
        }


        inputMap.put("uniqueId", "51fd9fbc-1e13-473b-92c5-4adfade246b7");
        inputMap.put("contractNo", "云信信2016-78-DK（378）号");
        inputMap.put("repaymentPlanUuid", assetSetUuid);
        inputMap.put("configUuid",configUuid);

        Map<String,Object> outputMap = new HashMap<String,Object>();
        ReconciliationDelayTaskServices reconciliationDelayTaskServices = new ReconciliationDelayTaskServices();
        reconciliationDelayTaskServices.evaluate(processedResult, delayProcessingTaskCacheHandler, sandboxDataSetHandler, inputMap, outputMap, logger);

        List<DelayProcessingTask> tasks =  delayProcessingTaskService.get_by_repaymentPlanUuid(assetSetUuid);
        assertEquals(1,tasks.size());
        DelayProcessingTask task = tasks.get(0);
        assertNotNull(task);

        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertEquals(assetSetUuid,task.getRepaymentPlanUuid());
        assertEquals(configUuid,task.getConfigUuid());
        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertFalse(StringUtils.isEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(),workParams.get("loanNo")+"");
        assertEquals("340826199407034015",workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("100.00",workParams.get("paidPrincipal")+"");
        assertEquals("10.00",workParams.get("paidInterest")+"");
        assertEquals("40.00",workParams.get("paidRepayCharge")+"");
        assertEquals("4",workParams.get("repayType")+"");
        assertEquals(DateUtils.format(tradeTime,DateUtils.LONG_DATE_FORMAT),workParams.get("tradeTime")+"");
    }

    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskServicesTest.sql",
            "classpath:test/yunxin/customize/test_insert_reconciliation_source_repository.sql"})
    public void test_evaluate_scripts() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");
        Result processedResult = new Result();
        Contract contract = contractService.getContract(1L);
        String assetSetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        String configUuid = "config_uuid_1";
        String recover_delay_task_uuid = "delay_task_config_uuid_01";
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
        Date tradeTime = new Date();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("configUuid",configUuid);
        inputMap.put("uniqueId", contract.getUniqueId());
        inputMap.put("contractNo", contract.getContractNo());
        inputMap.put("repaymentPlanUuid", assetSet==null?null:assetSet.getAssetUuid());
        inputMap.put("repurchaseDocUuid", "");
        inputMap.put("assetRecoverType", AssetRecoverType.LOAN_ASSET);
        inputMap.put("tradeTime",tradeTime);

        RepaymentChargesDetail detailAmount = new RepaymentChargesDetail();
        detailAmount.setLoanAssetPrincipal(new BigDecimal("100"));
        detailAmount.setLoanAssetInterest(new BigDecimal("10"));
        detailAmount.setLoanTechFee(new BigDecimal("20"));
        detailAmount.setLoanOtherFee(new BigDecimal("20"));

        inputMap.put("detailAmount",detailAmount);
        Integer currentPeriod = null;
        inputMap.put("repaymentPlanModifyReason",null);
        if(assetSet!=null){
            currentPeriod = assetSet.getCurrentPeriod();
            inputMap.put("assetSetCurrentPeriod",currentPeriod);
        }


        inputMap.put("uniqueId", "51fd9fbc-1e13-473b-92c5-4adfade246b7");
        inputMap.put("contractNo", "云信信2016-78-DK（378）号");
        inputMap.put("repaymentPlanUuid", assetSetUuid);
        inputMap.put("configUuid",configUuid);

        Map<String,Object> outputMap = new HashMap<String,Object>();
        DelayTaskServices reconciliationDelayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(recover_delay_task_uuid);
        reconciliationDelayTaskServices.evaluate(processedResult, delayProcessingTaskCacheHandler, sandboxDataSetHandler, inputMap, outputMap, logger);

        List<DelayProcessingTask> tasks =  delayProcessingTaskService.get_by_repaymentPlanUuid(assetSetUuid);
        assertEquals(1,tasks.size());
        DelayProcessingTask task = tasks.get(0);
        assertNotNull(task);

        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertEquals(assetSetUuid,task.getRepaymentPlanUuid());
        assertEquals(configUuid,task.getConfigUuid());
        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertFalse(StringUtils.isEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(),workParams.get("loanNo")+"");
//        assertEquals("340826199407034015",workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("100.00",workParams.get("paidPrincipal")+"");
        assertEquals("10.00",workParams.get("paidInterest")+"");
        assertEquals("40.00",workParams.get("paidRepayCharge")+"");
        assertEquals("4",workParams.get("repayType")+"");
        assertEquals(DateUtils.format(tradeTime,DateUtils.LONG_DATE_FORMAT),workParams.get("tradeTime")+"");
    }

    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskServicesTest.sql",
            "classpath:test/yunxin/customize/test_insert_reconciliation_source_repository_bqtwo.sql"})
    public void test_evaluate_scripts_bqtwo() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");
        Result processedResult = new Result();
        Contract contract = contractService.getContract(1L);
        String assetSetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        String configUuid = "config_uuid_1";
        String recover_delay_task_uuid = "delay_task_config_uuid_01";
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
        Date tradeTime = new Date();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("configUuid",configUuid);
        inputMap.put("uniqueId", contract.getUniqueId());
        inputMap.put("contractNo", contract.getContractNo());
        inputMap.put("repaymentPlanUuid", assetSet==null?null:assetSet.getAssetUuid());
        inputMap.put("repurchaseDocUuid", "");
        inputMap.put("assetRecoverType", AssetRecoverType.LOAN_ASSET);
        inputMap.put("tradeTime",tradeTime);

        RepaymentChargesDetail detailAmount = new RepaymentChargesDetail();
        detailAmount.setLoanAssetPrincipal(new BigDecimal("100"));
        detailAmount.setLoanAssetInterest(new BigDecimal("10"));
        detailAmount.setLoanTechFee(new BigDecimal("20"));
        detailAmount.setLoanOtherFee(new BigDecimal("20"));

        inputMap.put("detailAmount",detailAmount);
        Integer currentPeriod = null;
        inputMap.put("repaymentPlanModifyReason",null);
        if(assetSet!=null){
            currentPeriod = assetSet.getCurrentPeriod();
            inputMap.put("assetSetCurrentPeriod",currentPeriod);
        }


        inputMap.put("uniqueId", "51fd9fbc-1e13-473b-92c5-4adfade246b7");
        inputMap.put("contractNo", "云信信2016-78-DK（378）号");
        inputMap.put("repaymentPlanUuid", assetSetUuid);
        inputMap.put("configUuid",configUuid);

        Map<String,Object> outputMap = new HashMap<String,Object>();
        DelayTaskServices reconciliationDelayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(recover_delay_task_uuid);
        reconciliationDelayTaskServices.evaluate(processedResult, delayProcessingTaskCacheHandler, sandboxDataSetHandler, inputMap, outputMap, logger);

        List<DelayProcessingTask> tasks =  delayProcessingTaskService.get_by_repaymentPlanUuid(assetSetUuid);
        assertEquals(1,tasks.size());
        DelayProcessingTask task = tasks.get(0);
        assertNotNull(task);

        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertEquals(assetSetUuid,task.getRepaymentPlanUuid());
        assertEquals(configUuid,task.getConfigUuid());
        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertFalse(StringUtils.isEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(),workParams.get("loanNo")+"");
//        assertEquals("340826199407034015",workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("100.00",workParams.get("paidPrincipal")+"");
        assertEquals("10.00",workParams.get("paidInterest")+"");
        assertEquals("40.00",workParams.get("paidRepayCharge")+"");
        assertEquals("4",workParams.get("repayType")+"");
        assertEquals(DateUtils.format(tradeTime,DateUtils.LONG_DATE_FORMAT),workParams.get("tradeTime")+"");
    }


    @Test
    @Sql({"classpath:test/yunxin/customize/reconciliationDelayTaskServicesTest.sql",
            "classpath:test/yunxin/customize/test_insert_reconciliation_source_repository_bqtwo_yuqi.sql"})
    public void test_evaluate_scripts_bqtwo_yuqi() {
        stringRedisTemplate.delete("sr:delay_task_config_uuid_01");
        Result processedResult = new Result();
        Contract contract = contractService.getContract(1L);
        String assetSetUuid = "868b9b0b-af17-470f-89a8-22c7576df8e3";
        String configUuid = "config_uuid_1";
        String recover_delay_task_uuid = "delay_task_config_uuid_01";
        AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
        Date tradeTime = new Date();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("configUuid",configUuid);
        inputMap.put("uniqueId", contract.getUniqueId());
        inputMap.put("contractNo", contract.getContractNo());
        inputMap.put("repaymentPlanUuid", assetSet==null?null:assetSet.getAssetUuid());
        inputMap.put("repurchaseDocUuid", "");
        inputMap.put("assetRecoverType", AssetRecoverType.LOAN_ASSET);
        inputMap.put("tradeTime",tradeTime);

        RepaymentChargesDetail detailAmount = new RepaymentChargesDetail();
        detailAmount.setLoanAssetPrincipal(new BigDecimal("100"));
        detailAmount.setLoanAssetInterest(new BigDecimal("10"));
        detailAmount.setLoanTechFee(new BigDecimal("20"));
        detailAmount.setLoanOtherFee(new BigDecimal("20"));

        inputMap.put("detailAmount",detailAmount);
        Integer currentPeriod = null;
        inputMap.put("repaymentPlanModifyReason",null);
        if(assetSet!=null){
            currentPeriod = assetSet.getCurrentPeriod();
            inputMap.put("assetSetCurrentPeriod",currentPeriod);
        }


        inputMap.put("uniqueId", "51fd9fbc-1e13-473b-92c5-4adfade246b7");
        inputMap.put("contractNo", "云信信2016-78-DK（378）号");
        inputMap.put("repaymentPlanUuid", assetSetUuid);
        inputMap.put("configUuid",configUuid);

        Map<String,Object> outputMap = new HashMap<String,Object>();
        DelayTaskServices reconciliationDelayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(recover_delay_task_uuid);
        reconciliationDelayTaskServices.evaluate(processedResult, delayProcessingTaskCacheHandler, sandboxDataSetHandler, inputMap, outputMap, logger);

        List<DelayProcessingTask> tasks =  delayProcessingTaskService.get_by_repaymentPlanUuid(assetSetUuid);
        assertEquals(1,tasks.size());
        DelayProcessingTask task = tasks.get(0);
        assertNotNull(task);

        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertEquals(assetSetUuid,task.getRepaymentPlanUuid());
        assertEquals(configUuid,task.getConfigUuid());
        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertFalse(StringUtils.isEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(),workParams.get("loanNo")+"");
//        assertEquals("340826199407034015",workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("100.00",workParams.get("repayAmount")+"");
        assertEquals("10.00",workParams.get("repayInterest")+"");
        assertEquals("40.00",workParams.get("repayCost")+"");
        assertEquals("null",workParams.get("repayType")+"");
        assertEquals(DateUtils.format(tradeTime,DateUtils.LONG_DATE_FORMAT),workParams.get("repayDateTime")+"");
    }
}
