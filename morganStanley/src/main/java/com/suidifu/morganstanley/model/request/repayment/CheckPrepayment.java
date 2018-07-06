package com.suidifu.morganstanley.model.request.repayment;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.suidifu.morganstanley.controller.api.repayment.PrepaymentController;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校验提前全额还款接口请求参数
 * @author louguanyang at 2017/10/16 14:35
 */
public class CheckPrepayment {

    /**
     * 校验提前还款请求参数
     *
     * @param prepayment 提前还款请求参数
     */
    public static void check(@NotNull Prepayment prepayment) {
        Integer hasDeducted = prepayment.getHasDeducted();
        if (hasDeducted != null) {
            if (hasDeducted < 0 || hasDeducted > 1) {
                throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "hasDeducted:错误,取值范围:0-[本端未扣款],1-[对端已扣款]");
            }
        }
        if (AmountUtils.notEquals(prepayment.getAssetInitialValueBD(), prepayment.getAmount())) {
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "提前还款明细金额之和与总金额[assetInitialValue]不匹配");
        }
    }

    public static void checkInterestAndPrincipal(@NotNull Prepayment prepayment, boolean isWF, SandboxDataSetHandler sandboxDataSetHandler, ProductCategoryCacheHandler productCategoryCacheHandler){
        Map<String, String> preRequest = new HashMap<>();
        List<BigDecimal> interests = new ArrayList<>();
        List<BigDecimal> principals = new ArrayList<>();
        List<String> assetRecycleDates = new ArrayList<>();
        interests.add(new BigDecimal(prepayment.getAssetInterest()));
        principals.add(new BigDecimal(prepayment.getAssetInterest()));
        assetRecycleDates.add(prepayment.getAssetRecycleDate());
        preRequest.put("interest", JsonUtils.toJSONString(interests));
        preRequest.put("principal", JsonUtils.toJSONString(principals));
        preRequest.put("assetRecycleDate", JsonUtils.toJSONString(assetRecycleDates));
        preRequest.put("uniqueId", prepayment.getUniqueId());
        preRequest.put("contractNo", prepayment.getContractNo());

        if(isWF) {
            ProductCategory productCategory = productCategoryCacheHandler.get("importAssetPackage/weifang/10003", true);
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
            boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), LogFactory.getLog(PrepaymentController.class));
            if(!result){
                throw new ApiException(ApiResponseCode.CUSTOMER_FEE_CHECK_FAIL);
            }
        }
    }
}

