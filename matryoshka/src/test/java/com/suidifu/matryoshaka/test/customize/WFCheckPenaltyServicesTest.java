package com.suidifu.matryoshaka.test.customize;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshaka.test.scripts.WFCheckPenaltyServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwr on 17-11-6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"})
@Transactional()
@Rollback(true)
public class WFCheckPenaltyServicesTest extends AbstractTransactionalJUnit4SpringContextTests {
    private Log logger = LogFactory.getLog(WFCheckPenaltyServicesTest.class);
    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Test
    public void evaluate() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1,2,3,4,5
        };
        int[] principalArray = new int[]{
                15,30,45,60,75
        };
        preRequest.put("penalty", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));
        preRequest.put("r","0.1");
        preRequest.put("overdueDay","100");

        WFCheckPenaltyServices wfCheckPenaltyServices = new WFCheckPenaltyServices();
        boolean result = wfCheckPenaltyServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(result);
    }

    @Test
    public void evaluate_noR() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1,2,3,4,5
        };
        int[] principalArray = new int[]{
                15,30,45,60,75
        };
        preRequest.put("penalty", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));
        preRequest.put("overdueDay","100");

        WFCheckPenaltyServices wfCheckPenaltyServices = new WFCheckPenaltyServices();
        boolean result = wfCheckPenaltyServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(!result);
    }

    @Test
    public void evaluate_noOverdueDay() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1,2,3,4,5
        };
        int[] principalArray = new int[]{
                15,30,45,60,75
        };
        preRequest.put("penalty", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));
        preRequest.put("r","0.1");

        WFCheckPenaltyServices wfCheckPenaltyServices = new WFCheckPenaltyServices();
        boolean result = wfCheckPenaltyServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(!result);
    }

    @Test
    public void evaluate_failure() throws Exception {
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1,2,3,4,5
        };
        int[] principalArray = new int[]{
                15,30,4,60,75
        };
        preRequest.put("penalty", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));
        preRequest.put("r","0.1");
        preRequest.put("overdueDay","100");

        WFCheckPenaltyServices wfCheckPenaltyServices = new WFCheckPenaltyServices();
        boolean result = wfCheckPenaltyServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(!result);
    }
}