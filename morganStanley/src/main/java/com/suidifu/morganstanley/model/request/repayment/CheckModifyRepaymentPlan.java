package com.suidifu.morganstanley.model.request.repayment;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.morganstanley.controller.api.repayment.ModifyRepaymentPlanController;
import com.suidifu.morganstanley.model.enumeration.ModifyReason;
import com.suidifu.morganstanley.utils.JSONUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.utils.JsonUtils;
import lombok.Data;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.*;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/16 <br>
 * Time:下午9:06 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Data
public class CheckModifyRepaymentPlan {
    /**
     * 校验失败信息
     */
    private String failedMessage;

    /**
     * 校验本金，利息，还款日期（递增）
     */
    public void hasErrors(ModifyRepaymentPlan model) {
        List<RequestData> requestDataList = model.getRequestDataList();

        for (RequestData requestData : requestDataList) {
            ModifyReason modifyReason = ModifyReason.get(model.getRequestReason());
            if (modifyReason == ModifyReason.REASON_1 || modifyReason == ModifyReason.REASON_2) {
                if (requestData.getAssetType() == null || requestData.getAssetType() != 0) {
                    throw new ApiException(ASSET_TYPE_SHOULD_BEFORE);
                }
            } else {
                if (requestData.getAssetType() != null && requestData.getAssetType() == 0) {
                    throw new ApiException(ASSET_TYPE_SHOULD_NOT_BEFORE);
                }
            }

            isInvalidInterestAmount(requestData);

            isInvalidPrincipalAmount(requestData);

            isInvalidNotNullAmount(requestData);
        }
    }

    private void isInvalidPrincipalAmount(RequestData requestData) {
        BigDecimal assetPrincipal = requestData.getAssetPrincipal();
        if (BigDecimal.ZERO.compareTo(assetPrincipal) > 0) {
            throw new ApiException(INVALID_PRINCIPAL_AMOUNT);
        }
    }

    private void isInvalidInterestAmount(RequestData requestData) {
        BigDecimal assetInterest = requestData.getAssetInterest();
        if (BigDecimal.ZERO.compareTo(assetInterest) > 0) {
            throw new ApiException(INVALID_INTEREST_AMOUNT);
        }
    }

    //校验可空字段 如果没填正常通过,填了则检验金额必须大于或等于0
    private void isInvalidNotNullAmount(RequestData requestData){
        String msg = "金额必须大于或等于0.00";
        BigDecimal serviceCharge = requestData.getServiceCharge();
        BigDecimal maintenanceCharge = requestData.getMaintenanceCharge();
        BigDecimal otherCharge = requestData.getOtherCharge();
        if (serviceCharge != null){
            if (BigDecimal.ZERO.compareTo(serviceCharge) > 0){
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(),msg);
            }
        }
        if (maintenanceCharge != null)
            if (BigDecimal.ZERO.compareTo(maintenanceCharge) > 0){
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(),msg);
            }

        if (otherCharge != null){
            if (BigDecimal.ZERO.compareTo(otherCharge) > 0){
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(),msg);
            }
        }
    }

    public void checkInterestAndPrincipal(ModifyRepaymentPlan model, boolean isWF, SandboxDataSetHandler sandboxDataSetHandler, ProductCategoryCacheHandler productCategoryCacheHandler){
        List<RequestData> requestDataList = JSONUtils.getDetailModel(model.getRequestData(), RequestData.class);

        Map<String, String> preRequest = new HashMap<>();
        List<BigDecimal> interests = new ArrayList<>();
        List<BigDecimal> principals = new ArrayList<>();
        for (RequestData requestData : requestDataList) {
            interests.add(requestData.getAssetInterest());
            principals.add(requestData.getAssetPrincipal());
        }
        preRequest.put("interest", JsonUtils.toJSONString(interests));
        preRequest.put("principal", JsonUtils.toJSONString(principals));
        if(isWF) {
            ProductCategory productCategory = productCategoryCacheHandler.get("importAssetPackage/weifang/10001", true);
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
            boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), LogFactory.getLog(ModifyRepaymentPlanController.class));
            if(!result){
                throw new ApiException(ApiResponseCode.CUSTOMER_FEE_CHECK_FAIL);
            }
        }
    }

    public static void checkInterestAndPrincipalForPayment(ModifyRepaymentPlan model, boolean isWF, SandboxDataSetHandler sandboxDataSetHandler, ProductCategoryCacheHandler productCategoryCacheHandler){
        List<RequestData> requestDataList = JSONUtils.getDetailModel(model.getRequestData(), RequestData.class);

        Map<String, String> preRequest = new HashMap<>();
        List<BigDecimal> interests = new ArrayList<>();
        List<BigDecimal> principals = new ArrayList<>();
        List<String> assetRecycleDates = new ArrayList<>();
        for (RequestData requestData : requestDataList) {
            ModifyReason modifyReason = ModifyReason.get(model.getRequestReason());
            if (modifyReason == ModifyReason.REASON_1 || modifyReason == ModifyReason.REASON_2){
                interests.add(requestData.getAssetInterest());
                principals.add(requestData.getAssetPrincipal());
                assetRecycleDates.add(requestData.getAssetRecycleDate());
            }
        }
        preRequest.put("assetRecycleDate", JsonUtils.toJSONString(assetRecycleDates));
        preRequest.put("interest", JsonUtils.toJSONString(interests));
        preRequest.put("principal", JsonUtils.toJSONString(principals));
        preRequest.put("uniqueId", model.getUniqueId());
        preRequest.put("contractNo", model.getContractNo());
        if(isWF) {
            ProductCategory productCategory = productCategoryCacheHandler.get("importAssetPackage/weifang/10003", true);
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
            boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), LogFactory.getLog(ModifyRepaymentPlanController.class));
            if(!result){
                throw new ApiException(ApiResponseCode.CUSTOMER_FEE_CHECK_FAIL, ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getMessage() + "-提前还款");
            }
        }
    }
}