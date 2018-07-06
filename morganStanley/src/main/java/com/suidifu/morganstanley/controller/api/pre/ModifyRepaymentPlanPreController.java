package com.suidifu.morganstanley.controller.api.pre;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.suidifu.matryoshka.snapshot.SandboxDataSetSpec.CONFING_UUID;
import static com.suidifu.matryoshka.snapshot.SandboxDataSetSpec.CONTRACT_UUID;
import static com.suidifu.matryoshka.snapshot.SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID;
import static com.suidifu.matryoshka.snapshot.SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST;
import static com.suidifu.matryoshka.snapshot.SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.HTTP_ROOT;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.HTTP_URL;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_API;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_URL_MODIFY_REPAYMENT_PLAN;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.CONTRACT_NO;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.CONTRACT_NO_VALUE;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.DATA_TYPE_INTEGER;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.DATA_TYPE_STRING;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.MODIFY_REPAYMENT_PLAN_TYPE;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.MODIFY_REPAYMENT_PLAN_TYPE_VALUE;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.PARAM_TYPE_QUERY;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.REQUEST_DATA;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.REQUEST_DATA_VALUE;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.REQUEST_NO;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.REQUEST_NO_VALUE;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.UNIQUE_ID;
import static com.zufangbao.gluon.spec.morganstanley.SwaggerSpec.UNIQUE_ID_VALUE;

/**
 * 变更还款计划前置接口
 *
 * @author louguanyang
 */
@RestController
@RequestMapping(PRE_API)
@Api(tags = {"五维金融前置接口"}, description = " ")
@Log4j2
public class ModifyRepaymentPlanPreController extends BaseApiController {
	private static final Log LOGGER = LogFactory.getLog(FileUploadController.class);

	@Resource
	@Qualifier("productCategoryCacheHandler")
	private ProductCategoryCacheHandler productCategoryCacheHandler;

	@Resource
	@Qualifier("delayTaskConfigCacheHandler")
	private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

	@Resource
	@Qualifier("delayProcessingTaskCacheHandler")
	private DelayProcessingTaskCacheHandler delayProcessingTaskHandler;

	@Resource
	private SandboxDataSetHandler sandboxDataSetHandler;

	@PostMapping(value = PRE_URL_MODIFY_REPAYMENT_PLAN)
    @ResponseBody
	@ApiOperation(value = "变更还款计划前置接口", notes = "变更还款计划前置接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = REQUEST_NO, value = REQUEST_NO_VALUE, required = true, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
			@ApiImplicitParam(name = UNIQUE_ID, value = UNIQUE_ID_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
			@ApiImplicitParam(name = CONTRACT_NO, value = CONTRACT_NO_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
			@ApiImplicitParam(name = REQUEST_DATA, value = REQUEST_DATA_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
			@ApiImplicitParam(name = MODIFY_REPAYMENT_PLAN_TYPE, value = MODIFY_REPAYMENT_PLAN_TYPE_VALUE, paramType = PARAM_TYPE_QUERY, dataType =
					DATA_TYPE_INTEGER)
	})
	public String modifyRepaymentPlan(HttpServletRequest request, HttpServletResponse response,
	                                  @ApiParam(value = "商户代码", required = true, defaultValue = "zhongan")
	                                  @PathVariable String channelCode,
	                                  @ApiParam(value = "服务代码", required = true, defaultValue = "SXH-DeferredPayment",
			                                  allowableValues = "SXH-DeferredPayment,SXH-ChangeRepaymentDate,SXH-Prepayment,SXH-Cancel,Prepayment,Refunds")
	                                      @PathVariable String serviceCode) {
		try {
			ProductCategory productCategory = getProductCategory(request);
			CustomizeServices services = getHotCompileCodeObject(productCategory);
			Map<String, String> allParameters = getAllParameters(request);
			SandboxDataSet sandboxDataSet = getSandboxDataSet(allParameters);
			HashMap<String, String> postRequest = customizeServicesEvaluate(allParameters, services, sandboxDataSet);
			Result postResult = sendToCoreApi(request, productCategory, postRequest);
			doDelayTaskServices(productCategory, sandboxDataSet, postResult);
			return signSucResult(response, postResult.getData());
		} catch (Exception e) {
			log.error("#modifyRepaymentPlan occur error ! {}", ExceptionUtils.getStackTrace(e));
			return signErrorResult(response, e);
		}
	}

	/**
	 * 获取接口产品类型配置表
	 *
	 * @param request Http请求
	 * @return 接口产品类型配置表
	 */
	private ProductCategory getProductCategory(HttpServletRequest request) {
		ProductCategory productCategory = productCategoryCacheHandler.get(getRequestURL(request, HTTP_URL), true);
		if (null == productCategory) {
			throw new ApiException(ApiMessage.API_NOT_FOUND);
		}
		return productCategory;
	}

	/**
	 * 获取热编译类
	 *
	 * @param productCategory 接口产品类型配置表
	 * @return 热编译类
	 */
	private CustomizeServices getHotCompileCodeObject(ProductCategory productCategory) {
		Object object = null;
		try {
			object = productCategoryCacheHandler.getScript(productCategory);
		} catch (Exception e) {
			log.error("#getScript occur error\n{}", ExceptionUtils.getStackTrace(e));
		}
		if (object == null) {
			log.warn("CustomizeServices not found");
			throw new ApiException(ApiMessage.API_NOT_FOUND);
		}
		return (CustomizeServices) object;
	}

	/**
	 * @param request         Http请求
	 * @param productCategory 接口产品类型配置表
	 * @param coreApiParams   核心接口请求参数
	 * @return 核心接口响应结果
	 */
	private Result sendToCoreApi(HttpServletRequest request, ProductCategory productCategory, HashMap<String, String> coreApiParams) {
		String httpRoot = getRequestURL(request, HTTP_ROOT);
		String location = httpRoot + productCategory.getPostProcessInterfaceUrl();
		Result postResult = post(coreApiParams, location);
		Integer resultCode = Integer.valueOf(postResult.getCode());
		if (resultCode != ApiResponseCode.SUCCESS) {
			String errorMsg = postResult.getMessage();
			log.warn("modifyRepaymentPlan fail, {}", errorMsg);
			throw new ApiException(resultCode, errorMsg);
		}
		return postResult;
	}

	/**
	 * 执行热编译代码方法，返回核心Api请求参数
	 *
	 * @param allParameters     Http请求参数
	 * @param customizeServices 热编译代码类
	 * @param sandboxDataSet
	 * @return 调用核心Api请求参数
	 */
	private HashMap<String, String> customizeServicesEvaluate(Map<String, String> allParameters, CustomizeServices customizeServices, SandboxDataSet
			sandboxDataSet) {
		HashMap<String, String> postRequest = new HashMap<>();
		boolean evaluate = customizeServices.evaluate(sandboxDataSetHandler, allParameters, postRequest, LOGGER);
		if (!evaluate) {
			String errorMsg = postRequest.getOrDefault("errorMsg", "");
			log.warn("modifyRepaymentPlan, 热编译代码执行失败, {}", errorMsg);
			throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), errorMsg);
		}
		FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();
		String financialProductCode = financialContractSnapshot.getContractNo();
		postRequest.put(ApiConstant.FINANCIAL_PRODUCT_CODE, financialProductCode);
		return postRequest;
	}

