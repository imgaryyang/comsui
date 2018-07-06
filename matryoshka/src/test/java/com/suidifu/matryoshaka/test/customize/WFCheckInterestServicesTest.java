package com.suidifu.matryoshaka.test.customize;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshaka.test.scripts.WFCheckInterestServices;
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
public class WFCheckInterestServicesTest extends AbstractTransactionalJUnit4SpringContextTests {
    private Log logger = LogFactory.getLog(WFCheckInterestServicesTest.class);

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Test
    public void test(){
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1,2,3,4,5
        };
        int[] principalArray = new int[]{
                5,10,15,20,25
        };
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));

        WFCheckInterestServices wfCheckInterestServices = new WFCheckInterestServices();
        boolean result = wfCheckInterestServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(result);
    }

    @Test
    public void test_noInterest(){
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{

        };
        int[] principalArray = new int[]{

        };
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));

        WFCheckInterestServices wfCheckInterestServices = new WFCheckInterestServices();
        boolean result = wfCheckInterestServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(result);
    }

    @Test
    public void test_failure(){
        Map<String, String> preRequest = new HashMap<String, String>();

        int[] interestArray = new int[]{
                1,2,3,4,5
        };
        int[] principalArray = new int[]{
                5,10,1,20,25
        };
        preRequest.put("interest", JsonUtils.toJsonString(interestArray));
        preRequest.put("principal",JsonUtils.toJsonString(principalArray));

        WFCheckInterestServices wfCheckInterestServices = new WFCheckInterestServices();
        boolean result = wfCheckInterestServices.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(!result);
    }
}