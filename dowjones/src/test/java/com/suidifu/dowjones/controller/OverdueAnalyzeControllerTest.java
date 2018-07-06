package com.suidifu.dowjones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import com.suidifu.dowjones.vo.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/24 <br>
 * @time: 下午3:30 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class OverdueAnalyzeControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private TestRestTemplate template;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getDynamicOverdueRate() throws Exception {
        String requestBody = "{\n" +
                "  \"computeTypeFlag\": 0,\n" +
                "  \"financialContractUuid\": \"d2812bc5-5057-4a91-b3fd-9019506f0499\",\n" +
                "  \"includeRepurchase\": false,\n" +
                "  \"includeUnconfirmed\": false,\n" +
                "  \"overdueStage\": \"0,1,2,3\",\n" +
                "  \"periodDays\": 30\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/OverdueAnalyze/DynamicOverdueRate", OBJECT_MAPPER.readValue(requestBody, InputParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.OK.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.OK.getMessage()));
    }

    @Test
    public void getDynamicOverdueRate_EXCEPTION() throws Exception {
        String requestBody = "{\n" +
                "  \"computeTypeFlag\": 0,\n" +
                "  \"financialContractUuid\": \"string\",\n" +
                "  \"includeRepurchase\": false,\n" +
                "  \"includeUnconfirmed\": false,\n" +
                "  \"overdueStage\": \"string\",\n" +
                "  \"periodDays\": 1\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/OverdueAnalyze/DynamicOverdueRate", OBJECT_MAPPER.readValue(requestBody, InputParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.EXCEPTION_INFO.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.EXCEPTION_INFO.getMessage()));
    }

    @Test
    public void getStaticOverdueRate() throws Exception {
        String requestBody = "{\n" +
                "  \"computeTypeFlag\": 0,\n" +
                "  \"financialContractUuid\": \"d2812bc5-5057-4a91-b3fd-9019506f0499\",\n" +
                "  \"includeRepurchase\": false,\n" +
                "  \"includeUnconfirmed\": false,\n" +
                "  \"month\": 5,\n" +
                "  \"overdueStage\": \"0,1,2,3\",\n" +
                "  \"periodDays\": 30,\n" +
                "  \"year\": 2017\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/OverdueAnalyze/StaticOverdueRateOfMonth", OBJECT_MAPPER.readValue(requestBody, StaticOverdueRateInputParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.OK.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.OK.getMessage()));
    }

    @Test
    public void getStaticOverdueRate_EXCEPTION() throws Exception {
        String requestBody = "{\n" +
                "  \"computeTypeFlag\": 0,\n" +
                "  \"financialContractUuid\": \"string\",\n" +
                "  \"includeRepurchase\": false,\n" +
                "  \"includeUnconfirmed\": false,\n" +
                "  \"month\": 10,\n" +
                "  \"overdueStage\": \"string\",\n" +
                "  \"periodDays\": 30,\n" +
                "  \"year\": 2017\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/OverdueAnalyze/StaticOverdueRateOfMonth", OBJECT_MAPPER.readValue(requestBody, StaticOverdueRateInputParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.EXCEPTION_INFO.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.EXCEPTION_INFO.getMessage()));
    }
}