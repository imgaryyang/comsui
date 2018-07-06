package handler.bosc;


import com.suidifu.barclays.exception.PullCashflowException;
import com.suidifu.barclays.handler.CashflowHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author penghk
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml",})
public class BoscCashflowHandlerTest {

    @Autowired
    CashflowHandler boscCashflowHandler;

    @Autowired
    ChannelWorkerConfigService channelWorkerConfigService;

    @Test
    public void TestexecPullCashflow() {

        ChannelWorkerConfig channelWorkerConfig = new ChannelWorkerConfig();
        String config = "{\"userId\":\"174704\",\"userPWD\":\"962888\",\"sessionURL\":\"http://192.168.1.116:7071/CM/APISessionReqServlet?\","
                + "\"generalURL\":\"http://192.168.1.116:7071/CM/APIReqServlet?\",\"encryptUrl\":\"192.168.1.116\",\"encryptPort\":\"8010\"," +
                "\"channelAccountNo\":\"31600700009000356\",\"encoding\":\"GBK\"}";

        channelWorkerConfig.setLocalWorkingConfig(config);
        try {
            List<CashFlow> cashFlows = boscCashflowHandler.execPullCashflow(channelWorkerConfig);
            System.out.println(cashFlows.size());
        } catch (PullCashflowException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
