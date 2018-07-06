/*
package com.suidifu.matryoshaka.test.service.delayTask.BQ;

import com.demo2do.core.entity.Result;
import com.suidifu.hathaway.util.JsonUtils;
import com.suidifu.matryoshaka.test.scripts.BQModifyRepaymentPlanAmountV2;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.suidifu.matryoshka.snapshot.SandboxDataSetSpec;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

*/
/**
 * Created by hwr on 17-11-28.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml",
})
//@Transactional
//@Rollback(true)
@Sql("classpath:test/yunxin/delayTask/BQ/BQModifyrepaymentPlanAmountV2.sql")
//@Component
public class BQModifyRepaymentPlanAmountV2Test {

    @Autowired
//    @Qualifier("delayProcessingTaskCacheHandler")
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    @Qualifier("delayProcessingTaskDBService")
    private DelayProcessingTaskService delayProcessingTaskService;

    @Autowired
    private ContractService contractService;

    @Test
    public void test (){
//        DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler = new DelayProcessingTaskCacheHandlerImpl();
        Result result = new Result();
        result.setCode("0");
        HashMap<String, Object> inputMap = getEntity();
        Map<String, Object> resultMap = new HashMap<>();
        DelayTaskServices delayTaskServices = new BQModifyRepaymentPlanAmountV2();
        //DelayProcessingTask task = delayProcessingTaskCacheHandler.getByUuid("18be8bf0-d4d2-11e7-a83d-502b73c136df");
        delayTaskServices.evaluate(result, delayProcessingTaskCacheHandler, sandboxDataSetHandler, inputMap, resultMap);
        Contract contract = contractService.getContract("a0afc961-5fa8-11e6-b2c2-00163e002839");
        List<DelayProcessingTask> delayTasks = delayProcessingTaskService.get_by_repaymentPlanUuid("18b302ac-281d-4b39-b906-cded34b4b58");
        assertEquals(1,delayTasks.size());
        DelayProcessingTask task = delayTasks.get(0);
        assertNotNull(task);
        assertEquals("financial_contract_uuid_1",task.getFinancialContractUuid());
        assertEquals("contract_uuid_1",task.getContractUuid());
        assertEquals("customer_uuid_1",task.getCustomerUuid());
        assertEquals("18b302ac-281d-4b39-b906-cded34b42b58",task.getRepaymentPlanUuid());

        Map<String,Object> workParams = JsonUtils.parse(task.getWorkParams());
        assertTrue(StringUtils.isNotEmpty(workParams.get("tradeNo")+""));
        assertEquals(contract.getUniqueId(), workParams.get("loanNo"));
        assertEquals("MzQwODI2MTk5NDA3MDM0MDE1",workParams.get("thirdUserNo")+"");
        assertEquals("1",workParams.get("installmentNo")+"");
        assertEquals("6.00",workParams.get("repayPenalty")+"");
        assertEquals("1.00",workParams.get("repayInterest"));
        assertEquals("5.00",workParams.get("repayFine")+"");
        assertEquals("29.00",workParams.get("repayCost")+"");
        assertEquals("26.00",workParams.get("repayTotalAmount")+"");
        //assertEquals(DateUtils.format(tradeTime,com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT),workParams.get("repayDateTime")+"");

    }

    public HashMap<String, Object> getEntity(){
        HashMap<String, Object> inputMap = new HashMap<>();
        //402fd71a-393b-11e7-bf99-00163e002839
        inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "b1bbea29-d42e-11e7-a83d-502b73c136df");
        inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, "a0afc961-5fa8-11e6-b2c2-00163e002839");
        List<String> lists = new ArrayList<>();
        lists.add("ZC2730FAE4092E0A6E1");
        inputMap.put(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST, Arrays.asList("ZC2730FAE4092E0A6E"));
        inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, lists);
        inputMap.put(SandboxDataSetSpec.CONFING_UUID, "374da0c4-3935-11e7-952e-ba77244e1da4");
        return inputMap;
    }
}
*/
