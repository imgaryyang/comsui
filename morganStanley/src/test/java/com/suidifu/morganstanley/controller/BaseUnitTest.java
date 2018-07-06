package com.suidifu.morganstanley.controller;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_REPAYMENT_PLAN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/27 <br>
 * Time:上午10:52 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class BaseUnitTest {
    private HttpHeaders headers;
    private MultiValueMap<String, String> requestBody;
    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        headers = new HttpHeaders();
        requestBody = new LinkedMultiValueMap<>();
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
        headers = null;
    }

    @Test
    @Sql("classpath:test/api/common/testSystemError.sql")
    public void testSYSTEM_ERROR() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SYSTEM_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SYSTEM_ERROR.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/common/testApiNotFound.sql")
    public void testAPI_NOT_FOUND() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.API_NOT_FOUND.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.API_NOT_FOUND.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/common/testApiUnavailable.sql")
    public void testAPI_UNAVAILABLE() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.API_UNAVAILABLE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.API_UNAVAILABLE.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/common/testSignVerifyFail.sql")
    public void testSIGN_VERIFY_FAIL() throws Exception {
        headers = initHttpHeaders();
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SIGN_VERIFY_FAIL.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SIGN_VERIFY_FAIL.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/common/testSignMerConfigError.sql")
    public void testSIGN_MER_CONFIG_ERROR() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SIGN_MER_CONFIG_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SIGN_MER_CONFIG_ERROR.getMessage()));
    }

    @Ignore
    @Test
    public void testSYSTEM_BUSY() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SYSTEM_BUSY.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SYSTEM_BUSY.getMessage()));
    }

    @Ignore
    @Test
    public void testSYSTEM_TIME_OUT() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SYSTEM_TIME_OUT.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SYSTEM_TIME_OUT.getMessage()));
    }

    public HttpHeaders initHttpHeaders() {
        headers = new HttpHeaders();
        headers.add("merId", "t_test_zfb");
        headers.add("secret", "123456");

        return headers;
    }

}