	/**
	 * 根据Http请求参数获取沙盒数据集
	 *
	 * @param allParameters Http请求参数
	 * @return 沙盒数据
	 */
	private SandboxDataSet getSandboxDataSet(Map<String, String> allParameters) {
		String uniqueId = allParameters.getOrDefault(UNIQUE_ID, "");
		String contractNo = allParameters.getOrDefault(CONTRACT_NO, "");

		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(uniqueId, contractNo);
		if (sandboxDataSet == null) {
			log.warn("modifyRepaymentPlan, sandboxDataSet is null!");
			throw new ApiException(ApiMessage.INVALID_PARAMS);
		}
		if (null == sandboxDataSet.getContractSnapshot()) {
			log.warn("modifyRepaymentPlan, getContractSnapshot is null!");
			throw new ApiException(ApiMessage.CONTRACT_NOT_EXIST);
		}
		return sandboxDataSet;
	}

	/**
	 * 执行后置任务
	 *
	 * @param productCategory 接口产品类型配置表
	 * @param sandboxDataSet  沙盒数据集
	 * @param postResult      Http响应结果
	 */
	private void doDelayTaskServices(ProductCategory productCategory, SandboxDataSet sandboxDataSet, Result postResult) {
		String delayTaskConfigUuid = productCategory.getDelayTaskConfigUuid();
		DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
		if (null != delayTaskServices) {
			HashMap<String, Object> inputMap = buildDelayTaskServicesInputMap(sandboxDataSet, postResult.getData());
			inputMap.put(CONFING_UUID, delayTaskConfigUuid);
			delayTaskServices.evaluate(postResult, delayProcessingTaskHandler, sandboxDataSetHandler, inputMap, new HashMap<>(), LOGGER);
		} else {
			log.warn("modifyRepaymentPlan, delayTaskServices is null");
		}
	}

	/**
	 * 组装后置任务请求Map
	 *
	 * @param sandboxDataSet 沙盒数据
	 * @param data           核心接口返回参数
	 * @return 后置任务请求Map
	 */
	private HashMap<String, Object> buildDelayTaskServicesInputMap(SandboxDataSet sandboxDataSet, Map<String, Object> data) {
		HashMap<String, Object> inputMap = new HashMap<>(4);
		List<String> newPlanNos = (List<String>) data.getOrDefault(REPAYMENT_PLAN_NO_LIST, Collections.emptyList());
		List<String> oldPlanNos = sandboxDataSet.getPaymentPlanSnapshotList().stream().map(PaymentPlanSnapshot::getSingleLoanContractNo).collect(Collectors.toList
				());
		String financialContractUuid = sandboxDataSet.getFinancialContractUuid();
		String contractUuid = sandboxDataSet.getContractUuid();
		inputMap.put(FINANCIAL_CONTRACT_UUID, financialContractUuid);
		inputMap.put(CONTRACT_UUID, contractUuid);
		inputMap.put(REPAYMENT_PLAN_NO_LIST, newPlanNos);
		inputMap.put(ORIGINAL_REPAYMENT_PLAN_NO_LIST, oldPlanNos);
		return inputMap;
	}
}