package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.morganstanley.configuration.bean.zhonghang.ZhongHangProperties;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.ImportAssetPackageHandler;
import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackage;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.repayment.ImportAssetPackageResponseModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategyMode;
import com.zufangbao.wellsfargo.api.util.ApiConstant;
import com.zufangbao.wellsfargo.yunxin.handler.SupplierInfoHandler;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_IMPORT_ASSET_PACKAGE;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/25 <br>
 * Time:下午5:48 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class ImportAssetPackageController extends BaseApiController {
    @Resource
    private ImportAssetPackageHandler importAssetPackageHandler;

    @Resource
    private SignUpHandler signUpHandler;

    @Resource
    private MorganStanleyNotifyServer morganStanleyNotifyServer;

    @Resource
    private ZhongHangProperties zhongHang;

    @Resource
    private SupplierInfoHandler supplierInfoHandler;

    /**
     * 导入资产包
     *
     * @param model    导入资产包类模型
     * @param request  http请求
     * @param response http响应
     * @return 返回变更的结果信息，包括成功和其他各种失败情况
     */
    @ApiOperation(value = "导入资产包", httpMethod = "POST", notes = "导入资产包", response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @PostMapping(value = URL_IMPORT_ASSET_PACKAGE)
    @ResponseBody
    public BaseResponse importAssetPackage(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @ApiParam(value = "导入资产包", required = true)
                                           @Validated
                                           @ModelAttribute ImportAssetPackage model,
                                           BindingResult bindingResult) throws Exception {
        log.info(GloableLogSpec.AuditLogHeaderSpec());
        log.info("MORGANSTANLEY:IMPORT_ASSET_PACKAGE#");
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

        // 校验生效日期,生成返回信息
        List<String> responseMessage = importAssetPackageHandler.checkContractEffectDate(model, notOfflineRemittance, notNullRemittance);

        long start = System.currentTimeMillis();
        ImportAssetPackageResponseModel responseModel = importAssetPackageHandler.importAssetPackage(model, notOfflineRemittance);
        String loanContractNo = model.getRequestContentObject().getContractDetails().get(0).getLoanContractNo();
        log.info("loanContractNo:"+loanContractNo+" importAssetPackage used :"+ (System.currentTimeMillis()-start));
        responseModel.setResponseMessage(responseMessage);
        //如果放款对象是供应商的话 把供应商的信息保存到supplier中
        supplierInfoHandler.saveSupplierInfo(model);

        pushJobToEarthForSignUp(request, model.getRequestContentObject().getContractDetails(), fc, zhongHang.getSignUpGroupName(), zhongHang
                .getSignTransType(), zhongHang
                .getNotifyUrlToSignUp());

        log.info(GloableLogSpec.AuditLogHeaderSpec());
        log.info("MORGANSTANLEY:IMPORT_ASSET_PACKAGE#");
        log.info("[remoteKeyWord={} ip={}]", model.getRequestNo(), IpUtil.getIpAddress(request));
        log.info("[SUCCESS:import assetPackage success]");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("batchResponse", responseModel);

        return wrapHttpServletResponse(response, ApiMessage.SUCCESS, resultMap);
    }

    private void pushJobToEarthForSignUp(HttpServletRequest request, List<ContractDetail> contractDetails, FinancialContract
            financialContract, String groupName, String signTransType, String notifyUrlToSignUp) {
        if (!signUpHandler.judgeZhongHangByfinancialContractCode(financialContract.getFinancialContractUuid())) {
            return;
        }

        List<NotifyApplication> jobList = new ArrayList<>();
        String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
        String secret = request.getHeader(ApiConstant.PARAMS_SECRET);

        Map<String, String> paramForSignUp = new HashMap<>();
        for (ContractDetail contractDetail : contractDetails) {
            paramForSignUp.put("accName", contractDetail.getLoanCustomerName());
            paramForSignUp.put("accNo", contractDetail.getRepaymentAccountNo());
            paramForSignUp.put("certId", contractDetail.getIDCardNo());
            paramForSignUp.put("bankAliasName", contractDetail.getBankCode());
            paramForSignUp.put("bankFullName", StringUtils.EMPTY);
            paramForSignUp.put("phoneNo", contractDetail.getMobile() == null ? StringUtils.EMPTY : contractDetail.getMobile());

            List<NotifyApplication> jobs = signUpHandler.pushJobFromImportAssetAndContract(paramForSignUp, financialContract.getFinancialContractUuid(),
                    financialContract.getContractNo(), notifyUrlToSignUp, merId, secret, signTransType, groupName, "1");

            jobList.addAll(jobs);
        }

        morganStanleyNotifyServer.pushJobs(jobList);
    }

}