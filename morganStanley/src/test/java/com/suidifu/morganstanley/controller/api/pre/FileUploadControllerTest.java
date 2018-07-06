package com.suidifu.morganstanley.controller.api.pre;

import com.suidifu.matryoshka.productCategory.ProductCategoryCacheSpec;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.sun.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_API;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_URL_UPLOAD_FILE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/22 <br>
 * Time:上午1:10 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class FileUploadControllerTest {
    @Autowired
    private ResourceLoader loader;
    @Autowired
    private CacheManager cacheManager;

    private HttpHeaders headers;
    private MultiValueMap<String, Object> requestBody;
    @Autowired
    private TestRestTemplate template;
    private String privateKey;

    @Before
    public void setUp() throws Exception {
        privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        cache.clear();

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("tradeTime", DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        log.info("signContent:{}", signContent);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("tradeTime", mapParam.get("tradeTime"));
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
        headers = null;
        privateKey = null;
    }

    @Test
    @Sql("classpath:test/api/pre/testUpload_txt.sql")
    public void testUpload_TXT_FILE() throws Exception {
        Resource resource = loader.getResource("classpath:test/api/pre/testUploadForText.txt");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/pre/testUpload_txt.sql")
    public void testUpload_NO_TRADE_TIME() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("tradeTime", "12345");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        log.info("signContent:{}", signContent);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.remove("sign");
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("tradeTime", mapParam.get("tradeTime"));

        Resource resource = loader.getResource("classpath:test/api/pre/testUploadForText.txt");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo("请输入正确的交易时间"));
    }

    /**
     * 没有对应的productCategory
     */
    @Test
    @Sql("classpath:test/api/pre/testUpload_no_productcategory.sql")
    public void testUpload_NO_PRODUCTCATEGORY() {
        Resource resource = loader.getResource("classpath:test/api/pre/testUploadForText.txt");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.API_NOT_FOUND.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.API_NOT_FOUND.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/pre/testUpload_unsupported_file_type.sql")
    public void testUpload_UNSUPPORTED_FILE_TYPE() {
        Resource resource = loader.getResource("classpath:test/api/pre/testUploadForText.txt");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.UNSUPPORTED_FILE_TYPE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.UNSUPPORTED_FILE_TYPE.getMessage()));
    }

    /**
     * 文件不是.txt或者.csv结尾的文件  报错
     */
    @Test
    @Sql("classpath:test/api/pre/testUpload_file_unsupported.sql")
    public void testUpload_FILE_UNSUPPORTED() throws IOException {
        Resource resource = loader.getResource("classpath:test/api/pre/testUpload_file_unsupported.sql");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FILE_UNSUPPORTED.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FILE_UNSUPPORTED.getMessage()));
    }

    /**
     * csv文件   文件内容格式错误
     */
    @Test
    @Sql("classpath:test/api/pre/testUpload_csv.sql")
    public void testUpload_FILE_FORMAT_ERROR() throws Exception {
        Resource resource = loader.getResource("classpath:test/api/pre/testUploadForFileException.csv");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FILE_FORMAT_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FILE_FORMAT_ERROR.getMessage()));
    }

    /**
     * csv文件   文件内容格式正确
     */
    @Test
    @Sql("classpath:test/api/pre/testUpload_csv.sql")
    public void testUpload_CSV_FILE() throws Exception {
        Resource resource = loader.getResource("classpath:test/api/pre/testUploadForCsv.csv");
        requestBody.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));

    }

    //@Ignore
    @Test
    @Sql("classpath:test/api/pre/testUpload_csv.sql")
    public void testUpload_BATCH_FILE() throws Exception {
        String[] resourcesPaths = {"classpath:test/api/pre/testUploadForCsv.csv",
                "classpath:test/api/pre/testUploadForText.txt"};

        for (String location : resourcesPaths) {
            requestBody.add("file", loader.getResource(location));
        }

        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);

        Object[] uriVariables = {"HA0100", "10001"};

        BaseResponse resultResponse = template.exchange(PRE_API + PRE_URL_UPLOAD_FILE, HttpMethod.POST, formEntity, BaseResponse.class, uriVariables).getBody();

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));

    }
}