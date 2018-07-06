package com.suidifu.morganstanley.controller.api.pre;

import com.demo2do.core.entity.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.matryoshka.productCategory.ProductCategoryCacheSpec;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_API;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_URL_MODIFY_REPAYMENT_PLAN;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/10 <br>
 * Time:下午7:12 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan.sql")
public class ModifyRepaymentPlanPreControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private List<RepaymentPlanModifyRequestDataModel> requestDataModels;
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
	    privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG" +
			    "/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK" +
			    "+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25" +
			    "+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3" +
			    "+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS" +
			    "/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV" +
			    "+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx" +
			    "/eMcITaLq8l1qzZ907UXY+Mfs=";

	    Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
	    cache.clear();
	    requestDataModels = new ArrayList<>();
	    headers = new BaseUnitTest().initHttpHeaders();
    }

	public void buildHeaderAndRequestBody(Map<String, String> mapParam) {
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
		log.info("signContent:{}", signContent);
		String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestData", mapParam.get("requestData"));
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
        headers = null;
        privateKey = null;
    }

	@Test
	@Sql("classpath:test/api/pre/testSXHDeferredPayment.sql")
	public void testSXHDeferredPayment() throws Exception {
		Object[] uriVariables = {"zhongan", "SXH-DeferredPayment"};
		buildSXHDeferredPaymentRequestDate();

		Result result = postTestRequest(uriVariables);

		log.info("message is {}", result.getMessage());
		assertEquals(ApiMessage.SUCCESS.getCode() + "", result.getCode());
		assertThat(result.getMessage(), containsString(ApiMessage.SUCCESS.getMessage()));
	}

	public Result postTestRequest(Object[] uriVariables) throws java.io.IOException {
		Map<String, String> mapParam = buildParameterMap();
		buildHeaderAndRequestBody(mapParam);
		HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(requestBody, headers);
		String resultResponse = template.exchange(PRE_API + PRE_URL_MODIFY_REPAYMENT_PLAN, HttpMethod.POST, formEntity, String.class, uriVariables).getBody();
		return OBJECT_MAPPER.readValue(resultResponse, Result.class);
	}

	public Map<String, String> buildParameterMap() throws JsonProcessingException {
		Map<String, String> mapParam = new HashMap<>();
		mapParam.put("requestNo", UUID.randomUUID().toString());
		mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
		mapParam.put("contractNo", "G32000");
		mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
		return mapParam;
	}

	public void buildSXHDeferredPaymentRequestDate() {
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
        model.setAssetRecycleDate("2018-11-10");
        model.setAssetPrincipal(new BigDecimal("80000.00"));
		model.setAssetInterest(new BigDecimal("10000.00"));
		model.setOtherCharge(new BigDecimal("3.60"));
		model.setMaintenanceCharge(new BigDecimal("2.5"));
		requestDataModels.add(model);
	}

	public void buildSXHChangeRepaymentDate() {
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetRecycleDate("2018-10-20");
		model.setAssetPrincipal(new BigDecimal("80000.00"));
		model.setAssetInterest(new BigDecimal("10000.00"));
		model.setOtherCharge(new BigDecimal("3.60"));
		model.setMaintenanceCharge(new BigDecimal("2.5"));
		requestDataModels.add(model);
	}

	@Test
	@Sql("classpath:test/api/pre/testSXHChangeRepaymentDate.sql")
	public void testSXHChangeRepaymentDate() throws Exception {
		Object[] uriVariables = {"zhongan", "SXH-ChangeRepaymentDate"};
		buildSXHChangeRepaymentDate();

		Result result = postTestRequest(uriVariables);
		log.info("message is {}", result.getMessage());
		assertEquals(ApiMessage.SUCCESS.getCode() + "", result.getCode());
		assertThat(result.getMessage(), containsString(ApiMessage.SUCCESS.getMessage()));
	}

	public void buildSXHPrepayment() {
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setAssetPrincipal(new BigDecimal("80000.00"));
		model.setAssetInterest(new BigDecimal("10000.00"));
		requestDataModels.add(model);
	}

	@Test
	@Sql("classpath:test/api/pre/testSXHPrepayment.sql")
	public void testSXHPrepayment() throws Exception {
		Object[] uriVariables = {"zhongan", "SXH-Prepayment"};
		buildSXHPrepayment();

		Result result = postTestRequest(uriVariables);
		log.info("message is {}", result.getMessage());
		assertEquals(ApiMessage.SUCCESS.getCode() + "", result.getCode());
		assertThat(result.getMessage(), containsString(ApiMessage.SUCCESS.getMessage()));
	}

	public void buildSXHCancel() {
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setAssetPrincipal(new BigDecimal("80000.00"));
		model.setAssetInterest(new BigDecimal("10000.00"));
		model.setServiceCharge(new BigDecimal("100"));
		model.setMaintenanceCharge(new BigDecimal("50"));
		model.setOtherCharge(new BigDecimal("10"));
		requestDataModels.add(model);
	}

	@Test
	@Sql("classpath:test/api/pre/testSXHCancel.sql")
	public void testSXHCancel() throws Exception {
		Object[] uriVariables = {"zhongan", "SXH-Cancel"};
		buildSXHCancel();

		Result result = postTestRequest(uriVariables);
		log.info("message is {}", result.getMessage());
		assertEquals(ApiMessage.SUCCESS.getCode() + "", result.getCode());
		assertThat(result.getMessage(), containsString(ApiMessage.SUCCESS.getMessage()));
	}

	public void buildPrepayment() {
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setAssetPrincipal(new BigDecimal("80000.00"));
		model.setAssetInterest(new BigDecimal("10000.00"));
		model.setServiceCharge(new BigDecimal("100"));
		model.setMaintenanceCharge(new BigDecimal("150"));
		model.setOtherCharge(new BigDecimal("10"));
		requestDataModels.add(model);
	}

	@Test
	@Sql("classpath:test/api/pre/testPrepayment.sql")
	public void testPrepayment() throws Exception {
		Object[] uriVariables = {"zhongan", "Prepayment"};
		buildPrepayment();

		Result result = postTestRequest(uriVariables);
		log.info("message is {}", result.getMessage());
		assertEquals(ApiMessage.SUCCESS.getCode() + "", result.getCode());
		assertThat(result.getMessage(), containsString(ApiMessage.SUCCESS.getMessage()));
	}

	public void buildRefunds() {
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 1)));
		model.setAssetPrincipal(new BigDecimal("80000.00"));
		model.setAssetInterest(new BigDecimal("10000.00"));
		model.setServiceCharge(new BigDecimal("100"));
		model.setMaintenanceCharge(new BigDecimal("150"));
		requestDataModels.add(model);
	}

	@Test
	@Sql("classpath:test/api/pre/testRefunds.sql")
	public void testRefunds() throws Exception {
		Object[] uriVariables = {"zhongan", "Refunds"};
		buildRefunds();

		Result result = postTestRequest(uriVariables);
		log.info("message is {}", result.getMessage());
		assertEquals(ApiMessage.SUCCESS.getCode() + "", result.getCode());
		assertThat(result.getMessage(), containsString(ApiMessage.SUCCESS.getMessage()));
	}

}