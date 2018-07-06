package com.suidifu.morganstanley.controller.api;

import com.demo2do.core.entity.Result;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.*;
import static org.junit.Assert.assertThat;

/**
 * @author louguanyang at 2017/9/27 15:48
 */
@Slf4j
public abstract class BaseControllerTest {
    /**
     * 测试私钥
     */
    protected static final String privateKey =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG" +
                    "/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK" +
                    "+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25" +
                    "+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3" +
                    "+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS" +
                    "/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV" +
                    "+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx" +
                    "/eMcITaLq8l1qzZ907UXY+Mfs=";
    /**
     * 测试商户号
     */
    private static final String TEST_MER_ID = "t_test_zfb";
    /**
     * 测试商户密码
     */
    private static final String TEST_SECRET = "123456";
    @Autowired
    protected TestRestTemplate template;
    protected HttpHeaders headers;
    protected MultiValueMap<String, String> requestBody;
    protected Map<String, String> requestParams;
    protected String post_test_url;

    /**
     * 创建请求头
     */
    @PostConstruct
    protected void initHeadersRequestMap() {
        headers = new HttpHeaders();
        headers.set(PARAMS_MER_ID, TEST_MER_ID);
        headers.set(PARAMS_SECRET, TEST_SECRET);
        requestParams = new HashMap<>();
    }

    /**
     * @param expectedCode    期望返回代码
     * @param expectedMatcher 期望返回内容
     * @return Http响应结果
     */
    protected Result sendPostTest(String expectedCode, Matcher<? super String> expectedMatcher) {
        Assert.assertTrue(StringUtils.isNotEmpty(post_test_url));
        buildRequestParamsMap();
        signRequestParams();
        requestBody = getRequestBody(requestParams);
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        String responseStr = template.postForObject(post_test_url, formEntity, String.class);
        log.info("response string:{}\n\n\n", responseStr);
        Result result = JsonUtils.parse(responseStr, Result.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedCode, result.getCode());
        assertThat(result.getMessage(), expectedMatcher);
        return result;
    }

    /**
     * 对请求参数添加签名
     */
    private void signRequestParams() {
        String signContent = ApiSignUtils.getSignCheckContent(requestParams);
        log.info("signContent:{}", signContent);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        log.info("sign:{}", sign);
        Assert.assertNotNull(headers);
        headers.set(PARAMS_SIGN, sign);
    }

    /**
     * 转换请求参数Map
     *
     * @param requestParams 请求参数Map
     * @return Test Rest Template请求参数
     */
    private MultiValueMap<String, String> getRequestBody(Map<String, String> requestParams) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestParams.keySet();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            requestBody.add(entry.getKey(), entry.getValue());
        }
        return requestBody;
    }

    public abstract void buildRequestParamsMap();
}
