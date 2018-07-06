package morganStanley.controller;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.api.notify.InternalNotifyController;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.yunxin.entity.model.ResponeMapSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class InternalNotifyControllerTest {

    @Autowired
    private InternalNotifyController internalNotifyController;
    private MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
    private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @Test
    @Ignore
    public void test_notify_pay_result() {

        String deductApplicationUuid = "";
	    Map<String, String> mockResult = new HashMap<>();
	    mockResult.put(ResponeMapSpec.IS_SUCCESS, Boolean.TRUE + "");
        mockResult.put(ResponeMapSpec.FAIL_MESSAGE, "");
        mockResult.put(ResponeMapSpec.DEDUCT_APPLICATION_UUID, deductApplicationUuid);
        mockResult.put(ResponeMapSpec.DEDUCTREQUESTMODEL, Boolean.TRUE + "");


        //String resultJosn = internalNotifyController.notifyDeduct(mockHttpServletResponse, mockHttpServletRequest);
        //Result result = JsonUtils.parse(resultJosn, Result.class);
        //assertTrue(result.isValid());

    }

    private DeductRequestModel buildRequestModel(BigDecimal deductAmount, String deductId, String financialContractUuid, String financialContractCode, String
            uniqueId,
                                                 String contractNo) {

        Map<String, String> delayPostRequestParams = new HashMap<String, String>();

        String urlForEarthCallBack = "";
        String urlForJpmorganCallBack = "";

        delayPostRequestParams.put("fn", UUID.randomUUID().toString());
        delayPostRequestParams.put("requestNo", UUID.randomUUID().toString());
        delayPostRequestParams.put("deductId", deductId);
        delayPostRequestParams.put("financialProductCode", financialContractUuid);
        delayPostRequestParams.put("financialContractUuid", financialContractCode);
        delayPostRequestParams.put("uniqueId", uniqueId);
        delayPostRequestParams.put("contractNo", contractNo);
        delayPostRequestParams.put("apiCalledTime", StringUtils.EMPTY);
        delayPostRequestParams.put("transType", StringUtils.EMPTY);
        delayPostRequestParams.put("deductAmount", deductAmount + "");
        delayPostRequestParams.put("accountHolderName", StringUtils.EMPTY);
        delayPostRequestParams.put("standardBankCode", StringUtils.EMPTY);
        delayPostRequestParams.put("notifyUrl", "");
        delayPostRequestParams.put("batchDeductApplicationUuid", null);
        delayPostRequestParams.put("batchDeductId", null);
        delayPostRequestParams.put("protocolNo", StringUtils.EMPTY);

        DeductRequestModel deductRequestModel = new DeductRequestModel(delayPostRequestParams, urlForEarthCallBack, urlForJpmorganCallBack);
        return deductRequestModel;
    }
}
