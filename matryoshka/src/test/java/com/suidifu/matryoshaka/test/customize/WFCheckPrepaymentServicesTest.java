package com.suidifu.matryoshaka.test.customize;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshaka.test.scripts.WFCheckPrepaymentServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hwr on 17-11-6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"})
@Transactional()
@Rollback(true)

@Sql("classpath:test/yunxin/script/WFCheckPrepaymentServicesTest.sql")
public class WFCheckPrepaymentServicesTest extends AbstractTransactionalJUnit4SpringContextTests {
    private Log logger = LogFactory.getLog(WFCheckPrepaymentServicesTest.class);

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Test
    public void evaluate() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        List<BigDecimal> interestArray = new ArrayList<>();
        interestArray.add(new BigDecimal(1.54));
        int[] principalArray = new int[]{
                5
        };
        List<String> recycledates = new ArrayList<>();
        recycledates.add("2017-11-07");
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal", JsonUtils.toJsonString(principalArray));
        preRequest.put("uniqueId", "3b346052-64f5-4517-882b-90f70f4a0fc9");
        preRequest.put("assetRecycleDate", JsonUtils.toJsonString(recycledates));

        WFCheckPrepaymentServices wfCheckPrepaymentServices = new WFCheckPrepaymentServices();
        boolean result = wfCheckPrepaymentServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(result);
    }

    @Test
    public void evaluate_noUniqueIdAndContractNo() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        List<BigDecimal> interestArray = new ArrayList<>();
        interestArray.add(new BigDecimal(1.54));
        int[] principalArray = new int[]{
                5
        };
        List<String> recycledates = new ArrayList<>();
        recycledates.add("2017-11-07");
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal", JsonUtils.toJsonString(principalArray));
        preRequest.put("assetRecycleDate", JsonUtils.toJsonString(recycledates));

        WFCheckPrepaymentServices wfCheckPrepaymentServices = new WFCheckPrepaymentServices();
        boolean result = wfCheckPrepaymentServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(!result);
    }

    @Test
    public void evaluate_noAssetRecycleDate() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        List<BigDecimal> interestArray = new ArrayList<>();
        interestArray.add(new BigDecimal(1.54));
        int[] principalArray = new int[]{
                5
        };
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal", JsonUtils.toJsonString(principalArray));
        preRequest.put("uniqueId", "3b346052-64f5-4517-882b-90f70f4a0fc9");

        WFCheckPrepaymentServices wfCheckPrepaymentServices = new WFCheckPrepaymentServices();
        boolean result = wfCheckPrepaymentServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(!result);
    }

    @Test
    public void evaluate_failure() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1, 2, 300, 4, 5
        };
        int[] principalArray = new int[]{
                5, 10, 15, 20, 25
        };
        List<String> recycledates = new ArrayList<>();
        recycledates.add("2017-11-07");
        recycledates.add("2017-11-07");
        recycledates.add("2017-11-07");
        recycledates.add("2017-11-07");
        recycledates.add("2017-11-07");
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal", JsonUtils.toJsonString(principalArray));
        preRequest.put("uniqueId", "3b346052-64f5-4517-882b-90f70f4a0fc9");
        preRequest.put("assetRecycleDate", JsonUtils.toJsonString(recycledates));

        WFCheckPrepaymentServices wfCheckPrepaymentServices = new WFCheckPrepaymentServices();
        boolean result = wfCheckPrepaymentServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);

    }
}