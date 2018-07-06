package com.suidifu.dowjones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.ReRunParameter;
import com.suidifu.dowjones.vo.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 22:39 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class CashFingerPrinterControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private TestRestTemplate template;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //                 {
//  "dataStreamUuid": "0cb66f9e-420f-4e9a-bf49-5eb99b776252",
//  "financialContractUuid": "9495f5f2-d306-461a-8b03-5896923dc1b3",
//  "path": "/Users/wujunshen/Downloads/csv/",
//  "taskId": "0"
//}
    @Test
    public void getCashFingerPrinters_TASK_ID_0() throws IOException {
        String requestBody = "{\n" +
                "  \"dataStreamUuid\": \"0cb66f9e-420f-4e9a-bf49-5eb99b776252\",\n" +
                "  \"financialContractUuid\": \"9495f5f2-d306-461a-8b03-5896923dc1b3\",\n" +
                "  \"path\": \"/Users/wujunshen/Downloads/csv/\",\n" +
                "  \"taskId\": \"0\"\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/LedgerBook/CashFingerPrinter",
                OBJECT_MAPPER.readValue(requestBody, FingerPrinterParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.OK.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.OK.getMessage()));
    }

    @Test
    public void getCashFingerPrinters_TASK_ID_1() throws IOException {
        String requestBody = "{\n" +
                "  \"dataStreamUuid\": \"0cb66f9e-420f-4e9a-bf49-5eb99b776252\",\n" +
                "  \"financialContractUuid\": \"9495f5f2-d306-461a-8b03-5896923dc1b3\",\n" +
                "  \"path\": \"/Users/wujunshen/Downloads/csv/\",\n" +
                "  \"taskId\": \"1\"\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/LedgerBook/CashFingerPrinter",
                OBJECT_MAPPER.readValue(requestBody, FingerPrinterParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.OK.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.OK.getMessage()));
    }

    @Test
    public void getCashFingerPrinters_TASK_ID_2() throws IOException {
        String requestBody = "{\n" +
                "  \"dataStreamUuid\": \"0cb66f9e-420f-4e9a-bf49-5eb99b776252\",\n" +
                "  \"financialContractUuid\": \"9495f5f2-d306-461a-8b03-5896923dc1b3\",\n" +
                "  \"path\": \"/Users/wujunshen/Downloads/csv/\",\n" +
                "  \"taskId\": \"2\"\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/LedgerBook/CashFingerPrinter",
                OBJECT_MAPPER.readValue(requestBody, FingerPrinterParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.OK.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.OK.getMessage()));
    }

    @Ignore
    @Test
    public void getCashFingerPrintersRepeatedly() throws IOException {
        String requestBody = "{\n" +
                "  \"financialContractUuid\": \"9495f5f2-d306-461a-8b03-5896923dc1b3\",\n" +
                "  \"date\": \"2018-01-08\"\n" +
                "}";

        BaseResponse actual = template.postForObject("/api/LedgerBook/CashFingerPrintersRepeatedly",
                OBJECT_MAPPER.readValue(requestBody, ReRunParameter.class), BaseResponse.class);

        log.info("\n\njson is:\n\n{}\n\n", OBJECT_MAPPER.writeValueAsString(actual));
        assertThat(actual.getCode(), equalTo(ResponseStatus.OK.getCode()));
        assertThat(actual.getMessage(), equalTo(ResponseStatus.OK.getMessage()));
    }
}