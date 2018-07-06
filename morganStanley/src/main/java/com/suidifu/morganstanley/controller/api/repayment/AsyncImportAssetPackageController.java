package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.ImportAssetPackageHandler;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackage;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategyMode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_ASYNC_IMPORT_ASSET_PACKAGE;

@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class AsyncImportAssetPackageController extends BaseApiController {
    @Resource
    private ImportAssetPackageHandler importAssetPackageHandler;

    @Resource
    private MorganStanleyNotifyServer morganStanleyNotifyServer;

    @ApiOperation(value = "异步导入资产包", httpMethod = "POST", notes = "异步导入资产包", response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @PostMapping(value = URL_ASYNC_IMPORT_ASSET_PACKAGE)
    @ResponseBody
    public BaseResponse importAssetPackageAsync(HttpServletRequest request, HttpServletResponse response,
                                                @ApiParam(value = "异步导入资产包", required = true)
                                                @Validated
                                                @ModelAttribute ImportAssetPackage model, BindingResult bindingResult) throws Exception {
        log.info(GloableLogSpec.AuditLogHeaderSpec());
        log.info("{}", GloableLogSpec.EARTH_ASSETPACKAGE_FUNCTION_POINT.IMPORT_ASSET_PACKAGE);
        log.info("[remoteKeyWord={} ,", model.getRequestNo());
        log.info("ip={}]", model.getRequestNo(), IpUtil.getIpAddress(request));
        log.info("{}", GloableLogSpec.RawData(model.getImportAssetPackageContent()));

        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }
        // 校验model参数
        FinancialContract fc = importAssetPackageHandler.checkImportAssetPackageRequestModel(model, request);
        boolean notOfflineRemittance = fc.getRemittanceStrategyMode() != null && fc.getRemittanceStrategyMode() != RemittanceStrategyMode.OFFLINE_REMITTANCE;
        boolean notNullRemittance = fc.getRemittanceStrategyMode() != RemittanceStrategyMode.NULL_REMITTANCE;

        // 请求数据校验
        importAssetPackageHandler.dataVerification(model, notOfflineRemittance, notNullRemittance);

        importAssetPackageHandler.executeAsyncImport(morganStanleyNotifyServer, request, model);
        return wrapHttpServletResponse(response, ApiMessage.SUCCESS.getCode(),"请求成功，进入执行队列，等待处理");
    }
}